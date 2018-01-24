package cn.net.xinyi.xmjt.activity.ZHFK.Duty;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONException;
import com.alibaba.fastjson.JSONObject;
import com.loopj.android.http.AsyncHttpResponseHandler;

import org.apache.http.Header;

import cn.net.xinyi.xmjt.R;
import cn.net.xinyi.xmjt.api.ApiResource;
import cn.net.xinyi.xmjt.config.AppContext;
import cn.net.xinyi.xmjt.config.BaseActivity2;
import cn.net.xinyi.xmjt.model.DutyOperationModle;
import cn.net.xinyi.xmjt.utils.BaseDataUtils;
import cn.net.xinyi.xmjt.utils.BaseUtil;


public class DutyMainMenuAty extends BaseActivity2 implements View.OnClickListener{
    protected static DutyMainMenuAty intence;
    private DutyOperationModle operInfos; //巡逻状态
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.aty_duty_main_menu);
        intence=this;
        findViewById(R.id.ll_xl).setOnClickListener(this);
        findViewById(R.id.ll_cj).setOnClickListener(this);
        findViewById(R.id.ll_xz).setOnClickListener(this);
        if (BaseDataUtils.isPOLICE()) {
            findViewById(R.id.ll_xz).setVisibility(View.VISIBLE);
        }
    }



    @Override
    public boolean enableBackUpBtn() {
        return true;
    }
    @Override
    public String getAtyTitle() {
        return getString(R.string.xl_tips);
    }

    @Override
    public void onClick(View v) {
        //检测是否连接网络
        int  networkType = ((AppContext) getApplication()).getNetworkType();
        switch (v.getId()){
            case R.id.ll_xl://巡逻
                if (networkType == 0) {
                    BaseUtil.showDialog(getString(R.string.network_not_connected), DutyMainMenuAty.this);
                } else {
                    //  showActivity(DutyBeatsListAty.class);
                    getUserXlStatus(false);
                }
                break;

            case R.id.ll_cj://处警
                if (networkType == 0) {
                    BaseUtil.showDialog(getString(R.string.network_not_connected), DutyMainMenuAty.this);
                } else {
                    // showActivity(DutyPoliceAty.class);
                    getUserXlStatus(true);
                }
                break;
            case R.id.ll_xz://我是巡长
                if (networkType == 0) {
                    BaseUtil.showDialog(getString(R.string.network_not_connected), DutyMainMenuAty.this);
                } else {
                    getXLXZZT();//获得巡逻巡长状态
                }
                break;

        }
    }

    private void getXLXZZT() {
        showLoadding();
        JSONObject jo=new JSONObject();
        jo.put("SSPCS",userInfo.getOrgancode());//派出所编码
        String json=jo.toJSONString();
        ApiResource.getXLXZZT(json, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int i, Header[] headers, byte[] bytes) {
                String result =new String(bytes);
                if (result.length() > 2){
                    JSONArray jo=JSON.parseArray(result);
                    if (((JSONObject)jo.get(0)).getString("XZHM").toString().equals(userInfo.getUsername())
                            && ((JSONObject)jo.get(0)).getString("XZXM").equals(userInfo.getName())){
                        Intent intent=new Intent(DutyMainMenuAty.this,DutyLeaderAty.class);
                        intent.putExtra("ID",((JSONObject)jo.get(0)).getString("ID"));
                        startActivity(intent);
                    }else {
                        setXZTips(((JSONObject)jo.get(0)).getString("XZXM"),((JSONObject)jo.get(0)).getString("XZHM"));
                    }
                }else {
                    getData();
                }
            }
            @Override
            public void onFailure(int i, Header[] headers, byte[] bytes, Throwable throwable) {
                toast("上传失败！");
                stopLoading();
            }
        });
    }

    private void setXZTips(String xzxm, String xzhm) {//获取巡逻长状态提示
        new AlertDialog.Builder(this)
                .setTitle(R.string.tips)
                .setMessage("当前值班巡长是"+xzxm+",联系电话"+xzhm+",您是否确认接班？")
                .setPositiveButton(R.string.sure, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        getData();
                    }
                })
                .setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        stopLoading();
                    }
                })
                .setCancelable(false).show();
    }

    private void getData() {
        new AlertDialog.Builder(this)
                .setTitle(R.string.tips)
                .setMessage(R.string.xl_xzmessage)
                .setPositiveButton(R.string.sure, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        uploadData();
                    }
                })
                .setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        stopLoading();
                    }
                })
                .setCancelable(false).show();
    }

    private void uploadData() {//上传巡逻长字段信息
        JSONObject jo=new JSONObject();
        jo.put("XZZT",1);//巡长状态 1 巡长 0不是巡长
        jo.put("XZHM",userInfo.getUsername());//巡长号码
        jo.put("XZXM",userInfo.getName());//巡长号码
        jo.put("SSPCS",userInfo.getOrgancode());//所属派出所编码
        String json=jo.toJSONString();
        ApiResource.uploadXZInfo(json, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int i, Header[] headers, byte[] bytes) {
                String result =new String(bytes);
                JSONObject jo=JSON.parseObject(result);
                int IDs=Integer.parseInt(jo.get("ID").toString());
                if (i == 200 && IDs > 0){
                    stopLoading();
                    Intent intent=new Intent(DutyMainMenuAty.this,DutyLeaderAty.class);
                    intent.putExtra("ID",jo.get("ID").toString());
                    startActivity(intent);
                }else {
                    onFailure(i,headers,bytes,null);
                }
            }
            @Override
            public void onFailure(int i, Header[] headers, byte[] bytes, Throwable throwable) {
                toast("上传失败！");
                stopLoading();
            }
        });
    }


    /**
     * 获取用户巡逻状态
     */
    protected void getUserXlStatus(final boolean dutyType) {
        showLoadding();
        if (AppContext.instance().getLoginInfo() != null) {
            ApiResource.getLastDutyOperation(userInfo.getUsername(), new AsyncHttpResponseHandler() {
                @Override
                public void onSuccess(int i, Header[] headers, byte[] bytes) {
                    String result = new String(bytes);
                    if (i == 200 && result.length() > 4) {//
                        stopLoading();
                        operInfos = JSON.parseObject(result, DutyOperationModle.class);
                        try {
                            if (dutyType) {//点击处警
                                if (DutyStartAty.TYPE_BEATS_END.equals(operInfos.getLAST_TYPE())) {
                                    showActivity(DutyPoliceAty.class);//处警
                                } else {
                                    Intent intent = new Intent(DutyMainMenuAty.this, DutyPoliceAty.class);
                                    intent.putExtra("data", operInfos.getID());
                                    startActivity(intent);
                                }
                            } else {//点击巡逻
                                if (DutyPoliceAty.TYPE_POLICE_START_A.equals(operInfos.getLAST_TYPE())) {
                                    new AlertDialog.Builder(DutyMainMenuAty.this)
                                            .setTitle(R.string.tips)
                                            .setMessage("您当前正在处警中，需结束才能进行巡逻！")
                                            .setPositiveButton(R.string.sure, new DialogInterface.OnClickListener() {
                                                @Override
                                                public void onClick(DialogInterface dialogInterface, int i) {
                                                    Intent intent = new Intent(DutyMainMenuAty.this, DutyPoliceAty.class);
                                                    intent.putExtra("data", operInfos.getID());
                                                    startActivity(intent);
                                                }
                                            })
                                            .setNegativeButton(R.string.cancel, null)
                                            .setCancelable(false).show();
                                } else if (DutyStartAty.TYPE_BEATS_END.equals(operInfos.getLAST_TYPE())
                                        || (!DutyStartAty.TYPE_BEATS_START.equals(operInfos.getDUTY_OPR_TYPE())
                                        && DutyPoliceAty.TYPE_POLICE_END.equals(operInfos.getLAST_TYPE()))) {
                                    showActivity(DutyBeatsListAty.class);//巡段列表
                                } else {
                                    Intent intent = new Intent(DutyMainMenuAty.this, DutyStartAty.class);
                                    intent.putExtra("data", operInfos.getDUTY_BEATS_ID());
                                    startActivity(intent);
                                }
                            }
                        } catch (JSONException e) {
                            onFailure(i, headers, bytes, e);
                        }
                    } else {//没有开始巡逻或处警
                        if (dutyType) {
                            showActivity(DutyPoliceAty.class);//处警
                        }else {
                            showActivity(DutyBeatsListAty.class);//巡段列表
                        }
                    }
                }
                @Override
                public void onFailure(int i, Header[] headers, byte[] bytes, Throwable throwable) {
                    stopLoading();
                    toast("获取数据失败");
                }
            });
        }
    }
}
