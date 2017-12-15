package com.hrmp.util;

import android.content.Context;
import android.text.TextUtils;
import android.util.Log;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

/**
 * Created by Ly on 2017/4/30.
 */

public class FileTools {
    private static String TAG = "FileTools";

    /**
     * 第一次使用时安装数据库
     *
     * @param context
     * @return
     */
    public static int copyDB(Context context) {
        try {
            String DB_NAME = "telocation.db";
            String srcDB = "telocation.txt";
            String packageName = context.getPackageName();
            String destDBPath = "/data/data/" + packageName + "/databases/";
            String destDB = destDBPath + DB_NAME;
            boolean isExist = checkDataBase(destDB);
            if (isExist) {
                Log.i(TAG, "exist telocation.db not copy");
                return 2;
            }
            try {
                File dir = new File(destDBPath);
                if (!dir.exists()) {
                    dir.mkdirs();
                }
                File dbf = new File(destDB);
                if (dbf.exists()) {
                    dbf.delete();
                }
                copyDataBase(context, srcDB, destDB);

                // 删除assert中的db包
                try {
                    File src_db = new File(srcDB);
                    src_db.delete();
                } catch (Exception ex) {
                    Log.e(TAG, "del srcDB error ", ex);
                }
                return 1;
            } catch (Exception ex) {
                Log.e(TAG, "createDB error ", ex);
                return 4;
            }
        } catch (Exception ex) {
            Log.e(TAG, " copyDB error ", ex);
            return 3;
        }
    }

    /**
     * db文件是否已存在
     *
     * @param dbPath
     * @return
     */
    public static boolean checkDataBase(String dbPath) {
        return ((new File(dbPath)).exists());
    }

    /**
     * 考贝资源下的db文件到应用目录下
     *
     * @param context
     * @param srcDB
     * @param destDB
     * @throws IOException
     */

    private static void copyDataBase(Context context, String srcDB,
                                     String destDB) throws IOException {
        InputStream myInput = context.getAssets().open(srcDB);
        OutputStream myOutput = new FileOutputStream(destDB);
        byte[] buffer = new byte[1024];
        int length;
        while ((length = myInput.read(buffer)) > 0) {
            myOutput.write(buffer, 0, length);
        }
        myOutput.flush();
        myOutput.close();
        myInput.close();
    }

    /**
     * 是否已存在对应的帐号数据库(类似)13240761234efetion.db
     *
     * @param account
     *            用户登陆帐号
     * @return true:存在,不存在:false
     */
    public static boolean isExistAccountDBFile(Context context, String account,
                                               String DBName) {
        String packageName = context.getPackageName();
        String destDBPath = "/data/data/" + packageName + "/databases/";
        String destDB = destDBPath + account + DBName;
        if (FileTools.checkDataBase(destDB)) {
            return true;
        } else {
            return false;
        }
    }

    /**
     * copy 文件
     *
     * @param context
     * @param srcfile
     * @param destFile
     * @throws IOException
     */
    public static void copyFile(Context context, String srcfile, String destFile)
            throws IOException {
        // UITools.LogCatDebug(TAG, "srcfile="+srcfile);
        // UITools.LogCatDebug(TAG, "destFile="+destFile);
        InputStream myInput = null;
        try {
            myInput = new FileInputStream(srcfile);
        } catch (Exception ex) {
            ex.printStackTrace();
        }

        File file = new File(destFile);

        // OutputStream myOutput = new FileOutputStream (file,
        // Context.MODE_WORLD_READABLE + Context.MODE_WORLD_WRITEABLE);;
        OutputStream myOutput = new FileOutputStream(file);
        ;
        byte[] buffer = new byte[1024];
        int length;
        while ((length = myInput.read(buffer)) > 0) {
            myOutput.write(buffer, 0, length);
        }
        myOutput.flush();
        myOutput.close();
        myInput.close();
    }

    /**
     * 分解路径，获取文件名
     *
     * @param path
     * @return
     */
    public static String getFileName(String path) {
        String[] filePathSP = path.split("/");
        int len = filePathSP.length;
        return filePathSP[len - 1];
    }

    /**
     * 创建文件夹路径
     *
     * @param path
     *            路径
     */
    public static boolean dir_exist(String path) {
        File out = new File(path);
        if (!out.exists()) {
            return out.mkdirs();
        }
        return true;
    }

    /**
     * 判断给定的文件路径是否存在
     *
     * @param path：文件路径
     * @return
     */
    public static boolean isFileExist(String path) {
        boolean isExist = false;
        if(TextUtils.isEmpty(path))
            return isExist;
        File file = new File(path);
        isExist = file.exists();
        file = null;
        return isExist;
    }

    public static String appDir="/sdcard/hrmp";
    public static String LogDir="/sdcard/hrmp/log";
    public static String errorLogDir="/sdcard/hrmp/log/UploadAppError/";
    public static String errorLogDirName="UploadAppError";
    public static void  createLogDir()
    {
        dir_exist(appDir);
        dir_exist(LogDir);
        dir_exist(errorLogDir);
    }
}