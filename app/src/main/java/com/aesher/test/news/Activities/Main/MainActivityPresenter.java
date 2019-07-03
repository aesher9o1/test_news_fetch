package com.aesher.test.news.Activities.Main;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.TimePicker;
import android.widget.Toast;

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
    String distinctSource;

    MainActivityPresenter(Context context, View view){
        this.context = context;
        this.localData = new LocalData(context);
        this.view = view;

        if(localData.get_darkUI())
            context.setTheme(R.style.AppThemeDark);
    }

    /**
     * Returns a URL based on User's location as set on their phone. If it is called by the SwipeViewLayout
     * it takes in the data from the SharedPreference generates URL and returns it. Else generates the URL and
     * save it to the local storage
     *
     * @param calledByRefresh -> Default is false but when called by swipe view it is true
     * @return -> URL of the news feed API
     */
    private String createRequestUrl(Boolean calledByRefresh){
        String param = context.getResources().getConfiguration().locale.getCountry();
        String normalQueryUrl= "http://newsapi.org/v2/top-headlines?country="+param+"&apiKey="+ Constants.ConsumerKey;

        localData.set_queryURL(normalQueryUrl);

        if(calledByRefresh)
            return normalQueryUrl;

        String restoredText = localData.get_body();

        return (restoredText==null)? normalQueryUrl : restoredText;

    }

    /**
     * Responsible to tell the Notification Scheduler when to set the alarm
     *
     * @param h -> Hour as to when we have to set the alarm
     * @param m -> Minutes as to when we have to set the alarm
     */

    private void setAlarm(int h, int m){
        localData.set_hour(h);
        localData.set_min(m);
        NotificationScheduler.setReminder(context , NewsService.class, localData.get_hour(), localData.get_min());

    }

    /**
     * Responsible to fetch set and store the news fetched from the AsyncTask
     *
     * @param calledByRefresh -> Default is false and if called by SwipeViewLayout it is true
     */

    void fetchNews(boolean calledByRefresh){

        new FetchNews(new AsyncNews() {
            @Override
            public void processFinished(List<NewsModel> s, String body) {
                view.setNews(s);


                if(s.size()!=0){
                    localData.set_body(body);
                    distinctSource = "";

                    for(int i =0; i<s.size();i++)
                        if(!distinctSource.contains(s.get(i).getSource()))
                            distinctSource = distinctSource + s.get(i).getSource()+ "/";



                }


            }
        }).execute(createRequestUrl(calledByRefresh));
    }


    /**
     * Share of Whatsapp etc
     *
     * @param title -> News Title
     * @param url -> News Link
     */

    void shareNews(String title, String url){
        Intent share = new Intent(android.content.Intent.ACTION_SEND);
        share.setType("text/plain");
        share.putExtra(Intent.EXTRA_SUBJECT, title);
        share.putExtra(Intent.EXTRA_TEXT, url);

        context.startActivity(Intent.createChooser(share, "Share the amazing news with your friends"));
    }

    void openBrowserWithNews(Uri newsUri){
        Intent i = new Intent(Intent.ACTION_VIEW);
        i.setData(newsUri);
        context.startActivity(i);
    }

    void toggleNightView(){
        localData.set_darkUI();
        context.startActivity(new Intent(context,MainActivity.class));
    }

    void showDateTimePicker(){
        Activity activity  = (Activity) context;
        final android.view.View dialogView = LayoutInflater.from(context).inflate(R.layout.layout_timepicker, (ViewGroup) activity.findViewById(android.R.id.content), false);
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setView(dialogView);
        builder.setPositiveButton("Set Alarm", new DialogInterface.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.M)
            @Override
            public void onClick(DialogInterface dialog, int which) {
                TimePicker timePicker = dialogView.findViewById(R.id.timePicker);

                int hour = 0;
                int min = 0;

                int currentApiVersion = android.os.Build.VERSION.SDK_INT;
                if (currentApiVersion > android.os.Build.VERSION_CODES.LOLLIPOP_MR1){
                    hour = timePicker.getHour();
                    min = timePicker.getMinute();
                } else {
                    hour = timePicker.getCurrentHour();
                    min = timePicker.getCurrentMinute();
                }


                setAlarm(hour,min);

                Toast.makeText(context, "Your notification has been scheduled for the given time", Toast.LENGTH_LONG).show();

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

    public String[] getDistinctSources (){
       return distinctSource.split("/");
    }


    public  interface  View{
        void  setNews(List<NewsModel> s);
        void showNews(final NewsModel news);
    }
}
