package cn.net.xinyi.xmjt.model.Presenter;

import android.app.Activity;
import android.widget.TextView;

import com.j256.ormlite.dao.Dao;

import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import cn.net.xinyi.xmjt.model.View.IZAJCView;
import cn.net.xinyi.xmjt.model.ZAJCCYZModle;
import cn.net.xinyi.xmjt.model.ZAJCJKModle;
import cn.net.xinyi.xmjt.model.ZAJCModle;
import cn.net.xinyi.xmjt.model.ZAJCXFModle;
import cn.net.xinyi.xmjt.utils.BaseDataUtils;
import cn.net.xinyi.xmjt.utils.DB.DBHelperNew;

/**
 * Created by hao.zhou on 2016/3/19.
 */
public class ZAJCPresenter {
    private IZAJCView View;
    private ZAJCModle info;
    private ZAJCCYZModle ccyInfo;
    private ZAJCXFModle xfInfo=new ZAJCXFModle();
    private ZAJCJKModle jkInfo=new ZAJCJKModle();
    private Dao<ZAJCCYZModle, Integer> ZACYZDao;

    private Dao<ZAJCJKModle, Integer> ZAJKDao;
    private Dao<ZAJCModle, Integer> ZAJCDao;
    private Dao<ZAJCXFModle, Integer> ZAXFDao;
    private boolean flag;

    public ZAJCPresenter(IZAJCView View) {
        this.View = View;
        info=new ZAJCModle();
    }


    public boolean saveInfo(Activity aty, String lb, String cjfl,
                            String mc, String dz, String mph, String ldbm, String iv_mphqjt,
                            String iv_dmqjt, String jyzxm, String yjzdh, String fwdh,
                            String iv_yyzz, String zafzr, String sspcs,
                            String ssjws, Double jd, Double wd, String zafzrdh, int sfcjjk, int sfcjxf, String loctype, String bz,
                            List<ZAJCCYZModle> cyyModles, ZAJCXFModle xf, ZAJCJKModle jk, String iv_qt, String iv_hbpw, String iv_xfys, String iv_tzhy) {
        info.setLB(lb);
        info.setCJFL(cjfl);
        info.setMC(mc);
        info.setDZ(dz);
        info.setMPH(mph);
        info.setLDBM(ldbm);
        info.setIV_MPHQJT(iv_mphqjt);
        info.setIV_DMQJT(iv_dmqjt);
        info.setJYZXM(jyzxm);
        info.setJYZDH(yjzdh);
        info.setFWDH(fwdh);
        info.setIV_YYZZ(iv_yyzz);
        info.setIV_QT(iv_qt);
        info.setIV_HBPW(iv_hbpw);
        info.setIV_XFYS(iv_xfys);
        info.setIV_TZHY(iv_tzhy);
        info.setZAFZR(zafzr);
        info.setZAFZRDH(zafzrdh);
        info.setSSPCS(sspcs);
        info.setSSJWS(ssjws);
        info.setJD(jd);
        info.setWD(wd);
        info.setSFCJJK(sfcjjk);
        info.setSFCJXF(sfcjxf);
        info.setLOCTYPE(loctype);
        info.setBZ(bz);
        info.setCJSJ(new BaseDataUtils().getNowData());
        info.setGLID(new SimpleDateFormat("yyyyMMddHHmmssSSSS").format(new Date()));
        try {
            ZAJCDao = DBHelperNew.getInstance(aty).getZAJCDao();
            ZACYZDao = DBHelperNew.getInstance(aty).getZACYZDao();
            ZAJKDao = DBHelperNew.getInstance(aty).getZAJKDao();
            ZAXFDao = DBHelperNew.getInstance(aty).getZAXFDao();
            if (ZAJCDao != null) {
                /**基础信息**/
                ZAJCDao.create(info);
                /**从业者信息**/
                for (int i=0;i <cyyModles.size();i++){
                    ccyInfo=cyyModles.get(i);
                    ccyInfo.setGLID(info.getGLID());
                    ZACYZDao.create(ccyInfo);
                }
                /**监控信息**/
                if (jk!=null){
                    jkInfo=jk;
                    jkInfo.setGLID(info.getGLID());
                    ZAJKDao.create(jkInfo);
                }
                /**消防信息**/
                if (xf!=null){
                    xfInfo=xf;
                    xfInfo.setGLID(info.getGLID());
                    ZAXFDao.create(xfInfo);
                }
                flag=true;
            } else {
                flag=false;
            }
        } catch (SQLException e) {
            e.printStackTrace();
            flag=false;
        }
        return flag;
    }

