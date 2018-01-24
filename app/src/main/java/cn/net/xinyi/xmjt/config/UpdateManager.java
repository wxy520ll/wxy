package cn.net.xinyi.xmjt.config;

import android.app.AlertDialog.Builder;
import android.app.Dialog;
import android.app.DownloadManager;
import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.database.ContentObserver;
import android.net.Uri;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.fastjson.JSONObject;
import com.loopj.android.http.AsyncHttpResponseHandler;

import org.apache.http.Header;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.DecimalFormat;

import cn.net.xinyi.xmjt.R;
import cn.net.xinyi.xmjt.activity.Main.Setting.AboutActivity;
import cn.net.xinyi.xmjt.api.ApiResource;
import cn.net.xinyi.xmjt.model.Update;
import cn.net.xinyi.xmjt.utils.BaseUtil;
import cn.net.xinyi.xmjt.utils.UI;

/**
 * 应用程序更新工具包
 *
 * @author liux (http://my.oschina.net/liux)
 * @version 1.1
 * @created 2016-8-11
 */
public class UpdateManager {

	private static UpdateManager updateManager;
	public static final String  KEY_NAME_DOWNLOAD_ID = "downloadId";
	private static DownloadChangeObserver downloadObserver;
	private static CompleteReceiver completeReceiver;
	private static DownloadManager downloadManager;
	private static UpdateManagerPro downloadManagerPro;
	private static long   downloadId = 0;
	private static MyHandler  handler;
	public String  DOWNLOAD_FOLDER_NAME=null ;
	public String  DOWNLOAD_FILE_NAME =null ;
	private Context mContext;
	private TextView tv_message;
	private TextView tv_update_tips;
	private TextView tv_download_precent;
	private String apkName;
	private View v;


	public static UpdateManager getUpdateManager() {
		if (updateManager == null) {
			updateManager = new UpdateManager();
		}
		updateManager.interceptFlag = false;
		return updateManager;
	}

	/***
	 * 检查更新
	 * @param context
	 */
	public  void checkUpdate(final Context context) {
		handler = new MyHandler();
		mContext=context;
		//获取当前版本号
		final int curVersionCode =  BaseUtil.getVerCode(context,context.getPackageName());
		if (0==curVersionCode)
			return;
		new Thread(new Runnable() {
			@Override
			public void run() {
				//	Looper.prepare();
				ApiResource.getVersionByAppid(AppContext.APP_ID, new AsyncHttpResponseHandler() {
					@Override
					public void onSuccess(int i, Header[] headers, byte[] bytes) {
						String result = new String(bytes);
						if (result != null && result.trim() != "") {
							//获取服务器端版本号
							JSONObject jo_v = JSONObject.parseObject(result);
							mUpdate = new Update();
							mUpdate.setVersionName(jo_v.getString("RELEASEVERSION"));
							mUpdate.setVersionCode(Integer.parseInt(jo_v.getString("BUILDNUMBER")));
							mUpdate.setUpdateLog(jo_v.getString("RELEASEDESCRIPTION"));
							mUpdate.setDownloadUrl(AboutActivity.apkPath);
							if (mUpdate.getVersionCode() > curVersionCode) {
								//showDownloadDialog(mUpdate.getUpdateLog());
								Message msg=new Message();
								msg.what=10;
								handler.sendMessage(msg);
							}else{
								Message msg=new Message();
								msg.what=11;
								handler.sendMessage(msg);
							}
						} else {
							onFailure(i, headers, bytes, null);
						}
					}
					@Override
					public void onFailure(int i, Header[] headers, byte[] bytes, Throwable throwable) {
						Message msg=new Message();
						msg.what=12;
						handler.sendMessage(msg);
					}
				});
			}
		}).start();
		//	Looper.loop();
	}





	private  void showDownDialog(String updateMessage) {
		downloadManager = (DownloadManager)mContext.getSystemService(Context.DOWNLOAD_SERVICE);
		downloadManagerPro = new UpdateManagerPro(downloadManager);
		initView(updateMessage);
	}

