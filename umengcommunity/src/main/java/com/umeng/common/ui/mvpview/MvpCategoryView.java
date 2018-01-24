package com.umeng.common.ui.mvpview;

import com.umeng.comm.core.beans.Category;
import com.umeng.comm.core.beans.Topic;

import java.util.List;

/**
 * Created by wangfei on 15/11/24.
 */
public interface MvpCategoryView extends MvpBaseRefreshView{
    List<Category> getBindDataSource();

    void notifyDataSetChanged();

    /**
     * 刷新结束，仅仅hide相关View，不做数据相关操作</br>
     */
    void onRefreshEndNoOP();

    void ChangeAdapter(List<Topic> list);
}
