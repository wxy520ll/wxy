package cn.net.xinyi.xmjt.activity.Collection.House;


import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.alibaba.fastjson.util.TypeUtils;

import cn.net.xinyi.xmjt.R;
import cn.net.xinyi.xmjt.config.BaseActivity2;
import cn.net.xinyi.xmjt.utils.AnnotateManager;
import cn.net.xinyi.xmjt.utils.BindView;
import cn.net.xinyi.xmjt.utils.UI;

public class HouseInfoAty extends BaseActivity2 implements View.OnClickListener {
    /**
     * 楼栋编号
     **/
    @BindView(id = R.id.et_ldbh)
    private EditText et_ldbh;

    @BindView(id = R.id.btn_upl,click = true)
    private Button btn_upl;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.aty_house_info);
        AnnotateManager.initBindView(this);//注解式绑定控件
        TypeUtils.compatibleWithJavaBean = true;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btn_upl:
                if (et_ldbh.getText().toString().isEmpty()){
                    UI.toast(this,"楼栋编号不能为空！");
                }else {
                    Intent intent=new Intent(this,HouseCheckActivity.class);
                    intent.putExtra("ldbh",et_ldbh.getText().toString());
                    startActivity(intent);
                }
                break;
        }
    }

    @Override
    public boolean enableBackUpBtn() {
        return true;
    }


    @Override
    public String getAtyTitle() {
        return getString(R.string.house_info);
    }
}
