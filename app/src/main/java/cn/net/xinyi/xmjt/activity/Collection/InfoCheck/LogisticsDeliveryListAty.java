package cn.net.xinyi.xmjt.activity.Collection.InfoCheck;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewTreeObserver;
import android.view.inputmethod.EditorInfo;
import android.widget.AdapterView;
import android.widget.AutoCompleteTextView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONException;
import com.alibaba.fastjson.JSONObject;
import com.loopj.android.http.AsyncHttpResponseHandler;

import org.apache.http.Header;

import java.util.ArrayList;
import java.util.List;

import cn.net.xinyi.xmjt.R;
import cn.net.xinyi.xmjt.api.ApiResource;
import cn.net.xinyi.xmjt.config.BaseListAdp;
import cn.net.xinyi.xmjt.config.BaseListAty;
import cn.net.xinyi.xmjt.model.ZAJCModle;
import cn.net.xinyi.xmjt.utils.BaseDataUtils;
import cn.net.xinyi.xmjt.utils.DialogHelper;
import cn.net.xinyi.xmjt.utils.NestGridView;
import cn.net.xinyi.xmjt.utils.StringUtils;
import cn.net.xinyi.xmjt.utils.UI;

//寄递物流 列表
public class LogisticsDeliveryListAty extends BaseListAty {

