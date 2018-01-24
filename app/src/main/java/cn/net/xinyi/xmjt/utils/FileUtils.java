package cn.net.xinyi.xmjt.utils;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

public class FileUtils {


	private static final String TAG = "FileUtils";
	public static final String CLEAR_BUFFER = "ClearBuffer";
	public static final String CLEAR_TRACE = "ClearTrace";
	public static final String CLEAR_FILE = "ClearFile";

	// android获取一个用于打开HTML文件的intent
	public static Intent getHtmlFileIntent(File file) {
		Uri uri = Uri.parse(file.toString()).buildUpon()
				.encodedAuthority("com.android.htmlfileprovider")
				.scheme("content").encodedPath(file.toString()).build();
		Intent intent = new Intent("android.intent.action.VIEW");
		intent.setDataAndType(uri, "text/html");
		return intent;
	}

	// android获取一个用于打开图片文件的intent
	public static Intent getImageFileIntent(File file) {
		Intent intent = new Intent("android.intent.action.VIEW");
		intent.addCategory("android.intent.category.DEFAULT");
		intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		Uri uri = Uri.fromFile(file);
		intent.setDataAndType(uri, "image/*");
		return intent;
	}

	// android获取一个用于打开PDF文件的intent
	public static Intent getPdfFileIntent(File file) {
		// Intent intent = new Intent("android.intent.action.VIEW");
		// intent.addCategory("android.intent.category.DEFAULT");
		// intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		// Uri uri = Uri.fromFile(file);
		// intent.setDataAndType(uri, "application/pdf");
		// return intent;
		return getWpsIntent(file);
	}

	// android获取一个用于打开文本文件的intent
	public static Intent getTextFileIntent(File file) {
		// Intent intent = new Intent("android.intent.action.VIEW");
		// intent.addCategory("android.intent.category.DEFAULT");
		// intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		// Uri uri = Uri.fromFile(file);
		// intent.setDataAndType(uri, "text/plain");
		// return intent;
		return getWpsIntent(file);
	}

	// android获取一个用于打开音频文件的intent
	public static Intent getAudioFileIntent(File file) {
		Intent intent = new Intent("android.intent.action.VIEW");
		intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
		intent.putExtra("oneshot", 0);
		intent.putExtra("configchange", 0);
		Uri uri = Uri.fromFile(file);
		intent.setDataAndType(uri, "audio/*");
		return intent;
	}

	// android获取一个用于打开视频文件的intent
	public static Intent getVideoFileIntent(File file) {
		Intent intent = new Intent("android.intent.action.VIEW");
		intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
		intent.putExtra("oneshot", 0);
		intent.putExtra("configchange", 0);
		Uri uri = Uri.fromFile(file);
		intent.setDataAndType(uri, "video/*");
		return intent;
	}

	// android获取一个用于打开CHM文件的intent
	public static Intent getChmFileIntent(File file) {
		Intent intent = new Intent("android.intent.action.VIEW");
		intent.addCategory("android.intent.category.DEFAULT");
		intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		Uri uri = Uri.fromFile(file);
		intent.setDataAndType(uri, "application/x-chm");
		return intent;
	}

	// android获取一个用于打开Word文件的intent
	public static Intent getWordFileIntent(File file) {
		// Intent intent = new Intent("android.intent.action.VIEW");
		// intent.addCategory("android.intent.category.DEFAULT");
		// intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		// Uri uri = Uri.fromFile(file);
		// intent.setDataAndType(uri, "application/msword");
		// return intent;
		return getWpsIntent(file);
	}

	// android获取一个用于打开Excel文件的intent
	public static Intent getExcelFileIntent(File file) {
		// Intent intent = new Intent("android.intent.action.VIEW");
		// intent.addCategory("android.intent.category.DEFAULT");
		// intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		// Uri uri = Uri.fromFile(file);
		// intent.setDataAndType(uri, "application/vnd.ms-excel");
		// return intent;
		return getWpsIntent(file);
	}

	// android获取一个用于打开PPT文件的intent
	public static Intent getPPTFileIntent(File file) {
		// Intent intent = new Intent("android.intent.action.VIEW");
		// intent.addCategory("android.intent.category.DEFAULT");
		// intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		// Uri uri = Uri.fromFile(file);
		// intent.setDataAndType(uri, "application/vnd.ms-powerpoint");
		// return intent;
		return getWpsIntent(file);
	}

	// android获取一个用于打开apk文件的intent
	public static Intent getApkFileIntent(File file) {
		Intent intent = new Intent();
		intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		intent.setAction(Intent.ACTION_VIEW);
		intent.setDataAndType(Uri.fromFile(file),
				"application/vnd.android.package-archive");
		return intent;
	}

	/**
	 * 检查文件扩展名
	 *
	 * @param checkItsEnd
	 * @param fileEndings
	 * @return
	 */
	public static boolean checkEndsWithInStringArray(String checkItsEnd,
			String[] fileEndings) {
		for (String aEnd : fileEndings) {
			if (checkItsEnd.toLowerCase().endsWith(aEnd))
				return true;
		}
		return false;
	}

