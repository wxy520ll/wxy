package cn.net.xinyi.xmjt.service;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.view.WindowManager;

import com.alibaba.sdk.android.push.CloudPushService;
import com.alibaba.sdk.android.push.MessageReceiver;
import com.alibaba.sdk.android.push.noonesdk.PushServiceFactory;
import com.alibaba.sdk.android.push.notification.CPushMessage;
import com.google.gson.Gson;

import org.greenrobot.eventbus.EventBus;

import java.util.Iterator;
import java.util.Map;

import cn.net.xinyi.xmjt.config.AppContext;
import cn.net.xinyi.xmjt.model.DevicesPositionModel;
import cn.net.xinyi.xmjt.model.TaskModel;
import cn.net.xinyi.xmjt.model.View.TaskListModel;
import cn.net.xinyi.xmjt.utils.UI;

/**
 * @author: 正纬
 * @since: 15/4/9
 * @version: 1.1
 * @feature: 用于接收推送的通知和消息
 */
public class MyMessageReceiver extends MessageReceiver {
    Gson gson=new Gson();
    // 消息接收部分的LOG_TAG
    public static final String REC_TAG = "receiver";

    /**
     * 推送通知的回调方法
     * @param context
     * @param title
     * @param summary
     * @param extraMap
     */
    @Override
    public void onNotification(Context context, String title, String summary, Map<String, String> extraMap) {
        // TODO 处理推送通知
        if ( null != extraMap ) {
            for (Map.Entry<String, String> entry : extraMap.entrySet()) {
                // UI.toast(context,"@Get diy param : Key=" + entry.getKey() + " , Value=" + entry.getValue());
            }
        } else {
            UI.toast(context,"@收到通知 && 自定义消息为空");
        }
      //  UI.toast(context,"收到一条推送通知 ： " + title);
    }

    /**
     * 推送消息的回调方法
     * @param context
     * @param cPushMessage
     */
    @Override
    public synchronized void onMessage(Context context, CPushMessage cPushMessage) {
        try {
            if (cPushMessage.getTitle().equals("押解消息")) {
                DevicesPositionModel devicesPositionModel = gson.fromJson(cPushMessage.getContent(), DevicesPositionModel.class);
                if (devicesPositionModel.getCommand().equals("deviceData"))
                {
                    String s = cPushMessage.getTitle();
                    /*if (devicesPositionModel.getEscape().equals("1")) {
                        NotificationManager mNotificationManager = (NotificationManager) context.getSystemService(NOTIFICATION_SERVICE);
                        NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(context);
                        Intent resultIntent = new Intent(context, PlcDstrbtMpAty.class);
                        PendingIntent resultPendingIntent = PendingIntent.getActivity(context,0,resultIntent,PendingIntent.FLAG_UPDATE_CURRENT);
                        mBuilder.setContentTitle("告警！告警")//设置通知栏标题
                                .setContentText("有嫌疑外出逃注意") //设置通知栏显示内容
                                .setTicker("告警！告警") //通知首次出现在通知栏，带上升动画效果的
                                .setWhen(System.currentTimeMillis())//通知产生的时间，会在通知信息里显示，一般是系统获取到的时间
                                .setPriority(Notification.PRIORITY_DEFAULT) //设置该通知优先级
                                .setAutoCancel(true)//设置这个标志当用户单击面板就可以让通知将自动取消
                                .setContentIntent(resultPendingIntent)
                                .setOngoing(false)//ture，设置他为一个正在进行的通知。他们通常是用来表示一个后台任务,用户积极参与(如播放音乐)或以某种方式正在等待,因此占用设备(如一个文件下载,同步操作,主动网络连接)
                                .setSmallIcon(R.drawable.app_icon);//设置通知小ICON

                        mNotificationManager.notify(101, mBuilder.build());
                    }*/
                    EventBus.getDefault().postSticky(devicesPositionModel);
                }else {
                    boolean bl=false;
                    TaskModel taskModel=gson.fromJson(cPushMessage.getContent(),TaskModel.class);
                    TaskListModel.DataBean db=new TaskListModel.DataBean();
                    db.setId(taskModel.getTaskId());
                    db.setDestination(taskModel.getDestination());
                    db.setDevices(taskModel.getDevices());
                    db.setEscortType(taskModel.getEscortType());
                    db.setName(taskModel.getName());
                    db.setState(taskModel.getState());
                    db.setUnitId(taskModel.getUnitId());
                    Map<String,TaskListModel.DataBean>taskMap= AppContext.getTaskMap();
                    Iterator<Map.Entry<String,TaskListModel.DataBean>>iterator=taskMap.entrySet().iterator();
                    while (iterator.hasNext()){
                        Map.Entry<String, TaskListModel.DataBean> entry = iterator.next();
                       if (taskModel.getTaskId().equals(entry.getKey())){
                           bl=true;
                           TaskListModel.DataBean db1=entry.getValue();
                           db1.setId(taskModel.getTaskId());
                           db1.setDestination(taskModel.getDestination());
                           db1.setDevices(taskModel.getDevices());
                           db1.setEscortType(taskModel.getEscortType());
                           db1.setName(taskModel.getName());
                           db1.setState(taskModel.getState());
                           db1.setUnitId(taskModel.getUnitId());
                       }
                    }
                    if (!bl){
                        taskMap.put(""+taskModel.getTaskId(),db);
                    }
                    //UI.toast(AppContext.instance,"收到押解任务");
                    EventBus.getDefault().postSticky(taskModel);
                }
            }
            if (!cPushMessage.getTitle().equals("押解消息")) {
                UI.toast(context, "收到一条推送消息 ： " + cPushMessage.getTitle());
            }
//            // 持久化推送的消息到数据库
//            new MessageDao(context).add(new MessageEntity(cPushMessage.getMessageId().substring(6, 16), Integer.valueOf(cPushMessage.getAppId()), cPushMessage.getTitle(), cPushMessage.getContent(), new SimpleDateFormat("HH:mm:ss").format(new Date())));
//            // 刷新下消息列表
//            ActivityBox.CPDMainActivity.initMessageView();
        } catch (Exception e) {
            UI.toast(context, e.toString());
        }
    }

    /**
     * 从通知栏打开通知的扩展处理
     * @param context
     * @param title
     * @param summary
     * @param extraMap
     */
    @Override
    public void onNotificationOpened(Context context, String title, String summary, String extraMap) {
        CloudPushService cloudPushService = PushServiceFactory.getCloudPushService();
//       cloudPushService.setNotificationSoundFilePath();
//       UI.toast(context,"onNotificationOpened ： " + " : " + title + " : " + summary + " : " + extraMap);


        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle(title).setMessage(summary);
        builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        });
        final AlertDialog dialog = builder.create();
        //在dialog  show方法之前添加如下代码，表示该dialog是一个系统的dialog**
        dialog.getWindow().setType((WindowManager.LayoutParams.TYPE_SYSTEM_ALERT));
        dialog.show();
    }


    @Override
    public void onNotificationRemoved(Context context, String messageId) {
       // UI.toast(context, "onNotificationRemoved ： " + messageId);
    }


    @Override
    protected void onNotificationClickedWithNoAction(Context context, String title, String summary, String extraMap) {
       // UI.toast(context,"onNotificationClickedWithNoAction ： " + " : " + title + " : " + summary + " : " + extraMap);
    }
}