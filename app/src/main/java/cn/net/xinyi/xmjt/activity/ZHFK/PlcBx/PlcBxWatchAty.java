package cn.net.xinyi.xmjt.activity.ZHFK.PlcBx;

import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONException;
import com.alibaba.fastjson.JSONObject;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.nostra13.universalimageloader.core.ImageLoader;

import org.apache.http.Header;

import java.util.List;

import cn.net.xinyi.xmjt.R;
import cn.net.xinyi.xmjt.api.ApiHttpClient;
import cn.net.xinyi.xmjt.api.ApiResource;
import cn.net.xinyi.xmjt.config.BaseActivity2;
import cn.net.xinyi.xmjt.model.PoliceBoxModle;
import cn.net.xinyi.xmjt.utils.AnnotateManager;
import cn.net.xinyi.xmjt.utils.BindView;

/**
 * Created by studyjun on 2016/4/20.
 */
public class PlcBxWatchAty extends BaseActivity2 {


    //岗亭类型
    @BindView(id = R.id.tv_type)
    TextView mPlcBxType;

    //街道
    @BindView(id = R.id.et_street)
    TextView mStreet;

    //统一编号
    @BindView(id = R.id.et_unionNo)
    TextView mUnionNo;


    //建设需求
    @BindView(id = R.id.et_demand)
    TextView mDemand;


    //领头部门
    @BindView(id = R.id.et_initiator)
    TextView mInitiator;


    //所属派出所
    @BindView(id = R.id.et_police_station)
    TextView mPlcSttn;


    //完成时间
    @BindView(id = R.id.et_finish_time)
    TextView mFinishTime;


    //值守单位
    @BindView(id = R.id.et_watchOver_department )
    TextView watchOverDprtmt;

    //联系人
    @BindView(id = R.id.et_linkman)
    TextView mLinkMan;

    //手机号码
    @BindView(id = R.id.et_phoneno)
    TextView mPhoneNo;

    //手持终端类型
    @BindView(id = R.id.et_interphone_type)
    TextView mInterphoneType;


    //勤务模式
    @BindView(id = R.id.et_worktime)
    TextView mWorkTime;

    //手持终端号码
    @BindView(id = R.id.et_interphoneno)
    TextView mInterPhoneNo;


    //地址
    @BindView(id = R.id.tv_zb)
    TextView mAddress;

    //经纬度
    @BindView(id = R.id.et_latLngt)
    TextView mTvLatLngt;


    //照片1
    @BindView(id = R.id.iv_zp1)
    ImageView iv_zp1;

    //照片2
    @BindView(id = R.id.iv_zp2)
    ImageView iv_zp2;

    private List<PoliceBoxModle> list;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.aty_plc_bx_watch);
        AnnotateManager.initBindView(this);
        showLoadding();
        getDate();
    }

    private void getDate() {
        JSONObject jo=new JSONObject();
        jo.put("ID",getIntent().getStringExtra("data"));
        String json=jo.toJSONString();
        ApiResource.getGtList(json, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int i, Header[] headers, byte[] bytes) {
                String result = new String(bytes);
                try {
                    list = JSON.parseArray(result, PoliceBoxModle.class);
                    if (list != null) {
                        for (PoliceBoxModle p:list){
                            bindData(p);
                        }
                    } else {
                        onFailure(i,headers,bytes,null);
                    }
                } catch (JSONException e) {
                    onFailure(i,headers,bytes,e);
                }
            }

            @Override
            public void onFailure(int i, Header[] headers, byte[] bytes, Throwable throwable) {
                stopLoading();
                toast("获取防控点数据失败");
            }
        });
    }

    private void bindData(PoliceBoxModle pb) {
        mPlcBxType.setText(pb.getTYPE());
        mStreet.setText(pb.getSTREET());
        mUnionNo.setText(pb.getUNIFIEDNO());
        mDemand.setText(pb.getDEMAND());
        mInitiator.setText(pb.getINITIATOR());
        mPlcSttn.setText(pb.getPOLICESTATION());
        watchOverDprtmt.setText(pb.getWATCHOVERUNIT());
        mFinishTime.setText(pb.getfINISHTIME());
        mLinkMan.setText(pb.getLINKMAN());
        mPhoneNo.setText(pb.getPHONENO());
        mInterphoneType.setText(pb.getINTERPHONETYPE());
        mWorkTime.setText(pb.getFRNAME());
        mInterPhoneNo.setText(pb.getINTERPHONENO());
        mAddress.setText(pb.getADDRESS());
        if (pb.getLAT()>0)
            mTvLatLngt.setText("("+pb.getLAT()+","+pb.getLNGT()+")");

        if (pb.getIMG1()!=null){
            iv_zp1.setImageResource(R.drawable.loading_pic);
            ImageLoader.getInstance().displayImage(ApiHttpClient.IMAGE_HOST + "/gt/" + pb.getIMG1(), iv_zp1);
        }
        if (pb.getIMG2()!=null){
            iv_zp2.setImageResource(R.drawable.loading_pic);
            ImageLoader.getInstance().displayImage(ApiHttpClient.IMAGE_HOST + "/gt/" + pb.getIMG2(), iv_zp2);
        }
        stopLoading();
    }


    @Override
    public boolean enableBackUpBtn() {
        return true;
    }


    @Override
    public String getAtyTitle() {
        return getString(R.string.plc_bx_info);
    }


}