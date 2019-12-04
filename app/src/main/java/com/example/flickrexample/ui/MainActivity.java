package com.example.flickrexample.ui;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.example.flickrexample.R;
import com.example.flickrexample.ui.MainFragment;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.main_activity);

        if (savedInstanceState == null) {
            getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.layout_container, MainFragment.newInstance())
                    .commitNow();
        }
    }
}
