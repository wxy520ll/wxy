package cn.net.xinyi.xmjt.activity.Collection.SafetyManage.KpiCertificate;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.nostra13.universalimageloader.core.ImageLoader;

import org.apache.http.Header;

import java.io.File;

import cn.net.xinyi.xmjt.R;
import cn.net.xinyi.xmjt.api.ApiHttpClient;
import cn.net.xinyi.xmjt.api.ApiResource;
import cn.net.xinyi.xmjt.config.BaseActivity2;
import cn.net.xinyi.xmjt.utils.AnnotateManager;
import cn.net.xinyi.xmjt.utils.BaseDataUtils;
import cn.net.xinyi.xmjt.utils.BaseUtil;
import cn.net.xinyi.xmjt.utils.BindView;
import cn.net.xinyi.xmjt.utils.DialogHelper;
import cn.net.xinyi.xmjt.utils.ImageUtils;
import cn.net.xinyi.xmjt.utils.StringUtils;

public class KpiRecordAty extends BaseActivity2 implements View.OnClickListener{
    //证书编号
    @BindView(id =R.id.et_zsbh)
    private EditText et_zsbh;
    //证书图片
    @BindView(id =R.id.iv01,click = true)
    private ImageView iv01;
    @BindView(id =R.id.iv_ex1,click = true)
    private ImageView iv_ex1;
    @BindView(id =R.id.iv_ex2,click = true)
    private ImageView iv_ex2;
    //上传
    @BindView(id =R.id.btn_upl,click = true)
    private Button btn_upl;
    //编辑
    @BindView(id =R.id.btn_edt,click = true)
    private Button btn_edt;
    private String path1;
    private int uploadCount=0;
    private boolean isEdit=false;
    private Integer sID;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.aty_kpi_record);
        AnnotateManager.initBindView(this);  //控件绑定
        getData();
    }

    //同步上传采集数据到服务端
    public void getData() {
        JSONObject jo = new JSONObject();
        jo.put("CJYH",userInfo.getUsername());
        String json = jo.toJSONString();
        ApiResource.getKpiRecordList(json, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int i, Header[] headers, byte[] bytes) {
                String result = new String(bytes);
                if (!JSON.parseObject(result).getString("result").equals("[]")){
                    JSONObject jo=(JSONObject)JSON.parseArray(JSON.parseObject(result).getString("result")).get(0);
                    isEdit=true;
                    sID=jo.getInteger("ID");
                    et_zsbh.setText(jo.getString("ZSBH"));
                    btn_edt.setVisibility(View.VISIBLE);
                    btn_upl.setVisibility(View.GONE);
                    ImageLoader.getInstance().displayImage(ApiHttpClient.IMAGE_HOST + "/kpi/" +jo.getString("ZP"),iv01);
                }else {
                    onFailure(i,headers,bytes,null);
                }
            }
            @Override
            public void onFailure(int i, Header[] headers, byte[] bytes, Throwable throwable) {
                btn_edt.setVisibility(View.GONE);
                btn_upl.setVisibility(View.VISIBLE);
            }
        });
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.iv_ex1:
                DialogHelper.showBitMapPopMenu(this,iv_ex1,BitmapFactory.decodeResource(getResources(), R.drawable.kpi_exam1));
                break;
            case R.id.iv_ex2:
                DialogHelper.showBitMapPopMenu(this,iv_ex2,BitmapFactory.decodeResource(getResources(), R.drawable.kpi_exam2));
                break;


            case R.id.iv01:
                cameraPhoto();
                break;

            case R.id.btn_upl:
                if (StringUtils.isEmpty(et_zsbh.getText().toString())||et_zsbh.getText().toString().length()<8){
                    toast("请输入正确的证书编号!");
                }else if (StringUtils.isEmpty(path1)){
                    toast("证书图片不能为空");
                }else {
                    startUploadThread();
                }
                break;

            case R.id.btn_edt:
                if (StringUtils.isEmpty(et_zsbh.getText().toString())){
                    toast("证书编号不能为空");
                }else {
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
        if (!StringUtils.isEmpty(path1)) {
            filePath.add(path1);
            fileName.add(BaseUtil.getFileNameNoEx(path1));
            //上传图片
            ApiResource.KpiRecordImage(filePath, fileName, new AsyncHttpResponseHandler() {
                @Override
                public void onSuccess(final int i, final Header[] headers, final byte[] bytes) {
                    String result = new String(bytes);
                    if (i == 200 && result != null && result.startsWith("true")) {
                        fileName.clear();
                        filePath.clear();
                        if (isEdit){
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

    //更新
    private void updateInfo() {
        JSONObject jo = new JSONObject();
        jo.put("ID",sID);
        jo.put("ZSBH",et_zsbh.getText().toString());
        if (!StringUtils.isEmpty(path1)){
            jo.put("ZP", BaseDataUtils.getNowYearAndMonthAndDay()+"/"+ BaseUtil.getFileName(path1));
        }
        String json = jo.toJSONString();
        ApiResource.updateKpiInfo(json, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int i, Header[] headers, byte[] bytes) {
                String result = new String(bytes);
                uploadCount++;
                if (!result.isEmpty()) {
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
                    toast(new String(bytes));
                }
            }
        });
    }

    //同步上传采集数据到服务端
    public void uploadInfo() {
        //json处理
        JSONObject jo = new JSONObject();
        jo.put("CJYH",userInfo.getUsername());
        jo.put("ZSBH",et_zsbh.getText().toString());
        jo.put("ZP", BaseDataUtils.getNowYearAndMonthAndDay()+"/"+ BaseUtil.getFileName(path1));
        jo.put("CJSJ", BaseDataUtils.getNowData());

        String json = jo.toJSONString();
        ApiResource.AddKpiInfo(json, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int i, Header[] headers, byte[] bytes) {
                String result = new String(bytes);
                uploadCount++;
                if (!result.isEmpty()) {
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
                    toast(new String(bytes));
                }
            }
        });
    }


    private Handler handler=new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what){
                case 1:// 上传进度条显示
                    showLoadding("正在上传数据",KpiRecordAty.this);
                    break;

                case 2:// 上传之后
                    stopLoading();
                    DialogHelper.showAlertDialog(KpiRecordAty.this, "上传成功", new DialogHelper.OnOptionClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which, Object o) {
                            KpiRecordAty.this.finish();
                        }
                    });
                    break;

                case 3:// 上传失败
                    stopLoading();
                    BaseUtil.showDialog("上传失败，可能当前上传的人数较多，请稍候重试！", KpiRecordAty.this);
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
            iv01.setImageBitmap(ImageUtils.compressImageByPixel(imagePath));
        }
    }

    @Override
    public boolean enableBackUpBtn() {
        return true;
    }

    @Override
    public String getAtyTitle() {
        return getString(R.string.kpi_record);
    }
}
