package cn.net.xinyi.xmjt.utils.DB;

import android.app.Activity;
import android.content.Context;

import java.io.File;
import java.sql.SQLException;
import java.util.List;

import cn.net.xinyi.xmjt.config.AppContext;
import cn.net.xinyi.xmjt.model.JKSInfoModle;
import cn.net.xinyi.xmjt.model.SXTInfoModle;
import cn.net.xinyi.xmjt.model.ZAJCCYZModle;
import cn.net.xinyi.xmjt.model.ZAJCJKModle;
import cn.net.xinyi.xmjt.model.ZAJCModle;
import cn.net.xinyi.xmjt.model.ZAJCXFModle;

/**
 * Created by hao.zhou on 2015/10/29.
 * 采集信息查询辅助类
 */
public class CollectDBUtils {


    /**
     * 获取从业者本地数据库保存的从业者信息
     *
     * @return
     */
    public static List<ZAJCCYZModle> getCYZData(Context context, String glid) {
        List<ZAJCCYZModle> cyzInfos = null;
        try {
            cyzInfos = DBHelperNew.getInstance(context).getZACYZDao().queryBuilder().where().eq("gLID", glid).query();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return cyzInfos;
    }

    /**
     * 获取从业者本地数据库保存的监控信息
     *
     * @return
     */
    public static boolean delCYZData(Context context, int id) {
        boolean flag=false;
        try {
            DBHelperNew.getInstance(context).getZACYZDao().deleteById(id);
            flag=true;
        } catch (SQLException e) {
            e.printStackTrace();
            flag=false;
        }
        return flag;
    }

    /**
     * 获取监控本地数据库保存的监控信息
     *
     * @return
     */
    public static ZAJCJKModle getJKData(Context context, String glid) {
        ZAJCJKModle jkInfo = null;
        try {
            List<ZAJCJKModle> jkInfos = DBHelperNew.getInstance(context).getZAJKDao().queryBuilder().where().eq("gLID", glid).query();
            for (int i = 0; i < jkInfos.size(); i++) {
                jkInfo = jkInfos.get(i);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return jkInfo;
    }

    /**
     * 获取消防本地数据库保存的监控信息
     *
     * @return
     */
    public static ZAJCXFModle getXFData(Context context, String glid) {
        ZAJCXFModle xfInfo = null;
        try {
            List<ZAJCXFModle> jkInfos = DBHelperNew.getInstance(context).getZAXFDao().queryBuilder().where().eq("gLID", glid).query();
            for (int i = 0; i < jkInfos.size(); i++) {
                xfInfo = jkInfos.get(i);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return xfInfo;
    }


    /**
     * 获取本地数据库保存的监控信息
     *
     * @return
     */
    public static List<SXTInfoModle> getSXTLocalData(Context context) {

        String account = AppContext.instance.getLoginInfo().getUsername();
        if (account == null)
            return null;

        DBOperation dbo = new DBOperation(context);
        List<SXTInfoModle> list = dbo.getCamearInfoList(account);
        dbo.clossDb();
        return list;
    }


    /**
     * 获取监控室本地数据库保存的监控信息-最近5条数据
     *
     * @return
     */
    public static List<JKSInfoModle> getLocalCamearRoomInfo(Context context) {

        String account = AppContext.instance.getLoginInfo().getUsername();
        if (account == null)
            return null;
        DBOperation dbo = new DBOperation(context);
        List<JKSInfoModle> list = dbo.getLocalCamearRoomInfo(account);
        dbo.clossDb();
        return list;
    }

    /**
     * 获取监控室本地数据库保存的监控信息
     *
     * @return
     */
    public static List<JKSInfoModle> getJKSLocalData(Context context) {

        String account = AppContext.instance.getLoginInfo().getUsername();
        if (account == null)
            return null;

        DBOperation dbo = new DBOperation(context);
        List<JKSInfoModle> list = dbo.getCamearRoomInfoList(account);
        dbo.clossDb();
        return list;
    }


    /**治安采集上传成功  删除本地存储的照片**/
    public static void DeleteZALocal(Activity aty, List<ZAJCCYZModle> cyyModles, ZAJCJKModle jkInfo, ZAJCXFModle xfInfo, ZAJCModle mInfo) {
        for (ZAJCCYZModle cyzModle : cyyModles) {
            try {
                DBHelperNew.getInstance(aty).getZACYZDao().deleteById(cyzModle.getCYZID());
                if (cyzModle.getIV_CYZQSZ() != null) {
                    File plateImage = new File(cyzModle.getIV_CYZQSZ());
                    if (plateImage.exists()) {
                        plateImage.delete();
                    }
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        try {
            if (jkInfo!=null){
                DBHelperNew.getInstance(aty).getZAJKDao().deleteById(jkInfo.getJKID());
            }
            if (xfInfo!=null){
                DBHelperNew.getInstance(aty).getZAXFDao().deleteById(xfInfo.getXFID());
            }
            DBHelperNew.getInstance(aty).getZAJCDao().deleteById(mInfo.getZAJCID());
        } catch (SQLException e) {
            e.printStackTrace();
        }

        if(mInfo.getIV_MPHQJT()!=null)
        {
            File plateImage = new File(mInfo.getIV_MPHQJT());
            if (plateImage.exists()) {
                plateImage.delete();
            }
        }

        if(mInfo.getIV_MPHQJT()!=null)

        {
            File plateImage = new File(mInfo.getIV_MPHQJT());
            if (plateImage.exists()) {
                plateImage.delete();
            }
        }

        if(mInfo.getIV_YYZZ()!=null)

        {
            File plateImage = new File(mInfo.getIV_YYZZ());
            if (plateImage.exists()) {
                plateImage.delete();
            }
        }

        if(jkInfo !=null && jkInfo.getIV_JKPMT()!=null)

        {
            File plateImage = new File(jkInfo.getIV_JKPMT());
            if (plateImage.exists()) {
                plateImage.delete();
            }
        }

        if(jkInfo !=null && jkInfo.getIV_SXTQJT()!=null)

        {
            File plateImage = new File(jkInfo.getIV_SXTQJT());
            if (plateImage.exists()) {
                plateImage.delete();
            }
        }

        if(xfInfo !=null && xfInfo.getIV_XFQJT()!=null)

        {
            File plateImage = new File(xfInfo.getIV_XFQJT());
            if (plateImage.exists()) {
                plateImage.delete();
            }
        }
    }

    /**治安采集上传成功  删除本地存储的照片**/
    public static void DeleteZAImgLocal(List<ZAJCCYZModle> cyyModles, ZAJCJKModle jkInfo, ZAJCXFModle xfInfo, ZAJCModle mInfo) {
        for (ZAJCCYZModle cyzModle : cyyModles) {
            if (cyzModle.getIV_CYZQSZ() != null) {
                File plateImage = new File(cyzModle.getIV_CYZQSZ());
                if (plateImage.exists()) {
                    plateImage.delete();
                }
            }
        }

        if(mInfo.getIV_MPHQJT()!=null)
        {
            File plateImage = new File(mInfo.getIV_MPHQJT());
            if (plateImage.exists()) {
                plateImage.delete();
            }
        }

        if(mInfo.getIV_MPHQJT()!=null)

        {
            File plateImage = new File(mInfo.getIV_MPHQJT());
            if (plateImage.exists()) {
                plateImage.delete();
            }
        }

        if(mInfo.getIV_YYZZ()!=null)

        {
            File plateImage = new File(mInfo.getIV_YYZZ());
            if (plateImage.exists()) {
                plateImage.delete();
            }
        }

        if(jkInfo!=null && jkInfo.getIV_JKPMT()!=null)

        {
            File plateImage = new File(jkInfo.getIV_JKPMT());
            if (plateImage.exists()) {
                plateImage.delete();
            }
        }

        if(jkInfo!=null && jkInfo.getIV_SXTQJT()!=null)

        {
            File plateImage = new File(jkInfo.getIV_SXTQJT());
            if (plateImage.exists()) {
                plateImage.delete();
            }
        }

        if(xfInfo!=null && xfInfo.getIV_XFQJT()!=null)

        {
            File plateImage = new File(xfInfo.getIV_XFQJT());
            if (plateImage.exists()) {
                plateImage.delete();
            }
        }
    }

}
