package com.aesher.test.news.Activities.Main;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.aesher.test.news.R;

public class MainActivity extends AppCompatActivity implements  MainActivityPresenter.View {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    @Override
    public void toggleDarkUI() {

    }
}
