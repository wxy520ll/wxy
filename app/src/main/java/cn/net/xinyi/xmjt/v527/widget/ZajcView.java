package cn.net.xinyi.xmjt.v527.widget;

import android.content.Context;
import android.graphics.Typeface;
import android.support.annotation.IdRes;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentActivity;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.ForegroundColorSpan;
import android.text.style.StyleSpan;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.alibaba.fastjson.JSONObject;
import com.blankj.utilcode.util.SizeUtils;
import com.xinyi_tech.comm.form.FieldRadioGroup;
import com.xinyi_tech.comm.form.FieldView;
import com.xinyi_tech.comm.util.ResUtils;
import com.xinyi_tech.comm.util.ToastyUtil;

import java.util.List;

import cn.net.xinyi.xmjt.R;
import cn.net.xinyi.xmjt.v527.data.remote.retrofit.entity.ZajcEntity;
import cn.net.xinyi.xmjt.v527.util.DialogUtils;

/**
 * Created by Fracesuit on 2017/12/31.
 */

public class ZajcView extends LinearLayout {

    public ZajcView(Context context) {
        super(context);
        init();
    }

    public ZajcView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public void with(final FragmentActivity activity, final ZajcEntity zajcEntity) {
        removeAllViews();
        TextView titleTv = new TextView(getContext());
        titleTv.setTextSize(14);
        addView(titleTv);


        final String s = zajcEntity.getITEMCNAME() + "[规范]";
        final int length = s.length();
        final SpannableString spannableString = new SpannableString(s);
        int start = length - 4;
        spannableString.setSpan(new ForegroundColorSpan(ResUtils.getColor(R.color.comm_blue)), start, length, Spannable.SPAN_INCLUSIVE_EXCLUSIVE);
        spannableString.setSpan(new StyleSpan(Typeface.BOLD), start, length, Spannable.SPAN_INCLUSIVE_EXCLUSIVE);
        titleTv.setText(spannableString);
        titleTv.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                final List<JSONObject> list = zajcEntity.getGfList();//title  lawdes  handledesc
                final StringBuffer sb = new StringBuffer();
                if (list != null) {
                    for (JSONObject j : list) {
                        sb.append(j.getString("title") + "\n");
                        sb.append(j.getString("lawdes") + "\n");
                        sb.append(j.getString("handledesc") + "\n");
                    }
                    DialogUtils.showDialog(activity, "规范", sb.toString());
                } else {
                    ToastyUtil.warningShort("没有规范文件");
                }
            }
        });
        final FieldView.Builder builder = FieldView.newBuilder("", "select").valueInitContent(zajcEntity.getCKDESC_CN());
        final LayoutParams layoutParams = new LayoutParams(LayoutParams.WRAP_CONTENT, SizeUtils.dp2px(48));
        layoutParams.gravity = Gravity.RIGHT;
        layoutParams.rightMargin = SizeUtils.dp2px(10);
        final FieldRadioGroup fieldRadioGroup = new FieldRadioGroup(builder, getContext());
        fieldRadioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, @IdRes int checkedId) {
                zajcEntity.setDEFAULTCHECK(fieldRadioGroup.getValue());
            }
        });

        addView(fieldRadioGroup, layoutParams);
    }

    private void init() {
        setOrientation(LinearLayout.VERTICAL);
    }
}