	private  void initView(String updateMessage) {
		Builder builder = new Builder(mContext);
		final LayoutInflater inflater = LayoutInflater.from(mContext);
		v = inflater.inflate(R.layout.update_progress, null);
		mProgress = (ProgressBar) v.findViewById(R.id.update_progress);
		mProgressText = (TextView) v.findViewById(R.id.update_progress_text);
		tv_message = (TextView) v.findViewById(R.id.tv_message);
		tv_update_tips = (TextView) v.findViewById(R.id.tv_update_tips);
		tv_download_precent = (TextView) v.findViewById(R.id.download_precent);
		tv_update_tips.setText("龙岗协警助手"+mUpdate.getVersionName()+"版本更新内容如下：");
		tv_message.setText(updateMessage);
		builder.setView(v);
		downloadDialog = builder.create();
		downloadDialog.setCancelable(false);
		downloadDialog.setCanceledOnTouchOutside(false);
		downloadDialog.show();
		initData();
	}

	private  void initData() {
		downloadId = PreferencesUtils.getLong(mContext, KEY_NAME_DOWNLOAD_ID);
		//正在下载
		if (downloadManagerPro.getStatusById(downloadId) == DownloadManager.STATUS_RUNNING){
			return;
		}

		apkName = APPID + mUpdate.getVersionName() + ".apk";
		//判断是否挂载了SD卡
		String storageState = Environment.getExternalStorageState();
		if (storageState.equals(Environment.MEDIA_MOUNTED)) {
			DOWNLOAD_FOLDER_NAME = Environment.getExternalStorageDirectory().getAbsolutePath() + "/xmjt/update/";
			File file = new File(DOWNLOAD_FOLDER_NAME);
			if (!file.exists()) {
				file.mkdirs();
			}
			DOWNLOAD_FILE_NAME =DOWNLOAD_FOLDER_NAME+apkName;
		}

		//没有挂载SD卡，无法下载文件
		if (DOWNLOAD_FILE_NAME == null || DOWNLOAD_FILE_NAME == "") {
			mHandler.sendEmptyMessage(DOWN_NOSDCARD);
			return;
		}

		File ApkFile = new File(DOWNLOAD_FILE_NAME);
		if (ApkFile.exists()) { //是否已下载更新文件
			downloadDialog.dismiss();
			install(mContext,DOWNLOAD_FILE_NAME);
			return;
		}
		downloadObserver = new DownloadChangeObserver();
		completeReceiver = new CompleteReceiver();

		/** 监听下载成功 **/
		mContext.registerReceiver(completeReceiver, new IntentFilter(DownloadManager.ACTION_DOWNLOAD_COMPLETE));
		/** 监听下载进度 **/
		mContext.getContentResolver().registerContentObserver(UpdateManagerPro.CONTENT_URI, true, downloadObserver);
		updateView();
		initDownManage();
	}

