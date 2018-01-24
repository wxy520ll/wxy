package cn.net.xinyi.xmjt.utils.DB;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Environment;
import android.widget.Toast;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

import java.io.File;

import cn.net.xinyi.xmjt.config.AppConfig;
import cn.net.xinyi.xmjt.config.AppContext;
import cn.net.xinyi.xmjt.utils.BaseUtil;
import cn.net.xinyi.xmjt.utils.Encryptioner;

/**
 * Created by mazhongwang on 15/4/13.
 */
public class BlackPlateDBHelper {
    public static final String DATABASE_PATH = Environment.getExternalStorageDirectory().getPath()
            + File.separator + AppContext.APP_ID+File.separator+"db";
    public static final String DATABASE_FILENAME = "black_plate.db";
    public static final String DATABASE_ZIPFILENAME = "black_plate.zip";
    public static final String TABLE_BLACK_PLATE = "blackplate_info";
    public Context mContext;

    public BlackPlateDBHelper(Context context) {
        this.mContext = context;
    }

    public SQLiteDatabase openDatabase() {
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
        String databaseZipFilename = getDatabaseZipFile();
        String databaseFilename = getDatabaseFile();
        File zipFile = new File(databaseZipFilename);

        try {
            if (!zipFile.exists()) {
                //先解压文件
//                InputStream is = mContext.getResources().openRawResource(
//                        R.raw.black_plate);
//                FileOutputStream fos = new FileOutputStream(databaseZipFilename);
//                byte[] buffer = new byte[8192];
//                int count = 0;
//                while ((count = is.read(buffer)) > 0) {
//                    fos.write(buffer, 0, count);
//                }
//                fos.close();
//                is.close();
                Toast.makeText(mContext,"正在初始化本地车牌比对库，请稍候",Toast.LENGTH_LONG).show();
                BaseUtil.copyFileFromAssets(mContext,DATABASE_ZIPFILENAME,databaseZipFilename);
            }

            File dbFile = new File(databaseFilename);
            if(!dbFile.exists()){
                BaseUtil.upZipFile(zipFile,DATABASE_PATH);
                AppContext.instance().setProperty(AppConfig.BLACKPLATE_UPDATE_TIME,"2015-04-02");
                //BaseUtil.showDialog("初始化车牌比对库成功", mContext);
                Toast.makeText(mContext,"初始化车牌比对库成功",Toast.LENGTH_SHORT);
            }else{
                AppContext.instance().setProperty(AppConfig.BLACKPLATE_UPDATE_TIME,"2015-04-02");
                //BaseUtil.showDialog("本地车牌比对库已经初始化",mContext);
                Toast.makeText(mContext,"本地车牌比对库已经初始化",Toast.LENGTH_SHORT);
            }
        } catch (Exception e) {
            //BaseUtil.showDialog("初始化车牌比对库失败",mContext);
            Toast.makeText(mContext,"初始化车牌比对库失败",Toast.LENGTH_SHORT);
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

    public static String getDatabaseZipFile() {
        // 获得db文件的绝对路径
        String databaseFilename = DATABASE_PATH + "/" + DATABASE_ZIPFILENAME;
        File dir = new File(DATABASE_PATH);
        if (!dir.exists())
            dir.mkdirs();
        return databaseFilename;
    }

    public int insertBlackPlate(JSONArray jsonArray){
        SQLiteDatabase  db = openDatabase();
        db.beginTransaction();
        for(int i=0;i<jsonArray.size();i++){
            JSONObject jsonObject = (JSONObject)jsonArray.get(i);
            ContentValues cv = new ContentValues();
            cv.put("carno_md5",jsonObject.getString("carno_md5"));
            cv.put("writetime",jsonObject.getString("writetime"));
            db.insert(TABLE_BLACK_PLATE,"",cv);
        }
        db.setTransactionSuccessful();
        db.endTransaction();
        db.close();
        return  jsonArray.size();
    }

    public int checkBlatePlate(String lpn){
        String md5_lpn = Encryptioner.md5Encrypt(lpn);
        int count = 0;
        String sql = "select  * from blackplate_info where carno_md5 = '"+ md5_lpn + "'";
        try {
            SQLiteDatabase  db = openDatabase();
            Cursor cursor = db.rawQuery(sql, null);
            count = cursor.getCount();
            cursor.close();
            db.close();
        }catch (Exception e){
            e.printStackTrace();
        }
        return count;
    }

    public int getBlackListCount(){
        int count = 0;
        String sql = "select  count(*) from blackplate_info ";
        try {
            SQLiteDatabase  db = openDatabase();
            Cursor cursor = db.rawQuery(sql, null);
            cursor.moveToFirst();
            String total = cursor.getString(0);
            count = Integer.parseInt(total);
            cursor.close();
            db.close();
        }catch (Exception e){
            e.printStackTrace();
        }
        return count;
    }
}
