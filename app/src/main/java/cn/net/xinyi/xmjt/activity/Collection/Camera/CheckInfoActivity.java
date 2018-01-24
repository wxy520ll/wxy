package cn.net.xinyi.xmjt.activity.Collection.Camera;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.MenuItem;
import android.view.View;

import cn.net.xinyi.xmjt.R;
import cn.net.xinyi.xmjt.config.AppContext;
import cn.net.xinyi.xmjt.utils.BaseUtil;

/**
 * Created by hao.zhou on 2015/11/11.
 * 采集信息核查的主界面
 *1.监控室核查
 * 2.摄像头核查
 *  */
public class CheckInfoActivity extends Activity implements View.OnClickListener{
    private int networkType;
    private Intent intent;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.check_info);

        getActionBar().setIcon(R.drawable.actionbar_back_icon_normal);
        getActionBar().setTitle(getResources().getString(R.string.info_check));
        getActionBar().setHomeButtonEnabled(true);

        initView();
    }

    private void initView() {
        findViewById(R.id.JKSinfo_check).setOnClickListener(this);
        findViewById(R.id.SXTinfo_check).setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.JKSinfo_check:
                //检测是否连接网络
                networkType = ((AppContext) getApplication()).getNetworkType();
                if (networkType == 0) {
                    BaseUtil.showDialog("当前无可用的网络连接，将无法获取位置信息", CheckInfoActivity.this);
                    break;
                }
                intent=new Intent(this,CheckJKSInfoActivity.class);
                startActivity(intent);
                break;

            case R.id.SXTinfo_check:
                //检测是否连接网络
                networkType = ((AppContext) getApplication()).getNetworkType();
                if (networkType == 0) {
                    BaseUtil.showDialog("当前无可用的网络连接，将无法获取位置信息", CheckInfoActivity.this);
                    break;
                }
                intent=new Intent(this,CheckSXTInfoActivity.class);
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