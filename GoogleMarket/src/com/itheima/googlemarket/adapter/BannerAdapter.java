package com.itheima.googlemarket.adapter;

import java.util.List;

import com.itheima.googlemarket.http.Urls;
import com.nostra13.universalimageloader.core.ImageLoader;

import android.support.v4.view.PagerAdapter;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;

public class BannerAdapter extends PagerAdapter {

	//广告条目的地址
	private List<String> bannerUrl;

	public BannerAdapter(List<String> bannerUrl) {
		this.bannerUrl = bannerUrl;
	}

	@Override
	public int getCount() {
		return bannerUrl.size() * 10 * 10000;
	}

	@Override
	public boolean isViewFromObject(View arg0, Object arg1) {
		return arg0 == arg1;
	}
	
	@Override
	public Object instantiateItem(ViewGroup container, int position) {
		int currentPosition = position % bannerUrl.size();
		
		//显示viewpager上的图片
		ImageView imageView = new ImageView(container.getContext());
		
		imageView.setScaleType(ScaleType.FIT_XY);
		//将控件添加进来
		container.addView(imageView);
		
		//图片地址
		String picUrl = Urls.IMAGE + "?name=" + bannerUrl.get(currentPosition);
		ImageLoader.getInstance().displayImage(picUrl, imageView);
		
		return imageView;//返回的时展示图片的控件
	}
	
	@Override
	public void destroyItem(ViewGroup container, int position, Object object) {
		container.removeView((View) object);
	}
	

}
