package com.caijia.chat.service;

import android.app.ActivityManager;
import android.app.usage.UsageStats;
import android.app.usage.UsageStatsManager;
import android.content.ComponentName;
import android.content.Context;
import android.os.Build;
import android.util.TypedValue;

import java.util.List;
import java.util.SortedMap;
import java.util.TreeMap;

/**
 * Created by cai.jia on 2016/4/7 0007.
 */
public class DeviceUtil {

    private DeviceUtil() {

    }

    public static int dp2px(Context context, float value) {
        return (int) Math.ceil(TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, value, context.getResources().getDisplayMetrics()));
    }

    public static int getScreenWidth(Context context) {
        return context.getResources().getDisplayMetrics().widthPixels;
    }

    public static int getScreenHeight(Context context) {
        return context.getResources().getDisplayMetrics().heightPixels;
    }

    /**
     * 判断应用是否正在运行
     *
     * @param context
     * @return
     */
    public static boolean isRunForeground(Context context) {
        try {
            return getProcess(context).equals(context.getPackageName());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }


    public static String getProcess(Context context) throws Exception {
//        if (Build.VERSION.SDK_INT >= 21) {
//            return getProcessNew(context);
//        } else {
//            return getProcessOld(context);
//        }
        return getProcessOld(context);
    }

    public static String getProcessNew(Context context) throws Exception {
        String topPackageName = null;
        if (Build.VERSION.SDK_INT >= 21) {
            UsageStatsManager usage = (UsageStatsManager) context.getSystemService(Context.USAGE_STATS_SERVICE);
            long time = System.currentTimeMillis();
            List<UsageStats> stats = usage.queryUsageStats(UsageStatsManager.INTERVAL_DAILY, time - 10, time);
            if (stats != null) {
                SortedMap<Long, UsageStats> runningTask = new TreeMap<>();
                for (UsageStats usageStats : stats) {
                    runningTask.put(usageStats.getLastTimeUsed(), usageStats);
                }
                if (runningTask.isEmpty()) {
                    return null;
                }
                topPackageName = runningTask.get(runningTask.lastKey()).getPackageName();
            }
        }
        return topPackageName;
    }

    //API below 21
    @SuppressWarnings("deprecation")
    public static String getProcessOld(Context context) throws Exception {
        String topPackageName = null;
        ActivityManager activity = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        List<ActivityManager.RunningTaskInfo> runningTask = activity.getRunningTasks(1);
        if (runningTask != null) {
            ActivityManager.RunningTaskInfo taskTop = runningTask.get(0);
            ComponentName componentTop = taskTop.topActivity;
            topPackageName = componentTop.getPackageName();
        }
        return topPackageName;
    }

}
