package cn.net.xinyi.xmjt.activity.Collection;


import android.os.Bundle;
import android.view.View;

import cn.net.xinyi.xmjt.R;
import cn.net.xinyi.xmjt.activity.Collection.Person.PerReturnLocalAty;
import cn.net.xinyi.xmjt.activity.Collection.ZAJC.ZAJCLocalAty;
import cn.net.xinyi.xmjt.activity.Plate.PlateListActivity;
import cn.net.xinyi.xmjt.config.BaseActivity2;

/**
 * Created by hao.zhou on 2016/3/5.
 */
public class CollecManageAty extends BaseActivity2 implements View.OnClickListener {
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.aty_cj_manage);
        findViewById(R.id.ll_perretu).setOnClickListener(this);
        findViewById(R.id.ll_zajc).setOnClickListener(this);
        findViewById(R.id.ll_cp_manage).setOnClickListener(this);
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.ll_zajc:
                showActivity(ZAJCLocalAty.class);
                break;
            case R.id.ll_perretu:
                showActivity(PerReturnLocalAty.class);
                break;
            case R.id.ll_cp_manage:
                showActivity(PlateListActivity.class);
                break;

        }
    }

    @Override
    public boolean enableBackUpBtn() {
        return true;
    }


    @Override
    public String getAtyTitle() {
        return getString(R.string.manage);
    }

}