package cn.net.xinyi.xmjt.utils.DB;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;
import java.util.List;

import cn.net.xinyi.xmjt.config.AppException;
import cn.net.xinyi.xmjt.model.JKSInfoModle;
import cn.net.xinyi.xmjt.model.PlateInfoModle;
import cn.net.xinyi.xmjt.model.PoliceContacts;
import cn.net.xinyi.xmjt.model.SXTInfoModle;

public class DBOperation {

	static Context act;
	public static DBHelper db;

	public DBOperation(Context act) {
		db = new DBHelper(act);
		DBOperation.act = act;
	}

	public void clossDb() {
		db.close();
	}

	/**
	 * 根据账号获取本地数据库保存的摄像头信息
	 *
	 * @param username
	 * @return
	 */
	public  List<SXTInfoModle> getCamearInfoList(String username) {
		if (db == null)
			db = new DBHelper(act);
		Cursor cs = db.execQuery("SELECT *  FROM camera_info WHERE CJYH = '"
				+ username + "' ORDER BY id DESC");
		List<SXTInfoModle> list = new ArrayList<SXTInfoModle>();
		while (cs.moveToNext()) {
			SXTInfoModle cainfo=new SXTInfoModle();

			cainfo.setId(cs.getInt(cs.getColumnIndex("id")));
			cainfo.setSXTWZ(cs.getString(cs.getColumnIndex("SXTWZ")));
			cainfo.setWD(cs.getString(cs.getColumnIndex("WD")));
			cainfo.setJD(cs.getString(cs.getColumnIndex("JD")));
			cainfo.setCJDW(cs.getString(cs.getColumnIndex("CJDW")));
			cainfo.setCJSJ(cs.getString(cs.getColumnIndex("CJSJ")));
			cainfo.setSFZC(cs.getString(cs.getColumnIndex("SFZC")));
			cainfo.setCJYH(cs.getString(cs.getColumnIndex("CJYH")));
			cainfo.setZP1(cs.getString(cs.getColumnIndex("ZP1")));
			cainfo.setZP2(cs.getString(cs.getColumnIndex("ZP2")));
			cainfo.setZP3(cs.getString(cs.getColumnIndex("ZP3")));
     		cainfo.setLDBH(cs.getString(cs.getColumnIndex("LDBH")));
	     	cainfo.setJKSMC(cs.getString(cs.getColumnIndex("JKSMC")));
	     	cainfo.setJKSBH(cs.getString(cs.getColumnIndex("JKSBH")));
	     	cainfo.setSXTBH(cs.getString(cs.getColumnIndex("SXTBH")));
	     	cainfo.setSSWG(cs.getString(cs.getColumnIndex("SSWG")));

			cainfo.setBZ(cs.getString(cs.getColumnIndex("BZ")));
			cainfo.setATMBH(cs.getString(cs.getColumnIndex("ATMBH")));
			cainfo.setSXTLB(cs.getString(cs.getColumnIndex("SXTLB")));
			cainfo.setCSFL(cs.getString(cs.getColumnIndex("CSFL")));
			cainfo.setSPBCQX(cs.getString(cs.getColumnIndex("SPBCQX")));
			cainfo.setSSHJ(cs.getString(cs.getColumnIndex("SSHJ")));
			cainfo.setSCCS(cs.getString(cs.getColumnIndex("SCCS")));
			cainfo.setJSDW(cs.getString(cs.getColumnIndex("JSDW")));
			cainfo.setAZSJ(cs.getString(cs.getColumnIndex("AZSJ")));
			cainfo.setJKSBH(cs.getString(cs.getColumnIndex("JKSBH")));
			cainfo.setSXTLX(cs.getString(cs.getColumnIndex("SXTLX")));
			cainfo.setSXTFX(cs.getString(cs.getColumnIndex("SXTFX")));
			cainfo.setLOCTYPE(cs.getString(cs.getColumnIndex("LOCTYPE")));

			list.add(cainfo);
		}
		return list;

	}
	// 更新摄像头的记录，根据Id更新地址
	public int UpdateCamearInfo(int id, SXTInfoModle map) {
		if (db == null)
			db = new DBHelper(act);
		ContentValues cv = new ContentValues();
		cv.put("CJSJ", map.getCJSJ());
		cv.put("SXTWZ", map.getSXTWZ());
		cv.put("JD", map.getJD());
		cv.put("WD", map.getWD());
		cv.put("SFZC", map.getSFZC());
		cv.put("ZP1", map.getZP1());
		cv.put("ZP2", map.getZP2());
		cv.put("ZP3", map.getZP3());
		cv.put("LDBH", map.getLDBH());
		cv.put("JKSMC", map.getJKSMC());
		cv.put("JKSMC", map.getJKSMC());
		cv.put("BZ",map.getBZ());
		cv.put("ATMBH", map.getATMBH());
		cv.put("SXTLB", map.getSXTLB());
		cv.put("SSWG", map.getSSWG());
		cv.put("CSFL", map.getCSFL());
		cv.put("SPBCQX", map.getSPBCQX());
		cv.put("SSHJ",map.getSSHJ());
		cv.put("SCCS",map.getSCCS());
		cv.put("JSDW",map.getJSDW());
		cv.put("LDBH",map.getLDBH());
		cv.put("AZSJ",map.getAZSJ());
		cv.put("JKSBH",map.getJKSBH());
		cv.put("JKSMC",map.getJKSMC());
		cv.put("SXTBH",map.getSXTBH());
		cv.put("SXTLX",map.getSXTLX());
		cv.put("SXTFX", map.getSXTFX());
		cv.put("LOCTYPE",map.getLOCTYPE());
		return db.updateSQL(DBHelper.tableName_camera, cv,"id" + "=?", new String[]{""+id});
	}

