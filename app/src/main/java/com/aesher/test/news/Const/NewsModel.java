package com.aesher.test.news.Const;

import java.util.Date;

public class NewsModel {
   private String source;
   private String title;
   private String desc;
   private String url;
   private String urlToImage;
   private String content;
   private Date publishedAt;

   public NewsModel(String source, String title, String desc, String url, String urlToImage, String content, Date publishedAt){
       this.source = source;
       this.title = title;
       this.desc = desc;
       this.url = url;
       this.urlToImage = urlToImage;
       this.content = content;
       this.publishedAt = publishedAt;

   }

    public String getSource() {
        return source;
    }


    public String getTitle() {
        return title;
    }

    public String getDesc() {
        return desc;
    }

    public String getUrl() {
        return url;
    }

    public String getUrlToImage() {
        return urlToImage;
    }

    public String getContent() {
        return content;
    }

    public Date getPublishedAt() {
        return publishedAt;
    }
}
