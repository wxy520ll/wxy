package cn.net.xinyi.xmjt.activity.ZHFK.Duty;


import android.os.Bundle;
import android.view.View;

import cn.net.xinyi.xmjt.R;
import cn.net.xinyi.xmjt.activity.ZHFK.PlcBx.PlcBxListAty;
import cn.net.xinyi.xmjt.config.AppContext;
import cn.net.xinyi.xmjt.config.BaseActivity2;
import cn.net.xinyi.xmjt.utils.BaseUtil;


public class DutySettingMainAty extends BaseActivity2 implements View.OnClickListener{
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.aty_duty_setting_menu);
        findViewById(R.id.ll_flights).setOnClickListener(this);
        findViewById(R.id.ll_duty).setOnClickListener(this);
        findViewById(R.id.ll_plc).setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        //检测是否连接网络
        int  networkType = ((AppContext) getApplication()).getNetworkType();
        switch (v.getId()){
            case R.id.ll_flights://排班规则设置
                if (networkType == 0) {
                    BaseUtil.showDialog(getString(R.string.network_not_connected), DutySettingMainAty.this);
                } else {
                    showActivity(DutyFlightRulesAty.class);
                }
                break;

            case R.id.ll_duty://巡逻设置
                if (networkType == 0) {
                    BaseUtil.showDialog(getString(R.string.network_not_connected), DutySettingMainAty.this);
                } else {
                    showActivity(DutyBeatsAddAty.class);
                }
                break;

            case R.id.ll_plc://防控点设置
                if (networkType == 0) {
                    BaseUtil.showDialog(getString(R.string.network_not_connected), DutySettingMainAty.this);
                } else {
                    showActivity(PlcBxListAty.class);
                }
                break;

        }
    }
    @Override
    public boolean enableBackUpBtn() {
        return true;
    }

    @Override
    public String getAtyTitle() {
        return getString(R.string.duty_setting);
    }




}
