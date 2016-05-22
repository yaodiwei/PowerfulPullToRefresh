package com.yao.powerfulpulltorefresh.bean;

import java.util.ArrayList;
import java.util.Iterator;

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
 * @see com.yao.powerfulpulltorefresh.EnemyPlane.java
 */
public class EnemyPlane extends BaseGameBean {

	public static Bitmap bitmap1;
	public static Bitmap bitmap2;
	public static Bitmap bitmap3;
	public static int width;
	public static int height;
	

	public Status status = Status.A;

	static {
		Matrix matrix = new Matrix();
		matrix.postScale(3f, 3f); //长和宽放大缩小的比例 
		bitmap1 = BitmapFactory.decodeResource(UiUtils.getResources(), R.drawable.enemy_plane_1);
		bitmap2 = BitmapFactory.decodeResource(UiUtils.getResources(), R.drawable.enemy_plane_2);
		bitmap3 = BitmapFactory.decodeResource(UiUtils.getResources(), R.drawable.enemy_plane_3);
		bitmap1 = Bitmap.createBitmap(bitmap1, 0, 0, bitmap1.getWidth(), bitmap1.getHeight(), matrix, true);
		bitmap2 = Bitmap.createBitmap(bitmap2, 0, 0, bitmap2.getWidth(), bitmap2.getHeight(), matrix, true);
		bitmap3 = Bitmap.createBitmap(bitmap3, 0, 0, bitmap3.getWidth(), bitmap3.getHeight(), matrix, true);
		width = bitmap1.getWidth();//34*3
		height = bitmap1.getHeight();//38*3
	}

	public EnemyPlane(int x, int y) {
		super();
		this.x = x;
		this.y = y;
		speedY = 5;
	}

	public void draw(Canvas canvas, Paint paint, ArrayList<Bullet> bullets) {
		if (status == Status.A) {
			if (isCollide(bullets)) {
				status = Status.B;
				canvas.drawBitmap(bitmap2, x, y, paint);
			} else {
				y += speedY;
				canvas.drawBitmap(bitmap1, x, y, paint);
			}
		} else if (status == Status.B) {
			status = Status.C;
			canvas.drawBitmap(bitmap3, x, y, paint);
		}
	}

	public boolean isCollide(ArrayList<Bullet> bullets) {
		Iterator<Bullet> itBullet = bullets.iterator();
		while (itBullet.hasNext()) {
			Bullet bullet = itBullet.next();
			if (bullet.x + Bullet.width > x && bullet.x < x + width && y + height > bullet.y) {
				if (status == Status.A) {
					itBullet.remove();
					return true;
				}
			}
		}
		return false;
	}

	public static enum Status {
		A, B, C;
	}
}
