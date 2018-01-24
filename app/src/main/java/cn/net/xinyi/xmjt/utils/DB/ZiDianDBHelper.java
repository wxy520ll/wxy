package cn.net.xinyi.xmjt.utils.DB;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.os.Environment;
import android.widget.Toast;

import java.io.File;

import cn.net.xinyi.xmjt.config.AppConfig;
import cn.net.xinyi.xmjt.config.AppContext;
import cn.net.xinyi.xmjt.utils.BaseUtil;

/**
 * Created by mazhongwang on 15/4/13.
 */
public class ZiDianDBHelper {
    public static final String DATABASE_PATH = Environment.getExternalStorageDirectory().getPath()
            + File.separator + AppContext.APP_ID+File.separator+"db";
    public static final String DATABASE_FILENAME = "zd.db";
    public static final String TABLE_ZDLB_INFO = "ZDLB";
    public static final String TABLE_ZDXX_INFO = "ZDXX";
    public static Context mContext;

    public ZiDianDBHelper(Context context) {
        mContext = context;
    }

    public static SQLiteDatabase openDatabase() {
        try {
            String databaseFilename = getDatabaseFile();
            if(!(new File(databaseFilename)).exists()){
                initDatabase(mContext);
            }
            // 打开db文件
            SQLiteDatabase database = SQLiteDatabase.openOrCreateDatabase(
                    databaseFilename, null);
            return database;
        } catch (Exception e) {
        }
        return null;
    }

    public static void initDatabase(Context mContext) {
        String databaseFilename = getDatabaseFile();
        try {
            BaseUtil.copyFileFromAssets(mContext,DATABASE_FILENAME,databaseFilename);
            File dbFile = new File(databaseFilename);
            if(!dbFile.exists()){
                AppContext.instance().setProperty(AppConfig.ZD_UPDATE_TIME,"2015-10-14");
                Toast.makeText(mContext,"初始化字典信息成功",Toast.LENGTH_SHORT);
            }else{
                AppContext.instance().setProperty(AppConfig.ZD_UPDATE_TIME,"2015-10-14");
                Toast.makeText(mContext,"本地字典信息已经初始化",Toast.LENGTH_SHORT);
            }
        } catch (Exception e) {
            Toast.makeText(mContext,"初始化字典信息失败",Toast.LENGTH_SHORT);
        }
    }

