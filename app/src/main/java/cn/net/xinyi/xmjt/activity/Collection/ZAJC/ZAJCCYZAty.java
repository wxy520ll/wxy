package cn.net.xinyi.xmjt.activity.Collection.ZAJC;


import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.hardware.Camera;
import android.os.Bundle;
import android.os.Environment;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;

import cn.net.xinyi.xmjt.R;
import cn.net.xinyi.xmjt.config.BaseActivity2;
import cn.net.xinyi.xmjt.model.View.IZACYZView;
import cn.net.xinyi.xmjt.model.ZAJCCYZModle;
import cn.net.xinyi.xmjt.utils.AnnotateManager;
import cn.net.xinyi.xmjt.utils.BaseDataUtils;
import cn.net.xinyi.xmjt.utils.BaseUtil;
import cn.net.xinyi.xmjt.utils.BindView;
import cn.net.xinyi.xmjt.utils.ImageUtils;

/**
 * Created by hao.zhou on 2016/3/19.
 */
public class ZAJCCYZAty extends BaseActivity2 implements IZACYZView,View.OnClickListener {
    /***从业者姓名*/
    @BindView(id = R.id.et_cyzxm,click = false)
    private EditText et_cyzxm;
    /***扫描证件*/
    @BindView(id = R.id.tv_smzj,click = true)
    private TextView tv_smzj;
    /***从业者身份证*/
    @BindView(id = R.id.et_cyzsfz,click = false)
    private EditText et_cyzsfz;
    /***从业者电话*/
    @BindView(id = R.id.et_cyzdh,click = false)
    private EditText et_cyzdh;
    /***从业者照片*/
    @BindView(id = R.id.iv_cyz,click = true)
    private ImageView iv_cyz;
    /***保存*/
    @BindView(id = R.id.btn_ok,click = true)
    private Button btn_ok;
    private  String path1;
    private ZAJCCYZModle cyzInfo;
    /***ImageView拍照标识*/
    private int i_flag;
    private boolean isCatchPreview = false;
    private boolean isCatchPicture = false;
    private int WIDTH;
    private int HEIGHT;
    private int srcwidth;
    private int srcheight;
    int nMainID = 0;
    public static final String UPDATE_ACTION = "update_action";

