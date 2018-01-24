package cn.net.xinyi.xmjt.activity.Collection.SafetyManage.OneCamera;

import android.app.AlertDialog;
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

public class MachineRoomPersonAty extends BaseListAty implements View.OnClickListener{

    private Button btn_cancel;
    private EditText et_glyhm;
    private EditText et_glymz;
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
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

            }
        });
        mListView.setOnCreateContextMenuListener(new View.OnCreateContextMenuListener() {
            @Override
            public void onCreateContextMenu(ContextMenu menu, View view, ContextMenu.ContextMenuInfo contextMenuInfo) {
                menu.add(0, 0, 0, getString(R.string.editor));
                menu.add(0, 1, 0, getString(R.string.del));
                menu.add(0, 2, 0, getString(R.string.cancel));
            }
        });
        initDialog();
    }


    @Override
    protected void requestData() {
        showLoadding();
        JSONObject requestJson = new JSONObject();
        requestJson.put("ROOMID", getIntent().getSerializableExtra("ROOMID"));
        ApiResource.MachineRoomPersonList(requestJson.toJSONString(),new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int i, Header[] headers, byte[] bytes) {
                String result = new String(bytes);
                if (null!=machineModles){
                    machineModles.clear();
                }
                if (result!=null&&result.length()>5){
                    mNodata.setVisibility(View.GONE);
                    machineModles= JSON.parseArray(result, MachineModle.class);
                    MachineRoomPcsAdp adp = new MachineRoomPcsAdp(mListView,machineModles,R.layout.aty_machine_room_pcs_adp,MachineRoomPersonAty.this);
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
    public boolean onContextItemSelected(MenuItem item) {
        AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();
        position = (int) info.id;// 这里的info.id对应的就是数据库中_id的值
        switch (item.getItemId()){
            case 0://编辑
                setData();
                break;

            case 1://删除
                DialogHelper.showAlertDialog("确定删除管理员信息！", MachineRoomPersonAty.this, new DialogHelper.OnOptionClickListener() {
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
        et_glymz.setText(machineModles.get(position).getNAME());
        et_glyhm.setText(machineModles.get(position).getUSERNAME());
        showDialogAlert();
    }


    private String getRequestJson() {
        MachineModle machineModle=new MachineModle();
        machineModle.setCJYH(userInfo.getUsername());
        machineModle.setUSERNAME(et_glyhm.getText().toString());
        machineModle.setNAME(et_glymz.getText().toString());
        machineModle.setROOMID((Integer) getIntent().getSerializableExtra("ROOMID"));
        machineModle.setSTATUS(1);
        if (flag){
            machineModle.setID(machineModles.get(position).getID());
        }
        return JSONObject.toJSONString(machineModle);
    }


    //上传机房管理员信息
    private void uploadInfo() {
        showLoadding();
        ApiResource.MachineRoomPersonAdd(getRequestJson(), new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int i, Header[] headers, byte[] bytes) {
                stopLoading();
                String result =new String(bytes);
                if (i == 200&&result.length() > 4){
                    refrshData();
                    toast("添加管理员成功！");
                }else {
                    onFailure(i,headers,bytes,null);
                }
                InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                if (imm != null) {
                    imm.hideSoftInputFromWindow(MachineRoomPersonAty.this.getWindow().getDecorView().getWindowToken(), 0);
                }
            }

            @Override
            public void onFailure(int i, Header[] headers, byte[] bytes, Throwable throwable) {
                toast("添加管理员失败！");
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
        ApiResource.MachineRoomPersonUpdate(getRequestJson(), new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int i, Header[] headers, byte[] bytes) {
                stopLoading();
                String result =new String(bytes);
                if (i == 200 && result.equals("true")){
                    refrshData();
                    toast("编辑管理员成功");
                }else {
                    toast("编辑管理员失败！");
                }
            }
            @Override
            public void onFailure(int i, Header[] headers, byte[] bytes, Throwable throwable) {
                toast("编辑管理员失败！");
                stopLoading();
            }
        });
    }


    //巡逻段删除
    private void delInfo(int position) {
        showLoadding();
        JSONObject jo=new JSONObject();
        jo.put("ID",machineModles.get(position).getID());
        ApiResource.MachineRoomPersonDel(jo.toJSONString(), new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int i, Header[] headers, byte[] bytes) {
                stopLoading();
                String result =new String(bytes);
                if (i == 200 && result.equals("true")){
                    requestData();
                    toast("删除管理员成功");
                }else {
                    toast("删除管理员失败！");
                }
            }
            @Override
            public void onFailure(int i, Header[] headers, byte[] bytes, Throwable throwable) {
                toast("删除管理员失败！");
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
        myAddView = layoutInflater.inflate(R.layout.aty_machine_room_person_add, null);
        et_glyhm= (EditText) myAddView.findViewById(R.id.et_glyhm);
        et_glymz= (EditText) myAddView.findViewById(R.id.et_glymz);
        btn_cancel= (Button)myAddView.findViewById(R.id.btn_cancel);
        btn_add= (Button)myAddView.findViewById(R.id.btn_add);
        btn_cancel.setOnClickListener(this);
        btn_add.setOnClickListener(this);
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btn_add:
                if (et_glymz.getText().toString().isEmpty()) {
                    UI.toast(MachineRoomPersonAty.this, "管理员名字不能为空！");
                } else if (et_glyhm.getText().toString().isEmpty()||et_glyhm.getText().toString().length()<10){
                    UI.toast(MachineRoomPersonAty.this,"请输入正确的管理员号码！");
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
        alertDialog=new AlertDialog.Builder(MachineRoomPersonAty.this)
                .setView(myAddView)
                .setCancelable(false).show();
    }

    private void clearText() {
        dismissAlert(true);
        ((FrameLayout)myAddView.getParent()).removeView(myAddView);
        //上传成功后清除控件数据
        et_glyhm.setText("");
        et_glymz.setText("");
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
        return getString(R.string.machine_person_list);
    }

    @Override
    public boolean enableBackUpBtn() {
        setResult(RESULT_OK);
        return true;
    }


    class MachineRoomPcsAdp extends BaseListAdp<MachineModle> {

        public MachineRoomPcsAdp(AbsListView view, Collection<MachineModle> datas, int itemLayoutId, Context context) {
            super(view, datas, itemLayoutId, context);
        }

        @Override
        public void convert(AdapterHolder helper, MachineModle item, boolean isScrolling) {
            helper.setText(R.id.tv_mc,"管理员名字："+item.getNAME());
            helper.setText(R.id.tv_gly,"管理员电话："+item.getUSERNAME());
        }
    }
}
