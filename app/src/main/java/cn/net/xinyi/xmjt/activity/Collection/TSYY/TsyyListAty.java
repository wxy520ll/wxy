package cn.net.xinyi.xmjt.activity.Collection.TSYY;

import android.app.AlertDialog;
import android.content.Context;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
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
import cn.net.xinyi.xmjt.utils.DateUtil;
import cn.net.xinyi.xmjt.utils.UI;

/**
 * Created by hao.zhou on 2016/11/13.
 */
public class TsyyListAty extends BaseListAty implements View.OnClickListener {
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
    private TextView tv_yybh;
    private TextView tv_qx;
    private TssyyModle item;
    public static String time1=" 09:00:00";
    public static String time2=" 10:00:00";
    public static String time3=" 11:00:00";
    public static String time4=" 14:00:00";
    public static String time5=" 15:00:00";
    public static String time6=" 16:00:00";

    public static String time7=" 08:30:00";
    public static String time8=" 09:30:00";
    public static String time9=" 10:30:00";
    public static String time10=" 13:30:00";
    public static String time11=" 14:30:00";
    public static String time12=" 15:30:00";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        AnnotateManager.initBindView(this);//注解式绑定控件
        requestData();//获取申请人记录
        initDialog();
    }


    //获取申请人记录
    @Override
    protected void requestData() {
        showLoadding();
        JSONObject requestJson = new JSONObject();
        requestJson.put("YYRSJH", userInfo.getUsername());
        ApiResource.getTssyyListByUser(requestJson.toJSONString(),new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int i, Header[] headers, byte[] bytes) {
                String result = new String(bytes);
                if (result!=null&&result.length()>5){
                    tssyyModles= JSON.parseArray(result, TssyyModle.class);
                    mNodata.setVisibility(View.GONE);
                    adp=new TsyyListAdapter(mListView,tssyyModles,R.layout.aty_tsyy_list_adp,TsyyListAty.this);
                    mListView.setAdapter(adp);
                    adp.setonClickListener(new TsyyListAdapter.onClickListener() {
                        @Override
                        public void onWhatch(int index) {
                            tsyyqy(tssyyModles.get(index));
                        }
                    });
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
                item=tssyyModles.get(position);
                showDialogAlert();

            }
        });
        // 取得自定义View
        LayoutInflater layoutInflater = LayoutInflater.from(this);
        myAddView = layoutInflater.inflate(R.layout.aty_tsyy_list_dialog, null);

        tv_qx=(TextView)myAddView.findViewById(R.id.tv_qx);
        tv_yyzt=(TextView)myAddView.findViewById(R.id.tv_yyzt);
        tv_yybh=(TextView)myAddView.findViewById(R.id.tv_yybh);
        tv_yysj=(TextView)myAddView.findViewById(R.id.tv_yysj);
        tv_fjbh=(TextView)myAddView.findViewById(R.id.tv_fjbh);
        tv_fjmc=(TextView)myAddView.findViewById(R.id.tv_fjmc);
        tv_fjmj=(TextView)myAddView.findViewById(R.id.tv_fjmj);
        tv_fjms=(TextView)myAddView.findViewById(R.id.tv_fjms);
        tv_qxyy=(TextView)myAddView.findViewById(R.id.tv_qxyy);
        tv_qx.setOnClickListener(this);
        tv_qxyy.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {

        switch (v.getId()){
            case R.id.tv_qx:
                clearText();
                break;

            case R.id.tv_qxyy:
                tsyyqy(item);

                break;
        }
    }

    private void tsyyqy(TssyyModle item) {
        //不能取消30分钟之内的预约申请
        if (item.getYYLX().equals("9")&&(DateUtil.getDays2NowValus(item.getYYRQ()+time7) < (1000 * 60 * 30))){
            UI.toast(TsyyListAty.this,getString(R.string.tsys_qx_tips));
        }else if (item.getZT().equals("10")&&(DateUtil.getDays2NowValus(item.getYYRQ()+time8) < (1000 * 60 * 30))){
            UI.toast(TsyyListAty.this,getString(R.string.tsys_qx_tips));
        }else if (item.getZT().equals("11")&&(DateUtil.getDays2NowValus(item.getYYRQ()+time9) < (1000 * 60 * 30))) {
            UI.toast(TsyyListAty.this, getString(R.string.tsys_qx_tips));
        }else if (item.getYYLX().equals("14")&&(DateUtil.getDays2NowValus(item.getYYRQ()+time10) < (1000 * 60 * 30))){
            UI.toast(TsyyListAty.this,getString(R.string.tsys_qx_tips));
        }else if (item.getZT().equals("15")&&(DateUtil.getDays2NowValus(item.getYYRQ()+time11) < (1000 * 60 * 30))){
            UI.toast(TsyyListAty.this,getString(R.string.tsys_qx_tips));
        }else if (item.getZT().equals("16")&&(DateUtil.getDays2NowValus(item.getYYRQ()+time12) < (1000 * 60 * 30))){
            UI.toast(TsyyListAty.this,getString(R.string.tsys_qx_tips));
        }else {
            updateTsyy(item);
        }

    }


    private void updateTsyy(TssyyModle ite) {
        showLoadding();
        JSONObject jo=new JSONObject();
        jo.put("ID",""+ite.getID());
        jo.put("ZT","2");
        ApiResource.updateTssyy(jo.toJSONString(),new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int i, Header[] headers, byte[] bytes) {
                String result = new String(bytes);
                if (i==200&&result.startsWith("true")){
                    requestData();
                    clearText();
                    stopLoading();
                } else  {
                    onFailure(i, headers, bytes, null);
                }
            }

            @Override
            public void onFailure(int i, Header[] headers, byte[] bytes, Throwable throwable) {
                stopLoading();
                UI.toast(TsyyListAty.this,"上传数据失败");
            }
        });
    }


    private void showDialogAlert() {
        //预约状态：  1预约中 2.取消预约 3使用中 4已用完 5.失信
        tv_qxyy.setVisibility(View.GONE);
        if (item.getZT().equals("1")){
            tv_yyzt.setText("预约中");
            tv_qxyy.setVisibility(View.VISIBLE);
            tv_yyzt.setTextColor(getResources().getColor(R.color.bbutton_warning_pressed));
        }else if (item.getZT().equals("2")){
            tv_yyzt.setText("取消预约");
            tv_yyzt.setTextColor(getResources().getColor(R.color.bbutton_danger));
        }else if (item.getZT().equals("3")){
            tv_yyzt.setText("使用中");
            tv_yyzt.setTextColor(getResources().getColor(R.color.bbutton_danger));
        }else if (item.getZT().equals("4")){
            tv_yyzt.setText("已用完");
            tv_yyzt.setTextColor(getResources().getColor(R.color.black));
        }else if (item.getZT().equals("1")){
            tv_yyzt.setText("失信");
            tv_yyzt.setTextColor(getResources().getColor(R.color.bbutton_danger));
        }
        //预约时间
        tv_yysj.setText(item.getYYRQ()+" "+item.getYYLX()+":00");
        tv_fjbh.setText(item.getFJBH() );
        tv_fjmc.setText(item.getMC());
        tv_fjmj.setText(item.getMJ());
        tv_fjms.setText(item.getMS());
        tv_yybh.setText(item.getYYH());


        alertDialog=new AlertDialog.Builder(TsyyListAty.this)
                .setView(myAddView)
                .setCancelable(false).show();
    }


    private void clearText() {
        if (null!=alertDialog&&alertDialog.isShowing()){
            dismissAlert(true);
            ((FrameLayout)myAddView.getParent()).removeView(myAddView);
        }
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



    static class TsyyListAdapter extends BaseListAdp<TssyyModle> {
        private  Context context;
        public TsyyListAdapter(AbsListView view, Collection<TssyyModle> datas, int itemLayoutId, Context context) {
            super(view, datas, itemLayoutId, context);
            this.context=context;
        }

        @Override
        public void convert(final AdapterHolder helper, TssyyModle item, boolean isScrolling) {
            //预约状态：  1预约中 2.取消预约 3使用中 4已用完 5.失信
            if (item.getZT().equals("1")){
                helper.setText(R.id.tv_yyzt,"预约状态：预约中");
                helper.setTextColor(R.id.tv_yyzt,context.getResources().getColor(R.color.bbutton_warning_pressed));
            }else if (item.getZT().equals("2")){
                helper.setText(R.id.tv_yyzt,"预约状态：取消预约");
                helper.setTextColor(R.id.tv_yyzt,context.getResources().getColor(R.color.red));
            }else if (item.getZT().equals("3")){
                helper.setText(R.id.tv_yyzt,"预约状态：使用中");
                helper.setTextColor(R.id.tv_yyzt,context.getResources().getColor(R.color.red));
            }else if (item.getZT().equals("4")){
                helper.setText(R.id.tv_yyzt,"预约状态：已用完");
                helper.setTextColor(R.id.tv_yyzt,context.getResources().getColor(R.color.black));
            }else if (item.getZT().equals("1")){
                helper.setText(R.id.tv_yyzt,"预约状态：失信");
                helper.setTextColor(R.id.tv_yyzt,context.getResources().getColor(R.color.red));
            }
            //预约类型
            helper.setText(R.id.tv_yysj,"预约时间："+item.getYYRQ()+" "+item.getYYLX()+":00");

            helper.setText(R.id.tv_fjbh,"房间编号："+item.getFJBH());
            helper.setText(R.id.tv_fjmc,"房间名称："+item.getMC());
            helper.setText(R.id.tv_yyh,"预约编号："+item.getYYH());

            if (item.getZT().equals("1")) {
                helper.getView(R.id.tv_yyqxyy).setVisibility(View.VISIBLE);

                helper.getView(R.id.tv_yyqxyy).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (clickListener != null)
                            clickListener.onWhatch(helper.getPosition());
                    }
                });
            }else {
                helper.getView(R.id.tv_yyqxyy).setVisibility(View.GONE);
            }

        }



        public onClickListener clickListener;

        public void setonClickListener(onClickListener clickListener) {
            this.clickListener = clickListener;
        }

        public interface onClickListener {
            public void onWhatch(int index);

        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                setResult(RESULT_OK);
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
                &&event.getKeyCode() == KeyEvent.KEYCODE_BACK) {
            setResult(RESULT_OK);
            this.finish();
        }
        return super.dispatchKeyEvent(event);
    }


    @Override
    public String getAtyTitle() {
        return getString(R.string.tsys_jl);
    }

    @Override
    public boolean enableBackUpBtn() {
        return true;
    }

}
