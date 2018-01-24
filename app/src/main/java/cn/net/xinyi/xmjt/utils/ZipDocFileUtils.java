package cn.net.xinyi.xmjt.utils;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;

import net.lingala.zip4j.core.ZipFile;
import net.lingala.zip4j.exception.ZipException;
import net.lingala.zip4j.io.ZipInputStream;
import net.lingala.zip4j.model.FileHeader;
import net.lingala.zip4j.unzip.UnzipUtil;

import org.json.JSONArray;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import cn.net.xinyi.xmjt.model.FileInfoModle;

public class ZipDocFileUtils {

	public static final String CATEGORY_DOC = "文档库";
	public static final String CATEGORY_VIDEO = "视频库";
	public static final String CATEGORY_IMAGE = "图片库";
	public static final String OPEN_MODE = "OpenMode";
	public static final String SEND_CLOSE_BROAD = "SendCloseBroad";
	public static final String THIRD_PACKAGE = "ThirdPackage";
	public static final String CLEAR_BUFFER = "ClearBuffer";
	public static final String CLEAR_TRACE = "ClearTrace";
	public static final String CLEAR_FILE = "ClearFile";

	public static JSONArray root = new JSONArray();
	public static List fileHeaderList;
	public static ZipFile zipFile;
	private static final int BUFF_SIZE = 4096;
	private static final String TAG = "FileUtils";

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

	// 根据文件分类获取该分类的文件夹根目录
	public static String getFileDirByCategory(int category) {
		String docsRootDir = ZipDocFileUtils.getDocFileDirPath();
		String childDir = "docs";

		switch (category) {
		case -1:
			break;
		case 0:
			break;
		case 1:
			childDir = "docs/" + CATEGORY_DOC;
			break;
		case 2:
			childDir = "docs/" + CATEGORY_IMAGE;
			break;
		case 3:
			childDir = "docs/" + CATEGORY_VIDEO;
			break;

		}
		return childDir;

	}

	/**
	 * 根据文件夹获取文件列表（包括子文件夹）
	 *
	 * @param dirPath
	 * @return
	 */
	public static List<FileInfoModle> getFileListByDir(String dirPath) {

		if (!dirPath.endsWith("/"))
			dirPath += "/";

		// 对文件路径中的括号进行处理
		dirPath = dirPath.replace("(", "\\(");
		dirPath = dirPath.replace(")", "\\)");

		Log.d(TAG, "current dirPath:" + dirPath);

		List<FileInfoModle> fileList = new ArrayList<FileInfoModle>();

		if (fileHeaderList == null)
			return null;

		for (int i = 0; i < fileHeaderList.size(); i++) {
			FileHeader fileHeader = (FileHeader) fileHeaderList.get(i);
			String filePath = fileHeader.getFileName();

			// 获取当前目录下的子目录名及文件名，不包括子目下的文件；
			String reg = "^" + dirPath + "+((?!/).)+(/?)$";
			Pattern pattern = Pattern.compile(reg);
			Matcher matcher = pattern.matcher(filePath);
			if (matcher.find()) {
				boolean isDir = fileHeader.isDirectory();
				long fileSize = fileHeader.getUncompressedSize();
				int lastModFileTime = fileHeader.getLastModFileTime();
				int count = 0;

				FileInfoModle fileInfo = new FileInfoModle();
				fileInfo.fileName = filePath.replaceFirst(dirPath, "")
						.replaceAll("/", "");
				fileInfo.fileSize = fileSize;
				fileInfo.filePath = filePath;
				fileInfo.isDir = isDir;
				fileInfo.count = count;
				fileInfo.modifiedDate = lastModFileTime;
				fileList.add(fileInfo);
			}
		}

		Collections.sort(fileList, new FileInfoCmp());
		return fileList;
	}

	public static List<FileInfoModle> getZipFileList(String dirPath, String queryName) {

		if (dirPath.endsWith("/"))
			dirPath = dirPath.substring(0, dirPath.length() - 1);
		// 对文件路径中的括号进行处理
		dirPath = dirPath.replace("(", "\\(");
		dirPath = dirPath.replace(")", "\\)");

		Log.d(TAG, "current dirPath:" + dirPath);

		List<FileInfoModle> fileList = new ArrayList<FileInfoModle>();

		if (fileHeaderList == null)
			return null;

		for (int i = 0; i < fileHeaderList.size(); i++) {
			FileHeader fileHeader = (FileHeader) fileHeaderList.get(i);
			String filePath = fileHeader.getFileName();

			// 获取当前目录下的子目录名及文件名，不包括子目下的文件；
			String reg = "^" + dirPath + ".*/((?!/).)+$";
			Pattern pattern = Pattern.compile(reg);
			Matcher matcher = pattern.matcher(filePath);
			if (matcher.find()) {
				String fileName = filePath
						.substring(filePath.lastIndexOf("/") + 1);
				boolean isDir = fileHeader.isDirectory();
				if ((!isDir)
						&& fileName.toUpperCase().contains(
								queryName.trim().toUpperCase())) {
					long fileSize = fileHeader.getUncompressedSize();
					int lastModFileTime = fileHeader.getLastModFileTime();
					FileInfoModle fileInfo = new FileInfoModle();
					fileInfo.fileName = fileName;
					fileInfo.fileSize = fileSize;
					fileInfo.filePath = filePath;
					fileInfo.isDir = isDir;
					fileInfo.count = 0;
					fileInfo.modifiedDate = lastModFileTime;
					fileList.add(fileInfo);
				}
			}
		}
		Collections.sort(fileList, new FileInfoCmp());
		return fileList;
	}

