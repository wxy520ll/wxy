package cn.net.xinyi.xmjt.model;

/**
 * Created by hao.zhou on 2016/11/11.
 */
public class MachineExamModle {
    private int ID;
    private String CJYH;//考核用户
    private int SCORE;//考核分数
    private String SCSJ;//上传时间

    public static int ResuleScore=90;

    public String getSCSJ() {
        return SCSJ;
    }

    public void setSCSJ(String SCSJ) {
        this.SCSJ = SCSJ;
    }

    public int getSCORE() {
        return SCORE;
    }

    public void setSCORE(int SCORE) {
        this.SCORE = SCORE;
    }

    public String getCJYH() {
        return CJYH;
    }

    public void setCJYH(String CJYH) {
        this.CJYH = CJYH;
    }

    public int getID() {
        return ID;
    }

    public void setID(int ID) {
        this.ID = ID;
    }
}
