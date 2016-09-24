package com.itheima.googlemarket.base;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;

import com.itheima.googlemarket.MyApp;
import com.itheima.googlemarket.R;
import com.itheima.googlemarket.bean.SubjectInfo;
import com.itheima.googlemarket.bean.HomeBean.AppInfo;
import com.itheima.googlemarket.http.JsonRequestCallback;
import com.itheima.googlemarket.view.StateFrameLayout;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

public abstract class BaseFragment extends Fragment implements JsonRequestCallback{

	public StateFrameLayout rootView;
	public Context context;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		
		context = getActivity();
		rootView = (StateFrameLayout) inflater.inflate(R.layout.state_layout, null);
		//创建第四种状态布局
		rootView.setContentView(getContentView());
		initView();
		initListener();
		//initData();
		return rootView;
	}

	public <T> T findView(int id){
		@SuppressWarnings("unchecked")
		T view = (T)rootView.findViewById(id);
		return view;
	} 



	public boolean checkData(Collection<?> datas) {
		boolean result = false;
	
		if (datas == null) {
			rootView.showFailView();
		}else if(datas.isEmpty()){
			rootView.showEmptyView();
		}else {
			rootView.showContentView();
			return true;
		}
		return result;
	}
	
	
	//检查数据是否有效
	public boolean checkData(HashMap<?, ?> datas) {
			
			boolean result = false;
			if (datas == null) {
				rootView.showFailView();
			}else if(datas.isEmpty()){
				rootView.showEmptyView();
			}else{
				rootView.showContentView();
				result = true;
			}
			
			return result;
		}
	

	
	public abstract Object getContentView();
	public abstract void initData();
	public abstract CharSequence getTitle();
	public abstract void initListener();
	public abstract void initView();
	
	
}
