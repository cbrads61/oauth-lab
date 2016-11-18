package com.colinbradley.oauthlab;

import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import org.apache.commons.codec.binary.Base64;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.widget.EditText;
import android.widget.Toast;

import com.google.gson.Gson;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.Headers;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class MainActivity extends AppCompatActivity {

    EditText mEditText;
    Button mSearchButton;
    RecyclerView mRecyclerView;
    String mEncodedCrudentials;
    List<Tweet> mTweetList;
    Adapter mAdapter;
    String mBearerToken;

    public static final String CONSUMER_KEY = "2Bw1FlUAKVpUTqYfDknc9nGLR";
    public static final String CONSUMER_SECRET = "oqRFXq01HQdBIWLe2Esc0cPMhMIEwNTNwGc4rsS8meqFpSDeN0";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);




        ConnectivityManager connectivityManager = (ConnectivityManager)getSystemService(CONNECTIVITY_SERVICE);
        final NetworkInfo info = connectivityManager.getActiveNetworkInfo();

        if (info != null && info.isConnected()){
            String consumerCredentials = CONSUMER_KEY + ":" + CONSUMER_SECRET;
            mEncodedCrudentials = new String(Base64.encodeBase64(consumerCredentials.getBytes()));
            getBearerToken();
        }else {
            Toast.makeText(this, "NO CONNECTION", Toast.LENGTH_SHORT).show();
        }

        mTweetList = new ArrayList<>();
        mTweetList.add(new Tweet("placeholder tweet", "time/date"));
        mRecyclerView = (RecyclerView)findViewById(R.id.recyclerview);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(MainActivity.this, LinearLayoutManager.VERTICAL,false));
        mAdapter = new Adapter(mTweetList);
        mRecyclerView.setAdapter(mAdapter);
        mEditText = (EditText)findViewById(R.id.edittext);
        mSearchButton = (Button)findViewById(R.id.searchbutton);

        mSearchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (info != null && info.isConnected()){
                    startSearch(mEditText.getText().toString());
                }
            }
        });
    }

    private void getBearerToken(){
        OkHttpClient client = new OkHttpClient();
        Headers headers = new Headers.Builder()
                .add("Authorization", "Basic " + mEncodedCrudentials)
                .add("Content-Type", "application/x-www-form-urlencoded;charset=UTF-8")
                .build();
        RequestBody body = new FormBody.Builder()
                .add("grant_type", "client_credentials")
                .build();
        final Request request = new Request.Builder()
                .url("https://api.twitter.com/oauth2/token")
                .headers(headers)
                .post(body)
                .build();
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                e.printStackTrace();
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                try {
                    JSONObject responseObject = new JSONObject(response.body().string());
                    mBearerToken = responseObject.getString("access_token");
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    private void startSearch(String query){
        Toast.makeText(this, "Starting Search", Toast.LENGTH_SHORT).show();
        String url;
        url = "https://api.twitter.com/1.1/statuses/user_timeline.json?screen_name=" + query;
        OkHttpClient client = new OkHttpClient();
        Headers headers = new Headers.Builder()
                .add("Authorization", "Bearer " + mBearerToken)
                .build();
        Request request = new Request.Builder()
                .url(url)
                .headers(headers)
                .build();
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                e.printStackTrace();
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                Log.d("MainActivity", "Response Recieved");
                try {
                    mTweetList.clear();
                    JSONArray baseData = new JSONArray(response.body().string());
                    for (int i = 0; i < baseData.length(); i++){
                        JSONObject object = baseData.getJSONObject(i);
                        String tweet = object.toString();

                        Tweet tweetObject = new Gson().fromJson(tweet,Tweet.class);
                        String t = tweetObject.getTweet();
                        String d = tweetObject.getTimeOf();

                        mTweetList.add(new Tweet(t,d));
                        MainActivity.this.runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                mRecyclerView.getAdapter().notifyDataSetChanged();
                            }
                        });
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }

}
