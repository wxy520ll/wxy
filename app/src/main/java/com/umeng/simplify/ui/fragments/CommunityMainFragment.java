/*
 * The MIT License (MIT)
 *
 * Copyright (c) 2014-2015 Umeng, Inc
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 * 
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */

package com.umeng.simplify.ui.fragments;

import android.annotation.TargetApi;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Build;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;

import com.umeng.comm.core.beans.CommConfig;
import com.umeng.comm.core.beans.CommUser;
import com.umeng.comm.core.beans.MessageCount;
import com.umeng.comm.core.beans.Topic;
import com.umeng.comm.core.constants.Constants;
import com.umeng.comm.core.impl.CommunitySDKImpl;
import com.umeng.comm.core.listeners.Listeners;
import com.umeng.comm.core.nets.responses.GuestStatusResponse;
import com.umeng.comm.core.utils.CommonUtils;
import com.umeng.comm.core.utils.ResFinder;
import com.umeng.comm.core.utils.ResFinder.ResType;
import com.umeng.common.ui.fragments.BaseFragment;
import com.umeng.common.ui.mvpview.MvpUnReadMsgView;
import com.umeng.common.ui.presenter.impl.NullPresenter;
import com.umeng.common.ui.widgets.MainIndicator;
import com.umeng.simplify.ui.activities.FindActivity;
import com.umeng.simplify.ui.util.TopicUtil;
import com.umeng.simplify.ui.views.SimplifyIndicator;

import cn.net.xinyi.xmjt.activity.Commity.DepartmentAlarmListFragment;
import cn.net.xinyi.xmjt.activity.Commity.DepartmentListFragment;
import cn.net.xinyi.xmjt.config.AppContext;
import cn.net.xinyi.xmjt.utils.BaseDataUtils;


/**
 * 社区首页，包含关注、推荐、话题三个tab的页面，通过ViewPager管理页面之间的切换.
 */
public class CommunityMainFragment extends BaseFragment<Void, NullPresenter> implements
        OnClickListener, MvpUnReadMsgView {

    private ViewPager mViewPager;
    private String[] mTitles;
    private Fragment mCurrentFragment;


    /**
     * Feed流页面
     */
    private TopicFeedFragment realTimeFeedFragment;

    /**
     * 推荐Feed页面
     */
    private TopicFeedFragment topicFeedFragment;


    /**
     * 每个单位单独的交流界面
     */
    private TopicFeedFragment departmentFeedFragment;


    /**
     * 所以内部交流列表
     */
    private DepartmentListFragment departmentListFragment;


    /**
     * 每个单位内部警情
     */
    private TopicFeedFragment departmentAlarmFeedFragment;


    /**
     * 所以派出所内部警情列表
     */
    private DepartmentAlarmListFragment departmenAlarmtListFragment;


    /**
     * viewpager adapter
     */
    private CommFragmentPageAdapter adapter;


    /**
     * 回退按钮的可见性
     */
    private int mBackButtonVisible = View.GONE;
    /**
     * 跳转到话题搜索按钮的可见性
     */
    private int mTitleVisible = View.VISIBLE;
    /**
     * title的根布局
     */
    private View mTitleLayout;
    /**
     * 右上角的个人信息Button
     */
    private ImageView mProfileBtn;

    private String mContainerClass;
    /**
     * tab视图
     */
//    private SegmentView mSegmentView;
    private SimplifyIndicator indicator;
    /**
     * 未读消息的数量
     */
    //  private MessageCount mUnreadMsg = CommConfig.getConfig().mMessageCount;
    /**
     * 含有未读消息时的红点视图
     */
    private View mBadgeView;
    //    private PopupWindow hotPop, topicPop;
    public int TopicType = 2;

    private IndicatorListerner mIndicatorListerner;


    @Override
    protected int getFragmentLayout() {
        CommunitySDKImpl.getInstance().fetchCommunitystatus(new Listeners.SimpleFetchListener<GuestStatusResponse>() {
            @Override
            public void onComplete(GuestStatusResponse response) {
                CommonUtils.visitNum = response.guestStatus;
            }
        });
        return ResFinder.getLayout("umeng_simplify_community_frag_layout");
    }

    protected void initWidgets() {
        mContainerClass = getActivity().getClass().getName();
        initTitle(mRootView);
        initFragment();
        initViewPager(mRootView);
        registerInitSuccessBroadcast();
        CommunitySDKImpl.getInstance().upLoadUI("simplify");
    }

    /**
     * 初始化title</br
     *
     * @param rootView
     */
    @TargetApi(Build.VERSION_CODES.GINGERBREAD)
    private void initTitle(View rootView) {
        mIndicatorListerner = new IndicatorListerner();
        mTitles = getResources().getStringArray(
                ResFinder.getResourceId(ResType.ARRAY, "umeng_simplify_feed_titles"));
        if (!(BaseDataUtils.isAdminCommunityPcs() || BaseDataUtils.isPcs())) {
            mTitles = getResources().getStringArray(
                    ResFinder.getResourceId(ResType.ARRAY, "umeng_simplify_feed_titles_three"));
        }

        int titleLayoutResId = ResFinder.getId("topic_action_bar");
        mTitleLayout = rootView.findViewById(titleLayoutResId);
        mTitleLayout.setVisibility(View.GONE);

        int backButtonResId = ResFinder.getId("umeng_comm_back_btn");
        rootView.findViewById(backButtonResId).setOnClickListener(this);

        if (mBackButtonVisible != View.VISIBLE) {
            rootView.findViewById(backButtonResId).setVisibility(mBackButtonVisible);
        }

        mTitleLayout.setVisibility(mTitleVisible);

        mBadgeView = findViewById(ResFinder.getId("umeng_comm_badge_view"));
        mBadgeView.setVisibility(View.INVISIBLE);
        //
        mProfileBtn = (ImageView) rootView
                .findViewById(ResFinder.getId("umeng_comm_user_info_btn"));
        mProfileBtn.setOnClickListener(
                new OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (mBadgeView != null) {
                            mBadgeView.setVisibility(View.INVISIBLE);
                        }
//                    CommunitySDKImpl.getInstance().fetchAccessToken(null);
                        gotoFindActivity(CommConfig.getConfig().loginedUser);
                    }
                }
//                new LoginOnViewClickListener() {
//            @Override
//            protected void doAfterLogin(View v) {
//                if (mBadgeView != null) {
//                    mBadgeView.setVisibility(View.INVISIBLE);
//                }
//
//                    gotoFindActivity(CommConfig.getConfig().loginedUser);
//
//            }
//        }
        );
        indicator = (SimplifyIndicator) rootView.findViewById(ResFinder
                .getId("umeng_comm_segment_view"));
        // 设置tabs
