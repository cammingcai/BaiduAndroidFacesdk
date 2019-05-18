/**
 * Copyright (C) 2017 Baidu Inc. All rights reserved.
 */
package com.baidu.idl.face.platform.ui.widget;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.DashPathEffect;
import android.graphics.Paint;
import android.graphics.PathEffect;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;

import com.baidu.idl.face.platform.utils.DensityUtils;

/**
 * 人脸检测区域View
 */
public class FaceDetectRoundView extends View {

    private static final String TAG = FaceDetectRoundView.class.getSimpleName();

    public static final float SURFACE_HEIGHT = 1000f;

    //相机显示区域缩放大小
    public static final float SURFACE_RATIO = 1.0f;

    public static final float WIDTH_SPACE_RATIO = 0.33f;
    public static final float HEIGHT_RATIO = 0.1f;
    public static final float HEIGHT_EXT_RATIO = 0.4f;
    public static final int CIRCLE_SPACE = 5;
    public static final int PATH_SPACE = 16;
    public static final int PATH_SMALL_SPACE = 12;
    public static final int PATH_WIDTH = 4;

    public static final int COLOR_BG = Color.parseColor("#2F2F33");
    public static final int COLOR_RECT = Color.parseColor("#FFFFFF");
    public static final int COLOR_ROUND = Color.parseColor("#FFA800");

    public static final int COLOR_RECT_RED = Color.parseColor("#FF0000");
    private PathEffect mFaceRoundPathEffect = null;
    // new DashPathEffect(new float[]{PATH_SPACE, PATH_SPACE}, 1);
    private Paint mBGPaint;
    private Paint mPathPaint;
    private Paint mFaceRectPaint;
    private Paint mFaceRoundPaint;
    private Rect mFaceRect;
    private Rect mFaceDetectRect;

    private Paint mFaceRectPaint2;

    private float mX;
    private float mY;
    private float mR;
    private boolean mIsDrawDash = true;

