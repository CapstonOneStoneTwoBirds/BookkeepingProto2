package com.capstone.bookkeepingproto2.GroupHouseKeeping;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import com.capstone.bookkeepingproto2.HttpClient.HttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.apache.http.Header;

/**
 * Created by New on 2015-05-14.
 */
public class GetMemberListActivity extends Activity{
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        String groupid = getIntent().getStringExtra("_id");

        RequestParams param = new RequestParams();
        param.add("groupid", groupid);

        HttpClient.post("getMemberList/", param, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                System.out.println("getMemberList Success 1");
                System.out.println("output : " + new String(responseBody));
                switch (new String(responseBody)) {
                    case "1":
                        System.out.println("getMemberList error");
                        break;

                    case "2":
                        System.out.println("getMemberList Success 2");

                        Intent intent = new Intent(getApplicationContext(), GroupMainActivity.class);
                        intent.putExtra("_id", getIntent().getStringExtra("_id"));
                        startActivity(intent);

                        break;
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                System.out.println("error message 1 : " + error);
            }
        });
    }
}
