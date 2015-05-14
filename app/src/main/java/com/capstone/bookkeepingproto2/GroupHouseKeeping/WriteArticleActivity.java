package com.capstone.bookkeepingproto2.GroupHouseKeeping;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
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
 * Created by New on 2015-05-13.
 */
public class WriteArticleActivity extends Activity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.group_article_write);

        Button confirm = (Button)findViewById(R.id.confirm_write_btn);
        confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EditText title = (EditText)findViewById(R.id.grouptitle_edt);
                EditText content = (EditText)findViewById(R.id.groupcontent_edt);
                EditText price = (EditText)findViewById(R.id.groupprice_edt);

                SharedPreferences mPreference = getSharedPreferences("myInfo", MODE_PRIVATE);

                RequestParams param = new RequestParams();
                param.put("groupid", getIntent().getStringExtra("groupid"));
                param.put("writer", mPreference.getString("email",""));
                param.put("title", title.getText().toString());
                param.put("content", content.getText().toString());
                param.put("price", price.getText().toString());


                HttpClient.post("writeArticle/", param, new AsyncHttpResponseHandler() {
                    @Override
                    public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                        System.out.println("Article Save Success");
                        System.out.println("output : " + new String(responseBody));
                        switch ( new String(responseBody) ) {
                            case "1":
                                System.out.println("write article error");
                                break;

                            case "2":
                                System.out.println("write article Success");

                                Intent intent = new Intent(getApplicationContext(), GroupArticleActivity.class);
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
