package cn.net.xinyi.xmjt.utils;

/**
 * Created by hao.zhou on 2015/10/14.
 * 常量的使用
 */
public class GeneralUtils {
    /**
     * intent、Bunder。传递参数的 键
     * */
    //监控室-用于intent传递参数
    public final static String JKSInfo = "JKSINFO";
    //监控室编号-用于寻找下属摄像头--用于intent传递参数  键
    public final static String JKSBH = "JKSBH";
    public final static String JKSMC = "JKSMC";
    //摄像头-用于intent传递参数  键
    public final static String SXTInfo = "SXTInfo";
    //网格-用于intent传递参数  键
    public final static String WangGe = "WangGe";
    //ChooseWangGeActivity-用于intent传递参数  键
    public final static String RegistWangGe = "ChooseWangGeActivity";
    //用于intent传递参数  键
    public final static String Info = "info";
    /***数据库更新*/
    public final static boolean ISupload = true;
    public final static boolean ISCreate = false;
    /***上传数据前 版本的对比*/
    public final static String  BUILDNUMBER = "BUILDNUMBER";
    /***二三类点采集 汇总统计 */
    public final static String  SHZT = "SHZT";
    public final static String  TJLB = "TJLB";
    public final static String  JKS = "jks";
    public final static String  SXT = "sxt";
    public final static String  TOTAL = "TOTAL";
    /***数据分页 显示页码数*/
    public final static int pagesize = 20;

    /**
     * intent、跳转flag标识！
     * */
    //采集完成监控室传递监控室名字和监控室bh
    public final static int JKSInfoActivity = 1001;
    //核查采集监控室 flag 标识- 不通过
    public final static int CheckJKSInfo_flag = 1002;
    //核查采集摄像头 flag 标识- 不通过
    public final static int CheckSXTInfo_flag = 1003;
    //WatchSXTInfoActivity-用于intent跳转标识
    public final static int WatchSXTInfo = 1004;
    //WatchJKSInfoActivity-用于intent跳转标识
    public final static int WatchJKSInfo = 1005;
    //SettingActivity-用于intent跳转标识
    public final static int SettingActivity = 1006;
    //LoginActivity-用于intent跳转标识,修改本地注册为服务端
    public final static int LoginActivity = 1007;
    //AlreadyUploadActivity-用于intent跳转标识,
    public final static int CJUploadActivity = 1008;
    //SXTEditorActivity-用于intent跳转标识,
    public final static int SXTEditorActivity = 1009;
    //JKSListActivity-用于intent跳转标识,
    public final static int JKSListActivity = 1010;
    //DownJKSInfoActivity-用于intent跳转标识,
    public final static int DownJKSInfoActivity = 1011;
    //DownSXTInfoActivity-用于intent跳转标识,
    public final static int DownSXTInfoActivity = 1012;
    //CheckFaileJKSInfoActivity-用于intent跳转标识,
    public final static int CheckFaileJKSInfoActivity = 1013;
    //CheckFaileSXTInfoActivity-用于intent跳转标识,
    public final static int CheckFaileSXTInfoActivity = 1014;
    //WATCHINFO-网络下载部分 用于intent跳转标识,
    public final static int WATCHINFO = 1015;

    /**
     * 字典信息用到的 字典类表 标识
     * */

    //监控室业务分类
    public final static String XXCJ_JKSYWFL = "XXCJ_JKSYWFL";
    //摄像头所属环境
    public final static String XXCJ_SXTSSHJ = "XXCJ_SXTSSHJ";
    //摄像头场所分类
    public final static String XXCJ_SXTCSFL = "XXCJ_SXTCSFL";
    //摄像头状态
    public final static String XXCJ_SXTZT = "XXCJ_SXTZT";
    //摄像头方向
    public final static String XXCJ_SXTFX = "XXCJ_SXTFX";
    //摄像头类型
    public final static String XXCJ_SXTLX = "XXCJ_SXTLX";
    //人员身份
    public final static String XXCJ_RYSF = "XXCJ_RYSF";
    //街道
    public final static String ZZJG_JD = "ZZJG_JD";
    //社区
    public final static String ZZJG_SQ = "ZZJG_SQ";
    //市
    public final static String ZZJG_SJ = "ZZJG_SJ";
    //分局
    public final static String ZZJG_FJ = "ZZJG_FJ";
    //派出所
    public final static String ZZJG_PCS = "ZZJG_PCS";
    //摄像头类别
    public final static String XXCJ_SXTLB = "XXCJ_SXTLB";
    //摄像头保存时间
    public final static String XXCJ_SXTBCSJ = "XXCJ_SXTBCSJ";
    //警务室
    public final static String ZZJG_JWS = "ZZJG_JWS";
    //店铺分类
    public final static String XXCJ_MDFL = "XXCJ_MDFL";
    //店铺-经营范围
    public final static String XXCJ_MD_JYFW = "XXCJ_MD_JYFW";
    //快递-经营范围
    public final static String XXCJ_QY_JYFW = "XXCJ_QY_JYFW";
    //快递-网点类型
    public final static String XXCJ_WDTYPE = "XXCJ_WDTYPE";
    //经营性质
    public final static String XXCJ_JYXZ = "XXCJ_JYXZ";
    //经营场所分类
    public final static String XXCJ_CSFL = "XXCJ_CSFL";
    //回访结果
    public final static String XXCJ_ZFJG = "XXCJ_ZFJG";
    //治安基础分类
    public final static String XXCJ_ZAJCFL = "XXCJ_ZAJCFL";
    //治安基础分类（二级）
    public final static String XXCJ_ZAJCFL_JT = "XXCJ_ZAJCFL_JT";

    public final static String XXCJ_ZAJCFL_JT_NEW = "XXCJ_ZAJCFL_JT_NEW";
    //学历
    public final static String XXCJ_XL = "XXCJ_XL";
    //警情类别
    public final static String XXCJ_JQLB = "XXCJ_JQLB";

}
