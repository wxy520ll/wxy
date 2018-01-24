package com.dashi.fracesuit.bugly;

/**
 * Created by Fracesuit on 2017/6/13.
 */

import android.content.Context;

import com.tencent.bugly.Bugly;

/**
 * 集成在线更新
 * 只需要初始化这个类就可以
 * 无需多余操作
 */
public class BuglyApplication {
    public static void init(Context context, Boolean isDebug, String appid) {
        Bugly.init(context, appid, isDebug);
    }
}
