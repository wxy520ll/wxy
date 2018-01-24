/** 
 * Project Name:healthplus 
 * File Name:AdapterHolder.java 
 * Package Name:net.fitcome.health.adapter 
 * Date:2015-5-19涓???4:16:06 
 * Copyright (c) 2015, yh_android@163.com All Rights Reserved. 
 */  

package cn.net.xinyi.xmjt.config;


import android.graphics.Bitmap;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

/** 
 * 
 * @ClassName: AdapterHolder<br/> 
 * @Description: Adapter Item 组件<br/>
 * @date: 2015-6-1 上午11:28:57 <br/>
 * @author hao
 * @version
 * @since JDK 1.6
 */

public class AdapterHolder
{
	private final SparseArray<View> mViews;
    private final int mPosition;
    private final View mConvertView;

    private AdapterHolder(ViewGroup parent, int layoutId, int position) {
        this.mPosition = position;
        this.mViews = new SparseArray<View>();
        mConvertView = LayoutInflater.from(parent.getContext()).inflate(
                layoutId, parent, false);
        // setTag
        mConvertView.setTag(this);
    }

    /**
     * 拿到全部View
     *
     * @return
     */
    public SparseArray<View> getAllView() {
        return mViews;
    }

    /**
     * 拿到一个ViewHolder对象
     *
     * @param convertView
     * @param parent
     * @param layoutId
     * @param position
     * @return
     */
    public static AdapterHolder get(View convertView, ViewGroup parent,
                                    int layoutId, int position) {
        if (convertView == null||convertView.getTag()==null) {
            return new AdapterHolder(parent, layoutId, position);
        } else {
            return (AdapterHolder) convertView.getTag();
        }
    }

    public View getConvertView() {
        return mConvertView;
    }

    /**
     * 通过控件的Id获取对于的控件，如果没有则加入views
     *
     * @param viewId
     * @return
     */
    @SuppressWarnings("unchecked")
    public <T extends View> T getView(int viewId) {
        View view = mViews.get(viewId);
        if (view == null) {
            view = mConvertView.findViewById(viewId);
            mViews.put(viewId, view);
        }
        return (T) view;
    }

    /**
     * 为TextView设置字符串
     *
     * @param viewId
     * @param text
     * @return
     */
    public AdapterHolder setText(int viewId, String text) {
        TextView view = getView(viewId);
        view.setText(text);
        return this;
    }

    /**
     * 为TextView字体颜色
     *
     * @param viewId
     * @param text
     * @return
     */
    public AdapterHolder setTextColor(int viewId, int color) {
        TextView view = getView(viewId);
        view.setTextColor(color);
        return this;
    }
    /**
     * 为ImageView设置图片
     *
     * @param viewId
     * @param drawableId
     * @return
     */
    public AdapterHolder setImageResource(int viewId, int drawableId) {
        ImageView view = getView(viewId);
        view.setImageResource(drawableId);

        return this;
    }

    /**
     * 为ImageView设置图片
     *
     * @param viewId
     * @param bm
     * @return
     */
    public AdapterHolder setImageBitmap(int viewId, Bitmap bm) {
        ImageView view = getView(viewId);
        view.setImageBitmap(bm);
        return this;
    }

//    /**
//     * 为ImageView设置图片
//     * 
//     * @param viewId
//     * @param url
//     * @return
//     */
//    public AdapterHolder setImageByUrl(YHBitmap bitmap, int viewId, String url) {
//        bitmap.display(getView(viewId), url);
//        return this;
//    }

    public int getPosition() {
        return mPosition;
    }
}
