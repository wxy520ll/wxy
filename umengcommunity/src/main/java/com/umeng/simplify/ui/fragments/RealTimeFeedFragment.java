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
import android.view.View;
import android.widget.TextView;

import com.umeng.comm.core.beans.CommUser;
import com.umeng.comm.core.beans.FeedItem;
import com.umeng.comm.core.beans.Topic;
import com.umeng.comm.core.constants.Constants;
import com.umeng.comm.core.impl.CommunitySDKImpl;
import com.umeng.comm.core.login.LoginListener;
import com.umeng.comm.core.utils.CommonUtils;
import com.umeng.comm.core.utils.ResFinder;
import com.umeng.common.ui.presenter.impl.FeedListPresenter;
import com.umeng.simplify.ui.activities.PostFeedActivity;
import com.umeng.simplify.ui.presenter.impl.RealTimeFeedPresenter;


/**
 * 实时Feed的Fragment,会获取整个社区最新的200条Feed
 */
public class RealTimeFeedFragment extends FriendsFragment {

    public RealTimeFeedFragment() {
    }

    @Override
    protected FeedListPresenter createPresenters() {
        FeedListPresenter fragment = new RealTimeFeedPresenter(this);
        fragment.setIsShowTopFeeds(true);
        return fragment;
    }

    @Override
    protected void initWidgets() {
        super.initWidgets();
        TextView titleTextView = (TextView) mRootView.findViewById(ResFinder
                .getId("umeng_comm_title_tv"));
        titleTextView.setText(ResFinder.getString("umeng_comm_realtime")); // 实时内容
        mPostBtn.setVisibility(View.VISIBLE);
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
                                        gotoPostFeedActivity();
                                    }
                                }
                            });
                        } else {

                            gotoPostFeedActivity();
                        }
                    }
                })
        ;
        findViewById(ResFinder.getId("umeng_comm_friend_id")).setVisibility(View.GONE);
        findViewById(ResFinder.getId("umeng_comm_divide_id")).setVisibility(View.GONE);
    }

    public static RealTimeFeedFragment newRealTimeFeedRecommend() {
        return new RealTimeFeedFragment();
    }
    /**
     * 跳转至发送新鲜事页面</br>
     */
    private void gotoPostFeedActivity() {
        Intent postIntent = new Intent(getActivity(), PostFeedActivity.class);
        Topic topic = new Topic();
        topic.id="5769fba5ea77f746bed552bd";
        topic.name="交流";
        postIntent.putExtra(Constants.TAG_TOPIC,topic);
        startActivity(postIntent);
    }

    @Override
    protected void postFeedComplete(FeedItem feedItem) {
        super.postFeedComplete(feedItem);
        mFeedLvAdapter.getDataSource().add(feedItem);
        mPresenter.sortFeedItems(mFeedLvAdapter.getDataSource());
        mFeedLvAdapter.notifyDataSetChanged();
        mFeedsListView.setSelection(0);
    }
}
