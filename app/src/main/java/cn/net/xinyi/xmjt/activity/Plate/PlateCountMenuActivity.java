package cn.net.xinyi.xmjt.activity.Plate;

import android.os.Bundle;
import android.view.KeyEvent;
import android.view.MenuItem;
import android.view.View;

import cn.net.xinyi.xmjt.R;
import cn.net.xinyi.xmjt.config.AppContext;
import cn.net.xinyi.xmjt.config.BaseActivity;
import cn.net.xinyi.xmjt.utils.BaseUtil;

/**
 * Created by mazhongwang on 15/5/8.
 */
public class PlateCountMenuActivity extends BaseActivity implements View.OnClickListener {
    private static final String TAG = PlateCountMenuActivity.class.getName();
    private int networkType;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.plate_count_main);
        initResource();
        getActionBar().setIcon(R.drawable.actionbar_back_icon_normal);
        getActionBar().setTitle("车牌统计");
        getActionBar().setHomeButtonEnabled(true);
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

    /**返回键退出*/
    @Override
    public boolean dispatchKeyEvent(KeyEvent event) {
        /**返回如果地图是显示，先隐藏地图*/
        if (event.getAction() == KeyEvent.ACTION_DOWN
                && event.getKeyCode() == KeyEvent.KEYCODE_BACK) {
            PlateCountMenuActivity.this.finish();
        }
        return super.dispatchKeyEvent(event);
    }


    private void initResource(){
        findViewById(R.id.ll_user_upload_rank).setOnClickListener(this);
        findViewById(R.id.ll_organ_upload_rank).setOnClickListener(this);
        findViewById(R.id.ll_user_upload).setOnClickListener(this);
        findViewById(R.id.ll_organ_upload_total).setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){

            case R.id.ll_user_upload:
                //检测是否连接wifi网络
                networkType = ((AppContext) getApplication()).getNetworkType();
                if (networkType == 0) {
                    BaseUtil.showDialog("当前无可用的网络连接，无法查询数据信息", PlateCountMenuActivity.this);
                    break;
                }

                showActivity(PlateUploadUserCountActivity.class);
                break;

            case R.id.ll_user_upload_rank:
                networkType = ((AppContext) getApplication()).getNetworkType();
                //检测是否连接网络
                if (networkType == 0) {
                    BaseUtil.showDialog("当前无可用的网络连接，无法查询数据信息", PlateCountMenuActivity.this);
                    break;
                }
                showActivity( LpnUserRankActivity.class);
                break;

            case R.id.ll_organ_upload_rank:
                networkType = ((AppContext) getApplication()).getNetworkType();
                //检测是否连接网络
                if (networkType == 0) {
                    BaseUtil.showDialog("当前无可用的网络连接，无法查询数据信息", PlateCountMenuActivity.this);
                    break;
                }
                showActivity(LpnOrganRankActivity.class);
                break;

            case R.id.ll_organ_upload_total:
                networkType = ((AppContext) getApplication()).getNetworkType();
                //检测是否连接网络
                if (networkType == 0) {
                    BaseUtil.showDialog("当前无可用的网络连接，无法查询数据信息", PlateCountMenuActivity.this);
                    break;
                }
                showActivity(LpnCountActivity.class);
                break;
        }
    }
}
