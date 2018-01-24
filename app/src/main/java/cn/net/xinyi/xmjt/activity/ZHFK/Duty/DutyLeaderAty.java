package cn.net.xinyi.xmjt.activity.ZHFK.Duty;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.loopj.android.http.AsyncHttpResponseHandler;

import org.apache.http.Header;

import java.text.SimpleDateFormat;
import java.util.Date;

import cn.net.xinyi.xmjt.R;
import cn.net.xinyi.xmjt.activity.ZHFK.PlcBx.PickupMapActivity;
import cn.net.xinyi.xmjt.api.ApiResource;
import cn.net.xinyi.xmjt.config.AppContext;
import cn.net.xinyi.xmjt.config.BaseWithLocActivity;
import cn.net.xinyi.xmjt.model.LocaionInfo;
import cn.net.xinyi.xmjt.utils.AnnotateManager;
import cn.net.xinyi.xmjt.utils.BaiduTraceFacade;
import cn.net.xinyi.xmjt.utils.BaseDataUtils;
import cn.net.xinyi.xmjt.utils.BindView;


/**
 * Created by hao.zhou on 2016/4/7.
 */
public class DutyLeaderAty extends BaseWithLocActivity implements View.OnClickListener {
    /**
     * 年-月-日
     **/
    @BindView(id = R.id.tv_year)
    private TextView tv_year;
    /**
     * 时-分-秒
     **/
    @BindView(id = R.id.tv_date)
    private TextView tv_date;
    /**
     * 星期
     **/
    @BindView(id = R.id.tv_week)
    private TextView tv_week;
    /**
     * 地址
     **/
    @BindView(id = R.id.et_dz)
    private EditText et_dz;
    /**
     * 坐标
     **/
    @BindView(id = R.id.tv_zb)
    private TextView tv_zb;
    /**
     * 坐标
     **/
    @BindView(id = R.id.tv_sj)
    private TextView tv_sj;
    /**
     * 坐标
     **/
    @BindView(id = R.id.tv_dz)
    private TextView tv_dz;
    /**
     * 检查时间
     **/
    @BindView(id = R.id.tv_jcsj)
    private TextView tv_jcsj;
    /**
     * 刷新
     **/
    @BindView(id = R.id.tv_sddw, click = true)
    private TextView tv_sddw;
    /**
     * 开始检查
     **/
    @BindView(id = R.id.btn_ksjc, click = true)
    private Button btn_ksjc;
    /**
     * 结束检查
     **/
    @BindView(id = R.id.btn_jsjc, click = true)
    private Button btn_jsjc;

    /**
     * 布局
     **/
    @BindView(id = R.id.ll_all)
    private LinearLayout ll_all;
    /**
     * 布局
     **/
    @BindView(id = R.id.ll_jcjl)
    private LinearLayout ll_jcjl;

    /**
     * 检查记录
     **/
//    @BindView(id = R.id.xljc_record)
//    private ListView xljc_record;

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

