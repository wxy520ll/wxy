package cn.net.xinyi.xmjt.activity.Collection.ZAJC;


import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import cn.net.xinyi.xmjt.R;
import cn.net.xinyi.xmjt.config.BaseActivity2;
import cn.net.xinyi.xmjt.model.Presenter.ZAJCPresenter;
import cn.net.xinyi.xmjt.model.View.IZAJCView;
import cn.net.xinyi.xmjt.model.ZAJCCYZModle;
import cn.net.xinyi.xmjt.model.ZAJCJKModle;
import cn.net.xinyi.xmjt.model.ZAJCModle;
import cn.net.xinyi.xmjt.model.ZAJCXFModle;
import cn.net.xinyi.xmjt.utils.AnnotateManager;
import cn.net.xinyi.xmjt.utils.BaseDataUtils;
import cn.net.xinyi.xmjt.utils.BaseUtil;
import cn.net.xinyi.xmjt.utils.BindView;
import cn.net.xinyi.xmjt.utils.DB.CollectDBUtils;
import cn.net.xinyi.xmjt.utils.DB.DBHelperNew;
import cn.net.xinyi.xmjt.utils.GeneralUtils;
import cn.net.xinyi.xmjt.utils.ImageUtils;
import cn.net.xinyi.xmjt.utils.StringUtils;


/**
 * Created by hao.zhou on 2016/3/19.
 */
