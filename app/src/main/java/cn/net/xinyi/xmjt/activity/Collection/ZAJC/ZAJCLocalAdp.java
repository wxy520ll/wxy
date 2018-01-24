package cn.net.xinyi.xmjt.activity.Collection.ZAJC;

import android.content.Context;
import android.widget.AbsListView;

import java.util.Collection;

import cn.net.xinyi.xmjt.R;
import cn.net.xinyi.xmjt.config.AdapterHolder;
import cn.net.xinyi.xmjt.config.XYAdapter;
import cn.net.xinyi.xmjt.model.ZAJCModle;

/**
 * Created by hao.zhou on 2016/2/25.
 */
public class ZAJCLocalAdp extends XYAdapter<ZAJCModle> {
    private Context context;
    public ZAJCLocalAdp(AbsListView view, Collection<ZAJCModle> mDatas, int itemLayoutId, Context mContext) {
        super(view, mDatas, itemLayoutId, mContext);
        this.context=mContext;
    }

    @Override
    public void convert(AdapterHolder helper, ZAJCModle item, boolean isScrolling) {
        helper.setImageResource(R.id.iv_bg, R.drawable.rank_pcs);
        /**分类**/
        helper.setText(R.id.tv_1,context.getResources().getString(R.string.cjfl));
        helper.setText(R.id.tv_store_wz,item.getCJFL());
        helper.setText(R.id.tv_cjsj,item.getCJSJ());
        /**名称**/
        helper.setText(R.id.tv_3,context.getResources().getString(R.string.mc));
        helper.setText(R.id.tv_store_mc, item.getMC());
    }

}
