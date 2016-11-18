package com.colinbradley.oauthlab;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

/**
 * Created by colinbradley on 11/17/16.
 */

public class Adapter extends RecyclerView.Adapter<Holder> {

    private List<Tweet> mTweetList;

    public Adapter(List<Tweet> tweetList){
        mTweetList = tweetList;
    }

    @Override
    public Holder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.tweet_layout,parent,false);
        return new Holder(v);
    }

    @Override
    public void onBindViewHolder(Holder holder, int position) {
        holder.mTweet.setText(mTweetList.get(position).getTweet());
        holder.mTimeAndDate.setText(mTweetList.get(position).getTimeOf());

    }

    @Override
    public int getItemCount() {
        return mTweetList.size();
    }
}
