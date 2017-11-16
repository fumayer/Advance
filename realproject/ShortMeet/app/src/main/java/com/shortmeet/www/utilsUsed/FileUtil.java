package com.shortmeet.www.utilsUsed;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.os.Environment;

import java.io.BufferedOutputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.UUID;

/**
 * File工具类
 * Created by SHM on 2017/03/31.
 */
public class FileUtil {

    public static final String APP_NAME = "shortmeet";
    public static final String PATH = Environment.getExternalStorageDirectory() + "/" + APP_NAME + "/";

    /**
     * 将Bitmap 图片保存到本地路径，并返回路径
     *
     * @param c
     * @param fileName 文件名称
     * @param bitmap   图片
     * @return
     */
    public static String saveFile(Context c, String fileName, Bitmap bitmap) {
        return saveFile(c, "", fileName, bitmap);
    }

    public static String saveFile(Context c, String filePath, String fileName, Bitmap bitmap) {
        byte[] bytes = bitmapToBytes(bitmap);
        if (bytes == null) {
            return "";
        }
        return saveFile(c, filePath, fileName, bytes);
    }

    public static byte[] bitmapToBytes(Bitmap bm) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bm.compress(CompressFormat.JPEG, 100, baos);
        try {
            return baos.toByteArray();
        } catch (Exception e) {
            return null;
        }

    }

    public static String saveFile(Context c, String filePath, String fileName, byte[] bytes) {
        String fileFullName = "";
        BufferedOutputStream fos = null;
        String dateFolder = new SimpleDateFormat("yyyyMMdd", Locale.CHINA)
                .format(new Date());
        try {
            String suffix = "";
            if (filePath == null || filePath.trim().length() == 0) {
                filePath = Environment.getExternalStorageDirectory() + "/JiaXT/" + dateFolder + "/";
            }
            File file = new File(filePath);
            if (!file.exists()) {
                file.mkdirs();
            }
            File fullFile = new File(filePath, fileName + suffix);
            fileFullName = fullFile.getPath();
            fos = new BufferedOutputStream(new FileOutputStream(new File(filePath, fileName + suffix)));
            fos.write(bytes);
        } catch (Exception e) {
            fileFullName = "";
        } finally {
            if (fos != null) {
                try {
                    fos.close();
                } catch (IOException e) {
                    fileFullName = "";
                }
            }
        }
        return fileFullName;
    }

    /**
     * 通过路径生产对应的文件
     *
     * @param realPathFromURI
     */
    public static File makeFileFromPath(String realPathFromURI) {
        File file1 = new File(realPathFromURI);
        File fileParent1 = file1.getParentFile();
        if (!fileParent1.exists()) {
            fileParent1.mkdirs();
        }
        return file1;
    }

    public static File makeFile(Context context, String fileName) {
        String sdStatus = Environment.getExternalStorageState();
        if (sdStatus.equals(Environment.MEDIA_MOUNTED)) { // 检测sd是否可用
            File file = new File(PATH, fileName);
            File fileParent = file.getParentFile();
            if (!fileParent.exists()) {
                fileParent.mkdirs();
            }

            if (file.exists()) {
                file.delete();
            }

            return file;
        } else {
            File file = context.getCacheDir();
            // 封装缓存文件对象
            File cacheFile = new File(file, fileName);
            if (!cacheFile.exists()) {
                cacheFile.mkdirs();
            }
            if (file.exists()) {
                file.delete();
            }
            context = null;
            return cacheFile;
        }

    }

    //创建相机返回图片的文件路径
    public static final String POSTFIX = ".JPEG";
    public static final String CAMERA_PATH = "/" + APP_NAME + "/CameraImage/";
    public static final String CROP = "/" + APP_NAME + "/Crop/";

    public static File createCameraFile(Context context) {
        return createMediaFile(context, CAMERA_PATH);
    }

    public static File createCropFile(Context context) {
        return createMediaFile(context, CROP);
    }

    private static File createMediaFile(Context context, String parentPath) {
        String state = Environment.getExternalStorageState();
        File rootDir = state.equals(Environment.MEDIA_MOUNTED) ? Environment.getExternalStorageDirectory() : context.getCacheDir();

        File folderDir = new File(rootDir.getAbsolutePath() + parentPath);
        if (!folderDir.exists() && folderDir.mkdirs()) {
//            ToastUtil.show(context, "路径" + parentPath + "不存在");
        }

        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss", Locale.CHINA).format(new Date());
        String fileName = APP_NAME + "_" + timeStamp + "";
        File tmpFile = new File(folderDir, fileName + POSTFIX);
        return tmpFile;
    }

    public static String getUUID() {
        return UUID.randomUUID().toString().trim().replaceAll("-", "").toUpperCase();
    }

    /**
     * 创建文件夹
     * @param path
     */
    public static void createFileDir(String path){
        if (path == null || path.trim().length() == 0) {
            path = Environment.getExternalStorageDirectory()+ File.separator + "shortmeet/";
        }
        File file = new File(path);
        if (!file.exists()) {
            file.mkdirs();
        }
    }



    /** 保存图片 */
    public static  void saveBitmap(Bitmap bitmap,String filename) {
        File f = new File(filename);
        try {
            f.createNewFile();
        } catch (IOException e) {
            e.printStackTrace();
        }
        try {
            FileOutputStream out = new FileOutputStream(f);
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, out);
            out.flush();
            out.close();
            LogUtils.e("saveBitmap","图片存储成功");
        } catch (FileNotFoundException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

    }

    /**
     * 移动文件到另一个目录
     */
    public static  boolean  moveTotherFolders(String startPath,String endPath){
        try {
            File startFile = new File(startPath);
            File tmpFile = new File(endPath);//获取文件夹路径
            if(!tmpFile.exists()){//判断文件夹是否创建，没有创建则创建新文件夹
                tmpFile.mkdirs();
            }
            System.out.println(endPath + startFile.getName());
            if (startFile.renameTo(new File(endPath + startFile.getName()))) {
                LogUtils.e("文件移动成功！文件名：《{}》 目标路径：{}",startPath+"----->"+endPath);
                return true;
            } else {
                System.out.println("File is failed to move!");
                LogUtils.e("文件移动失败！文件名：《{}》 起始路径：{}",startPath);
                return false;
            }
        } catch (Exception e) {
            return false;
        }
    }
}
