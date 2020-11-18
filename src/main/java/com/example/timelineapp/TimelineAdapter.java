package com.example.timelineapp;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class TimelineAdapter extends RecyclerView.Adapter<TimelineAdapter.MyViewHolder> {

    private List<Timeline> mTimelines;
    private ClickHandler clickHandler;

    public TimelineAdapter (ClickHandler clickHandler){
        this.clickHandler = clickHandler;
    }

    public abstract static class ClickHandler{

        public abstract void click(int position,boolean showTimes);

        public abstract void makeMenu (View v, int position);

    };

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public View mView;
        public TextView mTimelineTitle;
        public TextView mItemCount;
        public ImageButton mMenu;
        public int mID;
        public boolean mShowTimes;
        public MyViewHolder(View theView) {
            super(theView);
            mView = theView;
            mTimelineTitle = theView.findViewById(R.id.timelineTitle);
            mItemCount = theView.findViewById(R.id.timelineContents);
            mMenu = theView.findViewById(R.id.timelineOptions);
            mView.setOnClickListener(v -> clickHandler.click(mID,mShowTimes));
            mMenu.setOnClickListener(v -> clickHandler.makeMenu(v,mID));
        }

    }

    void setItems (List<Timeline> timelines){
        mTimelines = timelines;
        notifyDataSetChanged();
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.timeline_fragment, parent,false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        if (mTimelines != null){
            Timeline timeline = mTimelines.get(position);
            holder.mTimelineTitle.setText(timeline.name);
            holder.mItemCount.setText(timeline.description);
            holder.mID = timeline.timelineID;
            holder.mShowTimes = timeline.showTimes;
        }
    }

    @Override
    public int getItemCount() {
        if (mTimelines != null){
            return mTimelines.size();
        } else{
            return 0;
        }
    }
}