package com.aesher.test.news.Services;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.aesher.test.news.Const.NewsModel;
import com.aesher.test.news.Const.NotificationScheduler;
import com.aesher.test.news.Const.LocalData;
import com.aesher.test.news.Interface.AsyncNews;
import com.aesher.test.news.Threads.FetchNews;

import java.io.IOException;
import java.util.List;


public class NewsService extends BroadcastReceiver {
    LocalData localData;


    @Override
    public void onReceive(final Context context, Intent intent) {
        localData = new LocalData(context);
        if (intent.getAction() != null) {
            if (intent.getAction().equalsIgnoreCase(Intent.ACTION_BOOT_COMPLETED)) {
                NotificationScheduler.setReminder(context, NewsService.class,
                        localData.get_hour(), localData.get_min());
                return;
            }
        }

        new FetchNews(new AsyncNews() {
            @Override
            public void processFinished(List<NewsModel> s, String body) {
                if(s.size()!=0)
                    localData.set_body(body);
                try {
                    NotificationScheduler.showNotification(context, s.get(0).getUrlToImage(),
                            s.get(0).getTitle(), s.get(0).getDesc());
                } catch (IOException e) {
                    e.printStackTrace();
                }


            }
        }).execute(localData.get_queryURL());


    }
}
