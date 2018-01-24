package cn.net.xinyi.xmjt.v527.presentation.task.csxc.adapter;

import android.view.View;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.xinyi_tech.comm.widget.KeyValueView;

import cn.net.xinyi.xmjt.R;
import cn.net.xinyi.xmjt.v527.presentation.task.csxc.model.CsxcModel;

/**
 * Created by Fracesuit on 2017/12/30.
 */

public class CsxcAdapter extends BaseQuickAdapter<CsxcModel, BaseViewHolder> {
    public CsxcAdapter() {
        super(R.layout.item_csxc);
    }

    @Override
    protected void convert(BaseViewHolder helper, final CsxcModel item) {
        final KeyValueView kv_taskname = helper.getView(R.id.kv_taskname);
        final KeyValueView kv_lx = helper.getView(R.id.kv_lx);
        final KeyValueView kv_pfr = helper.getView(R.id.kv_pfr);
        final KeyValueView kv_pfrdw = helper.getView(R.id.kv_pfrdw);
        final KeyValueView kv_fssj = helper.getView(R.id.kv_fssj);
        final KeyValueView kv_xqsj = helper.getView(R.id.kv_xqsj);
        final KeyValueView kv_taskjj = helper.getView(R.id.kv_taskjj);

        kv_taskname.setValueText(item.getTaskName());
        kv_lx.setValueText(item.getLx());
        kv_pfr.setValueText(item.getPfr());
        kv_pfrdw.setValueText(item.getPfrDw());
        kv_fssj.setValueText(item.getFqsj());
        kv_xqsj.setValueText(item.getXqsj());
        kv_taskjj.setValueText(item.getTaskJj());

        final View sbtn_zjrw = helper.getView(R.id.sbtn_zjrw);
        sbtn_zjrw.setVisibility("1".equals(item.getState()) ? View.VISIBLE : View.GONE);
        sbtn_zjrw.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (onStopTaskListener != null) {
                    onStopTaskListener.stopTask(item.getId());
                }
            }
        });
    }

    OnStopTaskListener onStopTaskListener;

    public void setOnStopTaskListener(OnStopTaskListener onStopTaskListener) {
        this.onStopTaskListener = onStopTaskListener;
    }

    public interface OnStopTaskListener {
        void stopTask(String id);
    }
}
