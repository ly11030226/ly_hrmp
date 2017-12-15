package com.hrmp.view;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.hrmp.DBTools;
import com.hrmp.util.LogUtils;

/**
 * Created by Ly on 2017/6/5.
 */

public class MyDatabaseOpenHelper extends SQLiteOpenHelper {

    public MyDatabaseOpenHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        String sql = "";
        DBTools dbTools = DBTools.get_inst();
        try {
            //工种表
            sql = "CREATE TABLE IF NOT EXISTS WORK_KIND(" +
                    "'_id' INTEGER PRIMARY KEY," +
                    "workKind VARCHAR NOT NULL," +
                    "hireNum VARCHAR" +
                    ")";
            dbTools.SafeDBExecute(sqLiteDatabase,sql);

            //每个工种含有的报名信息
            sql = "CREATE TABLE IF NOT EXISTS WORK_LIST(" +
                    "'_id' INTEGER PRIMARY KEY," +
                    "workDescri VARCHAR," +
                    "unitPrice VARCHAR" +
                    ")";
            dbTools.SafeDBExecute(sqLiteDatabase,sql);

            //消息表
            sql = "CREATE TABLE IF NOT EXISTS MESSAGE(" +
                    "'_id' INTEGER PRIMARY KEY," +
                    "createTime VARCHAR," +
                    "sendUserName VARCHAR," +
                    "messageTitle VARCHAR," +
                    "messageContent VARCHAR," +
                    "isRead VARCHAR" +
                    ")";
            dbTools.SafeDBExecute(sqLiteDatabase,sql);

            //我的报名表
            sql = "CREATE TABLE IF NOT EXISTS MY_WORK_LIST(" +
                    "'_id' INTEGER PRIMARY KEY," +
                    "signTime VARCHAR," +
                    "workDescri VARCHAR," +
                    "canCancelSign VARCHAR" +
                    ")";

        }catch (Exception ex) {
            LogUtils.e(" init_db is error :sql=" + sql, ex);
        } finally {
            if (sqLiteDatabase != null) {
            }
        }
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int oldVersion, int newVersion) {
        if(oldVersion == 1 && newVersion == 2){
            updateSubscription(sqLiteDatabase);
        }

    }

    private void updateSubscription(SQLiteDatabase db) {
        try{
            DBTools dbTools = DBTools.get_inst();
            String sql = "DROP TABLE subscription_info";
            dbTools.SafeDBExecute(db,sql);
            sql = "CREATE TABLE IF NOT EXISTS subscription_info(" +
                    "_id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    "msg_id VARCHAR NOT NULL, " +
                    "from_uri VARCHAR, " +
                    "to_uri VARCHAR, " +
                    "msg_type integer, " +
                    "content VARCHAR, " +
                    "content_type integer, " +
                    "time_stamp integer, " +
                    "is_readed integer, " +
                    "play_state integer default 0, " +
                    "STATUS integer," +
                    "TRANSFER_ID VARCHAR, " +
                    "PROGRESS INTEGER, " +
                    "FILE_SIZE INTEGER, " +
                    "THUMB_PATH VARCHAR, " +
                    "NEED_READREPORT INTEGER, " +
                    "msgName VARCHAR(64)," +
                    "version VARCHAR(64)," +
                    "userId VARCHAR(32)," +
                    "activeStatus VARCHAR(8)," +
                    "forwardable VARCHAR(4)," +
                    "pauuid VARCHAR(32)," +
                    "mediaType SMALLINT(6)," +
                    "createTime VARCHAR(32)," +
                    "title VARCHAR(64)," +
                    "author VARCHAR(16)," +
                    "thumbLink VARCHAR(128)," +
                    "originalLink VARCHAR(128)," +
                    "sourceLink VARCHAR(128)," +
                    "bodyLink VARCHAR(128)," +
                    "mainText VARCHAR(512)," +
                    "mediaUUID VARCHAR(32)" +
                    ")";;
            dbTools.SafeDBExecute(db,sql);
            sql = "DELETE FROM RECENT_COMMUNICATION WHERE length(other_uri)=11 and other_uri like '400%'";
            dbTools.SafeDBExecute(db,sql);

            //公众号列表表
            sql = "CREATE TABLE IF NOT EXISTS enterpriseServiceList(" +
                    "_id INTEGER PRIMARY KEY AUTOINCREMENT," +
                    "paUUID VARCHAR(32), " +
                    "name VARCHAR(32), " +
                    "logo VARCHAR(32), " +
                    "intro VARCHAR(32), " +
                    "time VARCHAR(32), " +
                    "status INTEGER, " +
                    "logoRealUrl VARCHAR(32)," +
                    "logolocalpath VARCHAR(32)" +
                    ")";
            dbTools.SafeDBExecute(db, sql);

        }catch (Exception e){
            e.printStackTrace();
        }
    }

}