	/**
	 * Java文件操作 获取文件扩展名
	 *
	 * Created on: 2011-8-2 Author: nodeny
	 */
	public static String getExtensionName(String filename) {
		if ((filename != null) && (filename.length() > 0)) {
			int dot = filename.lastIndexOf('.');
			if ((dot > -1) && (dot < (filename.length() - 1))) {
				return filename.substring(dot + 1);
			}
		}
		return filename;
	}

	/**
	 * Java文件操作 获取不带扩展名的文件名
	 *
	 * Created on: 2011-8-2 Author: nodeny
	 */
	public static String getFileNameNoEx(String filename) {
		if ((filename != null) && (filename.length() > 0)) {
			int dot = filename.lastIndexOf('.');
			if ((dot > -1) && (dot < (filename.length()))) {
				return filename.substring(0, dot);
			}
		}
		return filename;
	}

	/**
	 * Java文件操作 根据文件全路径获取文件名
	 *
	 * Created on: 2011-8-2 Author: nodeny
	 */
	public static String getFileName(String filePath) {

		if ((filePath != null) && (filePath.length() > 0)) {
			int dot = filePath.lastIndexOf('/');
			if ((dot > -1) && (dot < (filePath.length() - 1))) {
				return filePath.substring(dot + 1);
			}
		}
		return filePath;
	}

	/**
	 * 获取文档根目录
	 *
	 * @return
	 */
	public static String getDocFileDirPath() {

		String docFilesDirStr = Environment.getExternalStorageDirectory()
				+ "/xmjt/";
		File docRootDir = new File(docFilesDirStr);
		if (!docRootDir.exists()) {
			docRootDir.mkdirs();
		}
		return docRootDir.toString();
	}

	/**
	 * 获取临时文档生成目录
	 */
	public static String getTmpDocFileDirPath() {
		String docFilesDirStr = Environment.getExternalStorageDirectory()
				+ "/xmjt/tmp/";
		File docTmpDir = new File(docFilesDirStr);
		if (!docTmpDir.exists()) {
			docTmpDir.mkdirs();
		}
		return docTmpDir.toString();
	}

	/**
	 * 清除临时文件；
	 */
	public static void cleanTmpFile() {
		File docTmpDir = new File(getTmpDocFileDirPath());
		if (docTmpDir != null && docTmpDir.isDirectory()) {
			File[] fileList = docTmpDir.listFiles();
			for (int i = 0; i < fileList.length; i++) {
				fileList[i].delete();
			}
		}
	}

	/**
	 * 从assets文件夹复制文件
	 *
	 * @param context
	 * @param fileName
	 * @param path
	 * @return
	 */
	public static boolean copyFileFromAssets(Context context, String fileName,
			String path) {
		boolean copyIsFinish = false;
		try {
			InputStream is = context.getAssets().open(fileName);
			File file = new File(path);
			file.createNewFile();
			FileOutputStream fos = new FileOutputStream(file);
			byte[] temp = new byte[1024];
			int i = 0;
			while ((i = is.read(temp)) > 0) {
				fos.write(temp, 0, i);
			}
			fos.close();
			is.close();
			copyIsFinish = true;
		} catch (IOException e) {
			e.printStackTrace();
		}
		return copyIsFinish;
	}

	/**
	 * 获取打开WPS文档的 Intent
	 *
	 * @param path
	 * @return
	 */
	public static Intent getWpsIntent(File path) {
		String wpsPackageName = "cn.wps.moffice_eng";
		String selfPackageName = "cn.net.xinyi.xmjt.xsdoc";
		String wpsClassName = "cn.wps.moffice.documentmanager.PreStartActivity2";
		Intent intent = new Intent();
		Bundle bundle = new Bundle();
		// bundle.putString(OPEN_MODE, "ReadOnly");
		// bundle.putBoolean(SEND_CLOSE_BROAD, true);
		// bundle.putString(THIRD_PACKAGE, selfPackageName);
		bundle.putBoolean(CLEAR_BUFFER, true);
		bundle.putBoolean(CLEAR_TRACE, true);
		bundle.putBoolean(CLEAR_FILE, true);
		intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		intent.setAction(Intent.ACTION_VIEW);
		intent.setClassName(wpsPackageName, wpsClassName);

		Uri uri = Uri.fromFile(path);
		intent.setData(uri);
		intent.putExtras(bundle);
		return intent;
	}

	/**
	 * 获取更的存储节点
	 * 默认获取sd卡xinyi，如果没有，则放在data下
	 * @return
	 */
	public static String getRootSavePath(){
		String path;
		if (Environment.isExternalStorageEmulated()){
			path=Environment.getExternalStorageDirectory()+"XY";
		} else {
			path=Environment.getDataDirectory()+"'XY";
		}
		File file = new File(path);
		if (!file.exists()){
			file.mkdirs();
		}
		return path;
	}

	//判断文件是否存在
	public static boolean fileIsExists(String strFile)
	{
		try
		{
			File f=new File(strFile);
			if(!f.exists())
			{
				return false;
			}

		}
		catch (Exception e)
		{
			return false;
		}

		return true;
	}
}
