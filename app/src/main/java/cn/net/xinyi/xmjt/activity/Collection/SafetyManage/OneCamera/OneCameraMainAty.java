package cn.net.xinyi.xmjt.activity.Collection.SafetyManage.OneCamera;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.loopj.android.http.AsyncHttpResponseHandler;

import org.apache.http.Header;

import cn.net.xinyi.xmjt.R;
import cn.net.xinyi.xmjt.activity.Main.Setting.ModifyForComanyAty;
import cn.net.xinyi.xmjt.api.ApiResource;
import cn.net.xinyi.xmjt.config.AppContext;
import cn.net.xinyi.xmjt.config.BaseActivity2;
import cn.net.xinyi.xmjt.model.MachineExamModle;
import cn.net.xinyi.xmjt.utils.AnnotateManager;
import cn.net.xinyi.xmjt.utils.BaseDataUtils;
import cn.net.xinyi.xmjt.utils.BaseUtil;
import cn.net.xinyi.xmjt.utils.BindView;
import cn.net.xinyi.xmjt.utils.DialogHelper;
import cn.net.xinyi.xmjt.utils.UI;
import cn.net.xinyi.xmjt.v527.manager.ModelItemManager;

public class OneCameraMainAty extends BaseActivity2 implements View.OnClickListener {
    /***机房管理**/
    @BindView(id = R.id.ll_safty_manage, click = true)
    LinearLayout ll_safty_manage;
    /***学习管理**/
    @BindView(id = R.id.ll_safty_study, click = true)
    LinearLayout ll_safty_study;
    /***学习考核**/
    @BindView(id = R.id.ll_safty_check, click = true)
    LinearLayout ll_safty_check;
    /***学习考核**/
    @BindView(id = R.id.ll_safty_approval, click = true)
    LinearLayout ll_safty_approval;
    /***学习审批**/
    @BindView(id = R.id.ll_safty_approval_check, click = true)
    LinearLayout ll_safty_approval_check;
    /***审批查看**/
    @BindView(id = R.id.ll_safty_approval_watch, click = true)
    LinearLayout ll_safty_approval_watch;
    /***机房登记**/
    @BindView(id = R.id.tv_kh)
    TextView tv_kh;
    private boolean scorePass = false;
    private JSONObject jo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.aty_oncamera_main);
        AnnotateManager.initBindView(this);  //控件绑定
        initView();
    }

    private void initView() {
        if (ModelItemManager.canLook(12)) {//厂商
            machineAdminPerson();//判断该人员是否是机房管理员
        } else if (ModelItemManager.canLook(0, 1, 2, 11)) {//科技通信科//管道公司
            ll_safty_manage.setVisibility(View.VISIBLE);
            ll_safty_approval_check.setVisibility(View.VISIBLE);
        }
        getCheckData();//获取考核数据
    }


    @Override
    public void onClick(View v) {
        if (BaseDataUtils.isFastClick()) {
        } else if (((AppContext) getApplication()).getNetworkType() == 0) {
            BaseUtil.showDialog(getString(R.string.network_not_connected), OneCameraMainAty.this);
        } else {
            switch (v.getId()) {
                case R.id.ll_safty_manage:
                    showActivity(MachineRoomAty.class);
                    break;

                case R.id.ll_safty_study:
                    showActivity(MachRoomStudyAty.class);
                    break;

                case R.id.ll_safty_check:
                    Intent intent = new Intent(OneCameraMainAty.this, OneCameraExamAty.class);
                    startActivityForResult(intent, 1001);
                    break;

                case R.id.ll_safty_approval:
                    if (BaseDataUtils.isCompanyOrOther() == 1 && (null == userInfo.getSfzzp() || userInfo.getSfzzp().isEmpty())) {
                        DialogHelper.showAlertDialog("个人信息不全，请上传身份证照片", OneCameraMainAty.this, new DialogHelper.OnOptionClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which, Object o) {
                                showActivity(ModifyForComanyAty.class);
                                OneCameraMainAty.this.finish();
                            }
                        });
                    } else if (!scorePass) {
                        UI.toast(OneCameraMainAty.this, "施工申请前请进行安全学习并考核得分大于90分为通过！");
                    } else {
                        getApprovl();
                    }
                    break;

                case R.id.ll_safty_approval_check:
                    Intent intent1 = new Intent(this, MachineApprovalCheckAty.class);
                    intent1.setFlags(1);
                    startActivity(intent1);
                    break;

                case R.id.ll_safty_approval_watch:
                    Intent intent2 = new Intent(this, MachineApprovalCheckAty.class);
                    intent2.setFlags(2);
                    startActivity(intent2);
                    break;
            }
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1001 && resultCode == RESULT_OK) {
            getCheckData();
        }
    }

    private void getCheckData() {
        showLoadding();
        JSONObject requestJson = new JSONObject();
        requestJson.put("CJYH", userInfo.getUsername());
        ApiResource.SaftyExamineQuery(requestJson.toJSONString(), new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int i, Header[] headers, byte[] bytes) {
                String result = new String(bytes);
                if (result != null && result.length() > 5) {
                    tv_kh.setVisibility(View.VISIBLE);
                    MachineExamModle info = JSON.parseArray(result, MachineExamModle.class).get(0);
                    if (info.getSCORE() >= MachineExamModle.ResuleScore) {
                        scorePass = true;
                        AppContext.Score = scorePass;
                        tv_kh.setTextColor(getResources().getColor(R.color.blue));
                        tv_kh.setText(info.getSCSJ() + "考核通过，得分" + info.getSCORE());
                    } else {
                        tv_kh.setTextColor(getResources().getColor(R.color.bbutton_danger));
                        if (info.getSCORE() < MachineExamModle.ResuleScore) {
                            tv_kh.setText(info.getCJYH() + "考核，得分" + info.getSCORE() + ",请重新学习并考核");
                        }
                    }
                } else {
                    tv_kh.setVisibility(View.GONE);
                }
                stopLoading();
            }

            @Override
            public void onFailure(int i, Header[] headers, byte[] bytes, Throwable throwable) {
                stopLoading();
                toast("获取数据失败");
                tv_kh.setVisibility(View.GONE);
            }
        });
    }

    private void getApprovl() {
        showLoadding();
        JSONObject requestJson = new JSONObject();
        requestJson.put("SJHM", userInfo.getUsername());
        ApiResource.MachineApproveLast(requestJson.toJSONString(), new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int i, Header[] headers, byte[] bytes) {
                String result = new String(bytes);
                if (result != null && result.length() > 5) {
                    showActivity(MachineApprovalCheckAty.class);
                } else {
                    showActivity(MachineApprovalAty.class);
                }
                stopLoading();
            }

            @Override
            public void onFailure(int i, Header[] headers, byte[] bytes, Throwable throwable) {
                stopLoading();
                toast("获取数据失败");
            }
        });
    }

    //查询该用户是否为管理员用户
    private void machineAdminPerson() {
        showLoadding();
        JSONObject requestJson = new JSONObject();
        requestJson.put("USERNAME", userInfo.getUsername());
        ApiResource.MachineRoomPersonList(requestJson.toJSONString(), new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int i, Header[] headers, byte[] bytes) {
                String result = new String(bytes);
                if (result != null && result.length() > 5) {
                    ll_safty_approval_watch.setVisibility(View.VISIBLE);
                } else {
                    ll_safty_approval.setVisibility(View.VISIBLE);
                }
                stopLoading();
            }

            @Override
            public void onFailure(int i, Header[] headers, byte[] bytes, Throwable throwable) {
                stopLoading();
                toast("获取数据失败");
            }
        });
    }

    @Override
    public boolean enableBackUpBtn() {
        return true;
    }


    @Override
    public String getAtyTitle() {
        return getString(R.string.oncecamera);
    }

}
