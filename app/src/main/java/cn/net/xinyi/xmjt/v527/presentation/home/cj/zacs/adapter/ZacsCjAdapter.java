package cn.net.xinyi.xmjt.v527.presentation.home.cj.zacs.adapter;

import android.widget.ImageView;

import com.allen.library.SuperTextView;
import com.blankj.utilcode.util.StringUtils;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.xinyi_tech.comm.util.ImageLoaderUtils;
import com.xinyi_tech.comm.util.ResUtils;

import cn.net.xinyi.xmjt.R;
import cn.net.xinyi.xmjt.utils.DB.ZDXXUtils;
import cn.net.xinyi.xmjt.utils.GeneralUtils;
import cn.net.xinyi.xmjt.v527.presentation.home.cj.zacs.mode.ZacjModel;

import static cn.net.xinyi.xmjt.api.ApiHttpClient.IMAGE_HOST;

/**
 * Created by Fracesuit on 2018/1/18.
 */

public class ZacsCjAdapter extends BaseQuickAdapter<ZacjModel, BaseViewHolder> {
    public ZacsCjAdapter() {
        super(R.layout.item_zacs);
    }

    @Override
    protected void convert(BaseViewHolder helper, ZacjModel item) {
        final SuperTextView view = helper.getView(R.id.stv_info);
       String cjfl=new ZDXXUtils(view.getContext()).getZdlbAndZdbmToZdz(GeneralUtils.XXCJ_ZAJCFL_JT_NEW,item.getCJFL());
        view.setLeftTopString(cjfl+"--"+item.getMC())
                .setLeftString("最新检查时间:"+ (StringUtils.isEmpty(item.getJCSJ())?"":item.getJCSJ()))
                .setLeftBottomString("场所地址"+item.getDZ())
                .setRightTopString("")
                .setRightBottomString("");
//zajcxx
        final String jl = item.getJL();
        if (!StringUtils.isEmpty(jl)) {
            view.setRightTopString(jl + "米")
                    .setRightTopTextColor(ResUtils.getColor(R.color.comm_blue));
        }


        final ImageView leftIconIV = view.getLeftIconIV();

        ImageLoaderUtils.showImage(leftIconIV, IMAGE_HOST + "/zajcxx/" + item.getIV_MPHQJT());

    }
}
