package cn.net.xinyi.xmjt.v527.presentation.gzrz;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.xinyi_tech.comm.base.BaseListFragment;
import com.xinyi_tech.comm.base.ListSetupModel;
import com.xinyi_tech.comm.util.ResUtils;
import com.xinyi_tech.comm.util.ToastyUtil;
import com.xinyi_tech.comm.util.ToolbarUtils;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;
import cn.addapp.pickers.listeners.OnItemPickListener;
import cn.addapp.pickers.picker.DatePicker;
import cn.addapp.pickers.picker.SinglePicker;
import cn.net.xinyi.xmjt.R;
import cn.net.xinyi.xmjt.v527.presentation.gzrz.adapter.GzrzPageAdapter;
import cn.net.xinyi.xmjt.v527.presentation.gzrz.model.GzrzPageModel;
import cn.net.xinyi.xmjt.v527.presentation.gzrz.model.GzrzTypeModel;
import cn.net.xinyi.xmjt.v527.presentation.gzrz.presenter.GzrzPresenter;

import static android.app.Activity.RESULT_OK;


/**
 * Created by Fracesuit on 2017/12/22.
 */

public class GzrzFragment extends BaseListFragment<GzrzPageAdapter, GzrzPageModel, GzrzPresenter> {


    @BindView(R.id.ed_content)
    EditText edContent;
    @BindView(R.id.tv_search)
    TextView tvSearch;
    @BindView(R.id.tv_add)
    TextView tvAdd;
    @BindView(R.id.tv_time)
    TextView tvTime;
    @BindView(R.id.tv_type)
    TextView tvType;
    @BindView(R.id.tv_reporte)
    TextView tvReporte;
    @BindView(R.id.tv_sp)
    TextView tvSp;
    @BindView(R.id.recyclerview)
    RecyclerView recyclerview;
    @BindView(R.id.swiperefreshlayout)
    SwipeRefreshLayout swiperefreshlayout;
    @BindView(R.id.ll_baselist_container)
    LinearLayout llBaselistContainer;
    @BindView(R.id.fab)
    FloatingActionButton fab;
    @BindView(R.id.rl_baselist_root)
    RelativeLayout rlBaselistRoot;
    private List<GzrzTypeModel> gzrzTypeModelList;
    private String[] items;

    private String typeCode = "";
    private String timeCode = "";
    private GzrzPageAdapter adapter;
    private String sb = "";//是否上报
    private String sp = "";//是否审批
    private static final int ADDGZRZ = 1000;


    @Override
    protected void requestData(int requestCode, int pageIndex, int pageSize) {
        //setupModel.getPageIndex()
        requestData();
    }

    @Override
    protected ListSetupModel setupParam() {
        return new ListSetupModel.Builder()
                .isLasy(true)
                .isShowToolbar(true)
                .build();
    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_gzrz;
    }

    @Override
    protected void onCreateViewAfter(View view, Bundle savedInstanceState) {
        super.onCreateViewAfter(view, savedInstanceState);
        mPresenter.requestDictionary();
        ToolbarUtils.with(activity, toolbar).setTitle(ResUtils.getString(R.string.app_fullname), true)
                .build();
    }

    @Override
    protected GzrzPageAdapter getAdapter() {
        adapter = new GzrzPageAdapter(R.layout.item_gzrz);
        return adapter;
    }

    @Override
    protected GzrzPresenter getPresenter() {
        return new GzrzPresenter();
    }

    @Override
    public void doParseData(int requestCode, Object data) {
        super.doParseData(requestCode, data);
        if (requestCode == GzrzPresenter.SUCCESS) {
            gzrzTypeModelList = (List<GzrzTypeModel>) data;
            ArrayList<String> strings = new ArrayList<String>();
            if (gzrzTypeModelList != null && gzrzTypeModelList.size() > 0)
                for (int i = 0; i < gzrzTypeModelList.size(); i++) {
                    GzrzTypeModel g = gzrzTypeModelList.get(i);
                    strings.add(g.getNAME());
                }
            strings.add(0, "全部");
            items = (String[]) strings.toArray(new String[0]);
        }
    }

