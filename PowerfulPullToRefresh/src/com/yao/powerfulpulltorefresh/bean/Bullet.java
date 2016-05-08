/**
 * FileName:Bullet.java
 * Copyright(C) 2016 zteict
 */
package com.yao.powerfulpulltorefresh.bean;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.util.Log;

import com.yao.powerfulpulltorefresh.R;
import com.yao.powerfulpulltorefresh.util.UiUtils;

/**
 * @author YaoDiWei
 * @version 
 * @see com.yao.powerfulpulltorefresh.bean.Bullet.java
 */
public class Bullet extends GameObject {

	public static Bitmap bitmap;
	public static int width;
	public static int height;
	
	static {
		Matrix matrix = new Matrix();
		matrix.postScale(3f, 3f); //长和宽放大缩小的比例 
		bitmap = BitmapFactory.decodeResource(UiUtils.getResources(), R.drawable.bullet);
		bitmap = Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), matrix, true);
		width = bitmap.getWidth();//10*3
		height = bitmap.getHeight();//20*3
	}
	
	public Bullet(int x, int y) {
		super();
		this.x = x;
		this.y = y;
		speedY = -30;
	}
	
	public void draw(Canvas canvas, Paint paint) {
//		x += speedX;
		y += speedY;
		canvas.save();
		canvas.drawBitmap(bitmap, x, y, paint);
		canvas.restore();
	}
}
