package cn.net.xinyi.xmjt.activity.Collection.House;


import android.os.Bundle;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.util.TypeUtils;
import com.loopj.android.http.AsyncHttpResponseHandler;

import org.apache.http.Header;

import java.util.List;

import cn.net.xinyi.xmjt.R;
import cn.net.xinyi.xmjt.api.ApiResource;
import cn.net.xinyi.xmjt.config.BaseActivity2;
import cn.net.xinyi.xmjt.model.HouseCheckModle;
import cn.net.xinyi.xmjt.utils.AnnotateManager;
import cn.net.xinyi.xmjt.utils.BaseUtil;
import cn.net.xinyi.xmjt.utils.BindView;

public class HouseRankDetailsAty extends BaseActivity2 {

    @BindView(id = R.id.CJSZ_1)
    private TextView CJSZ_1;
    @BindView(id = R.id.SFKTJZDJZH_1)
    private TextView SFKTJZDJZH_1;
    @BindView(id = R.id.ZZLDS_1)
    private TextView ZZLDS_1;
    @BindView(id = R.id.BAYSL_1)
    private TextView BAYSL_1;
    @BindView(id = R.id.XLBARS_1)
    private TextView XLBARS_1;
    @BindView(id = R.id.SFYZGL_1)
    private TextView SFYZGL_1;
    @BindView(id = R.id.MGSL_1)
    private TextView MGSL_1;
    @BindView(id = R.id.FWSFYXFQC_1)
    private TextView FWSFYXFQC_1;
    @BindView(id = R.id.PSGSFAZPCHTMHY_1)
    private TextView PSGSFAZPCHTMHY_1;
    @BindView(id = R.id.SFAZFDM_1)
    private TextView SFAZFDM_1;
    @BindView(id = R.id.SXSFBJS_1)
    private TextView SXSFBJS_1;
    @BindView(id = R.id.XFSZSFWH_1)
    private TextView XFSZSFWH_1;
    @BindView(id = R.id.SFPBLDFWZ_1)
    private TextView SFPBLDFWZ_1;
    @BindView(id = R.id.SFPBLDZ_1)
    private TextView SFPBLDZ_1;
    @BindView(id = R.id.SFYGLY_1)
    private TextView SFYGLY_1;
    @BindView(id = R.id.SFYMHQ_1)
    private TextView SFYMHQ_1;
    @BindView(id = R.id.SFYPCSLW_1)
    private TextView SFYPCSLW_1;
    @BindView(id = R.id.SFYWYGLGS_1)
    private TextView SFYWYGLGS_1;
    @BindView(id = R.id.SFYYJZMD_1)
    private TextView SFYYJZMD_1;
    @BindView(id = R.id.SFAZSPMJ_1)
    private TextView SFAZSPMJ_1;
    @BindView(id = R.id.MJSFSXSK_1)
    private TextView MJSFSXSK_1;
    @BindView(id = R.id.MJSHSL_1)
    private TextView MJSHSL_1;
    @BindView(id = R.id.SFAZDJSPMJ_1)
    private TextView SFAZDJSPMJ_1;
    @BindView(id = R.id.GGQYTTSL_1)
    private TextView GGQYTTSL_1;
    @BindView(id = R.id.GQTTSL_1)
    private TextView GQTTSL_1;
    @BindView(id = R.id.PTTTSL_1)
    private TextView PTTTSL_1;
    @BindView(id = R.id.SPSHSL_1)
    private TextView SPSHSL_1;
    @BindView(id = R.id.ZWWQSFAZTT_1)
    private TextView ZWWQSFAZTT_1;
    @BindView(id = R.id.SFAZYPCSLWDYJBJ_1)
    private TextView SFAZYPCSLWDYJBJ_1;

