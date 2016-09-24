package com.itheima.googlemarket.fragment;

import java.lang.reflect.Type;
import java.util.List;

import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ScrollView;
import android.widget.TextView;

import com.google.gson.reflect.TypeToken;
import com.itheima.googlemarket.base.BaseFragment;
import com.itheima.googlemarket.http.NetUtil;
import com.itheima.googlemarket.http.Urls;
import com.itheima.googlemarket.utils.JsonUtils;
import com.itheima.googlemarket.utils.UiUtils;
import com.itheima.googlemarket.view.FlowLayout;

public class TopFragment extends BaseFragment {

	private FlowLayout flowLayout;

	@Override
	public void initData() {
		//联网获取数据
		NetUtil.requestData(Urls.TOP, null, this);
	}

	@Override
	public CharSequence getTitle() {
		return "排行";
		
	}

	@Override
	public void initListener() {
	}

	@Override
	public void initView() {
		
	}

	@Override
	public Object getContentView() {
		/*TextView text = new TextView(context);
		text.setText("这是正常界面的内容");
		text.setGravity(Gravity.CENTER);*/
		
		ScrollView  scrollView = new ScrollView(context);
		
		//创建自定义控件并添加到ScrollView中
		flowLayout = new FlowLayout(context);
		//设置自定义控件的缩进
		flowLayout.setPadding(6, 6, 6, 6);
		scrollView.addView(flowLayout);
		
		return scrollView;
	}

	@Override
	public void onRequestFinish(String json) {
		Type type = new TypeToken<List<String>>(){}.getType();
		//json解析
		List<String> datas = JsonUtils.json2Collection(json, type );
		
		//检查数据
		if (checkData(datas)) {
			
			for (final String string : datas) {
				
				//将数据展示到TextView上
				//创建个带有随机颜色的圆角矩形的背景的TextView
				TextView textView = UiUtils.createRandomColorShapeSelectorTextView(context);
				
				textView.setText(string);
				
				//将这个TextView添加到自定义的控件中  而自定义控件又在ScrollVIew中
				flowLayout.addView(textView);
				
				//对TextVIew创建点击监听
				textView.setOnClickListener(new OnClickListener() {
					
					@Override
					public void onClick(View v) {
						UiUtils.showToast(string);
						
					}
				});
				
			}
		}
	}



}
