package cn.net.xinyi.xmjt.activity.Collection.Person;

import android.os.Bundle;
import android.view.KeyEvent;
import android.view.MenuItem;
import android.view.View;

import cn.net.xinyi.xmjt.R;
import cn.net.xinyi.xmjt.config.BaseActivity;

/**
 * Created by hao.zhou on 2016/3/5.
 */
public class PerRetTotalAty extends BaseActivity implements View.OnClickListener {
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.aty_store_total);
        getActionBar().setIcon(R.drawable.actionbar_back_icon_normal);
        getActionBar().setTitle(getResources().getString(R.string.PER_RETURN_TOTAL));
        getActionBar().setHomeButtonEnabled(true);

        findViewById(R.id.ll_pcs).setOnClickListener(this);
        findViewById(R.id.ll_per_rank).setOnClickListener(this);
        findViewById(R.id.ll_per).setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.ll_pcs:
                showActivity(PerRetRankPcsAty.class);
                break;

            case R.id.ll_per_rank:
                showActivity(PerRetRankUserAty.class);
                break;

            case R.id.ll_per:
                showActivity(PerRetRankUploadAty.class);
                break;

        }
    }

    /**
     * 捕获后退按钮
     */
    @Override
    public boolean dispatchKeyEvent(KeyEvent event) {
        if (event.getKeyCode() == KeyEvent.KEYCODE_BACK) {
            this.finish();
        }
        // super.dispatchKeyEvent(event);
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