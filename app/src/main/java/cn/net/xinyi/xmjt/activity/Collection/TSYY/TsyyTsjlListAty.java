package cn.net.xinyi.xmjt.activity.Collection.TSYY;

import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.AbsListView;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.loopj.android.http.AsyncHttpResponseHandler;

import org.apache.http.Header;

import java.util.Collection;
import java.util.List;

import cn.net.xinyi.xmjt.R;
import cn.net.xinyi.xmjt.api.ApiResource;
import cn.net.xinyi.xmjt.config.AdapterHolder;
import cn.net.xinyi.xmjt.config.BaseListAdp;
import cn.net.xinyi.xmjt.config.BaseListAty;
import cn.net.xinyi.xmjt.model.TssyyModle;
import cn.net.xinyi.xmjt.utils.AnnotateManager;

/**
 * Created by hao.zhou on 2016/11/13.
 *个人提审室使用记录
 */
public class TsyyTsjlListAty extends BaseListAty {
    private List<TssyyModle> tssyyModles;
    private TsyyListAdapter adp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        AnnotateManager.initBindView(this);//注解式绑定控件
        requestData();//获取申请人记录
    }


    //获取申请人记录
    @Override
    protected void requestData() {
        showLoadding();
        JSONObject requestJson = new JSONObject();
        requestJson.put("JH", userInfo.getPoliceno());
        ApiResource.getTsjlList(requestJson.toJSONString(),new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int i, Header[] headers, byte[] bytes) {
                String result = new String(bytes);
                if (result!=null&&result.length()>5){
                    tssyyModles= JSON.parseArray(result, TssyyModle.class);
                    mNodata.setVisibility(View.GONE);
                    adp=new TsyyListAdapter(mListView,tssyyModles,R.layout.aty_tsyy_list_adp,TsyyTsjlListAty.this);
                    mListView.setAdapter(adp);
                } else {
                    mNodata.setVisibility(View.VISIBLE);
                }
                stopLoading();
            }
            @Override
            public void onFailure(int i, Header[] headers, byte[] bytes, Throwable throwable) {
                stopLoading();
                toast("获取数据失败");
                mNodata.setVisibility(View.VISIBLE);
            }
        });
    }



    class TsyyListAdapter extends BaseListAdp<TssyyModle> {
        private  Context context;
        public TsyyListAdapter(AbsListView view, Collection<TssyyModle> datas, int itemLayoutId, Context context) {
            super(view, datas, itemLayoutId, context);
            this.context=context;
        }

        @Override
        public void convert(AdapterHolder helper, TssyyModle item, boolean isScrolling) {


            helper.setText(R.id.tv_fjmc,"房间名称："+item.getMC()+"("+item.getFJBH()+")");
            helper.setText(R.id.tv_yysj,"房间面积："+item.getMJ());
            helper.getView(R.id.tv_yyqxyy).setVisibility(View.GONE);
            //根据结束时间是否为空判断提审室是否使用完
            if (null!=item.getJSSJ()){
                helper.setText(R.id.tv_yyzt,"预约状态：已用完");
                helper.setText(R.id.tv_fjbh,"使用时间:"+item.getKSSJ()+"至"+item.getJSSJ());
            }else {
                helper.setText(R.id.tv_yyzt,"预约状态:使用中（"+item.getKSSJ()+")");
                helper.setText(R.id.tv_fjbh,"使用时间:"+item.getKSSJ());
            }
            // 预约类型（1、网上提审预约 2、现在排队预约 3、律师约见）
            if (item.getYYLX().equals("1")){
                helper.setText(R.id.tv_yyh,"预约类型:网上提审预约("+item.getYYH()+")");
            }else if (item.getYYLX().equals("2")){
                helper.setText(R.id.tv_yyh,"预约类型:现场排队预约("+item.getPDBH()+")");
            }
        }
    }


    @Override
    public String getAtyTitle() {
        return getString(R.string.tsy_jl);
    }

    @Override
    public boolean enableBackUpBtn() {
        return true;
    }

}