	/**
	 * 删除记录
	 *
	 * @return
	 */
	public boolean delCameraInfo(int id) {
		if (db == null)
			db = new DBHelper(act);
		String []args = new String[]{String.valueOf(id)};
		return db.deleteSQL(DBHelper.tableName_camera, "id=?"
				, args) > 0;
	}


	/**
	 * 保存采集的摄像头信息到本地数据库
	 *
	 * @param map
	 * @return
	 * @throws AppException
	 */
	public long insertCamearToSqlite(SXTInfoModle map) throws AppException {
		if (db == null)
			db = new DBHelper(act);
		ContentValues cv = new ContentValues();
		cv.put("CJYH", map.getCJYH());
		cv.put("CJDW", map.getCJDW());
		cv.put("CJSJ", map.getCJSJ());
		cv.put("SXTWZ", map.getSXTWZ());
		cv.put("SFZC", map.getSFZC());
		cv.put("JD", map.getJD());
		cv.put("WD", map.getWD());
		cv.put("ZP1", map.getZP1());
		cv.put("ZP2", map.getZP2());
		cv.put("ZP3", map.getZP3());
		cv.put("LDBH", map.getLDBH());
		cv.put("BZ",map.getBZ());
		cv.put("ATMBH", map.getATMBH());
		cv.put("SXTLB", map.getSXTLB());
		cv.put("CSFL", map.getCSFL());
		cv.put("SSWG", map.getSSWG());
		cv.put("SPBCQX", map.getSPBCQX());
		cv.put("SSHJ",map.getSSHJ());
		cv.put("SCCS",map.getSCCS());
		cv.put("JSDW",map.getJSDW());
		cv.put("LDBH",map.getLDBH());
		cv.put("AZSJ",map.getAZSJ());
		cv.put("JKSBH",map.getJKSBH());
		cv.put("JKSMC",map.getJKSMC());
		cv.put("SXTBH",map.getSXTBH());
		cv.put("SXTLX",map.getSXTLX());
		cv.put("SXTFX",map.getSXTFX());
		cv.put("LOCTYPE", map.getLOCTYPE());

		return db.insertSQL(DBHelper.tableName_camera, null, cv);
	}



