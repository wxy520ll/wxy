package cn.net.xinyi.xmjt.activity.ZHFK.Duty;



import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONException;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.util.TypeUtils;
import com.loopj.android.http.AsyncHttpResponseHandler;

import org.apache.http.Header;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import cn.net.xinyi.xmjt.R;
import cn.net.xinyi.xmjt.api.ApiResource;
import cn.net.xinyi.xmjt.config.AppContext;
import cn.net.xinyi.xmjt.config.BaseListAty;
import cn.net.xinyi.xmjt.model.DutyBeatsModle;
import cn.net.xinyi.xmjt.model.DutyFlightRulesModle;
import cn.net.xinyi.xmjt.utils.AnnotateManager;
import cn.net.xinyi.xmjt.utils.BaseDataUtils;

/**
 * Created by hao.zhou on 2016/6/29.
 */
public class DutyBeatsAddAty extends BaseListAty implements View.OnClickListener {
    //title 查询按钮、以及查询条件
    private TextView tv_level;//选择巡逻段级别
    private TextView tv_type;//选择巡逻方式
    private Button btn_query;//查询
    //添加巡逻段布局
    private TextView tv_addlevel;//巡逻段级别
    private TextView tv_addtype;//巡逻方式
    private TextView tv_title;//标题
    private TextView tv_rules;//排班规则
    private EditText et_addname;//巡逻段名称
    private EditText et_adddes;//巡段段描述
    private Button btn_cancel;//取消
    private Button btn_add;//添加
    private View myAddView;//自定义布局
    private AlertDialog alertDialog;

    private Map<String,String> mapRusles=new HashMap<String, String>();
    private List<String> sRuleList=new ArrayList<String>();

