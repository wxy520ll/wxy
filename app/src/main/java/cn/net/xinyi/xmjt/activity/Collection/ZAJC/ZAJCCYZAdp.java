package cn.net.xinyi.xmjt.activity.Collection.ZAJC;

import android.content.Context;
import android.view.View;
import android.widget.AbsListView;

import java.util.Collection;

import cn.net.xinyi.xmjt.R;
import cn.net.xinyi.xmjt.config.AdapterHolder;
import cn.net.xinyi.xmjt.config.XYAdapter;
import cn.net.xinyi.xmjt.model.ZAJCCYZModle;

/**
 * Created by hao.zhou on 2016/3/20.
 */
public class ZAJCCYZAdp extends XYAdapter<ZAJCCYZModle> {
    boolean flag;
    public ZAJCCYZAdp(boolean flag,AbsListView view, Collection<ZAJCCYZModle> mDatas, int itemLayoutId, Context mContext) {
        super(view, mDatas, itemLayoutId, mContext);
        this.flag=flag;
    }

    @Override
    public void convert(final AdapterHolder helper, ZAJCCYZModle item, boolean isScrolling) {
        helper.setText(R.id.tv_cyzxm, item.getCYZXM());
        helper.setText(R.id.tv_cyzdh, item.getCYZSFZ());
        if (flag == false){
            helper.getConvertView()
                    .findViewById(R.id.btn_del).setVisibility(View.VISIBLE);
            helper.getConvertView()
                    .findViewById(R.id.btn_del).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (clickListener != null)
                        clickListener.onDel(helper.getPosition());
                }
            });
        }

    }
    public onClickListener clickListener;

    public void setonClickListener(onClickListener clickListener) {
        this.clickListener = clickListener;
    }

    public interface onClickListener {
        void onDel(int index);

    }
}