	/**
	 * 根据账号获取本地数据库保存的监控室信息
	 *
	 * @param username
	 * @return
	 */
	public List<JKSInfoModle> getCamearRoomInfoList(String username) {
		if (db == null)
			db = new DBHelper(act);
		Cursor cs = db.execQuery("SELECT *  FROM camear_room_info WHERE CJYH = '"
				+ username + "' ORDER BY id DESC");
		List<JKSInfoModle> list = new ArrayList<JKSInfoModle>();
		while (cs.moveToNext()) {
			JKSInfoModle carinfo=new JKSInfoModle();

			carinfo.setId(cs.getInt(cs.getColumnIndex("id")));
			carinfo.setJKSWZ(cs.getString(cs.getColumnIndex("JKSWZ")));
			carinfo.setWD(cs.getString(cs.getColumnIndex("WD")));
			carinfo.setJD(cs.getString(cs.getColumnIndex("JD")));
			carinfo.setCJDW(cs.getString(cs.getColumnIndex("CJDW")));
			carinfo.setCJSJ(cs.getString(cs.getColumnIndex("CJSJ")));
			carinfo.setJKSBH(cs.getString(cs.getColumnIndex("JKSBH")));
			carinfo.setCJYH(cs.getString(cs.getColumnIndex("CJYH")));
			carinfo.setZP1(cs.getString(cs.getColumnIndex("ZP1")));
			carinfo.setZP2(cs.getString(cs.getColumnIndex("ZP2")));
			carinfo.setZP3(cs.getString(cs.getColumnIndex("ZP3")));
			carinfo.setJKSMC(cs.getString(cs.getColumnIndex("JKSMC")));
			carinfo.setLDBH(cs.getString(cs.getColumnIndex("LDBH")));
			carinfo.setSXTSL(cs.getInt(cs.getColumnIndex("SXTSL")));
			carinfo.setZCSYSXTSL(cs.getInt(cs.getColumnIndex("ZCSYSXTSL")));
			carinfo.setAZSJ(cs.getString(cs.getColumnIndex("AZSJ")));
			carinfo.setCZSGRS(cs.getInt(cs.getColumnIndex("CZSGRS")));
			carinfo.setZRR(cs.getString(cs.getColumnIndex("ZRR")));
			carinfo.setLXDH(cs.getString(cs.getColumnIndex("LXDH")));
			carinfo.setSSPCS(cs.getString(cs.getColumnIndex("SSPCS")));
			carinfo.setSSWG(cs.getString(cs.getColumnIndex("SSWG")));
			carinfo.setBZ(cs.getString(cs.getColumnIndex("BZ")));
			carinfo.setSPCZCS(cs.getString(cs.getColumnIndex("SPCZCS")));
			carinfo.setJKSYWFL(cs.getString(cs.getColumnIndex("JKSYWFL")));
			carinfo.setJKSYWFLBM(cs.getString(cs.getColumnIndex("JKSYWFLBM")));
			carinfo.setLOCTYPE(cs.getString(cs.getColumnIndex("LOCTYPE")));

			list.add(carinfo);
		}
		return list;

	}

	/**
	 * 根据账号获取本地数据库保存的监控室信息-只取最近的5条
	 *
	 * @param username
	 * @return
	 */
	public List<JKSInfoModle> getLocalCamearRoomInfo(String username) {
		if (db == null)
			db = new DBHelper(act);
		Cursor cs = db.execQuery("SELECT *  FROM camear_room_info WHERE CJYH='"+username+"' ORDER BY id DESC ");
		List<JKSInfoModle> list = new ArrayList<JKSInfoModle>();
		while (cs.moveToNext()) {
			JKSInfoModle info=new JKSInfoModle();
			info.setId(cs.getInt(cs.getColumnIndex("id")));
//			info.setJKSWZ(cs.getString(cs.getColumnIndex("JKSWZ")));
//			info.setWD(cs.getString(cs.getColumnIndex("WD")));
			info.setJD(cs.getString(cs.getColumnIndex("JD")));
			info.setCJDW(cs.getString(cs.getColumnIndex("CJDW")));
			info.setCJSJ(cs.getString(cs.getColumnIndex("CJSJ")));
			info.setJKSBH(cs.getString(cs.getColumnIndex("JKSBH")));
			info.setCJYH(cs.getString(cs.getColumnIndex("CJYH")));
//			info.setZP1(cs.getString(cs.getColumnIndex("ZP1")));
//			info.setZP2(cs.getString(cs.getColumnIndex("ZP2")));
//			info.setZP3(cs.getString(cs.getColumnIndex("ZP3")));
			info.setJKSMC(cs.getString(cs.getColumnIndex("JKSMC")));
//			info.setLDBH(cs.getString(cs.getColumnIndex("LDBH")));
//			info.setSXTSL(cs.getInt(cs.getColumnIndex("SXTSL")));
//			info.setZCSYSXTSL(cs.getInt(cs.getColumnIndex("ZCSYSXTSL")));
//			info.setAZSJ(cs.getString(cs.getColumnIndex("AZSJ")));
//			info.setCZSGRS(cs.getInt(cs.getColumnIndex("CZSGRS")));
//			info.setZRR(cs.getString(cs.getColumnIndex("ZRR")));
//			info.setLXDH(cs.getString(cs.getColumnIndex("LXDH")));
//			info.setSSPCS(cs.getString(cs.getColumnIndex("SSPCS")));
			info.setSSWG(cs.getString(cs.getColumnIndex("SSWG")));
//			info.setBZ(cs.getString(cs.getColumnIndex("BZ")));
//			info.setSPCZCS(cs.getString(cs.getColumnIndex("SPCZCS")));
//			info.setJKSYWFL(cs.getString(cs.getColumnIndex("JKSYWFL")));
//			info.setJKSYWFLBM(cs.getString(cs.getColumnIndex("JKSYWFLBM")));

			list.add(info);
		}
		return list;
	}

