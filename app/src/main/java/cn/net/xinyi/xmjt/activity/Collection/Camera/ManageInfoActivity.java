package cn.net.xinyi.xmjt.activity.Collection.Camera;

import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.MenuItem;
import android.view.View;

import cn.net.xinyi.xmjt.R;
import cn.net.xinyi.xmjt.config.AppContext;
import cn.net.xinyi.xmjt.config.BaseActivity;
import cn.net.xinyi.xmjt.utils.BaseUtil;

/**
 * Created by hao.zhou on 2015/11/11.
 * 信息管理界面
 */
public class ManageInfoActivity extends BaseActivity implements View.OnClickListener{
    private int networkType;
    private Intent intent;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.manage_info);

        getActionBar().setIcon(R.drawable.actionbar_back_icon_normal);
        getActionBar().setTitle("信息管理");
        getActionBar().setHomeButtonEnabled(true);

        findViewById(R.id.JKSinfo_Manage).setOnClickListener(this);
        findViewById(R.id.SXTinfo_Manage).setOnClickListener(this);
        findViewById(R.id.JKSinfo_upload_Manage).setOnClickListener(this);
        findViewById(R.id.SXTinfo_upload_Manage).setOnClickListener(this);
        findViewById(R.id.CheckFaile_JKS_Manage).setOnClickListener(this);
        findViewById(R.id.CheckFaile_SXT_Manage).setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            /*** 监控室核查不通过信息修改 */
            case R.id.CheckFaile_JKS_Manage:
                //检测是否连接网络
                networkType = ((AppContext) getApplication()).getNetworkType();
                if (networkType == 0) {
                    BaseUtil.showDialog("当前无可用的网络连接，将无法获取位置信息", ManageInfoActivity.this);
                    break;
                }
                intent=new Intent(this,CheckFaileJKSInfoActivity.class);
                startActivity(intent);
                break;

            /*** 摄像头核查不通过信息修改 */
            case R.id.CheckFaile_SXT_Manage:
                //检测是否连接网络
                networkType = ((AppContext) getApplication()).getNetworkType();
                if (networkType == 0) {
                    BaseUtil.showDialog("当前无可用的网络连接，将无法获取位置信息", ManageInfoActivity.this);
                    break;
                }
                intent=new Intent(this,CheckFaileSXTInfoActivity.class);
                startActivity(intent);

                break;

            /*** 监控室本地信息管理 */
            case R.id.JKSinfo_Manage:
                //检测是否连接网络
                networkType = ((AppContext) getApplication()).getNetworkType();
                if (networkType == 0) {
                    BaseUtil.showDialog("当前无可用的网络连接，将无法获取位置信息", ManageInfoActivity.this);
                    break;
                }
                intent=new Intent(this,ManageJKSInfoActivity.class);
                startActivity(intent);
                break;

            /*** 摄像头本地信息管理 */
            case R.id.SXTinfo_Manage:
                //检测是否连接网络
                networkType = ((AppContext) getApplication()).getNetworkType();
                if (networkType == 0) {
                    BaseUtil.showDialog("当前无可用的网络连接，将无法获取位置信息", ManageInfoActivity.this);
                    break;
                }
                intent=new Intent(this,ManageSXTInfoActivity.class);
                startActivity(intent);
                break;

            /*** 监控室已上传信息管理 */
            case R.id.JKSinfo_upload_Manage:
                //检测是否连接网络
                networkType = ((AppContext) getApplication()).getNetworkType();
                if (networkType == 0) {
                    BaseUtil.showDialog("当前无可用的网络连接，将无法获取位置信息", ManageInfoActivity.this);
                    break;
                }
                intent=new Intent(this,DownJKSInfoActivity.class);
                startActivity(intent);
                break;

            /***  摄像头已上传信息管理*/
            case R.id.SXTinfo_upload_Manage:
                //检测是否连接网络
                networkType = ((AppContext) getApplication()).getNetworkType();
                if (networkType == 0) {
                    BaseUtil.showDialog("当前无可用的网络连接，将无法获取位置信息", ManageInfoActivity.this);
                    break;
                }
                intent=new Intent(this,DownSXTInfoActivity.class);
                startActivity(intent);
                break;
        }
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                this.finish();
                break;
            default:
                break;
        }
        return true;
    }

    /**
     * 捕获后退按钮
     */
    @Override
    public boolean dispatchKeyEvent(KeyEvent event) {
        if (event.getAction() == KeyEvent.ACTION_DOWN
                &&event.getKeyCode() == KeyEvent.KEYCODE_BACK) {
            this.finish();
        }
        return super.dispatchKeyEvent(event);
    }
}