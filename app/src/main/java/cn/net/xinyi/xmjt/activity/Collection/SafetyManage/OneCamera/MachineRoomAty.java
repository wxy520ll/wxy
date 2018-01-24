package cn.net.xinyi.xmjt.activity.Collection.SafetyManage.OneCamera;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.LinearLayout;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.loopj.android.http.AsyncHttpResponseHandler;

import org.apache.http.Header;

import cn.net.xinyi.xmjt.R;
import cn.net.xinyi.xmjt.api.ApiResource;
import cn.net.xinyi.xmjt.config.BaseListAty;
import cn.net.xinyi.xmjt.utils.AnnotateManager;

public class MachineRoomAty extends BaseListAty {

    private JSONArray rankArray;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        AnnotateManager.initBindView(this);//注解式绑定控件
        requestData();
    }
    @Override
    protected void requestData() {
        showLoadding();
        JSONObject requestJson = new JSONObject();
        requestJson.put("", "");
        ApiResource.MachineRoomMain(requestJson.toJSONString(),new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int i, Header[] headers, byte[] bytes) {
                String result = new String(bytes);
                if (result!=null&&result.length()>5){
                    rankArray= JSON.parseArray(result);
                    mNodata.setVisibility(View.GONE);
                    MachineRoomAdp adp = new MachineRoomAdp(MachineRoomAty.this,rankArray);
                    mListView.setAdapter(adp);
                    initView();
                } else {
                    mNodata.setVisibility(View.VISIBLE);
                }
                stopLoading();
            }
            @Override
            public void onFailure(int i, Header[] headers, byte[] bytes, Throwable throwable) {
                stopLoading();
                toast("获取数据失败");
                mNodata.setVisibility(View.VISIBLE);
            }
        });
    }

    private void initView() {
        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int i, long id) {
                Intent intent=new Intent(MachineRoomAty.this,MachineRoomPcsAty.class);
                intent.putExtra("PCSBM",((JSONObject)rankArray.get(i)).getString("PCSBM"));
                intent.putExtra("PCSMC",((JSONObject)rankArray.get(i)).getString("ZDZ"));
                startActivityForResult(intent,10001);
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode==10001&&resultCode==RESULT_OK){
            requestData();//刷新数据
        }
    }

    //顶部提示
    @Override
    public void setupTopView(LinearLayout parent) {
        super.setupTopView(parent);
        LayoutInflater.from(this).inflate(R.layout.aty_machine_room_top,parent);
        parent.setVisibility(View.VISIBLE);
    }

    @Override
    public String getAtyTitle() {
        return getString(R.string.machine_room_manage);
    }

    @Override
    public boolean enableBackUpBtn() {
        return true;
    }
}