	/**
	 * 根据账号获取本地数据库保存的监控室信息-只取最近的5条
	 *
	 * @param username
	 * @return
	 */
	public List<JKSInfoModle> getTopCamearRoomInfo(String username) {
		if (db == null)
			db = new DBHelper(act);
		Cursor cs = db.execQuery("SELECT *  FROM camear_room_info ORDER BY id DESC LIMIT 5");
		List<JKSInfoModle> list = new ArrayList<JKSInfoModle>();
		while (cs.moveToNext()) {
			JKSInfoModle carinfo=new JKSInfoModle();

			carinfo.setId(cs.getInt(cs.getColumnIndex("id")));
			carinfo.setJKSWZ(cs.getString(cs.getColumnIndex("JKSWZ")));
			carinfo.setWD(cs.getString(cs.getColumnIndex("WD")));
			carinfo.setJD(cs.getString(cs.getColumnIndex("JD")));
			carinfo.setCJDW(cs.getString(cs.getColumnIndex("CJDW")));
			carinfo.setCJSJ(cs.getString(cs.getColumnIndex("CJSJ")));
			carinfo.setJKSBH(cs.getString(cs.getColumnIndex("JKSBH")));
			carinfo.setCJYH(cs.getString(cs.getColumnIndex("CJYH")));
			carinfo.setZP1(cs.getString(cs.getColumnIndex("ZP1")));
			carinfo.setZP2(cs.getString(cs.getColumnIndex("ZP2")));
			carinfo.setZP3(cs.getString(cs.getColumnIndex("ZP3")));
			carinfo.setJKSMC(cs.getString(cs.getColumnIndex("JKSMC")));
			carinfo.setLDBH(cs.getString(cs.getColumnIndex("LDBH")));
			carinfo.setSXTSL(cs.getInt(cs.getColumnIndex("SXTSL")));
			carinfo.setZCSYSXTSL(cs.getInt(cs.getColumnIndex("ZCSYSXTSL")));
			carinfo.setAZSJ(cs.getString(cs.getColumnIndex("AZSJ")));
			carinfo.setCZSGRS(cs.getInt(cs.getColumnIndex("CZSGRS")));
			carinfo.setZRR(cs.getString(cs.getColumnIndex("ZRR")));
			carinfo.setLXDH(cs.getString(cs.getColumnIndex("LXDH")));
			carinfo.setSSPCS(cs.getString(cs.getColumnIndex("SSPCS")));
			carinfo.setSSWG(cs.getString(cs.getColumnIndex("SSWG")));
			carinfo.setBZ(cs.getString(cs.getColumnIndex("BZ")));
			carinfo.setSPCZCS(cs.getString(cs.getColumnIndex("SPCZCS")));
			carinfo.setJKSYWFL(cs.getString(cs.getColumnIndex("JKSYWFL")));
			carinfo.setJKSYWFLBM(cs.getString(cs.getColumnIndex("JKSYWFLBM")));

			list.add(carinfo);
		}
		return list;
	}


