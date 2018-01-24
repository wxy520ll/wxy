package cn.net.xinyi.xmjt.v527.presentation.home;

import android.os.Bundle;

import com.xinyi_tech.comm.base.BaseListActivity;
import com.xinyi_tech.comm.base.ListSetupModel;
import com.xinyi_tech.comm.util.ToolbarUtils;

import cn.net.xinyi.xmjt.v527.data.remote.retrofit.entity.JqdtEntity;
import cn.net.xinyi.xmjt.v527.presentation.MainPresenter;
import cn.net.xinyi.xmjt.v527.presentation.home.adapter.JqdtDetailListAdapter;

public class JqdtDetailListActivity extends BaseListActivity<JqdtDetailListAdapter, JqdtEntity, MainPresenter> {


    @Override
    protected void onCreateAfter(Bundle savedInstanceState) {
        super.onCreateAfter(savedInstanceState);
        ToolbarUtils.with(this, toolbar).setSupportBack(true).setTitle("警情动态详情", false).build();
    }

    @Override
    protected ListSetupModel setupParam() {
        return ListSetupModel.newBuilder()
                .isEnableRefresh(false)
                .build();
    }

    @Override
    protected JqdtDetailListAdapter getAdapter() {
        return new JqdtDetailListAdapter();
    }

    @Override
    protected void requestData(int requestCode, int pageIndex, int pageSize) {
        mPresenter.getJqdt(getIntent().getStringExtra("time"), pageIndex, pageSize, requestCode);
    }

    @Override
    protected MainPresenter getPresenter() {
        return new MainPresenter();
    }
}
