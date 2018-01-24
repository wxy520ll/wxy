package cn.net.xinyi.xmjt.model.Presenter;

import android.app.Activity;
import android.text.format.DateFormat;
import android.widget.TextView;

import com.j256.ormlite.dao.Dao;

import java.sql.SQLException;
import java.util.Calendar;
import java.util.Locale;

import cn.net.xinyi.xmjt.model.PerReturnModle;
import cn.net.xinyi.xmjt.model.View.IPerReturnView;
import cn.net.xinyi.xmjt.utils.DB.DBHelperNew;

/**
 * Created by hao.zhou on 2016/2/25.
 */
public class PreReturnPresenter {
    private IPerReturnView perView;
    private PerReturnModle perModle;
    private Dao<PerReturnModle, Integer> perDao;
    private boolean flag;

    public PreReturnPresenter(IPerReturnView View) {
        this.perView = View;
        perModle=new PerReturnModle();
    }


    /***采集的信息保存至数据库**/
    public boolean saveInfo(Activity aty, boolean ISupload, String name, int id, String sfzh,
                            String zfhdz, Double jd, Double wd,String bz, String lyqzzp, String loctype){
        perModle.setNAME(name);
        perModle.setSFZH(sfzh);
        perModle.setZFHDZ(zfhdz);
        perModle.setJd(jd);
        perModle.setWd(wd);
        perModle.setBZ(bz);
        perModle.setIV_LYQZZP(lyqzzp);
        perModle.setLOCTYPE(loctype);
        perModle.setCJSJ(DateFormat.format("yyyy-MM-dd kk:mm:ss", Calendar.getInstance(Locale.CHINA)).toString());
        try {
            perDao = DBHelperNew.getInstance(aty).getPreReturnDao();
            /**数据库更新**/
            if(ISupload == true ){
                if (perModle != null) {
                    perModle.setId(id);
                    perDao.update(perModle);
                    flag=true;
                } else {
                    flag=false;
                }
                /**数据库创建**/
            }else{
                if (perModle != null) {
                    perDao.create(perModle);
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


    public void setPreRetnData(PerReturnModle perInfos, TextView tv_zb) {
        tv_zb.setText("(" + perInfos.getWd() + "," + perInfos.getJd() + ")");
        perView.setNAME(perInfos.getNAME());
        perView.setSFZH(perInfos.getSFZH());
        perView.setZFHDZ(perInfos.getZFHDZ());
        perView.setJd(perInfos.getJd());
        perView.setWd(perInfos.getWd());
        perView.setBZ(perInfos.getBZ());
        perView.setLYQZZP(perInfos.getIV_LYQZZP());
        perView.setLOCTYPE(perInfos.getLOCTYPE());
    }

    public PerReturnModle uploadInfo(String name, String sfzh,String zfhdz, Double jd, Double wd, String bz,
                                     String lyqzzp, String loctype) {
        perModle.setNAME(name);
        perModle.setSFZH(sfzh);
        perModle.setZFHDZ(zfhdz);
        perModle.setJd(jd);
        perModle.setWd(wd);
        perModle.setBZ(bz);
        perModle.setIV_LYQZZP(lyqzzp);
        perModle.setLOCTYPE(loctype);
        perModle.setCJSJ(DateFormat.format("yyyy-MM-dd kk:mm:ss", Calendar.getInstance(Locale.CHINA)).toString());
        return perModle;
    }
}
