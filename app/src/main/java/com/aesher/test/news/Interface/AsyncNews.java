package com.aesher.test.news.Interface;

import com.aesher.test.news.Activities.Main.NewsModel;

import java.util.List;

public interface AsyncNews {
    void processFinished(List<NewsModel> s, String body);

}
