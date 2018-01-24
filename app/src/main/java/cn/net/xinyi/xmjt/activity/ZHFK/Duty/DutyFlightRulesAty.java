package cn.net.xinyi.xmjt.activity.ZHFK.Duty;


import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.ContextMenu;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONException;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.util.TypeUtils;
import com.loopj.android.http.AsyncHttpResponseHandler;

import org.apache.http.Header;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import cn.net.xinyi.xmjt.R;
import cn.net.xinyi.xmjt.api.ApiResource;
import cn.net.xinyi.xmjt.config.AdapterHolder;
import cn.net.xinyi.xmjt.config.AppContext;
import cn.net.xinyi.xmjt.config.BaseListAdp;
import cn.net.xinyi.xmjt.config.BaseListAty;
import cn.net.xinyi.xmjt.model.DutyFlightRulesModle;
import cn.net.xinyi.xmjt.utils.AnnotateManager;
import cn.net.xinyi.xmjt.utils.BaseDataUtils;

/**
 * Created by hao.zhou on 2016/6/29.
 */
public class DutyFlightRulesAty extends BaseListAty implements View.OnClickListener {
    private AlertDialog alertDialog;
    private List<DutyFlightRulesModle> list;
    private boolean flag;
    private int position;
    private EditText et_ruleName;
    private TextView tv_duty;
    private TextView tv_type;
    private Button btn_query;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        AnnotateManager.initBindView(this);//注解式绑定控件
        TypeUtils.compatibleWithJavaBean = true;
        list =new ArrayList<DutyFlightRulesModle>();
        DutyFlightRulesAddAdp mAdapter=new DutyFlightRulesAddAdp(mListView,list,R.layout.aty_duty_beats_add_item,this);
        setAdapter(mAdapter);
        mAdapter.setState(BaseListAdp.STATE_LOAD_MORE);
        initBaseData();
    }

    private void initBaseData() {
        requestData();
        initLongClick();
    }

    private String getRequest() {
        JSONObject requestJson = new JSONObject();
        requestJson.put("RULE_DUTY", BaseDataUtils.dutyRulesTypeToDutyNum(tv_type.getText().toString()));
        requestJson.put("DEPT_ID", userInfo.getOrgancode());
        requestJson.put("PAGENUMBER",mCurrentPage);
        requestJson.put("PAGESIZE",PAGE_SIZE);
        return requestJson.toJSONString();
    }

    @Override
    protected void requestData() {
        showLoadding();
        ApiResource.getDutyFlightReulsList(getRequest(), new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int i, Header[] headers, byte[] bytes) {
                if (mCurrentPage==1)
                    stopLoading();
                String result = new String(bytes);
                try {
                    list = JSON.parseArray(result,DutyFlightRulesModle.class);
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
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {
        AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();
        position = (int) info.id;// 这里的info.id对应的就是数据库中_id的值
        switch (item.getItemId()){
            case 0://编辑
                flag=true;
                //上传成功后清除控件数据
                showDialogAlert(getString(R.string.duty_flight_rules_edt));
                break;

            case 1://删除
                new AlertDialog.Builder(DutyFlightRulesAty.this).setTitle(getString(R.string.tips))
                        .setMessage("确定删除排班信息！")
                        .setPositiveButton(getString(R.string.sure), new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                delInfo(position);
                            }
                        })
                        .setNegativeButton(getString(R.string.cancel),null).show();
                break;

            case 2://取消
                toast(getString(R.string.cancel));
                break;
        }
        return super.onContextItemSelected(item);
    }

    //排班编辑
    private void editorInfo() {
        showLoadding();
        ApiResource.edtDutyFlightRules(getRequestJson(), new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int i, Header[] headers, byte[] bytes) {
                stopLoading();
                String result =new String(bytes);
                if (i == 200 && result.equals("true")){
                    refrshData();
                    toast("编辑成功");
                }else {
                    toast("编辑失败！");
                }
            }
            @Override
            public void onFailure(int i, Header[] headers, byte[] bytes, Throwable throwable) {
                toast("编辑失败！");
                stopLoading();
            }
        });
    }

    //排班删除
    private void delInfo(int position) {
        showLoadding();
        JSONObject jo=new JSONObject();
        jo.put("ID",((DutyFlightRulesModle)getAdapter().getData().get(position)).getID());
        ApiResource.delDutyFlightRules(jo.toJSONString(), new AsyncHttpResponseHandler() {
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
    public void onItemClick(AdapterView<?> parent, View view, int position, long id, Object t) {
        super.onItemClick(parent, view, position, id, t);
        if (t instanceof DutyFlightRulesModle){
            Intent intent = new Intent(this,DutyFlightsAty.class);
            intent.putExtra("data",((DutyFlightRulesModle)t).getID());
            startActivity(intent);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        MenuItem action_list = menu.findItem(R.id.action_list);
        action_list.setIcon(R.drawable.ic_menu_add);
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
            case R.id.action_list: //添加排班
                flag=false;
                showDialogAlert(getString(R.string.duty_flight_rules_add));
                break;
        }
        return true;
    }


    private void showDialogAlert(String title) {
        LinearLayout container = new LinearLayout(DutyFlightRulesAty.this);
        container.setOrientation(LinearLayout.VERTICAL);
        tv_duty = new TextView(DutyFlightRulesAty.this);
        tv_duty.setBackgroundColor(getResources().getColor(R.color.white));
        tv_duty.setHint("请选择排班类别");
        tv_duty.setMaxEms(20);
        tv_duty.setLines(3);
        tv_duty.setTextSize(14);
        tv_duty.setPadding(20,0,0,0);
        tv_duty.setGravity(Gravity.CENTER_VERTICAL);

        final TextView tv = new TextView(DutyFlightRulesAty.this);
        tv.setBackgroundColor(getResources().getColor(R.color.bg_dark));
        tv.setHeight(2);

        et_ruleName = new EditText(DutyFlightRulesAty.this);
        et_ruleName.setLines(3);
        et_ruleName.setMaxEms(20);
        et_ruleName.setTextSize(14);
        et_ruleName.setHint("请输入排班名称，如：龙岗分局巡段001");
        et_ruleName.setBackgroundColor(getResources().getColor(R.color.white));
        tv_duty.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                BaseDataUtils.showAlertDialog(DutyFlightRulesAty.this,getResources().getStringArray(R.array.duty_rulesduty),tv_duty);
            }
        });

        if (flag){
            et_ruleName.setText(((DutyFlightRulesModle)getAdapter().getData().get(position)).getRULE_NAME());
            tv_duty.setText(BaseDataUtils.dutyRulesNumToDutyRulesType(((DutyFlightRulesModle)getAdapter().getData().get(position)).getRULE_DUTY()));
        }

        container.addView(et_ruleName);
        container.addView(tv);
        container.addView(tv_duty);

        alertDialog=new AlertDialog.Builder(DutyFlightRulesAty.this)
                .setTitle(title)
                .setView(container)
                .setNegativeButton(R.string.sure, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        if (et_ruleName.getText().toString().isEmpty()){
                            toast("请输入排班规则名称！");
                            dismissAlert(false);
                        } else if (tv_duty.getText().toString().isEmpty()){
                            toast("请输入排班类型！");
                            dismissAlert(false);
                        }else {
                            if (flag){//编辑排班信息
                                editorInfo();
                            }else {//上传排班信息
                                uploadInfo();
                            }
                        }
                    }
                }).setPositiveButton(R.string.cancel, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        clearText();
                        alertDialog.dismiss();
                    }
                })
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
    //上传排班信息
    private void uploadInfo() {
        showLoadding();
        ApiResource.addDutyFlightRules(getRequestJson(), new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int i, Header[] headers, byte[] bytes) {
                stopLoading();
                String result =new String(bytes);
                if (i == 200 && result != "0"){
                    refrshData();
                    String IDs=JSON.parseObject(result).get("ID").toString();
                    Intent intent = new Intent(DutyFlightRulesAty.this,DutyFlightsAty.class);
                    intent.putExtra("data",""+IDs);
                    startActivity(intent);
                    toast("添加名称成功，继续添加勤务班次信息！");
                }else {
                    onFailure(i,headers,bytes,null);
                }
                InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                if (imm != null) {
                    imm.hideSoftInputFromWindow(DutyFlightRulesAty.this.getWindow().getDecorView().getWindowToken(), 0);
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
        DutyFlightRulesModle Info=new DutyFlightRulesModle();
        Info.setCREATOR_ID(userInfo.getId());
        Info.setCREATOR_NAME(userInfo.getName());
        Info.setSUBS_ID(getString(R.string.SUBS_ID));
        Info.setSUBS_NAME(getString(R.string.SUBS_NAME));
        Info.setDEPT_ID(userInfo.getOrgancode());
        Info.setDEPT_NAME(userInfo.getPcs());
        Info.setRULE_NAME(et_ruleName.getText().toString());
        Info.setRULE_DUTY(BaseDataUtils.dutyRulesTypeToDutyNum(tv_duty.getText().toString()));
        JSONObject jo=JSON.parseObject(JSON.toJSONString(Info));
        if (flag){
            jo.put("ID",((DutyFlightRulesModle)getAdapter().getData().get(position)).getID());
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
        et_ruleName.setText("");
        et_ruleName.setText("");
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
        tv_type.setText("巡段");
        tv_type.setOnClickListener(this);

        btn_query=(Button)addView.findViewById(R.id.btn_query);//查询
        btn_query.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.tv_type:
                BaseDataUtils.showAlertDialog(this, getResources().getStringArray(R.array.duty_rulesduty),tv_type);
                break;

            case R.id.btn_query:
                if (((AppContext) getApplication()).getNetworkType() == 0) {
                    toast(getString(R.string.network_not_connected));
                } else{
                    if (null!=list){
                        mCurrentPage=1;
                        getAdapter().getData().clear();
                    }
                    requestData();
                }
                break;
        }
    }

    @Override
    public boolean enableBackUpBtn() {
        return true;
    }


    @Override
    public String getAtyTitle() {
        return getString(R.string.duty_flight_rules);
    }



    public class DutyFlightRulesAddAdp extends BaseListAdp<DutyFlightRulesModle> {
        public DutyFlightRulesAddAdp(AbsListView view, Collection<DutyFlightRulesModle> mDatas, int itemLayoutId, Context mContext) {
            super(view, mDatas, itemLayoutId, mContext);
        }

        @Override
        public void convert(AdapterHolder helper, DutyFlightRulesModle item, boolean isScrolling) {
            helper.setText(R.id.tv_xh,""+(helper.getPosition()+1));
            helper.setText(R.id.tv_name,item.getRULE_NAME());
        }
    }

}