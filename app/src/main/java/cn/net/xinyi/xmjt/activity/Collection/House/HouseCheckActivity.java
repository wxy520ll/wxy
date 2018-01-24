package cn.net.xinyi.xmjt.activity.Collection.House;


import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.util.TypeUtils;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.nostra13.universalimageloader.core.ImageLoader;

import org.apache.http.Header;

import java.util.List;

import cn.net.xinyi.xmjt.R;
import cn.net.xinyi.xmjt.activity.ZHFK.PlcBx.PickupMapActivity;
import cn.net.xinyi.xmjt.api.ApiHttpClient;
import cn.net.xinyi.xmjt.api.ApiResource;
import cn.net.xinyi.xmjt.config.BaseWithLocActivity;
import cn.net.xinyi.xmjt.model.HouseCheckModle;
import cn.net.xinyi.xmjt.model.LocaionInfo;
import cn.net.xinyi.xmjt.utils.AnnotateManager;
import cn.net.xinyi.xmjt.utils.BaseDataUtils;
import cn.net.xinyi.xmjt.utils.BindView;
import cn.net.xinyi.xmjt.utils.ImageUtils;
import cn.net.xinyi.xmjt.utils.StringUtils;
import cn.net.xinyi.xmjt.utils.UI;

public class HouseCheckActivity extends BaseWithLocActivity implements RadioGroup.OnCheckedChangeListener,View.OnClickListener {

    //人防包括物业名称、住宅楼栋数等信息（花园小区显示，如果为城中村当有物业显示）
    @BindView(id = R.id.ll_rf)
    private LinearLayout ll_rf;
    //城中村人防是否物业管理
    @BindView(id = R.id.ll_rf_wy)
    private LinearLayout ll_rf_wy;
    //城中村人防是否服务站、业主管理等信息
    @BindView(id = R.id.ll_rf_fwz)
    private LinearLayout ll_rf_fwz;
    //物防
    @BindView(id = R.id.ll_wf)
    private LinearLayout ll_wf;
    //消防
    @BindView(id = R.id.ll_xf)
    private LinearLayout ll_xf;
    //其他 人防图片
    @BindView(id = R.id.ll_rf_qt)
    private LinearLayout ll_rf_qt;
    //其他 物防图片
    @BindView(id = R.id.ll_wf_qt)
    private LinearLayout ll_wf_qt;
    //其他消防图片
    @BindView(id = R.id.ll_xf_qt)
    private LinearLayout ll_xf_qt;
    //其他技防图片
    @BindView(id = R.id.ll_jf_qt)
    private LinearLayout ll_jf_qt;
    //宿舍人防是否
    @BindView(id = R.id.ll_rf_ss)
    private LinearLayout ll_rf_ss;
    //是否视频门禁
    @BindView(id = R.id.ll_jf_spmj)
    private LinearLayout ll_jf_spmj;
    //没有视频门禁
    @BindView(id = R.id.ll_jf_mj_no)
    private LinearLayout ll_jf_mj_no;
    //是否视频门禁
    @BindView(id = R.id.ll_jf)
    private LinearLayout ll_jf;
    //一键报警
    @BindView(id = R.id.ll_jf_bj)
    private LinearLayout ll_jf_bj;

    //出租屋类别
    @BindView(id = R.id.tv_lb,click = true)
    private TextView tv_lb;

    //楼栋编码
    @BindView(id = R.id.tv_ldbm)
    private TextView tv_ldbm;
    /**
     * 是否开通居住证
     **/
    @BindView(id = R.id.rg_ktjzz)
    private RadioGroup rg_ktjzz;
    @BindView(id = R.id.rb_ktjzz_0)
    private RadioButton rb_ktjzz_0;
    @BindView(id = R.id.rb_ktjzz_1)
    private RadioButton rb_ktjzz_1;
    int i_ktjzz = 3;

    //人防
    /**
     * 是否有物业
     **/
    @BindView(id = R.id.rg_wygl)
    private RadioGroup rg_wygl;
    @BindView(id = R.id.rb_wygl_0)
    private RadioButton rb_wygl_0;
    @BindView(id = R.id.rb_wygl_1)
    private RadioButton rb_wygl_1;
    int i_wygl = 3;

    //物业公司名称
    @BindView(id = R.id.et_wymc)
    private EditText et_wymc;

    //住宅楼栋数
    @BindView(id = R.id.et_zzlds)
    private EditText et_zzlds;

    //保安员数量
    @BindView(id = R.id.et_baysl)
    private EditText et_baysl;

    //门岗数量
    @BindView(id = R.id.et_mgsl)
    private EditText et_mgsl;

    //巡逻保安人数
    @BindView(id = R.id.et_xlbars)
    private EditText et_xlbars;


    //保安排班情况
    @BindView(id = R.id.et_bapbqk)
    private EditText et_bapbqk;

    /**
     * 是否有服务站
     **/
    @BindView(id = R.id.rg_sffwz)
    private RadioGroup rg_sffwz;
    @BindView(id = R.id.rb_sffwz_0)
    private RadioButton rb_sffwz_0;
    @BindView(id = R.id.rb_sffwz_1)
    private RadioButton rb_sffwz_1;
    int i_sffwz = 3;

    /**
     * 是否有楼栋长
     **/
    @BindView(id = R.id.rg_sfldz)
    private RadioGroup rg_sfldz;
    @BindView(id = R.id.rb_sfldz_0)
    private RadioButton rb_sfldz_0;
    @BindView(id = R.id.rb_sfldz_1)
    private RadioButton rb_sfldz_1;
    int i_sfldz = 3;

    /**
     * 是否业主管理
     **/
    @BindView(id = R.id.rg_sfyzgl)
    private RadioGroup rg_sfyzgl;
    @BindView(id = R.id.rb_sfyzgl_0)
    private RadioButton rb_sfyzgl_0;
    @BindView(id = R.id.rb_sfyzgl_1)
    private RadioButton rb_sfyzgl_1;
    int i_sfyzgl = 3;

    /**
     * 是否有管理
     **/
    @BindView(id = R.id.rg_sfgl)
    private RadioGroup rg_sfgl;
    @BindView(id = R.id.rb_sfgl_0)
    private RadioButton rb_sfgl_0;
    @BindView(id = R.id.rb_sfgl_1)
    private RadioButton rb_sfgl_1;
    int i_sfgl = 3;

    //保安值守情况
    @BindView(id = R.id.et_bazsqk)
    private EditText et_bazsqk;
    //宿舍管理情况
    @BindView(id = R.id.et_ssglqk)
    private EditText et_ssglqk;
    //人防图片
    @BindView(id = R.id.iv_rf, click = true)
    private ImageView iv_rf;


    //物防
    /**
     * 是否安装防盗门
     **/
    @BindView(id = R.id.rg_sffdm)
    private RadioGroup rg_sffdm;
    @BindView(id = R.id.rb_sffdm_0)
    private RadioButton rb_sffdm_0;
    @BindView(id = R.id.rb_sffdm_1)
    private RadioButton rb_sffdm_1;
    int i_sffdm = 3;

    /**
     * 是否B级锁芯
     **/
    @BindView(id = R.id.rg_sfbjs)
    private RadioGroup rg_sfbjs;
    @BindView(id = R.id.rb_sfbjs_0)
    private RadioButton rb_sfbjs_0;
    @BindView(id = R.id.rb_sfbjs_1)
    private RadioButton rb_sfbjs_1;
    int i_sfbjs = 3;

    /**
     * 是否水管安装爬刺
     **/
    @BindView(id = R.id.rg_sfazpc)
    private RadioGroup rg_sfazpc;
    @BindView(id = R.id.rb_sfazpc_0)
    private RadioButton rb_sfazpc_0;
    @BindView(id = R.id.rb_sfazpc_1)
    private RadioButton rb_sfazpc_1;
    int i_sfazpc = 3;

