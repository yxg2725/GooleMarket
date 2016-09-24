package com.itheima.googlemarket.fragment;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Date;
import java.util.TreeMap;

import android.text.format.DateFormat;
import android.widget.ListView;

import com.google.gson.reflect.TypeToken;
import com.handmark.pulltorefresh.library.ILoadingLayout;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshBase.Mode;
import com.handmark.pulltorefresh.library.PullToRefreshBase.OnRefreshListener;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.itheima.googlemarket.R;
import com.itheima.googlemarket.adapter.APPListAdapter;
import com.itheima.googlemarket.base.BaseFragment;
import com.itheima.googlemarket.bean.HomeBean.AppInfo;
import com.itheima.googlemarket.http.NetUtil;
import com.itheima.googlemarket.http.Urls;
import com.itheima.googlemarket.utils.JsonUtils;

public class APPFragment extends BaseFragment {

	private PullToRefreshListView mLv;
	private APPListAdapter adapter;
	private ILoadingLayout footerView;
	private ILoadingLayout headView;

	@Override
	public void initData() {
		
		//获取数据之前先判断是上啦还是下拉的数据
		int index = 0;
		if (mLv.getCurrentMode() == Mode.PULL_FROM_START) {//下拉
			index = 0;
		}else {
			index = adapter.getCount();//表示上啦加载 
		}
		//从网络获取数据
		//rootView.showContentView();
		TreeMap<String,String> params = new TreeMap<String, String>();
		params.put("index", index+"");
		
		NetUtil.requestData(Urls.APP,params,this);
	}
	
	/**请求数据完成回调函数*/
	@Override
	public void onRequestFinish(String json) {
		
		//获取返回的json数据 解析
		///HomeBean homeBean = JsonUtils.json2Bean(json, HomeBean.class);
		
		Type type = new TypeToken<ArrayList<AppInfo>>(){}.getType();
		ArrayList<AppInfo> appInfo = JsonUtils.json2Collection(json, type);
		//获取解析完成后的数据
		/*List<AppInfo> appInfo = null;
		if (homeBean  != null) {
			appInfo = homeBean.list;
		}*/
		
		//请求完数据后可以通知lv关闭刷新头
		mLv.onRefreshComplete();
		
		if (mLv.getCurrentMode() == Mode.PULL_FROM_START) {
			//检查数据
			if (checkData(appInfo)) {//如果检查数据为true
				
				// 把数据添加到list集合 更新数据
				adapter.getDatas().clear();
				adapter.getDatas().addAll(appInfo);
				adapter.notifyDataSetChanged();
				
				CharSequence lastUpdatedTime = DateFormat.format("最后刷新时间: MM-dd kk:mm:ss", new Date());
				//添加刷新时间
				headView.setLastUpdatedLabel(lastUpdatedTime);
			}
			
		}else {//如果是加载更多 则适配器的集合 不清空 直接添加数据
			if (appInfo != null || !appInfo.isEmpty() ) {
				adapter.getDatas().addAll(appInfo);
				adapter.notifyDataSetChanged();
			}
			
		}
		
	}

	@Override
	public CharSequence getTitle() {
		return "应用";
		
	}

	@Override
	public void initListener() {
		mLv.setOnRefreshListener(new OnRefreshListener<ListView>() {

			@Override
			public void onRefresh(PullToRefreshBase<ListView> refreshView) {
				//加载数据
				initData();
				
			}
		});
	}

	@Override
	public void initView() {
		mLv = findView(R.id.home_listview);
		
		adapter = new APPListAdapter(null);
		mLv.setAdapter(adapter);
		
		//初始化下拉和上啦
		initHeadAndFooter();
		
	}

	@SuppressWarnings("deprecation")
	private void initHeadAndFooter() {
		//设置模式可以上啦 也可以下拉
		mLv.setMode(Mode.BOTH);
		//设置下拉
		headView = mLv.getLoadingLayoutProxy(true, false);
		headView.setPullLabel("下拉以刷新");
		headView.setReleaseLabel("松开以刷新");
		headView.setRefreshingLabel("正在刷新...");
		
		
		//设置上啦
		footerView = mLv.getLoadingLayoutProxy(false, true);
		footerView.setPullLabel("上拉加载更多");
		footerView.setReleaseLabel("松开以加载更多");
		footerView.setRefreshingLabel("正在加载更多...");
	}


	//里面是一个ListView
	@Override
	public Object getContentView() {
		/*TextView text = new TextView(context);
		text.setText("这是正常界面的内容");
		text.setGravity(Gravity.CENTER);*/
		
		return R.layout.homefragmet;
	}



}
