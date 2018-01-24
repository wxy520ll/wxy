package cn.net.xinyi.xmjt.v527.presentation.txl;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.entity.MultiItemEntity;
import com.xinyi_tech.comm.base.BaseListActivity;
import com.xinyi_tech.comm.base.ListSetupModel;
import com.xinyi_tech.comm.util.ResUtils;
import com.xinyi_tech.comm.util.ToastyUtil;
import com.xinyi_tech.comm.util.ToolbarUtils;

import cn.net.xinyi.xmjt.R;
import cn.net.xinyi.xmjt.utils.StringUtils;
import cn.net.xinyi.xmjt.v527.presentation.MainPresenter;
import cn.net.xinyi.xmjt.v527.presentation.txl.model.TxlDeptModel;
import cn.net.xinyi.xmjt.v527.presentation.txl.model.TxlPersonModel;
import cn.net.xinyi.xmjt.v527.presentation.txl.model.TxlTypeModel;
import cn.net.xinyi.xmjt.v527.widget.ArrowView;

/**
 * Created by Fracesuit on 2017/12/22.
 */

public class TxlActivity extends BaseListActivity<TxlAdapter, MultiItemEntity, MainPresenter> {

    private ArrowView arrowView;

    @Override
    protected void onCreateAfter(Bundle savedInstanceState) {
        arrowView = new ArrowView(this);
        arrowView.setOnArrowItemClickListener(new ArrowView.OnArrowItemClickListener() {
            @Override
            public void onArrowClick(TxlDeptModel txlDeptModel) {
                requestListData(REQUEST_STATE_REFRESH);
            }
        });
        super.onCreateAfter(savedInstanceState);
        baselistContainer.addView(arrowView, 0);
        initToolbar();
    }


    private void initToolbar() {
        ToolbarUtils.with(this, toolbar).setSupportBack(true).setTitle(ResUtils.getString(R.string.app_fullname), true).build();
    }

    @Override
    protected ListSetupModel setupParam() {
        return ListSetupModel.newBuilder()
                .isEnableRefresh(true)
                .isEnableLoadMore(false)
                .isLasy(true)
                .build();
    }

    @Override
    protected TxlAdapter getAdapter() {
        return new TxlAdapter();
    }

    @Override
    public void doOnCancel(int requestCode) {
        super.doOnCancel(requestCode);
        if (requestCode == REQUEST_STATE_REFRESH) {
            recyclerview.scrollToPosition(0);
        }

    }

    @Override
    protected void requestData(int requestCode, int pageIndex, int pageSize) {
        final String orgcode = arrowView.getLastTxlDeptModel().getORGCODE();
        if (orgcode != null) {
            mPresenter.getTxl(orgcode, requestCode);//第一次获取龙岗分局下面的数据
        } else {
            doOnCancel(REQUEST_STATE_REFRESH);
        }

    }


    @Override
    protected MainPresenter getPresenter() {
        return new MainPresenter();
    }

    @Override
    public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
        final MultiItemEntity item = baseListAdapter.getItem(position);
        if (item instanceof TxlDeptModel) {
            TxlDeptModel m = (TxlDeptModel) item;
            arrowView.addData(m);
            requestListData(REQUEST_STATE_REFRESH);

        } else if (item instanceof TxlPersonModel) {
            TxlPersonModel person = (TxlPersonModel) item;
            if (StringUtils.isEmpty(person.getPOLICENO())) {
                ToastyUtil.warningShort("警号不存在，请重新选择");
                return;
            }
            final Intent intent = new Intent();
            intent.putExtra("person", person);
            setResult(RESULT_OK, intent);
            finish();
        } else if (item instanceof TxlTypeModel) {
            TxlTypeModel m = (TxlTypeModel) item;
            if (m.getPerson() != null && m.getPerson().size() > 0) {
                final TxlDeptModel txlDeptModel = new TxlDeptModel();
                txlDeptModel.setORGNAME(m.getACCOUNTTYPE());
                arrowView.addData(txlDeptModel);
                baseListAdapter.getData().clear();
                baseListAdapter.getData().addAll(m.getPerson());
                baseListAdapter.notifyDataSetChanged();
            } else {
                ToastyUtil.warningShort("没有对应的人员");
            }
        }
    }


    public boolean isScrollToStart() {
        final boolean b = arrowView.backOne();
        if (b) {
            mPresenter.getTxl(arrowView.getLastTxlDeptModel().getORGCODE(), REQUEST_STATE_REFRESH);//第一次获取龙岗分局下面的数据
        }
        return b;
    }

    @Override
    public void onBackPressed() {
        if (isScrollToStart()) {
            return;
        }
        super.onBackPressed();
    }
}
