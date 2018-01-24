package cn.net.xinyi.xmjt.utils;

import android.util.Log;


/**
 * Created by studyjun on 2016/5/25.
 */
public class XinyiLog {
    public static final String TAG="XinyiLog";

    public static void d(String tag,String msg){
        if (BuildConfig.ISDEBUG){
            Log.d(tag,msg);
        }
    }

    public static void i(String tag,String msg){
        if (BuildConfig.ISDEBUG){
            Log.i(tag,msg);
        }
    }


    public static void e(String tag,String msg){
        if (BuildConfig.ISDEBUG){
            Log.e(tag,msg);
        }
    }


    public static void v(String tag,String msg){
        if (BuildConfig.ISDEBUG){
            Log.v(tag,msg);
        }
    }


    /**
     * log with tag XinyiLog
     * @param msg
     */
    public static void d(String msg){
        if (BuildConfig.ISDEBUG&&msg!=null&&!msg.isEmpty()){
            Log.d(TAG,msg);
        }
    }

    /**
     * log with tag XinyiLog
     * @param msg
     */
    public static void i(String msg){
        if (BuildConfig.ISDEBUG){
            Log.d(TAG,msg);
        }
    }

    /**
     * log with tag XinyiLog
     * @param msg
     */
    public static void e(String msg){
        if (BuildConfig.ISDEBUG){
            Log.d(TAG,msg);
        }
    }

    /**
     * log with tag XinyiLog
     * @param msg
     */
    public static void v(String msg){
        if (BuildConfig.ISDEBUG){
            Log.d(TAG,msg);
        }
    }


}
