package cn.net.xinyi.xmjt.utils;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.TimeZone;

import cn.net.xinyi.xmjt.model.Update;

/**
 * Created by mazhongwang on 15/8/30.
 */
public class DateUtil {
    /**定义常量**/
    public static final String DATE_JFP_STR="yyyyMM";
    public static final String DATE_FULL_STR = "yyyy-MM-dd HH:mm:ss";
    public static final String DATE_SMALL_STR = "yyyy-MM-dd";
    public static final String DATE_KEY_STR = "yyMMddHHmmss";

    public static int daysBetween(String smdate, String bdate) throws ParseException {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        Calendar cal = Calendar.getInstance();
        cal.setTime(sdf.parse(smdate));
        long time1 = cal.getTimeInMillis();
        cal.setTime(sdf.parse(bdate));
        long time2 = cal.getTimeInMillis();
        long between_days = (time2 - time1) / (1000 * 3600 * 24);
        return Integer.parseInt(String.valueOf(between_days));
    }


    public static String date2String(Date date, String pattern) {
        SimpleDateFormat sdf = new SimpleDateFormat(pattern);
        return sdf.format(date);
    }

    public static Date string2Date(String date) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");
        try {
            return sdf.parse(date);
        } catch (ParseException e) {
            e.printStackTrace();

        }
        return null;

    }


    public static List<Update> get7date(String simp) {
        final String dayNames[] = { "星期日", "星期一", "星期二", "星期三", "星期四", "星期五", "星期六" };
        List<Update> dates = new ArrayList<Update>();
        Update update=new Update();//用更新的类复用此

        final Calendar c = Calendar.getInstance();
        c.setTimeZone(TimeZone.getTimeZone("GMT+8:00"));
        SimpleDateFormat sim = new SimpleDateFormat(simp);
        String date = sim.format(c.getTime());

        int dayOfWeek= c.get(Calendar.DAY_OF_WEEK);
        update.setDownloadUrl(date);//日期
        update.setVersionName(dayNames[dayOfWeek-1]);//星期
        if (!dayNames[dayOfWeek-1].contains("六")
                &&!dayNames[dayOfWeek-1].contains("日")) { //过滤周六、周日
            dates.add(update);
        }
        for (int i = 0; i < 6; i++) {
            update=new Update();//用更新的类复用此
            c.add(Calendar.DAY_OF_MONTH, 1);
            dayOfWeek= c.get(Calendar.DAY_OF_WEEK);
            date = sim.format(c.getTime());
            update.setDownloadUrl(date);//日期
            update.setVersionName(dayNames[dayOfWeek-1]);//星期
            if (!dayNames[dayOfWeek-1].contains("六")
                    &&!dayNames[dayOfWeek-1].contains("日")){ //过滤周六、周日
                dates.add(update);
            }
        }
        return dates;
    }


    public static Date string2Date(String date,String pattern) {
        SimpleDateFormat sdf = new SimpleDateFormat(pattern);
        try {
            return sdf.parse(date);
        } catch (ParseException e) {
            e.printStackTrace();

        }
        return null;
    }

    public static int Date2Minute(Date date) {
        return (date.getHours()*60)+date.getMinutes();
    }


    public static int minuteBetween(String date,String smdate) throws ParseException {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        Calendar cal = Calendar.getInstance();
        cal.setTime(sdf.parse(date));
        long time1 = cal.getTimeInMillis();
        cal.setTime(sdf.parse(smdate));
        long time2 = cal.getTimeInMillis();
        long between_minute = (time1 - time2) / (1000 * 60);
        return Integer.parseInt(String.valueOf(between_minute));
    }

   public static long secondBetween(String date,String smdate) throws ParseException {
        SimpleDateFormat sdf = new SimpleDateFormat(DATE_FULL_STR);
        Calendar cal = Calendar.getInstance();
        cal.setTime(sdf.parse(date));
        long time1 = cal.getTimeInMillis();
        cal.setTime(sdf.parse(smdate));
        long time2 = cal.getTimeInMillis();
        return time1 - time2;
    }



    public static String Date2Weekend(String s, String smdate) {


        final String dayNames[] = { "星期日", "星期一", "星期二", "星期三", "星期四", "星期五", "星期六" };
        SimpleDateFormat sdfInput = new SimpleDateFormat(smdate);
        Calendar calendar = Calendar.getInstance();
        Date date = new Date();
        try {
            date = sdfInput.parse(s);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        calendar.setTime(date);
        int dayOfWeek = calendar.get(Calendar.DAY_OF_WEEK);
        return dayNames[dayOfWeek - 1];
    }


    /**
     * 获取系统当前时间
     * @return
     */
    public static String getNowTime() {
        SimpleDateFormat df = new SimpleDateFormat(DATE_FULL_STR);
        return df.format(new Date());
    }

    /**
     * 获取系统当前时间
     * @return
     */
    public static String getNowTime(String type) {
        SimpleDateFormat df = new SimpleDateFormat(type);
        return df.format(new Date());
    }

    /**
     * 获取系统当前计费期
     * @return
     */
    public static String getJFPTime() {
        SimpleDateFormat df = new SimpleDateFormat(DATE_JFP_STR);
        return df.format(new Date());
    }

    /**
     * 将指定的日期转换成Unix时间戳
     * @paramString date 需要转换的日期 yyyy-MM-dd HH:mm:ss
     * @return long 时间戳
     */
    public static long dateToUnixTimestamp(String date) {
        long timestamp = 0;
        try {
            timestamp = new SimpleDateFormat(DATE_FULL_STR).parse(date).getTime();
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return timestamp;
    }

    /**
     * 将指定的日期转换成Unix时间戳
     * @paramString date 需要转换的日期 yyyy-MM-dd
     * @return long 时间戳
     */
    public static long dateToUnixTimestamp(String date, String dateFormat) {
        long timestamp = 0;
        try {
            timestamp = new SimpleDateFormat(dateFormat).parse(date).getTime();
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return timestamp;
    }

    /**
     * 将当前日期转换成Unix时间戳
     * @return long 时间戳
     */
    public static long dateToUnixTimestamp() {
        long timestamp = new Date().getTime();
        return timestamp;
    }


    /**
     * unixTimes将时间戳转换成日期
     * @paramlong timestamp 时间戳 毫秒
     * @return String 日期字符串
     */
    public static String unixTimestampToDate(long timestamp) {
        SimpleDateFormat sd = new SimpleDateFormat(DATE_FULL_STR);
        sd.setTimeZone(TimeZone.getTimeZone("GMT+8"));
        return sd.format(new Date(timestamp*1000));
    }




    /**
     * 将Unix时间戳转换成日期
     * @paramlong timestamp 时间戳
     * @return String 日期字符串
     */
    public static long getDays2NowValus(String time1) {
        DateFormat df = new SimpleDateFormat(DATE_FULL_STR);
        long date = 0;
        //预约类型
        try {
            date = df.parse(time1).getTime()-new Date(System.currentTimeMillis()).getTime();
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return date;
    }

    /* 将字符串转为时间戳 */
    public static String getTimeToStamp(String time) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy年MM月dd日HH时mm分ss秒",
                Locale.CHINA);
        Date date = new Date();
        try {
            date = sdf.parse(time);
        } catch (ParseException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        String tmptime = String.valueOf(date.getTime()).substring(0, 10);

        return tmptime;
    }




}