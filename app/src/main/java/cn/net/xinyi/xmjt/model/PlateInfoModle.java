package cn.net.xinyi.xmjt.model;

import java.io.Serializable;

public class PlateInfoModle implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -8514463941481607850L;
	//存入本地数据库
	public static final int ISUPDATE_LOCAL = 0;
	//正在上传到服务器
	public static final int ISUPDATE_UPDATE_SERVER = 1;
	//已上传到服务器
	public static final int ISUPDATE_FINNISH_SERVER = 2;
	
	//id
	private int id;
	//采集用户
	private String userName="";
	//采集单位
	private String orgName="";
	//采集设备
	private String deviceid="";
	//采集时间
	private String time="";
	//采集地点
	private String address="";
	//经度
	private String longitude="0.00000";
	//纬度
	private String latitude="0.00000";
	//基站ID
	private String cellID="0";
	//基站区域码
	private String cellLocationCode="0";
	//基站类型：1：移动，2：联通，3：电信
	private String networkCode="";
	//定位方式
	private String locType="";
	//车牌号码
	private String plateNumber="";
	//车牌颜色
	private String plateColor="";
	//车牌类型
	private String plateType="";
	//车牌图片
	private String plateImage="";
	//识别精度
	private String  accuracy="";
	//缩略图
	private String plateThumb="";
	//保存或上传标志
	private String isupdate="";
	//上传时间
	private String uploadTime="";
	//是否比对成功
	private String inBlacklist = "0";

	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getUserName() {
		return userName;
	}
	public void setUserName(String userName) {
		this.userName = userName;
	}
	public String getOrgName() {
		return orgName;
	}
	public void setOrgName(String orgName) {
		this.orgName = orgName;
	}
	public String getTime() {
		return time;
	}
	public void setTime(String time) {
		this.time = time;
	}
	public String getAddress() {
		return address;
	}
	public void setAddress(String address) {
		this.address = address;
	}
	public String getLongitude() {
		return longitude;
	}
	public void setLongitude(String longitude) {
		this.longitude = longitude;
	}
	public String getLatitude() {
		return latitude;
	}
	public void setLatitude(String latitude) {
		this.latitude = latitude;
	}
	public String getCellID() {
		return cellID;
	}
	public void setCellID(String cellID) {
		this.cellID = cellID;
	}
	public String getCellLocationCode() {
		return cellLocationCode;
	}
	public void setCellLocationCode(String cellLocationCode) {
		this.cellLocationCode = cellLocationCode;
	}
	public String getNetworkCode() {
		return networkCode;
	}
	public void setNetworkCode(String networkCode) {
		this.networkCode = networkCode;
	}
	public String getPlateNumber() {
		return plateNumber;
	}
	public void setPlateNumber(String plateNumber) {
		this.plateNumber = plateNumber;
	}
	public String getPlateColor() {
		return plateColor;
	}
	public void setPlateColor(String plateColor) {
		this.plateColor = plateColor;
	}
	public String getPlateType() {
		return plateType;
	}
	public void setPlateType(String plateType) {
		this.plateType = plateType;
	}
	public String getPlateImage() {
		return plateImage;
	}
	public void setPlateImage(String plateImage) {
		this.plateImage = plateImage;
	}
	public String getAccuracy() {
		return accuracy;
	}
	public void setAccuracy(String accuracy) {
		this.accuracy = accuracy;
	}
	public String getPlateThumb() {
		return plateThumb;
	}
	public void setPlateThumb(String plateThumb) {
		this.plateThumb = plateThumb;
	}
	public String getIsupdate() {
		return isupdate;
	}
	public void setIsupdate(String isupdate) {
		this.isupdate = isupdate;
	}
	public String getUploadTime() {
		return uploadTime;
	}
	public void setUploadTime(String uploadTime) {
		this.uploadTime = uploadTime;
	}
	public String getLocType() {
		return locType;
	}
	public void setLocType(String locType) {
		this.locType = locType;
	}
	public String getDeviceid() {
		return deviceid;
	}
	public void setDeviceid(String deviceid) {
		this.deviceid = deviceid;
	}

	public String getInBlacklist() {
		return inBlacklist;
	}

	public void setInBlacklist(String inBlacklist) {
		this.inBlacklist = inBlacklist;
	}
}
