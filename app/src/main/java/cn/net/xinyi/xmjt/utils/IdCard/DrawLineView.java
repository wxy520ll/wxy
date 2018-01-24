package cn.net.xinyi.xmjt.utils.IdCard;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Paint.Style;
import android.graphics.PorterDuff.Mode;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import java.util.Observable;
import java.util.Observer;

//划线识别相关（银行卡等）
public class DrawLineView extends View implements Observer{
    public static final String TAG = "DrawLineView";
	public Canvas canvas;
	private Bitmap bitmap;
	public Paint paint;
	public float x1,y1,x2,y2;
	public float rectX1,rectY1,rectX2,rectY2;
	public float scWidth;//屏幕宽度
	public float scHeight;//屏幕高度
	public float picWidth;//图像宽度
	public float picHeight;//图像高度
	public float scX1,scY1,scX2,scY2;//屏幕坐标
    public float picX1,picY1,picX2,picY2;//图像坐标
    public float ratioX, ratioY;//屏幕与图像坐标轴的比例
    public ImageView imageView;
    public ImageZoomView mZoomView;
    public String result;
    public static int recogType=-1;//1代表自动识别，2代表划框识别，3代表划线识别
	public String picturePath;
	public int bgColor;
	public View popuContentView;
	public Button search;
	public Button send;
	//识别参数
    String cls;
    int nTypeInitIDCard  = 0;
    int nTypeLoadImageToMemory = 0;
    int nMainID=1015;
    int[] nSubID;
    boolean GetSubID = true;
    String lpHeadFileName;
    String lpFileName;
    boolean GetVersionInfo;
    String sn;
    String logo;
    boolean isCut = true;
    String authfile;
    String userdata;
    String strtimelog = "";
//    public IDCardInit.MyBinder binder;
    public Integer lock = 0;
    // modify 2013.2.26 划线识别
    private int[] array;
    public DrawLineViewState drawLineState;

	public DrawLineView(Context context, AttributeSet attrs) {
	    super(context, attrs);
	    Log.i(TAG, "DrawRectangleView created");
	    setFocusable(true);
	    bgColor = Color.GRAY;               //设置背景颜色
	    canvas=new Canvas();         
	   
	    paint = new Paint(Paint.DITHER_FLAG);
	    paint.setAntiAlias(true);                //设置抗锯齿，一般设为true
	    paint.setColor(Color.GREEN);              //设置线的颜色
	    paint.setStrokeCap(Paint.Cap.SQUARE);     //设置线的类型
	    paint.setStrokeWidth(2);                //设置线的宽度
	    paint.setStyle(Style.STROKE);
	}
	
	public DrawLineView(Context context) {
		super(context);
		Log.i(TAG, "DrawRectangleView");
	}
	
	//触摸事件
	@Override
	public boolean onTouchEvent(MotionEvent event) {
		Log.i(TAG, "recogType="+recogType);
	    if (event.getAction() == MotionEvent.ACTION_MOVE && recogType == 1) { //自动识别
	        Log.i(TAG, "ACTION_MOVE");
            clear();
            rectX2 = event.getRawX();
            rectY2 = event.getRawY();
            Rect rect = new Rect((int)rectX1, (int)rectY1, (int)rectX2, (int)rectY2);
            canvas.drawRect(rect, paint);
            invalidate();
	    }
		if (event.getAction() == MotionEvent.ACTION_MOVE && recogType == 2) { //拖动屏幕 划框识别
		    Log.i(TAG, "ACTION_MOVE");
		    clear();
		    rectX2 = event.getRawX();
		    rectY2 = event.getRawY();
		    Rect rect = new Rect((int)rectX1, (int)rectY1, (int)rectX2, (int)rectY2);
            canvas.drawRect(rect, paint);
            invalidate();
		}
		
		if (event.getAction() == MotionEvent.ACTION_MOVE && recogType == 3) { //拖动屏幕 划线识别
		    Log.i(TAG, "ACTION_MOVE");
		    clear();
            rectX2=event.getRawX();
            rectY2=event.getRawY();
            canvas.drawLine(rectX1, rectY1, rectX2, rectY2, paint);
            invalidate();
        }
		
		if (event.getAction() == MotionEvent.ACTION_DOWN) {    //按下屏幕
		    Log.i(TAG, "scWidth="+scWidth+" scHeight="+scHeight+" picturePath="+picturePath);
		    clear();
		    Log.i(TAG, "picWidth="+picWidth+" picHeight="+picHeight);
		    //ratioX = picWidth/scWidth;
	        //ratioY = picHeight/scHeight;
			x1 = event.getRawX();				
			y1 = event.getRawY();
			rectX1 = x1;
			rectY1 = y1;
			canvas.drawPoint(x1, y1, paint);                //画点
			Log.i(TAG, "ACTION_DOWN"+"  x1="+x1+" y1="+y1+" x2="+x2+" y2="+y2);
			invalidate();
		}
		
		if (event.getAction() == MotionEvent.ACTION_UP) {    //松开屏幕
		    x2 = event.getX();                
            y2 = event.getY();
            Log.i(TAG, "ACTION_UP"+"  x1="+x1+" y1="+y1+" x2="+x2+" y2="+y2);
            calculatePoint();
            Log.i(TAG, "array[0]="+array[0]+" array[1]="+array[1]+" array[2]="+array[2]+" array[3]="+array[3]);
			drawLineState.setX1(x1);
			drawLineState.setY1(y1);
			drawLineState.setX2(x2);
			drawLineState.setY2(y2);
			drawLineState.notifyObservers(array);
            invalidate();
		}
		return true;
	}
	
	
	@Override
	public void onDraw(Canvas c) {
//	    Log.i(TAG, "onDraw");
	    c.drawBitmap(bitmap, 0, 0, null);
	    invalidate();
	}
	
