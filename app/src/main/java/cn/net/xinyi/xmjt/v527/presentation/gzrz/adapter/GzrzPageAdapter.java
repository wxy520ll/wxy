package cn.net.xinyi.xmjt.v527.presentation.gzrz.adapter;

import android.support.annotation.LayoutRes;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;

import cn.net.xinyi.xmjt.R;
import cn.net.xinyi.xmjt.v527.presentation.gzrz.model.GzrzPageModel;

import static cn.net.xinyi.xmjt.api.ApiHttpClient.IMAGE_HOST;

/**
 * Created by jiajun.wang on 2017/12/30.
 */

public class GzrzPageAdapter extends BaseQuickAdapter<GzrzPageModel,BaseViewHolder>{
    public GzrzPageAdapter(@LayoutRes int layoutResId) {
        super(layoutResId);
    }

    @Override
    protected void convert(BaseViewHolder helper,GzrzPageModel  item) {

        helper.setText(R.id.tv_title,item.getRECORD_TITLE());
        helper.setText(R.id.tv_type,item.getRECORD_TYPE_NAME());
        helper.setText(R.id.tv_time,item.getCREATED_TIME().split(" ")[0]);
        RequestOptions requestOptions = new RequestOptions();
        requestOptions.placeholder(R.drawable.camera)
                .error(R.drawable.camera);
        Glide.with((ImageView) helper.getView(R.id.iv_tp))
                .load(IMAGE_HOST+"/log/"+item.getFILE_ID())
                .apply(requestOptions)
                .into((ImageView) helper.getView(R.id.iv_tp));


        helper.setText(R.id.tv_name,item.getUSER_NAME());
    }
}
