package com.itheima.googlemarket.httpDown;

import android.app.ActivityManager;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.IBinder;
import com.lidroid.xutils.exception.DbException;
import com.lidroid.xutils.util.LogUtils;

import java.util.List;

/**
 * Author: wyouflf
 * Date: 13-11-10
 * Time: 上午1:04
 */
public class DownloadService extends Service {

    private static DownloadManager DOWNLOAD_MANAGER;

    //创建DownloadManger的对象   先开启了服务  后创建了DownloadManager对象
    public static DownloadManager getDownloadManager(Context appContext) {
    	
    	//先判断服务是否开启 如果没有开启的话在开启服务--------------
        if (!DownloadService.isServiceRunning(appContext)) {
            Intent downloadSvr = new Intent("download.service.action");
            
            //开启服务
            appContext.startService(downloadSvr);
        }
        
        //////////////////////////单例模式/////////////////////////////////////
        if (DownloadService.DOWNLOAD_MANAGER == null) {//单例模式
            DownloadService.DOWNLOAD_MANAGER = new DownloadManager(appContext);
        }
        return DOWNLOAD_MANAGER;
    }

    public DownloadService() {
        super();
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();
    }

    @Override
    public void onStart(Intent intent, int startId) {
        super.onStart(intent, startId);
    }

    //服务停止后 要停止所有的下载任务  保存下载的应用的信息
    @Override
    public void onDestroy() {
        if (DOWNLOAD_MANAGER != null) {
            try {
                DOWNLOAD_MANAGER.stopAllDownload();
                DOWNLOAD_MANAGER.backupDownloadInfoList();
            } catch (DbException e) {
                LogUtils.e(e.getMessage(), e);
            }
        }
        super.onDestroy();
    }

    //判断服务是否开启
    public static boolean isServiceRunning(Context context) {
        boolean isRunning = false;

        //获取进程管理者  
        ActivityManager activityManager =
                (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        
        //获取正在运行的所有的服务的集合
        List<ActivityManager.RunningServiceInfo> serviceList
                = activityManager.getRunningServices(Integer.MAX_VALUE);

        //对集合 为空 和为null判断    对 bean 只有为null判断
        if (serviceList == null || serviceList.size() == 0) {
            return false;
        }

        //遍历这个集合  如果这个集合中的服务的类名  等于我们要查看的服务的类名则表示存在
        for (int i = 0; i < serviceList.size(); i++) {
            if (serviceList.get(i).service.getClassName().equals(DownloadService.class.getName())) {
                isRunning = true;
                break;
            }
        }
        return isRunning;
    }
}
