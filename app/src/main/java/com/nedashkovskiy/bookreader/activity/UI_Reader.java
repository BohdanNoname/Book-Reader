package com.nedashkovskiy.bookreader.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.widget.TextView;

import com.nedashkovskiy.bookreader.R;

import java.io.File;
import java.util.Arrays;
import java.util.Objects;

import butterknife.BindView;
import butterknife.ButterKnife;

public class UI_Reader extends AppCompatActivity {

    @BindView(R.id.text)
    TextView view;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reader);
        ButterKnife.bind(this);

        Intent intent = getIntent();

        view.setText(intent.getStringExtra(UI_MainActivity.INTENT_KEY));
    }
}