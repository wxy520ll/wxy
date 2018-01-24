package cn.net.xinyi.xmjt.activity.Main;


import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.baidu.mapapi.map.offline.MKOfflineMap;
import com.baidu.mapapi.map.offline.MKOfflineMapListener;
import com.j256.ormlite.dao.Dao;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.umeng.analytics.MobclickAgent;

import org.apache.http.Header;

import java.sql.SQLException;
import java.util.Date;
import java.util.List;

import cn.net.xinyi.xmjt.R;
import cn.net.xinyi.xmjt.api.ApiResource;
import cn.net.xinyi.xmjt.config.AppConfig;
import cn.net.xinyi.xmjt.config.AppContext;
import cn.net.xinyi.xmjt.config.BaseActivity;
import cn.net.xinyi.xmjt.model.Zdxx;
import cn.net.xinyi.xmjt.utils.BaseDataUtils;
import cn.net.xinyi.xmjt.utils.BaseUtil;
import cn.net.xinyi.xmjt.utils.DB.DBHelperNew;
import cn.net.xinyi.xmjt.utils.UI;

public class AppStart extends BaseActivity implements MKOfflineMapListener {
	private View view;
	private MKOfflineMap mOffline = null;
	private MyHandler handler;
	private List<Zdxx> listZdxx;
	private int zdxxVerson;
	private int zdxxSize;
	private Dao<Zdxx, Integer> ZdxxDao;


	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		view = View.inflate(this, R.layout.start, null);
		this.setContentView(view);
		ititBaiDuMap();	//启动时导入离线地图
		initUMeng();//初始化友盟统计
		initAnimation();//过度界面动画
		TextView tv_version=(TextView)findViewById(R.id.tv_version);
		tv_version.setText("版本："+ BaseUtil.getVerName(this,getPackageName()));
	}

	/***
	 * 检查更新
	 */
	public  void updateZdxx() {
		//初始化zdxxDao类
		try {
			ZdxxDao= DBHelperNew.getInstance(AppStart.this).getZdxxDao();
			zdxxSize=ZdxxDao.queryForAll().size();//字典信息的大小
		} catch (SQLException e) {
			e.printStackTrace();
		}
		handler = new MyHandler();
		new Thread(new Runnable() {
			@Override
			public void run() {
				ApiResource.getVersionByAppid(AppContext.APP_ID, new AsyncHttpResponseHandler() {
					@Override
					public void onSuccess(int i, Header[] headers, byte[] bytes) {
						String result = new String(bytes);
						if (result != null && result.trim() != "") {
							//获取服务器端版本号
							JSONObject jo_v = JSONObject.parseObject(result);
							//获取现在的字典信息版本
							zdxxVerson=Integer.parseInt(jo_v.getString("ZDVERSION"));
							//获取最后一次更新的版本
							String s=AppContext.instance().getProperty(AppConfig.ZDINFOS_UPDATE_TIME);
							//最后一次更新 string 转换成 int
							int lastUpateTime = s==null?0:Integer.parseInt(s);
							if (zdxxSize==0||zdxxVerson > lastUpateTime) {
								Message msg=new Message();
								msg.what=10;
								msg.arg1=zdxxVerson;
								handler.sendMessage(msg);
							}else {
								onFailure(i, headers, bytes, null);
							}
						} else {
							onFailure(i, headers, bytes, null);
						}
					}
					@Override
					public void onFailure(int i, Header[] headers, byte[] bytes, Throwable throwable) {
						Message msg=new Message();
						msg.what=11;
						handler.sendMessage(msg);
					}
				});
			}
		}).start();
	}


	//更新字典信息
	private void updateZdxxDb() {
		//联网获取增量更新
		ApiResource.getAllZdxx(new AsyncHttpResponseHandler() {
			@Override
			public void onSuccess(int i, Header[] headers, byte[] bytes) {
				String json = new String(bytes);
				listZdxx = JSON.parseArray(json,Zdxx.class);
				if (listZdxx.size() > 0) {
					Message msg=new Message();
					msg.what=12;
					handler.sendMessage(msg);
				}else {
					onFailure(i, headers, bytes, null);
				}
			}

			@Override
			public void onFailure(int i, Header[] headers, byte[] bytes, Throwable throwable) {
				Message msg=new Message();
				msg.what=11;
				handler.sendMessage(msg);
			}
		});
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
					showLoadding("正在加载基础数据信息...",AppStart.this);
					updateZdxxDb();
					break;

				case 11:
					stopLoading();
					redirectTo();
					break;

				case 12:
					try {
						ZdxxDao.queryRaw("delete from 'table_zdxx'");//删除原来的数据
						ZdxxDao.create(listZdxx);//更新现在数据
						AppContext.instance().setProperty(AppConfig.ZDINFOS_UPDATE_TIME,""+zdxxVerson);//更新时间
					} catch (SQLException e) {
						e.printStackTrace();
					}
					UI.toast(AppStart.this,"共更新"+listZdxx.size()+"条数据");
					redirectTo();
					stopLoading();
					break;
			}
		}
	}



	private void ititBaiDuMap() {
		mOffline = new MKOfflineMap();
		mOffline.init(this);
		importFromSDCard();
	}

	private void initUMeng() {
		//共计三步，统计fragment第一步，下一步在TabMenuActivity.java：因为本app包含fragment，要统计fragment，所以需要加这一句话  add20160819
		MobclickAgent.openActivityDurationTrack(false);
	}
	private void initAnimation() {
		//首页渐变动画界面
		AlphaAnimation aa = new AlphaAnimation(0.3f,1.0f);
		aa.setDuration(1000);
		view.setAnimation(aa);

		aa.setAnimationListener(new AnimationListener(){
			@Override
			public void onAnimationEnd(Animation animation) {
				updateZdxx();
			}
			@Override
			public void onAnimationRepeat(Animation animation) {}

			@Override
			public void onAnimationStart(Animation animation) {

			}
		});
	}

	/**
	 * 跳转到首页
	 */
	private void redirectTo(){
		//获取本地存储 最后一次登录时间
		SharedPreferences settings = this.getSharedPreferences("LoginTime", 0);
		long historyTime = settings.getLong("LastLoginTime", 0);
		/** 需要重新输入密码
		 *用户信息为空*
		 *用户上一次登录时间大于现在时间
		 *最后一次登录时间超过 24小时内
		 */
		if (userInfo.getId() <= 0 || new Date(System.currentTimeMillis()).getTime() < historyTime ||
				new Date(System.currentTimeMillis()).getTime() - historyTime > 1000 * 60 * 60 * 24){
			lodingToLogin();
		}else {
			lodingToMainMenu();
		}
	}

	// 转到登录
	private void lodingToLogin() {
		Intent intent = new Intent(this, LoginActivity.class);
		startActivity(intent);
		this.finish();
	}

	// 转到主菜单
	private void lodingToMainMenu() {
		Intent intent = new Intent(this, cn.net.xinyi.xmjt.v527.presentation.MainActivity .class);
		//Intent intent = new Intent(this, TabMenuActivity .class);
		if (null!=userInfo.getPcs()&& BaseDataUtils.isAdminCommit()) {
			intent.putExtra("admin", true);
		} else {
			intent.putExtra("admin", false);
		}
		startActivity(intent);
		this.finish();
	}


	/**
	 * 从SD卡导入离线地图安装包
	 */
	public void importFromSDCard() {
		int num = mOffline.importOfflineData();
		String msg = "";
		if (num == 0) {
			msg = "没有导入离线包，这可能是离线包放置位置不正确，或离线包已经导入过";
		} else {
			msg = String.format("成功导入 %d 个离线地图包，可以在离线地图管理界面查看", num);
			Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
		}
	}

	@Override
	public void onGetOfflineMapState(int type, int state) {
		switch (type) {
			case MKOfflineMap.TYPE_DOWNLOAD_UPDATE:
				break;
			case MKOfflineMap.TYPE_NEW_OFFLINE:
				// 有新离线地图安装
				Log.d("OfflineDemo", String.format("add offlinemap num:%d", state));
				break;
			case MKOfflineMap.TYPE_VER_UPDATE:
				// 版本更新提示
				// MKOLUpdateElement e = mOffline.getUpdateInfo(state);
				break;
		}
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		stopLoading();
	}

//	public void showRoundProcessDialog() {
//		DialogInterface.OnKeyListener keyListener = new DialogInterface.OnKeyListener() {
//			@Override
//			public boolean onKey(DialogInterface dialog, int keyCode, KeyEvent event) {
//				if (keyCode == KeyEvent.KEYCODE_HOME || keyCode == KeyEvent.KEYCODE_SEARCH) {
//					return true;
//				}
//				return false;
//			}
//		};
//		View view=LayoutInflater.from(this).inflate(R.layout.loading_process_dialog_anim,null);
//		TextView tv_text=(TextView) view.findViewById(R.id.tv_text);
//		tv_text.setText("正在加载中...");
//		mDialog = new AlertDialog.Builder(this).create();
//		mDialog.setOnKeyListener(keyListener);
//		mDialog.show();
//		mDialog.setContentView(view);
//	}
}
