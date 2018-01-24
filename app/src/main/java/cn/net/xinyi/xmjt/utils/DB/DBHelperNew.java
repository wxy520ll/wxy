package cn.net.xinyi.xmjt.utils.DB;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

import com.j256.ormlite.android.apptools.OrmLiteSqliteOpenHelper;
import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.support.ConnectionSource;
import com.j256.ormlite.table.TableUtils;

import java.sql.SQLException;

import cn.net.xinyi.xmjt.model.JQInfoModle;
import cn.net.xinyi.xmjt.model.PerReturnModle;
import cn.net.xinyi.xmjt.model.SSPInfoModle;
import cn.net.xinyi.xmjt.model.ZAJCCYZModle;
import cn.net.xinyi.xmjt.model.ZAJCJKModle;
import cn.net.xinyi.xmjt.model.ZAJCModle;
import cn.net.xinyi.xmjt.model.ZAJCXFModle;
import cn.net.xinyi.xmjt.model.Zdxx;

/**
 * Created by hao.zhou on 2016/1/14.
 */
public class DBHelperNew extends OrmLiteSqliteOpenHelper {
    /**
     *
     * **/
    private static final String DATABASE_NAME = "xmjt_new.db";
    private static final int DATABASE_VERSION =16;
    private static DBHelperNew mDbHelper;
    private static Dao<PerReturnModle,Integer> perDao = null;
    private static Dao<ZAJCCYZModle, Integer> ZACYZDao = null;
    private static Dao<ZAJCJKModle, Integer> ZAJKDao = null;
    private static Dao<ZAJCModle, Integer> ZAJCDao = null;
    private static Dao<ZAJCXFModle, Integer> ZAXFDao = null;
    private static Dao<JQInfoModle, Integer> JQCJDao = null;
    private static Dao<Zdxx, Integer> ZdxxDao = null;


