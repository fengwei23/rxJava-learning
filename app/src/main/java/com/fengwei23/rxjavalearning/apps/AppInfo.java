package com.fengwei23.rxjavalearning.apps;

import android.support.annotation.NonNull;

import lombok.Data;
import lombok.experimental.Accessors;

/**
 * author: w.feng
 * time  : 2018/07/23
 * desc  :
 */

@Data
@Accessors(prefix = "m")
public class AppInfo implements Comparable<Object> {

    long mLastUpdateTime;

    String mName;

    String mIcon;


    public AppInfo(long lastUpdateTime, String name, String icon) {
        this.mLastUpdateTime = lastUpdateTime;
        this.mName = name;
        this.mIcon = icon;
    }

    @Override
    public int compareTo(@NonNull Object o) {

        AppInfo info = (AppInfo) o;
        return getName().compareTo(info.getName());
    }
}