    private boolean flag;
    private int position;
    private JSONArray rankArray;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        AnnotateManager.initBindView(this);//注解式绑定控件
        TypeUtils.compatibleWithJavaBean = true;
        initBaseData();
    }


    private void initBaseData() {
        //tv_level.setText(userInfo.getAccounttype().equals("民警") ? "二级":"三级");
        tv_level.setText("二级");
        tv_type.setText("徒步");
        requestData();
        getRulesData();
        initLongClick();
        initDialog();
    }


    private String getRequest() {
        JSONObject requestJson = new JSONObject();
        requestJson.put("DEPT_ID", AppContext.instance().getLoginInfo().getOrgancode());
        requestJson.put("BEAT_LEVEL", BaseDataUtils.getLevelNum(tv_level.getText().toString()));
        requestJson.put("BEAT_TYPE", BaseDataUtils.getTypeNum(tv_type.getText().toString()));
        return requestJson.toJSONString();
    }
    @Override
    protected void requestData() {
        showLoadding();
        ApiResource.getDutyBeatsList(getRequest(), new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int i, Header[] headers, byte[] bytes) {
                String result = new String(bytes);
                if (null!=rankArray){
                    rankArray.clear();
                }
                rankArray= JSON.parseArray(result);
                if (rankArray!=null&&rankArray.size()>0){
                    mNodata.setVisibility(View.GONE);
                    DutyBeatsAddAdp adp = new DutyBeatsAddAdp(DutyBeatsAddAty.this,rankArray);
                    mListView.setAdapter(adp);
                } else {
                    mNodata.setVisibility(View.VISIBLE);
                }
                stopLoading();
            }
            @Override
            public void onFailure(int i, Header[] headers, byte[] bytes, Throwable throwable) {
                toast("获取数据失败");
                mNodata.setVisibility(View.VISIBLE);
            }
        });
    }

    //查询部分布局
    @Override
    public void setupTopView(LinearLayout parent) {
        super.setupTopView(parent);
        View addView= LayoutInflater.from(this).inflate(R.layout.aty_dutybeats_top,parent);
        parent.setVisibility(View.VISIBLE);
        tv_level=(TextView)addView.findViewById(R.id.tv_level);//巡逻段级别
        tv_level.setOnClickListener(this);
        tv_type=(TextView)addView.findViewById(R.id.tv_type);//巡逻方式
        tv_type.setOnClickListener(this);
        btn_query=(Button)addView.findViewById(R.id.btn_query);//查询
        btn_query.setOnClickListener(this);
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
                Intent intent = new Intent(DutyBeatsAddAty.this,DutyBoxAddAty.class);
                intent.putExtra("data",((JSONObject)rankArray.get(i)).getString("BID"));
                startActivity(intent);
            }
        });
    }



    @Override
    public boolean onContextItemSelected(MenuItem item) {
        AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();
        position = (int) info.id;// 这里的info.id对应的就是数据库中_id的值
        switch (item.getItemId()){
            case 0://编辑
                setData();
                break;

            case 1://删除
                new AlertDialog.Builder(DutyBeatsAddAty.this).setTitle(getString(R.string.tips))
                        .setMessage("确定删除巡逻段信息！")
                        .setPositiveButton(getString(R.string.sure), new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                delInfo(position);
                            }
                        })
                        .setNegativeButton(getString(R.string.cancel), new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {

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
        tv_addlevel.setText(BaseDataUtils.getLevel(((JSONObject)rankArray.get(position)).getString("BEAT_LEVEL")));
        tv_addtype.setText(BaseDataUtils.getType(((JSONObject)rankArray.get(position)).getString("BEAT_TYPE")));
        et_adddes.setText(((JSONObject)rankArray.get(position)).getString("BEAT_DESCRIBE"));
        et_addname.setText(((JSONObject)rankArray.get(position)).getString("BID_NAME"));
        tv_rules.setText(((JSONObject)rankArray.get(position)).getString("FRNAME"));
        showDialogAlert(getString(R.string.duty_beats_editor));
    }

    //巡逻段编辑
    private void editorInfo() {
        showLoadding();
        ApiResource.updateDutyBeatsInfo(getRequestJson(), new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int i, Header[] headers, byte[] bytes) {
                stopLoading();
                String result =new String(bytes);
                if (i == 200 && result.equals("true")){
                    refrshData();
                    toast("编辑巡逻段成功");
                }else {
                    toast("编辑巡逻段失败！");
                }
            }
            @Override
            public void onFailure(int i, Header[] headers, byte[] bytes, Throwable throwable) {
                toast("编辑巡逻段失败！");
                stopLoading();
            }
        });
    }

    //巡逻段删除
    private void delInfo(int position) {
        showLoadding();
        JSONObject jo=new JSONObject();
        jo.put("BID",(((JSONObject)rankArray.get(position)).getString("BID")));
        ApiResource.delDutyBeatsInfo(jo.toJSONString(), new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int i, Header[] headers, byte[] bytes) {
                stopLoading();
                String result =new String(bytes);
                if (i == 200 && result.equals("true")){
                    requestData();
                    toast("删除巡逻段成功");
                }else {
                    toast("删除巡逻段失败！");
                }
            }
            @Override
            public void onFailure(int i, Header[] headers, byte[] bytes, Throwable throwable) {
                toast("删除巡逻段失败！");
                stopLoading();
            }
        });
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        menu.findItem(R.id.action_list).setIcon(R.drawable.ic_menu_add);
        menu.findItem(R.id.action_map).setVisible(false);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                this.finish();
                break;
            case R.id.action_list: //添加巡逻段
                flag=false;
                showDialogAlert(getString(R.string.duty_beats_add));
                break;

            default:
                break;
        }
        return true;
    }
    //初始化自定义添加巡段AlertDialog
    private void initDialog() {
        // 取得自定义View
        LayoutInflater layoutInflater = LayoutInflater.from(this);
        myAddView = layoutInflater.inflate(R.layout.aty_duty_beats_add, null);
        et_addname= (EditText) myAddView.findViewById(R.id.et_addname);
        et_adddes =(EditText) myAddView.findViewById(R.id.et_adddes);
        tv_addlevel= (TextView)myAddView.findViewById(R.id.tv_addlevel);
        tv_addtype= (TextView)myAddView.findViewById(R.id.tv_addtype);
        tv_rules= (TextView)myAddView.findViewById(R.id.tv_rules);
        tv_title= (TextView)myAddView.findViewById(R.id.tv_title);
        btn_cancel= (Button)myAddView.findViewById(R.id.btn_cancel);
        btn_add= (Button)myAddView.findViewById(R.id.btn_add);
        tv_addlevel.setOnClickListener(this);
        tv_addtype.setOnClickListener(this);
        btn_cancel.setOnClickListener(this);
        btn_add.setOnClickListener(this);
        tv_rules.setOnClickListener(this);
    }
    //点击时间监听
    @Override
    public void onClick(View view) {
        int  networkType = ((AppContext) getApplication()).getNetworkType();
        switch (view.getId()){
            case R.id.btn_query://点击查询
                if (((AppContext) getApplication()).getNetworkType() == 0) {
                    toast(getString(R.string.network_not_connected));
                } else{
                    requestData();
                }
                break;
            case R.id.tv_level://巡逻级别
                BaseDataUtils.showAlertDialog(this, getResources().getStringArray(R.array.duty_level),tv_level);
                break;
            case R.id.tv_type://巡逻类型
                BaseDataUtils.showAlertDialog(this, getResources().getStringArray(R.array.duty_type),tv_type);
                break;
            case R.id.tv_addlevel://巡逻级别
                BaseDataUtils.showAlertDialog(this, getResources().getStringArray(R.array.duty_level),tv_addlevel);
                break;
            case R.id.tv_addtype://巡逻类型
                BaseDataUtils.showAlertDialog(this, getResources().getStringArray(R.array.duty_type),tv_addtype);
                break;
            case R.id.tv_rules://获取排班规则
                if (networkType == 0) {
                    toast(getString(R.string.network_not_connected));
                } else if (mapRusles.size()<=0&&sRuleList.size()<=0){
                    getRulesData();
                }else {
                    BaseDataUtils.showAlertDialog(this, sRuleList,tv_rules);
                }
                break;
            case R.id.btn_add://添加巡逻段 以及编辑
                if (networkType == 0) {
                    toast(getString(R.string.network_not_connected));
                } else if (et_addname.getText().toString().isEmpty()){
                    toast("巡逻段名字不能为空！");
                    dismissAlert(false);
                }else if (et_adddes.getText().toString().isEmpty()){
                    toast("巡逻段描述不能为空！");
                    dismissAlert(false);
                }else if (tv_addlevel.getText().toString().isEmpty()){
                    toast("请选择巡逻段级别！");
                    dismissAlert(false);
                }else if (tv_addtype.getText().toString().isEmpty()){
                    toast("请选择巡逻方式！");
                    dismissAlert(false);
                }else if (tv_rules.getText().toString().isEmpty()){
                    toast("请选择排班规则！");
                    dismissAlert(false);
                }else {
                    if (flag){//编辑巡逻段信息
                        editorInfo();
                    }else {//上传巡逻段信息
                        uploadInfo();
                    }
                }
                break;
            case R.id.btn_cancel://取消巡逻段信息
                clearText();
                break;
        }
    }

    private String getReques() {
        JSONObject requestJson = new JSONObject();
        requestJson.put("DEPT_ID", userInfo.getOrgancode());
        requestJson.put("RULE_DUTY", "01");
        requestJson.put("BEATS_SET", "01");//过滤未设置勤务班次的规则
        return requestJson.toJSONString();
    }

    private void getRulesData() {
        showLoadding();
        ApiResource.getDutyFlightReulsList(getReques(), new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int i, Header[] headers, byte[] bytes) {
                stopLoading();
                String result = new String(bytes);
                if (i == 200 && result.length()>3){
                    try {
                        List<DutyFlightRulesModle> Ruleslist = JSON.parseArray(result, DutyFlightRulesModle.class);
                        if (Ruleslist.size()>0){
                            for (DutyFlightRulesModle info:Ruleslist){
                                sRuleList.add(info.getRULE_NAME());
                                mapRusles.put(info.getRULE_NAME(),info.getID());
                            }
                        }
                    } catch (JSONException e){
                        toast("获取数据失败");
                    }
                }else {
                    toast(getString(R.string.duty_flight_data));
                }
            }
            @Override
            public void onFailure(int i, Header[] headers, byte[] bytes, Throwable throwable) {
                toast("获取数据失败");
            }
        });
    }


    private void showDialogAlert(String title) {
        tv_title.setText(title);
        alertDialog=new AlertDialog.Builder(DutyBeatsAddAty.this)
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
    //上传巡逻段信息
    private void uploadInfo() {
        showLoadding();
        ApiResource.addDutyBeatsInfo(getRequestJson(), new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int i, Header[] headers, byte[] bytes) {
                stopLoading();
                String result =new String(bytes);
                if (i == 200&&result.length() > 5){
                    JSONObject jo= JSON.parseObject(result);
                    long IDs=Long.parseLong(jo.get("BID").toString());
                    refrshData();
                    Intent intent = new Intent(DutyBeatsAddAty.this,DutyBoxAddAty.class);
                    intent.putExtra("data",""+IDs);
                    startActivity(intent);
                    toast("添加巡逻段成功，继续设置签到点！");
                }else {
                    onFailure(i,headers,bytes,null);
                }
                InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                if (imm != null) {
                    imm.hideSoftInputFromWindow(DutyBeatsAddAty.this.getWindow().getDecorView().getWindowToken(), 0);
                }
            }

            @Override
            public void onFailure(int i, Header[] headers, byte[] bytes, Throwable throwable) {
                toast("添加巡逻段失败！");
                stopLoading();
            }
        });
    }

    public String getRequestJson(){
        DutyBeatsModle dutyBeatsInfo=new DutyBeatsModle();
        dutyBeatsInfo.setBEAT_LEVEL(BaseDataUtils.getLevelNum(tv_addlevel.getText().toString()));
        dutyBeatsInfo.setBEAT_TYPE(BaseDataUtils.getTypeNum(tv_addtype.getText().toString())) ;
        dutyBeatsInfo.setBEAT_DESCRIBE(et_adddes.getText().toString());
        dutyBeatsInfo.setBID_NAME(et_addname.getText().toString());
        dutyBeatsInfo.setSUBS_ID(getString(R.string.SUBS_ID)) ;
        dutyBeatsInfo.setSUBS_NAME(getString(R.string.SUBS_NAME));
        dutyBeatsInfo.setDEPT_ID(userInfo.getOrgancode()) ;
        dutyBeatsInfo.setDEPT_NAME(userInfo.getPcs());
        dutyBeatsInfo.setCREATOR_NAME(userInfo.getName());
        dutyBeatsInfo.setCREATOR_ID(userInfo.getId());
        dutyBeatsInfo.setCREATOR_SJHM(userInfo.getUsername());
        dutyBeatsInfo.setFRID(mapRusles.get(tv_rules.getText().toString().trim()));
        JSONObject jo=JSON.parseObject(JSON.toJSONString(dutyBeatsInfo));
        if (flag){
            jo.put("BID",((JSONObject)rankArray.get(position)).getString("BID"));
        }
        return jo.toJSONString();
    }

    private void refrshData(){
        requestData();
        clearText();
    }

    private void clearText() {
        dismissAlert(true);
        ((FrameLayout)myAddView.getParent()).removeView(myAddView);
        //上传成功后清除控件数据
        tv_addlevel.setText("");
        tv_addtype.setText("");
        et_adddes.setText("");
        tv_rules.setText("");
    }

    @Override
    public boolean enableBackUpBtn() {
        return true;
    }


    @Override
    public String getAtyTitle() {
        return getString(R.string.duty_beats_add);
    }


}