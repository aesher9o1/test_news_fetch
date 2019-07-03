package com.aesher.test.news.Activities.Main;

import android.content.Context;
import android.telephony.TelephonyManager;
import android.util.Log;

import com.aesher.test.news.Const.Constants;
import com.aesher.test.news.Const.LocalData;
import com.aesher.test.news.Const.NewsModel;
import com.aesher.test.news.Const.NotificationScheduler;
import com.aesher.test.news.Interface.AsyncNews;
import com.aesher.test.news.R;
import com.aesher.test.news.Services.NewsService;
import com.aesher.test.news.Threads.FetchNews;

import java.util.List;

class MainActivityPresenter {
    private LocalData localData;
    private  Context context;
    private  View view;

    MainActivityPresenter(Context context, View view){
        this.context = context;
        this.localData = new LocalData(context);
        this.view = view;

        if(localData.get_darkUI())
            context.setTheme(R.style.AppThemeDark);
    }


    private String createRequestUrl(Boolean calledByRefresh){
        String param = context.getResources().getConfiguration().locale.getCountry();
        String normalQueryUrl= "http://newsapi.org/v2/top-headlines?country="+param+"&apiKey="+ Constants.ConsumerKey;

        localData.set_queryURL(normalQueryUrl);

        if(calledByRefresh)
            return normalQueryUrl;

        String restoredText = localData.get_body();

        return (restoredText==null)? normalQueryUrl : restoredText;

    }

    void setAlarm(int h, int m){
        localData.set_hour(h);
        localData.set_min(m);
        NotificationScheduler.setReminder(context , NewsService.class, localData.get_hour(), localData.get_min());

    }


    void fetchNews(boolean calledByRefresh){

        new FetchNews(new AsyncNews() {
            @Override
            public void processFinished(List<NewsModel> s, String body) {
                view.setNews(s);
                Log.w("Aashis", s.toString());

                if(s.size()!=0)
                    localData.set_body(body);

            }
        }).execute(createRequestUrl(calledByRefresh));
    }

    public  interface  View{
        void toggleDarkUI();
        void  setNews(List<NewsModel> s);
        void showNews(final NewsModel news);
        void showDateTimePicker(int h, int m);
    }
}
