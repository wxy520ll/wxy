package cn.net.xinyi.xmjt.utils;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.util.DisplayMetrics;
import android.view.View;
import android.widget.Toast;


/**
 * Created by studyjun on 15/10/28.
 */
public class UI {
    /**
     * dp转px
     *
     * @param context
     * @param dipValue
     * @return
     */
    public static int dip2px(Context context, float dipValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dipValue * scale + 0.5f);
    }

    public static void toast(Context context, String text) {
        Toast.makeText(context, text, Toast.LENGTH_SHORT).show();
    }


    /**
     * dx转dp
     *
     * @param context
     * @param pxValue
     * @return
     */
    public static int px2dip(Context context, float pxValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (pxValue / scale + 0.5f);
    }



    public static int getScreenWidth(Activity context) {
        DisplayMetrics dm = new DisplayMetrics();
        // 获取屏幕信息
        context.getWindowManager().getDefaultDisplay().getMetrics(dm);
//		int screenHeigh = dm.heightPixels;
        return dm.widthPixels;


    }

    public static int getScreenHeight(Activity context) {
        DisplayMetrics dm = new DisplayMetrics();
        // 获取屏幕信息
        context.getWindowManager().getDefaultDisplay().getMetrics(dm);
//		int screenHeigh = dm.heightPixels;
        return dm.heightPixels;


    }

    /**
     * 启动activity
     * @param context
     * @param clazz
     */
    public static void startActivity(Context context,Class clazz){
        Intent intent = new Intent(context,clazz);
        context.startActivity(intent);
    }

    /**
     * 启动activity
     * @param context
     * @param clazz
     */
    public static void startActivity(Context context,Class clazz,Intent intent){
        context.startActivity(intent);
    }
    /**
     * 启动activity
     * @param context
     * @param clazz
     */
    public static void startActivity(int flag,Context context,Class clazz){
        Intent intent = new Intent(context,clazz);
        intent.setFlags(flag);
        context.startActivity(intent);
    }

    /**
     * 启动activity
     * @param context
     * @param clazz
     */
    public static void startActivity(Activity context,Class clazz,int requestCode){
        Intent intent = new Intent(context,clazz);
        context.startActivityForResult(intent, requestCode);
    }

    /**
     * 显示
     * @param context
     * @param view
     * @param
     */
    public static void toastView(Context context,View view ,int gravity){
        Toast toast = new Toast(context);
        toast.setView(view);
        toast.setGravity(gravity,0,0);
        toast.show();
    }


}
