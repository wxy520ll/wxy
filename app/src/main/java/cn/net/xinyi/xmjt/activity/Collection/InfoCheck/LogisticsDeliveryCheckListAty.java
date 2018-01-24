package cn.net.xinyi.xmjt.activity.Collection.InfoCheck;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.loopj.android.http.AsyncHttpResponseHandler;

import org.apache.http.Header;

import java.util.List;

import cn.net.xinyi.xmjt.R;
import cn.net.xinyi.xmjt.api.ApiResource;
import cn.net.xinyi.xmjt.config.BaseListAty;
import cn.net.xinyi.xmjt.model.HttpModle;
import cn.net.xinyi.xmjt.model.LogisticsDeliveryCheckModle;
import cn.net.xinyi.xmjt.model.ZAJCModle;

//寄递物流 列表
public class LogisticsDeliveryCheckListAty extends BaseListAty {
    private ZAJCModle infos;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        infos=(ZAJCModle)getIntent().getSerializableExtra("ZAJCModle");
        requestData();
    }

    @Override
    protected void requestData() {
        getSearchData();
    }

    private void getSearchData() {
        showLoadding();
        JSONObject jo=new JSONObject();
        jo.put("ZAJCXXID",infos.getID());
        String json=jo.toJSONString();
        ApiResource.getJSWLCheckList(json, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int i, Header[] headers, byte[] bytes) {
                stopLoading();
                String result = new String(bytes);
                HttpModle httpsInfo= JSON.parseObject(result, HttpModle.class);
                if (httpsInfo.getStatus().equals("0")&&null!=httpsInfo.getResult()) {
                    List<LogisticsDeliveryCheckModle> infos =  JSON.parseArray(httpsInfo.getResult(),LogisticsDeliveryCheckModle.class);
                    LogisticsDeliveryCheckListAdp mAdapter=new LogisticsDeliveryCheckListAdp(mListView,infos,R.layout.aty_logistics_delivery_check_list_item,LogisticsDeliveryCheckListAty.this);
                    setAdapter(mAdapter);
                    setHasData();
                }else {
                    onFailure(i, headers, httpsInfo.getMessage().getBytes(), null);
                }
            }
            @Override
            public void onFailure(int i, Header[] headers, byte[] bytes, Throwable throwable) {
                stopLoading();
                setNoData();
                toast(new String(bytes));
            }
        });
    }


    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id, Object t) {
        super.onItemClick(parent, view, position, id, t);
        if (t instanceof LogisticsDeliveryCheckModle){
            LogisticsDeliveryCheckModle info=(LogisticsDeliveryCheckModle)t;
            Intent intent = new Intent(LogisticsDeliveryCheckListAty.this, LogisticsDeliveryCheckAty.class);
            intent.setFlags(1);
            intent.putExtra("checkInfo",info);
            intent.putExtra("ZAJCModle",infos);
            startActivityForResult(intent,1001);

        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode==RESULT_OK){
            requestData();
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        menu.findItem(R.id.action_map).setVisible(false);
        menu.findItem(R.id.action_list).setIcon(R.drawable.ic_menu_add);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                this.finish();
                break;
            case R.id.action_list:
                Intent intent = new Intent(LogisticsDeliveryCheckListAty.this, LogisticsDeliveryCheckAty.class);
                intent.putExtra("ZAJCModle",infos);
                startActivityForResult(intent,1001);
                break;
            default:
                break;
        }
        return true;
    }


    @Override
    public String getAtyTitle() {
        return getString(R.string.jdwljl);
    }

    @Override
    public boolean enableBackUpBtn() {
        return true;
    }

}
