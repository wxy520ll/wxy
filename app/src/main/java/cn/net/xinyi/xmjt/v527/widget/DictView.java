package cn.net.xinyi.xmjt.v527.widget;

import android.app.Activity;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.AppCompatTextView;
import android.util.AttributeSet;
import android.view.View;

import com.afollestad.materialdialogs.MaterialDialog;
import com.blankj.utilcode.util.Utils;
import com.xinyi_tech.comm.form.DictField;
import com.xinyi_tech.comm.form.IFormField;

import java.util.ArrayList;
import java.util.List;

import cn.net.xinyi.xmjt.R;
import cn.net.xinyi.xmjt.v527.util.DialogUtils;

/**
 * Created by Fracesuit on 2018/1/19.
 */

public class DictView extends AppCompatTextView implements IFormField {
    List<DictField> datas = new ArrayList<>();
    int currentSelectIndex = 0;
    Activity activity;

    public DictView(Activity context, @NonNull List<DictField> datas) {
        super(context);
        this.datas = datas;
        init();
    }

    public DictView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public void init() {
        Drawable drawable = ContextCompat.getDrawable(Utils.getApp(), R.drawable.icon_arrow_down);
        drawable.setBounds(0, 0, drawable.getMinimumWidth(), drawable.getMinimumHeight());
        setCompoundDrawables(null, null, drawable, null);
        setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                DialogUtils.showSingleDictSelectDialog(activity, "请选择", datas, currentSelectIndex, new MaterialDialog.ListCallbackSingleChoice() {
                    @Override
                    public boolean onSelection(MaterialDialog dialog, View itemView, int which, CharSequence text) {
                        final DictField dictField = datas.get(which);
                        final String name = dictField.getName();
                        currentSelectIndex = which;
                        setDictText();
                        return true;
                    }
                });
            }
        });
    }

    public void setData(Activity activity, List<DictField> datas, String defautSelectValue) {
        this.activity = activity;
        this.datas = datas;
        setVaule(defautSelectValue);
    }


    public String getValue() {
        return datas.get(currentSelectIndex).getValue();
    }

    @Override
    public void setVaule(String value) {
        if (value != null) {
            for (int i = 0, len = datas.size(); i < len; i++) {
                final DictField dictField = datas.get(i);
                if (value.equals(dictField.getValue())) {
                    currentSelectIndex = i;
                }
            }
        }

        setDictText();
    }

    private void setDictText() {
        final DictField dictField = datas.get(currentSelectIndex);
        setText(dictField.getName());
        if (onDictItemClickListener == null ||
                onDictItemClickListener.onItemClick(currentSelectIndex, dictField.getValue(), dictField.getName())) {
        }
    }

    OnDictItemClickListener onDictItemClickListener;

    public void setOnDictItemClickListener(OnDictItemClickListener onDictItemClickListener) {
        this.onDictItemClickListener = onDictItemClickListener;
    }

    public interface OnDictItemClickListener {
        boolean onItemClick(int index, String value, String name);
    }
}
