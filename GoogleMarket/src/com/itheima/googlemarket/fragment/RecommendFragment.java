package com.itheima.googlemarket.fragment;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;

import android.graphics.drawable.StateListDrawable;
import android.view.Gravity;
import android.widget.TextView;

import com.google.gson.reflect.TypeToken;
import com.itheima.googlemarket.adapter.StellarAdapter;
import com.itheima.googlemarket.base.BaseFragment;
import com.itheima.googlemarket.http.NetUtil;
import com.itheima.googlemarket.http.Urls;
import com.itheima.googlemarket.stellarmap.StellarMap;
import com.itheima.googlemarket.utils.JsonUtils;
import com.itheima.googlemarket.utils.UiUtils;

public class RecommendFragment extends BaseFragment {

	private StellarMap stellar;

	@Override
	public void initData() {
		NetUtil.requestData(Urls.RECOMMEND, null, this);
	}

	@Override
	public CharSequence getTitle() {
		return "推荐";
		
	}

	@Override
	public void initListener() {
	}

	@Override
	public void initView() {
	}

	@Override
	public Object getContentView() {
	/*	TextView text = new TextView(context);
		text.setText("这是正常界面的内容");
		text.setGravity(Gravity.CENTER);*/
		
		stellar = new StellarMap(context);
		int pading = UiUtils.dp2px(6);
		stellar.setInnerPadding(pading, pading, pading, pading);
		
		return stellar;
	}

	/**请求数据完毕 得到数据*/
	@Override
	public void onRequestFinish(String json) {
		Type type = new TypeToken<HashMap<Integer, ArrayList<String>> >(){}.getType();
		HashMap<Integer, ArrayList<String>>  datas = JsonUtils.json2Collection(json, type );
		
		//检查数据是否有效
		if (checkData(datas)) {
			stellar.setAdapter(new StellarAdapter(datas));
			
			//遍历集合中的每一个组 判断每个组里的数据个数大于0 后将这个个数设置给密度
			int max = 0;
			for ( Integer key : datas.keySet()) {//遍历的是键的集合      序号
				//获取每个组的小
				int size = datas.get(key).size();//更具键获取值
				if (size > max) {
					max = size;
				}
			}
			
			//设置展示的数据条数规格密度
			stellar.setRegularity(max, max);
		}
	}

	
	

}
