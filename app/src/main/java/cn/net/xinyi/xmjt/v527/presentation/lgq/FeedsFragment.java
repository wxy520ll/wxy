package cn.net.xinyi.xmjt.v527.presentation.lgq;

import android.app.Activity;
import android.app.Application;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;

import com.blankj.utilcode.util.FragmentUtils;
import com.blankj.utilcode.util.Utils;
import com.umeng.analytics.MobclickAgent;
import com.umeng.comm.core.CommunitySDK;
import com.umeng.comm.core.beans.CommUser;
import com.umeng.comm.core.constants.Constants;
import com.umeng.comm.core.impl.CommunityFactory;
import com.umeng.comm.core.login.LoginListener;
import com.umeng.simplify.ui.fragments.CommunityMainFragment;
import com.xinyi_tech.comm.base.BaseFragment;
import com.xinyi_tech.comm.base.BasePresenter;
import com.xinyi_tech.comm.util.ResUtils;
import com.xinyi_tech.comm.util.ToolbarUtils;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import butterknife.BindView;
import cn.net.xinyi.xmjt.R;
import cn.net.xinyi.xmjt.config.AppContext;
import cn.net.xinyi.xmjt.model.UserInfo;
import cn.net.xinyi.xmjt.utils.XinyiLog;


/**
 * Created by zhiren.zhang on 2016/8/19.
 */
public class FeedsFragment extends BaseFragment<BasePresenter> implements View.OnClickListener {
    private static final String TAG = "TabMenuFragment";
    @BindView(R.id.tool_bar)
    Toolbar mToolBar;
    private CommunityMainFragment mFeedsFragment;
    UserInfo userInfo = AppContext.instance.getLoginInfo();

    @Override
    protected void requestData() {

    }

    @Override
    protected View getLayoutView() {
        View view = View.inflate(activity, R.layout.fragment_lgq, null);
        if (null != AppContext.instance().getLoginInfo().getPcs()) {
            mFeedsFragment = new CommunityMainFragment();
            FragmentUtils.add(activity.getSupportFragmentManager(), mFeedsFragment, R.id.umeng_comm_main_container);
        }
        return view;
    }

    @Override
    protected int getLayoutId() {
        return 0;
    }

    @Override
    protected void onCreateViewAfter(View view, Bundle savedInstanceState) {
        initToolbar();
        initUmengCommunity();
    }

    private void initToolbar() {
        ToolbarUtils.with(activity, mToolBar).setTitle(ResUtils.getString(R.string.app_fullname), true).build();
    }

    private void initUmengCommunity() {
        CommunitySDK communitySDK = CommunityFactory.getCommSDK(Utils.getApp());
        CommUser commUser = new CommUser();
        commUser.name = userInfo.getPcs() + userInfo.getName() + userInfo.getUsername();
        commUser.id = userInfo.getUsername();

        communitySDK.loginToUmengServerBySelfAccount(Utils.getApp(), commUser, new LoginListener() {
            @Override
            public void onStart() {
            }

            @Override
            public void onComplete(int i, CommUser commUser) {
                XinyiLog.d(i + "");
            }
        });
    }

    @Override
    protected BasePresenter getPresenter() {
        return new BasePresenter();
    }


    /**
     * 该代码仅仅在“一建生成apk”情况下被调用</br>
     */
    private void addLoginPlatforms() {
        boolean isFromGenerateApk = activity.getApplication().getClass().getSuperclass()
                .equals(Application.class);
        if (isFromGenerateApk) {
            try {
                Method method = activity.getApplication().getClass().getMethod("addLoginPlatforms",
                        Activity.class);
                method.invoke(null, this);
            } catch (NoSuchMethodException e) {
            } catch (IllegalArgumentException e) {
            } catch (IllegalAccessException e) {
            } catch (InvocationTargetException e) {
            }
        }
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.umeng_comm_back_btn) {// 点击返回按钮的情况
            activity.finish();
        }
    }


    /**
     * 处理退出登录后，回到FeedsActivity时的逻辑</br>
     */
    private void dealLogoutLoginc() {
        Bundle bundle = activity.getIntent().getExtras();
        if (bundle != null) {
            boolean fromLogout = bundle.getBoolean(Constants.FROM_COMMUNITY_LOGOUT);
            if (fromLogout) {
                // 此时需要将intent的数据更新，避免下次操作将数据带过来~
                activity.getIntent().putExtra(Constants.FROM_COMMUNITY_LOGOUT, false);
                mFeedsFragment.cleanAdapterData();
                mFeedsFragment.repeatLoadDataFromServer();
            }
        }
    }


    @Override
    public void doParseData(int requestCode, Object data) {

    }


    //共计三步，统计fragment第三步：因为本app包含fragment，要统计fragment，所以需要加这一句话  add20160819
    public void onResume() {
        super.onResume();
        dealLogoutLoginc();
        MobclickAgent.onPageStart(this.getClass().getName()); //统计页面，"MainScreen"为页面名称，可自定义
    }

    public void onPause() {
        super.onPause();
        MobclickAgent.onPageEnd(this.getClass().getName());
    }
}