   // 更新监控室的记录，根据Id更新地址
	public int UpdateCamearRoomInfo(int id, JKSInfoModle map) {
		if (db == null)
			db = new DBHelper(act);
		ContentValues cv = new ContentValues();

		cv.put("CJSJ", map.getCJSJ());
		cv.put("JKSWZ", map.getJKSWZ());
		cv.put("JD", map.getJD());
		cv.put("WD", map.getWD());
		cv.put("ZP1", map.getZP1());
		cv.put("JKSBH", map.getJKSBH());
		cv.put("ZP2", map.getZP2());
		cv.put("ZP3", map.getZP3());
		cv.put("JKSMC", map.getJKSMC());
		cv.put("LDBH", map.getLDBH());
		cv.put("SXTSL", map.getSXTSL());
		cv.put("ZCSYSXTSL", map.getZCSYSXTSL());
		cv.put("AZSJ", map.getAZSJ());
		cv.put("CZSGRS", map.getCZSGRS());
		cv.put("BZ",map.getBZ());
		cv.put("ZRR",map.getZRR());
		cv.put("LXDH",map.getLXDH());
		cv.put("SSPCS",map.getSSPCS());
		cv.put("SSWG",map.getSSWG());
		cv.put("SPCZCS",map.getSPCZCS());
		cv.put("JKSYWFL",map.getJKSYWFL());
		cv.put("JKSYWFLBM",map.getJKSYWFLBM());
		cv.put("LOCTYPE",map.getLOCTYPE());
		return db.updateSQL(DBHelper.tableName_camera_room, cv,"id" + "=?", new String[]{""+id});
	}

	/**
	 * 保存采集的监控室信息到本地数据库
	 *
	 * @param map
	 * @return
	 * @throws AppException
	 */
	public long insertCamearRoomToSqlite(JKSInfoModle map) throws AppException {
		if (db == null)
			db = new DBHelper(act);
		ContentValues cv = new ContentValues();
		cv.put("CJYH", map.getCJYH());
		cv.put("CJDW", map.getCJDW());
		cv.put("CJSJ", map.getCJSJ());
		cv.put("JKSWZ", map.getJKSWZ());
		cv.put("JD", map.getJD());
		cv.put("WD", map.getWD());
		cv.put("ZP1", map.getZP1());
		cv.put("ZP2", map.getZP2());
		cv.put("ZP3", map.getZP3());
		cv.put("JKSMC", map.getJKSMC());
		cv.put("JKSBH", map.getJKSBH());
		cv.put("LDBH", map.getLDBH());
		cv.put("SXTSL", map.getSXTSL());
		cv.put("ZCSYSXTSL", map.getZCSYSXTSL());
		cv.put("AZSJ", map.getAZSJ());
		cv.put("CZSGRS", map.getCZSGRS());
		cv.put("BZ",map.getBZ());
		cv.put("ZRR",map.getZRR());
		cv.put("LXDH",map.getLXDH());
		cv.put("SSPCS",map.getSSPCS());
		cv.put("SSWG",map.getSSWG());
		cv.put("SPCZCS",map.getSPCZCS());
		cv.put("JKSYWFL",map.getJKSYWFL());
		cv.put("JKSYWFLBM",map.getJKSYWFLBM());
		cv.put("LOCTYPE",map.getLOCTYPE());
		return db.insertSQL(DBHelper.tableName_camera_room, null, cv);
	}


	/**
	 * 删除记录
	 *
	 * @return
	 */
	public boolean delCameraRoomInfo(int id) {
		if (db == null)
			db = new DBHelper(act);
		String []args = new String[]{String.valueOf(id)};
		return db.deleteSQL(DBHelper.tableName_camera_room, "id=?"
				, args) > 0;
	}


