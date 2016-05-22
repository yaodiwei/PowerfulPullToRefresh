package com.yao.powerfulpulltorefresh.bean;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.util.Log;

public class Brick extends BaseGameBean {

	public static int width;
	public static int height;

	public static void setWidthAndHeight(int width, int height) {
		Brick.width = width;
		Brick.height = height;
	}

	//传x, y坐标
//	public Brick(int x, int y) {
//		this.x = x;
//		this.y = y;
//	}
	
	//传第几列, 第几行
	public Brick(int col, int row) {
		this.x = col * width;
		this.y = row * height;
	}

	public void draw(Canvas canvas, Paint paint) {
		paint.setStyle(Paint.Style.FILL);
		paint.setColor(0xFF374EFA);
		canvas.drawRect(x, y, x + width, y + height, paint);
		paint.setStyle(Paint.Style.STROKE);
		paint.setColor(0xFFFFFFFF);
		canvas.drawRect(x, y, x + width, y + height, paint);
	}

	@Override
	public String toString() {
		return "Brick [x=" + x + ", y=" + y + "]";
	}
	
}
