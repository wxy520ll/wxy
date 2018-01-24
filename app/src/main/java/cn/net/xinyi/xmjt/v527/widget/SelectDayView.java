package cn.net.xinyi.xmjt.v527.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.allen.library.SuperButton;
import com.blankj.utilcode.util.SizeUtils;
import com.blankj.utilcode.util.StringUtils;

import cn.net.xinyi.xmjt.R;

import static android.R.attr.tag;

/**
 * Created by Fracesuit on 2017/12/20.
 */

public class SelectDayView extends LinearLayout {
    int currentSelectIndex = -1;//当前选中的
    String selectTitle;
    String selectContent;

    public SelectDayView(Context context, String selectTitle) {
        super(context);
        this.selectTitle = selectTitle;
    }

    public SelectDayView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(attrs);
    }

    public void init(AttributeSet attrs) {
        setOrientation(LinearLayout.HORIZONTAL);
        setMinimumHeight(SizeUtils.dp2px(36));
        setGravity(Gravity.CENTER_VERTICAL);
        final int padding = SizeUtils.dp2px(5);
        setPadding(padding, padding, padding, padding);
        setBackgroundResource(R.color.divide_line);

        if (attrs != null) {
            TypedArray typedArray = getContext().obtainStyledAttributes(attrs, R.styleable.SelectView);
            selectTitle = typedArray.getString(R.styleable.SelectView_select_title);
            selectContent = typedArray.getString(R.styleable.SelectView_select_content);
            currentSelectIndex = typedArray.getInt(R.styleable.SelectView_select_check_index, -1);
            typedArray.recycle();
        }

        final LinearLayout linearLayout = new LinearLayout(getContext());
        linearLayout.setOrientation(LinearLayout.HORIZONTAL);
        linearLayout.setGravity(Gravity.CENTER_VERTICAL);
        final LayoutParams textLayoutParams = new LayoutParams(LayoutParams.WRAP_CONTENT, SizeUtils.dp2px(36));
        textLayoutParams.leftMargin = SizeUtils.dp2px(10);
        final TextView textView = new TextView(getContext());
        textView.setText(selectTitle);
        textView.setGravity(Gravity.CENTER_VERTICAL | Gravity.LEFT);
        textView.setTextSize(16);
        textView.setTextColor(ContextCompat.getColor(getContext(), R.color.black));
        linearLayout.addView(textView, textLayoutParams);
        final LayoutParams btnLayoutParams = new LayoutParams(0, SizeUtils.dp2px(24), 1);
        btnLayoutParams.leftMargin = SizeUtils.dp2px(30);
        String[] btns = {"今天", "本周", "本月"};
        if (!StringUtils.isEmpty(selectContent)) {
            btns = selectContent.split(",");
        }
        int i = 0;
        for (String day : btns) {
            linearLayout.addView(createSuperButton(day, i++), btnLayoutParams);
        }
        final LayoutParams layoutParams = new LayoutParams(0, SizeUtils.dp2px(36), 1);
        addView(linearLayout, layoutParams);

        // final ImageView imageView = new ImageView(getContext());
      /*  imageView.setImageResource(R.drawable.icon_right_go);
        imageView.setScaleType(ImageView.ScaleType.CENTER);
        imageView.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (onSelectViewClickListener != null) {
                    onSelectViewClickListener.onSelectView();
                }
            }
        });
        final LinearLayout.LayoutParams imageLayoutParams = new LinearLayout.LayoutParams(SizeUtils.dp2px(36), SizeUtils.dp2px(36));
        imageLayoutParams.leftMargin = SizeUtils.dp2px(5);
        addView(imageView, imageLayoutParams);*/

        change();
    }

    private SuperButton createSuperButton(String day, int tag) {
        final SuperButton sbtn = new SuperButton(getContext());
        sbtn.setShapeType(SuperButton.RECTANGLE)
                .setShapeCornersRadius(5)
                .setShapeStrokeWidth(1)
                .setTextGravity(SuperButton.TEXT_GRAVITY_CENTER)
                .setUseShape();
        sbtn.setText(day);
        sbtn.setTag(tag);
        sbtn.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                currentSelectIndex = (int) sbtn.getTag();
                change();
            }
        });
        return sbtn;
    }

    private void change() {
        final LinearLayout linearLayout = (LinearLayout) getChildAt(0);
        final int childCount = linearLayout.getChildCount();
        for (int i = 0; i < childCount; i++) {
            final View childView = linearLayout.getChildAt(i);
            if (childView instanceof SuperButton) {
                SuperButton child = (SuperButton) childView;
                boolean selected = (int) child.getTag() == currentSelectIndex;
                child.setShapeStrokeColor(ContextCompat.getColor(getContext(), R.color.comm_blue))
                        .setShapeSolidColor(ContextCompat.getColor(getContext(), !selected ? R.color.white : R.color.comm_blue))
                        .setUseShape();
                child.setTextColor(ContextCompat.getColor(getContext(), selected ? R.color.white : R.color.comm_blue));
                child.setEnabled(tag != currentSelectIndex);
            }
        }

        if (onDaySelectClickListener != null && currentSelectIndex != -1) {
            onDaySelectClickListener.onDaySelect(currentSelectIndex);
        }
    }


    OnDaySelectClickListener onDaySelectClickListener;
    OnSelectViewClickListener onSelectViewClickListener;

    public interface OnDaySelectClickListener {
        void onDaySelect(int day);
    }

    public interface OnSelectViewClickListener {
        void onSelectView();
    }

    public void setOnDaySelectClickListener(OnDaySelectClickListener onDaySelectClickListener) {
        this.onDaySelectClickListener = onDaySelectClickListener;
    }

    public void setOnSelectViewClickListener(OnSelectViewClickListener onSelectViewClickListener) {
        this.onSelectViewClickListener = onSelectViewClickListener;
    }
}
