package cn.net.xinyi.xmjt.model;


public class AnSwerInfo {

	public String Id; // 试题主键
	public String QUESTIONNAME; // 试题题目
	public String CORRECTANSWER; // 正确答案
	public String OPTIONA; // 正确答案A
	public String OPTIONB; // 正确答案B
	public String OPTIONC; // 正确答案C
	public String OPTIOND; // 正确答案D
	public String OPTIONE; // 正确答案E
	public String OPTION_TYPE; // 是否是图片题0是1否
	public String ISSELECT; // 是否选择0是1否
	public String QUESTIONTYPE; //试题类型 1单选 2多选
	public String SCORE; //分数

	public String getSCORE() {
		return SCORE;
	}

	public void setSCORE(String SCORE) {
		this.SCORE = SCORE;
	}

	public String getQUESTIONTYPE() {
		return QUESTIONTYPE;
	}

	public void setQUESTIONTYPE(String QUESTIONTYPE) {
		this.QUESTIONTYPE = QUESTIONTYPE;
	}

	public String getId() {
		return Id;
	}
	public void setId(String id) {
		this.Id = id;
	}
	public String getQUESTIONNAME() {
		return QUESTIONNAME;
	}
	public void setQUESTIONNAME(String QUESTIONNAME) {
		this.QUESTIONNAME = QUESTIONNAME;
	}
	public String getCORRECTANSWER() {
		return CORRECTANSWER;
	}
	public void setCORRECTANSWER(String CORRECTANSWER) {
		this.CORRECTANSWER = CORRECTANSWER;
	}
	public String getOPTIONA() {
		return OPTIONA;
	}
	public void setOPTIONA(String OPTIONA) {
		this.OPTIONA = OPTIONA;
	}
	public String getOPTIONB() {
		return OPTIONB;
	}
	public void setOPTIONB(String OPTIONB) {
		this.OPTIONB = OPTIONB;
	}
	public String getOPTIONC() {
		return OPTIONC;
	}
	public void setOPTIONC(String OPTIONC) {
		this.OPTIONC = OPTIONC;
	}
	public String getOPTIOND() {
		return OPTIOND;
	}
	public void setOPTIOND(String OPTIOND) {
		this.OPTIOND = OPTIOND;
	}
	public String getISSELECT() {
		return ISSELECT;
	}
	public void setISSELECT(String ISSELECT) {
		this.ISSELECT = ISSELECT;
	}
	public String getOPTIONE() {
		return OPTIONE;
	}
	public void setOPTIONE(String OPTIONE) {
		this.OPTIONE = OPTIONE;
	}
	public String getOPTION_TYPE() {
		return OPTION_TYPE;
	}
	public void setOPTION_TYPE(String OPTION_TYPE) {
		this.OPTION_TYPE = OPTION_TYPE;
	}
	
	
	
}
