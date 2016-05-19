/**
 * FileName:PowerfulPullToRefreshListView.java
 */
package com.yao.powerfulpulltorefresh.view;

import java.text.SimpleDateFormat;
import java.util.Date;

import android.animation.ValueAnimator;
import android.animation.ValueAnimator.AnimatorUpdateListener;
import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewTreeObserver.OnGlobalLayoutListener;
import android.view.animation.Animation;
import android.view.animation.RotateAnimation;
import android.widget.AbsListView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.yao.powerfulpulltorefresh.R;
import com.yao.powerfulpulltorefresh.util.UiUtils;

/**
 * @author YaoDiWei
 * @version
 * @see com.yao.powerfulpulltorefresh.view.SimplePullToRefreshListView.java
 */
/**
 * @author Administrator
 *
 */
/**
 * @author Administrator
 *
 */
public class GamePullToRefreshListView extends ListView {
	private static final int PULL_TO_REFRESH = 0;
	private static final int RELEASE_TO_REFRESH = 1;
	private static final int REFRESHING = 2;
	private static final int REFRESHED = 3;

	private int downStatus = PULL_TO_REFRESH;
	private int upStatus = PULL_TO_REFRESH;
	
	private ImageView ivArrow;
	private ProgressBar pbRotate;
	private TextView tvStatus;
	private TextView tvTime;
	private Button btnBack;
	private SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

	private ListAdapter adapter;

	private View headerView;
	private int headerViewHeight;//整个headerView的高度  即为控件的高度
	private int notifyHeaderViewHeight;//通知部分的高度
	private int gameHeaderViewHeight;//飞机游戏部分的高度
	private PlaneView planeView;
	
	private View footerView;
	private int footerViewHeight;
	
	private int paddingTop = 0;
	
	private int height;

	
	
	private RotateAnimation pull = new RotateAnimation(0, 180, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
	private RotateAnimation release = new RotateAnimation(180, 360, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);

	{
		pull.setDuration(200);
		pull.setFillAfter(true);
		release.setDuration(200);
		release.setFillAfter(true);
	}
	
	// 根据设置自由选择平滑移动还是瞬间移动
	private boolean isSmoothMovement = true;
	
	public void setSmoothMovement (boolean isSmoothMovement) {
		this.isSmoothMovement = isSmoothMovement;
	}
	
	//加载完成依然保持游戏界面
	private boolean isCompleteStillPlayingGame = true;
	
	
	
	

	public GamePullToRefreshListView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		initView(context);
	}

	public GamePullToRefreshListView(Context context, AttributeSet attrs) {
		super(context, attrs);
		initView(context);
	}

	public GamePullToRefreshListView(Context context) {
		super(context);
		initView(context);
	}

	private void initView(Context context) {
		footerView = initFooterView(context);
		addFooterView(footerView);
		
		this.getViewTreeObserver().addOnGlobalLayoutListener(new OnGlobalLayoutListener() {
			
			@Override
			public void onGlobalLayout() {
				GamePullToRefreshListView.this.getViewTreeObserver().removeOnGlobalLayoutListener(this);
				height = getHeight();
				headerView = initHeaderView(getContext());
				addHeaderView(headerView);
			}
		});

		// 方案二: 滚动闲置时候就执行上拉加载
		/*this.setOnScrollListener(new OnScrollListener() {

			@Override
			public void onScrollStateChanged(AbsListView view, int scrollState) {
				if (scrollState == OnScrollListener.SCROLL_STATE_IDLE && (view.getLastVisiblePosition() == view.getCount() - 1) && upStatus == PULL_TO_REFRESH) {
					Log.e("yao", "上拉加载:" + getCount());
					upStatus = REFRESHING;
					footerView.setPadding(0, 0, 0, 0);
					setSelection(getCount() - 1);
					footerView.postDelayed(new Runnable() {

						@Override
						public void run() {
							upStatus = PULL_TO_REFRESH;
							footerView.setPadding(0, 0, 0, -footerViewHeight);
						}
					}, 1000);
				}
			}

			@Override
			public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
			}
		});*/

	}

