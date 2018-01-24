package cn.net.xinyi.xmjt.activity.Commity;

import android.annotation.TargetApi;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTransaction;
import android.text.TextUtils;
import android.view.MenuItem;

import com.umeng.comm.core.beans.Topic;
import com.umeng.simplify.ui.fragments.TopicFeedFragment;
import com.umeng.simplify.ui.util.TopicUtil;

import cn.net.xinyi.xmjt.R;
import cn.net.xinyi.xmjt.config.AppContext;

/**
 * Created by studyjun on 2016/6/27.
 */
public class TopicFeedListAty extends FragmentActivity {

    @TargetApi(Build.VERSION_CODES.ICE_CREAM_SANDWICH)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.topic_feed_list);
        String department = getIntent().getStringExtra("data");
        int type = getIntent().getIntExtra("type",0);
        if (TextUtils.isEmpty(department)){
            department =AppContext.instance().getLoginInfo().getPcs();
        }


        Topic departmentTopic = new Topic(); //内部交流
        if (type==0){
            departmentTopic.id = TopicUtil.getDepartmentTopicInName(getApplicationContext(),department);
            getActionBar().setTitle("内部交流");
        } else {
            departmentTopic.id = TopicUtil.getDepartmentAlarmTopicInName(getApplicationContext(),department);
            getActionBar().setTitle("内部警情");
        }
        departmentTopic.name = AppContext.instance().getLoginInfo().getPcs();
        Fragment fragment = TopicFeedFragment.newTopicFeedFrmg(departmentTopic,true);

        getActionBar().setIcon(R.drawable.actionbar_back_icon_normal);
        getActionBar().setHomeButtonEnabled(true);


        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        ft.replace(R.id.topic_feed_list,fragment);
        ft.commit();


    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (android.R.id.home==item.getItemId()){
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }



}