//        String[] titles = new String[]{"热门", "推荐", "关注", "话题"};
        indicator.setTabItemTitles(mTitles);
        indicator.setVisibleTabCount(4);
        indicator.SetIndictorClick(mIndicatorListerner);
    }

    /**
     * listerner for MainIndicator 热门，推荐，关注，话题
     */
    private class IndicatorListerner implements MainIndicator.IndicatorListener {
        @Override
        public void SetItemClick() {
            int cCount = indicator.getChildCount();
            for (int i = 0; i < cCount; i++) {
                final int j = i;
                View view = indicator.getChildAt(i);
                view.setOnClickListener(new OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        mViewPager.setCurrentItem(j);
//                        }
                    }
                });
            }
        }
    }


    @Override
    public void onResume() {
        super.onResume();
//        mUnreadMsg = CommConfig.getConfig().mMessageCount;
////        Log.e("xxxxxx","!!!!!!!!yoyal="+mUnreadMsg.unReadTotal+"  "+mUnreadMsg.unReadCommentsCount+mUnreadMsg.unReadLikesCount+mUnreadMsg.unReadNotice);
//        if (mUnreadMsg.unReadTotal > 0 && CommonUtils.isLogin(getActivity())) {
//            mBadgeView.setVisibility(View.VISIBLE);
//        } else {
        mBadgeView.setVisibility(View.INVISIBLE);
        //    }
    }

    public ViewPager getViewPager() {
        return mViewPager;
    }

    /**
     * 跳转到发现Activity</br>
     *
     * @param user
     */
    public void gotoFindActivity(CommUser user) {
        if (getActivity() == null || getActivity().isFinishing()) {
            return;
        }
        Intent intent = new Intent(getActivity(), FindActivity.class);
        if (user == null) {// 来自开发者外部调用的情况
            intent.putExtra(Constants.TAG_USER, CommConfig.getConfig().loginedUser);
        } else {
            intent.putExtra(Constants.TAG_USER, user);
        }
        intent.putExtra(Constants.TYPE_CLASS, mContainerClass);
        getActivity().startActivity(intent);
    }

    /**
     * 设置回退按钮的可见性
     *
     * @param visible
     */
    public void setBackButtonVisibility(int visible) {
        if (visible == View.VISIBLE || visible == View.INVISIBLE || visible == View.GONE) {
            this.mBackButtonVisible = visible;
        }
    }

    /**
     * 设置Title区域的可见性
     *
     * @param visible {@see View#VISIBLE},{@see View#INVISIBLE},{@see View#GONE}
     */
    public void setNavTitleVisibility(int visible) {
        if (visible == View.VISIBLE || visible == View.INVISIBLE || visible == View.GONE) {
            mTitleVisible = visible;
        }
    }

    /**
     * 初始化ViewPager VIew</br>
     *
     * @param rootView
     */
    private void initViewPager(View rootView) {
        mViewPager = (ViewPager) rootView.findViewById(ResFinder.getId("viewPager"));
        mViewPager.setOffscreenPageLimit(mTitles.length);
        adapter = new CommFragmentPageAdapter(getChildFragmentManager());

        mViewPager.setAdapter(adapter);

//        mViewPager.setPageTransformer(false, new ViewPager.PageTransformer() {
//            @Override
//            public void transformPage(View view, float v) {
//                com.umeng.comm.core.utils.Log.d("ani","v:"+v);
//                if(Build.VERSION.SDK_INT==Build.VERSION_CODES.HONEYCOMB){
//                    view.setScaleX(0.5f);
//                    view.setScaleY(0.5f);
//                    com.umeng.comm.core.utils.Log.d("ani","v2:"+v);
//                }
//
//            }
//        });
        indicator.setOnPageChangeListener(new MainIndicator.PageChangeListener() {

            @Override
            public void onPageSelected(int page) {
                mCurrentFragment = getFragment(page);
                if (!(mCurrentFragment instanceof TopicFeedFragment)) {

                }

                if (mCurrentFragment instanceof RealTimeFeedFragment) {

                }
            }

            @Override
            public void onPageScrolled(int arg0, float arg1, int arg2) {
            }

            @Override
            public void onPageScrollStateChanged(int arg0) {
            }
        });
        indicator.setViewPager(mViewPager, 0);
    }

    class CommFragmentPageAdapter extends FragmentPagerAdapter {

        public CommFragmentPageAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int pos) {
            return getFragment(pos);
        }

        @Override
        public int getCount() {
            return getSize();
        }

    }

    private int getSize() {
        return mTitles.length;
    }

    /**
     * 初始化Fragment</br>
     */
    private void initFragment() {

        Topic communityTopic = new Topic();
        communityTopic.id = "5769fba5ea77f746bed552bd";
        communityTopic.name = "交流";


        Topic topic = new Topic();
        topic.id = "5769fbb1ee78504ca62290b5";
        topic.name = "通知";


        Topic departmentTopic = new Topic(); //内部交流
        departmentTopic.id = TopicUtil.getDepartmentTopicInName(getActivity().getApplicationContext(),AppContext.instance().getLoginInfo().getPcs());
        departmentTopic.name = AppContext.instance().getLoginInfo().getPcs();
        Topic departmentAlarmTopic = new Topic(); //内部交流
        departmentAlarmTopic.id = TopicUtil.getDepartmentAlarmTopicInName(getActivity().getApplicationContext(),AppContext.instance().getLoginInfo().getPcs());
        departmentAlarmTopic.name = AppContext.instance().getLoginInfo().getPcs() + "警情";


        realTimeFeedFragment = TopicFeedFragment.newTopicFeedFrmg(communityTopic, true);


        topicFeedFragment = TopicFeedFragment.newTopicFeedFrmg(topic, false);


//        departmentFeedFragment = TopicFeedFragment.newTopicFeedFrmg(departmentTopic, true);

        if (BaseDataUtils.isAdminCommunityPcs()) {
            departmentListFragment = new DepartmentListFragment();
        } else {
            departmentFeedFragment = TopicFeedFragment.newTopicFeedFrmg(departmentTopic, true);
        }


        if (BaseDataUtils.isAdminCommunityPcs()) {
            departmenAlarmtListFragment = new DepartmentAlarmListFragment();
        } else if (BaseDataUtils.isPcs()) {
            boolean isCanPost = false;
            if (BaseDataUtils.isPOLICE())
                isCanPost = true;
            departmentAlarmFeedFragment = TopicFeedFragment.newTopicFeedFrmg(departmentAlarmTopic, isCanPost);
        }

        mCurrentFragment = realTimeFeedFragment;// 默认是MainFeedFragment


    }

    /**
     * 获取当前页面被选中的Fragment</br>
     *
     * @return
     */
    public Fragment getCurrentFragment() {
        return mCurrentFragment;
    }

    /**
     * </br>
     *
     * @param pos
     * @return
     */
    private Fragment getFragment(int pos) {
        Fragment fragment = null;
        if ((BaseDataUtils.isAdminCommunityPcs()||BaseDataUtils.isPcs())){
            if (pos == 0) {
                fragment = topicFeedFragment;  //分局警情
            } else if (pos == 2) {
                fragment = realTimeFeedFragment; //分局交流
            } else if (pos == 3) {
                if (BaseDataUtils.isAdminCommunityPcs()) {
                    fragment = departmentListFragment;
                } else {
                    fragment = departmentFeedFragment; //内部交流
                }
            } else if (pos == 1) {
                if (BaseDataUtils.isAdminCommunityPcs()) {
                    fragment = departmenAlarmtListFragment;
                } else {
                    fragment = departmentAlarmFeedFragment; //内部警情
                }
            }
        } else {
            if (pos == 0) {
                fragment = topicFeedFragment;  //分局警情
            } else if (pos == 1) {
                fragment = realTimeFeedFragment; //分局交流
            } else if (pos == 2) {
                if (BaseDataUtils.isAdminCommunityPcs()) {
                    fragment = departmentListFragment;
                } else {
                    fragment = departmentFeedFragment; //内部交流
                }
            }
        }
        return fragment;
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == ResFinder.getId("umeng_comm_back_btn")) {
            getActivity().finish();
        }
    }

    /**
     * 隐藏MianFeedFragment的输入法，当退出fragment or activity的时候</br>
     */
    public void hideCommentLayoutAndInputMethod() {
//        if (realTimeFeedFragment != null) {
//            realTimeFeedFragment.hideCommentLayoutAndInputMethod();
//        }
    }

    /**
     * clean sub fragment data</br>
     */
    public void cleanAdapterData() {
//        if (realTimeFeedFragment != null) {
//            realTimeFeedFragment.clearListView();
//        }
//        if (realTimeFeedFragment != null) {
//            realTimeFeedFragment.cleanAdapterData();
//        }
    }

    @Override
    public void onFetchUnReadMsg(MessageCount unreadMsg) {
//        this.mUnreadMsg = unreadMsg;
////        Log.e("xxxxxx","!!!!!!!!yoyal444="+mUnreadMsg.unReadTotal+"  "+mUnreadMsg.unReadCommentsCount+mUnreadMsg.unReadLikesCount+mUnreadMsg.unReadNotice);
//
//        if (mUnreadMsg.unReadTotal > 0) {
//            mBadgeView.setVisibility(View.VISIBLE);
//        }
    }

    /**
     * 主动调用加载数据。 【注意】该接口仅仅在退出登录时，跳转到FeedsActivity清理数据后重新刷新数据</br>
     */
    public void repeatLoadDataFromServer() {
//        if (mMainFeedFragment != null) {
//            mMainFeedFragment.loadFeedFromServer();
//        }
//        if (mRecommendFragment != null) {
//            mRecommendFragment.loadDataFromServer();
//        }
    }

    /**
     * 注册登录成功时的广播</br>
     */
    private void registerInitSuccessBroadcast() {
        IntentFilter filter = new IntentFilter();
        filter.addAction(Constants.ACTION_INIT_SUCCESS);
        LocalBroadcastManager.getInstance(getActivity()).registerReceiver(mInitConfigReceiver,
                filter);
    }

    private BroadcastReceiver mInitConfigReceiver = new BroadcastReceiver() {

        @Override
        public void onReceive(Context context, Intent intent) {
            onFetchUnReadMsg(CommConfig.getConfig().mMessageCount);
        }
    };

    @Override
    public void onDestroy() {
        LocalBroadcastManager.getInstance(getActivity()).unregisterReceiver(mInitConfigReceiver);
        super.onDestroy();
    }

}