    public FaceDetectRoundView(Context context, AttributeSet attrs) {
        super(context, attrs);

        setLayerType(View.LAYER_TYPE_SOFTWARE, null);

        DisplayMetrics dm = context.getResources().getDisplayMetrics();
        float pathSpace = DensityUtils.dip2px(context, PATH_SPACE);
        float pathSmallSpace = DensityUtils.dip2px(context, PATH_SMALL_SPACE);
        float pathWidth = DensityUtils.dip2px(context, PATH_WIDTH);
        mFaceRoundPathEffect = new DashPathEffect(
                new float[]{pathSpace, dm.heightPixels < SURFACE_HEIGHT
                        ? pathSmallSpace : pathSpace}, 1);

        mBGPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mBGPaint.setColor(COLOR_BG);
        mBGPaint.setStyle(Paint.Style.FILL);
        mBGPaint.setAntiAlias(true);
        mBGPaint.setDither(true);

        mPathPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mPathPaint.setColor(COLOR_ROUND);
        mPathPaint.setStrokeWidth(pathWidth);
        mPathPaint.setStyle(Paint.Style.STROKE);//描边
        mPathPaint.setAntiAlias(true);
        mPathPaint.setDither(true);

        mFaceRectPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mFaceRectPaint.setColor(COLOR_RECT);
        mFaceRectPaint.setStrokeWidth(pathWidth);
        mFaceRectPaint.setStyle(Paint.Style.STROKE);
        mFaceRectPaint.setAntiAlias(true);
        mFaceRectPaint.setDither(true);

        mFaceRoundPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mFaceRoundPaint.setColor(COLOR_ROUND);
        mFaceRoundPaint.setStyle(Paint.Style.FILL);
        mFaceRoundPaint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.CLEAR));
        mFaceRoundPaint.setAntiAlias(true);
        mFaceRoundPaint.setDither(true);


        mFaceRectPaint2 = new Paint(Paint.ANTI_ALIAS_FLAG);
        mFaceRectPaint2.setColor(COLOR_RECT_RED);
        mFaceRectPaint2.setStrokeWidth(pathWidth);
        mFaceRectPaint2.setStyle(Paint.Style.STROKE);
        mFaceRectPaint2.setAntiAlias(true);
        mFaceRectPaint2.setDither(true);
    }

    public void processDrawState(boolean isDrawDash) {
        mIsDrawDash = isDrawDash;
        postInvalidate();
    }

    public float getRound() {
        return mR;
    }

    public Rect getFaceRoundRect() {
        if (mFaceRect != null) {
            Log.e(TAG, mFaceRect.toString());
        }
        return mFaceRect;
    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        float canvasWidth = right - left;
        float canvasHeight = bottom - top;
        Log.i(TAG,"canvasWidth="+canvasWidth+",,,canvasHeight="+canvasHeight);
        float x = canvasWidth / 2;
        float y = (canvasHeight / 2) - ((canvasHeight / 2) * HEIGHT_RATIO);
       // float r = (canvasWidth / 2) - ((canvasWidth / 2) * WIDTH_SPACE_RATIO);
        float r = (canvasWidth / 2);

//        if (mFaceRect == null) {
//            mFaceRect = new Rect((int) (x - r),
//                    (int) (y - r),
//                    (int) (x + r),
//                    (int) (y + r));
//        }



        if (mFaceDetectRect == null) {
            float hr = r + (r * HEIGHT_EXT_RATIO);
            mFaceDetectRect = new Rect((int) (x - r),
                    (int) (y - hr),
                    (int) (x + r),
                    (int) (y + hr));
        }
        mX = x;
        mY = y;
        if(canvasWidth>canvasHeight)
            mR = mY;
        else
            mR = r;


        if (mFaceRect == null) {
            mFaceRect = new Rect((int) (mX - mR),
                    (int) (mY - mR),
                    (int) (mX + mR),
                    (int) (mY + mR));
        }

        Log.i(TAG,"mX="+mX+",,,mY="+mY+",,,mR="+mR);

    }

   // private Canvas mCanvas;
    @Override
    public void onDraw(Canvas canvas) {
        super.onDraw(canvas);
       // this.mCanvas = canvas;
        canvas.drawColor(Color.TRANSPARENT);
        canvas.drawPaint(mBGPaint);
        if (mIsDrawDash) {
            mPathPaint.setPathEffect(mFaceRoundPathEffect);
        } else {
            mPathPaint.setPathEffect(null);
        }
        canvas.drawCircle(mX, mY, mR + CIRCLE_SPACE, mPathPaint);
        canvas.drawCircle(mX, mY, mR, mFaceRoundPaint);
        if (mFaceRect != null) {
            canvas.drawRect(mFaceRect, mFaceRectPaint);
        }

        //人脸检测的区域
//        if (mFaceDetectRect != null) {
//            canvas.drawRect(mFaceDetectRect, mFaceRectPaint);
//        }
    }

    public static Rect getPreviewDetectRect(int w, int pw, int ph) {




        //mDisplayWidth, mPreviewHight, mPreviewWidth
        float round = (w / 2) - ((w / 2) * WIDTH_SPACE_RATIO);
        float x = pw / 2;
        float y = (ph / 2) - ((ph / 2) * HEIGHT_RATIO);
        float r = (pw / 2) > round ? round : (pw / 2);
        float hr = r + (r * HEIGHT_EXT_RATIO);
        Rect rect = new Rect((int) (x - r),
                (int) (y - hr),
                (int) (x + r),
                (int) (y + hr));
       // Log.i(TAG, "FaceRoundView getPreviewDetectRect w=" + w +"-pw=" + pw + "-ph=" + ph + "-" + rect.toString());
        //  w=720-pw=486-ph=864-Rect(1, 51 - 484, 726)
        //Log.i(TAG, "FaceRoundView getPreviewDetectRect (x - r)=" + (x - r) +"-(y - hr)=" +(y - hr) + "-(x + r)=" + (x + r)+ "-(y + hr)" + (y + hr));
        //(x - r)=1.800003-(y - hr)=51.119995-(x + r)=484.2-(y + hr)726.48

        return rect;
    }

}