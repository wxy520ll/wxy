package cn.net.xinyi.xmjt.config;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.graphics.Bitmap;
import android.hardware.Camera;
import android.location.LocationManager;
import android.media.AudioManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Environment;
import android.telephony.TelephonyManager;

import com.alibaba.fastjson.util.TypeUtils;
import com.alibaba.sdk.android.push.CloudPushService;
import com.alibaba.sdk.android.push.CommonCallback;
import com.alibaba.sdk.android.push.noonesdk.PushServiceFactory;
import com.alibaba.sdk.android.push.register.HuaWeiRegister;
import com.baidu.location.LocationClient;
import com.baidu.mapapi.SDKInitializer;
import com.baidu.mapapi.map.Marker;
import com.dashi.fracesuit.bugly.BuglyApplication;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.PersistentCookieStore;
import com.loopj.android.http.SyncHttpClient;
import com.nostra13.universalimageloader.cache.disc.impl.UnlimitedDiskCache;
import com.nostra13.universalimageloader.cache.disc.naming.Md5FileNameGenerator;
import com.nostra13.universalimageloader.cache.memory.impl.WeakMemoryCache;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;
import com.nostra13.universalimageloader.core.assist.QueueProcessingType;
import com.nostra13.universalimageloader.utils.StorageUtils;
import com.tencent.bugly.beta.Beta;
import com.xinyi.FeedbackApplication;
import com.xinyi_tech.comm.BaseApplication;
import com.xinyi_tech.comm.net.retrofit2.config.RetrofitManager;
import com.xinyi_tech.comm.util.FileUtils2;
import com.xinyi_tech.comm.util.ImageLoaderUtils;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Properties;
import java.util.UUID;

import cn.net.xinyi.xmjt.BuildConfig;
import cn.net.xinyi.xmjt.R;
import cn.net.xinyi.xmjt.activity.Main.LoginActivity;
import cn.net.xinyi.xmjt.api.ApiAsyncHttpClient;
import cn.net.xinyi.xmjt.api.ApiSyncHttpClient;
import cn.net.xinyi.xmjt.model.DevicesModel;
import cn.net.xinyi.xmjt.model.DevicesPositionModel;
import cn.net.xinyi.xmjt.model.UnitModel;
import cn.net.xinyi.xmjt.model.UserInfo;
import cn.net.xinyi.xmjt.model.View.TaskListModel;
import cn.net.xinyi.xmjt.utils.DB.ZDXXUtils;
import cn.net.xinyi.xmjt.utils.StringUtils;
import cn.net.xinyi.xmjt.v527.data.remote.retrofit.CacheProvider;
import cn.net.xinyi.xmjt.v527.data.remote.retrofit.XinyiApi;
import cn.net.xinyi.xmjt.v527.help.okhttp.Okhttp3Help;
import io.rx_cache.internal.RxCache;

public class AppContext extends BaseApplication {
    public static long lastTime = 0l;
    public static final int NETTYPE_WIFI = 0x01;
    public static final int NETTYPE_CMWAP = 0x02;
    public static final int NETTYPE_CMNET = 0x03;
    public static final String APP_ID = "xmjt";
    public static AppContext instance;
    private ZDXXUtils zdUtils;
    private boolean login = false;    //登录状态
    private int loginUid = 0;    //登录用户的id

    // 全局相机
    public static Camera mCamera;
    // 百度定位；
    public LocationClient mLocationClient;
    //最后一次定位的数据
    public static long lastLocationTimeZone = 0;
    public static String lastLocationLatitude = "0.00000";
    public static String lastLocationLongitude = "0.00000";
    public static String lastLocationAddress = "";
    public static String lastLocationType = "";
    private boolean isCashError = true;
    //定位时间间隔
    public final static int LOCATION_SPAN = 60000;
    public static boolean Score = false;
    //采集专用时间间隔
    public final static int LOCATION_SPAN_CJ = 30000;

    private final static String FEEDKEY = "23554426";
    private final static String BUGLYKEY = "928c0a33e1";

