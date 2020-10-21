package com.example.timelineapp;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class EntryAdapter extends RecyclerView.Adapter<EntryAdapter.MyViewHolder>{

    private List<Entry> mEntries;

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public View mView;
        public boolean showImage;
        public boolean showText;
        public TextView mTitle;
        public TextView mPos;
        public TextView mTextBody;
        public ImageView mImage;
        public MyViewHolder(View theView) {
            super(theView);
            mView = theView;
            mTitle = mView.findViewById(R.id.EntryTitle);
            mPos = mView.findViewById(R.id.EntryPOS);
            mTextBody = mView.findViewById(R.id.EntryBody);
            mImage = mView.findViewById(R.id.EntryImage);
        }
    }

    void setItems (List<Entry> entries){
        mEntries = entries;
        notifyDataSetChanged();
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.entry_fragment, parent,false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {

    }

    @Override
    public int getItemCount() {
        if (mEntries != null){
            return mEntries.size();
        } else{
            return 2;
        }
    }
}