    //物防图片
    @BindView(id = R.id.iv_wf, click = true)
    private ImageView iv_wf;

    //消防
    /**
     * 是否消防器材
     **/
    @BindView(id = R.id.rg_sfxfqc)
    private RadioGroup rg_sfxfqc;
    @BindView(id = R.id.rb_sfxfqc_0)
    private RadioButton rb_sfxfqc_0;
    @BindView(id = R.id.rb_sfxfqc_1)
    private RadioButton rb_sfxfqc_1;
    int i_sfxfqc = 3;

    /**
     * 是否配置灭火器
     **/
    @BindView(id = R.id.rg_sfmhq)
    private RadioGroup rg_sfmhq;
    @BindView(id = R.id.rb_sfmhq_0)
    private RadioButton rb_sfmhq_0;
    @BindView(id = R.id.rb_sfmhq_1)
    private RadioButton rb_sfmhq_1;
    int i_sfmhq = 3;

    /**
     * 是否配置应急照明灯
     **/
    @BindView(id = R.id.rg_sfzmd)
    private RadioGroup rg_sfzmd;
    @BindView(id = R.id.rb_sfzmd_0)
    private RadioButton rb_sfzmd_0;
    @BindView(id = R.id.rb_sfzmd_1)
    private RadioButton rb_sfzmd_1;
    int i_sfzmd = 3;

    /**
     * 是否水阀完好
     **/
    @BindView(id = R.id.rg_sfxfsf)
    private RadioGroup rg_sfxfsf;
    @BindView(id = R.id.rb_sfxfsf_0)
    private RadioButton rb_sfxfsf_0;
    @BindView(id = R.id.rb_sfxfsf_1)
    private RadioButton rb_sfxfsf_1;
    int i_sfxfsf = 3;

    //消防图片
    @BindView(id = R.id.iv_xf, click = true)
    private ImageView iv_xf;

    //技防
    /**
     * 技防 是否有视频门禁
     **/
    @BindView(id = R.id.rg_spmj)
    private RadioGroup rg_spmj;
    @BindView(id = R.id.rb_spmj_0)
    private RadioButton rb_spmj_0;
    @BindView(id = R.id.rb_spmj_1)
    private RadioButton rb_spmj_1;
    int i_spmj = 3;

    //公共区域探头数量
    @BindView(id = R.id.et_ggqy)
    private EditText et_ggqy;
    //高清探头数量
    @BindView(id = R.id.et_gqtt)
    private EditText et_gqtt;
    //普通探头数量
    @BindView(id = R.id.et_pttt)
    private EditText et_pttt;
    //视频损坏数量
    @BindView(id = R.id.et_spshsl)
    private EditText et_spshsl;
    //门禁损坏数量
    @BindView(id = R.id.et_mjshsl)
    private EditText et_mjshsl;
    /**
     * 是否安装探头
     **/
    @BindView(id = R.id.rg_aztt)
    private RadioGroup rg_aztt;
    @BindView(id = R.id.rb_aztt_0)
    private RadioButton rb_aztt_0;
    @BindView(id = R.id.rb_aztt_1)
    private RadioButton rb_aztt_1;
    int i_aztt = 3;

    /**
     * 是否安装一键报警
     **/
    @BindView(id = R.id.rg_yjbb)
    private RadioGroup rg_yjbb;
    @BindView(id = R.id.rb_yjbb_0)
    private RadioButton rb_yjbb_0;
    @BindView(id = R.id.rb_yjbb_1)
    private RadioButton rb_yjbb_1;
    int i_yjbb = 3;

    /**
     * 是否安装单机视频门禁
     **/
    @BindView(id = R.id.rg_azdjspmj)
    private RadioGroup rg_azdjspmj;
    @BindView(id = R.id.rb_azdjspmj_0)
    private RadioButton rb_azdjspmj_0;
    @BindView(id = R.id.rb_azdjspmj_1)
    private RadioButton rb_azdjspmj_1;
    int i_azdjspmj = 3;

    /**
     * 是否门禁双向刷卡
     **/
    @BindView(id = R.id.rg_sfsxsk)
    private RadioGroup rg_sfsxsk;
    @BindView(id = R.id.rb_sfsxsk_0)
    private RadioButton rb_sfsxsk_0;
    @BindView(id = R.id.rb_sfsxsk_1)
    private RadioButton rb_sfsxsk_1;
    int i_sfsxsk = 3;

    //录像保存时间
    @BindView(id = R.id.et_lxbcsj)
    private EditText et_lxbcsj;
    //技防图片
    @BindView(id = R.id.iv_jf,click = true)
    private ImageView iv_jf;
    //上传
    @BindView(id = R.id.btn_upl, click = true)
    private ImageView btn_upl;
    //    //地址
//    @BindView(id = R.id.et_dz)
//    private EditText et_dz;

    //   街道
    @BindView(id = R.id.et_jd)
    private EditText et_jd;
    //   社区
    @BindView(id = R.id.et_sq)
    private EditText et_sq;
    //   地址
    @BindView(id = R.id.et_hy)
    private EditText et_hy;//
    //  地址
    @BindView(id = R.id.et_mph)
    private EditText et_mph;
    //   地址
    @BindView(id = R.id.et_dzx)
    private EditText et_dzx;

    //非花园地址
    @BindView(id = R.id.ll_dz)
    private LinearLayout ll_dz;
    //花园地址
    @BindView(id = R.id.ll_dz_hy)
    private LinearLayout ll_dz_hy;

    //坐标
    @BindView(id = R.id.tv_zb)
    private TextView tv_zb;
    //手动定位
    @BindView(id = R.id.tv_sddw,click = true)
    private TextView tv_sddw;

