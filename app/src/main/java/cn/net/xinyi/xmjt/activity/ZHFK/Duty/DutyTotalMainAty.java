package cn.net.xinyi.xmjt.activity.ZHFK.Duty;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import java.util.Date;

import cn.net.xinyi.xmjt.R;
import cn.net.xinyi.xmjt.config.AppContext;
import cn.net.xinyi.xmjt.config.BaseActivity2;
import cn.net.xinyi.xmjt.utils.BaseDataUtils;
import cn.net.xinyi.xmjt.utils.BaseUtil;
import cn.net.xinyi.xmjt.utils.DateUtil;

/**
 * Created by studyjun on 2016/5/24.
 */
public class DutyTotalMainAty extends BaseActivity2 implements View.OnClickListener{
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.aty_duty_total_main);
        findViewById(R.id.ll_xl_lc).setOnClickListener(this);
        findViewById(R.id.ll_xl_zg).setOnClickListener(this);
    }

    @Override
    public boolean enableBackUpBtn() {
        return true;
    }

    @Override
    public String getAtyTitle() {
        return getString(R.string.xl_tj);
    }

    @Override
    public void onClick(View v) {
        //检测是否连接网络
        int  networkType = ((AppContext) getApplication()).getNetworkType();
        switch (v.getId()){
            case R.id.ll_xl_zg://巡逻在岗统计
                if (networkType == 0) {
                    BaseUtil.showDialog(getString(R.string.network_not_connected), DutyTotalMainAty.this);
                }else if (!BaseDataUtils.notLeader()){
                    showActivity(DutyTotalAty.class);
                }else if (BaseDataUtils.isAdminPcs()){
                    showActivity(DutyTotalAty.class);
                }else {
                    Intent intent=new Intent(this,DutyTotalDetailsAty.class);
                    intent.putExtra("PCSBM",userInfo.getOrgancode());
                    intent.putExtra("TIME", DateUtil.date2String(new Date(),"yyyy-MM-dd HH:ss:mm"));
                    startActivity(intent);
                }
                break;

            case R.id.ll_xl_lc://巡逻里程统计
                if (networkType == 0) {
                    BaseUtil.showDialog(getString(R.string.network_not_connected), DutyTotalMainAty.this);
//                }else if (!BaseDataUtils.notLeader()){
//                    showActivity(DutyTotalSecondAty.class);
//                }else if (BaseDataUtils.isAdminPcs()){
//                    showActivity(DutyTotalSecondAty.class);
                }else {
                    showActivity(DutyTotalSecondAty.class);
//                    Intent intent=new Intent(this,DutyTotalDetailsSecondAty.class);
//                    intent.putExtra("PCSBM",userInfo.getOrgancode());
//                    startActivity(intent);
                }
                break;

        }
    }
}
