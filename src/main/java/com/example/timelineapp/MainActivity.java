package com.example.timelineapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.DialogFragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.ActivityOptions;
import android.transition.Explode;

import android.content.Intent;
import android.os.Bundle;
import android.transition.Fade;
import android.transition.Slide;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.PopupMenu;

public class MainActivity extends AppCompatActivity {

    AppViewModel mViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setEnterTransition(new Fade());
        getWindow().setExitTransition(new Fade());
        setContentView(R.layout.activity_main);

        Toolbar myToolbar = findViewById(R.id.toolbar);
        setSupportActionBar(myToolbar);

        mViewModel = new ViewModelProvider(this).get(AppViewModel.class);

        RecyclerView timelineList = findViewById(R.id.timelineList);
        timelineList.setLayoutManager(new LinearLayoutManager(this));
        timelineList.addItemDecoration(new DividerItemDecoration(this,
                DividerItemDecoration.VERTICAL));
        TimelineAdapter theAdapter = new TimelineAdapter(new TimelineAdapter.ClickHandler() {
            @Override
            public void click(int position,boolean showTimes) {
                System.out.println(position);
                Intent theIntent = new Intent(MainActivity.this, ViewTimeline.class);
                theIntent.putExtra(Intent.EXTRA_INDEX,position);
                theIntent.putExtra("SHOW_TIMES",showTimes);
                startActivity(theIntent, ActivityOptions.makeSceneTransitionAnimation(MainActivity.this).toBundle());
            }

            @Override
            public void makeMenu(View v, int position) {
                PopupMenu popupMenu = new PopupMenu(MainActivity.this,v);
                MenuInflater inflater = popupMenu.getMenuInflater();
                inflater.inflate(R.menu.timeline_menu,popupMenu.getMenu());
                popupMenu.setOnMenuItemClickListener(item -> {
                    switch (item.getItemId()){
                        case R.id.delete_timeline:
                            System.out.println("delete" + position);
                            mViewModel.deleteTimeline(position);
                            return true;
                        case R.id.edit_timeline:
                            System.out.println("edit" + position);
                            editTimeline(position);
                            return true;
                        default:
                            return false;
                    }

                });
                popupMenu.show();
            }
        });
        timelineList.setAdapter(theAdapter);
        mViewModel.getAllTimelines().observe(this, timelines -> {
            System.out.println(timelines.size());
            theAdapter.setItems(timelines);
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

    public void editTimeline (int id) {
        DialogFragment dialogFragment = new CreateTimeline(new CreateTimeline.ClickHandler() {
            @Override
            public void positive(String title, String description, boolean showTimes) {
                String newTitle = null;
                String newText = null;
                if(!title.isEmpty()){
                    System.out.println("title changed");
                    newTitle = title;
                }
                if(!description.isEmpty()){
                    System.out.println("description changed");
                    newText = description;
                }
                if (!title.isEmpty() || !description.isEmpty()) {
                    mViewModel.editTimeline(id, newTitle, newText, showTimes);
                }
            }
        },R.string.edit);
        dialogFragment.show(getSupportFragmentManager(),"b");
    }

    @Override
    public boolean onCreateOptionsMenu(final Menu menu) {
        getMenuInflater().inflate(R.menu.home_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

}