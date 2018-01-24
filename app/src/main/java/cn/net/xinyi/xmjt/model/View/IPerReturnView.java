package cn.net.xinyi.xmjt.model.View;

/**
 * Created by hao.zhou on 2016/2/25.
 */
public interface IPerReturnView {

    int getId();

    String getNAME() ;

    String getSFZH();

    String getZFHDZ() ;

    Double getJd() ;

    Double getWd() ;

    String getBZ();

    String getLYQZZP();

    String getLOCTYPE();

    void setNAME(String NAME);

    void setSFZH(String SFZH) ;

    void setZFHDZ(String ZFHDZ);

    void setJd(Double jd);

    void setWd(Double wd) ;

    void setBZ(String BZ) ;

    void setLYQZZP(String LYQZZP) ;

    void setLOCTYPE(String LOCTYPE);


}