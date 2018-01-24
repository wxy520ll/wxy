package cn.net.xinyi.xmjt.v527.presentation.home.cj.rycj;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.TextView;

import com.baidu.location.BDLocation;
import com.baidu.mapapi.map.BaiduMap;
import com.blankj.utilcode.util.ActivityUtils;
import com.xinyi_tech.comm.form.FieldView;
import com.xinyi_tech.comm.form.FormLayout;
import com.xinyi_tech.comm.util.ToastyUtil;
import com.xinyi_tech.comm.util.ToolbarUtils;
import com.xinyi_tech.comm.widget.picker.SuperImageView;

import butterknife.BindView;
import butterknife.OnClick;
import cn.net.xinyi.xmjt.R;
import cn.net.xinyi.xmjt.activity.ZHFK.PlcBx.PickupMapActivity;
import cn.net.xinyi.xmjt.model.LocaionInfo;
import cn.net.xinyi.xmjt.v527.base.BaseLocationActivity;
import cn.net.xinyi.xmjt.v527.presentation.home.cj.rycj.mode.RycjOtherModel;
import cn.net.xinyi.xmjt.v527.presentation.home.cj.rycj.presenter.RycjPresenter;

public class RycjOtherActivity extends BaseLocationActivity<RycjPresenter> {

    public static final int NET_ADD = 1;

    @BindView(R.id.tool_bar)
    Toolbar toolBar;
    @BindView(R.id.tv_address)
    TextView tv_address;
    @BindView(R.id.img_zlz)
    SuperImageView imgZlz;
    @BindView(R.id.img_clz)
    SuperImageView imgClz;
    @BindView(R.id.img_qsz)
    SuperImageView imgQsz;
    @BindView(R.id.form)
    FormLayout form;


    @OnClick({R.id.tv_sxwz, R.id.tv_sddw, R.id.sbtn_next})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.tv_sxwz:
                loc();
                break;
            case R.id.tv_sddw:
                Intent intent = new Intent(this, PickupMapActivity.class);
                startActivityForResult(intent, PickupMapActivity.MAP_PICK_UP);
                break;
            case R.id.sbtn_next:
                if (form.checkForm(FormLayout.ActionFieldType.FIELD_TYPE_VISIBLE)) {
                    final RycjOtherModel rycjOtherModel = form.getParams(RycjOtherModel.class, FormLayout.ActionFieldType.FIELD_TYPE_VISIBLE_HIDDLEN);
                    if (rycjOtherModel.getX() == null) {
                        ToastyUtil.warningShort("请选择地址");
                        return;
                    }
                    rycjOtherModel.setPerson_type(getIntent().getIntExtra("personType", -1) + "");
                    mPresenter.addPersonidentity(rycjOtherModel, NET_ADD);
                }
                break;
        }
    }

    @Override
    protected void onCreateAfter(Bundle savedInstanceState) {
        super.onCreateAfter(savedInstanceState);
        ToolbarUtils.with(this, toolBar).setTitle(getIntent().getStringExtra("title"), true).setSupportBack(true).build();
        imgZlz.with(this).setRequestCode(1);
        imgClz.with(this).setRequestCode(2);
        imgQsz.with(this).setRequestCode(3);

    }

    @Override
    protected BaiduMap getMap() {
        return null;
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_rycj_other;
    }

    @Override
    protected RycjPresenter getPresenter() {
        return new RycjPresenter();
    }

    @Override
    public void doParseData(int requestCode, Object data) {
        if (requestCode == NET_ADD) {
            ToastyUtil.successShort("提交成功");
            ActivityUtils.finishActivity(this);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        imgZlz.onActivityResult(requestCode, resultCode, data);
        imgClz.onActivityResult(requestCode, resultCode, data);
        imgQsz.onActivityResult(requestCode, resultCode, data);
        if (PickupMapActivity.MAP_PICK_UP == requestCode && Activity.RESULT_OK == resultCode && data != null) { //地图选点
            LocaionInfo info = (LocaionInfo) (data.getBundleExtra("data").get("data"));
            setAddress(info.getAddress(), info.getLat(), info.getLongt());
        }
    }

    @Override
    protected void locationState(int state, BDLocation locaionInfo) {
        super.locationState(state, locaionInfo);
        if (state == LOCATION_START) {
            tv_address.setText("正在定位中....");
        } else if (state == LOCATION_SUCCESS) {
            setAddress(locaionInfo.getAddrStr(), locaionInfo.getLatitude(), locaionInfo.getLongitude());
        } else {
            tv_address.setText("定位出现问题,请点击刷新或者手动定位");
        }
    }


    private void setAddress(String address, double lat, double lont) {
        final FieldView x = form.getFieldViewByName("x", FormLayout.ActionFieldType.FIELD_TYPE_VISIBLE_HIDDLEN);
        final FieldView y = form.getFieldViewByName("y", FormLayout.ActionFieldType.FIELD_TYPE_VISIBLE_HIDDLEN);
        tv_address.setText(address);
        x.setValue(lat + "");
        y.setValue(lont + "");
    }


}
