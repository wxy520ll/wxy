package cn.net.xinyi.xmjt.activity.Collection.Person;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.ListView;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.TypeReference;
import com.loopj.android.http.AsyncHttpResponseHandler;

import org.apache.http.Header;

import java.util.List;

import cn.net.xinyi.xmjt.R;
import cn.net.xinyi.xmjt.api.ApiResource;
import cn.net.xinyi.xmjt.config.AppContext;
import cn.net.xinyi.xmjt.config.BaseActivity;
import cn.net.xinyi.xmjt.model.PerReturnModle;
import cn.net.xinyi.xmjt.utils.AnnotateManager;
import cn.net.xinyi.xmjt.utils.BaseUtil;
import cn.net.xinyi.xmjt.utils.BindView;
import cn.net.xinyi.xmjt.utils.GeneralUtils;

/**
 * Created by hao.zhou on 2016/3/2.
 */
public class PerReturnUploadAty extends BaseActivity  {
    /**listview**/
    @BindView(id = R.id.lv_upload)
    private ListView lv_upload;
    /**没有数据显示空布局**/
    @BindView(id = R.id.ll_empty_data)
    private LinearLayout ll_nodata;

    private ProgressDialog mProgressDialog = null;
    private List<PerReturnModle> Infos;
    private PerReturnLocalAdp adapter;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.aty_store_info_uoload);
        /***控件绑定**/
        AnnotateManager.initBindView(this);
        mProgressDialog = new ProgressDialog(this);
        getData();
        /***item 点击*/
        initClick();
    }


    //上传信息列表
    private void getData() {
        JSONObject jo = new JSONObject();
        jo.put("username", AppContext.instance.getLoginInfo().getUsername());
        String json = jo.toJSONString();

        Message msg = new Message();
        msg.what = 0;
        mHandle.handleMessage(msg);
        ApiResource.StoreListALL(json, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int i, Header[] headers, byte[] bytes) {
                try {
                    if (i == 200 && bytes != null) {
                        String result = new String(bytes);
                        Infos = JSON.parseObject(result, new TypeReference<List<PerReturnModle>>() {
                        });
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
    private Handler mHandle = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 0:
                    mProgressDialog.setMessage("正在获取您已上传的数据");
                    mProgressDialog.setIndeterminate(true);
                    mProgressDialog.setCancelable(false);
                    mProgressDialog.show();
                    break;
                case 1:
                    if (mProgressDialog != null && mProgressDialog.isShowing()){
                        mProgressDialog.cancel();
                    }
                    if (Infos.size()==0){
                        dataNull();
                    }else {
                        initListViewData();//列表；
                    }
                    break;
                case 2:
                    if (mProgressDialog != null && mProgressDialog.isShowing())
                        mProgressDialog.cancel();
                    BaseUtil.showDialog("获取数据失败", PerReturnUploadAty.this);
                    dataNull();
                    break;

                default:
                    break;
            }
        }
    };

    private void initListViewData() {
     //   adapter=new PerReturnLocalAdp(lv_upload,Infos,R.layout.aty_store_info_manage_item, this);
        lv_upload.setAdapter(adapter);
    }

    private void dataNull() {
        ll_nodata.setVisibility(View.VISIBLE);
        lv_upload.setVisibility(View.GONE);
    }

    private void initClick() {
        lv_upload.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(PerReturnUploadAty.this, PerReturnWatchAty.class);
                Bundle bundle = new Bundle();
                bundle.putSerializable(GeneralUtils.Info, Infos.get(position));
                intent.putExtras(bundle);
                startActivityForResult(intent, 1001);
            }
        });
    }


}