    public static XinyiApi apiService;
    public static CacheProvider cacheProviders;
    private static List<DevicesPositionModel> deviceListPostion = new ArrayList<>();//全局保存 推送过来的设备位置信息
    private static List<Marker> overlays = new ArrayList<>();//保存所有的 覆盖物

    private static HashMap<String, DevicesModel.DataBean> devicesMap = new HashMap<>();
    private static HashMap<String, TaskListModel.DataBean> taskMap = new HashMap<>();
    private static HashMap<String, UnitModel.DataBean> unitModelMap = new HashMap<>();

    public AppContext() {
        super(BuildConfig.ISDEBUG);
    }


    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        // 安装tinker
        Beta.installTinker();
    }


    @Override
    public void onCreate() {
        super.onCreate();
        instance = this;
        initImageLoader(this);
        // 百度MAP,在使用 SDK 各组间之前初始化 context 信息，传入 ApplicationContext
        SDKInitializer.initialize(this);
        mLocationClient = new LocationClient(this.getApplicationContext());
        //初始化 阿里百川 feedback
        FeedbackApplication.init(this, FEEDKEY);
        //初始化网络通讯库
        PersistentCookieStore myCookieStore = new PersistentCookieStore(this);
        //异步http客户端
        AsyncHttpClient asyncHttpClient = new AsyncHttpClient();
        asyncHttpClient.setCookieStore(myCookieStore);
        ApiAsyncHttpClient.setHttpClient(asyncHttpClient);
        ApiAsyncHttpClient.setCookie(ApiAsyncHttpClient.getCookie(this));
        //同步http客户端
        SyncHttpClient syncHttpClient = new SyncHttpClient();
        syncHttpClient.setCookieStore(myCookieStore);
        ApiSyncHttpClient.setHttpClient(syncHttpClient);
        ApiSyncHttpClient.setCookie(ApiSyncHttpClient.getCookie(this));
        //初始化ImageLoad

        initCloudChannel(this);
        //自动更新
        BuglyApplication.init(this, BuildConfig.ISDEBUG, BUGLYKEY);

        //网络
        final RetrofitManager.Builder builder = RetrofitManager.newBuilder(BuildConfig.HOST, BuildConfig.PORT)
                .isDebug(BuildConfig.ISDEBUG)
                .connectTimeout(60)
                .readTimeout(60)
                .writeTimeout(60)
                .okBuilder(Okhttp3Help.getOkHttpClientBuilder());
        apiService = createApiService(builder, XinyiApi.class);
        cacheProviders = new RxCache.Builder()
                .persistence(FileUtils2.getNetCacheDir())
                .using(CacheProvider.class);
        ImageLoaderUtils.init(R.drawable.app_icon);

        //BlockCanary.install(this, new AppBlockCanaryContext()).start();
        TypeUtils.compatibleWithJavaBean = true;
    }


    /**
     * 初始化云推送通道
     *
     * @param applicationContext
     */
    private void initCloudChannel(Context applicationContext) {
        PushServiceFactory.init(applicationContext);
        CloudPushService pushService = PushServiceFactory.getCloudPushService();
        //Toast.makeText(applicationContext,"设备"+pushService.getDeviceId(), Toast.LENGTH_SHORT).show();
        pushService.register(applicationContext, new CommonCallback() {
            @Override
            public void onSuccess(String response) {

                // UI.toast(getApplicationContext(), "init cloudchannel success");
            }

            @Override
            public void onFailed(String errorCode, String errorMessage) {
                // UI.toast(getApplicationContext(), "init cloudchannel failed -- errorcode:" + errorCode + " -- errorMessage:" + errorMessage);
            }
        });

        // 初始化小米通道，自动判断是否支持小米系统推送，如不支持会跳过注册
        // MiPushRegister.register(applicationContext, "小米AppID", "小米AppKey");
        // 初始化华为通道，自动判断是否支持华为系统推送，如不支持会跳过注册
        HuaWeiRegister.register(applicationContext);
    }


    /*** 初始化ImageLoader的对象，默认 加在图*/
    @SuppressWarnings("static-access")
    public void initImageLoader(Context context) {
        ImageLoader imageLoader = ImageLoader.getInstance();
        @SuppressWarnings("deprecation")
        DisplayImageOptions options = new DisplayImageOptions.Builder()
                .imageScaleType(ImageScaleType.IN_SAMPLE_INT)
                .cacheInMemory(true)
                .cacheOnDisc(false)
                .bitmapConfig(Bitmap.Config.RGB_565).build();
        File cacheDir = StorageUtils.getOwnCacheDirectory(context, "xmjt/Cache");// 缓存地址
        ImageLoaderConfiguration config = (new ImageLoaderConfiguration.Builder(
                context).threadPriority(Thread.NORM_PRIORITY - 1)
                .memoryCacheExtraOptions(240, 320)// default = device screen dimensions 内存缓存文件的最大长宽
                // .diskCacheExtraOptions(240, 320, null)// 本地缓存的详细信息(缓存的最大长宽)，最好不要设置这个
                .threadPoolSize(5)// default  线程池内加载的数量
                .denyCacheImageMultipleSizesInMemory()
                .memoryCache(new WeakMemoryCache())//可以通过自己的内存缓存实现
                // .memoryCache(new LruMemoryCache(2 * 1024 * 1024))//可以通过自己的内存缓存实现
                .memoryCacheSize(50 * 1024 * 1024)// 50 Mb sd卡(本地)缓存的最大值
                .discCacheFileNameGenerator(new Md5FileNameGenerator())
                .diskCache(new UnlimitedDiskCache(cacheDir))
                .tasksProcessingOrder(QueueProcessingType.LIFO))
                .defaultDisplayImageOptions(options)
                .writeDebugLogs() // 打印debug log
                .build();
        imageLoader.getInstance().init(config);
    }

    /*** DisplayImageOptions，默认 获取图片时的图片*/
    @SuppressWarnings("static-access")
    public static DisplayImageOptions initOption() {
        DisplayImageOptions options = new DisplayImageOptions.Builder()
                .showImageOnLoading(R.drawable.loading_pic)
                .showImageForEmptyUri(R.drawable.loading_pic)
                .showImageOnFail(R.drawable.loading_pic)
                .resetViewBeforeLoading(false) // 设置延迟部分时间才开始加载 /
                .cacheInMemory(true)
                .cacheOnDisc(true).considerExifParams(true)
                .build();
        return options;
    }


    public static AppContext instance() {
        return instance;
    }

    /**
     * 获取设备imei码
     *
     * @return
     */
    public String getDeviceId() {
        TelephonyManager telephonyManager = (TelephonyManager) this.getSystemService(Context.TELEPHONY_SERVICE);
        String imei = telephonyManager.getDeviceId();
        return imei;
    }

    /**
     * 检测当前系统声音是否为正常模式
     *
     * @return
     */
    public boolean isAudioNormal() {
        AudioManager mAudioManager = (AudioManager) getSystemService(AUDIO_SERVICE);
        return mAudioManager.getRingerMode() == AudioManager.RINGER_MODE_NORMAL;
    }

    /**
     * 检测网络是否可用
     *
     * @return
     */
    public boolean isNetworkConnected() {
        ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo ni = cm.getActiveNetworkInfo();
        return ni != null && ni.isConnectedOrConnecting();
    }

    /**
     * 检查设备是否有相机
     */
    public boolean checkCameraHardware() {
        return getPackageManager().hasSystemFeature(PackageManager.FEATURE_CAMERA);
    }

    /**
     * 判断GPS是否开启，GPS或者AGPS开启一个就认为是开启的
     *
     * @return true 表示开启
     */
    public boolean isGpsOPen() {
        LocationManager locationManager
                = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        // 通过GPS卫星定位，定位级别可以精确到街（通过24颗卫星定位，在室外和空旷的地方定位准确、速度快）
        boolean gps = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
        // 通过WLAN或移动网络(3G/2G)确定的位置（也称作AGPS，辅助GPS定位。主要用于在室内或遮盖物（建筑群或茂密的深林等）密集的地方定位）
        //boolean network = locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);
        return gps;
    }

    /**
     * 获取当前网络类型
     *
     * @return 0：没有网络 1：WIFI网络 2：WAP网络 3：NET网络
     */
    public int getNetworkType() {
        int netType = 0;
        ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();

        if (networkInfo == null) {
            return netType;
        }
        int nType = networkInfo.getType();
        if (nType == ConnectivityManager.TYPE_MOBILE) {
            String extraInfo = networkInfo.getExtraInfo();
            if (!StringUtils.isEmpty(extraInfo)) {
                if (extraInfo.toLowerCase().equals("cmnet")) {
                    netType = NETTYPE_CMNET;
                } else {
                    netType = NETTYPE_CMWAP;
                }
            }
        } else if (nType == ConnectivityManager.TYPE_WIFI) {
            netType = NETTYPE_WIFI;
        }
        return netType;
    }


    /**
     * 判断当前版本是否兼容目标版本的方法
     *
     * @param VersionCode
     * @return
     */
    public static boolean isMethodsCompat(int VersionCode) {
        int currentVersion = android.os.Build.VERSION.SDK_INT;
        return currentVersion >= VersionCode;
    }

    /**
     * 获取App安装包信息
     *
     * @return
     */
    public PackageInfo getPackageInfo() {
        PackageInfo info = null;
        try {
            info = getPackageManager().getPackageInfo(getPackageName(), 0);
        } catch (NameNotFoundException e) {
            e.printStackTrace(System.err);
        }
        if (info == null) info = new PackageInfo();
        return info;
    }

    /**
     * 检测应用权限
     *
     * @param permissionName
     * @return
     */
    public boolean checkPermission(String permissionName) {
        boolean flag = false;
        try {
            PackageInfo packageInfo = getPackageManager().getPackageInfo(getPackageName(), PackageManager.GET_PERMISSIONS);
            String[] requestedPermissions = packageInfo.requestedPermissions;
            if (requestedPermissions != null) {
                for (int i = 0; i < requestedPermissions.length; i++) {
                    if (requestedPermissions[i].equals(permissionName)) {
                        flag = true;
                        break;
                    }
                }
            }
        } catch (Exception e) {

        }

        return flag;
    }

    /**
     * 检查指定的应用程序是否安装
     *
     * @param packageName
     * @return
     */
    public boolean isApkAvilible(String packageName) {
        final PackageManager packageManager = getPackageManager();
        // 获取所有已安装程序的包信息
        List<PackageInfo> pinfo = packageManager.getInstalledPackages(0);
        for (int i = 0; i < pinfo.size(); i++) {
            if (pinfo.get(i).packageName.equalsIgnoreCase(packageName))
                return true;
        }
        return false;
    }

    /**
     * 获取车牌生成的图片文件全路径
     *
     * @param type
     * @return
     */
    public String getCaptureImagePath(int type) {
        String tmp_dir = Environment.getExternalStorageDirectory().getPath()
                + File.separator + APP_ID + File.separator + "xpr" + File.separator;
        File file = new File(tmp_dir);
        if (!file.exists()) {
            file.mkdirs();
        }
        String account = AppContext.instance.getLoginInfo().getUsername();
        String name = account
                + "_" + new SimpleDateFormat("yyyyMMddHHmmssSSSS").format(new Date()) + ".jpg";
        String path = tmp_dir + name;
        return path;
    }


    /**
     * 获取监控室。摄像头等生成的图片文件全路径
     *
     * @param type
     * @return
     */
    public String getCameraImagePath(int type) {
        String tmp_dir = Environment.getExternalStorageDirectory().getPath()
                + File.separator + APP_ID + File.separator + "jks" + File.separator;
        File file = new File(tmp_dir);
        if (!file.exists()) {
            file.mkdirs();
        }
        String account = AppContext.instance.getLoginInfo().getUsername();
        String name = account
                + "_" + new SimpleDateFormat("yyyyMMddHHmmssSSSS").format(new Date())
                + ".jpg";
        String path = tmp_dir + name;
        return path;
    }

    /**
     * 店铺采集 图片
     *
     * @param type
     * @return
     */
    public String getStoreImagePath(int type) {
        String tmp_dir = Environment.getExternalStorageDirectory().getPath()
                + File.separator + APP_ID + File.separator + "store" + File.separator;
        File file = new File(tmp_dir);
        if (!file.exists()) {
            file.mkdirs();
        }
        String account = AppContext.instance.getLoginInfo().getUsername();
        String name = account
                + "_" + new SimpleDateFormat("yyyyMMddHHmmssSSSS").format(new Date())
                + ".jpg";
        String path = tmp_dir + name;
        return path;
    }

    public static String getCheckPath() {
        String tmp_dir = Environment.getExternalStorageDirectory().getPath()
                + File.separator + APP_ID + File.separator + "Cach" + File.separator;
        File file = new File(tmp_dir);
        if (!file.exists()) {
            file.mkdirs();
        }
        return tmp_dir;
    }


    /**
     * 获取App唯一标识
     *
     * @return
     */
    public String getAppId() {
        String uniqueID = getProperty(AppConfig.CONF_APP_UNIQUEID);
        if (StringUtils.isEmpty(uniqueID)) {
            uniqueID = UUID.randomUUID().toString();
            setProperty(AppConfig.CONF_APP_UNIQUEID, uniqueID);
        }
        return uniqueID;
    }

    public static HashMap<String, TaskListModel.DataBean> getTaskMap() {
        return taskMap;
    }

    /**
     * 保存登录信息
     */
    public void saveLoginInfo(final UserInfo user, final String permin) {
        this.loginUid = user.getId();
        this.login = true;
        setProperties(new Properties() {
            {
                setProperty("user.id", String.valueOf(user.getId()));
                setProperty("user.name", StringUtils.nullToString(user.getName()));
                setProperty("user.username", StringUtils.nullToString(user.getUsername()));
                setProperty("user.password", StringUtils.nullToString(user.getPassword()));
                setProperty("user.cellphone", StringUtils.nullToString(user.getCellphone()));
                setProperty("user.organcode", StringUtils.nullToString(user.getOrgancode()));
                setProperty("user.jwscode", StringUtils.nullToString(user.getJwscode()));
                setProperty("user.ssjd", StringUtils.nullToString(user.getSsjd()));
                setProperty("user.sssq", StringUtils.nullToString(user.getSssq()));
                setProperty("user.sswg", StringUtils.nullToString(user.getSswg()));
                setProperty("user.accounttype", StringUtils.nullToString(user.getAccounttype()));
                setProperty("user.policeno", StringUtils.nullToString(user.getPoliceno()));
                setProperty("user.createtime", StringUtils.nullToString(user.getCreatetime()));
                setProperty("user.sfzh", StringUtils.nullToString(user.getSfzh()));
                if (permin != null) {
                    setProperty("user.permin", permin);// 权限
                }
                setProperty("user.companyname", StringUtils.nullToString(user.getCompanyname()));// 厂商公司名称
                setProperty("user.sfzzp", StringUtils.nullToString(user.getSfzzp()));// 身份证照片
                setProperty("user.isregester", String.valueOf(user.getIsregester()));// 是否注册权限
                setProperty("user.isremember,", StringUtils.nullToString(user.getIsremember()));// 是否记住我的信息
                setProperty("user.pcs", StringUtils.nullToString(user.getPcs()));
                setProperty("user.jws", StringUtils.nullToString(user.getJws()));
                setProperty("user.fjbm", StringUtils.nullToString(user.getFjbm()));
                setProperty("user.fj", StringUtils.nullToString(user.getFj()));
                setProperty("user.txzp", StringUtils.nullToString(user.getTxzp()));
                setProperty("user.roleNmae", StringUtils.nullToString(user.getRoleNmae()));
                //所属警务室
                if (null != user.getJws() && !user.getJws().isEmpty()) {
                    setProperty("user.jws", StringUtils.nullToString(user.getJws()));
                } else {
                    setProperty("user.jws", "其他");
                }
            }
        });
    }

    /**
     * 清除登录信息
     */
    public void cleanLoginInfo() {
        this.loginUid = 0;
        this.login = false;
        removeProperty("user.id", "user.name", "user.username", "user.password", "user.cellphone", "user.organcode", "user.jwscode", "user.jws", "user.pcs", "user.ssjd",
                "user.sssq", "user.sswg", "user.accounttype", "user.policeno", "user.createtime", "user.isremember", "user.permin", "user.sfzh", "user.companyname", "user.sfzzp",
                "user.isregester", "user.fj", "user.fjbm");
    }

    /**
     * 获取登录信息
     *
     * @return
     */
    public UserInfo getLoginInfo() {
        UserInfo user = new UserInfo();
        user.setId(StringUtils.toInt(getProperty("user.id"), 0));
        user.setIsregester(StringUtils.toInt(getProperty("user.isregester"), 0));
        user.setName(getProperty("user.name"));
        user.setUsername(getProperty("user.username"));
        user.setPassword(getProperty("user.password"));
        user.setCellphone(getProperty("user.cellphone"));
        user.setOrgancode(getProperty("user.organcode"));
        user.setJwscode(getProperty("user.jwscode"));
        user.setSsjd(getProperty("user.ssjd"));
        user.setAccounttype(getProperty("user.accounttype"));
        user.setPoliceno(getProperty("user.policeno"));
        user.setCreatetime(getProperty("user.createtime"));
        user.setIsremember(getProperty("user.isremember"));
        user.setPcs(getProperty("user.pcs"));
        user.setJws(getProperty("user.jws"));
        user.setSssq(getProperty("user.sssq"));
        user.setSswg(getProperty("user.sswg"));
        user.setSfzh(getProperty("user.sfzh"));
        user.setCompanyname(getProperty("user.companyname"));
        user.setSfzzp(getProperty("user.sfzzp"));
        user.setFj(getProperty("user.fj"));
        user.setFjbm(getProperty("user.fjbm"));
        user.setTxzp(getProperty("user.txzp"));
        user.setRoleNmae(getProperty("user.roleNmae"));
        return user;
    }

    /**
     * 获取登录信息
     *
     * @return
     */
    public String getLoginPermis() {
        return getProperty("user.permin");
    }

    public boolean containsProperty(String key) {
        Properties props = getProperties();
        return props.containsKey(key);
    }

    public void setProperties(Properties ps) {
        AppConfig.getAppConfig(this).set(ps);
    }

    public Properties getProperties() {
        return AppConfig.getAppConfig(this).get();
    }

    public void setProperty(String key, String value) {
        AppConfig.getAppConfig(this).set(key, value);
    }

    public String getProperty(String key) {
        return AppConfig.getAppConfig(this).get(key);
    }

    public void removeProperty(String... key) {
        AppConfig.getAppConfig(this).remove(key);
    }

    //session过期重新登录
    public void sessionOutReLogin(final Context context) {
        String msg = context.getResources().getString(R.string.msg_session_timeout);
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setMessage(msg).setCancelable(false)
                .setPositiveButton(R.string.sure, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        Intent intent = new Intent(context, LoginActivity.class);
                        context.startActivity(intent);
                        ((Activity) context).finish();
                    }
                });
        AlertDialog alert = builder.create();
        alert.show();
    }


    public static long getLastTime() {
        return lastTime;
    }

    public static void setLastTime(long lastTime) {
        AppContext.lastTime = lastTime;
    }

    public static List<DevicesPositionModel> getDeviceListPostion() {
        return deviceListPostion;
    }

    public static void setDeviceListPostion(List<DevicesPositionModel> deviceListPostion) {
        AppContext.deviceListPostion = deviceListPostion;
    }

    public static HashMap<String, DevicesModel.DataBean> getDevicesMap() {
        return devicesMap;
    }


    public static HashMap<String, UnitModel.DataBean> getUnitModelMap() {
        return unitModelMap;
    }

    public static List<Marker> getOverlays() {
        return overlays;
    }

    public static void setOverlays(List<Marker> overlays) {
        AppContext.overlays = overlays;
    }
}
