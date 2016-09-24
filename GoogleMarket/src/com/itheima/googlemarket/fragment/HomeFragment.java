package com.itheima.googlemarket.fragment;

import java.util.Date;
import java.util.List;
import java.util.TreeMap;

import android.content.Intent;
import android.os.Handler;
import android.support.v4.view.ViewPager;
import android.text.format.DateFormat;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;

import com.handmark.pulltorefresh.library.ILoadingLayout;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshBase.Mode;
import com.handmark.pulltorefresh.library.PullToRefreshBase.OnRefreshListener;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.itheima.googlemarket.AppDetailActivity;
import com.itheima.googlemarket.R;
import com.itheima.googlemarket.adapter.APPListAdapter;
import com.itheima.googlemarket.adapter.BannerAdapter;
import com.itheima.googlemarket.base.BaseFragment;
import com.itheima.googlemarket.bean.HomeBean;
import com.itheima.googlemarket.bean.HomeBean.AppInfo;
import com.itheima.googlemarket.http.JsonRequestCallback;
import com.itheima.googlemarket.http.NetUtil;
import com.itheima.googlemarket.http.Urls;
import com.itheima.googlemarket.interfaces.Keys;
import com.itheima.googlemarket.utils.JsonUtils;

public class HomeFragment extends BaseFragment  {

	
	private static final int NEXT_PAGE = 0;
	private PullToRefreshListView mLv;
	private APPListAdapter adapter;
	private ILoadingLayout footerView;
	private ILoadingLayout headView;
	private ViewPager banner;
	private Handler handler = new Handler(){
		public void handleMessage(android.os.Message msg) {
			switch (msg.what) {
			case NEXT_PAGE:
				switchPage();
				break;
			}
		}

	};
	

	//自动切换界面
	private void switchPage() {
		//获取当前界面的position
		int currentItem = banner.getCurrentItem();
		banner.setCurrentItem(currentItem + 1);
		handler.removeMessages(NEXT_PAGE);
		handler.sendEmptyMessageDelayed(NEXT_PAGE, 3000);
	};

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
		
		NetUtil.requestData(Urls.HOME,params,this);
	}
	
	
	/**请求数据完成回调函数*/
	@Override
	public void onRequestFinish(String json) {
		//获取返回的json数据 解析
		HomeBean homeBean = JsonUtils.json2Bean(json, HomeBean.class);
		
		//获取解析完成后的数据
		
		List<AppInfo> appInfo = null;
		if (homeBean  != null) {
			appInfo = homeBean.list;
		}
		
		//获取广告条数据
		if (homeBean  != null && !homeBean.picture.isEmpty()) {
			List<String> bannerUrl = homeBean.picture;
			banner.setAdapter(new BannerAdapter(bannerUrl));
			banner.setCurrentItem(banner.getAdapter().getCount() / 2);
			handler.removeMessages(NEXT_PAGE);
			handler.sendEmptyMessageDelayed(NEXT_PAGE, 3000);
		}
		
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
		return "首页";
		
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
		
		//item的点击事件
		mLv.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				
				//从点击的item中获取数据
				AppInfo appInfo = (AppInfo) parent.getItemAtPosition(position);
				System.out.println(appInfo);
				
				//跳转到详情界面
				Intent intent = new Intent(context,AppDetailActivity.class);
				intent.putExtra(Keys.PACKAGENAME, appInfo.packageName);
				context.startActivity(intent);
				
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
		
		//初始化广告条viewPager
		initBanner();
		
	}

	/**初始化banner*/
	private void initBanner() {
		//因为PullToRefreshListView是一个线性布局 中间才是ListView 所以要获取中间的ListView并添加头条目
		ListView lv = mLv.getRefreshableView();
		View bannerView = View.inflate(context, R.layout.banner, null);
		banner = (ViewPager) bannerView.findViewById(R.id.vp_banner);
		
		lv.addHeaderView(bannerView);
		
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

	@Override
	public void onDestroy() {
		//取消handler
		handler.removeMessages(NEXT_PAGE);
		super.onDestroy();
	}
	

	
	
}
