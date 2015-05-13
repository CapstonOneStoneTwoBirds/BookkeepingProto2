package com.capstone.bookkeepingproto2.GroupHouseKeeping;

import android.app.Activity;
import android.app.DownloadManager;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.capstone.bookkeepingproto2.HttpClient.HttpClient;
import com.capstone.bookkeepingproto2.R;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.apache.http.Header;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * YoungHoonKim
 * Created by New on 2015-05-13.
 */
public class GroupMainActivity extends Activity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.group_article_list);

        final String _id = getIntent().getStringExtra("_id");
        System.out.println("_id : " + _id);

        Button btn = (Button)findViewById(R.id.newarticle_btn);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), WriteArticleActivity.class);
                intent.putExtra("_id", _id);
                startActivity(intent);
            }
        });

        RequestParams param = new RequestParams();
        param.put("_id", _id);

        HttpClient.post("getGroupArticle/", param, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                try {
                    if(responseBody != null){
                        JSONArray articles = new JSONArray(new String(responseBody));
                        System.out.println("Articles : " + articles);
                    }
                    else{
                        System.out.println("Here Checker");
                    }

                } catch (JSONException e) {
                    System.out.println(e);
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                System.out.println("error message : " + error);
            }
        });




    }
}
