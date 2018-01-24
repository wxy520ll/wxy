package cn.net.xinyi.xmjt.utils.DB;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.io.IOException;
import java.io.InputStream;

import cn.net.xinyi.xmjt.R;

/**
 * sqlite操作工具包
 * @author nodeny
 * @version 1.0
 * @created 2012-11-21
 */
public class DBHelper extends SQLiteOpenHelper {

	private static final String DBNAME = "xmjt.db";
	public static final String tableName_plate = "plate_info";
	public static final String tableName_contacts = "contacts_info";
	public static final String tableName_camera_room = "camear_room_info";
	public static final String tableName_camera = "camera_info";
	public static final String tableName_umessage = "umessage_info";

	private SQLiteDatabase myDB;
	/**
	 * 未使用orm-lite数据库版本
	 * 数据库版本 */
	public static final int DATABASE_VERSION = 5;
	private Context myContext;


	public DBHelper(Context context) {
		super(context, DBNAME, null, DATABASE_VERSION);
		this.myContext = context;
		if (myDB == null)
			myDB = getWritableDatabase();
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		//车牌信息采集
		String plate_db_init = this.getCreateSql(R.raw.plate_db_init);
		//监控室信息采集
		String camear_room_db_init = this.getCreateSql(R.raw.camear_room_db_init);
		//摄像头信息采集
		String camera_db_init = this.getCreateSql(R.raw.camera_db_init);
		//民警通讯录
		String contact_db_init = this.getCreateSql(R.raw.contact_db_init);
		//友盟推送消息
		String umessage = this.getCreateSql(R.raw.umessage);

		myDB = db;
		myDB.beginTransaction();
		try {
			execSQL(plate_db_init);
			execSQL(contact_db_init);
			execSQL(camear_room_db_init);
			execSQL(camera_db_init);
			execSQL(umessage);
			myDB.setTransactionSuccessful();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			myDB.endTransaction();
		}
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		//监控室信息采集
		String camear_room_db_init = this.getCreateSql(R.raw.camear_room_db_init);
		//摄像头信息采集
		String camera_db_init = this.getCreateSql(R.raw.camera_db_init);
		//民警通讯录
		String contact_db_init = this.getCreateSql(R.raw.contact_db_init);
		//车牌采集表更新
		String plate_db_update = this.getCreateSql(R.raw.plate_db_update);
		//友盟推送消息
		String umessage = this.getCreateSql(R.raw.umessage);
		//监控室信息采集更新
		String jks_db_init = this.getCreateSql(R.raw.jks_db_update);
		//摄像头信息采集更新
		String sxt_db_init = this.getCreateSql(R.raw.sxt_db_update);

		if(oldVersion<4 ){
			myDB = db;
			myDB.beginTransaction();
			try {
				execSQL(contact_db_init);
				execSQL(plate_db_update);
				execSQL(camear_room_db_init);
				execSQL(camera_db_init);
				execSQL(umessage);
				myDB.setTransactionSuccessful();
			} catch (Exception e) {
				e.printStackTrace();
			} finally {
				myDB.endTransaction();
			}
		}

		if(oldVersion<5){
			myDB = db;
			myDB.beginTransaction();
			try {
				execSQL(jks_db_init);
				execSQL(sxt_db_init);
				myDB.setTransactionSuccessful();
			} catch (Exception e) {
				e.printStackTrace();
			} finally {
				myDB.endTransaction();
			}
		}
	}

	public DBHelper open() throws SQLException {
		myDB = this.getWritableDatabase();
		return this;
	}

	public int deleteSQL(String table, String whereClause, String[] whereArgs) {
		return myDB.delete(table, whereClause, whereArgs);
	}

	public Cursor execQuery(String query) {
		// myDBHelper.open();
		if (myDB == null)
			myDB = this.getWritableDatabase();
		return myDB.rawQuery(query, null);
		// myDBHelper.close();
	}

	public void execSQL(String query) throws SQLException {
		if (myDB == null)
			myDB = this.getWritableDatabase();
		myDB.execSQL(query);
	}

	public Cursor queryCV(String tablename, String selection, String[] args,
						  String orderBy) {
		if (myDB == null)
			myDB = this.getWritableDatabase();
		return myDB.query(tablename, null, selection, args, null, "", orderBy);
	}

	public long insertSQL(String table, String nullColumnHack,
						  ContentValues values) throws SQLException {
		long rows = myDB.insertOrThrow(table, nullColumnHack, values);
		return rows;
	}

	public int updateSQL(String table, ContentValues values,
						 String whereClause, String[] whereArgs) throws SQLException {
		return myDB.update(table, values, whereClause, whereArgs);
	}

	public Cursor ExecQueryParam(String query, String[] param)
			throws SQLException {
		// myDBHelper.open();
		return myDB.rawQuery(query, param);
		// myDBHelper.close();
	}

	/**
	 * 关闭数据库
	 */
	public void closeDb() {
		if (myDB != null && myDB.isOpen()) {
			myDB.close();
		}
	}

	public String getCreateSql(int table) {
		InputStream is = this.myContext.getResources().openRawResource(table);
		StringBuffer out = new StringBuffer();
		byte[] b = new byte[4096];
		try {
			for (int n; (n = is.read(b)) != -1;) {
				out.append(new String(b, 0, n));
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		return out.toString();
	}

}