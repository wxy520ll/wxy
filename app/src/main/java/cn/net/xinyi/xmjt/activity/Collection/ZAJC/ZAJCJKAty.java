package cn.net.xinyi.xmjt.activity.Collection.ZAJC;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import cn.net.xinyi.xmjt.R;
import cn.net.xinyi.xmjt.config.BaseActivity2;
import cn.net.xinyi.xmjt.model.View.IZAJKView;
import cn.net.xinyi.xmjt.model.ZAJCJKModle;
import cn.net.xinyi.xmjt.utils.AnnotateManager;
import cn.net.xinyi.xmjt.utils.BaseUtil;
import cn.net.xinyi.xmjt.utils.BindView;
import cn.net.xinyi.xmjt.utils.ImageUtils;

/**
 * Created by hao.zhou on 2016/3/19.
 */
public class ZAJCJKAty extends BaseActivity2 implements IZAJKView,View.OnClickListener {
    /**监控室编号**/
    @BindView(id = R.id.et_jksbh,click = false)
    private EditText et_jksbh;
    /**摄像头总数**/
    @BindView(id = R.id.et_sxtzs,click = false)
    private EditText et_sxtzs;
    /**摄像头正常数**/
    @BindView(id = R.id.et_sxtzcs,click = false)
    private EditText et_sxtzcs;
    /**门口摄像头正常数**/
    @BindView(id = R.id.et_mksxts,click = false)
    private EditText et_mksxts;
    /**监控屏幕**/
    @BindView(id = R.id.iv_jkpm,click = true)
    private ImageView iv_jkpm;
    /**摄像头全景图**/
    @BindView(id = R.id.iv_sxtqjt,click = true)
    private ImageView iv_sxtqjt;
    /**上传**/
    @BindView(id = R.id.btn_ok,click = true)
    private Button btn_ok;
    private  String path1;
    private  String path2;
    private ZAJCJKModle jkInfo;
    /***ImageView拍照标识*/
    private int i_flag;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.aty_zajkxx);
        /***注解式绑定控件**/
        AnnotateManager.initBindView(this);
        jkInfo=new ZAJCJKModle();
        checkTextLength(et_sxtzcs,3);
        checkTextLength(et_sxtzs,3);
        checkTextLength(et_mksxts,3);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.iv_jkpm:
                i_flag = 1;
                cameraPhoto();
                break;

            case R.id.iv_sxtqjt:
                i_flag = 2;
                cameraPhoto();
                break;

            case R.id.btn_ok:
                String msg = "";
                int nu_sxtzcs=et_sxtzcs.getText().toString().isEmpty()  ? 0: Integer.parseInt(et_sxtzcs.getText().toString());
                int nu_sxtzs=et_sxtzs.getText().toString().isEmpty() ? 0: Integer.parseInt(et_sxtzs.getText().toString());
                int nu_mksxts=et_mksxts.getText().toString().isEmpty() ? 0: Integer.parseInt(et_mksxts.getText().toString());
                if (TextUtils.isEmpty(getSXTZS())){
                    msg =  getResources().getString(R.string.store_sxtzs)+getResources().getString(R.string.info_null) ;
                }else if (TextUtils.isEmpty(getZCS())){
                    msg =  getResources().getString(R.string.store_sxtzc)+getResources().getString(R.string.info_null) ;
                }else if (nu_sxtzcs > nu_sxtzs){
                    msg = getString(R.string.sxt_zcs_tips);
                }else if (nu_mksxts > nu_sxtzcs){
                    msg = getString(R.string.mksxt_zcs_tips);
                }
                if(!msg.isEmpty()) {
                    BaseUtil.showDialog(msg, this);
                }else {
                    jkInfo.setJKSBH(getJKSBH());
                    jkInfo.setSXTZS(getSXTZS());
                    jkInfo.setZCS(getZCS());
                    jkInfo.setMKSXTS(getMKSXTS());
                    jkInfo.setIV_JKPMT(path1);
                    jkInfo.setIV_SXTQJT(path2);
                    Intent resultIntent = new Intent();
                    resultIntent.putExtra("jkInfo",jkInfo);
                    setResult(3, resultIntent);
                    this.finish();
                }

                break;
        }

    }

    @Override
    public int getJKID() {
        return 0;
    }

    @Override
    public String getJKSBH() {
        return et_jksbh.getText().toString();
    }

    @Override
    public String getSXTZS() {
        return et_sxtzs.getText().toString();
    }

    @Override
    public String getZCS() {
        return et_sxtzcs.getText().toString();
    }

    @Override
    public String getMKSXTS() {
        return et_mksxts.getText().toString();
    }

    @Override
    public String getIV_JKPMT() {
        return path1;
    }

    @Override
    public String getIV_SXTQJT() {
        return path2;
    }


    /**
     *返回拍照
     */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == CAMERA_INTENT_REQUEST
                &&resultCode!=0
                && imagePath != null) {
            /***获得拍照的路径 写入数据库**/
            if (i_flag == 1){
                path1 = imagePath;
                iv_jkpm.setImageBitmap(ImageUtils.compressImageByPixel(imagePath));
            }else  if (i_flag == 2){
                path2 = imagePath;
                iv_sxtqjt.setImageBitmap(ImageUtils.compressImageByPixel(imagePath));
            }
        }
    }


    /**
     * 捕获后退按钮
     */
    @Override
    public boolean dispatchKeyEvent(KeyEvent event) {
        if (event.getAction() == KeyEvent.ACTION_DOWN
                && event.getKeyCode() == KeyEvent.KEYCODE_BACK) {
            this.finish();
        }
        // super.dispatchKeyEvent(event);
        return super.dispatchKeyEvent(event);
    }


    @Override
    public String getAtyTitle() {
        return getString(R.string.zajk);
    }

    @Override
    public boolean enableBackUpBtn() {
        return true;
    }
}