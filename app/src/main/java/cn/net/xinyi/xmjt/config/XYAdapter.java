/** 
 * Project Name:healthplus 
 * File Name:A_YHAdapter.java 
 * Package Name:net.fitcome.health.adapter 
 * Date:2015-5-19涓???4:14:46 
 * Copyright (c) 2015, yh_android@163.com All Rights Reserved. 
 */

package cn.net.xinyi.xmjt.config;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.BaseAdapter;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Set;

/**
 * ClassName: XYAdapter <br/>
 * Function: TODO ADD FUNCTION. <br/>
 * Reason: TODO ADD REASON(可选). <br/>
 * date: 2016-1-11 下午4:14:46 <br/>
 * 对ViewHolder的封装，以及更方便的控制ListView滑动过程中不加载图片
 *
 * @author hao
 * @version
 * @since JDK 1.6
 */

public abstract class XYAdapter<T> extends BaseAdapter implements
		OnScrollListener
{

	protected LayoutInflater mInflater;
	protected Collection<T> mDatas;
	protected List<List<T>> mDatass;
	protected final int mItemLayoutId;
	protected AbsListView mList;
	protected boolean isScrolling;
	protected Context mContext;
	private OnScrollListener listener;

	public XYAdapter(AbsListView view, Collection<T> mDatas,
					 int itemLayoutId, Context mContext)
	{
		this.mContext = mContext;
		this.mInflater = LayoutInflater.from(view.getContext());
		if(mDatas == null)
		{
			mDatas = new ArrayList<T>(0);
		}
		this.mDatas = mDatas;
		this.mItemLayoutId = itemLayoutId;
		this.mList = view;
		mList.setOnScrollListener(this);
	}

	public XYAdapter(AbsListView view,List<List<T>> mDatass,
					 int itemLayoutId, Context mContext)
	{
		this.mContext = mContext;
		this.mInflater = LayoutInflater.from(view.getContext());
		if(mDatas == null)
		{
			mDatas = new ArrayList<T>(0);
		}
		this.mDatass = mDatass;
		this.mItemLayoutId = itemLayoutId;
		this.mList = view;
		mList.setOnScrollListener(this);
	}

	public void refresh(Collection<T> datas)
	{
		if(datas == null)
		{
			datas = new ArrayList<T>(0);
		}
		this.mDatas = datas;
		notifyDataSetChanged();
	}

	public void addOnScrollListener(OnScrollListener l)
	{
		this.listener = l;
	}

	@Override
	public int getCount()
	{
		return mDatas.size();
	}

	@Override
	public T getItem(int position)
	{

		if(mDatas instanceof List)
		{
			return ((List<T>) mDatas).get(position);
		}
		else if(mDatas instanceof Set)
		{
			return new ArrayList<T>(mDatas).get(position);
		}
		else
		{
			return null;
		}
	}

	@Override
	public long getItemId(int position)
	{
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent)
	{
		final AdapterHolder viewHolder = getViewHolder(position, convertView,
				parent);
		convert(viewHolder, getItem(position), isScrolling);
		return viewHolder.getConvertView();

	}

	private AdapterHolder getViewHolder(int position, View convertView,
                                        ViewGroup parent)
	{
		return AdapterHolder.get(convertView, parent, mItemLayoutId, position);
	}

	public abstract void convert(AdapterHolder helper, T item,
                                 boolean isScrolling);

	@Override
	public void onScrollStateChanged(AbsListView view, int scrollState)
	{
		// 设置是否滚动的状态
		if(scrollState == OnScrollListener.SCROLL_STATE_IDLE)
		{
			isScrolling = false;
			this.notifyDataSetChanged();
		}
		else
		{
			isScrolling = true;
		}
		if(listener != null)
		{
			listener.onScrollStateChanged(view, scrollState);
		}
	}

	@Override
	public void onScroll(AbsListView view, int firstVisibleItem,
			int visibleItemCount, int totalItemCount)
	{
		if(listener != null)
		{
			listener.onScroll(view, firstVisibleItem, visibleItemCount,
					totalItemCount);
		}
	}
}
