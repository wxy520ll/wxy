package cn.net.xinyi.xmjt.activity.Collection.SafetyManage.OneCamera;

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
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.util.TypeUtils;
import com.loopj.android.http.AsyncHttpResponseHandler;

import org.apache.http.Header;

import java.lang.reflect.Field;
import java.util.Collection;
import java.util.List;

import cn.net.xinyi.xmjt.R;
import cn.net.xinyi.xmjt.api.ApiResource;
import cn.net.xinyi.xmjt.config.AdapterHolder;
import cn.net.xinyi.xmjt.config.BaseListAdp;
import cn.net.xinyi.xmjt.config.BaseListAty;
import cn.net.xinyi.xmjt.model.MachineModle;
import cn.net.xinyi.xmjt.utils.DialogHelper;
import cn.net.xinyi.xmjt.utils.UI;

public class MachineRoomPcsAty extends BaseListAty implements View.OnClickListener{

    private Button btn_cancel;
    private TextView tv_ssdw;
    private EditText et_jfmc;
    private Button btn_add;
    private View myAddView;
    private AlertDialog alertDialog;
    private int position;
    private List<MachineModle> machineModles;
    private boolean flag;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        TypeUtils.compatibleWithJavaBean = true;
        requestData();
        initView();
    }

    private void initView() {
        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int positio, long id) {
                Intent intent=new Intent(MachineRoomPcsAty.this,MachineRoomPersonAty.class);
                intent.putExtra("ROOMID",machineModles.get(positio).getROOMID());
                startActivityForResult(intent,10001);
            }
        });

        mListView.setOnCreateContextMenuListener(new View.OnCreateContextMenuListener() {
            @Override
            public void onCreateContextMenu(ContextMenu menu, View view, ContextMenu.ContextMenuInfo contextMenuInfo) {
                menu.add(0, 0, 0, getString(R.string.editor));
                menu.add(0, 1, 0,  getString(R.string.del));
                menu.add(0, 2, 0,  getString(R.string.cancel));
            }
        });
        initDialog();
    }


    @Override
    protected void requestData() {
        showLoadding();
        JSONObject requestJson = new JSONObject();
        requestJson.put("PCSBM", getIntent().getStringExtra("PCSBM"));
        ApiResource.MachineRoomPcsMain(requestJson.toJSONString(),new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int i, Header[] headers, byte[] bytes) {
                if (null!=machineModles){
                    machineModles.clear();
                }
                String result = new String(bytes);
                if (result!=null&&result.length()>5){
                    machineModles= JSON.parseArray(result, MachineModle.class);
                    mNodata.setVisibility(View.GONE);
                    MachineRoomPcsAdp  adp = new MachineRoomPcsAdp(mListView,machineModles,R.layout.aty_machine_room_pcs_adp,MachineRoomPcsAty.this);
                    mListView.setAdapter(adp);
                } else {
                    showDialogAlert();
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


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode==10001&&resultCode==RESULT_OK){
            requestData();//刷新数据
        }
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
                DialogHelper.showAlertDialog("确定删除机房信息！", MachineRoomPcsAty.this, new DialogHelper.OnOptionClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which, Object o) {
                        delInfo(position);
                    }
                });
                break;

            case 2://取消
                toast(getString(R.string.cancel));
                break;
        }
        return super.onContextItemSelected(item);
    }



    private void setData() {
        flag=true;
        et_jfmc.setText(machineModles.get(position).getMC());
        showDialogAlert();
    }


    private String getRequestJson() {
        MachineModle machineModle=new MachineModle();
        machineModle.setCJYH(userInfo.getUsername());
        machineModle.setMC(et_jfmc.getText().toString());
        machineModle.setPCSBM(getIntent().getStringExtra("PCSBM"));
        machineModle.setSTATUS(1);
        if (flag){
            machineModle.setID(machineModles.get(position).getROOMID());
        }
        return JSONObject.toJSONString(machineModle);
    }


    //上传机房管理员信息
    private void uploadInfo() {
        showLoadding();
        ApiResource.MachineRoomAdd(getRequestJson(), new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int i, Header[] headers, byte[] bytes) {
                stopLoading();
                String result =new String(bytes);
                if (i == 200&&result.length() > 4){
                    JSONObject jo= JSON.parseObject(result);
                    refrshData();
                    Intent intent = new Intent(MachineRoomPcsAty.this,MachineRoomPersonAty.class);
                    intent.putExtra("ROOMID",Integer.parseInt(jo.get("ID").toString()));
                    startActivity(intent);
                    toast("添加机房成功，请设置管理员！");

                }else {
                    onFailure(i,headers,bytes,null);
                }
                InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                if (imm != null) {
                    imm.hideSoftInputFromWindow(MachineRoomPcsAty.this.getWindow().getDecorView().getWindowToken(), 0);
                }
            }

            @Override
            public void onFailure(int i, Header[] headers, byte[] bytes, Throwable throwable) {
                toast("添加机房失败！");
                stopLoading();
            }
        });
    }


    private void refrshData(){
        requestData();
        clearText();
    }

    //巡逻段编辑
    private void editorInfo() {
        showLoadding();
        ApiResource.MachineRoomUpdate(getRequestJson(), new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int i, Header[] headers, byte[] bytes) {
                stopLoading();
                String result =new String(bytes);
                if (i == 200 && result.equals("true")){
                    refrshData();
                    toast("编辑机房成功");
                }else {
                    toast("编辑机房失败！");
                }
            }
            @Override
            public void onFailure(int i, Header[] headers, byte[] bytes, Throwable throwable) {
                toast("编辑机房失败！");
                stopLoading();
            }
        });
    }


    //巡逻段删除
    private void delInfo(int position) {
        showLoadding();
        JSONObject jo=new JSONObject();
        jo.put("ID",machineModles.get(position).getROOMID());
        ApiResource.MachineRoomDelete(jo.toJSONString(), new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int i, Header[] headers, byte[] bytes) {
                stopLoading();
                String result =new String(bytes);
                if (i == 200 && result.equals("true")){
                    requestData();
                    toast("删除机房成功");
                }else {
                    toast("删除机房失败！");
                }
            }
            @Override
            public void onFailure(int i, Header[] headers, byte[] bytes, Throwable throwable) {
                toast("删除机房失败！");
                stopLoading();
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        menu.findItem(R.id.action_map).setVisible(false);
        menu.findItem(R.id.action_list).setIcon(R.drawable.ic_menu_add);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                this.finish();
                break;

            case R.id.action_list:
                flag=false;
                showDialogAlert();
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
        myAddView = layoutInflater.inflate(R.layout.aty_machine_room_add, null);
        tv_ssdw= (TextView) myAddView.findViewById(R.id.tv_ssdw);
        et_jfmc= (EditText) myAddView.findViewById(R.id.et_jfmc);
        btn_cancel= (Button)myAddView.findViewById(R.id.btn_cancel);
        btn_add= (Button)myAddView.findViewById(R.id.btn_add);
        btn_cancel.setOnClickListener(this);
        btn_add.setOnClickListener(this);
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btn_add:
                if (et_jfmc.getText().toString().isEmpty()) {
                    UI.toast(MachineRoomPcsAty.this, "机房名称不能为空！");
                }else if (flag){
                    editorInfo();
                }else {
                    uploadInfo();
                }
                break;
            case R.id.btn_cancel:
                clearText();
                break;

        }
    }

    private void showDialogAlert() {
        tv_ssdw.setText(getIntent().getStringExtra("PCSMC"));
        alertDialog=new AlertDialog.Builder(MachineRoomPcsAty.this)
                .setView(myAddView)
                .setCancelable(false).show();
    }

    private void clearText() {
        dismissAlert(true);
        ((FrameLayout)myAddView.getParent()).removeView(myAddView);
        //上传成功后清除控件数据
        tv_ssdw.setText("");
        et_jfmc.setText("");
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

    @Override
    public String getAtyTitle() {
        return getString(R.string.machine_pcs_list);
    }

    @Override
    public boolean enableBackUpBtn() {
        setResult(RESULT_OK);
        return true;
    }


    class MachineRoomPcsAdp extends BaseListAdp<MachineModle>{

        public MachineRoomPcsAdp(AbsListView view, Collection<MachineModle> datas, int itemLayoutId, Context context) {
            super(view, datas, itemLayoutId, context);
        }

        @Override
        public void convert(AdapterHolder helper, MachineModle item, boolean isScrolling) {
            helper.setText(R.id.tv_mc,"机房名称："+item.getMC());
            if (null!=item.getDATALIST()){
                List<MachineModle> machinePersion = JSON.parseArray(item.getDATALIST(), MachineModle.class);
                StringBuilder sbBuilder = new StringBuilder();
                sbBuilder.append("机房管理员:");
                for (MachineModle per:machinePersion){
                    sbBuilder.append("\n\n"+per.getNAME()+"("+ per.getUSERNAME()+")");
                }
                helper.setText(R.id.tv_gly,sbBuilder.toString().length()>10?sbBuilder.toString():"暂未设置管理员，请点击添加");
            }
        }
    }
}
