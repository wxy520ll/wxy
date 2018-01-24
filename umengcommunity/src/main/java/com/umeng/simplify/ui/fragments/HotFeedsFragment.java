package com.umeng.simplify.ui.fragments;

import android.app.NotificationManager;
import android.content.Intent;
import android.view.View;
import android.view.animation.AlphaAnimation;

import com.umeng.comm.core.beans.CommUser;
import com.umeng.comm.core.beans.FeedItem;
import com.umeng.comm.core.beans.Topic;
import com.umeng.comm.core.constants.Constants;
import com.umeng.comm.core.impl.CommunitySDKImpl;
import com.umeng.comm.core.login.LoginListener;
import com.umeng.comm.core.utils.CommonUtils;
import com.umeng.comm.core.utils.Log;
import com.umeng.common.ui.util.BroadcastUtils;
import com.umeng.simplify.ui.activities.PostFeedActivity;
import com.umeng.simplify.ui.presenter.impl.HottestFeedPresenter;

import java.util.List;

/**
 * Created by umeng on 12/1/15.
 */
public class HotFeedsFragment extends PostBtnAnimFragment<HottestFeedPresenter> {

    HottestFeedPresenter mHottestFeedPresenter;
    boolean isAdmin =false;

    @Override
    protected void initWidgets() {
        super.initWidgets();
        mPostBtn.setVisibility(View.GONE);
        mPostBtn.setOnClickListener(
//                new Listeners.LoginOnViewClickListener() {
//            @Override
//            protected void doAfterLogin(View v) {
//                gotoPostFeedActivity();
//            }
//        })
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
    }

    /**
     * 跳转至发送新鲜事页面</br>
     */
    private void gotoPostFeedActivity() {
        Intent postIntent = new Intent(getActivity(), PostFeedActivity.class);
        Topic topic =new Topic();
        topic.id="5769fbb1ee78504ca62290b5";
        topic.name="通知";
        postIntent.putExtra(Constants.TAG_TOPIC,topic);
        startActivity(postIntent);



    }

    @Override
    protected void initEventHandlers() {
        super.initEventHandlers();
    }

    @Override
    protected void initAdapter() {
        super.initAdapter();

    }

    @Override
    protected void showPostButtonWithAnim() {

        if (isAdmin){
            AlphaAnimation showAnim = new AlphaAnimation(0.5f, 1.0f);
            showAnim.setDuration(500);
            mPostBtn.setVisibility(View.VISIBLE);
            mPostBtn.startAnimation(showAnim);
        }
    }

    @Override
    protected HottestFeedPresenter createPresenters() {
        mHottestFeedPresenter = new HottestFeedPresenter(this);
        mHottestFeedPresenter.setIsShowTopFeeds(false);
        return mHottestFeedPresenter;
    }

    @Override
    protected void registerBroadcast() {
        // 注册广播接收器
        BroadcastUtils.registerUserBroadcast(getActivity(), mReceiver);
        BroadcastUtils.registerFeedBroadcast(getActivity(), mReceiver);
        BroadcastUtils.registerFeedUpdateBroadcast(getActivity(), mReceiver);
    }

    @Override
    public List<FeedItem> getBindDataSource() {
        return super.getBindDataSource();
    }

    @Override
    public void notifyDataSetChanged() {
        super.notifyDataSetChanged();
    }

    @Override
    protected void postFeedComplete(FeedItem feedItem) {

    }

    @Override
    protected void deleteFeedComplete(FeedItem feedItem) {
        mFeedLvAdapter.getDataSource().remove(feedItem);
        mFeedLvAdapter.notifyDataSetChanged();
        updateForwardCount(feedItem, -1);
        Log.d(getTag(), "### 删除feed");
    }

    @Override
    public void clearListView() {
        super.clearListView();
    }

}
