package cn.net.xinyi.xmjt.activity.ZHFK.Duty;

import android.content.Context;
import android.widget.AbsListView;

import java.util.Collection;

import cn.net.xinyi.xmjt.R;
import cn.net.xinyi.xmjt.config.AdapterHolder;
import cn.net.xinyi.xmjt.config.BaseListAdp;
import cn.net.xinyi.xmjt.model.DutyBoxModle;

/**
 * Created by hao.zhou on 2016/4/28.
 */
public class DutyBoxAddAdp extends BaseListAdp<DutyBoxModle> {

    public DutyBoxAddAdp(AbsListView view, Collection<DutyBoxModle> datas, int itemLayoutId, Context context) {
        super(view, datas, itemLayoutId, context);
    }
    @Override
    public void convert(AdapterHolder helper, DutyBoxModle item, boolean isScrolling) {
        helper.setText(R.id.tv_pcs,item.getSID_NAME());
        helper.setText(R.id.tv_xld,item.getSIGNBOX_DESCRIBE());
    }


}
