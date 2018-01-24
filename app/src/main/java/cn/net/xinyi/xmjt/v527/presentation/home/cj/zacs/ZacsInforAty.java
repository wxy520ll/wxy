package cn.net.xinyi.xmjt.v527.presentation.home.cj.zacs;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.alibaba.fastjson.JSONObject;
import com.baidu.location.BDLocation;
import com.baidu.mapapi.map.BaiduMap;
import com.xinyi_tech.comm.util.ImageLoaderUtils;
import com.xinyi_tech.comm.util.ToastyUtil;
import com.xinyi_tech.comm.util.ToolbarUtils;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;
import cn.net.xinyi.xmjt.R;
import cn.net.xinyi.xmjt.activity.ZHFK.PlcBx.PickupMapActivity;
import cn.net.xinyi.xmjt.model.LocaionInfo;
import cn.net.xinyi.xmjt.utils.StringUtils;
import cn.net.xinyi.xmjt.v527.base.BaseLocationActivity;
import cn.net.xinyi.xmjt.v527.presentation.home.cj.zacs.adapter.ZacsXCAdapter;
import cn.net.xinyi.xmjt.v527.presentation.home.cj.zacs.mode.ZacjModel;
import cn.net.xinyi.xmjt.v527.presentation.home.cj.zacs.mode.ZacjXcModel;
import cn.net.xinyi.xmjt.v527.presentation.home.cj.zacs.presenter.ZacsInforPresenter;


/**
 * Created by jiajun.wang on 2018/1/23.
 */

public class ZacsInforAty extends BaseLocationActivity<ZacsInforPresenter> {

    @BindView(R.id.tool_bar)
    Toolbar toolBar;
    @BindView(R.id.placeName)
    TextView placeName;
    @BindView(R.id.tvGK)
    TextView tvGK;
    @BindView(R.id.tvGX)
    TextView tvGX;
    @BindView(R.id.tvLine1)
    TextView tvLine1;
    @BindView(R.id.tvLine2)
    TextView tvLine2;
    @BindView(R.id.tvNickName)
    TextView tvNickName;
    @BindView(R.id.tvAddress)
    TextView tvAddress;
    @BindView(R.id.tvGps)
    TextView tvGps;
    @BindView(R.id.placeImageView)
    ImageView placeImageView;
    @BindView(R.id.mRecyclerView)
    RecyclerView mRecyclerView;
    @BindView(R.id.childView1)
    LinearLayout childView1;
    @BindView(R.id.childView2)
    LinearLayout childView2;
    @BindView(R.id.et_name)
    EditText etName;
    @BindView(R.id.et_dz)
    EditText etDz;
    @BindView(R.id.tv_zb)
    TextView tvZb;
    @BindView(R.id.tv_sxwz)
    TextView tvSxwz;
    @BindView(R.id.tv_sddw)
    TextView tvSddw;
    @BindView(R.id.btUpdate)
    Button btUpdate;
    @BindView(R.id.csxc)
    Button csxc;

    private ZacjModel zacjModel;
    private BDLocation locaionInfo;
    private double jd;
    private double wd;
    private ZacsXCAdapter zacsXCAdapter;
    private List<ZacjXcModel> objects=new ArrayList<>();

    @Override
    protected void onCreateAfter(Bundle savedInstanceState) {
        super.onCreateAfter(savedInstanceState);
        ToolbarUtils.with(this, toolBar)
                .setSupportBack(true)
                .setTitle("场所查询", true)
                .build();
        zacjModel = (ZacjModel) getIntent().getSerializableExtra("itemData");
        tvNickName.setText(zacjModel.getNAME());
        tvAddress.setText("地址:" + zacjModel.getDZ());
        tvGps.setText("坐标:(" + zacjModel.getJD() + "," + zacjModel.getWD() + ")");
        ImageLoaderUtils.showImage(placeImageView, zacjModel.getIV_MPHQJT());

        etName.setText(zacjModel.getNAME());
        etDz.setText(zacjModel.getDZ());
        tvZb.setText("(" + zacjModel.getJD() + "," + zacjModel.getWD() + ")");

        zacsXCAdapter=new ZacsXCAdapter(R.layout.adapter_zacx,objects);

        JSONObject jsonObject = new JSONObject();
        jsonObject.put("ZAJCXXID", zacjModel.getID());
        jsonObject.put("JCYH", zacjModel.getCJYH());
        jsonObject.put("PAGESIZE",5);
        jsonObject.put("PAGENUMBER",1);
        mPresenter.queryZacsList(jsonObject);
    }

