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
import cn.net.xinyi.xmjt.utils.SharedPreferencesUtil;

/**
 * Created by hao.zhou on 2015/9/14.
 * 信息采集主界面
 */
public class CollectionInfoActivity extends BaseActivity implements View.OnClickListener {
    private int networkType;
    private Intent intent;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.collection_information);

        getActionBar().setIcon(R.drawable.actionbar_back_icon_normal);
        getActionBar().setTitle("二三类点助手");
        getActionBar().setHomeButtonEnabled(true);
        initView();
    }

    private void initView() {
        if (!SharedPreferencesUtil.getBoolean(this,"iscolltips",false)){
            SharedPreferencesUtil.putBoolean(this,"iscolltips",true);
            BaseUtil.showDialog(getString(R.string.coll_tips),this);
        }
        findViewById(R.id.ll_jks).setOnClickListener(this);
        findViewById(R.id.ll_sxt).setOnClickListener(this);
        findViewById(R.id.ll_manage).setOnClickListener(this);
        findViewById(R.id.ll_total).setOnClickListener(this);
        findViewById(R.id.ll_check).setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            //信息统计查询
            case R.id.ll_total:
                //检测是否连接网络
                networkType = ((AppContext) getApplication()).getNetworkType();
                if (networkType == 0) {
                    BaseUtil.showDialog("当前无可用的网络连接，将无法获取位置信息", CollectionInfoActivity.this);
                    break;
                }
                intent = new Intent(this, RankCountTotalActivity.class);
                startActivity(intent);
                break;

            //监控室信息采集
            case R.id.ll_jks:
//                if (!AppContext.instance.getLoginInfo().getAccounttype().equals("综管员")){
//                    BaseUtil.showDialog("此模块仅限综管员使用！", this);
//                    break;
//                }
                //检测是否连接网络
                networkType = ((AppContext) getApplication()).getNetworkType();
                if (networkType == 0) {
                    BaseUtil.showDialog("当前无可用的网络连接，将无法获取位置信息", CollectionInfoActivity.this);
                    break;
                }
                intent = new Intent(this, JKSInfoActivity.class);
                startActivity(intent);
                break;

            //摄像头信息采集
            case R.id.ll_sxt:
//                if (!AppContext.instance.getLoginInfo().getAccounttype().equals("综管员")){
//                    BaseUtil.showDialog("此模块仅限综管员使用！", this);
//                    break;
//                }

                //检测是否连接网络
                networkType = ((AppContext) getApplication()).getNetworkType();
                if (networkType == 0) {
                    BaseUtil.showDialog("当前无可用的网络连接，将无法获取位置信息", CollectionInfoActivity.this);
                    break;
                }

                intent = new Intent(this, SXTInfoActivity.class);
                startActivity(intent);
                break;

            //本地缓存信息，监控室信息列表
            case R.id.ll_manage:
//                if (!AppContext.instance.getLoginInfo().getAccounttype().equals("综管员")){
//                    BaseUtil.showDialog("此模块仅限综管员使用！", this);
//                    break;
//                }
                intent = new Intent(this, ManageInfoActivity.class);
                startActivity(intent);
                break;

            //信息核查
            case R.id.ll_check:
//                if (!AppContext.instance.getLoginInfo().getAccounttype().equals("监控员")){
//                    BaseUtil.showDialog("此模块仅限监控员使用！", this);
//                    break;
//                }
                intent=new Intent(this,CheckInfoActivity.class);
                startActivity(intent);
                break;
        }
    }


    /**
     * 捕获后退按钮
     */
    @Override
    public boolean dispatchKeyEvent(KeyEvent event) {
        if (event.getAction() == KeyEvent.ACTION_DOWN
                && event.getKeyCode() == KeyEvent.KEYCODE_BACK) {
            this.finish();
        }
        return super.dispatchKeyEvent(event);
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
}