package cn.net.xinyi.xmjt.v527.presentation.home.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.alibaba.android.vlayout.DelegateAdapter;
import com.alibaba.android.vlayout.LayoutHelper;
import com.blankj.utilcode.util.Utils;

import java.util.List;

import cn.net.xinyi.xmjt.R;
import cn.net.xinyi.xmjt.v527.manager.MainItemModel;


/**
 * Created by Fracesuit on 2017/6/29.
 */

public class ItemModelAdapter extends DelegateAdapter.Adapter<RecyclerView.ViewHolder> {
    private List<MainItemModel> items;
    private LayoutHelper mLayoutHelper;

    public static final int ITEMTYPE_HEAD = 111;
    public static final int ITEMTYPE_MODEL = 2222;

    public ItemModelAdapter(LayoutHelper layoutHelper, List<MainItemModel> items) {
        this.items = items;
        this.mLayoutHelper = layoutHelper;
    }

    @Override
    public LayoutHelper onCreateLayoutHelper() {
        return mLayoutHelper;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (viewType == ITEMTYPE_HEAD) {
            return new HeadHolder(LayoutInflater.from(Utils.getApp()).inflate(R.layout.item_model_head, parent, false));
        } else {
            return new ModelHolder(LayoutInflater.from(Utils.getApp()).inflate(R.layout.item_model_item, parent, false));
        }

    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {
        final int itemViewType = getItemViewType(position);
        switch (itemViewType) {
            case ITEMTYPE_HEAD:
                HeadHolder headHolder = (HeadHolder) holder;
                headHolder.headText.setText(items.get(position).getTitle());
                break;
            case ITEMTYPE_MODEL:
                ModelHolder modelHolder = (ModelHolder) holder;
                modelHolder.item_module_name.setText(items.get(position).getTitle());
                modelHolder.item_module_img.setImageResource(items.get(position).getDrawableId());
                modelHolder.root_model.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (onItemClickListener != null) {
                            onItemClickListener.onItemClick(items.get(position), position);
                        }
                    }
                });
                break;
        }
    }


    @Override
    public int getItemViewType(int position) {
        return items.get(position).getDrawableId() == 0 ? ITEMTYPE_HEAD : ITEMTYPE_MODEL;
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    static class ModelHolder extends RecyclerView.ViewHolder {
        ImageView item_module_img;
        TextView item_module_name;
        View root_model;

        ModelHolder(View view) {
            super(view);
            item_module_img = (ImageView) view.findViewById(R.id.item_module_img);
            item_module_name = (TextView) view.findViewById(R.id.item_module_name);
            root_model = view.findViewById(R.id.root_model);
        }
    }

    static class HeadHolder extends RecyclerView.ViewHolder {
        TextView headText;

        HeadHolder(View view) {
            super(view);
            headText = (TextView) view.findViewById(R.id.tv_head);
        }
    }

    OnItemClickListener onItemClickListener;

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

    public interface OnItemClickListener {
        void onItemClick(MainItemModel imageTitleModel, int position);
    }
}



