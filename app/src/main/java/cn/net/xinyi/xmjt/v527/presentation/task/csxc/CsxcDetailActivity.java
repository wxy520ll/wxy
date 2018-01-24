package cn.net.xinyi.xmjt.v527.presentation.task.csxc;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;

import com.allen.library.SuperButton;
import com.blankj.utilcode.util.ActivityUtils;
import com.xinyi_tech.comm.util.OverridePendingTransitionUtls;
import com.xinyi_tech.comm.util.ToastyUtil;
import com.xinyi_tech.comm.util.ToolbarUtils;
import com.xinyi_tech.comm.widget.KeyValueView;

import butterknife.BindView;
import butterknife.OnClick;
import cn.net.xinyi.xmjt.R;
import cn.net.xinyi.xmjt.v527.base.BaseJwtActivity;
import cn.net.xinyi.xmjt.v527.data.remote.retrofit.entity.CsxcEntity;
import cn.net.xinyi.xmjt.v527.presentation.task.csxc.model.CsxcModel;
import cn.net.xinyi.xmjt.v527.presentation.task.presenter.RwPresenter;

public class CsxcDetailActivity extends BaseJwtActivity<RwPresenter> {

    @BindView(R.id.tool_bar)
    Toolbar toolBar;
    @BindView(R.id.kv_kddh)
    KeyValueView kvKddh;
    @BindView(R.id.kv_mc)
    KeyValueView kvMc;
    @BindView(R.id.kv_dz)
    KeyValueView kvDz;
    @BindView(R.id.kv_bh)
    KeyValueView kvBh;
    @BindView(R.id.kv_zb)
    KeyValueView kvZb;
    @BindView(R.id.sbtn_zajc)
    SuperButton sbtnZajc;

    private CsxcModel model;

    @Override
    protected void onCreateAfter(Bundle savedInstanceState) {
        ToolbarUtils.with(this, toolBar).setSupportBack(true).setTitle("场所详情", false).build();
        model = (CsxcModel) getIntent().getSerializableExtra("model");
        final boolean isDeal = getIntent().getBooleanExtra("isDeal", false);
        if (isDeal) {
            sbtnZajc.setVisibility(View.GONE);
        }
        mPresenter.getTaskDetail(model.getCsId(), model.getCscode(), 100);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_csxc_detail;
    }

    @Override
    protected RwPresenter getPresenter() {
        return new RwPresenter();
    }

    @Override
    public void doParseData(int requestCode, Object data) {
        if (requestCode == 100) {
            if (data == null) {
                ToastyUtil.warningShort("没有找到数据");
            } else {
                CsxcEntity m = (CsxcEntity) data;
                kvKddh.setValueText(m.getCSLX_NAME());
                kvMc.setValueText(m.getNAME());
                kvDz.setValueText(m.getADDRESS());
                kvBh.setValueText(m.getID());
                kvZb.setValueText("(" + m.getLAT() + "," + m.getLNG() + ")");
            }

        }
    }


    @OnClick({R.id.sbtn_jcjl, R.id.sbtn_zajc})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.sbtn_jcjl:
                final Intent intent1 = new Intent(this, JcjlActivity.class);
                intent1.putExtra("id", model.getCsId());
                ActivityUtils.startActivity(intent1);
                OverridePendingTransitionUtls.slideRightEntry(this);
                break;
            case R.id.sbtn_zajc:
                final Intent intent = new Intent(this, ZajcPreActivity.class);
                intent.putExtra("model", model);
                ActivityUtils.startActivity(intent);
                OverridePendingTransitionUtls.slideRightEntry(this);
                break;
        }
    }
}
