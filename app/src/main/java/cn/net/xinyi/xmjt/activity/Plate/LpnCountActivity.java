package cn.net.xinyi.xmjt.activity.Plate;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.MenuItem;
import android.widget.ListView;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.loopj.android.http.AsyncHttpResponseHandler;

import org.apache.http.Header;

import cn.net.xinyi.xmjt.R;
import cn.net.xinyi.xmjt.activity.Plate.Adapter.LpnCountAdapter;
import cn.net.xinyi.xmjt.api.ApiResource;
import cn.net.xinyi.xmjt.config.BaseActivity;
import cn.net.xinyi.xmjt.utils.BaseUtil;

/**
 * Created by hao.zhou on 2015/8/29.
 */
public class LpnCountActivity extends BaseActivity {
    public static final String TAG = LpnCountActivity.class.getName();
    private TextView now_uploaded_number,total_number,total_per;
    private LpnCountAdapter mAdapter;
    private ListView mListView;
    private JSONArray mJsonArray;
    private ProgressDialog mProgressDialog = null;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.suboffice);
        setViews();
        getData();
        getActionBar().setIcon(R.drawable.actionbar_back_icon_normal);
        getActionBar().setTitle("车牌汇总统计");
        getActionBar().setHomeButtonEnabled(true);
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

    private void setViews() {
        mProgressDialog = new ProgressDialog(this);
        now_uploaded_number=(TextView) findViewById(R.id.now_uploaded_number);
        total_number=(TextView)findViewById(R.id.total_number);
        total_per=(TextView)findViewById(R.id.total_per);
        mListView=(ListView)findViewById(R.id.listView4);
    }

    private void getData(){
//        final String kssj = "2015-08-26";
//        final String jssj = "2019-08-26";

        JSONObject jo = new JSONObject();
//        jo.put("kssj",kssj);
//        jo.put("jssj",jssj);
        String json = jo.toJSONString();

        Message msg = new Message();
        msg.what = 0;
        mHandle.handleMessage(msg);

        ApiResource.getOrganRank(json, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int i, Header[] headers, byte[] bytes) {
                try {
                    String result = new String(bytes);
                    mJsonArray = JSON.parseArray(result);
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
                    mProgressDialog.setMessage("正在联网获取数据");
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
                    BaseUtil.showDialog("获取数据失败", LpnCountActivity.this);
                    break;
                default:
                    break;
            }
        }
    };

    private void initListView(){
        int upload_total =0;
        int total = 1885000;

        for(int i=0;i<mJsonArray.size();i++){
            String count = ((JSONObject)mJsonArray.get(i)).getString("TOTAL");
            upload_total += Integer.parseInt(count);
        }

        total_number.setText(String.valueOf(total));
        now_uploaded_number.setText(String.valueOf(upload_total));
        total_per.setText(upload_total*100/total+"%");

        mAdapter = new LpnCountAdapter(this, mJsonArray);
        mListView.setAdapter(mAdapter);
    }
}