package cn.net.xinyi.xmjt.activity.Main.Fragment;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.sdk.android.feedback.impl.FeedbackAPI;
import com.alibaba.sdk.android.feedback.util.IUnreadCountCallback;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.nostra13.universalimageloader.core.ImageLoader;

import org.apache.http.Header;

import java.io.File;
import java.text.MessageFormat;
import java.util.List;

import cn.net.xinyi.xmjt.R;
import cn.net.xinyi.xmjt.activity.Main.LoginActivity;
import cn.net.xinyi.xmjt.activity.Main.Setting.AboutActivity;
import cn.net.xinyi.xmjt.activity.Main.Setting.ModifyForComanyAty;
import cn.net.xinyi.xmjt.activity.Main.Setting.ModifyPasswordActivity;
import cn.net.xinyi.xmjt.activity.Main.Setting.ModifyUserInfoActivity;
import cn.net.xinyi.xmjt.activity.Main.Setting.OfflineMapActivity;
import cn.net.xinyi.xmjt.api.ApiResource;
import cn.net.xinyi.xmjt.config.AppConfig;
import cn.net.xinyi.xmjt.config.AppContext;
import cn.net.xinyi.xmjt.config.UpdateManager;
import cn.net.xinyi.xmjt.model.PoliceContacts;
import cn.net.xinyi.xmjt.service.BaiduTraceService;
import cn.net.xinyi.xmjt.utils.BaiduTraceFacade;
import cn.net.xinyi.xmjt.utils.BaseDataUtils;
import cn.net.xinyi.xmjt.utils.BaseUtil;
import cn.net.xinyi.xmjt.utils.ContactsHelper;
import cn.net.xinyi.xmjt.utils.DB.BlackPlateDBHelper;
import cn.net.xinyi.xmjt.utils.DialogHelper;
import cn.net.xinyi.xmjt.utils.SharedPreferencesUtil;

/**
 * Created by zhiren.zhang on 2016/8/19.
 */
public class SettingFragment extends BaseNewFragment implements View.OnClickListener {
    private static final String TAG = "SettingFragment";


