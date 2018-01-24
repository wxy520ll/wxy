package cn.net.xinyi.xmjt.activity.Collection.SafetyManage.OneCamera;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.nostra13.universalimageloader.core.ImageLoader;

import org.apache.http.Header;

import java.util.Collection;
import java.util.List;
import java.util.Map;

import cn.net.xinyi.xmjt.R;
import cn.net.xinyi.xmjt.api.ApiHttpClient;
import cn.net.xinyi.xmjt.api.ApiResource;
import cn.net.xinyi.xmjt.config.AdapterHolder;
import cn.net.xinyi.xmjt.config.BaseListAty;
import cn.net.xinyi.xmjt.config.XYAdapter;
import cn.net.xinyi.xmjt.model.MachineFowModle;
import cn.net.xinyi.xmjt.model.MachineModle;
import cn.net.xinyi.xmjt.utils.BaseDataUtils;
import cn.net.xinyi.xmjt.utils.DialogHelper;
import cn.net.xinyi.xmjt.utils.UI;

public class MachineApprovalCheckDetaAty extends BaseListAty implements View.OnClickListener {

    private View view;
    private TextView tv_sqdw;
    private TextView tv_sqr;
    private TextView tv_sqsj;
    private TextView tv_jfmc;
    private TextView tv_spld;
    private TextView tv_wcsj;
    private TextView tv_jfgly;
    private Map<String, String> map;
    private List<MachineFowModle> fowModles;
    private Button btn_tg;
    private Button btn_btg;
    private ImageView iv_zc;
    private ImageView iv_sq;
    private LinearLayout ll_tops;
    private String sfzzp;
    private String scjfcrz;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        map= (Map<String, String>) getIntent().getSerializableExtra("DATA");
        initData();
    }

    private void initData() {
        tv_spld.setText("审批人："+map.get("SHRYNAME")+"("+map.get("SHRY")+")");
        tv_sqdw.setText("施工公司："+map.get("COMPANYNAME"));
        tv_jfmc.setText("机房名称："+map.get("MC")+"("+map.get("ZDZ")+")");
        tv_sqr.setText("申请人员："+map.get("SQRYNAME")+"("+map.get("SJHM")+")");
        tv_sqsj.setText("申请时间："+map.get("SQSJ"));
        tv_wcsj.setText("完成时间："+map.get("SGWCSJ"));
        //机房ID
        Object operType = map.get("ROOMID");
        if(operType instanceof String){
            machinePerson(Integer.valueOf((String)operType));
        }else{
            machinePerson(((Integer)operType));
        }
        if (Integer.parseInt(map.get("SHJG"))==0&& BaseDataUtils.isCompanyOrOther()!=1){
            ll_tops.setVisibility(View.VISIBLE);
        }


        //注册时的图片
        sfzzp= ApiHttpClient.IMAGE_HOST + "/user/" +map.get("SFZZP");
        ImageLoader.getInstance().displayImage(sfzzp,iv_zc);
        //申请时图片
        scjfcrz= ApiHttpClient.IMAGE_HOST + "/fellow/" +map.get("SCJFCRZ");
        ImageLoader.getInstance().displayImage(scjfcrz,iv_sq);
        initAdapter();
    }

    @Override
    protected void requestData() {}

    private void initAdapter() {
        if (null!=map.get("DATALIST")){
            fowModles =JSON.parseArray(map.get("DATALIST"), MachineFowModle.class);
            if (null!=fowModles&&fowModles.size()>0){
                CheckDetaAdapter adapter=new CheckDetaAdapter(mListView,fowModles,R.layout.aty_machine_approval_check_deta_item,MachineApprovalCheckDetaAty.this);
                mListView.setAdapter(adapter);
            }else {
                toast("没有同行人员！");
            }
        }
        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

            }
        });
    }

    //顶部提示
    @Override
    public void setupTopView(LinearLayout parent) {
        super.setupTopView(parent);
        view=LayoutInflater.from(this).inflate(R.layout.aty_machine_approval_check_deta,parent);
        parent.setVisibility(View.VISIBLE);
        tv_spld =(TextView)view.findViewById(R.id.tv_spld);
        tv_jfgly =(TextView)view.findViewById(R.id.tv_jfgly);
        tv_sqdw =(TextView)view.findViewById(R.id.tv_sqdw);
        tv_sqr=(TextView)view.findViewById(R.id.tv_sqr);
        tv_sqsj=(TextView)view.findViewById(R.id.tv_sqsj);
        tv_jfmc=(TextView)view.findViewById(R.id.tv_jfmc);
        tv_wcsj=(TextView)view.findViewById(R.id.tv_wcsj);
        iv_zc=(ImageView)view.findViewById(R.id.iv_zc);
        iv_sq=(ImageView)view.findViewById(R.id.iv_sq);
        ll_tops=(LinearLayout) view.findViewById(R.id.ll_tops);
        btn_tg=(Button)view.findViewById(R.id.btn_tg);
        btn_btg=(Button)view.findViewById(R.id.btn_btg);
        btn_tg.setOnClickListener(this);
        btn_btg.setOnClickListener(this);
        iv_zc.setOnClickListener(this);
        iv_sq.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btn_tg:
                updateInfo(1,null);
                break;
            case R.id.btn_btg:
                showAlert();
                break;
            case R.id.iv_zc:
                DialogHelper.showPopMenu(this,iv_zc,sfzzp);
                break;
            case R.id.iv_sq:
                DialogHelper.showPopMenu(this,iv_sq,scjfcrz);
                break;
        }
    }

    private void showAlert() {
        final EditText editText = new EditText(this);
        editText.setLines(5);
        editText.setHint("请输入不通过的原因");
        editText.setTextSize(14);
        editText.setBackgroundColor(getResources().getColor(R.color.white));
        new AlertDialog.Builder(this).setTitle("审核不通过").setView(editText).setNegativeButton("取消", null).setPositiveButton("确定", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if(editText.getText().toString().isEmpty()){
                    UI.toast(MachineApprovalCheckDetaAty.this,"请输入请输入不通过的原因！");
                }else {
                    updateInfo(2,editText.getText().toString());
                }
            }
        }).show();
    }

    //更新上传采集数据到服务端
    public void updateInfo(int shjg,String reston) {
        JSONObject jsonObject=new JSONObject();
        jsonObject.put("ID",map.get("ID"));
        jsonObject.put("SHJG",shjg);
        if (null!=reston){
            jsonObject.put("BZ",reston);
        }
        String json = jsonObject.toJSONString();
        ApiResource.MachineRoomCheckUpdate(json, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int i, Header[] headers, byte[] bytes) {
                String result = new String(bytes);
                if (null!=result&&result.startsWith("true")){
                    requestData();
                    setResult(RESULT_OK);
                    MachineApprovalCheckDetaAty.this.finish();
                    UI.toast(MachineApprovalCheckDetaAty.this,"审核结果上传成功");
                }else {
                    onFailure(i,headers,bytes,null);
                }
            }
            @Override
            public void onFailure(int i, Header[] headers, byte[] bytes, Throwable throwable) {
                UI.toast(MachineApprovalCheckDetaAty.this,"上传失败，可能当前上传的人数较多，请稍候重试！");
            }
        });
    }





    //获取管理员信息
    private void machinePerson(int roomId) {
        showLoadding();
        JSONObject requestJson = new JSONObject();
        requestJson.put("ROOMID", roomId);
        ApiResource.MachineRoomPersonList(requestJson.toJSONString(),new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int i, Header[] headers, byte[] bytes) {
                String result = new String(bytes);
                if (result!=null&&result.length()>5){
                    List<MachineModle> machineModles= JSON.parseArray(result, MachineModle.class);
                    StringBuilder stringBuilder=new StringBuilder();
                    for (MachineModle per:machineModles){
                        stringBuilder.append(per.getNAME()+"("+ per.getUSERNAME()+")"+"、");
                    }
                    tv_jfgly.setText("管理员："+stringBuilder.toString());
                }
                stopLoading();
            }
            @Override
            public void onFailure(int i, Header[] headers, byte[] bytes, Throwable throwable) {
                stopLoading();
                toast("获取数据失败");
            }
        });
    }




    class  CheckDetaAdapter extends XYAdapter<MachineFowModle> {
        Context context;

        public CheckDetaAdapter(AbsListView view, Collection<MachineFowModle> mDatas, int itemLayoutId, Context mContext) {
            super(view, mDatas, itemLayoutId, mContext);
            this.context=mContext;
        }

        @Override
        public void convert(AdapterHolder helper, final MachineFowModle item, boolean isScrolling) {
            helper.setText(R.id.tv_sqr,"申请人信息："+item.getXM());
            helper.setText(R.id.tv_sqhm,"手机号码："+item.getSJHM() );
            helper.setText(R.id.tv_sfzh," 身份证号码："+item.getSFZH() );
            final ImageView iv=(ImageView) helper.getConvertView().findViewById(R.id.iv_sfz);
            if (null!=item.getSCJFCRZZP()){
                final String path= ApiHttpClient.IMAGE_HOST + "/fellow/" +item.getSCJFCRZZP();
                ImageLoader.getInstance().displayImage(path,iv);
                iv.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        DialogHelper.showPopMenu((Activity) context,iv,path);
                    }
                });
            }


        }
    }




    @Override
    public String getAtyTitle() {
        return "审批详情";
    }

    @Override
    public boolean enableBackUpBtn() {
        return true;
    }
}
