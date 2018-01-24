package cn.net.xinyi.xmjt.activity.Main;


import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;

import org.apache.http.Header;
import org.apache.http.client.CookieStore;
import org.apache.http.client.protocol.ClientContext;
import org.apache.http.cookie.Cookie;
import org.apache.http.protocol.HttpContext;

import java.util.Date;
import java.util.List;

import cn.net.xinyi.xmjt.R;
import cn.net.xinyi.xmjt.api.ApiAsyncHttpClient;
import cn.net.xinyi.xmjt.api.ApiResource;
import cn.net.xinyi.xmjt.config.AppConfig;
import cn.net.xinyi.xmjt.config.AppContext;
import cn.net.xinyi.xmjt.config.BaseActivity2;
import cn.net.xinyi.xmjt.model.UserInfo;
import cn.net.xinyi.xmjt.utils.AnnotateManager;
import cn.net.xinyi.xmjt.utils.BaseDataUtils;
import cn.net.xinyi.xmjt.utils.BaseUtil;
import cn.net.xinyi.xmjt.utils.BindView;
import cn.net.xinyi.xmjt.utils.GeneralUtils;

public class LoginActivity extends BaseActivity2 implements OnClickListener {
    private static final String TAG = "LoginActivity";
    @BindView(id = R.id.btn_Login, click = true)
    private Button loginBtn;
    @BindView(id = R.id.btn_login_regist, click = true)
    private TextView registerBtn;
    @BindView(id = R.id.edt_UsrName)
    private EditText accountEdit;
    @BindView(id = R.id.edt_UsrPwd)
    private EditText passwordEdit;
    @BindView(id = R.id.btn_foeget_pw, click = true)
    private TextView btn_foeget_pw;
    private int networkType;
    static LoginActivity instance;

