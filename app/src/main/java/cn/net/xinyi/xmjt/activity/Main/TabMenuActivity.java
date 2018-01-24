package cn.net.xinyi.xmjt.activity.Main;


import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.alibaba.fastjson.JSONObject;
import com.google.gson.Gson;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.umeng.analytics.AnalyticsConfig;
import com.umeng.analytics.MobclickAgent;
import com.umeng.comm.core.CommunitySDK;
import com.umeng.comm.core.beans.CommUser;
import com.umeng.comm.core.impl.CommunityFactory;
import com.umeng.comm.core.login.LoginListener;

import org.apache.http.Header;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import cn.net.xinyi.xmjt.R;
import cn.net.xinyi.xmjt.activity.Main.Fragment.FeedsFragment;
import cn.net.xinyi.xmjt.activity.Main.Fragment.GzrzFragment;
import cn.net.xinyi.xmjt.activity.Main.Fragment.MainMenuFragment;
import cn.net.xinyi.xmjt.activity.Main.Fragment.RwFragment;
import cn.net.xinyi.xmjt.activity.Main.Fragment.TxlFragment;
import cn.net.xinyi.xmjt.api.ApiAsyncHttpClient;
import cn.net.xinyi.xmjt.api.ApiResource;
import cn.net.xinyi.xmjt.config.AppContext;
import cn.net.xinyi.xmjt.config.AppManager;
import cn.net.xinyi.xmjt.model.DevicesModel;
import cn.net.xinyi.xmjt.model.UnitModel;
import cn.net.xinyi.xmjt.model.UserInfo;
import cn.net.xinyi.xmjt.model.View.TaskListModel;
import cn.net.xinyi.xmjt.service.BaiduTraceService;
import cn.net.xinyi.xmjt.utils.BaseDataUtils;
import cn.net.xinyi.xmjt.utils.DialogHelper;
import cn.net.xinyi.xmjt.utils.SharedPreferencesUtil;
import cn.net.xinyi.xmjt.utils.StringUtils;
import cn.net.xinyi.xmjt.utils.UI;
import cn.net.xinyi.xmjt.utils.XYViewPager;
import cn.net.xinyi.xmjt.utils.XinyiLog;
import cn.net.xinyi.xmjt.utils.zxing.activity.CaptureActivity;
import cn.net.xinyi.xmjt.v527.presentation.home.HomeFragment;


public class TabMenuActivity extends FragmentActivity {
    private XYViewPager vp_main_tab;//这个方法有问题，别用
    private List<Fragment> fragmentList = null;
    private FragmentPagerAdapter mAdapter = null;
    private RadioGroup rg_main_group;
    public UserInfo userInfo;
    public static TabMenuActivity instace;
    Gson gson = new Gson();

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        userInfo = AppContext.instance().getLoginInfo();
        instace = this;
        init();
        initViewPage();
        new Thread(new Runnable() {
            @Override
            public void run() {
                getDevices();
                getTasks();
                getUnit();
            }
        }).start();

