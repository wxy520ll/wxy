package cn.net.xinyi.xmjt.activity.Collection.SafetyManage.KpiCertificate;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.loopj.android.http.AsyncHttpResponseHandler;

import org.apache.http.Header;

import cn.net.xinyi.xmjt.R;
import cn.net.xinyi.xmjt.api.ApiResource;
import cn.net.xinyi.xmjt.config.AppContext;
import cn.net.xinyi.xmjt.config.BaseActivity2;
import cn.net.xinyi.xmjt.model.MachineExamModle;
import cn.net.xinyi.xmjt.utils.AnnotateManager;
import cn.net.xinyi.xmjt.utils.BaseDataUtils;
import cn.net.xinyi.xmjt.utils.BaseUtil;
import cn.net.xinyi.xmjt.utils.BindView;

public class KpiMainAty extends BaseActivity2 implements View.OnClickListener {
    /***kpi考核**/
    @BindView(id = R.id.ll_kpi_check,click = true)
    LinearLayout ll_kpi_check;
    /***kpi备案**/
    @BindView(id = R.id.ll_kpi_record,click = true)
    LinearLayout ll_kpi_record;
    /***kpi学习**/
    @BindView(id = R.id.ll_kpi_study,click = true)
    LinearLayout ll_kpi_study;
    /***考核分数**/
    @BindView(id = R.id.tv_kh)
    TextView tv_kh;
    private boolean scorePass=false;
    private JSONObject jo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.aty_kpi_main);
        AnnotateManager.initBindView(this);  //控件绑定
        initView();
    }

    private void initView() {
        if (BaseDataUtils.isPOLICE()){
            ll_kpi_record.setVisibility(View.VISIBLE);
        }
        getCheckData();//获取考核数据
    }


    @Override
    public void onClick(View v) {
        if (BaseDataUtils.isFastClick()) {
        } else if ( ((AppContext)getApplication()).getNetworkType() == 0) {
            BaseUtil.showDialog(getString(R.string.network_not_connected), KpiMainAty.this);
        } else {
            switch (v.getId()){
                case R.id.ll_kpi_study :
                    showActivity(KpiStudyAty.class);
                    break;

                case R.id.ll_kpi_check :
                    Intent intent=new Intent(KpiMainAty.this,KpiExamAty.class);
                    startActivityForResult(intent,1001);
                    break;

                case R.id.ll_kpi_record :
                    showActivity(KpiRecordAty.class);
                    break;

            }
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode==1001&&resultCode==RESULT_OK){
            getCheckData();
        }
    }

    private void getCheckData() {
        showLoadding();
        JSONObject requestJson = new JSONObject();
        requestJson.put("CJYH", userInfo.getUsername());
        ApiResource.KpiExamineQuery(requestJson.toJSONString(),new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int i, Header[] headers, byte[] bytes) {
                String result = new String(bytes);
                if (result!=null&&result.length()>5){
                    tv_kh.setVisibility(View.VISIBLE);
                    MachineExamModle info =JSON.parseArray(result, MachineExamModle.class).get(0);
                    if (info.getSCORE() >= MachineExamModle.ResuleScore){
                        scorePass=true;
                        AppContext.Score=scorePass;
                        tv_kh.setTextColor(getResources().getColor(R.color.blue));
                        tv_kh.setText(info.getSCSJ()+"考核通过，得分"+info.getSCORE());
                    }else {
                        tv_kh.setTextColor(getResources().getColor(R.color.bbutton_danger));
                        if (info.getSCORE()< MachineExamModle.ResuleScore){
                            tv_kh.setText(info.getCJYH()+"考核，得分"+info.getSCORE()+",请重新学习并考核");
                        }
                    }
                }else {
                    tv_kh.setVisibility(View.GONE);
                }
                stopLoading();
            }
            @Override
            public void onFailure(int i, Header[] headers, byte[] bytes, Throwable throwable) {
                stopLoading();
                toast("获取数据失败");
                tv_kh.setVisibility(View.GONE);
            }
        });
    }



    @Override
    public boolean enableBackUpBtn() {
        return true;
    }


    @Override
    public String getAtyTitle() {
        return getString(R.string.kpi);
    }

}
