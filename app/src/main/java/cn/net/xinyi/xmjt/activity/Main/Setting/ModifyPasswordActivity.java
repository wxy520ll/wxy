package cn.net.xinyi.xmjt.activity.Main.Setting;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;

import com.alibaba.fastjson.JSONObject;
import com.loopj.android.http.AsyncHttpResponseHandler;

import org.apache.http.Header;

import cn.net.xinyi.xmjt.R;
import cn.net.xinyi.xmjt.activity.Main.LoginActivity;
import cn.net.xinyi.xmjt.api.ApiAsyncHttpClient;
import cn.net.xinyi.xmjt.api.ApiResource;
import cn.net.xinyi.xmjt.config.AppContext;
import cn.net.xinyi.xmjt.config.BaseActivity;
import cn.net.xinyi.xmjt.model.UserInfo;
import cn.net.xinyi.xmjt.utils.BaseUtil;
import cn.net.xinyi.xmjt.utils.XyEncryptioner;

/**
 * Created by mazhongwang on 14/11/26.
 */
public class ModifyPasswordActivity extends BaseActivity implements OnClickListener {
    private static final String TAG = "RegisterInfoActivity";
    private EditText etOldPwd;
    private EditText etPassword;
    private EditText etPasswordConfirm;
    private Button btnModify;
    private ProgressDialog mProgressDialog = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.update_password);
        initResource();

        getActionBar().setTitle(getResources().getString(R.string.title_update_pwd));
        getActionBar().setIcon(R.drawable.actionbar_back_icon_normal);
//        getActionBar().setDisplayHomeAsUpEnabled(true);
        getActionBar().setHomeButtonEnabled(true);
    }

    //屏幕触摸事件
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        InputMethodManager imm = (InputMethodManager) this.getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(this.getCurrentFocus().getWindowToken(), 0);
        return super.onTouchEvent(event);
    }

    private void initResource() {
        etOldPwd = (EditText) findViewById(R.id.edt_OldPwd);
        etPassword = (EditText) findViewById(R.id.edt_UsrPwd);
        etPasswordConfirm = (EditText) findViewById(R.id.edt_UsrPwdRe);

        btnModify = (Button) findViewById(R.id.btn_modify_pwd);
        btnModify.setOnClickListener(this);

    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_modify_pwd:
                modifyPasswd();
                break;
            default:
                break;
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                this.finish();
                break;
            default:
                break;
        }
        return true;
    }

    //联网服务器登录验证
    private void modifyPasswd() {

        String oldPwd = etOldPwd.getText().toString().trim();
        String passwd = etPassword.getText().toString().trim();
        String passwdConfirm = etPasswordConfirm.getText().toString().trim();
        String dialogMsg = "";

        String oldPwdEncrypt = XyEncryptioner.md5Encrypt(oldPwd).toUpperCase();
        UserInfo user = AppContext.instance.getLoginInfo();
        String username = user.getUsername();
        String savePassword = user.getPassword();

        if (oldPwd.isEmpty() || !oldPwdEncrypt.equalsIgnoreCase(savePassword)) {
            dialogMsg = getResources().getString(R.string.msg_old_pwd_error);
        } else if (passwd.length() < 6) {
            dialogMsg = "新密码长度必须大于六位";
        } else if (passwd.isEmpty() || passwd.isEmpty()) {
            dialogMsg = getResources().getString(R.string.msg_new_pwd_null);
        } else if (!passwd.equals(passwdConfirm)) {
            dialogMsg = getResources().getString(R.string.msg_new_pwd_confirm_error);
        }

        if (!dialogMsg.isEmpty()) {
            BaseUtil.showDialog(dialogMsg, this);
            return;
        }

        JSONObject jo = new JSONObject();
        jo.put("username", username);
        jo.put("password", passwd);
        String json = jo.toJSONString();
//        String json = "{username:"+username+",password:"+passwd+"}";

        //提交更改到服务器
        Message msg = new Message();
        msg.what = 0;
        handler.sendMessage(msg);

        ApiResource.resetUserPwd(json, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int i, Header[] headers, byte[] bytes) {
                try {
                    if (i == 200 && bytes != null) {
                        //获取返回值
                        String result = new String(bytes);
                        if (!result.equals("\"1\"")) {
                            Message msg = new Message();
                            msg.what = 2;
                            handler.sendMessage(msg);
                        } else {
                            //清除Cookie
                            ApiAsyncHttpClient.clearUserCookies(AppContext.instance());
                            //清除用户信息
                            AppContext.instance().cleanLoginInfo();

                            Message msg = new Message();
                            msg.what = 1;
                            handler.sendMessage(msg);
                        }
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
                Message msg = new Message();
                msg.what = 3;
                handler.sendMessage(msg);
            }
        });
    }

    Handler handler = new Handler() {
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 0:
                    mProgressDialog = new ProgressDialog(ModifyPasswordActivity.this);
                    mProgressDialog.setMessage("正在修改密码，请稍候...");
                    mProgressDialog.setIndeterminate(true);
                    mProgressDialog.setCancelable(true);
                    mProgressDialog.show();
                    break;
                case 1:
                    mProgressDialog.cancel();
                    new AlertDialog.Builder(ModifyPasswordActivity.this)
                            .setTitle("修改成功")
                            .setMessage("请重新登录")
                            .setPositiveButton(R.string.sure,
                                    new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog,
                                                            int which) {
                                            // 点击“确认”后的操作
                                            Intent intent = new Intent(ModifyPasswordActivity.this, LoginActivity.class);
                                            startActivity(intent);
                                            //MainMenuActivity.instance.finish();
                                            //SettingActivity.instance.finish();
                                            ModifyPasswordActivity.this.finish();
                                        }
                                    }).show();
                    break;
                case 2:
                    mProgressDialog.cancel();
                    BaseUtil.showDialog("密码修改失败", ModifyPasswordActivity.this);
                    break;
                case 3:
                    mProgressDialog.cancel();
                    BaseUtil.showDialog("操作超时，连接不上服务器，请检查你的网络", ModifyPasswordActivity.this);
                    break;
                default:
                    break;
            }
        }
    };

}
