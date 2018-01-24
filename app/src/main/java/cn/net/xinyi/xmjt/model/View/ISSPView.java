package cn.net.xinyi.xmjt.model.View;

/**
 * Created by hao.zhou on 2016/3/14.
 */
public interface ISSPView {

     int getId();

     String getDESCRIPTION();

     Double getJD();

     Double getWD();

     String getDZ();

     String getZP1();

     String getZP2();

     String getZP3();

     String getLOCTYPE();

     void setDESCRIPTION(String DESCRIPTION);

     void setJD(Double JD);

     void setWD(Double WD);

     void setDZ(String DZ);

     void setZP1(String ZP1);

     void setZP2(String ZP2);

     void setZP3(String ZP3);

     void setLOCTYPE(String LOCTYPE);

     void setSCSJ(String SCSJ) ;
}