package cn.net.xinyi.xmjt.v527.presentation.home.cj.rycj;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;

import com.blankj.utilcode.util.ActivityUtils;
import com.xinyi_tech.comm.util.ToolbarUtils;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.net.xinyi.xmjt.R;
import cn.net.xinyi.xmjt.activity.Collection.Person.PerReturnAty;

public class RycjPreActivity extends AppCompatActivity {

    @BindView(R.id.tool_bar)
    Toolbar toolBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rycj_pre);
        ButterKnife.bind(this);
        ToolbarUtils.with(this, toolBar).setTitle("人员采集", true).setSupportBack(true).build();
    }

    @OnClick({R.id.rycj, R.id.llqt, R.id.lljsb})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.rycj:
                ActivityUtils.startActivity(this, PerReturnAty.class);
                break;
            case R.id.llqt:
                Intent intent = new Intent(this, RycjOtherActivity.class);
                intent.putExtra("title", "流浪乞讨人员");
                intent.putExtra("personType", 3);
                ActivityUtils.startActivity(intent);
                break;
            case R.id.lljsb:
                intent = new Intent(this, RycjOtherActivity.class);
                intent.putExtra("title", "流浪精神病人员");
                intent.putExtra("personType", 2);
                ActivityUtils.startActivity(intent);
                break;
        }
    }
}
