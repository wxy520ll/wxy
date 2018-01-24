package cn.net.xinyi.xmjt.activity.Plate;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.hardware.Camera;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.baidu.mapapi.search.geocode.GeoCoder;
import com.loopj.android.http.AsyncHttpResponseHandler;

import org.apache.http.Header;

import java.io.File;
import java.util.List;

import cn.net.xinyi.xmjt.R;
import cn.net.xinyi.xmjt.api.ApiResource;
import cn.net.xinyi.xmjt.api.RestUtils;
import cn.net.xinyi.xmjt.config.AppContext;
import cn.net.xinyi.xmjt.config.BaseActivity2;
import cn.net.xinyi.xmjt.model.PlateInfoModle;
import cn.net.xinyi.xmjt.utils.BaseUtil;
import cn.net.xinyi.xmjt.utils.DB.DBOperation;
import cn.net.xinyi.xmjt.utils.FileUtils;
import cn.net.xinyi.xmjt.utils.GeneralUtils;
import cn.net.xinyi.xmjt.utils.SharedPreferencesUtil;
import cn.net.xinyi.xmjt.utils.StringUtils;


public class PlateMainMenuActivity extends BaseActivity2 implements OnClickListener {
    private final static String TAG = "PlateMainMenuActivity";
    private LinearLayout menu_capture;
    private LinearLayout menu_upload;
    private LinearLayout menu_manage;
    private LinearLayout menu_count;

    // 获取本地视频列表
    List<PlateInfoModle> list;
    int networkType;
    boolean isGpsOpen;
    int uploadCount = 0;
    int uploadInforCount = 0;
    int noImageCount = 0;
    private GeoCoder geoCoder;
    private String address = null;

