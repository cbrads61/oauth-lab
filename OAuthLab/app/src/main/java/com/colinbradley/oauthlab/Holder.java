package com.colinbradley.oauthlab;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

/**
 * Created by colinbradley on 11/17/16.
 */

public class Holder extends RecyclerView.ViewHolder {

    TextView mTweet, mTimeAndDate;

    public Holder(View itemView) {
        super(itemView);

        mTimeAndDate = (TextView)itemView.findViewById(R.id.time_date);
        mTweet = (TextView)itemView.findViewById(R.id.tweet);
    }
}
