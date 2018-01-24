package cn.net.xinyi.xmjt.activity.Collection.Camera;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Matrix;
import android.media.ThumbnailUtils;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;
import android.text.Editable;
import android.text.TextWatcher;
import android.text.format.DateFormat;
import android.view.KeyEvent;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.baidu.location.BDLocation;
import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.MapView;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.nostra13.universalimageloader.core.ImageLoader;

import org.apache.http.Header;

import java.io.File;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Map;

import cn.net.xinyi.xmjt.R;
import cn.net.xinyi.xmjt.api.ApiHttpClient;
import cn.net.xinyi.xmjt.api.ApiResource;
import cn.net.xinyi.xmjt.config.AppContext;
import cn.net.xinyi.xmjt.config.BaseActivity;
import cn.net.xinyi.xmjt.model.JKSInfoModle;
import cn.net.xinyi.xmjt.model.MapDataModle;
import cn.net.xinyi.xmjt.model.Presenter.MapPresenter;
import cn.net.xinyi.xmjt.model.View.IMapDataView;
import cn.net.xinyi.xmjt.utils.AnnotateManager;
import cn.net.xinyi.xmjt.utils.BaseUtil;
import cn.net.xinyi.xmjt.utils.BindView;
import cn.net.xinyi.xmjt.utils.DB.DBOperation;
import cn.net.xinyi.xmjt.utils.GeneralUtils;
import cn.net.xinyi.xmjt.utils.GetLocation;
import cn.net.xinyi.xmjt.utils.ImageUtils;
import cn.net.xinyi.xmjt.utils.PositionUtil;

/**
 * Created by hao.zhou on 2015/9/18.
 * 监控室编辑页面
 */
public class JKSEditorActivity extends BaseActivity implements View.OnClickListener,IMapDataView {
    private ProgressDialog mProgressDialog = null;
    private String path1=null,path2=null,path3=null;
    private int networkType;
    private final int CAMERA_INTENT_REQUEST = 0XFF02;
    private int fla=0;
    private JKSInfoModle jKSInfo;
    private Calendar c=Calendar.getInstance();
    private boolean bUpload = false;
    private DBOperation dbo;
    private ArrayList<String> wangge = new ArrayList<String>();
    private static int image_width = 400;
    private static int image_height = 300;
    private Bitmap bitmap;
    private String imagePath;
    private boolean flag = false;
    private MapView mMapView;  // 地图标注及定位
    BaiduMap mBaiduMap;
    private int uploadCount=0;
    private boolean mapFlag=false;
    /**
     * 备注  et_BZ
     * 监控室名称  et_JKSMC
     * 联系电话  et_LXDH
     * 责任人  et_ZRR
     * 持证从业人数    et_CZSGRS
     * 安装时间  tv_AZSJ
     * */
    @BindView(id = R.id.et_remarks,click = false)
    EditText et_BZ;
    @BindView(id = R.id.et_name,click = false)
    EditText et_JKSMC;
    @BindView(id = R.id.et_phone,click = false)
    EditText et_LXDH;
    @BindView(id = R.id.et_trustee,click = false)
    EditText et_ZRR;
    @BindView(id = R.id.et_certificates_number,click = false)
    EditText et_CZSGRS;
    @BindView(id = R.id.tv_collection_azdata_jks,click = true)
    TextView tv_AZSJ;
    /**
     * 正常摄像头数量  et_ZCSYSXTSL
     * 摄像头数量  et_SXTSL
     * 楼栋编码  et_LDBH
     * 设备存储公司  et_SPCZCS
     * 监控室位置  tv_JKSWZ
     * 业务类型  tv_ywfl
     * */
    @BindView(id = R.id.et_camera_num_normal,click = false)
    EditText et_ZCSYSXTSL;
    @BindView(id = R.id.et_camera_num,click = false)
    EditText et_SXTSL;
    @BindView(id = R.id.et_block_number,click = false)
    EditText et_LDBH;
    @BindView(id = R.id.et_save_company,click = false)
    EditText et_SPCZCS;
    @BindView(id = R.id.tv_collection_posioion_jks,click = false)
    EditText tv_JKSWZ;
    @BindView(id = R.id.tv_ywfl,click = true)
    TextView tv_YWFL;
    /**
     * 上传监控室的三张图片  iv_ZP1  iv_ZP2  iv_ZP3
     * 缓存本地  btn_savelocal
     * //所属网格  tv_wangge
     * 所在派出所  tv_collecion_pcs
     * 手动刷新  tv_position_shoudong
     * 坐标显示  tv_gps_jks
     * 手动设置坐标  tv_position_shoudong
     * */
    @BindView(id = R.id.iv_03,click = true)
    ImageView iv_ZP3;
    @BindView(id = R.id.iv_02,click = true)
    ImageView iv_ZP2;
    @BindView(id = R.id.iv_01,click = true)
    ImageView iv_ZP1;

