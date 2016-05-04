/**
 * FileName:UiUtils.java
 * Copyright(C) 2016 zteict
 */
package com.yao.powerfulpulltorefresh.util;

import android.content.Context;
import android.content.res.Resources;

import com.yao.powerfulpulltorefresh.BaseApplication;

/**
 * @author YaoDiWei
 * @version
 * @see com.yao.powerfulpulltorefresh.util.UiUtils.java
 */
public class UiUtils {

	public static Context getContext() {
		return BaseApplication.getApplication();
	}

	public static Resources getResources() {
		return BaseApplication.getApplication().getResources();
	}
}