    public DBHelperNew(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    public static DBHelperNew getInstance(Context context) {
        if (mDbHelper == null) {
            mDbHelper = new DBHelperNew(context);
        }
        return mDbHelper;
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase, ConnectionSource connectionSource) {
        try {
            /**人员回访采集 **/
            TableUtils.createTableIfNotExists(connectionSource, PerReturnModle.class);
            /** 随手拍采集**/
            TableUtils.createTableIfNotExists(connectionSource, SSPInfoModle.class);
            /** 治安基础从业者**/
            TableUtils.createTableIfNotExists(connectionSource, ZAJCCYZModle.class);
            /** 治安基础监控**/
            TableUtils.createTableIfNotExists(connectionSource, ZAJCJKModle.class);
            /** 治安基础采集**/
            TableUtils.createTableIfNotExists(connectionSource, ZAJCModle.class);
            /** 治安基础消防**/
            TableUtils.createTableIfNotExists(connectionSource, ZAJCXFModle.class);
            /** 警情采集**/
            TableUtils.createTableIfNotExists(connectionSource, JQInfoModle.class);
            /** 字典信息**/
            TableUtils.createTableIfNotExists(connectionSource, Zdxx.class);

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, ConnectionSource connectionSource, int oldVersion, int newVersion) {
        try {
            if (oldVersion<8){
                /** 治安基础从业者**/
                TableUtils.createTableIfNotExists(connectionSource, ZAJCCYZModle.class);
                /** 治安基础监控**/
                TableUtils.createTableIfNotExists(connectionSource, ZAJCJKModle.class);
                /** 治安基础采集**/
                TableUtils.createTableIfNotExists(connectionSource, ZAJCModle.class);
                /** 治安基础消防**/
                TableUtils.createTableIfNotExists(connectionSource, ZAJCXFModle.class);
            }else if (oldVersion<9){
                /** 警情采集**/
                TableUtils.createTableIfNotExists(connectionSource, JQInfoModle.class);
            }else if (oldVersion<12){
                /**增加治安基础信息采集表*/
                ZAJCDao.executeRaw("ALTER TABLE `ZAJCModle` ADD COLUMN IV_QT VARCHAR;");
                ZAJCDao.executeRaw("ALTER TABLE `ZAJCModle` ADD COLUMN IV_HBPW VARCHAR;");
                ZAJCDao.executeRaw("ALTER TABLE `ZAJCModle` ADD COLUMN IV_XFYS VARCHAR;");
                ZAJCDao.executeRaw("ALTER TABLE `ZAJCModle` ADD COLUMN IV_TZHY VARCHAR;");
            }else if (oldVersion<13){
                JQCJDao.executeRaw("ALTER TABLE `JQInfoModle` ADD COLUMN BJDD VARCHAR;");
                JQCJDao.executeRaw("ALTER TABLE `JQInfoModle` ADD COLUMN JQLB VARCHAR;");
                JQCJDao.executeRaw("ALTER TABLE `JQInfoModle` ADD COLUMN BJRXM VARCHAR;");
                JQCJDao.executeRaw("ALTER TABLE `JQInfoModle` ADD COLUMN BJRXB VARCHAR;");
                JQCJDao.executeRaw("ALTER TABLE `JQInfoModle` ADD COLUMN BJRXL VARCHAR;");
                JQCJDao.executeRaw("ALTER TABLE `JQInfoModle` ADD COLUMN BJRNL VARCHAR;");
                JQCJDao.executeRaw("ALTER TABLE `JQInfoModle` ADD COLUMN BJRJG VARCHAR;");
                JQCJDao.executeRaw("ALTER TABLE `JQInfoModle` ADD COLUMN BJRJZD VARCHAR;");
            }else if (oldVersion<14){
                ZAJCDao.executeRaw("ALTER TABLE `ZAJCModle` ADD COLUMN LB VARCHAR;");
            }else if (oldVersion<15){
                JQCJDao.executeRaw("ALTER TABLE `JQInfoModle` ADD COLUMN SFSDAJ INTEGER;");
            }else if (oldVersion<16){
                /** 字典信息**/
                TableUtils.createTableIfNotExists(connectionSource, Zdxx.class);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    /***人员回访采集**/
    public Dao<PerReturnModle,Integer> getPreReturnDao() throws SQLException{
        if(perDao == null){
            perDao = getDao(PerReturnModle.class);
        }
        return perDao;
    }


    /***治安基础从业者**/
    public Dao<ZAJCCYZModle, Integer> getZACYZDao() throws SQLException{
        if(ZACYZDao == null){
            ZACYZDao = getDao(ZAJCCYZModle.class);
        }
        return ZACYZDao;
    }
    /***治安基础监控**/
    public Dao<ZAJCJKModle, Integer> getZAJKDao() throws SQLException{
        if(ZAJKDao == null){
            ZAJKDao = getDao(ZAJCJKModle.class);
        }
        return ZAJKDao;
    }
    /***治安基础采集**/
    public Dao<ZAJCModle, Integer> getZAJCDao() throws SQLException{
        if(ZAJCDao == null){
            ZAJCDao = getDao(ZAJCModle.class);
        }
        return ZAJCDao;
    }
    /***治安基础消防**/
    public Dao<ZAJCXFModle, Integer> getZAXFDao() throws SQLException{
        if(ZAXFDao == null){
            ZAXFDao = getDao(ZAJCXFModle.class);
        }
        return ZAXFDao;
    }
    /***警情采集**/
    public Dao<JQInfoModle, Integer> getJQCJDao() throws SQLException{
        if(JQCJDao == null){
            JQCJDao = getDao(JQInfoModle.class);
        }
        return JQCJDao;
    }
  /***警情采集**/
    public Dao<Zdxx, Integer> getZdxxDao() throws SQLException{
        if(ZdxxDao == null){
            ZdxxDao = getDao(Zdxx.class);
        }
        return ZdxxDao;
    }


    /**
     * 释放资源
     */
    @Override
    public void close()
    {
        super.close();
        perDao = null;
        ZACYZDao = null;
        ZAJKDao = null;
        ZAJCDao = null;
        ZAXFDao = null;
        JQCJDao = null;
        ZdxxDao = null;
    }
/**
 * activity操作
 * userDao=mDbHelper.getUserDao();
 * 自定义查询语句
 QueryBuilder builder = userDao.queryBuilder();
 builder.where().eq("age", "1");
 builder.query();
 * **/

    /*******************常用方法**********************/
    /**
     * Dao<T,V>

     包含两个泛型,第一个泛型表DAO操作的类,第二个表示操作类的主键类型

     主要方法:

     create:插入一条数据

     createIfNotExists:如果不存在则插入

     createOrUpdate:如果指定id则更新

     queryForId:更具id查找

     update 查找出数据

     refresh的解释:If you want to use other elds in the Account, you must call refresh on the accountDao class to get the Account object lled in.

     delte 删除数据

     queryBuilder() 创建一个查询生成器:进行复杂查询

     deleteBuilder() 创建一个删除生成器,进程复杂条件删除

     updateBuilder() 创建修条件生成器,进行复杂条件修改

     条件查找器DeleteBuilder,QueryBuilder,UpdateBuilder

     查找器是帮助拼接条件语句的.比如查找器中有 where()方法 and()方法 eq()方法 lt()方法 qt()方法 between方法这些方法很直观..很容易都明了什么意思

     最后使用prepare()方法生成条件使用DAO.query || DAO.delete|| DAO.update 方法执行

     可以使用查找生成器QueryBuilder 的 orderby limit offset 方法进行排序,分页,
     *
     * **/
}
