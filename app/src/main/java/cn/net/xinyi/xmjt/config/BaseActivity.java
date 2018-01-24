package cn.net.xinyi.xmjt.config;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.location.LocationManager;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.umeng.analytics.AnalyticsConfig;
import com.umeng.analytics.MobclickAgent;

import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import cn.net.xinyi.xmjt.R;
import cn.net.xinyi.xmjt.model.UserInfo;
import cn.net.xinyi.xmjt.utils.DB.ZDXXUtils;

public class BaseActivity extends Activity{


	protected Calendar c_start= Calendar.getInstance();
	protected Calendar c_end= Calendar.getInstance();
	protected SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
	protected DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	protected DecimalFormat formatString = new DecimalFormat("##0.00");//保留6位小数
	protected List<String> filePath = new ArrayList<String>();
	protected List<String> fileName = new ArrayList<String>();
	protected UserInfo userInfo;
	protected AlertDialog mDialog;
	protected ZDXXUtils zdUtils;



	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		initBase();
	}

	private void initBase() {
		MobclickAgent.setDebugMode(true);
		//添加Activity到堆栈
		AppManager.getAppManager().addActivity(this);
		//发送策略定义了用户由统计分析SDK产生的数据发送回友盟服务器的频率。
		MobclickAgent.updateOnlineConfig(this);
		/** 设置是否对日志信息进行加密, 默认false(不加密). */
		AnalyticsConfig.enableEncrypt(true);
		userInfo= AppContext.instance.getLoginInfo();
		/**字典类初始化**/
		zdUtils=new ZDXXUtils(this);

	}



	/**
	 * 显示加载中
	 */
	public void showLoadding(String loaddingtext,Context context){
		try {
			if (mDialog == null) {
				DialogInterface.OnKeyListener keyListener = new DialogInterface.OnKeyListener() {
					@Override
					public boolean onKey(DialogInterface dialog, int keyCode, KeyEvent event) {
						return keyCode == KeyEvent.KEYCODE_HOME || keyCode == KeyEvent.KEYCODE_SEARCH;
					}
				};
				View view = LayoutInflater.from(context).inflate(R.layout.loading_process_dialog_anim, null);
				TextView tv_text = (TextView) view.findViewById(R.id.tv_text);
				tv_text.setText(loaddingtext);
				mDialog = new AlertDialog.Builder(context).create();
				mDialog.setOnKeyListener(keyListener);
				mDialog.setCancelable(false);
				mDialog.show();
				mDialog.setContentView(view);
			}
			if (!mDialog.isShowing()) {
				mDialog.show();
			}
		}catch (NullPointerException n){
			n.printStackTrace();
		}
	}

	/**
	 * 显示加载中，默认文本
	 */
	public void showLoadding(){
		if (mDialog==null){
			DialogInterface.OnKeyListener keyListener = new DialogInterface.OnKeyListener() {
				@Override
				public boolean onKey(DialogInterface dialog, int keyCode, KeyEvent event) {
					return keyCode == KeyEvent.KEYCODE_HOME || keyCode == KeyEvent.KEYCODE_SEARCH;
				}
			};
			View view= LayoutInflater.from(this).inflate(R.layout.loading_process_dialog_anim,null);
			TextView tv_text=(TextView) view.findViewById(R.id.tv_text);
			tv_text.setText("正在加载中...");
			mDialog = new AlertDialog.Builder(this).create();
			mDialog.setCancelable(false);
			mDialog.setOnKeyListener(keyListener);
			mDialog.show();
			mDialog.setContentView(view);
		}
		if (!mDialog.isShowing()) {
			mDialog.show();
		}
	}

	/**
	 * 关闭加载对话框
	 */
	public void stopLoading(){
		if (mDialog!=null&&mDialog.isShowing()){
			mDialog.dismiss();
		}
		mDialog=null;
	}

	/***
	 * toast a message with short time
	 */
	public void toast(String msg){
		Toast.makeText(getApplicationContext(),msg,Toast.LENGTH_SHORT).show();
	}


	protected void showActivity(Class<?> cls) {
		Intent intent = new Intent();
		intent.setClass(this, cls);
		startActivity(intent);
	}

	protected void showActivity(Class<?> cls,int flag) {
		Intent intent = new Intent();
		intent.setClass(this, cls);
		intent.setFlags(flag);
		this.startActivity(intent);
	}

	protected void showToast(String text) {
		Toast.makeText(getApplicationContext(),text, Toast.LENGTH_LONG).show();
	}


	/**
	 * 判断GPS是否开启，GPS或者AGPS开启一个就认为是开启的
	 * @param context
	 * @return true 表示开启
	 */
	public static final boolean isOPen(final Context context) {
		LocationManager locationManager
				= (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);
		// 通过GPS卫星定位，定位级别可以精确到街（通过24颗卫星定位，在室外和空旷的地方定位准确、速度快）
		boolean gps = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
		// 通过WLAN或移动网络(3G/2G)确定的位置（也称作AGPS，辅助GPS定位。主要用于在室内或遮盖物（建筑群或茂密的深林等）密集的地方定位）
		boolean network = locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);
		return gps ;
	}


	public void isOpenGps(Activity aty) {
		LinearLayout container = new LinearLayout(aty);
		container.setOrientation(LinearLayout.VERTICAL);
		final ImageView imageView=new ImageView(aty);
		imageView.setImageResource(R.drawable.dr_gps);
		container.addView(imageView);

		new AlertDialog.Builder(aty)
				.setTitle("GPS开启提示：下拉菜单->选择【开关】->选择【GPS】")
				.setView(container)
				.setPositiveButton(R.string.sure, new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialogInterface, int i) {

					}
				})
				.setCancelable(false)
				.show();
	}

	/**
	 * 更新
	 * 删除
	 * 等操作  弹出框提示
	 * **/
	public static void setDialog(Activity aty,String mes) {
		Toast.makeText(aty,mes,Toast.LENGTH_LONG).show();
		/**设置onActivityResult resultCode**/
		aty.setResult(RESULT_OK);
		aty.finish();
	}

	public void onResume() {
		super.onResume();
		MobclickAgent.onResume(this);
	}


	public void onPause() {
		super.onPause();
		MobclickAgent.onPause(this);
	}

	@Override
	protected void onStop() {
		super.onStop();
		stopLoading();
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		onDoestroy1();
	}

	private void onDoestroy1() {
//		if (null!=UpdateManager.getUpdateManager().mProDialog){
//			UpdateManager.getUpdateManager().mProDialog.dismiss();
//		}
		AppManager.getAppManager().finishActivity(this);
		if (mDialog!=null&&mDialog.isShowing()){
			mDialog.dismiss();
		}
		mDialog=null;
	}



}
