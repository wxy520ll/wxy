package cn.net.xinyi.xmjt.v527.presentation.home.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.alibaba.android.vlayout.DelegateAdapter;
import com.alibaba.android.vlayout.LayoutHelper;
import com.allen.library.SuperTextView;
import com.blankj.utilcode.util.Utils;

import java.util.ArrayList;
import java.util.List;

import cn.net.xinyi.xmjt.R;
import cn.net.xinyi.xmjt.v527.data.remote.retrofit.entity.GzcxEntity;
import cn.net.xinyi.xmjt.v527.util.TimeUtils2;
import cn.net.xinyi.xmjt.v527.widget.SelectDayView;


/**
 * Created by Fracesuit on 2017/6/29.
 */

public class GzcxAdapter extends DelegateAdapter.Adapter<GzcxAdapter.GzcxHolder> {
    private List<GzcxEntity> jgtds = new ArrayList<>();
    private LayoutHelper mLayoutHelper;

    public GzcxAdapter(LayoutHelper layoutHelper) {
        this.mLayoutHelper = layoutHelper;
    }

    @Override
    public LayoutHelper onCreateLayoutHelper() {
        return mLayoutHelper;
    }

    @Override
    public GzcxHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new GzcxHolder(LayoutInflater.from(Utils.getApp()).inflate(R.layout.item_gzcx, parent, false));
    }

    @Override
    public void onBindViewHolder(GzcxHolder holder, int position) {

        holder.selectView.setOnDaySelectClickListener(new SelectDayView.OnDaySelectClickListener() {
            @Override
            public void onDaySelect(int day) {
                if (onAdapteItemClickListener != null) {
                    final String time = day == 0 ? TimeUtils2.getTime(0) :
                            day == 1 ? TimeUtils2.getTime(2) : TimeUtils2.getTime(3);
                    onAdapteItemClickListener.onClick(100, time);
                }
            }
        });
        if (jgtds.size() == 1) {
            final GzcxEntity gzcxEntity = jgtds.get(position);
            holder.stvGzcx
                    .setLeftTopTvClickListener(new SuperTextView.OnLeftTopTvClickListener() {
                        @Override
                        public void onClickListener() {
                            if (onAdapteItemClickListener != null) {
                                onAdapteItemClickListener.onClick(200, "RYPCRANK");
                            }
                        }
                    })
                    .setLeftBottomTvClickListener(new SuperTextView.OnLeftBottomTvClickListener() {
                        @Override
                        public void onClickListener() {
                            if (onAdapteItemClickListener != null) {
                                onAdapteItemClickListener.onClick(200, "RYPCRANK");
                            }
                        }
                    })
                    .setLeftTvClickListener(new SuperTextView.OnLeftTvClickListener() {
                        @Override
                        public void onClickListener() {
                            if (onAdapteItemClickListener != null) {
                                onAdapteItemClickListener.onClick(200, "RYPCRANK");
                            }
                        }
                    })
                    .setCenterTopTvClickListener(new SuperTextView.OnCenterTopTvClickListener() {
                        @Override
                        public void onClickListener() {
                            if (onAdapteItemClickListener != null) {
                                onAdapteItemClickListener.onClick(200, "PLATERANK");
                            }
                        }
                    })
                    .setCenterBottomTvClickListener(new SuperTextView.OnCenterBottomTvClickListener() {
                        @Override
                        public void onClickListener() {
                            if (onAdapteItemClickListener != null) {
                                onAdapteItemClickListener.onClick(200, "PLATERANK");
                            }
                        }
                    })
                    .setCenterTvClickListener(new SuperTextView.OnCenterTvClickListener() {
                        @Override
                        public void onClickListener() {
                            if (onAdapteItemClickListener != null) {
                                onAdapteItemClickListener.onClick(200, "PLATERANK");
                            }
                        }
                    })
                    .setRightTopTvClickListener(new SuperTextView.OnRightTopTvClickListener() {
                        @Override
                        public void onClickListener() {
                            if (onAdapteItemClickListener != null) {
                                onAdapteItemClickListener.onClick(200, "XLJLRANK");
                            }
                        }
                    })
                    .setRightBottomTvClickListener(new SuperTextView.OnRightBottomTvClickListener() {
                        @Override
                        public void onClickListener() {
                            if (onAdapteItemClickListener != null) {
                                onAdapteItemClickListener.onClick(200, "XLJLRANK");
                            }
                        }
                    })
                    .setRightTvClickListener(new SuperTextView.OnRightTvClickListener() {
                        @Override
                        public void onClickListener() {
                            if (onAdapteItemClickListener != null) {
                                onAdapteItemClickListener.onClick(200, "XLJLRANK");
                            }
                        }
                    })
                    .setLeftBottomString(gzcxEntity.getRYPCZS() + "(" + gzcxEntity.getRYPCRANK() + ")")
                    .setCenterBottomString(gzcxEntity.getPLATEZS() + "(" + gzcxEntity.getPLATERANK() + ")")
                    .setRightBottomString(gzcxEntity.getXLJL() + "(" + gzcxEntity.getXLJLRANK() + ")");
        }

    }

    @Override
    public int getItemCount() {
        return 1;
    }

    static class GzcxHolder extends RecyclerView.ViewHolder {
        SelectDayView selectView;
        SuperTextView stvGzcx;

        GzcxHolder(View view) {
            super(view);
            selectView = (SelectDayView) view.findViewById(R.id.select_gzcx);
            stvGzcx = (SuperTextView) view.findViewById(R.id.stv_gzcx);
        }
    }

    public interface OnAdapteItemClickListener {
        void onClick(int type, String param);
    }

    OnAdapteItemClickListener onAdapteItemClickListener;

    public void setOnAdapteItemClickListener(OnAdapteItemClickListener onAdapteItemClickListener) {
        this.onAdapteItemClickListener = onAdapteItemClickListener;
    }

    public List<GzcxEntity> getJgtds() {
        return jgtds;
    }

    public void update(GzcxEntity gzcxEntity) {
        jgtds.clear();
        jgtds.add(gzcxEntity);
        notifyDataSetChanged();
    }
}