    public void clear() {
        paint.setXfermode(new PorterDuffXfermode(Mode.CLEAR));
        canvas.drawPaint(paint);
        paint.setXfermode(new PorterDuffXfermode(Mode.SRC));
        invalidate();
    }
    
    public int[] calculatePoint(){
        array = new int[4];
        float rationPS = (picWidth*scHeight)/(scWidth*picHeight);
        Log.i(TAG, "rationPS="+rationPS);
        float picX1 = (picWidth*x1)/scWidth;
//        float picX1 = ((mZoomView.getWidth()*x1)/(x1-(scWidth-mZoomView.getWidth())/2));
        float picY1 = (picHeight*y1)/(scHeight);
        float picX2 = (picWidth*x2)/scWidth;
//        float picX2 = ((mZoomView.getWidth()*x2)/(x2-(scWidth-mZoomView.getWidth())/2));
        float picY2 = (picHeight*y2)/(scHeight);
        array[0] = (int)picX1;
        array[1] = (int)picY1;
        array[2] = (int)picX2;
        array[3] = (int)picY2;
        Log.i(TAG, "array[0]="+array[0]+" array[1]="+array[1]+" array[2]="+array[2]+" array[3]="+array[3]);
        return array;
    }
    
    public float getX1() {
        return x1;
    }
    
    public float getY1() {
        return y1;
    }
    
    public float getX2() {
        return x2;
    }
    
    public float getY2() {
        return y2;
    }

    public float getScWidth() {
        return scWidth;
    }

    public void setScWidth(float scWidth) {
        this.scWidth = scWidth;
    }

    public float getScHeight() {
        return scHeight;
    }

    public void setScHeight(float scHeight) {
        this.scHeight = scHeight;
    }

    public String getPicturePath() {
        return picturePath;
    }

    public void setPicturePath(String picturePath) {
        this.picturePath = picturePath;
        lpFileName = picturePath;
    }

    public Bitmap getBitmap() {
        return bitmap;
    }

    public void setBitmap(Bitmap bitmap) {
        canvas.setBitmap(bitmap);
        canvas.drawColor(Color.TRANSPARENT, Mode.CLEAR); //设置为透明，画布也是透
        this.bitmap = bitmap;
    }
    
    public float getPicWidth() {
        return picWidth;
    }

    public void setPicWidth(float picWidth) {
        this.picWidth = picWidth;
    }

    public float getPicHeight() {
        return picHeight;
    }

    public void setPicHeight(float picHeight) {
        this.picHeight = picHeight;
    }

    public static int getRecogType() {
        return recogType;
    }

    public static void setRecogType(int recogType) {
        DrawLineView.recogType = recogType;
    }
    
    public ImageZoomView getmZoomView() {
        return mZoomView;
    }

    public void setmZoomView(ImageZoomView mZoomView) {
        this.mZoomView = mZoomView;
    }
    
    public DrawLineViewState getDrawLineState() {
		return drawLineState;
	}

	public void setDrawLineState(DrawLineViewState drawLineState) {
		this.drawLineState = drawLineState;
	}

	@Override
    public void update(Observable observable, Object data) {
        Log.i(TAG, "update");
    }
 }
