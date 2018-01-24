package cn.net.xinyi.xmjt.activity.Collection.Person;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.hardware.Camera;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
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
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;

import cn.net.xinyi.xmjt.R;
import cn.net.xinyi.xmjt.activity.Collection.ZAJC.CameraActivity;
import cn.net.xinyi.xmjt.activity.Collection.ZAJC.ZAJCCYZAty;
import cn.net.xinyi.xmjt.activity.ZHFK.PlcBx.PickupMapActivity;
import cn.net.xinyi.xmjt.api.ApiResource;
import cn.net.xinyi.xmjt.config.AppContext;
import cn.net.xinyi.xmjt.config.BaseWithLocActivity;
import cn.net.xinyi.xmjt.model.LocaionInfo;
import cn.net.xinyi.xmjt.model.PerReturnModle;
import cn.net.xinyi.xmjt.model.Presenter.PreReturnPresenter;
import cn.net.xinyi.xmjt.model.View.IPerReturnView;
import cn.net.xinyi.xmjt.utils.AnnotateManager;
import cn.net.xinyi.xmjt.utils.BaseDataUtils;
import cn.net.xinyi.xmjt.utils.BaseUtil;
import cn.net.xinyi.xmjt.utils.BindView;
import cn.net.xinyi.xmjt.utils.GeneralUtils;
import cn.net.xinyi.xmjt.utils.ImageUtils;
import cn.net.xinyi.xmjt.utils.SharedPreferencesUtil;

/**
 * Created by hao.zhou on 2016/1/28.
 * 人员走访
 */
public class PerReturnAty extends BaseWithLocActivity implements IPerReturnView, View.OnClickListener{
    /**被走访人姓名**/
    @BindView(id = R.id.et_name)
    private EditText et_name;
    /**被走访人身份证号码**/
    @BindView(id = R.id.et_sfz)
    private EditText et_sfz;
    /***扫描证件*/
    @BindView(id = R.id.tv_smzj,click = true)
    private TextView tv_smzj;
    /**走访后地址**/
    @BindView(id = R.id.et_dzh)
    private EditText et_dzh;
    /**坐标**/
    @BindView(id = R.id.tv_zb)
    private TextView tv_zb;
    /**手动定位**/
    @BindView(id = R.id.tv_sddw,click = true)
    private TextView tv_sddw;
    /**手动定位**/
    @BindView(id = R.id.tv_sxwz,click = true)
    private TextView tv_sxwz;
    /**备注**/
    @BindView(id = R.id.et_bz)
    private EditText et_bz;
    /**楼宇全景**/
    @BindView(id = R.id.iv_lyqj,click = true)
    private ImageView iv_lyqj;
    /**保存**/
    @BindView(id = R.id.btn_save,click = true)
    private Button btn_save;
    /**上传**/
    @BindView(id = R.id.btn_upl,click = true)
    private Button btn_upl;
    /***数据库保存
     * false 默认 如果返回值为false 保存失败
     *               返回值为true 保存成功
     * */
    private boolean flag = false;
    /**百度坐标纬度**/
    private double bWd;
    /**百度坐标经度**/
    private double bJD;
    /***ImageView拍照标识*/
    private int i_flag;
    /***拍照图片路径 存放数据库**/
    private String path1;
    private PreReturnPresenter preRePres;
    /***定位类型*/
    private int map_type;
    private ProgressDialog mProgressDialog = null;
    private int uploadCount=0;
    private boolean isCatchPreview = false;
    private boolean isCatchPicture = false;
    private int WIDTH;
    private int HEIGHT;
    private int srcwidth;
    private int srcheight;
    int nMainID = 0;
    private LocaionInfo mLocation;

