package cn.net.xinyi.xmjt.utils;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.GridView;

/**
 * Created by $ wxy on 2017/4/20.
 * 嵌套的gridview
 */

public class NestGridView extends GridView {
    public NestGridView(Context context) {
        super(context);
    }

    public NestGridView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public NestGridView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        // TODO Auto-generated method stub
        int expandSpec = MeasureSpec.makeMeasureSpec(Integer.MAX_VALUE >> 2, MeasureSpec.AT_MOST);
        super.onMeasure(widthMeasureSpec, expandSpec);
    }
}