	/**
	 * 根据账号获取本地数据库保存的车牌列表
	 * 
	 * @param username
	 * @return
	 */
	public List<PlateInfoModle> getPlateInfoList(String username,String polno) {
		if(polno.isEmpty())
			polno = "999999";
		if (db == null)
			db = new DBHelper(act);
		Cursor cs = db.execQuery("SELECT *  FROM plate_info WHERE username = '"
				+ username + "' OR username = '"+polno+"' ORDER BY id DESC");
		List<PlateInfoModle> list = new ArrayList<PlateInfoModle>();
		while (cs.moveToNext()) {
			PlateInfoModle plate = new PlateInfoModle();
			plate.setId(cs.getInt(cs.getColumnIndex("id")));
			plate.setAccuracy(cs.getString(cs.getColumnIndex("accuracy")));
			plate.setAddress(cs.getString(cs.getColumnIndex("address")));
			plate.setDeviceid(cs.getString(cs.getColumnIndex("deviceid")));
			plate.setCellID(cs.getString(cs.getColumnIndex("cellid")));
			plate.setCellLocationCode(cs.getString(cs
					.getColumnIndex("celllocationcode")));
			plate.setIsupdate(cs.getString(cs.getColumnIndex("isupdate")));
			plate.setLatitude(cs.getString(cs.getColumnIndex("latitude")));
			plate.setLocType(cs.getString(cs.getColumnIndex("loctype")));
			plate.setLongitude(cs.getString(cs.getColumnIndex("longitude")));
			plate.setNetworkCode(cs.getString(cs.getColumnIndex("networkcode")));
			plate.setOrgName(cs.getString(cs.getColumnIndex("orgname")));
			plate.setPlateColor(cs.getString(cs.getColumnIndex("platecolor")));
			plate.setPlateImage(cs.getString(cs.getColumnIndex("plateimage")));
			plate.setPlateNumber(cs.getString(cs.getColumnIndex("platenumber")));
			plate.setPlateThumb(cs.getString(cs.getColumnIndex("platethumb")));
			plate.setPlateType(cs.getString(cs.getColumnIndex("platetype")));
			plate.setTime(cs.getString(cs.getColumnIndex("time")));
			plate.setUploadTime(cs.getString(cs.getColumnIndex("uploadtime")));
			plate.setUserName(cs.getString(cs.getColumnIndex("username")));
			plate.setInBlacklist(cs.getString(cs.getColumnIndex("inblacklist")));
			list.add(plate);
		}
		return list;

	}


	/**
	 * 保存采集的车牌信息到本地数据库
	 * 
	 * @param map
	 * @return
	 * @throws AppException
	 */
	public long insertPlateToSqlite(PlateInfoModle map) throws AppException {
		if (db == null)
			db = new DBHelper(act);

		// id
		int id;
		// 采集用户
		String userName;
		// 采集单位
		String orgName;
		// 采集时间
		String time;
		// 采集地点
		String address;
		// 经度
		String longitude;
		// 纬度
		String latitude;
		// 基站ID
		String cellID;
		// 基站区域码
		String cellLocationCode;
		// 基站类型：1：移动，2：联通，3：电信
		String networkCode;
		// 定位方式
		String locType;
		// 车牌号码
		String plateNumber;
		// 车牌颜色
		String plateColor;
		// 车牌类型
		String plateType;
		// 车牌图片
		String plateImae;
		// 识别精度
		String accuracy;
		// 缩略图
		String plateThumb;
		// 保存或上传标志
		String isupdate;
		// 上传时间
		String uploadTime;
		//比对结果（0:未成功，1：成功）
		String inBlacklist;

		ContentValues cv = new ContentValues();
		cv.put("username", map.getUserName());
		cv.put("orgname", map.getOrgName());
		cv.put("deviceid", map.getDeviceid());
		cv.put("time", map.getTime());
		cv.put("address", map.getAddress());
		cv.put("longitude", map.getLongitude());
		cv.put("latitude", map.getLatitude());
		cv.put("cellid", map.getCellID());
		cv.put("celllocationcode", map.getCellLocationCode());
		cv.put("networkcode", map.getNetworkCode());
		cv.put("platecolor", map.getPlateColor());
		cv.put("platenumber", map.getPlateNumber());
		cv.put("platetype", map.getPlateType());
		cv.put("plateimage", map.getPlateImage());
		cv.put("accuracy", map.getAccuracy());
		cv.put("platethumb", map.getPlateThumb());
		cv.put("isupdate", map.getIsupdate());
		cv.put("inblacklist",map.getInBlacklist());
		cv.put("locType",map.getLocType());
		return db.insertSQL(DBHelper.tableName_plate, null, cv);
	}


	/**
	 * 删除记录
	 * 
	 * @return
	 */
	public boolean delPlateInfo(int id) {
		if (db == null)
			db = new DBHelper(act);
		String []args = new String[]{String.valueOf(id)};
		return db.deleteSQL(DBHelper.tableName_plate, "id=?"
				, args) > 0;
	}


