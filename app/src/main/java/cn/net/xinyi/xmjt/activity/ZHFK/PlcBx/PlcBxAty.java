package cn.net.xinyi.xmjt.activity.ZHFK.PlcBx;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.Spinner;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONException;
import com.alibaba.fastjson.JSONObject;
import com.loopj.android.http.AsyncHttpResponseHandler;

import org.apache.http.Header;

import java.util.List;

import cn.net.xinyi.xmjt.R;
import cn.net.xinyi.xmjt.api.ApiResource;
import cn.net.xinyi.xmjt.config.AppContext;
import cn.net.xinyi.xmjt.config.BaseActivity2;
import cn.net.xinyi.xmjt.model.LocaionInfo;
import cn.net.xinyi.xmjt.model.PlcBxWorkLog;
import cn.net.xinyi.xmjt.model.PoliceBoxModle;
import cn.net.xinyi.xmjt.model.UserPlcBxInfo;
import cn.net.xinyi.xmjt.model.XinyiLatLng;
import cn.net.xinyi.xmjt.utils.AnnotateManager;
import cn.net.xinyi.xmjt.utils.BaseDataUtils;
import cn.net.xinyi.xmjt.utils.BaseUtil;
import cn.net.xinyi.xmjt.utils.BindView;

/**
 * Created by studyjun on 2016/4/20.
 */
public class PlcBxAty extends BaseActivity2 implements View.OnClickListener {

    public static final String PLC_BC_ACTION = "cn.net.xinyi.xmjt.plc_bx";
    @BindView(id = R.id.plc_sttn_map, click = true)
    LinearLayout mShowMap;
    @BindView(id = R.id.plc_sttn_zs, click = true)
    LinearLayout plc_sttn_zs;

