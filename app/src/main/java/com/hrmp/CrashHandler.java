package com.hrmp;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.os.Build;
import android.os.Environment;
import android.os.Looper;
import android.widget.Toast;

import com.hrmp.util.FileTools;
import com.hrmp.util.LogFileUtils;
import com.hrmp.util.LogUtils;
import com.hrmp.util.UploadUtil;
import com.hrmp.util.ZipUtils;

import java.io.File;
import java.io.FileOutputStream;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.io.Writer;
import java.lang.Thread.UncaughtExceptionHandler;
import java.lang.reflect.Field;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * UncaughtException处理类,当程序发生Uncaught异常的时候,有该类来接管程序,并记录发送错误报告
 *
 * 
 * @(#)CrashHandler.java
 * 
 * @Copyright
 */
@SuppressLint("SimpleDateFormat")
public class CrashHandler implements UncaughtExceptionHandler {

	public static final String TAG = "CrashHandler";

	// 记录是否上传崩溃日志标识
	private static final boolean isUploadError = true;
	//public static String requestURL;
	// 系统默认的UncaughtException处理类
	private Thread.UncaughtExceptionHandler mDefaultHandler;
	// CrashHandler实例
	private static CrashHandler INSTANCE = new CrashHandler();
	// 程序的Context对象
	private Context mContext;
	// 用来存储设备信息和异常信息
	private final Map<String, String> infos = new HashMap<String, String>();

	/** 保证只有一个CrashHandler实例 */
	private CrashHandler() {
	}

	/** 获取CrashHandler实例 ,单例模式 */
	public static CrashHandler getInstance() {
		return INSTANCE;
	}

	/**
	 * 初始化
	 * 
	 * @param context
	 */
	public void init(Context context) {
		mContext = context;
		// requestURL = "http://10.10.91.82:8080/UploadError/UploadServlet";
		// 获取系统默认的UncaughtException处理器
		mDefaultHandler = Thread.getDefaultUncaughtExceptionHandler();
		// 设置该CrashHandler为程序的默认处理器
		Thread.setDefaultUncaughtExceptionHandler(this);
	}

	/**
	 * 当UncaughtException发生时会转入该函数来处理
	 */
	@Override
	public void uncaughtException(Thread thread, Throwable ex) {
		if (!handleException(ex) && mDefaultHandler != null) {
			// 如果用户没有处理则让系统默认的异常处理器来处理
			HRMPApplication.getInstance().exit(0);
			mDefaultHandler.uncaughtException(thread, ex);
		} 
		else 
		{
			try {
				HRMPApplication.getInstance().exit(0);
				Thread.sleep(1000);
			} catch (InterruptedException e) {
				LogUtils.i(TAG, "error : " + e.getMessage());
			}
		}
		// 退出程序
		LogUtils.i(TAG, "kill : " + android.os.Process.myPid());
		android.os.Process.killProcess(android.os.Process.myPid());
		System.exit(0);
	}


	/**
	 * 自定义错误处理,收集错误信息 发送错误报告等操作均在此完成.
	 * 
	 * @param ex
	 * @return true:如果处理了该异常信息;否则返回false.
	 */
	private boolean handleException(Throwable ex) {
		if (ex == null) {
			return false;
		}
		// 使用Toast来显示异常信息
		new Thread() {
			@Override
			public void run() {
				Looper.prepare();
				Toast.makeText(mContext, R.string.app_crash_tip, Toast.LENGTH_LONG).show();
				Looper.loop();
			}
		}.start();

		// 收集设备参数信息
		collectDeviceInfo(mContext);
		// 保存日志文件
		saveCrashInfoile(ex);
		HRMPApplication.getInstance().exit(0);
		return true;
	}

	/**
	 * 收集设备参数信息
	 * 
	 * @param ctx
	 */
	public void collectDeviceInfo(Context ctx) {
		try {
			PackageManager pm = ctx.getPackageManager();
			PackageInfo pi = pm.getPackageInfo(ctx.getPackageName(), PackageManager.GET_ACTIVITIES);
			if (pi != null) {
				String versionName = pi.versionName == null ? "null": pi.versionName;
				String versionCode = pi.versionCode + "";
				infos.put("versionDesc", "New_Framework");
				infos.put("versionName", versionName);
				infos.put("versionCode", versionCode);
			}
		} catch (NameNotFoundException e) {
			LogUtils.e(TAG,"an error occured when collect package info", e);
		}
		Field[] fields = Build.class.getDeclaredFields();
		for (Field field : fields) {
			try {
				field.setAccessible(true);
				infos.put(field.getName(), field.get(null).toString());
			} catch (Exception e) {
				LogUtils.e(TAG,"an error occured when collect crash info", e);
			}
		}
	}

	/**
	 * 保存错误信息到文件中
	 * 
	 * @param ex
	 * @return 返回文件名称,便于将文件传送到服务器
	 */
	private String saveCrashInfoile(Throwable ex) {
		//服务器URL
		String requestURL = "test";
		
		StringBuffer sb = new StringBuffer();
		SimpleDateFormat logFt = new SimpleDateFormat("yyyyMMddHHmmss");
		for (Map.Entry<String, String> entry : infos.entrySet()) {
			String key = entry.getKey();
			String value = entry.getValue();
			if (key.equals("TIME")) {
				Date d = new Date(Long.valueOf(value + ""));
				sb.append(key + "=" + logFt.format(d) + "\n");
			} else {
				sb.append(key + "=" + value + "\n");
			}

		}
		
		
		Writer writer = new StringWriter();
		PrintWriter printWriter = new PrintWriter(writer);
		ex.printStackTrace(printWriter);
		Throwable cause = ex.getCause();
		while (cause != null) {
			cause.printStackTrace(printWriter);
			cause = cause.getCause();
		}
		printWriter.close();
		String result = writer.toString();
		sb.append(result);
		try {
			Date d = new Date(System.currentTimeMillis());
			String format_time = logFt.format(d);
			String fileName = "crash_" + format_time + ".txt";
			LogUtils.d(TAG, "log error ："+fileName);
			LogUtils.d(TAG, sb.toString());
			if(!Environment.getExternalStorageState().equals(// 无SD卡，则直接上传错误日志
					Environment.MEDIA_MOUNTED)) {
				if (!isUploadError) {
					return null;
				}
				LogUtils.d(TAG, "发错误日志到服务器----start");
				LogUtils.i(TAG, "=="+requestURL);
				UploadUtil uploadUtil = new UploadUtil();
				uploadUtil.uploadErrorByPost(UploadUtil.ErrorType.StringError, requestURL,null, sb.toString(), new ArrayList<File>());
				LogUtils.d(TAG, "发错误日志到服务器----end");
				return null;
			}

			File dir = new File(FileTools.errorLogDir);
			if (!dir.exists()) {
				dir.mkdirs();
			}
			File f = new File(FileTools.errorLogDir+ "/"+ fileName);
			if (!f.exists()) {
				f.createNewFile();
			}
			FileOutputStream fos = new FileOutputStream(f, true);
			fos.write(sb.toString().getBytes());
			fos.close();
			if (!isUploadError)
				return fileName;
			sendErrorLogFileToServer(f, requestURL);
			return fileName;
		} catch (Exception e) {
			LogUtils.e(TAG, "an error occured while writing file...", e);
		}
		return null;
	}

	/**
	 * 发错误日志文件到服务器
	 * 
	 * @param
	 * @param serverURL
	 */
	public static void sendErrorLogFileToServer(File logFile, String serverURL) {
		SimpleDateFormat logFt = new SimpleDateFormat("yyyyMMddHHmmss");
		List<File> files = new ArrayList<File>();
		LogFileUtils.list(logFile.getParentFile(), "crash", ".txt", "3", files);
		String time = logFt.format(new Date());
		try {
			if (files.isEmpty())
				return;
			LogUtils.d(TAG, "发错误日志到服务器--sendErrorLogFileToServer--start");
			File zipFile = new File(logFile.getParent() + "/crash_" + time
					+ ".txt.gz");
			LogUtils.d(TAG, "zipFile="+zipFile.getPath());
			ZipUtils.zipFiles(files, zipFile);
			UploadUtil uploadUtil = new UploadUtil();
			uploadUtil.uploadErrorByPost(UploadUtil.ErrorType.FileError, serverURL, zipFile, null, files);
			LogUtils.d(TAG, "发错误日志到服务器--sendErrorLogFileToServer--end");
		} catch (Exception e) {
			return;
		}
	}


}