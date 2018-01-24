package cn.net.xinyi.xmjt.model.Presenter;

import android.text.format.DateFormat;

import com.j256.ormlite.dao.Dao;

import java.util.Calendar;
import java.util.Locale;

import cn.net.xinyi.xmjt.model.SSPInfoModle;
import cn.net.xinyi.xmjt.model.View.ISSPView;

/**
 * Created by hao.zhou on 2016/3/14.
 */
public class SSPPresenter {
    private ISSPView sspView;
    private SSPInfoModle sspModle;
    private Dao<SSPInfoModle, Integer> sspDao;
    private boolean flag;

    public SSPPresenter(ISSPView sspView) {
        this.sspView = sspView;
        sspModle =new SSPInfoModle();
    }


    public SSPInfoModle uploadInfo(String description, Double jd, Double wd,
                                   String dz, String zp1, String zp2, String zp3, String loctype) {


        sspModle.setDESCRIPTION(description);
        sspModle.setJD(jd);
        sspModle.setWD(wd);
        sspModle.setDZ(dz);
        sspModle.setZP1(zp1);
        sspModle.setZP2(zp2);
        sspModle.setZP3(zp3);
        sspModle.setLOCTYPE(loctype);
        sspModle.setCJSJ(DateFormat.format("yyyy-MM-dd kk:mm:ss", Calendar.getInstance(Locale.CHINA)).toString());
        return sspModle;
    }
}