    private AutoCompleteTextView item_kb_keywords;
    private TextView department;
    private List<ZAJCModle> zajc;
    private NestGridView gv_hczt;
    private NestGridView gv_cslb;
    private int pHczt;
    private int pCslb;
    private Dialog dialog;
    private String textHczt="全部";
    private String textCslb="全部";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        zajc=new ArrayList<>();
        LogisticsDeliveryListAdp mAdapter=new LogisticsDeliveryListAdp(mListView,zajc,R.layout.aty_logistics_delivery_list_item,LogisticsDeliveryListAty.this);
        setAdapter(mAdapter);
        mAdapter.setState(BaseListAdp.STATE_LOAD_MORE);
        requestData();
    }
    @Override
    public void setupTopView(LinearLayout parent) {
        parent.setVisibility(View.VISIBLE);
        View view = LayoutInflater.from(getActivity()).inflate(R.layout.view_head_view, parent);

        department= (TextView) view.findViewById(R.id.item_department);
        department.setText("全部");
        department.setVisibility(View.VISIBLE);

        final List<String> mLists=new ArrayList<>();
        mLists.add("全部");
        mLists.add("未核查");
        mLists.add("已核查");

        department.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                BaseDataUtils.showAlertDialog(LogisticsDeliveryListAty.this,mLists,department);
            }
        });

        item_kb_keywords = (AutoCompleteTextView) view.findViewById(R.id.item_kb_keywords);
        item_kb_keywords.setHint("请输入场所名称");

        department.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                getSearchData();
            }
        });


        item_kb_keywords.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                    getSearchData();
                }
                return false;
            }
        });


        item_kb_keywords.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                item_kb_keywords.removeTextChangedListener(this);
                getSearchData();
                item_kb_keywords.addTextChangedListener(this);
            }
        });
        TextView search = (TextView) view.findViewById(R.id.item_kb_search);
        search.setVisibility(View.GONE);
    }





    /**
     * 点击弹出pop
     */
    private void showRightPopinWindow() {
        View view = View.inflate(this, R.layout.pop_right_layout, null);
        TextView reset = (TextView) view.findViewById(R.id.reset);
        TextView confirm = (TextView) view.findViewById(R.id.confirm);
        gv_hczt = (NestGridView) view.findViewById(R.id.gv_hczt);
        gv_cslb = (NestGridView) view.findViewById(R.id.gv_cslb);

        final List<String> mLists=new ArrayList<>();
        mLists.add("全部");
        mLists.add("未核查");
        mLists.add("已核查");
        final PopGridAdapter1 hcztAdapter = new PopGridAdapter1(this, mLists);
        gv_hczt.setAdapter(hcztAdapter);


        final List<String> mLists1=new ArrayList<>();
        mLists1.add("全部");
        mLists1.add("寄递企业或网点");
        mLists1.add("物流企业或网点");
        final PopGridAdapter1 cslbAdapter = new PopGridAdapter1(this, mLists1);
        gv_cslb.setAdapter(cslbAdapter);


        gv_hczt.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (view.isSelected()) {
                    view.setSelected(false);
                } else {
                    pHczt = position;
                    textHczt = mLists.get(position);
                    view.setSelected(true);
                }
            }
        });

        gv_cslb.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (view.isSelected()) {
                    view.setSelected(false);
                } else {
                    pCslb = position;
                    textCslb=mLists1.get(position);
                    view.setSelected(true);
                }
            }
        });

        confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (StringUtils.isEmpty(textCslb)||StringUtils.isEmpty(textHczt)){
                    UI.toast(LogisticsDeliveryListAty.this,"请选择筛选状态！");
                }else {
                    dialog.hide();
                    getSearchData();
                }
            }
        });

        reset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pCslb = -1;
                pHczt = -1;
                textCslb=null;
                textHczt=null;
                hcztAdapter.notifyDataSetChanged();
                cslbAdapter.notifyDataSetChanged();
            }
        });
        dialog = DialogHelper.alertViewRight(this, view);
        dialog.getWindow().getDecorView().getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                if (pCslb != -1 && gv_cslb != null){
                    gv_cslb.getChildAt(pCslb).setSelected(true);
                }
                if (pHczt != -1 && gv_hczt != null) {
                    gv_hczt.getChildAt(pHczt).setSelected(true);
                }
            }
        });
    }



    @Override
    protected void requestData() {
        showLoadding();
        JSONObject jo=new JSONObject();
        if (department.getText().toString().equals("全部")){
            jo.put("SFHC","");
        }else if (department.getText().toString().equals("未核查")){
            jo.put("SFHC","0");
        }else if (department.getText().toString().equals("已核查")){
            jo.put("SFHC","1");
        }
        if (getIntent().getFlags()==1001){//寄递
            jo.put("CJFL","30");
        }else if (getIntent().getFlags()==1002){//物流
            jo.put("CJFL","31");
        }
        jo.put("LB","2");

        if (BaseDataUtils.isAdminPcs()){
            jo.put("SSPCS","");
        }else {
            jo.put("SSPCS",userInfo.getPcs());
        }
        jo.put("PAGESIZE",PAGE_SIZE);
        jo.put("PAGENUMBER",mCurrentPage);
        jo.put("MC",item_kb_keywords.getText().toString());
        String json=jo.toJSONString();
        ApiResource.getZASerchList(json, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int i, Header[] headers, byte[] bytes) {
                stopLoading();
                String result = new String(bytes);
                try {
                    zajc =  JSON.parseArray(result,ZAJCModle.class);
                    if (zajc.size()>=PAGE_SIZE){
                        setStateFinish();
                        getAdapter().getData().addAll(zajc);
                    } else {
                        if (mCurrentPage==1&&zajc.size()==0){
                            setEmptyData();
                        } else {
                            getAdapter().getData().addAll(zajc);
                            setNotMoreState();
                        }
                    }
                    getAdapter().notifyDataSetChanged();
                } catch (JSONException e) {
                    onFailure(i,headers,bytes,e);
                }
            }
            @Override
            public void onFailure(int i, Header[] headers, byte[] bytes, Throwable throwable) {
                stopLoading();
                setNoData();
                toast("查询失败");
            }
        });
    }



    private void getSearchData() {
        mCurrentPage=1;
        getAdapter().getData().clear();
        requestData();
    }


    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id, Object t) {
        super.onItemClick(parent, view, position, id, t);
        if (t instanceof ZAJCModle){
            ZAJCModle info=(ZAJCModle)t;
            Intent intent=null;
            if (info.getHCSJ()==null) {
                intent = new Intent(LogisticsDeliveryListAty.this, LogisticsDeliveryCheckAty.class);
            }else {
                intent = new Intent(LogisticsDeliveryListAty.this, LogisticsDeliveryCheckListAty.class);
                intent.setFlags(1);
            }
            intent.putExtra("ZAJCModle",info);
            startActivityForResult(intent,1001);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode==RESULT_OK){
            requestData();
        }
    }

    @Override
    public String getAtyTitle() {
        return  getIntent().getFlags()==1001?"寄递企业或网点":"物流企业或网点";
    }

    @Override
    public boolean enableBackUpBtn() {
        return true;
    }

}
