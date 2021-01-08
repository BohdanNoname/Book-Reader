package com.nedashkovskiy.bookreader;

import android.app.Application;
import android.os.AsyncTask;
import android.os.Environment;
import android.os.StrictMode;

import java.io.File;

public class App extends Application {

    public static String MAIN_PATH_FOR_BOOKS = Environment.getExternalStorageDirectory() + "/Book Reader/";

    @Override
    public void onCreate() {
        super.onCreate();
        FolderCreating folderCreating = new FolderCreating();

        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);
    }

    /*_____________________________________________________________*/
    public static class FolderCreating extends AsyncTask<String, Void, Void> {

        public FolderCreating() {
            doInBackground();
        }

        @Override
        protected Void doInBackground(String... strings) {
            createFolder();
            return null;
        }

        public static void createFolder() {
            File dir = new File(Environment.getExternalStorageDirectory() + "/Book Reader");
            if (!dir.exists()) {
                dir.mkdirs();
            }
        }
    }
}
