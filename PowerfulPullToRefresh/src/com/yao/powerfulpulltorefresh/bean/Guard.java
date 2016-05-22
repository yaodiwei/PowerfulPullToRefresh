package com.yao.powerfulpulltorefresh.bean;

import android.graphics.Canvas;
import android.graphics.Paint;

public class Guard extends BaseGameBean {

	public int width;
	public int height;

	//传第几列, 第几行
	public Guard(int viewWidth, int viewHeight) {
		this.width = viewWidth / 5;
		this.height = viewHeight / 40;
		this.x = viewWidth/2 - width/2;
		this.y = viewHeight/16*15;
	}

	public void draw(Canvas canvas, Paint paint) {
		paint.setStyle(Paint.Style.FILL);
		paint.setColor(0xFF374EFA);
		canvas.drawRect(x, y, x + width, y + height, paint);
	}

	@Override
	public String toString() {
		return "Guard [width=" + width + ", height=" + height + ", x=" + x + ", y=" + y;
	}
	
}
