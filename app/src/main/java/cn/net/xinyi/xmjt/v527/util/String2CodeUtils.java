package cn.net.xinyi.xmjt.v527.util;

import java.util.List;

import cn.net.xinyi.xmjt.v527.presentation.gzrz.model.GzrzTypeModel;

/**
 * Created by jiajun.wang on 2017/12/30.
 */

public class String2CodeUtils {

    public static String[] getItems(List<GzrzTypeModel> gzrzTypeModelList){
        if (gzrzTypeModelList==null||gzrzTypeModelList.size()==0)
            return null;
        String []items=new String[gzrzTypeModelList.size()];
        for (int i=0;i<gzrzTypeModelList.size();i++){
            GzrzTypeModel g=gzrzTypeModelList.get(i);
            items[i]=g.getNAME();
        }
        return items;
    }

    public static String getCodeByName(List<GzrzTypeModel> gzrzTypeModelList,String name){
        if(gzrzTypeModelList==null)
            return null;
        for (GzrzTypeModel gz:gzrzTypeModelList
             ) {
            if (gz.getNAME().equals(name)){
                return gz.getCODE();
            }
        }
        return "";
    }
}
