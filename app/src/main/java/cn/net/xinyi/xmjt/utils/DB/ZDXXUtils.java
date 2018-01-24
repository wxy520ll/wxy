package cn.net.xinyi.xmjt.utils.DB;

import android.content.Context;

import com.j256.ormlite.dao.Dao;

import java.sql.SQLException;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import cn.net.xinyi.xmjt.model.Zdxx;
import cn.net.xinyi.xmjt.utils.GeneralUtils;

/**
 * Created by hao.zhou on 2015/12/28.
 */
public class ZDXXUtils {
    private Context context;

    public ZDXXUtils(Context context) {
        super();
        this.context = context;
    }



    //根据字典类别获取字典值
    public Map<String, String> getZdlbToZdz(String ZDLB) {
        Map<String,String> maps=new LinkedHashMap<>();
        try {
            Dao<Zdxx, Integer> ZdxxDao = DBHelperNew.getInstance(context).getZdxxDao();
            List<Zdxx >  s= ZdxxDao.queryBuilder().orderBy("XH",true).where().eq("ZDLB",ZDLB).query();
            for (Zdxx z:s){
                maps.put(z.getZDBM(),z.getZDZ());
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return maps;
    }

    //根据字典类别/父字典编码 获取字典值
    public Map<String, String> getZdlbAndFzdbmToZdz(String ZDLB, String FZDBM) {
        Map<String,String> maps=new LinkedHashMap<>();
        try {
            Dao<Zdxx, Integer> ZdxxDao = DBHelperNew.getInstance(context).getZdxxDao();
            List<Zdxx >  s= ZdxxDao.queryBuilder().orderBy("XH",true).where().eq("ZDLB",ZDLB).and().eq("FZDBM",FZDBM).query();
            for (Zdxx z:s){
                maps.put(z.getZDBM(),z.getZDZ());
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return maps;
    }

    //根据字典编码 字典类别 获取字典值集合
    public Map<String,String> getZdlbAndZdbmToZdzs(String ZDLB,String ZDBM) {
        Map<String,String> maps=new LinkedHashMap<>();
        try {
            Dao<Zdxx, Integer> ZdxxDao = DBHelperNew.getInstance(context).getZdxxDao();
            List<Zdxx >  s= ZdxxDao.queryBuilder().where().eq("ZDBM",ZDBM).and().eq("ZDLB",ZDLB).query();
            for (Zdxx z:s){
                maps.put(z.getZDBM(),z.getZDZ());
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return maps;
    }


    //根据字典值获取字典编码
    public String getZdlbAndZdzToZdbm(String ZDLB,String ZDZ) {
        if (null==ZDLB||null==ZDZ){
            return null;
        }
        String strings=null;
        if (ZDLB.equals(GeneralUtils.ZZJG_JWS)&&ZDZ.equals("其他")){
            strings="11";
        }else {
            List<Zdxx >  s=null;
            try {
                Dao<Zdxx, Integer> ZdxxDao = DBHelperNew.getInstance(context).getZdxxDao();
                s= ZdxxDao.queryBuilder().where().eq("ZDZ",ZDZ).and().eq("ZDLB",ZDLB).query();
                return  s.size()==0?"null":s.get(0).getZDBM();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return strings;
    }


    //根据字典值 字典类别 获取字典编码
    public String getZdlbAndZdbmToZdz(String ZDLB,String ZDBM) {
        if (null==ZDLB||null==ZDBM){
            return null;
        }
        List<Zdxx >  s=null;
        try {
            Dao<Zdxx, Integer> ZdxxDao = DBHelperNew.getInstance(context).getZdxxDao();
            s= ZdxxDao.queryBuilder().where().eq("ZDBM",ZDBM).and().eq("ZDLB",ZDLB).query();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return  s.size()==0?"null":s.get(0).getZDZ();
    }

    //根据字典值  父字典编码 字典类别 获取字典编码
    public String getZdlbAndFzdbmAndZdzToZdbm(String ZDLB,String FZDBM,String ZDZ) {
        if (null==ZDLB||null==ZDZ||null==FZDBM){
            return null;
        }

        List<Zdxx >  s=null;
        try {
            Dao<Zdxx, Integer> ZdxxDao = DBHelperNew.getInstance(context).getZdxxDao();
            s= ZdxxDao.queryBuilder().where().eq("ZDZ",ZDZ).and().eq("FZDBM",FZDBM).and().eq("ZDLB",ZDLB).query();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return s.size()==0?"null":s.get(0).getZDBM();
    }


    //根据字典类别 获取字典编码
    public String getZdlToZdz(String ZDLB) {
        List<Zdxx >  s=null;
        try {
            Dao<Zdxx, Integer> ZdxxDao = DBHelperNew.getInstance(context).getZdxxDao();
            s= ZdxxDao.queryBuilder().where().eq("ZDLB",ZDLB).query();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return  s.size()==0?"null":s.get(0).getZDZ();
    }




    //根据map的值获取key
    public String getMapKey(Map<String, String> map, String s) {
        String keyValus=null;
        for(String key:map.keySet()){
            if (map.get(key).equals(s)) {
                keyValus=key;
                break;
            }
        }
        return keyValus;
    }

}
