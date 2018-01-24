package cn.net.xinyi.xmjt.activity.Main;


import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.loopj.android.http.AsyncHttpResponseHandler;

import org.apache.http.Header;
import org.json.JSONException;
import org.json.JSONObject;

import cn.net.xinyi.xmjt.R;
import cn.net.xinyi.xmjt.api.ApiResource;
import cn.net.xinyi.xmjt.config.AppConfig;
import cn.net.xinyi.xmjt.config.BaseActivity2;
import cn.net.xinyi.xmjt.utils.AnnotateManager;
import cn.net.xinyi.xmjt.utils.BaseUtil;
import cn.net.xinyi.xmjt.utils.BindView;
import cn.net.xinyi.xmjt.utils.GeneralUtils;
import cn.net.xinyi.xmjt.utils.StringUtils;
import cn.smssdk.EventHandler;
import cn.smssdk.SMSSDK;
import cn.smssdk.utils.SMSLog;


/**
 * Created by hao.zhou on 2015/10/12.
 * 注册短信通知
 */


public class RegisterSmsActivity extends BaseActivity2 implements View.OnClickListener,Handler.Callback {

    // 填写从短信SDK应用后台注册得到的APPKEY
    public static String APPKEY = "afbe13be005b";
    // 填写从短信SDK应用后台注册得到的APPSECRET
    public static String APPSECRET = "81cb57cf75c0f7a41b97a93f715f2738";
    //点击获取短信验证码
    @BindView(id = R.id.tv_yanzhenma, click = true)
    private TextView tv_yanzhenma;
    //手机号码-也作为用户名
    @BindView(id = R.id.edt_phone)
    private EditText edt_phone;
    //输入验证码
    @BindView(id = R.id.et_shuruyzm)
    private EditText et_shuruyzm;
    //下一步
    @BindView(id = R.id.btn_next, click = true)
    private Button btn_next;
    private TimeCount time;
    private TextView tv_tips_info;
    public static RegisterSmsActivity instance;
    //选择人员身份
    @BindView(id = R.id.ll_rysf)
    private LinearLayout ll_rysf;
    //选择警务人员
    @BindView(id = R.id.ll_police, click = true)
    private LinearLayout ll_police;
    //选择公司
    @BindView(id = R.id.ll_company, click = true)
    private LinearLayout ll_company;
    //短信验证-默认Gone
    @BindView(id = R.id.ll_dxyz)
    private LinearLayout ll_dxyz;
    int registType = 0;
    private int isRegester;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.sms_register);
        AnnotateManager.initBindView(this);//注解式绑定控件
        instance = this;
        initView();
    }

    private void initView() {
        //mob注册
        SMSSDK.initSDK(this, APPKEY, APPSECRET);
        try {
            final Handler handler = new Handler(this);
            EventHandler eventHandler = new EventHandler() {
                public void afterEvent(int event, int result, Object data) {
                    Message msg = new Message();
                    msg.arg1 = event;
                    msg.arg2 = result;
                    msg.obj = data;
                    handler.sendMessage(msg);
                }
            };

            SMSSDK.registerEventHandler(eventHandler);
        } catch (Exception e) {
            e.printStackTrace();
        }


        //验证码倒计时
        time = new TimeCount(90000, 1000);

        //本地账号升级
        if (getIntent().getFlags() == GeneralUtils.LoginActivity) {
            //本地注册-提示
            tv_tips_info = (TextView) findViewById(R.id.tv_tips_info);
            tv_tips_info.setText("2.完善个人资料");
            // 本地注册-登录载入配置文件
            SharedPreferences sp = AppConfig.getSharedPreferences(this);
            String localUserName = sp.getString("account", "");
            if (localUserName.length() > 7) {
                edt_phone.setText(localUserName);
            } else {
                BaseUtil.showDialog("因系统升级，警号注册用户统一更改为手机号码注册", RegisterSmsActivity.this);
            }
        }
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.ll_police:
                registType = 1;
                ll_rysf.setVisibility(View.GONE);
                ll_dxyz.setVisibility(View.VISIBLE);
                break;

            case R.id.ll_company:
                registType = 2;
                ll_rysf.setVisibility(View.GONE);
                ll_dxyz.setVisibility(View.VISIBLE);
                break;

            //点击获取验证码
            case R.id.tv_yanzhenma:
                if (TextUtils.isEmpty(edt_phone.getText().toString().trim())) {
                    toast(getString(R.string.phone_not_null));
                } else if (edt_phone.getText().toString().trim().trim().length() != 11) {
                    toast(getString(R.string.phone_input));
                } else {
                    if (registType == 1) {//警务人员需要验证手机号码
                        requestData();
                    } else {
                        SMSSDK.getVerificationCode("86", edt_phone.getText().toString().trim());
                    }
                }
                break;

            //下一步
            case R.id.btn_next:
                if (TextUtils.isEmpty(edt_phone.getText().toString().trim()) ||
                        !StringUtils.isMobileNO(edt_phone.getText().toString().trim().trim())) {
                    toast(getString(R.string.phone_input));
                } else if (TextUtils.isEmpty(et_shuruyzm.getText().toString().trim())) {
                    toast(getString(R.string.sms_not_null));
                } else if (!TextUtils.isEmpty(et_shuruyzm.getText().toString())) {
                    SMSSDK.submitVerificationCode("86", edt_phone.getText().toString(),
                            et_shuruyzm.getText().toString());
                } else {
                    requestData();
                }
                break;

        }
    }


    private void requestData() {
        showLoadding();
        JSONObject jo = new JSONObject();
        try {
            jo.put("SJHM", edt_phone.getText().toString());
        } catch (JSONException e) {
            e.printStackTrace();
        }
        String json = jo.toString();
        ApiResource.queryTXL(json, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int i, Header[] headers, byte[] bytes) {
                String result = new String(bytes);
                if (!result.equals("[]")) {
                    isRegester = 0;
                    SMSSDK.getVerificationCode("86", edt_phone.getText().toString().trim());
                } else {
                    onFailure(i,headers,bytes,null);
//                    isRegester = 1;
//                    SMSSDK.getVerificationCode("86", edt_phone.getText().toString().trim());
                }
                stopLoading();
            }

            @Override
            public void onFailure(int i, Header[] headers, byte[] bytes, Throwable throwable) {
                stopLoading();
                if (StringUtils.isEmpty(new String(bytes))) {
                    BaseUtil.showDialog(getString(R.string.regeist_not_per), RegisterSmsActivity.this);
                }else {
                    BaseUtil.showDialog(new String(bytes), RegisterSmsActivity.this);
                }
            }
        });
    }




    @Override
    public boolean handleMessage(Message msg) {

        int event = msg.arg1;
        int result = msg.arg2;
        Object data = msg.obj;
        if (result == SMSSDK.RESULT_COMPLETE) {
            //短信注册成功后，返回MainActivity,然后提示新好友
            if (event == SMSSDK.EVENT_SUBMIT_VERIFICATION_CODE) {//提交验证码成功
                toast(getString(R.string.sms_submit_success));
                Intent intent = null;
                if (registType == 1) {//警务人员
                    intent = new Intent(RegisterSmsActivity.this, RegisterInfoActivity.class);
                    intent.putExtra("isRegester", isRegester);
                } else if (registType == 2) {//第三方公司
                    intent = new Intent(RegisterSmsActivity.this, RegistForComanyAty.class);
                }
                intent.putExtra("edt_phone", edt_phone.getText().toString().trim());
                if (getIntent().getFlags() == GeneralUtils.LoginActivity) {
                    intent.setFlags(GeneralUtils.LoginActivity);
                }
                startActivity(intent);
            } else if (event == SMSSDK.EVENT_GET_VERIFICATION_CODE) {
                time.start();
                toast(getString(R.string.sms_send_success));
            }else if (event ==SMSSDK.EVENT_GET_SUPPORTED_COUNTRIES){
                //����֧�ַ�����֤��Ĺ����б�
            }

        }else{
            int status = 0;
            try {
                ((Throwable) data).printStackTrace();
                Throwable throwable = (Throwable) data;

                JSONObject object = new JSONObject(throwable.getMessage());
                String des = object.optString("detail");
                status = object.optInt("status");
                if (!TextUtils.isEmpty(des)) {
                    Toast.makeText(RegisterSmsActivity.this, des, Toast.LENGTH_SHORT).show();
                    return false;
                }
            } catch (Exception e) {
                SMSLog.getInstance().w(e);
            }

        }
        return false;
    }


    // 倒计时  用于验证时间
    class TimeCount extends CountDownTimer {
        public TimeCount(long millisInFuture, long countDownInterval) {
            super(millisInFuture, countDownInterval);
        }

        @Override
        public void onFinish() {
            tv_yanzhenma.setText("重新获取");
            tv_yanzhenma.setTextColor(Color.WHITE);
            tv_yanzhenma.setTextSize(15);
            tv_yanzhenma.setClickable(true);
        }

        @Override
        public void onTick(long millisUntilFinished) {
            tv_yanzhenma.setClickable(false);
            tv_yanzhenma.setTextColor(Color.RED);
            tv_yanzhenma.setTextSize(15);
            tv_yanzhenma.setText(millisUntilFinished / 1000 + "秒");
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        SMSSDK.unregisterAllEventHandler();
    }


    @Override
    public String getAtyTitle() {
        return "短信验证";
    }

    @Override
    public boolean enableBackUpBtn() {
        return true;
    }
}