    private int KSJC=0;//开始检查
    private int JSJC=1;//结束检查

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.aty_xlxz);
        AnnotateManager.initBindView(this);/***注解式绑定控件**/
        initData();
        showLoadding();
        getXLXZJCSJ(0);//获取巡长检查时间
        getXLXZJCZT();//获取巡长检查状态
    }

    private void getXLXZJCSJ(final int timeTYPE) {//获取巡长检查时间
        JSONObject jo=new JSONObject();
        jo.put("XZID",getIntent().getStringExtra("ID"));//巡长ID
        String json=jo.toJSONString();
        ApiResource.getXZJCSJ(json, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int i, Header[] headers, byte[] bytes) {
                String result =new String(bytes);
                stopLoading();
                if (result.length() > 2) {
                    if (timeTYPE == 0) {
                        tv_jcsj.setText("当天累计检查" + result + "分钟");
                    }else {
                        BaiduTraceFacade.stop();    //点击结束检查 结束记录运动轨迹
                        new AlertDialog.Builder(DutyLeaderAty.this)
                                .setTitle(R.string.tips)
                                .setMessage("本次检查结束,当天累计检查" + result + "分钟!")
                                .setPositiveButton(R.string.sure, new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialogInterface, int i) {
                                        DutyLeaderAty.this.finish();
                                    }
                                }).setCancelable(false).show();
                    }
                }else {
                    if (timeTYPE == 0) {
                        tv_jcsj.setText("当天累计检查0分钟");
                    }else {
                        BaiduTraceFacade.stop();    //点击结束检查 结束记录运动轨迹
                        new AlertDialog.Builder(DutyLeaderAty.this)
                                .setTitle(R.string.tips)
                                .setMessage("本次检查结束,当天累计检查0分钟!")
                                .setPositiveButton(R.string.sure, new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialogInterface, int i) {
                                        DutyLeaderAty.this.finish();
                                    }
                                }).setCancelable(false).show();
                    }
                }
            }
            @Override
            public void onFailure(int i, Header[] headers, byte[] bytes, Throwable throwable) {
                toast("获取失败！");
                stopLoading();
            }
        });
    }

    private void getXLXZJCZT() {
        JSONObject jo=new JSONObject();
        jo.put("XZID",getIntent().getStringExtra("ID"));//巡长ID
        String json=jo.toJSONString();
        ApiResource.getXLXZJCZT(json, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int i, Header[] headers, byte[] bytes) {
                String result =new String(bytes);
                stopLoading();
                if (result.length() > 2){
                    JSONArray jo=JSON.parseArray(result);
                    ll_jcjl.setVisibility(View.VISIBLE);
                    btn_jsjc.setVisibility(View.VISIBLE);
                    btn_ksjc.setVisibility(View.GONE);
                    tv_sj.setText(((JSONObject)jo.get(0)).getString("SCSJ"));
                    tv_dz.setText(((JSONObject)jo.get(0)).getString("DZ"));
                }else {
                    ll_jcjl.setVisibility(View.GONE);
                    btn_jsjc.setVisibility(View.GONE);
                    btn_ksjc.setVisibility(View.VISIBLE);
                }
            }
            @Override
            public void onFailure(int i, Header[] headers, byte[] bytes, Throwable throwable) {
                toast("获取失败！");
                stopLoading();
            }
        });
    }



    private void initData() {
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

        //检测是否连接网络
        int  networkType = ((AppContext) getApplication()).getNetworkType();
        switch (v.getId()) {
            case R.id.tv_sddw:
                if (BaseDataUtils.isFastClick()) {
                    break;
                }
                loc();
//                Intent intent = new Intent(this, PickupMapActivity.class);
//                startActivityForResult(intent, PickupMapActivity.MAP_PICK_UP);
                break;
            case R.id.btn_ksjc:
                if (BaseDataUtils.isFastClick()) {
                    break;
                }
                if (networkType == 0) {
                    toast(getString(R.string.network_not_connected));
                } else {
                    loc();
                    getXLXZZT(KSJC);
                }
                break;

            case R.id.btn_jsjc:
                if (BaseDataUtils.isFastClick()) {
                    break;
                }
                if (networkType == 0) {
                    toast(getString(R.string.network_not_connected));
                } else {
                    loc();
                    getXLXZZT(JSJC);
                }
                break;
        }
    }

    private void getXLXZZT(final int jczt) {
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
                        setData(jczt);
                    }else {
                        setXZTips(((JSONObject)jo.get(0)).getString("XZXM"),((JSONObject)jo.get(0)).getString("XZHM"));
                    }
                }else {
                    setData(jczt);
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
                .setMessage("该所当前巡长是"+xzxm+",联系电话"+xzhm+"已经接班！")
                .setPositiveButton(R.string.sure, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        stopLoading();
                        DutyLeaderAty.this.finish();
                    }
                })
                .setCancelable(false).show();
    }
    private void setData(final int JCZT) {
        JSONObject jo=new JSONObject();
        jo.put("JCZT",JCZT);//开始检查状态 0 检查开始 1检查结束
        jo.put("XZID",getIntent().getStringExtra("ID"));//巡长ID
        jo.put("JD",bJD);//经度
        jo.put("WD",bWd);//纬度
        jo.put("DZ",et_dz.getText().toString());//地址
        String json=jo.toJSONString();
        ApiResource.uploadXZJCInfo(json, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int i, Header[] headers, byte[] bytes) {
                String result =new String(bytes);
                if (i == 200){
                    if (KSJC == JCZT){
                        showLoadding();
                        //点击开始检查  开始记录运动轨迹
                        BaiduTraceFacade.start(AppContext.instance().getLoginInfo().getUsername(), getApplicationContext());
                        getXLXZJCZT();
                    }else {
                        getXLXZJCSJ(1);//获取巡长检查时间
                    }
                }
            }
            @Override
            public void onFailure(int i, Header[] headers, byte[] bytes, Throwable throwable) {
                toast("上传失败！");
                stopLoading();
            }
        });
    }


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
    public boolean enableBackUpBtn() {
        return true;
    }


    @Override
    public String getAtyTitle() {
        return getString(R.string.xl_xz);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
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
            case R.id.action_list: //巡长历史记录
                showActivity(DutyLeaderHistoryAty.class);
                break;

            default:
                break;
        }
        return true;
    }
}