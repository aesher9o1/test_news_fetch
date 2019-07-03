package com.aesher.test.news.Activities.Main;

import android.content.Context;
import android.telephony.TelephonyManager;

import com.aesher.test.news.Const.LocalData;
import com.aesher.test.news.Interface.AsyncNews;
import com.aesher.test.news.R;
import com.aesher.test.news.Threads.FetchNews;

import java.util.List;

public class MainActivityPresenter {
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
        TelephonyManager tm = (TelephonyManager)context.getSystemService(Context.TELEPHONY_SERVICE);
        String param = tm.getNetworkCountryIso();
        final String CONSUMER_KEY = "b7e520bda6fa4f989ce5906cb156a0ec";
        String normalQueryUrl= "https://newsapi.org/v2/top-headlines?country="+param+"&apiKey="+ CONSUMER_KEY;

        localData.set_queryURL(normalQueryUrl);

        if(calledByRefresh)
            return normalQueryUrl;

        String restoredText = localData.get_body();

        return (restoredText==null)? normalQueryUrl : restoredText;

    }



    public void fetchNews(boolean calledByRefresh){
        new FetchNews(new AsyncNews() {
            @Override
            public void processFinished(List<NewsModel> s, String body) {
                view.setNews(s);

                if(s.size()!=0)
                    localData.set_body(body);

            }
        }).execute(createRequestUrl(calledByRefresh));
    }

    public  interface  View{
        void toggleDarkUI();
        void  setNews(List<NewsModel> s);
    }
}
