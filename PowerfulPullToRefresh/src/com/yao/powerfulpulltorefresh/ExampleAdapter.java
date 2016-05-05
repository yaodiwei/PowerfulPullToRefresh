package com.yao.powerfulpulltorefresh;

import java.util.ArrayList;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

public class ExampleAdapter extends BaseAdapter {

	private LayoutInflater inflater;
	private ArrayList<String> datas;
	
	public void setDatas(ArrayList<String> list) {
		this.datas = list;
	}
	
	public ExampleAdapter(Context context) {
		inflater = LayoutInflater.from(context);
	}

	@Override
	public int getCount() {
		return datas.size();
	}

	@Override
	public Object getItem(int position) {
		return datas.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View view, ViewGroup parent) {
		if (view == null) {
			view = inflater.inflate(android.R.layout.simple_list_item_1, null);
		}
		TextView tv = (TextView) view.findViewById(android.R.id.text1);
		tv.setText(datas.get(position));
		return view;
	}

}
