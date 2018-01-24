package cn.net.xinyi.xmjt.activity.Collection.ZAJC;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import cn.net.xinyi.xmjt.R;
import cn.net.xinyi.xmjt.config.BaseActivity2;
import cn.net.xinyi.xmjt.model.View.IZAXFView;
import cn.net.xinyi.xmjt.model.ZAJCJKModle;
import cn.net.xinyi.xmjt.model.ZAJCXFModle;
import cn.net.xinyi.xmjt.utils.AnnotateManager;
import cn.net.xinyi.xmjt.utils.BaseDataUtils;
import cn.net.xinyi.xmjt.utils.BaseUtil;
import cn.net.xinyi.xmjt.utils.BindView;
import cn.net.xinyi.xmjt.utils.ImageUtils;

/**
 * Created by hao.zhou on 2016/3/19.
 */
public class ZAJCXFInfoAty extends BaseActivity2 implements IZAXFView,View.OnClickListener {
    /**灭火器数量**/
    @BindView(id = R.id.et_mhqsl,click = false)
    private EditText et_mhqsl;
    /**灭火器正常数**/
    @BindView(id = R.id.et_mhqzcs,click = false)
    private EditText et_mhqzcs;
    /**到期日期**/
    @BindView(id = R.id.tv_dqrq,click = true)
    private TextView tv_dqrq;
    /**消防全景图**/
    @BindView(id = R.id.iv_xfqjt,click = true)
    private ImageView iv_xfqjt;
    /**完成**/
    @BindView(id = R.id.btn_ok,click = true)
    private Button btn_ok;
    private ZAJCXFModle xfInfo;
    private  String path1;
    private ZAJCJKModle jkInfo;
    /***ImageView拍照标识*/
    private int i_flag;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.aty_zaxfxx);
        /***注解式绑定控件**/
        AnnotateManager.initBindView(this);
        checkLength();
        xfInfo=new ZAJCXFModle();
    }


    private void checkLength() {
        checkTextLength(et_mhqsl,3);
        checkTextLength(et_mhqzcs,3);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.tv_dqrq:
                new BaseDataUtils().getData(this,tv_dqrq);
                break;

            case R.id.iv_xfqjt :
                i_flag = 1;
                cameraPhoto();
                break;

            case R.id.btn_ok:
                int nu_mhqzcs=et_mhqzcs.getText().toString().isEmpty()  ? 0: Integer.parseInt(et_mhqzcs.getText().toString());
                int nu_mhqsl=et_mhqsl.getText().toString().isEmpty()  ? 0: Integer.parseInt(et_mhqsl.getText().toString());
                String msg = "";
                if (TextUtils.isEmpty(getMHQSL())){
                    msg =  getResources().getString(R.string.za_mhqsl)+getResources().getString(R.string.info_null) ;
                }else if (TextUtils.isEmpty(getZCS())){
                    msg =  getResources().getString(R.string.za_zcs)+getResources().getString(R.string.info_null) ;
                }else if (nu_mhqzcs > nu_mhqsl){
                    msg = getString(R.string.mhqzcs_tips) ;
                }else if (TextUtils.isEmpty(getDQRQ())){
                    msg =  getResources().getString(R.string.za_dqrq)+getResources().getString(R.string.info_null) ;
                }
                if(!msg.isEmpty()) {
                    BaseUtil.showDialog(msg, this);
                }else {
                    xfInfo.setMHQSL(getMHQSL());
                    xfInfo.setZCS(getZCS());
                    xfInfo.setDQRQ(getDQRQ());
                    xfInfo.setIV_XFQJT(path1);
                    Intent resultIntent = new Intent();
                    resultIntent.putExtra("xfInfo",xfInfo);
                    setResult(2, resultIntent);
                    ZAJCXFInfoAty.this.finish();
                }
                break;
        }

    }

    @Override
    public int getXFID() {
        return 0;
    }

    @Override
    public String getMHQSL() {
        return et_mhqsl.getText().toString().trim();
    }

    @Override
    public String getZCS() {
        return et_mhqzcs.getText().toString().trim();
    }

    @Override
    public String getDQRQ() {
        return tv_dqrq.getText().toString().trim();
    }

    @Override
    public String getIV_XFQJT() {
        return path1;
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
                iv_xfqjt.setImageBitmap(ImageUtils.compressImageByPixel(imagePath));
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
        return getString(R.string.zaxf);
    }

    @Override
    public boolean enableBackUpBtn() {
        return true;
    }
}