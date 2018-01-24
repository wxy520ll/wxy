package cn.net.xinyi.xmjt.v527.util;

import com.blankj.utilcode.constant.TimeConstants;
import com.blankj.utilcode.util.TimeUtils;
import com.xinyi_tech.comm.constant.CommConstant;

import java.util.Calendar;
import java.util.Date;

/**
 * Created by zhiren.zhang on 2016/11/29.
 */
public class TimeUtils2 {
    private static final String TAG = "TimeUtils";
    public static final String TIME_0 = " 00:00:00";
    public static final String TIME_59 = " 23:59:59";

    public static String getTime(int time) {

        String startTime = null;
        String jintian = TimeUtils.date2String(new Date(), CommConstant.DeteFromat.DEFAULT_FORMAT_DAY);
        String endTime = jintian + TIME_59;
        ;
        switch (time) {
            case 0:
                String temp = TimeUtils.date2String(new Date(), CommConstant.DeteFromat.DEFAULT_FORMAT_DAY);
                startTime = temp + TIME_0;
                break;// 今天
            case 1:
                String zuotian = TimeUtils.getStringByNow(-1, CommConstant.DeteFromat.DEFAULT_FORMAT_DAY, TimeConstants.DAY);
                startTime = zuotian + TIME_0;
                endTime = zuotian + TIME_59;
                break;// 昨天
            case 2:
                String benzhou = TimeUtils.getStringByNow(-7, CommConstant.DeteFromat.DEFAULT_FORMAT_DAY, TimeConstants.DAY);
                startTime = benzhou + TIME_0;
                break;// 本周
            case 3:
                Calendar cal = Calendar.getInstance();
                cal.setTime(new Date());
                cal.add(Calendar.MONTH, -1);
                String month = TimeUtils.date2String(cal.getTime(), CommConstant.DeteFromat.DEFAULT_FORMAT_DAY);
                startTime = month + TIME_0;
                break;// 本月
            case 4:
                Calendar yearCal = Calendar.getInstance();
                yearCal.setTime(new Date());
                yearCal.add(Calendar.YEAR, -1);
                String year = TimeUtils.date2String(yearCal.getTime(), CommConstant.DeteFromat.DEFAULT_FORMAT_DAY);
                startTime = year + TIME_0;
                break;// 一年
            case 5:
                String tian15 = TimeUtils.getStringByNow(-15, CommConstant.DeteFromat.DEFAULT_FORMAT_DAY, TimeConstants.DAY);
                startTime = tian15 + TIME_0;
                break;// 15天
        }
        return startTime + "," + endTime;
    }
}
