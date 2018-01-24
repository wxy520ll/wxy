package cn.net.xinyi.xmjt.activity.Collection.Camera.Adapter;

import android.content.Context;
import android.widget.AbsListView;

import java.util.Collection;

import cn.net.xinyi.xmjt.R;
import cn.net.xinyi.xmjt.config.AdapterHolder;
import cn.net.xinyi.xmjt.config.XYAdapter;
import cn.net.xinyi.xmjt.model.SXTInfoModle;

/**
 * Created by hao.zhou on 2015/9/18.
 */
public class ShowsSXTAdapter extends XYAdapter<SXTInfoModle> {



    public ShowsSXTAdapter(AbsListView view, Collection<SXTInfoModle> mDatas, int itemLayoutId, Context mContext) {
        super(view, mDatas, itemLayoutId, mContext);
    }

    @Override
    public void convert(AdapterHolder helper, SXTInfoModle item, boolean isScrolling) {
        helper.setText(R.id.tv_camera_room_name, item.getJKSMC());
        helper.setText(R.id.tv_camera_address, item.getSXTWZ());
        helper.setText(R.id.tv_camera_time, item.getCJSJ());
    }





}