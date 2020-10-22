package com.example.timelineapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.DialogFragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import java.util.List;

public class ViewTimeline extends AppCompatActivity {

    TimelineViewModel mViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_timeline);
        mViewModel = new ViewModelProvider(this).get(TimelineViewModel.class);
        Toolbar myToolbar = findViewById(R.id.timelineBar);
        Bundle extras = getIntent().getExtras();
        String name = extras.getString(Intent.EXTRA_TITLE,"Not found");
        Integer id = extras.getInt(Intent.EXTRA_INDEX,-1);
        mViewModel.setTimeline(id);
        myToolbar.setTitle(name);
        setSupportActionBar(myToolbar);

        RecyclerView entryList = findViewById(R.id.entryList);
        entryList.setLayoutManager(new LinearLayoutManager(this));
        EntryAdapter theAdapter = new EntryAdapter();
        entryList.setAdapter(theAdapter);
        mViewModel.getAllEntries().observe(this, new Observer<List<Entry>>() {
            @Override
            public void onChanged(List<Entry> entries) {
                theAdapter.setItems(entries);
            }
        });

    }

    public void addEntry () {
        DialogFragment dialogFragment = new CreateTimeline(new CreateTimeline.ClickHandler() {
            @Override
            public void positive(String title, String description, boolean showTimes) {
                mViewModel.addEntry(title,description,1);
            }
        });
        dialogFragment.show(getSupportFragmentManager(),"a");
    }

    @Override
    public boolean onCreateOptionsMenu(final Menu menu) {
        getMenuInflater().inflate(R.menu.timeline_view_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.add_entries:
                addEntry();
            default:
                return false;
        }
    }

}