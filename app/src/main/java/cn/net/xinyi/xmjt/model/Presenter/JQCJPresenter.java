package cn.net.xinyi.xmjt.model.Presenter;

import android.app.Activity;
import android.widget.TextView;

import com.j256.ormlite.dao.Dao;

import java.sql.SQLException;
import java.util.List;

import cn.net.xinyi.xmjt.model.JQInfoModle;
import cn.net.xinyi.xmjt.model.View.IJQCJView;
import cn.net.xinyi.xmjt.utils.BaseDataUtils;
import cn.net.xinyi.xmjt.utils.DB.DBHelperNew;

/**
 * Created by hao.zhou on 2016/4/1.
 */
public class JQCJPresenter {
    private IJQCJView JQView;
    private JQInfoModle JQModle;
    private Dao<JQInfoModle, Integer> JQDao;
    private boolean flag;

    public JQCJPresenter(IJQCJView JQView) {
        this.JQView = JQView;
        JQModle =new JQInfoModle();
    }


    public boolean saveInfo(Activity aty, boolean ISupload, int id, String jqbh,int sfafdd, int sfbjdd,int sfsdaj,
                            String bjrxb, String bjrxm, String jqlb, String bjdd, String bjrjzd,
                            String bjrjg, String bjrnl, String bjrxl, String lxdh, String lxdh1,
                            String lxdh2,String jqdz, Double jd, Double wd, String bz, String iv_zp1,
                            String iv_zp2,String iv_zp3, String loctype) {
        JQModle.setJQBH(jqbh);
        JQModle.setSFAFDD(sfafdd);
        JQModle.setSFBJDD(sfbjdd);
        JQModle.setSFSDAJ(sfsdaj);
        JQModle.setBJRXB(bjrxb);
        JQModle.setBJRXM(bjrxm);
        JQModle.setJQLB(jqlb);
        JQModle.setBJDD(bjdd);
        JQModle.setBJRJZD(bjrjzd);
        JQModle.setBJRJG(bjrjg);
        JQModle.setBJRNL(bjrnl);
        JQModle.setBJRXL(bjrxl);
        JQModle.setLXDH(lxdh);
        JQModle.setLXDH1(lxdh1);
        JQModle.setLXDH2(lxdh2);
        JQModle.setDZ(jqdz);
        JQModle.setJD(jd);
        JQModle.setWD(wd);
        JQModle.setBZ(bz);
        JQModle.setIV_ZP1(iv_zp1);
        JQModle.setIV_ZP2(iv_zp2);
        JQModle.setIV_ZP3(iv_zp3);
        JQModle.setLOCTYPE(loctype);
        JQModle.setCJSJ(new BaseDataUtils().getNowData());
        try {
            JQDao = DBHelperNew.getInstance(aty).getJQCJDao();
            /**数据库更新**/
            if(ISupload == true ){
                if (JQModle != null) {
                    JQModle.setId(id);
                    JQDao.update(JQModle);
                    flag=true;
                } else {
                    flag=false;
                }
                /**数据库创建**/
            }else{
                if (JQModle != null) {
                    JQDao.create(JQModle);
                    flag=true;
                } else {
                    flag=false;
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
            flag=false;
        }
        return flag;
    }

    public boolean updateDBJQCJ(Activity aty,int id, String jqbh,int sfafdd, int sfbjdd,int sfsdaj,
                                String bjrxb, String bjrxm, String jqlb, String bjdd, String bjrjzd,
                                String bjrjg, String bjrnl, String bjrxl, String lxdh, String lxdh1,
                                String lxdh2,String jqdz, Double jd, Double wd, String bz, String iv_zp1,
                                String iv_zp2,String iv_zp3, String loctype,String cjsj) {
        JQModle.setJQBH(jqbh);
        JQModle.setSFAFDD(sfafdd);
        JQModle.setSFBJDD(sfbjdd);
        JQModle.setSFSDAJ(sfsdaj);
        JQModle.setBJRXB(bjrxb);
        JQModle.setBJRXM(bjrxm);
        JQModle.setJQLB(jqlb);
        JQModle.setBJDD(bjdd);
        JQModle.setBJRJZD(bjrjzd);
        JQModle.setBJRJG(bjrjg);
        JQModle.setBJRNL(bjrnl);
        JQModle.setBJRXL(bjrxl);
        JQModle.setLXDH(lxdh);
        JQModle.setLXDH1(lxdh1);
        JQModle.setLXDH2(lxdh2);
        JQModle.setDZ(jqdz);
        JQModle.setJD(jd);
        JQModle.setWD(wd);
        JQModle.setBZ(bz);
        JQModle.setIV_ZP1(iv_zp1);
        JQModle.setIV_ZP2(iv_zp2);
        JQModle.setIV_ZP3(iv_zp3);
        JQModle.setLOCTYPE(loctype);
        JQModle.setCJSJ(cjsj);
        try {
            JQDao = DBHelperNew.getInstance(aty).getJQCJDao();
            if (JQModle != null) {
                JQModle.setId(id);
                JQDao.update(JQModle);
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

    public void setJQCJData(JQInfoModle jqInfo, TextView tv_zb) {
        tv_zb.setText("(" + jqInfo.getWD() + "," + jqInfo.getJD() + ")");
        JQView.setJQBH(jqInfo.getJQBH());
        JQView.setSFAFDD(jqInfo.getSFAFDD());
        JQView.setSFBJDD(jqInfo.getSFBJDD());
        JQView.setSFSDAJ(jqInfo.getSFSDAJ());
        JQView.setBJRXB(jqInfo.getBJRXB());
        JQView.setBJRXM(jqInfo.getBJRXM());
        JQView.setJQLB(jqInfo.getJQLB());
        JQView.setBJDD(jqInfo.getBJDD());
        JQView.setBJRJZD(jqInfo.getBJRJZD());
        JQView.setBJRJG(jqInfo.getBJRJG());
        JQView.setBJRNL(jqInfo.getBJRNL());
        JQView.setBJRXL(jqInfo.getBJRXL());
        JQView.setLXDH(jqInfo.getLXDH());
        JQView.setLXDH1(jqInfo.getLXDH1());
        JQView.setLXDH2(jqInfo.getLXDH2());
        JQView.setJQDZ(jqInfo.getDZ());
        JQView.setJD(jqInfo.getJD());
        JQView.setWD(jqInfo.getWD());
        JQView.setBZ(jqInfo.getBZ());
        JQView.setIV_ZP1(jqInfo.getIV_ZP1());
        JQView.setIV_ZP2(jqInfo.getIV_ZP2());
        JQView.setIV_ZP3(jqInfo.getIV_ZP3());
        JQView.setLOCTYPE(jqInfo.getLOCTYPE());
    }

    public JQInfoModle uploadInfo(String jqbh, int sfafdd, int sfbjdd, int sfsdaj,String bjrxb, String bjrxm, String jqlb, String bjdd, String bjrjzd, String bjrjg, String bjrnl, String bjrxl, String lxdh,
                                  String lxdh1, String lxdh2, String jqdz, Double jd, Double wd,
                                  String bz, String iv_zp1, String iv_zp2, String iv_zp3, String loctype) {
        JQModle.setJQBH(jqbh);
        JQModle.setSFAFDD(sfafdd);
        JQModle.setSFBJDD(sfbjdd);
        JQModle.setSFSDAJ(sfsdaj);
        JQModle.setBJRXB(bjrxb);
        JQModle.setBJRXM(bjrxm);
        JQModle.setJQLB(jqlb);
        JQModle.setBJDD(bjdd);
        JQModle.setBJRJZD(bjrjzd);
        JQModle.setBJRJG(bjrjg);
        JQModle.setBJRNL(bjrnl);
        JQModle.setBJRXL(bjrxl);
        JQModle.setLXDH(lxdh);
        JQModle.setLXDH1(lxdh1);
        JQModle.setLXDH2(lxdh2);
        JQModle.setDZ(jqdz);
        JQModle.setJD(jd);
        JQModle.setWD(wd);
        JQModle.setBZ(bz);
        JQModle.setIV_ZP1(iv_zp1);
        JQModle.setIV_ZP2(iv_zp2);
        JQModle.setIV_ZP3(iv_zp3);
        JQModle.setLOCTYPE(loctype);
        JQModle.setCJSJ(new BaseDataUtils().getNowData());
        return JQModle;
    }


    public boolean getLocalData(Activity aty,String JQBH) {
        try {
            Dao<JQInfoModle, Integer> JQCJDao = DBHelperNew.getInstance(aty).getJQCJDao();
            /**查询数据所有数据**/
            List<JQInfoModle> JQCJs = JQCJDao.queryForAll();
            if (JQCJs.size()>0){
                for (JQInfoModle j:JQCJs){
                    if (JQBH.equals(j.getJQBH())){
                        return true;
                    }
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }
}
