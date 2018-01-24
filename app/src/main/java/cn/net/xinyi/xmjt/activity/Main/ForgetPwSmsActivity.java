package cn.net.xinyi.xmjt.activity.Main;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONObject;

import cn.net.xinyi.xmjt.R;
import cn.net.xinyi.xmjt.config.AppContext;
import cn.net.xinyi.xmjt.config.BaseActivity2;
import cn.net.xinyi.xmjt.utils.StringUtils;
import cn.smssdk.EventHandler;
import cn.smssdk.SMSSDK;
import cn.smssdk.utils.SMSLog;


/**
 * Created by hao.zhou on 2015/10/12.
 * 修改密码短信验证
 */

public class ForgetPwSmsActivity extends BaseActivity2 implements View.OnClickListener,Handler.Callback{

    private TextView tv_yanzhenma;
    private EditText edt_phone;
    private TimeCount time;
    private EditText et_shuruyzm;
    private Button btn_next;
    static ForgetPwSmsActivity instance;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.sms_modify_info);
        instance=this;

        //mob注册查询
        SMSSDK.initSDK(this, RegisterSmsActivity.APPKEY, RegisterSmsActivity.APPSECRET);

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

        //手机号码-也作为用户名
        edt_phone = (EditText) findViewById(R.id.edt_phone);
        edt_phone.setText(AppContext.instance.getLoginInfo().getUsername());
        //点击获取验证码
        tv_yanzhenma = (TextView) findViewById(R.id.tv_yanzhenma);
        //输入验证码
        et_shuruyzm = (EditText) findViewById(R.id.et_shuruyzm);
        //下一步 跳转信息注册页面
        btn_next=(Button)findViewById(R.id.btn_next);
        //监听
        tv_yanzhenma.setOnClickListener(this);
        //下一步
        btn_next.setOnClickListener(this);


    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            //点击获取验证码
            case R.id.tv_yanzhenma:
                if (TextUtils.isEmpty(edt_phone.getText().toString().trim())) {
                    toast(getString(R.string.phone_not_null));
                } else if (edt_phone.getText().toString().trim().trim().length() != 11) {
                    toast(getString(R.string.phone_input));
                } else {
                    SMSSDK.getVerificationCode("86", edt_phone.getText().toString().trim());
                }
                break;

            //下一步
            case  R.id.btn_next:
                if (TextUtils.isEmpty(edt_phone.getText().toString().trim())||
                        !StringUtils.isMobileNO(edt_phone.getText().toString().trim().trim())) {
                    toast(getString(R.string.phone_input));
                } else if (TextUtils.isEmpty(et_shuruyzm.getText().toString().trim())) {
                    toast(getString(R.string.sms_not_null));
                } else if (!TextUtils.isEmpty(et_shuruyzm.getText().toString())) {
                    SMSSDK.submitVerificationCode("86", edt_phone.getText().toString(),
                            et_shuruyzm.getText().toString());
                }
                break;

        }

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
                Intent intent=new Intent(ForgetPwSmsActivity.this,ForgetPasswordActivity.class);
                intent.putExtra("edt_phone",edt_phone.getText().toString().trim());
                startActivity(intent);

            } else if (event == SMSSDK.EVENT_GET_VERIFICATION_CODE){
                time.start();
                toast(getString(R.string.sms_send_success));
            }else if (event ==SMSSDK.EVENT_GET_SUPPORTED_COUNTRIES){

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
                    Toast.makeText(ForgetPwSmsActivity.this, des, Toast.LENGTH_SHORT).show();
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
            // TODO Auto-generated constructor stub
            super(millisInFuture, countDownInterval);
        }

        @Override
        public void onFinish() {
            // TODO Auto-generated method stub
            tv_yanzhenma.setText("重新获取");
            tv_yanzhenma.setTextColor(Color.WHITE);
            tv_yanzhenma.setTextSize(15);
            tv_yanzhenma.setClickable(true);
        }

        @Override
        public void onTick(long millisUntilFinished) {
            // TODO Auto-generated method stub
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
        return  "短信验证";
    }

    @Override
    public boolean enableBackUpBtn() {
        return true;
    }
}