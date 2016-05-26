package com.yao.powerfulpulltorefresh.bean;

import java.util.ArrayList;
import java.util.Iterator;

import com.yao.powerfulpulltorefresh.bean.EnemyPlane.Status;

import android.graphics.Canvas;
import android.graphics.Paint;

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

	public void draw(Canvas canvas, Paint paint) {
		paint.setStyle(Paint.Style.FILL);
		paint.setColor(0xFFFFFFFF);
		canvas.drawCircle(x, y, radius, paint);
	}


}
