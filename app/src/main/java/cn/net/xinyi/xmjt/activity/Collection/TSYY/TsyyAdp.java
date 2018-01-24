package cn.net.xinyi.xmjt.activity.Collection.TSYY;

import android.content.Context;
import android.widget.AbsListView;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;

import java.util.Collection;
import java.util.HashMap;
import java.util.List;

import cn.net.xinyi.xmjt.R;
import cn.net.xinyi.xmjt.config.AdapterHolder;
import cn.net.xinyi.xmjt.config.BaseListAdp;
import cn.net.xinyi.xmjt.model.TssModle;
import cn.net.xinyi.xmjt.model.TssyyModle;

/**
 * Created by zhouhao on 16/12/23.
 */

public class TsyyAdp extends BaseListAdp<TssModle> {
    private final HashMap map;
    private  Context mContext;
    private int ztNum;

    public TsyyAdp(AbsListView view, Collection<TssModle> datas, int itemLayoutId, Context context) {
        super(view, datas, itemLayoutId, context);
        this.mContext=context;
        map=new HashMap();
    }

    @Override
    public void convert(AdapterHolder helper, TssModle item, boolean isScrolling) {
        TextView tv_fl1=(TextView)helper.getConvertView().findViewById(R.id.tv_fl1);
        TextView tv_fl2=(TextView)helper.getConvertView().findViewById(R.id.tv_fl2);

        helper.setText(R.id.tv_fjbh, item.getFJBH());
        ztNum=0;

        List<TssyyModle> tssyyModles = JSON.parseArray(item.getDATALIST(), TssyyModle.class);

        for (TssyyModle info:tssyyModles){
                if (info.getYYLX().equals("9")
                        ||info.getYYLX().equals("10")
                        ||info.getYYLX().equals("11")){//预约类型 09-12
                    if (info.getZT().equals("0")){//0是空闲中
                        tv_fl1.setTextColor(mContext.getResources().getColor(R.color.black));
                        tv_fl1.setBackgroundResource(R.drawable.bg_white_point);
                    }else if (info.getZT().equals("1")){//1是预约中
                        tv_fl1.setTextColor(mContext.getResources().getColor(R.color.white));
                        tv_fl1.setBackgroundResource(R.drawable.bg_orage_point);
                    }else if (info.getZT().equals("2")){//2是使用中
                        tv_fl1.setTextColor(mContext.getResources().getColor(R.color.white));
                        tv_fl1.setBackgroundResource(R.drawable.bg_red_point);
                    }
                }else if (info.getYYLX().equals("14")
                        ||info.getYYLX().equals("15")
                        ||info.getYYLX().equals("16")){//预约类型 14-16
                    if (info.getZT().equals("0")){//0是空闲中
                        tv_fl2.setTextColor(mContext.getResources().getColor(R.color.black));
                        tv_fl2.setBackgroundResource(R.drawable.bg_white_point);
                    }else if (info.getZT().equals("1")){//1是预约中
                        tv_fl2.setTextColor(mContext.getResources().getColor(R.color.white));
                        tv_fl2.setBackgroundResource(R.drawable.bg_orage_point);
                    }else if (info.getZT().equals("2")){//2是使用中
                        tv_fl2.setTextColor(mContext.getResources().getColor(R.color.white));
                        tv_fl2.setBackgroundResource(R.drawable.bg_red_point);
                    }
                }else {
                    ztNum++;
                }
        }
        map.put(helper.getPosition(),ztNum);
    }


    public int getZtTotal(int position) {
        if (map.size()>0){
            return Integer.parseInt(map.get(position).toString());
        }
        return 0;
    }
}
