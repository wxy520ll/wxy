package cn.net.xinyi.xmjt.v527.presentation.task.csxc;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.blankj.utilcode.util.ActivityUtils;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.flyco.tablayout.SegmentTabLayout;
import com.flyco.tablayout.listener.OnTabSelectListener;
import com.xinyi_tech.comm.base.BaseListActivity;
import com.xinyi_tech.comm.base.ListSetupModel;
import com.xinyi_tech.comm.util.OverridePendingTransitionUtls;
import com.xinyi_tech.comm.util.ToastyUtil;
import com.xinyi_tech.comm.util.ToolbarUtils;

import cn.net.xinyi.xmjt.R;
import cn.net.xinyi.xmjt.v527.presentation.task.csxc.adapter.CsxcAdapter;
import cn.net.xinyi.xmjt.v527.presentation.task.csxc.model.CsxcModel;
import cn.net.xinyi.xmjt.v527.presentation.task.presenter.RwPresenter;

public class CsxcActivity extends BaseListActivity<CsxcAdapter, CsxcModel, RwPresenter> implements CsxcAdapter.OnStopTaskListener {
    @butterknife.BindView(R.id.tl_cl)
    SegmentTabLayout tl_cl;
    public static final int NET_STOP_TASK = 1;

    @Override
    protected ListSetupModel setupParam() {
        return ListSetupModel.newBuilder()
                .isRefreshAlways(true)
                .build();
    }


    @Override
    protected int getLayoutId() {
        return R.layout.activity_csxc;
    }

    @Override
    protected void onCreateAfter(Bundle savedInstanceState) {
        super.onCreateAfter(savedInstanceState);
        tl_cl.setTabData(new String[]{"待处理", "已处理"});
        tl_cl.setOnTabSelectListener(new OnTabSelectListener() {
            @Override
            public void onTabSelect(int position) {
                requestListData(REQUEST_STATE_REFRESH);
            }

            @Override
            public void onTabReselect(int position) {

            }
        });
        ToolbarUtils.with(this, toolbar).setSupportBack(true).build();
    }

    @Override
    protected CsxcAdapter getAdapter() {
        final CsxcAdapter csxcAdapter = new CsxcAdapter();
        csxcAdapter.setOnStopTaskListener(this);
        return csxcAdapter;
    }

    @Override
    public void doParseData(int requestCode, Object data) {
        if (requestCode == NET_STOP_TASK) {
            boolean result = (boolean) data;
            if (result) {
                ToastyUtil.successShort("终止任务成功");
                requestListData(REQUEST_STATE_REFRESH);
            } else {
                ToastyUtil.errorShort("终止任务失败");
            }
        } else {
            super.doParseData(requestCode, data);
        }

    }

    @Override
    protected void requestData(int requestCode, int pageIndex, int pageSize) {
        mPresenter.getTasklistByQuery(tl_cl.getCurrentTab() == 0 ? "1" : "3", pageIndex, pageSize, requestCode);
    }

    @Override
    protected RwPresenter getPresenter() {
        return new RwPresenter();
    }

    @Override
    public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
        final CsxcModel item = baseListAdapter.getItem(position);
        final Intent intent = new Intent(this, CsxcDetailActivity.class);
        intent.putExtra("model", item);
        intent.putExtra("isDeal", tl_cl.getCurrentTab() != 0);
        ActivityUtils.startActivity(intent);
        OverridePendingTransitionUtls.slideRightEntry(this);
    }

    @Override
    public void stopTask(String id) {
        mPresenter.stopTaskById(id, NET_STOP_TASK);
    }
}
