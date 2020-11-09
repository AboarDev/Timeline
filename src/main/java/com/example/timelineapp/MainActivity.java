package com.example.timelineapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.FileProvider;
import androidx.fragment.app.DialogFragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.PopupMenu;

import java.io.File;
import java.io.IOException;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    AppViewModel mViewModel;

    String filePath;

    int fileCount;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar myToolbar = findViewById(R.id.toolbar);
        setSupportActionBar(myToolbar);

        mViewModel = new ViewModelProvider(this).get(AppViewModel.class);

        fileCount = 0;

        RecyclerView timelineList = findViewById(R.id.timelineList);
        timelineList.setLayoutManager(new LinearLayoutManager(this));
        timelineList.addItemDecoration(new DividerItemDecoration(this,DividerItemDecoration.VERTICAL));
        TimelineAdapter theAdapter = new TimelineAdapter(new TimelineAdapter.ClickHandler() {
            @Override
            public void click(int position,String title) {
                System.out.println(position);
                Intent theIntent = new Intent(MainActivity.this, ViewTimeline.class);
                theIntent.putExtra(Intent.EXTRA_INDEX,position);
                theIntent.putExtra(Intent.EXTRA_TITLE,title);
                startActivity(theIntent);
            }

            @Override
            public void makeMenu(View v, int position) {
                PopupMenu popupMenu = new PopupMenu(MainActivity.this,v);
                MenuInflater inflater = popupMenu.getMenuInflater();
                inflater.inflate(R.menu.timeline_menu,popupMenu.getMenu());
                popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem item) {
                        switch (item.getItemId()){
                            case R.id.delete_timeline:
                                System.out.println("delete" + position);
                                mViewModel.deleteTimeline(position);
                                return true;
                            case R.id.edit_timeline:
                                System.out.println("edit" + position);
                                return true;
                            default:
                                return false;
                        }

                    }
                });
                popupMenu.show();
            }
        });
        timelineList.setAdapter(theAdapter);
        mViewModel.getAllTimelines().observe(this, new Observer<List<Timeline>>() {
            @Override
            public void onChanged(List<Timeline> timelines) {
                System.out.println(timelines.size());
                theAdapter.setItems(timelines);
            }
        });
    }

    public void openSettings (MenuItem menuItem){
        Intent theIntent = new Intent(MainActivity.this, Settings.class);
        startActivity(theIntent);
    }

    public void newTimeline (View view) {
        DialogFragment theFragment = new CreateTimeline(new CreateTimeline.ClickHandler() {
            @Override
            public void positive(String title, String description, boolean showTimes) {
                System.out.println(title);
                mViewModel.addTimeline(title,description,showTimes);
            }
        });
        theFragment.show(getSupportFragmentManager(),"a");
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1 && resultCode == RESULT_OK) {
            data.hasExtra("data");
            Bitmap thumbnail = data.getParcelableExtra("data");
            boolean hasThumb = thumbnail == null;
            Uri fullPhotoUri = data.getData();
            fullPhotoUri.toString();
            mViewModel.saveUri(fullPhotoUri, true);
        }
        else if (requestCode == 2 && resultCode == RESULT_OK){
            System.out.println(data);
            Uri theUri = Uri.parse(filePath);
            mViewModel.saveUri(theUri, false);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(final Menu menu) {
        getMenuInflater().inflate(R.menu.home_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

}