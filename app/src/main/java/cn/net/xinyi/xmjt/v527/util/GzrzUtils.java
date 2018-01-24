package cn.net.xinyi.xmjt.v527.util;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.widget.TextView;
import android.widget.Toast;

import com.xinyi_tech.comm.util.ToastyUtil;

import cn.addapp.pickers.listeners.OnItemPickListener;
import cn.addapp.pickers.picker.DatePicker;
import cn.addapp.pickers.picker.SinglePicker;
import cn.net.xinyi.xmjt.utils.StringUtils;

import static cn.net.xinyi.xmjt.v527.base.BaseJwtActivity.FILE_SELECT_CODE;

/**
 * Created by jiajun.wang on 2017/12/30.
 */

public class GzrzUtils {

    public static void showTypeSelect(final TextView textView, final String []arrayItem, Activity activity) {

        if (arrayItem==null){
            ToastyUtil.warningShort("日志类型数据获取失败");
            return;
        }
        SinglePicker<String> picker = new SinglePicker<String>(activity, arrayItem);
        picker.setTitleText("日志类型");
        picker.setTitleTextSize(18);
        picker.setCanLoop(false);//不禁用循环
        picker.setLineVisible(true);
        picker.setShadowVisible(false);
        picker.setTextSize(20);
        picker.setCancelTextColor(Color.parseColor("#7cb6e7"));
        picker.setSubmitTextColor(Color.parseColor("#7cb6e7"));
        picker.setSelectedIndex(0);
        picker.setWheelModeEnable(true);
        //启用权重 setWeightWidth 才起作用
        picker.setWeightEnable(true);
        picker.setWeightWidth(1);
        picker.setTopPadding(15);
        picker.setSelectedTextColor(0xFF279BAA);//前四位值是透明度
        picker.setUnSelectedTextColor(0xFF999999);
        picker.setOnItemPickListener(new OnItemPickListener<String>() {
            @Override
            public void onItemPicked(int index, String item) {
                textView.setText(item);
            }
        });
        picker.show();
    }

    public static void onYearMonthDayTimePicker(final TextView textView, Activity context) {
        final DatePicker picker = new DatePicker(context);
        picker.setCanLoop(false);
        picker.setTitleText("日志时间");
        picker.setTitleTextSize(18);
        picker.setCancelTextColor(Color.parseColor("#7cb6e7"));
        picker.setSubmitTextColor(Color.parseColor("#7cb6e7"));
        picker.setWheelModeEnable(true);
        picker.setTopPadding(15);
        picker.setTextSize(20);
        picker.setRangeStart(2016, 8, 29);
        picker.setRangeEnd(2111, 1, 11);
        picker.setWeightEnable(true);
        picker.setOnDatePickListener(new DatePicker.OnYearMonthDayPickListener() {
            @Override
            public void onDatePicked(String year, String month, String day) {
                textView.setText(year + "-" + month + "-" + day);
            }
        });
        picker.show();
    }



    public static void showFileChooser(Activity activity) {
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.setType("*/*");
        intent.addCategory(Intent.CATEGORY_OPENABLE);

        try {
            activity.startActivityForResult(Intent.createChooser(intent, "Select a File to Upload"), FILE_SELECT_CODE);
        } catch (android.content.ActivityNotFoundException ex) {
            Toast.makeText(activity, "Please install a File Manager.", Toast.LENGTH_SHORT).show();
        }
    }
    public static String getFileName(String pathandname){
        int start=pathandname.lastIndexOf("/");
        int end=pathandname.lastIndexOf(".");
        if(start!=-1 && end!=-1){
            return pathandname.substring(start+1,end);
        }else{
            return pathandname;
        }
    }

    public static boolean checkTextView(TextView textView){
        if (StringUtils.isEmpty(textView.getText().toString())){
            ToastyUtil.warningLong(textView.getHint().toString());
            return false;
        }else {
            return true;
        }
    }
}
