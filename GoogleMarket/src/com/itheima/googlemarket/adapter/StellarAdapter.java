package com.itheima.googlemarket.adapter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;

import android.view.View;
import android.view.View.OnClickListener;
import android.widget.TextView;

import com.itheima.googlemarket.MyApp;
import com.itheima.googlemarket.stellarmap.StellarMap.Adapter;
import com.itheima.googlemarket.utils.UiUtils;

public class StellarAdapter extends Adapter {

	private HashMap<Integer, ArrayList<String>> groups;
	private Random random;

	public StellarAdapter(HashMap<Integer, ArrayList<String>> groups) {
		this.groups = groups;
		random = new Random();
	}

	@Override
	public int getGroupCount() {
		return groups.size();
	}

	@Override
	public int getCount(int groupIndex) {
		return groups.get(groupIndex).size();
	}

	@Override
	public View getView(int groupIndex, int position, View convertView) {
		//获取指定索引的组
		ArrayList<String> oneGroup = groups.get(groupIndex);
		//获取组里的数据
		final String string = oneGroup.get(position);
		
		TextView tv = new TextView(MyApp.getContext());
		
		//设置字体大小
		float size = 12 + random.nextInt(16);
		tv.setTextSize(size);
		
		//设置字体的颜色
		int color = UiUtils.createRandomColor();
		tv.setTextColor(color);
		
		tv.setText(string);
		
		//设置TextView的点击监听事件
		tv.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				
				UiUtils.showToast(string);
			}
		});
		return tv;
	}

}
