package com.aesher.test.news.Activities.Main;

import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.aesher.test.news.R;
import com.aesher.test.news.Threads.NewsRecycler;
import com.mapzen.speakerbox.Speakerbox;
import com.sothree.slidinguppanel.SlidingUpPanelLayout;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class MainActivity extends AppCompatActivity implements  MainActivityPresenter.View {

    private static final String TAG ="MainActivity" ;

    @BindView(R.id.recyclerView)
    RecyclerView recyclerView;
    @BindView(R.id.nightView)
    ImageView NightViewToggle;
    @BindView(R.id.swipeRefresh)
    SwipeRefreshLayout swipeRefreshLayout;
    @BindView(R.id.sliding_layout)
    SlidingUpPanelLayout slidingUpPanelLayout;
    @BindView(R.id.searchBox)
    EditText searchBox;


    @OnClick(R.id.notification)
    public void showNotificationHandlerDialogue(){
//        showTimePickerDialog(localData.get_hour(), localData.get_min());
    }

    //Slide Panel Components
    @BindView(R.id.heading)
    TextView heading;
    @BindView(R.id.newsImage)
    ImageView newsImage;
    @BindView(R.id.newsContent)
    TextView newsContent;
    @BindView(R.id.speak)
    RelativeLayout speak;
    @BindView(R.id.share)
    RelativeLayout share;
    @BindView(R.id.openBrowser)
    RelativeLayout openBrowser;




    MainActivityPresenter presenter;
    Speakerbox speakerbox;
    NewsRecycler newsRecycler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        presenter = new MainActivityPresenter(this, this);

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ButterKnife.bind(this);
        speakerbox = new Speakerbox(getApplication());

        presenter.fetchNews(false);
    }


    @Override
    public void toggleDarkUI() {

    }

    @Override
    public void setNews(List<NewsModel> s) {
        swipeRefreshLayout.setRefreshing(false);

        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        recyclerView.setNestedScrollingEnabled(true);
        newsRecycler = new NewsRecycler(s);
        recyclerView.setAdapter(newsRecycler);
    }
}
