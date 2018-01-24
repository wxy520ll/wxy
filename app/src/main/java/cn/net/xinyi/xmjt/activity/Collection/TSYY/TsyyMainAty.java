package cn.net.xinyi.xmjt.activity.Collection.TSYY;


import android.os.Bundle;
import android.view.View;

import cn.net.xinyi.xmjt.R;
import cn.net.xinyi.xmjt.config.AppContext;
import cn.net.xinyi.xmjt.config.BaseActivity2;
import cn.net.xinyi.xmjt.utils.BaseDataUtils;
import cn.net.xinyi.xmjt.utils.BaseUtil;
import cn.net.xinyi.xmjt.utils.UI;

/**
 * Created by hao.zhou on 2016/3/5.
 */
public class TsyyMainAty extends BaseActivity2 implements View.OnClickListener {
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.aty_tsyy_main);
        findViewById(R.id.ll_tsyy).setOnClickListener(this);
        findViewById(R.id.ll_tspd).setOnClickListener(this);
        findViewById(R.id.ll_tsjl).setOnClickListener(this);
    }


    @Override
    public void onClick(View v) {
        //检测是否连接网络
        int networkType = ((AppContext)getApplication()).getNetworkType();
        if (BaseDataUtils.isFastClick()) {

        } else if (networkType == 0) {
            BaseUtil.showDialog(getString(R.string.network_not_connected), TsyyMainAty.this);
        } else{
            switch (v.getId()) {

                case R.id.ll_tsyy://提审预约
                    showActivity(TsyyAty.class);
                    break;

                case R.id.ll_tspd://提审排队
                    UI.startActivity(TsyyMainAty.this,TsyyPdListAty.class);
                    break;

                case R.id.ll_tsjl://提审记录
                    UI.startActivity(TsyyMainAty.this,TsyyTsjlListAty.class);
                    break;

            }
        }
    }

    @Override
    public boolean enableBackUpBtn() {
        return true;
    }


    @Override
    public String getAtyTitle() {
        return getString(R.string.ttys);
    }
}