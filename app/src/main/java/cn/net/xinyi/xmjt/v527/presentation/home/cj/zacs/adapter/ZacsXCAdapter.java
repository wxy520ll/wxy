package cn.net.xinyi.xmjt.v527.presentation.home.cj.zacs.adapter;

import android.support.annotation.LayoutRes;
import android.support.annotation.Nullable;
import android.widget.ImageView;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;

import java.util.List;

import cn.net.xinyi.xmjt.R;
import cn.net.xinyi.xmjt.v527.presentation.home.cj.zacs.mode.ZacjXcModel;


/**
 * Created by jiajun.wang on 2018/1/24.
 */

public class ZacsXCAdapter extends BaseQuickAdapter<ZacjXcModel, BaseViewHolder> {
    public ZacsXCAdapter(@LayoutRes int layoutResId, @Nullable List<ZacjXcModel> data) {
        super(layoutResId, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, ZacjXcModel item) {
        ImageView imageView=helper.getView(R.id.image);
        TextView name=helper.getView(R.id.name);
        TextView result=helper.getView(R.id.result);
        TextView time=helper.getView(R.id.time);
        //ImageLoaderUtils.showImage(imageView,);
    }
}
