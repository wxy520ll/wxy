package cn.net.xinyi.xmjt.activity.Main;

import android.support.v4.view.PagerAdapter;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import java.util.List;


/**
 * @Description:ViewPager实现轮播图片的循环播放
 */
public class ViewPagerAdapter extends PagerAdapter {

	//存放轮播图片的list
	private List<ImageView> listviews;

	//存放图片的数组
	private int[] pics;

	public ViewPagerAdapter(List<ImageView> listviews, int[] pics) {
		super();
		this.listviews = listviews;
		this.pics = pics;
	}
	@Override
	public void destroyItem(ViewGroup container, int position, Object object) {
		container.removeView(listviews.get(position % listviews.size()));
	}

	@Override
	public Object instantiateItem(ViewGroup container, int position) {
		try {
			if(listviews.get(position%listviews.size()).getParent() == null) {
				container.addView(listviews.get(position%listviews.size()));
			}
		}catch (Exception e){
			e.printStackTrace();
		}
		return listviews.get(position%listviews.size());
	}

	@Override
	public int getCount() {
		return Integer.MAX_VALUE;
	}

	@Override
	public boolean isViewFromObject(View arg0, Object arg1) {
		return arg0 == arg1;
	}

}
