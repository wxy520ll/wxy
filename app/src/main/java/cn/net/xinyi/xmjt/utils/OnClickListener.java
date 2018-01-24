package cn.net.xinyi.xmjt.utils;

import android.view.View;

import java.util.Date;

/**
 * Created by studyjun on 2016/6/6.
 */
public abstract class OnClickListener implements View.OnClickListener {

    long time = 0l;
    long lastTime =0l;

    @Override
    public void onClick(View v) {
        time = new Date().getTime();
        if (time-lastTime<=500){ //500毫秒只能点击一次
            return;
        }
        lastTime=time;
        onclick(v);
    }


    public  abstract void onclick(View view);
}
