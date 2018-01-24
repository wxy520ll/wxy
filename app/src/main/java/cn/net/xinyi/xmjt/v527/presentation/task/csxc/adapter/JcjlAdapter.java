package cn.net.xinyi.xmjt.v527.presentation.task.csxc.adapter;

import com.allen.library.SuperTextView;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;

import cn.net.xinyi.xmjt.R;
import cn.net.xinyi.xmjt.v527.data.remote.retrofit.entity.JcjlEntity;

/**
 * Created by Fracesuit on 2017/12/30.
 */

public class JcjlAdapter extends BaseQuickAdapter<JcjlEntity, BaseViewHolder> {
    public JcjlAdapter() {
        super(R.layout.item_jcjl);
    }

    @Override
    protected void convert(BaseViewHolder helper, JcjlEntity item) {
        final SuperTextView stv_csxc = helper.getView(R.id.stv_csxc);
        stv_csxc.setLeftTopString(item.getSCZWT())
                .setLeftString(item.getPCSJCMJXM() + "(" + item.getPCSJCMJJH() + ")")
                .setRightString(item.getCREATETIME())
                .setLeftBottomString(item.getSMJCLYJ());
    }
}
