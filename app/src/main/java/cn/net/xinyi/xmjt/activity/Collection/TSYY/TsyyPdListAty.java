package cn.net.xinyi.xmjt.activity.Collection.TSYY;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.loopj.android.http.AsyncHttpResponseHandler;

import org.apache.http.Header;

import java.lang.reflect.Field;
import java.util.Collection;
import java.util.List;

import cn.net.xinyi.xmjt.R;
import cn.net.xinyi.xmjt.api.ApiResource;
import cn.net.xinyi.xmjt.config.AdapterHolder;
import cn.net.xinyi.xmjt.config.BaseListAdp;
import cn.net.xinyi.xmjt.config.BaseListAty;
import cn.net.xinyi.xmjt.model.TssyyModle;
import cn.net.xinyi.xmjt.utils.AnnotateManager;
import cn.net.xinyi.xmjt.utils.DialogHelper;
import cn.net.xinyi.xmjt.utils.UI;

/**
 * Created by hao.zhou on 2016/11/13.
 * 提审预约排队
 */
public class TsyyPdListAty extends BaseListAty implements View.OnClickListener {
    private List<TssyyModle> tssyyModles;
    private TsyyListAdapter adp;
    private AlertDialog alertDialog;
    private View myAddView;
    private TextView tv_qxyy;
    private TextView tv_fjmj;
    private TextView tv_fjms;
    private TextView tv_fjbh;
    private TextView tv_fjmc;
    private TextView tv_yysj;
    private TextView tv_yyzt;
    private TextView tv_yybh_1;
    private TextView tv_yysj_1;
    private TextView tv_yybh;
    private TextView tv_qx;
    private TextView tv_pdbh;
    private TextView tv_pdrs;
    private TextView tv_pdzs;
    private TextView tv_sx;
    private TextView tv_qxpd;
    private View layoutView;
    private int pdId;//排队ID

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        AnnotateManager.initBindView(this);//注解式绑定控件
        requestData();//获取申请人记录
        initDialog();
    }

    //顶部提示
    @Override
    public void setupTopView(LinearLayout parent) {
        super.setupTopView(parent);
        layoutView=LayoutInflater.from(this).inflate(R.layout.aty_tsyypd_top,parent);
        parent.setVisibility(View.VISIBLE);
        tv_pdbh=(TextView)layoutView.findViewById(R.id.tv_pdbh);//排队编号
        tv_pdrs=(TextView)layoutView.findViewById(R.id.tv_pdrs);//排队人数
        tv_pdzs=(TextView)layoutView.findViewById(R.id.tv_pdzs);//排队总数
        tv_sx=(TextView)layoutView.findViewById(R.id.tv_sx);//刷新
        tv_qxpd=(TextView)layoutView.findViewById(R.id.tv_qxpd);//取消排队
        tv_sx.setOnClickListener(this);
        tv_qxpd.setOnClickListener(this);
    }


    //获取申请人记录
    private void getPDInfo() {
        JSONObject requestJson = new JSONObject();
        requestJson.put("PDRJH", userInfo.getPoliceno());
        ApiResource.getLatestTsspdByUser(requestJson.toJSONString(),new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int i, Header[] headers, byte[] bytes) {
                String result = new String(bytes);
                if (result!=null&&result.length()>5){
                    JSONArray jsonArry= JSON.parseArray(result);
                    if (jsonArry.size()>0){
                        JSONObject jo= (JSONObject)jsonArry.get(0);
                        if (jo.get("ZT").equals("1")){
                            pdId=jo.getInteger("ID");
                            tv_pdrs.setText(jo.get("TOTAL").toString());
                            tv_pdzs.setText(jo.get("TODAYTOTAL").toString());
                            tv_pdbh.setText("排队编号:"+jo.get("PDBH").toString());
                        }else {
                            onFailure(i,headers,bytes,null);
                        }
                    }else {
                        onFailure(i,headers,bytes,null);
                    }
                } else {
                    onFailure(i,headers,bytes,null);
                }
                stopLoading();
            }
            @Override
            public void onFailure(int i, Header[] headers, byte[] bytes, Throwable throwable) {
                stopLoading();
                tv_pdrs.setText("0");
                tv_pdzs.setText("0");
                tv_pdbh.setText("没有您今天的排队记录");
                UI.toast(TsyyPdListAty.this,"没有您今天的排队记录");
            }
        });
    }

    //获取申请人记录
    @Override
    protected void requestData() {
        showLoadding();
        getPDInfo();
        JSONObject requestJson = new JSONObject();
        requestJson.put("PDRJH", userInfo.getPoliceno());
        ApiResource.getTsspdList(requestJson.toJSONString(),new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int i, Header[] headers, byte[] bytes) {
                String result = new String(bytes);
                if (result!=null&&result.length()>5){
                    tssyyModles= JSON.parseArray(result, TssyyModle.class);
                    mNodata.setVisibility(View.GONE);
                    adp=new TsyyListAdapter(mListView,tssyyModles,R.layout.aty_tsyy_list_adp,TsyyPdListAty.this);
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



    //初始化自定义添加AlertDialog
    private void initDialog() {
        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                showDialogAlert(tssyyModles.get(position));

            }
        });
        // 取得自定义View
        LayoutInflater layoutInflater = LayoutInflater.from(this);
        myAddView = layoutInflater.inflate(R.layout.aty_tsyy_list_dialog, null);
        tv_qx=(TextView)myAddView.findViewById(R.id.tv_qx);
        tv_yyzt=(TextView)myAddView.findViewById(R.id.tv_yyzt);
        tv_yybh=(TextView)myAddView.findViewById(R.id.tv_yybh);
        tv_yybh_1=(TextView)myAddView.findViewById(R.id.tv_yybh_1);
        tv_yysj_1=(TextView)myAddView.findViewById(R.id.tv_yysj_1);
        tv_yysj=(TextView)myAddView.findViewById(R.id.tv_yysj);
        tv_fjbh=(TextView)myAddView.findViewById(R.id.tv_fjbh);
        tv_fjmc=(TextView)myAddView.findViewById(R.id.tv_fjmc);
        tv_fjmj=(TextView)myAddView.findViewById(R.id.tv_fjmj);
        tv_fjms=(TextView)myAddView.findViewById(R.id.tv_fjms);
        tv_qxyy=(TextView)myAddView.findViewById(R.id.tv_qxyy);
        tv_yybh_1.setText("排队编号");
        tv_yysj_1.setText("排队时间");
        tv_qx.setOnClickListener(this);
        tv_qxyy.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {

        switch (v.getId()){
            case R.id.tv_qx:
                clearText();
                break;

            case R.id.tv_sx://刷新排队信息
                showLoadding();
                getPDInfo();
                break;
            case R.id.tv_qxpd://取消排队
                if (tv_pdrs.getText().toString().equals("0")){
                    UI.toast(TsyyPdListAty.this,"当前没有您的现场排队信息！");
                }else {
                    DialogHelper.showAlertDialog("您将取消"+tv_pdbh.getText().toString(),TsyyPdListAty.this, new DialogHelper.OnOptionClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which, Object o) {
                            updateTsyypd();
                        }
                    });
                }
                break;
        }
    }



    private void updateTsyypd() {
        showLoadding();
        JSONObject jo=new JSONObject();
        jo.put("ID",""+pdId);
        jo.put("ZT","2");
        ApiResource.tsspdUpdate(jo.toJSONString(),new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int i, Header[] headers, byte[] bytes) {
                String result = new String(bytes);
                if (i==200&&result.startsWith("true")){
                    getPDInfo();
                    requestData();
                    stopLoading();
                } else  {
                    onFailure(i, headers, bytes, null);
                }
            }

            @Override
            public void onFailure(int i, Header[] headers, byte[] bytes, Throwable throwable) {
                stopLoading();
                UI.toast(TsyyPdListAty.this,"上传数据失败");
            }
        });
    }


    private void showDialogAlert(TssyyModle item) {
        //预约状态：  1预约中 2.取消预约 3使用中 4已用完 5.失信
        tv_qxyy.setVisibility(View.GONE);
        if (item.getZT().equals("1")){
            tv_yyzt.setText("排队中");
            tv_yyzt.setTextColor(getResources().getColor(R.color.bbutton_warning_pressed));
        }else if (item.getZT().equals("2")){
            tv_yyzt.setText("取消排队");
            tv_yyzt.setTextColor(getResources().getColor(R.color.bbutton_danger));
        }else if (item.getZT().equals("3")){
            tv_yyzt.setText("使用中");
            tv_yyzt.setTextColor(getResources().getColor(R.color.bbutton_danger));
        }else if (item.getZT().equals("4")){
            tv_yyzt.setText("已用完");
            tv_yyzt.setTextColor(getResources().getColor(R.color.black));
        }
        //预约时间
        tv_yysj.setText(item.getPDLRSJ());
        tv_fjbh.setText(item.getFJBH()==null?"未指定房间":item.getFJBH());
        tv_fjmc.setText(item.getMC()==null?"未指定房间":item.getMC());
        tv_fjmj.setText(item.getMJ()==null?"未指定房间":item.getMJ());
        tv_fjms.setText(item.getMS()==null?"未指定房间":item.getMS());
        tv_yybh.setText(item.getPDBH());
        alertDialog=new AlertDialog.Builder(TsyyPdListAty.this)
                .setView(myAddView)
                .setCancelable(false).show();
    }


    private void clearText() {
        dismissAlert(true);
        ((FrameLayout)myAddView.getParent()).removeView(myAddView);
    }

    private void dismissAlert(boolean b) {//控制对话框是否关闭
        try {
            Field field = alertDialog.getClass()
                    .getSuperclass().getDeclaredField("mShowing" );
            field.setAccessible(true);
            // 将mShowing变量设为false，表示对话框已关闭
            field.set(alertDialog,b);
            alertDialog.dismiss();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }



    class TsyyListAdapter extends BaseListAdp<TssyyModle> {
        private  Context context;
        public TsyyListAdapter(AbsListView view, Collection<TssyyModle> datas, int itemLayoutId, Context context) {
            super(view, datas, itemLayoutId, context);
            this.context=context;
        }

        @Override
        public void convert(AdapterHolder helper, TssyyModle item, boolean isScrolling) {
            helper.getView(R.id.tv_yyqxyy).setVisibility(View.GONE);
            //预约状态：  1排队中 2.取消排队 3使用中 4已用完
            if (item.getZT().equals("1")){
                helper.setText(R.id.tv_yyzt,"预约状态：排队中");
                helper.setTextColor(R.id.tv_yyzt,context.getResources().getColor(R.color.bbutton_warning_pressed));
            }else if (item.getZT().equals("2")){
                helper.setText(R.id.tv_yyzt,"预约状态：取消排队");
                helper.setTextColor(R.id.tv_yyzt,context.getResources().getColor(R.color.bbutton_danger));
            }else if (item.getZT().equals("3")){
                helper.setText(R.id.tv_yyzt,"预约状态：使用中");
                helper.setTextColor(R.id.tv_yyzt,context.getResources().getColor(R.color.bbutton_danger));
            }else if (item.getZT().equals("4")){
                helper.setText(R.id.tv_yyzt,"预约状态：已用完");
                helper.setTextColor(R.id.tv_yyzt,context.getResources().getColor(R.color.black));
            }
            helper.setText(R.id.tv_yysj,"排队时间："+item.getPDLRSJ());
            helper.setText(R.id.tv_fjbh,item.getFJBH()==null?"房间编号：未指定房间":"房间编号："+item.getFJBH());
            helper.setText(R.id.tv_fjmc,item.getMC()==null?"房间名称：未指定房间":"房间名称："+item.getMC());
            helper.setText(R.id.tv_yyh,"排队编号："+item.getPDBH());
        }
    }


    @Override
    public String getAtyTitle() {
        return getString(R.string.tsy_pd);
    }

    @Override
    public boolean enableBackUpBtn() {
        return true;
    }

}
