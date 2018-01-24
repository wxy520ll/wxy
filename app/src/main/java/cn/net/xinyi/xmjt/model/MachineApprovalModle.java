package cn.net.xinyi.xmjt.model;

import java.util.List;

/**
 * Created by hao.zhou on 2016/11/16.
 */
public class MachineApprovalModle {

   private int ID	;//
    private int  ROOMID;	//机房id
    private String  SJHM;			//申请人手机号码
    private String SHJG;		//'0'	审核结果（未审核0  审核通过1 审核未通过2）
    private String SHSJ;		//审核时间
    private String SQSJ;	//sysdate	申请时间
    private String SHRY;		//		审核人员
    private String BZ;	//		备注
    private String FELLOWCOUNT ;	//	同行人员人数
    private  String DATALIST;//数据集合
    private List<MachineFowModle> lists;

    public int getID() {
        return ID;
    }

    public void setID(int ID) {
        this.ID = ID;
    }

    public int getROOMID() {
        return ROOMID;
    }

    public void setROOMID(int ROOMID) {
        this.ROOMID = ROOMID;
    }

    public String getSJHM() {
        return SJHM;
    }

    public void setSJHM(String SJHM) {
        this.SJHM = SJHM;
    }

    public String getSHJG() {
        return SHJG;
    }

    public void setSHJG(String SHJG) {
        this.SHJG = SHJG;
    }

    public String getSHSJ() {
        return SHSJ;
    }

    public void setSHSJ(String SHSJ) {
        this.SHSJ = SHSJ;
    }

    public String getSQSJ() {
        return SQSJ;
    }

    public void setSQSJ(String SQSJ) {
        this.SQSJ = SQSJ;
    }

    public String getSHRY() {
        return SHRY;
    }

    public void setSHRY(String SHRY) {
        this.SHRY = SHRY;
    }

    public String getBZ() {
        return BZ;
    }

    public void setBZ(String BZ) {
        this.BZ = BZ;
    }

    public String getFELLOWCOUNT() {
        return FELLOWCOUNT;
    }

    public void setFELLOWCOUNT(String FELLOWCOUNT) {
        this.FELLOWCOUNT = FELLOWCOUNT;
    }

    public String getDATALIST() {
        return DATALIST;
    }

    public void setDATALIST(String DATALIST) {
        this.DATALIST = DATALIST;
    }

    public List<MachineFowModle> getLists() {
        return lists;
    }

    public void setLists(List<MachineFowModle> lists) {
        this.lists = lists;
    }
}
