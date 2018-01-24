package cn.net.xinyi.xmjt.activity.Collection.Person;


import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import cn.net.xinyi.xmjt.R;
import cn.net.xinyi.xmjt.config.BaseActivity;
import cn.net.xinyi.xmjt.model.PerReturnModle;
import cn.net.xinyi.xmjt.model.Presenter.PreReturnPresenter;
import cn.net.xinyi.xmjt.model.View.IPerReturnView;
import cn.net.xinyi.xmjt.utils.AnnotateManager;
import cn.net.xinyi.xmjt.utils.BaseUtil;
import cn.net.xinyi.xmjt.utils.BindView;
import cn.net.xinyi.xmjt.utils.DB.DBHelperNew;
import cn.net.xinyi.xmjt.utils.GeneralUtils;
import cn.net.xinyi.xmjt.utils.ImageUtils;


/**
 * Created by hao.zhou on 2016/1/28.
 * 人员走访
 */
public class PerReturnWatchAty extends BaseActivity implements IPerReturnView,View.OnClickListener{
    /**被走访人姓名**/
    @BindView(id = R.id.et_name,click = false)
    private EditText et_name;
    /**被走访人身份证号码**/
    @BindView(id = R.id.et_sfz,click = false)
    private EditText et_sfz;
    /**走访后地址**/
    @BindView(id = R.id.et_dzh,click = false)
    private EditText et_dzh;
    /**坐标**/
    @BindView(id = R.id.tv_zb,click = false)
    private TextView tv_zb;
    /**手动定位**/
    @BindView(id = R.id.tv_sddw,click = false)
    private TextView tv_sddw;
    /**备注**/
    @BindView(id = R.id.et_bz,click = false)
    private EditText et_bz;
    /**楼宇全景**/
    @BindView(id = R.id.iv_lyqj,click = false)
    private ImageView iv_lyqj;
    /**删除**/
    @BindView(id = R.id.btn_del,click = true)
    private Button btn_del;
    /**百度坐标纬度**/
    private double bWd;
    /**百度坐标经度**/
    private double bJD;
    /***拍照图片路径 存放数据库**/
    private String path1;
    private PreReturnPresenter preRePres;
    /***定位类型*/
    private int map_type;
    private PerReturnModle perInfos;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.aty_per_return_watch);
        /***注解式绑定控件**/
        AnnotateManager.initBindView(this);
        getActionBar().setIcon(R.drawable.actionbar_back_icon_normal);
        getActionBar().setTitle(getResources().getString(R.string.PER_RETURN));
        getActionBar().setHomeButtonEnabled(true);
        perInfos = (PerReturnModle) getIntent().getSerializableExtra(GeneralUtils.Info);
        initData();

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.btn_del:
                new AlertDialog.Builder(PerReturnWatchAty.this)
                        .setTitle(R.string.msg_delete_confirm_title)
                        .setMessage(R.string.msg_delete_confirm)
                        .setPositiveButton(R.string.sure,
                                new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog,
                                                        int which) {
                                        try {
                                            /**数据库执行删除操作**/
                                            DBHelperNew.getInstance(PerReturnWatchAty.this).getPreReturnDao().deleteById(perInfos.getId());
                                            /**弹出框删除成功**/
                                            setDialog(PerReturnWatchAty.this,getString(R.string.del_yes));
                                        } catch (Exception e) {
                                            BaseUtil.showDialog("删除失败，请稍后再试！", PerReturnWatchAty.this);
                                            e.printStackTrace();
                                        }
                                    }
                                })
                        .setNegativeButton(R.string.cancle,
                                new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog,
                                                        int which) {
                                    }
                                }).show();
                break;

        }
    }

    private void initData() {
        /**presenter 初始化**/
        preRePres = new PreReturnPresenter(this);
        /**初始化 采集信息*/
        preRePres.setPreRetnData(perInfos, tv_zb);
    }


    @Override
    public int getId() {
        return perInfos.getId();
    }

    @Override
    public String getNAME() {
        return et_name.getText().toString();
    }

    @Override
    public String getSFZH() {
        return et_sfz.getText().toString();
    }

    @Override
    public String getZFHDZ() {
        return et_dzh.getText().toString();
    }

    @Override
    public Double getJd() {
        return bJD;
    }

    @Override
    public Double getWd() {
        return bWd;
    }

    @Override
    public String getBZ() {
        return et_bz.getText().toString();
    }

    @Override
    public String getLYQZZP() {
        return path1;
    }

    @Override
    public String getLOCTYPE() {
        return ""+map_type;
    }

    @Override
    public void setNAME(String NAME) {
        et_name.setText(NAME);
    }

    @Override
    public void setSFZH(String SFZH) {
        et_sfz.setText(SFZH);
    }

    @Override
    public void setZFHDZ(String ZFHDZ) {
        et_dzh.setText(ZFHDZ);
    }

    @Override
    public void setJd(Double jd) {
        bJD=jd;
    }

    @Override
    public void setWd(Double wd) {
        bWd=wd;
    }

    @Override
    public void setBZ(String BZ) {
        et_bz.setText(BZ);
    }

    @Override
    public void setLYQZZP(String LYQZZP) {
        if (LYQZZP != null){
            path1 =LYQZZP;
            iv_lyqj.setImageBitmap(ImageUtils.decodeFile(path1));
        }

    }


    @Override
    public void setLOCTYPE(String LOCTYPE) {
        map_type=Integer.parseInt(LOCTYPE);
    }


    /***activity退出*/
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {

            case android.R.id.home:
                this.finish();

                break;
            default:
                break;
        }
        return true;
    }


    /**返回键退出*/
    @Override
    public boolean dispatchKeyEvent(KeyEvent event) {
        /**返回如果地图是显示，先隐藏地图*/
        if (event.getAction() == KeyEvent.ACTION_DOWN
                && event.getKeyCode() == KeyEvent.KEYCODE_BACK) {
            this.finish();
        }
        return super.dispatchKeyEvent(event);
    }


}