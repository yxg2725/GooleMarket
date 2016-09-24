package com.itheima.googlemarket.adapter;

import java.util.ArrayList;
import java.util.List;

import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

public abstract class MyBaseAdapter<T> extends BaseAdapter {
	public  List<T> datas;
	public MyBaseAdapter(List<T> datas) {
		this.datas = datas;
	}

	public List<T>  getDatas(){
		if (datas == null) {
			datas = new ArrayList<T>();
		}
		return datas;
	}
	@Override
	public int getCount() {
		return datas == null?0:datas.size();
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
	public View getView(int position, View convertView, ViewGroup parent) {
		Object holder = null ;
		if (convertView == null) {
			convertView = View.inflate(parent.getContext(), getLayoutId(position), null);
			holder = getHolderAndFindViewById(convertView,position);
			convertView.setTag(holder);
		} else {
			holder = convertView.getTag();
		}
		
		T data = datas.get(position);
		
		showData(holder,data,position);
		return convertView;
	}

	public abstract void showData(Object holder, T data, int position);

	public abstract Object getHolderAndFindViewById(View convertView, int position);

	public abstract int getLayoutId(int position);
	
	

}
