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
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

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
        mViewModel.getTimeline(id).observe(this, new Observer<Timeline>() {
            @Override
            public void onChanged(Timeline timeline) {
                boolean showTimes = timeline.showTimes;
            }
        });
        mViewModel.setTimeline(id);
        myToolbar.setTitle(name);

        setSupportActionBar(myToolbar);
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
        RecyclerView entryList = findViewById(R.id.entryList);
        entryList.setLayoutManager(new LinearLayoutManager(this));
        entryList.addItemDecoration(new DividerItemDecoration(this,DividerItemDecoration.VERTICAL));
        EntryAdapter theAdapter = new EntryAdapter(new EntryAdapter.ClickHandler() {
            @Override
            public void click(View v,int position) {
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
                            case R.id.edit_entry:
                                addEntry(position,true);
                                return true;
                            case R.id.edit_entry_time:
                                editTime();
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
        int requestType = requests.get(requestCode);
        if (requestCode >= 1 && requestType == 1 && resultCode == RESULT_OK) {
            data.hasExtra("data");
            //Bitmap thumbnail = data.getParcelableExtra("data");
            Uri fullPhotoUri = data.getData();
            String URIString = fullPhotoUri.toString();
            mViewModel.setURI(requestCode,URIString);
            System.out.println(requests.remove(requestCode));
        } else if (requestType == 2 && resultCode == RESULT_OK) {
            System.out.println(requests.remove(requestCode));
            //mViewModel.setURI(requestCode,filePath);
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

    public void addEntry (Integer id,boolean edit) {
        DialogFragment dialogFragment = new CreateTimeline(new CreateTimeline.ClickHandler() {
            @Override
            public void positive(String title, String description, boolean showTimes) {
                if (!edit){
                    mViewModel.addEntry(title,description,1);
                }
                else{
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
                        mViewModel.editEntry(id, newTitle, newText);
                    }
                }
            }
        },R.string.add_entries);
        dialogFragment.show(getSupportFragmentManager(),"a");
    }

    public void editTime () {
        DialogFragment dialogFragment = new PickDate();
        dialogFragment.show(getSupportFragmentManager(),"");
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
                addEntry(null,false);
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