package cn.net.xinyi.xmjt.v527.presentation.task.csxc;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.blankj.utilcode.util.ActivityUtils;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.xinyi_tech.comm.base.BaseListActivity;
import com.xinyi_tech.comm.util.ToolbarUtils;

import cn.net.xinyi.xmjt.v527.data.remote.retrofit.entity.JcjlEntity;
import cn.net.xinyi.xmjt.v527.presentation.task.csxc.adapter.JcjlAdapter;
import cn.net.xinyi.xmjt.v527.presentation.task.presenter.RwPresenter;

public class JcjlActivity extends BaseListActivity<JcjlAdapter, JcjlEntity, RwPresenter> {

    @Override
    protected void onCreateAfter(Bundle savedInstanceState) {
        super.onCreateAfter(savedInstanceState);
        ToolbarUtils.with(this, toolbar).setSupportBack(true).setTitle("检查记录", false).build();
    }

    @Override
    protected JcjlAdapter getAdapter() {
        return new JcjlAdapter();
    }

    @Override
    protected void requestData(int requestCode, int pageIndex, int pageSize) {
        mPresenter.getJcjl(getIntent().getStringExtra("id"), pageIndex, pageSize, requestCode);
    }

    @Override
    protected RwPresenter getPresenter() {
        return new RwPresenter();
    }

    @Override
    public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
        final JcjlEntity item = baseListAdapter.getItem(position);

        final Intent intent = new Intent(this, ZajcPreActivity.class);
        intent.putExtra("jljcId",item.getSID());
        ActivityUtils.startActivity(intent);
    }
}
