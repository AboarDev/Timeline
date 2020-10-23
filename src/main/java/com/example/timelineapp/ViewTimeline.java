package com.example.timelineapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
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
        entryList.addItemDecoration(new DividerItemDecoration(this,DividerItemDecoration.VERTICAL));
        EntryAdapter theAdapter = new EntryAdapter(new EntryAdapter.ClickHandler() {
            @Override
            public void click(int position) {
                getContent(position);
            }
        });
        entryList.setAdapter(theAdapter);
        mViewModel.getAllEntries().observe(this, new Observer<List<Entry>>() {
            @Override
            public void onChanged(List<Entry> entries) {
                theAdapter.setItems(entries);
            }
        });

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode >= 1 && resultCode == RESULT_OK) {
            data.hasExtra("data");
            Bitmap thumbnail = data.getParcelableExtra("data");
            boolean hasThumb = thumbnail == null;
            Uri fullPhotoUri = data.getData();
            String URIString = fullPhotoUri.toString();
            mViewModel.setURI(requestCode,URIString);
            //ImageView theImageView = findViewById(R.id.imageView);
            //theImageView.setImageURI(fullPhotoUri);
            //mViewModel.saveUri(fullPhotoUri, true);
        }
    }

    public void getContent (int id) {
        final int REQUEST_IMAGE_GET = id;
        Intent theIntent = new Intent(Intent.ACTION_OPEN_DOCUMENT);
        theIntent.setType("image/*");
        if (theIntent.resolveActivity(getPackageManager()) != null) {
            startActivityForResult(theIntent, REQUEST_IMAGE_GET);
        }
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