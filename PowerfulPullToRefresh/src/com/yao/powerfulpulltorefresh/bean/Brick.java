package com.yao.powerfulpulltorefresh.bean;

import com.yao.powerfulpulltorefresh.R;
import com.yao.powerfulpulltorefresh.util.UiUtils;

import android.graphics.Canvas;
import android.graphics.Paint;

public class Brick extends BaseGameBean {

	public static int width;
	public static int height;
	
	public boolean visible = true;

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
	
	public Brick(int col, int row, boolean visible) {
		this.x = col * width;
		this.y = row * height;
		this.visible = visible;
	}

	public void draw(Canvas canvas, Paint paint) {
		if (visible == true) {
			paint.setStyle(Paint.Style.FILL);
			paint.setColor(UiUtils.getResources().getColor(R.color.tcx_blue));
			canvas.drawRect(x, y, x + width, y + height, paint);
			paint.setStyle(Paint.Style.STROKE);
			paint.setColor(UiUtils.getResources().getColor(R.color.tcx_blue2));
			canvas.drawRect(x, y, x + width, y + height, paint);
		}
	}

	@Override
	public String toString() {
		return "Brick [x=" + x + ", y=" + y + "]";
	}
	
}