    /** * 手动定位需要隐藏的布局*/
    @BindView(id = R.id.sv_collection,click = false)
    ScrollView sv_collection;
    @BindView(id = R.id.btn_savelocal,click = true)
    Button btn_savelocal;
    @BindView(id = R.id.tv_wangge,click = true)
    TextView tv_wangge;
    @BindView(id = R.id.tv_collecion_pcs,click = true)
    TextView tv_collecion_pcs;
    @BindView(id = R.id.tv_collection_gps_jks,click = false)
    TextView tv_gps_jks;
    @BindView(id = R.id.tv_collection_position_shoudong,click = true)
    TextView tv_position_shoudong;
    private double bWd;
    private double bJD;
    private BDLocation map_location;
    private int map_type;
    private String address;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.camera_room_info);
        AnnotateManager.initBindView(this);
        getActionBar().setIcon(R.drawable.actionbar_back_icon_normal);
        getActionBar().setTitle("编辑监控室信息");
        getActionBar().setHomeButtonEnabled(true);

        /** * WatchJKSInfoActivity  传递的监控室信息*/
        jKSInfo =(JKSInfoModle)getIntent().getSerializableExtra(GeneralUtils.JKSInfo);
        /** * 初始化控件*/
        initViews();
        /** * 加载数据*/
        initData();
    }

    private void initViews() {
        /** * 百度地图初始化*/
        initMapView();
        /** *百度地图请求当前位置信息  */
        new GetLocation(this, handler).startLocation();
        et_JKSMC.setEnabled(false);
        iv_ZP1.setImageResource(R.drawable.loading_pic);
        iv_ZP2.setImageResource(R.drawable.loading_pic);
        iv_ZP3.setImageResource(R.drawable.loading_pic);
        /** * 网络状态获取*/
        networkType = ((AppContext) getApplication()).getNetworkType();
        /** *ProgressDialog初始化 */
        mProgressDialog=new ProgressDialog(JKSEditorActivity.this);

        /** * 核查监控室不通过
         * 1.更改编辑的名字为-“确认提交”
         * 2.暂时限制照片重拍*/
        if (getIntent().getFlags() == GeneralUtils.CheckFaileJKSInfoActivity){
            btn_savelocal.setText("确认提交");
            iv_ZP1.setEnabled(false);
            iv_ZP2.setEnabled(false);
            iv_ZP3.setEnabled(false);
            /** *如果核查的备注说明中包含位置或者地址不对  需要更新地址 */
            if (jKSInfo.getSHSBSM()!=null
                    &&(jKSInfo.getSHSBSM().contains("位置")
                    ||jKSInfo.getSHSBSM().contains("地址")
                    ||jKSInfo.getSHSBSM().contains("坐标")
                    ||jKSInfo.getSHSBYY().contains("地址"))){
                /** *百度地图请求当前位置信息  */
                new GetLocation(this, handler).startLocation();
                /***mapFlag 为true的时候  需要对控件 地址。坐标值进行更新 */
                mapFlag=true;
            }
        }

        /** * 监控室位置限制不超过50个字*/
        tv_JKSWZ.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.length() > 49) {
                    BaseUtil.showDialog("当前文本过长！", JKSEditorActivity.this);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            /** *网格  */
            case R.id.tv_wangge:
                wangge.clear();
                /** *所属网格-字符串转为集合*/
                String Sswg = AppContext.instance.getLoginInfo().getSswg();
                for (int i = 0; i < Sswg.split(",").length; i++) {
                    wangge.add(Sswg.split(",")[i]);
                }

                if (wangge.size()==0){
                    tv_wangge.setText(R.string.msg_null);
                }else {
                    /** *选择网格数据*/
                    new AlertDialog.Builder(this).setItems(wangge.toArray(new String[wangge.size()]),
                            new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    tv_wangge.setText(wangge.toArray(new String[wangge.size()])[which]);
                                }
                            }).create().show();}
                break;

            /** *  1,2，3*/
            case R.id.iv_01:
                fla=1;
                cameraPhoto();
                break;

            case R.id.iv_02:
                fla=2;
                cameraPhoto();
                break;

            case R.id.iv_03:
                fla=3;
                cameraPhoto();
                break;

            /** *手动定位  */
            case R.id.tv_collection_position_shoudong:
                /**获得地址信息**/
                tv_JKSWZ.setText(address);
                /**百度坐标，经纬度展现**/
                tv_gps_jks.setText("(" + bWd + "," + bJD + ")");
                /**
                 * *地图上标识当前所处的位置
                 * 如果没有获取到坐标  默认定位到龙岗中心城*/
                new GetLocation(this).mapLocation(map_location, mBaiduMap);
                /***手动定位类型*/
                flag = true;
                showMapView(flag);
                break;

            /** * 此控件复用
             * 1.本地缓存编辑，保存
             * 2.监控室未审核修改,提交部分
             * */
            case R.id.btn_savelocal:
                if (jKSInfo.getZP1()==null||jKSInfo.getZP2()==null||jKSInfo.getZP3()==null) {
                    BaseUtil.showDialog("请上传3张监控室图片", JKSEditorActivity.this);
                    break;
                }

                if (et_JKSMC.getText().toString().trim().isEmpty()) {
                    BaseUtil.showDialog("监控室的名称不能为空", JKSEditorActivity.this);
                    break;
                }
                if (tv_YWFL.getText().toString().trim().isEmpty()) {
                    BaseUtil.showDialog("业务分类不能为空", JKSEditorActivity.this);
                    break;
                }

                if (tv_JKSWZ.getText().toString().trim().isEmpty()) {
                    BaseUtil.showDialog("监控室所在位置不能为空", JKSEditorActivity.this);
                    break;
                }
                if (et_LDBH.getText().toString().trim().isEmpty()||
                        et_LDBH.getText().toString().trim().length()!=19) {
                    BaseUtil.showDialog("请输入19位正确楼栋编号", JKSEditorActivity.this);
                    break;
                }

                if (tv_AZSJ.getText().toString().trim().isEmpty()) {
                    BaseUtil.showDialog("安装时间不能为空", JKSEditorActivity.this);
                    break;
                }
                if (et_SXTSL.getText().toString().trim().isEmpty()) {
                    BaseUtil.showDialog("摄像头数量不能为空", JKSEditorActivity.this);
                    break;
                }
                if (et_ZCSYSXTSL.getText().toString().trim().isEmpty()) {
                    BaseUtil.showDialog("正常使用的摄像头数量不能为空", JKSEditorActivity.this);
                    break;
                }
                if (et_CZSGRS.getText().toString().trim().isEmpty()) {
                    BaseUtil.showDialog("持证上岗人数不能为空", JKSEditorActivity.this);
                    break;
                }
                if (et_ZRR.getText().toString().trim().isEmpty()) {
                    BaseUtil.showDialog("责任人不能为空", JKSEditorActivity.this);
                    break;
                }
                if (et_LXDH.getText().toString().trim().isEmpty()
                        ||et_LXDH.getText().toString().trim().length()<11) {
                    BaseUtil.showDialog("请输入正确联系电话", JKSEditorActivity.this);
                    break;
                }

                if (tv_wangge.getText().toString().trim().isEmpty()) {
                    BaseUtil.showDialog("所属网格不能为空", JKSEditorActivity.this);
                    break;
                }

                /** *监控室信息 审核未通过，修改提交 */
                if (getIntent().getFlags() == GeneralUtils.CheckFaileJKSInfoActivity){
                    uploadJKSInfoThread();
                }else {
                    /** *监控室信息 本地缓存修改编辑 */
                    bUpload = updateToLocalDB(JKSInfoModle.ISUPDATE_LOCAL);
                    if (bUpload) {
                        /** *编辑成功返回监控室列表页面*/
                        Toast.makeText(JKSEditorActivity.this, "编辑成功！",Toast.LENGTH_SHORT).show();
                        Intent intent=new Intent(JKSEditorActivity.this,ManageJKSInfoActivity.class);
                        startActivity(intent);
                        ManageJKSInfoActivity.intence.finish();
                        WatchJKSInfoActivity.intence.finish();
                        this.finish();
                    } else {
                        BaseUtil.showDialog("保存失败", JKSEditorActivity.this);
                    }
                }
                break;

            /** * 选择业务分类，界面显示中文，转为int类型 业务类型编码 */
            case R.id.tv_ywfl:
                final Map<String,String> jdMaps=zdUtils.getZdlbToZdz(GeneralUtils.XXCJ_JKSYWFL);
                new AlertDialog.Builder(this).setItems(jdMaps.values().toArray(new String[jdMaps.values().size()]),
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                tv_YWFL.setText(jdMaps.values().toArray(new String[jdMaps.values().size()])[which]);
                                jKSInfo.setJKSYWFLBM(zdUtils.getMapKey(jdMaps,tv_YWFL.getText().toString()));
                            }
                        }).create().show();

                break;

            /** *安装时间*/
            case R.id.tv_collection_azdata_jks:
                new DatePickerDialog(
                        JKSEditorActivity.this, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view,
                                          int year, int monthOfYear,
                                          int dayOfMonth) {
                        c.set(year, monthOfYear, dayOfMonth);
                        CharSequence forma= DateFormat.format("yyyy-MM-dd", c);
                        tv_AZSJ.setText(forma);
                    }
                }, c.get(Calendar.YEAR), c .get(Calendar.MONTH), c
                        .get(Calendar.DAY_OF_MONTH)).show();
                break;
        }
    }


    /** *更新数据库 监控室缓存信息  */
    private boolean updateToLocalDB(int isupdateLocal) {
        if (path1!=null)
            jKSInfo.setZP1(path1);
        if (path2!=null)
            jKSInfo.setZP2(path2);
        if (path3!=null)
            jKSInfo.setZP3(path3);
        updateJKSInfo();
        long rows = 0;
        // 本地保存
        dbo = new DBOperation(JKSEditorActivity.this);
        try {
            rows = dbo.UpdateCamearRoomInfo(jKSInfo.getId(), jKSInfo);
        } catch (Exception e) {
            e.printStackTrace();
        }
        dbo.clossDb();
        return rows != 0;
    }
    /** *判断控件值变化 重新赋值 */
    private void updateJKSInfo() {
        if (et_JKSMC.getText().toString().trim()!= jKSInfo.getJKSMC()){
            jKSInfo.setJKSMC(et_JKSMC.getText().toString().trim());
        }
        if (et_LDBH.getText().toString().trim()!= jKSInfo.getLDBH()){
            jKSInfo.setLDBH(et_LDBH.getText().toString().trim());
        }
        if (Integer.valueOf(et_SXTSL.getText().toString().trim())!= jKSInfo.getSXTSL()) {
            jKSInfo.setSXTSL(Integer.valueOf(et_SXTSL.getText().toString().trim()));
        }
        if (Integer.valueOf(et_ZCSYSXTSL.getText().toString().trim())!= jKSInfo.getZCSYSXTSL()) {
            jKSInfo.setZCSYSXTSL(Integer.valueOf(et_ZCSYSXTSL.getText().toString().trim()));
        }
        if (tv_AZSJ.getText().toString().trim()!= jKSInfo.getAZSJ()){
            jKSInfo.setAZSJ(tv_AZSJ.getText().toString().trim());
        }
        if (Integer.valueOf(et_CZSGRS.getText().toString().trim())!= jKSInfo.getCZSGRS()){
            jKSInfo.setCZSGRS(Integer.valueOf(et_CZSGRS.getText().toString().trim()));
        }
        if (et_ZRR.getText().toString().trim()!= jKSInfo.getZRR()){
            jKSInfo.setZRR(et_ZRR.getText().toString().trim());
        }
        if (et_LXDH.getText().toString().trim()!= jKSInfo.getLXDH()){
            jKSInfo.setLXDH(et_LXDH.getText().toString().trim());
        }
        if (tv_collecion_pcs.getText().toString().trim()!= jKSInfo.getSSPCS()){
            jKSInfo.setSSPCS(tv_collecion_pcs.getText().toString().trim());
        }
        if (tv_wangge.getText().toString().trim()!= jKSInfo.getSSWG()) {
            jKSInfo.setSSWG(tv_wangge.getText().toString().trim());
        }
        if (et_SPCZCS.getText().toString().trim()!= jKSInfo.getSPCZCS()) {
            jKSInfo.setSPCZCS(et_SPCZCS.getText().toString().trim());
        }
        if (tv_YWFL.getText().toString().trim()!= jKSInfo.getJKSYWFL()) {
            jKSInfo.setJKSYWFL(tv_YWFL.getText().toString().trim());
        }
        jKSInfo.setBZ(et_BZ.getText().toString().trim());
        jKSInfo.setIsupdate(String.valueOf(JKSInfoModle.ISUPDATE_LOCAL));
        if (tv_JKSWZ.getText().toString()!= jKSInfo.getJKSWZ()) {
            jKSInfo.setJKSWZ(tv_JKSWZ.getText().toString().trim());

        }
        if ("" + map_type != jKSInfo.getLOCTYPE()){
            jKSInfo.setLOCTYPE(""+map_type);
            jKSInfo.setWD("" + bWd);
            jKSInfo.setJD("" + bJD);
        }


    }

    /** *数据库加载数据
     * 1.getintent 判断来自下载的数据，需要使用imageload加载图片
     * 2.本地缓存信息加载
     * */
    private void initData() {
        if (getIntent().getFlags() == GeneralUtils.CheckJKSInfo_flag
                ||getIntent().getFlags() == GeneralUtils.DownJKSInfoActivity
                ||getIntent().getFlags() == GeneralUtils.CheckFaileJKSInfoActivity) {
            /** *将时间转换为yy-MM-dd*/
            String date=ImageUtils.ImageLoad(jKSInfo.getSCSJ());
            ImageLoader.getInstance().displayImage(ApiHttpClient.IMAGE_HOST + "/jks/" + date + "/" + jKSInfo.getZP1(), iv_ZP1);
            ImageLoader.getInstance().displayImage(ApiHttpClient.IMAGE_HOST + "/jks/" + date + "/" + jKSInfo.getZP2(), iv_ZP2);
            ImageLoader.getInstance().displayImage(ApiHttpClient.IMAGE_HOST + "/jks/" + date + "/" + jKSInfo.getZP3(), iv_ZP3);
        }else {
            if (jKSInfo.getZP1()!=null){
                path1= jKSInfo.getZP1();
                iv_ZP1.setImageBitmap(ImageUtils.decodeFile(path1));
            }
            if (jKSInfo.getZP2()!=null){
                path2= jKSInfo.getZP2();
                iv_ZP2.setImageBitmap(ImageUtils.decodeFile(path2));
            }
            if (jKSInfo.getZP3()!=null){
                path3= jKSInfo.getZP3();
                iv_ZP3.setImageBitmap(ImageUtils.decodeFile(path3));
            }
        }
        tv_gps_jks.setText("(" + jKSInfo.getWD() + "," + jKSInfo.getJD() + ")");
        et_BZ.setText(jKSInfo.getBZ());
        et_JKSMC.setText(jKSInfo.getJKSMC());
        tv_wangge.setText(jKSInfo.getSSWG());
        et_LXDH.setText(jKSInfo.getLXDH());
        et_ZRR.setText(jKSInfo.getZRR());
        et_SPCZCS.setText(jKSInfo.getSPCZCS());
        tv_YWFL.setText(jKSInfo.getJKSYWFL());
        et_CZSGRS.setText(String.valueOf(jKSInfo.getCZSGRS()));
        et_SXTSL.setText(String.valueOf(jKSInfo.getSXTSL()));
        et_ZCSYSXTSL.setText(String.valueOf(jKSInfo.getZCSYSXTSL()));
        et_LDBH.setText(jKSInfo.getLDBH());
        tv_AZSJ.setText(jKSInfo.getAZSJ());
        tv_JKSWZ.setText(jKSInfo.getJKSWZ());
        tv_collecion_pcs.setText(jKSInfo.getSSPCS());
    }

    /** * 启动一个线程上传监控室信息*/
    private void uploadJKSInfoThread() {
        /** * 保存与上传*/
        new Thread() {
            public void run() {
                Message msg = new Message();
                msg.what = 1;
                handler.sendMessage(msg);
                uploadJKSInfo();
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

    /** *上传核查不通过监控室  编辑后的信息*/
    private void uploadJKSInfo() {
        /***获取编辑的信息*/
        updateJKSInfo();
        //json处理
        JSONObject jo = JSON.parseObject(JSON.toJSONString(jKSInfo));
        jo.remove("zP1");
        jo.remove("zP2");
        jo.remove("zP3");
        String json = jo.toJSONString();
        ApiResource.postCheckFaileJKSInfo(json, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int i, Header[] headers, byte[] bytes) {
                try {
                    String result = new String(bytes);
                    uploadCount = 0;
                    if (!result.isEmpty() && result.startsWith("true")) {
                        //已上传记数+1；
                        uploadCount++;
                        mProgressDialog.incrementProgressBy(1);
                        mProgressDialog.cancel();
                        Message msg = new Message();
                        msg.what = 1;
                        handler.sendMessage(msg);
                    }
                } catch (Exception e) {
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
    private BDLocation location;
    @SuppressLint("HandlerLeak")
    Handler handler = new Handler() {
        public void handleMessage(Message msg) {
            switch (msg.what) {

                /***手动定位成功**/
                case 1001:
                    MapDataModle mapData= (MapDataModle) msg.getData().getSerializable("mapData");
                    /**获得地址信息**/
                    tv_JKSWZ.setText(mapData.getAddress());
                    /**获得纬度信息**/
                    bWd=Double.valueOf("" + mapData.getLatitude());
                    /**获得经度信息信息**/
                    bJD=Double.valueOf(""+mapData.getLongitude());
                    /**百度坐标，经纬度展现**/
                    tv_gps_jks.setText("(" + bWd + "," + bJD + ")");
                    map_type =Integer.parseInt(mapData.getLoctype());
                    showMapView(false);
                    break;

                case 1:// 上传进度条显示
                    int count = msg.arg1;
                    mProgressDialog = new ProgressDialog(JKSEditorActivity.this);
                    mProgressDialog.setProgress(count);
                    mProgressDialog.setMax(count);
                    mProgressDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
                    mProgressDialog.setTitle("信息上传中");
                    mProgressDialog.setIndeterminate(false);
                    mProgressDialog.setCancelable(false);
                    mProgressDialog.show();
                    break;

                case 2:// 上传之后
                    mProgressDialog.cancel();
                    mProgressDialog.cancel();
                    Toast.makeText(JKSEditorActivity.this,"修改成功",Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(JKSEditorActivity.this, CheckFaileJKSInfoActivity.class);
                    startActivity(intent);
                    CheckFaileJKSInfoActivity.intence.finish();
                    WatchJKSInfoActivity.intence.finish();
                    JKSEditorActivity.this.finish();
                    break;

                case 3:// 上传失败
                    mProgressDialog.cancel();
                    BaseUtil.showDialog("上传失败，可能当前上传的人数较多，请稍候重试！", JKSEditorActivity.this);
                    break;
                case 4://上传前检测
                    mProgressDialog = new ProgressDialog(JKSEditorActivity.this);
                    mProgressDialog.setTitle("上传前检测");
                    mProgressDialog.setMessage("检测网络情况及APP是否最新版本");
                    mProgressDialog.setIndeterminate(false);
                    mProgressDialog.setCancelable(false);
                    mProgressDialog.show();
                    break;
                default:
                    break;
            }
        }
    };
    @Override
    public void setLoction(BDLocation location) {
        map_location=location;
        /**获得地址信息**/
        address=location.getAddrStr();
        /**获得纬度信息**/
        bWd=location.getLatitude();
        /**获得经度信息信息**/
        bJD=location.getLongitude();
        map_type = map_location.getLocType();

    }

    /** * 百度地图运用  获取地理位置信息*/
    private void initMapView() {
        /**获取百度地图位置信息*/
        new MapPresenter(this).setMapData(this);
        /***mMapView*/
        mMapView = (MapView) findViewById(R.id.bmapView);
        mBaiduMap = mMapView.getMap();
        /***初始化手动定位信息
         * 手动定位成功handler 返回定位值
         * */
        new GetLocation(this).initMapView(mBaiduMap, handler);
    }


    /**
     * 显示或隐藏地图
     */
    private void showMapView(boolean mFlag) {
        if (mFlag) {
            this.mMapView.setVisibility(View.VISIBLE);
            sv_collection.setVisibility(View.GONE);
            btn_savelocal.setVisibility(View.GONE);
        } else {
            this.mMapView.setVisibility(View.GONE);
            sv_collection.setVisibility(View.VISIBLE);
            btn_savelocal.setVisibility(View.VISIBLE);
        }
    }



    /** * 拍照 */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode==CAMERA_INTENT_REQUEST&&resultCode!=0&& imagePath!=null) {
            getCaptureImageFile();
            showImgs(imagePath,"0");
        }
    }

    private void getCaptureImageFile() {
        Bitmap source_bitmap =ImageUtils.decodeFile(imagePath);
        if (source_bitmap.getWidth() > source_bitmap.getHeight()) {
            bitmap = ThumbnailUtils.extractThumbnail(source_bitmap,
                    image_width, image_height);
        } else {
            bitmap = ThumbnailUtils.extractThumbnail(source_bitmap,
                    image_height, image_width);
        }
        source_bitmap.recycle();
        bitmap.recycle();
    }

    /**  * 展示选择的图片showMapView */
    private void showImgs(String path,String orientation) {
        Bitmap bit=ImageUtils.compressImageByPixel(path);
        int angle = 0;
        if (orientation != null && !"".equals(orientation)) {
            angle = Integer.parseInt(orientation);
        }
        if (angle != 0) {
            /**  * 下面的方法主要作用是把图片转一个角度，也可以放大缩小等*/
            Matrix m = new Matrix();
            int width = bit.getWidth();
            int height = bit.getHeight();
            m.setRotate(angle); // 旋转angle度
            bit = Bitmap.createBitmap(bit, 0, 0, width, height, m, true);// 从新生成图片
        }
        if (fla==1){
            path1=path;
            iv_ZP1.setImageBitmap(bit);
        }else if (fla==2){
            path2=path;
            iv_ZP2.setImageBitmap(bit);
        }else if (fla==3){
            path3=path;
            iv_ZP3.setImageBitmap(bit);
        }
    }

    /** * 调用相机拍照*/
    private void cameraPhoto() {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        imagePath =((AppContext)getApplication()).getCameraImagePath(0);
        File mFile = new File(imagePath);
        intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(mFile));
        startActivityForResult(intent, CAMERA_INTENT_REQUEST);
    }
    @Override
    public void onPause() {
        super.onPause();
        mMapView.onPause();
    }

    @Override
    public void onResume() {
        super.onResume();
        mMapView.onResume();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        // 关闭定位图层
        mBaiduMap.setMyLocationEnabled(false);
        mMapView.onDestroy();
        mMapView = null;
        System.gc();
    }

    /**  * 捕获后退按钮
     * 1.如果在地图的模式先隐藏地图
     * 2.不在地图直接退出界面*/
    @Override
    public boolean dispatchKeyEvent(KeyEvent event) {
        // 在2级菜单用
        if (event.getAction() == KeyEvent.ACTION_DOWN
                && event.getKeyCode() == KeyEvent.KEYCODE_BACK) {
            if (mMapView.isShown()) {
                showMapView(false);
                return false;
            } else {
                this.finish();
            }
        }
        return super.dispatchKeyEvent(event);
    }

    /** * 1.如果在地图的模式先隐藏地图
     * 2.不在地图直接退出界面
     * */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                if (mMapView.isShown()) {
                    showMapView(false);
                }else {
                    this.finish();
                }
                break;
            default:
                break;
        }
        return true;
    }


    /** *点击空白部分隐藏软键盘 */
    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        if (ev.getAction() == MotionEvent.ACTION_DOWN) {
            View v = getCurrentFocus();
            if (PositionUtil.isShouldHideInput(v, ev)) {

                InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                if (imm != null) {
                    imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
                }
            }
            return super.dispatchTouchEvent(ev);
        }
        // 必不可少，否则所有的组件都不会有TouchEvent了
        if (getWindow().superDispatchTouchEvent(ev)) {
            return true;
        }
        return onTouchEvent(ev);
    }


}