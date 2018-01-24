package cn.net.xinyi.xmjt.activity.Collection.InfoCheck;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.util.TypeUtils;
import com.loopj.android.http.AsyncHttpResponseHandler;

import org.apache.http.Header;

import java.util.List;

import cn.net.xinyi.xmjt.R;
import cn.net.xinyi.xmjt.api.ApiResource;
import cn.net.xinyi.xmjt.config.AppContext;
import cn.net.xinyi.xmjt.config.BaseActivity2;
import cn.net.xinyi.xmjt.model.HttpModle;
import cn.net.xinyi.xmjt.model.LogisticsDeliveryCheckModle;
import cn.net.xinyi.xmjt.model.ZAJCModle;
import cn.net.xinyi.xmjt.utils.AnnotateManager;
import cn.net.xinyi.xmjt.utils.BaseDataUtils;
import cn.net.xinyi.xmjt.utils.BindView;
import cn.net.xinyi.xmjt.utils.UI;

//寄递物流核查
public class LogisticsDeliveryCheckAty extends BaseActivity2 implements RadioGroup.OnCheckedChangeListener,View.OnClickListener {

    private ZAJCModle zajcModle;
    //名称
    @BindView(id =R.id.tv_csmc )
    private TextView tv_csmc;
    //所属单位
    @BindView(id = R.id.tv_ssdw)
    private TextView tv_ssdw;
    //法人
    @BindView(id = R.id.tv_fr)
    private TextView tv_fr ;
    //所属地址
    @BindView(id = R.id.tv_ssdz)
    private TextView tv_ssdz;
    //核查时间
    @BindView(id = R.id.tv_hcsj)
    private TextView tv_hcsj ;

    //是否开箱验视
    @BindView(id = R.id.rg_kxys)
    private RadioGroup rg_kxys ;
    @BindView(id = R.id.rb_kxys_0)
    private RadioButton rb_kxys_0 ;
    @BindView(id = R.id.rb_kxys_1)
    private RadioButton rb_kxys_1 ;
    int i_kxys=2;

    //是否实名收件
    @BindView(id = R.id.rg_smsj)
    private RadioGroup rg_smsj ;
    @BindView(id = R.id.rb_smsj_0)
    private RadioButton rb_smsj_0 ;
    @BindView(id = R.id.rb_smsj_1)
    private RadioButton rb_smsj_1 ;
    int i_smsj=2;

    //从业人员是否信息采集
    @BindView(id = R.id.rg_ryxxcj)
    private RadioGroup rg_ryxxcj ;
    @BindView(id = R.id.rb_ryxxcj_0)
    private RadioButton rb_ryxxcj_0 ;
    @BindView(id = R.id.rb_ryxxcj_1)
    private RadioButton rb_ryxxcj_1 ;
    int i_ryxxcj=2;

    //从业人员是否持证上岗
    @BindView(id = R.id.rg_czsg)
    private RadioGroup rg_czsg ;
    @BindView(id = R.id.rb_czsg_0)
    private RadioButton rb_czsg_0 ;
    @BindView(id = R.id.rb_czsg_1)
    private RadioButton rb_czsg_1 ;
    int i_czsg=2;

    //是否组织安全培训
    @BindView(id = R.id.rg_anpx)
    private RadioGroup rg_anpx ;
    @BindView(id = R.id.rb_anpx_0)
    private RadioButton rb_anpx_0 ;
    @BindView(id = R.id.rb_anpx_1)
    private RadioButton rb_anpx_1 ;
    int i_anpx=2;

    //是否落实内部安保制度
    @BindView(id = R.id.rg_aazd)
    private RadioGroup rg_aazd ;
    @BindView(id = R.id.rb_aazd_0)
    private RadioButton rb_aazd_0 ;
    @BindView(id = R.id.rb_aazd_1)
    private RadioButton rb_aazd_1 ;
    int i_aazd=2;

    //是否张贴有奖宣传资料
    @BindView(id = R.id.rg_xczl)
    private RadioGroup rg_xczl ;
    @BindView(id = R.id.rb_xczl_0)
    private RadioButton rb_xczl_0 ;
    @BindView(id = R.id.rb_xczl_1)
    private RadioButton rb_xczl_1 ;
    int i_xczl=2;

    //监控保存是否30天
    @BindView(id = R.id.rg_jkbc)
    private RadioGroup rg_jkbc ;
    @BindView(id = R.id.rb_jkbc_0)
    private RadioButton rb_jkbc_0 ;
    @BindView(id = R.id.rb_jkbc_1)
    private RadioButton rb_jkbc_1 ;
    int i_jkbc=2;

