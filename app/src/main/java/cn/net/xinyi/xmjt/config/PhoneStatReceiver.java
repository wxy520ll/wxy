package cn.net.xinyi.xmjt.config;

import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.telephony.PhoneStateListener;
import android.telephony.TelephonyManager;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.android.internal.telephony.ITelephony;

import java.lang.reflect.Method;
import java.util.ArrayList;

import cn.net.xinyi.xmjt.R;
import cn.net.xinyi.xmjt.utils.ContactsHelper;
import cn.net.xinyi.xmjt.utils.StringUtils;

public class PhoneStatReceiver extends BroadcastReceiver {

    public static final String TAG = PhoneStatReceiver.class.getName();
    private Context context;
    private TextView txNumber;
    private TextView tvSysContactName;
    private TextView tvPolContactName;
    private LinearLayout mFloatLayout;//定义悬浮窗口布局 
    private WindowManager.LayoutParams wmParams;
    private WindowManager mWindowManager;//创建悬浮窗口设置布局参数的对象
    private boolean firstNew = false;//判断弹窗是否已经显示

    @Override
    public void onReceive(Context context, Intent intent) {
        this.context = context;
        if (intent.getAction().equals(Intent.ACTION_NEW_OUTGOING_CALL)) {
            //监听拨打电话
        } else {
            //监听接听电话
            TelephonyManager phoneManager = (TelephonyManager) context.getSystemService(Service.TELEPHONY_SERVICE);
            phoneManager.listen(listener, PhoneStateListener.LISTEN_CALL_STATE);
        }
    }

    PhoneStateListener listener = new PhoneStateListener() {
        public void onCallStateChanged(int state, final String incomingNumber) {
            super.onCallStateChanged(state, incomingNumber);
            switch (state) {
                case TelephonyManager.CALL_STATE_IDLE://电话挂断状态
                    Intent i = new Intent();
                    i.setAction("cn.net.xinyi.xmjt.ACTION_END_CALL");//发送电话处于挂断状态的广播
                    context.sendBroadcast(i);
                    popPhoneRemove();//关闭来电悬浮窗界面
                    break;
                case TelephonyManager.CALL_STATE_OFFHOOK://电话接听状态
                    break;
                case TelephonyManager.CALL_STATE_RINGING://电话铃响状态
                    telRinging(incomingNumber);//打开来电悬浮窗界面,传递来电号码
                    break;
                default:
                    break;
            }
        }

        private void telRinging(final String number) {
            if (!firstNew) {
                //第一次启动悬浮窗，延迟两秒后显示界面,并且把firstNew设置为true
                firstNew = true;
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        popPhone(number);
                    }
                }, 1000);
            } else {
                //如果悬浮窗已经显示
            }
        }

        private void popPhone(String phone) {
            LayoutInflater inflater = LayoutInflater.from(context);
            mFloatLayout = (LinearLayout) inflater.inflate(R.layout.phone_pop, null);//获取浮动窗口视图所在布局 
            tvPolContactName = (TextView) mFloatLayout.findViewById(R.id.tv_pol_contact_name);
            tvSysContactName = (TextView) mFloatLayout.findViewById(R.id.tv_sys_contact_name);
            txNumber = (TextView) mFloatLayout.findViewById(R.id.tv_number);
            txNumber.setText("来电号码："+phone);

            ArrayList<String> sysContacts = ContactsHelper.getSysContactsByNumber(context.getApplicationContext(), phone);
            ArrayList<String> polContacts = ContactsHelper.getPoliceContactsByNumber(context.getApplicationContext(),phone);

            if(sysContacts.size()==0){
                tvSysContactName.setVisibility(View.GONE);
            }else{
                tvSysContactName.setVisibility(View.VISIBLE);
                String strSysContacts = StringUtils.arrayToString(sysContacts);
                tvSysContactName.setText("电话本联系人："+strSysContacts);
            }

            if(polContacts.size()==0){
                tvPolContactName.setVisibility(View.GONE);
            }else{
                tvPolContactName.setVisibility(View.VISIBLE);
                String strPolContacts = StringUtils.arrayToString(polContacts);
                tvPolContactName.setText("民警通联系人："+ strPolContacts);
            }

            wmParams = new WindowManager.LayoutParams();
            wmParams.type = WindowManager.LayoutParams.TYPE_SYSTEM_ERROR;//定义WindowManager.LayoutParams类型,TYPE_SYSTEM_ERROR为系统内部错误提示，显示于所有内容之上
            mWindowManager = (WindowManager) context.getApplicationContext().getSystemService(Context.WINDOW_SERVICE);//系统服务，注意这里必须加getApplicationContext(),否则无法把悬浮窗显示在最上层
            wmParams.flags = WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE; //不能抢占聚焦点 
            wmParams.flags = wmParams.flags | WindowManager.LayoutParams.FLAG_WATCH_OUTSIDE_TOUCH;
            wmParams.flags = wmParams.flags | WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS; //排版不受限制 
            wmParams.width = WindowManager.LayoutParams.MATCH_PARENT;
            wmParams.height = getWindowHeight() / 2;//屏幕的一半高度
            wmParams.gravity = Gravity.TOP;//居上显示
            mWindowManager.addView(mFloatLayout, wmParams); //创建View
        }

        private void popPhoneRemove() {
            if (mFloatLayout != null) {
                mWindowManager.removeView(mFloatLayout);
                firstNew = false;
            }
            mFloatLayout = null;//必须加入此语句，否则会windowManager会找不到view
        }

        private int getWindowHeight() {
            WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);//系统服务
            DisplayMetrics metric = new DisplayMetrics();
            wm.getDefaultDisplay().getMetrics(metric);
            return metric.heightPixels;// 屏幕高度（像素）
        }
    };

    /**
     * 接听电话
     * @return
     */
    private void answerCall(){
        TelephonyManager telManager = (TelephonyManager)context.getSystemService(Context.TELEPHONY_SERVICE);
        Class<TelephonyManager> c = TelephonyManager.class;
        try {
            Method getTelMethod = c.getDeclaredMethod("getITelephony", (Class[])null);
            getTelMethod.setAccessible(true);
            ITelephony iTelephony = null;
            iTelephony = (ITelephony)getTelMethod.invoke(telManager, (Object[])null);
            iTelephony.answerRingingCall();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 挂断电话
     */
    public void endCall() {
        TelephonyManager mTelMgr = (TelephonyManager)context.getSystemService(Service.TELEPHONY_SERVICE);
        Class<TelephonyManager> c = TelephonyManager.class;
        try {
            Method getITelephonyMethod = c.getDeclaredMethod("getITelephony", (Class[])null);
            getITelephonyMethod.setAccessible(true);
            ITelephony iTelephony = null;
            iTelephony = (ITelephony)getITelephonyMethod.invoke(mTelMgr, (Object[])null);
            iTelephony.endCall();
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("Fail to answer ring call.");
        }
    }
}