    @BindView(id = R.id.CJSZ_2)
    private TextView CJSZ_2;
    @BindView(id = R.id.SFKTJZDJZH_2)
    private TextView SFKTJZDJZH_2;
    @BindView(id = R.id.ZZLDS_2)
    private TextView ZZLDS_2;
    @BindView(id = R.id.BAYSL_2)
    private TextView BAYSL_2;
    @BindView(id = R.id.XLBARS_2)
    private TextView XLBARS_2;
    @BindView(id = R.id.SFYZGL_2)
    private TextView SFYZGL_2;
    @BindView(id = R.id.MGSL_2)
    private TextView MGSL_2;
    @BindView(id = R.id.FWSFYXFQC_2)
    private TextView FWSFYXFQC_2;
    @BindView(id = R.id.PSGSFAZPCHTMHY_2)
    private TextView PSGSFAZPCHTMHY_2;
    @BindView(id = R.id.SFAZFDM_2)
    private TextView SFAZFDM_2;
    @BindView(id = R.id.SXSFBJS_2)
    private TextView SXSFBJS_2;
    @BindView(id = R.id.XFSZSFWH_2)
    private TextView XFSZSFWH_2;
    @BindView(id = R.id.SFPBLDFWZ_2)
    private TextView SFPBLDFWZ_2;
    @BindView(id = R.id.SFPBLDZ_2)
    private TextView SFPBLDZ_2;
    @BindView(id = R.id.SFYGLY_2)
    private TextView SFYGLY_2;
    @BindView(id = R.id.SFYMHQ_2)
    private TextView SFYMHQ_2;
    @BindView(id = R.id.SFYPCSLW_2)
    private TextView SFYPCSLW_2;
    @BindView(id = R.id.SFYWYGLGS_2)
    private TextView SFYWYGLGS_2;
    @BindView(id = R.id.SFYYJZMD_2)
    private TextView SFYYJZMD_2;
    @BindView(id = R.id.SFAZSPMJ_2)
    private TextView SFAZSPMJ_2;
    @BindView(id = R.id.MJSFSXSK_2)
    private TextView MJSFSXSK_2;
    @BindView(id = R.id.MJSHSL_2)
    private TextView MJSHSL_2;
    @BindView(id = R.id.SFAZDJSPMJ_2)
    private TextView SFAZDJSPMJ_2;
    @BindView(id = R.id.GGQYTTSL_2)
    private TextView GGQYTTSL_2;
    @BindView(id = R.id.GQTTSL_2)
    private TextView GQTTSL_2;
    @BindView(id = R.id.PTTTSL_2)
    private TextView PTTTSL_2;
    @BindView(id = R.id.SPSHSL_2)
    private TextView SPSHSL_2;
    @BindView(id = R.id.ZWWQSFAZTT_2)
    private TextView ZWWQSFAZTT_2;
    @BindView(id = R.id.SFAZYPCSLWDYJBJ_2)
    private TextView SFAZYPCSLWDYJBJ_2;

