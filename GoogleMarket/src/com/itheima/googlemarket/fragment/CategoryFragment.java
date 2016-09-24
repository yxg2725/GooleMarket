package com.itheima.googlemarket.fragment;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import android.widget.ListView;

import com.google.gson.reflect.TypeToken;
import com.itheima.googlemarket.R;
import com.itheima.googlemarket.adapter.CategoryListAdapter;
import com.itheima.googlemarket.base.BaseFragment;
import com.itheima.googlemarket.bean.CategoryInfo;
import com.itheima.googlemarket.http.NetUtil;
import com.itheima.googlemarket.http.Urls;
import com.itheima.googlemarket.utils.JsonUtils;

public class CategoryFragment extends BaseFragment {

	private ListView listView;

	@Override
	public void initData() {
		NetUtil.requestData(Urls.CATEGORY, null, this);

	}

	@Override
	public CharSequence getTitle() {
		return "分类";

	}

	@Override
	public void initListener() {
	}

	@Override
	public void initView() {
		listView = findView(R.id.category_listview);

	}

	@Override
	public Object getContentView() {
		return R.layout.fragment_category;
	}

	@Override
	public void onRequestFinish(String json) {
		//	Logger.i(this, "json:" + json);
		//解析
		Type type = new TypeToken<List<CategoryInfo>>(){}.getType();
		List<CategoryInfo> datas = JsonUtils.json2Collection(json, type );

		if (checkData(datas)) {
			showdatas(datas);
		}

	}

	private void showdatas(List<CategoryInfo> datas) {
		//创建一个新集合
		List<Object> newList = new ArrayList<Object>();
		for (CategoryInfo info : datas) {
			newList.add(info.title);
			newList.addAll(info.infos);
		}

		listView.setAdapter(new CategoryListAdapter(newList));

	}

}
