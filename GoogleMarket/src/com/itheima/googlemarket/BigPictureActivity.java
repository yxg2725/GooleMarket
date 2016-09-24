package com.itheima.googlemarket;

import java.util.ArrayList;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.view.ViewPager;

import com.itheima.googlemarket.interfaces.Keys;

public class BigPictureActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		//二话不说先获取intent传递过来的数据
		int position = getIntent().getIntExtra(Keys.POSITION , 0);
		//图片地址
		ArrayList<String> screenUrls = getIntent().getStringArrayListExtra(Keys.SCREEN_URLS);
		
		//展示数据  到一个viewPager上
		ViewPager viewPager = new HackyViewPager(this);//第三方自定义的viewpager的子控件
		
		viewPager.setAdapter(new ViewPagerAdapter(screenUrls));
		viewPager.setCurrentItem(position);
		
		setContentView(viewPager);
		
		
	}

	

}
