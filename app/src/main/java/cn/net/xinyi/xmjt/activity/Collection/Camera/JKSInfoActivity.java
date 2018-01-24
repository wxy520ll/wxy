package cn.net.xinyi.xmjt.activity.Collection.Camera;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
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
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.baidu.location.BDLocation;
import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.MapView;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.Map;

import cn.net.xinyi.xmjt.R;
import cn.net.xinyi.xmjt.config.AppContext;
import cn.net.xinyi.xmjt.config.AppException;
import cn.net.xinyi.xmjt.config.BaseActivity;
import cn.net.xinyi.xmjt.model.JKSInfoModle;
import cn.net.xinyi.xmjt.model.MapDataModle;
import cn.net.xinyi.xmjt.model.Presenter.MapPresenter;
import cn.net.xinyi.xmjt.model.View.IMapDataView;
import cn.net.xinyi.xmjt.utils.AnnotateManager;
import cn.net.xinyi.xmjt.utils.BaseDataUtils;
import cn.net.xinyi.xmjt.utils.BaseUtil;
import cn.net.xinyi.xmjt.utils.BindView;
import cn.net.xinyi.xmjt.utils.DB.DBOperation;
import cn.net.xinyi.xmjt.utils.GeneralUtils;
import cn.net.xinyi.xmjt.utils.GetLocation;
import cn.net.xinyi.xmjt.utils.ImageUtils;
import cn.net.xinyi.xmjt.utils.PositionUtil;


/**
 * Created by hao.zhou on 2015/9/14.
 * 监控室采集
 */
public class JKSInfoActivity extends BaseActivity implements View.OnClickListener,IMapDataView {

    private int fla = 0;
    private JKSInfoModle jKSInfo;
    private String path1 = null, path2 = null, path3 = null;
    private final int SYS_INTENT_REQUEST = 0XFF01;
    private final int CAMERA_INTENT_REQUEST = 0XFF02;
    private boolean bUpload = false;
    private DBOperation dbo;
    private String imagePath = null;
    private Bitmap bitmap;
    private String orientation;
    private static int image_width = 400;
    private static int image_height = 300;
    private String jksywbm;
    private boolean flag = false;
    private MapView mMapView;  // 地图标注及定位
    private BaiduMap mBaiduMap;
    private ArrayList<String> wangge = new ArrayList<String>();

    /**
     * 备注  et_BZ
     * 监控室名称  et_JKSMC
     * 联系电话  et_LXDH
     * 责任人  et_ZRR
     * 持证从业人数    et_CZSGRS
     * 安装时间  tv_AZSJ
     */
    @BindView(id = R.id.et_remarks, click = false)
    EditText et_BZ;
    @BindView(id = R.id.et_name, click = false)
    EditText et_JKSMC;
    @BindView(id = R.id.et_phone, click = false)
    EditText et_LXDH;
    @BindView(id = R.id.et_trustee, click = false)
    EditText et_ZRR;
    @BindView(id = R.id.et_certificates_number, click = false)
    EditText et_CZSGRS;
    @BindView(id = R.id.tv_collection_azdata_jks, click = true)
    TextView tv_AZSJ;
    /**
     * 正常摄像头数量  et_ZCSYSXTSL
     * 摄像头数量  et_SXTSL
     * 楼栋编码  et_LDBH
     * 设备存储公司  et_SPCZCS
     * 监控室位置  tv_JKSWZ
     * 业务类型  tv_ywfl
     */
    @BindView(id = R.id.et_camera_num_normal, click = false)
    EditText et_ZCSYSXTSL;
    @BindView(id = R.id.et_camera_num, click = false)
    EditText et_SXTSL;
    @BindView(id = R.id.et_block_number, click = false)
    EditText et_LDBH;
    @BindView(id = R.id.et_save_company, click = false)
    EditText et_SPCZCS;
    @BindView(id = R.id.tv_collection_posioion_jks, click = true)
    EditText tv_JKSWZ;
    @BindView(id = R.id.tv_ywfl, click = true)
    TextView tv_ywfl;
    /**
     * 上传监控室的三张图片  iv_ZP1  iv_ZP2  iv_ZP3
     * 缓存本地  btn_savelocal
     * //所属网格  tv_wangge
     * 所在派出所  tv_collecion_pcs
     * 手动刷新  tv_position_shoudong
     * 坐标显示  tv_gps_jks
     * 手动设置坐标  tv_position_shoudong
     */
    @BindView(id = R.id.iv_03, click = true)
    ImageView iv_ZP3;
    @BindView(id = R.id.iv_02, click = true)
    ImageView iv_ZP2;
    @BindView(id = R.id.iv_01, click = true)
    ImageView iv_ZP1;
    @BindView(id = R.id.btn_savelocal, click = true)
    Button btn_savelocal;
    @BindView(id = R.id.tv_wangge, click = true)
    TextView tv_wangge;
    @BindView(id = R.id.tv_collecion_pcs, click = false)
    TextView tv_collecion_pcs;
    @BindView(id = R.id.tv_collection_gps_jks, click = false)
    TextView tv_gps_jks;
    @BindView(id = R.id.tv_collection_position_shoudong, click = true)
    TextView tv_position_shoudong;

