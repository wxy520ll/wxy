package cn.net.xinyi.xmjt.activity.Collection.Camera;

import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import cn.net.xinyi.xmjt.R;
import cn.net.xinyi.xmjt.config.AppContext;
import cn.net.xinyi.xmjt.config.BaseActivity;
import cn.net.xinyi.xmjt.utils.AnnotateManager;
import cn.net.xinyi.xmjt.utils.BaseUtil;
import cn.net.xinyi.xmjt.utils.BindView;

/**
 * Created by hao.zhou on 2015/11/7.
 * 数据统计界面
 */
public class RankCountTotalActivity extends BaseActivity implements View.OnClickListener {
    private int networkType;
    private Intent intent;
    //街道办采集排名
    @BindView(id = R.id.tv_jd_rank,click = true)
    TextView tv_jd_rank;
    //派出所采集排名
    @BindView(id = R.id.tv_pcs_rank,click = true)
    TextView tv_pcs_rank;
    //用户每日上传数据
    @BindView(id = R.id.tv_yh_rank,click = true)
    TextView tv_yh_rank;
    //街道办用户采集排名
    @BindView(id = R.id.tv_jd_yh_rank,click = true)
    TextView tv_jd_yh_rank;
    //派出所用户采集排名
    @BindView(id = R.id.tv_pcs_yh_rank,click = true)
    TextView tv_pcs_yh_rank;
    //汇总统计
    @BindView(id = R.id.tv_total,click = true)
    TextView tv_total;
    //派出所用户核查排名
    @BindView(id = R.id.tv_pcs_check_user_rank,click = true)
    TextView tv_pcs_check_user_rank;
    //派出所用户核查排名
    @BindView(id = R.id.tv_pcs_check_rank,click = true)
    TextView tv_pcs_check_rank;
    //个人核查排名
    @BindView(id = R.id.tv_yh_check_rank,click = true)
    TextView tv_yh_check_rank;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.cj_count_total);
        AnnotateManager.initBindView(this);
        getActionBar().setIcon(R.drawable.actionbar_back_icon_normal);
        getActionBar().setTitle(getResources().getString(R.string.info_total));
        getActionBar().setHomeButtonEnabled(true);

    }

    @Override
    public void onClick(View v) {

        switch (v.getId()){
            case R.id.tv_jd_rank:
                //检测是否连接wifi网络
                networkType = ((AppContext) getApplication()).getNetworkType();
                if (networkType == 0) {
                    BaseUtil.showDialog("当前无可用的网络连接，无法查询数据信息", RankCountTotalActivity.this);
                    break;
                }
                 intent = new Intent(this,RankJDActivity.class);
                this.startActivity(intent);
                break;
            case R.id.tv_pcs_rank:
                //检测是否连接wifi网络
                networkType = ((AppContext) getApplication()).getNetworkType();
                if (networkType == 0) {
                    BaseUtil.showDialog("当前无可用的网络连接，无法查询数据信息", RankCountTotalActivity.this);
                    break;
                }
                intent = new Intent(this,RankPCSActivity.class);
                this.startActivity(intent);
                break;


            case R.id.tv_jd_yh_rank:
                //检测是否连接wifi网络
                networkType = ((AppContext) getApplication()).getNetworkType();
                if (networkType == 0) {
                    BaseUtil.showDialog("当前无可用的网络连接，无法查询数据信息", RankCountTotalActivity.this);
                    break;
                }
                intent = new Intent(this,RankJDUserActivity.class);
                this.startActivity(intent);
                break;

            case R.id.tv_pcs_yh_rank:
                //检测是否连接wifi网络
                networkType = ((AppContext) getApplication()).getNetworkType();
                if (networkType == 0) {
                    BaseUtil.showDialog("当前无可用的网络连接，无法查询数据信息", RankCountTotalActivity.this);
                    break;
                }
                intent = new Intent(this,RankPCSUserActivity.class);
                this.startActivity(intent);
                break;

            case R.id.tv_yh_rank:
                //检测是否连接wifi网络
                networkType = ((AppContext) getApplication()).getNetworkType();
                if (networkType == 0) {
                    BaseUtil.showDialog("当前无可用的网络连接，无法查询数据信息", RankCountTotalActivity.this);
                    break;
                }
                intent = new Intent(this,RankUploadUserActivity.class);
                this.startActivity(intent);
                break;

            case R.id.tv_total:
                //检测是否连接wifi网络
                networkType = ((AppContext) getApplication()).getNetworkType();
                if (networkType == 0) {
                    BaseUtil.showDialog("当前无可用的网络连接，无法查询数据信息", RankCountTotalActivity.this);
                    break;
                }
                intent= new Intent(this,RankTotalActivity.class);
                this.startActivity(intent);
                break;

            case R.id.tv_pcs_check_user_rank:
                //检测是否连接wifi网络
                networkType = ((AppContext) getApplication()).getNetworkType();
                if (networkType == 0) {
                    BaseUtil.showDialog("当前无可用的网络连接，无法查询数据信息", RankCountTotalActivity.this);
                    break;
                }
                intent= new Intent(this,RankPCSUserCheckActivity.class);
                this.startActivity(intent);
                break;

            case R.id.tv_yh_check_rank:
                //检测是否连接wifi网络
                networkType = ((AppContext) getApplication()).getNetworkType();
                if (networkType == 0) {
                    BaseUtil.showDialog("当前无可用的网络连接，无法查询数据信息", RankCountTotalActivity.this);
                    break;
                }
                intent= new Intent(this,RankCheckUserActivity.class);
                this.startActivity(intent);
                break;

            case R.id.tv_pcs_check_rank:
                //检测是否连接wifi网络
                networkType = ((AppContext) getApplication()).getNetworkType();
                if (networkType == 0) {
                    BaseUtil.showDialog("当前无可用的网络连接，无法查询数据信息", RankCountTotalActivity.this);
                    break;
                }
                intent= new Intent(this,RankPCSCheckActivity.class);
                this.startActivity(intent);
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
                && event.getKeyCode() == KeyEvent.KEYCODE_BACK) {
            this.finish();
        }
        return super.dispatchKeyEvent(event);
    }
}