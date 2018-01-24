/*
 * Copyright (c) 2010, Sony Ericsson Mobile Communication AB. All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without modification, 
 * are permitted provided that the following conditions are met:
 *
 *    * Redistributions of source code must retain the above copyright notice, this 
 *      list of conditions and the following disclaimer.
 *    * Redistributions in binary form must reproduce the above copyright notice,
 *      this list of conditions and the following disclaimer in the documentation
 *      and/or other materials provided with the distribution.
 *    * Neither the name of the Sony Ericsson Mobile Communication AB nor the names
 *      of its contributors may be used to endorse or promote products derived from
 *      this software without specific prior written permission.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" AND
 * ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED 
 * WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE DISCLAIMED.
 * IN NO EVENT SHALL THE COPYRIGHT HOLDER OR CONTRIBUTORS BE LIABLE FOR ANY DIRECT,
 * INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING,
 * BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE,
 * DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF
 * LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE
 * OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED
 * OF THE POSSIBILITY OF SUCH DAMAGE.
 */

package cn.net.xinyi.xmjt.utils.IdCard;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;

import java.util.Observable;
import java.util.Observer;

import cn.net.xinyi.xmjt.activity.Collection.ZAJC.RecognizeActivity;


//划线识别相关（银行卡等）

/**
 * View capable of drawing an image at different zoom state levels
 */
public class ImageZoomView extends View implements Observer{
    public static final String TAG = "ImageZoomView";
    private final Paint mPaint = new Paint(Paint.FILTER_BITMAP_FLAG);
    private final Rect mRectSrc = new Rect();
    private final Rect mRectDst = new Rect();
    private Bitmap mBitmap;
    private float mAspectQuotient;
    private ImageZoomViewState mZoomState;
    public float mRectHeight;
    public float mRectWidth;
    public float mScreenHeiht;
    public float mScreenWidth;
    private RecognizeActivity.ControlType mControlType = RecognizeActivity.ControlType.ZOOM;
    private float mX;
    private float mY;
    // Public methods
    /**
     * Constructor
     */
    public ImageZoomView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    private void calculateAspectQuotient() {
        if (mBitmap != null) {
            mAspectQuotient = (((float)mBitmap.getWidth()) / mBitmap.getHeight())/ (((float)getWidth()) / getHeight());
        }
    }

    @Override
    protected void onDraw(Canvas canvas) {
        Log.i(TAG, "onDraw");
        if (mBitmap != null && mZoomState != null) {
            final int viewWidth = getWidth();
            final int viewHeight = getHeight();
            final int bitmapWidth = mBitmap.getWidth();
            final int bitmapHeight = mBitmap.getHeight();
            final float panX = mZoomState.getPanX();
            final float panY = mZoomState.getPanY();
            final float zoomX = mZoomState.getZoomX(mAspectQuotient) * viewWidth / bitmapWidth;
            final float zoomY = mZoomState.getZoomY(mAspectQuotient) * viewHeight / bitmapHeight;
            // Setup source and destination rectangles
            mRectSrc.left = (int)(panX * bitmapWidth - viewWidth / (zoomX * 2));
            mRectSrc.top = (int)(panY * bitmapHeight - viewHeight / (zoomY * 2));
            mRectSrc.right = (int)(mRectSrc.left + viewWidth / zoomX);
            mRectSrc.bottom = (int)(mRectSrc.top + viewHeight / zoomY);
            mRectDst.left = getLeft();
            mRectDst.top = getTop();
            mRectDst.right = getRight();
            mRectDst.bottom = getBottom();

            // Adjust source rectangle so that it fits within the source image.
            if (mRectSrc.left < 0) {
                mRectDst.left += -mRectSrc.left * zoomX;
                mRectSrc.left = 0;
            }
            if (mRectSrc.right > bitmapWidth) {
                mRectDst.right -= (mRectSrc.right - bitmapWidth) * zoomX;
                mRectSrc.right = bitmapWidth;
            }
            if (mRectSrc.top < 0) {
                mRectDst.top += -mRectSrc.top * zoomY;
                mRectSrc.top = 0;
            }
            if (mRectSrc.bottom > bitmapHeight) {
                mRectDst.bottom -= (mRectSrc.bottom - bitmapHeight) * zoomY;
                mRectSrc.bottom = bitmapHeight;
            }
            mRectHeight = mRectDst.bottom - mRectDst.top;
            mRectWidth = mRectDst.right - mRectDst.left;
            Log.i(TAG, "mRectHeight="+mRectHeight+" mRectWidth="+mRectWidth);
            canvas.drawBitmap(mBitmap, mRectSrc, mRectDst, mPaint);
        }
    }

    @Override
	public boolean onTouchEvent(MotionEvent event) {
		final int action = event.getAction();
		Log.i(TAG, "onTouchEvent action" + action);
		final float x = event.getX();
		final float y = event.getY();
		switch (action) {
		case MotionEvent.ACTION_DOWN:
			mX = x;
			mY = y;
			break;
		case MotionEvent.ACTION_MOVE: {
			final float dx = (x - mX) / mRectWidth;
			final float dy = (y - mY) / mRectHeight;
			if (mControlType == RecognizeActivity.ControlType.ZOOM) {
				mZoomState.setZoom(mZoomState.getZoom() * (float) Math.pow(20, -dy));
				mZoomState.notifyObservers();
			} else {
				mZoomState.setPanX(mZoomState.getPanX() - dx);
				mZoomState.setPanY(mZoomState.getPanY() - dy);
				mZoomState.notifyObservers();
			}
			mX = x;
			mY = y;
			break;
		}
		}
		return true;
	}

	@Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        super.onLayout(changed, left, top, right, bottom);
        Log.i(TAG, "onLayout");
        calculateAspectQuotient();
    }

    // implements Observer
    @Override
    public void update(Observable observable, Object data) {
        Log.i(TAG, "update");
        invalidate();
    }
    
    public void setImage(Bitmap bitmap) {
        mBitmap = bitmap;
        calculateAspectQuotient();
        invalidate();
    }

    public void setZoomState(ImageZoomViewState state) {
        if (mZoomState != null) {
            mZoomState.deleteObserver(this);
        }
        mZoomState = state;
        mZoomState.addObserver(this);
        invalidate();
    }
    
    public void setControlType(RecognizeActivity.ControlType controlType) {
        mControlType = controlType;
    }
    
    public void addObserver(){
    	mZoomState.addObserver(this);
    }
    
    public void removeObserver(){
    	mZoomState.deleteObserver(this);
    }
    
    public Rect getmRectSrc() {
        return mRectSrc;
    }

    public Rect getmRectDst() {
        return mRectDst;
    }

    public float getmRectHeight() {
        return mRectHeight;
    }

    public void setmRectHeight(float mRectHeight) {
        this.mRectHeight = mRectHeight;
    }

    public float getmRectWidth() {
        return mRectWidth;
    }

    public void setmRectWidth(float mRectWidth) {
        this.mRectWidth = mRectWidth;
    }

    public float getmScreenHeiht() {
        return mScreenHeiht;
    }

    public void setmScreenHeiht(float mScreenHeiht) {
        this.mScreenHeiht = mScreenHeiht;
    }

    public float getmScreenWidth() {
        return mScreenWidth;
    }

    public void setmScreenWidth(float mScreenWidth) {
        this.mScreenWidth = mScreenWidth;
    }

}
