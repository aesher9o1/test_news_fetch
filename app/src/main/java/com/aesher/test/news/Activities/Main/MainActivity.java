package com.aesher.test.news.Activities.Main;


import android.net.Uri;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.aesher.test.news.Const.NewsModel;
import com.aesher.test.news.Interface.RecyclerNewsClick;
import com.aesher.test.news.R;
import com.aesher.test.news.Adapter.NewsRecycler;
import com.mapzen.speakerbox.Speakerbox;
import com.sothree.slidinguppanel.SlidingUpPanelLayout;
import com.squareup.picasso.Picasso;

import java.util.List;
import java.util.stream.Stream;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class MainActivity extends AppCompatActivity implements  MainActivityPresenter.View {

    private static final String TAG ="MainActivity" ;
    MainActivityPresenter presenter;
    Speakerbox speakerbox;
    NewsRecycler newsRecycler;

    @BindView(R.id.recyclerView)
    RecyclerView recyclerView;
    @BindView(R.id.swipeRefresh)
    SwipeRefreshLayout swipeRefreshLayout;
    @BindView(R.id.sliding_layout)
    SlidingUpPanelLayout slidingUpPanelLayout;
    @BindView(R.id.searchBox)
    EditText searchBox;
    @BindView(R.id.notification)
    ImageView notification;

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


    @OnClick(R.id.notification)
    public void showNotificationHandlerDialogue(){
        presenter.showDateTimePicker();
    }
    @OnClick(R.id.nightView)
    public void toggleNightView(){
        presenter.toggleNightView();
    }
    @OnClick(R.id.menu)
    public void buildShowMenu(View v){
        PopupMenu menu = new PopupMenu(this, v);
        menu.getMenu().add("Sort by Time").setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                newsRecycler.sortByDesc();
                return false;
            }
        });

        for(int i =0; i<presenter.getDistinctSources().length;i++){
            final int finalI = i;
            menu.getMenu().add("Show "+ presenter.getDistinctSources()[i]).setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
                @Override
                public boolean onMenuItemClick(MenuItem item) {
                    newsRecycler.showOnlyFrom(presenter.getDistinctSources()[finalI]);
                    return false;
                }
            });
        }

        menu.getMenu().add("Reset Filters").setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                newsRecycler.resetFilter();
                return false;
            }
        });
        menu.show();
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        presenter = new MainActivityPresenter(this, this);

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ButterKnife.bind(this);
        speakerbox = new Speakerbox(getApplication());

        presenter.fetchNews(false);
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                presenter.fetchNews(true);
            }
        });

    }


    @Override
    public void setNews(List<NewsModel> s) {

        swipeRefreshLayout.setRefreshing(false);

        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        recyclerView.setNestedScrollingEnabled(true);
        newsRecycler = new NewsRecycler(s);
        recyclerView.setAdapter(newsRecycler);

        newsRecycler.setNewsClickListener(new RecyclerNewsClick() {
            @Override
            public void newsClicked(NewsModel newsModel) {
                showNews(newsModel);
            }
        });



        searchBox.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) { }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                newsRecycler.getFilter().filter(s);
            }

            @Override
            public void afterTextChanged(Editable s) { }
        });
    }

    @Override
    public void showNews(final NewsModel news) {
        heading.setText(news.getTitle());
        Picasso.get().load(news.getUrlToImage()).placeholder(R.mipmap.ic_launcher).into(newsImage);
        newsContent.setText(news.getContent());

        slidingUpPanelLayout.setPanelState(SlidingUpPanelLayout.PanelState.EXPANDED);
        speak.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                speakerbox.play(news.getContent());
            }
        });

        share.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                presenter.shareNews(news.getTitle(), news.getUrl());
            }
        });

        openBrowser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                presenter.openBrowserWithNews(Uri.parse(news.getUrl()));
            }
        });

    }


}