    /***ImageView拍照标识*/
    private int i_flag;
    /***拍照图片路径 存放数据库**/
    private String path1;
    private String path2;
    private String path3;
    private String path4;
    private List<HouseCheckModle> houseCheckModles;
    private boolean flag=false;
    /**百度坐标纬度**/
    private double bWd;
    /**百度坐标经度**/
    private double bJD;
    private LocaionInfo mLocation;
    private int map_type;
    private String addRess;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.aty_house);
        AnnotateManager.initBindView(this);//注解式绑定控件
        TypeUtils.compatibleWithJavaBean = true;
        initView();
    }



    @Override
    public void onReceiveLoc(LocaionInfo location, boolean isSuccess, Throwable errMsg) {
        if (!flag&&isSuccess) {
            mLocation = location;
            /**获得纬度信息**/
            bWd=mLocation.getLat();
            /**获得经度信息信息**/
            bJD=mLocation.getLongt();
            addRess=mLocation.getAddress();
            map_type = mLocation.getLoctype();
            //et_dz.setText(addRess);  //获得地址信息
            tv_zb.setText("(" + bWd + "," + bJD + ")"); //百度坐标，经纬度展现
        }
    }

    @Override
    public void onClick(View view) {
        String msg = "";
        switch (view.getId()) {
            case R.id.tv_sddw:
                Intent intent=new Intent(this,PickupMapActivity.class);
                startActivityForResult(intent, PickupMapActivity.MAP_PICK_UP);
                break;

            case R.id.tv_lb:
                BaseDataUtils.showAlertDialog(HouseCheckActivity.this, HouseCheckModle.sLB, tv_lb);
                break;

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
                i_flag = 4;
                cameraPhoto();
                break;

            case R.id.btn_upl:
                if (HouseCheckModle.lb_hyxq.equals(StringUtils.nullToString(tv_lb.getText().toString()))) {//花园小区
                    if (i_ktjzz == 3) {
                        msg = "请选择是否开通居住证";
                    } else if (et_jd.getText().toString().isEmpty()||et_sq.getText().toString().isEmpty()
                            ||et_hy.getText().toString().isEmpty()) {
                        msg = "请填写正确地址";
                    } else if (bJD< 0.0||bWd<0.0) {
                        msg = "经纬度坐标不能为空";
                    } else if (et_wymc.getText().toString().isEmpty()) {
                        msg = "请输入物业公司名称";
                    } else if (et_zzlds.getText().toString().isEmpty()) {
                        msg = "请输入住宅楼栋数";
                    } else if (et_baysl.getText().toString().isEmpty()) {
                        msg = "请输入保安员数量";
                    } else if (et_mgsl.getText().toString().isEmpty()) {
                        msg = "请输入门岗数量";
                    } else if (et_xlbars.getText().toString().isEmpty()) {
                        msg = "请输入巡逻保安员数量";
                    } else if (et_bapbqk.getText().toString().isEmpty()) {
                        msg = "请输入保安排班情况";
                    } else if (i_sffdm == 3) {
                        msg = "请选择是否安装防盗门";
                    } else if (i_sfbjs == 3) {
                        msg = "请选择锁芯是否是B级锁";
                    } else if (i_sfazpc == 3) {
                        msg = "请选择排水管是否安装爬刺和涂抹黄油";
                    } else if (i_sfxfqc == 3) {
                        msg = "请选择房屋是否有消防器材";
                    } else if (i_sfmhq == 3) {
                        msg = "请选择是否有灭火器";
                    } else if (i_sfzmd == 3) {
                        msg = "请选择是否有应急照明灯";
                    } else if (i_sfxfsf == 3) {
                        msg = "请选择消防水阀是否完好";
                    } else if (et_ggqy.getText().toString().isEmpty()) {
                        msg = "请输入公共区域探头数量";
                    } else if (et_gqtt.getText().toString().isEmpty()) {
                        msg = "请输入高清探头数量";
                    } else if (et_pttt.getText().toString().isEmpty()) {
                        msg = "请输入普通探头数量";
                    } else if (et_spshsl.getText().toString().isEmpty()) {
                        msg = "请输入视频损坏数量";
                    } else if (i_aztt == 3) {
                        msg = "请选择周围外墙是否安装探头";
                    } else if (i_yjbb == 3) {
                        msg = "请选择是否安装与派出所联网的一键报警";
                    }
                    if (!msg.isEmpty()) {
                        UI.toast(this, msg);
                    } else {
                        HouseCheckModle houseCheckModle = new HouseCheckModle();
                        houseCheckModle.setTYPE(HouseCheckModle.getTypeNum(tv_lb.getText().toString()));
                        houseCheckModle.setSFKTJZDJZH(i_ktjzz);
                        houseCheckModle.setWYMC(et_wymc.getText().toString());
                        houseCheckModle.setZZLDS(et_zzlds.getText().toString());
                        houseCheckModle.setBAYSL(et_baysl.getText().toString());
                        houseCheckModle.setXLBARS(et_xlbars.getText().toString());
                        houseCheckModle.setMGSL(et_mgsl.getText().toString());
                        houseCheckModle.setBAPBQK(et_bapbqk.getText().toString());
                        houseCheckModle.setLON(bJD);
                        houseCheckModle.setLAT(bWd);
                        houseCheckModle.setJD(et_jd.getText().toString());
                        houseCheckModle.setSQ(et_sq.getText().toString());
                        houseCheckModle.setHY(et_hy.getText().toString());

                        //   houseCheckModle.setDZ(et_dz.getText().toString());
                        houseCheckModle.setLOCTYPE(map_type);

                        houseCheckModle.setSFAZFDM(i_sffdm);
                        houseCheckModle.setSXSFBJS(i_sfbjs);
                        houseCheckModle.setPSGSFAZPCHTMHY(i_sfazpc);
                        houseCheckModle.setFWSFYXFQC(i_sfxfqc);
                        houseCheckModle.setSFYMHQ(i_sfmhq);
                        houseCheckModle.setSFYYJZMD(i_sfzmd);
                        houseCheckModle.setXFSZSFWH(i_sfxfsf);

                        houseCheckModle.setGGQYTTSL(et_ggqy.getText().toString());
                        houseCheckModle.setGQTTSL(et_gqtt.getText().toString());
                        houseCheckModle.setPTTTSL(et_pttt.getText().toString());
                        houseCheckModle.setSPSHSL(et_spshsl.getText().toString());
                        houseCheckModle.setZWWQSFAZTT(i_aztt);
                        houseCheckModle.setSFAZYPCSLWDYJBJ(i_yjbb);
                        houseCheckModle.setLDID(getIntent().getStringExtra("ldbh"));
                        initInentAty(houseCheckModle);
                    }
                } else if (HouseCheckModle.lb_czc.equals(StringUtils.nullToString(tv_lb.getText().toString()))) {//城中村
                    if (i_ktjzz == 3) {
                        msg = "请选择是否开通居住证";
                    } else if (et_jd.getText().toString().isEmpty()||et_sq.getText().toString().isEmpty()
                            ||et_dzx.getText().toString().isEmpty()||et_mph.getText().toString().isEmpty()) {
                        msg = "请填写正确地址";
                    } else if (bJD< 0.0||bWd<0.0) {
                        msg = "地址或经纬度坐标不能为空";
                    } else if (i_wygl == 3) {
                        msg = "请选择是否有物业公司管理";
                    }  else  if (i_wygl == 0&&et_wymc.getText().toString().isEmpty()) {
                        msg = "请输入物业公司名称";
                    } else if (i_wygl == 0&&et_zzlds.getText().toString().isEmpty()) {
                        msg = "请输入住宅楼栋数";
                    } else if (i_wygl == 0&&et_baysl.getText().toString().isEmpty()) {
                        msg = "请输入保安员数量";
                    } else if (i_wygl == 0&&et_mgsl.getText().toString().isEmpty()) {
                        msg = "请输入门岗数量";
                    } else if (i_wygl == 0&&et_xlbars.getText().toString().isEmpty()) {
                        msg = "请输入巡逻保安员数量";
                    } else if (i_wygl == 0&&et_bapbqk.getText().toString().isEmpty()) {
                        msg = "请输入保安排班情况";
                    }else if (i_wygl == 1&&i_sffwz == 3) {
                        msg = "请选择是否配置楼栋服务站";
                    } else if (i_wygl == 1&&i_sfldz == 3) {
                        msg = "请选择是否配置楼栋长";
                    } else if (i_wygl == 1&&i_sfyzgl == 3) {
                        msg = "请选择是否业主管理";
                    } else if (i_sffdm == 3) {
                        msg = "请选择是否安装防盗门";
                    } else if (i_sfbjs == 3) {
                        msg = "请选择锁芯是否是B级锁";
                    } else if (i_sfazpc == 3) {
                        msg = "请选择排水管是否安装爬刺和涂抹黄油";
                    } else if (i_sfxfqc == 3) {
                        msg = "请选择房屋是否有消防器材";
                    } else if (i_sfmhq == 3) {
                        msg = "请选择是否有灭火器";
                    } else if (i_sfzmd == 3) {
                        msg = "请选择是否有应急照明灯";
                    } else if (i_sfxfsf == 3) {
                        msg = "请选择消防水阀是否完好";
                    } else if (i_spmj == 3) {
                        msg = "请选择是否安装视频门禁";
                    } else  if (i_spmj == 0&&et_ggqy.getText().toString().isEmpty()) {
                        msg = "请输入公共区域探头数量";
                    } else if (i_spmj == 0&&et_gqtt.getText().toString().isEmpty()) {
                        msg = "请输入高清探头数量";
                    } else if (i_spmj == 0&&et_pttt.getText().toString().isEmpty()) {
                        msg = "请输入普通探头数量";
                    } else if (i_spmj == 0&&et_spshsl.getText().toString().isEmpty()) {
                        msg = "请输入视频损坏数量";
                    } else if (i_spmj == 1&&et_mjshsl.getText().toString().isEmpty()) {
                        msg = "请输入门禁损坏数量";
                    } else if (i_spmj == 0&&i_aztt == 3) {
                        msg = "请选择周围外墙是否安装探头";
                    } else if (i_spmj == 1&&i_azdjspmj == 3) {
                        msg = "请选择是否安装单机视频门禁";
                    } else if (i_spmj == 1&&i_sfsxsk == 3) {
                        msg = "请选择门禁是否双向刷卡";
                    } else if (i_spmj == 1&&et_lxbcsj.getText().toString().isEmpty()) {
                        msg = "请输入录像保存时间";
                    } else if (i_yjbb == 3) {
                        msg = "请选择是否安装与派出所联网的一键报警";
                    }
                    if (!msg.isEmpty()) {
                        UI.toast(this, msg);
                    } else {
                        HouseCheckModle houseCheckModle =new HouseCheckModle();
                        houseCheckModle.setTYPE(HouseCheckModle.getTypeNum(tv_lb.getText().toString()));
                        houseCheckModle.setSFKTJZDJZH(i_ktjzz);
                        houseCheckModle.setSFYWYGLGS(i_wygl);
                        houseCheckModle.setLON(bJD);
                        houseCheckModle.setLAT(bWd);
                        houseCheckModle.setJD(et_jd.getText().toString());
                        houseCheckModle.setSQ(et_sq.getText().toString());
                        houseCheckModle.setDZX(et_dzx.getText().toString());
                        houseCheckModle.setMPH(et_mph.getText().toString());
                        //   houseCheckModle.setDZ(et_dz.getText().toString());
                        houseCheckModle.setLOCTYPE(map_type);
                        if (i_wygl==0){
                            houseCheckModle.setWYMC(et_wymc.getText().toString());
                            houseCheckModle.setZZLDS(et_zzlds.getText().toString());
                            houseCheckModle.setBAYSL(et_baysl.getText().toString());
                            houseCheckModle.setXLBARS(et_xlbars.getText().toString());
                            houseCheckModle.setMGSL(et_mgsl.getText().toString());
                            houseCheckModle.setBAPBQK(et_bapbqk.getText().toString());
                        }else if (i_wygl==1){
                            houseCheckModle.setSFPBLDFWZ(i_sffwz);
                            houseCheckModle.setSFPBLDZ(i_sfldz);
                            houseCheckModle.setSFYZGL (i_sfyzgl);
                        }
                        houseCheckModle.setSFAZFDM(i_sffdm);
                        houseCheckModle.setSXSFBJS(i_sfbjs);
                        houseCheckModle.setPSGSFAZPCHTMHY(i_sfazpc);
                        houseCheckModle.setFWSFYXFQC(i_sfxfqc);
                        houseCheckModle.setSFYMHQ(i_sfmhq);
                        houseCheckModle.setSFYYJZMD(i_sfzmd);
                        houseCheckModle.setXFSZSFWH(i_sfxfsf);
                        houseCheckModle.setSFAZSPMJ (i_spmj);
                        if (i_spmj==0){
                            houseCheckModle.setGGQYTTSL(et_ggqy.getText().toString());
                            houseCheckModle.setGQTTSL(et_gqtt.getText().toString());
                            houseCheckModle.setPTTTSL(et_pttt.getText().toString());
                            houseCheckModle.setSPSHSL(et_spshsl.getText().toString());
                            houseCheckModle.setZWWQSFAZTT(i_aztt);
                        }else if (i_spmj==1){
                            houseCheckModle.setSFAZDJSPMJ(i_azdjspmj);
                            houseCheckModle.setMJSFSXSK(i_sfsxsk);
                            houseCheckModle.setLXZLBCSJ(et_lxbcsj.getText().toString());
                            houseCheckModle.setMJSHSL(et_mjshsl.getText().toString());
                        }
                        houseCheckModle.setLDID(getIntent().getStringExtra("ldbh"));
                        houseCheckModle.setSFAZYPCSLWDYJBJ (i_yjbb);
                        initInentAty(houseCheckModle);
                    }
                } else if (HouseCheckModle.lb_gcss.equals(StringUtils.nullToString(tv_lb.getText().toString()))) {//工厂宿舍
                    if (i_ktjzz == 3) {
                        msg = "请选择是否开通居住证";
                    } else if (et_jd.getText().toString().isEmpty()||et_sq.getText().toString().isEmpty()
                            ||et_dzx.getText().toString().isEmpty()||et_mph.getText().toString().isEmpty()) {
                        msg = "请填写正确地址";
                    } else if (bJD< 0.0||bWd<0.0) {
                        msg = "地址或经纬度坐标不能为空";
                    } else if (i_sfgl == 3) {
                        msg = "请选择是否有管理员";
                    }  else  if (et_bazsqk.getText().toString().isEmpty()) {
                        msg = "请输入保安值守情况";
                    } else if (et_ssglqk.getText().toString().isEmpty()) {
                        msg = "请输入宿舍管理情况";
                    } else if (i_sffdm == 3) {
                        msg = "请选择是否安装防盗门";
                    } else if (i_sfbjs == 3) {
                        msg = "请选择锁芯是否是B级锁";
                    } else if (i_sfazpc == 3) {
                        msg = "请选择排水管是否安装爬刺和涂抹黄油";
                    } else if (i_sfxfqc == 3) {
                        msg = "请选择房屋是否有消防器材";
                    } else if (i_sfmhq == 3) {
                        msg = "请选择是否有灭火器";
                    } else if (i_sfzmd == 3) {
                        msg = "请选择是否有应急照明灯";
                    } else if (i_sfxfsf == 3) {
                        msg = "请选择消防水阀是否完好";
                    } else if (i_spmj == 3) {
                        msg = "请选择是否安装视频门禁";
                    } else  if (et_ggqy.getText().toString().isEmpty()) {
                        msg = "请输入公共区域探头数量";
                    } else if (et_gqtt.getText().toString().isEmpty()) {
                        msg = "请输入高清探头数量";
                    } else if (et_pttt.getText().toString().isEmpty()) {
                        msg = "请输入普通探头数量";
                    } else if (et_spshsl.getText().toString().isEmpty()) {
                        msg = "请输入视频损坏数量";
                    } else if (i_spmj == 0&&i_aztt == 3) {
                        msg = "请选择周围外墙是否安装探头";
                    } else if (i_yjbb == 3) {
                        msg = "请选择是否安装与派出所联网的一键报警";
                    }
                    if (!msg.isEmpty()) {
                        UI.toast(this, msg);
                    } else {
                        HouseCheckModle houseCheckModle =new HouseCheckModle();
                        houseCheckModle.setTYPE(HouseCheckModle.getTypeNum(tv_lb.getText().toString()));
                        houseCheckModle.setSFKTJZDJZH(i_ktjzz);
                        houseCheckModle.setLON(bJD);
                        houseCheckModle.setLAT(bWd);
                        houseCheckModle.setJD(et_jd.getText().toString());
                        houseCheckModle.setSQ(et_sq.getText().toString());
                        houseCheckModle.setDZX(et_dzx.getText().toString());
                        houseCheckModle.setMPH(et_mph.getText().toString());
                        //  houseCheckModle.setDZ(et_dz.getText().toString());
                        houseCheckModle.setLOCTYPE(map_type);

                        houseCheckModle.setSFYGLY(i_sfgl);
                        houseCheckModle.setBAZSQK(et_bazsqk.getText().toString() );
                        houseCheckModle.setSSGLQK(et_ssglqk.getText().toString());

                        houseCheckModle.setSFAZFDM(i_sffdm);
                        houseCheckModle.setSXSFBJS(i_sfbjs);
                        houseCheckModle.setPSGSFAZPCHTMHY(i_sfazpc);
                        houseCheckModle.setFWSFYXFQC(i_sfxfqc);
                        houseCheckModle.setSFYMHQ(i_sfmhq);
                        houseCheckModle.setSFYYJZMD(i_sfzmd);
                        houseCheckModle.setXFSZSFWH(i_sfxfsf);
                        houseCheckModle.setSFAZSPMJ (i_spmj);

                        houseCheckModle.setGGQYTTSL(et_ggqy.getText().toString());
                        houseCheckModle.setGQTTSL(et_gqtt.getText().toString());
                        houseCheckModle.setPTTTSL(et_pttt.getText().toString());
                        houseCheckModle.setSPSHSL(et_spshsl.getText().toString());
                        houseCheckModle.setZWWQSFAZTT(i_aztt);
                        houseCheckModle.setSFAZYPCSLWDYJBJ (i_yjbb);
                        houseCheckModle.setLDID(getIntent().getStringExtra("ldbh"));
                        initInentAty(houseCheckModle);
                    }
                } else if (HouseCheckModle.lb_xcqxx.equals(StringUtils.nullToString(tv_lb.getText().toString()))) {//小产权信息
                    if (i_ktjzz == 3) {
                        msg = "请选择是否开通居住证";
                    } else if (et_jd.getText().toString().isEmpty()||et_sq.getText().toString().isEmpty()
                            ||et_dzx.getText().toString().isEmpty()||et_mph.getText().toString().isEmpty()) {
                        msg = "请填写正确地址";
                    } else if (bJD< 0.0||bWd<0.0) {
                        msg = "地址或经纬度坐标不能为空";
                    } else if (i_wygl == 3) {
                        msg = "请选择是否有物业公司管理";
                    }else if (i_wygl == 1&&i_sffwz == 3) {
                        msg = "请选择是否配置楼栋服务站";
                    } else if (i_wygl == 1&&i_sfldz == 3) {
                        msg = "请选择是否配置楼栋长";
                    } else if (i_wygl == 1&&i_sfyzgl == 3) {
                        msg = "请选择是否业主管理";
                    } else if (i_sffdm == 3) {
                        msg = "请选择是否安装防盗门";
                    } else if (i_sfbjs == 3) {
                        msg = "请选择锁芯是否是B级锁";
                    } else if (i_sfazpc == 3) {
                        msg = "请选择排水管是否安装爬刺和涂抹黄油";
                    } else if (i_sfxfqc == 3) {
                        msg = "请选择房屋是否有消防器材";
                    } else if (i_sfmhq == 3) {
                        msg = "请选择是否有灭火器";
                    } else if (i_sfzmd == 3) {
                        msg = "请选择是否有应急照明灯";
                    } else if (i_sfxfsf == 3) {
                        msg = "请选择消防水阀是否完好";
                    } else if (i_spmj == 3) {
                        msg = "请选择是否安装视频门禁";
                    } else if (i_yjbb == 3) {
                        msg = "请选择是否安装与派出所联网的一键报警";
                    }
                    if (!msg.isEmpty()) {
                        UI.toast(this, msg);
                    } else {
                        HouseCheckModle houseCheckModle =new HouseCheckModle();
                        houseCheckModle.setTYPE(HouseCheckModle.getTypeNum(tv_lb.getText().toString()));
                        houseCheckModle.setSFKTJZDJZH(i_ktjzz);
                        houseCheckModle.setLON(bJD);
                        houseCheckModle.setLAT(bWd);
                        houseCheckModle.setJD(et_jd.getText().toString());
                        houseCheckModle.setSQ(et_sq.getText().toString());
                        houseCheckModle.setDZX(et_dzx.getText().toString());
                        houseCheckModle.setMPH(et_mph.getText().toString());
                        //  houseCheckModle.setDZ(et_dz.getText().toString());
                        houseCheckModle.setLOCTYPE(map_type);

                        houseCheckModle.setSFYWYGLGS(i_wygl);
                        houseCheckModle.setSFPBLDFWZ(i_sffwz);
                        houseCheckModle.setSFPBLDZ(i_sfldz);
                        houseCheckModle.setSFYZGL(i_sfyzgl);

                        houseCheckModle.setSFAZFDM(i_sffdm);
                        houseCheckModle.setSXSFBJS(i_sfbjs);
                        houseCheckModle.setPSGSFAZPCHTMHY(i_sfazpc);
                        houseCheckModle.setFWSFYXFQC(i_sfxfqc);
                        houseCheckModle.setSFYMHQ(i_sfmhq);
                        houseCheckModle.setSFYYJZMD(i_sfzmd);
                        houseCheckModle.setXFSZSFWH(i_sfxfsf);
                        houseCheckModle.setSFAZSPMJ(i_spmj);
                        houseCheckModle.setSFAZYPCSLWDYJBJ(i_yjbb);
                        houseCheckModle.setLDID(getIntent().getStringExtra("ldbh"));
                        initInentAty(houseCheckModle);
                    }
                } else if (HouseCheckModle.lb_qt.equals(StringUtils.nullToString(tv_lb.getText().toString()))) {//其他
                    if (i_ktjzz == 3) {
                        msg = "请选择是否开通居住证";
                    } else if (et_jd.getText().toString().isEmpty()||et_sq.getText().toString().isEmpty()
                            ||et_dzx.getText().toString().isEmpty()||et_mph.getText().toString().isEmpty()) {
                        msg = "请填写正确地址";
                    } else if (bJD< 0.0||bWd<0.0) {
                        msg = "地址或经纬度坐标不能为空";
                    }else if (null == path1){
                        msg = "人防图片不能为空";
                    }else if (null == path2){
                        msg = "物防图片不能为空";
                    }else if (null == path3){
                        msg = "消防图片不能为空";
                    }else if (null == path4){
                        msg = "技防图片不能为空";
                    }
                    if (!msg.isEmpty()) {
                        UI.toast(this, msg);
                    } else {
                        HouseCheckModle houseCheckModle =new HouseCheckModle();
                        houseCheckModle.setTYPE(HouseCheckModle.getTypeNum(tv_lb.getText().toString()));
                        houseCheckModle.setLOCTYPE(map_type);
                        houseCheckModle.setLON(bJD);
                        houseCheckModle.setLAT(bWd);
                        houseCheckModle.setJD(et_jd.getText().toString());
                        houseCheckModle.setSQ(et_sq.getText().toString());
                        houseCheckModle.setDZX(et_dzx.getText().toString());
                        houseCheckModle.setMPH(et_mph.getText().toString());
                        //   houseCheckModle.setDZ(et_dz.getText().toString());
                        houseCheckModle.setLOCTYPE(map_type);
                        houseCheckModle.setSFKTJZDJZH(i_ktjzz);
                        houseCheckModle.setRFIMG(path1);
                        houseCheckModle.setJFIMG(path2);
                        houseCheckModle.setXFIMG(path3);
                        houseCheckModle.setWFIMG(path4);
                        houseCheckModle.setLDID(getIntent().getStringExtra("ldbh"));
                        initInentAty(houseCheckModle);
                    }
                }
                break;
        }
    }

    private void initData() {
        HouseCheckModle checkInfo=houseCheckModles.get(0);
        bWd=checkInfo.getLAT();
        bJD=checkInfo.getLON();
        map_type=checkInfo.getLOCTYPE();
        //et_dz.setText(checkInfo.getDZ());  //获得地址信息
        tv_zb.setText("(" + bWd + "," +bJD+ ")"); //百度坐标，经纬度展现
        tv_lb.setText(HouseCheckModle.getType(checkInfo.getTYPE()));
        BaseDataUtils.initRBCheck(checkInfo.getSFKTJZDJZH(),rb_ktjzz_0,rb_ktjzz_1,i_ktjzz);
        et_jd.setText(checkInfo.getJD());
        et_sq.setText(checkInfo.getSQ());
        if (checkInfo.getTYPE().equals("01")){
            setRFView(true, false, false, false, false);
            setDZView( false, true);
            setJFView(false, false, true, false, true);
            setXFView(true, false);
            setWFView(true, false);
            et_hy.setText(checkInfo.getHY());
            et_wymc.setText(checkInfo.getWYMC());
            et_zzlds.setText(checkInfo.getZZLDS());
            et_baysl.setText(checkInfo.getBAYSL());
            et_xlbars.setText(checkInfo.getXLBARS());
            et_mgsl.setText(checkInfo.getMGSL());
            et_bapbqk.setText(checkInfo.getBAPBQK());

            BaseDataUtils.initRBCheck(checkInfo.getSFAZFDM(),rb_sffdm_0,rb_sffdm_1,i_sffdm);
            BaseDataUtils.initRBCheck(checkInfo.getSXSFBJS(),rb_sfbjs_0,rb_sfbjs_1,i_sfbjs);
            BaseDataUtils. initRBCheck(checkInfo.getPSGSFAZPCHTMHY(),rb_sfazpc_0,rb_sfazpc_1,i_sfazpc);
            BaseDataUtils.initRBCheck(checkInfo.getFWSFYXFQC(),rb_sfxfqc_0,rb_sfxfqc_1,i_sfxfqc);
            BaseDataUtils.initRBCheck(checkInfo.getSFYMHQ(),rb_sfmhq_0,rb_sfmhq_1,i_sfmhq);
            BaseDataUtils.initRBCheck(checkInfo.getSFYYJZMD(),rb_sfzmd_0,rb_sfzmd_1,i_sfzmd);
            BaseDataUtils.initRBCheck(checkInfo.getXFSZSFWH(),rb_sfxfsf_0,rb_sfxfsf_1,i_sfxfsf);
            et_ggqy.setText(checkInfo.getGGQYTTSL());
            et_gqtt.setText(checkInfo.getGQTTSL());
            et_pttt.setText(checkInfo.getPTTTSL());
            et_spshsl.setText(checkInfo.getSPSHSL());
            BaseDataUtils.initRBCheck(checkInfo.getZWWQSFAZTT(),rb_aztt_0,rb_aztt_1,i_aztt);
            BaseDataUtils.initRBCheck(checkInfo.getSFAZYPCSLWDYJBJ(),rb_yjbb_0,rb_yjbb_1,i_yjbb);

        }else  if (checkInfo.getTYPE().equals("02")){
            setRFView(false, true, false, false, false);
            setJFView(true, false, false, false, true);
            setXFView(true, false);
            setWFView(true, false);
            setDZView(true,false);
            et_dzx.setText(checkInfo.getDZX());
            et_mph.setText(checkInfo.getMPH());
            BaseDataUtils.initRBCheck(checkInfo.getSFYWYGLGS(),rb_wygl_0,rb_wygl_1,i_wygl);
            if (checkInfo.getSFYWYGLGS()==0){
                setRFView(true, true, false, false, false);
                et_wymc.setText(checkInfo.getWYMC());
                et_zzlds.setText(checkInfo.getZZLDS());
                et_baysl.setText(checkInfo.getBAYSL());
                et_xlbars.setText(checkInfo.getXLBARS());
                et_mgsl.setText(checkInfo.getMGSL());
                et_bapbqk.setText(checkInfo.getBAPBQK());
            }else if (checkInfo.getSFYWYGLGS()==1){
                setRFView(false, true, true, false, false);
                BaseDataUtils.initRBCheck(checkInfo.getSFPBLDFWZ(),rb_sffwz_0,rb_sffwz_1,i_sffwz);
                BaseDataUtils.initRBCheck(checkInfo.getSFPBLDZ(),rb_sfldz_0,rb_sfldz_1,i_sfldz);
                BaseDataUtils.initRBCheck(checkInfo.getSFYZGL(),rb_sfyzgl_0,rb_sfyzgl_1,i_sfyzgl);
            }

            BaseDataUtils.initRBCheck(checkInfo.getSFAZFDM(),rb_sffdm_0,rb_sffdm_1,i_sffdm);
            BaseDataUtils.initRBCheck(checkInfo.getSXSFBJS(),rb_sfbjs_0,rb_sfbjs_1,i_sfbjs);
            BaseDataUtils. initRBCheck(checkInfo.getPSGSFAZPCHTMHY(),rb_sfazpc_0,rb_sfazpc_1,i_sfazpc);
            BaseDataUtils.initRBCheck(checkInfo.getFWSFYXFQC(),rb_sfxfqc_0,rb_sfxfqc_1,i_sfxfqc);
            BaseDataUtils.initRBCheck(checkInfo.getSFYMHQ(),rb_sfmhq_0,rb_sfmhq_1,i_sfmhq);
            BaseDataUtils.initRBCheck(checkInfo.getSFYYJZMD(),rb_sfzmd_0,rb_sfzmd_1,i_sfzmd);
            BaseDataUtils.initRBCheck(checkInfo.getXFSZSFWH(),rb_sfxfsf_0,rb_sfxfsf_1,i_sfxfsf);
            BaseDataUtils.initRBCheck(checkInfo.getSFAZSPMJ(),rb_spmj_0,rb_spmj_1,i_spmj);
            if (checkInfo.getSFAZSPMJ()==0){
                setJFView(true, false, true, false, true);
                et_ggqy.setText(checkInfo.getGGQYTTSL());
                et_gqtt.setText(checkInfo.getGQTTSL());
                et_pttt.setText(checkInfo.getPTTTSL());
                et_spshsl.setText(checkInfo.getSPSHSL());
                BaseDataUtils.initRBCheck(checkInfo.getZWWQSFAZTT(),rb_aztt_0,rb_aztt_1,i_aztt);
            }else if (checkInfo.getSFAZSPMJ()==1){
                setJFView(true, true, false, false, true);
                BaseDataUtils.initRBCheck(checkInfo.getSFAZDJSPMJ(),rb_azdjspmj_0,rb_azdjspmj_1,i_azdjspmj);
                BaseDataUtils.initRBCheck(checkInfo.getMJSFSXSK(),rb_sfsxsk_0,rb_sfsxsk_1,i_sfsxsk);
                et_lxbcsj.setText(checkInfo.getLXZLBCSJ());
                et_mjshsl.setText(checkInfo.getMJSHSL());
            }
            BaseDataUtils.initRBCheck(checkInfo.getSFAZYPCSLWDYJBJ(),rb_yjbb_0,rb_yjbb_1,i_yjbb);
        }else  if (checkInfo.getTYPE().equals("03")){
            setRFView(false, false, false, true, false);
            setJFView(true, false, true, false, true);
            setXFView(true, false);
            setWFView(true, false);
            setDZView(true,false);
            et_dzx.setText(checkInfo.getDZX());
            et_mph.setText(checkInfo.getMPH());
            BaseDataUtils.initRBCheck(checkInfo.getSFYGLY(),rb_sfgl_0,rb_sfgl_1,i_sfgl);
            et_bazsqk.setText(checkInfo.getBAZSQK());
            et_ssglqk.setText(checkInfo.getSSGLQK());
            BaseDataUtils.initRBCheck(checkInfo.getSFAZFDM(),rb_sffdm_0,rb_sffdm_1,i_sffdm);
            BaseDataUtils.initRBCheck(checkInfo.getSXSFBJS(),rb_sfbjs_0,rb_sfbjs_1,i_sfbjs);
            BaseDataUtils. initRBCheck(checkInfo.getPSGSFAZPCHTMHY(),rb_sfazpc_0,rb_sfazpc_1,i_sfazpc);
            BaseDataUtils.initRBCheck(checkInfo.getFWSFYXFQC(),rb_sfxfqc_0,rb_sfxfqc_1,i_sfxfqc);
            BaseDataUtils.initRBCheck(checkInfo.getSFYMHQ(),rb_sfmhq_0,rb_sfmhq_1,i_sfmhq);
            BaseDataUtils.initRBCheck(checkInfo.getSFYYJZMD(),rb_sfzmd_0,rb_sfzmd_1,i_sfzmd);
            BaseDataUtils.initRBCheck(checkInfo.getXFSZSFWH(),rb_sfxfsf_0,rb_sfxfsf_1,i_sfxfsf);
            BaseDataUtils.initRBCheck(checkInfo.getSFAZSPMJ(),rb_spmj_0,rb_spmj_1,i_spmj);
            et_ggqy.setText(checkInfo.getGGQYTTSL());
            et_gqtt.setText(checkInfo.getGQTTSL());
            et_pttt.setText(checkInfo.getPTTTSL());
            et_spshsl.setText(checkInfo.getSPSHSL());
            BaseDataUtils.initRBCheck(checkInfo.getZWWQSFAZTT(),rb_aztt_0,rb_aztt_1,i_aztt);
            BaseDataUtils.initRBCheck(checkInfo.getSFAZYPCSLWDYJBJ(),rb_yjbb_0,rb_yjbb_1,i_yjbb);
        }else  if (checkInfo.getTYPE().equals("04")){
            setRFView(false, true, true, false, false);
            setJFView(true, false, false, false, true);
            setXFView(true, false);
            setWFView(true, false);
            setDZView(true,false);
            et_dzx.setText(checkInfo.getDZX());
            et_mph.setText(checkInfo.getMPH());
            BaseDataUtils.initRBCheck(checkInfo.getSFYWYGLGS(),rb_wygl_0,rb_wygl_1,i_wygl);
            BaseDataUtils.initRBCheck(checkInfo.getSFPBLDFWZ(),rb_sffwz_0,rb_sffwz_1,i_sffwz);
            BaseDataUtils.initRBCheck(checkInfo.getSFPBLDZ(),rb_sfldz_0,rb_sfldz_1,i_sfldz);
            BaseDataUtils.initRBCheck(checkInfo.getSFYZGL(),rb_sfyzgl_0,rb_sfyzgl_1,i_sfyzgl);
            BaseDataUtils.initRBCheck(checkInfo.getSFAZFDM(),rb_sffdm_0,rb_sffdm_1,i_sffdm);
            BaseDataUtils.initRBCheck(checkInfo.getSXSFBJS(),rb_sfbjs_0,rb_sfbjs_1,i_sfbjs);
            BaseDataUtils.initRBCheck(checkInfo.getPSGSFAZPCHTMHY(),rb_sfazpc_0,rb_sfazpc_1,i_sfazpc);
            BaseDataUtils.initRBCheck(checkInfo.getFWSFYXFQC(),rb_sfxfqc_0,rb_sfxfqc_1,i_sfxfqc);
            BaseDataUtils.initRBCheck(checkInfo.getSFYMHQ(),rb_sfmhq_0,rb_sfmhq_1,i_sfmhq);
            BaseDataUtils.initRBCheck(checkInfo.getSFYYJZMD(),rb_sfzmd_0,rb_sfzmd_1,i_sfzmd);
            BaseDataUtils.initRBCheck(checkInfo.getXFSZSFWH(),rb_sfxfsf_0,rb_sfxfsf_1,i_sfxfsf);
            BaseDataUtils.initRBCheck(checkInfo.getSFAZSPMJ(),rb_spmj_0,rb_spmj_1,i_spmj);
            BaseDataUtils.initRBCheck(checkInfo.getSFAZYPCSLWDYJBJ(),rb_yjbb_0,rb_yjbb_1,i_yjbb);

        }else  if (checkInfo.getTYPE().equals("05")){
            et_dzx.setText(checkInfo.getDZX());
            et_mph.setText(checkInfo.getMPH());
            setRFView(false, false, false, false, true);
            setJFView(false, false, false, true, false);
            setXFView(false, true);
            setWFView(false, true);
            setDZView(true,false);
            path1="j"+checkInfo.getRFIMG();
            path2="j"+checkInfo.getJFIMG();
            path3="j"+checkInfo.getXFIMG();
            path4="j"+checkInfo.getWFIMG();
            ImageLoader.getInstance().displayImage(ApiHttpClient.IMAGE_HOST + "/czw/" +checkInfo.getRFIMG(), iv_rf);
            ImageLoader.getInstance().displayImage(ApiHttpClient.IMAGE_HOST + "/czw/" +checkInfo.getJFIMG(), iv_jf);
            ImageLoader.getInstance().displayImage(ApiHttpClient.IMAGE_HOST + "/czw/" +checkInfo.getXFIMG(), iv_xf);
            ImageLoader.getInstance().displayImage(ApiHttpClient.IMAGE_HOST + "/czw/" +checkInfo.getWFIMG(), iv_wf);
        }
    }


    private void initInentAty(HouseCheckModle houseCheckModle) {
        Intent intent=new Intent(this,HousePoliceAty.class);
        if (flag){
            houseCheckModle.setID(houseCheckModles.get(0).getID());
            intent.setFlags(1);
        }
        intent.putExtra("houseCheckModle", houseCheckModle);
        startActivity(intent);
    }


    @Override
    public void onCheckedChanged(RadioGroup group, int checkedId) {
        switch (group.getId()) {
            case R.id.rg_ktjzz:
                i_ktjzz = new BaseDataUtils().getRGNum(group, R.id.rb_ktjzz_0);
                break;
            case R.id.rg_sffwz:
                i_sffwz = new BaseDataUtils().getRGNum(group, R.id.rb_sffwz_0);
                break;
            case R.id.rg_sfldz:
                i_sfldz = new BaseDataUtils().getRGNum(group, R.id.rb_sfldz_0);
                break;
            case R.id.rg_sfgl:
                i_sfgl = new BaseDataUtils().getRGNum(group, R.id.rb_sfgl_0);
                break;
            case R.id.rg_sfyzgl:
                i_sfyzgl = new BaseDataUtils().getRGNum(group, R.id.rb_sfyzgl_0);
                break;
            case R.id.rg_sffdm:
                i_sffdm = new BaseDataUtils().getRGNum(group, R.id.rb_sffdm_0);
                break;
            case R.id.rg_sfbjs:
                i_sfbjs = new BaseDataUtils().getRGNum(group, R.id.rb_sfbjs_0);
                break;
            case R.id.rg_sfazpc:
                i_sfazpc = new BaseDataUtils().getRGNum(group, R.id.rb_sfazpc_0);
                break;
            case R.id.rg_sfxfqc:
                i_sfxfqc = new BaseDataUtils().getRGNum(group, R.id.rb_sfxfqc_0);
                break;
            case R.id.rg_sfmhq :
                i_sfmhq = new BaseDataUtils().getRGNum(group, R.id.rb_sfmhq_0);
                break;
            case R.id.rg_sfzmd:
                i_sfzmd = new BaseDataUtils().getRGNum(group, R.id.rb_sfzmd_0);
                break;
            case R.id.rg_sfxfsf:
                i_sfxfsf = new BaseDataUtils().getRGNum(group, R.id.rb_sfxfsf_0);
                break;
            case R.id.rg_aztt :
                i_aztt = new BaseDataUtils().getRGNum(group, R.id.rb_aztt_0);
                break;
            case R.id.rg_yjbb:
                i_yjbb = new BaseDataUtils().getRGNum(group, R.id.rb_yjbb_0);
                break;
            case R.id.rg_azdjspmj:
                i_azdjspmj = new BaseDataUtils().getRGNum(group, R.id.rb_azdjspmj_0);
                break;
            case R.id.rg_sfsxsk:
                i_sfsxsk = new BaseDataUtils().getRGNum(group, R.id.rb_sfsxsk_0);
                break;

            case R.id.rg_wygl:
                if (HouseCheckModle.lb_czc.equals(StringUtils.nullToString(tv_lb.getText().toString())) && checkedId == R.id.rb_wygl_0) {
                    setRFView(true, true, false, false, false);
                    i_wygl=0;
                    // setJFView(true,true,false);
                } else if (HouseCheckModle.lb_czc.equals(StringUtils.nullToString(tv_lb.getText().toString()))) {
                    setRFView(false, true, true, false, false);
                    i_wygl=1;
                    // setJFView(true,true,false);
                } else if (checkedId == R.id.rb_wygl_0) {
                    i_wygl=0;
                } else if (checkedId == R.id.rb_wygl_1) {
                    i_wygl=1;
                }
                break;

            case R.id.rg_spmj:
                if (HouseCheckModle.lb_czc.equals(StringUtils.nullToString(tv_lb.getText().toString())) && checkedId == R.id.rb_spmj_0) {
                    // setRFView(true,true,false,false);
                    setJFView(true, false, true, false, true);
                    i_spmj=0;
                } else if (HouseCheckModle.lb_czc.equals(StringUtils.nullToString(tv_lb.getText().toString()))) {
                    //  setRFView(false,true,true,false);
                    setJFView(true, true, false, false, true);
                    i_spmj=1;
                } else if (checkedId == R.id.rb_spmj_0) {
                    //  setRFView(false,true,true,false);
                    //  setJFView(true,true,true,false,true);
                    i_spmj=0;
                } else if (checkedId == R.id.rb_spmj_1) {
                    i_spmj=1;
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
                &&resultCode!=0
                && imagePath != null) {
            /***获得拍照的路径 写入数据库**/
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
        }else  if (PickupMapActivity.MAP_PICK_UP == requestCode) { //地图选点
            if (Activity.RESULT_OK == resultCode) {
                LocaionInfo info = (LocaionInfo) (data.getBundleExtra("data").get("data"));
                bWd=info.getLat();
                bJD=info.getLongt();
                addRess=info.getAddress();
                map_type = info.getLoctype();
                //et_dz.setText(addRess);
                /**百度坐标，经纬度展现**/
                tv_zb.setText("(" + bWd + "," + bJD + ")");
            }
        }
    }

    private void initView() {
        getData();
        tv_lb.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (HouseCheckModle.lb_hyxq.equals(StringUtils.nullToString(tv_lb.getText().toString()))) {//花园小区
                    setRFView(true, false, false, false, false);
                    setJFView(false, false, true, false, true);
                    setXFView(true, false);
                    setWFView(true, false);
                    setDZView(false,true);
                } else if (HouseCheckModle.lb_czc.equals(StringUtils.nullToString(tv_lb.getText().toString()))) {//城中村
                    setRFView(false, true, false, false, false);
                    setJFView(true, false, false, false, true);
                    setXFView(true, false);
                    setWFView(true, false);
                    setDZView(true,false);
                } else if (HouseCheckModle.lb_gcss.equals(StringUtils.nullToString(tv_lb.getText().toString()))) {//工厂宿舍
                    setRFView(false, false, false, true, false);
                    setJFView(true, false, true, false, true);
                    setXFView(true, false);
                    setWFView(true, false);
                    setDZView(true,false);
                } else if (HouseCheckModle.lb_xcqxx.equals(StringUtils.nullToString(tv_lb.getText().toString()))) {//小产权信息
                    setRFView(false, true, true, false, false);
                    setJFView(true, false, false, false, true);
                    setXFView(true, false);
                    setWFView(true, false);
                    setDZView(true,false);
                } else if (HouseCheckModle.lb_qt.equals(StringUtils.nullToString(tv_lb.getText().toString()))) {//其他
                    setRFView(false, false, false, false, true);
                    setJFView(false, false, false, true, false);
                    setXFView(false, true);
                    setWFView(false, true);
                    setDZView(true,false);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        rg_sfbjs.setOnCheckedChangeListener(this);
        rg_sfazpc.setOnCheckedChangeListener(this);
        rg_sfxfqc.setOnCheckedChangeListener(this);
        rg_sfmhq.setOnCheckedChangeListener(this);
        rg_sfzmd.setOnCheckedChangeListener(this);
        rg_sfxfsf.setOnCheckedChangeListener(this);
        rg_aztt.setOnCheckedChangeListener(this);
        rg_yjbb.setOnCheckedChangeListener(this);
        rg_azdjspmj.setOnCheckedChangeListener(this);
        rg_sfsxsk.setOnCheckedChangeListener(this);
        rg_sffwz.setOnCheckedChangeListener(this);
        rg_sfldz.setOnCheckedChangeListener(this);
        rg_sfgl.setOnCheckedChangeListener(this);
        rg_sffdm.setOnCheckedChangeListener(this);
        rg_wygl.setOnCheckedChangeListener(this);
        rg_spmj.setOnCheckedChangeListener(this);
        rg_ktjzz.setOnCheckedChangeListener(this);
        rg_sfyzgl.setOnCheckedChangeListener(this);
    }

    private void getData() {
        showLoadding();
        JSONObject requestJson = new JSONObject();
        requestJson.put("LDID",getIntent().getStringExtra("ldbh"));
        tv_ldbm.setText(getIntent().getStringExtra("ldbh"));
        String jo= requestJson.toJSONString();
        ApiResource.getCZWList(jo, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int i, Header[] headers, byte[] bytes) {
                stopLoading();
                String result = new String(bytes);
                if (i==200&&result.length()>2){
                    houseCheckModles = JSON.parseArray(result, HouseCheckModle.class);
                    if (houseCheckModles!=null&&houseCheckModles.size()>0){
                        flag=true;
                        initData();
                    }
                }else {
                    setNoData();
                }
            }
            @Override
            public void onFailure(int i, Header[] headers, byte[] bytes, Throwable throwable) {
                stopLoading();
                setNoData();
                toast("获取数据失败");
            }
        });
    }

    private void setNoData() {
        tv_lb.setText(HouseCheckModle.lb_hyxq);
        setRFView(true, false, false, false, false);
        setJFView(false, false, true, false, true);
        setXFView(true, false);
        setWFView(true, false);
        setDZView(false,true);
    }


    //人防布局
    private void setRFView(boolean b, boolean b1, boolean b2, boolean b3, boolean b4) {
        ll_rf.setVisibility(b == true ? View.VISIBLE : View.GONE); //人防含花园小区采集的信息其他界面复用
        ll_rf_wy.setVisibility(b1 == true ? View.VISIBLE : View.GONE);//是否有物业管理
        ll_rf_fwz.setVisibility(b2 == true ? View.VISIBLE : View.GONE);//人防是否服务站、业主管理等信息
        ll_rf_ss.setVisibility(b3 == true ? View.VISIBLE : View.GONE);//人防宿舍
        ll_rf_qt.setVisibility(b4 == true ? View.VISIBLE : View.GONE);//人防其他界面
    }
    //人防布局
    private void setDZView(boolean b5,boolean b6) {
        ll_dz.setVisibility(b5 == true ? View.VISIBLE : View.GONE);//
        ll_dz_hy.setVisibility(b6 == true ? View.VISIBLE : View.GONE);//
    }

    //技防
    private void setJFView(boolean b4, boolean b5, boolean b6, boolean b7, boolean b8) {
        ll_jf_spmj.setVisibility(b4 == true ? View.VISIBLE : View.GONE);//是否有视频门禁
        ll_jf_mj_no.setVisibility(b5 == true ? View.VISIBLE : View.GONE);//没有视频门禁
        ll_jf.setVisibility(b6 == true ? View.VISIBLE : View.GONE);//技防含花园小区采集的信息其他界面复用
        ll_jf_qt.setVisibility(b7 == true ? View.VISIBLE : View.GONE);
        ll_jf_bj.setVisibility(b8 == true ? View.VISIBLE : View.GONE);
    }

    //消防界面view
    private void setXFView(boolean b1, boolean b2) {
        ll_xf.setVisibility(b1 == true ? View.VISIBLE : View.GONE);
        ll_xf_qt.setVisibility(b2 == true ? View.VISIBLE : View.GONE);
    }

    //物防界面view
    private void setWFView(boolean b1, boolean b2) {
        ll_wf.setVisibility(b1 == true ? View.VISIBLE : View.GONE);
        ll_wf_qt.setVisibility(b2 == true ? View.VISIBLE : View.GONE);
    }




    @Override
    public boolean enableBackUpBtn() {
        return true;
    }


    @Override
    public String getAtyTitle() {
        return getString(R.string.house);
    }

}
