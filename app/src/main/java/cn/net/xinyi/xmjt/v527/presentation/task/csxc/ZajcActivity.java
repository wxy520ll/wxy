package cn.net.xinyi.xmjt.v527.presentation.task.csxc;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;

import com.allen.library.SuperButton;
import com.blankj.utilcode.util.ActivityUtils;
import com.xinyi_tech.comm.form.FormLayout;
import com.xinyi_tech.comm.util.ToastyUtil;
import com.xinyi_tech.comm.util.ToolbarUtils;
import com.xinyi_tech.comm.widget.picker.SuperImageView;
import com.xinyi_tech.comm.widget.picker.SuperMutiPickerView;

import butterknife.BindView;
import butterknife.OnClick;
import cn.net.xinyi.xmjt.R;
import cn.net.xinyi.xmjt.v527.base.BaseJwtActivity;
import cn.net.xinyi.xmjt.v527.presentation.task.csxc.model.ZajcModel;
import cn.net.xinyi.xmjt.v527.presentation.task.presenter.RwPresenter;




public class ZajcActivity extends BaseJwtActivity<RwPresenter> {

    @BindView(R.id.tool_bar)
    Toolbar toolBar;
    @BindView(R.id.form)
    FormLayout form;
    @BindView(R.id.smimg_zp)
    SuperMutiPickerView smimg_zp;
    @BindView(R.id.simg_qm)
    SuperImageView simg_qm;
    @BindView(R.id.sbtn_submit)
    SuperButton sbtnSubmit;
    private ZajcModel checkresult;

    @Override
    protected void onCreateAfter(Bundle savedInstanceState) {
        ToolbarUtils.with(this, toolBar).setSupportBack(true).setTitle("场所巡查", false).build();
        checkresult = (ZajcModel) getIntent().getSerializableExtra("checkresult");
        if (checkresult != null) {
            sbtnSubmit.setVisibility(View.GONE);
            form.bindData(checkresult, FormLayout.ActionFieldType.FIELD_TYPE_VISIBLE);
            simg_qm.setLookMode(true);
            smimg_zp.setupParams(new SuperMutiPickerView.Builder(this).lookMode(true));
        } else {
            smimg_zp.setupParams(new SuperMutiPickerView.Builder(this).isOnlyCamera(true).lookMode(false).maxSelectCount(1));
            simg_qm.setOnImageViewClickListener(new SuperImageView.OnImageViewClickListener() {
                @Override
                public boolean onClick(boolean hasDrawable) {
                    final Intent intent = new Intent(ZajcActivity.this, SignatureActivity.class);
                    ZajcActivity.this.startActivityForResult(intent, 1);
                    return true;
                }
            });
        }
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_zajc;
    }

    @Override
    protected RwPresenter getPresenter() {
        return new RwPresenter();
    }

    @Override
    public void doParseData(int requestCode, Object data) {
        if (requestCode == 100) {
            ToastyUtil.successShort("提交成功");
            ActivityUtils.finishActivity(this);
            ActivityUtils.finishActivity(ZajcPreActivity.class);
            ActivityUtils.finishActivity(CsxcDetailActivity.class);
        }
    }

    @OnClick(R.id.sbtn_submit)
    public void onViewClicked() {
        if (form.checkForm(FormLayout.ActionFieldType.FIELD_TYPE_VISIBLE)) {
            final ZajcModel zajcModel = form.getParams(ZajcModel.class, FormLayout.ActionFieldType.FIELD_TYPE_VISIBLE);
            zajcModel.setResultDetail(getIntent().getStringExtra("selectCodes"));
            zajcModel.setPD_ID(getIntent().getStringExtra("taskId"));
            zajcModel.setSystemid(getIntent().getStringExtra("csId"));
            mPresenter.addZajc(zajcModel, 100);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == 1 && data != null) {
            simg_qm.show(data.getStringExtra("qmzp"));
        }
        smimg_zp.onActivityResult(requestCode, resultCode, data);
        super.onActivityResult(requestCode, resultCode, data);
    }
}
