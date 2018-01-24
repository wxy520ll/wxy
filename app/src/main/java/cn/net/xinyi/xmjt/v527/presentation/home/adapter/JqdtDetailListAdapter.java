package cn.net.xinyi.xmjt.v527.presentation.home.adapter;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.xinyi_tech.comm.form.FormLayout;

import cn.net.xinyi.xmjt.R;
import cn.net.xinyi.xmjt.v527.data.remote.retrofit.entity.JqdtEntity;


/**
 * Created by Fracesuit on 2017/6/29.
 */

public class JqdtDetailListAdapter extends BaseQuickAdapter<JqdtEntity, BaseViewHolder> {
    public JqdtDetailListAdapter() {
        super(R.layout.item_jqdt_detail_list);
    }

    @Override
    protected void convert(BaseViewHolder helper, JqdtEntity item) {
        final FormLayout form=  helper.getView(R.id.form);
        form.bindData(item, FormLayout.ActionFieldType.FIELD_TYPE_VISIBLE);
    }
}



