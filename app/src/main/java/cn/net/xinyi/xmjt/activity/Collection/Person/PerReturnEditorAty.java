package cn.net.xinyi.xmjt.activity.Collection.Person;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.baidu.location.BDLocation;
import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.MapView;
import com.j256.ormlite.dao.Dao;

import java.io.File;

import cn.net.xinyi.xmjt.R;
import cn.net.xinyi.xmjt.config.AppContext;
import cn.net.xinyi.xmjt.config.BaseActivity;
import cn.net.xinyi.xmjt.model.MapDataModle;
import cn.net.xinyi.xmjt.model.PerReturnModle;
import cn.net.xinyi.xmjt.model.Presenter.MapPresenter;
import cn.net.xinyi.xmjt.model.Presenter.PreReturnPresenter;
import cn.net.xinyi.xmjt.model.View.IMapDataView;
import cn.net.xinyi.xmjt.model.View.IPerReturnView;
import cn.net.xinyi.xmjt.utils.AnnotateManager;
import cn.net.xinyi.xmjt.utils.BaseDataUtils;
import cn.net.xinyi.xmjt.utils.BaseUtil;
import cn.net.xinyi.xmjt.utils.BindView;
import cn.net.xinyi.xmjt.utils.DB.DBHelperNew;
import cn.net.xinyi.xmjt.utils.GeneralUtils;
import cn.net.xinyi.xmjt.utils.GetLocation;
import cn.net.xinyi.xmjt.utils.ImageUtils;

/**
 * Created by hao.zhou on 2016/1/28.
 * 人员走访
 */
