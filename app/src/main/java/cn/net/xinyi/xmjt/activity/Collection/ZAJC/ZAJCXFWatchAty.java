package cn.net.xinyi.xmjt.activity.Collection.ZAJC;

import android.os.Bundle;
import android.view.KeyEvent;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import cn.net.xinyi.xmjt.R;
import cn.net.xinyi.xmjt.config.BaseActivity;
import cn.net.xinyi.xmjt.model.View.IZAXFView;
import cn.net.xinyi.xmjt.model.ZAJCXFModle;
import cn.net.xinyi.xmjt.utils.AnnotateManager;
import cn.net.xinyi.xmjt.utils.BindView;
import cn.net.xinyi.xmjt.utils.ImageUtils;

/**
 * Created by hao.zhou on 2016/3/19.
 */
public class ZAJCXFWatchAty extends BaseActivity implements IZAXFView {
    /**灭火器数量**/
    @BindView(id = R.id.et_mhqsl,click = false)
    private EditText et_mhqsl;
    /**灭火器正常数**/
    @BindView(id = R.id.et_mhqzcs,click = false)
    private EditText et_mhqzcs;
    /**到期日期**/
    @BindView(id = R.id.tv_dqrq,click = false)
    private TextView tv_dqrq;
    /**消防全景图**/
    @BindView(id = R.id.iv_xfqjt,click = false)
    private ImageView iv_xfqjt;
    private String path1;
    private ZAJCXFModle jcInfo;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.aty_zaxfxx_watch);
        /***注解式绑定控件**/
        AnnotateManager.initBindView(this);
        getActionBar().setIcon(R.drawable.actionbar_back_icon_normal);
        getActionBar().setTitle(getString(R.string.zaxf_watch));
        getActionBar().setHomeButtonEnabled(true);
        jcInfo = (ZAJCXFModle) getIntent().getSerializableExtra("xfInfo");
        initData();
    }

    private void initData() {
        if (jcInfo!=null){
            et_mhqsl.setText(jcInfo.getMHQSL());
            et_mhqzcs.setText(jcInfo.getZCS());
            tv_dqrq.setText(jcInfo.getDQRQ());
            if (jcInfo.getIV_XFQJT()!=null){
                iv_xfqjt.setImageBitmap(ImageUtils.decodeFile(jcInfo.getIV_XFQJT()) );
            }
        }
    }


    @Override
    public int getXFID() {
        return 0;
    }

    @Override
    public String getMHQSL() {
        return et_mhqsl.getText().toString();
    }

    @Override
    public String getZCS() {
        return et_mhqzcs.getText().toString().trim();
    }

    @Override
    public String getDQRQ() {
        return tv_dqrq.getText().toString();
    }

    @Override
    public String getIV_XFQJT() {
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