package cn.net.xinyi.xmjt.activity.ZHFK.Duty;

import android.content.Context;
import android.widget.AbsListView;

import java.util.Collection;

import cn.net.xinyi.xmjt.R;
import cn.net.xinyi.xmjt.config.AdapterHolder;
import cn.net.xinyi.xmjt.config.BaseListAdp;
import cn.net.xinyi.xmjt.model.DutyOperationModle;

/**
 * Created by hao.zhou on 2016/4/19.
 */
public class DutyOperHistoryAdp extends BaseListAdp<DutyOperationModle> {

    public DutyOperHistoryAdp(AbsListView view, Collection<DutyOperationModle> datas, int itemLayoutId, Context context) {
        super(view, datas, itemLayoutId, context);
    }

    @Override
    public void convert(AdapterHolder helper, DutyOperationModle item, boolean isScrolling) {
        helper.setText(R.id.tv_dz,item.getADDRESS());
        helper.setText(R.id.tv_dk,"共"+(item.getDISTANCE()==null?0:item.getDISTANCE())+"米");
        helper.setText(R.id.tv_name,item.getSHOWNAME());
        helper.setText(R.id.tv_dh,item.getTEL_NUMBER());
        helper.setText(R.id.tv_ksxl,"巡逻");
        if (null==item.getENDDTIME()||item.getENDDTIME().isEmpty()){
            helper.setText(R.id.tv_year,item.getCREATETIME());
        }else {
            helper.setText(R.id.tv_year,item.getCREATETIME()+"至"+item.getENDDTIME());
        }
    }


}
