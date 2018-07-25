package com.fengwei23.rxjavalearning.demo1;

import android.app.Fragment;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.fengwei23.rxjavalearning.App;
import com.fengwei23.rxjavalearning.R;
import com.fengwei23.rxjavalearning.Utils;
import com.fengwei23.rxjavalearning.apps.AppInfo;
import com.fengwei23.rxjavalearning.apps.AppInfoRich;
import com.fengwei23.rxjavalearning.apps.ApplicationAdapter;
import com.fengwei23.rxjavalearning.apps.ApplicationsList;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.File;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.reactivex.Observable;
import io.reactivex.SingleObserver;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

/**
 * author: w.feng
 * time  : 2018/07/23
 * desc  :
 */
public class FirstExampleFragment extends Fragment {

    private static final String TAG = "FirstExampleFragment";

    @BindView(R.id.fragment_first_example_list)
    RecyclerView recyclerView;

    @BindView(R.id.fragment_first_example_swipe_container)
    SwipeRefreshLayout swipeRefreshLayout;

    private ApplicationAdapter mAdapter;

    private File mFileDir;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_example, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ButterKnife.bind(this, view);

        recyclerView.setLayoutManager(new LinearLayoutManager(view.getContext()));

        mAdapter = new ApplicationAdapter(new ArrayList<>(), R.layout.applications_list_items);
        recyclerView.setAdapter(mAdapter);

        swipeRefreshLayout.setProgressViewOffset(false, 0, (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 24,
                getResources().getDisplayMetrics()));

        swipeRefreshLayout.setEnabled(false);
        swipeRefreshLayout.setRefreshing(true);
        recyclerView.setVisibility(View.GONE);

        getFileDir()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(file -> {
                    mFileDir = file;
                    refreshTheList();
                });

    }

    private void refreshTheList() {
        getApps().
                distinct()
                .toSortedList()
                .subscribe(new SingleObserver<List<AppInfo>>() {

                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onSuccess(List<AppInfo> appInfos) {
                        Log.i(TAG, "onSuccess: " + appInfos.size());
                        recyclerView.setVisibility(View.VISIBLE);
                        mAdapter.addApplications(appInfos);
                        swipeRefreshLayout.setRefreshing(false);
                        storedList(appInfos);
                    }

                    @Override
                    public void onError(Throwable e) {
                        swipeRefreshLayout.setRefreshing(false);
                    }

                });
    }

    private void storedList(List<AppInfo> appInfos) {
        ApplicationsList.getInstance().setList(appInfos);
        Schedulers.io().scheduleDirect(() -> {
            SharedPreferences sharedPreferences = getActivity().getPreferences(Context.MODE_PRIVATE);
            Type appInfoType = new TypeToken<List<AppInfo>>() {

            }.getType();
            sharedPreferences.edit().putString("APPS", new Gson().toJson(appInfos, appInfoType)).apply();
        });
    }


    private Observable<File> getFileDir() {
        return Observable.create(subscriber -> {
            subscriber.onNext(App.instance.getFilesDir());
            subscriber.onComplete();
        });
    }

    private Observable<AppInfo> getApps() {
        return Observable.create(subscriber -> {
            List<AppInfoRich> apps = new ArrayList<>();

            final Intent mainIntent = new Intent(Intent.ACTION_MAIN, null);
            mainIntent.addCategory(Intent.CATEGORY_DEFAULT);

            List<ResolveInfo> infos = getActivity().getPackageManager().queryIntentActivities(mainIntent, PackageManager
                    .MATCH_UNINSTALLED_PACKAGES);
            Collections.sort(infos, new ResolveInfo.DisplayNameComparator(getActivity().getPackageManager()));
            for (ResolveInfo info : infos) {
                apps.add(new AppInfoRich(getActivity(), info));
            }

            for (AppInfoRich app : apps) {
                Bitmap icon = Utils.drawableToBitmap(app.getIcon());
                String name = app.getName();
                String iconPath = mFileDir + "/" + name;
                Utils.storeBitmap(App.instance, icon, name);
                if (subscriber.isDisposed()) {
                    return;
                }
                subscriber.onNext(new AppInfo(app.getLastUpdateTime(), name, iconPath));
            }
            if (!subscriber.isDisposed()) {
                subscriber.onComplete();
            }
        });
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }
}
