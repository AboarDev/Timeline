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
import android.view.MenuInflater;
import android.view.View;
import android.widget.ImageView;
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
        //getContent();

        RecyclerView timelineList = findViewById(R.id.timelineList);
        timelineList.setLayoutManager(new LinearLayoutManager(this));
        timelineList.addItemDecoration(new DividerItemDecoration(this,DividerItemDecoration.VERTICAL));
        TimelineAdapter theAdapter = new TimelineAdapter(new TimelineAdapter.ClickHandler() {
            @Override
            public void click(int position,String title) {
                System.out.println(position);
                //mViewModel.setTimeline(position);
                Intent theIntent = new Intent(MainActivity.this, ViewTimeline.class);
                theIntent.putExtra(Intent.EXTRA_INDEX,position);
                theIntent.putExtra(Intent.EXTRA_TITLE,title);
                startActivity(theIntent);
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

        //takeImage();
    }

    public void showMenu (View view){
        PopupMenu popupMenu = new PopupMenu(this,view);
        MenuInflater inflater = popupMenu.getMenuInflater();
        inflater.inflate(R.menu.timeline_menu,popupMenu.getMenu());
        popupMenu.show();
    }

    public void getContent (View view) {
        final int REQUEST_IMAGE_GET = 1;

        Intent theIntent = new Intent(Intent.ACTION_GET_CONTENT);
        theIntent.setType("image/*");
        if (theIntent.resolveActivity(getPackageManager()) != null) {
            startActivityForResult(theIntent, REQUEST_IMAGE_GET);
        }

    }
    public File createFile() throws IOException {
        File dir = getExternalFilesDir("my_images");
        File image = File.createTempFile(String.valueOf(fileCount) + "thisisafilename",".jpg",dir);
        filePath = image.getAbsolutePath();

        fileCount++;
        return image;
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

    public void takeImage () {
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
                startActivityForResult(theIntent, 2);
            }
        }
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
            //ImageView theImageView = findViewById(R.id.imageView);
            //theImageView.setImageURI(fullPhotoUri);
            mViewModel.saveUri(fullPhotoUri, true);
        }
        else if (requestCode == 2 && resultCode == RESULT_OK){
            //ImageView theImageView = findViewById(R.id.imageView);
            System.out.println(data);
            Uri theUri = Uri.parse(filePath);
            //theImageView.setImageURI(theUri);
            mViewModel.saveUri(theUri, false);
        }
    }


}