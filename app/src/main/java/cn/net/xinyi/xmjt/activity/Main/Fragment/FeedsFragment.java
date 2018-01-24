package cn.net.xinyi.xmjt.activity.Main.Fragment;

import android.app.Activity;
import android.app.Application;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.umeng.comm.core.constants.Constants;
import com.umeng.simplify.ui.fragments.CommunityMainFragment;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import cn.net.xinyi.xmjt.R;
import cn.net.xinyi.xmjt.config.AppContext;

/**
 * Created by zhiren.zhang on 2016/8/19.
 */
public class FeedsFragment extends BaseNewFragment implements View.OnClickListener {
    private static final String TAG = "TabMenuFragment";

    private CommunityMainFragment mFeedsFragment;


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = View.inflate(activity, R.layout.umeng_comm_feeds_activity, null);
        if (null!= AppContext.instance().getLoginInfo().getPcs()){
            mFeedsFragment = new CommunityMainFragment();
            activity.getSupportFragmentManager()
                    .beginTransaction()
                    //R.id.content_frame  这个是主页的id
                    .replace(R.id.umeng_comm_main_container, mFeedsFragment, "mFeedsFragment")
                    .commit();
            addLoginPlatforms();
        }
        return view;
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

    @Override
    public void onResume() {
        super.onResume();
        dealLogoutLoginc();
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
    public void onDestroy() {
        if (mFeedsFragment != null) {
            mFeedsFragment.hideCommentLayoutAndInputMethod();
        }
        super.onDestroy();
    }


}
