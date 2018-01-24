package cn.net.xinyi.xmjt.v527.presentation.gzrz.adapter;

import android.support.annotation.LayoutRes;
import android.support.annotation.Nullable;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;

import java.util.List;

import cn.net.xinyi.xmjt.model.ModelItem;

/**
 * Created by jiajun.wang on 2017/12/29.
 */

public class GzrzAdapter extends BaseQuickAdapter<ModelItem,BaseViewHolder> {
    public GzrzAdapter(@LayoutRes int layoutResId, @Nullable List<ModelItem> data) {
        super(layoutResId, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, ModelItem item) {

    }
}
