package cn.net.xinyi.xmjt.activity.Collection.House;


import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.Button;
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
import cn.net.xinyi.xmjt.model.HousePoliceModle;
import cn.net.xinyi.xmjt.utils.AnnotateManager;
import cn.net.xinyi.xmjt.utils.BaseDataUtils;
import cn.net.xinyi.xmjt.utils.BaseUtil;
import cn.net.xinyi.xmjt.utils.BindView;
import cn.net.xinyi.xmjt.utils.DialogHelper;
import cn.net.xinyi.xmjt.utils.ImageUtils;
import cn.net.xinyi.xmjt.utils.UI;

public class HouseJfActivity extends BaseActivity2 implements View.OnClickListener {
    //人防
    @BindView(id = R.id.iv_rf,click = true)
    private ImageView iv_rf;
    //物防
    @BindView(id = R.id.iv_wf,click = true)
    private ImageView iv_wf;
    //消防
    @BindView(id = R.id.iv_xf,click = true)
    private ImageView iv_xf;
    //技防
    @BindView(id = R.id.iv_jf,click = true)
    private ImageView iv_jf;
    //上传
    @BindView(id = R.id.btn_upl,click = true)
    private Button btn_upl;
    private int i_flag;
    /***拍照图片路径 存放数据库**/
    private String path1;
    private String path2;
    private String path3;
    private String path4;
    private int uploadCount=0;