    public boolean updateDB(Activity aty, String lb, String cjfl,
                            String mc, String dz, String mph, String ldbm, String iv_mphqjt,
                            String iv_dmqjt, String jyzxm, String yjzdh, String fwdh,
                            String iv_yyzz, String zafzr, String sspcs,
                            String ssjws, Double jd, Double wd, String zafzrdh,
                            int sfcjjk, int sfcjxf, String bz, List<ZAJCCYZModle> cyyModles, ZAJCXFModle xf, ZAJCJKModle jk, String iv_qt,
                            String iv_hbpw, String iv_xfys, String iv_tzhy, ZAJCModle zajc) {
        info.setLB(lb);
        info.setCJFL(cjfl);
        info.setMC(mc);
        info.setDZ(dz);
        info.setMPH(mph);
        info.setLDBM(ldbm);
        info.setIV_MPHQJT(iv_mphqjt);
        info.setIV_DMQJT(iv_dmqjt);
        info.setJYZXM(jyzxm);
        info.setJYZDH(yjzdh);
        info.setFWDH(fwdh);
        info.setIV_YYZZ(iv_yyzz);
        info.setIV_QT(iv_qt);
        info.setIV_HBPW(iv_hbpw);
        info.setIV_XFYS(iv_xfys);
        info.setIV_TZHY(iv_tzhy);
        info.setZAFZR(zafzr);
        info.setZAFZRDH(zafzrdh);
        info.setSSPCS(sspcs);
        info.setSSJWS(ssjws);
        info.setJD(jd);
        info.setWD(wd);
        info.setSFCJJK(sfcjjk);
        info.setSFCJXF(sfcjxf);
        info.setLOCTYPE(zajc.getLOCTYPE());
        info.setBZ(bz);
        info.setCJSJ(zajc.getCJSJ());
        info.setGLID(zajc.getGLID());
        try {
            ZAJCDao = DBHelperNew.getInstance(aty).getZAJCDao();
            ZACYZDao = DBHelperNew.getInstance(aty).getZACYZDao();
            ZAJKDao = DBHelperNew.getInstance(aty).getZAJKDao();
            ZAXFDao = DBHelperNew.getInstance(aty).getZAXFDao();
                if (ZAJCDao != null) {
                    info.setZAJCID(zajc.getZAJCID());
                    ZAJCDao.update(info);
                    //
                    for (int i=0;i <cyyModles.size();i++){
                        ccyInfo=cyyModles.get(i);
                        if (null == ccyInfo.getGLID()){
                            ccyInfo.setGLID(info.getGLID());
                            ZACYZDao.create(ccyInfo);
                        }else {
                            ZACYZDao.update(ccyInfo);
                        }
                    }
                    /**监控信息**/
                    if (jk!=null){
                        if (null == jk.getGLID()){
                            jkInfo=jk;
                            jkInfo.setGLID(info.getGLID());
                            ZAJKDao.create(jkInfo);
                        }else {
                            ZAJKDao.update(jk);
                        }
                    }
                    /**消防信息**/
                    if (xf!=null){
                        if (null == xf.getGLID()){
                            xfInfo=xf;
                            xfInfo.setGLID(info.getGLID());
                            ZAXFDao.create(xfInfo);
                        }else {
                            ZAXFDao.update(xf);
                        }
                    }
                    flag=true;
                } else {
                    flag=false;
                }
        } catch (SQLException e) {
            e.printStackTrace();
            flag=false;
        }
        return flag;
    }



    public void setPreRetnData(ZAJCModle jcInfo, TextView tv_zb) {
        tv_zb.setText("(" + jcInfo.getWD() + "," + jcInfo.getJD() + ")");
        View.setLB(jcInfo.getLB());
        View.setCJFL(jcInfo.getCJFL());
        View.setMC(jcInfo.getMC());
        View.setDZ(jcInfo.getDZ());
        View.setMPH(jcInfo.getMPH());
        View.setLDBM(jcInfo.getLDBM());
        View.setIV_MPHQJT(jcInfo.getIV_MPHQJT());
        View.setIV_DMQJT(jcInfo.getIV_DMQJT());
        View.setJYZXM(jcInfo.getJYZXM());
        View.setJYZDH(jcInfo.getJYZDH());
        View.setFWDH(jcInfo.getFWDH());
        View.setIV_YYZZ(jcInfo.getIV_YYZZ());
        View.setIV_QT(jcInfo.getIV_QT());
        View.setIV_TZHY(jcInfo.getIV_TZHY());
        View.setIV_XFYS(jcInfo.getIV_XFYS());
        View.setIV_HBPW(jcInfo.getIV_HBPW());
        View.setZAFZR(jcInfo.getZAFZR());
        View.setZAFZRDH(jcInfo.getZAFZRDH());
        View.setSSPCS(jcInfo.getSSPCS());
        View.setSSJWS(jcInfo.getSSJWS());
        View.setJD(jcInfo.getJD());
        View.setWD(jcInfo.getWD());
        View.setSFCJJK(jcInfo.getSFCJJK());
        View.setSFCJXF(jcInfo.getSFCJXF());
        View.setLOCTYPE(jcInfo.getLOCTYPE());
        View.setBZ(jcInfo.getBZ());
    }

    public ZAJCModle uploadInfo(String lb, String cjfl, String mc, String dz,
                                String mph, String ldbm, String iv_mphqjt, String iv_dmqjt,
                                String jyzxm, String jyzdh, String fwdh, String iv_yyzz, String zafzr,
                                String sspcs, String ssjws, Double jd, Double wd, String zafzrdh, int sfcjjk, int sfcjxf, String loctype, String bz,
                                String iv_qt, String iv_hbpw, String iv_xfys, String iv_tzhy) {

       info.setLB(lb);
        info.setCJFL(cjfl);
        info.setMC(mc);
        info.setDZ(dz);
        info.setMPH(mph);
        info.setLDBM(ldbm);
        info.setIV_MPHQJT(iv_mphqjt);
        info.setIV_DMQJT(iv_dmqjt);
        info.setJYZXM(jyzxm);
        info.setJYZDH(jyzdh);
        info.setFWDH(fwdh);
        info.setIV_YYZZ(iv_yyzz);
        info.setIV_QT(iv_qt);
        info.setIV_HBPW(iv_hbpw);
        info.setIV_XFYS(iv_xfys);
        info.setIV_TZHY(iv_tzhy);
        info.setZAFZR(zafzr);
        info.setZAFZRDH(zafzrdh);
        info.setSSPCS(sspcs);
        info.setSSJWS(ssjws);
        info.setJD(jd);
        info.setWD(wd);
        info.setSFCJJK(sfcjjk);
        info.setSFCJXF(sfcjxf);
        info.setLOCTYPE(loctype);
        info.setBZ(bz);
        info.setCJSJ(new BaseDataUtils().getNowData());
        return info;
    }
}
