package cn.net.xinyi.xmjt.v527.widget;

import android.support.annotation.Nullable;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;

import java.util.List;

import cn.net.xinyi.xmjt.R;
import cn.net.xinyi.xmjt.v527.presentation.txl.model.TxlDeptModel;

/**
 * Created by Fracesuit on 2017/12/27.
 */

public class ArrowAdapter extends BaseQuickAdapter<TxlDeptModel, BaseViewHolder> {

    public ArrowAdapter(@Nullable List<TxlDeptModel> data) {
        super(R.layout.item_arrow, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, TxlDeptModel item) {
        helper.setText(R.id.tv_arrow_name, item.getORGNAME());
    }
}
