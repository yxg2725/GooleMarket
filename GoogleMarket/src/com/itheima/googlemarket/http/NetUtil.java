package com.itheima.googlemarket.http;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.net.URLEncoder;
import java.util.TreeMap;

import org.apache.http.protocol.HTTP;

import android.os.AsyncTask;

import com.itheima.googlemarket.MyApp;
import com.itheima.googlemarket.utils.Logger;
import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest.HttpMethod;
import com.lidroid.xutils.util.IOUtils;

public class NetUtil {

	protected static final Object TAG = NetUtil.class;

	public static void requestData(String url, TreeMap<String, String> params, JsonRequestCallback jsonRequestCallback) {
		//拼接网址
		String requestUrl = createUrl(url,params);
		//获取缓存文件
		File cacheFile = getCacheFile(requestUrl);
		//判断缓存文件是否有效
		if (cacheFileVaild(cacheFile)) {//如果有效 读取缓存文件
			getDataFromCache(cacheFile,jsonRequestCallback);
		}else {	//如果无效 则访问网络
			getDataFromNet(requestUrl,jsonRequestCallback);
		}
		
	
		
	}

	/**拼接网络链接*/
	private static String createUrl(String url, TreeMap<String, String> params) {
		
		if (params == null || params.isEmpty()) {
			return url;
		}
		
		StringBuffer sb = new StringBuffer();
		
		//遍历map集合的键的集合
		for (String key : params.keySet()) {
			sb.append("&").append(key).append("=").append(params.get(key));
		}
		sb.deleteCharAt(0);//删除第一个元素
		String requestUrl = url + "?" + sb.toString();
		return requestUrl;
	}

	/**获取缓存文件*/
	private static File getCacheFile(String requestUrl) {
		
		//将网址进行编码  去掉特殊字符 这样就可以作为文件名了
		String cacheFileName;
		try {
			cacheFileName = URLEncoder.encode(requestUrl, HTTP.UTF_8);
			File cacheFile = new File(MyApp.getContext().getCacheDir(),cacheFileName);
			return cacheFile;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	/**判断缓存文件是否有效,不存在  或 为空  或超时 都视为无效*/
	private static boolean cacheFileVaild(File cacheFile) {
		if (cacheFile == null || !cacheFile.exists()) {
			return false;
		}
		//获取文件最后修改时间
		long  lastModifyTime = cacheFile.lastModified();
		//定义有效时间为三分钟
		long vaildExistsTime = 3 * 60 * 1000;
		
		boolean isVaild = lastModifyTime < vaildExistsTime;
		return isVaild;
	}

	/**获取网路数据
	 * @param jsonRequestCallback */
	private static void getDataFromNet(final String requestUrl, final JsonRequestCallback jsonRequestCallback) {
		HttpUtils httpUtils = new HttpUtils();
		RequestCallBack<String> callBack = new RequestCallBack<String>() {

			@Override
			public void onSuccess(ResponseInfo<String> responseInfo) {
				String json = responseInfo.result;
				Logger.i(TAG, responseInfo.result);
				//保存缓存数据
				cacheData(requestUrl,json);
				jsonRequestCallback.onRequestFinish(json);
			}

			@Override
			public void onFailure(HttpException error, String msg) {
				Logger.e(TAG, "请求网络数据失败：" + msg, error);
				jsonRequestCallback.onRequestFinish(null);
				
			}
		};
		httpUtils.send(HttpMethod.GET, requestUrl, callBack );
	}

	/**保存缓存数据*/
	protected static void cacheData(String requestUrl, String json) {
	
		if (json == null) {
			return;
		}
		BufferedWriter bw = null;
		try {
			//获取缓存文件
			File cacheFile = getCacheFile(requestUrl);
			//写json到缓存文件
			bw = new BufferedWriter(new FileWriter(cacheFile));
			bw.write(json);
			bw.flush();
			
		} catch (Exception e) {
			e.printStackTrace();
		}finally{
			IOUtils.closeQuietly(bw);
		}
		
		
	}

	/**读取缓存中的数据,  在子线程中进行
	 * @param jsonRequestCallback */
	private static void getDataFromCache(final File cacheFile, final JsonRequestCallback jsonRequestCallback) {
		new AsyncTask<Void, Void, String>() {

			@Override
			protected String doInBackground(Void... params) {
				BufferedReader br = null;
				StringBuffer sb = new StringBuffer();
				try {
					 br = new BufferedReader(new FileReader(cacheFile));
					 String line = null;
					 while((line = br.readLine()) != null){
						 sb.append(line).append("\n");
					 }
					 return sb.toString();
				} catch (Exception e) {
					e.printStackTrace();
				}finally{
					IOUtils.closeQuietly(br);
				}
				return null;
			}
			
			/**获取数据完毕后 接受返回的数据并处理*/
			protected void onPostExecute(String result) {
				Logger.i(TAG, "接受到的json数据为:" + result);
				jsonRequestCallback.onRequestFinish(result);
				
			};
		}.execute();
		
	}

}