    public static String getDatabaseFile() {
        // 获得db文件的绝对路径
        String databaseFilename = DATABASE_PATH + "/" + DATABASE_FILENAME;
        File dir = new File(DATABASE_PATH);

        if (!dir.exists())
            dir.mkdirs();
        return databaseFilename;
    }

//
//    //获得字典信息数据
//    public  List<Zdxx> getData(){
//        String sql = "select * from ZDXX  ORDER BY id ASC";
//        List<Zdxx> list = new ArrayList<Zdxx>();
//        try {
//            SQLiteDatabase  db = openDatabase();
//            Cursor cs = db.rawQuery(sql, null);
//            while (cs.moveToNext()){
//                Zdxx zdxx=new Zdxx();
//                zdxx.setID(cs.getInt(cs.getColumnIndex("ID")));
//                zdxx.setBZ(cs.getString(cs.getColumnIndex("BZ")));
//                zdxx.setXH(cs.getString(cs.getColumnIndex("XH")));
//                zdxx.setZDZ(cs.getString(cs.getColumnIndex("ZDZ")));
//                zdxx.setZDBM(cs.getString(cs.getColumnIndex("ZDBM")));
//                zdxx.setFZDBM(cs.getString(cs.getColumnIndex("FZDBM")));
//                zdxx.setPY(cs.getString(cs.getColumnIndex("PY")));
//                zdxx.setZDLB(cs.getString(cs.getColumnIndex("ZDLB")));
//                list.add(zdxx);
//            }
//            cs.close();
//            db.close();
//        }catch (Exception e){
//            e.printStackTrace();
//        }
//        return list;
//    }
//
//
//
//    //根据字典列表 获得字典信息数据
//    public List<Zdxx> getZDLBData(String ZDLB){
//        String sql = "select * from ZDXX where ZDLB="+ZDLB+" ORDER BY id ASC";
//        List<Zdxx> list = new ArrayList<Zdxx>();
//        try {
//            SQLiteDatabase  db = openDatabase();
//            Cursor cs = db.rawQuery(sql, null);
//            while (cs.moveToNext()){
//                Zdxx zdxx=new Zdxx();
//                zdxx.setID(cs.getInt(cs.getColumnIndex("ID")));
//                zdxx.setBZ(cs.getString(cs.getColumnIndex("BZ")));
//                zdxx.setXH(cs.getString(cs.getColumnIndex("XH")));
//                zdxx.setZDZ(cs.getString(cs.getColumnIndex("ZDZ")));
//                zdxx.setZDBM(cs.getString(cs.getColumnIndex("ZDBM")));
//                zdxx.setFZDBM(cs.getString(cs.getColumnIndex("FZDBM")));
//                zdxx.setPY(cs.getString(cs.getColumnIndex("PY")));
//                zdxx.setZDLB(cs.getString(cs.getColumnIndex("ZDLB")));
//                list.add(zdxx);
//            }
//            cs.close();
//            db.close();
//        }catch (Exception e){
//            e.printStackTrace();
//        }
//        return list;
//    }
//
//    //插入信息表
//    public int insertZDXXInfo(JSONArray jsonArray){
//        SQLiteDatabase  db = openDatabase();
//        db.beginTransaction();
//        for(int i=0;i<jsonArray.size();i++){
//            JSONObject jsonObject = (JSONObject)jsonArray.get(i);
//            ContentValues cv = new ContentValues();
//
//            cv.put("BZ",jsonObject.getString("BZ"));
//            cv.put("ID",jsonObject.getString("ID"));
//            cv.put("XH", jsonObject.getString("XH"));
//            cv.put("ZDZ", jsonObject.getString("ZDZ"));
//            cv.put("ZDBM", jsonObject.getString("ZDBM"));
//            cv.put("FZDBM", jsonObject.getString("FZDBM"));
//            cv.put("PY", jsonObject.getString("PY"));
//            cv.put("ZDLB", jsonObject.getString("ZDLB"));
//            cv.put("writetime", jsonObject.getString("writetime"));
//            db.insert(TABLE_ZDXX_INFO,"",cv);
//        }
//        db.setTransactionSuccessful();
//        db.endTransaction();
//        db.close();
//        return  jsonArray.size();
//    }
//
//
//
//    //插入信息列表
//    public int insertZDLBInfo(JSONArray jsonArray){
//        SQLiteDatabase  db = openDatabase();
//        db.beginTransaction();
//        for(int i=0;i<jsonArray.size();i++){
//            JSONObject jsonObject = (JSONObject)jsonArray.get(i);
//            ContentValues cv = new ContentValues();
//            cv.put("ZDJB",jsonObject.getString("ZDJB"));
//            cv.put("LBBM",jsonObject.getString("LBBM"));
//            cv.put("LBMC",jsonObject.getString("LBMC"));
//            cv.put("PY",jsonObject.getString("PY"));
//            cv.put("BZ",jsonObject.getString("BZ"));
//            cv.put("ID",jsonObject.getString("ID"));
//            cv.put("writetime",jsonObject.getString("writetime"));
//            db.insert(TABLE_ZDLB_INFO,"",cv);
//        }
//        db.setTransactionSuccessful();
//        db.endTransaction();
//        db.close();
//        return  jsonArray.size();
//    }
//
//
//    /**
//     * 删除记录
//     *
//     * @return
//     */
//    public boolean delZDXXInfo(int ID) {
//        SQLiteDatabase  db = openDatabase();
//        String []args = new String[]{String.valueOf(ID)};
//        return db.delete(TABLE_ZDXX_INFO, "ID=?"
//                , args) > 0;
//    }
//
//
//
//    //获得字典信息数据
//    public List<Zdlb> getZDLBData(){
//        String sql = "select * from ZDLB ORDER BY id ASC";
//        List<Zdlb> list = new ArrayList<Zdlb>();
//        try {
//            SQLiteDatabase  db = openDatabase();
//            Cursor cs = db.rawQuery(sql, null);
//            while (cs.moveToNext()){
//                Zdlb zdlb=new Zdlb();
//
//                zdlb.setID(cs.getString(cs.getColumnIndex("ID")));
//                zdlb.setBZ(cs.getString(cs.getColumnIndex("BZ")));
//                zdlb.setZDJB(cs.getString(cs.getColumnIndex("ZDJB")));
//                zdlb.setLBBM(cs.getString(cs.getColumnIndex("LBBM")));
//                zdlb.setLBMC(cs.getString(cs.getColumnIndex("LBMC")));
//                zdlb.setLY(cs.getString(cs.getColumnIndex("LY")));
//                zdlb.setPY(cs.getString(cs.getColumnIndex("PY")));
//                list.add(zdlb);
//            }
//            cs.close();
//            db.close();
//        }catch (Exception e){
//            e.printStackTrace();
//        }
//        return list;
//    }
//
//
//
//
//    //字典列表数量
//    public int getZDLBListCount(){
//        int count = 0;
//        String sql = "select  count(*) from ZDLB ";
//        try {
//            SQLiteDatabase  db = openDatabase();
//            Cursor cursor = db.rawQuery(sql, null);
//            cursor.moveToFirst();
//            String total = cursor.getString(0);
//            count = Integer.parseInt(total);
//            cursor.close();
//            db.close();
//        }catch (Exception e){
//            e.printStackTrace();
//        }
//        return count;
//    }
//
//    /**
//     * 删除记录
//     *
//     * @return
//     */
//    public boolean delZDLBInfo(String ID) {
//        SQLiteDatabase  db = openDatabase();
//        String []args = new String[]{String.valueOf(ID)};
//        return db.delete(TABLE_ZDLB_INFO, "ID=?"
//                , args) > 0;
//    }
//
//
//
//
//    /**
//     * @param FZDBM  父字典编码
//     * @param ZDZ  字典值
//     * 根据父字典编码  字典值  查询 字典所对应的 字典编码
//     * ***/
//    public static String getFzdbmAndZdzToZdbm(String ZDLB, String FZDBM, String ZDZ){
//        String sql = "select * from ZDXX WHERE  ZDLB='"+ZDLB+"' AND FZDBM='"+FZDBM+"'AND ZDZ='"+ZDZ+"'";
//        String zdbm=null;
//        try {
//            SQLiteDatabase  db = openDatabase();
//            Cursor cs = db.rawQuery(sql, null);
//            while (cs.moveToNext()){
//                zdbm=cs.getString(cs.getColumnIndex("ZDBM"));
//            }
//            cs.close();
//            db.close();
//        }catch (Exception e){
//            e.printStackTrace();
//        }
//        return zdbm;
//    }
//    /**
//     * 根据字典类别获取派出所
//     * ***/
//    public static List<String> getZdlbToZdz(String ZDLB){
//        String sql = "select * from ZDXX WHERE  ZDLB='"+ZDLB+"'";
//        List<String> list = new ArrayList<String>();
//        String zdbm=null;
//        try {
//            SQLiteDatabase  db = openDatabase();
//            Cursor cs = db.rawQuery(sql, null);
//            while (cs.moveToNext()){
//                list.add(cs.getString(cs.getColumnIndex("ZDZ")));
//            }
//            cs.close();
//            db.close();
//        }catch (Exception e){
//            e.printStackTrace();
//        }
//        return list;
//    }
//
//
//    /**
//     * @param ZDLB  字典类别
//     * @param ZDZ  字典值
//     * 根据  字典值  查询 字典所对应的 字典编码
//     * ***/
//    public static String getZdzToZdbm(String ZDLB, String ZDZ){
//        String sql = "select * from ZDXX WHERE  ZDLB='"+ZDLB+"' AND ZDZ='"+ZDZ+"'";
//        String zdbm=null;
//        try {
//            SQLiteDatabase  db = openDatabase();
//            Cursor cs = db.rawQuery(sql, null);
//            while (cs.moveToNext()){
//                zdbm=cs.getString(cs.getColumnIndex("ZDBM"));
//            }
//            cs.close();
//            db.close();
//        }catch (Exception e){
//            e.printStackTrace();
//        }
//        return zdbm;
//    }
//    /**
//     * 根据字典编码获取该字典值
//     * ***/
//    public static String getNumToZdz(String ZDLB,String ZDBM){
//        String sql = "select * from ZDXX WHERE  ZDLB='"+ZDLB+"'AND ZDBM='"+ZDBM+"'";
//        String ZDZ=null;
//        try {
//            SQLiteDatabase  db = openDatabase();
//            Cursor cs = db.rawQuery(sql, null);
//            while (cs.moveToNext()){
//                ZDZ=cs.getString(cs.getColumnIndex("ZDZ"));
//            }
//            cs.close();
//            db.close();
//        }catch (Exception e){
//            e.printStackTrace();
//        }
//        return ZDZ;
//    }
//
//    /**
//     * 根据派出所编码获得下属警务室
//     * ***/
//    public static List<String> getJWSMC(String ZDLB,String FZDBM){
//        String sql = "select * from ZDXX WHERE  ZDLB='"+ZDLB+"'AND FZDBM='"+FZDBM+"'";
//        List<String> list = new ArrayList<String>();
//        try {
//            SQLiteDatabase  db = openDatabase();
//            Cursor cs = db.rawQuery(sql, null);
//            while (cs.moveToNext()){
//                list.add(cs.getString(cs.getColumnIndex("ZDZ")));
//            }
//            cs.close();
//            db.close();
//        }catch (Exception e){
//            e.printStackTrace();
//        }
//        return list;
//    }


}
