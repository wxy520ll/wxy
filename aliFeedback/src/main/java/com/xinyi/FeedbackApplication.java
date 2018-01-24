package com.xinyi;

import android.app.Application;

import com.alibaba.sdk.android.feedback.impl.FeedbackAPI;

/**
 * Created by Fracesuit on 2017/12/22.
 * <p>
 * 集成用户反馈  只需要初始化这个类就行了，无需其他操作
 */

public class FeedbackApplication {

    public static void init(Application application, String feedKey) {
        FeedbackAPI.init(application, feedKey);
    }
}
