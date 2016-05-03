package com.yao.powerfulpulltorefresh.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.PixelFormat;
import android.util.AttributeSet;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

public class PlaneView extends SurfaceView implements SurfaceHolder.Callback, Runnable {

	private Canvas canvas;
	private SurfaceHolder holder;
	private boolean isDrawing;
	
	public PlaneView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		initView();
	}

	public PlaneView(Context context, AttributeSet attrs) {
		super(context, attrs);
		initView();
	}

	public PlaneView(Context context) {
		super(context);
		initView();
	}

	
	private void initView() {
		holder = getHolder();
        holder.addCallback(this);
        setFocusable(true);
        setFocusableInTouchMode(true);
        this.setKeepScreenOn(true);
//        holder.setFormat(PixelFormat.OPAQUE);
	}

	@Override
	public void run() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void surfaceCreated(SurfaceHolder holder) {
		isDrawing = true;
		new Thread(this).start();
		
	}

	@Override
	public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void surfaceDestroyed(SurfaceHolder holder) {
		isDrawing = false;
		
	}

	
}
