package cn.net.xinyi.xmjt.activity.ZHFK.Duty;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AbsListView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONException;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.TypeReference;
import com.loopj.android.http.AsyncHttpResponseHandler;

import org.apache.http.Header;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Collection;
import java.util.Date;
import java.util.List;

import cn.net.xinyi.xmjt.R;
import cn.net.xinyi.xmjt.activity.ZHFK.PlcBx.PickupMapActivity;
import cn.net.xinyi.xmjt.api.ApiResource;
import cn.net.xinyi.xmjt.config.AdapterHolder;
import cn.net.xinyi.xmjt.config.AppContext;
import cn.net.xinyi.xmjt.config.BaseListAdp;
import cn.net.xinyi.xmjt.config.BaseWithLocActivity;
import cn.net.xinyi.xmjt.model.DutyOperationModle;
import cn.net.xinyi.xmjt.model.LocaionInfo;
import cn.net.xinyi.xmjt.utils.AnnotateManager;
import cn.net.xinyi.xmjt.utils.BaiduMapUtil;
import cn.net.xinyi.xmjt.utils.BaiduTraceFacade;
import cn.net.xinyi.xmjt.utils.BaseDataUtils;
import cn.net.xinyi.xmjt.utils.BindView;
import cn.net.xinyi.xmjt.utils.DateUtil;
import cn.net.xinyi.xmjt.utils.XinyiLog;


/**
 * Created by hao.zhou on 2016/4/7.
 */
public class DutyPoliceAty extends BaseWithLocActivity implements View.OnClickListener {
    /**
     * 年-月-日
     **/
    @BindView(id = R.id.tv_year, click = true)
    private TextView tv_year;
    /**
     * 时-分-秒
     **/
    @BindView(id = R.id.tv_date, click = true)
    private TextView tv_date;
    /**
     * 星期
     **/
    @BindView(id = R.id.tv_week, click = true)
    private TextView tv_week;
    /**
     * 地址
     **/
    @BindView(id = R.id.et_dz, click = true)
    private EditText et_dz;
    /**
     * 坐标
     **/
    @BindView(id = R.id.tv_zb, click = true)
    private TextView tv_zb;
    /**
     * 手动定位
     **/
    @BindView(id = R.id.tv_sddw, click = true)
    private TextView tv_sddw;

    /**
     * 描述
     **/
    @BindView(id = R.id.et_ms, click = true)
    private EditText et_ms;
    /**
     * 处警
     **/
    @BindView(id = R.id.btn_polic_start, click = true)
    private Button btn_polic_start;

    /**
     * 布局
     **/
    @BindView(id = R.id.ll_all)
    private LinearLayout ll_all;

    /**
     * 记录
     **/
    @BindView(id = R.id.xlcj_record)
    private ListView xlcj_record;

    private Thread myThread = null;
    /**
     * 百度坐标纬度
     **/
    private double bWd;
    /**
     * 百度坐标经度
     **/
    private double bJD;

    private LocaionInfo mLocation;

    private boolean isStarted = false;
    BaseListAdp mAdapter;

