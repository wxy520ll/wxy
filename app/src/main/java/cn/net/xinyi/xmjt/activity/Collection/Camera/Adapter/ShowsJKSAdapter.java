package cn.net.xinyi.xmjt.activity.Collection.Camera.Adapter;

import android.content.Context;
import android.widget.AbsListView;

import java.util.Collection;
import java.util.List;

import cn.net.xinyi.xmjt.R;
import cn.net.xinyi.xmjt.config.AdapterHolder;
import cn.net.xinyi.xmjt.config.XYAdapter;
import cn.net.xinyi.xmjt.model.JKSInfoModle;

/**
 * Created by hao.zhou on 2015/9/18.
 */
public class ShowsJKSAdapter extends XYAdapter<JKSInfoModle> {
    private Context context;
    private List<JKSInfoModle> list;

    public ShowsJKSAdapter(AbsListView view, Collection<JKSInfoModle> mDatas, int itemLayoutId, Context mContext) {
        super(view, mDatas, itemLayoutId, mContext);
    }

    @Override
    public void convert(AdapterHolder helper, JKSInfoModle item, boolean isScrolling) {
        helper.setText(R.id.tv_address , item.getJKSWZ() );
        helper.setText(R.id. tv_name, item.getJKSMC() );
        helper.setText(R.id.tv_cjsj , item.getCJSJ() );

    }


//        holder.btn_watch_sxt.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                if (clickListener != null)
//                    clickListener.onWhatch(position);
//            }
//        });


//    public onClickListener clickListener;
//
//    public void setonClickListener(onClickListener clickListener) {
//        this.clickListener = clickListener;
//    }
//
//    public interface onClickListener {
//        public void onWhatch(int index);
//
//    }

}
