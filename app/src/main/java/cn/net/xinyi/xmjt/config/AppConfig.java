package cn.net.xinyi.xmjt.config;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.util.Properties;

public class AppConfig{
	
	private final static String APP_CONFIG = "config";
	public final static String CONF_APP_UNIQUEID = "APP_UNIQUEID";
	public final static String CONTACTS_UPDATE_TIME = "CONTACTS_UPDATE_TIME";
	public final static String BLACKPLATE_UPDATE_TIME = "BLACKPLATE_UPDATE_TIME";
	public final static String ZD_UPDATE_TIME = "ZDINFOS_UPDATE_TIME";
	public final static String ZDINFOS_UPDATE_TIME = "ZDINFOSs_UPDATE_TIME";

	
	private Context mContext;
	private static AppConfig appConfig;
	
	public static AppConfig getAppConfig(Context context)
	{
		if(appConfig == null){
			appConfig = new AppConfig();
			appConfig.mContext = context;
		}
		return appConfig;
	}
	
	/**
	 * 获取Preference设置
	 */
	public static SharedPreferences getSharedPreferences(Context context)
	{
		return PreferenceManager.getDefaultSharedPreferences(context);
		//return context.getSharedPreferences("xmjt", 0);
	}
	
	public static void CleanCurrentConfig(Context context){
        //清除保存的用户信息
        // 载入配置文件
        SharedPreferences sp = AppConfig.getSharedPreferences(context);
        SharedPreferences.Editor spEd = sp.edit();
        spEd.remove("account");
        spEd.remove("password");
        spEd.remove("autologin");
        spEd.remove("pcs");
        spEd.remove("jws");
        spEd.commit();
	}

	public String get(String key) {
		Properties props = get();
		return (props != null) ? props.getProperty(key) : null;
	}

	public Properties get() {
		FileInputStream fis = null;
		Properties props = new Properties();
		try {
			// 读取files目录下的config
			// fis = activity.openFileInput(APP_CONFIG);

			// 读取app_config目录下的config
			File dirConf = mContext.getDir(APP_CONFIG, Context.MODE_MULTI_PROCESS);
			fis = new FileInputStream(dirConf.getPath() + File.separator
					+ APP_CONFIG);

			props.load(fis);
		} catch (Exception e) {
		} finally {
			try {
				fis.close();
			} catch (Exception e) {
			}
		}
		return props;
	}

	private void setProps(Properties p) {
		FileOutputStream fos = null;
		try {
			// 把config建在files目录下
			// fos = activity.openFileOutput(APP_CONFIG, Context.MODE_PRIVATE);

			// 把config建在(自定义)app_config的目录下
			File dirConf = mContext.getDir(APP_CONFIG, Context.MODE_MULTI_PROCESS);
			File conf = new File(dirConf, APP_CONFIG);
			fos = new FileOutputStream(conf);

			p.store(fos, null);
			fos.flush();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				fos.close();
			} catch (Exception e) {
			}
		}
	}

	public void set(Properties ps) {
		Properties props = get();
		props.putAll(ps);
		setProps(props);
	}

	public void set(String key, String value) {
		Properties props = get();
		props.setProperty(key, value);
		setProps(props);
	}

	public void remove(String... key) {
		Properties props = get();
		for (String k : key)
			props.remove(k);
		setProps(props);
	}

}
