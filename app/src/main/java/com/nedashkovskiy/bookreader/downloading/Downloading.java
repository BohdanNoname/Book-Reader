package com.nedashkovskiy.bookreader.downloading;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.os.Build;
import android.os.IBinder;
import android.webkit.MimeTypeMap;

import androidx.annotation.Nullable;
import androidx.core.app.NotificationCompat;

import com.nedashkovskiy.bookreader.App;
import com.nedashkovskiy.bookreader.R;
import com.nedashkovskiy.bookreader.activity.UI_MainActivity;


import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;

public class Downloading extends Service {
    private final String CHANNEL_ID = "NotificationChanel";

    public static final String MASSAGE_DOWNLOADING_FOREGROUND_SERVICE = "Ваш файл загружается в данный момент!";
    public static final String MASSAGE_STOP_FOREGROUND_SERVICE = "Загрузка файла завершена!";
    public static final String MAIN_LINK = "http://flibusta.is";

    private boolean startingPermission = false;
    private String bookLink;
    private String bookName;

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        App.FolderCreating.createFolder();
        bookLink = intent.getStringExtra("bookLink");
        bookName = intent.getStringExtra("bookName");

        try {
            startForeground(1, pushNotification(MASSAGE_DOWNLOADING_FOREGROUND_SERVICE, true));
            startForeground(2, pushNotification(MASSAGE_STOP_FOREGROUND_SERVICE, false));

            stopForeground(false);}
        catch (Exception e) {
            e.printStackTrace();
        }

        return Service.START_STICKY;
    }

    private Notification pushNotification(String contentText, boolean isProgressBar) {
        Intent notificationIntent = new Intent(this, UI_MainActivity.class);
        PendingIntent pendingIntent = PendingIntent.getService(getApplicationContext(), 0, notificationIntent, PendingIntent.FLAG_UPDATE_CURRENT);

        checkVersion();

        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(Downloading.this, CHANNEL_ID)
                .setContentText(contentText)
                .setSmallIcon(R.drawable.download_icon)
                .setContentIntent(pendingIntent);

        if (isProgressBar && bookName != null && bookLink != null) {
            Notification notificationProgress = notificationBuilder.
                    setProgress(100, 0, true)
                    .build();
            startingPermission = true;
            return notificationProgress;
        } else if (bookName != null && bookLink != null && startingPermission) {
            downloadingFile(bookLink, bookName);
            Notification notification = notificationBuilder
                    .build();
            return notification;
        } return null;
    }

    private void downloadingFile(String bookLink, String bookName) {
        try {
            URL url = new URL( MAIN_LINK + bookLink + "/download");
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();

            BufferedInputStream inputStream = new BufferedInputStream(connection.getInputStream());

            FileOutputStream outputStream = new FileOutputStream
                    (App.MAIN_PATH_FOR_BOOKS + bookName + '.' + setFormat(connection.getContentType()));

            BufferedOutputStream bufferedOutputStream = new BufferedOutputStream(outputStream, 1024);

            double size = (double) connection.getContentLengthLong();

            byte[] data = new byte[1024];
            double downloaded = 0.00;
            int read = 0;
            double percent = 0.00;

            while ((read = inputStream.read(data, 0, 1024)) >= 0 ){
                bufferedOutputStream.write(data, 0, read);
                downloaded += read;
                percent = ((downloaded*100)/size);
                String done = String.format("%.2f", percent);
                System.out.println("Downloaded: " + done + "% of a file");
            }

            bufferedOutputStream.close();
            inputStream.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void checkVersion(){
        if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel notificationChannel = new NotificationChannel(CHANNEL_ID, "EXE", NotificationManager.IMPORTANCE_DEFAULT);
            NotificationManager manager = getSystemService(NotificationManager.class);
            assert manager != null;
            manager.createNotificationChannel(notificationChannel);
        }
    }

    private String setFormat(String mime){
        int point = 0;
        for(int i = 0; i < mime.length(); i++){
            if(mime.charAt(i) == '/'){
                point = i + 1;
                break;
            }
        }

        char[] mas = new char[mime.length() - point];
        int masIndex = 0;
        for (int i = point; i < mime.length(); i++ ){
            mas[masIndex] = mime.charAt(i);
            masIndex++;
        }
        return new String(mas);
    }
}
