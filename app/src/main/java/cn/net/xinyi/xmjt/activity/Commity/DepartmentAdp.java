package cn.net.xinyi.xmjt.activity.Commity;

import android.content.Context;
import android.widget.AbsListView;

import java.util.Collection;

import cn.net.xinyi.xmjt.config.AdapterHolder;
import cn.net.xinyi.xmjt.config.BaseListAdp;

/**
 * Created by studyjun on 2016/6/27.
 */
public class DepartmentAdp extends BaseListAdp<String> {
    public DepartmentAdp(AbsListView view, Collection<String> datas, int itemLayoutId, Context context) {
        super(view, datas, itemLayoutId, context);
    }

    @Override
    public void convert(AdapterHolder helper, String item, boolean isScrolling) {
        helper.setText(android.R.id.text1,item);
    }
}