    private DBOperation dbo;
    private PlateInfoModle mPlateInf;
    private int id;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.plate_main_menu);
        initResource();
        getCameraInformation();

    }


    @Override
    public String getAtyTitle() {
        return getString(R.string.title_plate);
    }

    @Override
    public boolean enableBackUpBtn() {
        return true;
    }


    private void initResource() {
        if (!SharedPreferencesUtil.getBoolean(getActivity(),"iscolltips",false)){
            SharedPreferencesUtil.putBoolean(getActivity(),"iscolltips",true);
            BaseUtil.showDialog(getString(R.string.coll_tips),getActivity());
        }
        menu_capture = (LinearLayout) findViewById(R.id.ll_capture);
        menu_upload = (LinearLayout) findViewById(R.id.ll_upload);
        menu_manage = (LinearLayout) findViewById(R.id.ll_manage);
        menu_count = (LinearLayout) findViewById(R.id.ll_count);

        menu_capture.setOnClickListener(this);
        menu_upload.setOnClickListener(this);
        menu_manage.setOnClickListener(this);
        menu_count.setOnClickListener(this);

    }

    private ProgressDialog mProgressDialog = null;
    Handler handler = new Handler() {
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 0://上传前检测
                    mProgressDialog = new ProgressDialog(PlateMainMenuActivity.this);
                    mProgressDialog.setTitle("上传前检测");
                    mProgressDialog.setMessage("检测网络情况及APP是否最新版本");
                    mProgressDialog.setIndeterminate(false);
                    mProgressDialog.setCancelable(false);
                    mProgressDialog.show();
                    break;
                case 1:// 上传进度条显示
                    int count = msg.arg1;
                    String msgText = "本机共有 " + count + " 个车牌需要上传";
                    mProgressDialog = new ProgressDialog(PlateMainMenuActivity.this);
                    mProgressDialog.setProgress(count);
                    mProgressDialog.setMax(count);
                    mProgressDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
                    mProgressDialog.setTitle("车牌上传中");
                    mProgressDialog.setMessage(msgText);
                    mProgressDialog.setIndeterminate(false);
                    mProgressDialog.setCancelable(false);
                    mProgressDialog.show();
                    break;
                case 2:// 上传之后
                    mProgressDialog.cancel();
                    if (noImageCount>0){
                        BaseUtil.showDialog("当前"+noImageCount+"条数据上传失败，请检查图片是否丢失",PlateMainMenuActivity.this);
                    }else {
                        BaseUtil.showDialog("本次成功上传了" + msg.arg1 + "条采集信息",PlateMainMenuActivity.this);
                    }
                    break;
                case 3:// 上传失败
                    mProgressDialog.cancel();
                    if (noImageCount>0){
                        BaseUtil.showDialog("当前"+noImageCount+"条数据上传失败，请检查图片是否丢失",PlateMainMenuActivity.this);
                    }else {
                        BaseUtil.showDialog("上传失败，可能当前上传的人数较多，请稍候重试！",PlateMainMenuActivity.this);
                    }
                    break;

                case 4://上传检测完成
                    mProgressDialog.cancel();
                    if (msg.arg1 == 0) {
                        BaseUtil.showDialog("系统检测到当前APP 版本过低，请回到民警通主菜单点击【系统设置】-【APP 更新】，按提示升级APP 后重新上传！", PlateMainMenuActivity.this);
                    } else {
                        startUploadThread();
                    }
                    break;

                case 5://上传检测失败
                    mProgressDialog.cancel();
                    if (msg.arg1 == 0) {
                        BaseUtil.showDialog("系统检测到你当前的WIFI网络无法连接互联网或不稳定，无法上传，请检查网络正常后重新上传！", PlateMainMenuActivity.this);
                    } else {
                        BaseUtil.showDialog("检测失败！", PlateMainMenuActivity.this);
                    }
                    break;


                case 6:// 上传进度条显示
                    int count1 = msg.arg1;
                    String msgText1 = "本机共有 " + count1 + " 个数据需要更新";
                    mProgressDialog = new ProgressDialog(PlateMainMenuActivity.this);
                    mProgressDialog.setProgress(count1);
                    mProgressDialog.setMax(count1);
                    mProgressDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
                    mProgressDialog.setTitle("数据上传中");
                    mProgressDialog.setMessage(msgText1);
                    mProgressDialog.setIndeterminate(false);
                    mProgressDialog.setCancelable(false);
                    mProgressDialog.show();
                    break;
                case 7:// 上传之后
                    mProgressDialog.cancel();
                    BaseUtil.showDialog("本次更新了" + msg.arg1 + "条数据", PlateMainMenuActivity.this);
                    break;
                case 8:// 上传失败
                    mProgressDialog.cancel();
                    BaseUtil.showDialog("更新失败，请稍候重试！", PlateMainMenuActivity.this);
                    break;


                default:
                    break;
            }
        }
    };


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.ll_capture:
                cpcj();
                break;
            case R.id.ll_upload:
                //检测本地是否有未上传的车牌
                list = getLocalData();
                if (list == null || list.size() == 0) {
                    BaseUtil.showDialog("没有需要上传的车牌！", PlateMainMenuActivity.this);
                    break;
                }

                //检测是否连接wifi网络
                networkType = ((AppContext) getApplication()).getNetworkType();
                if (networkType == 0) {
                    BaseUtil.showDialog("当前无可用的网络连接，无法上传", PlateMainMenuActivity.this);
                    break;
                }
                //只允许在 WIFI 下上传
                if (networkType != 1) {
                    BaseUtil.showDialog("只允许在 WIFI 网络下上传", PlateMainMenuActivity.this);
                    break;
                }
                //开始上传
                diaoShow();
                break;
            case R.id.ll_manage:
                showManage(PlateMainMenuActivity.this);
                break;
            case R.id.ll_count:
                showPlateCount(PlateMainMenuActivity.this);
                break;
            default:
                break;
        }
    }

    private void cpcj() {
        networkType = ((AppContext) getApplication()).getNetworkType();
        isGpsOpen = ((AppContext) getApplication()).isGpsOPen();

        if (networkType == 0) {
            if (isGpsOpen) {
                Toast.makeText(PlateMainMenuActivity.this, "当前无网络连接，定位方式为GPS定位",
                        Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(PlateMainMenuActivity.this, "GPS未启用或搜索不到信号,无法定位采集坐标",
                        Toast.LENGTH_SHORT).show();
            }
        }
        if (!((AppContext) getApplication()).checkCameraHardware()) {
            BaseUtil.showDialog("本设备没有摄像头", PlateMainMenuActivity.this);
        } else if (readIntPreferences("PlateService", "picWidth") != 0 && readIntPreferences("PlateService", "picHeight") != 0
                && readIntPreferences("PlateService", "preWidth") != 0 && readIntPreferences("PlateService", "preHeight") != 0
                && readIntPreferences("PlateService", "preMaxWidth") != 0 && readIntPreferences("PlateService", "preMaxHeight") != 0) {
            showCamera(PlateMainMenuActivity.this);
        } else {
            BaseUtil.showDialog("采集车牌需要您开启摄像头访问权限，请到设备的【设置】-【权限管理】-【应用程序】-【民警通】界面，勾选【信任此应用程序】，开启权限", PlateMainMenuActivity.this);
        }
    }


    private void uploadInfomation() {
        uploadInforCount = 0;
        String url = ApiResource.getAddress(mPlateInf.getLatitude(), mPlateInf.getLongitude());
        //String  url = "http://183.62.140.8:8080/xsmws-web/api/v1.0/contacts";
        String result = RestUtils.postJson(url, null);
        noImageCount++;
    }


    private void diaoShow() {
        AlertDialog dialog = new AlertDialog.Builder(PlateMainMenuActivity.this).
                setTitle(getString(R.string.tips)).setMessage("确定上传车牌？")
                .setPositiveButton(R.string.sure, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        Message msg = new Message();
                        msg.what = 0;
                        handler.sendMessage(msg);

                        //检查APP是否最新版本及网络是否连通
                        new Thread() {
                            @Override
                            public void run() {
                                ApiResource.getVersionByAppid(AppContext.APP_ID, new AsyncHttpResponseHandler() {
                                    @Override
                                    public void onSuccess(int i, Header[] headers, byte[] bytes) {
                                        Message msg = new Message();
                                        try {
                                            String result = new String(bytes);
                                            if (result != null && result.trim() != "") {
                                                //获取服务器端版本号
                                                JSONObject jo_v = JSONObject.parseObject(result);
                                                int newVersionCode = Integer.parseInt(jo_v.getString(GeneralUtils.BUILDNUMBER));

                                                //获取当前版本号
                                                PackageInfo info = getApplicationContext().getPackageManager().getPackageInfo(getPackageName(), 0);
                                                int curVersionCode = info.versionCode;

                                                if (newVersionCode > curVersionCode) {
                                                    //有新版本
                                                    msg.arg1 = 0;
                                                } else {
                                                    msg.arg1 = 1;
                                                }
                                                msg.what = 4;
                                                handler.sendMessage(msg);
                                            } else {
                                                onFailure(i, headers, bytes, null);
                                            }
                                        } catch (Exception e1) {
                                            onFailure(i, headers, bytes, null);
                                        }
                                    }

                                    @Override
                                    public void onFailure(int i, Header[] headers, byte[] bytes, Throwable throwable) {
                                        Message msg = new Message();
                                        msg.what = 5;
                                        if (i == 0) {
                                            //检测超时或当前网络不能连接到互联网
                                            msg.arg1 = 0;
                                        } else {
                                            msg.arg1 = 1;
                                        }
                                        handler.sendMessage(msg);
                                    }
                                });
                            }
                        }.start();
                    }
                }).setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
