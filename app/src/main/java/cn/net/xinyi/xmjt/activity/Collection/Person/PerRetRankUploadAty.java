package cn.net.xinyi.xmjt.activity.Collection.Person;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.KeyEvent;
import android.view.MenuItem;
import android.widget.ListView;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.loopj.android.http.AsyncHttpResponseHandler;

import org.apache.http.Header;

import cn.net.xinyi.xmjt.R;
import cn.net.xinyi.xmjt.activity.Collection.Camera.Adapter.RankUploadUserAdapter;
import cn.net.xinyi.xmjt.api.ApiResource;
import cn.net.xinyi.xmjt.config.AppContext;
import cn.net.xinyi.xmjt.config.BaseActivity;
import cn.net.xinyi.xmjt.model.UserInfo;
import cn.net.xinyi.xmjt.utils.BaseUtil;

/**
 * Created by mazhongwang on 15/5/8.
 */
public class PerRetRankUploadAty extends BaseActivity {
    private ListView mListView;
    private RankUploadUserAdapter mAdapter;
    private JSONArray mArray;
    private ProgressDialog mProgressDialog = null;
    private int flags=2;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.plate_upload_user_count);
        initResource();
        getCount();
        getActionBar().setIcon(R.drawable.actionbar_back_icon_normal);
        getActionBar().setTitle("用户每日上传统计");
        getActionBar().setHomeButtonEnabled(true);
    }

    private void initResource() {
        mProgressDialog = new ProgressDialog(this);
        mListView = (ListView) findViewById(R.id.userCountlistView);

    }

    private void initListView() {
        mAdapter = new RankUploadUserAdapter(this, mArray,flags);
        mListView.setAdapter(mAdapter);
    }

    private void getCount() {
        Message msg = new Message();
        msg.what = 0;
        mHandle.handleMessage(msg);

        String username="";
        //获取当前登录用户
        UserInfo user = AppContext.instance().getLoginInfo();
        if (user != null && user.getId() > 0) {
            username=user.getUsername();
        }

        ApiResource.PerUserCount(username, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int i, Header[] headers, byte[] bytes) {
                try {
                    String result = new String(bytes);
                    mArray = JSON.parseArray(result);
                    Message msg = new Message();
                    msg.what = 1;
                    mHandle.sendMessage(msg);

                } catch (Exception e) {
                    onFailure(i, headers, bytes, null);
                }
            }

            @Override
            public void onFailure(int i, Header[] headers, byte[] bytes, Throwable throwable) {
                Message msg = new Message();
                msg.what = 2;
                mHandle.sendMessage(msg);
            }
        });
    }

    private Handler mHandle = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 0:
                    mProgressDialog.setMessage("正在获取统计数据");
                    mProgressDialog.setIndeterminate(true);
                    mProgressDialog.setCancelable(false);
                    mProgressDialog.show();
                    break;
                case 1:
                    if (mProgressDialog != null && mProgressDialog.isShowing())
                        mProgressDialog.cancel();
                    initListView();
                    break;
                case 2:
                    if (mProgressDialog != null && mProgressDialog.isShowing())
                        mProgressDialog.cancel();
                    BaseUtil.showDialog("获取数据失败", PerRetRankUploadAty.this);
                    break;
                default:
                    break;
            }
        }
    };

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


