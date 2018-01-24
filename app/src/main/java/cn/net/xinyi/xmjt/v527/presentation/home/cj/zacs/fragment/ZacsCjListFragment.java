package cn.net.xinyi.xmjt.v527.presentation.home.cj.zacs.fragment;

import android.content.Intent;
import android.view.View;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.xinyi_tech.comm.base.BaseListFragment;
import com.xinyi_tech.comm.base.ListSetupModel;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.List;

import cn.net.xinyi.xmjt.v527.presentation.home.cj.zacs.ZacsInforAty;
import cn.net.xinyi.xmjt.v527.presentation.home.cj.zacs.adapter.ZacsCjAdapter;
import cn.net.xinyi.xmjt.v527.presentation.home.cj.zacs.mode.ZacjModel;
import cn.net.xinyi.xmjt.v527.presentation.home.cj.zacs.mode.ZacjQueryCondition;
import cn.net.xinyi.xmjt.v527.presentation.home.cj.zacs.presenter.ZacsCjPresenter;

/**
 * Created by Fracesuit on 2018/1/19.
 */

public class ZacsCjListFragment extends BaseListFragment<ZacsCjAdapter, ZacjModel, ZacsCjPresenter> {
    ZacjQueryCondition condition ;
    ZacsCjAdapter adapter;
    @Override
    protected ListSetupModel setupParam() {
        return ListSetupModel.newBuilder().isEnableRefresh(false).build();
    }

    @Override
    protected ZacsCjAdapter getAdapter() {
        adapter=new ZacsCjAdapter();
        return adapter;
    }

    @Override
    public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
        super.onItemClick(adapter, view, position);
        Intent intent=new Intent(getActivity(),ZacsInforAty.class);
        List<ZacjModel>zacjModelList=adapter.getData();
        ZacjModel z=zacjModelList.get(position);
        intent.putExtra("itemData",z);
        startActivity(intent);
    }

    @Override
    protected void requestData(int requestCode, int pageIndex, int pageSize) {
        if(condition!=null)
        {
            condition.setPAGESIZE(pageSize);
            condition.setPAGENUMBER(pageIndex);
            mPresenter.getZajcxxlistByQueryNew(condition, requestCode);
        }


    }

    @Override
    protected ZacsCjPresenter getPresenter() {
        return new ZacsCjPresenter();
    }

    @Override
    public void onStart() {
        super.onStart();
        EventBus.getDefault().register(this);
    }

    @Override
    public void onStop() {
        super.onStop();
        EventBus.getDefault().unregister(this);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMessageEvent(ZacjQueryCondition zacjQueryConditiont) {
        this.condition = zacjQueryConditiont;
        requestListData(REQUEST_STATE_REFRESH);
    }

}
