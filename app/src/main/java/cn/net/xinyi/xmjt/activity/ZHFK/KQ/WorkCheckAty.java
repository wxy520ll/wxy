package cn.net.xinyi.xmjt.activity.ZHFK.KQ;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.loopj.android.http.AsyncHttpResponseHandler;

import org.apache.http.Header;

import java.io.File;

import cn.net.xinyi.xmjt.R;
import cn.net.xinyi.xmjt.activity.ZHFK.PlcBx.PickupMapActivity;
import cn.net.xinyi.xmjt.api.ApiResource;
import cn.net.xinyi.xmjt.config.BaseWithLocActivity;
import cn.net.xinyi.xmjt.model.LocaionInfo;
import cn.net.xinyi.xmjt.model.WorkCheckModle;
import cn.net.xinyi.xmjt.utils.AnnotateManager;
import cn.net.xinyi.xmjt.utils.BaseDataUtils;
import cn.net.xinyi.xmjt.utils.BaseUtil;
import cn.net.xinyi.xmjt.utils.BindView;
import cn.net.xinyi.xmjt.utils.ImageUtils;

/**
 * Created by hao.zhou on 2016/1/28.
 * 勤务督查
 */
public class WorkCheckAty extends BaseWithLocActivity implements View.OnClickListener {
    /**内容**/
    @BindView(id = R.id.et_nr)
    private EditText et_nr;
    /**地址**/
    @BindView(id = R.id.et_dz)
    private EditText et_dz;
    /**坐标**/
    @BindView(id = R.id.tv_zb)
    private TextView tv_zb;
    /**手动定位**/
    @BindView(id = R.id.tv_sddw,click = true)
    private TextView tv_sddw;
    /**手动定位**/
    @BindView(id = R.id.tv_sxwz,click = true)
    private TextView tv_sxwz;
    /**图片1**/
    @BindView(id = R.id.iv_1,click = true)
    private ImageView iv_1;
    /**图片2**/
    @BindView(id = R.id.iv_2,click = true)
    private ImageView iv_2;
    /**图片3**/
    @BindView(id = R.id.iv_3,click = true)
    private ImageView iv_3;
    /**上传**/
    @BindView(id = R.id.btn_upl,click = true)
    private Button btn_upl;
    /***ImageView拍照标识*/
    private int i_flag;
    /***拍照图片路径 存放数据库**/
    private String path1;
    private String path2;
    private String path3;
    /***判断地图是否展示
     * */
    private boolean flag = false;
    /***定位类型*/
    private int map_type;
    private ProgressDialog mProgressDialog ;
    private int uploadCount=0;
    /**百度坐标纬度**/
    private double bWd;
    /**百度坐标经度**/
    private double bJD;
    private LocaionInfo mLocation;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.aty_workcheck);
        /***注解式绑定控件**/
        AnnotateManager.initBindView(this);
        initData();
    }

    private void initData() {
        checkTextLength(et_dz,25);
        checkTextLength(et_nr,120);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){

            /***手动定位*/
            case R.id.tv_sddw:
                if (BaseDataUtils.isFastClick()){
                    break;
                }
                Intent intent=new Intent(this,PickupMapActivity.class);
                startActivityForResult(intent, PickupMapActivity.MAP_PICK_UP);
                break;

            /***刷新位置*/
            case R.id.tv_sxwz:
                if (BaseDataUtils.isFastClick()){
                    break;
                }
                loc();
                break;

            /***照片1*/
            case R.id.iv_1:
                if (BaseDataUtils.isFastClick()){
                    break;
                }
                i_flag = 1;
                cameraPhoto();
                break;

            /***照片2*/
            case R.id.iv_2:
                if (BaseDataUtils.isFastClick()){
                    break;
                }
                i_flag = 2;
                cameraPhoto();
                break;

            /***照片3*/
            case R.id.iv_3:
                if (BaseDataUtils.isFastClick()){
                    break;
                }
                i_flag = 3;
                cameraPhoto();
                break;
            /**上传消息**/
            case R.id.btn_upl:
                if (BaseDataUtils.isFastClick()){
                    break;
                }
                String msg1=getMsg();
                if(!msg1.isEmpty()) {
                    BaseUtil.showDialog(msg1, this);
                }else {
                    WorkCheckModle workInfo=new WorkCheckModle();
                    workInfo.setBZ(et_nr.getText().toString());
                    workInfo.setWD(bJD);
                    workInfo.setJD(bWd);
                    workInfo.setDZ(et_dz.getText().toString() );
                    workInfo.setDCDW(userInfo.getOrgancode());
                    workInfo.setDCRY(userInfo.getUsername() );
                    workInfo.setIV_ZP1(path1 );
                    workInfo.setIV_ZP2(path2 );
                    workInfo.setIV_ZP3(path3 );
                    workInfo.setLOCTYPE(map_type);
                    if (workInfo != null){
                        //开始上传采集信息
                        startUploadThread(workInfo);
                    }else {
                        Message msg2 = new Message();
                        msg2.what = 3;
                        handler.sendMessage(msg2);
                    }
                }
                break;
        }
    }


    private void startUploadThread(final WorkCheckModle Info) {
        // 保存与上传
        new Thread() {
            public void run() {
                Message msg = new Message();
                msg.what = 1;
                handler.sendMessage(msg);
                // 将数据及视频上传到服务器
                uploadImage(Info);
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
    void uploadImage(final WorkCheckModle mInfo) {
        if (mInfo.getIV_ZP1()!=null
                || mInfo.getIV_ZP2()!=null
                || mInfo.getIV_ZP3()!=null ){
            if (mInfo.getIV_ZP1() != null) {
                String path= mInfo.getIV_ZP1();
                filePath.add(path);
                fileName.add(BaseUtil.getFileNameNoEx(path));
            }

            if (mInfo.getIV_ZP2() != null) {
                String path= mInfo.getIV_ZP2();
                filePath.add(path);
                fileName.add(BaseUtil.getFileNameNoEx(path));
            }

            if (mInfo.getIV_ZP3() != null) {
                String path= mInfo.getIV_ZP3();
                filePath.add(path);
                fileName.add(BaseUtil.getFileNameNoEx(path));
            }

            //上传图片
            ApiResource.uploadQWDCImage(filePath, fileName, new AsyncHttpResponseHandler() {
                @Override
                public void onSuccess(final int i, final Header[] headers, final byte[] bytes) {
                    String result = new String(bytes);
                    if (i == 200 && result != null && result.startsWith("true")) {
                        fileName.clear();
                        filePath.clear();
                        uploadInfo(mInfo);
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
            /**没有图片直接上传信息**/
            uploadInfo(mInfo);
        }
    }

    //同步上传采集数据到服务端
    public void uploadInfo(final WorkCheckModle info) {
        //json处理
        JSONObject jo = JSON.parseObject(JSON.toJSONString(info));
        if (jo != null) {
            jo.remove("id");
            jo.remove("iV_ZP1");
            jo.remove("iV_ZP2");
            jo.remove("iV_ZP3");

            if (info.getIV_ZP1() != null) {
                jo.put("IV_ZP1", BaseUtil.getFileName(info.getIV_ZP1()));
            }

            if (info.getIV_ZP2() != null) {
                jo.put("IV_ZP2", BaseUtil.getFileName(info.getIV_ZP2()));
            }
            if (info.getIV_ZP3() != null) {
                jo.put("IV_ZP3", BaseUtil.getFileName(info.getIV_ZP3()));
            }
        }

        String json = jo.toJSONString();

        ApiResource.addQWDCInfo(json, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int i, Header[] headers, byte[] bytes) {
                String result = new String(bytes);
                uploadCount++;
                if (!result.isEmpty() && result.startsWith("true")) {
                    if (info.getIV_ZP1() != null) {
                        File plateImage = new File(info.getIV_ZP1());
                        if (plateImage.exists()) {
                            plateImage.delete();
                        }
                    }

                    if (info.getIV_ZP2() != null) {
                        File plateImage = new File(info.getIV_ZP2());
                        if (plateImage.exists()) {
                            plateImage.delete();
                        }
                    }
                    if (info.getIV_ZP3() != null) {
                        File plateImage = new File(info.getIV_ZP3());
                        if (plateImage.exists()) {
                            plateImage.delete();
                        }
                    }
                    mProgressDialog.incrementProgressBy(1);
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

    private String getMsg() {
        String msg = "";
        if (TextUtils.isEmpty(et_dz.getText().toString())){
            msg = "地址不能为空";
        }else if (path1 == null && path2 == null&& path3 == null){
            msg = "至少上传一张图片！";
        }
        return msg;
    }


    private Handler handler=new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what){
                case 1:// 上传进度条显示
                    mProgressDialog = new ProgressDialog(WorkCheckAty.this);
                    mProgressDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
                    mProgressDialog.setTitle("上传");
                    mProgressDialog.setMessage("数据正在上传中...");
                    mProgressDialog.setIndeterminate(false);
                    mProgressDialog.setCancelable(false);
                    mProgressDialog.show();
                    break;

                case 2:// 上传之后
                    mProgressDialog.cancel();
                    new AlertDialog.Builder(WorkCheckAty.this).setTitle(getString(R.string.tips))
                            .setMessage("上传成功!")
                            .setNegativeButton ("返回",
                                    new DialogInterface.OnClickListener() {

                                        @Override
                                        public void onClick(DialogInterface dialog,
                                                            int which) {
                                            WorkCheckAty.this.finish();
                                        }
                                    })
                            .setPositiveButton("继续督查",
                                    new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog,
                                                            int which) {
                                            Intent intent = new Intent(WorkCheckAty.this, WorkCheckAty.class);
                                            startActivity(intent);
                                            WorkCheckAty.this.finish();
                                        }
                                    }).setCancelable(false).show();

                    break;

                case 3:// 上传失败
                    mProgressDialog.cancel();
                    BaseUtil.showDialog("上传失败，可能当前上传的人数较多，请稍候重试！", WorkCheckAty.this);
                    break;

                case 4://上传检测完成
                    mProgressDialog.cancel();
                    if (msg.arg1 == 0) {
                        BaseUtil.showDialog("系统检测到当前APP 版本过低，请回到民警通主菜单点击【系统设置】-【APP 更新】，按提示升级APP 后重新上传！", WorkCheckAty.this);
                    } else {

                    }
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
                &&resultCode!=0
                && imagePath != null) {
            /***获得拍照的路径 写入数据库**/
            if (i_flag == 1){
                path1 = imagePath;
                iv_1.setImageBitmap(ImageUtils.compressImageByPixel(imagePath));
            }else  if (i_flag == 2){
                path2 = imagePath;
                iv_2.setImageBitmap(ImageUtils.compressImageByPixel(imagePath));
            }else  if (i_flag == 3){
                path3 = imagePath;
                iv_3.setImageBitmap(ImageUtils.compressImageByPixel(imagePath));
            }
        }else  if (PickupMapActivity.MAP_PICK_UP == requestCode) { //地图选点
            if (Activity.RESULT_OK == resultCode) {
                LocaionInfo info = (LocaionInfo) (data.getBundleExtra("data").get("data"));
                onReceiveLoc(info, true, null);
            }
        }
    }


    @Override
    public void onReceiveLoc(LocaionInfo location, boolean isSuccess, Throwable errMsg) {
        if (isSuccess) {
            mLocation = location;
            /**获得纬度信息**/
            bWd=mLocation.getLat();
            /**获得经度信息信息**/
            bJD=mLocation.getLongt();
            /**获得地址信息**/
            et_dz.setText(mLocation.getAddress());
            /**百度坐标，经纬度展现**/
            tv_zb.setText("(" + bWd + "," + bJD + ")");
            map_type = mLocation.getLoctype();
        }
    }

    /***activity退出*/
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                BaseDataUtils.loginOut(WorkCheckAty.this);
                break;
            case R.id.action_list://历史记录
                showActivity(WorkCheckHistoryAty.class);
                break;

            default:
                break;
        }
        return true;
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        MenuItem action_map = menu.findItem(R.id.action_map);
        action_map.setVisible(false);
        return true;
    }

    @Override
    public boolean enableBackUpBtn() {
        return true;
    }


    @Override
    public String getAtyTitle() {
        return getString(R.string.qw_workcheck);
    }

}