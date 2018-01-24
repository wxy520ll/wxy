package cn.net.xinyi.xmjt.activity.ZHFK.PlcBx.person;

import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.List;

import cn.net.xinyi.xmjt.R;

/**
 * Created by gjz on 9/4/16.
 */
public class PcsAdapter extends RecyclerView.Adapter<PcsAdapter.PcsViewHolder> {

    private List<String> pcss;
    public int selectIndex = -1;

    public interface OnItemClickListener {
        void onItemClick(int position);
    }

    private OnItemClickListener onItemClickListener;

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

    public PcsAdapter(List<String> pcss) {
        this.pcss = pcss;
    }

    @Override
    public PcsViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.item_pcs, null);
        return new PcsViewHolder(view);
    }

    @Override
    public void onBindViewHolder(PcsViewHolder holder, final int position) {
        String contact = pcss.get(position);
        holder.tv_pcs.setText(contact);
        holder.ll_pcs.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (onItemClickListener != null) {
                    onItemClickListener.onItemClick(position);
                }
            }
        });

        if (selectIndex == position) {
            holder.ll_pcs.setBackgroundColor(Color.GRAY);
        } else {
            holder.ll_pcs.setBackgroundColor(Color.WHITE);
        }
    }

    @Override
    public int getItemCount() {
        return pcss.size();
    }

    class PcsViewHolder extends RecyclerView.ViewHolder {
        public TextView tv_pcs;
        public LinearLayout ll_pcs;

        public PcsViewHolder(View itemView) {
            super(itemView);
            tv_pcs = (TextView) itemView.findViewById(R.id.tv_pcs);
            ll_pcs = (LinearLayout) itemView.findViewById(R.id.ll_pcs);
        }
    }
}