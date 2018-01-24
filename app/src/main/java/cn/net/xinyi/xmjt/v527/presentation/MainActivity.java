package cn.net.xinyi.xmjt.v527.presentation;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.Toast;

import com.flyco.tablayout.CommonTabLayout;
import com.flyco.tablayout.listener.CustomTabEntity;
import com.xinyi_tech.comm.base.BaseFragment;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import cn.net.xinyi.xmjt.R;
import cn.net.xinyi.xmjt.config.AppContext;
import cn.net.xinyi.xmjt.config.AppManager;
import cn.net.xinyi.xmjt.model.UserInfo;
import cn.net.xinyi.xmjt.service.BaiduTraceService;
import cn.net.xinyi.xmjt.utils.DialogHelper;
import cn.net.xinyi.xmjt.utils.UI;
import cn.net.xinyi.xmjt.v527.base.BaseJwtActivity;
import cn.net.xinyi.xmjt.v527.manager.MainItemModel;
import cn.net.xinyi.xmjt.v527.manager.ModelItemManager;
import cn.net.xinyi.xmjt.v527.presentation.txl.TxlFragment;
import cn.net.xinyi.xmjt.v527.widget.FlexibleViewPager;


public class MainActivity extends BaseJwtActivity<MainPresenter> {


    @BindView(R.id.viewpager)
    FlexibleViewPager mViewpager;
    @BindView(R.id.bottomBar)
    CommonTabLayout mBottomBar;

    List<BaseFragment> fragmentLlist = new ArrayList<>();
    public final UserInfo userInfo = AppContext.instance.getLoginInfo();
    int currentItem = 0;

    @Override
    protected void onCreateAfter(Bundle savedInstanceState) {
        mPresenter.getInfoByCommand();//初始化数据
        initBottomBar();
        initViewpager();
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_main;
    }

    @Override
    protected MainPresenter getPresenter() {
        return new MainPresenter();
    }

    private void initBottomBar() {
        ArrayList<CustomTabEntity> mTabEntities = new ArrayList<>();
        final List<MainItemModel> tabByRole = ModelItemManager.getTabByRole();
        for (MainItemModel tab : tabByRole) {
            mTabEntities.add(new BotomBarTabEntity(tab));
            try {
                fragmentLlist.add((BaseFragment) tab.getClazz().newInstance());
            } catch (InstantiationException e) {
                e.printStackTrace();
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
        }

        mBottomBar.setTabData(mTabEntities);
        mBottomBar.setOnTabSelectListener(new com.flyco.tablayout.listener.OnTabSelectListener() {
            @Override
            public void onTabSelect(int position) {
                mViewpager.setCurrentItem(position);
            }

            @Override
            public void onTabReselect(int position) {

            }
        });

        mViewpager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                mBottomBar.setCurrentTab(position);
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

        mViewpager.setCurrentItem(currentItem);

    }


    private void initViewpager() {
        mViewpager.setCanScroll(true);
        mViewpager.setAdapter(new MainFragmentPagerAdapter());
        mViewpager.setOffscreenPageLimit(fragmentLlist.size());
        if (fragmentLlist.size() == 1) {
            mBottomBar.setVisibility(View.GONE);
        }

    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case 100:
                if (data != null) {
                    Bundle bundle = data.getExtras();
                    final String code = bundle.getString("result");
                    if (code != null) {
                        doConfirmAndLogin(code);
                    } else {
                        Toast.makeText(this, R.string.msg_scan_fail, Toast.LENGTH_LONG).show();
                    }

                }

                break;
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    private void doConfirmAndLogin(final String code) {
        if (code.startsWith("xyjwt-")) {
            DialogHelper.showAlertDialog(getString(R.string.msg_scan_login_title), this, new DialogHelper.OnOptionClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which, Object o) {
                    {
                        if (null != userInfo.getUsername()) {
                            mPresenter.loginByScan(code);
                        }
                    }
                }
            });
        } else {
            UI.toast(MainActivity.this, getString(R.string.msg_scan_error));
        }
    }


    @Override
    public void onBackPressed() {
        final BaseFragment baseFragment = fragmentLlist.get(mBottomBar.getCurrentTab());
        if (baseFragment instanceof TxlFragment) {
            TxlFragment txlFragment = (TxlFragment) baseFragment;
            if (txlFragment.isScrollToStart()) {
                return;
            }
        }

        exit();
    }

    /**
     * 退出
     */
    private long mExitTime = 0;

    private void exit() {
        if (System.currentTimeMillis() - mExitTime > 2000) {
            Toast.makeText(this, "再按一次退出程序", Toast.LENGTH_SHORT).show();
            mExitTime = System.currentTimeMillis();
        } else {
            this.stopService(new Intent(MainActivity.this, BaiduTraceService.class));
            finish();
            // System.exit(0);
        }
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        AppManager.getAppManager().finishActivity(this);
    }

    @Override
    public void doOnStart(int requestCode) {
        if (requestCode == MainPresenter.NET_GETINFOBYCOMMAND) {

        } else {
            super.doOnStart(requestCode);
        }

    }

    @Override
    public void doParseData(int requestCode, Object data) {
        if (requestCode == MainPresenter.NET_GETINFOBYCOMMAND) {
        } else if (requestCode == MainPresenter.NET_LOGINBYSCAN) {
            if (((String) data).contains("SUCCESS")) {
                UI.toast(MainActivity.this, getString(R.string.msg_scan_login_success));
            } else {
                UI.toast(MainActivity.this, getString(R.string.msg_scan_login_fail));
            }
        }
    }


    class MainFragmentPagerAdapter extends FragmentPagerAdapter {
        public MainFragmentPagerAdapter() {
            super(MainActivity.this.getSupportFragmentManager());
        }

        @Override
        public Fragment getItem(int position) {
            return fragmentLlist.get(position);
        }

        @Override
        public int getCount() {
            return fragmentLlist.size();
        }
    }
}
