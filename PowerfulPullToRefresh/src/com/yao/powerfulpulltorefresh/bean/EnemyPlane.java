package com.yao.powerfulpulltorefresh.bean;

import android.graphics.BitmapFactory;
import android.util.Log;

import com.yao.powerfulpulltorefresh.R;
import com.yao.powerfulpulltorefresh.util.UiUtils;

/**
 * @author YaoDiWei
 * @version 
 * @see com.yao.powerfulpulltorefresh.EnemyPlane.java
 */
public class EnemyPlane extends GameObject {

	
	
	
	
	public EnemyPlane() {
		super();
		resId = R.drawable.enemy_plane;
		bitmap = BitmapFactory.decodeResource(UiUtils.getResources(), resId);
		width= bitmap.getWidth();
		height = bitmap.getHeight();
		Log.e("yao", "EnemyPlane width:" + width);
		Log.e("yao", "EnemyPlane height:" + height);
	}

	
}