    private ProgressDialog mProgressDialog = null;
    private List<PoliceContacts> allContacts;
    private TextView clearcatch;
    private TextView contactsdb;
    private TextView blackcardb;
    private LinearLayout loginout;
    private LinearLayout about;
    private LinearLayout update;
    private TextView tv_update;
    private LinearLayout offlinemap;
    private LinearLayout feedback;
    private LinearLayout modifypassword;
    private LinearLayout personInfo;
    private TextView tv_sf;
    private TextView tv_dw;
    private TextView tv_yjfk;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = View.inflate(activity, R.layout.config_actitiy, null);
        //AnnotateManager.initBindView(activity,view);
        initView(view);
        initData();
        initListener();
        return view;
    }

    private void initListener() {
        clearcatch.setOnClickListener(this);
        contactsdb.setOnClickListener(this);
        blackcardb.setOnClickListener(this);
        loginout.setOnClickListener(this);
        about.setOnClickListener(this);
        update.setOnClickListener(this);
        offlinemap.setOnClickListener(this);
        feedback.setOnClickListener(this);
        modifypassword.setOnClickListener(this);
        personInfo.setOnClickListener(this);
    }

    private void initView(View view) {
        clearcatch = (TextView) view.findViewById(R.id.clear_catch);//清除缓存
        contactsdb = (TextView) view.findViewById(R.id.contactsdb);//更新通讯录
        blackcardb = (TextView) view.findViewById(R.id.blackcardb);//更新黑名单
        loginout = (LinearLayout) view.findViewById(R.id.login_out);//注销
        about = (LinearLayout) view.findViewById(R.id.about);//关于
        update = (LinearLayout) view.findViewById(R.id.update);//版本更新
        tv_update = (TextView) view.findViewById(R.id.tv_update);//当前版本
        offlinemap = (LinearLayout) view.findViewById(R.id.offlinemap);//离线地图
        feedback = (LinearLayout) view.findViewById(R.id.feedback);//意见反馈
        modifypassword = (LinearLayout) view.findViewById(R.id.modify_password);//修改密码
        personInfo = (LinearLayout) view.findViewById(R.id.personInfo);//人员信息
        tv_sf= (TextView) view.findViewById(R.id.tv_sf);
        tv_dw = (TextView) view.findViewById(R.id.tv_dw);
        tv_yjfk = (TextView) view.findViewById(R.id.tv_yjfk);//意见反馈
        FeedbackAPI.getFeedbackUnreadCount(new IUnreadCountCallback() {
            @Override
            public void onSuccess(int i) {
                tv_yjfk.append("（未读消息"+i+"条）");
            }

            @Override
            public void onError(int i, String s) {

            }
        });
    }



    private void initData() {
        tv_dw.setText(userInfo.getName());
        tv_sf.setText(userInfo.getAccounttype() + "--" + userInfo.getUsername());

        String currentAppVersion = "1.0";
        try {
            currentAppVersion = activity.getPackageManager().getPackageInfo(activity.getPackageName(), 0).versionName;
            tv_update.setText("APP更新" + "(当前版本：" + currentAppVersion + ")");
        } catch (Exception e) {
            e.printStackTrace();
        }
        int networkType = ((AppContext) activity.getApplication()).getNetworkType();
        if (networkType == 0) {
            BaseUtil.showDialog(getResources().getString(R.string.msg_check_version_no_network), activity);
        }

        if (AppContext.instance.getProperty(AppConfig.CONTACTS_UPDATE_TIME) != null) {
            String s = AppContext.instance.getProperty(AppConfig.CONTACTS_UPDATE_TIME);
            contactsdb.setText("通讯录更新" + "(本地版本：" + s + ")");
        } else {
            contactsdb.setText("通讯录更新" + "(本机没有缓存的通讯录)");
        }


        if (AppContext.instance.getProperty(AppConfig.BLACKPLATE_UPDATE_TIME) != null) {
            String s = AppContext.instance.getProperty(AppConfig.BLACKPLATE_UPDATE_TIME);
            int count = new BlackPlateDBHelper(activity).getBlackListCount();
            blackcardb.setText("车牌比对库更新" + "(本地版本：" + s + "，共计" + count + "个车牌样本)");
        } else {
            blackcardb.setText("车牌比对库更新" + "(本机没有缓存的车牌比对库)");
        }
    }

    @Override
    public void onClick(View v) {
        int networkType = ((AppContext) activity.getApplication()).getNetworkType();
        if (networkType == 0) {
            BaseUtil.showDialog(getResources().getString(R.string.msg_check_version_no_network), activity);
        }else {
            switch (v.getId()) {
                case R.id.clear_catch:
                    Message msg = new Message();
                    msg.what = 13;
                    mHandle.sendMessage(msg);
                    break;

                case R.id.modify_password:
                    showActivity(ModifyPasswordActivity.class);
                    break;

                case R.id.login_out:
                    login_out();
                    break;

                case R.id.personInfo:
                    if (BaseDataUtils.isCompanyOrOther()==1){
                        showActivity(ModifyForComanyAty.class);
                    }else {
                        showActivity(ModifyUserInfoActivity.class);
                    }
                    //   Intent intent = new Intent(activity, UserInfoActivity.class);
                    //   startActivity(intent);
                    break;

                case R.id.offlinemap:
                    if (networkType != 1) {
                        BaseUtil.showDialog(getResources().getString(R.string.msg_check_version_wifi_only), activity);
                        break;
                    }
                    showActivity(OfflineMapActivity.class);
                    break;

                case R.id.feedback:
                    if (!((AppContext) activity.getApplication()).isNetworkConnected()) {
                        BaseUtil.showDialog(getResources().getString(R.string.msg_offline_feedback), activity);
                    } else {
                        FeedbackAPI.openFeedbackActivity();
                    }
                    break;

                case R.id.blackcardb:
                    if (networkType != 1) {
                        BaseUtil.showDialog(getResources().getString(R.string.msg_check_version_wifi_only), activity);
                        break;
                    }
                    getPlateBlackDb();
                    break;

                case R.id.contactsdb:
                    if (networkType != 1) {
                        BaseUtil.showDialog(getResources().getString(R.string.msg_check_version_wifi_only), activity);
                        break;
                    }
                    getRemoteContacts();
                    break;

                case R.id.update:
                    if (BaseDataUtils.isFastClick()){
                        break;
                    }
                    //通过更新服务
                    new UpdateManager().checkUpdate(activity);
                    break;

                case R.id.about:
                    Intent intent7 = new Intent(activity, AboutActivity.class);
                    activity.startActivity(intent7);
                    break;


            }
        }
    }

    //注销账号
    private void login_out() {
        DialogHelper.showAlertDialog("将要退出当前账号，确定注销账号吗？", activity, new DialogHelper.OnOptionClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which, Object o) {
                SharedPreferencesUtil.putBoolean(getActivity(),"iscolltips",false);
                AppContext.instance().cleanLoginInfo();
                BaiduTraceFacade.stop();
                activity.stopService(new Intent(activity, BaiduTraceService.class));
                Intent intent = new Intent(activity, LoginActivity.class);
                startActivity(intent);
                activity.finish();
            }
        });

    }


    private Handler mHandle = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 0:
                    mProgressDialog = new ProgressDialog(activity);
                    mProgressDialog.setMessage("正在联网同步更新通讯录信息");
                    mProgressDialog.setIndeterminate(true);
                    mProgressDialog.setCancelable(false);
                    mProgressDialog.show();
                    break;
                case 1:
                    if (mProgressDialog != null && mProgressDialog.isShowing())
                        mProgressDialog.cancel();
                    BaseUtil.showDialog("同步更新通讯录成功", activity);

                    break;
                case 2:
                    if (mProgressDialog != null && mProgressDialog.isShowing())
                        mProgressDialog.cancel();
                    BaseUtil.showDialog("同步更新通讯录失败", activity);
                    break;
                case 3:
                    mProgressDialog = new ProgressDialog(activity);
                    mProgressDialog.setMessage("正在更新本地电话本，该操作需要较长时间，请保持网络畅通耐心等候");
                    mProgressDialog.setIndeterminate(true);
                    mProgressDialog.setCancelable(false);
                    mProgressDialog.show();
                    break;
                case 4:
                    if (mProgressDialog != null && mProgressDialog.isShowing())
                        mProgressDialog.cancel();
                    BaseUtil.showDialog("本地电话本更新成功", activity);
                    break;
                case 5:
                    mProgressDialog = new ProgressDialog(activity);
                    mProgressDialog.setMessage("正在更新车牌比对库，该操作需要较长时间，请保持网络畅通耐心等候");
                    mProgressDialog.setIndeterminate(true);
                    mProgressDialog.setCancelable(false);
                    mProgressDialog.show();
                    break;
                case 6:
                    if (mProgressDialog != null && mProgressDialog.isShowing())
                        mProgressDialog.cancel();
                    int count = msg.arg1;
                    if (count == 0) {
                        BaseUtil.showDialog("车牌比对库已经是最新版本，无需更新。", activity);
                    } else {
                        BaseUtil.showDialog(
                                MessageFormat.format("车牌比对库更新成功，本次更新了{0}条比对车牌样本。", count)
                                , activity);
                    }
                    break;
                case 7:
                    if (mProgressDialog != null && mProgressDialog.isShowing())
                        mProgressDialog.cancel();
                    BaseUtil.showDialog("同步更新车牌比对库失败", activity);
                    break;

                case 13:
                    mProgressDialog = new ProgressDialog(activity);
                    mProgressDialog.setMessage("缓存正在清理中...");
                    mProgressDialog.setIndeterminate(true);
                    mProgressDialog.setCancelable(false);
                    mProgressDialog.show();
                    cleanApplicationData(activity);
                    break;

                default:
                    break;
            }
        }
    };





    //初始化或增量更新车牌比对库
    private void getPlateBlackDb() {

        Message msg = new Message();
        msg.what = 5;
        mHandle.sendMessage(msg);

        //初始化内置的车牌比对库
        String dbFileName = BlackPlateDBHelper.getDatabaseFile();
        File dbFile = new File(dbFileName);
        if (!dbFile.exists()) {
            BlackPlateDBHelper.initDatabase(activity);
        }

        //更新车牌比对库版本；
        String lastUpateTime = AppContext.instance().getProperty(AppConfig.BLACKPLATE_UPDATE_TIME);
        int count = new BlackPlateDBHelper(activity).getBlackListCount();
        blackcardb.setText("车牌比对库更新" + "(本地版本：" + lastUpateTime + "，共计" + count + "个车牌样本)");

        //联网获取增量更新
        ApiResource.getPlateBlack(lastUpateTime, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int i, Header[] headers, byte[] bytes) {
                String json = new String(bytes);
                JSONArray jsonArray = JSON.parseArray(json);

                Message msg = new Message();
                msg.what = 6;

                if (jsonArray.size() > 0) {
                    BlackPlateDBHelper dbHelper = new BlackPlateDBHelper(activity);
                    int count = dbHelper.insertBlackPlate(jsonArray);
                    int blackListCount = dbHelper.getBlackListCount();

                    //更新车牌比对库版本；
                    String newUpdateTime = ((JSONObject) jsonArray.get(jsonArray.size() - 1)).getString("writetime");
                    AppContext.instance().setProperty(AppConfig.BLACKPLATE_UPDATE_TIME, newUpdateTime);
                    blackcardb.setText("车牌比对库更新" + "(本地版本：" + newUpdateTime + "，共计" + blackListCount + "个车牌样本)");

                    msg.arg1 = count;
                } else {
                    msg.arg1 = 0;
                }
                mHandle.sendMessage(msg);
            }

            @Override
            public void onFailure(int i, Header[] headers, byte[] bytes, Throwable throwable) {
                Message msg = new Message();
                msg.what = 7;
                mHandle.sendMessage(msg);
            }
        });

    }

    private void getRemoteContacts() {
        Message msg = new Message();
        msg.what = 0;
        mHandle.sendMessage(msg);
        ApiResource.getPoliceContacts(new AsyncHttpResponseHandler() {
            @Override

            public void onSuccess(int i, Header[] headers, byte[] bytes) {
                String result = new String(bytes);
                if (result != null) {
                    try {
                        allContacts = JSON.parseArray(result, PoliceContacts.class);

//                        //更新本地缓存的通讯录；
//                        updateLocalContacts();
                        //更新本地缓存的通讯录；
                        ContactsHelper.updateLocalContacts(activity, allContacts);

                        Message msg = new Message();
                        msg.what = 1;
                        mHandle.handleMessage(msg);
                    } catch (Exception e) {
                        onFailure(i, headers, bytes, null);
                    }
                } else {
                    onFailure(i, headers, bytes, null);
                }
            }

            @Override
            public void onFailure(int i, Header[] headers, byte[] bytes, Throwable throwable) {
                Message msg = new Message();
                msg.what = 2;
                mHandle.handleMessage(msg);
            }
        });
    }


    /**
     * 清除本应用内部缓存(/data/data/com.xxx.xxx/cache) * * @param context
     */
    public static void cleanInternalCache(Context context) {
        deleteFilesByDirectory(context.getCacheDir());
    }

    /**
     * 清除/data/data/com.xxx.xxx/files下的内容 * * @param context
     */
    public static void cleanFiles(Context context) {
        deleteFilesByDirectory(context.getFilesDir());
    }

    /**
     * * 清除外部cache下的内容(/mnt/sdcard/android/data/com.xxx.xxx/cache) * * @param
     * context
     */
    public static void cleanExternalCache(Context context) {
        if (Environment.getExternalStorageState().equals(
                Environment.MEDIA_MOUNTED)) {
            deleteFilesByDirectory(context.getExternalCacheDir());
        }
    }

    /**
     * 清除本应用所有的数据 * * @param context * @param filepath
     */
    public void cleanApplicationData(Context context) {
        cleanInternalCache(context);
        cleanExternalCache(context);
        cleanFiles(context);
        ImageLoader imageLoader = ImageLoader.getInstance();
        imageLoader.clearDiskCache();
        imageLoader.clearMemoryCache();
        mProgressDialog.dismiss();
        BaseUtil.showDialog("缓存清理成功", activity);
    }

    /**
     * 删除方法 这里只会删除某个文件夹下的文件，如果传入的directory是个文件，将不做处理 * * @param directory
     */
    private static void deleteFilesByDirectory(File directory) {
        if (directory != null && directory.exists() && directory.isDirectory()) {
            for (File item : directory.listFiles()) {
                item.delete();
            }
        }
    }
}
