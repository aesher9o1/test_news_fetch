package com.aesher.test.news.Activities.Main;

import android.content.Context;

import com.aesher.test.news.Const.LocalData;
import com.aesher.test.news.R;

public class MainActivityPresenter {
    private LocalData localData;
    private  Context context;

    MainActivityPresenter(Context context){
        this.context = context;
        this.localData = new LocalData(context);

        if(localData.get_darkUI())
            context.setTheme(R.style.AppThemeDark);
    }

    public void setNews(){
    }

    public void fetchNews(boolean calledByRefresh){

    }

    public  interface  View{
        void toggleDarkUI();
    }
}
