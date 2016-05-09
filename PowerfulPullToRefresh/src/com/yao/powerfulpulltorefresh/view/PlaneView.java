package com.yao.powerfulpulltorefresh.view;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import com.yao.powerfulpulltorefresh.R;
import com.yao.powerfulpulltorefresh.bean.Bullet;
import com.yao.powerfulpulltorefresh.bean.EnemyPlane;
import com.yao.powerfulpulltorefresh.bean.MyPlane;
import com.yao.powerfulpulltorefresh.util.UiUtils;

public class PlaneView extends SurfaceView implements SurfaceHolder.Callback, Runnable {

	private SurfaceHolder holder;
	private Timer timer;
	private TimerTask task;
	private ArrayList<EnemyPlane> enemyPlanes = new ArrayList<EnemyPlane>();
	private ArrayList<Bullet> bullets = new ArrayList<Bullet>();
	private Random random = new Random();

	private int enemyPlaneMaxRightPosition;//敌方飞机最靠右的极限位置
	private int enemyPlaneHeight;
	private MyPlane myPlane;

	private int width;
	private int height;

	private boolean isFingerOnMyPlane;

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
		//holder.setFormat(PixelFormat.OPAQUE);

	}

	@Override
	public void run() {

	}

	@Override
	public void surfaceCreated(final SurfaceHolder holder) {
		BitmapFactory.Options options = new BitmapFactory.Options();
		options.inJustDecodeBounds = true;
		BitmapFactory.decodeResource(UiUtils.getResources(), R.drawable.enemy_plane_1, options);
		enemyPlaneMaxRightPosition = getWidth() - options.outWidth * (int) (getResources().getDisplayMetrics().density);
		enemyPlaneHeight = options.outHeight * (int) (getResources().getDisplayMetrics().density);
		width = getWidth();
		height = getHeight();
		myPlane = new MyPlane(width, height);

		timer = new Timer();
		task = new TimerTask() {
			@Override
			public void run() {

				continueInitEnemyPlane();

				continueInitBullet();

				Paint paint = new Paint();
				Canvas canvas = holder.lockCanvas();
				canvas.drawColor(Color.WHITE);

				//画敌机
				Iterator<EnemyPlane> itPlane = enemyPlanes.iterator();
				while (itPlane.hasNext()) {
					EnemyPlane ep = itPlane.next();
					if (ep.y > height || ep.status == EnemyPlane.Status.C) {
						itPlane.remove();
					} else {
						ep.draw(canvas, paint, bullets);
					}
				}

				//画子弹
				Iterator<Bullet> itBullet = bullets.iterator();
				while (itBullet.hasNext()) {
					Bullet bullet = itBullet.next();
					if (bullet.y + Bullet.height < 0) {
						itBullet.remove();
					} else {
						bullet.draw(canvas, paint);
					}
				}

				//画我机
				myPlane.draw(canvas, paint, enemyPlanes);

				holder.unlockCanvasAndPost(canvas);

			}

		};
		timer.schedule(task, 0, 20);
	}

	@Override
	public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
		// TODO Auto-generated method stub

	}

	@Override
	public void surfaceDestroyed(SurfaceHolder holder) {
		timer.cancel();
	}

	@Override
	public boolean onTouchEvent(MotionEvent event) {
		int x = (int) event.getX();
		int y = (int) event.getY();
		if (event.getAction() == MotionEvent.ACTION_MOVE) {
			if (isFingerOnMyPlane) {
				myPlane.setTarget(x, y);
			}
		} else if (event.getAction() == MotionEvent.ACTION_DOWN) {
			if (isTouchOnPlane(x, y)) {
				isFingerOnMyPlane = true;
			}
		} else if (event.getAction() == MotionEvent.ACTION_UP) {
			if (isFingerOnMyPlane == true) {
				isFingerOnMyPlane = false;
			}
		}
		if (y - MyPlane.height / 2 < 0) {
			y = y - MyPlane.height;
		}
		if (y - MyPlane.height / 2 > height) {
			y = height - MyPlane.height / 2;
		}
		//		myPlane.x = x - MyPlane.width / 2;
		//		myPlane.y = y - MyPlane.height / 2;
		return true;
	}

	private boolean isTouchOnPlane(int x, int y) {
		if (x > myPlane.x && x < myPlane.x + MyPlane.width && y > myPlane.y && y < myPlane.y + MyPlane.height) {
			return true;
		}
		return false;
	}

	private void continueInitEnemyPlane() {
		if (System.currentTimeMillis() % 10 == 0) {
			int x = random.nextInt(enemyPlaneMaxRightPosition);
			EnemyPlane ep = new EnemyPlane(x, -enemyPlaneHeight);
			enemyPlanes.add(ep);
		}
	}

	int bulletInterval = 0;

	private void continueInitBullet() {
		if (bulletInterval % 20 == 0 && (myPlane.status == MyPlane.Status.A || myPlane.status == MyPlane.Status.B)) {
			Bullet bullet = new Bullet(myPlane.x + MyPlane.width / 2 - Bullet.width / 2, myPlane.y + MyPlane.height / 2 - Bullet.height / 2);
			bullets.add(bullet);
			bulletInterval = 1;
		}
		bulletInterval++;
	}

}