    BroadcastReceiver updateReceiver = new BroadcastReceiver() {

        @Override
        public void onReceive(Context context, Intent intent) {
            String[] fieldvalue= (String[]) intent.getSerializableExtra("fieldvalue");
            et_name.setText(fieldvalue[1]);
            et_sfz.setText(fieldvalue[6]);
        }
    };


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.aty_per_return);
        /***注解式绑定控件**/
        AnnotateManager.initBindView(this);
        checkCameraParameters();
        initData();
        IntentFilter filter = new IntentFilter(ZAJCCYZAty.UPDATE_ACTION);
        registerReceiver(updateReceiver, filter);
        if (!SharedPreferencesUtil.getBoolean(getActivity(),"iscolltips",false)){
            SharedPreferencesUtil.putBoolean(getActivity(),"iscolltips",true);
            BaseUtil.showDialog(getString(R.string.coll_tips),getActivity());
        }
    }

    private void initData() {
        /**presenter 初始化**/
        preRePres = new PreReturnPresenter(this);
        checkTextLength(et_name,10);
        checkTextLength(et_sfz,19);
        checkTextLength(et_dzh,25);
        checkTextLength(et_bz,30);
    }



    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.tv_smzj:
                if (BaseDataUtils.isFastClick()){
                    break;
                }
                if (isCatchPreview == true && isCatchPicture == true) {
                    Intent intent = new Intent();
                    intent.setClass(this, CameraActivity.class);
                    writePreferences("", "WIDTH", WIDTH);
                    writePreferences("", "HEIGHT", HEIGHT);
                    writePreferences("", "srcwidth", srcwidth);
                    writePreferences("", "srcheight", srcheight);
                    writePreferences("", "isAutoTakePic", 0);
                    intent.putExtra("WIDTH", WIDTH);
                    intent.putExtra("HEIGHT", HEIGHT);
                    intent.putExtra("srcwidth", srcwidth);
                    intent.putExtra("srcheight", srcheight);
                    intent.putExtra("nMainID", nMainID);
                    startActivity(intent);
                }

                break;

            /***手动定位**/
            case R.id.tv_sddw:
                if (BaseDataUtils.isFastClick()){
                    break;
                }
                Intent intent=new Intent(this,PickupMapActivity.class);
                startActivityForResult(intent, PickupMapActivity.MAP_PICK_UP);
                break;

            /***手动定位*/
            case R.id.tv_sxwz:
                if (BaseDataUtils.isFastClick()){
                    break;
                }
                loc();
                break;


            /***楼宇全景照片**/
            case R.id.iv_lyqj:
                if (BaseDataUtils.isFastClick()){
                    break;
                }
                i_flag=1;
                cameraPhoto();
                break;

            /***保存信息到数据库**/
            case R.id.btn_save:
                if (BaseDataUtils.isFastClick()){
                    break;
                }
                String msg=getMsg();
                if(!msg.isEmpty()) {
                    BaseUtil.showDialog(msg, this);
                }else {
                    boolean flag= preRePres.saveInfo(this, GeneralUtils.ISCreate,getNAME(),getId(),
                            getSFZH(),getZFHDZ(),getJd(),getWd(),
                            getBZ(),getLYQZZP(),getLOCTYPE());
                    if (flag==true){
                        BaseUtil.showDialog(this, PerReturnLocalAty.class, PerReturnAty.class);
                    }else {
                        BaseUtil.showDialog("保存失败",this);
                    }
                }
                break;

            case R.id.btn_upl:
                if (BaseDataUtils.isFastClick()){
                    break;
                }
                String msg1=getMsg();
                if(!msg1.isEmpty()) {
                    BaseUtil.showDialog(msg1, this);
                }else {
                    PerReturnModle perRetInfo= preRePres.uploadInfo(getNAME(), getSFZH(),
                            getZFHDZ(), getJd(), getWd(),getBZ(), getLYQZZP(), getLOCTYPE());
                    if (perRetInfo != null){
                        //开始上传采集信息
                        startUploadThread(perRetInfo);
                    }else {
                        Message msg2 = new Message();
                        msg2.what = 3;
                        handler.sendMessage(msg2);
                    }
                }
                break;
        }
    }

    private String getMsg() {
        String msg = "";
        if (TextUtils.isEmpty(getNAME())){
            msg =  getResources().getString(R.string.per_bfrxm)+getResources().getString(R.string.info_null) ;
        }else if (getSFZH().length()<12){
            msg = "请输入正确的身份证号码！" ;
        }else if (TextUtils.isEmpty(getZFHDZ())){
            msg =  getResources().getString(R.string.per_dzh)+getResources().getString(R.string.info_null) ;
        }else if (path1 == null){
            msg = "人员照片不能为空！" ;
        }
        return msg;
    }


    @Override
    public int getId() {
        return 0;
    }

    @Override
    public String getNAME() {
        return et_name.getText().toString();
    }

    @Override
    public String getSFZH() {
        return et_sfz.getText().toString();
    }

    @Override
    public String getZFHDZ() {
        return et_dzh.getText().toString();
    }

    @Override
    public Double getJd() {
        return bJD;
    }

    @Override
    public Double getWd() {
        return bWd;
    }

    @Override
    public String getBZ() {
        return et_bz.getText().toString();
    }

    @Override
    public String getLYQZZP() {
        return path1;
    }

    @Override
    public String getLOCTYPE() {
        return ""+map_type;
    }

    @Override
    public void setNAME(String NAME) {}

    @Override
    public void setSFZH(String SFZH) { }

    @Override
    public void setZFHDZ(String ZFHDZ) { }

    @Override
    public void setJd(Double jd) { }

    @Override
    public void setWd(Double wd) { }

    @Override
    public void setBZ(String BZ) { }

    @Override
    public void setLYQZZP(String LYQZZP) { }

    @Override
    public void setLOCTYPE(String LOCTYPE) { }

    private Handler handler=new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what){
                case 1:// 上传进度条显示
                    mProgressDialog = new ProgressDialog(PerReturnAty.this);
                    mProgressDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
                    mProgressDialog.setTitle("上传");
                    mProgressDialog.setMessage("数据正在上传中...");
                    mProgressDialog.setIndeterminate(false);
                    mProgressDialog.setCancelable(false);
                    mProgressDialog.show();
                    break;

                case 2:// 上传之后
                    mProgressDialog.cancel();
                    new AlertDialog.Builder(PerReturnAty.this).setTitle("提示")
                            .setMessage("上传成功!")
                            .setNegativeButton ("返回",
                                    new DialogInterface.OnClickListener() {

                                        @Override
                                        public void onClick(DialogInterface dialog,
                                                            int which) {
                                            PerReturnAty.this.finish();
                                        }
                                    })
                            .setPositiveButton("继续采集",
                                    new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog,
                                                            int which) {
                                            Intent intent = new Intent(PerReturnAty.this, PerReturnAty.class);
                                            startActivity(intent);
                                            PerReturnAty.this.finish();
                                        }
                                    }).setCancelable(false).show();

                    break;

                case 3:// 上传失败
                    mProgressDialog.cancel();
                    BaseUtil.showDialog("上传失败，可能当前上传的人数较多，请稍候重试！",PerReturnAty.this);
                    break;
            }
        }
    };


    private void startUploadThread(final PerReturnModle Info) {
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
    void uploadImage(final PerReturnModle mInfo) {

        if (mInfo.getIV_LYQZZP() != null) {
            String path= mInfo.getIV_LYQZZP();
            filePath.add(path);
            fileName.add(BaseUtil.getFileNameNoEx(path));
        }

        //上传图片
        ApiResource.uploadPerImage(filePath, fileName, new AsyncHttpResponseHandler() {
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
    public void uploadInfo(final PerReturnModle info) {
        //json处理
        JSONObject jo = JSON.parseObject(JSON.toJSONString(info));
        if (jo != null) {
            jo.remove("id");
            jo.remove("zFDZ");
            jo.remove("hFJG");
            jo.remove("hFLDBM");
            jo.remove(PerReturnModle.sLYQZZP);
            jo.remove(PerReturnModle.sMPHZP);
            jo.remove(PerReturnModle.sQTZP);
            jo.put("CJYH", AppContext.instance.getLoginInfo().getUsername());
            jo.put("CJDW", AppContext.instance.getLoginInfo().getOrgancode());
            if (info.getIV_LYQZZP() != null) {
                jo.put(PerReturnModle.sLYQZZP, BaseUtil.getFileName(info.getIV_LYQZZP()));
            }

        }

        String json = jo.toJSONString();

        ApiResource.addPerInfo(json, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int i, Header[] headers, byte[] bytes) {
                String result = new String(bytes);

                if (!result.isEmpty() && result.startsWith("true")) {
                    //已上传记数+1；
                    uploadCount++;
                    //删除本地数据库中的记录

                    if (info.getIV_LYQZZP() != null) {
                        File plateImage = new File(info.getIV_LYQZZP());
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
                iv_lyqj.setImageBitmap(ImageUtils.compressImageByPixel(imagePath));
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
            et_dzh.setText(mLocation.getAddress());
            /**百度坐标，经纬度展现**/
            tv_zb.setText("(" + bWd + "," + bJD + ")");
            map_type = mLocation.getLoctype();
        }
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        unregisterReceiver(updateReceiver);
    }
    @Override
    public boolean enableBackUpBtn() {
        return true;
    }


    @Override
    public String getAtyTitle() {
        return getString(R.string.PER_RETURN);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        MenuItem  action_list = menu.findItem(R.id.action_list);
        MenuItem action_map = menu.findItem(R.id.action_map);
        action_map.setVisible(false);
        return true;
    }

    /***activity退出*/
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {

            case android.R.id.home:
                new BaseDataUtils().loginOut(PerReturnAty.this);
                break;

            /**缓存数据列表模式**/
            case R.id.action_list:
                Intent intent=new Intent(this,PerReturnLocalAty.class);
                startActivity(intent);
                break;

            default:
                break;
        }
        return true;
    }


    /**返回键退出*/
    @Override
    public boolean dispatchKeyEvent(KeyEvent event) {
        /**返回如果地图是显示，先隐藏地图*/
        if (event.getAction() == KeyEvent.ACTION_DOWN
                && event.getKeyCode() == KeyEvent.KEYCODE_BACK) {
            new BaseDataUtils().loginOut(PerReturnAty.this);
        }
        return super.dispatchKeyEvent(event);
    }

    protected void writePreferences(String perferencesName, String key,
                                    int value) {
        SharedPreferences preferences = getSharedPreferences(perferencesName,
                MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putInt(key, value);
        editor.commit();
    }
    public void checkCameraParameters() {
        // 读取支持的预览尺寸
        Camera camera = null;
        try {
            camera = Camera.open();
            if (camera != null) {
                // 读取支持的预览尺寸,优先选择640后320
                Camera.Parameters parameters = camera.getParameters();
                List<Integer> SupportedPreviewFormats = parameters
                        .getSupportedPreviewFormats();
                for (int i = 0; i < SupportedPreviewFormats.size(); i++) {
                    System.out.println("PreviewFormats="
                            + SupportedPreviewFormats.get(i));
                }
                Log.i("TAG",
                        "preview-size-values:"
                                + parameters.get("preview-size-values"));
                List<Camera.Size> previewSizes = splitSize(
                        parameters.get("preview-size-values"), camera);// parameters.getSupportedPreviewSizes();

                // 冒泡排序算法 实现分辨率从小到大排列
                // 该算法以分辨率的宽度为准，如果宽度相等，则判断高度
                int tempWidth = 0;
                int tempHeight = 0;
                for (int i = 0; i < previewSizes.size(); i++) {
                    for (int j = i + 1; j < previewSizes.size(); j++) {
                        if (previewSizes.get(i).width > previewSizes.get(j).width) {

                            tempWidth = previewSizes.get(i).width;
                            tempHeight = previewSizes.get(i).height;
                            previewSizes.get(i).width = previewSizes.get(j).width;
                            previewSizes.get(i).height = previewSizes.get(j).height;
                            previewSizes.get(j).width = tempWidth;
                            previewSizes.get(j).height = tempHeight;

                        } else if (previewSizes.get(i).width == previewSizes
                                .get(j).width
                                && previewSizes.get(i).height > previewSizes
                                .get(j).height) {
                            tempWidth = previewSizes.get(i).width;
                            tempHeight = previewSizes.get(i).height;
                            previewSizes.get(i).width = previewSizes.get(j).width;
                            previewSizes.get(i).height = previewSizes.get(j).height;
                            previewSizes.get(j).width = tempWidth;
                            previewSizes.get(j).height = tempHeight;
                        }
                    }
                }
                for (int i = 0; i < previewSizes.size(); i++) {
                    System.out.println("宽度:" + previewSizes.get(i).width + "--"
                            + "高度:" + previewSizes.get(i).height);
                }
                // 冒泡排序算法
                // 该段程序主要目的是为了遵循:优先选择比640*480大的并且是最接近的而且是比例为4:3的原则编写的。
                for (int i = 0; i < previewSizes.size(); i++) {
                    // 当预览宽度和高度分别大于640和480并且宽和高的比为4:3时。
                    if (previewSizes.get(i).width > 640
                            && previewSizes.get(i).height > 480
                            && (((float) previewSizes.get(i).width / previewSizes
                            .get(i).height) == (float) 4 / 3)) {
                        isCatchPreview = true;
                        WIDTH = previewSizes.get(i).width;
                        HEIGHT = previewSizes.get(i).height;
                        break;
                    }
                    // 如果在640*480前没有满足的值，WIDTH和HEIGHT就都为0，然后进行如下判断，看是否有640*480，如果有则赋值，如果没有则进行下一步验证。
                    if (previewSizes.get(i).width == 640
                            && previewSizes.get(i).height == 480 && WIDTH < 640
                            && HEIGHT < 480) {
                        isCatchPreview = true;
                        WIDTH = 640;
                        HEIGHT = 480;
                    }
                    if (previewSizes.get(i).width == 320
                            && previewSizes.get(i).height == 240 && WIDTH < 320
                            && HEIGHT < 240) {// 640 //480
                        isCatchPreview = true;
                        WIDTH = 320;
                        HEIGHT = 240;
                    }
                }
                Log.i("TAG", "isCatchPreview=" + isCatchPreview);

                // 读取支持的相机尺寸,优先选择1280后1600后2048
                List<Integer> SupportedPictureFormats = parameters
                        .getSupportedPictureFormats();
                for (int i = 0; i < SupportedPictureFormats.size(); i++) {
                    System.out.println("PictureFormats="
                            + SupportedPictureFormats.get(i));
                }
                Log.i("TAG",
                        "picture-size-values:"
                                + parameters.get("picture-size-values"));
                List<Camera.Size> PictureSizes = splitSize(
                        parameters.get("picture-size-values"), camera);// parameters.getSupportedPictureSizes();
                for (int i = 0; i < PictureSizes.size(); i++) {
//                    if (PictureSizes.get(i).width == 3264
//                            && PictureSizes.get(i).height == 1840) {
//                        // 优先选择小的照片分辨率
//                        isCatchPicture = true;
//                        srcwidth = 3264;
//                        srcheight = 1840;
//
//                    }
                    if (PictureSizes.get(i).width == 2048
                            && PictureSizes.get(i).height == 1536) {
                        // 优先选择小的照片分辨率
                        if ((srcwidth == 0 && srcheight == 0)
                                || (srcwidth > 2048 && srcheight > 1536)) {
                            isCatchPicture = true;
                            srcwidth = 2048;
                            srcheight = 1536;
                        }

                    }
                    if (PictureSizes.get(i).width == 1600
                            && PictureSizes.get(i).height == 1200) {
                        if ((srcwidth == 0 && srcheight == 0)
                                || (srcwidth > 1600 && srcheight > 1200)) {
                            isCatchPicture = true;
                            srcwidth = 1600;
                            srcheight = 1200;
                        }

                    }
                    if (PictureSizes.get(i).width == 1280
                            && PictureSizes.get(i).height == 960) {
                        if ((srcwidth == 0 && srcheight == 0)
                                || (srcwidth > 1280 && srcheight > 960)) {
                            isCatchPicture = true;
                            srcwidth = 1280;
                            srcheight = 960;
                        }
                    }
                    if (PictureSizes.get(i).width == 640
                            && PictureSizes.get(i).height == 480) {
                        if ((srcwidth == 0 && srcheight == 0)
                                || (srcwidth > 640 && srcheight > 480)) {
                            isCatchPicture = true;
                            srcwidth = 640;
                            srcheight = 480;
                        }
                    }
                }
                Log.i("TAG", "isCatchPicture=" + isCatchPicture);
            }
            camera.release();
            camera = null;
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (camera != null) {
                try {
                    camera.release();
                    camera = null;
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }

    private ArrayList<Camera.Size> splitSize(String str, Camera camera) {
        if (str == null)
            return null;
        StringTokenizer tokenizer = new StringTokenizer(str, ",");
        ArrayList<Camera.Size> sizeList = new ArrayList<Camera.Size>();
        while (tokenizer.hasMoreElements()) {
            Camera.Size size = strToSize(tokenizer.nextToken(), camera);
            if (size != null)
                sizeList.add(size);
        }
        if (sizeList.size() == 0)
            return null;
        return sizeList;
    }
    private Camera.Size strToSize(String str, Camera camera) {
        if (str == null)
            return null;
        int pos = str.indexOf('x');
        if (pos != -1) {
            String width = str.substring(0, pos);
            String height = str.substring(pos + 1);
            return camera.new Size(Integer.parseInt(width),
                    Integer.parseInt(height));
        }
        return null;
    }
    public int readMainID() {
        int mainID = 0;
        String cfgPath = Environment.getExternalStorageDirectory().toString()
                + "/AndroidWT/idcard.cfg";
        File cfgFile = new File(cfgPath);
        char[] buf = new char[14];
        if (!cfgFile.exists()) {
            return 0;
        } else {
            try {
                FileReader fr = new FileReader(cfgFile);
                fr.read(buf);
                String str = String.valueOf(buf);
                String[] splits = str.split("==##");
                mainID = Integer.valueOf(splits[0]);
                Log.i("TAG", "readMainID mainID=" + mainID);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return mainID;
    }

}