package cn.net.xinyi.xmjt.activity.Collection.ZAJC;

import android.content.Context;
import android.widget.AbsListView;

import java.util.Collection;

import cn.net.xinyi.xmjt.R;
import cn.net.xinyi.xmjt.config.AdapterHolder;
import cn.net.xinyi.xmjt.config.BaseListAdp;
import cn.net.xinyi.xmjt.model.ZAJCModle;

/**
 * Created by hao.zhou on 2016/5/13.
 */
public class ZAJCSearchAdp extends BaseListAdp<ZAJCModle> {


    public ZAJCSearchAdp(AbsListView view, Collection<ZAJCModle> datas, int itemLayoutId, Context context) {
        super(view, datas, itemLayoutId, context);
    }

    @Override
    public void convert(AdapterHolder helper, ZAJCModle item, boolean isScrolling) {
        //经营者姓名
        helper.setText(R.id.tv_mc ,item.getMC()==null ?"":item.getMC() );
        //经营者地址
        helper.setText(R.id.tv_dz ,item.getDZ()==null ?"":item.getDZ()  );
        //经营者姓名
        String jxzxm=item.getJYZXM()==null ? "":item.getJYZXM();
        String jxzdh=item.getJYZDH()==null ? "":item.getJYZDH();
        helper.setText(R.id.za_jyzxm ,jxzxm+"("+jxzdh+")" );
    }
}