	// 更新users表的记录，根据Id更新地址
	public int UpdateUserInfo(int id, String address) {
		if (db == null)
			db = new DBHelper(act);
		ContentValues values = new ContentValues();
		values.put("address", address);
		return db.updateSQL(DBHelper.tableName_plate, values,"id" + "=?", new String[]{""+id});
	}



	public List<PoliceContacts> getContacts(){
		return getContacts("");
	}

	public List<PoliceContacts> getContacts(String whereSql){
		if (db == null)
			db = new DBHelper(act);
		Cursor cs = db.execQuery("SELECT *  FROM contacts_info " + whereSql
				+ " ORDER BY namespell ");
		List<PoliceContacts> list = new ArrayList<PoliceContacts>();
		while(cs.moveToNext()){
			PoliceContacts contact = new PoliceContacts();
			contact.setName(cs.getString(cs.getColumnIndex("name")));
			contact.setTitle(cs.getString(cs.getColumnIndex("title")));
			contact.setPolno(cs.getString(cs.getColumnIndex("polno")));
			contact.setOrgan(cs.getString(cs.getColumnIndex("organ")));
			contact.setDept(cs.getString(cs.getColumnIndex("dept")));
			contact.setMobileno(cs.getString(cs.getColumnIndex("mobileno")));
			contact.setTelno(cs.getString(cs.getColumnIndex("telno")));
			contact.setId(cs.getString(cs.getColumnIndex("id")));
			contact.setNamespell(cs.getString(cs.getColumnIndex("namespell")));
			contact.setOrganspell(cs.getString(cs.getColumnIndex("organspell")));
			list.add(contact);
		}
		cs.close();

		return list;
	}



	public List<PoliceContacts> getContactsByOrgans(ArrayList<String> organs){
		if(organs==null || organs.size() ==0){
			return null;
		}

		StringBuilder stringBuilder = new StringBuilder("WHERE organ in ( ");
		for(int i=0;i<organs.size();i++){
			stringBuilder.append("'"+organs.get(i)+"'");
			if(i!=organs.size()-1){
				stringBuilder.append(",");
			}
		}
		stringBuilder.append(")");
		return getContacts(stringBuilder.toString());
	}

	public List<PoliceContacts> getContactsByNumber(String number){
		String whereSql = " WHERE mobileno like '%" + number
				+ "%' OR telno like '%"+number+"%' ";
		return getContacts(whereSql);
	}

	public ArrayList<String> getContactsOrgan(){
		if (db == null)
			db = new DBHelper(act);
		Cursor cs = db.execQuery("SELECT DISTINCT organ  FROM contacts_info where organ is not null and organ!='' "
				+ " ORDER BY organspell ");
		ArrayList<String> list = new ArrayList<String>();
		while(cs.moveToNext()){
			String organ = cs.getString(cs.getColumnIndex("organ"));
			list.add(organ);
		}
		return list;
	}

	public long insertContacts(List<PoliceContacts> contacts){
		if (db == null)
			db = new DBHelper(act);

		SQLiteDatabase mdb = db.getWritableDatabase();
		mdb.beginTransaction();
		//先清空本地通讯录；
		mdb.delete(DBHelper.tableName_contacts,null,null);

		for(int i=0;i<contacts.size();i++){
			ContentValues cv = new ContentValues();
			cv.put("id",contacts.get(i).getId());
			cv.put("name",contacts.get(i).getName());
			cv.put("title",contacts.get(i).getTitle());
			cv.put("polno",contacts.get(i).getPolno());
			cv.put("organ",contacts.get(i).getOrgan());
			cv.put("dept",contacts.get(i).getDept());
			cv.put("mobileno",contacts.get(i).getMobileno());
			cv.put("telno",contacts.get(i).getTelno());
			cv.put("namespell",contacts.get(i).getNamespell());
			cv.put("organspell",contacts.get(i).getOrganspell());
			long flag = mdb.insert(DBHelper.tableName_contacts,"",cv);
			if(flag == -1){
				mdb.endTransaction();
				mdb.close();
				return 0;
			}
		}
		mdb.setTransactionSuccessful();
		mdb.endTransaction();
		mdb.close();
		return contacts.size();
	}

}
