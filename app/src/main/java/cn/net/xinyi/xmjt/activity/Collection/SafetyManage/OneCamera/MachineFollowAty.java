package cn.net.xinyi.xmjt.activity.Collection.SafetyManage.OneCamera;


import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.util.TypeUtils;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.nostra13.universalimageloader.core.ImageLoader;

import org.apache.http.Header;

import java.io.File;

import cn.net.xinyi.xmjt.R;
import cn.net.xinyi.xmjt.api.ApiHttpClient;
import cn.net.xinyi.xmjt.api.ApiResource;
import cn.net.xinyi.xmjt.config.BaseActivity2;
import cn.net.xinyi.xmjt.model.MachineFowModle;
import cn.net.xinyi.xmjt.utils.AnnotateManager;
import cn.net.xinyi.xmjt.utils.BaseDataUtils;
import cn.net.xinyi.xmjt.utils.BaseUtil;
import cn.net.xinyi.xmjt.utils.BindView;
import cn.net.xinyi.xmjt.utils.ImageUtils;
import cn.net.xinyi.xmjt.utils.UI;

/**
 * Created by hao.zhou on 2016/3/19.
 */
public class MachineFollowAty extends BaseActivity2 implements View.OnClickListener {
    /***所属单位*/
    @BindView(id = R.id.et_ssdw)
    private EditText et_ssdw;
    /***姓名*/
    @BindView(id = R.id.et_xm)
    private EditText et_xm;
    /***手机号码*/
    @BindView(id = R.id.et_sjhm)
    private EditText et_sjhm;
    /***身份证号码*/
    @BindView(id = R.id.et_sfz)
    private EditText et_sfz;
    /***身份证*/
    @BindView(id = R.id.iv_sfz,click = true)
    private ImageView iv_sfz;
    /***保存*/
    @BindView(id = R.id.btn_ok,click = true)
    private Button btn_ok;
    /***更新*/
    @BindView(id = R.id.btn_update,click = true)
    private Button btn_update;
    /***删除*/
    @BindView(id = R.id.btn_del,click = true)
    private Button btn_del;
    private  String path1;
    private MachineFowModle fowInfo;
    private int uploadCount;
    private boolean booleanUpload=false;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.aty_machine_follow);
        TypeUtils.compatibleWithJavaBean = true;
        /***注解式绑定控件**/
        AnnotateManager.initBindView(this);
        if (null!=getIntent().getSerializableExtra("DATA")){
            fowInfo= (MachineFowModle) getIntent().getSerializableExtra("DATA");
            btn_ok.setVisibility(View.GONE);
            btn_del.setVisibility(View.VISIBLE);
            btn_update.setVisibility(View.VISIBLE);
            initData();
        }else {
            fowInfo=new MachineFowModle();
            et_ssdw.setText(userInfo.getCompanyname());
        }
    }

    private void initData() {
        et_ssdw.setText(fowInfo.getDW());
        et_xm.setText(fowInfo.getXM());
        et_sjhm.setText(fowInfo.getSJHM());
        et_sfz.setText(fowInfo.getSFZH());
        ImageLoader.getInstance().displayImage(ApiHttpClient.IMAGE_HOST + "/fellow/" +fowInfo.getSCJFCRZZP(),iv_sfz);
    }


    @Override
    public void onClick(View v) {
        String msg = "";
        switch (v.getId()){
            case R.id.btn_update:
                if (TextUtils.isEmpty(et_ssdw.getText().toString())){
                    msg =  "所属单位不能为空" ;
                }else  if (TextUtils.isEmpty(et_xm.getText().toString())){
                    msg =  "姓名不能为空" ;
                }else  if (TextUtils.isEmpty(et_sjhm.getText().toString())||et_sjhm.getText().toString().length()<10){
                    msg =  "请填写正确的手机号码" ;
                }else  if (TextUtils.isEmpty(et_sfz.getText().toString())||et_sfz.getText().toString().length()<15){
                    msg =  "请填写正确的身份证号码" ;
                } else  if (!btn_update.getText().toString().equals(getString(R.string.update))&&null==path1){
                    msg =  "人员手持身份证照片不能为空" ;
                }
                if(!msg.isEmpty()) {
                    UI.toast(this,msg);
                }else {
                    fowInfo.setXM(et_xm.getText().toString());
                    fowInfo.setSJHM(et_sjhm.getText().toString());
                    fowInfo.setSFZH(et_sfz.getText().toString());
                    fowInfo.setDW(et_ssdw.getText().toString());
                    fowInfo.setMANAGER_SJHM(userInfo.getUsername());
                    booleanUpload=true;
                    startUploadThread();
                }
                break;

            case R.id.btn_del:
                deleteInfo();
                break;

            case R.id.iv_sfz:
                if (BaseDataUtils.isFastClick()){
                    break;
                }
                cameraPhoto();
                break;


            case R.id.btn_ok:
                if (BaseDataUtils.isFastClick()){
                    break;
                }

                if (TextUtils.isEmpty(et_ssdw.getText().toString())){
                    msg =  "所属单位不能为空" ;
                }else  if (TextUtils.isEmpty(et_xm.getText().toString())){
                    msg =  "姓名不能为空" ;
                }else  if (TextUtils.isEmpty(et_sjhm.getText().toString())||et_sjhm.getText().toString().length()<10){
                    msg =  "请填写正确的手机号码" ;
                }else  if (TextUtils.isEmpty(et_sfz.getText().toString())||et_sfz.getText().toString().length()<15){
                    msg =  "请填写正确的身份证号码" ;
                } else  if (null==path1){
                    msg =  "人员手持身份证照片不能为空" ;
                }
                if(!msg.isEmpty()) {
                    UI.toast(this,msg);
                }else {
                    fowInfo.setXM(et_xm.getText().toString());
                    fowInfo.setSJHM(et_sjhm.getText().toString());
                    fowInfo.setSFZH(et_sfz.getText().toString());
                    fowInfo.setDW(et_ssdw.getText().toString());
                    fowInfo.setMANAGER_SJHM(userInfo.getUsername());
                    startUploadThread();
                }
                break;
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
        if (path1!= null) {
            filePath.add(path1);
            fileName.add(BaseUtil.getFileNameNoEx(path1));
            //上传图片
            ApiResource.upMachineRoomFellow(filePath, fileName, new AsyncHttpResponseHandler() {
                @Override
                public void onSuccess(final int i, final Header[] headers, final byte[] bytes) {
                    String result = new String(bytes);
                    if (i == 200 && result != null && result.startsWith("true")) {
                        fileName.clear();
                        filePath.clear();
                        if (booleanUpload){
                            updateInfo();
                        }else {
                            uploadInfo();
                        }
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
            updateInfo();
        }
    }


    //同步上传采集数据到服务端
    public void uploadInfo() {
        JSONObject jo = JSON.parseObject(JSON.toJSONString(fowInfo));
        jo.put("SCJFCRZZP",new BaseDataUtils().getNowYearAndMonthAndDay()+"/"+ BaseUtil.getFileName(path1));
        jo.put("ISCHECKED","1");
        String json = jo.toJSONString();
        ApiResource.MachineRoomFellowAdd(json, new AsyncHttpResponseHandler() {
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
                } else {
                    onFailure(i, headers, bytes, null);
                }
            }
            @Override
            public void onFailure(int i, Header[] headers, byte[] bytes, Throwable throwable) {
                if (bytes != null) {
                    String result = new String(bytes);
                }
            }
        });
    }

    //更新上传采集数据到服务端
    public void updateInfo() {
        JSONObject jo = JSON.parseObject(JSON.toJSONString(fowInfo));
        jo.put("ID",fowInfo.getID());
        if(null!=path1){
            jo.put("SCJFCRZZP",new BaseDataUtils().getNowYearAndMonthAndDay()+"/"+ BaseUtil.getFileName(path1));
        }else {
            jo.put("SCJFCRZZP",fowInfo.getSCJFCRZZP());
        }
        jo.put("ISCHECKED","1");
        jo.remove("GXSJ");
        String  json = jo.toJSONString();
        ApiResource.MachineRoomFellow(json, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int i, Header[] headers, byte[] bytes) {
                String result = new String(bytes);
                if (null!=result&&result.startsWith("true")){
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
            }
            @Override
            public void onFailure(int i, Header[] headers, byte[] bytes, Throwable throwable) {
                UI.toast(MachineFollowAty.this,"上传失败，可能当前上传的人数较多，请稍候重试！");
            }
        });
    }

    //删除采集数据到服务端
    public void deleteInfo() {
        JSONObject jo = JSON.parseObject(JSON.toJSONString(fowInfo));
        jo.put("ID",fowInfo.getID());
        String  json = jo.toJSONString();
        ApiResource.MachineRoomFellowDel(json, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int i, Header[] headers, byte[] bytes) {
                String result = new String(bytes);
                if (null!=result&&result.startsWith("true")){
                    UI.toast(MachineFollowAty.this,"删除成功");
                    Intent intent = new Intent();
                    setResult(RESULT_OK,intent);
                    MachineFollowAty.this.finish();
                }else {
                    onFailure(i,headers,bytes,null);
                }
            }
            @Override
            public void onFailure(int i, Header[] headers, byte[] bytes, Throwable throwable) {
                UI.toast(MachineFollowAty.this,"上传失败，可能当前上传的人数较多，请稍候重试！");
            }
        });
    }


    private Handler handler=new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what){
                case 1:// 上传进度条显示
                    showLoadding("正在上传数据",MachineFollowAty.this);
                    break;

                case 2:// 上传之后
                    stopLoading();
                    UI.toast(MachineFollowAty.this,"上传成功");
                    Intent intent = new Intent();
                    setResult(RESULT_OK,intent);
                    MachineFollowAty.this.finish();
                    break;

                case 3:// 上传失败
                    stopLoading();
                    UI.toast(MachineFollowAty.this,"上传失败，可能当前上传的人数较多，请稍候重试！");
                    break;
            }
        }
    };


    /**
     *返回拍照
     */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == CAMERA_INTENT_REQUEST
                &&resultCode!=0 && imagePath != null) {
            path1 = imagePath;
            iv_sfz.setImageBitmap(ImageUtils.compressImageByPixel(imagePath));
        }
    }

    @Override
    public boolean enableBackUpBtn() {
        return true;
    }

    @Override
    public String getAtyTitle() {
        return "机房同行人员";
    }
}