package cn.net.xinyi.xmjt.activity.ZHFK.PlcBx;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.loopj.android.http.AsyncHttpResponseHandler;

import org.apache.http.Header;

import cn.net.xinyi.xmjt.R;
import cn.net.xinyi.xmjt.api.ApiResource;
import cn.net.xinyi.xmjt.config.AppContext;
import cn.net.xinyi.xmjt.config.BaseListAty;
import cn.net.xinyi.xmjt.utils.BaseDataUtils;

/**
 * Created by studyjun on 2016/4/20.
 */
public class PlcBxListAty extends BaseListAty implements View.OnClickListener {

    public static PlcBxListAty intence;
    private JSONArray rankArray;
    private TextView tv_type;
    private Button btn_query;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        intence=this;
        initItemClick();
    }

    @Override
    protected void requestData() {
        showLoadding();
        ApiResource.getNoPosGtList(getRequest(), new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int i, Header[] headers, byte[] bytes) {
                String result = new String(bytes);
                if (null!=rankArray){
                    rankArray.clear();
                }
                rankArray= JSON.parseArray(result);
                if (rankArray!=null&&rankArray.size()>0){
                    setHasData();
                    PlcBxListAdp adp = new PlcBxListAdp(PlcBxListAty.this,rankArray);
                    mListView.setAdapter(adp);
                } else {
                    setNoData();
                }
                stopLoading();
            }
            @Override
            public void onFailure(int i, Header[] headers, byte[] bytes, Throwable throwable) {
                toast("获取数据失败");
                setNoData();
            }
        });
    }

    private String getRequest() {
        JSONObject requestJson = new JSONObject();
        requestJson.put("POLICESTATION", userInfo.getPcs());
        requestJson.put("TYPE",tv_type.getText().toString());
        requestJson.put("PAGENUMBER",mCurrentPage);
        requestJson.put("PAGESIZE",PAGE_SIZE);
        return requestJson.toJSONString();
    }
    private void initItemClick() {
        requestData();
        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                if (getIntent().getFlags()==1){
                    Intent intent=new Intent(PlcBxListAty.this,PlcBxZSAty.class);
                    intent.putExtra("data",((JSONObject)rankArray.get(i)).getString("ID"));
                    startActivity(intent);
                }else {
                    Intent intent = new Intent(PlcBxListAty.this,PlcBxInfoAty.class);
                    intent.putExtra("data",((JSONObject)rankArray.get(i)).getString("ID"));
                    startActivityForResult(intent,1);
                }
            }
        });
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        MenuItem action_list = menu.findItem(R.id.action_list);
        action_list.setIcon(R.drawable.ic_menu_add);
        MenuItem action_map = menu.findItem(R.id.action_map);
        action_map.setVisible(false);
        if (getIntent().getFlags()==1){
            action_list.setVisible(false);
        }
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId()== R.id.action_list){
            Intent intent = new Intent(this,PlcBxInfoAddAty.class);
            startActivityForResult(intent,2);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean enableBackUpBtn() {
        return true;
    }


    @Override
    public String getAtyTitle() {
        return getString(R.string.plc_bx_cj_list);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (RESULT_OK==resultCode){
            requestData();
        }
    }
    //查询部分布局
    @Override
    public void setupTopView(LinearLayout parent) {
        super.setupTopView(parent);
        View addView= LayoutInflater.from(this).inflate(R.layout.aty_dutybeats_top,parent);
        parent.setVisibility(View.VISIBLE);
        TextView tv_level=(TextView)addView.findViewById(R.id.tv_level);
        tv_level.setVisibility(View.GONE);
        tv_type=(TextView)addView.findViewById(R.id.tv_type);//类别
        tv_type.setText("二级防控点");
        tv_type.setOnClickListener(this);

        btn_query=(Button)addView.findViewById(R.id.btn_query);//查询
        btn_query.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.tv_type:
                BaseDataUtils.showAlertDialog(this, getResources().getStringArray(R.array.duty_plate_type),tv_type);
                break;

            case R.id.btn_query:
                if (((AppContext) getApplication()).getNetworkType() == 0) {
                    toast(getString(R.string.network_not_connected));
                } else{
                    requestData();
                }
                break;
        }
    }
}