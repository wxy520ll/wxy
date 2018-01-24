package cn.net.xinyi.xmjt.activity.Main.Setting;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import cn.net.xinyi.xmjt.R;
import cn.net.xinyi.xmjt.config.AppContext;
import cn.net.xinyi.xmjt.config.BaseActivity2;


/**
 * Created by hao.zhou on 2015/10/16.
 */
public class UserInfoActivity extends BaseActivity2 {
    private TextView tv_phone,tv_username,tv_pcs,tv_jws,tv_jd,tv_wg,tv_sq,tv_sfbs,tv_jhnumber;
    private Button btn_modeify;
    public static UserInfoActivity instance;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.register_editor);

        getActionBar().setIcon(R.drawable.actionbar_back_icon_normal);
        getActionBar().setTitle("个人信息");
        getActionBar().setHomeButtonEnabled(true);
        instance = this;
       // 初始化布局
        setViews();
    }


    private void setViews() {
        //电话-登录用户名
        tv_phone=(TextView)findViewById(R.id.tv_phone);
        //名字
        tv_username=(TextView)findViewById(R.id.tv_username);
        //派出所
        tv_pcs =(TextView)findViewById(R.id.tv_pcs);
        //警务室
        tv_jws =(TextView) findViewById(R.id.tv_jws);
        //身份编号
        tv_sfbs=(TextView)findViewById(R.id.tv_sfbs);
        //警号
        tv_jhnumber =(TextView)findViewById(R.id.tv_jhnumber);
        //街道
        tv_jd=(TextView)findViewById(R.id.tv_jd);
         //社区
        tv_sq=(TextView)findViewById(R.id.tv_sq);
         //网格
        tv_wg=(TextView)findViewById(R.id.tv_wg);
        btn_modeify=(Button)findViewById(R.id.btn_modeify);
        btn_modeify.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(UserInfoActivity.this,ModifyUserInfoActivity.class);
                startActivity(intent);
            }
        });
       //获得数据
        setData();
    }

    private void setData() {
        tv_phone.setText(AppContext.instance.getLoginInfo().getUsername());
        tv_username.setText(AppContext.instance.getLoginInfo().getName());
        tv_pcs.setText(AppContext.instance.getLoginInfo().getPcs());
        tv_jws.setText(AppContext.instance.getLoginInfo().getJws());
        tv_sfbs.setText(AppContext.instance.getLoginInfo().getAccounttype());
        tv_jhnumber.setText(AppContext.instance.getLoginInfo().getPoliceno());
        tv_jd.setText(AppContext.instance.getLoginInfo().getSsjd());
        tv_sq.setText(AppContext.instance.getLoginInfo().getSssq());
        tv_wg.setText(AppContext.instance.getLoginInfo().getSswg());
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