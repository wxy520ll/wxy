package cn.net.xinyi.xmjt.activity.Collection.Camera.Adapter;

import android.content.Context;
import android.widget.AbsListView;

import java.util.Collection;

import cn.net.xinyi.xmjt.R;
import cn.net.xinyi.xmjt.config.AdapterHolder;
import cn.net.xinyi.xmjt.config.XYAdapter;
import cn.net.xinyi.xmjt.model.JKSInfoModle;

/**
 * Created by hao.zhou on 2015/9/18.
 */
public class AlreadyUploadAdapter extends XYAdapter<JKSInfoModle> {

    public AlreadyUploadAdapter(AbsListView view, Collection<JKSInfoModle> mDatas, int itemLayoutId, Context mContext) {
        super(view, mDatas, itemLayoutId, mContext);
    }

    @Override
    public void convert(AdapterHolder helper, JKSInfoModle item, boolean isScrolling) {
        helper.setText(R.id.tv_cj_dz,item.getJKSWZ());
        helper.setText(R.id.tv_ss_wg,item.getSSWG());
        helper.setText(R.id.tv_cjsj,item.getCJSJ());
        helper.setText(R.id.tv_cj_ry,item.getCJYH());
    }
}
