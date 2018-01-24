package cn.net.xinyi.xmjt.v527.presentation.txl;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;

import com.blankj.utilcode.util.ActivityUtils;
import com.blankj.utilcode.util.IntentUtils;
import com.xinyi_tech.comm.base.BasePresenter;
import com.xinyi_tech.comm.form.FormLayout;
import com.xinyi_tech.comm.util.ToolbarUtils;

import butterknife.BindView;
import butterknife.OnClick;
import cn.net.xinyi.xmjt.R;
import cn.net.xinyi.xmjt.v527.base.BaseJwtActivity;
import cn.net.xinyi.xmjt.v527.presentation.txl.model.TxlPersonModel;


public class TxlDetailActivity extends BaseJwtActivity<BasePresenter> {


    @BindView(R.id.tool_bar)
    Toolbar toolBar;
    @BindView(R.id.form)
    FormLayout form;
    private TxlPersonModel personModel;

    @Override
    protected void onCreateAfter(Bundle savedInstanceState) {
        ToolbarUtils.with(this, toolBar).setSupportBack(true).setTitle("通讯录详情", true).build();
        personModel = (TxlPersonModel) getIntent().getSerializableExtra("model");
        form.bindData(personModel, FormLayout.ActionFieldType.FIELD_TYPE_VISIBLE);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_txl_detail;
    }

    @Override
    protected BasePresenter getPresenter() {
        return new BasePresenter();
    }

    @Override
    public void doParseData(int requestCode, Object data) {

    }


    @OnClick({R.id.img_call, R.id.img_dx})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.img_call:
                final Intent callIntent = IntentUtils.getCallIntent(personModel.getUSERNAME());
                ActivityUtils.startActivity(callIntent);
                break;
            case R.id.img_dx:
                final Intent dxIntent = IntentUtils.getSendSmsIntent(personModel.getUSERNAME(), "");
                ActivityUtils.startActivity(dxIntent);
                break;
        }
    }
}
