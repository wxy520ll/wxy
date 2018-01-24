package cn.net.xinyi.xmjt.activity.ZHFK.KQ;

import android.app.Activity;
import android.content.Context;
import android.view.View;
import android.widget.AbsListView;
import android.widget.ImageView;
import android.widget.PopupWindow;

import java.util.Collection;

import cn.net.xinyi.xmjt.R;
import cn.net.xinyi.xmjt.api.ApiHttpClient;
import cn.net.xinyi.xmjt.config.AdapterHolder;
import cn.net.xinyi.xmjt.config.BaseListAdp;
import cn.net.xinyi.xmjt.model.WorkCheckModle;
import cn.net.xinyi.xmjt.utils.DialogHelper;
import cn.net.xinyi.xmjt.utils.ImageUtils;

/**
 * Created by hao.zhou on 2016/6/23.
 */
public class WorkCheckHistoryAdp extends BaseListAdp<WorkCheckModle> {

    private ImageView iv_ck;
    private Context context;
    private PopupWindow mpopupWindow;
    public WorkCheckHistoryAdp(AbsListView view, Collection<WorkCheckModle> datas, int itemLayoutId, Context context) {
        super(view, datas, itemLayoutId, context);
        this.context = context;
    }

    @Override
    public void convert(AdapterHolder helper, final WorkCheckModle item, boolean isScrolling) {
        if (null !=item.getBZ()){
            helper.setText(R.id.tv_yy,item.getBZ());
        }else {
            helper.setText(R.id.tv_yy, "");
        }
        helper.setText(R.id.tv_sj, item.getSCSJ());
        helper.setText(R.id.tv_dz, item.getDZ());

        iv_ck = (ImageView) helper.getConvertView().findViewById(R.id.iv_ck);
        iv_ck.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (null != item.getIV_ZP1()){
                    DialogHelper.showPopMenu((Activity) context,iv_ck,ApiHttpClient.IMAGE_HOST + "/qwdc/" + ImageUtils.ImageLoad(item.getSCSJ()) + "/" + item.getIV_ZP1());
                }else if (null != item.getIV_ZP2()){
                    DialogHelper.showPopMenu((Activity) context,iv_ck,ApiHttpClient.IMAGE_HOST + "/qwdc/" + ImageUtils.ImageLoad(item.getSCSJ()) + "/" + item.getIV_ZP1());
                }else if (null != item.getIV_ZP3()){
                    DialogHelper.showPopMenu((Activity) context,iv_ck,ApiHttpClient.IMAGE_HOST + "/qwdc/" + ImageUtils.ImageLoad(item.getSCSJ()) + "/" + item.getIV_ZP3());
                }
            }
        });
    }
}