    //是否配备X光检测设备
    @BindView(id = R.id.rg_xgjc)
    private RadioGroup rg_xgjc ;
    @BindView(id = R.id.rb_xgjc_0)
    private RadioButton rb_xgjc_0 ;
    @BindView(id = R.id.rb_xgjc_1)
    private RadioButton rb_xgjc_1 ;
    int i_xgjc=2;

    //快递公司是否安装快捷卫士app
    @BindView(id = R.id.rg_gsazapp)
    private RadioGroup rg_gsazapp ;
    @BindView(id = R.id.rb_gsazapp_0)
    private RadioButton rb_gsazapp_0 ;
    @BindView(id = R.id.rb_gsazapp_1)
    private RadioButton rb_gsazapp_1 ;
    int i_gsazapp=2;

    //快递人员是否安装快捷卫士app
    @BindView(id = R.id.rg_ryazapp)
    private RadioGroup rg_ryazapp ;
    @BindView(id = R.id.rb_ryazapp_0)
    private RadioButton rb_ryazapp_0 ;
    @BindView(id = R.id.rb_ryazapp_1)
    private RadioButton rb_ryazapp_1 ;
    int i_ryazapp=2;

    //是否按要求使用app收件
    @BindView(id = R.id.rg_appsj)
    private RadioGroup rg_appsj ;
    @BindView(id = R.id.rb_appsj_0)
    private RadioButton rb_appsj_0 ;
    @BindView(id = R.id.rb_appsj_1)
    private RadioButton rb_appsj_1 ;
    int i_appsj=2;

