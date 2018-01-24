package cn.net.xinyi.xmjt.activity.Collection.Camera;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.KeyEvent;
import android.view.MenuItem;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.loopj.android.http.AsyncHttpResponseHandler;

import org.apache.http.Header;

import cn.net.xinyi.xmjt.R;
import cn.net.xinyi.xmjt.api.ApiResource;
import cn.net.xinyi.xmjt.config.BaseActivity;
import cn.net.xinyi.xmjt.utils.AnnotateManager;
import cn.net.xinyi.xmjt.utils.BaseUtil;
import cn.net.xinyi.xmjt.utils.BindView;
import cn.net.xinyi.xmjt.utils.GeneralUtils;


/**
 * Created by hao.zhou on 2015/8/27.
 */
public class RankTotalActivity extends BaseActivity  {
    public static final String TAG = RankTotalActivity.class.getName();
    private ProgressDialog mProgressDialog = null;
    private int  dhc_jks=0,tghc_jks=0,btghc_jks=0,dhc_sxt=0,tghc_sxt=0,btghc_sxt=0,
            zctj_sxt=0,zctj_jks=0;
    private JSONArray rankArray;
    @BindView(id = R.id.tv_upload_jks)
    TextView tv_upload_jks;
    @BindView(id = R.id.tv_check_yes_jks)
    TextView tv_check_yes_jks;
    @BindView(id = R.id.tv_check_no_jks)
    TextView tv_check_no_jks;
    @BindView(id = R.id.tv_upload_sxt)
    TextView tv_upload_sxt;
    @BindView(id = R.id.tv_check_yes_sxt)
    TextView tv_check_yes_sxt;
    @BindView(id = R.id.tv_check_no_sxt)
    TextView tv_check_no_sxt;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.rank_total);
        AnnotateManager.initBindView(this);
        mProgressDialog = new ProgressDialog(this);
        getRank();

        getActionBar().setIcon(R.drawable.actionbar_back_icon_normal);
        getActionBar().setTitle("汇总统计");
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

    private void getRank(){
        Message msg = new Message();
        msg.what = 0;
        mHandle.handleMessage(msg);

        ApiResource.getCountAll(new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int i, Header[] headers, byte[] bytes) {
                try {
                    String result = new String(bytes);
                    rankArray = JSON.parseArray(result);
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
                    mProgressDialog.setMessage("正在联网获取排行榜数据");
                    mProgressDialog.setIndeterminate(true);
                    mProgressDialog.setCancelable(false);
                    mProgressDialog.show();
                    break;
                case 1:
                    if (mProgressDialog != null && mProgressDialog.isShowing())
                        mProgressDialog.cancel();
                    initData();
                    break;
                case 2:
                    if (mProgressDialog != null && mProgressDialog.isShowing())
                        mProgressDialog.cancel();
                    BaseUtil.showDialog("获取数据失败", RankTotalActivity.this);
                    break;
                default:
                    break;
            }
        }
    };

    private void initData() {
        // [{"shzt":"0","tjlb":"jks","total":21},{"shzt":"1","tjlb":"jks","total":4},{"shzt":"2","tjlb":"jks","total":9},
        // {"shzt":"0","tjlb":"sxt","total":23},{"shzt":"1","tjlb":"sxt","total":3},{"shzt":"2","tjlb":"sxt","total":9}]
        if (rankArray.size()!=0){
            for (int i=0;i<rankArray.size();i++){
                //待核查监控室
                if (((JSONObject)rankArray.get(i)).getString(GeneralUtils.SHZT).equals("0")&&
                        ((JSONObject)rankArray.get(i)).getString(GeneralUtils.TJLB).equals(GeneralUtils.JKS)){
                    dhc_jks=Integer.parseInt(((JSONObject) rankArray.get(i)).getString(GeneralUtils.TOTAL));
                    //通过监控室数量
                }else if (((JSONObject)rankArray.get(i)).getString(GeneralUtils.SHZT).equals("1")&&
                        ((JSONObject)rankArray.get(i)).getString(GeneralUtils.TJLB).equals(GeneralUtils.JKS)) {
                    tghc_jks=Integer.parseInt(((JSONObject) rankArray.get(i)).getString(GeneralUtils.TOTAL));
                    //不通过监控室数量
                }else if (((JSONObject)rankArray.get(i)).getString(GeneralUtils.SHZT).equals("2")&&
                        ((JSONObject)rankArray.get(i)).getString(GeneralUtils.TJLB).equals(GeneralUtils.JKS)){
                    btghc_jks=Integer.parseInt(((JSONObject) rankArray.get(i)).getString(GeneralUtils.TOTAL));
                }else if (((JSONObject)rankArray.get(i)).getString(GeneralUtils.SHZT).equals("3")&&
                        ((JSONObject)rankArray.get(i)).getString(GeneralUtils.TJLB).equals(GeneralUtils.JKS)){
                    zctj_jks=Integer.parseInt(((JSONObject) rankArray.get(i)).getString(GeneralUtils.TOTAL));
                    //待核查摄像头数量
                } else if (((JSONObject)rankArray.get(i)).getString(GeneralUtils.SHZT).equals("0")&&
                        ((JSONObject)rankArray.get(i)).getString(GeneralUtils.TJLB).equals(GeneralUtils.SXT)){
                    dhc_sxt=Integer.parseInt(((JSONObject) rankArray.get(i)).getString(GeneralUtils.TOTAL));
                    //通过摄像头数量
                }else if (((JSONObject)rankArray.get(i)).getString(GeneralUtils.SHZT).equals("1")&&
                        ((JSONObject)rankArray.get(i)).getString(GeneralUtils.TJLB).equals(GeneralUtils.SXT)) {
                    tghc_sxt=Integer.parseInt(((JSONObject) rankArray.get(i)).getString(GeneralUtils.TOTAL));
                    //不通过摄像头数量
                }else if (((JSONObject)rankArray.get(i)).getString(GeneralUtils.SHZT).equals("2")&&
                        ((JSONObject)rankArray.get(i)).getString(GeneralUtils.TJLB).equals(GeneralUtils.SXT)){
                    btghc_sxt=Integer.parseInt(((JSONObject) rankArray.get(i)).getString(GeneralUtils.TOTAL));
                }else if (((JSONObject)rankArray.get(i)).getString(GeneralUtils.SHZT).equals("3")&&
                        ((JSONObject)rankArray.get(i)).getString(GeneralUtils.TJLB).equals(GeneralUtils.SXT)){
                    zctj_sxt=Integer.parseInt(((JSONObject) rankArray.get(i)).getString(GeneralUtils.TOTAL));
                }
            }
            tv_upload_jks.setText(String.valueOf(dhc_jks+tghc_jks+btghc_jks+zctj_jks));
            tv_check_yes_jks.setText(String.valueOf(tghc_jks));
            tv_check_no_jks.setText(String.valueOf(btghc_jks));
            tv_upload_sxt.setText(String.valueOf(dhc_sxt+tghc_sxt+btghc_sxt+zctj_sxt));
            tv_check_yes_sxt.setText(String.valueOf(tghc_sxt));
            tv_check_no_sxt.setText(String.valueOf(btghc_sxt));
        }
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