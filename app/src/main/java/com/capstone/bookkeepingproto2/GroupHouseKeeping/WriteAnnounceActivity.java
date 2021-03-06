package com.capstone.bookkeepingproto2.GroupHouseKeeping;


import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.capstone.bookkeepingproto2.HttpClient.HttpClient;
import com.capstone.bookkeepingproto2.R;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.apache.http.Header;

/**
 * Created by New on 2015-05-14.
 */
public class WriteAnnounceActivity extends Activity{

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.group_announce_write);



        Button confirm = (Button)findViewById(R.id.announce_write_btn);
        confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EditText titleedt = (EditText)findViewById(R.id.announce_title_edt);
                EditText contentedt = (EditText)findViewById(R.id.announce_content_edt);

                System.out.println("title 1: " + titleedt.getText().toString());

                RequestParams param = new RequestParams();
                param.put("groupid", getIntent().getStringExtra("groupid"));
                param.put("title", titleedt.getText().toString());
                param.put("content", contentedt.getText().toString());

                //System.out.println("title 2: " + title);
                HttpClient.post("writeAnnounce/", param, new AsyncHttpResponseHandler() {
                    @Override
                    public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                        System.out.println("Announce Save Success");
                        System.out.println("output : " + new String(responseBody));
                        switch (new String(responseBody)) {
                            case "1":
                                System.out.println("write announce error");
                                break;

                            case "2":
                                System.out.println("write announce Success");

                                Intent intent = new Intent(getApplicationContext(), GroupAnnounceActivity.class);
                                intent.putExtra("groupid", getIntent().getStringExtra("groupid"));
                                startActivity(intent);
                                finish();

                                break;
                        }
                    }

                    @Override
                    public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                        System.out.println("error message 1 : " + error);
                    }
                });
            }
        });
    }
}
