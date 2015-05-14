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
public class WriteMemberActivity extends Activity{
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.group_member_write);

        Button btn = (Button)findViewById(R.id.member_invite_btn);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EditText emailedt = (EditText)findViewById(R.id.member_email_edt);
                String email = emailedt.getText().toString();

                final String _id = getIntent().getStringExtra("groupid");
                RequestParams param = new RequestParams();
                param.put("groupid", _id);
                param.put("email", email);

                HttpClient.post("writeMember/", param, new AsyncHttpResponseHandler() {
                    @Override
                    public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                        System.out.println("Member Save Success");
                        System.out.println("output : " + new String(responseBody));
                        switch (new String(responseBody)) {
                            case "1":
                                System.out.println("write member error");
                                break;

                            case "2":
                                System.out.println("write member Success");

                                Intent intent = new Intent(getApplicationContext(), GroupArticleActivity.class);
                                intent.putExtra("groupid", _id);
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
