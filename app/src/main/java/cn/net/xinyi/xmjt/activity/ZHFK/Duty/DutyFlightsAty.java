package cn.net.xinyi.xmjt.activity.ZHFK.Duty;


import android.app.AlertDialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.TextView;
import android.widget.TimePicker;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONException;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.util.TypeUtils;
import com.loopj.android.http.AsyncHttpResponseHandler;

import org.apache.http.Header;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.List;

import cn.net.xinyi.xmjt.R;
import cn.net.xinyi.xmjt.api.ApiResource;
import cn.net.xinyi.xmjt.config.AdapterHolder;
import cn.net.xinyi.xmjt.config.AppContext;
import cn.net.xinyi.xmjt.config.BaseListAdp;
import cn.net.xinyi.xmjt.config.BaseListAty;
import cn.net.xinyi.xmjt.model.DutyFlightsModle;
import cn.net.xinyi.xmjt.utils.AnnotateManager;
import cn.net.xinyi.xmjt.utils.BaseDataUtils;

/**
 * Created by hao.zhou on 2016/6/29.
 */
public class DutyFlightsAty extends BaseListAty implements View.OnClickListener {
    //添加勤务班次布局
    private EditText et_name;//勤务名称
    private TextView tv_kssj;//开始时间
    private TextView tv_jssj;//结束时间
    private EditText et_sc;//时长
    private TextView tv_title;//标题
    private Button btn_cancel;//取消
    private Button btn_add;//添加
    private View myAddView;//自定义布局
    private AlertDialog alertDialog;
    private List<DutyFlightsModle> list;
    private boolean flag;
    private int position;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        AnnotateManager.initBindView(this);//注解式绑定控件
        TypeUtils.compatibleWithJavaBean = true;
        list =new ArrayList<DutyFlightsModle>();
        DutyFlightAddAdp mAdapter=new DutyFlightAddAdp(mListView,list,R.layout.aty_duty_flights,this);
        setAdapter(mAdapter);
        mAdapter.setState(BaseListAdp.STATE_LOAD_MORE);
        initBaseData();
    }


    private void initBaseData() {
        requestData();
        if (getIntent().getFlags()!=1){
            initLongClick();
            initDialog();
        }
    }


    private String getRequest() {
        JSONObject requestJson = new JSONObject();
        requestJson.put("FR_FRID", getIntent().getStringExtra("data")==null?0:getIntent().getStringExtra("data"));
        requestJson.put("PAGENUMBER",mCurrentPage);
        requestJson.put("PAGESIZE",PAGE_SIZE);
        return requestJson.toJSONString();
    }
    @Override
    protected void requestData() {
        showLoadding();
        ApiResource.getDutyFligtsList(getRequest(), new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int i, Header[] headers, byte[] bytes) {
                if (mCurrentPage==1)
                    stopLoading();
                String result = new String(bytes);
                try {
                    list = JSON.parseArray(result,DutyFlightsModle.class);
                    if (list!=null){
                        if (list.size()>=PAGE_SIZE){
                            setStateFinish();
                            getAdapter().getData().addAll(list);
                            mNodata.setVisibility(View.GONE);
                        } else {
                            if (mCurrentPage==1&&list.size()==0){
                                setEmptyData();
                            } else {
                                getAdapter().getData().addAll(list);
                                mNodata.setVisibility(View.GONE);
                                setNotMoreState();
                            }
                        }
                    } else {
                        toast("没有数据");
                        setEmptyData();
                    }
                    getAdapter().notifyDataSetChanged();
                } catch (JSONException e){
                    toast("获取数据失败");
                    setEmptyData();
                }
            }

            @Override
            public void onFailure(int i, Header[] headers, byte[] bytes, Throwable throwable) {
                if (mCurrentPage==1)
                    stopLoading();
                toast("获取数据失败");
                setEmptyData();
            }
        });
    }


    //初始化长按item执行删除、修改等操作
    private void initLongClick() {
        mListView.setOnCreateContextMenuListener(new View.OnCreateContextMenuListener() {
            @Override
            public void onCreateContextMenu(ContextMenu menu, View view, ContextMenu.ContextMenuInfo contextMenuInfo) {
                menu.add(0, 0, 0, getString(R.string.editor));
                menu.add(0, 1, 0,  getString(R.string.del));
                menu.add(0, 2, 0,  getString(R.string.cancel));
            }
        });
        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

            }
        });
    }
    //
    @Override
    public boolean onContextItemSelected(MenuItem item) {
        AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();
        position = (int) info.id;// 这里的info.id对应的就是数据库中_id的值
        switch (item.getItemId()){
            case 0://编辑
                setData();
                break;

            case 1://删除
                new AlertDialog.Builder(DutyFlightsAty.this).setTitle(getString(R.string.tips))
                        .setMessage("确定删除信息！")
                        .setPositiveButton(getString(R.string.cancel), new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {

                            }
                        })
                        .setNegativeButton(getString(R.string.sure), new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                delInfo(position);
                            }
                        }).show();
                break;

            case 2://取消
                toast(getString(R.string.cancel));
                break;
        }
        return super.onContextItemSelected(item);
    }

    private void setData() {
        flag=true;
        //上传成功后清除控件数据
        tv_jssj.setText(((DutyFlightsModle)getAdapter().getData().get(position)).getEND_TIME());
        tv_kssj.setText(((DutyFlightsModle)getAdapter().getData().get(position)).getSTART_TIME());
        et_sc.setText(((DutyFlightsModle)getAdapter().getData().get(position)).getLENGTH());
        et_name.setText(((DutyFlightsModle)getAdapter().getData().get(position)).getFLIGHT_NAME());
        showDialogAlert(getString(R.string.duty_flight_edt));
    }

    //勤务班次编辑
    private void editorInfo() {
        showLoadding();
        ApiResource.edtDutyFlight(getRequestJson(), new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int i, Header[] headers, byte[] bytes) {
                stopLoading();
                String result =new String(bytes);
                if (i == 200 && result.equals("true")){
                    refrshData();
                    toast("编辑勤务班次成功");
                }else {
                    toast("编辑勤务班次失败！");
                }
            }
            @Override
            public void onFailure(int i, Header[] headers, byte[] bytes, Throwable throwable) {
                toast("编辑勤务班次失败！");
                stopLoading();
            }
        });
    }

    //勤务班次删除
    private void delInfo(int position) {
        showLoadding();
        JSONObject jo=new JSONObject();
        jo.put("ID",((DutyFlightsModle)getAdapter().getData().get(position)).getId());
        ApiResource.delDutyFlight(jo.toJSONString(), new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int i, Header[] headers, byte[] bytes) {
                stopLoading();
                String result =new String(bytes);
                if (i == 200 && result.equals("true")){
                    getAdapter().getData().clear();
                    mCurrentPage=1;
                    requestData();
                    toast("删除成功");
                }else {
                    toast("删除失败！");
                }
            }
            @Override
            public void onFailure(int i, Header[] headers, byte[] bytes, Throwable throwable) {
                toast("删除失败！");
                stopLoading();
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
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                this.finish();
                break;
            case R.id.action_list: //添加勤务班次
                flag=false;
                showDialogAlert(getString(R.string.duty_flight_add));
                break;

            default:
                break;
        }
        return true;
    }
    //初始化自定义添加勤务班次AlertDialog
    private void initDialog() {
        // 取得自定义View
        LayoutInflater layoutInflater = LayoutInflater.from(this);
        myAddView = layoutInflater.inflate(R.layout.aty_duty_flights_add, null);
        et_name= (EditText) myAddView.findViewById(R.id.et_name);
        tv_kssj =(TextView) myAddView.findViewById(R.id.tv_kssj);
        tv_jssj= (TextView)myAddView.findViewById(R.id.tv_jssj);
        et_sc= (EditText)myAddView.findViewById(R.id.et_sc);
        tv_title= (TextView)myAddView.findViewById(R.id.tv_title);
        btn_cancel= (Button)myAddView.findViewById(R.id.btn_cancel);
        btn_add= (Button)myAddView.findViewById(R.id.btn_add);
        tv_kssj.setOnClickListener(this);
        tv_jssj.setOnClickListener(this);
        btn_cancel.setOnClickListener(this);
        btn_add.setOnClickListener(this);
//        tv_kssj.addTextChangedListener(new TextWatcher() {
//            @Override
//            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {}
//
//            @Override
//            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
//                if (!tv_kssj.getText().toString().isEmpty()&&!tv_jssj.getText().toString().isEmpty()){
//                    try {
//                        DateFormat df1 = new SimpleDateFormat("HH:mm:ss");
//                        long kssj=df1.parse(tv_kssj.getText().toString()+":00").getTime();
//                        long jssj=df1.parse(tv_jssj.getText().toString()+":00").getTime();
//                        if (kssj>jssj){
//                            et_sc.setText(""+((kssj-jssj)/(1000*60*60)));
//                        }else {
//                            et_sc.setText(""+((jssj-kssj)/(1000*60*60)));
//                        }
//                    } catch (ParseException e) {
//                        e.printStackTrace();
//                    }
//                }
//            }
//
//            @Override
//            public void afterTextChanged(Editable editable) {}
//        });
//        tv_jssj.addTextChangedListener(new TextWatcher() {
//            @Override
//            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {}
//
//            @Override
//            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
////                if (!tv_kssj.getText().toString().isEmpty()&&!tv_jssj.getText().toString().isEmpty()){
////                    int kssj=Integer.valueOf(tv_kssj.getText().toString().substring(0,2));
////                    int jssj=Integer.valueOf(tv_jssj.getText().toString().substring(0,2));
////                    if (kssj>jssj){
////                        et_sc.setText(""+((24-kssj)+jssj));
////                    }else {
////                        et_sc.setText(""+(jssj-kssj));
////                    }
////                }
//                try {
//                    DateFormat df1 = new SimpleDateFormat("HH:mm:ss");
//                    long kssj=df1.parse(tv_kssj.getText().toString()+":00").getTime();
//                    long jssj=df1.parse(tv_jssj.getText().toString()+":00").getTime();
//                    if (kssj>jssj){
//                        et_sc.setText(""+((kssj-jssj)/(1000*60*60)));
//                    }else {
//                        et_sc.setText(""+((jssj-kssj)/(1000*60*60)));
//                    }
//                } catch (ParseException e) {
//                    e.printStackTrace();
//                }
//            }
//
//            @Override
//            public void afterTextChanged(Editable editable) {}
//        });
    }
    //点击时间监听
    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.tv_kssj://开始时间
                if (BaseDataUtils.isFastClick()) {

                }else {showTimePicket(tv_kssj);}
                break;
            case R.id.tv_jssj://结束时间
                if (BaseDataUtils.isFastClick()) {

                }else {showTimePicket(tv_jssj);}
                break;
            case R.id.btn_add://添加勤务班次 以及编辑
                if (BaseDataUtils.isFastClick()) {

                }else if (((AppContext) getApplication()).getNetworkType() == 0) {
                    toast(getString(R.string.network_not_connected));
                } else if (et_name.getText().toString().isEmpty()){
                    toast("班次名称不能为空！");
                    dismissAlert(false);
                }else if (tv_kssj.getText().toString().isEmpty()){
                    toast("开始时间不能为空！");
                    dismissAlert(false);
                }else if (tv_jssj.getText().toString().isEmpty()){
                    toast("结束时间不能为空！");
                    dismissAlert(false);
                }else if (et_sc.getText().toString().isEmpty()){
                    toast("时长（小时）不能为空！");
                    dismissAlert(false);
                }else {
                    if (flag){//编辑勤务班次
                        editorInfo();
                    }else {//上传勤务班次
                        uploadInfo();
                    }
                }
                break;
            case R.id.btn_cancel://取消勤务班次
                if (BaseDataUtils.isFastClick()) {

                }else{
                    clearText();
                }
                break;
        }
    }

    private void showTimePicket(final TextView tv) {
        Calendar c=Calendar.getInstance();
        c.setTimeInMillis(System.currentTimeMillis());
        int hour = c.get(Calendar.HOUR_OF_DAY);
        int  minute = c.get(Calendar.MINUTE);

        new TimePickerDialog(DutyFlightsAty.this, new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker timePicker, int hourOfDay, int minuteOfHour) {
                if (minuteOfHour >= 30){
                    tv.setText(hourOfDay< 10 ? "0"+hourOfDay+":30":hourOfDay+":30");
                }else {
                    tv.setText(hourOfDay< 10 ? "0"+hourOfDay+":00":hourOfDay+":00");
                }

            }
        },hour, minute, true).show();
    }

    private void showDialogAlert(String title) {
        tv_title.setText(title);
        alertDialog=new AlertDialog.Builder(DutyFlightsAty.this)
                .setView(myAddView)
                .setCancelable(false).show();
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
    //上传勤务班次
    private void uploadInfo() {
        showLoadding();
        ApiResource.addDutyFlight(getRequestJson(), new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int i, Header[] headers, byte[] bytes) {
                stopLoading();
                String result =new String(bytes);
                if (i == 200 && result.length() > 5){
                    refrshData();
                }else {
                    onFailure(i,headers,bytes,null);
                }
                InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                if (imm != null) {
                    imm.hideSoftInputFromWindow(DutyFlightsAty.this.getWindow().getDecorView().getWindowToken(), 0);
                }
            }

            @Override
            public void onFailure(int i, Header[] headers, byte[] bytes, Throwable throwable) {
                toast("添加失败！");
                stopLoading();
            }
        });
    }

    public String getRequestJson(){
        DutyFlightsModle Info=new DutyFlightsModle();
        Info.setCREATOR_ID(userInfo.getId());
        Info.setCREATOR_NAME(userInfo.getName());
        Info.setFR_FRID(getIntent().getStringExtra("data"));
        Info.setEND_TIME(tv_jssj.getText().toString());
        Info.setSTART_TIME(tv_kssj.getText().toString());
        Info.setLENGTH(et_sc.getText().toString());
        Info.setFLIGHT_NAME(et_name.getText().toString());
        JSONObject jo=JSON.parseObject(JSON.toJSONString(Info));
        if (flag){
            jo.put("ID",((DutyFlightsModle)getAdapter().getData().get(position)).getId());
        }
        return jo.toJSONString();
    }

    private void refrshData(){
        getAdapter().getData().clear();
        mCurrentPage=1;
        requestData();
        clearText();
    }

    private void clearText() {
        dismissAlert(true);//控制点击 diaAlert是否出现
        ((FrameLayout)myAddView.getParent()).removeView(myAddView);//移除view,否则会报错
        //上传成功后清除控件数据
        tv_kssj.setText("");
        tv_jssj.setText("");
        et_name.setText("");
        et_sc.setText("");
    }

    @Override
    public boolean enableBackUpBtn() {
        return true;
    }


    @Override
    public String getAtyTitle() {
        return getString(R.string.duty_flight);
    }


    public class DutyFlightAddAdp extends BaseListAdp<DutyFlightsModle> {
        public DutyFlightAddAdp(AbsListView view, Collection<DutyFlightsModle> mDatas, int itemLayoutId, Context mContext) {
            super(view, mDatas, itemLayoutId, mContext);
        }

        @Override
        public void convert(AdapterHolder helper, DutyFlightsModle item, boolean isScrolling) {
            helper.setText(R.id.tv_mz,item.getFLIGHT_NAME());
            helper.setText(R.id.tv_sj,"时长（小时）："+item.getLENGTH());
            helper.setText(R.id.tv_kssj,"开始时间："+item.getSTART_TIME());
            helper.setText(R.id.tv_jssj,"结束时间："+item.getEND_TIME());
        }
    }
}