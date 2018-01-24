package cn.net.xinyi.xmjt.activity.Collection.Camera;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
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

import com.baidu.location.BDLocation;
import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.MapView;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import cn.net.xinyi.xmjt.R;
import cn.net.xinyi.xmjt.config.AppContext;
import cn.net.xinyi.xmjt.config.AppException;
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
 * Created by hao.zhou on 2015/9/14.
 *摄像头采集
 */


public class SXTInfoActivity extends BaseActivity implements View.OnClickListener,IMapDataView {


    private final int CAMERA_INTENT_REQUEST = 0XFF02;
    private int fla = 0;
    private Calendar c = Calendar.getInstance();
    private SXTInfoModle sXTInfo;
    private String path1 = null, path2 = null, path3 = null;
    private String imagePath;
    private static int image_width = 400;
    private static int image_height = 300;
    private Bitmap bitmap;
    private List<JKSInfoModle> jKSInfo;
    private boolean bUpload = false;
    private DBOperation dbo;
    private boolean flag = false;
    private MapView mMapView;  // 地图标注及定位
    BaiduMap mBaiduMap;

    /**摄像头的位置              tv_posioion_sxt
     * 保存本地数据  menu_save_local
     *坐标显示  tv_gps_sxt
     * 刷新位置  tv_position_sxt_shoudong
     * */
    @BindView(id = R.id.tv_collection_posioion_sxt,click = false)
    TextView tv_posioion_sxt;
    @BindView(id = R.id.menu_save_local,click = true)
    Button menu_save_local;
    @BindView(id = R.id.tv_collection_gps_sxt,click = false)
    TextView tv_gps_sxt;



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
    //手动定位需要隐藏的布局
    @BindView(id = R.id.sv_collection,click = false)
    ScrollView sv_collection;
    @BindView(id = R.id.ll_save,click = false)
    LinearLayout ll_save;
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


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.camera_info);
        AnnotateManager.initBindView(this);
        getActionBar().setIcon(R.drawable.actionbar_back_icon_normal);
        getActionBar().setTitle("摄像头采集");
        getActionBar().setHomeButtonEnabled(true);
        initView();
    }

    private void initView() {
        initMapView();
        sXTInfo = new SXTInfoModle();
        tv_SXTWZ.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.length() > 49) {
                    BaseUtil.showDialog("当前文本过长", SXTInfoActivity.this);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });

        Intent intent=getIntent();
        if (intent.getFlags()== GeneralUtils.JKSInfoActivity){
            tv_JKSMC.setText(intent.getStringExtra("JKSMC"));
            sXTInfo.setJKSBH(intent.getStringExtra("JKSBH"));
            sXTInfo.setSSWG(intent.getStringExtra("WG"));
        }

    }





    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            //选择监控室名称
            case R.id.btn_room_name:
                //获得监控室本地数据
                jKSInfo = CollectDBUtils.getJKSLocalData(SXTInfoActivity.this);
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
                                    Intent intent=new Intent(SXTInfoActivity.this,AlreadyUploadActivity.class);
                                    startActivityForResult(intent, 2009);
                                }else {
                                    tv_JKSMC.setText(a1[which]);
                                    sXTInfo.setJKSBH(a2[which]);
                                    sXTInfo.setSSWG(a3[which]);
                                }
                            }
                        }).create().show();
                break;

            //缓存信息到本地
            case R.id.menu_save_local:
                if (path1 == null || path2 == null || path3 == null) {
                    BaseUtil.showDialog("请上传3张摄像头图片", SXTInfoActivity.this);
                    break;
                }
                if (tv_JKSMC.getText().toString().trim().isEmpty()) {
                    BaseUtil.showDialog("监控室的名称不能为空", SXTInfoActivity.this);
                    break;
                }
                if (tv_SXTLX.getText().toString().trim().isEmpty()) {
                    BaseUtil.showDialog("摄像头的类型不能为空", SXTInfoActivity.this);
                    break;
                }
                if (tv_SXTWZ.getText().toString().trim().isEmpty()) {
                    BaseUtil.showDialog("摄像头所在位置不能为空", SXTInfoActivity.this);
                    break;
                }
                if (tv_SFZC.getText().toString().trim().isEmpty()) {
                    BaseUtil.showDialog("请选择摄像头是否正常", SXTInfoActivity.this);
                    break;
                }
                if (et_LDBH.getText().toString().trim().isEmpty()||
                        et_LDBH.getText().toString().trim().length()!=19) {
                    BaseUtil.showDialog("请输入19位正确楼栋编号", SXTInfoActivity.this);
                    break;
                }
                if (tv_SXTFX.getText().toString().trim().isEmpty()) {
                    BaseUtil.showDialog("请选择摄像头方向", SXTInfoActivity.this);
                    break;
                }
                if (tv_AZSJ.getText().toString().trim().isEmpty()) {
                    BaseUtil.showDialog("摄像头安装时间不能为空", SXTInfoActivity.this);
                    break;
                }
                if (tv_SXTLB.getText().toString().trim().isEmpty()) {
                    BaseUtil.showDialog("请选择摄像头类别", SXTInfoActivity.this);
                    break;
                }

                if (tv_CSFL.getText().toString().trim().isEmpty()) {
                    BaseUtil.showDialog("请选择摄像头所处场所", SXTInfoActivity.this);
                    break;
                }
                if (tv_SSHJ.getText().toString().trim().isEmpty()) {
                    BaseUtil.showDialog("请选择摄像头所处环境", SXTInfoActivity.this);
                    break;
                }
                if (tv_SPBCQX.getText().toString().trim().isEmpty()) {
                    BaseUtil.showDialog("请选择视频保存时间", SXTInfoActivity.this);
                    break;
                }

