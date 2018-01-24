package cn.net.xinyi.xmjt.activity.Collection.ZAJC;


import android.content.Intent;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import java.util.List;

import cn.net.xinyi.xmjt.R;
import cn.net.xinyi.xmjt.config.BaseActivity;
import cn.net.xinyi.xmjt.model.Presenter.ZAJCPresenter;
import cn.net.xinyi.xmjt.model.View.IZAJCView;
import cn.net.xinyi.xmjt.model.ZAJCCYZModle;
import cn.net.xinyi.xmjt.model.ZAJCJKModle;
import cn.net.xinyi.xmjt.model.ZAJCModle;
import cn.net.xinyi.xmjt.model.ZAJCXFModle;
import cn.net.xinyi.xmjt.utils.AnnotateManager;
import cn.net.xinyi.xmjt.utils.BindView;
import cn.net.xinyi.xmjt.utils.DB.CollectDBUtils;
import cn.net.xinyi.xmjt.utils.GeneralUtils;

/**
 * Created by hao.zhou on 2016/3/19.
 */
public class ZAJCWatchAty extends BaseActivity implements IZAJCView,View.OnClickListener {
    /**采集分类**/
    @BindView(id = R.id.tv_type,click = false)
    private TextView tv_type;
    /**名称**/
    @BindView(id = R.id.et_mc,click = false)
    private EditText et_mc;
    /**地址**/
    @BindView(id = R.id.et_dz,click = false)
    private EditText et_dz;
    /**地址**/
    @BindView(id = R.id.et_mph,click = false)
    private EditText et_mph;
    /**坐标**/
    @BindView(id = R.id.tv_zb,click = false)
    private TextView tv_zb;
    /**楼栋编码**/
    @BindView(id = R.id.et_ldbm,click = false)
    private EditText et_ldbm;
    /**门牌号照片**/
    @BindView(id = R.id.iv_mph,click = false)
    private ImageView iv_mph;
    /**店门全景图**/
    @BindView(id = R.id.iv_dm,click = false)
    private ImageView iv_dm;
    /**经营者姓名**/
    @BindView(id = R.id.et_jyzxm,click = false)
    private EditText et_jyzxm;
    /**经营者电话**/
    @BindView(id = R.id.et_jyzdh,click = false)
    private EditText et_jyzdh;
    /**经营者服务电话**/
    @BindView(id = R.id.et_jyzfwdh,click = false)
    private EditText et_jyzfwdh;
    /**营业执照**/
    @BindView(id = R.id.iv_yyzz,click = false)
    private ImageView iv_yyzz;
    /**特种行业许可证**/
    @BindView(id = R.id.iv_tzhy,click = false)
    private ImageView iv_tzhy;
    /**消防验收合格证**/
    @BindView(id = R.id.iv_xfys,click = false)
    private ImageView iv_xfys;
    /**环保批文**/
    @BindView(id = R.id.iv_hbpw,click = false)
    private ImageView iv_hbpw;
    /**其他**/
    @BindView(id = R.id.iv_qt,click = false)
    private ImageView iv_qt;
    /**治安负责人**/
    @BindView(id = R.id.et_zafzr,click = false)
    private EditText et_zafzr;
    /**治安负责人电话**/
    @BindView(id = R.id.et_zafzrdh,click = false)
    private EditText et_zafzrdh;
    /**所属派出所**/
    @BindView(id = R.id.tv_sspcs,click = false)
    private TextView tv_sspcs;
    /**所属警务室**/
    @BindView(id = R.id.tv_ssjws,click = false)
    private TextView tv_ssjws;
    /**listview从业者**/
    @BindView(id = R.id.lv_cyz)
    private ListView lv_cyz;
    /**监控信息采集**/
    @BindView(id = R.id.rg_sfcjxf)
    private RadioGroup rg_sfcjxf;
    /**消防信息采集**/
    @BindView(id = R.id.rg_sfcjjk)
    private RadioGroup rg_sfcjjk;
    /**备注**/
    @BindView(id = R.id.et_bz,click = false)
    private EditText et_bz;
    /**百度坐标纬度**/
    private double bWd;
    /**百度坐标经度**/
    private double bJD;
    /***拍照图片路径 存放数据库**/
    private String path1;
    private String path2;
    private String path3;
    private String path4;
    private String path5;
    private String path6;
    private String path7;
    /***定位类型*/
    private int map_type;
    private ZAJCPresenter pres;
    private int i_sfcjjk=1;
    private int i_sfcjxf=1;
    private ZAJCModle jcInfo;
    /**是否采集监控**/
    @BindView(id = R.id.rb_sfcjjk_0)
    private RadioButton rb_sfcjjk_0;
    @BindView(id = R.id.rb_sfcjjk_1)
    private RadioButton rb_sfcjjk_1;
    /**是否采集消防**/
    @BindView(id = R.id.rb_sfcjxf_0)
    private RadioButton rb_sfcjxf_0;
    @BindView(id = R.id.rb_sfcjxf_1)
    private RadioButton rb_sfcjxf_1;
    /**是否采集监控**/
    @BindView(id = R.id.ll_jk,click = true)
    private LinearLayout ll_jk;
    @BindView(id = R.id.tv_sxtzs,click = false)
    private TextView tv_sxtzs;
    @BindView(id = R.id.tv_sxtzcs,click = false)
    private TextView tv_sxtzcs;
    /**是否采集消防**/
    @BindView(id = R.id.ll_xf,click = true)
    private LinearLayout ll_xf;
    @BindView(id = R.id.tv_mhqsl,click = false)
    private TextView tv_mhqsl;
    @BindView(id = R.id.tv_mhqzcs,click = false)
    private TextView tv_mhqzcs;
    private ZAJCJKModle jkInfo;
    private ZAJCXFModle xfInfo;
    private List<ZAJCCYZModle> cyzInfos;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getActionBar().setIcon(R.drawable.actionbar_back_icon_normal);
        getActionBar().setTitle(getString(R.string.zamanage_watch));
        getActionBar().setHomeButtonEnabled(true);
        setContentView(R.layout.aty_zabase_watch);
        /***注解式绑定控件**/
        AnnotateManager.initBindView(this);
        jcInfo = (ZAJCModle) getIntent().getSerializableExtra(GeneralUtils.Info);
        initData();
    }

    private void initData() {
        /**presenter 初始化**/
        pres = new ZAJCPresenter(this);
        /**根据关联id查询出数据库从业者信息**/
        cyzInfos= CollectDBUtils.getCYZData(ZAJCWatchAty.this, jcInfo.getGLID());
        /**根据关联id查询出数据库监控信息**/
        jkInfo= CollectDBUtils.getJKData(ZAJCWatchAty.this, jcInfo.getGLID());
        /**根据关联id查询出数据库消防信息**/
        xfInfo= CollectDBUtils.getXFData(ZAJCWatchAty.this, jcInfo.getGLID());
        /**初始化 采集信息*/
        pres.setPreRetnData(jcInfo, tv_zb);
        initLiseView();
    }
    /***初始化adapter*/
    private void initLiseView() {
        boolean flag=true;
        ZAJCCYZAdp mAdapter=new ZAJCCYZAdp(flag,lv_cyz,cyzInfos,R.layout.aty_zacyz_item,ZAJCWatchAty.this);
        lv_cyz.setAdapter(mAdapter);
        lv_cyz.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent=new Intent(ZAJCWatchAty.this,ZAJCCYZWatchAty.class);
                intent.putExtra(GeneralUtils.Info,cyzInfos.get(position));
                startActivity(intent);
            }
        });
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.ll_jk:
                Intent intent1=new Intent(ZAJCWatchAty.this,ZAJCJKWatchAty.class);
                intent1.putExtra("jkInfo", jkInfo);
                startActivity(intent1);
                break;

            case R.id.ll_xf:
                Intent intent2=new Intent(ZAJCWatchAty.this,ZAJCXFWatchAty.class);
                intent2.putExtra("xfInfo", xfInfo);
                startActivity(intent2);
                break;

        }
    }
    @Override
    public int getZAJCID() {
        return 0;
    }

    @Override
    public String getLB() {
        return null;
    }

    @Override
    public String getCJFL() {
        return tv_type.getText().toString().trim();
    }

    @Override
    public String getMC() {
        return et_mc.getText().toString().trim();
    }


    @Override
    public String getDZ() {
        return et_dz.getText().toString().trim();
    }

    @Override
    public String getMPH() {
        return et_mph.getText().toString().trim();
    }

    @Override
    public String getLDBM() {
        return et_ldbm.getText().toString().trim();
    }

    @Override
    public String getIV_MPHQJT() {
        return path1;
    }

    @Override
    public String getIV_DMQJT() {
        return path2;
    }

    @Override
    public String getJYZXM() {
        return et_jyzxm.getText().toString().trim();
    }

    @Override
    public String getJYZDH() {
        return et_jyzdh.getText().toString().trim();
    }

    @Override
    public String getFWDH() {
        return et_jyzfwdh.getText().toString().trim();
    }

    @Override
    public String getIV_YYZZ() {
        return path3;
    }

    @Override
    public String getIV_QT() {
        return path4;
    }

    @Override
    public String getIV_HBPW() {
        return path5;
    }

    @Override
    public String getIV_XFYS() {
        return path6;
    }

    @Override
    public String getIV_TZHY() {
        return path7;
    }

    @Override
    public String getZAFZR() {
        return et_zafzr.getText().toString().trim();
    }

    @Override
    public String getSSPCS() {
        return tv_sspcs.getText().toString().trim();
    }

    @Override
    public String getSSJWS() {
        return tv_ssjws.getText().toString().trim();
    }

    @Override
    public Double getJD() {
        return bJD;
    }

    @Override
    public Double getWD() {
        return bWd;
    }
    @Override
    public String getZAFZRDH() {
        return et_zafzrdh.getText().toString().trim();
    }

    @Override
    public int getSFCJJK() {
        return i_sfcjjk;
    }

    @Override
    public int getSFCJXF() {
        return i_sfcjxf;
    }

    @Override
    public String getBZ() {
        return et_bz.getText().toString();
    }

    @Override
    public String getLOCTYPE() {
        return ""+map_type;
    }

    @Override
    public void setLB(String LB) {

    }

    @Override
    public void setCJFL(String CJFL) {
        tv_type.setText(CJFL);
    }

    @Override
    public void setMC(String MC) {
        et_mc.setText(MC);
    }

    @Override
    public void setDZ(String DZ) {
        et_dz.setText(DZ);
    }

    @Override
    public void setMPH(String MPH) {
        et_mph.setText(MPH);
    }

    @Override
    public void setLDBM(String LDBM) {
        et_ldbm.setText(LDBM);
    }

    @Override
    public void setIV_MPHQJT(String IV_MPHQJT) {
        if (IV_MPHQJT != null){
            path1 =IV_MPHQJT;
            iv_mph.setImageBitmap(BitmapFactory.decodeFile(path1));
        }
    }

    @Override
    public void setIV_DMQJT(String IV_DMQJT) {
        if (IV_DMQJT != null){
            path2 =IV_DMQJT;
            iv_dm.setImageBitmap(BitmapFactory.decodeFile(path2));
        }
    }

    @Override
    public void setJYZXM(String JYZXM) {
        et_jyzxm.setText(JYZXM);
    }

    @Override
    public void setJYZDH(String JYZDH) {
        et_jyzdh.setText(JYZDH);
    }

    @Override
    public void setFWDH(String FWDH) {
        et_jyzfwdh.setText(FWDH);
    }

    @Override
    public void setIV_YYZZ(String IV_YYZZ) {
        if (IV_YYZZ != null){
            path3 =IV_YYZZ;
            iv_yyzz.setImageBitmap(BitmapFactory.decodeFile(path3));
        }
    }

    @Override
    public void setIV_HBPW(String IV_HBPW) {
        if (IV_HBPW != null){
            path5 =IV_HBPW;
            iv_hbpw.setImageBitmap(BitmapFactory.decodeFile(path5));
        }
    }

    @Override
    public void setIV_TZHY(String IV_TZHY) {
        if (IV_TZHY != null){
            path7 =IV_TZHY;
            iv_tzhy.setImageBitmap(BitmapFactory.decodeFile(path7));
        }
    }

    @Override
    public void setIV_QT(String IV_QT) {
        if (IV_QT != null){
            path4 =IV_QT;
            iv_qt.setImageBitmap(BitmapFactory.decodeFile(path4));
        }
    }

    @Override
    public void setIV_XFYS(String IV_XFYS) {
        if (IV_XFYS != null){
            path6 =IV_XFYS;
            iv_xfys.setImageBitmap(BitmapFactory.decodeFile(path6));
        }
    }

    @Override
    public void setZAFZR(String ZAFZR) {
        et_zafzr.setText(ZAFZR);
    }

    @Override
    public void setSSPCS(String SSPCS) {
        tv_sspcs.setText(SSPCS);
    }

    @Override
    public void setSSJWS(String SSJWS) {
        tv_ssjws.setText(SSJWS);
    }

    @Override
    public void setJD(Double JD) {
        bJD=JD;
    }

    @Override
    public void setWD(Double WD) {
        bWd=WD;
    }

    @Override
    public void setZAFZRDH(String ZAFZRDH) {
        et_zafzrdh.setText(ZAFZRDH);
    }

    @Override
    public void setSFCJJK(int SFCJJK) {
        if (jkInfo!=null && 0==SFCJJK){
            rb_sfcjjk_0.setChecked(true);
            if (jkInfo.getSXTZS()!=null)
                tv_sxtzs.setText(jkInfo.getSXTZS() );
            if (jkInfo.getZCS()!=null)
                tv_sxtzcs.setText(jkInfo.getZCS());
            ll_jk.setVisibility(View.VISIBLE);
        }else {
            rb_sfcjjk_1.setChecked(true);
        }
    }

    @Override
    public void setSFCJXF(int SFCJXF) {
        if (xfInfo !=null && 0==SFCJXF){
            rb_sfcjxf_0.setChecked(true);
            if (xfInfo.getMHQSL()!=null)
                tv_mhqsl.setText(xfInfo.getMHQSL());
            if (xfInfo.getZCS()!=null)
                tv_mhqzcs.setText(xfInfo.getZCS());
            ll_xf.setVisibility(View.VISIBLE);
        }else {
            rb_sfcjxf_1.setChecked(true);
        }
    }

    @Override
    public void setLOCTYPE(String LOCTYPE) {

    }

    @Override
    public void setBZ(String BZ) {
        et_bz.setText(BZ);
    }


    /**
     * 捕获后退按钮
     */
    @Override
    public boolean dispatchKeyEvent(KeyEvent event) {
        if (event.getAction() == KeyEvent.ACTION_DOWN
                && event.getKeyCode() == KeyEvent.KEYCODE_BACK) {
            this.finish();
        }
        // super.dispatchKeyEvent(event);
        return super.dispatchKeyEvent(event);
    }


    /***activity退出*/
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {

            case android.R.id.home:
                this.finish();
                break;

            default:
                break;
        }
        return true;
    }


}