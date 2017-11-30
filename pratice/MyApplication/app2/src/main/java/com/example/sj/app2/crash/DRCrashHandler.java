package com.example.sj.app2.crash;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Environment;
import android.os.Looper;
import android.widget.Toast;

import com.example.sj.app2.LogUtil;
import com.example.sj.app2.MyApp;

import java.io.File;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.io.Writer;
import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

/**
 * Created by sunjie on 2017/11/29.
 */

public  class DRCrashHandler implements Thread.UncaughtExceptionHandler {

    /**
     * 系统默认的UncaughtException处理类
     **/
    private Thread.UncaughtExceptionHandler mDefaultHandler;
    /**
     * 程序context
     **/
    private MyApp mContext;
    /**
     * 存储设备信息和异常信息
     **/
    private Map<String, String> mInfos = new HashMap<String, String>();
    /**
     * 程序出错提示信息
     **/
    private String mDRTipMsg = "抱歉，程序异常，3s后退出！";
    /**
     * 设置crash文件位置
     **/
    private String mDRCrashFilePath = Environment.getExternalStorageDirectory().getAbsolutePath();
    /**
     * 生成的log文件
     **/
    private File logFile;
    /**
     * 生成的crash文件
     **/
    private File crashFile;

    /**
     * 初始化
     *
     * @param context
     */
    public void init(MyApp context) {
        LogUtil.e("DRCrashHandler", "64-----init--->" + "DRCrashHandler is Ready For Application! ");

        // 1、上下文
        mContext = context;
        // 2、获取系统默认的UncaughtException处理器
        mDefaultHandler = Thread.getDefaultUncaughtExceptionHandler();
        // 3、初始化参数
//        initParams();
        // 4、设置当前CrashHandler为默认处理异常类
        Thread.setDefaultUncaughtExceptionHandler(this);
    }

    /**
     * 3.1 初始化参数 <br/>
     * <br/>
     * <p>
     * setCrashFilePath(Constants.CRASH_FILE_PATH); <br/>
     * <br/>
     * <p>
     * 如果想使用自己的CrashHandler，则复写initParams()方，然后设置参数<br/>
     * <p>
     * <code>
     * public class MyCrashHandler extends DRCrashHandler {<br/>
     * private static final Logger log = Logger.getLogger(MyCrashHandler.class);<br/>
     * <p>
     * &#64;Override<br/>
     * public void initParams() {<br/>
     * log.trace("MyCrashHandler: initParams()");<br/>
     * <p>
     * setDRTipMsg("MyCrashHandler tip msg!!!");<br/>
     * setDRCrashFilePath(Constants.CRASH_FILE_PATH);<br/>
     * }<br/>
     * }<br/>
     * </code>
     */
//    public abstract void initParams();

