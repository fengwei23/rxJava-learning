package com.fengwei23.rxjavalearning.apps;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.fengwei23.rxjavalearning.R;

import java.util.List;
import java.util.Objects;

import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

/**
 * author: w.feng
 * time  : 2018/07/23
 * desc  :
 */
public class ApplicationAdapter extends RecyclerView.Adapter<ApplicationAdapter.ViewHolder> {

    private static final String TAG = "ApplicationAdapter";
    private List<AppInfo> mAppInfos;

    private int mRowLayout;

    public ApplicationAdapter(List<AppInfo> appInfos, int rowLayout) {
        mAppInfos = appInfos;
        mRowLayout = rowLayout;
    }

    public void addApplications(List<AppInfo> appInfos) {
        mAppInfos.clear();
        mAppInfos.addAll(appInfos);

        notifyDataSetChanged();
    }

    public void addApplication(int position, AppInfo appInfo) {
        if (position < 0) {
            position = 0;
        }
        mAppInfos.add(position, appInfo);
        notifyItemInserted(position);
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(mRowLayout, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull ApplicationAdapter.ViewHolder holder, int position) {
        final AppInfo appInfo = mAppInfos.get(position);
        holder.name.setText(appInfo.getName());
        getBitmap(appInfo.getIcon())
                .filter(Objects::nonNull)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<Bitmap>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onNext(Bitmap bitmap) {
                        holder.image.setImageBitmap(bitmap);
                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onComplete() {

                    }
                });
    }

    @Override
    public int getItemCount() {
        return mAppInfos == null ? 0 : mAppInfos.size();
    }

    private Observable<Bitmap> getBitmap(String icon) {
        return Observable.create(subscriber -> {
            subscriber.onNext(BitmapFactory.decodeFile(icon));
            subscriber.onComplete();
        });
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public TextView name;

        public ImageView image;

        public ViewHolder(View itemView) {
            super(itemView);
            name = itemView.findViewById(R.id.name);
            image = itemView.findViewById(R.id.image);
        }
    }
}
