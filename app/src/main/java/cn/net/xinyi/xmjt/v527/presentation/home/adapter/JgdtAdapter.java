package cn.net.xinyi.xmjt.v527.presentation.home.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.alibaba.android.vlayout.DelegateAdapter;
import com.alibaba.android.vlayout.LayoutHelper;
import com.blankj.utilcode.util.Utils;
import com.gongwen.marqueen.MarqueeFactory;
import com.gongwen.marqueen.SimpleMarqueeView;

import java.util.ArrayList;
import java.util.List;

import cn.net.xinyi.xmjt.R;
import cn.net.xinyi.xmjt.v527.data.remote.retrofit.entity.JqdtEntity;
import cn.net.xinyi.xmjt.v527.util.TimeUtils2;
import cn.net.xinyi.xmjt.v527.widget.SelectDayView;


/**
 * Created by Fracesuit on 2017/6/29.
 */

public class JgdtAdapter extends DelegateAdapter.Adapter<JgdtAdapter.JgdtHolder> {
    private List<JqdtEntity> jgtds = new ArrayList<>();
    private LayoutHelper mLayoutHelper;

    public JgdtAdapter(LayoutHelper layoutHelper) {
        this.mLayoutHelper = layoutHelper;
    }

    @Override
    public LayoutHelper onCreateLayoutHelper() {
        return mLayoutHelper;
    }

    @Override
    public JgdtHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new JgdtHolder(LayoutInflater.from(Utils.getApp()).inflate(R.layout.item_jgdt, parent, false));
    }

    @Override
    public void onBindViewHolder(JgdtHolder holder, final int position) {
        SimpleMF marqueeFactory = new SimpleMF(Utils.getApp());
        marqueeFactory.setData(jgtds);
        holder.simpleMarqueeView.setMarqueeFactory(marqueeFactory);
        holder.simpleMarqueeView.startFlipping();

        holder.selectView.setOnDaySelectClickListener(new SelectDayView.OnDaySelectClickListener() {
            @Override
            public void onDaySelect(int day) {
                if (onAdapteItemClickListener != null) {
                    final String time = day == 0 ? TimeUtils2.getTime(2) :
                            day == 1 ? TimeUtils2.getTime(5) : TimeUtils2.getTime(3);
                    onAdapteItemClickListener.onClick(100, time);
                }
            }
        });
        marqueeFactory.setOnItemClickListener(new MarqueeFactory.OnItemClickListener<TextView, JqdtEntity>() {
            @Override
            public void onItemClickListener(MarqueeFactory.ViewHolder<TextView, JqdtEntity> holder) {
                final String zyaq = holder.data.getZYAQ();
                if (onAdapteItemClickListener != null && !"暂时没有警情动态".equals(zyaq)) {
                    onAdapteItemClickListener.onClick(200, zyaq);
                }
            }
        });

    }

    @Override
    public int getItemCount() {
        return 1;
    }

    static class JgdtHolder extends RecyclerView.ViewHolder {
        SelectDayView selectView;
        SimpleMarqueeView simpleMarqueeView;

        JgdtHolder(View view) {
            super(view);
            selectView = (SelectDayView) view.findViewById(R.id.select_jkdt);
            simpleMarqueeView = (SimpleMarqueeView) view.findViewById(R.id.simpleMarqueeView);
        }
    }

    public interface OnAdapteItemClickListener {
        void onClick(int type, String param);
    }

    OnAdapteItemClickListener onAdapteItemClickListener;

    public void setOnAdapteItemClickListener(OnAdapteItemClickListener onAdapteItemClickListener) {
        this.onAdapteItemClickListener = onAdapteItemClickListener;
    }

    public void update(List<JqdtEntity> list) {
        if (list == null || list.size() == 0) {
            final JqdtEntity jqdtEntity = new JqdtEntity();
            jqdtEntity.setZYAQ("暂时没有警情动态");
            list.add(jqdtEntity);
        }
        jgtds.clear();
        jgtds.addAll(list);
        notifyDataSetChanged();
    }

    class SimpleMF extends MarqueeFactory<TextView, JqdtEntity> {
        public SimpleMF(Context mContext) {
            super(mContext);
        }

        @Override
        public TextView generateMarqueeItemView(JqdtEntity data) {
            TextView mView = new TextView(mContext);
            final String zyaq = data.getZYAQ();
            if ("暂时没有警情动态".equals(zyaq)) {
                mView.setText(zyaq);
            } else {
                mView.setText(data.getFASJCZ() + "    " + data.getABMC() + "  " + data.getFADD());
            }

            return mView;
        }
    }
}



