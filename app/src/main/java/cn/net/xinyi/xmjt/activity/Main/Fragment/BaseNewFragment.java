package cn.net.xinyi.xmjt.activity.Main.Fragment;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Toast;

import com.umeng.analytics.MobclickAgent;

import cn.net.xinyi.xmjt.R;
import cn.net.xinyi.xmjt.config.AppContext;
import cn.net.xinyi.xmjt.model.UserInfo;

/**
 * Created by zhiren.zhang on 2016/8/19.
 */
public class BaseNewFragment extends Fragment {
    private static final String TAG = "BaseNewFragment";


    protected FragmentActivity activity;
    protected UserInfo userInfo;
    private ProgressDialog mProgressDialog;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activity=getActivity();
        userInfo = AppContext.instance.getLoginInfo();
    }

    /**
     * 显示加载中，默认文本
     */
    public void showLoadding(){
        if (mProgressDialog==null){
            mProgressDialog = ProgressDialog.show(activity,null,"加载中...",true,true);
            mProgressDialog.setCancelable(false);
        }
        mProgressDialog.setMessage("加载中...");
        if (!mProgressDialog.isShowing()){
            mProgressDialog.show();
        }
    }

    /**
     * 关闭加载对话框
     */
    public void stopLoading(){
        if (mProgressDialog!=null&&mProgressDialog.isShowing()){
            mProgressDialog.dismiss();
        }
        mProgressDialog=null;
    }

    /***
     * toast a message with short time
     */
    public void toast(String msg){
        Toast.makeText(activity.getApplicationContext(),msg,Toast.LENGTH_SHORT).show();
    }


    protected void showActivity(Class<?> cls) {
        Intent intent = new Intent();
        intent.setClass(activity, cls);
        startActivity(intent);
    }

    /**
     * 判断GPS是否开启，GPS或者AGPS开启一个就认为是开启的
     * @param context
     * @return true 表示开启
     */
    public static final boolean isOPen(final Context context) {
        LocationManager locationManager
                = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);
        // 通过GPS卫星定位，定位级别可以精确到街（通过24颗卫星定位，在室外和空旷的地方定位准确、速度快）
        boolean gps = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
        // 通过WLAN或移动网络(3G/2G)确定的位置（也称作AGPS，辅助GPS定位。主要用于在室内或遮盖物（建筑群或茂密的深林等）密集的地方定位）
        boolean network = locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);
        return gps ;
    }


    public void isOpenGps(Activity aty) {
        View view= LayoutInflater.from(aty).inflate(R.layout.aty_gps_tips,null);
        new AlertDialog.Builder(aty)
                .setTitle("GPS开启提示：")
                .setView(view)
                .setPositiveButton(R.string.sure, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                    }
                })
                .setCancelable(false)
                .show();
    }


    //共计三步，统计fragment第三步：因为本app包含fragment，要统计fragment，所以需要加这一句话  add20160819
    public void onResume() {
        super.onResume();
        MobclickAgent.onPageStart(this.getClass().getName()); //统计页面，"MainScreen"为页面名称，可自定义
    }
    public void onPause() {
        super.onPause();
        MobclickAgent.onPageEnd(this.getClass().getName());
    }


}
