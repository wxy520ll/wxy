package cn.net.xinyi.xmjt.activity.Collection.Person;

import android.app.ActivityGroup;
import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.MenuItem;
import android.widget.FrameLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import cn.net.xinyi.xmjt.R;
import cn.net.xinyi.xmjt.utils.BaseUtil;

/**
 * Created by hao.zhou on 2016/3/2.
 */
public class PerReturnMangeMainAty extends ActivityGroup {
    private FrameLayout content;
    private RadioGroup tabs;
    private Intent intent;
    public static PerReturnMangeMainAty instance;
    private RadioButton rb_local;
    private RadioButton rb_upload;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.aty_manage_info);
        getActionBar().setIcon(R.drawable.actionbar_back_icon_normal);
        getActionBar().setTitle(R.string.PER_RETURN_MANAGE);
        getActionBar().setHomeButtonEnabled(true);
        instance=this;
        initView();
    }

    private void initView() {
        content = (FrameLayout) findViewById(R.id.tabcontent);
        tabs = (RadioGroup) findViewById(R.id.tabs);
        rb_local=(RadioButton)findViewById(R.id.rb_local);
        rb_upload=(RadioButton)findViewById(R.id.rb_upload);

        tabs.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup arg0, int arg1) {
                // TODO Auto-generated method stub
                switch (arg1) {
                    case R.id.rb_local:// 本地缓存
                        rb_local.setTextColor(getResources().getColor(R.color.blue));
                        rb_upload.setTextColor(getResources().getColor(R.color.black));
                        intent = new Intent(PerReturnMangeMainAty.this, PerReturnLocalAty.class);
                        setTab("rb_local", intent);
                        break;

                    case R.id.rb_upload:// 已上传
                        BaseUtil.showDialog("暂时不支持查看！", PerReturnMangeMainAty.this);
//                        rb_upload.setTextColor(getResources().getColor(R.color.blue));
//                        rb_local.setTextColor(getResources().getColor(R.color.black));
//                        intent = new Intent(PerReturnMangeMainAty.this,PerReturnUploadAty.class);
//                        setTab("rb_upload", intent);
                        break;

                }
            }
        });
        tabs.check(R.id.rb_local);
    }


    private void setTab(String id, Intent intent) {
        content.removeAllViews();
        content.addView(getLocalActivityManager().startActivity(id,
                intent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP))
                .getDecorView());
    }



    /***activity退出*/
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
        if (event.getAction() == KeyEvent.ACTION_DOWN
                && event.getKeyCode() == KeyEvent.KEYCODE_BACK) {
            this.finish();
        }
        return super.dispatchKeyEvent(event);
    }

}