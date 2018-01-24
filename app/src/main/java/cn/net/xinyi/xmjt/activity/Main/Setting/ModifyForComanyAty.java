package cn.net.xinyi.xmjt.activity.Main.Setting;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.nostra13.universalimageloader.core.ImageLoader;

import org.apache.http.Header;

import java.io.File;

import cn.net.xinyi.xmjt.R;
import cn.net.xinyi.xmjt.activity.Main.LoginActivity;
import cn.net.xinyi.xmjt.api.ApiHttpClient;
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

public class ModifyForComanyAty extends BaseActivity2 implements View.OnClickListener {
    @BindView(id = R.id.tv_yhm)
    private TextView tv_yhm ;
    @BindView(id = R.id.et_companyname)
    private EditText et_companyname ;
    @BindView(id = R.id.et_xm)
    private EditText et_xm ;
    @BindView(id = R.id.et_cardno)
    private EditText et_cardno ;
    @BindView(id = R.id.iv_sfz,click = true)
    private ImageView iv_sfz ;
    @BindView(id = R.id.btn_next,click = true)
    private Button btn_next ;
    @BindView(id = R.id.ll_confir_password)
    private LinearLayout ll_confir_password ;
    @BindView(id = R.id.ll_password)
    private LinearLayout ll_password ;

    private  String path1;
    private int uploadCount;
    private String result;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.aty_regist_for_comany);
        AnnotateManager.initBindView(this);//注解式绑定控件
        ll_confir_password.setVisibility(View.GONE);
        ll_password.setVisibility(View.GONE);
        btn_next.setText("确认修改");
        setCompanyData();
    }

    private void setCompanyData() {
        tv_yhm.setText(userInfo.getUsername());
        et_companyname.setText(userInfo.getCompanyname());
        et_xm.setText(userInfo.getName());
        et_cardno.setText(userInfo.getSfzh());
        if (null!= userInfo.getSfzzp()){
            ImageLoader.getInstance().displayImage(ApiHttpClient.IMAGE_HOST + "/user/" +userInfo.getSfzzp(),iv_sfz);
        }
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
                }
                if(!msg.isEmpty()){
                    BaseUtil.showDialog(msg, ModifyForComanyAty.this);
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
                    showLoadding("正在上传数据",ModifyForComanyAty.this);
                    break;

                case 2:// 上传之后
                    stopLoading();
                    if (result.equals("\"1\"")) {
                        AppContext.instance().cleanLoginInfo();
                        toast("修改资料成功，请重新登录!");
                        AppContext.instance().cleanLoginInfo();
                        showActivity(LoginActivity.class);
                        finish();
                    } else  {
                        UI.toast(ModifyForComanyAty.this,"修改资料失败");
                    }
                    break;

                case 3:// 上传失败
                    stopLoading();
                    UI.toast(ModifyForComanyAty.this,"上传失败，可能当前上传的人数较多，请稍候重试！");
                    break;
            }
        }
    };



    /**
     * 保存用户信息
     */
    private void uploadData() {
        showLoadding();
        UserInfo  userInfos=new UserInfo();
        userInfos.setId(userInfo.getId());
        userInfos.setUsername(tv_yhm.getText().toString());
        userInfos.setCellphone(tv_yhm.getText().toString());
        userInfos.setName(et_xm.getText().toString());
        userInfos.setSfzh(et_cardno.getText().toString());
        userInfos.setCompanyname(et_companyname.getText().toString());
        userInfos.setAccounttype("厂商");
        if (path1!=null){
            userInfos.setSfzzp(new BaseDataUtils().getNowYearAndMonthAndDay()+"/"+ BaseUtil.getFileName(path1));
        }else {
            userInfos.setSfzzp(userInfo.getSfzzp());
        }
        ApiResource.updateUserInfo(JSON.toJSONString(userInfos), new AsyncHttpResponseHandler() {
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
                    stopLoading();
                } catch (Exception e) {
                    onFailure(i, headers, bytes, null);
                }
            }

            @Override
            public void onFailure(int i, Header[] headers, byte[] bytes, Throwable throwable) {
                stopLoading();
            }
        });
    }


    @Override
    public String getAtyTitle() {
        return getResources().getString(R.string.btn_modeify);
    }

    @Override
    public boolean enableBackUpBtn() {
        return true;
    }
}
