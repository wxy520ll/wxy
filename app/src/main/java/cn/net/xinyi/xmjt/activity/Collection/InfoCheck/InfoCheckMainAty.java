package cn.net.xinyi.xmjt.activity.Collection.InfoCheck;

import android.os.Bundle;
import android.view.View;

import cn.net.xinyi.xmjt.R;
import cn.net.xinyi.xmjt.config.AppContext;
import cn.net.xinyi.xmjt.config.BaseActivity2;
import cn.net.xinyi.xmjt.utils.BaseUtil;
import cn.net.xinyi.xmjt.utils.UI;


public class InfoCheckMainAty extends BaseActivity2 implements View.OnClickListener{
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.aty_info_check_main);
        findViewById(R.id.ll_jdqy).setOnClickListener(this);
        findViewById(R.id.ll_wlqy).setOnClickListener(this);
    }



    @Override
    public boolean enableBackUpBtn() {
        return true;
    }
    @Override
    public String getAtyTitle() {
        return "信息核查";
    }

    @Override
    public void onClick(View v) {
        //检测是否连接网络
        int  networkType = ((AppContext) getApplication()).getNetworkType();
        switch (v.getId()){
            case R.id.ll_jdqy://寄递企业
                if (networkType == 0) {
                    BaseUtil.showDialog(getString(R.string.network_not_connected), InfoCheckMainAty.this);
                } else {
                    UI.startActivity(1001,this,LogisticsDeliveryListAty.class);
                }
                break;

            case R.id.ll_wlqy://物流企业
                if (networkType == 0) {
                    BaseUtil.showDialog(getString(R.string.network_not_connected), InfoCheckMainAty.this);
                } else {

                    UI.startActivity(1002,this,LogisticsDeliveryListAty.class);
                }
                break;
        }
    }

}