    @Override
    protected void locationState(int state, BDLocation locaionInfo) {
        super.locationState(state, locaionInfo);
        this.locaionInfo = locaionInfo;
        if (state == LOCATION_SUCCESS) {
            etDz.setText(locaionInfo.getAddrStr());
            tvZb.setText("(" + locaionInfo.getLatitude() + "," + locaionInfo.getLongitude() + ")");
            zacjModel.setJD("" + locaionInfo.getLongitude());
            zacjModel.setWD("" + locaionInfo.getLatitude());
        } else if (state == LOCATION_START) {
            etDz.setText("正在定位中....");
        } else {
            ToastyUtil.errorShort("定位失败");
            etDz.setText("定位出现问题,请重新定位");
        }
    }


    @Override
    protected BaiduMap getMap() {
        return null;
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_zacs;
    }

    @Override
    protected ZacsInforPresenter getPresenter() {
        return new ZacsInforPresenter();
    }


    @Override
    public void doParseData(int requestCode, Object data) {
        if (requestCode == ZacsInforPresenter.UPDATESUCCESS) {
            boolean bl = (boolean) data;
            if (bl) {
                tvNickName.setText(zacjModel.getNAME());
                tvAddress.setText("地址:" + zacjModel.getDZ());
                tvGps.setText("坐标:(" + zacjModel.getJD() + "," + zacjModel.getWD() + ")");
                ToastyUtil.successShort("更新成功");

                tvNickName.setText(zacjModel.getNAME());
                tvAddress.setText("地址:" + zacjModel.getDZ());
                tvGps.setText("坐标:(" + zacjModel.getJD() + "," + zacjModel.getWD() + ")");
            } else {
                ToastyUtil.errorShort("更新失败");
            }
        } else if (requestCode == ZacsInforPresenter.QUERYDATA) {
            objects.clear();
            List<ZacjXcModel> objs= (List<ZacjXcModel>) data;
            objects.addAll(objs);
            zacsXCAdapter.notifyDataSetChanged();
        }
    }

    @OnClick({R.id.tvGK, R.id.tvGX, R.id.tv_sddw, R.id.btUpdate, R.id.tv_sxwz,R.id.csxc})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.tvGK:
                tvGK.setTextColor(Color.parseColor("#7D7DFF"));
                tvGX.setTextColor(Color.parseColor("#4d4d4d"));
                childView1.setVisibility(View.VISIBLE);
                childView2.setVisibility(View.GONE);
                tvLine1.setBackgroundColor(Color.parseColor("#7D7DFF"));
                tvLine2.setBackgroundColor(Color.parseColor("#00000000"));
                break;
            case R.id.tvGX:
                tvGK.setTextColor(Color.parseColor("#4d4d4d"));
                tvGX.setTextColor(Color.parseColor("#7D7DFF"));
                tvLine1.setBackgroundColor(Color.parseColor("#00000000"));
                tvLine2.setBackgroundColor(Color.parseColor("#7D7DFF"));
                childView2.setVisibility(View.VISIBLE);
                childView1.setVisibility(View.GONE);
                break;
            case R.id.btUpdate:
                if (StringUtils.isEmpty(etName.getText().toString())) {
                    ToastyUtil.warningShort("请输入名称");
                    break;
                }
                if (StringUtils.isEmpty(etDz.getText().toString())) {
                    ToastyUtil.warningShort("请选择地址");
                    break;
                }
                zacjModel.setNAME(etName.getText().toString());
                mPresenter.updateAddress(zacjModel);
                break;
            case R.id.tv_sxwz:
                mLocationClient.start();
                break;
            case R.id.tv_sddw:
                Intent intent = new Intent(this, PickupMapActivity.class);
                startActivityForResult(intent, PickupMapActivity.MAP_PICK_UP);
                break;
            case R.id.csxc:
                Intent intent1 = new Intent(this, ZacsXCAty.class);
                intent1.putExtra("ID",zacjModel.getID());

                startActivity(intent1);
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (PickupMapActivity.MAP_PICK_UP == requestCode) { //地图选点
            if (Activity.RESULT_OK == resultCode) {
                LocaionInfo info = (LocaionInfo) (data.getBundleExtra("data").get("data"));
                etDz.setText(info.getAddress());
                tvZb.setText("(" + info.getLat() + "," + info.getLongt() + ")");
                jd = info.getLongt();
                wd = info.getLat();
                zacjModel.setJD("" + jd);
                zacjModel.setWD("" + wd);
                zacjModel.setDZ(info.getAddress());
            }
        }
    }


}
