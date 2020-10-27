package com.example.timelineapp;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
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
import android.transition.Explode;
import android.view.ContextMenu;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.PopupMenu;

import java.io.File;
import java.io.IOException;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class ViewTimeline extends AppCompatActivity {

    TimelineViewModel mViewModel;

    Map<Integer, Integer> requests;

    String filePath;

    int fileCount;

    public ViewTimeline() {
        super();
        requests = new HashMap<Integer, Integer>();
    }

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
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        RecyclerView entryList = findViewById(R.id.entryList);
        entryList.setLayoutManager(new LinearLayoutManager(this));
        entryList.addItemDecoration(new DividerItemDecoration(this,DividerItemDecoration.VERTICAL));
        EntryAdapter theAdapter = new EntryAdapter(new EntryAdapter.ClickHandler() {
            @Override
            public void click(View v,int position) {
                //getContent(position);
                PopupMenu popupMenu = new PopupMenu(ViewTimeline.this,v);
                MenuInflater menuInflater = popupMenu.getMenuInflater();
                menuInflater.inflate(R.menu.entry_context_menu,popupMenu.getMenu());
                popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem item) {
                        switch (item.getItemId()){
                            case R.id.add_image:
                                getContent(position);
                                return true;
                            case R.id.delete:
                                mViewModel.deleteEntry(position);
                                return true;
                            case R.id.new_image:
                                takeImage(position);
                                return true;
                            default:
                                return false;
                        }
                    }
                });
                popupMenu.show();
            }
        });
        entryList.setAdapter(theAdapter);
        //getWindow().setExitTransition(new Explode());
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
        if (requestCode >= 1 && requests.get(requestCode) == 1 && resultCode == RESULT_OK) {
            data.hasExtra("data");
            Bitmap thumbnail = data.getParcelableExtra("data");
            Uri fullPhotoUri = data.getData();
            String URIString = fullPhotoUri.toString();
            mViewModel.setURI(requestCode,URIString);
            System.out.println(requests.remove(requestCode));
        } else if (requests.get(requestCode) == 2 && resultCode == RESULT_OK) {
            System.out.println(requests.remove(requestCode));
            Uri theUri = Uri.parse(filePath);
            Uri fullPhotoUri = data.getData();
            mViewModel.setURI(requestCode,filePath);
            //MediaStore.getMediaUri(this,theUri);
        }
    }

    public File createFile() throws IOException {
        File dir = getExternalFilesDir("my_images");
        File image = File.createTempFile(String.valueOf(fileCount) + "thisisafilename",".jpg",dir);
        filePath = image.getAbsolutePath();

        fileCount++;
        return image;
    }

    public void takeImage (int id) {
        requests.put(id, 2);
        Intent theIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

        if(theIntent.resolveActivity(getPackageManager()) != null){
            File image = null;
            try{
                image = createFile();
            } catch (IOException exception){

            }
            if (image != null){
                Uri theUri = FileProvider.getUriForFile(this,"com.example.android.fileprovider",image);
                theIntent.putExtra(MediaStore.EXTRA_OUTPUT, theUri);
                startActivityForResult(theIntent, id);
            }
        }
    }


    public void getContent (int id) {
        final int REQUEST_IMAGE_GET = id;
        requests.put(id, 1);
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
        },"Add Entry");
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

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }
    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return true;
    }

}