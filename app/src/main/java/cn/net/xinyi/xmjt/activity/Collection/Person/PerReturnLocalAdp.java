package cn.net.xinyi.xmjt.activity.Collection.Person;

import android.content.Context;
import android.widget.AbsListView;

import java.util.Collection;

import cn.net.xinyi.xmjt.R;
import cn.net.xinyi.xmjt.config.AdapterHolder;
import cn.net.xinyi.xmjt.config.XYAdapter;
import cn.net.xinyi.xmjt.model.PerReturnModle;

/**
 * Created by hao.zhou on 2016/2/25.
 */
public class PerReturnLocalAdp extends XYAdapter<PerReturnModle> {
    private Context context;

    public PerReturnLocalAdp(AbsListView view, Collection<PerReturnModle> mDatas, int itemLayoutId, Context mContext) {
        super(view, mDatas, itemLayoutId, mContext);
        this.context=mContext;
    }


    @Override
    public void convert(AdapterHolder helper, PerReturnModle item, boolean isScrolling) {
        helper.setImageResource(R.id.iv_bg, R.drawable.dr_prereturn);
        /**名称**/
        helper.setText(R.id.tv_1,context.getResources().getString(R.string.per_bfrxm));
        helper.setText(R.id.tv_store_wz, item.getNAME());

        helper.setText(R.id.tv_cjsj,item.getCJSJ());
        /**地址**/
        helper.setText(R.id.tv_3,context.getResources().getString(R.string.per_dzh));
        helper.setText(R.id.tv_store_mc,item.getZFHDZ());

    }
}
