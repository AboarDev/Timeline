package com.example.timelineapp;

import android.content.ContentResolver;
import android.content.Context;
import android.content.res.Resources;
import android.net.Uri;

import androidx.annotation.NonNull;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.room.migration.Migration;
import androidx.sqlite.db.SupportSQLiteDatabase;

import java.text.SimpleDateFormat;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Database(entities = {Timeline.class, Entry.class, MediaBinding.class}, version = 4)
public abstract class ModelDatabase extends RoomDatabase {

    static final Migration MIGRATION_2_3 = new Migration(3, 4) {
        @Override
        public void migrate(SupportSQLiteDatabase database) {
            database.execSQL("ALTER TABLE entry_table "
                    + " ADD COLUMN dateTime TEXT");
        }
    };

    public abstract TimelineDao timelineDao();
    public abstract EntryDao entryDao();
    public abstract MediaBindingDao mediaBindingDao();

    public synchronized static ModelDatabase getInstance(Context context) {
        if (INSTANCE == null) {
            INSTANCE = getDatabase(context);
        }
        return INSTANCE;
    }

    public static Uri getURI(Context context, int id){
        Resources resources = context.getResources();
        return new Uri.Builder().scheme(ContentResolver.SCHEME_ANDROID_RESOURCE)
                .authority(resources.getResourcePackageName(id))
                .appendPath(resources.getResourceTypeName(id))
                .appendPath(resources.getResourceEntryName(id))
                .build();
    }

    private static volatile ModelDatabase INSTANCE;
    private static final int NUMBER_OF_THREADS = 4;
    static final ExecutorService databaseWriteExecutor =
            Executors.newFixedThreadPool(NUMBER_OF_THREADS);
    static ModelDatabase getDatabase(final Context context){
        if (INSTANCE == null){
            synchronized (ModelDatabase.class){
                if (INSTANCE == null){
                    INSTANCE = Room.databaseBuilder(context.getApplicationContext(),
                            ModelDatabase.class,"timeline_database")
                            .addMigrations(MIGRATION_2_3).addCallback(new RoomDatabase.Callback() {
                        public void onCreate (@NonNull SupportSQLiteDatabase db) {
                            super.onOpen(db);
                            Executors.newSingleThreadScheduledExecutor().execute(() -> {
                                //Improved examples for initial prototype
                                ModelDatabase modelDatabase = getInstance(context);
                                SimpleDateFormat formatTime = new SimpleDateFormat("dd/MM/yyyy - h:mm");
                                Timeline timeline = new Timeline("Example",
                                        "A simple example timeline without times shown",
                                        false);
                                modelDatabase.timelineDao().insert(timeline);
                                int id = modelDatabase.timelineDao().getByName("Example")
                                        .timelineID;
                                Entry entry = new Entry(id,"An example entry",
                                        "This entry has no images",1,"");
                                modelDatabase.entryDao().insert(entry);
                                entry = new Entry(id,"Another entry","This Entry has an image",2,
                                        "");
                                entry.URI = getURI(context,R.raw.m20200816_153134).toString();
                                modelDatabase.entryDao().insert(entry);
                                timeline = new Timeline("Example #2","A simple example timeline with times shown",true);
                                modelDatabase.timelineDao().insert(timeline);
                                id = modelDatabase.timelineDao().getByName("Example #2")
                                        .timelineID;
                                String time = "23/09/2020";
                                entry = new Entry(id,"An entry with a date","",1,time);
                                modelDatabase.entryDao().insert(entry);
                                time = "23/09/2020 - 4:38pm";
                                entry = new Entry(id,"An entry with a time","This entry has an image",2,time);
                                entry.URI = getURI(context,R.raw.m20200923_163831).toString();
                                modelDatabase.entryDao().insert(entry);
                            });
                        }
                        public void onOpen (@NonNull SupportSQLiteDatabase db) {
                            // do something every time database is open
                            super.onOpen(db);
                        }
                    }).build();
                }
            }
        }
        return INSTANCE;
    }
}
