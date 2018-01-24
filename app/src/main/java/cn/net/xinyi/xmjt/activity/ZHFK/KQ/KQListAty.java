package cn.net.xinyi.xmjt.activity.ZHFK.KQ;


import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONException;
import com.alibaba.fastjson.JSONObject;
import com.loopj.android.http.AsyncHttpResponseHandler;

import org.apache.http.Header;

import java.util.ArrayList;
import java.util.List;

import cn.net.xinyi.xmjt.R;
import cn.net.xinyi.xmjt.api.ApiResource;
import cn.net.xinyi.xmjt.config.AppContext;
import cn.net.xinyi.xmjt.config.BaseListAdp;
import cn.net.xinyi.xmjt.config.BaseListAty;
import cn.net.xinyi.xmjt.model.KQModle;

/**
 * Created by hao.zhou on 2016/4/11.
 */
public class KQListAty extends BaseListAty {

    protected static int PAGE_SIZE = 20; //每页加载条数
    private List<KQModle> kqInfos=new ArrayList<KQModle>();


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        KQListAdp  mAdapter=new KQListAdp(mListView,kqInfos,R.layout.aty_kqlist_item,KQListAty.this);
        setAdapter(mAdapter);
        mAdapter.setState(BaseListAdp.STATE_LOAD_MORE);
        showLoadding();
        requestData();
    }

    @Override
    protected void requestData() {
        ApiResource.postKQList(getRequest(), new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int i, Header[] headers, byte[] bytes) {
                if (mCurrentPage==1)
                    stopLoading();
                String result = new String(bytes);
                try {
                    List<KQModle> list = JSON.parseArray(result,KQModle.class);
                    if (list!=null){
                        if (list.size()>=PAGE_SIZE){
                            setStateFinish();
                            getAdapter().getData().addAll(list);
                        } else {
                            if (mCurrentPage==1&&list.size()==0){
                                setEmptyData();
                            } else {
                                getAdapter().getData().addAll(list);
                                setNotMoreState();
                            }
                        }
                    } else {
                        toast("没有数据");

                    }
                    getAdapter().notifyDataSetChanged();
                } catch (JSONException e){
                    toast("获取数据失败");
                }
            }

            @Override
            public void onFailure(int i, Header[] headers, byte[] bytes, Throwable throwable) {
                if (mCurrentPage==1)
                    stopLoading();
                toast("获取数据失败");
            }
        });
    }

    private String getRequest() {
        JSONObject requestJson = new JSONObject();
        requestJson.put("CJYH", AppContext.instance.getLoginInfo().getUsername());
        requestJson.put("PAGENUMBER",mCurrentPage);
        requestJson.put("PAGESIZE",PAGE_SIZE);
        return requestJson.toJSONString();
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        MenuItem  action_list = menu.findItem(R.id.action_list);
        action_list.setIcon(R.drawable.ic_menu_add);
        MenuItem action_map = menu.findItem(R.id.action_map);
        action_map.setIcon(R.drawable.ic_query);
        action_map.setVisible(false);
        return true;
    }

    @Override
    public boolean enableBackUpBtn() {
        return true;
    }
    @Override
    public String getAtyTitle() {
        return getString(R.string.kq_jl);
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                this.finish();
                break;
            /**采集**/
            case R.id.action_list:
                this.finish();
                if (getIntent().getFlags()!=1){
                    showActivity(KQAty.class);
                }
                break;

            default:
                break;
        }
        return true;
    }

}