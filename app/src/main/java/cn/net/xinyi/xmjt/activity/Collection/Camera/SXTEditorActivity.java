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
import android.widget.LinearLayout;
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
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Calendar;
import java.util.List;
import java.util.Map;

import cn.net.xinyi.xmjt.R;
import cn.net.xinyi.xmjt.api.ApiHttpClient;
import cn.net.xinyi.xmjt.api.ApiResource;
import cn.net.xinyi.xmjt.config.AppContext;
import cn.net.xinyi.xmjt.config.BaseActivity;
import cn.net.xinyi.xmjt.model.JKSInfoModle;
import cn.net.xinyi.xmjt.model.MapDataModle;
import cn.net.xinyi.xmjt.model.Presenter.MapPresenter;
import cn.net.xinyi.xmjt.model.SXTInfoModle;
import cn.net.xinyi.xmjt.model.View.IMapDataView;
import cn.net.xinyi.xmjt.utils.AnnotateManager;
import cn.net.xinyi.xmjt.utils.BaseDataUtils;
import cn.net.xinyi.xmjt.utils.BaseUtil;
import cn.net.xinyi.xmjt.utils.BindView;
import cn.net.xinyi.xmjt.utils.DB.CollectDBUtils;
import cn.net.xinyi.xmjt.utils.DB.DBOperation;
import cn.net.xinyi.xmjt.utils.GeneralUtils;
import cn.net.xinyi.xmjt.utils.GetLocation;
import cn.net.xinyi.xmjt.utils.ImageUtils;
import cn.net.xinyi.xmjt.utils.PositionUtil;

/**
 * Created by hao.zhou on 2015/9/18.
 * 摄像头编辑
 */
public class SXTEditorActivity extends BaseActivity implements View.OnClickListener,IMapDataView {
    private ProgressDialog mProgressDialog = null;
    private int networkType;
    private final int CAMERA_INTENT_REQUEST = 0XFF02;
    private int fla=0;
    private SXTInfoModle sXTInfo;
    private Calendar c= Calendar.getInstance();
    private boolean bUpload = false;
    private DBOperation dbo;
    private String path1=null,path2=null,path3=null;
    private static int image_width = 400;
    private static int image_height = 300;
    private Bitmap bitmap;
    private String imagePath;
    private List<JKSInfoModle> jKSInfo;
    private ScrollView sv_collection;
    private boolean flag = false;
    private MapView mMapView;  // 地图标注及定位
    BaiduMap mBaiduMap;
    private LinearLayout ll_save;
    private int uploadCount=0;
    private boolean mapFlag=false;
    /**摄像头的位置              tv_posioion_sxt
     * 手动定位需要隐藏的布局  sv_collection
     * 保存本地数据  menu_save_local
     *坐标显示  tv_gps_sxt
     * 刷新位置  tv_position_sxt_shoudong
     * */
    @BindView(id = R.id.menu_save_local,click = true)
    Button menu_save_local;
    @BindView(id = R.id.tv_collection_gps_sxt,click = false)
    TextView tv_sxt_gps;

    /**摄像头安装
     * 环境  tv_SSHJ
     * 类型  tv_SXTLX
     * 正常  tv_SFZC
     * 方向  tv_SXTFX
     * 类别  tv_SXTLB
     * 场所  tv_CSFL
     * 保存时间  tv_SPBCQX
     * 安装摄像头时间  tv_AZSJ
     * */
    @BindView(id = R.id.tv_collection_hj_sxt,click = true)
    TextView tv_SSHJ;
    @BindView(id = R.id.tv_collection_type_sxt,click = true)
    TextView tv_SXTLX;
    @BindView(id = R.id.tv_collection_zc_sxt,click = true)
    TextView tv_SFZC;
    @BindView(id = R.id.tv_collection_fx_sxt,click = true)
    TextView tv_SXTFX;
    @BindView(id = R.id.tv_collection_lb_sxt,click = true)
    TextView tv_SXTLB;
    @BindView(id = R.id.tv_collection_cs_sxt,click = true)
    TextView tv_CSFL;
    @BindView(id = R.id.tv_collection_savedate_sxt,click = true)
    TextView tv_SPBCQX;
    @BindView(id = R.id.tv_collection_andata_sxt,click = true)
    TextView tv_AZSJ;

