package cn.net.xinyi.xmjt.v527.presentation.txl;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.blankj.utilcode.util.ActivityUtils;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.entity.MultiItemEntity;
import com.xinyi_tech.comm.base.BaseListFragment;
import com.xinyi_tech.comm.base.ListSetupModel;
import com.xinyi_tech.comm.util.ToastyUtil;

import butterknife.BindView;
import cn.net.xinyi.xmjt.R;
import cn.net.xinyi.xmjt.v527.presentation.MainPresenter;
import cn.net.xinyi.xmjt.v527.presentation.txl.model.TxlDeptModel;
import cn.net.xinyi.xmjt.v527.presentation.txl.model.TxlPersonModel;
import cn.net.xinyi.xmjt.v527.presentation.txl.model.TxlTypeModel;
import cn.net.xinyi.xmjt.v527.widget.ArrowView;
import cn.net.xinyi.xmjt.v527.widget.MaterialSearchView;

/**
 * Created by Fracesuit on 2017/12/22.
 */

public class TxlFragment extends BaseListFragment<TxlAdapter, MultiItemEntity, MainPresenter> {


    @BindView(R.id.search_view)
    MaterialSearchView searchView;
    private ArrowView arrowView;
    private TxlAdapter seacherAdapter;

    @Override
    protected void onCreateViewAfter(View view, Bundle savedInstanceState) {
        arrowView = new ArrowView(getContext());
        arrowView.setOnArrowItemClickListener(new ArrowView.OnArrowItemClickListener() {
            @Override
            public void onArrowClick(TxlDeptModel txlDeptModel) {
                requestListData(REQUEST_STATE_REFRESH);
            }
        });
        super.onCreateViewAfter(view, savedInstanceState);
        baselistContainer.addView(arrowView, 0);
        initToolbar();
    }

    private void initToolbar() {
      /*  ToolbarUtils.with(activity, toolbar)
                .setTitle(ResUtils.getString(R.string.app_fullname), true)
                .build();*/
        seacherAdapter = new TxlAdapter();
        seacherAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                final Intent intent = new Intent(activity, TxlDetailActivity.class);
                intent.putExtra("model", (TxlPersonModel) seacherAdapter.getItem(position));
                ActivityUtils.startActivity(intent);
            }
        });
        searchView.setAdapter(seacherAdapter);

        searchView.setOnQueryTextListener(new MaterialSearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                seacher(query);
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                seacher(newText);
                return true;
            }
        });
    }

    private void seacher(String query) {
        seacherAdapter.setNewData(baseListAdapter.query(query));
        searchView.showSuggestions();
    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_txl;
    }

    @Override
    protected ListSetupModel setupParam() {
        return ListSetupModel.newBuilder()
                .isEnableRefresh(true)
                .isEnableLoadMore(false)
                .isLasy(true)
                .isShowToolbar(true)
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
            final Intent intent = new Intent(activity, TxlDetailActivity.class);
            intent.putExtra("model", (TxlPersonModel) item);
            ActivityUtils.startActivity(intent);
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
        if (searchView.dismissSuggestions()) {
            return true;
        }

        final boolean b = arrowView.backOne();
        if (b) {
            mPresenter.getTxl(arrowView.getLastTxlDeptModel().getORGCODE(), REQUEST_STATE_REFRESH);//第一次获取龙岗分局下面的数据
        }
        return b;
    }


}
