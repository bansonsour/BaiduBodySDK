package com.gourd.baidubodysdk.util;

import android.app.Activity;
import android.app.ActivityManager;
import android.content.Context;
import android.content.ContextWrapper;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Process;
import android.view.View;

import java.util.Iterator;
import java.util.List;

import static android.content.Context.ACTIVITY_SERVICE;

/**
 * <p>Project: bi</p>
 * <p>Description: AppUtils</p>
 * <p>Copyright (c) 2016 www.duowan.com Inc. All rights reserved.</p>
 * <p>Company: YY Inc.</p>
 *
 * @author: Aragon.Wu
 * @date: 2016-06-01
 * @vserion: 1.0
 */
public class AppUtils {

    private static Context mAppContext = null;

    public static void setContext(Context context) {
        mAppContext = context.getApplicationContext();
    }

    public static Context getAppContext() {
        if (null == mAppContext) {
            throw new IllegalAccessError("application context is null !");
        }
        return mAppContext;
    }

    private static volatile String sProcessName;

    private static final Object sNameLock = new Object();

    public static String processName(Context context) {
        if (null == sProcessName) {
            synchronized (sNameLock) {
                if (null == sProcessName) {
                    sProcessName = obtainProcessName(context);
                }
            }
        }
        return sProcessName;
    }

    private static String obtainProcessName(Context context) {
        List runnableActivityList = ((ActivityManager) context.getSystemService(ACTIVITY_SERVICE)).getRunningAppProcesses();
        Iterator iterator = null;
        if ((runnableActivityList != null) && (runnableActivityList.size() > 0)) {
            iterator = runnableActivityList.iterator();
        }

        if (null == iterator) {
            return null;
        }

        ActivityManager.RunningAppProcessInfo localRunningAppProcessInfo;
        do {
            if (!iterator.hasNext()) {
                return null;
            }
            localRunningAppProcessInfo = (ActivityManager.RunningAppProcessInfo) iterator.next();
        }
        while ((localRunningAppProcessInfo == null) || (localRunningAppProcessInfo.pid != Process.myPid()));
        return localRunningAppProcessInfo.processName;
    }

    public static boolean appInstalled(String pkgName, int flag) {
        boolean installed = false;
        try {
            PackageManager packageManager = AppUtils.getAppContext().getPackageManager();
            PackageInfo packageInfo = packageManager.getPackageInfo(pkgName, flag);
            installed = null != packageInfo;
        } catch (PackageManager.NameNotFoundException e) {
            installed = false;
        }

        return installed;
    }

    public static Activity getOriginalActivity(Context cont) {
        if (cont == null) {
            return null;
        } else if (cont instanceof Activity) {
            return (Activity) cont;
        } else if (cont instanceof ContextWrapper) {
            return getOriginalActivity(((ContextWrapper) cont).getBaseContext());
        }

        return null;
    }

    public static void setBackground(View view, Drawable drawable) {
        if (Build.VERSION.SDK_INT >= 16) {
            view.setBackground(drawable);
        } else {
            view.setBackgroundDrawable(drawable);
        }
    }

}
