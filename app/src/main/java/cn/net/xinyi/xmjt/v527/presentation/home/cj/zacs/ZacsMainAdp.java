package cn.net.xinyi.xmjt.v527.presentation.home.cj.zacs;

import android.content.Context;
import android.widget.AbsListView;

import java.util.List;

import cn.net.xinyi.xmjt.R;
import cn.net.xinyi.xmjt.config.AdapterHolder;
import cn.net.xinyi.xmjt.config.BaseListAdp;

/**
 * Created by zhouhao on 2017/2/27.
 */

public class ZacsMainAdp extends BaseListAdp<String> {


    public ZacsMainAdp(AbsListView view, List<String> datas, int itemLayoutId, Context context) {
        super(view, datas, itemLayoutId, context);
    }

    @Override
    public void convert(AdapterHolder helper, String item, boolean isScrolling) {
        helper.setText(R.id.tv_lb,item);
    }
}