    @Override
    public void uncaughtException(Thread thread, Throwable ex) {
        LogUtil.e("DRCrashHandler", "103-----uncaughtException--->" + "DRCrashHandler dispatcher uncaughtException! ");

        if (mDefaultHandler != null && !handlerException(ex)) {
            mDefaultHandler.uncaughtException(thread, ex);
        } else {
            // 程序休眠3s后退出
            try {
                Thread.sleep(3000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
//            (mContext.getApplicationContext()).appExit();
        }
    }

    /**
     * 5、处理异常<br>
     * <br>
     * <p>
     * 5.1 收集设备参数信息<br>
     * 5.2 弹出窗口提示信息<br>
     * 5.3 保存log和crash到文件<br>
     * 5.4 发送log和crash到服务器<br>
     *
     * @param ex
     * @return 是否处理了异常
     */
    protected boolean handlerException(Throwable ex) {
        LogUtil.e("DRCrashHandler", "130-----handlerException--->" + "DRCrashHandler is handling Exception! ");
        if (ex == null) {
            return false;
        } else {
            // 5.1 收集设备参数信息
            collectDeviceInfo(mContext);
            // 5.2 弹出窗口提示信息
            new Thread(new Runnable() {
                @Override
                public void run() {
                    LogUtil.e("DRCrashHandler", "140-----run--->" + "DRCrashHandler is ready send crash-info to device!");
                    Looper.prepare();
                    Toast.makeText(mContext, getDRTipMsg(), Toast.LENGTH_SHORT).show();
                    Looper.loop();
                }
            }).start();
            // 5.3 保存log和crash到文件
            saveLogAndCrash(ex);
            // 5.4 发送log和crash到服务器
            sendLogAndCrash();
            return true;
        }
    }

    /**
     * 5.1 收集设备信息
     *
     * @param ctx
     */
    protected void collectDeviceInfo(Context ctx) {
       LogUtil.e("crash","DRCrashHandler is collecting DeviceInfo! ");
        
        try {
            PackageManager pm = ctx.getPackageManager();
            PackageInfo pi = pm.getPackageInfo(ctx.getPackageName(), PackageManager.GET_ACTIVITIES);
            if (pi != null) {
                String versionName = pi.versionName == null ? "null" : pi.versionName;
                String versionCode = pi.versionCode + "";
                mInfos.put("versionName", versionName);
                mInfos.put("versionCode", versionCode);
            }
        } catch (PackageManager.NameNotFoundException e) {
           LogUtil.e("crash","An error occured when collect package info, Error: " + e);
        }
        Field[] fields = Build.class.getDeclaredFields();
        for (Field field : fields) {
            try {
                field.setAccessible(true);
                mInfos.put(field.getName(), field.get(null).toString());
            } catch (Exception e) {
               LogUtil.e("crash","An error occured when collect crash info, Error: " + e);
            }
        }
    }

    /**
     * 5.3 保存log和crash到文件
     *
     * @param ex
     */
    protected void saveLogAndCrash(Throwable ex) {
       LogUtil.e("crash","DRCrashHandler is saving Log! ");
        StringBuffer sb = new StringBuffer();
        sb.append("[DeviceInfo: ]\n");
        // 遍历infos
        for (Map.Entry<String, String> entry : mInfos.entrySet()) {
            String key = entry.getKey().toLowerCase(Locale.getDefault());
            String value = entry.getValue();
            sb.append("  " + key + ": " + value + "\n");
        }
        // 将错误手机到writer中
        Writer writer = new StringWriter();
        PrintWriter pw = new PrintWriter(writer);
        ex.printStackTrace(pw);
        Throwable cause = ex.getCause();
        while (cause != null) {
            cause.printStackTrace(pw);
            cause = cause.getCause();
        }
        pw.close();
        String result = writer.toString();
        sb.append("[Excetpion: ]\n");
        sb.append(result);
        // 将异常写入日志文件
       LogUtil.e("crash",result);
        // 5.3.1 记录异常到特定文件中
        saveToCrashFile(sb.toString());
    }

    /**
     * 5.3.1写入文本
     *
     * @param crashText
     */
    protected void saveToCrashFile(String crashText) {
       LogUtil.e("crash","DRCrashHandler is writing crash-info to CrashFile(" + this.mDRCrashFilePath + ")! ");
        crashFile = new File(mDRCrashFilePath);
        // 创建文件
//        FileUtil.createFileAndFolder(crashFile);
        // 追加文本
//        FileUtil.appendToFile(crashFile, crashText);
    }

    /**
     * 5.4 发送log和crash到服务器
     */
    protected void sendLogAndCrash() {
//        logFile = new File(mContext.getDrLogHelper().getLog4jFilePath());
        crashFile = new File(getDRCrashFilePath());
        // 5.4.1
//        sendToServer(logFile, crashFile);
    }

    /**
     * 5.4.1 将错误报告发送到服务器

     */
//    protected abstract void sendToServer(File logFile, File crashFile);

    public String getDRTipMsg() {
        return mDRTipMsg;
    }

    /**
     * 设置程序崩溃提示信息
     *
     * @param mDRTipMsg
     */
    public void setDRTipMsg(String mDRTipMsg) {
        this.mDRTipMsg = mDRTipMsg;
    }

    public String getDRCrashFilePath() {
        return mDRCrashFilePath;
    }

    /**
     * 设置记录崩溃信息的文件位置
     *
     * @param mDRCrashFilePath
     */
    public void setDRCrashFilePath(String mDRCrashFilePath) {
        this.mDRCrashFilePath = mDRCrashFilePath;
    }

}