    @BindView(id = R.id.btn_upl,click = true)
    private Button btn_upl;
    private LogisticsDeliveryCheckModle checkInfo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.aty_logistics_delivery_check);
        TypeUtils.compatibleWithJavaBean = true;
        AnnotateManager.initBindView(this);  //控件绑定

        //intent传递的参数
        zajcModle=(ZAJCModle)getIntent().getSerializableExtra("ZAJCModle");
        //场所名称
        tv_csmc.setText("场所名称:"+(zajcModle.getMC()==null ?"":zajcModle.getMC()));
        //所属单位
        tv_ssdw.setText("所属单位:"+(zajcModle.getSSPCS()==null ?"":zajcModle.getSSPCS()));
        //经营者地址
        tv_ssdz.setText("所属地址:"+(zajcModle.getDZ()==null ?"":zajcModle.getDZ()));
        //最后一次核查时间
        tv_hcsj.setText("最后一次核查时间:"+(zajcModle.getHCSJ()==null ?"":zajcModle.getHCSJ()));
        //经营者姓名(法人)
        String jxzxm=zajcModle.getJYZXM()==null ? "":zajcModle.getJYZXM();
        String jxzdh=zajcModle.getJYZDH()==null ? "":zajcModle.getJYZDH();
        tv_fr.setText("经营者姓名:"+(jxzxm+"("+jxzdh+")" ));
        if (getIntent().getFlags()==1){
            setClickEnable();
            getCheckDate();
        }else {
            initCheckListen();
        }
    }

    private void getCheckDate() {
        btn_upl.setVisibility(View.GONE);
        checkInfo=(LogisticsDeliveryCheckModle)getIntent().getSerializableExtra("checkInfo");
        showLoadding();
        JSONObject jo=new JSONObject();
        jo.put("ID",checkInfo.getID());
        String json=jo.toJSONString();
        ApiResource.getJSWLCheckList(json, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int i, Header[] headers, byte[] bytes) {
                stopLoading();
                String result = new String(bytes);
                HttpModle httpsInfo =  JSON.parseObject(result,HttpModle.class);
                if (httpsInfo.getStatus().equals("0")&&null!=httpsInfo.getResult()){
                    List<LogisticsDeliveryCheckModle> infos =  JSON.parseArray(httpsInfo.getResult(),LogisticsDeliveryCheckModle.class);
                        setDate(infos.get(0));
                }else {
                    onFailure(i,headers,bytes,null);
                }
            }
            @Override
            public void onFailure(int i, Header[] headers, byte[] bytes, Throwable throwable) {
                stopLoading();
                toast(new String(bytes));
            }
        });
    }

    private void setDate(LogisticsDeliveryCheckModle info) {
        rb_kxys_0.setChecked((info.getSFKXYS() == 0) ? true : false);
        rb_kxys_1.setChecked((info.getSFKXYS() == 1) ? true : false);

        rb_smsj_0.setChecked((info.getSFSMDJ() == 0) ? true : false);
        rb_smsj_1.setChecked((info.getSFSMDJ() == 1) ? true : false);

        rb_ryxxcj_0.setChecked((info.getSFCJRYXX() == 0) ? true : false);
        rb_ryxxcj_1.setChecked((info.getSFCJRYXX() == 1) ? true : false);

        rb_czsg_0.setChecked((info.getSFCZSG() == 0) ? true : false);
        rb_czsg_1.setChecked((info.getSFCZSG() == 1) ? true : false);

        rb_anpx_0.setChecked((info.getSFKZPXHD() == 0) ? true : false);
        rb_anpx_1.setChecked((info.getSFKZPXHD() == 1) ? true : false);

        rb_aazd_0.setChecked((info.getSFLSABZD() == 0) ? true : false);
        rb_aazd_1.setChecked((info.getSFLSABZD() == 1) ? true : false);

        rb_xczl_0.setChecked((info.getSFZTXCZL() == 0) ? true : false);
        rb_xczl_1.setChecked((info.getSFZTXCZL() == 1) ? true : false);

        rb_jkbc_0.setChecked((info.getSFJKBCSST() == 0) ? true : false);
        rb_jkbc_1.setChecked((info.getSFJKBCSST() == 1) ? true : false);

        rb_xgjc_0.setChecked((info.getSFYXGJJC() == 0) ? true : false);
        rb_xgjc_1.setChecked((info.getSFYXGJJC() == 1) ? true : false);

        rb_gsazapp_0.setChecked((info.getSFGSAZAPP() == 0) ? true : false);
        rb_gsazapp_1.setChecked((info.getSFGSAZAPP() == 1) ? true : false);

        rb_ryazapp_0.setChecked((info.getSFRYAZAPP() == 0) ? true : false);
        rb_ryazapp_1.setChecked((info.getSFRYAZAPP() == 1) ? true : false);

        rb_appsj_0.setChecked((info.getSFZQSYAPP() == 0) ? true : false);
        rb_appsj_1.setChecked((info.getSFZQSYAPP() == 1) ? true : false);

    }

    private void setClickEnable() {
        rg_kxys.setEnabled(false);
        rg_smsj.setEnabled(false);
        rg_ryxxcj.setEnabled(false);
        rg_czsg.setEnabled(false);
        rg_anpx.setEnabled(false);
        rg_aazd.setEnabled(false);
        rg_xczl.setEnabled(false);
        rg_jkbc.setEnabled(false);
        rg_xgjc.setEnabled(false);
        rg_gsazapp.setEnabled(false);
        rg_ryazapp.setEnabled(false);
        rg_appsj.setEnabled(false);
    }

    private void initCheckListen() {
        //rg_kxys rg_smsj rg_ryxxcj rg_czsg  rg_anpx rg_aazd rg_xczl
        // rg_jkbc rg_xgjc rg_gsazapp rg_ryazapp rg_appsj
        rg_kxys.setOnCheckedChangeListener(this);
        rg_smsj.setOnCheckedChangeListener(this);
        rg_ryxxcj.setOnCheckedChangeListener(this);
        rg_czsg.setOnCheckedChangeListener(this);
        rg_anpx.setOnCheckedChangeListener(this);
        rg_aazd.setOnCheckedChangeListener(this);
        rg_xczl.setOnCheckedChangeListener(this);
        rg_jkbc.setOnCheckedChangeListener(this);
        rg_xgjc.setOnCheckedChangeListener(this);
        rg_gsazapp.setOnCheckedChangeListener(this);
        rg_ryazapp.setOnCheckedChangeListener(this);
        rg_appsj.setOnCheckedChangeListener(this);
    }

    @Override
    public void onCheckedChanged(RadioGroup group, int checkedId) {
        switch (group.getId()){
            case R.id.rg_kxys:
                i_kxys = new BaseDataUtils().getRGNum(group, R.id.rb_kxys_0);
                break;

            case R.id.rg_smsj:
                i_smsj = new BaseDataUtils().getRGNum(group, R.id.rb_smsj_0);
                break;

            case R.id.rg_ryxxcj:
                i_ryxxcj = new BaseDataUtils().getRGNum(group, R.id.rb_ryxxcj_0);
                break;

            case R.id.rg_czsg:
                i_czsg = new BaseDataUtils().getRGNum(group, R.id.rb_czsg_0);
                break;

            case R.id.rg_anpx:
                i_anpx = new BaseDataUtils().getRGNum(group, R.id.rb_anpx_0);
                break;

            case R.id.rg_aazd:
                i_aazd = new BaseDataUtils().getRGNum(group, R.id.rb_aazd_0);
                break;


            case R.id.rg_xczl:
                i_xczl = new BaseDataUtils().getRGNum(group, R.id.rb_xczl_0);
                break;

            case R.id.rg_jkbc:
                i_jkbc = new BaseDataUtils().getRGNum(group, R.id.rb_jkbc_0);
                break;

            case R.id.rg_xgjc:
                i_xgjc = new BaseDataUtils().getRGNum(group, R.id.rb_xgjc_0);
                break;

            case R.id.rg_gsazapp:
                i_gsazapp = new BaseDataUtils().getRGNum(group, R.id.rb_gsazapp_0);
                break;

            case R.id.rg_ryazapp:
                i_ryazapp = new BaseDataUtils().getRGNum(group, R.id.rb_ryazapp_0);
                break;

            case R.id.rg_appsj:
                i_appsj = new BaseDataUtils().getRGNum(group, R.id.rb_appsj_0);
                break;

        }


    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btn_upl:
                String msg="";
                if (((AppContext) getApplication()).getNetworkType() == 0) {
                    msg = getString(R.string.network_not_connected);
                }else if (i_kxys==2){
                    msg=getResources().getString(R.string.jdwlhc_sfkxys);
                }else if (i_smsj==2){
                    msg=getResources().getString(R.string.jdwlhc_sfsmsj);
                }else if (i_ryxxcj==2){
                    msg=getResources().getString(R.string.jdwlhc_sfryxxcj);
                }else if (i_czsg==2){
                    msg=getResources().getString(R.string.jdwlhc_sfczsg);
                }else if (i_anpx==2){
                    msg=getResources().getString(R.string.jdwlhc_sfanpx);
                }else if (i_aazd==2){
                    msg=getResources().getString(R.string.jdwlhc_sfaazd);
                }else if (i_xczl==2){
                    msg=getResources().getString(R.string.jdwlhc_sfxczl);
                }else if (i_jkbc==2){
                    msg=getResources().getString(R.string.jdwlhc_sfjkbc);
                }else if (i_xgjc==2){
                    msg=getResources().getString(R.string.jdwlhc_sfxgjc);
                }else if (i_gsazapp==2){
                    msg=getResources().getString(R.string.jdwlhc_sfgsazapp);
                }else if (i_ryazapp==2){
                    msg=getResources().getString(R.string.jdwlhc_sfryazapp);
                }else if (i_appsj==2){
                    msg=getResources().getString(R.string.jdwlhc_sfappsj);
                }
                if (!msg.isEmpty()){
                    UI.toast(LogisticsDeliveryCheckAty.this,msg+getResources().getString(R.string.info_null));
                } else {
                    uploadInfo();
                }
                break;
        }
    }

    //同步上传采集数据到服务端
    private void uploadInfo() {
        showLoadding();
        LogisticsDeliveryCheckModle checkModle=new LogisticsDeliveryCheckModle();
        checkModle.setZAJCXXID(zajcModle.getID());
        checkModle.setSFKXYS(i_kxys);
        checkModle.setSFSMDJ(i_smsj);
        checkModle.setSFCJRYXX(i_ryxxcj);
        checkModle.setSFCZSG(i_czsg);
        checkModle.setSFKZPXHD(i_anpx);
        checkModle.setSFLSABZD(i_aazd);
        checkModle.setSFZTXCZL(i_xczl);
        checkModle.setSFJKBCSST(i_jkbc);
        checkModle.setSFYXGJJC(i_xgjc);
        checkModle.setSFGSAZAPP(i_gsazapp);
        checkModle.setSFRYAZAPP(i_ryazapp);
        checkModle.setSFZQSYAPP(i_appsj);
        checkModle.setHCYH(userInfo.getUsername());
        checkModle.setHCDW(userInfo.getOrgancode());
        JSONObject jo = JSON.parseObject(JSON.toJSONString(checkModle));
        String json = jo.toJSONString();
        ApiResource.addJSWLCheck(json, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int i, Header[] headers, byte[] bytes) {
                String result = new String(bytes);
                HttpModle httpModle= JSON.parseObject(result, HttpModle.class);
                if (httpModle.getStatus().equals("0")) {
                    stopLoading();
                    setResult(RESULT_OK);
                    LogisticsDeliveryCheckAty.this.finish();
                    UI.toast(LogisticsDeliveryCheckAty.this,getString(R.string.info_upload_success));
                } else {
                    onFailure(i, headers, httpModle.getMessage().getBytes(), null);
                }

//                JSONObject jo= JSON.parseObject(result);
//                if (jo.getString("status").equals("0")) {
//                    stopLoading();
//                    setResult(RESULT_OK);
//                    LogisticsDeliveryCheckAty.this.finish();
//                    UI.toast(LogisticsDeliveryCheckAty.this,jo.getString("message"));
//                } else {
//                    onFailure(i, headers, jo.getString("message").getBytes(), null);
//                }

            }
            @Override
            public void onFailure(int i, Header[] headers, byte[] bytes, Throwable throwable) {
                stopLoading();
                if (bytes != null) {
                    String result = new String(bytes);
                    UI.toast(LogisticsDeliveryCheckAty.this,result);
                }
            }
        });
    }


    @Override
    public String getAtyTitle() {
        return getString(R.string.jdwlhc);
    }

    @Override
    public boolean enableBackUpBtn() {
        return true;
    }



}
