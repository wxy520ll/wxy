package cn.net.xinyi.xmjt.activity.Main;

import android.content.Context;
import android.widget.AbsListView;

import java.util.Collection;

import cn.net.xinyi.xmjt.R;
import cn.net.xinyi.xmjt.config.AdapterHolder;
import cn.net.xinyi.xmjt.config.BaseListAdp;
import cn.net.xinyi.xmjt.model.ModelItem;

/**
 * Created by studyjun on 2016/5/1.
 */
public class MainMenuAdp extends BaseListAdp<ModelItem> {


    public MainMenuAdp(AbsListView view, Collection<ModelItem> datas, int itemLayoutId, Context context) {
        super(view, datas, itemLayoutId, context);
    }

    @Override
    public void convert(AdapterHolder helper, ModelItem item, boolean isScrolling) {
        helper.setImageResource(R.id.item_module_img, item.resIcon);
        helper.setText(R.id.item_module_name, item.name);
    }
}