    @OnClick({R.id.tv_time, R.id.tv_type, R.id.tv_reporte, R.id.tv_sp, R.id.tv_add, R.id.tv_search})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.tv_time:
                onYearMonthDayTimePicker((TextView) view, getActivity());
                break;
            case R.id.tv_type:
                showTypeSelect((TextView) view, items, getActivity());
                break;
            case R.id.tv_reporte:
                ShowChoise("是否上报", (TextView) view, 1);
                break;
            case R.id.tv_sp:
                ShowChoise("是否审批", (TextView) view, 2);
                break;
            case R.id.tv_search:
                requestData();
                break;
            case R.id.tv_add:
                Intent intent = new Intent(getActivity(), AddGzrzActivity.class);
                startActivityForResult(intent, ADDGZRZ);
                break;
        }
    }

    public void showTypeSelect(final TextView textView, final String[] arrayItem, Activity activity) {

        if (arrayItem == null) {
            ToastyUtil.warningShort("日志类型数据获取失败");
            return;
        }
        SinglePicker<String> picker = new SinglePicker<String>(activity, arrayItem);
        picker.setTitleText("日志类型");
        picker.setTitleTextSize(18);
        picker.setCanLoop(false);//不禁用循环
        picker.setLineVisible(true);
        picker.setShadowVisible(false);
        picker.setTextSize(20);
        picker.setCancelTextColor(Color.parseColor("#7cb6e7"));
        picker.setSubmitTextColor(Color.parseColor("#7cb6e7"));
        picker.setSelectedIndex(0);
        picker.setWheelModeEnable(true);
        //启用权重 setWeightWidth 才起作用
        picker.setWeightEnable(true);
        picker.setWeightWidth(1);
        picker.setTopPadding(15);
        picker.setSelectedTextColor(0xFF279BAA);//前四位值是透明度
        picker.setUnSelectedTextColor(0xFF999999);
        picker.setOnItemPickListener(new OnItemPickListener<String>() {
            @Override
            public void onItemPicked(int index, String item) {
                tvType.setText(item);
                for (GzrzTypeModel g : gzrzTypeModelList
                        ) {
                    if (item.equals("全部")) {
                        typeCode = "";
                        break;
                    }
                    if (item.equals(g.getNAME())) {
                        typeCode = g.getCODE();
                        break;
                    }
                }
                requestData();
            }
        });
        picker.show();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            if (requestCode == ADDGZRZ )
                requestData();
            else if (requestCode==1006){
                requestData();
            }
        }
    }

    public void onYearMonthDayTimePicker(final TextView textView, Activity context) {
        final DatePicker picker = new DatePicker(context);
        picker.setCanLoop(false);
        picker.setTitleText("日志时间");
        picker.setTitleTextSize(18);
        picker.setCancelTextColor(Color.parseColor("#7cb6e7"));
        picker.setSubmitTextColor(Color.parseColor("#7cb6e7"));
        picker.setWheelModeEnable(true);
        picker.setTopPadding(15);
        picker.setTextSize(20);
        picker.setRangeStart(2016, 8, 29);
        picker.setRangeEnd(2111, 1, 11);
        picker.setWeightEnable(true);
        picker.setOnDatePickListener(new DatePicker.OnYearMonthDayPickListener() {
            @Override
            public void onDatePicked(String year, String month, String day) {
                tvTime.setText(year + "-" + month + "-" + day);
                timeCode = year + "-" + month + "-" + day + " 00:00:00";
                requestData();
            }
        });
        picker.show();
    }


    private void ShowChoise(String s, final TextView textView, final int i) {
        final String[] items = {"是", "否"};
        AlertDialog.Builder alertBuilder = new AlertDialog.Builder(getActivity());
        alertBuilder.setTitle(s);
        alertBuilder.setSingleChoiceItems(items, 0, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface arg0, int index) {
                arg0.dismiss();
                textView.setText(items[index]);
                if (i == 1) {
                    sb = index == 0 ? "1" : "0";
                } else {
                    sp = index == 0 ? "1" : "0";
                }
                requestData();
            }
        });
        alertBuilder.create().show();

    }

    public void requestData() {
        mPresenter.getGzrzRecord(REQUEST_STATE_REFRESH, setupModel.getPageIndex(), setupModel.getPageSize(),
                timeCode, typeCode, sb, sp, edContent.getText().toString());
    }


    @Override
    public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
        //super.onItemClick(adapter, view, position);
        Intent intent = new Intent(getActivity(), AddGzrzActivity.class);
        Bundle bundle = new Bundle();
        bundle.putSerializable("data", (Serializable) adapter.getData().get(position));
        intent.putExtras(bundle);
        startActivityForResult(intent,1006);
    }
}
