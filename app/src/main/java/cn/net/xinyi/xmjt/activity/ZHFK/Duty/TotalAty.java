package cn.net.xinyi.xmjt.activity.ZHFK.Duty;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import java.text.SimpleDateFormat;
import java.util.Date;

import cn.net.xinyi.xmjt.R;
import cn.net.xinyi.xmjt.activity.ZHFK.KQ.WorkCheckHistoryAty;
import cn.net.xinyi.xmjt.activity.ZHFK.PlcBx.PlcBxTJAty;
import cn.net.xinyi.xmjt.activity.ZHFK.PlcBx.PlcBxTJXQAty;
import cn.net.xinyi.xmjt.config.AppContext;
import cn.net.xinyi.xmjt.config.BaseActivity2;
import cn.net.xinyi.xmjt.utils.BaseDataUtils;
import cn.net.xinyi.xmjt.utils.BaseUtil;

/**
 * Created by studyjun on 2016/5/24.
 */
public class TotalAty extends BaseActivity2 implements View.OnClickListener{
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.aty_zhfk_tj);
        findViewById(R.id.plc_sttn_tj).setOnClickListener(this);
        findViewById(R.id.ll_tj).setOnClickListener(this);
        findViewById(R.id.ll_xz).setOnClickListener(this);
        findViewById(R.id.ll_check).setOnClickListener(this);
        if (BaseDataUtils.isPOLICE()) {
            findViewById(R.id.ll_xz).setVisibility(View.VISIBLE);
            findViewById(R.id.ll_check).setVisibility(View.VISIBLE);
        }
    }

    @Override
    public boolean enableBackUpBtn() {
        return true;
    }

    @Override
    public String getAtyTitle() {
        return getString(R.string.duty_total);
    }

    @Override
    public void onClick(View v) {
        //检测是否连接网络
        int  networkType = ((AppContext) getApplication()).getNetworkType();
        switch (v.getId()){
            case R.id.plc_sttn_tj://岗亭统计
                if (BaseDataUtils.isFastClick()){
                }else if (networkType == 0) {
                    BaseUtil.showDialog(getString(R.string.network_not_connected), TotalAty.this);
                }else if (!BaseDataUtils.notLeader()){
                    showActivity(PlcBxTJAty.class);
                }else if (BaseDataUtils.isAdminPcs()){
                    showActivity(PlcBxTJAty.class);
                }else {
                    Intent intent=new Intent(this,PlcBxTJXQAty.class);
                    intent.putExtra("SJC",new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()));
                    intent.putExtra("PCSMC",userInfo.getPcs());
                    startActivity(intent);
                }
                break;

            case R.id.ll_tj://巡逻统计
                if (networkType == 0) {
                    BaseUtil.showDialog(getString(R.string.network_not_connected), TotalAty.this);
//                } else if (!BaseDataUtils.notLeader()){
//                    showActivity(DutyTotalAty.class);
//                }else if (BaseDataUtils.isAdminPcs()){
//                    showActivity(DutyTotalAty.class);
//                }else {
//                    Intent intent=new Intent(this,DutyTotalDetailsAty.class);
//                    intent.putExtra("PCSBM",userInfo.getOrgancode());
//                    startActivity(intent);
//                }
                }else {
                    showActivity(DutyTotalMainAty.class);
                }
                break;

            case R.id.ll_xz://巡长统计
                if (networkType == 0) {
                    BaseUtil.showDialog(getString(R.string.network_not_connected), TotalAty.this);
                }else {
                    showActivity(DutyLeaderTotalAty.class);
                }
                break;

            case R.id.ll_check:
                if (networkType == 0) {
                    BaseUtil.showDialog(getString(R.string.network_not_connected), TotalAty.this);
                }else {
                    Intent intent=new Intent(this, WorkCheckHistoryAty.class);
                    intent.setFlags(1);
                    startActivity(intent);
                }
                break;
        }
    }
}
