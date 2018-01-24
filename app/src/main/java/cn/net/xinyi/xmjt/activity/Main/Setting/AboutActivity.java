package cn.net.xinyi.xmjt.activity.Main.Setting;

import android.content.ClipboardManager;
import android.content.Context;
import android.content.pm.PackageInfo;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import cn.net.xinyi.xmjt.R;
import cn.net.xinyi.xmjt.config.BaseActivity2;
import cn.net.xinyi.xmjt.utils.AndroidShare;
import cn.net.xinyi.xmjt.utils.AnnotateManager;
import cn.net.xinyi.xmjt.utils.BindView;

public class AboutActivity extends BaseActivity2 implements View.OnClickListener{

    @BindView(id = R. id.iv_ercode,click = true)
    ImageView iv_ercode;
    @BindView(id = R. id.txt_SoftNameD)
    TextView appname;
    @BindView(id = R. id.txt_VersionD)
    TextView appversion;
    @BindView(id = R. id.tv_apk)
    TextView tv_apk;
    @BindView(id = R. id.tv_copyapk,click = true)
    TextView tv_copyapk;
    @BindView(id = R. id.tv_pdf)
    TextView tv_pdf;
    @BindView(id = R. id.tv_copypdf,click = true)
    TextView tv_copypdf;
    public static String apkPath="http://219.134.134.156:8088/xsmws-web/xmjt.apk";
    private String pdfPath="http://219.134.134.156:8088/xsmws-web/guide.pdf";
    private String imagPath="http://219.134.134.156:8088/xsmws-web/ercode.png";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.aty_aboutsoft);
        AnnotateManager.initBindView(this);
        initResource();
    }


    private void initResource(){
        tv_apk.setText(apkPath);
        tv_pdf.setText(pdfPath);
        try {
            PackageInfo info = getPackageManager().getPackageInfo(this.getPackageName(), 0);
            appversion.setText(info.versionName);
        }catch (Exception e){
            e.printStackTrace();
        }

    }
    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.iv_ercode:
                AndroidShare as = new AndroidShare(
                        AboutActivity.this,imagPath, imagPath);
                as.show();
                break;

            case R.id.tv_copyapk:
                copyText(apkPath);
                break;

            case R.id.tv_copypdf:
                copyText(pdfPath);
                break;
        }
    }

    private void copyText(String Path) {
        ClipboardManager copy = (ClipboardManager) AboutActivity.this
                .getSystemService(Context.CLIPBOARD_SERVICE);
        copy.setText(Path);
        toast("复制成功");
    }

    @Override
    public boolean enableBackUpBtn() {
        return true;
    }


    @Override
    public String getAtyTitle() {
        return getString(R.string.title_about);
    }


}
