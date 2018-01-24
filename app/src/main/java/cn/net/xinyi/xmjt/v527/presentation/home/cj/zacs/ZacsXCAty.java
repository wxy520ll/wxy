package cn.net.xinyi.xmjt.v527.presentation.home.cj.zacs;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioButton;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.xinyi_tech.comm.picker.picture.entity.LocalMedia;
import com.xinyi_tech.comm.util.ToolbarUtils;
import com.xinyi_tech.comm.widget.picker.SuperImageView;
import com.xinyi_tech.comm.widget.picker.SuperMutiPickerView;

import java.util.HashMap;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;
import cn.net.xinyi.xmjt.R;
import cn.net.xinyi.xmjt.v527.base.BaseJwtActivity;
import cn.net.xinyi.xmjt.v527.presentation.home.cj.zacs.presenter.ZacsInforPresenter;
import cn.net.xinyi.xmjt.v527.presentation.task.csxc.SignatureActivity;

import static cn.net.xinyi.xmjt.v527.presentation.home.cj.zacs.presenter.ZacsInforPresenter.ADDDATE;
import static cn.net.xinyi.xmjt.v527.presentation.home.cj.zacs.presenter.ZacsInforPresenter.ADDJSON;
import static cn.net.xinyi.xmjt.v527.presentation.home.cj.zacs.presenter.ZacsInforPresenter.UPLOADFILE;



/**
 * Created by jiajun.wang on 2018/1/24.
 */

public class ZacsXCAty extends BaseJwtActivity<ZacsInforPresenter> {

    @BindView(R.id.tool_bar)
    Toolbar toolBar;
    @BindView(R.id.btUpload)
    Button btUpload;
    @BindView(R.id.rfYButton)
    RadioButton rfYButton;
    @BindView(R.id.rfNButton)
    RadioButton rfNButton;
    @BindView(R.id.wfYButton)
    RadioButton wfYButton;
    @BindView(R.id.wfNButton)
    RadioButton wfNButton;
    @BindView(R.id.jfYButton)
    RadioButton jfYButton;
    @BindView(R.id.jfNButton)
    RadioButton jfNButton;
    @BindView(R.id.xfYButton)
    RadioButton xfYButton;
    @BindView(R.id.xfNButton)
    RadioButton xfNButton;
    @BindView(R.id.ivQM)
    SuperImageView ivQM;
    @BindView(R.id.jianyi1)
    EditText jianyi1;
    @BindView(R.id.rfImages)
    SuperMutiPickerView rfImages;
    @BindView(R.id.childView1)
    LinearLayout childView1;
    @BindView(R.id.jianyi2)
    EditText jianyi2;
    @BindView(R.id.wfImages)
    SuperMutiPickerView wfImages;
    @BindView(R.id.childView2)
    LinearLayout childView2;
    @BindView(R.id.jianyi3)
    EditText jianyi3;
    @BindView(R.id.jfImages)
    SuperMutiPickerView jfImages;
    @BindView(R.id.childView3)
    LinearLayout childView3;
    @BindView(R.id.jianyi4)
    EditText jianyi4;
    @BindView(R.id.xfImages)
    SuperMutiPickerView xfImages;
    @BindView(R.id.childView4)
    LinearLayout childView4;
    private JSONArray jsonArray=null;


