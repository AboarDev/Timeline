package com.example.timelineapp;

import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class EntryAdapter extends RecyclerView.Adapter<EntryAdapter.MyViewHolder> {

    private List<Entry> mEntries;
    private EntryAdapter.ClickHandler clickHandler;
    private boolean showTimes;

    public EntryAdapter(boolean showTimes, EntryAdapter.ClickHandler clickHandler) {
        this.clickHandler = clickHandler;
        this.showTimes = showTimes;
    }

    public abstract static class ClickHandler {
        public abstract void click(View v, int position);
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public View mView;
        public TextView mTitle;
        public TextView mPos;
        public TextView mTextBody;
        public ImageView mImage;
        public ImageButton mButton;
        public int id;

        public MyViewHolder(View theView) {
            super(theView);
            mView = theView;
            mTitle = mView.findViewById(R.id.EntryTitle);
            mPos = mView.findViewById(R.id.EntryPOS);
            mTextBody = mView.findViewById(R.id.EntryBody);
            mImage = mView.findViewById(R.id.EntryImage);
            mButton = mView.findViewById(R.id.entryButton);
            mButton.setOnClickListener(v -> clickHandler.click(v, id));
        }
    }

    void setItems(List<Entry> entries) {
        mEntries = entries;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.entry_fragment, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        if (mEntries != null) {
            Entry entry = mEntries.get(position);
            if (!entry.title.isEmpty()) {
                holder.mTitle.setText(entry.title);
            } else {
                holder.mTitle.setVisibility(View.GONE);
            }
            if (!entry.text.isEmpty()) {
                holder.mTextBody.setText(entry.text);
            } else {
                holder.mTextBody.setVisibility(View.GONE);
            }

            holder.id = entry.entryID;
            if (entry.dateTime != null && showTimes) {
                holder.mPos.setText(entry.dateTime);
            } else {
                holder.mPos.setText((position + 1) + ".");
            }
            if (entry.URI != null) {
                holder.mImage.setImageURI(Uri.parse(entry.URI));
                holder.mImage.setVisibility(View.VISIBLE);
            } else {
                holder.mImage.setVisibility(View.GONE);
            }
        }
    }

    @Override
    public int getItemCount() {
        if (mEntries != null) {
            return mEntries.size();
        } else {
            return 0;
        }
    }
}
