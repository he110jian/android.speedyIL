package com.example.speedyil;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.DashPathEffect;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.PathEffect;
import android.graphics.PointF;
import android.view.MotionEvent;
import android.view.View;

class myView extends View {
	private Paint mPaint;
	private Bitmap bitMap;
	private Bitmap bitMapDisplay;
	private int m_posX;
	private int m_posY;
	private int m_bitMapWidth;
	private int m_bitMapHeight;
	private float c_posX, c_posY, radius;
	private PointF startPoint;
/*	private int mod = 0;
	private float startDis;*/
	private Matrix matrix;
	private float scale;//图像比例

	public myView(Context context) {
		super(context);
		mPaint = new Paint();
		mPaint.setFlags(Paint.ANTI_ALIAS_FLAG);
		mPaint.setAntiAlias(true); // 消除锯齿
		mPaint.setStyle(Paint.Style.STROKE); // 绘制空心圆
		mPaint.setStrokeWidth(2);  
		mPaint.setColor(Color.rgb(35, 92, 251));
		PathEffect effects = new DashPathEffect(new float[]{5,5,5,5},1);  
        mPaint.setPathEffect(effects);//虚线
		bitMap = BitmapFactory.decodeResource(this.getResources(),
				R.drawable.seulib);
		bitMapDisplay = bitMap;
		// 获取图片宽高
		m_bitMapWidth = bitMap.getWidth();
		m_bitMapHeight = bitMap.getHeight();
		m_posX = 0;
		m_posY = 0;
		c_posX = 0;
		c_posY = 0;
		radius = 0;
		scale = 1f;
		matrix = new Matrix();
		startPoint = new PointF();
	}

	public void setCircle(float x, float y, float r) {
		c_posX = x;
		c_posY = y;
		radius = r;
	}

	@Override
	public boolean onTouchEvent(MotionEvent event) {
		switch (event.getAction()) {
		case MotionEvent.ACTION_DOWN:
//			mod = 1;
			startPoint.set(event.getX(), event.getY());
			break;
		case MotionEvent.ACTION_MOVE:
//			if (mod == 1) {
				move(event.getX() - startPoint.x, event.getY() - startPoint.y);
				startPoint.set(event.getX(), event.getY());
/*			} else if (mod == 2) {
				float endDis = distance(event);// 结束距离
				if (endDis > 10f) { // 两个手指并拢在一起的时候像素大于1
		//			zoom(endDis / startDis);
				}
			}*/
			break;
		case MotionEvent.ACTION_UP:
			// 当触点离开屏幕，但是屏幕上还有触点(手指)
/*		case MotionEvent.ACTION_POINTER_UP:
			mod = 0;
			break;
		// 当屏幕上已经有触点(手指)，再有一个触点压下屏幕
		case MotionEvent.ACTION_POINTER_DOWN:
			mod = 2;
			*//** 计算两个手指间的距离 *//*
			startDis = distance(event);*/
			break;
		}
		return true;
	}

	public PointF getPos() {
		PointF pos = new PointF(this.m_posX, this.m_posY);
		return pos;
	}

	public void setM_pos(int x, int y) {
		m_posX = x;
		m_posY = y;
	}

	public void move(float dx, float dy) {
		m_posX += dx;
		m_posY += dy;
		c_posX += dx;
		c_posY += dy;
		setCircle(c_posX, c_posY, radius);
	}

	public void zoom(float s) {
		matrix.reset();
		scale = s*scale;
		matrix.postScale(scale, scale);
		bitMapDisplay = Bitmap.createBitmap(bitMap, 0, 0, m_bitMapWidth,
				m_bitMapHeight, matrix, true);
		c_posX = m_posX+(c_posX-m_posX)*s;
		c_posY = m_posY+(c_posY-m_posY)*s;
		radius = radius*s;
/*		System.out.println(scale+" "+m_posX+" "+m_posY+" "+
		m_bitMapWidth+" "+m_bitMapHeight);*/
	}

	public float getScale() {
		return scale;
	}

	public void setScale(float scale) {
		this.scale = scale;
	}

/*	*//** 计算两个手指间的距离 *//*
	private float distance(MotionEvent event) {
		float dx = event.getX(1) - event.getX(0);
		float dy = event.getY(1) - event.getY(0);
		*//** 使用勾股定理返回两点之间的距离 *//*
		return FloatMath.sqrt(dx * dx + dy * dy);
	}*/

	public int getBmpWidth() {
		return m_bitMapWidth;
	}

	public int getBmpHeight() {
		return m_bitMapHeight;
	}

	@Override
	protected void onDraw(Canvas canvas) {
		super.onDraw(canvas);
		canvas.drawBitmap(bitMapDisplay, m_posX, m_posY, mPaint);
		canvas.drawCircle(c_posX, c_posY, radius, mPaint);
		invalidate();
	}

}
