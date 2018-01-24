package cn.net.xinyi.xmjt.activity.Collection.ZAJC;

import android.os.Bundle;
import android.view.KeyEvent;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.ImageView;

import cn.net.xinyi.xmjt.R;
import cn.net.xinyi.xmjt.config.BaseActivity;
import cn.net.xinyi.xmjt.model.View.IZAJKView;
import cn.net.xinyi.xmjt.model.ZAJCJKModle;
import cn.net.xinyi.xmjt.utils.AnnotateManager;
import cn.net.xinyi.xmjt.utils.BindView;
import cn.net.xinyi.xmjt.utils.ImageUtils;

/**
 * Created by hao.zhou on 2016/3/19.
 */
public class ZAJCJKWatchAty extends BaseActivity implements IZAJKView {
    /**监控室编号**/
    @BindView(id = R.id.et_jksbh,click = false)
    private EditText et_jksbh;
    /**摄像头总数**/
    @BindView(id = R.id.et_sxtzs,click = false)
    private EditText et_sxtzs;
    /**摄像头正常数**/
    @BindView(id = R.id.et_sxtzcs,click = false)
    private EditText et_sxtzcs;
    /**门口摄像头正常数**/
    @BindView(id = R.id.et_mksxts,click = false)
    private EditText et_mksxts;
    /**监控屏幕**/
    @BindView(id = R.id.iv_jkpm,click = false)
    private ImageView iv_jkpm;
    /**摄像头全景图**/
    @BindView(id = R.id.iv_sxtqjt,click = false)
    private ImageView iv_sxtqjt;
    private  String path1;
    private  String path2;
    private ZAJCJKModle jkInfo;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.aty_zajkxx_watch);
        getActionBar().setIcon(R.drawable.actionbar_back_icon_normal);
        getActionBar().setTitle(getString(R.string.zajk_watch));
        getActionBar().setHomeButtonEnabled(true);
        /***注解式绑定控件**/
        AnnotateManager.initBindView(this);
        jkInfo = (ZAJCJKModle) getIntent().getSerializableExtra("jkInfo");
        initData();
    }

    private void initData() {
        if (jkInfo!=null){
        et_jksbh.setText(jkInfo.getJKSBH());
        et_sxtzs.setText(jkInfo.getSXTZS());
        et_sxtzcs.setText(jkInfo.getZCS());
        et_mksxts.setText(jkInfo.getMKSXTS());
        if (jkInfo.getIV_JKPMT()!=null){
            iv_jkpm.setImageBitmap(ImageUtils.decodeFile(jkInfo.getIV_JKPMT()));
        }
        if (jkInfo.getIV_SXTQJT()!=null){
            iv_sxtqjt.setImageBitmap(ImageUtils.decodeFile(jkInfo.getIV_SXTQJT()) );
        }
    }
    }

    @Override
    public int getJKID() {
        return 0;
    }

    @Override
    public String getJKSBH() {
        return et_jksbh.getText().toString();
    }

    @Override
    public String getSXTZS() {
        return et_sxtzs.getText().toString();
    }

    @Override
    public String getZCS() {
        return et_sxtzcs.getText().toString();
    }

    @Override
    public String getMKSXTS() {
        return et_mksxts.getText().toString();
    }

    @Override
    public String getIV_JKPMT() {
        return path1;
    }

    @Override
    public String getIV_SXTQJT() {
        return path2;
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