    @Override
    protected void onCreateAfter(Bundle savedInstanceState) {

        jfImages.setupParams(new SuperMutiPickerView.Builder(this).requestCode(1001).maxSelectCount(3).isOnlyPicture(true));
        xfImages.setupParams(new SuperMutiPickerView.Builder(this).requestCode(1001).maxSelectCount(3).isOnlyPicture(true));
        wfImages.setupParams(new SuperMutiPickerView.Builder(this).requestCode(1002).maxSelectCount(3).isOnlyPicture(true));
        rfImages.setupParams(new SuperMutiPickerView.Builder(this).requestCode(1003).maxSelectCount(3).isOnlyPicture(true));

        ToolbarUtils.with(this, toolBar)
                .setSupportBack(true)
                .setTitle("场所巡查", true)
                .build();
        ivQM.setOnImageViewClickListener(new SuperImageView.OnImageViewClickListener() {
            @Override
            public boolean onClick(boolean hasDrawable) {
                final Intent intent = new Intent(ZacsXCAty.this, SignatureActivity.class);
                ZacsXCAty.this.startActivityForResult(intent, 1);
                return true;
            }
        });
        rfNButton.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                childView1.setVisibility(isChecked ? View.VISIBLE : View.GONE);
            }
        });
        wfNButton.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                childView2.setVisibility(isChecked ? View.VISIBLE : View.GONE);
            }
        });
        jfNButton.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                childView3.setVisibility(isChecked ? View.VISIBLE : View.GONE);
            }
        });


        xfNButton.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                childView4.setVisibility(isChecked ? View.VISIBLE : View.GONE);
            }
        });
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_zacsxc;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == 1 && data != null) {
            ivQM.show(data.getStringExtra("qmzp"));
        }
        jfImages.onActivityResult(requestCode,resultCode,data);
        xfImages.onActivityResult(requestCode,resultCode,data);
        wfImages.onActivityResult(requestCode,resultCode,data);
        rfImages.onActivityResult(requestCode,resultCode,data);
        super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    protected ZacsInforPresenter getPresenter() {
        return new ZacsInforPresenter();
    }

    @Override
    public void doParseData(int requestCode, Object data) {
        if (requestCode==UPLOADFILE){
            jsonArray= JSON.parseArray(data.toString());
            JSONObject jsonObject=new JSONObject();
            jsonObject.put("ZAJCXXID",getIntent().getIntExtra("ID",0));
            jsonObject.put("RFSFHG", rfYButton.isChecked() ? "0" : "1");
            jsonObject.put("WFSFHG", wfYButton.isChecked() ? "0" : "1");
            jsonObject.put("XFSFHG", xfYButton.isChecked() ? "0" : "1");
            jsonObject.put("JFSFHG", jfYButton.isChecked() ? "0" : "1");
            jsonObject.put("JCYH", "18566266518");
            mPresenter.addZajc(jsonObject);
        }else if (requestCode==ADDDATE){
            JSONObject jsonObject= (JSONObject) data;
            String fileID=jsonObject.getString("ID");
            JSONArray arrays=new JSONArray();
            if (jsonObject!=null){
                for (int i = 0; i < jsonArray.size(); i++) {
                    JSONObject obj=new JSONObject();
                    obj.put("F_ID",fileID);
                    obj.put("TPFLX","XXCJ_ZAJCXX_JC");
                    JSONObject j = jsonArray.getJSONObject(i);
                    String key = j.keySet().iterator().next();
                    if (key.equals("4")){
                        obj.put("TPLX","RF");
                        obj.put("PATH",j.getString("4"));
                    }else if (key.equals("3")){
                        obj.put("TPLX","WF");
                        obj.put("PATH",j.getString("3"));
                    }else if (key.equals("2")){
                        obj.put("TPLX","XF");
                        obj.put("PATH",j.getString("2"));
                    }else if (key.equals("1")){
                        obj.put("TPLX","JF");
                        obj.put("PATH",j.getString("1"));
                    }else {
                        obj.put("TPLX","RYQM");
                        obj.put("PATH",j.getString("5"));
                    }
                    arrays.set(i,obj);
                }
                JSONObject jsonObject1=new JSONObject();
                JSONObject jsonObject2=new JSONObject();
                jsonObject2.put("FILES",arrays.toJSONString());
                jsonObject1.put("json",jsonObject2.toJSONString());
                mPresenter.addJson(jsonObject1);
            }
        }else if (ADDJSON==requestCode){
            Boolean s= (Boolean) data;
        }
    }


    @OnClick(R.id.btUpload)
    public void onViewClicked() {
        HashMap<List<LocalMedia>,Integer>map=new HashMap<>();
        List<LocalMedia> selectImage1 = jfImages.getSelectImage();
        List<LocalMedia> selectImage2 = xfImages.getSelectImage();
        List<LocalMedia> selectImage3 = wfImages.getSelectImage();
        List<LocalMedia> selectImage4 = rfImages.getSelectImage();
        map.put(selectImage1,1);
        map.put(selectImage2,2);
        map.put(selectImage3,3);
        map.put(selectImage4,4);


        mPresenter.uploadImages(map,ivQM.getImgPath());
    }
}
