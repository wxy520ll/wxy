package cn.net.xinyi.xmjt.activity.Collection.ZAJC;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Rect;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.lang.ref.SoftReference;
import java.util.ArrayList;
import java.util.Date;
import java.util.Observable;
import java.util.Observer;

import cn.net.xinyi.xmjt.R;
import cn.net.xinyi.xmjt.activity.Plate.PlateRecogResultActivity;
import cn.net.xinyi.xmjt.utils.IdCard.DrawLineView;
import cn.net.xinyi.xmjt.utils.IdCard.DrawLineViewState;
import cn.net.xinyi.xmjt.utils.IdCard.ImageZoomView;
import cn.net.xinyi.xmjt.utils.IdCard.ImageZoomViewState;

//划线识别Activity（银行卡等）
public class RecognizeActivity extends Activity implements OnClickListener, Observer {
	public static final String TAG = "RecognizeActivity";

	public DisplayMetrics dm;
	public static String picturePath;
	public ImageButton lockButton;
	public ImageButton unlockButton;
	public ImageButton zoomButton;
	public ImageButton moveButton;
	public ImageButton resetButton;
	public ImageButton numberButton;
	public ImageButton nameButton;
	public ImageButton placeButton;
	public DrawLineView drawLineView;
	public ImageZoomView mZoomView;
	public ImageView imageView;
	public Bitmap bitImage;

	public float scWidth;
	public float scHeight;
	public float picWidth;
	public float picHeight;
	public float scX1, scY1, scX2, scY2;
	public float picX1, picY1, picX2, picY2;
	public float ratioX, ratioY;
	public Rect mRectDst;
	public boolean call = false;
	private ImageZoomViewState mZoomState;
	private Bitmap mBitmap;
	public View popuContentView;
	public EditText popuEditText;
	int nTypeInitIDCard = 0;
	public ArrayList<SoftReference<Bitmap>> arrayList = new ArrayList<SoftReference<Bitmap>>();
	public int recogType = 0;
	public int autoRecog = -1;
	public int drawRetRecog = -1;
	public int drawLineRecog = -1;
	public int nMainID = 1100;
	public File countFile;
	public long startTime;
	public long endTime;
	public DrawLineViewState drawLineState;