    /**
     * 监控室名字 tv_JKSMC
     * 选择监控室名称  btn_jks
     * 摄像头位置  tv_SXTWZ
     * 经纬度  tv_sxt_gps
     * 房屋编号  et_LDBH
     * 安装公司 et_JSDW
     * 生产企业et_company
     * Atm编号 et_ATMBH
     * */
    @BindView(id = R.id.tv_room_name,click = false)
    TextView tv_JKSMC;
    @BindView(id = R.id.btn_room_name,click = true)
    Button btn_jks;
    @BindView(id = R.id.tv_collection_posioion_sxt,click = false)
    EditText tv_SXTWZ;
    @BindView(id = R.id.tv_collection_position_sxt_shoudong,click = true)
    TextView tv_sxt_position_shoudong;
    @BindView(id = R.id.et_block_number,click = false)
    EditText et_LDBH;
    @BindView(id = R.id.et_construction_unit,click = false)
    EditText et_JSDW;
    @BindView(id = R.id.et_company,click = false)
    EditText et_SCCS;
    @BindView(id = R.id.remarks,click = false)
    EditText et_BZ;
    @BindView(id = R.id.et_atm_number,click = false)
    EditText et_ATMBH;
    /**
     * 照片 1 2 3
     * */
    @BindView(id = R.id.iv_04,click = true)
    ImageView iv_ZP1;
    @BindView(id = R.id.iv_05,click = true)
    ImageView iv_ZP2;
    @BindView(id = R.id.iv_06,click = true)
    ImageView iv_ZP3;
    private double bWd;
    private double bJD;
    private BDLocation map_location;
    private int map_type;
    private String address;



    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.camera_info);
        AnnotateManager.initBindView(this);
        getActionBar().setIcon(R.drawable.actionbar_back_icon_normal);
        getActionBar().setTitle("编辑摄像头信息");
        getActionBar().setHomeButtonEnabled(true);

        // WatchSXTInfoActivity  传递的摄像头信息
        sXTInfo =(SXTInfoModle)getIntent().getSerializableExtra(GeneralUtils.SXTInfo);
        /** *初始化控件*/
        initViews();
        /** *加载数据*/
        initData();
    }

    private void initViews() {
        initMapView();
        //初始化网络信息
        networkType = ((AppContext) getApplication()).getNetworkType();
        iv_ZP1.setImageResource(R.drawable.loading_pic);
        iv_ZP2.setImageResource(R.drawable.loading_pic);
        iv_ZP3.setImageResource(R.drawable.loading_pic);

        /** *百度地图请求当前位置信息  */
        new GetLocation(this, handler).startLocation();

        /** * 核查摄像头不通过  限制照片重拍*/
        if (getIntent().getFlags() == GeneralUtils.CheckFaileSXTInfoActivity){
            menu_save_local.setText("确认提交");
            iv_ZP1.setEnabled(false);
            iv_ZP2.setEnabled(false);
            iv_ZP3.setEnabled(false);
            /** *如果核查的备注说明中包含位置或者地址不对  需要更新地址 */
            if (sXTInfo.getSHSBSM()!=null&&
                    (sXTInfo.getSHSBSM().contains("位置")
                            ||sXTInfo.getSHSBSM().contains("地址")
                            ||sXTInfo.getSHSBSM().contains("坐标")
                            ||sXTInfo.getSHSBYY().contains("地址"))){
                /** *百度地图请求当前位置信息  */
                new GetLocation(this, handler).startLocation();
                /***mapFlag 为true的时候  需要对控件 地址。坐标值进行更新 */
                mapFlag=true;
            }
        }
        /** * 摄像头位置限制不超过50个字*/
        tv_SXTWZ.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.length() > 49) {
                    BaseUtil.showDialog("当前文本过长", SXTEditorActivity.this);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {}
        });
        //手动定位需要隐藏的布局
        sv_collection = (ScrollView) findViewById(R.id.sv_collection);
        ll_save = (LinearLayout) findViewById(R.id.ll_save);

    }



    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            //选择监控室名称
            case R.id.btn_room_name:
                //获得监控室本地数据
                jKSInfo = CollectDBUtils.getJKSLocalData(SXTEditorActivity.this);
                //添加选项-选择已上传的监控室
                JKSInfoModle jksdata=new JKSInfoModle();
                jksdata.setJKSMC(getResources().getString(R.string.upload_success));
                jKSInfo.add(jksdata);
                //数组-监控室名字
                final String[] a1 = new String[jKSInfo.size()];
                //数组-监控室编号
                final String[] a2=new String[jKSInfo.size()];
                //数组-监控室网格
                final String[] a3=new String[jKSInfo.size()];
                //根据监控室编号，设置摄像头所属监控室名称、监控室编号、以及网格
                for (int i = 0; i < jKSInfo.size(); i++) {
                    a1[i] = jKSInfo.get(i).getJKSMC();
                    a2[i]= jKSInfo.get(i).getJKSBH();
                    a3[i]=jKSInfo.get(i).getSSWG();
                }
                new AlertDialog.Builder(this).setItems(a1,
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                if (a1[which].equals(getResources().getString(R.string.upload_success))){
                                    Intent intent=new Intent(SXTEditorActivity.this,AlreadyUploadActivity.class);
                                    intent.setFlags(GeneralUtils.SXTEditorActivity);
                                    startActivityForResult(intent, 2009);
                                }else {
                                    tv_JKSMC.setText(a1[which]);
                                    sXTInfo.setJKSBH(a2[which]);
                                    sXTInfo.setSSWG(a3[which]);
                                }
                            }
                        }).create().show();
                break;

            //手动定位
            case R.id.tv_collection_position_sxt_shoudong:
                /**获得地址信息**/
                tv_SXTWZ.setText(address);
                /**百度坐标，经纬度展现**/
                tv_sxt_gps.setText("(" + bWd + "," + bJD + ")");
                /**
                 * *地图上标识当前所处的位置
                 * 如果没有获取到坐标  默认定位到龙岗中心城*/
                new GetLocation(this).mapLocation(map_location, mBaiduMap);
                /***手动定位类型*/
                flag = true;
                showMapView(flag);
                break;

            //缓存本地
            case R.id.menu_save_local:
                if (sXTInfo.getZP1()==null||sXTInfo.getZP2()==null||sXTInfo.getZP3()==null) {
                    BaseUtil.showDialog("请上传3张监控室图片", SXTEditorActivity.this);
                    break;
                }
                if (tv_JKSMC.getText().toString().trim().isEmpty()) {
                    BaseUtil.showDialog("监控室的名称不能为空", SXTEditorActivity.this);
                    break;
                }
                if (tv_SXTWZ.getText().toString().trim().isEmpty()) {
                    BaseUtil.showDialog("摄像头所在位置不能为空", SXTEditorActivity.this);
                    break;
                }
                if (tv_SXTLX.getText().toString().trim().isEmpty()) {
                    BaseUtil.showDialog("摄像头的类型不能为空", SXTEditorActivity.this);
                    break;
                }
                if (tv_SFZC.getText().toString().trim().isEmpty()) {
                    BaseUtil.showDialog("请选择摄像头是否正常", SXTEditorActivity.this);
                    break;
                }
                if (et_LDBH.getText().toString().trim().isEmpty()||
                        et_LDBH.getText().toString().trim().length()!=19) {
                    BaseUtil.showDialog("请输入19位正确楼栋编号", SXTEditorActivity.this);
                    break;
                }
                if (tv_SXTFX.getText().toString().trim().isEmpty()) {
                    BaseUtil.showDialog("请选择摄像头方向", SXTEditorActivity.this);
                    break;
                }
                if (tv_AZSJ.getText().toString().trim().isEmpty()) {
                    BaseUtil.showDialog("摄像头安装时间不能为空", SXTEditorActivity.this);
                    break;
                }
                if (tv_SXTLB.getText().toString().trim().isEmpty()) {
                    BaseUtil.showDialog("请选择摄像头类别", SXTEditorActivity.this);
                    break;
                }
                if (tv_CSFL.getText().toString().trim().isEmpty()) {
                    BaseUtil.showDialog("请选择摄像头所处场所", SXTEditorActivity.this);
                    break;
                }
                if (tv_SSHJ.getText().toString().trim().isEmpty()) {
                    BaseUtil.showDialog("请选择摄像头所处环境", SXTEditorActivity.this);
                    break;
                }
                if (tv_SPBCQX.getText().toString().trim().isEmpty()) {
                    BaseUtil.showDialog("请选择视频保存时间", SXTEditorActivity.this);
                    break;
                }
                /** *摄像头 审核未通过，修改提交 */
                if (getIntent().getFlags() == GeneralUtils.CheckFaileSXTInfoActivity){
                    uploadSXTInfoThread();
                }else {
                    /** *摄像头信息 本地缓存修改编辑 */
                    bUpload = updateToLocalDB(JKSInfoModle.ISUPDATE_LOCAL);
                    if (bUpload) {
                        Toast.makeText(SXTEditorActivity.this, "编辑成功！",Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(SXTEditorActivity.this, ManageSXTInfoActivity.class);
                        startActivity(intent);
                        ManageSXTInfoActivity.intence.finish();
                        WatchSXTInfoActivity.intence.finish();
                        this.finish();
                    } else {
                        BaseUtil.showDialog("保存失败", SXTEditorActivity.this);
                    }
                }
                break;


            //摄像头安装时间
            case R.id.tv_collection_andata_sxt:
                new DatePickerDialog(
                        SXTEditorActivity.this, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view,
                                          int year, int monthOfYear,
                                          int dayOfMonth) {
                        c.set(year, monthOfYear, dayOfMonth);
                        CharSequence forma = DateFormat.format("yyyy-MM-dd", c);
                        tv_AZSJ.setText(forma);
                    }
                }, c.get(Calendar.YEAR), c.get(Calendar.MONTH), c
                        .get(Calendar.DAY_OF_MONTH)).show();
                break;

            //摄像头类别
            case R.id.tv_collection_type_sxt:

                final Map<String,String> sxtMaps=zdUtils.getZdlbToZdz(GeneralUtils.XXCJ_SXTLX);
                BaseDataUtils.showAlertDialog(this,sxtMaps.values(),tv_SXTLX);

                break;
            //摄像头是否正常
            case R.id.tv_collection_zc_sxt:
                final Map<String,String> zcsxtMaps=zdUtils.getZdlbToZdz(GeneralUtils.XXCJ_SXTZT);
                BaseDataUtils.showAlertDialog(this,zcsxtMaps.values(),tv_SFZC);
                break;

            //摄像头安装方向
            case R.id.tv_collection_fx_sxt:
                final Map<String,String> sxtztMaps=zdUtils.getZdlbToZdz(GeneralUtils.XXCJ_SXTFX);
                BaseDataUtils.showAlertDialog(this,sxtztMaps.values(),tv_SXTFX);
                break;

            //摄像头安装场所
            case R.id.tv_collection_cs_sxt:
                final Map<String,String> csflMaps=zdUtils.getZdlbToZdz(GeneralUtils.XXCJ_SXTCSFL);
                BaseDataUtils.showAlertDialog(this,csflMaps.values(),tv_CSFL);
                break;

            //摄像头视频保存时间
            case R.id.tv_collection_savedate_sxt:
                final Map<String,String> bcsjMaps=zdUtils.getZdlbToZdz(GeneralUtils.XXCJ_SXTBCSJ);
                BaseDataUtils.showAlertDialog(this,bcsjMaps.values(),tv_SPBCQX);
                break;

            //摄像头安装类别
            case R.id.tv_collection_lb_sxt:
                final Map<String,String> sxtlbMaps=zdUtils.getZdlbToZdz(GeneralUtils.XXCJ_SXTLB);
                BaseDataUtils.showAlertDialog(this,sxtlbMaps.values(),tv_SXTLB);
                break;

            //摄像头安装环境
            case R.id.tv_collection_hj_sxt:
                final Map<String,String> sxthjMaps=zdUtils.getZdlbToZdz(GeneralUtils.XXCJ_SXTSSHJ);
                BaseDataUtils.showAlertDialog(this,sxthjMaps.values(),tv_SSHJ);
                break;

            case R.id.iv_04:
                fla=1;
                cameraPhoto();
                break;

            case R.id.iv_05:
                fla=2;
                cameraPhoto();
                break;

            case R.id.iv_06:
                fla=3;
                cameraPhoto();
                break;
        }
    }

    private void uploadSXTInfoThread() {
        /** * 保存与上传*/
        new Thread() {
            public void run() {
                Message msg = new Message();
                msg.what = 1;
                handler.sendMessage(msg);
                uploadSXTInfo();
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
    /**上传核查不同的摄像头  编辑后的信息*/
    private void uploadSXTInfo() {
        /***获取编辑的信息*/
        updateSXTInfo();
        //json处理
        JSONObject jo = JSON.parseObject(JSON.toJSONString(sXTInfo));
        jo.remove("zP1");
        jo.remove("zP2");
        jo.remove("zP3");
        String json = jo.toJSONString();
        ApiResource.postCheckFaileSXTInfo(json, new AsyncHttpResponseHandler() {
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


    //更新数据库
    private boolean updateToLocalDB(int isupdateLocal) {
        if (path1!=null)
            sXTInfo.setZP1(path1);
        if (path2!=null)
            sXTInfo.setZP2(path2);
        if (path3!=null)
            sXTInfo.setZP3(path3);
        updateSXTInfo();
        long rows = 0;
        // 本地保存
        dbo = new DBOperation(SXTEditorActivity.this);
        try {
            rows = dbo.UpdateCamearInfo(sXTInfo.getId(), sXTInfo);
        } catch (Exception e) {
            e.printStackTrace();
        }
        dbo.clossDb();
        return rows != 0;
    }
    /** *获取值*/
    private void updateSXTInfo() {
        if (tv_SXTLX.getText().toString().trim()!= sXTInfo.getSXTLX()){
            sXTInfo.setSXTLX(tv_SXTLX.getText().toString().trim());
        }
        if (et_ATMBH.getText().toString().trim()!= sXTInfo.getATMBH()){
            sXTInfo.setATMBH(et_ATMBH.getText().toString().trim());
        }
        if (tv_SFZC.getText().toString().trim()!= sXTInfo.getSFZC()){
            sXTInfo.setSFZC(tv_SFZC.getText().toString().trim());
        }
        if (tv_SXTWZ.getText().toString().trim()!= sXTInfo.getSXTWZ()){
            sXTInfo.setSXTWZ(tv_SXTWZ.getText().toString().trim());
        }

        if (tv_SXTFX.getText().toString().trim()!= sXTInfo.getSXTFX()){
            sXTInfo.setSXTFX(tv_SXTFX.getText().toString().trim());
        }
        if (tv_AZSJ.getText().toString().trim()!= sXTInfo.getAZSJ()){
            sXTInfo.setAZSJ(tv_AZSJ.getText().toString().trim());
        }
        if (tv_CSFL.getText().toString().trim()!= sXTInfo.getCSFL()) {
            sXTInfo.setCSFL(tv_CSFL.getText().toString().trim());
        }
        if (tv_SSHJ.getText().toString().trim()!= sXTInfo.getSSHJ()){
            sXTInfo.setSSHJ(tv_SSHJ.getText().toString().trim());
        }
        if (et_LDBH.getText().toString().trim()!= sXTInfo.getLDBH()){
            sXTInfo.setLDBH(et_LDBH.getText().toString().trim());
        }
        if (tv_SXTLB.getText().toString().trim()!= sXTInfo.getSXTLB()){
            sXTInfo.setSXTLB(tv_SXTLB.getText().toString().trim());
        }
        if (tv_JKSMC.getText().toString().trim()!= sXTInfo.getJKSMC()){
            sXTInfo.setJKSMC(tv_JKSMC.getText().toString().trim());
        }
        if (tv_SPBCQX.getText().toString().trim()!= sXTInfo.getSPBCQX()){
            sXTInfo.setSPBCQX(tv_SPBCQX.getText().toString().trim());
        }
        if (!et_SCCS.getText().toString().trim().equals( sXTInfo.getSCCS())){
            sXTInfo.setSCCS(et_SCCS.getText().toString().trim());
        }

        if (!et_JSDW.getText().toString().trim().equals( sXTInfo.getJSDW())){
            sXTInfo.setJSDW(et_JSDW.getText().toString().trim());
        }
        if (!et_BZ.getText().toString().trim().equals(sXTInfo.getBZ())){
            sXTInfo.setBZ(et_BZ.getText().toString().trim());
        }
        if (!("" + map_type).equals(sXTInfo.getLOCTYPE())){
            sXTInfo.setLOCTYPE("" + map_type);
            sXTInfo.setJD("" + bJD);
            sXTInfo.setWD("" + bWd);
        }
        sXTInfo.setIsupdate(String.valueOf(JKSInfoModle.ISUPDATE_LOCAL));
    }


    //初始化加载数据
    private void initData() {
        if (getIntent().getFlags() == GeneralUtils.CheckSXTInfo_flag
                ||getIntent().getFlags() == GeneralUtils.DownSXTInfoActivity
                ||getIntent().getFlags() == GeneralUtils.CheckFaileSXTInfoActivity){
            //转换的时间yy-MM-dd
            String date=ImageUtils.ImageLoad(sXTInfo.getSCSJ());
            ImageLoader.getInstance().displayImage(ApiHttpClient.IMAGE_HOST + "/sxt/" + date + "/" + sXTInfo.getZP1(), iv_ZP1);
            ImageLoader.getInstance().displayImage(ApiHttpClient.IMAGE_HOST + "/sxt/" + date + "/" + sXTInfo.getZP2(), iv_ZP2);
            ImageLoader.getInstance().displayImage(ApiHttpClient.IMAGE_HOST + "/sxt/" + date + "/" + sXTInfo.getZP3(), iv_ZP3);
        }else {
            if (sXTInfo.getZP1()!=null){
                path1= sXTInfo.getZP1();
                iv_ZP1.setImageBitmap(ImageUtils.decodeFile(path1));
            }
            if (sXTInfo.getZP2()!=null){
                path2= sXTInfo.getZP2();
                iv_ZP2.setImageBitmap(ImageUtils.decodeFile(path2));
            }
            if (sXTInfo.getZP3()!=null){
                path3= sXTInfo.getZP3();
                iv_ZP3.setImageBitmap(ImageUtils.decodeFile(path3));
            }
        }
        tv_sxt_gps.setText("(" + sXTInfo.getWD() + "," + sXTInfo.getJD() + ")");
        tv_SXTLX.setText(sXTInfo.getSXTLX());
        tv_SFZC.setText(sXTInfo.getSFZC());
        tv_SXTWZ.setText(sXTInfo.getSXTWZ());
        tv_SXTFX.setText(sXTInfo.getSXTFX());
        tv_AZSJ.setText(sXTInfo.getAZSJ());
        tv_CSFL.setText(sXTInfo.getCSFL());
        tv_SSHJ.setText(sXTInfo.getSSHJ());
        tv_SXTLB.setText(sXTInfo.getSXTLB());
        tv_SPBCQX.setText(sXTInfo.getSPBCQX());
        tv_JKSMC.setText(sXTInfo.getJKSMC());
        et_LDBH.setText(sXTInfo.getLDBH());
        et_ATMBH.setText(sXTInfo.getATMBH());
        et_SCCS.setText(sXTInfo.getSCCS());
        et_JSDW.setText(sXTInfo.getJSDW());
        et_BZ.setText(sXTInfo.getBZ());
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
                    tv_SXTWZ.setText(mapData.getAddress());
                    /**获得纬度信息**/
                    bWd=Double.valueOf("" + mapData.getLatitude());
                    /**获得经度信息信息**/
                    bJD=Double.valueOf(""+mapData.getLongitude());
                    /**百度坐标，经纬度展现**/
                    tv_sxt_gps.setText("(" + bWd + "," + bJD + ")");
                    map_type =Integer.parseInt(mapData.getLoctype());
                    showMapView(false);
                    break;


                case 1:// 上传进度条显示
                    int count = msg.arg1;
                    mProgressDialog = new ProgressDialog(SXTEditorActivity.this);
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
                    Toast.makeText(SXTEditorActivity.this,"修改成功",Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(SXTEditorActivity.this, CheckFaileSXTInfoActivity.class);
                    startActivity(intent);
                    CheckFaileSXTInfoActivity.intence.finish();
                    WatchSXTInfoActivity.intence.finish();
                    SXTEditorActivity.this.finish();
                    break;

                case 3:// 上传失败
                    mProgressDialog.cancel();
                    BaseUtil.showDialog("上传失败，可能当前上传的人数较多，请稍候重试！", SXTEditorActivity.this);
                    break;

                case 4://上传前检测
                    mProgressDialog = new ProgressDialog(SXTEditorActivity.this);
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

    /** * 显示或隐藏地图 */
    private void showMapView(boolean mFlag) {
        if (mFlag) {
            this.mMapView.setVisibility(View.VISIBLE);
            sv_collection.setVisibility(View.GONE);
            ll_save.setVisibility(View.GONE);
        } else {
            this.mMapView.setVisibility(View.GONE);
            sv_collection.setVisibility(View.VISIBLE);
            ll_save.setVisibility(View.VISIBLE);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        //拍照
        if (requestCode==CAMERA_INTENT_REQUEST&&resultCode!=0&& imagePath!=null) {
            getCaptureImageFile();
            showImgs(imagePath,"0");
            //选择服务端监控室
        }else if (resultCode==RESULT_OK&&data!=null){
            JKSInfoModle jksdata=(JKSInfoModle)data.getSerializableExtra(GeneralUtils.RegistWangGe);
            tv_JKSMC.setText(jksdata.getJKSMC());
            sXTInfo.setJKSBH(jksdata.getJKSBH());
            sXTInfo.setSSWG(jksdata.getSSWG());
        }

    }


    private void getSelectImageFile(String tmpFile) {
        // 获取选择的图片
        Bitmap source_bitmap = ImageUtils.decodeFile(tmpFile);
        //缩略图
        if (source_bitmap.getWidth() > source_bitmap.getHeight()) {
            bitmap = ThumbnailUtils.extractThumbnail(source_bitmap,
                    image_width, image_height);
        } else {
            bitmap = ThumbnailUtils.extractThumbnail(source_bitmap,
                    image_height, image_width);
        }
        imagePath =((AppContext)getApplication()).getCameraImagePath(0);

        FileOutputStream b = null;
        try {
            b = new FileOutputStream(imagePath);
            source_bitmap.compress(Bitmap.CompressFormat.JPEG, 40, b);// 把数据写入文件

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } finally {
            source_bitmap.recycle();
            bitmap.recycle();
            try {
                b.flush();
                b.close();

            } catch (IOException e) {
                e.printStackTrace();
            }
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
        source_bitmap.recycle();bitmap.recycle();
    }

    /** * 展示选择的图片showMapView */
    private void showImgs(String path,String orientation) {
        Bitmap bit=ImageUtils.compressImageByPixel(path);
        int angle = 0;
        if (orientation != null && !"".equals(orientation)) {
            angle = Integer.parseInt(orientation);
        }
        if (angle != 0) {
            // 下面的方法主要作用是把图片转一个角度，也可以放大缩小等
            Matrix m = new Matrix();
            int width = bit.getWidth();
            int height = bit.getHeight();
            m.setRotate(angle); // 旋转angle度
            bit = Bitmap.createBitmap(bit, 0, 0, width, height,m, true);// 从新生成图片
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


    /**
     * 调用相机拍照
     */
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
        System.gc();
    }


    /**
     * 捕获后退按钮
     */
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

    //点击空白部分隐藏软键盘
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