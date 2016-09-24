package com.itheima.googlemarket.view;

import android.content.Context;
import android.provider.Telephony.Sms.Conversations;
import android.util.AttributeSet;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;

import com.itheima.googlemarket.R;

public class StateFrameLayout extends FrameLayout {

	private View state_loading;
	private View state_fail;
	private View state_empty;
	private View contentView;
	
	
	public StateFrameLayout(Context context, AttributeSet attrs) {
		super(context, attrs);
		
	}
	
	
	
	public StateFrameLayout(Context context) {
		super(context);
		// TODO Auto-generated constructor stub
	}



	//加载布局后
	@Override
	protected void onFinishInflate() {
		initView();
		super.onFinishInflate();
	}
	private void initView() {
		//三中状态
		state_loading = findViewById(R.id.state_pbar);
		state_fail = findViewById(R.id.state_fail);
		state_empty = findViewById(R.id.state_empty);
		
		showView(state_loading);
	}

	public  void showLoadingView(){
		showView(state_loading);
	}

	public  void showFailView(){
		showView(state_fail);
	}
	public  void showEmptyView(){
		showView(state_empty);
	}
	public  void showContentView(){
		showView(contentView);
	}

	private void showView(View view) {
		for (int i = 0; i < getChildCount(); i++) {
			View child = getChildAt(i);
			child.setVisibility(child == view ? View.VISIBLE : View.GONE);
		}
	}
	
	//第四种正常内容状态
	public void setContentView(Object viewOrId){
		if (viewOrId == null) {
			throw new IllegalArgumentException("必须要给Fragemnt的getContentView方法返回一个布局id，或者设置一个View");
		}else if(viewOrId instanceof Integer){
			int  id = (Integer) viewOrId;
			contentView =View.inflate(getContext(), id, null);
		}else {
			contentView = (View) viewOrId;
		}
		
		//将conventView 添加到状态布局
		addView(contentView);
		//隐藏
		contentView.setVisibility(View.GONE);
	}
}