    UserPlcBxInfo mPlcBxInfo;
    LocaionInfo mLocaionInfo;
    private ArrayAdapter<PoliceBoxModle> mAdapter;
    private List<PoliceBoxModle> list;
    private int networkType;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.aty_plc_bx);
        AnnotateManager.initBindView(this);
        mAdapter = new ArrayAdapter<PoliceBoxModle>(this, R.layout.simple_spinner_dropdown_item_padding);
    }

    private void getUserPlcBxStatus() {
        showLoadding();
        ApiResource.getUserGtZt(getUserInfoJson(), new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int i, Header[] headers, byte[] bytes) {
                stopLoading();
                String result = new String(bytes);
                try {
                    if ("\"-1\"".equals(result)) {
                        toast("获取用户失败，请重新登录");
                    } else {
                        List<UserPlcBxInfo> userPlcBxInfos = JSON.parseArray(result, UserPlcBxInfo.class);
                        if (userPlcBxInfos.size() > 0) {
                            mPlcBxInfo = userPlcBxInfos.get(0);
                            mPlcBxInfo.setLists(JSON.parseArray(mPlcBxInfo.getDATALIST(), PlcBxWorkLog.class));
                        }

                        if (mPlcBxInfo != null) {
                            if (mPlcBxInfo.getZSZT() == 1||mPlcBxInfo.getZSZT() == 5) { //值守中
                                Intent intent2 = new Intent(PLC_BC_ACTION); //发送广播关闭值守监听
                                intent2.putExtra("type", 1);
                                if (mPlcBxInfo.getDATALIST() != null && mPlcBxInfo.getLists().size() > 0) {
                                    XinyiLatLng ll = new XinyiLatLng(mPlcBxInfo.getLists().get(0).getLAT(), mPlcBxInfo.getLists().get(0).getLNGT());
                                    intent2.putExtra("latLngt", ll);
                                    intent2.putExtra("GTID", mPlcBxInfo.getGTID());
                                    intent2.putExtra("GTZSID", mPlcBxInfo.getID());
                                    sendBroadcast(intent2);
                                    getGTData(mPlcBxInfo.getZSZT(),mPlcBxInfo.getID(),mPlcBxInfo.getGTID());
                                }
                            } else if (mPlcBxInfo.getZSZT() == 3||mPlcBxInfo.getZSZT() == 4){ //0,2,3,4 不在值守
                                getGTData(mPlcBxInfo.getZSZT(),mPlcBxInfo.getID(),mPlcBxInfo.getGTID());
                            }else {
                                Intent intent=new Intent(PlcBxAty.this,PlcBxListAty.class);
                                intent.setFlags(1);
                                startActivity(intent);
                            }
                        } else {
                            Intent intent=new Intent(PlcBxAty.this,PlcBxListAty.class);
                            intent.setFlags(1);
                            startActivity(intent);
                        }

                    }
                } catch (JSONException e) {
                    stopLoading();
                    toast("获取值守状态失败");
                }
            }

            @Override
            public void onFailure(int i, Header[] headers, byte[] bytes, Throwable throwable) {
                stopLoading();
                toast("获取值守状态失败");
            }
        });
    }
    /**
     *获取用户当前值守状态的请求json
     *
     * @return
     */
    private String getUserInfoJson() {
        JSONObject requestJson = new JSONObject();
        requestJson.put("YH", getUsername());
        return requestJson.toJSONString();
    }

    private void getGTData(final int ZSZT,final int GTJLID,final int id) {
        JSONObject jo=new JSONObject();
        jo.put("ID",id);
        jo.put("ISDISPLAYDEL",1);
        String json=jo.toJSONString();
        ApiResource.getGtList(json, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int i, Header[] headers, byte[] bytes) {
                stopLoading();
                String result = new String(bytes);
                try {
                    list = JSON.parseArray(result, PoliceBoxModle.class);
                    if (list != null) {
                        for (PoliceBoxModle p:list){
                            Intent intent=new Intent(PlcBxAty.this,PlcBxZSAty.class);
                            intent.putExtra("data",id);
                            intent.putExtra("id",GTJLID);
                            intent.putExtra("zszt",ZSZT);
                            intent.setFlags(1);
                            startActivity(intent);
                        }
                    } else {
                        onFailure(i,headers,bytes,null);
                    }
                } catch (JSONException e) {
                    onFailure(i,headers,bytes,e);
                }
            }

            @Override
            public void onFailure(int i, Header[] headers, byte[] bytes, Throwable throwable) {
                stopLoading();
                toast("获取防控点数据失败");
            }
        });
    }


    void  showDialog(){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        final Spinner sp=new Spinner(this);
        sp.setAdapter(mAdapter);
        sp.setBackgroundColor(getResources().getColor(R.color.white));
        builder.setTitle("请选择值守防控点：").setView(sp)
                .setNegativeButton(R.string.cancel,null)
                .setPositiveButton(R.string.sure, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        Intent intent=new Intent(PlcBxAty.this,PlcBxZSAty.class);
                        intent.putExtra("data",mAdapter.getItem(sp.getSelectedItemPosition()));
                        startActivity(intent);
                    }
                })
                .setCancelable(false).show();
    }

    private void getUserPlcBxList() {
        ApiResource.getGtList(getUserPlcBxListJson(), new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int i, Header[] headers, byte[] bytes) {
                stopLoading();
                String result = new String(bytes);
                try {
                    list = JSON.parseArray(result, PoliceBoxModle.class);
                    if (list != null && list.size()!=0) {
                        mAdapter.clear();
                        mAdapter.addAll(list);
                        showDialog();
                    } else {
                        new AlertDialog.Builder(PlcBxAty.this).setMessage("您当前没有负责值守的防控点").setNegativeButton("确定", null).show();
                    }
                } catch (JSONException e) {
                    toast("获取防控点数据失败");
                }
            }

            @Override
            public void onFailure(int i, Header[] headers, byte[] bytes, Throwable throwable) {
                stopLoading();
                toast("获取防控点数据失败");
            }
        });
    }


    @Override
    public void onClick(View v) {
        //检测是否连接网络
        networkType = ((AppContext) getApplication()).getNetworkType();
        switch (v.getId()) {
            case R.id.plc_sttn_map://岗亭地图
                if (BaseDataUtils.isFastClick()){
                    return;
                }
                if (networkType == 0) {
                    BaseUtil.showDialog(getString(R.string.network_not_connected), PlcBxAty.this);
                    return;
                }
                start(PlcBxMapActivity.class);
                break;
            case R.id.plc_sttn_zs://岗亭值守
                if (BaseDataUtils.isFastClick()){
                    return;
                }
                if (networkType == 0) {
                    BaseUtil.showDialog(getString(R.string.network_not_connected), PlcBxAty.this);
                    return;
                }
                getUserPlcBxStatus();
                break;

        }
    }
    /**
     *获取用户负责的岗亭列表的请求json
     *
     * @return
     */
    private String getUserPlcBxListJson() {
        JSONObject requestJson = new JSONObject();
        requestJson.put("PHONENO", getUsername());
        return requestJson.toJSONString();
    }


    @Override
    public boolean enableBackUpBtn() {
        return true;
    }


    @Override
    public String getAtyTitle() {
        return getString(R.string.plc);
    }




}