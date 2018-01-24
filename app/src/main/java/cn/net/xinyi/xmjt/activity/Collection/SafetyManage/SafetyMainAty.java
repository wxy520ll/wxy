package cn.net.xinyi.xmjt.activity.Collection.SafetyManage;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;

import com.alibaba.fastjson.JSONObject;
import com.loopj.android.http.AsyncHttpResponseHandler;

import org.apache.http.Header;

import cn.net.xinyi.xmjt.R;
import cn.net.xinyi.xmjt.activity.Collection.SafetyManage.EntryandExitApproval.EntryandExitApprovalAty;
import cn.net.xinyi.xmjt.activity.Collection.SafetyManage.KpiCertificate.KpiMainAty;
import cn.net.xinyi.xmjt.activity.Collection.SafetyManage.OneCamera.OneCameraMainAty;
import cn.net.xinyi.xmjt.activity.Main.LoginActivity;
import cn.net.xinyi.xmjt.activity.Main.TabMenuActivity;
import cn.net.xinyi.xmjt.api.ApiResource;
import cn.net.xinyi.xmjt.config.BaseActivity2;
import cn.net.xinyi.xmjt.utils.BaseDataUtils;
import cn.net.xinyi.xmjt.utils.DialogHelper;
import cn.net.xinyi.xmjt.utils.SharedPreferencesUtil;
import cn.net.xinyi.xmjt.utils.StringUtils;
import cn.net.xinyi.xmjt.utils.UI;
import cn.net.xinyi.xmjt.v527.manager.ModelItemManager;

public class SafetyMainAty extends BaseActivity2 implements View.OnClickListener {

    private EditText et_sfz;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.aty_safe_main);
        findViewById(R.id.ll_kpi).setOnClickListener(this);
        findViewById(R.id.ll_oncecamera).setOnClickListener(this);
        findViewById(R.id.ll_enterandexit).setOnClickListener(this);
        if (ModelItemManager.canLook(12)) {
            findViewById(R.id.ll_enterandexit).setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.ll_kpi:
                if (!BaseDataUtils.isPOLICE() && !SharedPreferencesUtil.getBoolean(this, userInfo.getUsername(), false)) {
                    View view = LayoutInflater.from(this).inflate(R.layout.kpi_sfz_add, null);
                    et_sfz = (EditText) view.findViewById(R.id.ed_sfz);
                    if (!StringUtils.isEmpty(userInfo.getSfzh())) {
                        et_sfz.setText(userInfo.getSfzh());
                    }
                    AlertDialog alertDialog = new AlertDialog.Builder(this)
                            .setView(view)
                            .setNegativeButton(R.string.cancle, null)
                            .setPositiveButton(R.string.sure, new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    if (et_sfz.getText().toString().length() < 15) {
                                        UI.toast(SafetyMainAty.this, "身份证号码格式不对!");
                                    } else {
                                        updateData();
                                    }
                                }
                            }).create();
                    alertDialog.show();
                } else {
                    showActivity(KpiMainAty.class);
                }
                break;

            case R.id.ll_oncecamera:
                showActivity(OneCameraMainAty.class);
                break;

            case R.id.ll_enterandexit:
                showActivity(EntryandExitApprovalAty.class);
                break;

        }
    }


    private String getRequest() {
        JSONObject requestJson = new JSONObject();
        requestJson.put("SFZH", et_sfz.getText().toString());
        return requestJson.toJSONString();
    }

    private void updateData() {
        showLoadding();
        ApiResource.updateSfzhm(getRequest(), new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int i, Header[] headers, byte[] bytes) {
                stopLoading();
                String result = new String(bytes);
                if (result.equals("true")) {
                    DialogHelper.showAlertDialog(SafetyMainAty.this, "身份证信息匹配成功，请重新登录", new DialogHelper.OnOptionClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which, Object o) {
                            SharedPreferencesUtil.putBoolean(SafetyMainAty.this, userInfo.getUsername(), true);
                            // 点击“确认”后的操作
                            Intent intent = new Intent(SafetyMainAty.this, LoginActivity.class);
                            startActivity(intent);
                            if (TabMenuActivity.instace!=null) {
                                TabMenuActivity.instace.finish();
                            }
                            SafetyMainAty.this.finish();
                        }
                    });
                } else {
                    onFailure(i, headers, bytes, null);
                }
            }

            @Override
            public void onFailure(int i, Header[] headers, byte[] bytes, Throwable throwable) {
                stopLoading();
                if (StringUtils.isEmpty(new String(bytes))) {
                    UI.toast(SafetyMainAty.this, "获取数据失败");
                } else {
                    UI.toast(SafetyMainAty.this, new String(bytes));
                }
            }
        });
    }

    @Override
    public boolean enableBackUpBtn() {
        return true;
    }


    @Override
    public String getAtyTitle() {
        return getString(R.string.safety_manage);
    }
}
