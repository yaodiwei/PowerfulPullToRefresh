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
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import com.yao.powerfulpulltorefresh.R;
import com.yao.powerfulpulltorefresh.bean.Bullet;
import com.yao.powerfulpulltorefresh.bean.EnemyPlane;
import com.yao.powerfulpulltorefresh.util.UiUtils;

public class PlaneView extends SurfaceView implements SurfaceHolder.Callback, Runnable {

	private Canvas canvas;
	private SurfaceHolder holder;
	private boolean isDrawing;
	private Timer timer;
	private TimerTask task;
	private ArrayList<EnemyPlane> planes = new ArrayList<EnemyPlane>();
	private ArrayList<Bullet> bullets = new ArrayList<Bullet>();
	private Random random = new Random();

	private int enemyPlaneMaxRightPosition;//敌方飞机最靠右的极限位置
	private int enemyPlaneHeight;

	private int width;
	private int height;

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
		BitmapFactory.decodeResource(UiUtils.getResources(), R.drawable.enemy_plane, options);
		enemyPlaneMaxRightPosition = getWidth() - options.outWidth * (int) (getResources().getDisplayMetrics().density);
		enemyPlaneHeight = options.outHeight * (int) (getResources().getDisplayMetrics().density);
		Log.e("yao", "enemyPlaneHeight:" + enemyPlaneHeight);
		width = getWidth();
		height = getHeight();
		Log.e("yao", "getHeight():" + getHeight());

		isDrawing = true;
		timer = new Timer();
		task = new TimerTask() {
			@Override
			public void run() {
				continueInitEnemyPlane();

				continueInitBullet();

				Paint paint = new Paint();
				Canvas canvas = holder.lockCanvas();
				canvas.drawColor(Color.WHITE);
				Iterator<EnemyPlane> itPlane = planes.iterator();
				while (itPlane.hasNext()) {
					EnemyPlane ep = itPlane.next();
					if (ep.y > height) {
						itPlane.remove();
					} else if (ep.isHit(bullets)){
						itPlane.remove();
					} else {
						ep.draw(canvas, paint);
						
					}
				}

				Iterator<Bullet> itBullet = bullets.iterator();
				while (itBullet.hasNext()) {
					Bullet bullet = itBullet.next();
					if (bullet.y + bullet.height < 0) {
						itBullet.remove();
					} else {
						bullet.draw(canvas, paint);
					}
				}

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

	private void continueInitEnemyPlane() {
		if (System.currentTimeMillis() % 5 == 0) {
			int x = random.nextInt(enemyPlaneMaxRightPosition);
			EnemyPlane ep = new EnemyPlane(x, -enemyPlaneHeight);
			planes.add(ep);
			//Log.i("yao", "planes.size():"+planes.size());
		}
	}

	int bulletInterval = 0;
	private void continueInitBullet() {
		if (bulletInterval % 1000 == 0) {
			Bullet bullet = new Bullet(width / 2, height);
			bullets.add(bullet);
			Log.e("yao", "bullets.size():" + bullets.size());
			bulletInterval = 1;
		}
		bulletInterval ++;
	}

}
