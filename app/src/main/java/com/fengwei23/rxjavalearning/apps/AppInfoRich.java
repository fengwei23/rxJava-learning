package com.fengwei23.rxjavalearning.apps;

import android.content.ComponentName;
import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.content.res.AssetManager;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.support.annotation.NonNull;
import android.util.DisplayMetrics;

import java.util.Locale;

import lombok.Setter;
import lombok.experimental.Accessors;

/**
 * author: w.feng
 * time  : 2018/07/23
 * desc  :
 */

@Accessors(prefix = "m")
public class AppInfoRich implements Comparable<Object> {


    @Setter
    String mName = null;

    private Context mContext;

    private ResolveInfo mResolveInfo;

    private ComponentName mComponentName;

    private PackageInfo pi = null;

    private Drawable icon = null;


    public AppInfoRich(Context ctx, ResolveInfo ri) {
        mContext = ctx;
        mResolveInfo = ri;

        mComponentName = new ComponentName(ri.activityInfo.applicationInfo.packageName, ri.activityInfo.name);

        try {
            pi = ctx.getPackageManager().getPackageInfo(getPackageName(), 0);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
    }

    public String getPackageName() {
        return mResolveInfo.activityInfo.packageName;
    }

    public String getName() {
        if (mName != null) {
            return mName;
        } else {
            try {
                return getNameFromResolveInfo(mResolveInfo);
            } catch (PackageManager.NameNotFoundException e) {
                e.printStackTrace();
                return getPackageName();
            }
        }
    }

    public String getActivityName() {
        return mResolveInfo.activityInfo.name;
    }


    public ComponentName getComponentName() {
        return mComponentName;
    }


    public String getComponentInfo() {
        if (getComponentName() != null) {
            return getComponentName().toString();
        } else {
            return "";
        }
    }


    public ResolveInfo getResolveInfo() {
        return mResolveInfo;
    }

    public PackageInfo getPackageInfo() {
        return pi;
    }


    public String getVersionName() {
        PackageInfo pi = getPackageInfo();
        if (pi != null) {
            return pi.versionName;
        } else {
            return "";
        }
    }

    public int getVersionCode() {
        PackageInfo pi = getPackageInfo();
        if (pi != null) {
            return pi.versionCode;
        } else {
            return 0;
        }
    }

    public Drawable getIcon() {
        if (icon == null) {
            icon = getResolveInfo().loadIcon(mContext.getPackageManager());
            /*
            Drawable dr = getResolveInfo().loadIcon(mContext.getPackageManager());
            Bitmap bitmap = ((BitmapDrawable) dr).getBitmap();
            icon = new BitmapDrawable(mContext.getResources(), AppHelper.getResizedBitmap(bitmap, 144, 144));
            */
        }
        return icon;
    }

    public long getFirstInstallTime() {
        PackageInfo pi = getPackageInfo();
        if (pi != null) {
            return pi.firstInstallTime;
        } else {
            return 0;
        }
    }


    public long getLastUpdateTime() {
        PackageInfo pi = getPackageInfo();
        if (pi != null) {
            return pi.lastUpdateTime;
        } else {
            return 0;
        }
    }

    private String getNameFromResolveInfo(ResolveInfo ri) throws PackageManager.NameNotFoundException {
        String name = ri.resolvePackageName;
        if (ri.activityInfo != null) {
            Resources res = mContext.getPackageManager().getResourcesForApplication(ri.activityInfo.applicationInfo);
            Resources cnRes = getChineseResources(res);

            if (ri.activityInfo.labelRes != 0) {
                name = cnRes.getString(ri.activityInfo.labelRes);

                if ("".equals(name)) {
                    name = res.getString(ri.activityInfo.labelRes);
                }
            } else {
                name = ri.activityInfo.applicationInfo.loadLabel(mContext.getPackageManager()).toString();
            }
        }
        return name;
    }

    private Resources getChineseResources(Resources res) {

        AssetManager assetManager = res.getAssets();
        DisplayMetrics metrics = res.getDisplayMetrics();
        Configuration configuration = new Configuration(res.getConfiguration());
        configuration.setLocale(Locale.CHINESE);
        return new Resources(assetManager, metrics, configuration);
    }

    @Override
    public int compareTo(@NonNull Object o) {
        AppInfoRich f = (AppInfoRich) o;
        return getName().compareTo(f.getName());
    }

    @Override
    public String toString() {
        return getName();
    }
}
