package cn.net.xinyi.xmjt.activity.ZHFK.Duty;

import android.content.Context;
import android.widget.AbsListView;

import java.text.ParseException;
import java.util.Collection;
import java.util.Date;

import cn.net.xinyi.xmjt.R;
import cn.net.xinyi.xmjt.config.AdapterHolder;
import cn.net.xinyi.xmjt.config.BaseListAdp;
import cn.net.xinyi.xmjt.model.DutyOperationModle;
import cn.net.xinyi.xmjt.utils.DateUtil;

/**
 * Created by hao.zhou on 2016/4/14.
 */
public class DutyStartAdp extends BaseListAdp<DutyOperationModle> {
    private Context context;

    public DutyStartAdp(AbsListView view, Collection<DutyOperationModle> mDatas, int itemLayoutId, Context mContext) {
        super(view, mDatas, itemLayoutId, mContext);
        context = this.mContext;
    }

    @Override
    public void convert(AdapterHolder helper, DutyOperationModle item, boolean isScrolling) {
        helper.setText(R.id.tv_dz, item.getADDRESS());
        if (item.getDUTY_OPR_TYPE().equals(DutyStartAty.TYPE_BEATS_START)) {//巡逻
            helper.setText(R.id.tv_year, item.getCREATETIME());
            helper.setText(R.id.tv_lx, context.getResources().getString(R.string.duty_beats));
            helper.getConvertView().findViewById(R.id.tv_lx)
                    .setBackgroundColor(context.getResources().getColor(R.color.blue));
            try {
                int minuteBetween= DateUtil.minuteBetween(DateUtil.date2String(new Date(),"yyyy-MM-dd HH:mm"),item.getCREATETIME());
                helper.setText(R.id.tv_xldmc,item.getTOTALTIME()==0 ?"":"当前剩余勤务时间："+(item.getTOTALTIME()-minuteBetween)+"分钟");//勤务间隔时间
            } catch (ParseException e) {
                e.printStackTrace();
            }
        } else if (item.getDUTY_OPR_TYPE().equals(DutyStartAty.TYPE_BOXS)) {//签到
            helper.setText(R.id.tv_year, item.getCREATETIME());
            helper.setText(R.id.tv_lx, context.getResources().getString(R.string.duty_point));
            helper.setText(R.id.tv_xldmc, item.getDUTY_SIGNBOX_NAME());
            helper.getConvertView().findViewById(R.id.tv_lx)
                    .setBackgroundColor(context.getResources().getColor(R.color.bg_dark));
        } else if (item.getDUTY_OPR_TYPE().equals(DutyStartAty.TYPE_REPORTS_START)) {//报备
            if (item.getENDDTIME().isEmpty()){
                helper.setText(R.id.tv_year,item.getCREATETIME());
            }else {
                helper.setText(R.id.tv_year,item.getCREATETIME()+"至"+item.getENDDTIME());
            }
            helper.setText(R.id.tv_lx, context.getResources().getString(R.string.duty_bb));
            helper.setText(R.id.tv_xldmc,item.getDESCRIPTION());
            helper.getConvertView().findViewById(R.id.tv_lx)
                    .setBackgroundColor(context.getResources().getColor(R.color.background_new_tag));
        } else if (item.getDUTY_OPR_TYPE().equals(DutyPoliceAty.TYPE_POLICE_START)) {//处警
            if (item.getENDDTIME().isEmpty()){
                helper.setText(R.id.tv_year,item.getCREATETIME());
            }else {
                helper.setText(R.id.tv_year,item.getCREATETIME()+"至"+item.getENDDTIME());
            }
            helper.setText(R.id.tv_xldmc, " ");
            helper.setText(R.id.tv_lx, context.getResources().getString(R.string.duty_police));
            helper.getConvertView().findViewById(R.id.tv_lx)
                    .setBackgroundColor(context.getResources().getColor(R.color.bbutton_success));
        }
    }
}
