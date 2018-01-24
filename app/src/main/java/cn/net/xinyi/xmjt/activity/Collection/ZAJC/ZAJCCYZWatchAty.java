package cn.net.xinyi.xmjt.activity.Collection.ZAJC;

import android.os.Bundle;
import android.view.KeyEvent;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.ImageView;

import cn.net.xinyi.xmjt.R;
import cn.net.xinyi.xmjt.config.BaseActivity;
import cn.net.xinyi.xmjt.model.View.IZACYZView;
import cn.net.xinyi.xmjt.model.ZAJCCYZModle;
import cn.net.xinyi.xmjt.utils.AnnotateManager;
import cn.net.xinyi.xmjt.utils.BindView;
import cn.net.xinyi.xmjt.utils.GeneralUtils;
import cn.net.xinyi.xmjt.utils.ImageUtils;

/**
 * Created by hao.zhou on 2016/3/19.
 */
public class ZAJCCYZWatchAty extends BaseActivity implements IZACYZView {
    /***从业者姓名*/
    @BindView(id = R.id.et_cyzxm,click = false)
    private EditText et_cyzxm;
    /***从业者身份证*/
    @BindView(id = R.id.et_cyzsfz,click = false)
    private EditText et_cyzsfz;
    /***从业者电话*/
    @BindView(id = R.id.et_cyzdh,click = false)
    private EditText et_cyzdh;
    /***从业者照片*/
    @BindView(id = R.id.iv_cyz,click = false)
    private ImageView iv_cyz;
    private  String path1;
    private ZAJCCYZModle cyzInfo;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.aty_zacyz_watch);
        getActionBar().setIcon(R.drawable.actionbar_back_icon_normal);
        getActionBar().setTitle(getString(R.string.zacyz_watch));
        getActionBar().setHomeButtonEnabled(true);
        /***注解式绑定控件**/
        AnnotateManager.initBindView(this);
        cyzInfo=(ZAJCCYZModle)getIntent().getSerializableExtra(GeneralUtils.Info);
        initData();
    }

    private void initData() {
        if (cyzInfo!=null){
            et_cyzxm.setText(cyzInfo.getCYZXM());
            et_cyzsfz.setText(cyzInfo.getCYZSFZ());
            et_cyzdh.setText(cyzInfo.getCYZDH());
            if (cyzInfo.getIV_CYZQSZ()!=null){
                iv_cyz.setImageBitmap(ImageUtils.decodeFile(cyzInfo.getIV_CYZQSZ()));
            }
        }
    }




    @Override
    public int getCYZID() {
        return 0;
    }

    @Override
    public String getCYZXM() {
        return et_cyzxm.getText().toString().trim();
    }

    @Override
    public String getCYZSFZ() {
        return et_cyzsfz.getText().toString().trim();
    }

    @Override
    public String getCYZDH() {
        return et_cyzdh.getText().toString().trim();
    }

    @Override
    public String getIV_CYZQSZ() {
        return path1;
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