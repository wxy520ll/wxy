package com.umeng.simplify.ui.util;

import android.content.Context;
import android.content.SharedPreferences;
import android.text.TextUtils;

/**
 * Created by studyjun on 2016/6/27.
 */
public class TopicUtil {
    public static final String SDF_DEPARTMENT_TOPIC="department_topic.sdf";
    public static final String SDF_DEPARTMENT_ALARM_TOPIC="department_alarm_topic.sdf";

    /**
     * 获取内部交流话题
     * @param orgName
     * @return
     */
    public static String getDepartmentTopicInName(Context context,String orgName){
        SharedPreferences sdf = context.getSharedPreferences(SDF_DEPARTMENT_TOPIC, Context.MODE_APPEND);
        if(TextUtils.isEmpty(sdf.getString(orgName,""))){
            initDepartmentTopic(context);
        }
        return sdf.getString(orgName,"");
    }

    /**
     * 获取内部话题
     * @param orgName
     * @return
     */
    public static String getDepartmentAlarmTopicInName(Context context,String orgName){
        SharedPreferences sdf = context.getSharedPreferences(SDF_DEPARTMENT_ALARM_TOPIC, Context.MODE_APPEND);
        if(TextUtils.isEmpty(sdf.getString(orgName,""))){
            initDepartmentAlarmTopic(context);
        }
        return sdf.getString(orgName,"5769fb76ee78504ca62290b2"); //获取不到默认为日常话题
    }

    /**
     * 内部交流警情
     */
    public static void initDepartmentTopic(Context context) {
        SharedPreferences sdf = context.getSharedPreferences(SDF_DEPARTMENT_TOPIC, Context.MODE_APPEND);
        SharedPreferences.Editor editor =sdf.edit();
        editor.putString("水径所","5770eb2a7019c95212e050c4");
        editor.putString("布吉所","5770eb3ad0146369496b3744");
        editor.putString("罗岗所","5770eb43b51b2d6cc39d170f");
        editor.putString("坂田所","5770eb4eb51b2d6cc39d1712");
        editor.putString("宝岗所","5770eb59ea77f7302db3786c");
        editor.putString("沙湾所","5770eb67ea77f7305ac6c889");
        editor.putString("南岭所","5770eb70b51b2d6cc39d1715");
        editor.putString("李朗所","5770eb7a7019c95212e050c7");
        editor.putString("横岗所","5770eb82ee785044820e25cd");
        editor.putString("荷坳所","5770eb8f7019c93ae00e2019");
        editor.putString("六约所","5770eb99ea77f7305ac6c88c");
        editor.putString("梧桐所","5770eba37019c96661cbf374");
        editor.putString("龙新所","5770ebafb51b2d5c66b00413");
        editor.putString("同乐所","5770ebb9ee785044820e25d0");
        editor.putString("龙岗所","5770ebc1d014632b74ad386e");
        editor.putString("宝龙所","5770ebcd7019c938966dc8ee");
        editor.putString("新生所","5770ebd655c40010d5338b31");
        editor.putString("爱联所","5770ebdeb51b2d6cc39d1718");
        editor.putString("盛平所","5770ebe7ea77f7305ac6c88f");
        editor.putString("新城所","5770ebefee785044820e25d3");
        editor.putString("平湖所","5770ebf9b51b2d7fd1535121");
        editor.putString("平南所","5770a516d0146369496b3724");
        editor.putString("辅城所","5770ec09ee78505bd933619c");
        editor.putString("坪地所","5770ec12ee785044820e25d6");
        editor.putString("大运城所","5770ec26ee78505bd933619f");
        editor.putString("戒毒所","5770ec30ee78505bd93361a2");
        editor.putString("行政科","5770ec37ee78505bd93361a5");
        editor.putString("保安办","5770ec40ea77f7305ac6c892");
        editor.putString("保安公司","5770ec4f55c40010d5338b34");
        editor.putString("指挥处","5770ec59d014632b74ad3872");
        editor.putString("人口管理科","5770ec62b51b2d5c66b00417");
        editor.putString("刑警大队","5770ec6b55c400fe5a2f4c5c");
        editor.putString("巡警大队","5770ec7755c40010d5338b37");
        editor.putString("国保大队","5770ec7f7019c93c421798e2");
        editor.putString("监察科","5770ec8aee785017557eaa69");
        editor.putString("看守所","5770ecd2b51b2d6cc39d171c");
        editor.putString("治安管理科","5770ecddb51b2d5c66b0041a");
        editor.putString("预审大队","5770ece57019c913789c4927");
        editor.putString("政治处","5770ececd0146369496b3747");
        editor.putString("巡警大队办公室","5770ed3f55c40010d5338b3a");
        editor.putString("法制科","5770ed4655c40010d5338b3d");
        editor.putString("消防大队","5770ed9bd014632b74ad3876");
        editor.putString("出入境管理科","5770edbfee78505bd93361a8");
        editor.putString("科技通信科","5770edd755c400fe5a2f4c5f");
        editor.putString("经侦大队","5770ede155c40010d5338b41");
        editor.putString("刑警大队综合科","5770edeaea77f7302db37870");
        editor.putString("卡点大队","5770edf1ee78505bd93361ab");
        editor.putString("龙岗分局","5770edfbea77f7305ac6c89f");
        editor.commit();


    }


    /**
     * 内部
     */
    public static void initDepartmentAlarmTopic(Context context) {
        SharedPreferences sdf = context.getSharedPreferences(SDF_DEPARTMENT_ALARM_TOPIC, Context.MODE_APPEND);
        SharedPreferences.Editor editor =sdf.edit();
        editor.putString("水径所","57760a85d014636c8f0a54cc");
        editor.putString("布吉所","57760a98ea77f7920fcab9c4");
        editor.putString("罗岗所","57760aacee78502bc296cf74");
        editor.putString("坂田所","57760ab7d014636c8f0a54cf");
        editor.putString("宝岗所","57760ac0ea77f7920fcab9c7");
        editor.putString("沙湾所","57760ac8d014639a2bd66b5d");
        editor.putString("南岭所","57760ad1ea77f7920ec0969a");
        editor.putString("李朗所","57760ada55c400f4b1ccab62");
        editor.putString("横岗所","57760ae3ea77f7920fcab9ca");
        editor.putString("荷坳所","57760aecd014639a2bd66b60");
        editor.putString("六约所","57760afdb51b2dac37b521c8");
        editor.putString("梧桐所","57760b63b51b2dac36951f10");
        editor.putString("龙新所","57760b6dee78502bc58a3c40");
        editor.putString("同乐所","57760b7655c400f4b41735c4");
        editor.putString("龙岗所","57760b7f55c400f4b41735c7");
        editor.putString("宝龙所","57760b8db51b2dac37b521cb");
        editor.putString("新生所","57760b97ee78502bc58a3c43");
        editor.putString("爱联所","57760ba955c400f4b41735ca");
        editor.putString("盛平所","57760bb155c400f4b41735cd");
        editor.putString("新城所","57760bbad014636c8f0a54d2");
        editor.putString("平湖所","57760bc3b51b2dac37b521e0");
        editor.putString("平南所","57760bccd014636c8f0a54d7");
        editor.putString("辅城所","57760bd4d014639a2bd66b63");
        editor.putString("坪地所","57760bde55c400f4b41735d0");
        editor.putString("大运城所","57760befd014636c8f0a54da");
        editor.putString("日常","5769fb76ee78504ca62290b2");
        editor.commit();


    }
}
