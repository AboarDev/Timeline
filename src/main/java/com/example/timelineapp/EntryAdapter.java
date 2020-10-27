package com.example.timelineapp;

import android.net.Uri;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class EntryAdapter extends RecyclerView.Adapter<EntryAdapter.MyViewHolder>{

    private List<Entry> mEntries;
    private EntryAdapter.ClickHandler clickHandler;

    public EntryAdapter (EntryAdapter.ClickHandler clickHandler){
        this.clickHandler = clickHandler;
    }
    public abstract static class ClickHandler{
        public abstract void click(View v,int position);
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public View mView;
        public boolean showImage;
        //public boolean showText;
        public TextView mTitle;
        public TextView mPos;
        public TextView mTextBody;
        public ImageView mImage;
        public int id;
        public MyViewHolder(View theView) {
            super(theView);
            mView = theView;
            mTitle = mView.findViewById(R.id.EntryTitle);
            mPos = mView.findViewById(R.id.EntryPOS);
            mTextBody = mView.findViewById(R.id.EntryBody);
            mImage = mView.findViewById(R.id.EntryImage);
            mView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    clickHandler.click(v,id);
                }
            });
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
        if (mEntries != null){
            Entry entry = mEntries.get(position);
            holder.mTitle.setText(entry.title);
            holder.mTextBody.setText(entry.text);
            //holder.mPos.setText((position + 1) +".");
            holder.mPos.setText("30/1/2020 - 10:45 am");
            holder.id = entry.entryID;
            if (entry.URI != null){
                holder.mImage.setImageURI(Uri.parse(entry.URI));
                holder.mImage.setVisibility(View.VISIBLE);
            }
            else {
                holder.mImage.setVisibility(View.GONE);
            }
        }
    }

    @Override
    public int getItemCount() {
        if (mEntries != null){
            return mEntries.size();
        } else{
            return 0;
        }
    }
}