    @BindView(id = R.id.CJSZ_3)
    private TextView CJSZ_3;
    @BindView(id = R.id.SFKTJZDJZH_3)
    private TextView SFKTJZDJZH_3;
    @BindView(id = R.id.ZZLDS_3)
    private TextView ZZLDS_3;
    @BindView(id = R.id.BAYSL_3)
    private TextView BAYSL_3;
    @BindView(id = R.id.XLBARS_3)
    private TextView XLBARS_3;
    @BindView(id = R.id.SFYZGL_3)
    private TextView SFYZGL_3;
    @BindView(id = R.id.MGSL_3)
    private TextView MGSL_3;
    @BindView(id = R.id.FWSFYXFQC_3)
    private TextView FWSFYXFQC_3;
    @BindView(id = R.id.PSGSFAZPCHTMHY_3)
    private TextView PSGSFAZPCHTMHY_3;
    @BindView(id = R.id.SFAZFDM_3)
    private TextView SFAZFDM_3;
    @BindView(id = R.id.SXSFBJS_3)
    private TextView SXSFBJS_3;
    @BindView(id = R.id.XFSZSFWH_3)
    private TextView XFSZSFWH_3;
    @BindView(id = R.id.SFPBLDFWZ_3)
    private TextView SFPBLDFWZ_3;
    @BindView(id = R.id.SFPBLDZ_3)
    private TextView SFPBLDZ_3;
    @BindView(id = R.id.SFYGLY_3)
    private TextView SFYGLY_3;
    @BindView(id = R.id.SFYMHQ_3)
    private TextView SFYMHQ_3;
    @BindView(id = R.id.SFYPCSLW_3)
    private TextView SFYPCSLW_3;
    @BindView(id = R.id.SFYWYGLGS_3)
    private TextView SFYWYGLGS_3;
    @BindView(id = R.id.SFYYJZMD_3)
    private TextView SFYYJZMD_3;
    @BindView(id = R.id.SFAZSPMJ_3)
    private TextView SFAZSPMJ_3;
    @BindView(id = R.id.MJSFSXSK_3)
    private TextView MJSFSXSK_3;
    @BindView(id = R.id.MJSHSL_3)
    private TextView MJSHSL_3;
    @BindView(id = R.id.SFAZDJSPMJ_3)
    private TextView SFAZDJSPMJ_3;
    @BindView(id = R.id.GGQYTTSL_3)
    private TextView GGQYTTSL_3;
    @BindView(id = R.id.GQTTSL_3)
    private TextView GQTTSL_3;
    @BindView(id = R.id.PTTTSL_3)
    private TextView PTTTSL_3;
    @BindView(id = R.id.SPSHSL_3)
    private TextView SPSHSL_3;
    @BindView(id = R.id.ZWWQSFAZTT_3)
    private TextView ZWWQSFAZTT_3;
    @BindView(id = R.id.SFAZYPCSLWDYJBJ_3)
    private TextView SFAZYPCSLWDYJBJ_3;

