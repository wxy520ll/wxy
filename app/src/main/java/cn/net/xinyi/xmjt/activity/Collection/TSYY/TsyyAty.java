package cn.net.xinyi.xmjt.activity.Collection.TSYY;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.GridView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.util.TypeUtils;
import com.bigkoo.pickerview.OptionsPickerView;
import com.bigkoo.pickerview.TimePickerView;
import com.loopj.android.http.AsyncHttpResponseHandler;

import org.apache.http.Header;

import java.lang.reflect.Field;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import cn.net.xinyi.xmjt.R;
import cn.net.xinyi.xmjt.api.ApiResource;
import cn.net.xinyi.xmjt.config.BaseActivity2;
import cn.net.xinyi.xmjt.config.BaseListAdp;
import cn.net.xinyi.xmjt.model.TssModle;
import cn.net.xinyi.xmjt.model.TssyyModle;
import cn.net.xinyi.xmjt.utils.AnnotateManager;
import cn.net.xinyi.xmjt.utils.BaseUtil;
import cn.net.xinyi.xmjt.utils.BindView;
import cn.net.xinyi.xmjt.utils.DateUtil;
import cn.net.xinyi.xmjt.utils.DialogHelper;
import cn.net.xinyi.xmjt.utils.HorizontalListView;
import cn.net.xinyi.xmjt.utils.UI;

public class TsyyAty extends BaseActivity2 implements View.OnClickListener {

    //预约布局
    @BindView(id = R.id.gv_yy)
    GridView gv_yy;
    //自定义横向listview
    @BindView(id = R.id.listview)
    HorizontalListView listview;
    //自定义横向listview
    @BindView(id = R.id.ll_empty_data)
    LinearLayout ll_empty_data;

    private  TextView tv_qx;
    private  TextView tv_fjmc;
    private  TextView tv_fjbh;
    private  TextView tv_fjmj;
    private  TextView tv_fjms;
    private  Button btn_yylx1;
    private  Button btn_yylx2;
    private  Button btn_yylx3;
    private  Button btn_yylx4;
    private  Button btn_yylx5;
    private  Button btn_yylx6;
    private  LinearLayout ll_sw;
    private  LinearLayout ll_xw;


