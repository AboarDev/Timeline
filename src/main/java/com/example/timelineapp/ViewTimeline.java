package com.example.timelineapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.FileProvider;
import androidx.fragment.app.DialogFragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.transition.Explode;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.transition.Fade;
import android.transition.Slide;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.PopupMenu;
import android.widget.TextView;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class ViewTimeline extends AppCompatActivity {

    TimelineViewModel mViewModel;
    Map<Integer, Integer> requests;
    String filePath;
    int fileCount;
    int id;
    boolean showTimes;

    public ViewTimeline() {
        super();
        requests = new HashMap<>();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setEnterTransition(new Fade());
        getWindow().setExitTransition(new Fade());

        setContentView(R.layout.activity_view_timeline);
        mViewModel = new ViewModelProvider(this).get(TimelineViewModel.class);
        Toolbar myToolbar = findViewById(R.id.timelineBar);
        Bundle extras = getIntent().getExtras();
        id = extras.getInt(Intent.EXTRA_INDEX, -1);
        showTimes = extras.getBoolean("SHOW_TIMES");
        setSupportActionBar(myToolbar);

        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
        RecyclerView entryList = findViewById(R.id.entryList);
        entryList.setLayoutManager(new LinearLayoutManager(this));
        entryList.addItemDecoration(new DividerItemDecoration(this,
                DividerItemDecoration.VERTICAL));
        EntryAdapter theAdapter = new EntryAdapter(showTimes, new EntryAdapter.ClickHandler() {
            @Override
            public void click(View v, int position) {
                PopupMenu popupMenu = new PopupMenu(ViewTimeline.this, v);
                MenuInflater menuInflater = popupMenu.getMenuInflater();
                menuInflater.inflate(R.menu.entry_context_menu, popupMenu.getMenu());
                popupMenu.setOnMenuItemClickListener(item -> {
                    switch (item.getItemId()) {
                        case R.id.add_image:
                            getContent(position);
                            return true;
                        case R.id.delete:
                            mViewModel.deleteEntry(position);
                            return true;
                        case R.id.remove_image:
                            mViewModel.removeImage(position);
                            return true;
                        case R.id.edit_entry:
                            editEntry(position);
                            return true;
                        case R.id.edit_entry_time:
                            editTime();
                            return true;
                        default:
                            return false;
                    }
                });
                popupMenu.show();
            }
        });
        entryList.setAdapter(theAdapter);

        mViewModel.getAllEntries(id).observe(this, theAdapter::setItems);
        mViewModel.getTimeline(id).observe(this, timeline -> {
            myToolbar.setTitle(timeline.name);
            TextView description = findViewById(R.id.timelineDescription);
            if (timeline.description == null || !timeline.description.isEmpty()) {
                description.setText(timeline.description);
            } else {
                description.setVisibility(View.GONE);
                //findViewById(R.id.descriptionDivider).setVisibility(View.GONE);
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
            mViewModel.setURI(requestCode, URIString);
            System.out.println(requests.remove(requestCode));
        } else if (requestType == 2 && resultCode == RESULT_OK) {
            System.out.println(requests.remove(requestCode));
            //mViewModel.setURI(requestCode,filePath);
        }
    }

    public File createFile() throws IOException {
        File dir = getExternalFilesDir("my_images");
        File image = File.createTempFile(String.valueOf(fileCount) +
                "thisisafilename", ".jpg", dir);
        filePath = image.getAbsolutePath();
        fileCount++;
        return image;
    }

    public void takeImage(int id) {
        requests.put(id, 2);
        Intent theIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (theIntent.resolveActivity(getPackageManager()) != null) {
            File image = null;
            try {
                image = createFile();
            } catch (IOException exception) {

            }
            if (image != null) {
                Uri theUri = FileProvider.getUriForFile(this,
                        "com.example.android.fileprovider", image);
                theIntent.putExtra(MediaStore.EXTRA_OUTPUT, theUri);
                startActivityForResult(theIntent, id);
            }
        }
    }


    public void getContent(int id) {
        requests.put(id, 1);
        Intent theIntent = new Intent(Intent.ACTION_OPEN_DOCUMENT);
        theIntent.setType("image/*");
        if (theIntent.resolveActivity(getPackageManager()) != null) {
            startActivityForResult(theIntent, id);
        }
    }

    public void addEntry() {
        DialogFragment dialogFragment = new CreateTimeline(new CreateTimeline.ClickHandler() {
            @Override
            public void positive(String title, String description, boolean showTimes) {
                mViewModel.addEntry(id, title, description, 1);
            }
        }, R.string.add_entries);
        dialogFragment.show(getSupportFragmentManager(), "a");
    }

    public void editEntry(Integer id) {
        DialogFragment dialogFragment = new CreateTimeline(new CreateTimeline.ClickHandler() {
            @Override
            public void positive(String title, String description, boolean showTimes) {
                String newTitle = null;
                String newText = null;
                if (!title.isEmpty()) {
                    System.out.println("title changed");
                    newTitle = title;
                }
                if (!description.isEmpty()) {
                    System.out.println("description changed");
                    newText = description;
                }
                if (!title.isEmpty() || !description.isEmpty()) {
                    mViewModel.editEntry(id, newTitle, newText);
                }
            }
        }, R.string.entry_edit);
        dialogFragment.show(getSupportFragmentManager(), "a");
    }

    public void editTime() {
        DialogFragment dialogFragment = new PickDate();
        dialogFragment.show(getSupportFragmentManager(), "");
    }

    @Override
    public boolean onCreateOptionsMenu(final Menu menu) {
        getMenuInflater().inflate(R.menu.timeline_view_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.add_entries) {
            addEntry();
        }
        return false;
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