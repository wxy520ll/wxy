package cn.net.xinyi.xmjt.activity.ZHFK.KQ;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.text.format.DateFormat;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.DatePicker;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONException;
import com.alibaba.fastjson.JSONObject;
import com.loopj.android.http.AsyncHttpResponseHandler;

import org.apache.http.Header;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Map;

import cn.net.xinyi.xmjt.R;
import cn.net.xinyi.xmjt.api.ApiResource;
import cn.net.xinyi.xmjt.config.BaseListAdp;
import cn.net.xinyi.xmjt.config.BaseListAty;
import cn.net.xinyi.xmjt.model.WorkCheckModle;
import cn.net.xinyi.xmjt.utils.BaseDataUtils;
import cn.net.xinyi.xmjt.utils.DB.ZDXXUtils;
import cn.net.xinyi.xmjt.utils.GeneralUtils;

/**
 * Created by hao.zhou on 2016/5/31.
 */
public class WorkCheckHistoryAty extends BaseListAty implements View.OnClickListener{

    private View addView;
    protected static int PAGE_SIZE = 20; //每页加载条数
    private List<WorkCheckModle> workChecks=new ArrayList<WorkCheckModle>();
    private TextView tv_choose_date;
    private TextView tv_choose_pcs;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        WorkCheckHistoryAdp  mAdapter=new WorkCheckHistoryAdp(mListView,workChecks,R.layout.aty_workcheckhistory_item,WorkCheckHistoryAty.this);
        setAdapter(mAdapter);
        mAdapter.setState(BaseListAdp.STATE_LOAD_MORE);
        showLoadding();
        setData();
    }

    private void setData() {
        tv_choose_date.setText(BaseDataUtils.getNowYearAndMonthAndDay());
        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {

            }
        });
        requestData();
    }

    @Override
    protected void requestData() {
        showLoadding();
        ApiResource.getQWDCHistoryData(getRequest(), new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int i, Header[] headers, byte[] bytes) {
                if (mCurrentPage==1)
                    stopLoading();
                String result = new String(bytes);
                try {
                    List<WorkCheckModle> list = JSON.parseArray(result,WorkCheckModle.class);
                    getAdapter().getData().clear();//清空数据
                    if (list!=null && list.size() > 0){
                        getAdapter().getData().addAll(list);
                        if (list.size()>=PAGE_SIZE){
                            setStateFinish();
                        } else {
                            setNotMoreState();
                        }
                    } else {
                        setEmptyData();
                    }
                    getAdapter().notifyDataSetChanged();
                } catch (JSONException e){
                    toast("获取数据失败");
                }
            }
            @Override
            public void onFailure(int i, Header[] headers, byte[] bytes, Throwable throwable) {
                if (mCurrentPage==1) {
                    stopLoading();
                    setEmptyData();
                    toast("获取数据失败");
                }
            }
        });
    }

    private String getRequest() {
        JSONObject  requestJson = new JSONObject();
        if (getIntent().getFlags()==1){
            requestJson.put("PCSBM", new ZDXXUtils(this).getZdlbAndZdzToZdbm(GeneralUtils.ZZJG_PCS,tv_choose_pcs.getText().toString()));
        }else {
            requestJson.put("DCRY",userInfo.getUsername());
        }
        requestJson.put("SCSJ",tv_choose_date.getText().toString());
        requestJson.put("PAGENUMBER",mCurrentPage);
        requestJson.put("PAGESIZE",PAGE_SIZE);
        return requestJson.toJSONString();
    }

    @Override

    public void setupTopView(LinearLayout parent) {
        super.setupTopView(parent);
        parent.setVisibility(View.VISIBLE);
        addView= LayoutInflater.from(this).inflate(R.layout.aty_xlxz_tj,parent);
        tv_choose_date=(TextView) addView.findViewById(R.id.tv_choose_date);
        tv_choose_pcs=(TextView) addView.findViewById(R.id.tv_choose_pcs);
        if (getIntent().getFlags()==1&&(!BaseDataUtils.notLeader() || BaseDataUtils.isAdminPcs())){
            addView.findViewById(R.id.ll_pcs).setVisibility(View.VISIBLE);
        }
        tv_choose_pcs.setText(userInfo.getPcs());
        addView.findViewById(R.id.top_title).setVisibility(View.GONE);
        tv_choose_date.setOnClickListener(this);
        tv_choose_pcs.setOnClickListener(this);
        tv_choose_pcs.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {}

            @Override
            public void afterTextChanged(Editable s) {
                requestData();
            }
        });

    }

    @Override
    public boolean enableBackUpBtn() {
        return true;
    }


    @Override
    public String getAtyTitle() {
        return getString(R.string.qw_workcheck_history);
    }


    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.tv_choose_date:
                new DatePickerDialog(
                        WorkCheckHistoryAty.this, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view,
                                          int year, int monthOfYear,
                                          int dayOfMonth) {
                        c_start.set(year, monthOfYear, dayOfMonth);
                        CharSequence forma= DateFormat.format("yyyy-MM-dd", c_start);
                        tv_choose_date.setText(forma);
                        getAdapter().getData().clear();
                        requestData();
                    }
                },c_end.get(Calendar.YEAR), c_end .get(Calendar.MONTH), c_end
                        .get(Calendar.DAY_OF_MONTH)).show();
                break;
            case R.id.tv_choose_pcs:

                final Map<String,String> pcsMaps=zdUtils.getZdlbToZdz(GeneralUtils.ZZJG_PCS);
                BaseDataUtils.showAlertDialog(WorkCheckHistoryAty.this,pcsMaps.values(),tv_choose_pcs);
                break;
        }
    }
}