    BroadcastReceiver updateReceiver = new BroadcastReceiver() {

        @Override
        public void onReceive(Context context, Intent intent) {
            String[] fieldvalue= (String[]) intent.getSerializableExtra("fieldvalue");
            et_cyzxm.setText(fieldvalue[1]);
            et_cyzsfz.setText(fieldvalue[6]);
        }
    };

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unregisterReceiver(updateReceiver);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.aty_zacyz);
        getActionBar().setIcon(R.drawable.actionbar_back_icon_normal);
        getActionBar().setTitle(getString(R.string.zacyz));
        getActionBar().setHomeButtonEnabled(true);
        /***注解式绑定控件**/
        AnnotateManager.initBindView(this);
        checkCameraParameters();
        nMainID = readMainID();
        cyzInfo=new ZAJCCYZModle();
        checkLength();
        IntentFilter filter = new IntentFilter(UPDATE_ACTION);
        registerReceiver(updateReceiver, filter);

    }

    private void checkLength() {
        checkTextLength(et_cyzxm,15);
        checkTextLength(et_cyzsfz,18);
        checkTextLength(et_cyzdh,15);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){

            case R.id.iv_cyz:
                if (BaseDataUtils.isFastClick()){
                    break;
                }
                i_flag = 1;
                cameraPhoto();
                break;

            case R.id.tv_smzj:
                if (BaseDataUtils.isFastClick()){
                    break;
                }
                if (isCatchPreview == true && isCatchPicture == true) {
                    Intent intent = new Intent();
                    intent.setClass(this, CameraActivity.class);
                    writePreferences("", "WIDTH", WIDTH);
                    writePreferences("", "HEIGHT", HEIGHT);
                    writePreferences("", "srcwidth", srcwidth);
                    writePreferences("", "srcheight", srcheight);
                    writePreferences("", "isAutoTakePic", 0);
                    intent.putExtra("WIDTH", WIDTH);
                    intent.putExtra("HEIGHT", HEIGHT);
                    intent.putExtra("srcwidth", srcwidth);
                    intent.putExtra("srcheight", srcheight);
                    intent.putExtra("nMainID", nMainID);
                    startActivity(intent);
                }
                break;

            case R.id.btn_ok:
                if (BaseDataUtils.isFastClick()){
                    break;
                }
                String msg = "";
                if (TextUtils.isEmpty(getCYZXM())){
                    msg =  getResources().getString(R.string.net_cyzxm)+getResources().getString(R.string.info_null) ;
                }else if (getCYZSFZ().length()<12){
                    msg = "请输入正确的身份证号码！" ;
                }
                if(!msg.isEmpty()) {
                    BaseUtil.showDialog(msg, this);
                }else {
                    cyzInfo.setCYZXM(getCYZXM());
                    cyzInfo.setCYZSFZ(getCYZSFZ());
                    cyzInfo.setCYZDH(getCYZDH());
                    cyzInfo.setIV_CYZQSZ(getIV_CYZQSZ());
                    Intent resultIntent = new Intent();
                    resultIntent.putExtra("cyzInfo",cyzInfo);
                    setResult(1, resultIntent);
                    ZAJCCYZAty.this.finish();
                }
                break;
        }
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
                iv_cyz.setImageBitmap(ImageUtils.compressImageByPixel(path1));
            }
        }
    }

    @Override
    public int getCYZID() {
        return 0;
    }

    @Override
    public String getCYZXM() {
        return et_cyzxm.getText().toString().trim();
    }

    @Override
    public String getCYZSFZ() {
        return et_cyzsfz.getText().toString().trim();
    }

    @Override
    public String getCYZDH() {
        return et_cyzdh.getText().toString().trim();
    }

    @Override
    public String getIV_CYZQSZ() {
        return path1;
    }


    protected void writePreferences(String perferencesName, String key,
                                    int value) {
        SharedPreferences preferences = getSharedPreferences(perferencesName,
                MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putInt(key, value);
        editor.commit();
    }
    public void checkCameraParameters() {
        // 读取支持的预览尺寸
        Camera camera = null;
        try {
            camera = Camera.open();
            if (camera != null) {
                // 读取支持的预览尺寸,优先选择640后320
                Camera.Parameters parameters = camera.getParameters();
                List<Integer> SupportedPreviewFormats = parameters
                        .getSupportedPreviewFormats();
                for (int i = 0; i < SupportedPreviewFormats.size(); i++) {
                    System.out.println("PreviewFormats="
                            + SupportedPreviewFormats.get(i));
                }
                Log.i("TAG",
                        "preview-size-values:"
                                + parameters.get("preview-size-values"));
                List<Camera.Size> previewSizes = splitSize(
                        parameters.get("preview-size-values"), camera);// parameters.getSupportedPreviewSizes();

                // 冒泡排序算法 实现分辨率从小到大排列
                // 该算法以分辨率的宽度为准，如果宽度相等，则判断高度
                int tempWidth = 0;
                int tempHeight = 0;
                for (int i = 0; i < previewSizes.size(); i++) {
                    for (int j = i + 1; j < previewSizes.size(); j++) {
                        if (previewSizes.get(i).width > previewSizes.get(j).width) {

                            tempWidth = previewSizes.get(i).width;
                            tempHeight = previewSizes.get(i).height;
                            previewSizes.get(i).width = previewSizes.get(j).width;
                            previewSizes.get(i).height = previewSizes.get(j).height;
                            previewSizes.get(j).width = tempWidth;
                            previewSizes.get(j).height = tempHeight;

                        } else if (previewSizes.get(i).width == previewSizes
                                .get(j).width
                                && previewSizes.get(i).height > previewSizes
                                .get(j).height) {
                            tempWidth = previewSizes.get(i).width;
                            tempHeight = previewSizes.get(i).height;
                            previewSizes.get(i).width = previewSizes.get(j).width;
                            previewSizes.get(i).height = previewSizes.get(j).height;
                            previewSizes.get(j).width = tempWidth;
                            previewSizes.get(j).height = tempHeight;
                        }
                    }
                }
                for (int i = 0; i < previewSizes.size(); i++) {
                    System.out.println("宽度:" + previewSizes.get(i).width + "--"
                            + "高度:" + previewSizes.get(i).height);
                }
                // 冒泡排序算法
                // 该段程序主要目的是为了遵循:优先选择比640*480大的并且是最接近的而且是比例为4:3的原则编写的。
                for (int i = 0; i < previewSizes.size(); i++) {
                    // 当预览宽度和高度分别大于640和480并且宽和高的比为4:3时。
                    if (previewSizes.get(i).width > 640
                            && previewSizes.get(i).height > 480
                            && (((float) previewSizes.get(i).width / previewSizes
                            .get(i).height) == (float) 4 / 3)) {
                        isCatchPreview = true;
                        WIDTH = previewSizes.get(i).width;
                        HEIGHT = previewSizes.get(i).height;
                        break;
                    }
                    // 如果在640*480前没有满足的值，WIDTH和HEIGHT就都为0，然后进行如下判断，看是否有640*480，如果有则赋值，如果没有则进行下一步验证。
                    if (previewSizes.get(i).width == 640
                            && previewSizes.get(i).height == 480 && WIDTH < 640
                            && HEIGHT < 480) {
                        isCatchPreview = true;
                        WIDTH = 640;
                        HEIGHT = 480;
                    }
                    if (previewSizes.get(i).width == 320
                            && previewSizes.get(i).height == 240 && WIDTH < 320
                            && HEIGHT < 240) {// 640 //480
                        isCatchPreview = true;
                        WIDTH = 320;
                        HEIGHT = 240;
                    }
                }
                Log.i("TAG", "isCatchPreview=" + isCatchPreview);

                // 读取支持的相机尺寸,优先选择1280后1600后2048
                List<Integer> SupportedPictureFormats = parameters
                        .getSupportedPictureFormats();
                for (int i = 0; i < SupportedPictureFormats.size(); i++) {
                    System.out.println("PictureFormats="
                            + SupportedPictureFormats.get(i));
                }
                Log.i("TAG",
                        "picture-size-values:"
                                + parameters.get("picture-size-values"));
                List<Camera.Size> PictureSizes = splitSize(
                        parameters.get("picture-size-values"), camera);// parameters.getSupportedPictureSizes();
                for (int i = 0; i < PictureSizes.size(); i++) {
//                    if (PictureSizes.get(i).width == 3264
//                            && PictureSizes.get(i).height == 1840) {
//                        // 优先选择小的照片分辨率
//                        isCatchPicture = true;
//                        srcwidth = 3264;
//                        srcheight = 1840;
//
//                    }
                    if (PictureSizes.get(i).width == 2048
                            && PictureSizes.get(i).height == 1536) {
                        // 优先选择小的照片分辨率
                        if ((srcwidth == 0 && srcheight == 0)
                                || (srcwidth > 2048 && srcheight > 1536)) {
                            isCatchPicture = true;
                            srcwidth = 2048;
                            srcheight = 1536;
                        }

                    }
                    if (PictureSizes.get(i).width == 1600
                            && PictureSizes.get(i).height == 1200) {
                        if ((srcwidth == 0 && srcheight == 0)
                                || (srcwidth > 1600 && srcheight > 1200)) {
                            isCatchPicture = true;
                            srcwidth = 1600;
                            srcheight = 1200;
                        }

                    }
                    if (PictureSizes.get(i).width == 1280
                            && PictureSizes.get(i).height == 960) {
                        if ((srcwidth == 0 && srcheight == 0)
                                || (srcwidth > 1280 && srcheight > 960)) {
                            isCatchPicture = true;
                            srcwidth = 1280;
                            srcheight = 960;
                        }
                    }
                    if (PictureSizes.get(i).width == 640
                            && PictureSizes.get(i).height == 480) {
                        if ((srcwidth == 0 && srcheight == 0)
                                || (srcwidth > 640 && srcheight > 480)) {
                            isCatchPicture = true;
                            srcwidth = 640;
                            srcheight = 480;
                        }
                    }
                }
                Log.i("TAG", "isCatchPicture=" + isCatchPicture);
            }
            camera.release();
            camera = null;
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (camera != null) {
                try {
                    camera.release();
                    camera = null;
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }

    private ArrayList<Camera.Size> splitSize(String str, Camera camera) {
        if (str == null)
            return null;
        StringTokenizer tokenizer = new StringTokenizer(str, ",");
        ArrayList<Camera.Size> sizeList = new ArrayList<Camera.Size>();
        while (tokenizer.hasMoreElements()) {
            Camera.Size size = strToSize(tokenizer.nextToken(), camera);
            if (size != null)
                sizeList.add(size);
        }
        if (sizeList.size() == 0)
            return null;
        return sizeList;
    }
    private Camera.Size strToSize(String str, Camera camera) {
        if (str == null)
            return null;
        int pos = str.indexOf('x');
        if (pos != -1) {
            String width = str.substring(0, pos);
            String height = str.substring(pos + 1);
            return camera.new Size(Integer.parseInt(width),
                    Integer.parseInt(height));
        }
        return null;
    }
    public int readMainID() {
        int mainID = 0;
        String cfgPath = Environment.getExternalStorageDirectory().toString()
                + "/AndroidWT/idcard.cfg";
        File cfgFile = new File(cfgPath);
        char[] buf = new char[14];
        if (!cfgFile.exists()) {
            return 0;
        } else {
            try {
                FileReader fr = new FileReader(cfgFile);
                fr.read(buf);
                String str = String.valueOf(buf);
                String[] splits = str.split("==##");
                mainID = Integer.valueOf(splits[0]);
                Log.i("TAG", "readMainID mainID=" + mainID);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return mainID;
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



}