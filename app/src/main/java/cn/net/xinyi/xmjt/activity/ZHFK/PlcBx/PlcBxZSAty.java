package cn.net.xinyi.xmjt.activity.ZHFK.PlcBx;


import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONException;
import com.alibaba.fastjson.JSONObject;
import com.baidu.mapapi.model.LatLng;
import com.baidu.mapapi.utils.DistanceUtil;
import com.bigkoo.pickerview.TimePickerView;
import com.loopj.android.http.AsyncHttpResponseHandler;

import org.apache.http.Header;

import java.text.ParseException;
import java.util.Date;
import java.util.List;

import cn.net.xinyi.xmjt.R;
import cn.net.xinyi.xmjt.api.ApiResource;
import cn.net.xinyi.xmjt.config.AppContext;
import cn.net.xinyi.xmjt.config.AppManager;
import cn.net.xinyi.xmjt.config.BaseMapActivity;
import cn.net.xinyi.xmjt.model.LocaionInfo;
import cn.net.xinyi.xmjt.model.PlcBxWorkLog;
import cn.net.xinyi.xmjt.model.PoliceBoxModle;
import cn.net.xinyi.xmjt.model.UserPlcBxInfo;
import cn.net.xinyi.xmjt.model.XinyiLatLng;
import cn.net.xinyi.xmjt.service.BaiduTraceService;
import cn.net.xinyi.xmjt.utils.AnnotateManager;
import cn.net.xinyi.xmjt.utils.BaiduTraceFacade;
import cn.net.xinyi.xmjt.utils.BaseDataUtils;
import cn.net.xinyi.xmjt.utils.BaseUtil;
import cn.net.xinyi.xmjt.utils.BindView;
import cn.net.xinyi.xmjt.utils.DateUtil;
import cn.net.xinyi.xmjt.utils.DialogHelper;
import cn.net.xinyi.xmjt.utils.UI;

/**
 * Created by hao.zhou on 2016/5/9.
 */
