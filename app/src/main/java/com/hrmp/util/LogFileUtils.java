package com.hrmp.util;

import android.os.Environment;
import android.util.Log;

import java.io.File;
import java.io.FilenameFilter;
import java.util.List;

import static android.content.ContentValues.TAG;

/**
 * 
 * @(#)FileUitl.java
 * 
 * @Copyright 2012 AngelShine
 */
public class LogFileUtils {


	public static boolean deleteDirectory(String fileName) {
		boolean status;
		SecurityManager checker = new SecurityManager();

		if (!fileName.equals("")) {

			// File path = Environment.getExternalStorageDirectory();
			File newPath = new File(fileName);
			checker.checkDelete(newPath.toString());
			if (newPath.isDirectory()) {
				String[] listfile = newPath.list();
				// delete all files within the specified directory and then
				// delete the directory
				try {
					for (int i = 0; i < listfile.length; i++) {
						File deletedFile = new File(newPath.toString() + "/"
								+ listfile[i].toString());
						deletedFile.delete();
					}
					newPath.delete();
					Log.e(TAG, fileName);
					status = true;
				} catch (Exception e) {
					e.printStackTrace();
					status = false;
				}

			} else
				status = false;
		} else
			status = false;
		return status;
	}


	public static boolean deleteFile(String fileName) {
		boolean status;
		SecurityManager checker = new SecurityManager();

		if (!fileName.equals("")) {

			File path = Environment.getExternalStorageDirectory();
			File newPath = new File(path.toString() + fileName);
			checker.checkDelete(newPath.toString());
			if (newPath.isFile()) {
				try {
					Log.e(TAG, fileName);
					newPath.delete();
					status = true;
				} catch (SecurityException se) {
					se.printStackTrace();
					status = false;
				}
			} else
				status = false;
		} else
			status = false;
		return status;
	}

	public static List<File> list(File dir, String nametxt, String ext,
								  String type, List<File> fs) {

		listFile(dir, nametxt, type, ext, fs);
		File[] all = dir.listFiles();
		// 閫掑綊鑾峰緱褰撳墠鐩綍鐨勬墍鏈夊瓙鐩綍
		for (int i = 0; i < all.length; i++) {
			File d = all[i];
			if (d.isDirectory()) {
				list(d, nametxt, ext, type, fs);
			}
		}
		return null;
		// 閬嶅巻瀛愮洰,鍒楀嚭姣忎釜瀛愮洰褰曠殑鏂囦欢
	}

	/**
	 *
	 */
	private static List<File> listFile(File dir, String nametxt, String type,
									   String ext, List<File> fs) {
		File[] all = dir.listFiles(new Fileter(ext));
		for (int i = 0; i < all.length; i++) {
			File d = all[i];
			if (d.getName().toLowerCase().indexOf(nametxt.toLowerCase()) >= 0) {
				if (type.equals("1")) {
					fs.add(d);
				} else if (d.isDirectory() && type.equals("2")) {
					fs.add(d);
				} else if (!d.isDirectory() && type.equals("3")) {
					fs.add(d);
				}
			}

		}
		return fs;
	}

	static class Fileter implements FilenameFilter {
		private final String ext;

		public Fileter(String ext) {
			this.ext = ext;
		}

		@Override
		public boolean accept(File dir, String name) {
			return name.endsWith(ext);

		}
	}

}