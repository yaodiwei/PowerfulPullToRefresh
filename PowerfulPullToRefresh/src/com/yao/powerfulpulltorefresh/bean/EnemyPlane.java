package com.yao.powerfulpulltorefresh.bean;

import java.util.ArrayList;

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
 * @see com.yao.powerfulpulltorefresh.EnemyPlane.java
 */
public class EnemyPlane extends GameObject {

	public EnemyPlane(int x, int y) {
		super();
		resId = R.drawable.enemy_plane;
		bitmap = BitmapFactory.decodeResource(UiUtils.getResources(), resId);
		Matrix matrix = new Matrix();
		matrix.postScale(3f, 3f); //长和宽放大缩小的比例 
		bitmap = Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), matrix, true);
		width = bitmap.getWidth();//34
		height = bitmap.getHeight();//84
		this.x = x;
		this.y = y;
		speedY = 5;
	}

	public void draw(Canvas canvas, Paint paint) {
//		x += speedX;
		y += speedY;
		canvas.save();
		canvas.clipRect(x, y, x + width, y + height / 3);
		canvas.drawBitmap(bitmap, x, y, paint);
		canvas.restore();
	}
	
	public boolean isHit(ArrayList<Bullet> bullets) {
		for (Bullet bullet : bullets) {
			if (bullet.x + bullet.width > x && bullet.x < x + width && y+height/3 > bullet.y) {
				return true;
			}
		}
		return false;
	}

}
