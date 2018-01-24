package cn.net.xinyi.xmjt.activity.Collection.SafetyManage.OneCamera;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.loopj.android.http.AsyncHttpResponseHandler;

import org.apache.http.Header;

import cn.net.xinyi.xmjt.R;
import cn.net.xinyi.xmjt.activity.Main.Setting.ModifyForComanyAty;
import cn.net.xinyi.xmjt.api.ApiResource;
import cn.net.xinyi.xmjt.config.AppContext;
import cn.net.xinyi.xmjt.config.BaseListAty;
import cn.net.xinyi.xmjt.utils.AnnotateManager;
import cn.net.xinyi.xmjt.utils.BaseDataUtils;
import cn.net.xinyi.xmjt.utils.DialogHelper;
import cn.net.xinyi.xmjt.utils.UI;

/**
 * Created by hao.zhou on 2016/11/13.
 */
public class MachineApprovalCheckAty extends BaseListAty {
    private JSONArray jsArray;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        AnnotateManager.initBindView(this);//注解式绑定控件
        if (getIntent().getFlags()==1) {
            requestData();//获取审批记录
        }else if (getIntent().getFlags()==2){
            requestMachPersionData();//获取审批记录
        }else {
            requestSQData();//获取申请人记录
        }
        initClick();
    }

    @Override
    protected void requestData() {
        showLoadding();
        JSONObject requestJson = new JSONObject();
        requestJson.put("SHRY", userInfo.getUsername());
        ApiResource.MachineRoomCheckList(requestJson.toJSONString(),new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int i, Header[] headers, byte[] bytes) {
                String result = new String(bytes);
                if (result!=null&&result.length()>5){
                    jsArray= JSON.parseArray(result);
                    mNodata.setVisibility(View.GONE);
                    MachineApprovalCheckAdp adp = new MachineApprovalCheckAdp(MachineApprovalCheckAty.this,jsArray);
                    mListView.setAdapter(adp);
                } else {
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

    private void requestMachPersionData() {
        showLoadding();
        JSONObject requestJson = new JSONObject();
        requestJson.put("SJHM", userInfo.getUsername());
        ApiResource.MachineRoomPersonLists(requestJson.toJSONString(),new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int i, Header[] headers, byte[] bytes) {
                String result = new String(bytes);
                if (result!=null&&result.length()>5){
                    jsArray= JSON.parseArray(result);
                    mNodata.setVisibility(View.GONE);
                    MachineApprovalCheckAdp adp = new MachineApprovalCheckAdp(MachineApprovalCheckAty.this,jsArray);
                    mListView.setAdapter(adp);
                } else {
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

    private void requestSQData() {
        showLoadding();
        JSONObject requestJson = new JSONObject();
        requestJson.put("SJHM", userInfo.getUsername());
        ApiResource.MachineApproveAll(requestJson.toJSONString(),new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int i, Header[] headers, byte[] bytes) {
                String result = new String(bytes);
                if (result!=null&&result.length()>5){
                    jsArray= JSON.parseArray(result);
                    mNodata.setVisibility(View.GONE);
                    MachineApprovalCheckAdp adp = new MachineApprovalCheckAdp(MachineApprovalCheckAty.this,jsArray);
                    mListView.setAdapter(adp);
                } else {
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


    private void initClick() {
        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (getIntent().getFlags()==1||getIntent().getFlags()==2){
                    Intent intent=new Intent(MachineApprovalCheckAty.this,MachineApprovalCheckDetaAty.class);
                    intent.putExtra("DATA",((JSONObject)jsArray.get(position)));
                    startActivityForResult(intent,10001);
                }else {
                    Intent intent=new Intent(MachineApprovalCheckAty.this,MachineApprovalAty.class);
                    intent.putExtra("DATA",((JSONObject)jsArray.get(position)));
                    startActivityForResult(intent,10001);
                }
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode==RESULT_OK){
            if (getIntent().getFlags()==1) {
                requestData();//获取审批记录
            }else if (getIntent().getFlags()==2){
                requestMachPersionData();//获取审批记录
            }else {
                requestSQData();//获取申请人记录
            }
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        if (getIntent().getFlags()!=1&&getIntent().getFlags()!=2){
            getMenuInflater().inflate(R.menu.main, menu);
            MenuItem action_list = menu.findItem(R.id.action_list);
            action_list.setIcon(R.drawable.ic_menu_add);
            MenuItem action_map = menu.findItem(R.id.action_map);
            action_map.setVisible(false);
        }
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                this.finish();
                break;

            case R.id.action_list:
                if (BaseDataUtils.isCompanyOrOther()==1&&(null==userInfo.getSfzzp()||userInfo.getSfzzp().isEmpty())){
                    DialogHelper.showAlertDialog("个人信息不全，请上传身份证照片", MachineApprovalCheckAty.this, new DialogHelper.OnOptionClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which, Object o) {
                            showActivity(ModifyForComanyAty.class);
                            MachineApprovalCheckAty.this.finish();
                        }
                    });
                }else if (!AppContext.Score){
                    UI.toast(MachineApprovalCheckAty.this,"请进行安全学习并通过考核才能进行施工申请！");
                }else {
                    Intent intent=new Intent(this,MachineApprovalAty.class);
                    startActivityForResult(intent,1001);
                }
                break;

            default:
                break;
        }
        return true;
    }

    @Override
    public String getAtyTitle() {
        return BaseDataUtils.isCompanyOrOther()==1? getString(R.string.safety_approval):getString(R.string.safety_approval_check);
    }

    @Override
    public boolean enableBackUpBtn() {
        return true;
    }

}
