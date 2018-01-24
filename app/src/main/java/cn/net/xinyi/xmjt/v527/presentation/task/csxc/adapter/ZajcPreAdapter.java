package cn.net.xinyi.xmjt.v527.presentation.task.csxc.adapter;

import android.support.v4.app.FragmentActivity;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;

import cn.net.xinyi.xmjt.R;
import cn.net.xinyi.xmjt.v527.data.remote.retrofit.entity.ZajcEntity;
import cn.net.xinyi.xmjt.v527.widget.ZajcView;

/**
 * Created by Fracesuit on 2017/12/31.
 */

public class ZajcPreAdapter extends BaseQuickAdapter<ZajcEntity, BaseViewHolder> {
    FragmentActivity activity;

    public ZajcPreAdapter(FragmentActivity activity) {
        super(R.layout.item_zajc_pre);
        this.activity = activity;
    }

    @Override
    protected void convert(BaseViewHolder helper, final ZajcEntity item) {
        ZajcView zajc = helper.getView(R.id.zajc);
        zajc.with(activity, item);
    }
}