//                // 设置最后一次定位的数据；
//                AppContext.lastLocationTimeZone = (new Date()).getTime();
//                AppContext.lastLocationLatitude = String.valueOf(sXTInfo.getWD());
//                AppContext.lastLocationLongitude = String.valueOf(sXTInfo.getJD());
//                AppContext.lastLocationAddress = sXTInfo.getSXTWZ();
//                AppContext.lastLocationType = sXTInfo.getLOCTYPE();

                //保存本地
                bUpload = saveToLocalDB(JKSInfoModle.ISUPDATE_LOCAL);
                if (bUpload) {
                    new AlertDialog.Builder(this)
                            .setTitle("提示")
                            .setMessage("采集摄像头信息成功")
                            .setNegativeButton("查看",
                                    new DialogInterface.OnClickListener() {

                                        @Override
                                        public void onClick(DialogInterface dialog,
                                                            int which) {
                                            Intent intent = new Intent(SXTInfoActivity.this, ManageSXTInfoActivity.class);
                                            startActivity(intent);
                                            showPlateList();

                                        }
                                    })
                            .setPositiveButton("继续采集",
                                    new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog,
                                                            int which) {
                                            Intent intent = new Intent(SXTInfoActivity.this, SXTInfoActivity.class);
                                            //将摄像头需要的监控室编号、监控室名称、所属网格传递
                                            Bundle mBundle=new Bundle();
                                            mBundle.putSerializable("JKSBH", sXTInfo.getJKSBH());
                                            mBundle.putSerializable("JKSMC", sXTInfo.getJKSMC());
                                            mBundle.putSerializable("WG", sXTInfo.getSSWG());
                                            intent.putExtras(mBundle);
                                            intent.setFlags(GeneralUtils.JKSInfoActivity);
                                            startActivity(intent);
                                            showPlateList();

                                        }
                                    }).setCancelable(false).show();
                } else {
                    BaseUtil.showDialog("保存失败", SXTInfoActivity.this);
                }
                break;

            //摄像头安装时间
            case R.id.tv_collection_andata_sxt:
                new DatePickerDialog(
                        SXTInfoActivity.this, new DatePickerDialog.OnDateSetListener() {
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
                if (tv_SXTWZ.getText().toString().trim().isEmpty()){
                    /** *百度地图请求当前位置信息  */
                    new GetLocation(this, handler).startLocation();
                }

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

            //手动刷新
            case R.id.tv_collection_position_sxt_shoudong:
                /**
                 * *地图上标识当前所处的位置
                 * 如果没有获取到坐标  默认定位到龙岗中心城*/
                new GetLocation(this).mapLocation(location, mBaiduMap);
                flag = true;
                showMapView(flag);
                break;

            case R.id.iv_04:
                if (tv_SXTWZ.getText().toString().trim().isEmpty()){
                    /** *百度地图请求当前位置信息  */
                    new GetLocation(this, handler).startLocation();
                }
                fla = 1;
                cameraPhoto();
                //  tipCamear();
                break;

            case R.id.iv_05:
                fla = 2;
                cameraPhoto();
                //  tipCamear();
                break;

            case R.id.iv_06:
                fla = 3;
                cameraPhoto();
                //  tipCamear();
                break;

        }

    }

    /**
     * 保存数据到本地
     */
    private boolean saveToLocalDB(int isupdateLocal) {
        sXTInfo.setCJYH(AppContext.instance.getLoginInfo().getUsername());
        sXTInfo.setCJDW(AppContext.instance.getLoginInfo().getOrgancode());
        sXTInfo.setCJSJ(DateFormat.format("yyyy-MM-dd kk:mm:ss", Calendar.getInstance(Locale.CHINA)).toString());
        if (path1 != null)
            sXTInfo.setZP1(path1);
        if (path2 != null)
            sXTInfo.setZP2(path2);
        if (path3 != null)
            sXTInfo.setZP3(path3);
        sXTInfo.setATMBH(et_ATMBH.getText().toString().trim());
        sXTInfo.setSXTLX(tv_SXTLX.getText().toString().trim());
        sXTInfo.setSFZC(tv_SFZC.getText().toString().trim());
        sXTInfo.setSXTWZ(tv_SXTWZ.getText().toString().trim());
        sXTInfo.setSXTFX(tv_SXTFX.getText().toString().trim());
        sXTInfo.setAZSJ(tv_AZSJ.getText().toString().trim());
        sXTInfo.setCSFL(tv_CSFL.getText().toString().trim());
        sXTInfo.setSSHJ(tv_SSHJ.getText().toString().trim());
        sXTInfo.setSXTLB(tv_SXTLB.getText().toString().trim());
        sXTInfo.setSPBCQX(tv_SPBCQX.getText().toString().trim());
        sXTInfo.setJKSMC(tv_JKSMC.getText().toString().trim());
        sXTInfo.setLDBH(et_LDBH.getText().toString().trim());
        sXTInfo.setSCCS(et_SCCS.getText().toString().trim());
        sXTInfo.setJSDW(et_JSDW.getText().toString().trim());
        sXTInfo.setBZ(et_BZ.getText().toString().trim());
        sXTInfo.setJD("" + bJD);
        sXTInfo.setWD(""+bWd);
        sXTInfo.setLOCTYPE(""+map_type);
        sXTInfo.setSXTWZ(tv_SXTWZ.getText().toString());
        sXTInfo.setSXTBH("SXT" + AppContext.instance.getLoginInfo().getUsername() +  new SimpleDateFormat("yyyyMMddHHmmssSSSS").format(new Date()));
        long rows = 0;
        // 本地保存
        dbo = new DBOperation(SXTInfoActivity.this);
        try {
            rows = dbo.insertCamearToSqlite(sXTInfo);
        } catch (AppException e) {
            e.printStackTrace();
        }
        dbo.clossDb();
        return rows != 0;
    }

    private void showPlateList() {
        this.finish();
    }


    //返回值
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        //拍照
        if (requestCode == CAMERA_INTENT_REQUEST&&resultCode!=0 && imagePath != null) {
            getCaptureImageFile();
            showImgs(imagePath, "0");
        }else if (resultCode==RESULT_OK&&data!=null){
            JKSInfoModle jksdata=(JKSInfoModle)data.getSerializableExtra(GeneralUtils.RegistWangGe);
            tv_JKSMC.setText(jksdata.getJKSMC());
            sXTInfo.setJKSBH(jksdata.getJKSBH());
            sXTInfo.setSSWG(jksdata.getSSWG());
        }
    }

    private void getCaptureImageFile() {
        Bitmap source_bitmap = ImageUtils.decodeFile(imagePath);
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


    /**
     * 展示选择的图片showMapView
     */
    private void showImgs(String path, String orientation) {
        Bitmap bit = ImageUtils.compressImageByPixel(path);
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
            bit = Bitmap.createBitmap(bit, 0, 0, width, height, m, true);// 从新生成图片
        }
        if (fla == 1) {
            path1 = path;
            iv_ZP1.setImageBitmap(bit);
        } else if (fla == 2) {
            path2 = path;
            iv_ZP2.setImageBitmap(bit);
        } else if (fla == 3) {
            path3 = path;
            iv_ZP3.setImageBitmap(bit);
        }
    }



    /**
     * 调用相机拍照
     */
    private void cameraPhoto() {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        imagePath = ((AppContext) getApplication()).getCameraImagePath(0);
        File mFile = new File(imagePath);
        intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(mFile));
        startActivityForResult(intent, CAMERA_INTENT_REQUEST);
    }
    @Override
    public void setLoction(BDLocation location) {
        map_location=location;
        /**获得纬度信息**/
        bWd=location.getLatitude();
        /**获得经度信息信息**/
        bJD=location.getLongitude();
        /**获得地址信息**/
        tv_SXTWZ.setText(location.getAddrStr());
        /**百度坐标，经纬度展现**/
        tv_gps_sxt.setText("(" + bWd + "," + bJD + ")");
        map_type = map_location.getLocType();
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
                    tv_gps_sxt.setText("(" + bWd + "," + bJD + ")");
                    map_type =Integer.parseInt(mapData.getLoctype());
                    showMapView(false);
                    break;

                default:
                    break;
            }
        }
    };
    /**
     * 百度地图运用  获取地理位置信息
     */
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
            ll_save.setVisibility(View.GONE);
        } else {
            this.mMapView.setVisibility(View.GONE);
            sv_collection.setVisibility(View.VISIBLE);
            ll_save.setVisibility(View.VISIBLE);
        }
    }


    @Override
    public void onPause() {
        mMapView.onPause();
        super.onPause();
    }

    @Override
    public void onResume() {
        mMapView.onResume();
        super.onResume();
    }

    @Override
    protected void onDestroy() {
        // 关闭定位图层
        mBaiduMap.setMyLocationEnabled(false);
        mMapView.onDestroy();
        mMapView = null;
        super.onDestroy();
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
               new BaseDataUtils().loginOut(this);
            }
        }
        // super.dispatchKeyEvent(event);
        return super.dispatchKeyEvent(event);
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                if (mMapView.isShown()) {
                    showMapView(false);
                }else {
                    new BaseDataUtils().loginOut(this);
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