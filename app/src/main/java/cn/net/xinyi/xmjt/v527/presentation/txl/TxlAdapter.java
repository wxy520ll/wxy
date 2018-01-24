package cn.net.xinyi.xmjt.v527.presentation.txl;

import android.content.Intent;
import android.widget.ImageView;

import com.allen.library.SuperTextView;
import com.amulyakhare.textdrawable.TextDrawable;
import com.blankj.utilcode.util.ActivityUtils;
import com.blankj.utilcode.util.IntentUtils;
import com.blankj.utilcode.util.SizeUtils;
import com.blankj.utilcode.util.StringUtils;
import com.chad.library.adapter.base.BaseMultiItemQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.chad.library.adapter.base.entity.MultiItemEntity;
import com.xinyi_tech.comm.util.ResUtils;

import java.util.ArrayList;
import java.util.List;

import cn.net.xinyi.xmjt.R;
import cn.net.xinyi.xmjt.v527.presentation.txl.model.TxlDeptModel;
import cn.net.xinyi.xmjt.v527.presentation.txl.model.TxlGroup;
import cn.net.xinyi.xmjt.v527.presentation.txl.model.TxlPersonModel;
import cn.net.xinyi.xmjt.v527.presentation.txl.model.TxlTypeModel;
import cn.net.xinyi.xmjt.v527.util.ResUtils2;


/**
 * Created by Fracesuit on 2017/12/27.
 */

public class TxlAdapter extends BaseMultiItemQuickAdapter<MultiItemEntity, BaseViewHolder> {
    public TxlAdapter() {
        super(null);
        addItemType(0, R.layout.item_txl);
        addItemType(1, R.layout.item_txl);
        addItemType(2, R.layout.item_txl_group);
        addItemType(3, R.layout.item_txl);
    }

    @Override
    protected void convert(BaseViewHolder helper, MultiItemEntity item) {
        final int itemViewType = helper.getItemViewType();
        switch (itemViewType) {
            case 0:
                person(helper, item);
                break;
            case 1:
                dept(helper, item);
                break;
            case 2:
                group(helper, item);
                break;
            case 3:
                type(helper, item);
                break;
        }
    }

    private void group(BaseViewHolder helper, MultiItemEntity item) {
        TxlGroup txlGroup = (TxlGroup) item;
        helper.setText(R.id.tv_txl_group, txlGroup.getGroupName());
    }

    private void dept(BaseViewHolder helper, MultiItemEntity item) {
        TxlDeptModel txlDeptModel = (TxlDeptModel) item;
        final ImageView img_name = helper.getView(R.id.img_name);
        final SuperTextView txl = helper.getView(R.id.stv_txl);
        final String orgname = txlDeptModel.getORGNAME();
        TextDrawable drawable = getTextDrawable(orgname);
        img_name.setImageDrawable(drawable);
        txl.setLeftString(orgname)
                .setRightIcon(R.mipmap.comm_right_arrow);
    }

    private void type(BaseViewHolder helper, MultiItemEntity item) {
        TxlTypeModel txlTypeModel = (TxlTypeModel) item;
        final ImageView img_name = helper.getView(R.id.img_name);
        final SuperTextView txl = helper.getView(R.id.stv_txl);
        final String orgname = txlTypeModel.getACCOUNTTYPE();
        TextDrawable drawable = getTextDrawable(orgname);
        img_name.setImageDrawable(drawable);
        txl.setLeftString(orgname).setRightIcon(R.mipmap.comm_right_arrow);
    }

    private TextDrawable getTextDrawable(String orgname) {
        return TextDrawable.builder()
                .beginConfig()
                .textColor(ResUtils.getColor(R.color.white))
                .fontSize(SizeUtils.sp2px(14))
                .endConfig()
                .buildRound(orgname.substring(0, 1), ResUtils.getColor(ResUtils2.getRandomColor()));
    }

    private void person(BaseViewHolder helper, MultiItemEntity item) {
        final TxlPersonModel personModel = (TxlPersonModel) item;
        final ImageView img_name = helper.getView(R.id.img_name);
        final SuperTextView txl = helper.getView(R.id.stv_txl);
        final String name = personModel.getNAME();
        TextDrawable drawable = getTextDrawable(name);
        img_name.setImageDrawable(drawable);
        txl.setLeftString(name)
                .setRightString(personModel.getUSERNAME())
                .setRightTextColor(ResUtils.getColor(R.color.comm_blue))
                .setRightTvClickListener(new SuperTextView.OnRightTvClickListener() {
                    @Override
                    public void onClickListener() {
                        final Intent callIntent = IntentUtils.getCallIntent(personModel.getUSERNAME());
                        ActivityUtils.startActivity(callIntent);
                    }
                })
                .setLeftBottomString(personModel.getACCOUNTTYPE());
    }


    public List<MultiItemEntity> query(String text) {
        final List<MultiItemEntity> data = getData();
        final ArrayList<MultiItemEntity> txlPersonModels = new ArrayList<>();
        if (!StringUtils.isEmpty(text)) {
            for (MultiItemEntity m : data) {
                if (m instanceof TxlTypeModel) {
                    TxlTypeModel t = (TxlTypeModel) m;
                    final List<TxlPersonModel> person = t.getPerson();
                    for (TxlPersonModel p : person) {
                        if (p.getNAME().contains(text) || p.getUSERNAME().contains(text)) {
                            txlPersonModels.add(p);
                        }
                    }

                } else if (m instanceof TxlPersonModel) {
                    TxlPersonModel p = (TxlPersonModel) m;
                    if (p.getNAME().contains(text) || p.getUSERNAME().contains(text)) {
                        txlPersonModels.add(p);
                    }
                }
            }
        }

        return txlPersonModels;
    }

}
