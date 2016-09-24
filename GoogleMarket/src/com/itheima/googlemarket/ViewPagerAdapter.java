package com.itheima.googlemarket;

import java.util.ArrayList;

import uk.co.senab.photoview.PhotoView;

import com.itheima.googlemarket.http.Urls;
import com.nostra13.universalimageloader.core.ImageLoader;

import android.support.v4.view.PagerAdapter;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

public class ViewPagerAdapter extends PagerAdapter {

	private ArrayList<String> screenUrls;

	public ViewPagerAdapter(ArrayList<String> screenUrls) {
		this.screenUrls = screenUrls;
	}

	@Override
	public int getCount() {
		return screenUrls.size();
	}

	@Override
	public boolean isViewFromObject(View arg0, Object arg1) {
		return arg0 == arg1;
	}
	
	@Override
	public Object instantiateItem(ViewGroup container, int position) {
		
		ImageView imageView  = new PhotoView(container.getContext());
		container.addView(imageView);//这一步很重要---------------------
				 
		//获取图片地址
		String picUrl = Urls.IMAGE + "?name=" + screenUrls.get(position);
		
		//展示
		ImageLoader.getInstance().displayImage(picUrl, imageView);
		
		return imageView;
	}
	
	@Override
	public void destroyItem(ViewGroup container, int position, Object object) {
		container.removeView((View) object);
	}

}
