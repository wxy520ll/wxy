package cn.net.xinyi.xmjt.v527.presentation.home;

import android.os.Bundle;
import android.view.View;

import com.xinyi_tech.comm.base.BaseListActivity;
import com.xinyi_tech.comm.base.ListSetupModel;
import com.xinyi_tech.comm.util.ToolbarUtils;

import cn.net.xinyi.xmjt.R;
import cn.net.xinyi.xmjt.v527.presentation.MainPresenter;
import cn.net.xinyi.xmjt.v527.presentation.home.adapter.GzcxDetailListAdapter;
import cn.net.xinyi.xmjt.v527.presentation.home.model.GzcxModel;

public class GzcxDetailListActivity extends BaseListActivity<GzcxDetailListAdapter, GzcxModel, MainPresenter> {


    @Override
    protected void onCreateAfter(Bundle savedInstanceState) {
        super.onCreateAfter(savedInstanceState);
        final View inflate = View.inflate(getApplicationContext(), R.layout.item_gzcx_detail_list_head, null);
        baseListAdapter.addHeaderView(inflate);
        baseListAdapter.setHeaderAndEmpty(false);
        ToolbarUtils.with(this, toolbar).setSupportBack(true).setTitle("工作成效详情", false).build();
    }

    @Override
    protected ListSetupModel setupParam() {
        return ListSetupModel.newBuilder()
                .isEnableRefresh(false)
                .isEnableLoadMore(false)
                .build();
    }

    @Override
    protected GzcxDetailListAdapter getAdapter() {
        return new GzcxDetailListAdapter();
    }

    @Override
    protected void requestData(int requestCode, int pageIndex, int pageSize) {
        mPresenter.getGzcxDetailList(getIntent().getStringExtra("time"), getIntent().getStringExtra("type"), requestCode);
    }

    @Override
    protected MainPresenter getPresenter() {
        return new MainPresenter();
    }
}