//                        dialog.dismiss();
                        dialog.cancel();
                    }
                }).create();
        dialog.show();

    }

    private void startUploadThread() {
        // 保存与上传
        new Thread() {
            public void run() {
                Message msg = new Message();
                msg.what = 1;
                msg.arg1 = list.size();
                handler.sendMessage(msg);
                // 将数据及视频上传到服务器
                uploadPlateImage(list);
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

    /**
     * 获取本地数据库保存的车牌数据
     *
     * @return
     */
    private List<PlateInfoModle> getLocalData() {

        String account = AppContext.instance.getLoginInfo().getUsername();
        String polno = AppContext.instance.getLoginInfo().getPoliceno();
        if (account.isEmpty())
            return null;

        DBOperation dbo = new DBOperation(this);
        List<PlateInfoModle> list = dbo.getPlateInfoList(account, polno);
        dbo.clossDb();
        return list;
    }

    /**
     * 删除本地数据库中已上传车牌
     *
     * @param id
     * @return
     */
    private boolean delPlateInfo(int id) {
        boolean mFlag = false;
        DBOperation dbo = new DBOperation(this);
        mFlag = dbo.delPlateInfo(id);
        dbo.clossDb();

        return mFlag;
    }


    //同步上传车牌图片到服务端
    void uploadPlateImage(List<PlateInfoModle> list) {
        uploadCount = 0;
        noImageCount = 0;

        for (int i = 0; i < list.size(); i++) {
            final PlateInfoModle mPlateInfo = list.get(i);
            if (!StringUtils.isEmpty(mPlateInfo.getPlateImage())&& FileUtils.fileIsExists(mPlateInfo.getPlateImage())){
                String filePath = mPlateInfo.getPlateImage();
                String fileName = BaseUtil.getFileNameNoEx(filePath);
                //上传车牌图片
                ApiResource.uploadCapturePlateImage(fileName, filePath, new AsyncHttpResponseHandler() {
                    @Override
                    public void onSuccess(final int i, final Header[] headers, final byte[] bytes) {
                        String result = new String(bytes);
                        if (i == 200 && result != null && result.startsWith("true")) {
                            uploadPlateInfo(mPlateInfo);
                        } else {
                            onFailure(i, headers, bytes, null);
                        }
                    }

                    @Override
                    public void onFailure(final int i, final Header[] headers, final byte[] bytes, final Throwable throwable) {
                        if (bytes != null) {
                            String result = new String(bytes);
                            Log.d(TAG, result);
                        }
                    }
                });
            }else {
                noImageCount++;
            }
        }
    }

    //同步上传车牌数据到服务端
    public void uploadPlateInfo(final PlateInfoModle info) {

        String pcs = AppContext.instance.getLoginInfo().getPcs();
        String jws = AppContext.instance.getLoginInfo().getJws();

        //json处理
        JSONObject jo = JSON.parseObject(JSON.toJSONString(info));
        if (jo != null) {
            jo.remove("id");
            jo.remove("plateImage");
            jo.remove("plateThumb");
            jo.put("plateImage", BaseUtil.getFileName(info.getPlateImage()));
            jo.put("pcs", pcs);
            jo.put("jws", jws);
        }

        String json = jo.toJSONString();
        //保存上传日志
        //AppConfig.saveUploadLog(json);

        ApiResource.addCapturePlate(json, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int i, Header[] headers, byte[] bytes) {
                String result = new String(bytes);

                if (!result.isEmpty() && result.startsWith("true")) {
                    //已上传记数+1；
                    uploadCount++;
                    //删除本地数据库中的记录
                    boolean flag = delPlateInfo(info.getId());

                    if (flag) {
                        //删除本地图片
                        File plateImage = new File(info.getPlateImage());
                        File plateImageThub = new File(info.getPlateThumb());
                        if (plateImage.exists()) {
                            plateImage.delete();
                        }
                        if (plateImageThub.exists()) {
                            plateImageThub.delete();
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
                    Log.d(TAG, result);
                }
            }
        });
    }

    public void showCamera(Context context) {
        Intent intent = new Intent(context, CameraActivity.class);
        context.startActivity(intent);
    }

    public void showManage(Context context) {
        Intent intent = new Intent(context, PlateListActivity.class);
        context.startActivity(intent);
    }

    public void showPlateCount(Context context) {
        Intent intent = new Intent(context, PlateCountMenuActivity.class);
        context.startActivity(intent);
    }



    /**
     * 获取设备下面的硬件信息，基本的拍照分辨率，预览分辨率
     */
    private void getCameraInformation() {
        if (readIntPreferences("PlateService", "picWidth") == 0
                || readIntPreferences("PlateService", "picHeight") == 0
                || readIntPreferences("PlateService", "preWidth") == 0
                || readIntPreferences("PlateService", "preHeight") == 0
                || readIntPreferences("PlateService", "preMaxWidth") == 0
                || readIntPreferences("PlateService", "preMaxHeight") == 0) {

            Camera camera = null;
            int pre_Max_Width = 640;
            int pre_Max_Height = 480;
            final int Max_Width = 2048;
            final int Max_Height = 1536;
            boolean isCatchPicture = false;
            int picWidth = 2048;
            int picHeight = 1536;
            int preWidth = 320;
            int preHeight = 240;
            try {
                camera = Camera.open();
                if (camera != null) {
                    Camera.Parameters parameters = camera.getParameters();
                    List<Camera.Size> previewSizes = parameters
                            .getSupportedPreviewSizes();
                    Camera.Size size;
                    int second_Pre_Width = 0, second_Pre_Height = 0;
                    int length = previewSizes.size();
                    if (length == 1) {
                        size = previewSizes.get(0);
                        pre_Max_Width = size.width;
                        pre_Max_Height = size.height;
                    } else {
                        for (int i = 0; i < length; i++) {
                            size = previewSizes.get(i);
                            if (size.width <= Max_Width
                                    && size.height <= Max_Height) {
                                second_Pre_Width = size.width;
                                second_Pre_Height = size.height;
                                if (pre_Max_Width < second_Pre_Width) {
                                    pre_Max_Width = second_Pre_Width;
                                    pre_Max_Height = second_Pre_Height;
                                }
                            }
                        }
                    }

                    for (int i = 0; i < previewSizes.size(); i++) {
                        if (previewSizes.get(i).width == 640
                                && previewSizes.get(i).height == 480) {
                            preWidth = 640;
                            preHeight = 480;
                            break;
                        }
                        if (previewSizes.get(i).width == 320
                                && previewSizes.get(i).height == 240) {
                            preWidth = 320;
                            preHeight = 240;
                        }
                    }
                    if (preWidth == 0 || preHeight == 0) {
                        if (previewSizes.size() == 1) {
                            preWidth = previewSizes.get(0).width;
                            preHeight = previewSizes.get(0).height;
                        } else {
                            preWidth = previewSizes
                                    .get(previewSizes.size() / 2).width;
                            preHeight = previewSizes
                                    .get(previewSizes.size() / 2).height;
                        }
                    }

                    List<Camera.Size> PictureSizes = parameters
                            .getSupportedPictureSizes();
                    for (int i = 0; i < PictureSizes.size(); i++) {
                        if (PictureSizes.get(i).width == 2048
                                && PictureSizes.get(i).height == 1536) {
                            if (isCatchPicture == true) {
                                break;
                            }
                            isCatchPicture = true;
                            picWidth = 2048;
                            picHeight = 1536;
                        }
                        if (PictureSizes.get(i).width == 1600
                                && PictureSizes.get(i).height == 1200) {
                            isCatchPicture = true;
                            picWidth = 1600;
                            picHeight = 1200;
                        }
                        if (PictureSizes.get(i).width == 1280
                                && PictureSizes.get(i).height == 960) {
                            isCatchPicture = true;
                            picWidth = 1280;
                            picHeight = 960;
                            break;
                        }
                    }
                }

                writeIntPreferences("PlateService", "picWidth", picWidth);
                writeIntPreferences("PlateService", "picHeight", picHeight);
                writeIntPreferences("PlateService", "preWidth", preWidth);
                writeIntPreferences("PlateService", "preHeight", preHeight);
                writeIntPreferences("PlateService", "preMaxWidth",
                        pre_Max_Width);
                writeIntPreferences("PlateService", "preMaxHeight",
                        pre_Max_Height);
            } catch (Exception e) {

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
    }

    public int readIntPreferences(String perferencesName, String key) {
        SharedPreferences preferences = getSharedPreferences(perferencesName,
                MODE_PRIVATE);
        int result = preferences.getInt(key, 0);
        return result;
    }

    public void writeIntPreferences(String perferencesName, String key,
                                    int value) {
        SharedPreferences preferences = getSharedPreferences(perferencesName,
                MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putInt(key, value);
        editor.commit();
    }
}
