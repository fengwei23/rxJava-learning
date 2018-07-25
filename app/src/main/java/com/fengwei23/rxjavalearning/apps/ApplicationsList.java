package com.fengwei23.rxjavalearning.apps;

import java.util.List;

import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

/**
 * author: w.feng
 * time  : 2018/07/24
 * desc  :
 */

@Accessors(prefix = "m")
public class ApplicationsList {
    private static ApplicationsList ourInstance = new ApplicationsList();

    @Setter
    @Getter
    private List<AppInfo> mList;

    private ApplicationsList() {

    }

    public static ApplicationsList getInstance() {
        return ourInstance;
    }
}