        getActionBar().setDisplayShowHomeEnabled(false);
    }

    private void init() {
        try {
            fragmentList = new ArrayList<Fragment>();
            vp_main_tab = (XYViewPager) findViewById(R.id.vp_content);
            rg_main_group = (RadioGroup) findViewById(R.id.tabs);
            fragmentList.add(new MainMenuFragment());// 主界面
            //fragmentList.add(new MainDutyFragment());//勤务
            fragmentList.add(new HomeFragment());// 主界面
            fragmentList.add(new RwFragment());// 任务
            fragmentList.add(new FeedsFragment());//微社区
            fragmentList.add(new GzrzFragment());//工作日志
            fragmentList.add(new TxlFragment());//通讯
            // fragmentList.add(new SettingFragment());//设置
            initUmengCommunity();
            vp_main_tab.setOffscreenPageLimit(3);//缓存多个页面，虽然占用内存，但是防止出现卡顿
            initBase();
            if (Build.VERSION.SDK_INT > 11) {//3.0以上才支持
                vp_main_tab.setPageTransformer(true, new DepthPageTransformer());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void initBase() {
        MobclickAgent.setDebugMode(true);
        //发送策略定义了用户由统计分析SDK产生的数据发送回友盟服务器的频率。
        MobclickAgent.updateOnlineConfig(this);
        /** 设置是否对日志信息进行加密, 默认false(不加密). */
        AnalyticsConfig.enableEncrypt(true);
        initNewVersion();
    }

    private void initNewVersion() {
        getSession();//获取当前登录的session,判断是否其他终端登录
    }

    private void initUmengCommunity() {
        CommunitySDK communitySDK = CommunityFactory.getCommSDK(getApplicationContext());
        CommUser commUser = new CommUser();
        commUser.name = userInfo.getPcs() + userInfo.getName() + userInfo.getUsername();
        commUser.id = userInfo.getUsername();

        communitySDK.loginToUmengServerBySelfAccount(getApplicationContext(), commUser, new LoginListener() {
            @Override
            public void onStart() {
            }

            @Override
            public void onComplete(int i, CommUser commUser) {
                XinyiLog.d(i + "");
            }
        });
    }

    private void initViewPage() {
        FragmentManager fm = getSupportFragmentManager();
        mAdapter = new FragmentPagerAdapter(fm) {

            @Override
            public int getCount() {
                return fragmentList == null ? 0 : fragmentList.size();
            }

            @Override
            public Fragment getItem(int position) {
                return fragmentList.get(position);
            }
        };

        vp_main_tab.setAdapter(mAdapter);

        rg_main_group.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {

            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                int index = 0;
                switch (checkedId) {
                    case R.id.tab01:// 主界面  采集
                        index = 0;
                        break;
                    case R.id.tab02:// 勤务
                        if (BaseDataUtils.isCompanyOrOther() == 1
                                || BaseDataUtils.isCompanyOrOther() == 2
                                || BaseDataUtils.isCompanyOrOther() == 4
                                || userInfo.getIsregester() == 1) {
                            UI.toast(TabMenuActivity.this, "没有权限操作！");
                        } else {
                            index = 1;
                        }
                        break;
                    case R.id.tab03:// 微社区
                        if (BaseDataUtils.isCompanyOrOther() == 1
                                || BaseDataUtils.isCompanyOrOther() == 2
                                || BaseDataUtils.isCompanyOrOther() == 4
                                || userInfo.getIsregester() == 1) {
                            UI.toast(TabMenuActivity.this, "没有权限操作！");
                        } else {
                            index = 2;
                        }
                        break;
                    case R.id.tab04:// 系统设置
                        index = 3;
                        break;
                    case R.id.tab05:// 系统设置
                        index = 4;
                        break;
                    default:
                        break;
                }
                vp_main_tab.setCurrentItem(index);
                initNewVersion();
            }
        });

        //当没有设置权限的时候不允许滑动
        if (BaseDataUtils.isCompanyOrOther() == 1
                || BaseDataUtils.isCompanyOrOther() == 2
                || BaseDataUtils.isCompanyOrOther() == 4
                || userInfo.getIsregester() == 1) {
            vp_main_tab.setNoScroll(true);
        }
        vp_main_tab.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                int index = R.id.tab01;
                switch (position) {
                    case 0:// 主界面  采集
                        index = R.id.tab01;
                        break;
                    case 1:// 勤务
                        if (BaseDataUtils.isCompanyOrOther() == 1
                                || BaseDataUtils.isCompanyOrOther() == 2
                                || BaseDataUtils.isCompanyOrOther() == 4
                                || userInfo.getIsregester() == 1) {
                            UI.toast(TabMenuActivity.this, "没有权限操作！");
                            index = R.id.tab01;
                        } else {
                            index = R.id.tab02;
                        }
                        break;
                    case 2:// 微社区
                        if (BaseDataUtils.isCompanyOrOther() == 1
                                || BaseDataUtils.isCompanyOrOther() == 2
                                || BaseDataUtils.isCompanyOrOther() == 4
                                || userInfo.getIsregester() == 1) {
                            UI.toast(TabMenuActivity.this, "没有权限操作！");
                            index = R.id.tab04;
                        } else {
                            index = R.id.tab03;
                        }
                        break;
                    case 3:// 系统设置
                        index = R.id.tab04;
                        break;
                    case 4:// 系统设置
                        index = R.id.tab05;
                        break;
                }
                rg_main_group.check(index);
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }


    //滑动动画，解决太生硬
    class DepthPageTransformer implements ViewPager.PageTransformer {
        private static final float MIN_SCALE = 0.75f;

        public void transformPage(View view, float position) {
            int pageWidth = view.getWidth();
            if (position < -1) { // [-Infinity,-1)
                // This page is way off-screen to the left.
                view.setAlpha(0);
            } else if (position <= 0) { // [-1,0]
                // Use the default slide transition when moving to the left page
                view.setAlpha(1);
                view.setTranslationX(0);
                view.setScaleX(1);
                view.setScaleY(1);
            } else if (position <= 1) { // (0,1]
                // Fade the page out.
                view.setAlpha(1 - position);
                // Counteract the default slide transition
                view.setTranslationX(pageWidth * -position);

                // Scale the page down (between MIN_SCALE and 1)
                float scaleFactor = MIN_SCALE
                        + (1 - MIN_SCALE) * (1 - Math.abs(position));
                view.setScaleX(scaleFactor);
                view.setScaleY(scaleFactor);

            } else { // (1,+Infinity]
                view.setAlpha(0);
            }
        }
    }

    /**
     * USERNAME 获取当前用户的最新session
     * LOGIN_TYPE  类型为手机
     **/
    private void getSession() {
        if (null != AppContext.instance.getLoginInfo().getUsername() && null != ApiAsyncHttpClient.getCookie(AppContext.instance())) {
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("USERNAME", AppContext.instance.getLoginInfo().getUsername());
            jsonObject.put("LOGIN_TYPE", "PHONE");
            String json = jsonObject.toJSONString();
            ApiResource.getSessionKey(json, new AsyncHttpResponseHandler() {
                @Override
                public void onSuccess(int i, Header[] headers, byte[] bytes) {
                    String result = subMessage(new String(bytes));
                    String cookie = ApiAsyncHttpClient.getCookie(AppContext.instance());
                    if (i == 200 && result.length() > 4 && cookie != null && !cookie.substring(cookie.lastIndexOf("=") + 1, cookie.length() - 1).equals(result)) {
                        DialogHelper.showAlertDialog(TabMenuActivity.this, "您的账号当前在其他手机登录，如非本人操作，则密码可能泄露，请至登录界面点击【忘记密码】进行修改！", new DialogHelper.OnOptionClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which, Object o) {
                                lodingToLogin();
                            }
                        });
                    }
                }

                @Override
                public void onFailure(int i, Header[] headers, byte[] bytes, Throwable throwable) {
                }
            });
        }
    }

    // string 返回时 是2个“”，需要进行截取
    private String subMessage(String text) {
        return text.length() > 3 ? text.substring(1, text.length() - 1) : text;
    }

    // 转到登录
    private void lodingToLogin() {
        Intent intent = new Intent(this, LoginActivity.class);
        startActivity(intent);
        this.finish();
        AppManager.getAppManager().finishAllActivity(LoginActivity.class);
    }

    //共计三步，统计fragment第二步，
    // 下一步在BaseNewFragment.java：因为本app包含fragment，要统计fragment，所以需要加这一句话  add20160819
    public void onResume() {
        super.onResume();
        MobclickAgent.onResume(this);       //统计时长
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_tab_menu_act, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_scan://二维码扫描
                Intent intent = new Intent(this, CaptureActivity.class);
                intent.putExtra("from", "TabMenuActivity");
                startActivityForResult(intent, 100);
                break;
        }
        return true;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (resultCode) {
            case RESULT_OK:
                Bundle bundle = data.getExtras();
                final String code = bundle.getString("result");
                if (code != null) {
                    doConfirmAndLogin(code);
                } else {
                    Toast.makeText(this, R.string.msg_scan_fail, Toast.LENGTH_LONG).show();
                }
                break;
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    private void doConfirmAndLogin(final String code) {
        if (code.startsWith("xyjwt-")) {
            DialogHelper.showAlertDialog(getString(R.string.msg_scan_login_title), this, new DialogHelper.OnOptionClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which, Object o) {
                    {
                        if (null != userInfo.getUsername()) {
                            JSONObject jsonObject = new JSONObject();
                            jsonObject.put("USERNAME", userInfo.getUsername());
                            jsonObject.put("LOGIN_TYPE", "PHONE");
                            jsonObject.put("UUID", code);
                            jsonObject.put("DEVICEID", ((AppContext) getApplication()).getDeviceId());
                            String json = jsonObject.toJSONString();
                            ApiResource.winLogin(json, new AsyncHttpResponseHandler() {
                                @Override
                                public void onSuccess(int i, Header[] headers, byte[] bytes) {
                                    XinyiLog.e("TEST", new String(bytes));
                                    if (new String(bytes).contains("SUCCESS")) {
                                        UI.toast(TabMenuActivity.this, getString(R.string.msg_scan_login_success));
                                    } else {
                                        UI.toast(TabMenuActivity.this, getString(R.string.msg_scan_login_fail));
                                    }
                                }

                                @Override
                                public void onFailure(int i, Header[] headers, byte[] bytes, Throwable throwable) {
                                    UI.toast(TabMenuActivity.this, getString(R.string.msg_scan_login_fail));
                                }
                            });
                        }
                    }
                }
            });
        } else {
            UI.toast(TabMenuActivity.this, getString(R.string.msg_scan_error));
        }
    }

    public void onPause() {
        super.onPause();
        MobclickAgent.onPause(this);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        onDoestroy1();
    }

    private void onDoestroy1() {
        AppManager.getAppManager().finishActivity(this);
    }

    private long exitTime = 0;

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK && event.getAction() == KeyEvent.ACTION_DOWN) {
            if ((System.currentTimeMillis() - exitTime) > 2000) {
                Toast.makeText(getApplicationContext(), "再按一次退出程序", Toast.LENGTH_SHORT).show();
                exitTime = System.currentTimeMillis();
            } else {
                this.stopService(new Intent(TabMenuActivity.this, BaiduTraceService.class));
                finish();
                System.exit(0);
            }
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }


    /**
     *
     */
    public void getDevices() {
        HttpClient httpCient = new DefaultHttpClient();
        HttpPost httpPost = new HttpPost("http://219.134.134.156:8088/xsmws-web/api/v1.0/yajie");
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("command", "getDeviceList");
        jsonObject.put("apikey", "AEGCIOKMteD8Iu6G");
        StringEntity postingString = null;// json传递
        try {
            postingString = new StringEntity(jsonObject.toJSONString());
            httpPost.setEntity(postingString);
            httpPost.setHeader("Content-type", "application/json");
            HttpResponse response = httpCient.execute(httpPost);
            int statusCode = response.getStatusLine().getStatusCode();
            if (statusCode == 200) {
                String content = EntityUtils.toString(response.getEntity());
                DevicesModel d = gson.fromJson(content, DevicesModel.class);
                if (d.getError().equals("0")) {
                    SharedPreferencesUtil.putString(this, "getDeviceList", content);
                    Map<String, DevicesModel.DataBean> map = AppContext.getDevicesMap();
                    for (DevicesModel.DataBean db : d.getData()
                            ) {
                        map.put("" + db.getId(), db);
                    }

                }
            } else {
                String content = SharedPreferencesUtil.getString(this, "getDeviceList", "");
                if (!StringUtils.isEmpty(content)) {
                    DevicesModel d = gson.fromJson(content, DevicesModel.class);
                    Map<String, DevicesModel.DataBean> map = AppContext.getDevicesMap();
                    for (DevicesModel.DataBean db : d.getData()
                            ) {
                        map.put("" + db.getId(), db);
                    }
                } else {
                    Toast.makeText(instace, "设备获取失败", Toast.LENGTH_SHORT).show();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 获取所有的 任务，，初始值为null
     */
    public void getTasks() {
        HttpClient httpCient = new DefaultHttpClient();
        HttpPost httpPost = new HttpPost("http://219.134.134.156:8088/xsmws-web/api/v1.0/yajie");
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("command", "getTaskList");
        jsonObject.put("apikey", "AEGCIOKMteD8Iu6G");
        StringEntity postingString = null;// json传递
        try {
            postingString = new StringEntity(jsonObject.toJSONString());
            httpPost.setEntity(postingString);
            httpPost.setHeader("Content-type", "application/json");
            HttpResponse response = httpCient.execute(httpPost);
            int statusCode = response.getStatusLine().getStatusCode();
            if (statusCode == 200) {
                String content = EntityUtils.toString(response.getEntity());
                TaskListModel taskListModel = gson.fromJson(content, TaskListModel.class);
                if (taskListModel.getError().equals("0")) {
                    Map<String, TaskListModel.DataBean> map = AppContext.getTaskMap();
                    for (TaskListModel.DataBean db : taskListModel.getData()
                            ) {
                        map.put("" + db.getId(), db);
                    }
                    SharedPreferencesUtil.putString(this, "getTaskList", content);
                }
            } else {
                String content = SharedPreferencesUtil.getString(this, "getTaskList", "");
                if (!StringUtils.isEmpty(content)) {
                    TaskListModel d = gson.fromJson(content, TaskListModel.class);
                    Map<String, TaskListModel.DataBean> map = AppContext.getTaskMap();
                    for (TaskListModel.DataBean db : d.getData()
                            ) {
                        map.put("" + db.getId(), db);
                    }
                } else {
                    Toast.makeText(instace, "任务获取失败", Toast.LENGTH_SHORT).show();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * l
     */
    public void getUnit() {
        HttpClient httpCient = new DefaultHttpClient();
        HttpPost httpPost = new HttpPost("http://219.134.134.156:8088/xsmws-web/api/v1.0/yajie");
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("command", "getUnitList");
        jsonObject.put("apikey", "AEGCIOKMteD8Iu6G");
        StringEntity postingString = null;// json传递
        try {
            postingString = new StringEntity(jsonObject.toJSONString());
            httpPost.setEntity(postingString);
            httpPost.setHeader("Content-type", "application/json");
            HttpResponse response = httpCient.execute(httpPost);
            int statusCode = response.getStatusLine().getStatusCode();
            if (statusCode == 200) {
                String content = EntityUtils.toString(response.getEntity());
                UnitModel unitModel = gson.fromJson(content, UnitModel.class);
                if (unitModel.getError().equals("0")) {
                    SharedPreferencesUtil.putString(this, "getUnitList", content);
                    Map<String, UnitModel.DataBean> map = AppContext.getUnitModelMap();
                    for (UnitModel.DataBean db : unitModel.getData()
                            ) {
                        map.put("" + db.getId(), db);
                    }
                }
            } else {
                String content = SharedPreferencesUtil.getString(this, "getUnitList", "");
                if (!StringUtils.isEmpty(content)) {
                    UnitModel d = gson.fromJson(content, UnitModel.class);
                    Map<String, UnitModel.DataBean> map = AppContext.getUnitModelMap();
                    for (UnitModel.DataBean db : d.getData()
                            ) {
                        map.put("" + db.getId(), db);
                    }
                } else {
                    Toast.makeText(instace, "任务单位失败", Toast.LENGTH_SHORT).show();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
