package com.aesher.test.news.Activities.Main;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.aesher.test.news.Const.NewsModel;
import com.aesher.test.news.Interface.RecyclerNewsClick;
import com.aesher.test.news.R;
import com.aesher.test.news.Adapter.NewsRecycler;
import com.mapzen.speakerbox.Speakerbox;
import com.sothree.slidinguppanel.SlidingUpPanelLayout;
import com.squareup.picasso.Picasso;

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
                Intent share = new Intent(android.content.Intent.ACTION_SEND);
                share.setType("text/plain");
                share.putExtra(Intent.EXTRA_SUBJECT, news.getTitle());
                share.putExtra(Intent.EXTRA_TEXT, news.getUrl());

                startActivity(Intent.createChooser(share, "Share the amazing news with your friends"));
            }
        });

        openBrowser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(Intent.ACTION_VIEW);
                i.setData(Uri.parse(news.getUrl()));
                startActivity(i);
            }
        });

    }

    @Override
    public void showDateTimePicker(int h, int m) {
        final View dialogView = LayoutInflater.from(this).inflate(R.layout.layout_timepicker, (ViewGroup) findViewById(android.R.id.content), false);
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setView(dialogView);
        builder.setPositiveButton("Set Alarm", new DialogInterface.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.M)
            @Override
            public void onClick(DialogInterface dialog, int which) {
                TimePicker timePicker = dialogView.findViewById(R.id.timePicker);
                presenter.setAlarm(timePicker.getHour(),timePicker.getMinute());

               Toast.makeText(getApplicationContext(), "Your notification has been scheduled for the given time", Toast.LENGTH_LONG).show();

            }
        });
        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }

}