    private HousePoliceModle poInfo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_house_jf);
        AnnotateManager.initBindView(this);//注解式绑定控件
        TypeUtils.compatibleWithJavaBean = true;
        poInfo= (HousePoliceModle) getIntent().getSerializableExtra("poInfo");
        if (poInfo.getSFJF()==1){
            setData();
        }
    }


    private void startUploadThread(final HousePoliceModle Info) {
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
    void uploadImage(final HousePoliceModle mInfo) {

        if (path1!= null) {
            poInfo.setIV_RF(path1);
            String path = mInfo.getIV_RF();
            filePath.add(path);
            fileName.add(BaseUtil.getFileNameNoEx(path));
        }
        if (path2 != null) {
            poInfo.setIV_WF(path2);
            String path = mInfo.getIV_WF();
            filePath.add(path);
            fileName.add(BaseUtil.getFileNameNoEx(path));
        }
        if (path3 != null) {
            poInfo.setIV_XF(path3);
            String path = mInfo.getIV_XF();
            filePath.add(path);
            fileName.add(BaseUtil.getFileNameNoEx(path));
        }
        if (path4 != null) {
            poInfo.setIV_JF(path4);
            String path = mInfo.getIV_JF();
            filePath.add(path);
            fileName.add(BaseUtil.getFileNameNoEx(path));
        }

        //上传图片
        ApiResource.uploadCZWZFImage(filePath, fileName, new AsyncHttpResponseHandler() {
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
    }


    //同步上传采集数据到服务端
    public void uploadInfo(final HousePoliceModle mInfo) {
        //json处理
        JSONObject jo = JSON.parseObject(JSON.toJSONString(mInfo));
        if (jo != null) {
            jo.remove("IV_RF");
            jo.remove("IV_WF");
            jo.remove("IV_XF");
            jo.remove("IV_JF");
            if (path1!= null) {
                jo.put("IV_RF",new BaseDataUtils().getNowYearAndMonthAndDay()+"/"+ BaseUtil.getFileName(path1));
            }else {
                jo.put("IV_RF",mInfo.getIV_RF());
            }
            if (path2!= null) {
                jo.put("IV_WF",new BaseDataUtils().getNowYearAndMonthAndDay()+"/"+ BaseUtil.getFileName(path2));
            }else {
                jo.put("IV_WF",mInfo.getIV_WF());
            }
            if (path3!= null) {
                jo.put("IV_XF",new BaseDataUtils().getNowYearAndMonthAndDay()+"/"+ BaseUtil.getFileName(path3));
            }else {
                jo.put("IV_XF",mInfo.getIV_XF());
            }
            if (path4!= null) {
                jo.put("IV_JF",new BaseDataUtils().getNowYearAndMonthAndDay()+"/"+ BaseUtil.getFileName(path4));
            }else {
                jo.put("IV_JF",mInfo.getIV_JF());
            }
        }

        String json = jo.toJSONString();
        ApiResource.upCZWZFInfo(json, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int i, Header[] headers, byte[] bytes) {
                String result = new String(bytes);
                uploadCount++;
//                JSONObject jo= JSON.parseObject(result);
//                int ID= Integer.parseInt(jo.get("ID").toString());
                if (!result.isEmpty()) {
                    if (null!=path1) {
                        File plateImage = new File(path1);
                        if (plateImage.exists()) {
                            plateImage.delete();
                        }
                    }
                    if (path2 != null) {
                        File plateImage = new File(path2);
                        if (plateImage.exists()) {
                            plateImage.delete();
                        }
                    }

                    if (path3 != null) {
                        File plateImage = new File(path3);
                        if (plateImage.exists()) {
                            plateImage.delete();
                        }
                    }
                    if (path4 != null) {
                        File plateImage = new File(path4);
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


    private Handler handler=new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what){
                case 1:// 上传进度条显示
                    showLoadding("正在上传数据",HouseJfActivity.this);
                    break;

                case 2:// 上传之后
                    stopLoading();
                    DialogHelper.showAlertDialog(HouseJfActivity.this, "上传成功", new DialogHelper.OnOptionClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which, Object o) {
                            setResult(RESULT_OK);
                            HouseJfActivity.this.finish();
                        }
                    });
                    break;

                case 3:// 上传失败
                    stopLoading();
                    BaseUtil.showDialog("上传失败，可能当前上传的人数较多，请稍候重试！", HouseJfActivity.this);
                    break;
            }
        }
    };

    private void setData() {
        ImageLoader.getInstance().displayImage(ApiHttpClient.IMAGE_HOST + "/czwzf/" +poInfo.getIV_RF(), iv_rf);
        ImageLoader.getInstance().displayImage(ApiHttpClient.IMAGE_HOST + "/czwzf/" +poInfo.getIV_WF(), iv_wf);
        ImageLoader.getInstance().displayImage(ApiHttpClient.IMAGE_HOST + "/czwzf/" +poInfo.getIV_XF(), iv_xf);
        ImageLoader.getInstance().displayImage(ApiHttpClient.IMAGE_HOST + "/czwzf/" +poInfo.getIV_JF(), iv_jf);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.iv_rf:
                i_flag = 1;
                cameraPhoto();
                break;
            case R.id.iv_wf:
                i_flag = 2;
                cameraPhoto();
                break;
            case R.id.iv_xf:
                i_flag = 3;
                cameraPhoto();
                break;
            case R.id.iv_jf:
                i_flag =4;
                cameraPhoto();
                break;
            case R.id.btn_upl:
                String msg="";
                if (null == path1&&null==poInfo.getIV_RF()){
                    msg="人防照片不能为空";
                }else if (null == path2&&null==poInfo.getIV_WF()){
                    msg="物防照片不能为空";
                }else if (null == path3&&null==poInfo.getIV_XF()){
                    msg="消防照片不能为空 ";
                }else if (null == path4&&null==poInfo.getIV_JF()){
                    msg="技防照片不能为空";
                }
                if (!msg.isEmpty()){
                    UI.toast(this,msg);
                }else {
                    poInfo.setSFJF(1);
                    startUploadThread(poInfo);
                }
                break;
        }
    }

    /**
     *返回拍照
     */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == CAMERA_INTENT_REQUEST
                &&resultCode!=0 && imagePath != null) {
            if (i_flag == 1){
                path1 = imagePath;
                iv_rf.setImageBitmap(ImageUtils.compressImageByPixel(imagePath));
            }else  if (i_flag == 2){
                path2 = imagePath;
                iv_wf.setImageBitmap(ImageUtils.compressImageByPixel(imagePath));
            }else  if (i_flag == 3){
                path3 = imagePath;
                iv_xf.setImageBitmap(ImageUtils.compressImageByPixel(imagePath));
            }else  if (i_flag == 4){
                path4 = imagePath;
                iv_jf.setImageBitmap(ImageUtils.compressImageByPixel(imagePath));
            }
        }
    }

    @Override
    public boolean enableBackUpBtn() {
        return true;
    }

    @Override
    public String getAtyTitle() {
        return getString(R.string.house_police_jf);
    }


}
