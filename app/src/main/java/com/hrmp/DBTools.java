package com.hrmp;

/**
 * Created by Ly on 2017/6/5.
 */

import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.text.TextUtils;
import android.util.Log;

import com.hrmp.util.LogUtils;
import com.hrmp.view.MyDatabaseOpenHelper;

import java.io.File;
import java.util.ArrayList;


public class DBTools {
    private static String TAG = "DBTools";
    private static DBTools g_db = null;
    private static SQLiteOpenHelper sqLiteOpenHelper;
    private static String dbName = "";
    private Context m_owner;
    private static String versionStr = "1.0.0.0";
    /** 数据库版本号 */
    private static int dbVersion = 1;

    private static SQLiteDatabase mDataBase;

    /**
     * 初始化数据库
     *
     * @param app
     * @return
     */
    public boolean init(Context app) {
        if (g_db == null) {
            g_db = new DBTools();
        }
        g_db.init_db(app);
        return true;
    }

    /**
     * 关闭数据库
     */
    public static void close() {
        g_db = null;
    }

    /**
     * 返回数据库
     */
    public static DBTools get_inst() {
        if (g_db == null) {
            g_db = new DBTools();
        }
        return g_db;
    }

    /**
     * 执行SQL语句
     *
     * @param sql 命令
     * @return
     */
    public synchronized boolean ExecuteSQL(String sql) {
        try {
            SQLiteDatabase db = getCurDB();
            boolean ret = SafeDBExecute(db, sql);
            return ret;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * 查询
     *
     * @param sql 语句
     * @return 查询 结果
     */
    public synchronized ArrayList<String[]> QuerySQL(String sql) {
        ArrayList<String[]> list = new ArrayList<String[]>();
        try {
            SQLiteDatabase db = getCurDB();
            Cursor cursor = SafeQuery(db, sql);
//            long start = System.currentTimeMillis();
            while (cursor.moveToNext()) {
//                long start1 = System.currentTimeMillis();
                int col = cursor.getColumnCount();
                String[] row = new String[col];
                for (int i = 0; i < col; i++) {
                    row[i] = cursor.getString(i);
                }
                list.add(row);
//                long end1 = System.currentTimeMillis();
//                LogUtils.e("DB_SQLCipher", "Move---"+(end1-start1)+"..." + (start1-start));
            }
            long end = System.currentTimeMillis();
//            LogUtils.e("DB_SQLCipher", "Query---"+(end-start)+"");
            cursor.close();
//            Log.i("DBTools", "sql :" + sql + "...size : " + list.size());
            return list;
        } catch (Exception e) {

        }
        return list;
    }

    /**
     * 初始化数据库
     */
    protected void init_db(Context app) {
        m_owner = app;
        dbName = this.getDBName();
        LogUtils.d(TAG, "init_db()-------begin");
        if (TextUtils.isEmpty(dbName))
            return;
        SQLiteDatabase db = null;
        try {

            if(sqLiteOpenHelper==null){
                synchronized (DBTools.class) {
                    if(sqLiteOpenHelper==null){
                        sqLiteOpenHelper = new MyDatabaseOpenHelper(HRMPApplication.getInstance(), dbName, null, dbVersion);
                    }
                }
            }
            db = getCurDB();
        } catch (Exception e) {
            e.printStackTrace();
            LogUtils.i(TAG, e.getMessage());
        }
        if (db == null) {
            return;
        }

        initVersionTable();
        updateDB(app, db);
    }

    public void initVersionTable() {
        SQLiteDatabase db = getCurDB();
        String sql = "";

        // 版本表不存在
        if (isExistsTable(db, "db_version") == false) {
            sql = "CREATE TABLE IF NOT EXISTS db_version (table_name VARCHAR (64),version VARCHAR (64))";
            SafeDBExecute(db, sql);

            sql = String.format("insert into db_version (table_name,version) values ('WORK_KIND', '%s')", versionStr);
            SafeDBExecute(db, sql);

            sql = String.format("insert into db_version (table_name,version) values ('WORK_LIST', '%s')", versionStr);
            SafeDBExecute(db, sql);

            sql = String.format("insert into db_version (table_name,version) values ('MESSAGE', '%s')", versionStr);
            SafeDBExecute(db, sql);

            sql = String.format("insert into db_version (table_name,version) values ('MY_WORK_LIST', '%s')", versionStr);
            SafeDBExecute(db, sql);

        } else {

        }
    }

    /**
     * 执行SQL
     *
     * @param db
     * @param sql
     */
    public boolean SafeDBExecute(SQLiteDatabase db, String sql) {
        try {
            db.execSQL(sql);
//            long start = System.currentTimeMillis();
//            long end = System.currentTimeMillis();
//            LogUtils.e("DB_SQLCipher", "SafeExcute---"+(end-start)+"");
            //           LogUtils.i(TAG, "sql: "+sql);
            return true;
        } catch (Exception e) {
            LogUtils.i(TAG, "SafeDBExecute异常:" + "sql=" + sql + " ," + e.getMessage());
            return false;
        }
    }

    /**
     * 查询sql
     *
     * @param db
     * @param sql
     * @return
     */
    public Cursor SafeQuery(SQLiteDatabase db, String sql) {
        try {
            Cursor cursor = db.rawQuery(sql, null);
//            long start = System.currentTimeMillis();
//            long end = System.currentTimeMillis();
//            LogUtils.e("DB_SQLCipher", "SafeQuery---"+(end-start)+"");
            return cursor;
        } catch (Exception e) {
            Log.d(TAG, "执行SQL读取失败！" + sql);
            return null;
        }
    }


    public synchronized void closeDB(){
        if(mDataBase!=null){
            mDataBase.close();
//            long start = System.currentTimeMillis();
//            long end = System.currentTimeMillis();
//            LogUtils.e("DB_SQLCipher", "DBClose---"+(end-start)+"");
            sqLiteOpenHelper.close();
            mDataBase = null;
            sqLiteOpenHelper = null;
        }
    }

    public String getDBName() {
        String path = HRMPApplication.getInstance().getApplicationInfo().dataDir;
        dbName = path+"/files/users/"+HRMPApplication.getInstance().currentUser.getLoginName()+"/"+Constant.DB_NAME;
//        LogUtils.i(TAG, "dbName-->"+dbName);
        return dbName;
    }

    /**
     * 更新表结构
     *
     * @param db
     */
    private void updateTable(SQLiteDatabase db) {
         /*HISTORY_IMS是否存在is_readed integer 字段*/
        if (!isExistsColumn(db, "HISTORY_IMS", "is_readed")) {
            updateColumn(db, "HISTORY_IMS", "is_readed", "integer");
        }
    }


    // 检查某个表是否存在
    private boolean isExistsTable(SQLiteDatabase db, String tableName) {
        boolean result = false;
        Cursor cursor = null;
        try {
            cursor = db.rawQuery("select * from sqlite_master where name = ?", new String[]{tableName});
            result = null != cursor && cursor.moveToFirst();
        } catch (Exception e) {
            LogUtils.e(TAG, "isExistsTable...", e);
        } finally {
            if (null != cursor && !cursor.isClosed()) {
                cursor.close();
            }
        }
        return result;
    }

    /**
     * 检查表中某列是否存在
     *
     * @param db
     * @param tableName  表名
     * @param columnName 列名
     * @return
     */
    private boolean isExistsColumn(SQLiteDatabase db, String tableName, String columnName) {
        boolean result = false;
        Cursor cursor = null;
        try {
            cursor = db.rawQuery("select * from sqlite_master where name = ? and sql like ?"
                    , new String[]{tableName, "%" + columnName + "%"});
            result = null != cursor && cursor.moveToFirst();
        } catch (Exception e) {
            LogUtils.e(TAG, "isExistsColumn...", e);
        } finally {
            if (null != cursor && !cursor.isClosed()) {
                cursor.close();
            }
        }
        return result;
    }

    /**
     * 修改表结构
     *
     * @param db
     * @param tableName  表名
     * @param columnName 字段名
     */
    private void updateColumn(SQLiteDatabase db, String tableName, String columnName, String DBType) {
        String sql = "ALTER TABLE " + tableName + " ADD " + columnName + " " + DBType;
        LogUtils.i(TAG, " updateColumn() sql=" + sql);
        try {
            SafeDBExecute(db, sql);
        } catch (Exception e) {
            LogUtils.e(TAG, " updateTable is error :sql=" + sql, e);
        }
    }

    /**
     * 升级数据库
     */
    private void updateDB(Context context, SQLiteDatabase db) {

        PackageManager pm = context.getPackageManager();
        PackageInfo pkgInfo = null;
        try {
            pkgInfo = pm.getPackageInfo(Constant.APP_PACKAGE_NAME, PackageManager.GET_CONFIGURATIONS);
        } catch (PackageManager.NameNotFoundException e) {
            LogUtils.e(TAG, "updateDB() not get pkgInfo info", e);
            return;
        }
        updateTable(db);
    }

    /*
     *	获取当前使用的db对象
     */
    public synchronized SQLiteDatabase getCurDB() {
        if(mDataBase!=null){
            return mDataBase;
        }
        try {
            String DBName = this.getDBName();
            if (TextUtils.isEmpty(DBName)) {
                Log.d(TAG, "dbname is null！");
                return null;
            }
            mDataBase = sqLiteOpenHelper.getWritableDatabase();
        } catch (Exception e) {
            LogUtils.i(TAG, e.getMessage());

        }
        return mDataBase;
    }


    public void setCurDBNone(){
        mDataBase = null;
    }

    /**
     * 数据库数据迁移
     * @param tempDBPath
     * @return
     */
    public boolean databaseTransfer(String tempDBPath, Context context){

        try {
            LogUtils.i(TAG, "databaseTransfer begin ");

            SQLiteDatabase database = getCurDB();
            String sql = "attach '"+tempDBPath+"' as 'efetionOld' key ''";
//            database.execSQL(sql);
            SafeDBExecute(database,sql);


            database.beginTransaction();
            sql = "insert into HISTORY_IMS select * from efetionOld.HISTORY_IMS";
            SafeDBExecute(database,sql);
            sql = "insert into GROUPS(GROUP_ID, GROUP_NAME,GROUP_VERSION) select GROUP_ID,GROUP_NAME,GROUP_VERSION from efetionOld.GROUPS";
            SafeDBExecute(database,sql);
            sql = "insert into GROUP_EVENT select * from efetionOld.GROUP_EVENT";
            SafeDBExecute(database,sql);
            sql = "insert into GROUP_MEMBER select * from efetionOld.GROUP_MEMBER";
            SafeDBExecute(database,sql);
            sql = "insert into SYS_NOTIFY select * from efetionOld.SYS_NOTIFY";
            SafeDBExecute(database,sql);
            sql = "insert into ENTERPRISE_BULLETIN select * from efetionOld.ENTERPRISE_BULLETIN";
            SafeDBExecute(database,sql);
            sql = "insert into v_call_log select * from efetionOld.v_call_log";
            SafeDBExecute(database,sql);
            sql = "insert into recv_files select * from efetionOld.recv_files";
            SafeDBExecute(database,sql);
            sql = "insert into LATEST_HISTORY_IMS select * from efetionOld.LATEST_HISTORY_IMS";
            SafeDBExecute(database,sql);
            sql = "insert into RECENT_COMMUNICATION select * from efetionOld.RECENT_COMMUNICATION";
            SafeDBExecute(database,sql);
            sql = "insert into PLATFORM_SMS select * from efetionOld.PLATFORM_SMS";
            SafeDBExecute(database,sql);
            sql = "insert into SessionConfig select * from efetionOld.SessionConfig";
            SafeDBExecute(database,sql);
            sql = "insert into UnsendMsg select * from efetionOld.UnsendMsg";
            SafeDBExecute(database,sql);
            sql = "insert into ent_department select * from efetionOld.ent_department";
            SafeDBExecute(database,sql);
            sql = "insert into ent_employees select * from efetionOld.ent_employees";
            SafeDBExecute(database,sql);
            sql = "insert into ent_comlinkman select * from efetionOld.ent_comlinkman";
            SafeDBExecute(database,sql);
            database.setTransactionSuccessful();
            database.endTransaction();
            database.close();
            setCurDBNone();

            File file = new File(tempDBPath);
            boolean suc = file.delete();

            SharedPreferences shp = context.getSharedPreferences(Constant.DB_VERSION, Context.MODE_PRIVATE);
            SharedPreferences.Editor editor = shp.edit();
            editor.putString(HRMPApplication.getInstance().currentUser.getLoginName()+Constant.DB_VERSION, "1");
            editor.commit();
            LogUtils.i(TAG, "databaseTransfer end ");

            return true;
        }catch (SQLException e){
            LogUtils.i(TAG, e.getMessage());
            return false;

        }

    }

    private void deleteDirtyData(){
        SQLiteDatabase database = getCurDB();
        try{
            String sql = "DELETE FROM SUBSCRIPTION_INFO";
            SafeDBExecute(database,sql);
            sql = "DELETE FROM RECENT_COMMUNICATION WHERE length(other_uri)=11 and other_uri like '400%'";
            SafeDBExecute(database,sql);
        }catch (Exception e){
            e.printStackTrace();
        }finally {
//            database.close();
        }
    }

}
