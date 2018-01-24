package cn.net.xinyi.xmjt.activity.Collection;


import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.loopj.android.http.AsyncHttpResponseHandler;

import org.apache.http.Header;

import cn.net.xinyi.xmjt.R;
import cn.net.xinyi.xmjt.activity.Collection.House.HouseTotalAty;
import cn.net.xinyi.xmjt.activity.Collection.Person.PerRetTotalAty;
import cn.net.xinyi.xmjt.activity.Collection.ZAJC.ZAJCTotalAty;
import cn.net.xinyi.xmjt.activity.Plate.PlateCountMenuActivity;
import cn.net.xinyi.xmjt.api.ApiResource;
import cn.net.xinyi.xmjt.config.BaseActivity2;
import cn.net.xinyi.xmjt.utils.AnnotateManager;
import cn.net.xinyi.xmjt.utils.BindView;
import cn.net.xinyi.xmjt.utils.GeneralUtils;

/**
 * Created by hao.zhou on 2016/3/5.
 */
public class CollecTotalAty extends BaseActivity2 implements View.OnClickListener {
    private JSONArray mArray;
    /***人员回访**/
    @BindView(id = R.id.ll_pre, click = true)
    LinearLayout ll_pre;
    @BindView(id = R.id.tv_pre)
    TextView tv_pre;
    /***治安基础**/
    @BindView(id = R.id.ll_zajc, click = true)
    LinearLayout ll_zajc;
    @BindView(id = R.id.tv_zajc)
    TextView tv_zajc;
    /***出租屋采集**/
    @BindView(id = R.id.ll_house, click = true)
    LinearLayout ll_house;

    @BindView(id = R.id.ll_cp_count, click = true)
    LinearLayout ll_cp_count;
    @BindView(id = R.id.tv_house)
    TextView tv_house;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.aty_cj_total);
        AnnotateManager.initBindView(this);  //控件绑定
        getCount();
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.ll_pre:
                showActivity(PerRetTotalAty.class);
                break;

            case R.id.ll_zajc:
                showActivity(ZAJCTotalAty.class);
                break;

            case R.id.ll_house:
                showActivity(HouseTotalAty.class);
                break;
            case R.id.ll_cp_count:
                showActivity(PlateCountMenuActivity.class);
                break;

        }
    }


    private void getCount() {
        showLoadding();
        ApiResource.CountAll(new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int i, Header[] headers, byte[] bytes) {
                try {
                    String result = new String(bytes);
                    mArray = JSON.parseArray(result);
                    initData();
                    stopLoading();
                } catch (Exception e) {
                    onFailure(i, headers, bytes, null);
                }
            }

            @Override
            public void onFailure(int i, Header[] headers, byte[] bytes, Throwable throwable) {
                stopLoading();
                toast("获取数据失败");
            }
        });
    }

    private void initData() {
        if (mArray.size() != 0) {
            for (int j = 0; j < mArray.size(); j++) {
                if (((JSONObject) mArray.get(j)).getString(GeneralUtils.TJLB).equals("personvisits")) {
                    tv_pre.setText("总数:" + ((JSONObject) mArray.get(j)).getString(GeneralUtils.TOTAL));
                }else if (((JSONObject) mArray.get(j)).getString(GeneralUtils.TJLB).equals("zajcxx")) {
                    tv_zajc.setText("总数:" + ((JSONObject) mArray.get(j)).getString(GeneralUtils.TOTAL));
                }  else if (((JSONObject) mArray.get(j)).getString(GeneralUtils.TJLB).equals("house")) {
                    tv_house.setText("总数:" + ((JSONObject) mArray.get(j)).getString(GeneralUtils.TOTAL));
                }
            }
        }
    }

    @Override
    public boolean enableBackUpBtn() {
        return true;
    }


    @Override
    public String getAtyTitle() {
        return getString(R.string.total);
    }
}