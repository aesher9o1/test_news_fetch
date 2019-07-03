package com.aesher.test.news.Activities.Main;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.aesher.test.news.R;
import com.mapzen.speakerbox.Speakerbox;

import butterknife.ButterKnife;

public class MainActivity extends AppCompatActivity implements  MainActivityPresenter.View {


    MainActivityPresenter presenter;
    Speakerbox speakerbox;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        presenter = new MainActivityPresenter(this);

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ButterKnife.bind(this);
        speakerbox = new Speakerbox(getApplication());

        presenter.fetchNews(false);
    }

    @Override
    public void toggleDarkUI() {

    }
}
