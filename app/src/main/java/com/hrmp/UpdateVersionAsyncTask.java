package com.hrmp;

import android.os.AsyncTask;
import android.os.Environment;

import com.dd.CircularProgressButton;
import com.hrmp.util.LogUtils;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;

import static android.os.Environment.getExternalStorageDirectory;

/**
 * 更新版本用到的AsyncTask
 */

public class UpdateVersionAsyncTask extends AsyncTask<String,Integer,String> {

    private static final String TAG = "UpdateVersionAsyncTask";
    private CircularProgressButton cpb;
    public UpdateVersionAsyncTask(CircularProgressButton cpb) {
        this.cpb = cpb;
    }

    /**
     * onPreExecute方法用于在执行后台任务前做一些UI操作
     */
    @Override
    protected void onPreExecute() {
        super.onPreExecute();
    }

    /**
     * doInBackground方法内部执行后台任务,不可在此方法内修改UI
     * @param params
     * @return
     */
    @Override
    protected String doInBackground(String... params) {
        try {
            LogUtils.i(TAG,"doInBackground url ... "+params[0]);
            URL url = new URL(params[0]);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");
            conn.connect();
            if ( 200 == conn.getResponseCode()) {
                //判断手机是否已经插入了SD卡且具有读写sd卡的能力
                if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
                    File file = null;
                    //在存储卡下创建apk
                    String dirName = getExternalStorageDirectory().getAbsolutePath()+File.separator+"hrmp.apk";
                    file = new File(dirName);
                    if (!file.exists()) {
                        file.mkdirs();
                    }
                    file = new File(getExternalStorageDirectory(),"hrmp.apk");
                    if (file.exists()) {
                        file.delete();
                    }
                    int total = conn.getContentLength();
                    LogUtils.i(TAG,"getContentLength ... "+total);
                    //得到输入流
                    InputStream is = new BufferedInputStream(conn.getInputStream());
                    OutputStream out = new FileOutputStream(file);

                    byte[] buffer = new byte[1024];
                    int len;
                    long count = 0;
                    while(-1 != (len = is.read(buffer))){
                        out.write(buffer,0,len);
                        count+=len;
                        //调用publishProgress公布进度,最后onProgressUpdate方法将被执行
                        LogUtils.i(TAG,"len ... "+len+"   count ... "+count);
                        publishProgress((int)(count* 100 / total));
                    }
                    out.flush();
                    out.close();
                    is.close();
                }else{
                    LogUtils.i(TAG,"手机没有插入了SD卡或者不具有读写sd卡的能力");
                }
            }else{
                LogUtils.i(TAG,"responseCode ... "+conn.getResponseCode());
            }
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (ProtocolException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
    /**
     * onPostExecute方法用于在执行完后台任务后更新UI,显示结果
     * @param s
     */
    @Override
    protected void onPostExecute(String s) {
        super.onPostExecute(s);
    }

    /**
     * onProgressUpdate方法用于更新进度信息
     * @param values
     */
    @Override
    protected void onProgressUpdate(Integer... values) {
        if (this.cpb!=null) {
            LogUtils.i(TAG,"onProgressUpdate value ... "+values[0]);
            this.cpb.setProgress(values[0]);
        }
    }

    @Override
    protected void onCancelled(String s) {
        super.onCancelled(s);
    }

    /**
     * onCancelled方法用于在取消执行中的任务时更改UI
     */
    @Override
    protected void onCancelled() {
        cpb.setProgress(0);
    }
}
