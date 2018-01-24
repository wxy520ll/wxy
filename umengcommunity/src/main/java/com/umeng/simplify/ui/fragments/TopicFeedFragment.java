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

import android.content.Intent;
import android.database.DatabaseUtils;
import android.util.TypedValue;
import android.view.View;
import android.view.animation.AlphaAnimation;

import com.umeng.comm.core.beans.CommUser;
import com.umeng.comm.core.beans.FeedItem;
import com.umeng.comm.core.beans.Topic;
import com.umeng.comm.core.constants.Constants;
import com.umeng.comm.core.impl.CommunitySDKImpl;
import com.umeng.comm.core.listeners.Listeners.OnResultListener;
import com.umeng.comm.core.login.LoginListener;
import com.umeng.comm.core.utils.CommonUtils;
import com.umeng.common.ui.presenter.impl.TopicFeedPresenter;
import com.umeng.common.ui.util.Filter;
import com.umeng.simplify.ui.activities.PostFeedActivity;
import com.umeng.simplify.ui.adapters.FeedAdapter;

import java.util.Iterator;
import java.util.List;

/**
 * 某个话题的所有feed页面,即话题的详情页面.
 */
public class TopicFeedFragment extends PostBtnAnimFragment<TopicFeedPresenter> {


    private OnResultListener mAnimationListener = null;
    private boolean isPostBtnVisible = false;
    private boolean isPostBtnVisibleInRole = true;


    public TopicFeedFragment() {

    }


    /**
     * 先创建一个TopicFeedFragment对象
     *
     * @return
     */
    public static TopicFeedFragment newTopicFeedFrmg(final Topic topic,boolean isPostBtnVisible) {
        TopicFeedFragment topicFeedFragment = new TopicFeedFragment();
        topicFeedFragment.mTopic = topic;
        topicFeedFragment.isPostBtnVisible = isPostBtnVisible;

        topicFeedFragment.mFeedFilter = new Filter<FeedItem>() {

            @Override
            public List<FeedItem> doFilte(List<FeedItem> newItems) {
                if (newItems == null || newItems.size() == 0) {
                    return newItems;
                }
                Iterator<FeedItem> iterator = newItems.iterator();
                while (iterator.hasNext()) {
                    List<Topic> topics = iterator.next().topics;
                    if (!topics.contains(topic)) {
                        iterator.remove();
                    }
                }
                return newItems;
            }
        };
        return topicFeedFragment;
    }



    /**
     * 先创建一个TopicFeedFragment对象
     *
     * @return
     */
    public static TopicFeedFragment newTopicFeedFrmg(final Topic topic) {
       return newTopicFeedFrmg(topic,false);
    }


    public void setIsPostBtnVisible(boolean isPostBtnVisible) {
        this.isPostBtnVisible = isPostBtnVisible;
        if (isPostBtnVisible) {
            mPostBtn.setVisibility(View.VISIBLE);
        }
    }

    @Override
    protected void showPostButtonWithAnim() {
        AlphaAnimation showAnim = new AlphaAnimation(0.5f, 1.0f);
        showAnim.setDuration(500);

        mPostBtn.setVisibility(View.VISIBLE);
        mPostBtn.startAnimation(showAnim);
    }

    @Override
    protected void initViews() {
        super.initViews();
        if (!isPostBtnVisible&&getActivity()!=null&&getActivity().getIntent()!=null)
            isPostBtnVisibleInRole=getActivity().getIntent().getBooleanExtra("admin",false);

        setIsDisplayTopic(false);
        mPostBtn.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (!CommonUtils.isLogin(getActivity())) {
                            CommunitySDKImpl.getInstance().login(getActivity(), new LoginListener() {
                                @Override
                                public void onStart() {
                                    if (getActivity() != null && !getActivity().isFinishing()) {
                                        mProcessDialog.show();
                                    }
                                }

                                @Override
                                public void onComplete(int stCode, CommUser userInfo) {
                                    if (getActivity() != null && !getActivity().isFinishing()) {
                                        mProcessDialog.dismiss();
                                    }
                                    if (stCode == 0) {
                                        Intent postFeedIntent = new Intent(getActivity(),
                                                PostFeedActivity.class);
                                        postFeedIntent.putExtra(Constants.TAG_TOPIC, mTopic);
                                        getActivity().startActivity(postFeedIntent);
                                    }
                                }
                            });
                        } else {

                            Intent postFeedIntent = new Intent(getActivity(),
                                    PostFeedActivity.class);
                            postFeedIntent.putExtra(Constants.TAG_TOPIC, mTopic);
                            getActivity().startActivity(postFeedIntent);
                        }
                    }
                }
        );
        if (mAnimationListener != null) {
            mRefreshLayout.setOnScrollDirectionListener(new OnResultListener() {

                @Override
                public void onResult(int status) {
                    if (!isScrollEffective) {
                        return;
                    }
                    // 1:向上滑动且第一项显示 (up)
                    // 0:向下且大于第一项 (down)
                    int firstVisible = mFeedsListView.getFirstVisiblePosition();
                    int headerCount = mFeedsListView.getHeaderViewsCount();
                    if ((status == 1 && firstVisible >= headerCount)
                            || (status == 0 && firstVisible == headerCount)) {
                        mAnimationListener.onResult(status);
                    }
                }
            });
        }
//        findViewById(ResFinder.getId("umeng_comm_new_post_btn")).setVisibility(View.GONE);
        if (!isPostBtnVisibleInRole){
            mPostBtn.setVisibility(View.GONE);
        }
    }

    @Override
    protected FeedAdapter createListViewAdapter() {
        FeedAdapter adapter = new FeedAdapter(getActivity());
        adapter.setIsDisplayTopic(false);
        return adapter;
    }

    @Override
    protected void loadMoreFeed() {
        mPresenter.fetchNextPageData();
    }

    @Override
    protected TopicFeedPresenter createPresenters() {
        TopicFeedPresenter presenter = new TopicFeedPresenter(this);
        presenter.setId(mTopic.id);
        return presenter;
    }

    @Override
    public void onRefreshStart() {
        if (!isAdded()) {
            return;
        }
        mRefreshLayout.setProgressViewOffset(false, 0,
                (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 24, getResources()
                        .getDisplayMetrics()));
        mRefreshLayout.setRefreshing(true);
    }

    public void setOnAnimationListener(final OnResultListener listener) {
        mAnimationListener = listener;
    }

    private boolean isScrollEffective = false; // 是否滚动生效，避免两个页面的滚动事件冲突，只有在显示的时候才考虑滚动

    // private boolean isExecuteScroll = true;

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        isScrollEffective = isVisibleToUser;
    }

    @Override
    protected void executeAnimation(boolean show) {
        if (!isPostBtnVisibleInRole)
            show = false;

        super.executeAnimation(show);


    }
}
