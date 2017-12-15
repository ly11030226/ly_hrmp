package com.hrmp.util;

/**
 * Created by Ly on 2017/4/30.
 */

import android.text.TextUtils;
import android.util.Log;

/**
 * LogUtils工具说明:
 * 1 只输出等级大于等于LEVEL的日志
 *   所以在开发和产品发布后通过修改LEVEL来选择性输出日志.
 *   当LEVEL=NOTHING则屏蔽了所有的日志.
 * 2 v,d,i,w,e均对应两个方法.
 *   若不设置TAG或者TAG为空则为设置默认TAG
 * 3 输出的日志除了本想输出的信息外还包含了该日志输出时所属
 *   的线程,类,方法名,已经在该方法中的行数等实用信息
 * 4 该类中主要用到了StackTrace和StackTraceElement，可以查看相应资料去掌握
 *
 */
public class LogUtils {
    public static final int VERBOSE = 1;
    public static final int DEBUG = 2;
    public static final int INFO = 3;
    public static final int WARN = 4;
    public static final int ERROR = 5;
    public static final int NOTHING = 6;
    public static final int LEVEL = VERBOSE;
    public static final String SEPARATOR = ",";
    public static final String DEFAULT_TAG_NAME = "hrmp";

    public static void v(String message) {
        if (LEVEL <= VERBOSE) {
            StackTraceElement stackTraceElement = Thread.currentThread().getStackTrace()[3];
            String tag = getDefaultTag(stackTraceElement);
            Log.v(tag, getLogInfo(stackTraceElement) + message);
        }
    }

    public static void v(String tag, String message) {
        if (LEVEL <= VERBOSE) {
            StackTraceElement stackTraceElement = Thread.currentThread().getStackTrace()[3];
            if (TextUtils.isEmpty(tag)) {
                tag = getDefaultTag(stackTraceElement);
            }
            Log.v(tag, getLogInfo(stackTraceElement) + message);
        }
    }

    public static void d(String message) {
        if (LEVEL <= DEBUG) {
            StackTraceElement stackTraceElement = Thread.currentThread().getStackTrace()[3];
            String tag = getDefaultTag(stackTraceElement);
            Log.d(tag, getLogInfo(stackTraceElement) + message);
        }
    }

    public static void d(String tag, String message) {
        if (LEVEL <= DEBUG) {
            StackTraceElement stackTraceElement = Thread.currentThread().getStackTrace()[3];
            if (TextUtils.isEmpty(tag)) {
                tag = getDefaultTag(stackTraceElement);
            }
            Log.d(tag, getLogInfo(stackTraceElement) + message);
        }
    }

    public static void i(String message) {
        if (LEVEL <= INFO) {
            StackTraceElement stackTraceElement = Thread.currentThread().getStackTrace()[3];
            String tag = getDefaultTag(stackTraceElement);
            Log.i(tag, getLogInfo(stackTraceElement) + message);
        }
    }

    public static void i(String tag, String message) {
        if (LEVEL <= INFO) {
            StackTraceElement stackTraceElement = Thread.currentThread().getStackTrace()[3];
            if (TextUtils.isEmpty(tag)) {
                tag = getDefaultTag(stackTraceElement);
            }
            Log.i(tag, getLogInfo(stackTraceElement) + message);
        }
    }

    public static void w(String message) {
        if (LEVEL <= WARN) {
            StackTraceElement stackTraceElement = Thread.currentThread().getStackTrace()[3];
            String tag = getDefaultTag(stackTraceElement);
            Log.w(tag, getLogInfo(stackTraceElement) + message);
        }
    }

    public static void w(String tag, String message) {
        if (LEVEL <= WARN) {
            StackTraceElement stackTraceElement = Thread.currentThread().getStackTrace()[3];
            if (TextUtils.isEmpty(tag)) {
                tag = getDefaultTag(stackTraceElement);
            }
            Log.w(tag, getLogInfo(stackTraceElement) + message);
        }
    }

    public static void e(String tag, String message) {
        if (LEVEL <= ERROR) {
            StackTraceElement stackTraceElement = Thread.currentThread().getStackTrace()[3];
            if (TextUtils.isEmpty(tag)) {
                tag = getDefaultTag(stackTraceElement);
            }
            Log.e(tag, getLogInfo(stackTraceElement) + message);
        }
    }
    public static void e(String tag,String message,Throwable th){
        if (LEVEL <= ERROR) {
            StackTraceElement stackTraceElement = Thread.currentThread().getStackTrace()[3];
            if (TextUtils.isEmpty(tag)) {
                tag = getDefaultTag(stackTraceElement);
            }
            Log.e(tag, getLogInfo(stackTraceElement) + message,th);
        }
    }

    public static void e(String message,Throwable th){
        if (LEVEL <= ERROR) {
            StackTraceElement stackTraceElement = Thread.currentThread().getStackTrace()[3];
            Log.e(getDefaultTag(stackTraceElement), getLogInfo(stackTraceElement) + message,th);
        }
    }
    /**
     * 获取默认的TAG名称.
     * 比如在MainActivity.java中调用了日志输出.
     * 则TAG为MainActivity
     * 这里暂时已自定义的hrmp命名
     */
    public static String getDefaultTag(StackTraceElement stackTraceElement) {
        String fileName = stackTraceElement.getFileName();
        String stringArray[] = fileName.split("\\.");
//        String tag = stringArray[0];
        String tag = DEFAULT_TAG_NAME;
        return tag;
    }

    /**
     * 输出日志所包含的信息
     */
    public static String getLogInfo(StackTraceElement stackTraceElement) {
        StringBuilder logInfoStringBuilder = new StringBuilder();
        // 获取线程名
        String threadName = Thread.currentThread().getName();
        // 获取线程ID
        long threadID = Thread.currentThread().getId();
        // 获取文件名.即xxx.java
        String fileName = stackTraceElement.getFileName();
        // 获取类名.即包名+类名
        String className = stackTraceElement.getClassName();
        // 获取方法名称
        String methodName = stackTraceElement.getMethodName();
        // 获取生日输出行数
        int lineNumber = stackTraceElement.getLineNumber();

        logInfoStringBuilder.append("[ ");
        logInfoStringBuilder.append("threadID=" + threadID).append(SEPARATOR);
        logInfoStringBuilder.append("threadName=" + threadName).append(SEPARATOR);
        logInfoStringBuilder.append("fileName=" + fileName).append(SEPARATOR);
        logInfoStringBuilder.append("className=" + className).append(SEPARATOR);
        logInfoStringBuilder.append("methodName=" + methodName).append(SEPARATOR);
        logInfoStringBuilder.append("lineNumber=" + lineNumber);
        logInfoStringBuilder.append(" ] ");
        return logInfoStringBuilder.toString();
    }

}
