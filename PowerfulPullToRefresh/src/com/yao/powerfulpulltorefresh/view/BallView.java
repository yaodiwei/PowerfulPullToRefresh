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
import com.yao.powerfulpulltorefresh.util.UiUtils;

public class BallView extends SurfaceView implements SurfaceHolder.Callback {

	private boolean isStartGame;
	private Timer timer;
	private TimerTask task;

	private SurfaceHolder holder;
	private int width;
	private int height;
	
	private ArrayList<Brick> bricks = new ArrayList<Brick>();
	private Guard guard;
	private Ball ball;
	
	private int score;

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
	}
	

	public static final int BRICK_ROW = 7;
	public static final int BRICK_COL = 8;
	private void initGame() {
		int brickWidth = width / BRICK_COL;
		int brickHeight = (int) (height * 0.4 / BRICK_ROW);
		Brick.setWidthAndHeight(brickWidth, brickHeight);
		
		//读档  调试代码用
		int i = 0;
		boolean[] visible = new boolean[]{true, true, true, true, true, true, true, true, true, true, true, true, true, true, true, true, true, true, true, true, true, true, true, true, true, true, true, true, true, true, true, true, true, true, true, true, false, false, false, true, true, true, false, false, false, false, false, false, true, false, false, false, false, false, false, false, true, true, true, true, true, true, true, true, true, true, true, true, true, true, true, true, true, true, true, true, true, true, true, true, true, true, true, true, true, true, true, true, true, true, true, true, true, true, true, true, true, true, true, false, false, false, false, true, true, false, false, false, false, false, false, false};
		
		
		
		
		for (int row=0; row<BRICK_ROW; row++) {
			for (int col=0; col<BRICK_COL; col++){
				Brick brick = new Brick(col, row);
//				Brick brick = new Brick(col, row, visible[i++]);
				bricks.add(brick);
			}
		}

		guard = new Guard(width, height);
		ball = new Ball(width, height, 10, -10);
//		ball = new Ball(width, height, 322, 765, -8, -10);
	}
	
	public void print(){
		StringBuilder str = new StringBuilder();
		for (Brick brick : bricks) {
			str.append(brick.visible).append(", ");
		}
		Log.e("yao", "boolean[] visible = new boolean[]{" + str.substring(0, str.length()-2) + "};");
		
		Log.e("yao", "ball = new Ball(width, height, " + ball.x + ", " + ball.y + ", " + ball.speedX + ", " + ball.speedY + ");");
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
			if (!isStartGame) {
				initGame();
				isStartGame = true;
				score = 0;
			}
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
					ball.draw(canvas, paint);
					handlerCollide();
					
					//画标题栏
					paint.setColor(0x55000000);
					canvas.drawRect(0, height-UiUtils.dp2px(30), width, height, paint);
					paint.setColor(0xFF000000);
					canvas.drawText("得分:" + score, 0, height-UiUtils.dp2px(5), paint);
					
					//画结束游戏
					if (!isStartGame) {
						paint.setColor(0xFF000000);
						int textWidth = (int)paint.measureText("GAME OVER");
						canvas.drawText("GAME OVER", width/2 - textWidth/2, height/2, paint);
					}
					
					holder.unlockCanvasAndPost(canvas);
				}

			};
			timer.schedule(task, 0, 2);
		} else {
			timer.cancel();
		}
	}
	
	public boolean handlerCollide() {
		//检测是否和砖块碰撞
		ball.x = ball.x + ball.speedX;
		ball.y = ball.y + ball.speedY;
		
		for (int i=0; i<bricks.size(); i++) {
			Brick brick = bricks.get(i);
			
			if (brick.visible == false) {
				continue;
			}
			
//			if (
//					//碰撞左上角
//					Math.pow((brick.x - ball.x), 2) + Math.pow((brick.y - ball.y), 2) <= ball.radius * ball.radius 
//					&& !bricks.get(i-1).visible && !bricks.get(i-BRICK_COL).visible //左砖和上砖不存在
//					&& ball.speedX > 0 && ball.speedY > 0 //必须要右下方飞行
//					
//					//右上角
//					|| Math.pow((brick.x + Brick.width - ball.x), 2) + Math.pow((brick.y - ball.y), 2) <= ball.radius * ball.radius
//					&& i+1<bricks.size() && !bricks.get(i+1).visible && !bricks.get(i-BRICK_COL).visible //右砖和上砖不存在
//					&& ball.speedX < 0 && ball.speedY > 0 //必须要左下方飞行
//					
//					//左下角
//					|| Math.pow((brick.x - ball.x), 2) + Math.pow((brick.y + Brick.height - ball.y), 2) <= ball.radius * ball.radius
//					&& !bricks.get(i-1).visible //左转消失
//					&& (i+BRICK_COL>=bricks.size() || i+BRICK_COL<bricks.size() && !bricks.get(i+BRICK_COL).visible) //下砖消失或者下砖根本没有
//					&& ball.speedX > 0 && ball.speedY < 0 //必须要右上方飞行
//							
//					//右下角		
//					|| Math.pow((brick.x + Brick.width - ball.x), 2) + Math.pow((brick.y + Brick.height - ball.y), 2) <= ball.radius * ball.radius
//					&& !bricks.get(i+1).visible //右砖消失
//					&& (i+BRICK_COL>=bricks.size() || i+BRICK_COL<bricks.size() && !bricks.get(i+BRICK_COL).visible) //下砖消失或者下砖根本没有
//					&& ball.speedX < 0 && ball.speedY < 0 //必须要左上方飞行
//					) {
//				ball.speedX = -ball.speedX;
//				ball.speedY = -ball.speedY;
//				brick.visible = false;
//				score++;
//				return true;
//			}
			
			if (
					//碰撞左上角
					Math.pow((brick.x - ball.x), 2) + Math.pow((brick.y - ball.y), 2) <= ball.radius * ball.radius 
					&& !bricks.get(i-1).visible && !bricks.get(i-BRICK_COL).visible //左砖和上砖不存在
					) {
				if (ball.speedX > 0 && ball.speedY > 0) { //如果右下方飞行
					ball.speedX = -ball.speedX;
					ball.speedY = -ball.speedY;
				} else if (ball.speedX < 0 && ball.speedY > 0) { //如果左下方飞行
					ball.speedY = -ball.speedY;
				} else if (ball.speedX > 0 && ball.speedY < 0) { //如果右上方飞行
					ball.speedX = -ball.speedX;
				}
				brick.visible = false;
				score++;
				return true;
			}
			
			if (
					//碰撞右上角
					Math.pow((brick.x + Brick.width - ball.x), 2) + Math.pow((brick.y - ball.y), 2) <= ball.radius * ball.radius
					&& i+1<bricks.size() && !bricks.get(i+1).visible && !bricks.get(i-BRICK_COL).visible //右砖和上砖不存在
					) {
				if (ball.speedX < 0 && ball.speedY > 0) { //如果左下方飞行
					ball.speedX = -ball.speedX;
					ball.speedY = -ball.speedY;
				} else if (ball.speedX > 0 && ball.speedY > 0) { //如果右下方飞行
					ball.speedY = -ball.speedY;
				} else if (ball.speedX < 0 && ball.speedY < 0) { //如果左上方飞行
					ball.speedX = -ball.speedX;
				}
				brick.visible = false;
				score++;
				return true;
			}
			
			if (
					//碰撞左下角
					Math.pow((brick.x - ball.x), 2) + Math.pow((brick.y + Brick.height - ball.y), 2) <= ball.radius * ball.radius
					&& !bricks.get(i-1).visible //左转消失
					&& (i+BRICK_COL>=bricks.size() || i+BRICK_COL<bricks.size() && !bricks.get(i+BRICK_COL).visible) //下砖消失或者下砖根本没有
					) {
				if (ball.speedX > 0 && ball.speedY < 0) { //如果右上方飞行
					ball.speedX = -ball.speedX;
					ball.speedY = -ball.speedY;
				} else if (ball.speedX < 0 && ball.speedY < 0) { //如果左上方飞行
					ball.speedY = -ball.speedY;
				} else if (ball.speedX > 0 && ball.speedY > 0) { //如果右下方飞行
					ball.speedX = -ball.speedX;
				}
				brick.visible = false;
				score++;
				return true;
			}
			
			if (
					//碰撞右下角
					Math.pow((brick.x + Brick.width - ball.x), 2) + Math.pow((brick.y + Brick.height - ball.y), 2) <= ball.radius * ball.radius
					&& !bricks.get(i+1).visible //右砖消失
					&& (i+BRICK_COL>=bricks.size() || i+BRICK_COL<bricks.size() && !bricks.get(i+BRICK_COL).visible) //下砖消失或者下砖根本没有
					) {
				if (ball.speedX < 0 && ball.speedY < 0) { //如果左上方飞行
					ball.speedX = -ball.speedX;
					ball.speedY = -ball.speedY;
				} else if (ball.speedX > 0 && ball.speedY < 0) { //如果右上方飞行
					ball.speedY = -ball.speedY;
				} else if (ball.speedX < 0 && ball.speedY > 0) { //如果左下方飞行
					ball.speedX = -ball.speedX;
				}
				brick.visible = false;
				score++;
				return true;
			}
			
			
			
			if (brick.x <= ball.x && ball.x <= brick.x + Brick.width) { // 砖块左 <= 圆心x <= 砖块右
				if (ball.y <= brick.y && brick.y <= ball.y + ball.radius) {//  圆心y <= 砖块顶边 <= 圆底切线
					// 碰撞顶边条件成立
					
					//4句代码防止球冲进砖块内部(只能碰到砖块的边)
					int ballShouldY = brick.y - ball.radius;
					int diff = ball.y - ballShouldY;
					ball.x = ball.x - diff * (ball.speedX>0?1:-1);
					ball.y = ballShouldY;
					
					//删除砖块逻辑
					//当bricks.get(i-1)的砖块已经消失, 所以bricks.get(i-1)没有进入碰撞事件.
					if (ball.x > brick.x) {
						brick.visible = false;
					} else if (brick.x <= ball.x && ball.x <= brick.x + Brick.width) {
						brick.visible = false;
					} else if (brick.x + Brick.width < ball.x) {
						if (bricks.get(i+1).visible) {
							bricks.get(i+1).visible = false;
						} else {
							brick.visible = false;
						}
					}
					score++;
					
					//y转向
					ball.speedY = -ball.speedY;
					return true;
				} else if (ball.y-ball.radius <= brick.y+Brick.height && brick.y+Brick.height <= ball.y) {// 圆顶切线 <= 砖块底边 <= 圆心
					// 碰撞底边条件成立
					
					//4句代码系列
					int ballShouldY = brick.y + Brick.height + ball.radius;
					int diff = ballShouldY - ball.y;
					ball.x = ball.x - diff * (ball.speedX>0?1:-1);
					ball.y = ballShouldY;
					
					//删除砖块逻辑
					//当bricks.get(i-1)的砖块已经消失, 所以bricks.get(i-1)没有进入碰撞事件.
					if (ball.x > brick.x) {
						brick.visible = false;
					} else if (brick.x <= ball.x && ball.x <= brick.x + Brick.width) {
						brick.visible = false;
					} else if (brick.x + Brick.width < ball.x) {
						if (bricks.get(i+1).visible) {
							bricks.get(i+1).visible = false;
						} else {
							brick.visible = false;
						}
					}
					score++;
					
					//y转向
					ball.speedY = -ball.speedY;
					return true;
				}
			}
			
			if (brick.y <= ball.y && ball.y <= brick.y + Brick.height) { // 砖块上 <= 圆心y <= 砖块下
				if (ball.x <= brick.x && brick.x <= ball.x + ball.radius) { // 圆心 <= 砖块左边 <= 圆右切线
					//碰撞左边条件成立
					
					//4句代码系列
					int ballShouldX = brick.x - ball.radius;
					int diff = ball.x - ballShouldX;
					ball.y = ball.y - diff * (ball.speedY>0?1:-1);
					ball.x = ballShouldX;
					
					//删除砖块逻辑
					//当bricks.get(i-BRICK_COL)的砖块已经消失, 所以bricks.get(i-BRICK_COL)没有进入碰撞事件.
					if (ball.y > brick.y) {
						brick.visible = false;
					} else if (brick.y <= ball.y && ball.y <= brick.y + Brick.height) {
						brick.visible = false;
					} else if (brick.y + Brick.height < ball.y) {
						if (bricks.get(i+BRICK_COL).visible) {
							bricks.get(i+BRICK_COL).visible = false;
						} else {
							brick.visible = false;
						}
					}
					score++;
					
					ball.speedX = -ball.speedX;//x转向
					return true;
				} else if (ball.x-ball.radius <= brick.x+Brick.width && brick.x+Brick.width <= ball.x) {// 圆左切线 <= 砖块右边 <= 圆心
					//碰撞右边条件成立
					
					//4句代码系列
					int ballShouldX = brick.x + Brick.width + ball.radius;
					int diff = ballShouldX - ball.x;
					ball.y = ball.y - diff * (ball.speedY>0?1:-1);
					ball.x = ballShouldX;
					
					//删除砖块逻辑
					//当bricks.get(i-BRICK_COL)的砖块已经消失, 所以bricks.get(i-BRICK_COL)没有进入碰撞事件.
					if (ball.y > brick.y) {
						brick.visible = false;
					} else if (brick.y <= ball.y && ball.y <= brick.y + Brick.height) {
						brick.visible = false;
					} else if (brick.y + Brick.height < ball.y) {
						if (bricks.get(i+BRICK_COL).visible) {
							bricks.get(i+BRICK_COL).visible = false;
						} else {
							brick.visible = false;
						}
					}
					score++;
					
					ball.speedX = -ball.speedX;//x转向
					return true;
				}
			}
		}
		
		//检测是不是和挡板碰上
		if (
				//碰撞左上角
				Math.pow((guard.x - ball.x), 2) + Math.pow((guard.y - ball.y), 2) <= ball.radius * ball.radius 
				&& ball.speedX > 0 && ball.speedY > 0 //必须要右下方飞行
				
				//右上角
				|| Math.pow((guard.x + guard.width - ball.x), 2) + Math.pow((guard.y - ball.y), 2) <= ball.radius * ball.radius
				&& ball.speedX < 0 && ball.speedY > 0 //必须要左下方飞行
				) {
			ball.speedX = -ball.speedX;
			ball.speedY = -ball.speedY;
			return true;
		}
		
		if (guard.x <= ball.x && ball.x <= guard.x + guard.width) { // 挡板左 <= 圆心x <= 挡板右
			if (ball.y <= guard.y && guard.y <= ball.y + ball.radius) {//  圆心y <= 挡板顶边 <= 圆底切线
				// 碰撞顶边条件成立
				
				//4句代码防止球冲进砖块内部(只能碰到砖块的边)
				int ballShouldY = guard.y - ball.radius;
				int diff = ball.y - ballShouldY;
				ball.x = ball.x - diff * (ball.speedX>0?1:-1);
				ball.y = ballShouldY;
				
				//y转向
				ball.speedY = -ball.speedY;
				
				// 增加x的变化,以此增加随机性(可删去)
				// 按照策略 |左方向+10速度 ---------- 不做处理 ---------- 右方向+10速度| 
//				int midGuard = guard.x + guard.width/2;
//				float percentage = (float)(ball.x - midGuard) / (guard.width/2);
//				ball.speedX = ball.speedX + (int) (percentage*10); //10的一个系数
//				Log.e("yao", "percentage: " + percentage + " speedX: " + ball.speedX);
				return true;
			}
		}
		
		
		
		//检测是否和左墙,右墙,上墙碰撞
		if (ball.x - ball.radius < 0) { //左墙
			ball.speedX = Math.abs(ball.speedX);
		}
		if (ball.x + ball.radius > width) { //右墙
			ball.speedX = -Math.abs(ball.speedX);
		}
		if (ball.y - ball.radius < 0) { //上墙
			ball.speedY = Math.abs(ball.speedY);
		}
		if (ball.y + ball.radius > height) { //下墙
//			ball.speedY = -ball.speedY;
			ball.speedX = 0;
			ball.speedY = 0;
			isStartGame = false;
			timer.cancel();
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
				y > guard.y) {
			return true;
		}
		return false;
	}

}