	/**
	 * 初始化压缩文件，并获取文件列表；
	 *
	 * @param mZipFilePath
	 * @param mEncrypt
	 */
	public static boolean decodeZipFile(String mZipFilePath, String mEncrypt) {
		boolean isOK = false;
		try {

			zipFile = new ZipFile(mZipFilePath);

			zipFile.setFileNameCharset("GBK");

			if (zipFile.isEncrypted()) {
				zipFile.setPassword(mEncrypt);
			}

			fileHeaderList = zipFile.getFileHeaders();
			if (fileHeaderList == null || fileHeaderList.size() == 0)
				isOK = false;

			for (int i = 0; i < fileHeaderList.size(); i++) {
				FileHeader mFileHeader = (FileHeader) fileHeaderList.get(i);
				if (!mFileHeader.isDirectory()) {
					String filePath = mFileHeader.getFileName();
					// 测试解压密码是否正确；
					String tmpFilePath = ZipDocFileUtils
							.ExtractSelectFilesWithInputStream(filePath);
					if (!tmpFilePath.trim().isEmpty()) {
						isOK = true;
						ZipDocFileUtils.cleanTmpFile();
						break;
					}
				}
			}

		} catch (ZipException e) {
			e.printStackTrace();
		}

		return isOK;
	}

	/**
	 * 解压临时文件
	 *
	 * @param filePath
	 * @return
	 */
	public static String ExtractSelectFilesWithInputStream(String filePath) {
		// 首先清除所有文件
		// cleanTmpFile();

		String fileName = getFileName(filePath);
		String tempFilePath = getTmpDocFileDirPath() + "/" + fileName;

		try {

			ZipInputStream is = null;
			OutputStream os = null;

			FileHeader fileHeader = zipFile.getFileHeader(filePath);

			if (fileHeader != null) {

				// Build the output file
				String outFilePath = tempFilePath;

				File outFile = new File(outFilePath);

				// Checks if the file is a directory
				if (fileHeader.isDirectory()) {
					// This functionality is up to your requirements
					// For now I create the directory
					outFile.mkdirs();
					return "";
				}

				// Check if the directories(including parent directories)
				// in the output file path exists
				File parentDir = outFile.getParentFile();
				if (!parentDir.exists()) {
					parentDir.mkdirs(); // If not create those directories
				}

				// Get the InputStream from the ZipFile
				is = zipFile.getInputStream(fileHeader);
				// Initialize the output stream
				os = new FileOutputStream(outFile);

				int readLen = -1;
				byte[] buff = new byte[BUFF_SIZE];

				// Loop until End of File and write the contents to the output
				// stream
				while ((readLen = is.read(buff)) != -1) {
					os.write(buff, 0, readLen);
				}

				// Closing inputstream also checks for CRC of the the just
				// extracted file.
				// If CRC check has to be skipped (for ex: to cancel the unzip
				// operation, etc)
				// use method is.close(boolean skipCRCCheck) and set the flag,
				// skipCRCCheck to false
				// NOTE: It is recommended to close outputStream first because
				// Zip4j throws
				// an exception if CRC check fails
				is.close();

				// Close output stream
				os.close();

				// To restore File attributes (ex: last modified file time,
				// read only flag, etc) of the extracted file, a utility class
				// can be used as shown below
				UnzipUtil.applyFileAttributes(fileHeader, outFile);

				System.out.println("Done extracting: "
						+ fileHeader.getFileName());
			} else {
				System.err.println("FileHeader does not exist");
				return "";
			}

		} catch (Exception e) {
			e.printStackTrace();
			return "";
		}

		return tempFilePath;

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
	 * 文件排序
	 * 
	 * @author mazhongwang
	 * 
	 */
	public static final class FileInfoCmp implements Comparator<FileInfoModle> {

		@Override
		public int compare(FileInfoModle file1, FileInfoModle file2) {
			String name1 = file1.fileName;
			String name2 = file2.fileName;
			int filetype1 = file1.isDir ? 1 : 0;
			int filetype2 = file2.isDir ? 1 : 0;
			int result = 1;
			if (filetype1 > filetype2) {
				result = -1;
			} else if(filetype1 == filetype2){
				if (name1.compareTo(name2) <= 0) {
					result = -1;
				}else{
					result = 1;
				}
			}
			
			return result;
		}

	}
}
