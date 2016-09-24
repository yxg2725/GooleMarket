package com.itheima.googlemarket.adapter;

import java.util.List;

import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.itheima.googlemarket.R;
import com.itheima.googlemarket.bean.CategoryInfo;
import com.itheima.googlemarket.bean.CategoryInfo.Info;
import com.itheima.googlemarket.http.Urls;
import com.itheima.googlemarket.utils.UiUtils;
import com.nostra13.universalimageloader.core.ImageLoader;


public class CategoryListAdapter extends MyBaseAdapter<Object> {

	private static final int TYPE_TITLE = 0;
	private static final int TYPE_INFO = 1;

	public CategoryListAdapter(List<Object> datas) {
		super(datas);
	}

	//设置有几种数据类型
	@Override
	public int getViewTypeCount() {
		return 2;
	}
	
	//返回指定的position是哪种数据类型
	@Override
	public int getItemViewType(int position) {
		Object object = datas.get(position);
		return object instanceof String ? TYPE_TITLE : TYPE_INFO;
	}
	
	
	
	@Override
	public void showData(Object holder, Object data, int position) {

		ViewHolder mholder = (ViewHolder) holder;
		
		//判断数据类型
		int type = getItemViewType(position);
		if (type == TYPE_TITLE) {
			mholder.title.setText((String) data);
			int pad = (position == 0?6:0);
			mholder.title.setPadding(6 , pad, 0, 0);
		}else {
			Info info = (Info) data;
			//展示一行中的第一个分类
			mholder.tv_name1.setText(info.name1);
			ImageLoader.getInstance().displayImage(Urls.IMAGE + "?name=" + info.url1, mholder.iv_icon1);
			
			//展示一行中的第二个分类   因为有可能不存在所以要进行判断
			if (TextUtils.isEmpty(info.name2)) {//为空
				mholder.ll_2.setVisibility(View.INVISIBLE);
			}else {
				mholder.ll_2.setVisibility(View.VISIBLE);
				mholder.tv_name2.setText(info.name2);
				ImageLoader.getInstance().displayImage(Urls.IMAGE + "?name=" + info.url2, mholder.iv_icon2);
			}
			
			//展示一行中的第三个分类   因为有可能不存在所以要进行判断
			if (TextUtils.isEmpty(info.name3)) {//为空
				mholder.ll_3.setVisibility(View.INVISIBLE);
			}else {
				mholder.ll_3.setVisibility(View.VISIBLE);
				mholder.tv_name3.setText(info.name3);
				ImageLoader.getInstance().displayImage(Urls.IMAGE + "?name=" + info.url3, mholder.iv_icon3);
			}
			
			
			//把name数据绑定到各自的ll中 为了点解这个ll 弹出对应的数据的吐司
			mholder.ll_1.setTag(info.name1);
			mholder.ll_2.setTag(info.name2);
			mholder.ll_3.setTag(info.name3);
			
			//设置各个分类的点击事件
			mholder.ll_1.setOnClickListener(listener);
			mholder.ll_2.setOnClickListener(listener);
			mholder.ll_3.setOnClickListener(listener);
		}
		
		
		
	}
	
	OnClickListener listener  = new OnClickListener(){

		@Override
		public void onClick(View v) {
			UiUtils.showToast((String)v.getTag());
		}
		
	};

	@Override
	public Object getHolderAndFindViewById(View convertView, int position) {
		
		ViewHolder holder = new ViewHolder();
		int viewType = getItemViewType(position);
		if (viewType == TYPE_TITLE) {
			holder.title = (TextView) convertView;
		}else {
			holder.ll_1 = (LinearLayout) convertView.findViewById(R.id.ll_1);
			holder.ll_2 = (LinearLayout) convertView.findViewById(R.id.ll_2);
			holder.ll_3 = (LinearLayout) convertView.findViewById(R.id.ll_3);
			
			holder.iv_icon1 = (ImageView) convertView.findViewById(R.id.iv_icon1);
			holder.iv_icon2 = (ImageView) convertView.findViewById(R.id.iv_icon_2);
			holder.iv_icon3 = (ImageView) convertView.findViewById(R.id.iv_icon_3);
			
			holder.tv_name1 = (TextView) convertView.findViewById(R.id.tv_name1);
			holder.tv_name2 = (TextView) convertView.findViewById(R.id.tv_name2);
			holder.tv_name3 = (TextView) convertView.findViewById(R.id.tv_name3);
			
		}
		return holder;
	}

	@Override
	public int getLayoutId(int position) {
		int type = getItemViewType(position);
		return type == TYPE_TITLE ? R.layout.item_category_title : R.layout.item_category_info;
	}
	
	static class ViewHolder {
		TextView title;
			
		LinearLayout ll_1;
		ImageView iv_icon1;
		TextView tv_name1;
			
		LinearLayout ll_2;
		ImageView iv_icon2;
		TextView tv_name2;
			
		LinearLayout ll_3;
		ImageView iv_icon3;
		TextView tv_name3;
	}

}