    //选择拍照的方式
    @BindView(id = R.id.selected_image_from_camera, click = true)
    TextView selected_image_from_camera;
    @BindView(id = R.id.selected_image_from_file, click = true)
    TextView selected_image_from_file;

    //手动定位需要隐藏的布局
    @BindView(id = R.id.sv_collection, click = false)
    ScrollView sv_collection;
    @BindView(id = R.id.rl_selected_head, click = true)
    RelativeLayout rl_selected_head;
    @BindView(id = R.id.ll_selected_head, click = false)
    LinearLayout ll_selected_head;
    private double bWd;
    private double bJD;
    private BDLocation map_location;
    private int map_type;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.camera_room_info);
        AnnotateManager.initBindView(this);
        getActionBar().setIcon(R.drawable.actionbar_back_icon_normal);
        getActionBar().setTitle("监控室采集");
        getActionBar().setHomeButtonEnabled(true);

        /** *百度地图请求当前位置信息  */
        new GetLocation(this, handler).startLocation();
        //初始化数据
        initResource();
    }

    private void initResource() {
        initMapView();
        jKSInfo = new JKSInfoModle();
        tv_collecion_pcs.setText(AppContext.instance.getLoginInfo().getPcs());
        et_JKSMC.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.length() > 24) {
                    BaseUtil.showDialog("当前文本过长", JKSInfoActivity.this);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });
        tv_JKSWZ.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.length() > 49) {
                    BaseUtil.showDialog("当前文本过长", JKSInfoActivity.this);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

    }





    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            //网格
            case R.id.tv_wangge:
                wangge.clear();
                //所属网格-字符串转为集合
                String Sswg = AppContext.instance.getLoginInfo().getSswg();
                for (int i = 0; i < Sswg.split(",").length; i++) {
                    wangge.add(Sswg.split(",")[i]);
                }

                if (wangge.size() == 0) {
                    tv_wangge.setText(R.string.msg_null);
                } else {
                    new AlertDialog.Builder(this).setItems(wangge.toArray(new String[wangge.size()]),
                            new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    tv_wangge.setText(wangge.toArray(new String[wangge.size()])[which]);
                                }
                            }).create().show();
                }
                break;


            //保存按钮
            case R.id.btn_savelocal:
                if (path1 == null || path2 == null || path3 == null) {
                    BaseUtil.showDialog("请上传3张监控室图片", JKSInfoActivity.this);
                    break;
                }

                if (et_JKSMC.getText().toString().trim().isEmpty()) {
                    BaseUtil.showDialog("监控室的名称不能为空", JKSInfoActivity.this);
                    break;
                }
                if (tv_ywfl.getText().toString().trim().isEmpty()) {
                    BaseUtil.showDialog("业务分类不能为空", JKSInfoActivity.this);
                    break;
                }
                if (tv_JKSWZ.getText().toString().trim().isEmpty()) {
                    BaseUtil.showDialog("监控室所在位置不能为空", JKSInfoActivity.this);
                    break;
                }

                if (et_LDBH.getText().toString().trim().isEmpty() ||
                        et_LDBH.getText().toString().trim().length() != 19) {
                    BaseUtil.showDialog("请输入19位正确楼栋编号", JKSInfoActivity.this);
                    break;
                }
                if (et_SXTSL.getText().toString().trim().isEmpty()) {
                    BaseUtil.showDialog("摄像头数量不能为空", JKSInfoActivity.this);
                    break;
                }
                if (tv_AZSJ.getText().toString().trim().isEmpty()) {
                    BaseUtil.showDialog("安装时间不能为空", JKSInfoActivity.this);
                    break;
                }
                if (et_ZCSYSXTSL.getText().toString().trim().isEmpty()) {
                    BaseUtil.showDialog("正常使用的摄像头数量不能为空", JKSInfoActivity.this);
                    break;
                }
                if (et_CZSGRS.getText().toString().trim().isEmpty()) {
                    BaseUtil.showDialog("持证上岗人数不能为空", JKSInfoActivity.this);
                    break;
                }
                if (et_ZRR.getText().toString().trim().isEmpty()) {
                    BaseUtil.showDialog("责任人不能为空", JKSInfoActivity.this);
                    break;
                }
                if (et_LXDH.getText().toString().trim().isEmpty() ||
                        et_LXDH.getText().toString().trim().length() < 11) {
                    BaseUtil.showDialog("请输入正确联系电话", JKSInfoActivity.this);
                    break;
                }
                if (tv_collecion_pcs.getText().toString().toString().isEmpty()) {
                    BaseUtil.showDialog("请选择所在的派出所", JKSInfoActivity.this);
                    break;
                }
                if (tv_wangge.getText().toString().trim().isEmpty()) {
                    BaseUtil.showDialog("所属网格不能为空", JKSInfoActivity.this);
                    break;
                }

                bUpload = saveToLocalDB(JKSInfoModle.ISUPDATE_LOCAL);
                if (bUpload) {
                    new AlertDialog.Builder(this).setTitle("提示")
                            .setMessage("采集监控室信息成功")
                            .setNegativeButton("查看",
                                    new DialogInterface.OnClickListener() {

                                        @Override
                                        public void onClick(DialogInterface dialog,
                                                            int which) {
                                            Intent intent = new Intent(JKSInfoActivity.this, ManageJKSInfoActivity.class);
                                            startActivity(intent);
                                            showPlateList();

                                        }
                                    })
                            .setPositiveButton("采集摄像头",
                                    new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog,
                                                            int which) {

                                            if (!jKSInfo.getJKSBH().toString().trim().isEmpty() &&
                                                    !jKSInfo.getJKSMC().toString().trim().isEmpty()) {
                                                Intent intent = new Intent(JKSInfoActivity.this, SXTInfoActivity.class);
                                                //将摄像头需要的监控室编号、监控室名称、所属网格传递
                                                Bundle mBundle = new Bundle();
                                                mBundle.putSerializable("JKSBH", jKSInfo.getJKSBH().toString().trim());
                                                mBundle.putSerializable("JKSMC", jKSInfo.getJKSMC().toString().trim());
                                                mBundle.putSerializable("WG", jKSInfo.getSSWG().toString().trim());
                                                intent.putExtras(mBundle);
                                                intent.setFlags(GeneralUtils.JKSInfoActivity);
                                                startActivity(intent);
                                                showPlateList();
                                            }
                                        }
                                    }).setCancelable(false).show();

                } else {
                    BaseUtil.showDialog("保存失败", JKSInfoActivity.this);
                }
                break;

            //业务分类
            case R.id.tv_ywfl:
                final Map<String,String> jdMaps=zdUtils.getZdlbToZdz(GeneralUtils.XXCJ_JKSYWFL);
                new AlertDialog.Builder(this).setItems(jdMaps.values().toArray(new String[jdMaps.values().size()]),
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                tv_ywfl.setText(jdMaps.values().toArray(new String[jdMaps.values().size()])[which]);
                                jksywbm=zdUtils.getMapKey(jdMaps,tv_ywfl.getText().toString());
                            }
                        }).create().show();
                break;

            //监控室安装时间
            case R.id.tv_collection_azdata_jks:
                new BaseDataUtils().getData(this,tv_AZSJ);
                break;

            //选择拍照
            case R.id.rl_selected_head:
                rl_selected_head.setVisibility(View.GONE);
                ll_selected_head.setVisibility(View.GONE);
                Animation outAnimation = AnimationUtils.loadAnimation(
                        getApplicationContext(), R.anim.push_down_out);
                ll_selected_head.startAnimation(outAnimation);
                break;

            //调用系统相册
            case R.id.selected_image_from_file:
                systemPhoto();
                break;
            //拍照
            case R.id.selected_image_from_camera:
                cameraPhoto();
                break;

            //手动定位
            case R.id.tv_collection_position_shoudong:
                /**
                 * *地图上标识当前所处的位置
                 * 如果没有获取到坐标  默认定位到龙岗中心城*/
                new GetLocation(this).mapLocation(map_location, mBaiduMap);
                flag = true;
                showMapView(flag);
                break;

            //图片  1,2，3
            case R.id.iv_01:
                fla = 1;
                //  tipCamear();
                cameraPhoto();
                break;

            case R.id.iv_02:
                fla = 2;
                //    tipCamear();
                cameraPhoto();
                break;

            case R.id.iv_03:
                fla = 3;
                //   tipCamear();
                cameraPhoto();
                break;
        }
    }


    /**
     * 保存数据到本地
     */
    private boolean saveToLocalDB(int isupdateLocal) {
        jKSInfo.setCJYH(AppContext.instance.getLoginInfo().getUsername());
        jKSInfo.setCJDW(AppContext.instance.getLoginInfo().getOrgancode());
        jKSInfo.setCJSJ(DateFormat.format("yyyy-MM-dd kk:mm:ss", Calendar.getInstance(Locale.CHINA)).toString());
        if (path1 != null) {
            jKSInfo.setZP1(path1);
        }
        if (path2 != null) {
            jKSInfo.setZP2(path2);
        }
        if (path3 != null)
            jKSInfo.setZP3(path3);
        jKSInfo.setJKSWZ(tv_JKSWZ.getText().toString().trim());
        jKSInfo.setJKSMC(et_JKSMC.getText().toString().trim());
        jKSInfo.setLDBH(et_LDBH.getText().toString().trim());
        jKSInfo.setSXTSL(Integer.parseInt(et_SXTSL.getText().toString().trim()));
        jKSInfo.setZCSYSXTSL(Integer.parseInt(et_ZCSYSXTSL.getText().toString().trim()));
        jKSInfo.setAZSJ(tv_AZSJ.getText().toString().trim());
        jKSInfo.setCZSGRS(Integer.parseInt(et_CZSGRS.getText().toString().trim()));
        jKSInfo.setZRR(et_ZRR.getText().toString().trim());
        jKSInfo.setLXDH(et_LXDH.getText().toString().trim());
        jKSInfo.setSSPCS(tv_collecion_pcs.getText().toString().trim());
        jKSInfo.setSSWG(tv_wangge.getText().toString().trim());
        jKSInfo.setBZ(et_BZ.getText().toString().trim());
        jKSInfo.setSPCZCS(et_SPCZCS.getText().toString().trim());
        jKSInfo.setJKSBH("JKS" + AppContext.instance.getLoginInfo().getUsername() + new SimpleDateFormat("yyyyMMddHHmmssSSSS").format(new Date()));
        jKSInfo.setJKSYWFL(tv_ywfl.getText().toString().trim());
        jKSInfo.setIsupdate(String.valueOf(JKSInfoModle.ISUPDATE_LOCAL));
        jKSInfo.setJKSYWFLBM(jksywbm);
        jKSInfo.setJD("" + bJD);
        jKSInfo.setWD("" + bWd);
        jKSInfo.setLOCTYPE("" + map_type);
        jKSInfo.setJKSWZ(tv_JKSWZ.getText().toString());
        long rows = 0;
        // 本地保存
        dbo = new DBOperation(JKSInfoActivity.this);
        try {
            rows = dbo.insertCamearRoomToSqlite(jKSInfo);
        } catch (AppException e) {
            e.printStackTrace();
        }
        dbo.clossDb();
        return rows != 0;
    }

    private void showPlateList() {
        this.finish();
    }


    /**
     * 图片的相关处理
     */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == SYS_INTENT_REQUEST && data != null) {
            Uri uri = data.getData();
            Cursor cursor = getContentResolver().query(uri, null, null, null, null);
            String tmpFile = "";
            if (cursor == null) {
                tmpFile = uri.getPath();
            } else {
                cursor.moveToFirst();
                tmpFile = cursor.getString(1);
                orientation = cursor.getString(cursor.getColumnIndex("orientation"));// 获取旋转的角度
                cursor.close();
            }
            getSelectImageFile(tmpFile);
            showImgs(imagePath, orientation);

        } else if (requestCode == CAMERA_INTENT_REQUEST && resultCode != 0 && imagePath != null) {
            getCaptureImageFile();
            showImgs(imagePath, "0");
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
        imagePath = ((AppContext) getApplication()).getCameraImagePath(0);

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
        rl_selected_head.setVisibility(View.GONE);
        ll_selected_head.setVisibility(View.GONE);
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
     * 打开系统相册
     */
    private void systemPhoto() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(intent, SYS_INTENT_REQUEST);
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


    private void tipCamear() {
        rl_selected_head.setVisibility(View.VISIBLE);
        ll_selected_head.setVisibility(View.VISIBLE);
        Animation inAnimation = AnimationUtils.loadAnimation(
                getApplicationContext(), R.anim.push_up_in);
        ll_selected_head.startAnimation(inAnimation);
    }

    @Override
    public void setLoction(BDLocation location) {
        map_location=location;
        /**获得纬度信息**/
        bWd=location.getLatitude();
        /**获得经度信息信息**/
        bJD=location.getLongitude();
        /**获得地址信息**/
        tv_JKSWZ.setText(location.getAddrStr());
        /**百度坐标，经纬度展现**/
        tv_gps_jks.setText("(" + bWd + "," + bJD + ")");
        map_type = map_location.getLocType();

    }

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
            btn_savelocal.setVisibility(View.GONE);
        } else {
            this.mMapView.setVisibility(View.GONE);
            sv_collection.setVisibility(View.VISIBLE);
            btn_savelocal.setVisibility(View.VISIBLE);
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


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                if (mMapView.isShown()) {
                    showMapView(false);
                } else {
                    new BaseDataUtils().loginOut(this);
                }
                break;
            default:
                break;
        }
        return true;
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