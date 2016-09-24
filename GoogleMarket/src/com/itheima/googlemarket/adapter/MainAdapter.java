package com.itheima.googlemarket.adapter;

import java.util.List;

import com.itheima.googlemarket.base.BaseFragment;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

public class MainAdapter extends FragmentPagerAdapter {

	private final List<Fragment> fragments;

	public MainAdapter(FragmentManager fm, List<Fragment> fragments) {
		super(fm);
		this.fragments = fragments;
	}

	@Override
	public int getCount() {
		return fragments.size();
	}
	

	@Override
	public Fragment getItem(int position) {
		
		return fragments.get(position);
	}
	
	@Override
	public CharSequence getPageTitle(int position) {
		BaseFragment baseFragment = (BaseFragment) fragments.get(position);
		return baseFragment.getTitle();
	}

}