	private void initDownManage() {
		DownloadManager.Request request = new DownloadManager.Request(Uri.parse(AboutActivity.apkPath));
		request.setDestinationInExternalPublicDir("/xmjt/update/",apkName);
		request.setTitle(mContext.getString(R.string.tips));
		request.setDescription(mContext.getString(R.string.app_name));
		//在下载过程中通知栏会一直显示该下载的Notification，在下载完成后该Notification会继续显示，直到用户点击该Notification或者消除该Notification
		//request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED);
		//在下载进行的过程中，通知栏中会一直显示该下载的Notification，当下载完成时，该Notification会被移除，这是默认的参数值
		request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE);
		request.setVisibleInDownloadsUi(false);
		request.setMimeType("application/cn.xinyi.download.file");
		downloadId = downloadManager.enqueue(request);
		/** save download id to preferences **/
		PreferencesUtils.putLong(mContext, KEY_NAME_DOWNLOAD_ID, downloadId);
	}


	static class DownloadChangeObserver extends ContentObserver {

		public DownloadChangeObserver() {
			super(handler);
		}

		@Override
		public void onChange(boolean selfChange) {
			updateView();
		}

	}


	class CompleteReceiver extends BroadcastReceiver {

		@Override
		public void onReceive(Context context, Intent intent) {

			long completeDownloadId = intent.getLongExtra(DownloadManager.EXTRA_DOWNLOAD_ID, -1);
			if (completeDownloadId == downloadId) {
				if (downloadManagerPro.getStatusById(downloadId) == DownloadManager.STATUS_SUCCESSFUL) {
					downloadDialog.dismiss();
					mContext.unregisterReceiver(completeReceiver);
					mContext.getContentResolver().unregisterContentObserver(downloadObserver);
					install(context,DOWNLOAD_FILE_NAME);
				}
			}
		}
	}

	/**
	 * 安装 app
	 *
	 * @param context
	 * @param filePath
	 * @return whether apk exist
	 */
	public static boolean install(Context context, String filePath) {
		Intent i = new Intent(Intent.ACTION_VIEW);
		File file = new File(filePath);
		if (file != null && file.length() > 0 && file.exists() && file.isFile()) {
			i.setDataAndType(Uri.parse("file://" + filePath), "application/vnd.android.package-archive");
			i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
			context.startActivity(i);
			return true;
		}
		return false;
	}

	//更细进度
	public static void updateView() {
		int[] bytesAndStatus = downloadManagerPro.getBytesAndStatus(downloadId);
		handler.sendMessage(handler.obtainMessage(0, bytesAndStatus[0], bytesAndStatus[1], bytesAndStatus[2]));
	}

	/**
	 * MyHandler
	 *
	 * @author Trinea 2016-8-11
	 */
	private  class MyHandler extends Handler {

		@Override
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
			switch (msg.what) {
				case 10:
					showDownDialog(mUpdate.getUpdateLog());
					break;
				case 11:
					UI.toast(mContext,"当前为最新版本");
					break;
				case 12:
					UI.toast(mContext,"更新失败");
					break;
				case 0:
					int status = (Integer)msg.obj;
					if (isDownloading(status)) {
						mProgress.setVisibility(View.VISIBLE);
						mProgress.setMax(0);
						mProgress.setProgress(0);
						tv_download_precent.setVisibility(View.VISIBLE);
						mProgress.setVisibility(View.VISIBLE);

						if (msg.arg2 < 0) {
							mProgress.setIndeterminate(true);
							tv_download_precent.setText("0%");
							mProgressText.setText("0M/0M");
						} else {
							mProgress.setIndeterminate(false);
							mProgress.setMax(msg.arg2);
							mProgress.setProgress(msg.arg1);
							tv_download_precent.setText(getNotiPercent(msg.arg1, msg.arg2));
							mProgressText.setText(getAppSize(msg.arg1) + "/" + getAppSize(msg.arg2));
						}
					} else {
						mProgress.setVisibility(View.GONE);
						mProgress.setMax(0);
						mProgress.setProgress(0);
						mProgressText.setVisibility(View.GONE);
						tv_download_precent.setVisibility(View.GONE);
					}
					break;
			}
		}
	}

	static final DecimalFormat DOUBLE_DECIMAL_FORMAT = new DecimalFormat("0.##");

	public static final int    MB_2_BYTE  = 1024 * 1024;
	public static final int    KB_2_BYTE    = 1024;

	/**
	 * @param size
	 * @return
	 */
	public static CharSequence getAppSize(long size) {
		if (size <= 0) {
			return "0M";
		}

		if (size >= MB_2_BYTE) {
			return new StringBuilder(16).append(DOUBLE_DECIMAL_FORMAT.format((double)size / MB_2_BYTE)).append("M");
		} else if (size >= KB_2_BYTE) {
			return new StringBuilder(16).append(DOUBLE_DECIMAL_FORMAT.format((double)size / KB_2_BYTE)).append("K");
		} else {
			return size + "B";
		}
	}


	public static String getNotiPercent(long progress, long max) {
		int rate = 0;
		if (progress <= 0 || max <= 0) {
			rate = 0;
		} else if (progress > max) {
			rate = 100;
		} else {
			rate = (int)((double)progress / max * 100);
		}
		return new StringBuilder(16).append(rate).append("%").toString();
	}

	public static boolean isDownloading(int downloadManagerStatus) {
		return downloadManagerStatus == DownloadManager.STATUS_RUNNING
				|| downloadManagerStatus == DownloadManager.STATUS_PAUSED
				|| downloadManagerStatus == DownloadManager.STATUS_PENDING;
	}

















	private final static String APPID = "xmjt";

	private static final int DOWN_NOSDCARD = 0;
	private static final int DOWN_UPDATE = 1;
	private static final int DOWN_OVER = 2;



	private Update mUpdate;

	//下载对话框
	private Dialog downloadDialog;
	//进度条
	private ProgressBar mProgress;
	//显示下载数值
	private TextView mProgressText;
	//查询动画
	public ProgressDialog mProDialog;
	//进度值
	private int progress;
	//下载线程
	private Thread downLoadThread;
	//终止标记
	private boolean interceptFlag;
	//提示语
	private String updateMsg = "";
	//返回的安装包url
	private String apkUrl = AboutActivity.apkPath;
	//下载包保存路径
	private String savePath = "";
	//apk保存完整路径
	private String apkFilePath = "";
	//临时下载文件路径
	private String tmpFilePath = "";
	//下载文件大小
	private String apkFileSize;
	//已下载文件大小
	private String tmpFileSize;



	private Handler mHandler = new Handler() {
		public void handleMessage(Message msg) {
			switch (msg.what) {
				case DOWN_UPDATE:
					mProgress.setProgress(progress);
					mProgressText.setText(tmpFileSize + "/" + apkFileSize);
					break;
				case DOWN_OVER:
					downloadDialog.dismiss();
					installApk();
					break;
				case DOWN_NOSDCARD:
					downloadDialog.dismiss();
					Toast.makeText(mContext, "无法下载安装文件，请检查SD卡是否挂载", Toast.LENGTH_LONG).show();
					break;
			}
		}

	};

	private Runnable mdownApkRunnable = new Runnable() {
		@Override
		public void run() {
			try {
				String apkName = APPID + mUpdate.getVersionName() + ".apk";
				String tmpApk = APPID + mUpdate.getVersionName() + ".tmp";
				//判断是否挂载了SD卡
				String storageState = Environment.getExternalStorageState();
				if (storageState.equals(Environment.MEDIA_MOUNTED)) {
					savePath = Environment.getExternalStorageDirectory().getAbsolutePath() + "/xmjt/update/";
					File file = new File(savePath);
					if (!file.exists()) {
						file.mkdirs();
					}
					apkFilePath = savePath + apkName;
					tmpFilePath = savePath + tmpApk;
				}

				//没有挂载SD卡，无法下载文件
				if (apkFilePath == null || apkFilePath == "") {
					mHandler.sendEmptyMessage(DOWN_NOSDCARD);
					return;
				}

				File ApkFile = new File(apkFilePath);

				//是否已下载更新文件
				if (ApkFile.exists()) {
					downloadDialog.dismiss();
					installApk();
					return;
				}

				//输出临时下载文件
				File tmpFile = new File(tmpFilePath);
				FileOutputStream fos = new FileOutputStream(tmpFile);

				URL url = new URL(apkUrl);
				HttpURLConnection conn = (HttpURLConnection) url.openConnection();
				conn.connect();
				int length = conn.getContentLength();
				InputStream is = conn.getInputStream();

				//显示文件大小格式：2个小数点显示
				DecimalFormat df = new DecimalFormat("0.00");
				//进度条下面显示的总文件大小
				apkFileSize = df.format((float) length / 1024 / 1024) + "MB";

				int count = 0;
				byte buf[] = new byte[15000];

				do {
					int numread = is.read(buf);
					count += numread;
					//进度条下面显示的当前下载文件大小
					tmpFileSize = df.format((float) count / 1024 / 1024) + "MB";
					//当前进度值
					progress = (int) (((float) count / length) * 100);
					//更新进度
					mHandler.sendEmptyMessage(DOWN_UPDATE);
					if (numread <= 0) {
						//下载完成 - 将临时下载文件转成APK文件
						if (tmpFile.renameTo(ApkFile)) {
							//通知安装
							mHandler.sendEmptyMessage(DOWN_OVER);
						}
						break;
					}
					fos.write(buf, 0, numread);
				} while (!interceptFlag);//点击取消就停止下载

				fos.close();
				is.close();
			} catch (MalformedURLException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}

		}
	};

	/**
	 * 下载apk
	 */
	private void downloadApk() {
		downLoadThread = new Thread(mdownApkRunnable);
		downLoadThread.start();
	}

	/**
	 * 安装apk
	 */
	private void installApk() {
		File apkfile = new File(apkFilePath);
		if (!apkfile.exists()) {
			return;
		}
		Intent i = new Intent(Intent.ACTION_VIEW);
		i.setDataAndType(Uri.parse("file://" + apkfile.toString()), "application/vnd.android.package-archive");
		mContext.startActivity(i);
	}
}
