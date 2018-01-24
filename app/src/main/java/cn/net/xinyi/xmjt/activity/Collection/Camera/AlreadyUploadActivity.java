package cn.net.xinyi.xmjt.activity.Collection.Camera;


import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;
import com.loopj.android.http.AsyncHttpResponseHandler;

import org.apache.http.Header;

import java.util.ArrayList;
import java.util.List;

import cn.net.xinyi.xmjt.R;
import cn.net.xinyi.xmjt.activity.Collection.Camera.Adapter.AlreadyUploadAdapter;
import cn.net.xinyi.xmjt.activity.Main.ChooseWangGeActivity;
import cn.net.xinyi.xmjt.api.ApiResource;
import cn.net.xinyi.xmjt.config.AppContext;
import cn.net.xinyi.xmjt.config.BaseActivity2;
import cn.net.xinyi.xmjt.model.JKSInfoModle;
import cn.net.xinyi.xmjt.utils.BaseUtil;
import cn.net.xinyi.xmjt.utils.GeneralUtils;

/**
 * Created by hao.zhou on 2015/8/27.
 * 采集摄像头 获取已上传监控室信息
 */
public class AlreadyUploadActivity extends BaseActivity2 implements View.OnClickListener {
    public static final String TAG = AlreadyUploadActivity.class.getName();
    private ListView mListView;
    private AlreadyUploadAdapter mAdapter;
    private ProgressDialog mProgressDialog = null;
    private TextView tv_choose_jd,tv_choose_sq,tv_choose_wg;
    private String sq_bm;
    private List<JKSInfoModle> jksInfo;
    private TextView empty_data;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.cj_upload_activity);

        setViews();//初始化组件
        getJKSList();//获得已上传监控室列表
    }


    private void setViews() {
        mProgressDialog = new ProgressDialog(this);
        //选择街道
        tv_choose_jd = (TextView) findViewById(R.id.tv_choose_jd);
        tv_choose_jd.setOnClickListener(this);
        //选择社区
        tv_choose_sq = (TextView) findViewById(R.id.tv_choose_sq);
        tv_choose_sq.setOnClickListener(this);
        //选择网格
        tv_choose_wg = (TextView) findViewById(R.id.tv_choose_wg);
        tv_choose_wg.setOnClickListener(this);
        //设置街道、社区 默认值
        tv_choose_jd.setText(AppContext.instance.getLoginInfo().getSsjd());
        tv_choose_sq.setText(AppContext.instance.getLoginInfo().getSssq());
        /**s 为获取所属网格的默认值，如果账号分管多个网格，划分第一个网格信息*/
        String s=AppContext.instance.getLoginInfo().getSswg();
        if (!s.isEmpty()){
            tv_choose_wg.setText(s.substring(0, s.indexOf(",")));
        }
        empty_data = (TextView)findViewById(R.id.empty_data);//textview为空
        mListView = (ListView) findViewById(R.id.listView3);//listview
        /***根据社区名称寻找社区编码 以此来匹配寻找下属网格！*/

        sq_bm=zdUtils.getZdlbAndZdzToZdbm(GeneralUtils.ZZJG_SQ,tv_choose_sq.getText().toString().trim());
        /***点击已上传监控室信息 封装bundle 传递到SXTInfoActivity*/
        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent();
                Bundle bundle = new Bundle();
                bundle.putSerializable(GeneralUtils.RegistWangGe, jksInfo.get(position));
                intent.putExtras(bundle);
                setResult(RESULT_OK, intent);
                AlreadyUploadActivity.this.finish();
            }
        });
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            //选择网格数据
            case R.id.tv_choose_wg:
                //判断上级社区是否为空
                if (tv_choose_sq.getText().toString().trim().isEmpty()) {
                    BaseUtil.showDialog("所属社区不能为空", AlreadyUploadActivity.this);
                } else if (sq_bm !=null&&!tv_choose_sq.getText().toString().trim().isEmpty()) {
                    Intent intent = new Intent(AlreadyUploadActivity.this, ChooseWangGeActivity.class);
                    intent.putExtra(GeneralUtils.WangGe, sq_bm);
                    intent.setFlags(GeneralUtils.CJUploadActivity);
                    startActivityForResult(intent, 101);
                }
                break;

            //选择所属社区
            case R.id.tv_choose_sq:
                Toast.makeText(AlreadyUploadActivity.this, "只能选择您当前所在社区的网格！",Toast.LENGTH_LONG).show();
                break;

            //选择所属街道
            case R.id.tv_choose_jd:
                Toast.makeText(AlreadyUploadActivity.this, "只能选择您当前所在社区的网格！",Toast.LENGTH_LONG).show();
                break;
        }
    }
    /*** 选择网格反馈的 */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        //选择网格名称
        if (resultCode==RESULT_OK&&data!=null){
            ArrayList<String> data1 = data.getStringArrayListExtra(GeneralUtils.RegistWangGe);
            String[] ary=data1.toArray(new String[data1.size()]);
            StringBuffer sb = new StringBuffer();
            for(int i = 0; i < ary.length; i++){
                sb. append(ary[i]);
            }
            tv_choose_wg.setText(sb.toString());
            getJKSList();//获得已上传监控室列表
        }

    }

    /***联网获得已经上传的监控室信息*/
    private void getJKSList(){
        Message msg = new Message();
        msg.what = 0;
        mHandle.handleMessage(msg);

        ApiResource.WGJKSList(tv_choose_wg.getText().toString().trim(), new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int i, Header[] headers, byte[] bytes) {
                try {
                    String result = new String(bytes);
                    try {
                        jksInfo = JSON.parseObject(result, new TypeReference<List<JKSInfoModle>>() {
                        });
                        Message msg = new Message();
                        msg.what = 1;
                        mHandle.sendMessage(msg);
                    } catch (Exception e) {
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
                    mProgressDialog.setMessage("正在联网获取排行榜数据");
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
                    BaseUtil.showDialog("获取数据失败", AlreadyUploadActivity.this);
                    break;
                default:
                    break;
            }
        }
    };

    private void initListView(){
        if (jksInfo.size()==0){//数据为空，显示
            empty_data.setVisibility(View.VISIBLE);
            mListView.setVisibility(View.GONE);
        }else {
            empty_data.setVisibility(View.GONE);
            mListView.setVisibility(View.VISIBLE);
            mAdapter = new AlreadyUploadAdapter(mListView,jksInfo,R.layout.cj_upload_adapter,this);
            mListView.setAdapter(mAdapter);
        }
    }

    @Override
    public boolean enableBackUpBtn() {
        return true;
    }


    @Override
    public String getAtyTitle() {
        return getString(R.string.upload_jks_success);
    }
}