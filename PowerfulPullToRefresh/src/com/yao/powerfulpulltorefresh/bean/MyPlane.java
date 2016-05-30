package com.yao.powerfulpulltorefresh.bean;

import java.util.ArrayList;
import java.util.Iterator;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;

import com.yao.powerfulpulltorefresh.R;
import com.yao.powerfulpulltorefresh.bean.EnemyPlane.Status;
import com.yao.powerfulpulltorefresh.util.UiUtils;

public class MyPlane extends BaseGameBean {

	public static Bitmap bitmap1;
	public static Bitmap bitmap2;
	public static Bitmap bitmap3;
	public static Bitmap bitmap4;
	public static int width;
	public static int height;
	public int targetX;
	public int targetY;

	public Status status = Status.A;

	static {
		Matrix matrix = new Matrix();
		matrix.postScale(3f, 3f); //长和宽放大缩小的比例 
		bitmap1 = BitmapFactory.decodeResource(UiUtils.getResources(), R.drawable.my_plane_1);
		bitmap2 = BitmapFactory.decodeResource(UiUtils.getResources(), R.drawable.my_plane_2);
		bitmap3 = BitmapFactory.decodeResource(UiUtils.getResources(), R.drawable.my_plane_explore_1);
		bitmap4 = BitmapFactory.decodeResource(UiUtils.getResources(), R.drawable.my_plane_explore_2);
		bitmap1 = Bitmap.createBitmap(bitmap1, 0, 0, bitmap1.getWidth(), bitmap1.getHeight(), matrix, true);
		bitmap2 = Bitmap.createBitmap(bitmap2, 0, 0, bitmap2.getWidth(), bitmap2.getHeight(), matrix, true);
		bitmap3 = Bitmap.createBitmap(bitmap3, 0, 0, bitmap3.getWidth(), bitmap3.getHeight(), matrix, true);
		bitmap4 = Bitmap.createBitmap(bitmap4, 0, 0, bitmap4.getWidth(), bitmap4.getHeight(), matrix, true);
		width = bitmap1.getWidth();
		height = bitmap1.getHeight();//84
	}

	public MyPlane(int width, int height) {
		super();
		this.x = width/2 - MyPlane.width / 2;
		this.y = height - MyPlane.height;
		targetX = x;
		targetY = y;
		speedX = 5;
		speedY = 5;
	}

	private int exploreTimes;
	public void draw(Canvas canvas, Paint paint, ArrayList<EnemyPlane> eps) {
		if (status==Status.A || status==Status.B){
			if (isHit(eps)) {
				status = Status.C;
				canvas.drawBitmap(bitmap3, x, y, paint);
			} else {
				if (targetX > x) {
					x = Math.min((x+speedX), targetX);
				} else if (targetX < x) {
					x = Math.max((x-speedX), targetX);
				}
				if (targetY > y) {
					y = Math.min((y+speedY), targetY);
				} else if (targetY < y) {
					y = Math.max((y-speedY), targetY);
				}
				if (status == Status.A) {
					canvas.drawBitmap(bitmap1, x, y, paint);
					status = Status.B;
				} else {
					canvas.drawBitmap(bitmap2, x, y, paint);
					status = Status.A;
				}
			}
		} else if (status == Status.C){
			exploreTimes++;
			if (exploreTimes > 20) {
				exploreTimes = 0;
				status = Status.D;
				canvas.drawBitmap(bitmap4, x, y, paint);
			} else {
				canvas.drawBitmap(bitmap3, x, y, paint);
			}
		} else if (status == Status.D) {
			if (exploreTimes < 20) {
				exploreTimes++;
				canvas.drawBitmap(bitmap4, x, y, paint);
			} else {
			}
		}
	}
	
	public void setTarget(int x, int y) {
		targetX = x - width/2;
		targetY = y - height;
	}
	
	
	public boolean isHit(ArrayList<EnemyPlane> enemyPlane){
		Iterator<EnemyPlane> it = enemyPlane.iterator();
		while (it.hasNext()) {
			EnemyPlane ep = it.next();
			if (ep.x+EnemyPlane.width > x && ep.x < x+width && ep.y+EnemyPlane.height > y && ep.y<y+height){
				return true;
			}
		}
		return false;
	}
	
	public static enum Status {
		A, B, C, D;
	}

	
}
