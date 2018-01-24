package cn.net.xinyi.xmjt.utils;


import android.app.Activity;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.text.format.DateFormat;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.DatePicker;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.List;
import java.util.Locale;

import cn.net.xinyi.xmjt.R;
import cn.net.xinyi.xmjt.config.AppContext;
import cn.net.xinyi.xmjt.model.UserInfo;

/**
 * Created by hao.zhou on 2016/2/22.
 */
public class BaseDataUtils {
    private static long lastClickTime;
    private Context myContext;

    /***
     * 日历初始化
     */
    private Calendar c = Calendar.getInstance();

    public BaseDataUtils() {
        super();
    }

    /**
     * RadioGroup
     * RadioButton 选择是否
     * 0  代表 是  1 代表否
     **/

    public int getRGNum(RadioGroup group, int rb_sfglzr_0) {
        int num;
        //获取变更后的选中项的ID
        int radioButtonId = group.getCheckedRadioButtonId();
//        //根据ID获取RadioButton的实例
//        RadioButton rb = (RadioButton) aty.findViewById(radioButtonId);
        if (radioButtonId == rb_sfglzr_0) {
            num = 0;
        } else {
            num = 1;
        }
        return num;
    }

    public static void initRBCheck(int i_rb, RadioButton rb_0, RadioButton rb_1, int i) {
        if (i_rb == 0) {
            i = 0;
            rb_0.setChecked(true);
        } else if (i_rb == 1) {
            i = 1;
            rb_1.setChecked(true);
        }
    }

