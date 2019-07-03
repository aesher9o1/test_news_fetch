package com.aesher.test.news.Adapter;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.aesher.test.news.Const.NewsModel;
import com.aesher.test.news.Interface.RecyclerNewsClick;
import com.aesher.test.news.R;
import com.squareup.picasso.Picasso;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.TimeUnit;

public class NewsRecycler extends RecyclerView.Adapter<NewsRecycler.ViewHolder> implements Filterable {

    private List<NewsModel> locationModel;
    private List<NewsModel> newsModelFiltered;

    public NewsRecycler(List<NewsModel> locationModel){
       this.locationModel = locationModel;
       this.newsModelFiltered = locationModel;
    }
    private RecyclerNewsClick newsClickListner;

    public void setNewsClickListener(RecyclerNewsClick newsClickListener){
        this.newsClickListner = newsClickListener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.layout_news_cards,viewGroup,false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder viewholder, int i) {

        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-mm-dd'T'HH:mm:ss", Locale.US);
        Date now = null;
        try {
            now = formatter.parse(formatter.format(new Date()));
        } catch (ParseException e) {
            e.printStackTrace();
        }

        assert now != null;
        String difference = TimeUnit.MILLISECONDS.toHours(now.getTime()- newsModelFiltered.get(i).getPublishedAt().getTime())+" hours ago";

        Picasso.get().load(newsModelFiltered.get(i).getUrlToImage()).placeholder(R.mipmap.ic_launcher).into(viewholder.newsImage);
        viewholder.title.setText(newsModelFiltered.get(i).getTitle());
        viewholder.source.setText(newsModelFiltered.get(i).getSource());
        viewholder.publishedTime.setText(difference);

        viewholder.body.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                newsClickListner.newsClicked(newsModelFiltered.get(viewholder.getAdapterPosition()));
            }
        });
    }

    @Override
    public int getItemCount() {
        return newsModelFiltered.size();
    }

    @Override
    public Filter getFilter() {
        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence constraint) {
                String charString = constraint.toString();


                if (charString.isEmpty())
                    newsModelFiltered = locationModel;
                else {
                    List<NewsModel> filteredList = new ArrayList<>();
                    for(NewsModel newsModel : locationModel){
                        if(newsModel.getTitle().toLowerCase().contains(charString.toLowerCase()))
                            filteredList.add(newsModel);
                    }
                    newsModelFiltered = filteredList;
                }
                FilterResults filterResults = new FilterResults();
                filterResults.values = newsModelFiltered;
                return  filterResults;
            }

            @Override
            protected void publishResults(CharSequence constraint, FilterResults results) {
                newsModelFiltered = (ArrayList<NewsModel>)results.values;
                notifyDataSetChanged();
            }
        };
    }

    public void showOnlyFrom(String source){
        List<NewsModel> filteredList = new ArrayList<>();
        for(NewsModel newsModel : locationModel){
            if(newsModel.getSource().toLowerCase().contains(source.toLowerCase()))
                filteredList.add(newsModel);
        }
        newsModelFiltered = filteredList;
        notifyDataSetChanged();
    }

    public void sortByDesc(){
        newsModelFiltered =locationModel;
        Collections.reverse(newsModelFiltered);
        notifyDataSetChanged();
    }

    public  void resetFilter(){
        newsModelFiltered = locationModel;
    }


        static class ViewHolder extends RecyclerView.ViewHolder {
        ImageView newsImage;
        TextView title,source,publishedTime;
        LinearLayout body;

        private ViewHolder(@NonNull View itemView) {
            super(itemView);
              newsImage= itemView.findViewById(R.id.newsImage);
              title = itemView.findViewById(R.id.title);
              source = itemView.findViewById(R.id.source);
              publishedTime = itemView.findViewById(R.id.publishedTime);
              body = itemView.findViewById(R.id.body);
        }
    }
}