public class ZAJCEditorAty extends BaseActivity2 implements IZAJCView,View.OnClickListener,RadioGroup.OnCheckedChangeListener {
    /**采集分类**/
    @BindView(id = R.id.tv_lb)
    private TextView tv_lb;
    /**采集分类**/
    @BindView(id = R.id.tv_type)
    private TextView tv_type;
    /**名称**/
    @BindView(id = R.id.et_mc)
    private EditText et_mc;
    /**地址**/
    @BindView(id = R.id.et_dz)
    private EditText et_dz;
    /**地址**/
    @BindView(id = R.id.et_mph)
    private EditText et_mph;
    /**坐标**/
    @BindView(id = R.id.tv_zb)
    private TextView tv_zb;
    /**楼栋编码**/
    @BindView(id = R.id.et_ldbm)
    private AutoCompleteTextView et_ldbm;
    /**门牌号照片**/
    @BindView(id = R.id.iv_mph,click = true)
    private ImageView iv_mph;
    /**店门全景图**/
    @BindView(id = R.id.iv_dm,click = true)
    private ImageView iv_dm;
    /**经营者姓名**/
    @BindView(id = R.id.et_jyzxm)
    private EditText et_jyzxm;
    /**经营者电话**/
    @BindView(id = R.id.et_jyzdh)
    private EditText et_jyzdh;
    /**经营者服务电话**/
    @BindView(id = R.id.et_jyzfwdh)
    private EditText et_jyzfwdh;
    /**营业执照**/
    @BindView(id = R.id.iv_yyzz,click = true)
    private ImageView iv_yyzz;
    /**特种行业许可证**/
    @BindView(id = R.id.iv_tzhy,click = true)
    private ImageView iv_tzhy;
    /**消防验收合格证**/
    @BindView(id = R.id.iv_xfys,click = true)
    private ImageView iv_xfys;
    /**环保批文**/
    @BindView(id = R.id.iv_hbpw,click = true)
    private ImageView iv_hbpw;
    /**其他**/
    @BindView(id = R.id.iv_qt,click = true)
    private ImageView iv_qt;
    /**治安负责人**/
    @BindView(id = R.id.et_zafzr)
    private EditText et_zafzr;
    /**治安负责人电话**/
    @BindView(id = R.id.et_zafzrdh)
    private EditText et_zafzrdh;
    /**所属派出所**/
    @BindView(id = R.id.tv_sspcs,click = true)
    private TextView tv_sspcs;
    /**所属警务室**/
    @BindView(id = R.id.tv_ssjws,click = true)
    private TextView tv_ssjws;
    /**listview从业者**/
    @BindView(id = R.id.lv_cyz)
    private ListView lv_cyz;
    /**增加从业者信息**/
    @BindView(id = R.id.tv_addcyz,click = true)
    private TextView tv_addcyz;
    /**监控信息采集**/
    @BindView(id = R.id.rg_sfcjxf)
    private RadioGroup rg_sfcjxf;
    /**消防信息采集**/
    @BindView(id = R.id.rg_sfcjjk)
    private RadioGroup rg_sfcjjk;
    /**保存**/
    @BindView(id = R.id.btn_save,click = true)
    private Button btn_save;
    /**删除**/
    @BindView(id = R.id.btn_del,click = true)
    private Button btn_del;
    /**备注**/
    @BindView(id = R.id.et_bz,click = true)
    private EditText et_bz;
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
    @BindView(id = R.id.tv_sxtzs)
    private TextView tv_sxtzs;
    @BindView(id = R.id.tv_sxtzcs)
    private TextView tv_sxtzcs;
    /**是否采集消防**/
    @BindView(id = R.id.ll_xf,click = true)
    private LinearLayout ll_xf;
    @BindView(id = R.id.tv_mhqsl)
    private TextView tv_mhqsl;
    @BindView(id = R.id.tv_mhqzcs)
    private TextView tv_mhqzcs;
    /**百度坐标纬度**/
    private double bWd;
    /**百度坐标经度**/
    private double bJD;
    /***ImageView拍照标识*/
    private int i_flag;
    /***拍照图片路径 存放数据库**/
    private String path1;
    private String path2;
    private String path3;
    private String path4;
    private String path5;
    private String path6;
    private String path7;
    private ZAJCPresenter pres;
    private List<ZAJCCYZModle> cyzInfos;
    private ZAJCXFModle xfInfo;
    private ZAJCJKModle jkInfo;
    private int i_sfcjjk;
    private int i_sfcjxf;
    private String pcs_num;
    private List<String> jwsOrgans=new ArrayList<String>();
    private ZAJCModle jcInfo;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.aty_zabase_edt);
        /***注解式绑定控件**/
        AnnotateManager.initBindView(this);
        jcInfo = (ZAJCModle) getIntent().getSerializableExtra(GeneralUtils.Info);
        initData();
    }

    private void initData() {
        /**presenter 初始化**/
        pres = new ZAJCPresenter(this);
        /**根据关联id查询出数据库从业者信息**/
        cyzInfos= CollectDBUtils.getCYZData(ZAJCEditorAty.this, jcInfo.getGLID());
        /**根据关联id查询出数据库监控信息**/
        jkInfo= CollectDBUtils.getJKData(ZAJCEditorAty.this, jcInfo.getGLID());
        /**根据关联id查询出数据库消防信息**/
        xfInfo= CollectDBUtils.getXFData(ZAJCEditorAty.this, jcInfo.getGLID());
        /**初始化 采集信息*/
        pres.setPreRetnData(jcInfo, tv_zb);
        //如果派出所编码为空，根据派出所名称寻找派出所编码 来匹配寻找下属警务室！
        pcs_num = zdUtils.getZdlbAndZdzToZdbm(GeneralUtils.ZZJG_PCS,tv_sspcs.getText().toString().trim());
        initCYZAdapt(cyzInfos);
        initClick();
    }

    private void initClick() {
        rg_sfcjxf.setOnCheckedChangeListener(this);
        rg_sfcjjk.setOnCheckedChangeListener(this);
        checkTextLength(et_mc,20);
        checkTextLength(et_dz,30);
        checkTextLength(et_mph,10);
        checkTextLength(et_ldbm,19);
        checkTextLength(et_jyzxm,12);
        checkTextLength(et_jyzdh,12);
        checkTextLength(et_jyzfwdh,12);
        checkTextLength(et_jyzfwdh,12);
        checkTextLength(et_zafzr,12);
        checkTextLength(et_zafzrdh,12);
        checkTextLength(et_bz,12);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.iv_mph :
                if (BaseDataUtils.isFastClick()){
                    break;
                }
                i_flag = 1;
                cameraPhoto();
                break;

            case R.id.iv_dm :
                if (BaseDataUtils.isFastClick()){
                    break;
                }
                i_flag = 2;
                cameraPhoto();
                break;

            case R.id.iv_yyzz :
                if (BaseDataUtils.isFastClick()){
                    break;
                }
                i_flag = 3;
                cameraPhoto();
                break;
            case R.id.iv_qt :
                if (BaseDataUtils.isFastClick()){
                    break;
                }
                i_flag = 4;
                cameraPhoto();
                break;
            case R.id.iv_hbpw :
                if (BaseDataUtils.isFastClick()){
                    break;
                }
                i_flag = 5;
                cameraPhoto();
                break;
            case R.id.iv_xfys :
                if (BaseDataUtils.isFastClick()){
                    break;
                }
                i_flag = 6;
                cameraPhoto();
                break;
            case R.id.iv_tzhy :
                if (BaseDataUtils.isFastClick()){
                    break;
                }
                i_flag = 7;
                cameraPhoto();
                break;

            case R.id.tv_addcyz :
                if (BaseDataUtils.isFastClick()){
                    break;
                }
                Intent intent=new Intent(this,ZAJCCYZAty.class);
                startActivityForResult(intent,0);
                break;

            case R.id.ll_jk :
                if (BaseDataUtils.isFastClick()){
                    break;
                }
                Intent intent1=new Intent(this,ZAJCJKWatchAty.class);
                intent1.putExtra("jkInfo", jkInfo);
                startActivity(intent1);
                break;

            case R.id.ll_xf:
                if (BaseDataUtils.isFastClick()){
                    break;
                }
                Intent intent2=new Intent(this,ZAJCXFWatchAty.class);
                intent2.putExtra("xfInfo", xfInfo);
                startActivity(intent2);
                break;

            //选择派出所
            //选择派出所
            case R.id.tv_sspcs:
                if (BaseDataUtils.isFastClick()){
                    break;
                }
                //需要清空警务室信息，以免数据重复
                jwsOrgans.clear();
                tv_ssjws.setText("");
                tv_ssjws.setHint("请选择警务室");
                final Map<String,String> pcsMaps=zdUtils.getZdlbToZdz(GeneralUtils.ZZJG_PCS);
                new AlertDialog.Builder(this).setItems(pcsMaps.values().toArray(new String[pcsMaps.values().size()]),
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                tv_sspcs.setText(pcsMaps.values().toArray(new String[pcsMaps.values().size()])[which]);
                                pcs_num=zdUtils.getMapKey(pcsMaps,tv_sspcs.getText().toString());
                            }
                        }).create().show();
                break;

            //警务室的选择
            case R.id.tv_ssjws:
                if (BaseDataUtils.isFastClick()){
                    break;
                }
                jwsOrgans.clear();
                tv_ssjws.setText("");
                tv_ssjws.setHint("请选择警务室");
                if (tv_sspcs.getText().toString().trim().isEmpty()) {
                    BaseUtil.showDialog("派出所不能为空",this);
                } else if (pcs_num!=null&&!tv_sspcs.getText().toString().trim().isEmpty()) {
                    final Map<String,String> jwsMaps=zdUtils.getZdlbAndFzdbmToZdz(GeneralUtils.ZZJG_JWS,pcs_num);
                    jwsMaps.put("1","其他");
                    new AlertDialog.Builder(this).setItems(jwsMaps.values().toArray(new String[jwsMaps.values().size()]),
                            new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    tv_ssjws.setText(jwsMaps.values().toArray(new String[jwsMaps.values().size()])[which]);
                                }
                            }).create().show();
                }
                break;

            case R.id.btn_save :
                if (BaseDataUtils.isFastClick()){
                    break;
                }
                String msg = getMsg();
                if(!msg.isEmpty()) {
                    BaseUtil.showDialog(msg, this);
                }else {
                    boolean flag = pres.updateDB(this, getLB(),getCJFL(), getMC(),  getDZ(),getMPH(), getLDBM(), getIV_MPHQJT(), getIV_DMQJT(),
                            getJYZXM(), getJYZDH(), getFWDH(), getIV_YYZZ(), getZAFZR(), getSSPCS(),
                            getSSJWS(), getJD(), getWD(), getZAFZRDH(), getSFCJJK(), getSFCJXF(),getBZ(),
                            cyzInfos, xfInfo, jkInfo ,getIV_QT(),getIV_HBPW(),getIV_XFYS(),getIV_TZHY(),jcInfo);
                    if (flag == true) {
                        setDialog(this,"修改成功");
                    } else {
                        toast("保存失败");
                    }
                }
                break;
            case R.id.btn_del:
                if (BaseDataUtils.isFastClick()){
                    break;
                }
                new AlertDialog.Builder(this)
                        .setTitle(R.string.msg_delete_confirm_title)
                        .setMessage(R.string.msg_delete_confirm)
                        .setPositiveButton(R.string.sure,
                                new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog,
                                                        int which) {
                                        try {
                                            /**数据库执行删除操作**/
                                            DBHelperNew.getInstance(ZAJCEditorAty.this).getZAJCDao().deleteById(jcInfo.getZAJCID());
                                            /**弹出框删除成功**/
                                            setDialog(ZAJCEditorAty.this,getString(R.string.del_yes));
                                        } catch (Exception e) {
                                            toast("删除失败，请稍后再试！");
                                            e.printStackTrace();
                                        }
                                    }
                                })
                        .setNegativeButton(R.string.cancle,
                                new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog,
                                                        int which) {
                                    }
                                }).show();
                break;
        }
    }

    private String getMsg() {
        String msg = "";
        /***判断 采集分类是否为空**/
        if (TextUtils.isEmpty(getCJFL())){
            msg=getResources().getString(R.string.cjfl)+getResources().getString(R.string.info_null);
            /***判断 采集分类是否为空  **/
        }else if(TextUtils.isEmpty(getMC())){
            msg=getResources().getString(R.string.mc)+getResources().getString(R.string.info_null);
            /***判断 地址是否为空  **/
        }else if(TextUtils.isEmpty(getDZ())){
            msg=getResources().getString(R.string.dz)+getResources().getString(R.string.info_null);
            /*** 判断 经营者姓名是否为空 **/
        }else if(TextUtils.isEmpty(getJYZXM())){
            msg=getResources().getString(R.string.za_jyzxm)+getResources().getString(R.string.info_null);
            /*** 判断 经营者电话是否为空 **/
        }else if(TextUtils.isEmpty(getJYZDH())){
            msg=getResources().getString(R.string.za_jyzdh)+getResources().getString(R.string.info_null);
            /*** 判断 经营者电话是否符合规范**/
        }else if(StringUtils.isMobileNO(getJYZDH())==false&&getJYZDH().length()!=12){
            msg="经营者电话请输入11位手机号码或12位座机号码";
            /***判断是否采集从业人员  **/
        }else if (cyzInfos.size()==0){
            msg = "请采集从业者人员信息" ;
        }
        return msg;
    }

    @Override
    public void onCheckedChanged(RadioGroup group, int checkedId) {
        switch (group.getId()){
            /**是否采集监控*/
            case R.id.rg_sfcjjk :
                //获取变更后的选中项的ID
                int radioButtonId = group.getCheckedRadioButtonId();
                RadioButton rb = (RadioButton) this.findViewById(radioButtonId);
                /**显示有监控需要填写监控资料**/
                if (jkInfo==null && radioButtonId == R.id.rb_sfcjjk_0){
                    Intent intent=new Intent(this,ZAJCJKAty.class);
                    startActivityForResult(intent,0);
                    rb_sfcjjk_1.setChecked(true);
                }else if (radioButtonId == R.id.rb_sfcjjk_1) {
                    i_sfcjjk=1;
                    ll_jk.setVisibility(View.GONE);
                    jkInfo=null;
                }
                break;
            /**是否采集消防*/
            case R.id.rg_sfcjxf :
                //获取变更后的选中项的ID
                int radioButtonId2 = group.getCheckedRadioButtonId();
                /**显示有监控需要填写监控资料**/
                if (xfInfo==null && radioButtonId2 == R.id.rb_sfcjxf_0){
                    Intent intent=new Intent(this,ZAJCXFInfoAty.class);
                    startActivityForResult(intent, 0);
                    rb_sfcjxf_1.setChecked(true);
                }else if (radioButtonId2 == R.id.rb_sfcjxf_1){
                    i_sfcjxf=1;
                    ll_xf.setVisibility(View.GONE);
                    xfInfo=null;
                }
                break;
        }
    }


    @Override
    public int getZAJCID() {
        return 0;
    }

    @Override
    public String getLB() {
        return tv_lb.getText().toString();
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
        return null;
    }

    @Override
    public void setLB(String LB) {
        tv_lb.setText(LB);
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
            iv_mph.setEnabled(false);
            path1 =IV_MPHQJT;
            iv_mph.setImageBitmap(BitmapFactory.decodeFile(path1));
        }
    }

    @Override
    public void setIV_DMQJT(String IV_DMQJT) {
        if (IV_DMQJT != null){
            iv_dm.setEnabled(false);
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
            iv_yyzz.setEnabled(false);
            path3 =IV_YYZZ;
            iv_yyzz.setImageBitmap(BitmapFactory.decodeFile(path3));
        }
    }

    @Override
    public void setIV_HBPW(String IV_HBPW) {
        if (IV_HBPW != null){
            iv_hbpw.setEnabled(false);
            path5 =IV_HBPW;
            iv_hbpw.setImageBitmap(BitmapFactory.decodeFile(path5));
        }
    }

    @Override
    public void setIV_TZHY(String IV_TZHY) {
        if (IV_TZHY != null){
            iv_tzhy.setEnabled(false);
            path7 =IV_TZHY;
            iv_tzhy.setImageBitmap(BitmapFactory.decodeFile(path7));
        }
    }

    @Override
    public void setIV_QT(String IV_QT) {
        if (IV_QT != null){
            iv_qt.setEnabled(false);
            path4 =IV_QT;
            iv_qt.setImageBitmap(BitmapFactory.decodeFile(path4));
        }
    }

    @Override
    public void setIV_XFYS(String IV_XFYS) {
        if (IV_XFYS != null){
            iv_xfys.setEnabled(false);
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
            i_sfcjjk=0;
            rb_sfcjjk_0.setEnabled(false);
            rb_sfcjjk_1.setEnabled(false);
            rb_sfcjjk_0.setChecked(true);
            if (jkInfo.getSXTZS()!=null)
                tv_sxtzs.setText(jkInfo.getSXTZS() );
            if (jkInfo.getZCS()!=null)
                tv_sxtzcs.setText(jkInfo.getZCS());
            ll_jk.setVisibility(View.VISIBLE);
        }else {
            i_sfcjjk=1;
            rb_sfcjjk_1.setChecked(true);
        }
    }

    @Override
    public void setSFCJXF(int SFCJXF) {
        if (xfInfo !=null && 0==SFCJXF){
            i_sfcjxf=0;
            rb_sfcjxf_0.setEnabled(false);
            rb_sfcjxf_1.setEnabled(false);
            rb_sfcjxf_0.setChecked(true);
            if (xfInfo.getMHQSL()!=null)
                tv_mhqsl.setText(xfInfo.getMHQSL());
            if (xfInfo.getZCS()!=null)
                tv_mhqzcs.setText(xfInfo.getZCS());
            ll_xf.setVisibility(View.VISIBLE);
        }else {
            i_sfcjxf=1;
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
                iv_mph.setImageBitmap(ImageUtils.compressImageByPixel(imagePath));
            }else  if (i_flag == 2){
                path2 = imagePath;
                iv_dm.setImageBitmap(ImageUtils.compressImageByPixel(imagePath));
            }else  if (i_flag == 3){
                path3 = imagePath;
                iv_yyzz.setImageBitmap(ImageUtils.compressImageByPixel(imagePath));
            }else  if (i_flag == 4){
                path4 = imagePath;
                iv_qt.setImageBitmap(ImageUtils.compressImageByPixel(imagePath));
            }else  if (i_flag == 5){
                path5 = imagePath;
                iv_hbpw.setImageBitmap(ImageUtils.compressImageByPixel(imagePath));
            }else  if (i_flag == 6){
                path6 = imagePath;
                iv_xfys.setImageBitmap(ImageUtils.compressImageByPixel(imagePath));
            }else  if (i_flag == 7){
                path7 = imagePath;
                iv_tzhy.setImageBitmap(ImageUtils.compressImageByPixel(imagePath));
            }
        }else if (resultCode ==1){
            ZAJCCYZModle cyyModle= (ZAJCCYZModle) data.getSerializableExtra("cyzInfo");
            if (cyzInfos.size()==0){
                cyzInfos.add(cyyModle);
            }else {
                for (ZAJCCYZModle zajc: cyzInfos){
                    if (zajc.getCYZSFZ().equals(cyyModle.getCYZSFZ())){
                        toast("该从业人员身份证号码已经采集！");
                        return;
                    }
                }
                cyzInfos.add(cyyModle);
            }
            /**初始化从业者adapter**/
            initCYZAdapt(cyzInfos);
        }else if (resultCode ==2){
            i_sfcjxf=0;
            xfInfo=(ZAJCXFModle) data.getSerializableExtra("xfInfo");
            tv_mhqsl.setText(((ZAJCXFModle) data.getSerializableExtra("xfInfo")).getMHQSL());
            tv_mhqzcs.setText(((ZAJCXFModle) data.getSerializableExtra("xfInfo")).getZCS());
            ll_xf.setVisibility(View.VISIBLE);
            rb_sfcjxf_0.setChecked(true);
        }else if (resultCode ==3){
            jkInfo=(ZAJCJKModle) data.getSerializableExtra("jkInfo");
            tv_sxtzs.setText(((ZAJCJKModle) data.getSerializableExtra("jkInfo")).getSXTZS() );
            tv_sxtzcs.setText(((ZAJCJKModle) data.getSerializableExtra("jkInfo")).getZCS());
            ll_jk.setVisibility(View.VISIBLE);
            rb_sfcjjk_0.setChecked(true);
            i_sfcjjk=0;
        }
    }

    /**初始化从业者adapter**/
    private void initCYZAdapt(final List<ZAJCCYZModle> cyyModles) {
        if (cyyModles.size()!=0){
            lv_cyz.setVisibility(View.VISIBLE);
            boolean bflag=false;
            ZAJCCYZAdp mAdapter=new ZAJCCYZAdp(bflag,lv_cyz,cyyModles,R.layout.aty_zacyz_item,ZAJCEditorAty.this);
            lv_cyz.setAdapter(mAdapter);
            lv_cyz.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    Intent intent = new Intent(ZAJCEditorAty.this, ZAJCCYZWatchAty.class);
                    intent.putExtra(GeneralUtils.Info, cyyModles.get(position));
                    startActivity(intent);
                }
            });
            mAdapter.setonClickListener(new ZAJCCYZAdp.onClickListener() {
                @Override
                public void onDel(final int index) {
                    new AlertDialog.Builder(ZAJCEditorAty.this)
                            .setTitle(R.string.msg_delete_confirm_title)
                            .setMessage(R.string.msg_delete_confirm)
                            .setPositiveButton(R.string.sure, new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    List delList = new ArrayList();     /**需要删除集合*/
                                    for (Iterator it = cyyModles.iterator(); it.hasNext();) {
                                        ZAJCCYZModle cyzInfo = (ZAJCCYZModle) it.next();
                                        if (cyzInfo.getCYZSFZ().equals(cyyModles.get(index).getCYZSFZ())) {
                                            if (null ==cyzInfo.getGLID()){
                                                delList.add(cyzInfo);
                                                cyyModles.removeAll(delList);
                                                /**初始化从业者adapter**/
                                                initCYZAdapt(cyyModles);
                                                return;
                                            }else {
                                                boolean flag= CollectDBUtils.delCYZData(ZAJCEditorAty.this,cyzInfo.getCYZID());
                                                if (flag){
                                                    /**根据关联id查询出数据库从业者信息**/
                                                    cyzInfos= CollectDBUtils.getCYZData(ZAJCEditorAty.this, jcInfo.getGLID());
                                                    /**初始化从业者adapter**/
                                                    initCYZAdapt(cyzInfos);
                                                    toast("删除成功");
                                                    return;
                                                }else {
                                                    toast("删除失败");
                                                }
                                            }
                                        }
                                    }

                                }
                            })
                            .setNegativeButton(R.string.cancle, new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {

                                }
                            }).show();
                }
            });

        }else {
            lv_cyz.setVisibility(View.GONE);
        }
    }

    @Override
    public String getAtyTitle() {
        return getString(R.string.zamanage_ed);
    }


    @Override
    public boolean enableBackUpBtn() {
        return true;
    }
}