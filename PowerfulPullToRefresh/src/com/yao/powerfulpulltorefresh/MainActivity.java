package com.yao.powerfulpulltorefresh;

import java.util.ArrayList;

import android.app.Activity;
import android.os.Bundle;

import com.yao.powerfulpulltorefresh.view.GamePullToRefreshListView;
import com.yao.powerfulpulltorefresh.view.GamePullToRefreshListView.OnRefreshListener;

public class MainActivity extends Activity {

	private GamePullToRefreshListView lv;
	private ArrayList<String> list;
	private ExampleAdapter adapter;
	private int i = 0;
	private int maxPos = 10;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		lv = (GamePullToRefreshListView) findViewById(R.id.lv);
		list = getDatas();
		adapter = new ExampleAdapter(this);
		adapter.setDatas(list);
		lv.setAdapter(adapter);
		lv.setOnRefreshListener(new OnRefreshListener() {

			@Override
			public void onPullUpToRefresh() {
				new Thread(new Runnable() {

					@Override
					public void run() {
						try {
							Thread.sleep(1000);
						} catch (InterruptedException e) {
							e.printStackTrace();
						}
						list.add("data  " + (maxPos++));
						runOnUiThread(new Runnable() {

							@Override
							public void run() {
								lv.onComplete(false);
							}
						});
					}
				}).start();
			}

			@Override
			public void onPullDownToRefresh() {
				new Thread(new Runnable() {

					@Override
					public void run() {
						try {
							Thread.sleep(1000);
						} catch (InterruptedException e) {
							e.printStackTrace();
						}
						list.add(0, "data new  " + (i++));
						runOnUiThread(new Runnable() {

							@Override
							public void run() {
								lv.onComplete(true);
							}
						});
					}
				}).start();

			}
		});
	}

	
	private ArrayList<String> getDatas() {
		ArrayList<String> list = new ArrayList<String>();
		for (int i = 0; i < maxPos; i++) {
			list.add("data  " + i);
		}
		return list;
	}

}
