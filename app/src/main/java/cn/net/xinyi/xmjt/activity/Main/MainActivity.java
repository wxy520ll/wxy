package cn.net.xinyi.xmjt.activity.Main;

import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.IBinder;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import com.baidu.mapapi.map.offline.MKOfflineMap;
import com.baidu.mapapi.map.offline.MKOfflineMapListener;
import com.wintone.plateid.AuthService;
import com.wintone.plateid.PlateAuthParameter;
import com.wintone.plateid.RecogService;

import java.util.Date;

import cn.net.xinyi.xmjt.R;
import cn.net.xinyi.xmjt.config.BaseActivity;
import cn.net.xinyi.xmjt.utils.BaseDataUtils;
import cn.net.xinyi.xmjt.utils.BaseUtil;

public class MainActivity extends BaseActivity implements MKOfflineMapListener {


    // 文通车牌识别验证服务
    public AuthService.MyBinder authBinder;
    public RecogService.MyBinder recogBinder;
    private MKOfflineMap mOffline = null;

    // 授权验证服务绑定后的操作与start识别服务
    public ServiceConnection authConn = new ServiceConnection() {
        @Override
        public void onServiceDisconnected(ComponentName name) {
            authBinder = null;
        }

        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            authBinder = (AuthService.MyBinder) service;
            try {
                // sn：采用序列号方式激活时设置此参数，否则写""
                // authfile：采用激活文件方式激活时设置此参数，否则写""
                // 以上俩个参数都不为""时按序列号方式激活；当sn和authfile为""时会在根目录下找激活文件xxxxxxxxxxxxxxx_cp.txt
                // ReturnAuthority = authBinder.getAuth(sn, authfile);
                PlateAuthParameter pap = new PlateAuthParameter();
                pap.sn = "";
                pap.authFile = "";

                int returnAuthority = authBinder.getAuth(pap);
                if (returnAuthority != 0) {
                    // 激活失败
                } else {
                    // 激活成功
                }
            } catch (Exception e) {
                // 激活失败
                e.printStackTrace();
            } finally {
                if (authBinder != null) {
                    unbindService(authConn);
                }
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.start);

        // 文通车牌识别授权验证
//		Intent authIntent = new Intent(this, AuthService.class);
//		bindService(authIntent, authConn, Service.BIND_AUTO_CREATE);
        //启动时导入离线地图
        mOffline = new MKOfflineMap();
        mOffline.init(this);
        importFromSDCard();
        //显示版本信息
        TextView tv_version = (TextView) findViewById(R.id.tv_version);
        tv_version.setText("版本：" + BaseUtil.getVerName(this, getPackageName()));
        /**登录判断
         * 登录界面or主界面
         * */
        ifLoginOrMainMenu();

    }


    private void ifLoginOrMainMenu() {
        //获取本地存储 最后一次登录时间
        SharedPreferences settings = this.getSharedPreferences("LoginTime", 0);
        long historyTime = settings.getLong("LastLoginTime", 0);
        /** 需要重新输入密码
         *用户信息为空*
         *用户上一次登录时间大于现在时间
         *最后一次登录时间超过 24小时内
         */
        if (userInfo.getId() <= 0 ||
                new Date(System.currentTimeMillis()).getTime() < historyTime ||
                new Date(System.currentTimeMillis()).getTime() - historyTime > 1000 * 60 * 60 * 24) {
            lodingToLogin();
        } else {
            lodingToMainMenu();
        }
    }


    /**
     * 从SD卡导入离线地图安装包
     */
    public void importFromSDCard() {
        int num = mOffline.importOfflineData();
        String msg = "";
        if (num == 0) {
            msg = "没有导入离线包，这可能是离线包放置位置不正确，或离线包已经导入过";
        } else {
            msg = String.format("成功导入 %d 个离线地图包，可以在离线地图管理界面查看", num);
            Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onGetOfflineMapState(int type, int state) {
        switch (type) {
            case MKOfflineMap.TYPE_DOWNLOAD_UPDATE:
                break;
            case MKOfflineMap.TYPE_NEW_OFFLINE:
                // 有新离线地图安装
                Log.d("OfflineDemo", String.format("add offlinemap num:%d", state));
                break;
            case MKOfflineMap.TYPE_VER_UPDATE:
                // 版本更新提示
                // MKOLUpdateElement e = mOffline.getUpdateInfo(state);
                break;
        }

    }

    // 转到登录
    private void lodingToLogin() {
        Intent intent = new Intent(this, LoginActivity.class);
        startActivity(intent);
        this.finish();
    }

    // 转到主菜单
    private void lodingToMainMenu() {
        Intent intent = new Intent(this, cn.net.xinyi.xmjt.v527.presentation.MainActivity .class);
        if (BaseDataUtils.isAdminCommit()) {
            intent.putExtra("admin", true);
        } else {
            intent.putExtra("admin", false);
        }
        startActivity(intent);
        this.finish();
    }
}
