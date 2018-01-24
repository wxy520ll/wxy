package cn.net.xinyi.xmjt.activity.ZHFK.PlcBx;

import android.content.Context;
import android.widget.AbsListView;

import java.text.ParseException;
import java.util.Collection;
import java.util.Date;

import cn.net.xinyi.xmjt.R;
import cn.net.xinyi.xmjt.config.AdapterHolder;
import cn.net.xinyi.xmjt.config.BaseListAdp;
import cn.net.xinyi.xmjt.model.PlcBxWorkLog;
import cn.net.xinyi.xmjt.utils.DateUtil;

/**
 * Created by hao.zhou on 2016/5/10.
 */
public class PlcBxZSAdp extends BaseListAdp<PlcBxWorkLog> {
    private Context context;
    public PlcBxZSAdp(AbsListView view, Collection<PlcBxWorkLog> datas, int itemLayoutId, Context context) {
        super(view, datas, itemLayoutId, context);
        this.context=context;
    }

    @Override
    public void convert(AdapterHolder helper, PlcBxWorkLog item, boolean isScrolling) {
        if (item.getLX().equals("1")){//开始值守
            try {
                int minuteBetween= DateUtil.minuteBetween(DateUtil.date2String(new Date(),"yyyy-MM-dd HH:mm"),item.getSCSJ());
                helper.setText(R.id.tv_dz,item.getTOTALTIME()==0 ?"":"当前剩余勤务时间："+(item.getTOTALTIME()-minuteBetween)+"分钟");//勤务间隔时间
            } catch (ParseException e) {
                e.printStackTrace();
            }
            helper.setText(R.id.tv_year,item.getSCSJ());
            helper.setText(R.id.tv_lx, context.getResources().getString(R.string.plc_sttn_startwork));
            helper.getConvertView().findViewById(R.id.tv_lx)
                    .setBackgroundColor(context.getResources().getColor(R.color.blue));
        }else if (item.getLX().equals("2")){//结束值守
            helper.setText(R.id.tv_dz,"");
            helper.setText(R.id.tv_year,item.getSCSJ());
            helper.setText(R.id.tv_lx, context.getResources().getString(R.string.ending_work_plc_bx));
            helper.getConvertView().findViewById(R.id.tv_lx)
                    .setBackgroundColor(context.getResources().getColor(R.color.blue));
        }else if (item.getLX().equals("3")){//离岗报备
            helper.setText(R.id.tv_dz,item.getMS());
            helper.setText(R.id.tv_year,item.getSCSJ());
            helper.setText(R.id.tv_lx, context.getResources().getString(R.string.bb_work_plc_bx));
            helper.getConvertView().findViewById(R.id.tv_lx)
                    .setBackgroundColor(context.getResources().getColor(R.color.bbutton_danger));
        }else if (item.getLX().equals("4")){//擅自离岗
            helper.setText(R.id.tv_dz,"");
            helper.setText(R.id.tv_year,item.getSCSJ());
            helper.setText(R.id.tv_lx, context.getResources().getString(R.string.sz_work_plc_bx));
            helper.getConvertView().findViewById(R.id.tv_lx)
                    .setBackgroundColor(context.getResources().getColor(R.color.bbutton_danger));
        }else if (item.getLX().equals("5")){//继续值守
            helper.setText(R.id.tv_dz,"");
            helper.setText(R.id.tv_year,item.getSCSJ());
            helper.setText(R.id.tv_lx, context.getResources().getString(R.string.contin_work_plc_bx));
            helper.getConvertView().findViewById(R.id.tv_lx)
                    .setBackgroundColor(context.getResources().getColor(R.color.blue));
        }

    }
}
