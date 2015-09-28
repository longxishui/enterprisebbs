package com.aspirecn.corpsocial.common.util;

import android.util.Log;

import com.aspirecn.corpsocial.common.config.Constant;

import java.io.File;
import java.lang.reflect.Field;
import java.util.Calendar;
import java.util.List;

public class LogUtil {
    private static final String TAG = "CorpSocialLog";

	/*
     * 1.获取当前运行代码的类名，方法名，行号，主要是通过java.lang.StackTraceElement类 2. [1]获得调用者的方法名,
	 * 同new Throwable String _methodName = new
	 * Exception().getStackTrace()[1].getMethodName(); [0]获得当前的方法名, 同new
	 * Throwable String _thisMethodName = new
	 * Exception().getStackTrace()[0].getMethodName();
	 */
    /*
     * [当前记录log的方法名]msg
	 */

    /**
     * Send a INFO log message
     *
     * @param msg
     */
    public static void i(String msg) {
        try {
            Log.i(TAG,
                    "["
                            + Thread.currentThread().getStackTrace()[3]
                            .getClassName()
                            + "."
                            + Thread.currentThread().getStackTrace()[3]
                            .getMethodName() + "] - " + msg);
        } catch (Exception e) {
            Log.e("", "error in frameworklog.", e);
        }
    }

    /**
     * Send a INFO log message and log the exception.
     *
     * @param msg
     * @param tr
     */
    public static void i(String msg, Throwable tr) {
        try {
            Log.i(TAG,
                    "["
                            + Thread.currentThread().getStackTrace()[3]
                            .getClassName()
                            + "."
                            + Thread.currentThread().getStackTrace()[3]
                            .getMethodName() + "] - " + msg, tr);
        } catch (Exception e) {
            Log.e("", "error in frameworklog.", e);
        }
    }

    /**
     * Send a DEBUG log message
     *
     * @param msg
     */
    public static void d(String msg) {
        try {

            Log.d(TAG,
                    "["
                            + Thread.currentThread().getStackTrace()[3]
                            .getClassName()
                            + "."
                            + Thread.currentThread().getStackTrace()[3]
                            .getMethodName() + "] - " + msg);
        } catch (Exception e) {
            Log.e("", "error in frameworklog.", e);
        }
    }

    /**
     * Send a DEBUG log message and log the exception.
     *
     * @param msg
     * @param tr
     */
    public static void d(String msg, Throwable tr) {
        try {

            Log.d(TAG,
                    "["
                            + Thread.currentThread().getStackTrace()[3]
                            .getClassName()
                            + "."
                            + Thread.currentThread().getStackTrace()[3]
                            .getMethodName() + "] - " + msg, tr);
        } catch (Exception e) {
            Log.e("", "error in frameworklog.", e);
        }
    }

    /**
     * Send a ERROR log message
     *
     * @param msg
     */
    public static void e(String msg) {
        try {
            Log.e(TAG,
                    "["
                            + Thread.currentThread().getStackTrace()[3]
                            .getClassName()
                            + "."
                            + Thread.currentThread().getStackTrace()[3]
                            .getMethodName() + "] - " + msg);
        } catch (Exception e) {
            Log.e("", "error in frameworklog.", e);
        }
    }

    public static void e(String msg, Exception exception) {
        try {
            Log.e(TAG, msg + "\n" + getStackMsg(exception));
        } catch (Exception e) {
            Log.e("", "error in frameworklog.", e);
        }
    }

    /**
     * Send a ERROR log message and log the exception.
     *
     * @param msg
     * @param tr
     */
    public static void e(String msg, Throwable tr) {
        try {
            Log.e(TAG,
                    "["
                            + Thread.currentThread().getStackTrace()[3]
                            .getClassName()
                            + "."
                            + Thread.currentThread().getStackTrace()[3]
                            .getMethodName() + "] - " + msg, tr);
        } catch (Exception e) {
            Log.e("", "error in frameworklog.", e);
        }
    }

    public static void clearLogFile() {
        File mFile = new File(Constant.LOG_PATH);
        String files[] = mFile.list();
        Calendar c = Calendar.getInstance();
        c.add(Calendar.MONTH, -1);
        long t = c.getTimeInMillis();
        for (String item : files) {
            File f = new File(Constant.LOG_PATH + item);
            if (f.exists()) {
                long d = f.lastModified();
                if (d < t) {
                    f.delete();
                }
            }
        }
    }

    public static String getStackMsg(Exception e) {

        StringBuffer sb = new StringBuffer();
        StackTraceElement[] stackArray = e.getStackTrace();
        sb.append(e.getLocalizedMessage() + "\n");
        for (int i = 0; i < stackArray.length; i++) {
            StackTraceElement element = stackArray[i];
            sb.append(element.toString() + "\n");
        }
        return sb.toString();
    }

    public static void printObject(Object obj){
        i("aspire------printobject");
        if(obj instanceof List){
            i("aspire------list");
            List list=(List)obj;
            for(Object object:list){
                printObject(object);
            }
        }else{
            i("aspire------object");
            Field[] fields=obj.getClass().getDeclaredFields();
            StringBuilder builder=new StringBuilder();
            builder.append("[");
            for(Field field:fields){
                try {
                    field.setAccessible(true);

                    builder.append(field.getName() + ":" + field.get(obj) + ",");

                }catch(Exception e){e.printStackTrace();}
            }
            builder.append("]");
            Log.i(TAG, builder.toString() + "\n");
        }
    }

}
