package cn.net.xinyi.xmjt.activity.Collection.InfoCheck;

import android.content.Context;
import android.widget.AbsListView;

import java.util.Collection;

import cn.net.xinyi.xmjt.R;
import cn.net.xinyi.xmjt.config.AdapterHolder;
import cn.net.xinyi.xmjt.config.BaseListAdp;
import cn.net.xinyi.xmjt.model.ZAJCModle;

/**
 * Created by zhouhao on 2017/2/27.
 */

public class LogisticsDeliveryListAdp extends BaseListAdp<ZAJCModle> {


    public LogisticsDeliveryListAdp(AbsListView view, Collection<ZAJCModle> datas, int itemLayoutId, Context context) {
        super(view, datas, itemLayoutId, context);
    }

    @Override
    public void convert(AdapterHolder helper, ZAJCModle item, boolean isScrolling) {
        //经营者姓名
        helper.setText(R.id.tv_csmc ,"场所名称:"+(item.getMC()==null ?"":item.getMC()));
        //所属单位
        helper.setText(R.id.tv_ssdw ,"所属单位:"+(item.getSSPCS()==null ?"":item.getSSPCS()) );
        //经营者姓名
        String jxzxm=item.getJYZXM()==null ? "":item.getJYZXM();
        String jxzdh=item.getJYZDH()==null ? "":item.getJYZDH();
        helper.setText(R.id.tv_fr ,"经营者姓名:"+(jxzxm+"("+jxzdh+")"));
        //经营者地址
        helper.setText(R.id.tv_ssdz ,"所属地址:"+(item.getDZ()==null ?"":item.getDZ()));
        //最后一次核查时间
        helper.setText(R.id.tv_hcsj ,"最后一次核查时间:"+(item.getHCSJ()==null ?"":item.getHCSJ()));
    }
}