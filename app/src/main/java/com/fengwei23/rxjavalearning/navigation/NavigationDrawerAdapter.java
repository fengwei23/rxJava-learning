package com.fengwei23.rxjavalearning.navigation;

import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.fengwei23.rxjavalearning.R;

import java.util.List;

/**
 * author: w.feng
 * time  : 2018/07/24
 * desc  :
 */
public class NavigationDrawerAdapter extends RecyclerView.Adapter<NavigationDrawerAdapter.ViewHolder> {

    private List<NavigationItem> mData;
    private NavigationDrawerCallbacks mNavigationDrawerCallbacks;
    private int mSelectedPosition;

    private int mTouchedPosition = -1;


    public NavigationDrawerAdapter(List<NavigationItem> data) {
        mData = data;
    }

    public void setNavigationDrawerCallbacks(NavigationDrawerCallbacks navigationDrawerCallbacks) {
        mNavigationDrawerCallbacks = navigationDrawerCallbacks;
    }

    @NonNull
    @Override

    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.drawer_row, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.textView.setText(mData.get(position).getText());
        holder.textView.setCompoundDrawablesRelativeWithIntrinsicBounds(mData.get(position).getDrawable(), null, null, null);

        holder.itemView.setOnTouchListener((v, event) -> {
            switch (event.getAction()) {
                case MotionEvent.ACTION_DOWN:
                    touchPosition(position);
                    return false;
                case MotionEvent.ACTION_CANCEL:
                    touchPosition(-1);
                    return false;
                case MotionEvent.ACTION_MOVE:
                    return false;
                case MotionEvent.ACTION_UP:
                    touchPosition(-1);
                    return false;
                default:
                    break;
            }
            return true;
        });


        holder.itemView.setOnClickListener(v -> {
            if (mNavigationDrawerCallbacks != null) {
                mNavigationDrawerCallbacks.onNavigationDrawerItemSelected(position);
            }
        });

        if (mSelectedPosition == position || mTouchedPosition == position) {
            holder.itemView.setBackgroundColor(holder.itemView.getContext().getResources().getColor(R.color
                    .cardview_shadow_end_color));
        } else {
            holder.itemView.setBackgroundColor(Color.TRANSPARENT);
        }
    }

    private void touchPosition(int position) {
        int lastPosition = mTouchedPosition;
        mTouchedPosition = position;
        if (lastPosition >= 0) {
            notifyItemChanged(lastPosition);
        }
        if (position >= 0) {
            notifyItemChanged(position);
        }
    }

    public void selectPosition(int position) {
        int lastPosition = mSelectedPosition;
        mSelectedPosition = position;
        notifyItemChanged(lastPosition);
        notifyItemChanged(position);
    }


    @Override
    public int getItemCount() {
        return mData != null ? mData.size() : 0;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public TextView textView;

        public ViewHolder(View itemView) {
            super(itemView);
            textView = itemView.findViewById(R.id.item_name);
        }
    }
}
