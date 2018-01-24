package cn.net.xinyi.xmjt.utils.View;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.ListView;

/**
 * 取代ListView的LinearLayout，使之能够成功嵌套在ScrollView中
 * @author terry_龙
 */
public class LinearLayoutForListView extends ListView {


	public LinearLayoutForListView(Context context) {
		super(context);
		// TODO Auto-generated constructor stub
	}

	public LinearLayoutForListView(Context context, AttributeSet attrs) {
		super(context, attrs);
	}
	public LinearLayoutForListView(Context context, AttributeSet attrs,
			int defStyle) {
		super(context, attrs, defStyle);
	}


	@Override
	/**
	 * 重写该方法，达到使ListView适应ScrollView的效果
	 */
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		int expandSpec = MeasureSpec.makeMeasureSpec(Integer.MAX_VALUE >> 2,
				MeasureSpec.AT_MOST);
		super.onMeasure(widthMeasureSpec, expandSpec);

	}
}

