package cn.net.xinyi.xmjt.activity.Collection.InfoCheck;

import android.content.Context;
import android.widget.AbsListView;

import java.util.Collection;

import cn.net.xinyi.xmjt.R;
import cn.net.xinyi.xmjt.config.AdapterHolder;
import cn.net.xinyi.xmjt.config.BaseListAdp;
import cn.net.xinyi.xmjt.model.LogisticsDeliveryCheckModle;

/**
 * Created by zhouhao on 2017/2/27.
 */

public class LogisticsDeliveryCheckListAdp extends BaseListAdp<LogisticsDeliveryCheckModle> {


    public LogisticsDeliveryCheckListAdp(AbsListView view, Collection<LogisticsDeliveryCheckModle> datas, int itemLayoutId, Context context) {
        super(view, datas, itemLayoutId, context);
    }

    @Override
    public void convert(AdapterHolder helper, LogisticsDeliveryCheckModle item, boolean isScrolling) {
        //核查人员
        helper.setText(R.id.tv_hcry ,"核查人员:"+(item.getNAME()+"("+item.getHCYH()+")"));
        //最后一次核查时间
        helper.setText(R.id.tv_hcsj ,"核查时间:"+(item.getHCSJ()==null ?"":item.getHCSJ()));
    }
}