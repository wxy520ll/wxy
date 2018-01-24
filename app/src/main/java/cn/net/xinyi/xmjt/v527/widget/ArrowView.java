package cn.net.xinyi.xmjt.v527.widget;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.view.View;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.xinyi_tech.comm.help.recycleview.RecyclerViewHelper;

import java.util.ArrayList;
import java.util.List;

import cn.net.xinyi.xmjt.config.AppContext;
import cn.net.xinyi.xmjt.model.UserInfo;
import cn.net.xinyi.xmjt.v527.presentation.txl.model.TxlDeptModel;

/**
 * Created by Fracesuit on 2017/12/27.
 */

public class ArrowView extends RecyclerView {
    private List<TxlDeptModel> arrowName = new ArrayList<>();
    private ArrowAdapter arrowAdapter;

    public ArrowView(@NonNull Context context) {
        super(context);
        init();
    }

    public ArrowView(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public void init() {
        final UserInfo loginInfo = AppContext.instance().getLoginInfo();
        final TxlDeptModel txlDeptModel = new TxlDeptModel();
        txlDeptModel.setORGNAME(loginInfo.getPcs());
        txlDeptModel.setORGCODE(loginInfo.getOrgancode());
        arrowName.add(txlDeptModel);
        arrowAdapter = new ArrowAdapter(arrowName);
        arrowAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                final List<TxlDeptModel> data = arrowAdapter.getData();
                final int size = data.size();
                for (int i = size - 1; i > position; i--) {
                    data.remove(i);
                }
                arrowAdapter.notifyDataSetChanged();
                if (onArrowItemClickListener != null) {
                    onArrowItemClickListener.onArrowClick(arrowAdapter.getItem(position));
                }
            }
        });
        RecyclerViewHelper.initRecyclerViewH(this, false, arrowAdapter);
    }

    public void addData(TxlDeptModel txlDeptModel) {
        arrowAdapter.addData(txlDeptModel);
        arrowAdapter.notifyDataSetChanged();
    }

    //退一格
    public boolean backOne() {
        final List<TxlDeptModel> data = arrowAdapter.getData();
        if (data.size() > 1) {
            data.remove(data.size() - 1);
            arrowAdapter.notifyDataSetChanged();
            return true;
        }
        return false;
    }

    public TxlDeptModel getLastTxlDeptModel() {
        final int size = arrowAdapter.getData().size();
        return arrowAdapter.getItem(size - 1);
    }


    public interface OnArrowItemClickListener {
        void onArrowClick(TxlDeptModel txlDeptModel);
    }

    OnArrowItemClickListener onArrowItemClickListener;

    public void setOnArrowItemClickListener(OnArrowItemClickListener onArrowItemClickListener) {
        this.onArrowItemClickListener = onArrowItemClickListener;
    }
}
