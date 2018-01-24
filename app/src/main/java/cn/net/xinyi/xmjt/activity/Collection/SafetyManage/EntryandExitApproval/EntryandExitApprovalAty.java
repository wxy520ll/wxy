package cn.net.xinyi.xmjt.activity.Collection.SafetyManage.EntryandExitApproval;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.alibaba.fastjson.JSONObject;
import com.loopj.android.http.AsyncHttpResponseHandler;

import org.apache.http.Header;

import java.io.File;

import cn.net.xinyi.xmjt.R;
import cn.net.xinyi.xmjt.api.ApiResource;
import cn.net.xinyi.xmjt.config.BaseActivity2;
import cn.net.xinyi.xmjt.utils.AnnotateManager;
import cn.net.xinyi.xmjt.utils.BaseDataUtils;
import cn.net.xinyi.xmjt.utils.BaseUtil;
import cn.net.xinyi.xmjt.utils.BindView;
import cn.net.xinyi.xmjt.utils.DialogHelper;
import cn.net.xinyi.xmjt.utils.ImageUtils;
import cn.net.xinyi.xmjt.utils.UI;

//机房出入证申请
public class EntryandExitApprovalAty extends BaseActivity2 implements View.OnClickListener{
    @BindView(id = R.id.tv_sqrxm)
    private TextView tv_sqrxm;

    @BindView(id = R.id.tv_sqrsjhm)
    private TextView tv_sqrsjhm;

    @BindView(id = R.id.tv_sqrssdw)
    private TextView tv_sqrssdw;

    @BindView(id = R.id.tv_sqrsfz)
    private TextView tv_sqrsfz;

    @BindView(id = R.id.iv_fj1,click = true)
    private ImageView iv_fj1;

    @BindView(id = R.id.btn_sq,click = true)
    private Button btn_sq;
    private String path1;
    private int uploadCount;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.aty_entryand_exit_approval);
        //控件绑定
        AnnotateManager.initBindView(this);
        tv_sqrxm.setText(userInfo.getName());
        tv_sqrsjhm.setText(userInfo.getUsername());
        tv_sqrsfz.setText(userInfo.getSfzh());
        tv_sqrssdw.setText(userInfo.getCompanyname());

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.iv_fj1:
                cameraPhoto();
                break;

            case R.id.btn_sq:
                startUploadThread();
                break;
        }
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == CAMERA_INTENT_REQUEST
                &&resultCode!=0 && imagePath != null) {
            path1 = imagePath;
            iv_fj1.setImageBitmap(ImageUtils.compressImageByPixel(imagePath));
        }
    }


    private Handler handler=new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what){
                case 1:// 上传进度条显示
                    showLoadding("正在上传数据",EntryandExitApprovalAty.this);
                    break;
                case 2:// 上传之后
                    stopLoading();
                    DialogHelper.showAlertDialog(EntryandExitApprovalAty.this, "您的出入证信息申请成功！", new DialogHelper.OnOptionClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which, Object o) {
                            EntryandExitApprovalAty.this.finish();
                        }
                    });
                    break;
                case 3:// 上传失败
                    stopLoading();
                    UI.toast(EntryandExitApprovalAty.this,"上传失败，可能当前上传的人数较多，请稍候重试！");
                    break;
            }
        }
    };

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
        filePath.add(path1);
        fileName.add(BaseUtil.getFileNameNoEx(path1));
        //上传图片
        ApiResource.upCrzsq(filePath, fileName, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(final int i, final Header[] headers, final byte[] bytes) {
                String result = new String(bytes);
                if (i == 200 && result != null && result.startsWith("true")) {
                    fileName.clear();
                    filePath.clear();
                    uploadData(); //添加
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
    }

    private void uploadData() {
        JSONObject requestJson = new JSONObject();
        requestJson.put("CJYH", userInfo.getUsername());
        requestJson.put("MZ", userInfo.getName());
        requestJson.put("SFZH", userInfo.getSfzh());
        requestJson.put("SQDW", userInfo.getCompanyname());
        if (null!=path1){
            requestJson.put("FJ01", new BaseDataUtils().getNowYearAndMonthAndDay()+"/"+ BaseUtil.getFileName(path1));
        }
        ApiResource.CrzsqAdd(requestJson.toJSONString(),new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int i, Header[] headers, byte[] bytes) {
                String result = new String(bytes);
                if (!result.isEmpty()) {
                    uploadCount++;
                    if (null!=path1) {
                        File plateImage = new File(path1);
                        if (plateImage.exists()) {
                            plateImage.delete();
                        }
                    }
                }else {
                    onFailure(i,headers,bytes,null);
                }
                stopLoading();
            }
            @Override
            public void onFailure(int i, Header[] headers, byte[] bytes, Throwable throwable) {
                stopLoading();
                UI.toast(EntryandExitApprovalAty.this,"数据上传失败");
            }
        });
    }


    @Override
    public String getAtyTitle() {
        return getString(R.string.entryandexitapproval);
    }

    @Override
    public boolean enableBackUpBtn() {
        return true;
    }


}