    public final static String TYPE_POLICE_START = "04";//处警开始
    public final static String TYPE_POLICE_START_A = "04A";//处警开始
    public final static String TYPE_POLICE_END = "04B";//处警结束
    private String duty_parent_id;
    private String LAST_TYPE;
    private List<DutyOperationModle> operationInfos;
    private String distance;
    private String LAST_ID;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.aty_duty_polic);
        /***注解式绑定控件**/
        AnnotateManager.initBindView(this);
        initData();
        getDutyPoliceType();
    }

    private void initData() {
        duty_parent_id=getIntent().getStringExtra("data");
        /**当前系统时间**/
        long time = System.currentTimeMillis();
        Date date = new Date(time);
        //年-月-日
        format = new SimpleDateFormat("yyyy年MM月dd日");
        tv_year.setText(format.format(date));
        //星期*
        format = new SimpleDateFormat("EEEE");
        tv_week.setText(format.format(date));
        Runnable runnable = new CountDownRunner();
        myThread = new Thread(runnable);
        myThread.start();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_sddw:
                if (BaseDataUtils.isFastClick()) {

                }else if (((AppContext) getApplication()).getNetworkType() == 0) {
                    toast(getString(R.string.network_not_connected));
                } else {Intent intent = new Intent(this, PickupMapActivity.class);
                    startActivityForResult(intent, PickupMapActivity.MAP_PICK_UP);}
                break;

            /**开始巡逻*/
            case R.id.btn_polic_start:
                if (BaseDataUtils.isFastClick()) {

                }else if (((AppContext) getApplication()).getNetworkType() == 0) {
                    toast(getString(R.string.network_not_connected));
                }else if (checkFormVaild()) {
                    if (isStarted) {
                        //结束
                        updateXLStatus(TYPE_POLICE_END);
                    } else {
                        uploadInfo(TYPE_POLICE_START);
                    }
                }
                break;
        }
    }

    /**
     * 检查是否满足处警或结束要求
     *
     * @return
     */
    private boolean checkFormVaild() {
        if (TextUtils.isEmpty(et_dz.getText().toString()) || bWd <= 0 && bJD <= 0) {
            showToast("经纬度不能为空");
            return false;
        }
        return true;
    }


    private void updateXLStatus(final String status) {
        if (DateUtil.string2Date(operationInfos.get(operationInfos.size()-1).getCREATETIME(), "yyyy-MM-dd HH:mm:ss") != null) { //处于处警状态且处警时间不为空
            showLoadding();
            Date startDate = DateUtil.string2Date(operationInfos.get(operationInfos.size()-1).getCREATETIME(), "yyyy-MM-dd HH:mm:ss");
            XinyiLog.d(startDate.getTime() + "times" + operationInfos.get(operationInfos.size()-1).getCREATETIME());
            ApiResource.getTraceHistroy(AppContext.instance().getLoginInfo().getUsername(), startDate.getTime() / 1000, new Date().getTime() / 1000, new AsyncHttpResponseHandler() {
                @Override
                public void onSuccess(int i, Header[] headers, byte[] bytes) {
                    String result = new String(bytes);
                    XinyiLog.d(result);
                    if (BaiduMapUtil.isOptionSuceess(result)) {
                        try {
                            JSONObject jsonObject = JSON.parseObject(result);
                            DecimalFormat format = new DecimalFormat("##0.00");//保留2位小数
                            distance=Double.parseDouble(format.format(jsonObject.getDouble("distance"))) + "";

                        } catch (JSONException e) {

                        } finally {
                            stopLoading();
                            updateData(status);
                        }

                    }else{
                        onFailure(i,headers,bytes,null);
                    }

                }

                @Override
                public void onFailure(int i, Header[] headers, byte[] bytes, Throwable throwable) {
                    stopLoading();
                    updateData(status);
                }
            });
        } else {
            stopLoading();
            updateData(status);
        }
    }


    /** 巡逻结束、 报备结束、处警结束  执行对数据的更新操作
     * */
    public void updateData(String dutyType) {
        showLoadding();
        JSONObject jo = new JSONObject();
        jo.put("DUTY_OPR_TYPE", dutyType.substring(0,2));
        jo.put("PARENT_ID",duty_parent_id);
        jo.put("IS_CJLX", operationInfos.get(operationInfos.size()-1).getPARENT_ID()==null ? 1:null);//为1的时候为
        jo.put("ID", LAST_ID);
        jo.put("DISTANCE", distance);
        String json = jo.toJSONString();
        ApiResource.updateDutyOpraation(json, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int i, Header[] headers, byte[] bytes) {
                String result = new String(bytes);
                if (!result.isEmpty() && result.equals("true") ) {
                    stopLoading();
                    Intent refreshStateIntent = new Intent(DutyStartAty.XL_STATUS_ACTION); //通知MainQW界面更新状态
                    sendBroadcast(refreshStateIntent);
                    // BaiduTraceService.stopXl(getApplicationContext());//停止鹰眼轨迹服务
                    showToast(getString(R.string.cj_end));//提示处警结束
                    //  showActivity(XLListAty.class);//跳转巡逻记录历史界面
                    DutyPoliceAty.this.finish();//巡逻界面关闭
                } else {
                    onFailure(i, headers, bytes, null);
                }
            }

            @Override
            public void onFailure(int i, Header[] headers, byte[] bytes, Throwable throwable) {
                if (bytes != null) {
                    stopLoading();
                    showToast("打卡失败，请稍候重试！");
                }
            }
        });
    }


    //异步上传采集数据到服务端
    public void uploadInfo(final String typePoliceStart) {
        showLoadding();
        JSONObject jo =new JSONObject();
        jo.put("PARENT_ID",duty_parent_id);
        jo.put("DUTY_OPR_TYPE",typePoliceStart);
        jo.put("DUTY_OPR_NAME", BaseDataUtils.dutyNumToDutyType(typePoliceStart));
        jo.put("LON",bJD);
        jo.put("LAT",bWd);
        jo.put("ADDRESS",et_dz.getText().toString());
        jo.put("USERID", userInfo.getId());
        jo.put("SHOWNAME", userInfo.getName());
        jo.put("TEL_NUMBER", userInfo.getUsername());
        String json = jo.toJSONString();
        ApiResource.addDutyOpraation(json, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int i, Header[] headers, byte[] bytes) {
                String result = new String(bytes);
                if (!result.isEmpty() && result.length() >4 ) {
                    Intent startInt = new Intent(DutyStartAty.XL_STATUS_ACTION);
                    sendBroadcast(startInt); //发送广播通知结束
                    if (TYPE_POLICE_START.equals(typePoliceStart)) {
                        getDutyPoliceType();
                        isStarted = true;
                        btn_polic_start.setText("结束处警");
                        BaiduTraceFacade.start(AppContext.instance().getLoginInfo().getUsername(), getApplicationContext());
                    }
                } else {
                    onFailure(i,headers,bytes,null);
                }
            }
            @Override
            public void onFailure(int i, Header[] headers, byte[] bytes, Throwable throwable) {
                stopLoading();
                if (TYPE_POLICE_START.equals(typePoliceStart)) {
                    toast("处警失败");
                } else if (TYPE_POLICE_END.equals(typePoliceStart)) {
                    toast("结束失败");
                }
            }
        });
    }

    /**
     * 返回拍照
     */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (PickupMapActivity.MAP_PICK_UP == requestCode) { //地图选点
            if (Activity.RESULT_OK == resultCode) {
                LocaionInfo info = (LocaionInfo) (data.getBundleExtra("data").get("data"));
                onReceiveLoc(info, true, null);
            }
        }
    }


    public void doWork() {
        runOnUiThread(new Runnable() {
            public void run() {
                try {
                    Date dt = new Date();
                    int hours = dt.getHours();
                    int minutes = dt.getMinutes();
                    int seconds = dt.getSeconds();
                    String curTime = (hours >= 10 ? hours : "0" + hours) + ":" + (minutes >= 10 ? minutes : "0" + minutes) + ":" + (seconds >= 10 ? seconds : "0" + seconds);
                    tv_date.setText(curTime);
                } catch (Exception e) {
                }
            }
        });
    }


    class CountDownRunner implements Runnable {
        // @Override
        public void run() {
            while (!Thread.currentThread().isInterrupted()) {
                try {
                    doWork();
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                } catch (Exception e) {
                }
            }
        }
    }

    @Override
    public void onReceiveLoc(LocaionInfo location, boolean isSuccess, Throwable errMsg) {
        if (isSuccess) {
            mLocation = location;
            /**获得纬度信息**/
            bWd = mLocation.getLat();
            /**获得经度信息信息**/
            bJD = mLocation.getLongt();
            /**获得地址信息**/
            et_dz.setText(mLocation.getAddress());
            /**百度坐标，经纬度展现**/
            tv_zb.setText("(" + bWd + "," + bJD + ")");
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        MenuItem action_map = menu.findItem(R.id.action_map);
        action_map.setIcon(R.drawable.ic_menu_add);
        return true;
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                this.finish();
                break;
            case R.id.action_list://获取处警历史列表
                showActivity(DutyPoliceHistoryAty.class);
                break;

            default:
                break;
        }
        return true;
    }



    //获得打卡数据  显示在ListView上
    private void getDutyPoliceType() {
        showLoadding("正在获取处警状态...",DutyPoliceAty.this);
        JSONObject jo = new JSONObject();
        jo.put("TEL_NUMBER", userInfo.getUsername());
        jo.put("DUTY_OPR_TYPE", "04");
        ApiResource.getLastDutyPoliceByUser(jo.toJSONString(), new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int i, Header[] headers, byte[] bytes) {
                try {
                    if (i == 200 && bytes != null) {
                        JSONArray js = JSON.parseArray(new String(bytes));
                        if (js.size()>0){
                            LAST_TYPE = ((JSONObject) js.get(0)).getString("LAST_TYPE"); //获取当前最新状态
                            //巡逻记录数据 在listview显示
                            operationInfos = JSON.parseObject(((JSONObject) js.get(0)).getString("DATALIST"), new TypeReference<List<DutyOperationModle>>() {
                            });
                        }

                        if (TYPE_POLICE_START_A.equals(LAST_TYPE)){
                            isStarted=true;
                            if (operationInfos != null &&operationInfos.size()>0){
                                LAST_ID=operationInfos.get(operationInfos.size()-1).getID();
                                mAdapter = new XlCjAdapter(xlcj_record, operationInfos, R.layout.aty_dutyhistory_item_adp, DutyPoliceAty.this);
                                xlcj_record.setAdapter(mAdapter);
                                btn_polic_start.setText("结束处警");
                                stopLoading();
                                BaiduTraceFacade.start(AppContext.instance().getLoginInfo().getUsername(), getApplicationContext());
                            }
                        }
                        stopLoading();
                    } else {
                        onFailure(i, headers, bytes, null);
                    }
                } catch (Exception e) {
                    onFailure(i, headers, bytes, null);
                }
            }

            @Override
            public void onFailure(int i, Header[] headers, byte[] bytes, Throwable throwable) {
                stopLoading();
            }
        });
    }


    class XlCjAdapter extends BaseListAdp<DutyOperationModle> {

        public XlCjAdapter(AbsListView view, Collection<DutyOperationModle> datas, int itemLayoutId, Context context) {
            super(view, datas, itemLayoutId, context);
        }

        @Override
        public void convert(AdapterHolder helper, DutyOperationModle item, boolean isScrolling) {
            String status = "";
            if (LAST_TYPE.equals(TYPE_POLICE_START_A)) {
                helper.setText(R.id.tv_xldmc, item.getDUTY_OPR_NAME());
                helper.getConvertView().findViewById(R.id.tv_xldmc).setVisibility(View.VISIBLE);
                status = "处警中";
            } else {
                helper.setText(R.id.tv_xldmc, "");
                helper.getConvertView().findViewById(R.id.tv_xldmc).setVisibility(View.GONE);

            }

            if (LAST_TYPE.equals(TYPE_POLICE_END)) {
                status = "处警结束";
                helper.getConvertView().findViewById(R.id.tv_lx)
                        .setBackgroundColor(getResources().getColor(R.color.bbutton_danger));
            }

            helper.setText(R.id.tv_lx, status);
            helper.setText(R.id.tv_dz, item.getADDRESS());
            helper.setText(R.id.tv_year, item.getCREATETIME());
        }
    }


    @Override
    public boolean enableBackUpBtn() {
        return true;
    }

    @Override
    public String getAtyTitle() {
        return getString(R.string.duty_police);
    }

}