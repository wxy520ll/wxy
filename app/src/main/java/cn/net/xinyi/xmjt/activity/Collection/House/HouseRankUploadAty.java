package cn.net.xinyi.xmjt.activity.Collection.House;


import android.os.Bundle;
import android.widget.ListView;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.loopj.android.http.AsyncHttpResponseHandler;

import org.apache.http.Header;

import cn.net.xinyi.xmjt.R;
import cn.net.xinyi.xmjt.activity.Collection.Camera.Adapter.RankUploadUserAdapter;
import cn.net.xinyi.xmjt.api.ApiResource;
import cn.net.xinyi.xmjt.config.BaseActivity2;
import cn.net.xinyi.xmjt.utils.BaseUtil;


public class HouseRankUploadAty extends BaseActivity2 {
    private ListView mListView;
    private RankUploadUserAdapter mAdapter;
    private JSONArray mArray;
    private int flags=2;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.plate_upload_user_count);
        mListView = (ListView) findViewById(R.id.userCountlistView);
        getCount();
    }

    private void getCount() {
        showLoadding();
        JSONObject jo = new JSONObject();
        jo.put("TEL_NUMBER",userInfo.getUsername());
        ApiResource.HouseUserCount(jo.toJSONString(), new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int i, Header[] headers, byte[] bytes) {
                String result = new String(bytes);
                if (result.length() > 4) {
                    stopLoading();
                    try {
                        mArray = JSON.parseArray(result);
                        mAdapter = new RankUploadUserAdapter(HouseRankUploadAty.this, mArray, flags);
                        mListView.setAdapter(mAdapter);
                    } catch (Exception e) {
                        onFailure(i, headers, bytes, null);
                    }
                }else {
                    onFailure(i, headers, bytes, null);
                }
            }
            @Override
            public void onFailure(int i, Header[] headers, byte[] bytes, Throwable throwable) {
                stopLoading();
                BaseUtil.showDialog("获取数据失败", HouseRankUploadAty.this);
            }
        });
    }

    @Override
    public boolean enableBackUpBtn() {
        return true;
    }

    @Override
    public String getAtyTitle() {
        return "用户每日上传统计";
    }
}


