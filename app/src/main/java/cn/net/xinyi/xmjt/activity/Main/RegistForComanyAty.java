package cn.net.xinyi.xmjt.activity.Main;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.loopj.android.http.AsyncHttpResponseHandler;

import org.apache.http.Header;

import java.io.File;

import cn.net.xinyi.xmjt.R;
import cn.net.xinyi.xmjt.api.ApiResource;
import cn.net.xinyi.xmjt.config.AppContext;
import cn.net.xinyi.xmjt.config.BaseActivity2;
import cn.net.xinyi.xmjt.model.UserInfo;
import cn.net.xinyi.xmjt.utils.AnnotateManager;
import cn.net.xinyi.xmjt.utils.BaseDataUtils;
import cn.net.xinyi.xmjt.utils.BaseUtil;
import cn.net.xinyi.xmjt.utils.BindView;
import cn.net.xinyi.xmjt.utils.ImageUtils;
import cn.net.xinyi.xmjt.utils.UI;

public class RegistForComanyAty extends BaseActivity2 implements View.OnClickListener {
    @BindView(id = R.id.tv_yhm)
    private TextView tv_yhm ;
    @BindView(id = R.id.et_companyname)
    private EditText et_companyname ;
    @BindView(id = R.id.et_xm)
    private EditText et_xm ;
    @BindView(id = R.id.et_cardno)
    private EditText et_cardno ;
    @BindView(id = R.id.et_password)
    private EditText et_password ;
    @BindView(id = R.id.et_pwd_confirm)
    private EditText et_pwd_confirm ;
    @BindView(id = R.id.iv_sfz,click = true)
    private ImageView iv_sfz ;
    @BindView(id = R.id.btn_next,click = true)
    private Button btn_next ;
    private  String path1;
    private int uploadCount;
    private String result;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.aty_regist_for_comany);
        AnnotateManager.initBindView(this);//注解式绑定控件
        tv_yhm.setText(getIntent().getStringExtra("edt_phone"));
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.iv_sfz:
                cameraPhoto();
                break;

            case R.id.btn_next:
                String msg = "";
                if (((AppContext) getApplication()).getNetworkType() == 0) {
                    msg =getString(R.string.network_not_connected);
                }else  if (TextUtils.isEmpty(et_companyname.getText().toString())) {
                    msg = "所属公司不能为空！";
                } else if (TextUtils.isEmpty(et_xm.getText().toString().trim())) {
                    msg = "姓名不能为空";
                } else if (et_cardno.getText().toString().trim().isEmpty()||et_cardno.getText().toString().length()<14) {
                    msg = "请填写正确的身份证号码";
                } else if (TextUtils.isEmpty(et_password.getText().toString()) ||TextUtils.isEmpty(et_pwd_confirm.getText().toString())) {
                    msg = getResources().getString(R.string.msg_reg_pwd_null);
                } else if (!et_password.getText().toString().equals(et_pwd_confirm.getText().toString())) {
                    msg = getResources().getString(R.string.msg_reg_pwd_confirm_error);
                }

                if(!msg.isEmpty()){
                    BaseUtil.showDialog(msg, RegistForComanyAty.this);
                }else{
                    startUploadThread();
                }
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == CAMERA_INTENT_REQUEST
                &&resultCode!=0 && imagePath != null) {
            path1 = imagePath;
            iv_sfz.setImageBitmap(ImageUtils.compressImageByPixel(imagePath));
        }
    }

    private void startUploadThread() {
        // 保存与上传
        new Thread() {
            public void run() {
                Message msg = new Message();
                msg.what = 1;
                handler.sendMessage(msg);
                // 将数据及视频上传到服务器
                uploadImage();
                int result = uploadCount;
                if (result > 0) {
                    msg = new Message();
                    msg.what = 2;
                    msg.arg1 = result;
                    handler.sendMessage(msg);
                } else {
                    msg = new Message();
                    msg.what = 3;
                    handler.sendMessage(msg);
                }
            }
        }.start();
    }

    //同步上传图片到服务端
    void uploadImage() {
        if (path1!= null){
            filePath.add(path1);
            fileName.add(BaseUtil.getFileNameNoEx(path1));
            //上传图片
            ApiResource.userUpload(filePath, fileName, new AsyncHttpResponseHandler() {
                @Override
                public void onSuccess(final int i, final Header[] headers, final byte[] bytes) {
                    String result = new String(bytes);
                    if (i == 200 && result != null && result.startsWith("true")) {
                        fileName.clear();
                        filePath.clear();
                        uploadData();
                    } else {
                        onFailure(i, headers, bytes, null);
                    }
                }

                @Override
                public void onFailure(final int i, final Header[] headers, final byte[] bytes, final Throwable throwable) {
                    if (bytes != null) {
                        String result = new String(bytes);
                    }
                }
            });
        }else {
            uploadData();
        }
    }


    private Handler handler=new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what){
                case 1:// 上传进度条显示
                    showLoadding("正在上传数据",RegistForComanyAty.this);
                    break;

                case 2:// 上传之后
                    stopLoading();
                    if (result.equals("\"1\"")) {
                        UI.toast(RegistForComanyAty.this,"注册成功");
                        Intent intent = new Intent(RegistForComanyAty.this, LoginActivity.class);
                        startActivity(intent);
                        RegistForComanyAty.this.finish();
                        RegisterSmsActivity.instance.finish();
                        LoginActivity.instance.finish();
                    } else if (result.equals("\"-1\"")) {
                        UI.toast(RegistForComanyAty.this,"注册信息不完整");
                    } else if (result.equals("\"-2\"")) {
                        UI.toast(RegistForComanyAty.this,"该手机已经注册");
                    } else if (result.equals("\"0\"")) {
                        UI.toast(RegistForComanyAty.this,"注册失败");
                    }
                    break;

                case 3:// 上传失败
                    stopLoading();
                    UI.toast(RegistForComanyAty.this,"注册失败请稍候再试....！");
                    break;
            }
        }
    };



    /**
     * 保存用户信息
     */
    private void uploadData() {
        UserInfo userInfos=new UserInfo();
        userInfos.setUsername(tv_yhm.getText().toString());
        userInfos.setCellphone(tv_yhm.getText().toString());
        userInfos.setName( et_xm.getText().toString());
        userInfos.setPassword(et_password.getText().toString());
        userInfos.setCompanyname(et_companyname.getText().toString());
        userInfos.setSfzh(et_cardno.getText().toString());
        userInfos.setAccounttype("厂商");
        if (path1!=null){
            userInfos.setSfzzp(new BaseDataUtils().getNowYearAndMonthAndDay()+"/"+ BaseUtil.getFileName(path1));
        }
        ApiResource.getRegister(JSON.toJSONString(userInfos), new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int i, Header[] headers, byte[] bytes) {
                try {
                    result = new String(bytes);
                    if (!result.isEmpty()) {
                        uploadCount++;
                        if (null!=path1) {
                            File plateImage = new File(path1);
                            if (plateImage.exists()) {
                                plateImage.delete();
                            }
                        }
                    }
                } catch (Exception e) {
                    onFailure(i, headers, bytes, null);
                }
            }

            @Override
            public void onFailure(int i, Header[] headers, byte[] bytes, Throwable throwable) {
                UI.toast(RegistForComanyAty.this,"注册失败");
            }
        });
    }


    @Override
    public String getAtyTitle() {
        return getResources().getString(R.string.title_new_user_reg);
    }

    @Override
    public boolean enableBackUpBtn() {
        return true;
    }
}