    /**
     * 选择日期
     **/
    public void getData(Activity aty, final TextView textView) {
        new DatePickerDialog(aty, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view,
                                  int year, int monthOfYear,
                                  int dayOfMonth) {
                c.set(year, monthOfYear, dayOfMonth);
                CharSequence forma = DateFormat.format("yyyy-MM-dd", c);
                textView.setText(forma);
            }
        }, c.get(Calendar.YEAR), c.get(Calendar.MONTH), c
                .get(Calendar.DAY_OF_MONTH)).show();
    }

    /**
     * 选择日期
     **/
    public void getMonthData(Activity aty, final TextView textView) {
        new DatePickerDialog(aty, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view,
                                  int year, int monthOfYear,
                                  int dayOfMonth) {
                c.set(year, monthOfYear, dayOfMonth);
                CharSequence forma = DateFormat.format("yyyyMMdd", c);
                textView.setText(forma);
            }
        }, c.get(Calendar.YEAR), c.get(Calendar.MONTH), c
                .get(Calendar.DAY_OF_MONTH)).show();
    }

    /**
     * 选择日期
     **/
    public static String getNowData() {
        return DateFormat.format("yyyy-MM-dd kk:mm:ss", Calendar.getInstance(Locale.CHINA)).toString();
    }

    /**
     * 选择日期 获取年-月-日
     **/
    public static String getNowYearAndMonthAndDay() {
        return DateFormat.format("yyyy-MM-dd", Calendar.getInstance(Locale.CHINA)).toString();
    }

    /**
     * 选择日期 获取年-月-日
     **/
    public static String getNowYearAndMonth() {
        return DateFormat.format("yyyyMM", Calendar.getInstance(Locale.CHINA)).toString();
    }

    /**
     * 选择日期  中文显示
     **/
    public static String getNowDataC() {
        return DateFormat.format("yyyy年MM月dd日 kk:mm", Calendar.getInstance(Locale.CHINA)).toString();
    }

    /**
     * 选择日期  中文显示
     **/
    public static String getNowYearC() {
        return DateFormat.format("yyyy年MM月dd日", Calendar.getInstance(Locale.CHINA)).toString();
    }


    /***
     * diaolog弹出框
     * 点击控件-弹出显示
     **/
    public static void showAlertDialog(Activity aty, final List<String> dpfl, final TextView tv_mdfl) {
        if (dpfl.size() == 0) {
            tv_mdfl.setText(R.string.msg_null);
        } else {
            new AlertDialog.Builder(aty).setItems(dpfl.toArray(new String[dpfl.size()]),
                    new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            tv_mdfl.setText(dpfl.toArray(new String[dpfl.size()])[which]);
                        }
                    }).create().show();
        }
    }


    /***
     * diaolog弹出框
     * 点击控件-弹出显示
     **/
    public static void showAlertDialog(Activity aty, final Collection<String> dpfl, final TextView tv_mdfl) {
        if (dpfl.size() == 0) {
            tv_mdfl.setText(R.string.msg_null);
        } else {
            new AlertDialog.Builder(aty).setItems(dpfl.toArray(new String[dpfl.size()]),
                    new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            tv_mdfl.setText(dpfl.toArray(new String[dpfl.size()])[which]);
                        }
                    }).create().show();
        }
    }


    /***
     * diaolog弹出框
     * 点击控件-弹出显示
     **/
    public static void showSpinnerDialog(Activity aty, final List dpfl, final TextView tv_mdfl) {
        if (dpfl.size() == 0) {
            tv_mdfl.setText(R.string.msg_null);
        } else {
            final List<String> list = new ArrayList<>();
            for (Object o : dpfl) {
                list.add(o.toString());
            }
            new AlertDialog.Builder(aty).setItems(list.toArray(new String[dpfl.size()]),
                    new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            tv_mdfl.setText(list.toArray(new String[list.size()])[which]);
                        }
                    }).create().show();
        }
    }

    /***
     * diaolog弹出框
     * 点击控件-弹出显示
     **/
    public static void showAlertDialog(Activity aty, final String[] dpfl, final TextView tv_mdfl) {
        if (dpfl.length == 0) {
            tv_mdfl.setHint(R.string.msg_null);
        } else {
            new AlertDialog.Builder(aty).setItems(dpfl,
                    new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            tv_mdfl.setText(dpfl[which]);
                        }
                    }).create().show();
        }

    }

    /***
     * 未点击保存的 提示 是否提出当前界面
     *
     * @param aty 当前activity
     */
    public static void loginOut(final Activity aty) {
        new AlertDialog.Builder(aty)
                .setTitle("提示").
                setMessage(aty.getResources().getString(R.string.logout_tips))
                .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        aty.finish();
                    }
                }).setNegativeButton("取消", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
            }
        }).show();
    }

    /**
     * 把指定AutoCompleteTextView中内容保存到sharedPreference中指定的字符段
     *
     * @param history 保存在sharedPreference中的字段名
     * @param et_fwbm 要操作的AutoCompleteTextView
     */
    public static void saveHistory(Activity aty, String history, String num, AutoCompleteTextView et_fwbm) {
        String text = et_fwbm.getText().toString();
        SharedPreferences sp = aty.getSharedPreferences("network_url", 0);
        String longhistory = sp.getString(history, num);
        if (!longhistory.contains(text + ",")) {
            StringBuilder sb = new StringBuilder(longhistory);
            sb.insert(0, text + ",");
            sp.edit().putString("history", sb.toString()).commit();
        }
    }

    /**
     * 初始化AutoCompleteTextView，最多显示5项提示，使 AutoCompleteTextView在一开始获得焦点时自动提示
     *
     * @param history 保存在sharedPreference中的字段名
     * @param et_fwbm 要操作的AutoCompleteTextView
     */
    public static void initAutoComplete(Activity aty, String history, String num, AutoCompleteTextView et_fwbm) {

        SharedPreferences sp = aty.getSharedPreferences("network_url", 0);
        String longhistory = sp.getString(history, num);
        String[] histories = longhistory.split(",");
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(aty,
                android.R.layout.simple_list_item_1, histories);
        // 只保留最近的50条的记录
        if (histories.length > 10) {
            String[] newHistories = new String[10];
            System.arraycopy(histories, 0, newHistories, 0, 10);
            adapter = new ArrayAdapter<String>(aty,
                    android.R.layout.simple_list_item_1, newHistories);
        }
        et_fwbm.setAdapter(adapter);
        et_fwbm.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                AutoCompleteTextView view = (AutoCompleteTextView) v;
                if (hasFocus) {
                    view.showDropDown();
                }
            }
        });
    }

    /**
     * 防止用户疯狂的点击某个button
     */
    public synchronized static boolean isFastClick() {
        long time = System.currentTimeMillis();
        if (time - lastClickTime < 1000) {
            return true;
        }
        lastClickTime = time;
        return false;
    }

    /**
     * 领导不要显示采集轨迹数据
     *
     * @string 060387  华振强  局长
     * @string 050525  陈卫军  政委
     * @string 064482  彭国庆  第一副局长
     * @string 060138  于承恩  副局长
     * @string 061464  王玉芝  政治处主任
     * @string 064717  罗明    指挥处处长
     * @string 060282  欧伟国   副局长
     * @string 054568  潘宗强   第一副督察长
     * @string 054661  黄雄
     */
    public static boolean notLeader() {
        String policeNo = AppContext.instance.getLoginInfo().getPoliceno();
        return !(policeNo != null && (policeNo.equals("060387") || policeNo.equals("050525") || policeNo.equals("064482") || policeNo.equals("054568") ||
                policeNo.equals("060138") || policeNo.equals("061464") || policeNo.equals("064717") || policeNo.equals("060282") ||
                policeNo.equals("054661")));
    }

    /**
     * 交流中心管理员权限
     **/
    public static boolean isAdminCommit() {
        return AppContext.instance().getLoginInfo().getPcs().contains("视频警察")
                || AppContext.instance().getLoginInfo().getPcs().contains("指挥")
                || AppContext.instance().getLoginInfo().getPcs().contains("人口")
                || AppContext.instance().getLoginInfo().getPcs().equals("龙岗分局")
                || AppContext.instance().getLoginInfo().getPcs().contains("情报");
    }

    /**
     * 单位设置权限
     **/
    public static boolean isAdminPcs() {
        return ("民警").equals(AppContext.instance().getLoginInfo().getAccounttype())
                && (AppContext.instance().getLoginInfo().getPcs().contains("人口")
                || AppContext.instance().getLoginInfo().getPcs().contains("视频警察")
                || AppContext.instance().getLoginInfo().getPcs().contains("卡点大队")
                || AppContext.instance().getLoginInfo().getPcs().contains("治安")
                || AppContext.instance().getLoginInfo().getPcs().contains("指挥")
                || AppContext.instance().getLoginInfo().getPcs().contains("机训")
                || AppContext.instance().getLoginInfo().getPcs().equals("龙岗分局")
                || AppContext.instance().getLoginInfo().getPcs().contains("情报"));
    }


    /**
     * 社区单位权限设置设置权限
     **/
    public static boolean isAdminCommunityPcs() {
        return AppContext.instance().getLoginInfo().getPcs().contains("人口")
                || AppContext.instance().getLoginInfo().getPcs().contains("视频警察")
                || AppContext.instance().getLoginInfo().getPcs().contains("治安")
                || AppContext.instance().getLoginInfo().getPcs().contains("指挥")
                || AppContext.instance().getLoginInfo().getPcs().equals("龙岗分局")
                || !notLeader();
    }


    /**
     * 社区单位权限设置设置权限
     **/
    public static boolean isPcs() {

        return AppContext.instance().getLoginInfo().getPcs().equals("水径所")
                || AppContext.instance().getLoginInfo().getPcs().equals("布吉所")
                || AppContext.instance().getLoginInfo().getPcs().equals("罗岗所")
                || AppContext.instance().getLoginInfo().getPcs().equals("坂田所")
                || AppContext.instance().getLoginInfo().getPcs().equals("宝岗所")
                || AppContext.instance().getLoginInfo().getPcs().equals("沙湾所")
                || AppContext.instance().getLoginInfo().getPcs().equals("南岭所")
                || AppContext.instance().getLoginInfo().getPcs().equals("李朗所")
                || AppContext.instance().getLoginInfo().getPcs().equals("横岗所")
                || AppContext.instance().getLoginInfo().getPcs().equals("荷坳所")
                || AppContext.instance().getLoginInfo().getPcs().equals("六约所")
                || AppContext.instance().getLoginInfo().getPcs().equals("梧桐所")
                || AppContext.instance().getLoginInfo().getPcs().equals("龙新所")
                || AppContext.instance().getLoginInfo().getPcs().equals("同乐所")
                || AppContext.instance().getLoginInfo().getPcs().equals("龙岗所")
                || AppContext.instance().getLoginInfo().getPcs().equals("宝龙所")
                || AppContext.instance().getLoginInfo().getPcs().equals("新生所")
                || AppContext.instance().getLoginInfo().getPcs().equals("爱联所")
                || AppContext.instance().getLoginInfo().getPcs().equals("盛平所")
                || AppContext.instance().getLoginInfo().getPcs().equals("新城所")
                || AppContext.instance().getLoginInfo().getPcs().equals("平湖所")
                || AppContext.instance().getLoginInfo().getPcs().equals("平南所")
                || AppContext.instance().getLoginInfo().getPcs().equals("辅城所")
                || AppContext.instance().getLoginInfo().getPcs().equals("坪地所")
                || AppContext.instance().getLoginInfo().getPcs().equals("大运城所");
    }


    /**
     * 是否是民警
     **/
    public static boolean isPOLICE() {
        return AppContext.instance.getLoginInfo().getAccounttype().equals("民警");
    }

    /**
     * 是厂商还是管道公司
     **/
    public static int isCompanyOrOther() {
        UserInfo userInfo = AppContext.instance().getLoginInfo();

        int type = 0;

        if ("民警".equals(userInfo.getAccounttype())) {
            type = 6;
        } else if (null != userInfo.getAccounttype() && userInfo.getAccounttype().contains("厂商")) {//厂商
            type = 1;
        } else if (null != userInfo.getAccounttype() && userInfo.getAccounttype().contains("管道")) {//管道公司
            type = 2;
        } else if (null != userInfo.getPcs() && userInfo.getPcs().contains("视频警察") && userInfo.getFj().contains("龙岗")) {//视频大队
            type = 3;
        } else if (null != userInfo.getFj() && !userInfo.getFj().contains("龙岗")) {//不是龙岗分局单位的人员
            type = 4;
        } else {//分局单位
            type = 5;
        }
        return type;
    }


    public BaseDataUtils(Context context) {
        this.myContext = context;
    }


    //巡段级别
    public static String getLevelNum(String s) {
        String snum = null;
        if (s.equals("一级")) {
            snum = "01";
        } else if (s.equals("二级")) {
            snum = "02";
        } else if (s.equals("三级")) {
            snum = "03";
        } else if (s.equals("视频")) {
            snum = "04";
        } else if (s.equals("春运巡段")) {
            snum = "05";
        }
        return snum;
    }

    public static String getLevel(String s) {
        String text = null;
        if (s.equals("01")) {
            text = "一级";
        } else if (s.equals("02")) {
            text = "二级";
        } else if (s.equals("03")) {
            text = "三级";
        } else if (s.equals("04")) {
            text = "视频";
        } else if (s.equals("05")) {
            text = "春运巡段";
        }
        return text;
    }

    //巡段方式
    public static String getType(String s) {
        String text = null;
        if (s.equals("01")) {
            text = "徒步";
        } else if (s.equals("02")) {
            text = "GPS车辆";
        } else if (s.equals("03")) {
            text = "摩托车";
        } else if (s.equals("04")) {
            text = "视频";
        } else if (s.equals("05")) {
            text = "电瓶车";
        } else if (s.equals("06")) {
            text = "自行车";
        } else if (s.equals("99")) {
            text = "其他";
        }
        return text;
    }

    public static String getTypeNum(String s) {
        String snum = null;
        if (s.equals("徒步")) {
            snum = "01";
        } else if (s.equals("GPS车辆")) {
            snum = "02";
        } else if (s.equals("摩托车")) {
            snum = "03";
        } else if (s.equals("视频")) {
            snum = "04";
        } else if (s.equals("电瓶车")) {
            snum = "05";
        } else if (s.equals("自行车")) {
            snum = "06";
        } else if (s.equals("其他")) {
            snum = "99";
        }
        return snum;
    }


    //巡逻状态
    public static String dutyNumToDutyType(String s) {
        String text = null;
        if (s.equals("01")) {
            text = "巡逻";
        } else if (s.equals("02")) {
            text = "签到";
        } else if (s.equals("03")) {
            text = "报备";
        } else if (s.equals("04")) {
            text = "出警";
        }
        return text;
    }

    public static String dutyRulesNumToDutyRulesType(String s) {
        String text = null;
        if (s.equals("01")) {
            text = "巡段";
        } else if (s.equals("02")) {
            text = "防控点";
        } else if (s.equals("03")) {
            text = "重点区域";
        }
        return text;
    }

    public static String dutyRulesTypeToDutyNum(String s) {
        String text = null;
        if (s.equals("巡段")) {
            text = "01";
        } else if (s.equals("防控点")) {
            text = "02";
        } else if (s.equals("重点区域")) {
            text = "03";
        }
        return text;
    }


}