	public enum ControlType {
		PAN, ZOOM
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		Log.i(TAG, "onCreate");
		super.onCreate(savedInstanceState);
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);// 去掉标题栏
		this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
				WindowManager.LayoutParams.FLAG_FULLSCREEN);// 
		setContentView(R.layout.layout_recognize_activity);
		String[] typeDraw = { getString(R.string.dialog_drawline_recog), getString(R.string.dialog_drawrang_recog) };
		// 获取屏幕大小
		WindowManager manager = getWindowManager();
		scWidth = manager.getDefaultDisplay().getWidth();
		scHeight = manager.getDefaultDisplay().getHeight();
		Intent intent = this.getIntent();
		picturePath = intent.getStringExtra("selectPath");
		recogType = intent.getIntExtra("recogType", 0);
		nMainID = intent.getIntExtra("nMainID", 1100);
		Log.i(TAG, "recogType=" + recogType);
		DrawLineView.recogType = recogType;
		if (picturePath == null) {
			picturePath = intent.getStringExtra("path");
		}
		findViews();
		mBitmap = BitmapFactory.decodeFile(picturePath);
		arrayList.add(new SoftReference<Bitmap>(mBitmap));
		Log.i(TAG, "imageView=" + imageView);
		imageView.setImageBitmap(arrayList.get(0).get());
		drawLineState = new DrawLineViewState();
		mZoomState = new ImageZoomViewState();
		mZoomView.setZoomState(mZoomState);
		mZoomView.setImage(arrayList.get(0).get());
		mZoomView.setmScreenHeiht(scHeight);
		mZoomView.setmScreenWidth(scWidth);
		bitImage = Bitmap.createBitmap((int) scWidth, (int) scHeight, Bitmap.Config.ARGB_8888); // 设置位图，线就画在位图上面，第一二个参数是位图宽和高
		arrayList.add(new SoftReference<Bitmap>(bitImage));
		picWidth = mBitmap.getWidth();
		picHeight = mBitmap.getHeight();
		drawLineView.setBitmap(arrayList.get(1).get());
		drawLineView.setScWidth(scWidth);
		drawLineView.setScHeight(scHeight);
		drawLineView.setPicWidth(picWidth);
		drawLineView.setPicHeight(picHeight);
		drawLineView.setPicturePath(picturePath);
		drawLineView.setDrawLineState(drawLineState);
		drawLineState.addObserver(this);
		Log.i(TAG, "scwidth=" + scWidth + " scheight=" + scHeight);
		Log.i(TAG, "picWidth=" + picWidth + " picHeight=" + picHeight);
		ratioX = picWidth / scWidth;
		ratioY = picHeight / scHeight;
		Log.i(TAG, "ratioX=" + ratioX + " ratioY=" + ratioY);
		resetZoomState();
		drawLineView.setVisibility(View.INVISIBLE);
		drawLineView.clearFocus();
		imageView.setVisibility(View.INVISIBLE);
		imageView.clearFocus();
	}

	public void findViews() {
		imageView = (ImageView) findViewById(R.id.imageview3);
		drawLineView = (DrawLineView) findViewById(R.id.drawline);
		mZoomView = (ImageZoomView) findViewById(R.id.zoomview);
		lockButton = (ImageButton) findViewById(R.id.imagebutton_lock);
		unlockButton = (ImageButton) findViewById(R.id.imagebutton_unlock);
		zoomButton = (ImageButton) findViewById(R.id.imagebutton_zoom);
		moveButton = (ImageButton) findViewById(R.id.imagebutton_move);
		resetButton = (ImageButton) findViewById(R.id.imagebutton_reset);
		numberButton = (ImageButton) findViewById(R.id.imagebutton_number);
		numberButton.setVisibility(View.INVISIBLE);
		// nameButton = (ImageButton) findViewById(R.id.imagebutton_name);
		// placeButton = (ImageButton) findViewById(R.id.imagebutton_place);
		lockButton.setOnClickListener(this);
		unlockButton.setOnClickListener(this);
		zoomButton.setOnClickListener(this);
		moveButton.setOnClickListener(this);
		resetButton.setOnClickListener(this);

		numberButton.setOnClickListener(this);
		// nameButton.setOnClickListener(this);
		// placeButton.setOnClickListener(this);
	}

	private void resetZoomState() {
		mZoomState.setPanX(0.5f);
		mZoomState.setPanY(0.5f);
		mZoomState.setZoom(1f);
		mZoomState.notifyObservers();
	}

	@Override
	public void onClick(View v) {
		Log.i(TAG, "onClick");
		switch (v.getId()) {
		case R.id.imagebutton_unlock:
			Log.i(TAG, "imagebutton_unlock clicked");
			drawLineView.setVisibility(View.INVISIBLE);
			drawLineView.clearFocus();
			drawLineView.setVisibility(View.INVISIBLE);
			imageView.setVisibility(View.INVISIBLE);
			mZoomView.setVisibility(View.VISIBLE);
			mZoomView.addObserver();
			lockButton.setVisibility(View.VISIBLE);
			unlockButton.setVisibility(View.INVISIBLE);
			break;
		case R.id.imagebutton_lock:
			Log.i(TAG, "imagebutton_lock clicked");
			mZoomView.clearFocus();
			drawLineView.setVisibility(View.VISIBLE);
			imageView.setVisibility(View.VISIBLE);
			mZoomView.setVisibility(View.INVISIBLE);
			mZoomView.removeObserver();
			lockButton.setVisibility(View.INVISIBLE);
			unlockButton.setVisibility(View.VISIBLE);
			break;
		case R.id.imagebutton_zoom:
			mZoomView.setControlType(ControlType.ZOOM);
			break;
		case R.id.imagebutton_move:
			mZoomView.setControlType(ControlType.PAN);
			break;
		case R.id.imagebutton_reset:
			resetZoomState();
			mZoomView.clearFocus();
			break;
		case R.id.imagebutton_number:
			nMainID = 1100;// 1014 车辆识别代码
			break;
		default:
		}
	}

	// compress image
	public Bitmap compressImageView(String path) {
		Bitmap bitmap = null;
		try {
			// 取得图片
			InputStream temp = new FileInputStream(path);
			BitmapFactory.Options options = new BitmapFactory.Options();
			options.inJustDecodeBounds = true;
			BitmapFactory.decodeStream(temp, null, options);
			temp.close();
			// 生成压缩的图片
			int i = 0;
			bitmap = null;
			while (true) {
				if ((options.outWidth >> i <= 640) && (options.outHeight >> i <= 480)) {
					temp = new FileInputStream(path);
					options.inSampleSize = (int) Math.pow(2.0D, i);
					options.inJustDecodeBounds = false;
					bitmap = BitmapFactory.decodeStream(temp, null, options);
					break;
				}
				i += 1;
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		return bitmap;
	}

	public void showPopupWindow(String result, String lpFileIn, String lpFileOut, int ReturnRecogIDCard) {
		popuContentView = LayoutInflater.from(RecognizeActivity.this).inflate(R.layout.result, null);
		final PopupWindow popuWindow = new PopupWindow(popuContentView, (int) scWidth, 200, true);
		popuWindow.setContentView(popuContentView);
		// popuWindow.setAnimationStyle()
		ColorDrawable color = new ColorDrawable(Color.BLACK);
		color.setAlpha(60);
		popuWindow.setBackgroundDrawable(color);
		popuWindow.setOutsideTouchable(true);
		popuWindow.setFocusable(true);
		// popuContentView.post(new Runnable() {
		//
		// @Override
		// public void run() {
		// popuWindow.showAtLocation(drawLineView, Gravity.BOTTOM, 100, 0);
		//
		// }
		// });
		popuEditText = (EditText) popuContentView.findViewById(R.id.edit_file);
		ImageView image = (ImageView) popuContentView.findViewById(R.id.imageView1);
		popuEditText.setText(getString(R.string.recog_result) + "\n" + getString(R.string.idcard_type)
				+ ReturnRecogIDCard + "\n" + result);
		Log.i(TAG, "lpFileOut=" + lpFileOut);
		if (null != lpFileOut) {
			image.setImageURI(Uri.fromFile(new File(lpFileOut)));
		} else {
			Toast.makeText(this, getString(R.string.cut_image_failure), Toast.LENGTH_SHORT).show();
		}
		popuWindow.showAtLocation(drawLineView, Gravity.BOTTOM, 100, 0);
		String str = null;
		if (null != lpFileOut) {
			str = lpFileOut.substring(0, lpFileOut.length());
		} else {
			str = lpFileIn.substring(0, lpFileIn.length());
		}
		Log.i(TAG, "str=" + str);
		// boolean successed =
		// RecordResult.getInstance().recordResult("idcard_result.txt", str,
		// "\n" + result);
		Button takePicture = (Button) popuContentView.findViewById(R.id.button3);
		takePicture.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent intent = new Intent(RecognizeActivity.this, CameraActivity.class);
				intent.putExtra("recogType", recogType);
				startActivity(intent);
				finish();
			}
		});

	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		Log.i(TAG, "resultCode=" + resultCode + "\n requestCode=" + requestCode);
		Log.i(TAG, "nMainID=" + nMainID);
		Date endDate = new Date();
		endTime = endDate.getTime();
		long timeCount = endTime - startTime;
		String lpFileOut = null;
		String lpFileIn = null;
		String ReturnLPFileName = null;
		int ReturnRecogIDCard = 0;
		String result = "";
		if (requestCode == 13 && resultCode == RESULT_OK) {
			// 读识别返回值
			int ReturnAuthority = data.getIntExtra("ReturnAuthority", -100000);// 取激活状态
			int ReturnInitIDCard = data.getIntExtra("ReturnInitIDCard", -100000);// 取初始化返回值
			int ReturnLoadImageToMemory = data.getIntExtra("ReturnLoadImageToMemory", -100000);// 取读图像的返回值
			ReturnRecogIDCard = data.getIntExtra("ReturnRecogIDCard", -100000);// 取识别的返回值
			ReturnLPFileName = ReturnLPFileName = data.getStringExtra("ReturnLPFileName");
			lpFileOut = data.getStringExtra("lpFileOut");
			lpFileIn = data.getStringExtra("lpFileIn");
			if (ReturnAuthority == 0 && ReturnInitIDCard == 0 && ReturnLoadImageToMemory == 0 && ReturnRecogIDCard > 0) {
				String[] fieldname = (String[]) data.getSerializableExtra("GetFieldName");
				String[] fieldvalue = (String[]) data.getSerializableExtra("GetRecogResult");
				if (null != fieldname) {
					int count = fieldname.length;
					for (int i = 0; i < count; i++) {
						if (fieldname[i] != null) {
							result += fieldname[i] + ":" + fieldvalue[i] + ";\n";
							// Log.i(TAG, "result="+result);
						}
					}
				}
				showPopupWindow(result, lpFileIn, ReturnLPFileName, ReturnRecogIDCard);
			} else {
				String str = "";
				if (ReturnAuthority == -100000) {
					str = getString(R.string.failure_not_recog) + ReturnAuthority;
				} else if (ReturnAuthority != 0) {
					str = getString(R.string.failure_activate) + ReturnAuthority;
				} else if (ReturnInitIDCard != 0) {
					str = getString(R.string.failure_init) + ReturnInitIDCard;
				} else if (ReturnLoadImageToMemory != 0) {
					if (ReturnLoadImageToMemory == 3) {
						str = getString(R.string.failure_load_reload) + ReturnLoadImageToMemory;
					} else if (ReturnLoadImageToMemory == 1) {
						str = getString(R.string.failure_load_init_retry) + ReturnLoadImageToMemory;
					} else {
						str = getString(R.string.failure_load) + ReturnLoadImageToMemory;
					}
				} else if (ReturnRecogIDCard != 0) {
					str = getString(R.string.failure_recog) + ReturnRecogIDCard;
				}
				showPopupWindow(str, lpFileIn, ReturnLPFileName, ReturnRecogIDCard);
			}
		}

	}

	@Override
	public void update(Observable observable, Object data) {
		Log.i(TAG, "update");
		int[] array = (int[]) data;
		Log.i(TAG, "array[0]=" + array[0] + " array[1]=" + array[1] + " array[2]=" + array[2] + " array[3]=" + array[3]);
		Bundle bundle = new Bundle();
		int nSubID[] = null;// {0x0001};
		bundle.putString("cls", "checkauto.com.IdcardRunner");
		bundle.putInt("nTypeInitIDCard", 0); // 保留，传0即可
		bundle.putString("lpFileName", picturePath);// 指定的图像路径
		bundle.putInt("nTypeLoadImageToMemory", 0);// 0不确定是哪种图像，1可见光图，2红外光图，4紫外光图
		// bundle.putInt("nMainID", 1015); //nMainID证件的主类型。可取值见证件主类型说明
		// bundle.putInt("nMainID", 1011);
		// nMainID可取值：1100 车辆识别代码, 1101 银行卡
		Log.i(TAG, "nMainID=" + nMainID);
		bundle.putInt("nMainID", nMainID);
		bundle.putInt("x1", array[0]);
		bundle.putInt("y1", array[1]);
		bundle.putInt("x2", array[2]);
		bundle.putInt("y2", array[3]);
		bundle.putInt("multiRows", 1);// 多行区域 预留参数 1就是一行
		bundle.putIntArray("nSubID", nSubID); // 保存要识别的证件的子ID，每个证件下面包含的子类型见证件子类型说明。nSubID[0]=null，表示设置主类型为nMainID的所有证件。
		String snString = null;
		try {
			snString = getSn();
		} catch (IOException e) {
			Toast.makeText(getApplicationContext(), R.string.read_file_exception, Toast.LENGTH_LONG).show();
			e.printStackTrace();
		}
		if (snString != null && !snString.equals("")) {
			bundle.putString("sn", snString);
		} else {
			bundle.putString("sn", ""); // 序列号激活方式 XTC41WTNDFSYB29YYQ8SYYY49
		}

		// bundle.putString("datefile", "/mnt/sdcard/wtdate.lsc");

		bundle.putString("devcode", PlateRecogResultActivity.Devcode); // 项目授权所需参数

		// bundle.putString("versionfile", "/mnt/sdcard/wtversion.lsc");
		// bundle.putString("sn", "WTSYQQRWVFUY1IWYYNJVYYW29");
		// bundle.putString("authfile", ""); //文件激活方式
		bundle.putString("logo", null); // logo路径，logo显示在识别等待页面右上角
		bundle.putBoolean("isCut", true); // true如不设置此项默认自动裁切
		bundle.putString("returntype", "withvalue");// 返回值传递方式withvalue带参数的传值方式（新传值方式）
		Intent intentBean = new Intent("wintone.idcard");
		intentBean.putExtras(bundle);
		intentBean.putExtra("nMainID", nMainID);
		Date startDate = new Date();
		startTime = startDate.getTime();
		RecognizeActivity.this.startActivityForResult(intentBean, 13);
	}

	public String getSn() throws IOException {
		String PATH = Environment.getExternalStorageDirectory().toString() + "/AndroidWT";
		File folder = new File(PATH);
		String snString = null;
		if (folder.exists()) {
			String filePATH = PATH + "/idcard.sn";
			File snFile = new File(filePATH);
			if (snFile.exists()) {
				BufferedReader bfReader = new BufferedReader(new FileReader(snFile));
				snString = bfReader.readLine().toUpperCase();
				bfReader.close();
			} else {
				// Toast.makeText(getApplicationContext(),
				// R.string.file_sn_noexist, 3000).show();
			}
		} else {
			// Toast.makeText(getApplicationContext(),
			// R.string.file_wintone_noexist, 3000).show();
		}
		return snString;
	}

	@Override
	protected void onStop() {
		super.onStop();
		drawLineView.clear();
		drawLineView.invalidate();
		System.gc();
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		Log.i(TAG, "onDestroy");
		if (!mBitmap.isRecycled()) {
			mBitmap.recycle();
			System.gc();
		}
		if (!bitImage.isRecycled()) {
			bitImage.recycle();
			System.gc();
		}

	}
}
