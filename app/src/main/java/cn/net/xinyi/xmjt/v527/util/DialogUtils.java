package cn.net.xinyi.xmjt.v527.util;

import android.app.Activity;
import android.content.Context;
import android.support.annotation.NonNull;

import com.afollestad.materialdialogs.MaterialDialog;
import com.xinyi_tech.comm.form.DictField;

import java.util.Collection;

/**
 * Created by Fracesuit on 2017/7/28.
 */

public class DialogUtils {


    public static void showSingleSelectDialog(Activity compatActivity, Collection<String> data, @NonNull MaterialDialog.ListCallbackSingleChoice singleButtonCallback) {
        showSingleSelectDialog(compatActivity, data, -1, singleButtonCallback);
    }

    public static void showSingleSelectDialog(Activity compatActivity, Collection<String> data, int selectedIndex, @NonNull MaterialDialog.ListCallbackSingleChoice singleButtonCallback) {
        showSingleSelectDialog(compatActivity, null, data, -1, singleButtonCallback);
    }

    public static void showSingleSelectDialog(Activity compatActivity, String title, Collection<String> data, int selectedIndex, @NonNull MaterialDialog.ListCallbackSingleChoice singleButtonCallback) {
        new MaterialDialog.Builder(compatActivity)
                .items(data)
                .title(title)
                .autoDismiss(true)
                .itemsCallbackSingleChoice(selectedIndex, singleButtonCallback)
                .positiveText("确认")
                .negativeText("取消")
                .show();
    }

    public static void showSingleDictSelectDialog(Activity compatActivity, String title, Collection<DictField> data, int selectedIndex, @NonNull MaterialDialog.ListCallbackSingleChoice singleButtonCallback) {
        new MaterialDialog.Builder(compatActivity)
                .items(data)
                .title(title)
                .cancelable(true)
                .autoDismiss(true)
                .itemsCallbackSingleChoice(selectedIndex, singleButtonCallback)
                .show();
    }


    public static void showDialog(Context compatActivity, String content, @NonNull MaterialDialog.SingleButtonCallback onPositiveListener, @NonNull MaterialDialog.SingleButtonCallback onNegativeListener) {
        new MaterialDialog.Builder(compatActivity)
                .title("提示")
                .content(content)
                .canceledOnTouchOutside(false)
                .autoDismiss(true)
                .positiveText("确认")
                .negativeText("取消")
                .onPositive(onPositiveListener)
                .onNegative(onNegativeListener)
                .build().show();
    }

    public static void showDialog(Context compatActivity, String title, String content) {
        new MaterialDialog.Builder(compatActivity)
                .title(title)
                .content(content)
                .canceledOnTouchOutside(false)
                .autoDismiss(true)
                .positiveText("我知道了")
                .build().show();
    }

}
