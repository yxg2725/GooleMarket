package com.itheima.googlemarket;

import java.util.ArrayList;
import java.util.List;

import android.os.Bundle;
import android.support.v4.app.ActionBarDrawerToggle;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.view.MenuItem;

import com.itheima.googlemarket.adapter.MainAdapter;
import com.itheima.googlemarket.base.BaseFragment;
import com.itheima.googlemarket.fragment.HomeFragment;
import com.itheima.googlemarket.fragment.APPFragment;
import com.itheima.googlemarket.fragment.GameFragment;
import com.itheima.googlemarket.fragment.SubjectFragment;
import com.itheima.googlemarket.fragment.RecommendFragment;
import com.itheima.googlemarket.fragment.CategoryFragment;
import com.itheima.googlemarket.fragment.TopFragment;
import com.itheima.googlemarket.utils.Logger;

public class MainActivity extends ActionBarActivity {

	
	private ActionBarDrawerToggle toggle;
	private DrawerLayout drawerLayout;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		//在标题栏增加个菜单
		initActionBar();
		initViewAndTab();
	}


	//初始化actionBar
	private void initActionBar() {
		
		// 获取actionBar
		ActionBar actionBar = getSupportActionBar();
		
		//设置左上角的返回按钮
		actionBar.setDisplayHomeAsUpEnabled(true);
		//获取抽屉布局
		drawerLayout = (DrawerLayout) findViewById(R.id.drawerlayout);
		
		//创建一个actionBar开关
		toggle = new ActionBarDrawerToggle(this, drawerLayout, R.drawable.ic_drawer_am, 0, 0);
		//设置左上角为菜单图标
		toggle.syncState();
		//抽屉滑动监听
		drawerLayout.setDrawerListener(toggle);
		
	}

	private void initViewAndTab() {
		ViewPager viewPager = (ViewPager) findViewById(R.id.view_pager);

		//创建一个集合用来 封装fragment的
		fragments = new ArrayList<Fragment>();
		fragments.add(new HomeFragment());
		fragments.add(new APPFragment());
		fragments.add(new GameFragment());
		fragments.add(new SubjectFragment());
		fragments.add(new RecommendFragment());
		fragments.add(new CategoryFragment());
		fragments.add(new TopFragment());
		
		//显示
		viewPager.setAdapter(new MainAdapter(getSupportFragmentManager(), fragments));
		///设置viewpager预加载的数
		viewPager.setOffscreenPageLimit(fragments.size() - 1);
		
		PagerSlidingTabStrip tabs = (PagerSlidingTabStrip) findViewById(R.id.tabs);
		tabs.setOnPageChangeListener(listener);
		tabs.setViewPager(viewPager);
		
	}
	
	private List<Integer> positions = new  ArrayList<Integer>();
	//page切换监听事件
	 private OnPageChangeListener listener = new OnPageChangeListener() {
		
		@Override
		public void onPageSelected(int position) {
			//当Page切换时在联网操作   当加载了哪个界面时 就把哪个界面的position加载到一个集合 下下返回到这个界面时就不在再次联网了
			if (!positions.contains(position)) {
				positions.add(position);
				//联网显示数据
				BaseFragment fragment = (BaseFragment) fragments.get(position);
				fragment.initData();
			}
			
		}
		
		@Override
		public void onPageScrolled(int arg0, float arg1, int arg2) {}
		@Override
		public void onPageScrollStateChanged(int arg0) {}
	};
	private List<Fragment> fragments;
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		if (toggle.onOptionsItemSelected(item)) {
			return true;///表示消费了此事件
		}
		return super.onOptionsItemSelected(item);
	}

}
