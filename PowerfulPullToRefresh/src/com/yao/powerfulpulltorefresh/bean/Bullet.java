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

import com.yao.powerfulpulltorefresh.R;
import com.yao.powerfulpulltorefresh.util.UiUtils;

/**
 * @author YaoDiWei
 * @version 
 * @see com.yao.powerfulpulltorefresh.bean.Bullet.java
 */
public class Bullet extends GameObject {

	public Bullet(int x, int y) {
		super();
		resId = R.drawable.bullet;
		bitmap = BitmapFactory.decodeResource(UiUtils.getResources(), resId);
		Matrix matrix = new Matrix();
		matrix.postScale(3f, 3f); //长和宽放大缩小的比例 
		bitmap = Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), matrix, true);
		width = bitmap.getWidth();//10
		height = bitmap.getHeight();//20
		this.x = x;
		this.y = y;
		speedY = -3;
	}
	
	public void draw(Canvas canvas, Paint paint) {
//		x += speedX;
		y += speedY;
		canvas.save();
		canvas.drawBitmap(bitmap, x, y, paint);
		canvas.restore();
	}
}
