package cn.net.xinyi.xmjt.model;

import cn.net.xinyi.xmjt.R;

/**
 * Created by studyjun on 2016/5/1.
 */
public class ModelItem {
    /**
     * "车牌助手", "二三类点助手","治安采集","警情采集","人员盘查",
     "随手拍","采集管理","采集统计","员工考勤","巡逻","岗亭","巡逻设置","警力分布"
     */



    public static final int ICON_CPZS= R.drawable.menu_xpr;//车牌助手
    public static final int ICON_ESLDZS=R.drawable.menu_infomation ;//二三类点助手
    public static final int ICON_ZACJ= R.drawable.menu_zajc;//治安采集
    public static final int ICON_QJCJ=R.drawable.menu_jqcj;//警情采集
    public static final int ICON_RYPC=R.drawable.menu_prereturn;//人员盘查
    public static final int ICON_SSP=R.drawable.menu_ssp;//随手拍
    public static final int ICON_RENTALHOUSE=R.drawable.menu_house;//出租屋采集
    public static final int ICON_CJGL= R.drawable.menu_guanli;//采集管理
    public static final int ICON_CJTJ=R.drawable.menu_tongji;//采集统计
    public static final int ICON_YGKQ=R.drawable.menu_kq;//员工考勤
    public static final int ICON_XL=R.drawable.menu_xl;//巡逻
    public static final int ICON_GT=R.drawable.menu_gangting;//岗亭
    public static final int ICON_XLSZ=R.drawable.menu_xlsz;//巡逻设置
    public static final int ICON_JLFB=R.drawable.menu_plc_map;//警力分布
    public static final int ICON_QWTJ=R.drawable.menu_qwtj;//勤务统计
    public static final int ICON_XDXT=R.drawable.menu_xtdk;//系统选点
    public static final int ICON_QWDC=R.drawable.menu_qwdc;//勤务督查
    public static final int ICON_SALALY=R.drawable.menu_salary;//薪资查询
    public static final int ICON_SAFETY_MANAGE=R.drawable.menu_safeyt_manage;//安全管理
    public static final int ICON_TSYY=R.drawable.menu_tsyy;//提审预约
    public static final int ICON_XXHC=R.drawable.menu_infocheck;//信息核查
    public static final int ICON_WCFT=R.drawable.menu_infocheck;//外出防逃


    public static final int ID_CPZS=1;
    public static final int ID_ESLDZS=2;
    public static final int ID_ZACJ=3;
    public static final int ID_QJCJ=4;
    public static final int ID_RYPC=5;
    public static final int ID_SSP=6;
    public static final int ID_CJGL=7;
    public static final int ID_CJTJ=8;
    public static final int ID_YGKQ=9;
    public static final int ID_XL=10;
    public static final int ID_GT=11;
    public static final int ID_XLSZ=12;
    public static final int ID_JLFB=13;
    public static final int ID_QWTJ=14;
    public static final int ID_XDXT=15;
    public static final int ID_QWDC=16;
    public static final int ID_RENTALHOUSE=17;
    public static final int ID_SALALY=18;
    public static final int ID_SAFETY_MANAGE=19;
    public static final int ID_TSYY=20;
    public static final int ID_XXHC=21;
    public static final int ID_WCFT=22;


    public static final String NAME_CPZS="车牌助手";
    public static final String NAME_ESLDZS="二三类点";
    public static final String NAME_ZACJ="治安采集";
    public static final String NAME_QJCJ="警情采集";
    public static final String NAME_RYPC="人员盘查";
    public static final String NAME_SSP="随手抓拍";
    public static final String NAME_RENTALHOUSE="出租屋信息";
    public static final String NAME_CJGL="采集管理";
    public static final String NAME_CJTJ="采集统计";
    public static final String NAME_YGKQ="考勤签到";
    public static final String NAME_XL="警务巡逻";
    public static final String NAME_GT="卡点防控";
    public static final String NAME_XLSZ="勤务设置";
    public static final String NAME_JLFB="地图展示";
    public static final String NAME_QWTJ="勤务统计";
    public static final String NAME_XDXT="监控选点";
    public static final String NAME_QWDC="勤务督查";
    public static final String NAME_SALALY="薪资查询";
    public static final String NAME_SAFETY_MANAGE="安全管理";
    public static final String NAME_TSYY="提审预约";
    public static final String NAME_XXHC="信息核查";
    public static final String NAME_WCFT="外出防逃";

    public String name;
    public int id;
    public int resIcon;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getResIcon() {
        return resIcon;
    }

    public void setResIcon(int resIcon) {
        this.resIcon = resIcon;
    }

    public ModelItem(String name, int id, int resIcon) {
        this.name = name;
        this.id = id;
        this.resIcon = resIcon;
    }

    public ModelItem() {
    }
}
