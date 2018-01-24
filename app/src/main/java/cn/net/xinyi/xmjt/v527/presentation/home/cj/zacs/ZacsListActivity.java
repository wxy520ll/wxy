package cn.net.xinyi.xmjt.v527.presentation.home.cj.zacs;

import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.widget.TextView;

import com.baidu.location.BDLocation;
import com.baidu.mapapi.map.BaiduMap;
import com.blankj.utilcode.util.ActivityUtils;
import com.blankj.utilcode.util.FragmentUtils;
import com.blankj.utilcode.util.StringUtils;
import com.xinyi_tech.comm.base.BasePresenter;
import com.xinyi_tech.comm.form.DictField;
import com.xinyi_tech.comm.util.ToastyUtil;
import com.xinyi_tech.comm.util.ToolbarUtils;
import com.xinyi_tech.comm.widget.seacher.SuperSearchView;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.OnClick;
import cn.net.xinyi.xmjt.R;
import cn.net.xinyi.xmjt.v527.base.BaseLocationActivity;
import cn.net.xinyi.xmjt.v527.presentation.home.cj.zacs.fragment.ZacsCjListFragment;
import cn.net.xinyi.xmjt.v527.presentation.home.cj.zacs.mode.ZacjQueryCondition;
import cn.net.xinyi.xmjt.v527.widget.DictView;

public class ZacsListActivity extends BaseLocationActivity<BasePresenter> {
    @BindView(R.id.tool_bar)
    Toolbar toolBar;
    @BindView(R.id.tv_address)
    TextView tv_address;
    @BindView(R.id.dict_view)
    DictView dictView;
    @BindView(R.id.seacher_view)
    SuperSearchView seacherView;
    BDLocation locaionInfo;

    @Override
    protected void onCreateAfter(Bundle savedInstanceState) {
        super.onCreateAfter(savedInstanceState);
        ToolbarUtils.with(this, toolBar).setSupportBack(true).setTitle("场所查询", true).setInflateMenu(R.menu.menu_zacscj).build();
        FragmentUtils.add(getSupportFragmentManager(), new ZacsCjListFragment(), R.id.fl_content);
        toolBar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                ActivityUtils.startActivity(ZacsListActivity.this, ZacsMainAty.class);
                return true;
            }
        });
        final ArrayList<DictField> dictFields = new ArrayList<>();
        dictFields.add(new DictField("50米", "50"));
        dictFields.add(new DictField("100米", "100"));
        dictFields.add(new DictField("500米", "500"));
        dictFields.add(new DictField("1000米", "1000"));
        dictFields.add(new DictField("1500米", "1500"));
        dictView.setData(this, dictFields, null);
        seacherView.setOnQueryChangeListener(new SuperSearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                search();
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        });

    }


    private void search() {
        ZacjQueryCondition zacjQueryCondition = new ZacjQueryCondition();
        final String value = dictView.getValue();
        if (!StringUtils.isEmpty(value)) {
            zacjQueryCondition.setJL(value);
        }
        zacjQueryCondition.setJD(locaionInfo.getLongitude() + "");
        zacjQueryCondition.setWD(locaionInfo.getLatitude() + "");
        zacjQueryCondition.setMC(seacherView.getText());
        EventBus.getDefault().post(zacjQueryCondition);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_zacs_cj_list;
    }

    @Override
    protected BaiduMap getMap() {
        return null;
    }


    @Override
    protected BasePresenter getPresenter() {
        return new BasePresenter();
    }

    @Override
    public void doParseData(int requestCode, Object data) {

    }

    @Override
    protected void locationState(int state, BDLocation locaionInfo) {
        super.locationState(state, locaionInfo);
        if (state == LOCATION_SUCCESS) {
            this.locaionInfo = locaionInfo;
            tv_address.setText(locaionInfo.getAddrStr());
            search();
            //ToastyUtil.successShort("您当前位置:" + locaionInfo.getAddrStr());
        } else if (state == LOCATION_START) {
            tv_address.setText("正在定位中....");
        } else {
            ToastyUtil.errorShort("定位失败");
            tv_address.setText("定位出现问题,请重新定位");
        }
    }


    @OnClick(R.id.tv_location)
    public void onViewClicked() {
        loc();
    }
}