    private String permis;
    private SharedPreferences settings;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login);
        /***注解式绑定控件**/
        AnnotateManager.initBindView(this);
        instance = this;
        initResources();
    }

    private void initResources() {
        setTitle(R.string.app_name);
        //SharePreference初始化
        settings = this.getSharedPreferences("LoginTime", 0);
        //是否为记录最后一次登录号码
        if (null != settings.getString("UserPhone", "")) {
            //获取最后一次登录账号  将手机号码赋值登录名
            accountEdit.setText(settings.getString("UserPhone", ""));
        }
        //判断用户信息是否为空
        if (userInfo.getId() > 0) {
            AppContext.instance().cleanLoginInfo();//清除登录数据
        }
        // 本地注册-登录载入配置文件
        SharedPreferences sp = AppConfig.getSharedPreferences(this);
        if (sp != null && (userInfo == null || userInfo.getId() == 0)) {
            String localUserName = sp.getString("account", "");
            if (!localUserName.isEmpty()) {
                new AlertDialog.Builder(LoginActivity.this)
                        .setTitle("重要提示")
                        .setMessage("因系统升级，需要完善您的个人资料，升级后您本地保存的数据不受影响！")
                        .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                Intent intent = new Intent(LoginActivity.this, RegisterSmsActivity.class);
                                intent.setFlags(GeneralUtils.LoginActivity);
                                startActivity(intent);
                            }
                        }).show();
            }
        }
    }


    // 屏幕触摸事件
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        InputMethodManager imm = (InputMethodManager) this
                .getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(this.getCurrentFocus().getWindowToken(), 0);
        return super.onTouchEvent(event);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        stopLoading();
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            //忘记密码
            case R.id.btn_foeget_pw:
                Intent intent2 = new Intent(LoginActivity.this, ForgetPwSmsActivity.class);
                startActivity(intent2);
                break;

            case R.id.btn_Login:
                //检测是否连接网络
                networkType = ((AppContext) getApplication()).getNetworkType();
                if (networkType == 0) {
                    toast(getString(R.string.net_error));
                } else if (accountEdit.getText().toString().length() < 11) {
                    toast(getString(R.string.phone_input));
                    break;
                } else if (passwordEdit.getText().toString().isEmpty()) {
                    toast(getString(R.string.msg_reg_pwd_null));
                } else {
                    loginAsyn();
                }
                break;
            case R.id.btn_login_regist:
                Intent intent = new Intent(LoginActivity.this, RegisterSmsActivity.class);
                startActivity(intent);
                break;
        }
    }

    //联网服务器登录验证
    private void loginAsyn() {
        showLoadding("正在登录,请稍候...",LoginActivity.this);

        final String username = accountEdit.getText().toString().trim();
        final String pwd = passwordEdit.getText().toString().trim();
        final String deviceId = ((AppContext) getApplication()).getDeviceId();
        ApiResource.login(username, pwd, deviceId, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int i, Header[] headers, byte[] bytes) {
                try {
                    if (i == 200) {
                        //登录后，获取用户信息并保存
                        String result = new String(bytes);
                        JSONObject jo = JSON.parseObject(result);
                        //权限
                        JSONArray arr = JSON.parseArray(jo.getString("credentials"));
                        for (int j = 0; j < arr.size(); j++) {
                            if (null != arr.get(j) && arr.get(j).equals("admin")) {
                                permis = arr.get(j).toString();
                                break;
                            }
                        }
                        String userJson = jo.getString("model");

                        //获取角色
                        final JSONObject jsonObject = JSON.parseObject(userJson, JSONObject.class);
                        final List<JSONObject> role = JSON.parseArray(jsonObject.getString("role"), JSONObject.class);
                        String name = null;
                        if (role != null && role.size() > 0) {
                            name = role.get(0).getString("NAME");
                        }
                        UserInfo userInfo = JSON.parseObject(userJson, UserInfo.class);
                        userInfo.setIsremember("1");
                        userInfo.setRoleNmae(name);
                        AppContext.instance().saveLoginInfo(userInfo, permis);

                        //清除原来本地登录保存的数据
                        AppConfig.CleanCurrentConfig(getApplicationContext());

                        //保存cookie
                        AsyncHttpClient client = ApiAsyncHttpClient.getHttpClient();
                        HttpContext httpContext = client.getHttpContext();
                        CookieStore cookies = (CookieStore) httpContext
                                .getAttribute(ClientContext.COOKIE_STORE);
                        if (cookies != null) {
                            String tmpcookies = "";
                            for (Cookie c : cookies.getCookies()) {
                                tmpcookies += (c.getName() + "=" + c.getValue()) + ";";
                            }
                            AppContext.instance().setProperty("cookie", tmpcookies);
                            ApiAsyncHttpClient.setCookie(ApiAsyncHttpClient.getCookie(AppContext.instance()).substring(0, ApiAsyncHttpClient.getCookie(AppContext.instance()).length() - 1));
                        }

                        gotoMainMenu();
                    } else {
                        onFailure(i, headers, bytes, null);
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                    onFailure(i, headers, bytes, e);
                }
            }

            @Override
            public void onFailure(int i, Header[] headers, byte[] bytes, Throwable throwable) {
                stopLoading();
                BaseUtil.showDialog(bytes == null ? "网络异常请稍后！" : new String(bytes).toString(), LoginActivity.this);
            }
        });
    }

    private void gotoMainMenu() {


        SharedPreferences.Editor localEditor = settings.edit();
        localEditor.clear().commit();//清除历史记录
        localEditor.putLong("LastLoginTime", new Date(System.currentTimeMillis()).getTime());//记录登录时间
        localEditor.putString("UserPhone", accountEdit.getText().toString());//记录登录账号
        localEditor.commit();

        Intent intent = new Intent(this, cn.net.xinyi.xmjt.v527.presentation.MainActivity.class);
        if (null != userInfo.getPcs() && BaseDataUtils.isAdminCommit()) {
            intent.putExtra("admin", true);
        } else if (null != userInfo.getPcs()) {
            intent.putExtra("admin", false);
        }
        startActivity(intent);
        finish();
    }

    @Override
    public boolean enableBackUpBtn() {
        return false;
    }
}
