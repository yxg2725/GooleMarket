package com.itheima.googlemarket.adapter;

import java.util.List;

import android.content.Context;
import android.text.format.Formatter;
import android.view.View;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.itheima.googlemarket.R;
import com.itheima.googlemarket.bean.HomeBean.AppInfo;
import com.itheima.googlemarket.http.Urls;
import com.nostra13.universalimageloader.core.ImageLoader;

public class APPListAdapter extends MyBaseAdapter<AppInfo> {

	public APPListAdapter(List<AppInfo> datas) {
		super(datas);
	}

	@Override
	public int getLayoutId(int position) {
		return R.layout.appinfo_list_item;
	}
	
	@Override
	public Object getHolderAndFindViewById(View convertView, int position) {
		ViewHolder holder = new ViewHolder();
		holder.appIcon = (ImageView) convertView.findViewById(R.id.app_icon);
		holder.appName = (TextView) convertView.findViewById(R.id.app_name);
		holder.appSize = (TextView) convertView.findViewById(R.id.app_size);
		holder.appDesc = (TextView) convertView.findViewById(R.id.app_desc);
		holder.appDown = (TextView) convertView.findViewById(R.id.app_down);
		holder.ratingBar = (RatingBar) convertView.findViewById(R.id.rating_bar);
		return holder;
	}
	
	@Override
	public void showData(Object holder, AppInfo data, int position) {
		
		ViewHolder viewholder = (ViewHolder) holder;
		Context context = viewholder.appSize.getContext();
		//获取数据
		viewholder.appName.setText(data.name);
		viewholder.appSize.setText(Formatter.formatFileSize(context , data.size));
		viewholder.appDesc.setText(data.des);
		viewholder.ratingBar.setRating(data.stars);
		
		//显示图片
		//图片地址
		String iconUrl = Urls.IMAGE + "?name=" + data.iconUrl;
		ImageLoader.getInstance().displayImage(iconUrl, viewholder.appIcon);
	}
	
	static class ViewHolder{
		ImageView appIcon;
		TextView appName;
		TextView appSize;
		TextView appDesc;
		TextView appDown;
		RatingBar ratingBar;
		
		
	}


	
	

}
