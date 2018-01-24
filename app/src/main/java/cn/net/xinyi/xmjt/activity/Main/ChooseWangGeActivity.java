package cn.net.xinyi.xmjt.activity.Main;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ListView;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.loopj.android.http.AsyncHttpResponseHandler;

import org.apache.http.Header;

import java.util.ArrayList;

import cn.net.xinyi.xmjt.R;
import cn.net.xinyi.xmjt.api.ApiResource;
import cn.net.xinyi.xmjt.config.BaseActivity;
import cn.net.xinyi.xmjt.utils.BaseUtil;
import cn.net.xinyi.xmjt.utils.GeneralUtils;

/**
 * Created by hao.zhou on 2015/10/26.
 */
public class ChooseWangGeActivity extends BaseActivity {
    private Button btn_sure;
    private ListView lv_wangge;
    private GridView gv_wangge;
    private WangGeAdapter mAdapter;
    private ArrayList<String> get_WG;
    private ArrayList<String> WG;
    private JSONArray rankArray;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.regist_wangge);

        getActionBar().setIcon(R.drawable.actionbar_back_icon_normal);
        getActionBar().setTitle("所属网格");
        getActionBar().setHomeButtonEnabled(true);


        //确定修改
        btn_sure=(Button)findViewById(R.id.btn_sure);
        // lv_wangge=(ListView)findViewById(R.id.lv_wangge);
        //GridView 布局  一行两列
        gv_wangge=(GridView)findViewById(R.id.gv_wangge);
        getWangGeDate();

        //确定 将返回到信息注册页面  RegisterInfoActivity
        btn_sure.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                WG = new ArrayList<String>();
                //将已经选择的 添加到新的集合
                for (int i = 0; i < get_WG.size(); i++) {
                    if (WangGeAdapter.getIsSelected().get(i).equals(true)) {
                        WG.add(get_WG.get(i));
                    }
                }

                if (WG.size() != 0) {
                    if (getIntent().getFlags() == GeneralUtils.CJUploadActivity){
                        if (WG.size()!=1){
                            BaseUtil.showDialog("只能选择一项",ChooseWangGeActivity.this);
                        }else {
                            Intent intent = new Intent();
                            intent.putStringArrayListExtra(GeneralUtils.RegistWangGe, WG);
                            setResult(RESULT_OK, intent);
                            ChooseWangGeActivity.this.finish();
                        }
                    }else {
                        Intent intent = new Intent();
                        intent.putStringArrayListExtra(GeneralUtils.RegistWangGe, WG);
                        setResult(RESULT_OK, intent);
                        ChooseWangGeActivity.this.finish();
                    }
                }
            }
        });
    }

    private void getWangGeDate() {
        mProgressDialog = new ProgressDialog(this);
        Message msg = new Message();
        msg.what = 0;
        mHandle.handleMessage(msg);
        ApiResource.getWangGeList(getIntent().getStringExtra(GeneralUtils.WangGe), new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int i, Header[] headers, byte[] bytes) {
                try {
                    if (i == 200 && bytes != null) {
                        String result = new String(bytes);
                        rankArray = JSON.parseArray(result);

                        //解析数据添加到集合
                        if (rankArray.size()!=0){
                            get_WG=new ArrayList<String>();
                            for (int k=0;k<rankArray.size();k++){
                                get_WG.add(((JSONObject)rankArray.get(k)).getString("ZDZ"));
                            }
                        }

                        Message msg = new Message();
                        msg.what = 1;
                        mHandle.sendMessage(msg);
                    } else {
                        onFailure(i, headers, bytes, null);
                    }

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


    private ProgressDialog mProgressDialog;
    private Handler mHandle = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 0:
                    mProgressDialog.setMessage("正在获取网格信息...");
                    mProgressDialog.setIndeterminate(true);
                    mProgressDialog.setCancelable(false);
                    mProgressDialog.show();
                    break;
                case 1:
                    if (mProgressDialog != null && mProgressDialog.isShowing())
                        mProgressDialog.cancel();
                    //初始化 网格列表
                    initListViewData();
                    break;
                case 2:
                    if (mProgressDialog != null && mProgressDialog.isShowing())
                        mProgressDialog.cancel();
                    BaseUtil.showDialog("获取数据失败", ChooseWangGeActivity.this);
                    //  dataNull();
                    break;
                case 3:
                    if (mProgressDialog != null && mProgressDialog.isShowing()){
                        mProgressDialog.cancel();
                    }
                    break;
                default:
                    break;
            }
        }
    };

    private void initListViewData() {
        btn_sure.setVisibility(View.VISIBLE);
        mAdapter=new WangGeAdapter(this, get_WG);
        gv_wangge.setAdapter(mAdapter);
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