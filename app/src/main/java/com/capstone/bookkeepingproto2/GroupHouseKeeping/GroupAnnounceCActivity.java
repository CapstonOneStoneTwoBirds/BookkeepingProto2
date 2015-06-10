package com.capstone.bookkeepingproto2.GroupHouseKeeping;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.capstone.bookkeepingproto2.HttpClient.HttpClient;
import com.capstone.bookkeepingproto2.R;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.apache.http.Header;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by New on 2015-06-06.
 */
public class GroupAnnounceCActivity extends Activity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.group_announce);

        final TextView title = (TextView)findViewById(R.id.announce_title_tv);
        final TextView content = (TextView)findViewById(R.id.announce_content_tv);
        String jsonobj = getIntent().getStringExtra("jsonobject");
        if( jsonobj == null ){
            RequestParams param = new RequestParams();
            param.add("announce_id", getIntent().getStringExtra("announce_id").toString());
            HttpClient.post("getAnnounce/", param, new AsyncHttpResponseHandler() {
                @Override
                public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                    try {
                        if (responseBody != null) {
                            final JSONObject obj = new JSONObject(new String(responseBody));
                            title.setText(obj.get("title").toString());
                            content.setText(obj.get("content").toString());

                        } else {
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
        else {
            Log.e("GroupAnnounceCActivity | ", jsonobj);
            try {
                final JSONObject obj = new JSONObject(jsonobj);
                title.setText(obj.get("title").toString());
                content.setText(obj.get("content").toString());
                System.out.println("json obj ::::::::::: " + jsonobj);
            } catch (Exception e) {
            }
        }
    }
}
