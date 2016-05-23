package com.yao.powerfulpulltorefresh.view;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.Timer;
import java.util.TimerTask;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import com.yao.powerfulpulltorefresh.bean.Ball;
import com.yao.powerfulpulltorefresh.bean.Brick;
import com.yao.powerfulpulltorefresh.bean.Guard;
import com.yao.powerfulpulltorefresh.bean.MyPlane;
import com.yao.powerfulpulltorefresh.util.UiUtils;

public class BallView extends SurfaceView implements SurfaceHolder.Callback {

	private Timer timer;
	private TimerTask task;

	private SurfaceHolder holder;
	private int width;
	private int height;
	
	private ArrayList<Brick> bricks = new ArrayList<Brick>();
	private Guard guard;
	private Ball ball;

	public BallView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		initView();
	}

	public BallView(Context context, AttributeSet attrs) {
		super(context, attrs);
		initView();
	}

	public BallView(Context context) {
		super(context);
		initView();
	}

	private void initView() {
		holder = getHolder();
		holder.addCallback(this);
		setFocusable(true);
		setFocusableInTouchMode(true);
		this.setKeepScreenOn(true);
		//holder.setFormat(PixelFormat.OPAQUE);
	}

	@Override
	public void surfaceCreated(final SurfaceHolder holder) {
		width = getWidth();
		height = getHeight();
		initGame();
	}
	

	public static final int BRICK_ROW = 7;
	public static final int BRICK_COL = 8;
	private void initGame() {
		int brickWidth = width / BRICK_COL;
		int brickHeight = (int) (height * 0.4 / BRICK_ROW);
		Brick.setWidthAndHeight(brickWidth, brickHeight);
		for (int row=0; row<BRICK_ROW; row++) {
			for (int col=0; col<BRICK_COL; col++){
				Brick brick = new Brick(col, row);
				bricks.add(brick);
			}
		}

		guard = new Guard(width, height);
		ball = new Ball(width, height, 10, -10);
	}

	@Override
	public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {

	}

	@Override
	public void surfaceDestroyed(SurfaceHolder holder) {
		if (timer != null) {
			timer.cancel();
		}
	}

	public void switchGame(boolean open) {
		if (open) {
			final Paint paint = new Paint();
			paint.setTextSize(UiUtils.dp2px(24));
			paint.setTextAlign(Paint.Align.LEFT);
			timer = new Timer();
			task = new TimerTask() {
				@Override
				public void run() {
					Canvas canvas = holder.lockCanvas();
					canvas.drawColor(0xFF2DABC6);
					
					//画砖块
					Iterator<Brick> it = bricks.iterator();
					while(it.hasNext()) {
						Brick brick = it.next();
						brick.draw(canvas, paint);
					}
					
					//画自己挡板守卫
					guard.draw(canvas, paint);
					
					//画球
					isCollide();
					ball.draw(canvas, paint);
					
					holder.unlockCanvasAndPost(canvas);
				}

			};
			timer.schedule(task, 0, 10);
		} else {
			timer.cancel();
		}
	}
	
	public boolean isCollide() {
		//检测是否和砖块碰撞
		boolean result = false;
		Iterator<Brick> it = bricks.iterator();
		while (it.hasNext()) {
			Brick brick = it.next();

			if (Math.pow((brick.x - ball.x), 2) + Math.pow((brick.y - ball.y), 2) <= ball.radius * ball.radius//左上角
					|| Math.pow((brick.x + Brick.width - ball.x), 2) + Math.pow((brick.y - ball.y), 2) <= ball.radius * ball.radius//右上角
					|| Math.pow((brick.x - ball.x), 2) + Math.pow((brick.y + Brick.height - ball.y), 2) <= ball.radius * ball.radius//左下角
					|| Math.pow((brick.x + Brick.width - ball.x), 2) + Math.pow((brick.y + Brick.height - ball.y), 2) <= ball.radius * ball.radius) {//右下角
				ball.speedX = -ball.speedX;
				ball.speedY = -ball.speedY;
				result = true;
			}

			if (brick.x < ball.x && brick.x + Brick.width > ball.x) {
				if (ball.y < brick.y && ball.y + ball.radius > brick.y) {//碰撞顶边
					ball.speedY = -ball.speedY;
					result = true;
				} else if (ball.y-ball.radius < brick.y+Brick.height && ball.y > brick.y+Brick.height) {//碰撞底边
					ball.speedY = -ball.speedY;
					result = true;
				}
			}
			
			if (brick.y < ball.y && brick.y + Brick.height > ball.y) {
				if (ball.x < brick.x && ball.x + ball.radius > brick.x) {//碰撞左边
					ball.speedX = -ball.speedX;
					result = true;
				} else if (ball.x-ball.radius < brick.x+Brick.width && ball.x > brick.x+Brick.width) {//碰撞右边
					ball.speedX = -ball.speedX;
					result = true;
				}
			}
			
			if (result == true) {
				it.remove();
				break;
			}
		}
		
		//检测是否和左墙,右墙,上墙碰撞
		if (ball.x - ball.radius < 0) { //左墙
			ball.speedX = -ball.speedX;
		}
		if (ball.x + ball.radius > width) { //右墙
			ball.speedX = -ball.speedX;
		}
		if (ball.y - ball.radius < 0) { //上墙
			ball.speedY = -ball.speedY;
		}
		if (ball.y + ball.radius > height) { //下墙
			ball.speedY = -ball.speedY;
		}
		
		
		
		if (result == true) {
			return true;
		}
		return false;
	}
	
	@Override
	public boolean dispatchTouchEvent(MotionEvent event) {
		getParent().requestDisallowInterceptTouchEvent(true);// 用getParent去请求不拦截
		return super.dispatchTouchEvent(event);
	}
	
	private boolean downOnGuard;
	@Override
	public boolean onTouchEvent(MotionEvent event) {
		int x = (int) event.getX();
		int y = (int) event.getY();
		if (event.getAction() == MotionEvent.ACTION_MOVE) {
			if (downOnGuard) {
				guard.x = x - guard.width/2;
			}
		} else if (event.getAction() == MotionEvent.ACTION_DOWN) {
			if (isTouchOnGuard(x, y)) {
				downOnGuard = true;
			}
		} else if (event.getAction() == MotionEvent.ACTION_UP) {
			if (downOnGuard == true) {
				downOnGuard = false;
			}
		}
		return true;
	}
	
	private boolean isTouchOnGuard(int x, int y) {
		//-40和+40是对高度范围的扩大
		if (x > guard.x && x < guard.x + guard.width && 
				y > guard.y - 40 && y < guard.y + guard.height + 40) {
			return true;
		}
		return false;
	}

}
