package com.yao.powerfulpulltorefresh.bean;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.util.Log;

public class Ball extends BaseGameBean {

	public int radius;

	//传第几列, 第几行
	public Ball(int viewWidth, int viewHeight, int speedX, int speedY) {
		this.radius = viewWidth / 25;
		this.x = viewWidth / 2;
		this.y = viewHeight / 2;
		this.speedX = speedX;
		this.speedY = speedY;
	}
	
	public Ball(int viewWidth, int viewHeight, int x, int y, int speedX, int speedY) {
		this.radius = viewWidth / 25;
		Log.e("yao", "Ball radius = " + radius);
		this.x = x;
		this.y = y;
		this.speedX = speedX;
		this.speedY = speedY;
	}
	
	public void draw(Canvas canvas, Paint paint) {
		paint.setStyle(Paint.Style.FILL);
		paint.setColor(0xFFFFFFFF);
		canvas.drawCircle(x, y, radius, paint);
	}


}
