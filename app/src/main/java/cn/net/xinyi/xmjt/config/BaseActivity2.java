package cn.net.xinyi.xmjt.config;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.MenuItem;
import android.widget.EditText;

import com.alibaba.fastjson.JSONObject;
import com.loopj.android.http.AsyncHttpResponseHandler;

import org.apache.http.Header;

import java.io.File;

import cn.net.xinyi.xmjt.R;
import cn.net.xinyi.xmjt.activity.Main.LoginActivity;
import cn.net.xinyi.xmjt.api.ApiAsyncHttpClient;
import cn.net.xinyi.xmjt.api.ApiResource;
import cn.net.xinyi.xmjt.utils.DialogHelper;


public abstract class BaseActivity2 extends BaseActivity {

	/***intent 用于拍照*/
	protected final int CAMERA_INTENT_REQUEST = 0XFF02;
	protected String imagePath;


	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		if (getActionBar()!=null){
			if (enableBackUpBtn()){
				getActionBar().setIcon(R.drawable.actionbar_back_icon_normal);
				getActionBar().setHomeButtonEnabled(true);
			}
			getActionBar().setTitle(getAtyTitle());
		}
		getSession();//获取当前登录的session,判断是否其他终端登录
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		if (android.R.id.home==item.getItemId()){
			finish();
			return true;
		}
		return super.onOptionsItemSelected(item);
	}


	@Override
	public AppContext getApplicationContext() {
		return (AppContext)super.getApplicationContext();
	}


	/**
	 * 打开新的activity
	 * @param clazz
	 */
	public void start(Class<?> clazz){
		Intent intent = new Intent(this,clazz);
		startActivity(intent);
	}


	/**
	 * 是否支持返回键
	 * @return
	 */
	public boolean enableBackUpBtn(){
		return false;
	}



	/**
	 * 标题
	 * @return
	 */
	public String getAtyTitle(){
		return getString(R.string.app_name);
	}

	public String getUsername(){
		return AppContext.instance().getLoginInfo().getUsername();
	}



	/**
	 * 手机拍照
	 */
	protected void cameraPhoto() {
		Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
		imagePath = ((AppContext) getApplication()).getStoreImagePath(0);
		File mFile = new File(imagePath);
		intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(mFile));
		startActivityForResult(intent, CAMERA_INTENT_REQUEST);
	}



	public  void checkTextLength(EditText et, final int length){
		et.addTextChangedListener(new TextWatcher() {
			@Override
			public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

			}

			@Override
			public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
				if (charSequence.length() == length){
					toast("您当前已经达到字数限制！");
				}
			}

			@Override
			public void afterTextChanged(Editable editable) {

			}
		});
	}


	/**
	 * 获取当前的activity
	 * @return
	 */
	public Activity getActivity(){
		return this;
	}





	/**
	 * USERNAME 获取当前用户的最新session
	 * LOGIN_TYPE  类型为手机
	 * **/
	private void getSession() {
		if (null!=userInfo&&null!= ApiAsyncHttpClient.getCookie(AppContext.instance())){
			JSONObject jsonObject=new JSONObject();
			jsonObject.put("USERNAME",userInfo.getUsername());
			jsonObject.put("LOGIN_TYPE","PHONE");
			String json=jsonObject.toJSONString();
			ApiResource.getSessionKey(json, new AsyncHttpResponseHandler() {
				@Override
				public void onSuccess(int i, Header[] headers, byte[] bytes) {
					String result = subMessage(new String(bytes));
					String cookie=ApiAsyncHttpClient.getCookie(AppContext.instance());
					if (i==200&&result.length()>4&&cookie!=null&&!cookie.substring(cookie.lastIndexOf("=")+1,cookie.length()-1).equals(result)){
						DialogHelper.showAlertDialog(getActivity(), "您的账号当前在其他手机登录，如非本人操作，则密码可能泄露，请至登录界面点击【忘记密码】进行修改！", new DialogHelper.OnOptionClickListener() {
							@Override
							public void onClick(DialogInterface dialog, int which, Object o) {
								lodingToLogin();
							}
						});
					}
				}
				@Override
				public void onFailure(int i, Header[] headers, byte[] bytes, Throwable throwable) {
				}
			});
		}
	}

	// string 返回时 是2个“”，需要进行截取
	private String subMessage(String text) {
		return  text.length() > 3 ? text.substring(1, text.length() - 1) : text;
	}

	// 转到登录
	private void lodingToLogin() {
		Intent intent = new Intent(this,LoginActivity.class);
		startActivity(intent);
		this.finish();
		AppManager.getAppManager().finishAllActivity(LoginActivity.class);
	}




}


