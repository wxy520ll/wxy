package cn.net.xinyi.xmjt.activity.Collection.House;

import android.os.Bundle;
import android.view.View;

import cn.net.xinyi.xmjt.R;
import cn.net.xinyi.xmjt.config.BaseActivity2;

/**
 * Created by hao.zhou on 2016/3/5.
 */
public class HouseTotalAty extends BaseActivity2 implements View.OnClickListener {
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.aty_store_total);
        findViewById(R.id.ll_pcs).setOnClickListener(this);
        findViewById(R.id.ll_per_rank).setOnClickListener(this);
        findViewById(R.id.ll_per).setVisibility(View.GONE);

    }


    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.ll_pcs:
                showActivity(HouseRankPcsAty.class);
                break;

            case R.id.ll_per_rank:
                showActivity(HouseRankUploadAty.class);
                break;
        }
    }


    @Override
    public String getAtyTitle() {
        return getString(R.string.house_total);
    }

    @Override
    public boolean enableBackUpBtn() {
        return true;
    }
}