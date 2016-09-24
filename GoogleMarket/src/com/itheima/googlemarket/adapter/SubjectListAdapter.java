package com.itheima.googlemarket.adapter;

import java.util.List;

import com.itheima.googlemarket.R;
import com.itheima.googlemarket.bean.SubjectInfo;
import com.itheima.googlemarket.http.Urls;
import com.nostra13.universalimageloader.core.ImageLoader;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

public class SubjectListAdapter extends MyBaseAdapter<SubjectInfo> {

	public SubjectListAdapter(List<SubjectInfo> datas) {
		super(datas);
	}
	

	@Override
	public void showData(Object holder, SubjectInfo data, int position) {
		ViewHolder viewHolder = (ViewHolder) holder;
		
		//显示描述
		viewHolder.tvDesc.setText(data.des);
		
		String picUrl = Urls.IMAGE + "?name=" + data.url;
		//显示图片
		ImageLoader.getInstance().displayImage(picUrl, viewHolder.iv);
		
	}

	@Override
	public Object getHolderAndFindViewById(View convertView, int position) {
		
		ViewHolder holder = new ViewHolder();
		holder.tvDesc = (TextView) convertView.findViewById(R.id.subject_desc);
		holder.iv = (ImageView) convertView.findViewById(R.id.subject_image);
		
		return holder;
	}

	@Override
	public int getLayoutId(int position) {
		return R.layout.item_subject_list;
	}
	
	class ViewHolder{
		ImageView iv;
		TextView tvDesc;
	}

	

}
