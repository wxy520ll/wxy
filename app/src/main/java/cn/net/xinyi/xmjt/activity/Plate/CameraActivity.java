package cn.net.xinyi.xmjt.activity.Plate;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.FeatureInfo;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.PixelFormat;
import android.hardware.Camera;
import android.hardware.Camera.AutoFocusCallback;
import android.hardware.Camera.PictureCallback;
import android.hardware.Camera.ShutterCallback;
import android.media.ToneGenerator;
import android.os.Build;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.KeyEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.lang.reflect.Method;

import cn.net.xinyi.xmjt.R;
import cn.net.xinyi.xmjt.config.AppContext;

public class CameraActivity extends Activity implements SurfaceHolder.Callback {

    private int preWidth = 320;
    private int preHeight = 240;
    private int picWidth = 2048;
    private int picHeight = 1536;
    private Camera camera;
    private SurfaceView surfaceView;
    private SurfaceHolder surfaceHolder;
    private ToneGenerator tone;
    private ImageView imageView;
    private Bitmap bitmap;
    private RelativeLayout rlyaout;
    private String recogPicPath;
    private int width, height;
    private boolean hasFlashLigth = false;
    private long fastClick = 0;
    private ImageButton backImage, restartImage, takeImage, recogImage,
            lightOn, lightOff;
    private TextView backAndRestartText, takeAndRecogText, lightText;
    private TextView topTextView;
    private String topText = "拍摄标准：在机动车前车牌正前方,手机向下倾斜45度，横向拍摄，"
            + "确保车牌处于红框的正中央，拍摄时要包含机动车牌号、车标、年检标志";
    private ImageView leftImage, rightImage;
    private boolean taking = false;
    private boolean recoging = false;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
//		setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
        DisplayMetrics displayMetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        width = Math.max(displayMetrics.widthPixels,
                displayMetrics.heightPixels);
        height = Math.min(displayMetrics.widthPixels,
                displayMetrics.heightPixels);
        setContentView(R.layout.plate_wintone_camera);
        picWidth = readIntPreferences("PlateService", "picWidth");
        picHeight = readIntPreferences("PlateService", "picHeight");
        preWidth = readIntPreferences("PlateService", "preWidth");
        preHeight = readIntPreferences("PlateService", "preHeight");
        findViewAndLayout();
        surfaceHolder = surfaceView.getHolder();
        surfaceHolder.addCallback(CameraActivity.this);
        surfaceHolder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);
        FeatureInfo[] features = this.getPackageManager()
                .getSystemAvailableFeatures();
        for (FeatureInfo featureInfo : features) {

            if (PackageManager.FEATURE_CAMERA_FLASH.equals(featureInfo.name)) {
                hasFlashLigth = true;
            }
        }
    }

    protected void onStart() {
        super.onStart();
        showFrameImageView();
    }

    private void findViewAndLayout() {
        int layoutWidth = width - ((height * 4) / 3);
        RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(
                layoutWidth, height);
        layoutParams.addRule(RelativeLayout.ALIGN_PARENT_RIGHT,
                RelativeLayout.TRUE);
        layoutParams.addRule(RelativeLayout.ALIGN_PARENT_TOP,
                RelativeLayout.TRUE);
        rlyaout = (RelativeLayout) findViewById(R.id.rlyaout);
        rlyaout.setLayoutParams(layoutParams);

        imageView = (ImageView) findViewById(R.id.BimageView);
        surfaceView = (SurfaceView) findViewById(R.id.surfaceViwe);

        int layout_hieght = (int) (height * 0.12);
        layoutParams = new RelativeLayout.LayoutParams(layout_hieght,
                layout_hieght);
        layoutParams.addRule(RelativeLayout.CENTER_HORIZONTAL,
                RelativeLayout.TRUE);
        layoutParams.addRule(RelativeLayout.ALIGN_PARENT_TOP,
                RelativeLayout.TRUE);
        layoutParams.topMargin = layout_hieght;
        backImage = (ImageButton) findViewById(R.id.backimage);
        backImage.setBackgroundResource(R.drawable.back);
        backImage.setVisibility(View.VISIBLE);
        backImage.setLayoutParams(layoutParams);
        restartImage = (ImageButton) findViewById(R.id.restartimage);
        restartImage.setBackgroundResource(R.drawable.rephotograph);
        restartImage.setLayoutParams(layoutParams);
        restartImage.setVisibility(View.INVISIBLE);
        layoutParams = new RelativeLayout.LayoutParams(
                RelativeLayout.LayoutParams.WRAP_CONTENT,
                RelativeLayout.LayoutParams.WRAP_CONTENT);
        layoutParams.addRule(RelativeLayout.CENTER_HORIZONTAL,
                RelativeLayout.TRUE);
        layoutParams.addRule(RelativeLayout.BELOW, R.id.restartimage);
        backAndRestartText = (TextView) findViewById(R.id.backandrestarttext);
        backAndRestartText.setVisibility(View.VISIBLE);
        backAndRestartText.setText("返回");
        backAndRestartText.setLayoutParams(layoutParams);
        backAndRestartText.setTextColor(Color.BLACK);
        layoutParams = new RelativeLayout.LayoutParams(layout_hieght,
                layout_hieght);
        layoutParams.addRule(RelativeLayout.CENTER_IN_PARENT,
                RelativeLayout.TRUE);
        takeImage = (ImageButton) findViewById(R.id.takeimage);
        takeImage.setBackgroundResource(R.drawable.takepic);
        takeImage.setLayoutParams(layoutParams);
        takeImage.setVisibility(View.VISIBLE);
        recogImage = (ImageButton) findViewById(R.id.recogimage);
        recogImage.setBackgroundResource(R.drawable.recognition);
        recogImage.setLayoutParams(layoutParams);
        recogImage.setVisibility(View.INVISIBLE);

        layoutParams = new RelativeLayout.LayoutParams(
                RelativeLayout.LayoutParams.WRAP_CONTENT,
                RelativeLayout.LayoutParams.WRAP_CONTENT);
        layoutParams.addRule(RelativeLayout.CENTER_HORIZONTAL,
                RelativeLayout.TRUE);
        layoutParams.addRule(RelativeLayout.BELOW, R.id.recogimage);
        takeAndRecogText = (TextView) findViewById(R.id.takeandrecogtext);
        takeAndRecogText.setText("拍照");
        takeAndRecogText.setLayoutParams(layoutParams);
        takeAndRecogText.setVisibility(View.VISIBLE);
        takeAndRecogText.setTextColor(Color.BLACK);


        lightOn = (ImageButton) findViewById(R.id.lightimage1);
        lightOn.setBackgroundResource(R.drawable.light1);
        lightOn.setVisibility(View.VISIBLE);
        lightOff = (ImageButton) findViewById(R.id.lightimage2);
        lightOff.setBackgroundResource(R.drawable.light2);
        lightOff.setVisibility(View.INVISIBLE);

        layoutParams = new RelativeLayout.LayoutParams(
                RelativeLayout.LayoutParams.WRAP_CONTENT,
                RelativeLayout.LayoutParams.WRAP_CONTENT);
        layoutParams.addRule(RelativeLayout.CENTER_HORIZONTAL,
                RelativeLayout.TRUE);
        layoutParams.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM,
                RelativeLayout.TRUE);
        layoutParams.bottomMargin = layout_hieght;
        lightText = (TextView) findViewById(R.id.lighttext);
        lightText.setText("开闪光灯");
        lightText.setLayoutParams(layoutParams);
        lightText.setVisibility(View.VISIBLE);
        lightText.setTextColor(Color.BLACK);
        layoutParams = new RelativeLayout.LayoutParams(layout_hieght,
                layout_hieght);
        layoutParams.addRule(RelativeLayout.CENTER_HORIZONTAL,
                RelativeLayout.TRUE);
        layoutParams.addRule(RelativeLayout.ABOVE, R.id.lighttext);
        lightOn.setLayoutParams(layoutParams);
        lightOff.setLayoutParams(layoutParams);

        leftImage = (ImageView) findViewById(R.id.cameraframeleft);
        leftImage.setBackgroundResource(R.drawable.left_frame_min);
        rightImage = (ImageView) findViewById(R.id.cameraframeright);
        rightImage.setBackgroundResource(R.drawable.right_frame_min);

        //浮动文字提示
        topTextView = (TextView) findViewById(R.id.topTextView);
        topTextView.setText(topText);

        int surface_width = (height * 4) / 3;
        int pic_width = readIntPreferences("PlateService", "picWidth");
        int frame_width = (160 * surface_width) / pic_width;
        if (frame_width <= (int) (height * 0.16)) {
            frame_width = (int) (height * 0.16 + 80);
        } else {
            frame_width = frame_width + 40;
        }
        int margin = (surface_width - frame_width) / 2 - 60;

        RelativeLayout.LayoutParams frame_left = new RelativeLayout.LayoutParams(
                (int) (height * 0.06), (int) (height * 0.18));
        //frame_left.addRule(RelativeLayout.CENTER_VERTICAL, RelativeLayout.TRUE);
        frame_left.addRule(RelativeLayout.ALIGN_PARENT_LEFT,
                RelativeLayout.TRUE);
        frame_left.topMargin = (int) (height * 0.5);
        frame_left.leftMargin = margin;
        leftImage.setLayoutParams(frame_left);

        RelativeLayout.LayoutParams frame_right = new RelativeLayout.LayoutParams(
                (int) (height * 0.06), (int) (height * 0.18));
        //frame_right.addRule(RelativeLayout.CENTER_VERTICAL, RelativeLayout.TRUE);
        frame_right.addRule(RelativeLayout.LEFT_OF, R.id.rlyaout);
        frame_right.topMargin = (int) (height * 0.5);
        frame_right.rightMargin = margin;
        rightImage.setLayoutParams(frame_right);

        RelativeLayout.LayoutParams frame_top = new RelativeLayout.LayoutParams(
                (int) (width * 0.75), 340);
        frame_top.addRule(RelativeLayout.CENTER_VERTICAL, RelativeLayout.TRUE);
        frame_top.addRule(RelativeLayout.ALIGN_PARENT_TOP,
                RelativeLayout.TRUE);
        frame_top.topMargin = 10;
        frame_top.leftMargin = 10;
        topTextView.setLayoutParams(frame_top);

        showFrameImageView();

        backImage.setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
                freeCamera();
                CameraActivity.this.finish();
            }
        });

        restartImage.setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
                showFrameImageView();
                takeImage.setVisibility(View.VISIBLE);
                recogImage.setVisibility(View.INVISIBLE);
                restartImage.setVisibility(View.INVISIBLE);
                backImage.setVisibility(View.VISIBLE);
                backAndRestartText.setText("返回");
                takeAndRecogText.setText("拍照");
                if (bitmap != null) {
                    if (!bitmap.isRecycled()) {
                        bitmap.recycle();
                    }
                    bitmap = null;
                }
                imageView.setVisibility(View.INVISIBLE);
                imageView.setImageDrawable(null);
                camera.startPreview();
            }
        });

        takeImage.setOnClickListener(new OnClickListener() {
            public void onClick(View v) {

//				Configuration mConfiguration = CameraActivity.this.getResources().getConfiguration(); //获取设置的配置信息
//				int ori = mConfiguration.orientation ; //获取屏幕方向
//
//				if(ori == mConfiguration.ORIENTATION_PORTRAIT){
//					BaseUtil.showDialog("请横向拍照",CameraActivity.this);
//					return;
//				}

//				if(width>height){
//					BaseUtil.showDialog("请横向拍照",CameraActivity.this);
//					return;
//				}

                if (!taking) {
                    taking = true;
                    takePicture();

                } else {
                    System.out.println("正在拍照");
                }
            }
        });

        recogImage.setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
                if (recoging) {
                    System.out.println("正在保存");
                } else {
                    saveAndRecogPic();
                }
            }
        });

        lightOn.setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
                lightText.setText("关闪光灯");
                lightOn.setVisibility(View.INVISIBLE);
                lightOff.setVisibility(View.VISIBLE);
                if (hasFlashLigth) {
                    openFlahsLight();
                }

            }
        });

        lightOff.setOnClickListener(new OnClickListener() {

            public void onClick(View v) {
                lightText.setText("开闪光灯");
                lightOn.setVisibility(View.VISIBLE);
                lightOff.setVisibility(View.INVISIBLE);
                if (hasFlashLigth) {
                    closeFlashLigth();
                }
            }
        });

    }

    public void freeCamera() {
        try {
            if (camera != null) {
                camera.cancelAutoFocus();
                camera.setPreviewCallback(null);
                camera.stopPreview();
                camera.release();
                camera = null;
            }
        } catch (Exception camera_exception) {
            if (AppContext.mCamera != null) {
                AppContext.mCamera.release();
                AppContext.mCamera = null;
            }
        }

        if (bitmap != null) {
            if (!bitmap.isRecycled()) {
                bitmap.recycle();
            }
            bitmap = null;
        }
        imageView.setImageDrawable(null);
    }

    protected int readIntPreferences(String perferencesName, String key) {
        SharedPreferences preferences = getSharedPreferences(perferencesName,
                MODE_PRIVATE);
        int result = preferences.getInt(key, 0);
        return result;
    }

    private void showFrameImageView() {
        leftImage.setVisibility(View.VISIBLE);
        rightImage.setVisibility(View.VISIBLE);
        topTextView.setVisibility(View.VISIBLE);
    }

    private void hideFrameImageView() {
        leftImage.setVisibility(View.INVISIBLE);
        rightImage.setVisibility(View.INVISIBLE);
        topTextView.setVisibility(View.INVISIBLE);
    }

    public boolean isEffectClick() {
        long lastClick = System.currentTimeMillis();
        long diffTime = lastClick - fastClick;
        if (diffTime > 5000) {
            fastClick = lastClick;
            return true;
        }
        return false;
    }

    public void openFlahsLight() {
        if (camera != null) {
            Camera.Parameters parameters = camera.getParameters();
            parameters.setFlashMode(Camera.Parameters.FLASH_MODE_TORCH);
            camera.setParameters(parameters);
            camera.autoFocus(new AutoFocusCallback() {
                public void onAutoFocus(boolean success, Camera camera) {
                }
            });
            camera.startPreview();
        }

    }

    public void closeFlashLigth() {
        if (camera != null) {
            camera.stopPreview();
            Camera.Parameters parameters = camera.getParameters();
            parameters.setFlashMode(Camera.Parameters.FLASH_MODE_OFF);
            camera.setParameters(parameters);
            camera.startPreview();

        }
    }

    private void saveAndRecogPic() {
        recoging = true;
        try {
            recogPicPath = ((AppContext) getApplication()).getCaptureImagePath(0);
            File file = new File(recogPicPath);
            if (file.exists()) {
                file.delete();
            }
            file.createNewFile();
            BufferedOutputStream bos = new BufferedOutputStream(
                    new FileOutputStream(file));
            bitmap.compress(Bitmap.CompressFormat.JPEG, 25, bos);
            bos.flush();
            bos.close();
        } catch (Exception e) {
            recogPicPath = "";
        } finally {
            bitmap.recycle();
            bitmap = null;
            System.gc();
        }

        Intent intent = new Intent(CameraActivity.this,
                PlateRecogResultActivity.class);
        intent.putExtra(PlateListActivity.KEY_PLATE_IMAGE, recogPicPath);
        this.startActivity(intent);
        // this.finish();
    }

    public void takePicture() {
        if (camera != null) {
            try {
                camera.autoFocus(new AutoFocusCallback() {
                    public void onAutoFocus(boolean success, Camera camera) {
                        camera.takePicture(shutterCallback, null, PictureCallback);
                    }
                });
            } catch (Exception e) {
                e.printStackTrace();
                Toast.makeText(this, "相机没有开启", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private PictureCallback PictureCallback = new PictureCallback() {
        public void onPictureTaken(byte[] data, Camera camera) {
            camera.stopPreview();
            if (bitmap != null) {
                if (!bitmap.isRecycled()) {
                    bitmap.recycle();
                }
                bitmap = null;
            }
            BitmapFactory.Options opts = new BitmapFactory.Options();
            opts.inInputShareable = true;
            opts.inPurgeable = true;
            bitmap = BitmapFactory.decodeByteArray(data, 0, data.length, opts);
            imageView.setImageBitmap(bitmap);

            hideFrameImageView();
            taking = false;
            saveAndRecogPic();
        }
    };

    private ShutterCallback shutterCallback = new ShutterCallback() {
        public void onShutter() {
            try {
                if (tone == null) {
                    tone = new ToneGenerator(1, ToneGenerator.MIN_VOLUME);
                }
                tone.startTone(ToneGenerator.TONE_PROP_BEEP);
            } catch (Exception e) {
                e.printStackTrace();
            }

        }
    };

    @Override
    public void finish() {
        try {
            if (camera != null) {
                camera.release();
                camera = null;
            }
        } catch (Exception camera_exception) {
            if (AppContext.mCamera != null) {
                AppContext.mCamera.release();
                AppContext.mCamera = null;
            }
        }

        if (bitmap != null) {
            if (!bitmap.isRecycled()) {
                bitmap.recycle();
            }
            bitmap = null;
        }
        super.finish();

    }

    public void surfaceCreated(SurfaceHolder holder) {
        if (camera == null) {
            try {
                camera = Camera.open();
                AppContext.mCamera = camera;
            } catch (Exception e) {
                if (AppContext.mCamera != null) {
                    AppContext.mCamera.release();
                    camera = Camera.open();
                    AppContext.mCamera = camera;
                }
            }
        }
    }

    public void surfaceChanged(SurfaceHolder holder, int format, int width,
                               int height) {
        if (camera != null) {
            try {
                Camera.Parameters parameters = camera.getParameters();
                parameters.setPictureFormat(PixelFormat.JPEG);
                parameters.setPreviewSize(preWidth, preHeight);
                parameters.setPictureSize(picWidth, picHeight);

                /**
                 * 解决部分机型拍照 自动旋转的问题
                 * */

                // 判断系统版本是否大于等于2.2
                if (Integer.parseInt(Build.VERSION.SDK) >= 8) {
                    // 旋转90，前提是当前页portrait，纵向
                    setDisplayOrientation(camera, 90);
                    Log.e("a", "cc");
                    // 系统版本在2.2以下的采用下面的方式旋转
                } else {
                    if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT) {
                        parameters.set("orientation", "portrait");
                        parameters.set("rotation", 90);
                    }
                    if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE) {
                        parameters.set("orientation", "landscape");
                        parameters.set("rotation", 90);
                    }
                    Log.e("c", "dd");
                }

                camera.setParameters(parameters);
                camera.setPreviewDisplay(holder);
                camera.setDisplayOrientation(0);
                camera.startPreview();
            } catch (Exception e) {
                e.printStackTrace();
                Toast.makeText(getApplicationContext(), "相机没有开启", Toast.LENGTH_SHORT).show();
            }
        }

    }


    public void surfaceDestroyed(SurfaceHolder holder) {
        try {
            if (camera != null) {
                camera.cancelAutoFocus();
                camera.setPreviewCallback(null);
                camera.stopPreview();
                camera.release();
                camera = null;
            }
        } catch (Exception camera_exception) {
            if (AppContext.mCamera != null) {
                AppContext.mCamera.release();
                AppContext.mCamera = null;
            }
        }
    }

    //设置拍照的方向
    protected void setDisplayOrientation(Camera camera, int angle) {

        Method downPolymorphic;

        try {

            downPolymorphic = camera.getClass().getMethod(

                    "setDisplayOrientation", int.class);

            if (downPolymorphic != null)

                downPolymorphic.invoke(camera, angle);

        } catch (Exception e1) {

            e1.printStackTrace();

        }
    }

//	@Override
//	public void onConfigurationChanged(Configuration newConfig)
//	{
//		super.onConfigurationChanged(newConfig);
//		if (this.getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE)
//		{
//			int temp = width;
//			width = height;
//			height = temp;
//		}
//		else if (this.getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT)
//		{
//			int temp = height;
//			height = width;
//			width = temp;
//		}
//	}

    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0) {
            freeCamera();
            showPlateList();
        }
        return super.onKeyDown(keyCode, event);
    }


    private void showPlateList() {
        this.finish();
    }

}