    private TimePickerView pvTime;
    private TsyyAdp mAdapter;
    private OptionsPickerView pvOptions;
    private List<TssModle> mLists;
    private HorizontalListViewAdapter dateAdapter;
    private View myAddView;
    private AlertDialog alertDialog;
    private String sDate;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.aty_tsyy);
        TypeUtils.compatibleWithJavaBean = true;
        AnnotateManager.initBindView(this);  //控件绑定
        initDialog();
        getRQ();
    }

    private void getRQ() {
        showLoadding();
        JSONObject jo=new JSONObject();
        jo.put("SJ",new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()));
        ApiResource.getTssyyrqList(jo.toJSONString(),new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int i, Header[] headers, byte[] bytes) {
                try {
                    String result = new String(bytes);
                    initListView(JSON.parseArray(result));
                    stopLoading();
                } catch (Exception e) {
                    onFailure(i, headers, bytes, null);
                }
            }
            @Override
            public void onFailure(int i, Header[] headers, byte[] bytes, Throwable throwable) {
                stopLoading();
                toast("获取数据失败");
                ll_empty_data.setVisibility(View.VISIBLE);
                listview.setVisibility(View.GONE);
                gv_yy.setVisibility(View.GONE);

            }
        });
    }

    private void initListView(final JSONArray jsonArray) {
        dateAdapter= new HorizontalListViewAdapter(TsyyAty.this,jsonArray);
        listview.setAdapter(dateAdapter);
        if (jsonArray.size()>0){
            dateAdapter.setSelectIndex(0);//默认选择今天日期
            gwtTssyyData(((JSONObject)jsonArray.get(0)).getString("SJ"));//根据时间查询预约情况
        }
        listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                gwtTssyyData(((JSONObject)jsonArray.get(position)).getString("SJ"));//根据时间查询预约情况
                dateAdapter.setSelectIndex(position);
                dateAdapter.notifyDataSetChanged();
            }
        });
    }


    private void gwtTssyyData(String selectDate) {
        showLoadding();
        JSONObject jo=new JSONObject();
        sDate=selectDate;
        jo.put("YYRQ",selectDate);
        ApiResource.getTssList(jo.toJSONString(),new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int i, Header[] headers, byte[] bytes) {
                try {
                    String result = new String(bytes);
                    mLists = JSON.parseArray(result, TssModle.class);
                    initGridView();//初始化GridView数据
                    stopLoading();
                } catch (Exception e) {
                    onFailure(i, headers, bytes, null);
                }
            }

            @Override
            public void onFailure(int i, Header[] headers, byte[] bytes, Throwable throwable) {
                stopLoading();
                toast("获取数据失败");
                ll_empty_data.setVisibility(View.VISIBLE);
                gv_yy.setVisibility(View.GONE);
            }
        });
    }


    private void initGridView() {
        gv_yy.setVisibility(View.VISIBLE);
        ll_empty_data.setVisibility(View.GONE);
        mAdapter = new TsyyAdp(gv_yy,mLists,R.layout.item_tsyy,this);
        mAdapter.setState(BaseListAdp.STATE_LESS_ONE_PAGE);
        // 添加并且显示
        gv_yy.setAdapter(mAdapter);
        // 添加消息处理
        gv_yy.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (mAdapter.getZtTotal(position)>0){
                    showDialogAlert(mLists.get(position));
                }else {
                    UI.toast(TsyyAty.this,"你选择的提审室预约已满！");
                }
            }
        });
    }


    //初始化自定义添加AlertDialog
    private void initDialog() {
        // 取得自定义View
        LayoutInflater layoutInflater = LayoutInflater.from(this);
        myAddView = layoutInflater.inflate(R.layout.aty_tsyy_add, null);
        tv_qx=(TextView)myAddView.findViewById(R.id.tv_qx);
        ll_sw=(LinearLayout)myAddView.findViewById(R.id.ll_sw);
        ll_xw=(LinearLayout)myAddView.findViewById(R.id.ll_xw);
        tv_fjbh=(TextView)myAddView.findViewById(R.id.tv_fjbh);
        tv_fjmc=(TextView)myAddView.findViewById(R.id.tv_fjmc);
        tv_fjmj=(TextView)myAddView.findViewById(R.id.tv_fjmj);
        tv_fjms=(TextView)myAddView.findViewById(R.id.tv_fjms);
        btn_yylx1=(Button)myAddView.findViewById(R.id.btn_yylx1);
        btn_yylx2=(Button)myAddView.findViewById(R.id.btn_yylx2);
        btn_yylx3=(Button)myAddView.findViewById(R.id.btn_yylx3);
        btn_yylx4=(Button)myAddView.findViewById(R.id.btn_yylx4);
        btn_yylx5=(Button)myAddView.findViewById(R.id.btn_yylx5);
        btn_yylx6=(Button)myAddView.findViewById(R.id.btn_yylx6);
        tv_qx.setOnClickListener(this);
        btn_yylx1.setOnClickListener(this);
        btn_yylx2.setOnClickListener(this);
        btn_yylx3.setOnClickListener(this);
        btn_yylx4.setOnClickListener(this);
        btn_yylx5.setOnClickListener(this);
        btn_yylx6.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.tv_qx:
                clearText();
                break;

            case R.id.btn_yylx1:
                //不能申请大于当前时间的时段
                try {
                    if (DateUtil.getDays2NowValus(sDate+ TsyyListAty.time1+DateUtil.getNowTime("yyyy-MM-dd HH:mm:ss")) < 0){
                        UI.toast(TsyyAty.this,getString(R.string.tsys_sq_tips1));
                    }else if (DateUtil.getDays2NowValus(sDate+ TsyyListAty.time1+DateUtil.getNowTime("yyyy-MM-dd HH:mm:ss")) < 0){
                        UI.toast(TsyyAty.this,getString(R.string.tsys_sq_tips));
                    }else if (DateUtil.secondBetween(DateUtil.getNowTime("yyyy-MM-dd HH:mm:ss"),sDate+ TsyyListAty.time1) >0){
                        UI.toast(TsyyAty.this,getString(R.string.tsys_sq_tips));
                    }else {
                        uploadInfo("1","9",btn_yylx1);
                    }
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                break;

            case R.id.btn_yylx2:
                //不能申请大于当前时间的时段
                try {

                    if (DateUtil.getDays2NowValus(sDate+ TsyyListAty.time1+DateUtil.getNowTime("yyyy-MM-dd HH:mm:ss")) < 0){
                        UI.toast(TsyyAty.this,getString(R.string.tsys_sq_tips1));
                    }else if (DateUtil.getDays2NowValus(sDate+ TsyyListAty.time2+DateUtil.getNowTime("yyyy-MM-dd HH:mm:ss")) < 1000 * 60 * 30){
                        UI.toast(TsyyAty.this,getString(R.string.tsys_sq_tips));
                    }else  if (DateUtil.secondBetween(DateUtil.getNowTime("yyyy-MM-dd HH:mm:ss"),sDate+ TsyyListAty.time2)>0){
                        UI.toast(TsyyAty.this,getString(R.string.tsys_sq_tips));
                    }else {
                        uploadInfo("1","10",btn_yylx2);
                    }
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                break;

            case R.id.btn_yylx3:
                //不能申请大于当前时间的时段
                try {
                    if (DateUtil.getDays2NowValus(sDate+ TsyyListAty.time1+DateUtil.getNowTime("yyyy-MM-dd HH:mm:ss")) < 0){
                        UI.toast(TsyyAty.this,getString(R.string.tsys_sq_tips1));
                    }else if (DateUtil.getDays2NowValus(sDate+ TsyyListAty.time3+DateUtil.getNowTime("yyyy-MM-dd HH:mm:ss")) < 1000 * 60 * 30){
                        UI.toast(TsyyAty.this,getString(R.string.tsys_sq_tips));
                    }else if (DateUtil.secondBetween(DateUtil.getNowTime("yyyy-MM-dd HH:mm:ss"),sDate+ TsyyListAty.time3)>0){
                        UI.toast(TsyyAty.this,getString(R.string.tsys_sq_tips));
                    }else {
                        uploadInfo("1","11",btn_yylx3);
                    }
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                break;

            case R.id.btn_yylx4:
                //不能申请大于当前时间的时段
                try {
                    if (DateUtil.getDays2NowValus(sDate+ TsyyListAty.time4+DateUtil.getNowTime("yyyy-MM-dd HH:mm:ss")) < 0){
                        UI.toast(TsyyAty.this,getString(R.string.tsys_sq_tips2));
                    }else if (DateUtil.getDays2NowValus(sDate+ TsyyListAty.time4+DateUtil.getNowTime("yyyy-MM-dd HH:mm:ss")) < 1000 * 60 * 30){
                        UI.toast(TsyyAty.this,getString(R.string.tsys_sq_tips));
                    }else  if (DateUtil.secondBetween(DateUtil.getNowTime("yyyy-MM-dd HH:mm:ss"),sDate+ TsyyListAty.time4)>0){
                        UI.toast(TsyyAty.this,getString(R.string.tsys_sq_tips));
                    }else {
                        uploadInfo("2","14",btn_yylx4);
                    }
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                break;

            case R.id.btn_yylx5:
                //不能申请大于当前时间的时段
                try {

                    if (DateUtil.getDays2NowValus(sDate+ TsyyListAty.time4+DateUtil.getNowTime("yyyy-MM-dd HH:mm:ss")) < 0){
                        UI.toast(TsyyAty.this,getString(R.string.tsys_sq_tips2));
                    }else if (DateUtil.getDays2NowValus(sDate+ TsyyListAty.time5+DateUtil.getNowTime("yyyy-MM-dd HH:mm:ss")) < 1000 * 60 * 30){
                        UI.toast(TsyyAty.this,getString(R.string.tsys_sq_tips));
                    }else if (DateUtil.secondBetween(DateUtil.getNowTime("yyyy-MM-dd HH:mm:ss"),sDate+ TsyyListAty.time5)>0){
                        UI.toast(TsyyAty.this,getString(R.string.tsys_sq_tips));
                    }else {
                        uploadInfo("2","15",btn_yylx5);
                    }
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                break;

            case R.id.btn_yylx6:
                //不能申请大于当前时间的时段
                try {
                    if (DateUtil.getDays2NowValus(sDate+ TsyyListAty.time4+DateUtil.getNowTime("yyyy-MM-dd HH:mm:ss")) < 0){
                        UI.toast(TsyyAty.this,getString(R.string.tsys_sq_tips2));
                    }else if (DateUtil.getDays2NowValus(sDate+ TsyyListAty.time6+DateUtil.getNowTime("yyyy-MM-dd HH:mm:ss")) < 1000 * 60 * 30){
                        UI.toast(TsyyAty.this,getString(R.string.tsys_sq_tips));
                    }else  if (DateUtil.secondBetween(DateUtil.getNowTime("yyyy-MM-dd HH:mm:ss"),sDate+ TsyyListAty.time6)>0){
                        UI.toast(TsyyAty.this,getString(R.string.tsys_sq_tips));
                    }else {
                        uploadInfo("2","16",btn_yylx6);
                    }
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                break;
        }
    }

    private void uploadInfo(final String yysd,final String type,Button btn) {
        DialogHelper.showAlertDialog("您当前预约的时间为" + btn.getText().toString(), TsyyAty.this, new DialogHelper.OnOptionClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which, Object o) {
                showLoadding();
                TssyyModle tssyyModle=new TssyyModle();
                tssyyModle.setYYRJH(userInfo.getPoliceno());//警号
                tssyyModle.setYYRSJH(userInfo.getUsername());//用户手机号码
                tssyyModle.setYYLX(type);//预约类型
                tssyyModle.setYYSD(yysd);
                tssyyModle.setZT("1");//预约状态
                tssyyModle.setFJBH(tv_fjbh.getText().toString());//预约房间编号
                tssyyModle.setYYRQ(sDate);//预约日期
                JSONObject jo=JSON.parseObject(JSON.toJSONString(tssyyModle));
                ApiResource.addTssyy(jo.toJSONString(),new AsyncHttpResponseHandler() {
                    @Override
                    public void onSuccess(int i, Header[] headers, byte[] bytes) {
                        String result = new String(bytes);
                        if (i==200&&result.contains("T")){
                            JSONObject jo=JSON.parseObject(result);
                            BaseUtil.showDialog("预约成功，预约编号为："+jo.get("YYH"),TsyyAty.this);
                            clearText();
                            gwtTssyyData(sDate);
                            stopLoading();
                        } else {
                            onFailure(i, headers, bytes, null);
                        }
                    }
                    @Override
                    public void onFailure(int i, Header[] headers, byte[] bytes, Throwable throwable) {
                        stopLoading();
                        UI.toast(TsyyAty.this,bytes==null?"上传失败":new String(bytes).toString());
                    }
                });
            }
        });
    }


    private void showDialogAlert(TssModle tssModle) {
        alertDialog=new AlertDialog.Builder(TsyyAty.this)
                .setView(myAddView)
                .setCancelable(false).show();

        tv_fjbh.setText(tssModle.getFJBH());
        tv_fjmc.setText(tssModle.getMC());
        tv_fjmj.setText(tssModle.getMJ());
        tv_fjms.setText(tssModle.getMC());
        List<TssyyModle> tssyyModles = JSON.parseArray(tssModle.getDATALIST(), TssyyModle.class);
        ll_sw.setVisibility(View.VISIBLE);
        ll_xw.setVisibility(View.VISIBLE);
        for (TssyyModle info:tssyyModles) {
            if (info.getYYLX().equals("9")
                    || info.getYYLX().equals("10")
                    || info.getYYLX().equals("11")) {//预约类型 上午
                ll_sw.setVisibility(View.GONE);
            } else if (info.getYYLX().equals("14")
                    || info.getYYLX().equals("15")
                    || info.getYYLX().equals("16")) {//预约类型 下午
                ll_xw.setVisibility(View.GONE);
            }
        }
    }


    private void clearText() {
        dismissAlert(true);
        ((FrameLayout)myAddView.getParent()).removeView(myAddView);
    }

    private void dismissAlert(boolean b) {//控制对话框是否关闭
        try {
            Field field = alertDialog.getClass()
                    .getSuperclass().getDeclaredField("mShowing" );
            field.setAccessible(true);
            // 将mShowing变量设为false，表示对话框已关闭
            field.set(alertDialog,b);
            alertDialog.dismiss();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


       private void initTimePickerView() {
        //时间选择器
        pvTime = new TimePickerView(this, TimePickerView.Type.YEAR_MONTH_DAY);
        pvTime.setTitle("请选择时间");
        //控制时间范围
        final Calendar calendar = Calendar.getInstance();
        pvTime.setRange(calendar.get(Calendar.YEAR), calendar.get(Calendar.YEAR)+20);//要在setTime 之前才有效果哦
        pvTime.setTime(new Date());
        pvTime.setCyclic(false);
        pvTime.setCancelable(true);

        //当前日期的七天后的日期
        calendar.set(Calendar.DATE, calendar.get(Calendar.DATE) + 7);
        final String SevenLaterTime=DateUtil.date2String(calendar.getTime(),"yyyyMMdd");

        //时间选择后回调
        pvTime.setOnTimeSelectListener(new TimePickerView.OnTimeSelectListener() {

            @Override
            public void onTimeSelect(Date date) {
                String choosDate=DateUtil.date2String(date,"yyyyMMdd");
                if (DateUtil.Date2Weekend(choosDate,"yyyyMMdd").contains("六")//判断选择的日期是否为工作日
                        ||DateUtil.Date2Weekend(choosDate,"yyyyMMdd").contains("日")){
                    UI.toast(TsyyAty.this,"您当前选择的为非工作日时间不能预约！");
                }else if (Integer.parseInt(choosDate)>Integer.parseInt(SevenLaterTime)){//判断选择的时间是否为近7天
                    UI.toast(TsyyAty.this,"您当前只能预约近7天");
                }else {
                    // gwtTssyyData();
                }
            }
        });
    }

    private void initDataPickerView() {
        //选项选择器
        pvOptions = new OptionsPickerView(this);
        //选项2
        final ArrayList<String> options2Items_01=new ArrayList<>();
        options2Items_01.add("09-12");
        options2Items_01.add("14-16");
        options2Items_01.add("16-18");
        //联动效果
        pvOptions.setPicker(options2Items_01);
        pvOptions.setTitle("选择时间类型");
        pvOptions.setCyclic(false);
        //监听确定选择按钮
        pvOptions.setSelectOptions(1);

        pvOptions.setOnoptionsSelectListener(new OptionsPickerView.OnOptionsSelectListener() {

            @Override
            public void onOptionsSelect(int options1, int option2, int options3) {

            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode==RESULT_OK){
            gwtTssyyData(sDate);
        }
    }



    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main1, menu);
        final MenuItem action_list = menu.findItem(R.id.action_list1);
        action_list.getActionView().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onOptionsItemSelected(action_list);
            }
        });
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                this.finish();
                break;
            case R.id.action_list1: //获取个人预约记录
                UI.startActivity(this,TsyyListAty.class,1001);
                break;
            default:
                break;
        }
        return true;
    }


    @Override
    public boolean enableBackUpBtn() {
        return true;
    }

    @Override
    public String getAtyTitle() {
        return getString(R.string.ttys);
    }


}
