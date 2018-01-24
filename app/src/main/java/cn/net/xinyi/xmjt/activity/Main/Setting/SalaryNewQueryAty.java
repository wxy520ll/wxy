package cn.net.xinyi.xmjt.activity.Main.Setting;

import android.os.Bundle;
import android.widget.TextView;

import cn.net.xinyi.xmjt.R;
import cn.net.xinyi.xmjt.config.BaseActivity2;
import cn.net.xinyi.xmjt.model.SalaryNewModle;
import cn.net.xinyi.xmjt.utils.AnnotateManager;
import cn.net.xinyi.xmjt.utils.BindView;
import cn.net.xinyi.xmjt.utils.StringUtils;

public class SalaryNewQueryAty extends BaseActivity2 {

    private SalaryNewModle salary;
    //月份
    @BindView(id = R.id.tv_month)
    TextView tv_month;
    //名字
    @BindView(id = R.id.tv_realname)
    TextView tv_realname;
    //单位
    @BindView(id = R.id.tv_organname)
    TextView tv_organname;
    //岗位类别
    @BindView(id = R.id.tv_posttype)
    TextView tv_posttype;
    //岗位
    @BindView(id = R.id.tv_postname)
    TextView tv_postname;
    //银行账号
    @BindView(id = R.id.tv_bankno)
    TextView tv_bankno;
    //基本工资
    @BindView(id = R.id.tv_basewages)
    TextView tv_basewages;
    //工龄工资
    @BindView(id = R.id.tv_workagewage)
    TextView tv_workagewage;
    //岗位工资
    @BindView(id = R.id.tv_gwgz)
    TextView tv_gwgz;
    //职级补贴
    @BindView(id = R.id.tv_zjbt)
    TextView tv_zjbt;
    //绩效工资
    @BindView(id = R.id.tv_jxgz)
    TextView tv_jxgz;
    //职务津贴
    @BindView(id = R.id.tv_zwjt)
    TextView tv_zwjt;
    //高温补贴
    @BindView(id = R.id.tv_gwbt)
    TextView tv_gwbt;
    //当月绩效
    @BindView(id = R.id.tv_monthperform)
    TextView tv_monthperform;
    //周末加班工资
    @BindView(id = R.id.tv_zmjbgz)
    TextView tv_zmjbgz;
    //法定节假日工资
    @BindView(id = R.id.tv_fdjrjbgz)
    TextView tv_fdjrjbgz;
    //加班工资汇总
    @BindView(id = R.id.tv_jbgz)
    TextView tv_jbgz;
    //其他工资
    @BindView(id = R.id.tv_otherwage)
    TextView tv_otherwage;
    //其他工资备注
    @BindView(id = R.id.tv_otherwanemark)
    TextView tv_otherwanemark;
    //特殊岗位补贴
    @BindView(id = R.id.tv_tsgwbt)
    TextView tv_tsgwbt;
    //小教官补贴
    @BindView(id = R.id.tv_xjgbt)
    TextView tv_xjgbt;
    //特殊情况处理
    @BindView(id = R.id.tv_specialflag)
    TextView tv_specialflag;
    //扣税
    @BindView(id = R.id.tv_fax)
    TextView tv_fax;
    //公基金
    @BindView(id = R.id.tv_housefund)
    TextView tv_housefund;
    //社保
    @BindView(id = R.id.tv_sociafund)
    TextView tv_sociafund;
    //扣款合计
    @BindView(id = R.id.tv_deductall)
    TextView tv_deductall;
    //税后工资
    @BindView(id = R.id.tv_shgz)
    TextView tv_shgz;
    //应发合计
    @BindView(id = R.id.tv_totalwages)
    TextView tv_totalwages;
    //实发工资
    @BindView(id = R.id.tv_shoudgive)
    TextView tv_shoudgive;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.aty_salary_new_query);
        AnnotateManager.initBindView(this);//注解式绑定控件
        salary= (SalaryNewModle) getIntent().getSerializableExtra("salary");
        setData();
    }

    private void setData() {
        //月份
        tv_month.setText(salary.getDATAYEAR()+"-"+salary.getDATAMONTH());
        //名字
        tv_realname.setText(salary.getREALNAME());
        //单位
        tv_organname.setText(salary.getORGANNAME());
        //岗位类别
        tv_posttype.setText(salary.getPOSTTYPE() );
        //岗位
        tv_postname.setText(salary.getPOSTNAME() );
        //银行账号
        tv_bankno.setText(salary.getBANKNO() );
        //基本工资
        tv_basewages.setText(""+salary.getBASEWAGES() );
        //工龄工资
        tv_workagewage.setText(""+salary.getWORKAGEWAGE() );
        //岗位工资
        tv_gwgz.setText(""+salary.getGWGZ() );
        //职级补贴
        tv_zjbt.setText(""+salary.getZJBT() );
        //绩效工资
        tv_jxgz.setText(""+salary.getJXGZ() );
        //职务津贴
        tv_zwjt.setText(""+salary.getZWJT() );
        //高温补贴
        tv_gwbt.setText(""+salary.getGWBT() );
        //当月绩效
        tv_monthperform.setText(""+salary.getMONTHPERFORM() );
        //周末加班工资
        tv_zmjbgz.setText(""+salary.getZMJBGZ() );
        //法定节假日工资
        tv_fdjrjbgz.setText(""+salary.getFDJRJBGZ() );
        //加班工资汇总
        tv_jbgz.setText(""+salary.getJXGZ() );
        //其他工资
        tv_otherwage.setText(""+salary.getOTHERWAGE() );
        //其他工资备注
        tv_otherwanemark.setText(StringUtils.isEmpty(salary.getOTHERWAGEMARK())?"":salary.getOTHERWAGEMARK());
        //特殊岗位补贴
        tv_tsgwbt.setText(""+salary.getTSGWBT() );
        //小教官补贴
        tv_xjgbt.setText(""+salary.getXJGBT() );
        //特殊情况处理
        tv_specialflag.setText(""+salary.getSPECIALFLAG());
        //扣税
        tv_fax.setText(""+salary.getFAX() );
        //公积金
        tv_housefund.setText(""+salary.getHOUSEFUND() );
        //社保
        tv_sociafund.setText(""+salary.getSOCIALFUND());
        //扣款合计
        tv_deductall.setText(""+salary.getDEDUCTALL() );
        //税后工资
        tv_shgz.setText(""+salary.getSHGZ() );
        //应发合计
        tv_totalwages.setText(""+salary.getTOTALWAGES() );
        //实发工资
        tv_shoudgive.setText(""+salary.getSHOUDGIVE() );
    }

    @Override
    public boolean enableBackUpBtn() {
        return true;
    }

    @Override
    public String getAtyTitle() {
        return getString(R.string.salary_query);
    }
}