public class PlcBxZSAty extends BaseMapActivity implements View.OnClickListener {
    //开始值守
    @BindView(id = R.id.btn_ks,click = true)
    Button btn_ks;
    //结束值守
    @BindView(id = R.id.btn_js,click = true)
    Button btn_js;
    //离岗报备
    @BindView(id = R.id.btn_bb,click = true)
    Button btn_bb;
    //岗亭类别
    @BindView(id = R.id.tv_gtlb)
    TextView tv_gtlb;
    //统一编号
    @BindView(id = R.id.tv_tybh)
    TextView tv_tybh;
    //岗亭地址
    @BindView(id = R.id.tv_dz)
    TextView tv_dz;
    //listview
    @BindView(id = R.id.lv_list)
    ListView lv_list;
    //LinearLayout
    @BindView(id = R.id.ll_empty_data)
    LinearLayout ll_empty_data;
    private LocaionInfo mLocaionInfo;
    public static final String PLC_BC_ACTION = "cn.net.xinyi.xmjt.plc_bx";
    private PoliceBoxModle mPlcBxInfo;
    private int ZSID;
    UserPlcBxInfo mUserInfo;
    private PlcBxZSAdp mAdapter;
    private List<UserPlcBxInfo> userPlcBxInfos;
    private List<PlcBxWorkLog> userPlcBxWork;
    //   private FenceReiver mReceiver;//广播
    private TimePickerView pvTime;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        AnnotateManager.initBindView(this);
        //广播通知擅自离岗
//        IntentFilter filter = new IntentFilter(BaiduTraceService.FENCE_ACTION);
//        mReceiver = new FenceReiver();
//        registerReceiver(mReceiver, filter);
        getPlateData();
        initTimePickerView();
    }
    private void getPlateData() {
        JSONObject jo=new JSONObject();
        jo.put("ID",getIntent().getStringExtra("data")==null?getIntent().getSerializableExtra("data"):getIntent().getStringExtra("data"));
        jo.put("ISDISPLAYDEL",1);
        String json=jo.toJSONString();
        ApiResource.getGtList(json, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int i, Header[] headers, byte[] bytes) {
                stopLoading();
                String result = new String(bytes);
                try {
                    List<PoliceBoxModle>  mPbs = JSON.parseArray(result,PoliceBoxModle.class );
                    if (mPbs != null&&mPbs.size()>0){
                        mPlcBxInfo=mPbs.get(0);
                        setData();
                        setMarker(new LatLng(mPlcBxInfo.getLAT(),mPlcBxInfo.getLNGT()),R.drawable.plc_bx_offline);
                    }
                } catch (JSONException e) {
                    onFailure(i,headers,bytes,e);
                }
            }
            @Override
            public void onFailure(int i, Header[] headers, byte[] bytes, Throwable throwable) {
                stopLoading();
                toast("获取数据失败");
            }
        });
    }

    @Override
    public void setupFooterView(LinearLayout parent) {
        parent.setVisibility(View.VISIBLE);
        View view =LayoutInflater.from(this).inflate(R.layout.aty_plcbx_zs,parent);
        view.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, 0,1.5F));
    }
    private void initTimePickerView() {
        //时间选择器
        pvTime = new TimePickerView(this, TimePickerView.Type.HOURS_MINS);
        pvTime.setTitle("请选择勤务时间");
        pvTime.setTime(DateUtil.string2Date("2016-10-10 08:00:00","yyyy-MM-dd HH:mm:ss"));
        pvTime.setCyclic(false);
        pvTime.setCancelable(true);
        //时间选择后回调
        pvTime.setOnTimeSelectListener(new TimePickerView.OnTimeSelectListener() {

            @Override
            public void onTimeSelect(Date date) {
                if (DateUtil.Date2Minute(date)==0){
                    UI.toast(PlcBxZSAty.this,"勤务时间应该大于0");
                }else {
                    if (getString(R.string.start_working_plc_bx).equals(btn_ks.getText().toString())) {
                     UI.toast(PlcBxZSAty.this,"勤务时间到期,将自动结束当前防控点值守工作！");
                        AppManager.getAppManager().finishActivity(PlcBxListAty.intence);
                        postUpdatePlcBxUserStatus(1, mPlcBxInfo.getID(), 0, mLocaionInfo.getLat(), mLocaionInfo.getLongt(), null, DateUtil.Date2Minute(date));
                    }
                }
            }
        });
    }

    @Override
    public void onClick(View view) {
        //检测是否连接网络
        int  networkType = ((AppContext) getApplication()).getNetworkType();
        if (BaseDataUtils.isFastClick()) {
        }else if (networkType == 0) {
            toast(getString(R.string.network_not_connected));
        } else {
            switch (view.getId()) {
                case R.id.btn_ks:
                    loc();
                    if (getString(R.string.start_working_plc_bx).equals(btn_ks.getText().toString())) {
                        //开始值守
                        if (mLocaionInfo != null && mPlcBxInfo.getLAT() > 0) {
                            if (DistanceUtil.getDistance(new LatLng(mPlcBxInfo.getLAT(), mPlcBxInfo.getLNGT()), new LatLng(mLocaionInfo.lat, mLocaionInfo.longt)) <= 250) {
                                pvTime.show();
                            } else {
                                toast("当前距离岗亭" + formatString.format(DistanceUtil.getDistance(new LatLng(mPlcBxInfo.getLAT(), mPlcBxInfo.getLNGT()), new LatLng(mLocaionInfo.lat, mLocaionInfo.longt))) + "米，请到附近打卡");
                            }
                        } else {
                            if (mLocaionInfo == null) {
                                toast("获取不到当前位置,请确认是否允许定位");
                            } else {
                                toast("当前选择的岗亭没有录入经纬度信息");
                            }
                        }
                    } else {
                        //继续值守
                        if (mLocaionInfo != null && mPlcBxInfo.getLAT() > 0) {
                            if (DistanceUtil.getDistance(new LatLng(mPlcBxInfo.getLAT(), mPlcBxInfo.getLNGT()), new LatLng(mLocaionInfo.lat, mLocaionInfo.longt)) <= 250) {
                                postUpdatePlcBxUserStatus(5, 0, ZSID, mLocaionInfo.getLat(), mLocaionInfo.getLongt(), null, 0);
                            } else {
                                toast("当前位置与岗亭位置距离太远");
                            }
                        } else {
                            if (mLocaionInfo == null) {
                                toast("获取不到当前位置,请确认是否允许定位");
                            } else {
                                toast("当前选择的岗亭没有录入经纬度信息");
                            }
                        }
                    }
                    break;

                case R.id.btn_js:
                    try {
                        if (userPlcBxInfos.get(0).getTOTALTIME()>0&& DateUtil.minuteBetween(DateUtil.date2String(new Date(),"yyyy-MM-dd HH:mm"),userPlcBxInfos.get(0).getSCSJ()) < userPlcBxInfos.get(0).getTOTALTIME()){
                            DialogHelper.showAlertDialog( "当前勤务时间未到，是否结束本次值守？",PlcBxZSAty.this, new DialogHelper.OnOptionClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which, Object o) {
                                    finishDuty();//结束值守
                                }
                            });
                        }else {
                            DialogHelper.showAlertDialog("确定结束本次值守",PlcBxZSAty.this,new DialogHelper.OnOptionClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which, Object o) {
                                    finishDuty();//结束值守
                                }
                            });
                        }
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }
                    break;

                case R.id.btn_bb:
                    if (getString(R.string.start_working_plc_bx).equals(btn_ks.getText().toString())) {
                        toast("请开始值守");
                    } else if (getString(R.string.plc_bx_leaving).equals(btn_bb.getText().toString())) {
                        toast("你当前已在离岗状态");
                    } else {
//                        DialogHelper.showAlertDialog(this, getString(R.string.bbtips), new DialogHelper.OnOptionClickListener() {
//                            @Override
//                            public void onClick(DialogInterface dialog, int which, Object o) {
//
//                            }
//                        });
                    //离岗报备
                    final EditText editText = new EditText(this);
                    editText.setLines(3);
                    editText.setHint("请填写离岗的时间和原因");
                    editText.setTextSize(14);
                    editText.setBackgroundColor(getResources().getColor(R.color.white));
                    new AlertDialog.Builder(this).setTitle("离岗报备").setView(editText).setNegativeButton("取消", null).setPositiveButton("确定", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            if(editText.getText().toString().isEmpty()){
                                toast("请填写离岗的时间和原因");

                            }else {
                                postUpdatePlcBxUserStatus(3, 0, ZSID, mLocaionInfo.getLat(), mLocaionInfo.getLongt(), editText.getText().toString(),0);
                            }
                        }
                    }).show();
                    }
                    break;
            }
        }
    }
    //结束值守
    private void finishDuty() {
        if (mPlcBxInfo != null) {
            if (mLocaionInfo != null && mPlcBxInfo.getLAT() > 0) {
                postUpdatePlcBxUserStatus(2, 0, ZSID, mLocaionInfo.getLat(), mLocaionInfo.getLongt(), null,0);
            } else {
                if (mLocaionInfo == null) {
                    toast("获取不到当前位置,请确认是否允许定位");
                } else {
                    toast("当前选择的岗亭没有录入经纬度信息");
                }
            }
        } else {
            toast("结束值守失败");
            getUserPlcBxStatus();
        }
    }

    private void postUpdatePlcBxUserStatus(final int lx, int GTID, int GTZSID, double LAT, double LNGT, String ms,int totalTime) {
        showLoadding();
        ApiResource.postUpdatePlcBxUserStatus(getWorkStatusJson(lx, GTID, GTZSID, LAT, LNGT, ms,totalTime), new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int i, Header[] headers, byte[] bytes) {
                stopLoading();
                String result = new String(bytes);
                if ("true".equals(result)) {
                    switch (lx) {
                        case 1:
                            getUserPlcBxStatus();
                            setVisibleOrGone(false,true,true,R.string.contin_work_plc_bx);
                            //   BaiduTraceFacade.createGTSimpleFence(getUsername(), mPlcBxInfo.getID() + "",mPlcBxInfo.getLNGT(), mPlcBxInfo.getLAT());
                            break;
                        case 2:
                            BaiduTraceService.stopPlcBxCheck(getApplicationContext());
                            BaiduTraceFacade.clearGTFence(getUsername());//清除鹰眼记录
                            setVisibleOrGone(true,false,true,R.string.start_working_plc_bx);
                            PlcBxZSAty.this.finish();
                            break;
                        case 3:
                            getUserPlcBxStatus();
                            //  BaiduTraceService.stopPlcBxCheck(getApplicationContext());
                            setVisibleOrGone(true,true,false,R.string.contin_work_plc_bx);
                            break;
                        case 4:
                            BaseUtil.showDialog("您当前为值守状态未报备离开岗亭所在区域,判定为擅自离岗！",PlcBxZSAty.this);
                            getUserPlcBxStatus();
                            // BaiduTraceService.stopPlcBxCheck(getApplicationContext());
                            setVisibleOrGone(true,true,true,R.string.contin_work_plc_bx);
                            break;
                        case 5:
                            getUserPlcBxStatus();
                            setVisibleOrGone(false,true,true,R.string.contin_work_plc_bx);
                            break;
                    }
                } else {
                    switch (lx) {
                        case 1:
                            toast("开始值守失败");
                            break;
                        case 2:
                            toast("结束值守失败");
                            break;
                        case 3:
                            toast("离岗报备失败");
                            break;
                        case 4:
                            toast("您当前为值守状态未报备离开岗亭所在区域！");
                            break;
                        case 5:
                            toast("继续值守失败");
                            break;
                    }
                }
            }

            @Override
            public void onFailure(int i, Header[] headers, byte[] bytes, Throwable throwable) {
                stopLoading();
                toast("离岗失败"+i);
            }
        });
    }

    /**
     *更新岗亭状态
     *
     * @return
     */
    private String getWorkStatusJson(int LX, int GTID, int GTZSID, double LAT, double LNGT, String MS,int totalTime) {
        JSONObject requestJson = new JSONObject();

        requestJson.put("LX", LX + "");
        requestJson.put("GTID", GTID + "");
        requestJson.put("DW", AppContext.instance().getLoginInfo().getPcs());
        if (LX != 1) {
            requestJson.put("GTZSID", GTZSID + "");
        }
        requestJson.put("YH", getUsername());
        requestJson.put("LAT", LAT + "");
        requestJson.put("LNGT", LNGT + "");
        requestJson.put("LOCTYPE", 161 + "");
        if (!TextUtils.isEmpty(MS)) {
            requestJson.put("MS", MS);
        }
        if (totalTime >0) {
            requestJson.put("TOTALTIME",totalTime);
        }
        return requestJson.toJSONString();
    }


    private void getUserPlcBxStatus() {
        ApiResource.getUserGtZt(getUserInfoJson(), new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int i, Header[] headers, byte[] bytes) {
                stopLoading();
                String result = new String(bytes);
                try {
                    if ("\"-1\"".equals(result)) {
                        toast("获取用户失败，请重新登录");
                    } else {
                        userPlcBxInfos = JSON.parseArray(result, UserPlcBxInfo.class);
                        if (userPlcBxInfos.size() > 0) {
                            lv_list.setVisibility(View.VISIBLE);
                            ll_empty_data.setVisibility(View.GONE);
                            mUserInfo = userPlcBxInfos.get(0);
                            userPlcBxWork=JSON.parseArray(mUserInfo.getDATALIST(), PlcBxWorkLog.class);
                            for (PlcBxWorkLog p:userPlcBxWork){
                                ZSID=p.getGTZSID();
                            }

                            try {
                                if (mUserInfo.getTOTALTIME()>0&& DateUtil.minuteBetween(DateUtil.date2String(new Date(),"yyyy-MM-dd HH:mm"),mUserInfo.getSCSJ()) >userPlcBxInfos.get(0).getTOTALTIME()){
                                    DialogHelper.showAlertDialog(PlcBxZSAty.this, "当前勤务时间已到，自动结束当前岗亭值守！", new DialogHelper.OnOptionClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which, Object o) {
                                            finishDuty();//结束值守
                                        }
                                    });
                                }else {
                                    if (mUserInfo.getZSZT() == 1||mUserInfo.getZSZT() == 5) { //值守中 //开启岗亭值守围栏
                                        XinyiLatLng ll = new XinyiLatLng(userPlcBxWork.get(0).getLAT(), userPlcBxWork.get(0).getLNGT());
                                        BaiduTraceService.startPlcBxCheck(getApplicationContext(),ll, mUserInfo.getGTID(),mUserInfo.getID());
                                        if (mLocaionInfo != null &&mPlcBxInfo.getLAT() > 0 ) {//值守的状态，如果当前的距离和岗亭超过250米结束岗亭值守
                                            if (DistanceUtil.getDistance(new LatLng(mPlcBxInfo.getLAT(),mPlcBxInfo.getLNGT()), new LatLng(mLocaionInfo.lat, mLocaionInfo.longt)) > 230) {
                                                postUpdatePlcBxUserStatus(4, 0,ZSID, mLocaionInfo.getLat(), mLocaionInfo.getLongt(), null,0);
                                            }
                                        }
                                    }
                                    mAdapter=new PlcBxZSAdp(lv_list,userPlcBxWork,R.layout.aty_dutyhistory_item_adp, PlcBxZSAty.this);
                                    lv_list.setAdapter(mAdapter);
                                }
                            } catch (ParseException e) {
                                e.printStackTrace();
                            }
                        }else {
                            lv_list.setVisibility(View.GONE);
                            ll_empty_data.setVisibility(View.VISIBLE);
                        }
                    }
                } catch (JSONException e) {
                    onFailure(i,headers,bytes,null);
                }
            }

            @Override
            public void onFailure(int i, Header[] headers, byte[] bytes, Throwable throwable) {
                stopLoading();
                toast("获取数据失败");
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

    private void setData() {
        tv_gtlb.setText(mPlcBxInfo.getTYPE());
        tv_tybh.setText(mPlcBxInfo.getUNIFIEDNO());
        tv_dz.setText(mPlcBxInfo.getADDRESS());
        ll_empty_data.setVisibility(View.VISIBLE);
        //获取值守状态  1为值守中  5为离岗报备回来值守状态
        if (getIntent().getFlags()==1){
            getUserPlcBxStatus();
            ZSID= (int) getIntent().getSerializableExtra("id");
            if (getIntent().getSerializableExtra("zszt").equals(1)
                    ||getIntent().getSerializableExtra("zszt").equals(5)){
                setVisibleOrGone(false,true,true,R.string.contin_work_plc_bx);
                //3为ligang
            }else if (getIntent().getSerializableExtra("zszt").equals(3)){
                setVisibleOrGone(true,true,false,R.string.contin_work_plc_bx);
            }else if (getIntent().getSerializableExtra("zszt").equals(4)){
                setVisibleOrGone(true,true,true,R.string.contin_work_plc_bx);
            }
        }
    }

    private void setVisibleOrGone(boolean ks, boolean js, boolean bb, int tks) {
        btn_ks.setText(tks);
        btn_ks.setVisibility(ks==true?View.VISIBLE:View.GONE);
        btn_js.setVisibility(js==true?View.VISIBLE:View.GONE);
        btn_bb.setVisibility(bb==true?View.VISIBLE:View.GONE);
    }

    @Override
    public void onReceiveLoc(LocaionInfo location, boolean isSuccess, Throwable errMsg) {
        if (isSuccess) {
            super.onReceiveLoc(location, isSuccess, errMsg);
            mLocaionInfo = location;
        }
    }

    @Override
    public boolean enableBackUpBtn() {
        return true;
    }


    @Override
    public String getAtyTitle() {
        return getString(R.string.plc_sttn_zs);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        MenuItem action_list = menu.findItem(R.id.action_list);
        MenuItem action_map = menu.findItem(R.id.action_map);
        action_map.setVisible(false);
        return true;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                this.finish();
                break;

            case R.id.action_list:
                start(PlcBxHistoryAty.class);
                break;

            default:
                break;
        }
        return true;
    }

    @Override
    public boolean needKeepLoc() {
        return true;
    }

//
//    class FenceReiver extends BroadcastReceiver {
//        @Override
//        public void onReceive(Context context, Intent intent) {
//            String msg = intent.getStringExtra("data");
//            int type = intent.getIntExtra("type", 0);
//            //  UI.toast(PlcBxZSAty.this,"plcbxzs"+type);
//            switch (type) {
//                case FENCE_ALARM:
//                    try {
//                        JSONObject js = JSON.parseObject(msg);
//                        //   UI.toast(PlcBxZSAty.this,"plcbxzs"+js.toString()+"值守状态"+mUserInfo.getZSZT());
//                        if (null != js) {
//                            if (null!=mUserInfo&&mUserInfo.getZSZT() != 3&&js.getIntValue("action") == 2) {
//                                postUpdatePlcBxUserStatus(4, 0,ZSID, mLocaionInfo.getLat(), mLocaionInfo.getLongt(), null);
//                            }
//                        }
//                    } catch (JSONException e) {
//                        e.printStackTrace();
//                    }
//                    break;
//            }
//        }
//   }

}