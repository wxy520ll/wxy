package cn.net.xinyi.xmjt.utils;

import android.app.Notification;
import android.app.NotificationManager;
import android.content.Context;

import cn.net.xinyi.xmjt.R;

/**
 * Created by studyjun on 2016/5/12.
 */
public class NotificationHelper {

    /**
     * 创建巡逻打卡通知
     *
     * @param context
     * @param xldwid
     */
    public static void createXLDK(Context context, String xldwid) {
        if (xldwid != null && StringUtils.isNumber(xldwid)) {
            Notification.Builder builder = new Notification.Builder(context);
            builder.setContentTitle("打卡提醒");
            builder.setContentText("你当前已进入巡逻点" + xldwid);
            builder.setSmallIcon(R.drawable.app_icon);
            NotificationManager manager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
            manager.notify(xldwid,Integer.parseInt(xldwid.substring(0,6)), builder.getNotification());

        }


    }

    /**
     * 清除巡逻打卡通知
     *
     * @param context
     * @param xldwid
     */
    public static void clearXLDK(Context context, String xldwid) {
        if (xldwid != null && StringUtils.isNumber(xldwid)) {
            NotificationManager manager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
            manager.cancel(Integer.parseInt(xldwid));
        }

    }
}