public class PerReturnEditorAty extends BaseActivity implements IPerReturnView,IMapDataView, View.OnClickListener{
    /**被走访人姓名**/
    @BindView(id = R.id.et_name,click = false)
    private EditText et_name;
    /**被走访人身份证号码**/
    @BindView(id = R.id.et_sfz,click = false)
    private EditText et_sfz;
    /**走访后地址**/
    @BindView(id = R.id.et_dzh,click = false)
    private EditText et_dzh;
    /**坐标**/
    @BindView(id = R.id.tv_zb,click = false)
    private TextView tv_zb;
    /**手动定位**/
    @BindView(id = R.id.tv_sddw,click = true)
    private TextView tv_sddw;
    /**备注**/
    @BindView(id = R.id.et_bz,click = false)
    private EditText et_bz;
    /**楼宇全景**/
    @BindView(id = R.id.iv_lyqj,click = true)
    private ImageView iv_lyqj;
    /**保存**/
    @BindView(id = R.id.btn_save,click = true)
    private Button btn_save;
    /***底部 保存  地图模式隐藏**/
    @BindView(id = R.id.ll_boom)
    private LinearLayout ll_boom;
    /***字段 信息布局  当地图模式隐藏*/
    @BindView(id = R.id.sv_all)
    private ScrollView sv_all;
    /***删除**/
    @BindView(id = R.id.btn_upl,click = true)
    private Button btn_del;
    private MapView mMapView;
    /***数据库保存
     * false 默认 如果返回值为false 保存失败
     *               返回值为true 保存成功
     * */
    private boolean flag = false;
    /**百度地图数据**/
    private BDLocation map_location;
    private BaiduMap mBaiduMap;
    /**列表模式**/
    private MenuItem action_list;
    /**地图模式**/
    private MenuItem action_map;
    /**百度坐标纬度**/
    private double bWd;
    /**百度坐标经度**/
    private double bJD;
    /**mvp结构  地图构造**/
    private MapPresenter mapPres;
    /***拍照图片路径*/
    private String imagePath;
    /***intent 用于拍照*/
    private final int CAMERA_INTENT_REQUEST = 0XFF02;
    /***ImageView拍照标识*/
    private int i_flag;
    /***拍照图片路径 存放数据库**/
    private String path1;
    private PreReturnPresenter preRePres;
    /***定位类型*/
    private int map_type;
    private PerReturnModle perInfos;
    private Dao<PerReturnModle, Integer> perRetDao;
    private String address;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.aty_per_return);
        /***注解式绑定控件**/
        AnnotateManager.initBindView(this);
        getActionBar().setIcon(R.drawable.actionbar_back_icon_normal);
        getActionBar().setTitle(getResources().getString(R.string.PER_RETURN));
        getActionBar().setHomeButtonEnabled(true);
        btn_del.setVisibility(View.VISIBLE);
        perInfos = (PerReturnModle) getIntent().getSerializableExtra(GeneralUtils.Info);
        initData();

    }

    private void initData() {
        /**presenter 初始化**/
        preRePres = new PreReturnPresenter(this);
        /**初始化 采集信息*/
        preRePres.setPreRetnData(perInfos, tv_zb);
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



    @Override
    public void onClick(View v) {
        switch (v.getId()){
            /***手动定位**/
            case R.id.tv_sddw:
                if (BaseDataUtils.isFastClick()){
                    break;
                }
                /**获得地址信息**/
                et_dzh.setText(address);
                /**百度坐标，经纬度展现**/
                tv_zb.setText("(" + bWd + "," + bJD + ")");
                /**
                 * *地图上标识当前所处的位置
                 * 如果没有获取到坐标  默认定位到龙岗中心城*/
                new GetLocation(this).mapLocation(map_location, mBaiduMap);
                /***手动定位类型*/
                flag = true;
                showMapView(flag);
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
                String msg = "";
                if (TextUtils.isEmpty(getNAME())){
                    msg =  getResources().getString(R.string.per_bfrxm)+getResources().getString(R.string.info_null) ;
                }else if (TextUtils.isEmpty(getSFZH())){
                    msg =  getResources().getString(R.string.per_sfz)+getResources().getString(R.string.info_null) ;
                }else if (TextUtils.isEmpty(getZFHDZ())){
                    msg =  getResources().getString(R.string.per_dzh)+getResources().getString(R.string.info_null) ;
                }else if (path1 == null){
                    msg = "人员照片不能为空！" ;
                }
                if(!msg.isEmpty()) {
                    BaseUtil.showDialog(msg, this);
                }else {
                    boolean flag= preRePres.saveInfo(this, GeneralUtils.ISupload,getNAME(),getId(),
                            getSFZH(),getZFHDZ(),getJd(),getWd(),
                            getBZ(),getLYQZZP(),getLOCTYPE());
                    if (flag==true){
                        setDialog(this,"更新成功");
                    }else {
                        BaseUtil.showDialog("保存失败",this);
                    }
                }
                break;

            /**弹出框
             * 提醒是否确定执行删除操作**/
            case R.id.btn_upl:
                if (BaseDataUtils.isFastClick()){
                    break;
                }
                new AlertDialog.Builder(this)
                        .setTitle(R.string.msg_delete_confirm_title)
                        .setMessage(R.string.msg_delete_confirm)
                        .setPositiveButton(R.string.sure, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                try {
                                    perRetDao = DBHelperNew.getInstance(PerReturnEditorAty.this).getPreReturnDao();
                                    /**数据库执行删除操作**/
                                    perRetDao.deleteById(perInfos.getId());
                                    /**弹出框删除成功**/
                                    setDialog(PerReturnEditorAty.this,getResources().getString(R.string.del_yes));
                                }catch (Exception e){
                                    BaseUtil.showDialog("删除失败，请稍后再试！", PerReturnEditorAty.this);
                                    e.printStackTrace();
                                }
                            }
                        })
                        .setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                            }
                        }).create().show();
                break;
        }
    }



    @Override
    public int getId() {
        return perInfos.getId();
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
    public void setNAME(String NAME) {
        et_name.setText(NAME);
    }

    @Override
    public void setSFZH(String SFZH) {
        et_sfz.setText(SFZH);
    }

    @Override
    public void setZFHDZ(String ZFHDZ) {
        et_dzh.setText(ZFHDZ);
    }

    @Override
    public void setJd(Double jd) {
        bJD=jd;
    }

    @Override
    public void setWd(Double wd) {
        bWd=wd;
    }

    @Override
    public void setBZ(String BZ) {
        et_bz.setText(BZ);
    }

    @Override
    public void setLYQZZP(String LYQZZP) {
        if (LYQZZP != null){
            path1 =LYQZZP;
            iv_lyqj.setImageBitmap(ImageUtils.compressImageByPixel(path1));
        }

    }




    @Override
    public void setLOCTYPE(String LOCTYPE) {
        map_type=Integer.parseInt(LOCTYPE);
    }

    @Override
    public void setLoction(BDLocation location) {
        map_location=location;
        /**获得地址信息**/
        address=location.getAddrStr();
        /**获得纬度信息**/
        bWd=location.getLatitude();
        /**获得经度信息信息**/
        bJD=location.getLongitude();
    }

    private Handler handler=new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what){
                /***手动定位成功**/
                case 1001:
                    MapDataModle mapData= (MapDataModle) msg.getData().getSerializable("mapData");
                    /**获得地址信息**/
                    et_dzh.setText(mapData.getAddress());
                    /**获得纬度信息**/
                    bWd=Double.valueOf("" + mapData.getLatitude());
                    /**获得经度信息信息**/
                    bJD=Double.valueOf(""+mapData.getLongitude());
                    /**百度坐标，经纬度展现**/
                    tv_zb.setText("(" + bWd + "," + bJD + ")");
                    map_type =Integer.parseInt(mapData.getLoctype());
                    showMapView(false);
                    break;
            }
        }
    };


    /**
     * 显示地图
     */
    private void showMapView(boolean mFlag) {
        if (mFlag) {
            this.mMapView.setVisibility(View.VISIBLE);
            sv_all.setVisibility(View.GONE);
            ll_boom.setVisibility(View.GONE);
        } else {
            this.mMapView.setVisibility(View.GONE);
            sv_all.setVisibility(View.VISIBLE);
            ll_boom.setVisibility(View.VISIBLE);
        }
    }

    /**
     * 手机拍照
     */
    private void cameraPhoto() {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        imagePath = ((AppContext) getApplication()).getStoreImagePath(0);
        File mFile = new File(imagePath);
        intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(mFile));
        startActivityForResult(intent, CAMERA_INTENT_REQUEST);
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
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        action_list = menu.findItem(R.id.action_list);
        action_map = menu.findItem(R.id.action_map);
        action_map.setVisible(false);
        return true;
    }

    /***activity退出*/
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
            if (mMapView.isShown()) {
                showMapView(false);
                return false;
            } else {
                this.finish();
            }
        }
        return super.dispatchKeyEvent(event);
    }
}