	private View initHeaderView(Context context) {
		final LinearLayout linearLayout = (LinearLayout) View.inflate(context, R.layout.view_header_plane, null);
		ivArrow = (ImageView) linearLayout.findViewById(R.id.ivArrow);
		pbRotate = (ProgressBar) linearLayout.findViewById(R.id.pbRotate);
		tvStatus = (TextView) linearLayout.findViewById(R.id.tvStatus);
		tvTime = (TextView) linearLayout.findViewById(R.id.tvTime);
		tvTime.setText(sdf.format(new Date()));
		checkSwitchButton = (CheckSwitchButton) linearLayout.findViewById(R.id.checkSwithcButton);
		checkSwitchButton.setOnCheckedChangeListener(new OnCheckedChangeListener() {
			
			@Override
			public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
				if(isChecked){
					isCompleteStillPlayingGame = true;
				}else{
					isCompleteStillPlayingGame = false;
				}
			}
		});
		btnBack = (Button) linearLayout.findViewById(R.id.btnBack);
		btnBack.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				if (downStatus == REFRESHED) {
//					linearLayout.setPadding(0, -height, 0, 0);
					startMyValueAnimator(linearLayout, true, 0, -height);
					downStatus = PULL_TO_REFRESH;
					planeView.switchGame(false);
				}
			}
		});
		
		linearLayout.measure(0, 0);
		notifyHeaderViewHeight = linearLayout.getMeasuredHeight();
		headerViewHeight = height;
		gameHeaderViewHeight = height - notifyHeaderViewHeight;
		
		planeView = new PlaneView(context);
		LinearLayout.LayoutParams lp2 = new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT, height - notifyHeaderViewHeight);
		planeView.setLayoutParams(lp2);
		linearLayout.addView(planeView, 0);
