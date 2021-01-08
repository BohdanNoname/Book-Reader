package com.nedashkovskiy.bookreader.activity;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.recyclerview.widget.RecyclerView;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.widget.ImageView;

import com.kursx.parser.fb2.FictionBook;
import com.nedashkovskiy.bookreader.R;
import com.nedashkovskiy.bookreader.downloading.FilePathRead;


import org.xml.sax.SAXException;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;
import java.util.zip.ZipInputStream;

import javax.xml.parsers.ParserConfigurationException;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class UI_MainActivity extends AppCompatActivity {
    @BindView(R.id.global_searching)
    ImageView globalSearching;
    @BindView(R.id.folder_searching)
    ImageView folderSearching;
    @BindView(R.id.recycler_view_start_activity)
    RecyclerView recyclerView;

    private static final int REQUEST_CODE = 1001;
    public static final String INTENT_KEY = "PATH_BOOK_KEY";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start);
        ButterKnife.bind(this);
    }

    @Override
    protected void onResume() {
        super.onResume();
        checkPermission();
    }

    @OnClick(R.id.folder_searching)
    public void folderSearching() { //Поиск файлов внутри устройства
        try {
            Intent intent = new Intent(Intent.ACTION_OPEN_DOCUMENT);
            intent.setType("*/*");
            intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
            intent = Intent.createChooser(intent, "Chose a file");
            startActivityForResult(intent, REQUEST_CODE);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @OnClick(R.id.global_searching) //Поиск файла в интернете
    public void globalSearching() {
        Intent intent = new Intent(UI_MainActivity.this, UI_BookDownloading.class);
        startActivity(intent);
    }

//________________________________________________________________

    private void checkPermission() {
        Context context = getApplicationContext();
        if (ActivityCompat.checkSelfPermission(context, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(UI_MainActivity.this,
                    new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 100);
        }

        if (ActivityCompat.checkSelfPermission(context, Manifest.permission.READ_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(UI_MainActivity.this,
                    new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, 100);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CODE  && resultCode == RESULT_OK) {
            if (data != null) {
                String filePath = FilePathRead.getPath(getApplicationContext(), data.getData());
                unzipping(filePath);
                if (filePath != null) {
                    Intent intent = new Intent(UI_MainActivity.this, UI_Reader.class);
                    intent.putExtra(INTENT_KEY, filePath);
                    startActivity(intent);
                }
            }
        }
    }

    private static void unzipping(String path){
        try {
            ZipInputStream zipInputStream = new ZipInputStream(new FileInputStream(path));
            ZipFile zipFile = null;
            InputStream inputStream = null;
            BufferedReader bufferedReader = null;

            ZipEntry entry;
            long size;
            String name;

            while ((entry = zipInputStream.getNextEntry()) != null){
                name = entry.getName();
                size = entry.getSize();
                System.out.printf("File name: %s \t File size: %d \n", name, size);

                zipFile = new ZipFile(path);
                FictionBook fictionBook = new FictionBook(new File(path));
                System.out.println(fictionBook.getBody());
                System.out.println(fictionBook.getAuthors());
                inputStream = zipFile.getInputStream(entry);
                bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
                for(int i = 0; i < 100; i++){
                    System.out.println(bufferedReader.readLine());
                }

            }

            bufferedReader.close();
            zipInputStream.close();
        } catch (IOException | ParserConfigurationException | SAXException e) {
            e.printStackTrace();
        }
    }
}