    @BindView(id = R.id.CJSZ_4)
    private TextView CJSZ_4;
    @BindView(id = R.id.SFKTJZDJZH_4)
    private TextView SFKTJZDJZH_4;
    @BindView(id = R.id.ZZLDS_4)
    private TextView ZZLDS_4;
    @BindView(id = R.id.BAYSL_4)
    private TextView BAYSL_4;
    @BindView(id = R.id.XLBARS_4)
    private TextView XLBARS_4;
    @BindView(id = R.id.SFYZGL_4)
    private TextView SFYZGL_4;
    @BindView(id = R.id.MGSL_4)
    private TextView MGSL_4;
    @BindView(id = R.id.FWSFYXFQC_4)
    private TextView FWSFYXFQC_4;
    @BindView(id = R.id.PSGSFAZPCHTMHY_4)
    private TextView PSGSFAZPCHTMHY_4;
    @BindView(id = R.id.SFAZFDM_4)
    private TextView SFAZFDM_4;
    @BindView(id = R.id.SXSFBJS_4)
    private TextView SXSFBJS_4;
    @BindView(id = R.id.XFSZSFWH_4)
    private TextView XFSZSFWH_4;
    @BindView(id = R.id.SFPBLDFWZ_4)
    private TextView SFPBLDFWZ_4;
    @BindView(id = R.id.SFPBLDZ_4)
    private TextView SFPBLDZ_4;
    @BindView(id = R.id.SFYGLY_4)
    private TextView SFYGLY_4;
    @BindView(id = R.id.SFYMHQ_4)
    private TextView SFYMHQ_4;
    @BindView(id = R.id.SFYPCSLW_4)
    private TextView SFYPCSLW_4;
    @BindView(id = R.id.SFYWYGLGS_4)
    private TextView SFYWYGLGS_4;
    @BindView(id = R.id.SFYYJZMD_4)
    private TextView SFYYJZMD_4;
    @BindView(id = R.id.SFAZSPMJ_4)
    private TextView SFAZSPMJ_4;
    @BindView(id = R.id.MJSFSXSK_4)
    private TextView MJSFSXSK_4;
    @BindView(id = R.id.MJSHSL_4)
    private TextView MJSHSL_4;
    @BindView(id = R.id.SFAZDJSPMJ_4)
    private TextView SFAZDJSPMJ_4;
    @BindView(id = R.id.GGQYTTSL_4)
    private TextView GGQYTTSL_4;
    @BindView(id = R.id.GQTTSL_4)
    private TextView GQTTSL_4;
    @BindView(id = R.id.PTTTSL_4)
    private TextView PTTTSL_4;
    @BindView(id = R.id.SPSHSL_4)
    private TextView SPSHSL_4;
    @BindView(id = R.id.ZWWQSFAZTT_4)
    private TextView ZWWQSFAZTT_4;
    @BindView(id = R.id.SFAZYPCSLWDYJBJ_4)
    private TextView SFAZYPCSLWDYJBJ_4;
    private List<HouseCheckModle> houseInfos;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.aty_house_rank_details);
        AnnotateManager.initBindView(this);//注解式绑定控件
        TypeUtils.compatibleWithJavaBean = true;
        getData();
    }

    private void getData(){
        showLoadding();
        JSONObject jo = new JSONObject();
        jo.put("PCSBM",getIntent().getStringExtra("PCSBM"));
        String json = jo.toJSONString();
        ApiResource.HousePCSRankDetails(json, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int i, Header[] headers, byte[] bytes) {
                String result = new String(bytes);
                if (result.length()>4){
                    stopLoading();
                    houseInfos = JSON.parseArray(result, HouseCheckModle.class);
                    setData();
                }else {
                    onFailure(i, headers, bytes, null);
                }
            }
            @Override
            public void onFailure(int i, Header[] headers, byte[] bytes, Throwable throwable) {
                stopLoading();
                BaseUtil.showDialog("获取数据失败", HouseRankDetailsAty.this);
            }
        });
    }
    // CJSZ_1  SFKTJZDJZH_1   ZZLDS_1  BAYSL_1  XLBARS_1  SFYZGL_1
    // MGSL_1  FWSFYXFQC_1  PSGSFAZPCHTMHY_1  SFAZFDM_1  SXSFBJS_1  XFSZSFWH_1  SFPBLDFWZ_1  SFPBLDZ_1
    // SFYGLY_1  SFYMHQ_1  SFYPCSLW_1  SFYWYGLGS_1  SFYYJZMD_1 SFAZSPMJ_1  MJSFSXSK_1  MJSHSL_1  SFAZDJSPMJ_1
    //GGQYTTSL_1  GQTTSL_1  PTTTSL_1  SPSHSL_1  ZWWQSFAZTT_1  SFAZYPCSLWDYJBJ_1
    private void setData() {
        for (HouseCheckModle info:houseInfos){
            if (info.getTYPE().equals("01")){
                CJSZ_1.setText(info.getXQZS());
                SFKTJZDJZH_1.setText(""+info.getSFKTJZDJZH());
                ZZLDS_1.setText(info.getZZLDS());
                BAYSL_1.setText(info.getBAYSL());
                XLBARS_1.setText(info.getXLBARS());
                SFYZGL_1.setText(""+info.getSFYZGL());
                MGSL_1.setText(info.getMGSL());
                FWSFYXFQC_1.setText(""+info.getFWSFYXFQC());
                PSGSFAZPCHTMHY_1.setText(""+info.getPSGSFAZPCHTMHY());
                SFAZFDM_1.setText(""+info.getSFAZFDM());
                SXSFBJS_1.setText(""+info.getSXSFBJS());
                XFSZSFWH_1.setText(""+info.getXFSZSFWH());
                SFPBLDFWZ_1.setText(""+info.getSFPBLDFWZ());
                SFPBLDZ_1.setText(""+info.getSFPBLDZ());
                SFYGLY_1.setText(""+info.getSFYGLY());
                SFYMHQ_1.setText(""+info.getSFYMHQ());
                SFYPCSLW_1.setText(""+info.getSFYPCSLW());
                SFYWYGLGS_1.setText(""+info.getSFYWYGLGS());
                SFYYJZMD_1.setText(""+info.getSFYYJZMD());
                SFAZSPMJ_1.setText(""+info.getSFAZSPMJ());
                MJSFSXSK_1.setText(""+info.getMJSFSXSK());
                MJSHSL_1.setText(info.getMJSHSL());
                SFAZDJSPMJ_1.setText(""+info.getSFAZDJSPMJ());
                GGQYTTSL_1.setText(info.getGGQYTTSL());
                GQTTSL_1.setText(info.getGQTTSL());
                PTTTSL_1.setText(info.getPTTTSL());
                SPSHSL_1.setText(info.getSPSHSL());
                ZWWQSFAZTT_1.setText(""+info.getZWWQSFAZTT());
                SFAZYPCSLWDYJBJ_1.setText(""+info.getSFAZYPCSLWDYJBJ());
            }else if (info.getTYPE().equals("02")){
                CJSZ_2.setText(info.getXQZS());
                SFKTJZDJZH_2.setText(""+info.getSFKTJZDJZH());
                ZZLDS_2.setText(info.getZZLDS());
                BAYSL_2.setText(info.getBAYSL());
                XLBARS_2.setText(info.getXLBARS());
                SFYZGL_2.setText(""+info.getSFYZGL());
                MGSL_2.setText(info.getMGSL());
                FWSFYXFQC_2.setText(""+info.getFWSFYXFQC());
                PSGSFAZPCHTMHY_2.setText(""+info.getPSGSFAZPCHTMHY());
                SFAZFDM_2.setText(""+info.getSFAZFDM());
                SXSFBJS_2.setText(""+info.getSXSFBJS());
                XFSZSFWH_2.setText(""+info.getXFSZSFWH());
                SFPBLDFWZ_2.setText(""+info.getSFPBLDFWZ());
                SFPBLDZ_2.setText(""+info.getSFPBLDZ());
                SFYGLY_2.setText(""+info.getSFYGLY());
                SFYMHQ_2.setText(""+info.getSFYMHQ());
                SFYPCSLW_2.setText(""+info.getSFYPCSLW());
                SFYWYGLGS_2.setText(""+info.getSFYWYGLGS());
                SFYYJZMD_2.setText(""+info.getSFYYJZMD());
                SFAZSPMJ_2.setText(""+info.getSFAZSPMJ());
                MJSFSXSK_2.setText(""+info.getMJSFSXSK());
                MJSHSL_2.setText(info.getMJSHSL());
                SFAZDJSPMJ_2.setText(""+info.getSFAZDJSPMJ());
                GGQYTTSL_2.setText(info.getGGQYTTSL());
                GQTTSL_2.setText(info.getGQTTSL());
                PTTTSL_2.setText(info.getPTTTSL());
                SPSHSL_2.setText(info.getSPSHSL());
                ZWWQSFAZTT_2.setText(""+info.getZWWQSFAZTT());
                SFAZYPCSLWDYJBJ_2.setText(""+info.getSFAZYPCSLWDYJBJ());
            }else  if (info.getTYPE().equals("03")){
                CJSZ_3.setText(info.getXQZS());
                SFKTJZDJZH_3.setText(""+info.getSFKTJZDJZH());
                ZZLDS_3.setText(info.getZZLDS());
                BAYSL_3.setText(info.getBAYSL());
                XLBARS_3.setText(info.getXLBARS());
                SFYZGL_3.setText(""+info.getSFYZGL());
                MGSL_3.setText(info.getMGSL());
                FWSFYXFQC_3.setText(""+info.getFWSFYXFQC());
                PSGSFAZPCHTMHY_3.setText(""+info.getPSGSFAZPCHTMHY());
                SFAZFDM_3.setText(""+info.getSFAZFDM());
                SXSFBJS_3.setText(""+info.getSXSFBJS());
                XFSZSFWH_3.setText(""+info.getXFSZSFWH());
                SFPBLDFWZ_3.setText(""+info.getSFPBLDFWZ());
                SFPBLDZ_3.setText(""+info.getSFPBLDZ());
                SFYGLY_3.setText(""+info.getSFYGLY());
                SFYMHQ_3.setText(""+info.getSFYMHQ());
                SFYPCSLW_3.setText(""+info.getSFYPCSLW());
                SFYWYGLGS_3.setText(""+info.getSFYWYGLGS());
                SFYYJZMD_3.setText(""+info.getSFYYJZMD());
                SFAZSPMJ_3.setText(""+info.getSFAZSPMJ());
                MJSFSXSK_3.setText(""+info.getMJSFSXSK());
                MJSHSL_3.setText(info.getMJSHSL());
                SFAZDJSPMJ_3.setText(""+info.getSFAZDJSPMJ());
                GGQYTTSL_3.setText(info.getGGQYTTSL());
                GQTTSL_3.setText(info.getGQTTSL());
                PTTTSL_3.setText(info.getPTTTSL());
                SPSHSL_3.setText(info.getSPSHSL());
                ZWWQSFAZTT_3.setText(""+info.getZWWQSFAZTT());
                SFAZYPCSLWDYJBJ_3.setText(""+info.getSFAZYPCSLWDYJBJ());
            }else  if (info.getTYPE().equals("04")){
                CJSZ_4.setText(info.getXQZS());
                SFKTJZDJZH_4.setText(""+info.getSFKTJZDJZH());
                ZZLDS_4.setText(info.getZZLDS());
                BAYSL_4.setText(info.getBAYSL());
                XLBARS_4.setText(info.getXLBARS());
                SFYZGL_4.setText(""+info.getSFYZGL());
                MGSL_4.setText(info.getMGSL());
                FWSFYXFQC_4.setText(""+info.getFWSFYXFQC());
                PSGSFAZPCHTMHY_4.setText(""+info.getPSGSFAZPCHTMHY());
                SFAZFDM_4.setText(""+info.getSFAZFDM());
                SXSFBJS_4.setText(""+info.getSXSFBJS());
                XFSZSFWH_4.setText(""+info.getXFSZSFWH());
                SFPBLDFWZ_4.setText(""+info.getSFPBLDFWZ());
                SFPBLDZ_4.setText(""+info.getSFPBLDZ());
                SFYGLY_4.setText(""+info.getSFYGLY());
                SFYMHQ_4.setText(""+info.getSFYMHQ());
                SFYPCSLW_4.setText(""+info.getSFYPCSLW());
                SFYWYGLGS_4.setText(""+info.getSFYWYGLGS());
                SFYYJZMD_4.setText(""+info.getSFYYJZMD());
                SFAZSPMJ_4.setText(""+info.getSFAZSPMJ());
                MJSFSXSK_4.setText(""+info.getMJSFSXSK());
                MJSHSL_4.setText(info.getMJSHSL());
                SFAZDJSPMJ_4.setText(""+info.getSFAZDJSPMJ());
                GGQYTTSL_4.setText(info.getGGQYTTSL());
                GQTTSL_4.setText(info.getGQTTSL());
                PTTTSL_4.setText(info.getPTTTSL());
                SPSHSL_4.setText(info.getSPSHSL());
                ZWWQSFAZTT_4.setText(""+info.getZWWQSFAZTT());
                SFAZYPCSLWDYJBJ_4.setText(""+info.getSFAZYPCSLWDYJBJ());
            }
        }
    }


    @Override
    public boolean enableBackUpBtn() {
        return true;
    }

    @Override
    public String getAtyTitle() {
        return "统计详情";
    }


}