//		view.setPadding(0, -height, 0, 0);
		
		linearLayout.setPadding(0, -height, 0, 0);
		
		return linearLayout;
	}
	
	private View initFooterView(Context context) {
		View view = View.inflate(context, R.layout.view_footer, null);
		view.measure(0, 0);
		footerViewHeight = view.getMeasuredHeight();
		Log.e("yao", "footerViewHeight:" + footerViewHeight);
		view.setPadding(0, 0, 0, -footerViewHeight);
		return view;
	}

	@Override
	public void setAdapter(ListAdapter adapter) {
		this.adapter = adapter;
		super.setAdapter(adapter);
	}

	// 用于记录按下位置 和 headView未显示出来时候的最后位置(可用来计算offsetY)
	private int startY = 0;
	// 用于记录按下位置
	private int downY = 0;

	@Override
	public boolean onTouchEvent(MotionEvent ev) {
		Log.e("yao", "main onTouchEvent:"+downStatus);
		if (downStatus == REFRESHED || downStatus == REFRESHING) {
			return false;
		}
		switch (ev.getAction()) {
		case MotionEvent.ACTION_DOWN:
			startY = (int) ev.getY();
			downY = (int) ev.getY();
			break;
		case MotionEvent.ACTION_MOVE:
			// 如果头布局可见 且 属于下拉或松开刷新状态,则进入判断下拉刷新的逻辑
			if (getFirstVisiblePosition() == 0 && (downStatus == PULL_TO_REFRESH || downStatus == RELEASE_TO_REFRESH)) {
				// 根据当前手指位置 - headView未显示出来时候的最后位置 , 得到Y的偏移
				int offsetY = (int) ev.getY() - startY;
				// 只有offsetY为正数,就拦截事件,不执行listView的滚动了, 只执行headerView.setPadding来模拟滚动
				if (offsetY > 0) {
					// 让headerView随手指慢慢滑出
					paddingTop = -headerViewHeight + offsetY;
					headerView.setPadding(0, paddingTop, 0, 0);

					// 如果headerView整个都滑出 且 处于下拉刷新状态
					// 更改成松开刷新状态
					if (-notifyHeaderViewHeight + offsetY > 0 && downStatus == PULL_TO_REFRESH) {
						downStatus = RELEASE_TO_REFRESH;
						tvStatus.setText("松开刷新");
						ivArrow.clearAnimation();
						ivArrow.startAnimation(pull);
					}
					// 如果headerView没有整个都滑出 且 处于松开刷新状态
					// 更改成下拉刷新状态
					if (-notifyHeaderViewHeight + offsetY < 0 && downStatus == RELEASE_TO_REFRESH) {
						downStatus = PULL_TO_REFRESH;
						tvStatus.setText("下拉刷新");
						ivArrow.clearAnimation();
						ivArrow.startAnimation(release);
					}
					return true;// 拦截事件
				}
				// 如果头布局不可见,则更新downY的位置
			} else {
				startY = (int) ev.getY();
			}
			break;
		case MotionEvent.ACTION_UP:
			// 松开手指,如果是处于松开刷新状态,则执行刷新逻辑
			if (downStatus == RELEASE_TO_REFRESH) {
				pullDownRefreshing();
				// 否则重新隐藏头布局
			} else {
				if (isSmoothMovement && paddingTop!=-headerViewHeight) {
					startMyValueAnimator(headerView, true, paddingTop, -headerViewHeight);
					paddingTop = -headerViewHeight;//不赋这个值,下拉一次后,每次点击列表都会进入动画
				} else {
					headerView.setPadding(0, -headerViewHeight, 0, 0);
				}
			}

			// 方案一: 松开手时候判断条件,为真执行上拉加载
			int offsetY = (int) ev.getY() - downY;
			if (getLastVisiblePosition() == getCount() - 1 && upStatus == PULL_TO_REFRESH && offsetY < 0) {
				pullUpRefreshing();
			}
			break;
		}
		return super.onTouchEvent(ev);
	}

	private void pullDownRefreshing() {
		planeView.switchGame(true);
		downStatus = REFRESHING;
		// 先清除动画,才能设置不可见
		ivArrow.clearAnimation();
		ivArrow.setVisibility(View.GONE);
		pbRotate.setVisibility(View.VISIBLE);
		tvStatus.setText("刷新中");
		if (onRefreshListener == null) {
			postDelayed(new Runnable() {
				@Override
				public void run() {
					onComplete(true);
				}
			}, 1000);
		} else {
			onRefreshListener.onPullDownToRefresh();
		}
		if (isSmoothMovement) {
			startMyValueAnimator(headerView, true, paddingTop, 0);
			paddingTop = -headerViewHeight;//不赋这个值,下拉一次后,每次点击列表都会进入动画
		} else {
			headerView.setPadding(0, 0, 0, 0);
		}
	}

	private void pullUpRefreshing() {
		Log.e("yao", "上拉加载:" + getCount());
		upStatus = REFRESHING;
		if (onRefreshListener == null) {
			postDelayed(new Runnable() {
				@Override
				public void run() {
					onComplete(false);
				}
			}, 1000);
		} else {
			onRefreshListener.onPullUpToRefresh();
		}
		if (isSmoothMovement && footerViewHeight!=0) {
			startMyValueAnimator(footerView, false, -footerViewHeight, 0);
		} else {
			footerView.setPadding(0, 0, 0, 0);
			setSelection(getCount() - 1);
		}
	}

	
	/**
	 * @param isPullDown true表示下拉刷新, false表示上拉加载
	 */
	public void onComplete(boolean isPullDown) {
		if (isPullDown) { // 是下拉刷新
			downStatus = REFRESHED;
			//TODO 改成完成刷新(玩游戏状态)
			tvStatus.setText("完成刷新");
			ivArrow.clearAnimation();
			ivArrow.setVisibility(View.VISIBLE);
			pbRotate.setVisibility(View.GONE);
			tvTime.setText(sdf.format(new Date()));
			if (adapter instanceof BaseAdapter) {
				((BaseAdapter) adapter).notifyDataSetChanged();
			}
			if (isCompleteStillPlayingGame) {
				
			} else {
				if (isSmoothMovement) {
					startMyValueAnimator(headerView, true, 0, -headerViewHeight);
				} else {
					headerView.setPadding(0, -headerViewHeight, 0, 0);
				}
			}
		} else { // 是上拉加载
			upStatus = PULL_TO_REFRESH;
			if (isSmoothMovement && 0!=footerViewHeight) {
				startMyValueAnimator(footerView, false, 0, -footerViewHeight);
			} else {
				footerView.setPadding(0, 0, 0, -footerViewHeight);
			}
			if (adapter instanceof BaseAdapter) {
				((BaseAdapter) adapter).notifyDataSetChanged();
			}
		}
	}

	
	/**
	 * @param view 需要移动的控件
	 * @param isPaddingTop true表示下拉刷新, false表示上拉加载
	 * @param value1 从value1值
	 * @param value2 到value2值
	 */
	private void startMyValueAnimator(final View view, final boolean isPaddingTop, int value1, int value2) {
		ValueAnimator animator = ValueAnimator.ofInt(value1, value2);
		animator.addUpdateListener(new AnimatorUpdateListener() { // 监听值的变化

			@Override
			public void onAnimationUpdate(ValueAnimator animator) {
				int value = (Integer) animator.getAnimatedValue();// 运行当前时间点的一个值
				if (isPaddingTop) {
					view.setPadding(0, value, 0, 0);
				} else {
					view.setPadding(0, 0, 0, value);
					setSelection(getCount() - 1);
				}
			}
		});
		animator.setDuration(100);
		animator.start();
	}

	private OnRefreshListener onRefreshListener;
	private CheckSwitchButton checkSwitchButton;
	

	public void setOnRefreshListener(OnRefreshListener onRefreshListener) {
		this.onRefreshListener = onRefreshListener;
	}

	public static interface OnRefreshListener {
		void onPullDownToRefresh();

		void onPullUpToRefresh();
	}

}
