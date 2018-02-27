package com.aiwue.utils;

/**
 * Created by liaixiong on 16-8-25.
 */
public class ProcessCheck {
    public static String AIWUE_SERVICE_PROCESSNAME = ":service";

    private static Thread  s_mainThread			    = null;
    private static boolean s_bIsSerivceProcess      = false;
    private static boolean s_bIsUiProcess		    = false;

    public static void Init(String procName){
        s_mainThread = Thread.currentThread();
        if ( procName.contains(AIWUE_SERVICE_PROCESSNAME) ){
            s_bIsSerivceProcess = true;
        } else {
            s_bIsUiProcess = true;
        }
    }

    public static void SetServiceProcess(){
        s_bIsUiProcess = false;
        s_bIsSerivceProcess = true;
    }

    public static void CheckUiProcess(){
        if (!s_bIsUiProcess){
            throw new RuntimeException("Must run in UI Process");
        }
    }

    public static void CheckServiceProcess(){
        if (!s_bIsSerivceProcess){
            throw new RuntimeException("Must run in Service Process");
        }
    }

    public static void CheckMainUIThread(){
        if ( Thread.currentThread() != s_mainThread ){
            throw new RuntimeException("Must run in UI Thread");
        }
    }

    public static void CheckNoUIThread(){
        if ( Thread.currentThread() == s_mainThread ){
            throw new RuntimeException("Must not run in UI Thread");
        }
    }


    public static boolean IsUIProcess(){
        return s_bIsUiProcess;
    }

    public static boolean IsServiceProcess(){
        return s_bIsSerivceProcess;
    }

}
