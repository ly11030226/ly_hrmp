package com.hrmp.util;


import android.text.TextUtils;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

/**
 * 上传工具类
 * 
 */
public class UploadUtil {
	private static final String TAG = "uploadFile";
	private static final int TIME_OUT = 100 * 1000; // 超时时间
	private static final String CHARSET = "utf-8"; // 设置编码

	public enum ErrorType {
		StringError,FileError
	}


	private static String transferInputStreamToString(InputStream in)
			throws IOException {
		ByteArrayOutputStream outStream = new ByteArrayOutputStream();
		byte[] data = new byte[4096];
		int count;
		while ((count = in.read(data, 0, 4096)) != -1) {
			outStream.write(data, 0, count);
		}
		return new String(outStream.toByteArray(), "UTF-8");
	}

	public void uploadErrorByPost(ErrorType type, String url, File file,
								  String stringError, final List<File> files) {
		final String requestUrl = url;
		final File uploadFile = file;
		final ErrorType uploadType = type;
		final String stringErrorContent = stringError;
		new Thread() {
			@Override
			public void run() {
				super.run();


				try {
					String errorFileMsg = "";
					if (ErrorType.StringError == uploadType
							&& !TextUtils.isEmpty(stringErrorContent)) {
						errorFileMsg = new String(
								Base64_2.encode(stringErrorContent.getBytes()));

					} else if (uploadFile != null) {
						errorFileMsg = transferInputStreamToString(new FileInputStream(
								uploadFile));
						errorFileMsg = new String(Base64_2.encode(errorFileMsg
								.getBytes()));
					}

					SimpleDateFormat format = new SimpleDateFormat(
							"yyyyMMddHHmmss");
					Date date = new Date(System.currentTimeMillis());
					String uploadTime = format.format(date);

					// POST XML数据
					String requestContent = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>"
							+ "<Client_Error_Log>"
							+ "<SipUri>"
							//用户id
//							+ UserAccountService.getOwnId()
							+ "</SipUri>"
							+ "<Client_Version>"
							+ "cmccuc-2013"
							+ "</Client_Version>"
							+ "<Log_Upload_Time>"
							+ uploadTime
							+ "</Log_Upload_Time>"
							+ "<Error_Type>"
							+ "3024"
							+ "</Error_Type>"
							+ "<Error_Details>"
							+ "dumpinfo"
							+ "</Error_Details>"
							+ "<Dump_File encoding=\"base64\">"
							+ errorFileMsg
							+ "</Dump_File>" + "</Client_Error_Log>";

					//发送post请求
//					HttpAction.httpsPost(requestUrl, requestContent, new HttpAction.HttpsCallback() {
//						@Override
//						public void onCallback(String response) {
//							LogUtils.d(TAG, response);
//							if (uploadFile != null && uploadFile.exists()) {
//								uploadFile.delete();
//							}
//						}
//					});

				} catch (Exception e) {
					e.printStackTrace();
				} finally {
					if (uploadFile != null && uploadFile.exists()) {
						uploadFile.delete();
					}
				}
			}
		}.start();

		// 网上的以文件形式POST的方式
		/*
		 * File input = new File("D:\\01\\simpass\\auth.xml"); try {
		 * postAsyn.setRequestBody(new FileInputStream(input)); if (input.length() <
		 * Integer.MAX_VALUE) postAsyn.setRequestContentLength(input.length()); else
		 * postAsyn
		 * .setRequestContentLength(EntityEnclosingMethod.CONTENT_LENGTH_CHUNKED
		 * ); // 指定请求内容的类型 postAsyn.setRequestHeader("Content-type", "text/xml;
		 * charset=UTF-8"); } catch (FileNotFoundException e) { // TODO
		 * Auto-generated catch block e.printStackTrace(); }
		 */

		// 使用StringRequestEntity POST XML

	}


}