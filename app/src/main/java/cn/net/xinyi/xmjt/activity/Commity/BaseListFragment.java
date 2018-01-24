package cn.net.xinyi.xmjt.activity.Commity;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Map;

import cn.net.xinyi.xmjt.R;
import cn.net.xinyi.xmjt.config.AppContext;
import cn.net.xinyi.xmjt.config.BaseListAdp;
import cn.net.xinyi.xmjt.config.BaseListAty;
import cn.net.xinyi.xmjt.utils.DB.ZDXXUtils;
import cn.net.xinyi.xmjt.utils.GeneralUtils;
import cn.net.xinyi.xmjt.utils.View.XListView;


/**
 * Created by studyjun on 2016/6/27.
 */
public abstract class BaseListFragment extends Fragment {

    public static final int STATE_NONE = 0;
    public static final int STATE_REFRESH = 1;
    public static final int STATE_LOADMORE = 2;
    public static int mState = STATE_NONE;

    protected BaseListAdp mAdapter;
    protected RelativeLayout mContainer;

    protected int mCurrentPage = 1; //当前页数
    protected static int PAGE_SIZE = 20; //每页加载条数

    protected ListView mListView;

    protected View mNodata;
    protected LinearLayout ll_top;

    private View mRootView;
    public ArrayList<String> valueList;

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
         super.onCreateView(inflater, container, savedInstanceState);
        if (mRootView==null){

            ZDXXUtils orgs=new ZDXXUtils(getActivity());
           // final Map<String,String> pcsMaps=orgs.getZdlbToZdz(GeneralUtils.ZZJG_PCS);
            final Map<String,String> pcsMaps=orgs.getZdlbAndFzdbmToZdz(GeneralUtils.ZZJG_PCS, AppContext.instance().getLoginInfo().getFjbm());
            //获取派出所单位的值 并将map 的 vaule值转换为list
            Collection<String> valueCollection = pcsMaps.values();
            valueList =new ArrayList<String>(valueCollection);

            mRootView = inflater.inflate(R.layout.aty_base_list,null);
            mListView = (ListView) mRootView.findViewById(R.id.base_list);
            mContainer = (RelativeLayout) mRootView.findViewById(R.id.list_container);
            mNodata = mRootView.findViewById(R.id.ll_empty_data);
            ll_top = (LinearLayout) mRootView.findViewById(R.id.ll_top);
            setupTopView(ll_top);
            mListView.setOnScrollListener(new XListView.OnXScrollListener() {
                @Override
                public void onXScrolling(View view) {

                }

                @Override
                public void onScrollStateChanged(AbsListView view, int scrollState) {
                    if (mAdapter == null || mAdapter.getCount() == 0) {
                        return;
                    }
                    // 数据已经全部加载，或数据为空时，或正在加载，不处理滚动事件
                    if (mState == STATE_LOADMORE || mState == STATE_REFRESH) {
                        return;
                    }
                    // 判断是否滚动到底部
                    boolean scrollEnd = false;
                    try {
                        if (view.getPositionForView(mAdapter.getFooterView()) == view
                                .getLastVisiblePosition())
                            scrollEnd = true;
                    } catch (Exception e) {
                        scrollEnd = false;
                    }

                    if (mState == STATE_NONE && scrollEnd) {
                        if (mAdapter.getState() == BaseListAdp.STATE_LOAD_MORE
                                || mAdapter.getState() == BaseListAdp.STATE_NETWORK_ERROR) {
                            mCurrentPage++;
                            mState = STATE_LOADMORE;
                            requestData();
                            mAdapter.setFooterViewLoading();
                        }
                    }
                }

                @Override
                public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {

                }
            });

            mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    if (position < mAdapter.getData().size()) {
                        BaseListFragment.this.onItemClick(parent, view, position, id, getAdapter().getData().get(position));
                    }
                }
            });
        }
        return mRootView;
    }

    public void onItemClick(AdapterView<?> parent, View view, int position, long id, Object t) {

    }

    protected abstract void requestData();


    /**
     * 设置topview
     *
     * @param parent
     */
    public void setupTopView(LinearLayout parent) {

    }


    /**
     * 设置adapter
     *
     * @param baseAdapter
     */
    public void setAdapter(BaseListAdp baseAdapter) {
        if (mListView != null && baseAdapter != null) {
            mAdapter = baseAdapter;
            mListView.setAdapter(mAdapter);
            mAdapter.notifyDataSetChanged();
        }
    }

    /**
     * 设置adapter
     *
     * @return
     */
    public BaseListAdp getAdapter() {
        return mAdapter;
    }


    /**
     * 设置完成加载
     */
    public void setStateFinish() {
        mNodata.setVisibility(View.GONE);
        BaseListAty.mState = STATE_NONE;
        mAdapter.setState(BaseListAdp.STATE_LOAD_MORE);
        mAdapter.notifyDataSetChanged();
    }


    /**
     * 没有更多
     */
    protected void setNotMoreState() {
        BaseListAty.mState = STATE_NONE;
        mNodata.setVisibility(View.GONE);
        mAdapter.setState(BaseListAdp.STATE_NO_MORE);
        mAdapter.notifyDataSetChanged();
    }

    /**
     * 没有数据
     */
    protected void setEmptyData() {
        BaseListAty.mState = STATE_NONE;
        mNodata.setVisibility(View.VISIBLE);
        mAdapter.setState(BaseListAdp.STATE_EMPTY_ITEM);
    }

    /**
     * 没有数据
     */
    protected void setEmptyData2() {
        BaseListAty.mState = STATE_NONE;
        mAdapter.setState(BaseListAdp.STATE_EMPTY_ITEM);
        mAdapter.notifyDataSetChanged();
    }


    /**
     * 没有数据
     */
    protected void setLessMorePage() {
        BaseListAty.mState = STATE_NONE;
        mAdapter.setState(BaseListAdp.STATE_LESS_ONE_PAGE);
        mAdapter.notifyDataSetChanged();
    }


    public int getListID() {
        return R.id.base_list;
    }
}
