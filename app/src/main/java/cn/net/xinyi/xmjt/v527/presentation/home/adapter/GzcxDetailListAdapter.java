package cn.net.xinyi.xmjt.v527.presentation.home.adapter;

import android.widget.ImageView;

import com.allen.library.SuperTextView;
import com.amulyakhare.textdrawable.TextDrawable;
import com.blankj.utilcode.util.SizeUtils;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.xinyi_tech.comm.util.ResUtils;

import cn.net.xinyi.xmjt.R;
import cn.net.xinyi.xmjt.v527.presentation.home.model.GzcxModel;


/**
 * Created by Fracesuit on 2017/6/29.
 */

public class GzcxDetailListAdapter extends BaseQuickAdapter<GzcxModel, BaseViewHolder> {
    public GzcxDetailListAdapter() {
        super(R.layout.item_gzcx_detail_list);
    }
    private TextDrawable getTextDrawable(String position) {
        return TextDrawable.builder()
                .beginConfig()
                .textColor(ResUtils.getColor(R.color.white))
                .fontSize(SizeUtils.sp2px(14))
                .endConfig()
                .buildRound(position, ResUtils.getColor(R.color.comm_lightGreen));
    }

    @Override
    protected void convert(BaseViewHolder helper, GzcxModel item) {
        final ImageView img_pm = helper.getView(R.id.img_pm);
        final SuperTextView gzcx = helper.getView(R.id.stv_gzcx);
        TextDrawable drawable = getTextDrawable(item.getPm());
        img_pm.setImageDrawable(drawable);
        gzcx.setLeftString(item.getName())
                .setLeftBottomString(item.getMobile())
                .setCenterString(item.getTypeName())
                .setRightString(item.getNum())
        ;
    }
}



