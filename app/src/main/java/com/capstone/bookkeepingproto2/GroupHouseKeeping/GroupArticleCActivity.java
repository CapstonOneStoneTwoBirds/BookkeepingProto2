package com.capstone.bookkeepingproto2.GroupHouseKeeping;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import org.bson.types.ObjectId;

import com.capstone.bookkeepingproto2.HttpClient.HttpClient;
import com.capstone.bookkeepingproto2.R;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.apache.http.Header;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

/**
 * Created by New on 2015-05-26.
 */
public class GroupArticleCActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.group_article);

        final TextView title = (TextView)findViewById(R.id.article_title_tv);
        final TextView content = (TextView)findViewById(R.id.article_content_tv);
        final TextView price = (TextView)findViewById(R.id.article_price_tv);
        final EditText comment = (EditText)findViewById(R.id.comment_content);
        final Button write = (Button)findViewById(R.id.write_comment_btn);

        final String jsonobj = getIntent().getStringExtra("jsonobject");
        if(jsonobj == null ){

            RequestParams param = new RequestParams();
                param.add("article_id", getIntent().getStringExtra("article_id").toString());
                HttpClient.post("getArticle/", param, new AsyncHttpResponseHandler() {
                    @Override
                    public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                    try {
                        if (responseBody != null) {
                            final JSONObject obj = new JSONObject(new String(responseBody));
                            title.setText(obj.get("title").toString());
                            content.setText(obj.get("content").toString());
                            price.setText(obj.get("price").toString());

                            write.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    Intent intent = new Intent(getApplicationContext(), GroupCommentWriteActivity.class);
                                    intent.putExtra("content", comment.getText().toString());
                                    System.out.println(comment.getText());
                                    try {
                                        intent.putExtra("article_id", obj.get("_id").toString());
                                    } catch (Exception e) { }
                                    startActivity(intent);
                                }
                            });
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

            HttpClient.post("getCommentList/", param, new AsyncHttpResponseHandler() {
                @Override
                public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                    try {
                        if (responseBody != null) {
                            JSONArray comments = new JSONArray(new String(responseBody));

                            ArrayList<String> arrListInsert = new ArrayList();
                            ArrayAdapter<String> adapterInsert = new ArrayAdapter(getApplicationContext(), R.layout.calendertext, arrListInsert);
                            ListView listViewInsert = (ListView) findViewById(R.id.comment_lv);

                            for (int i = 0; i < comments.length(); i++) {
                                JSONObject got = new JSONObject(comments.get(i).toString());
                                arrListInsert.add(got.get("writer").toString() + " / " + got.get("content").toString());
                                ObjectId obj = new ObjectId(got.get("_id").toString());
                                Date date = obj.getDate();
                                SimpleDateFormat ft = new SimpleDateFormat("E yyyy.MM.dd 'at' hh:mm:ss a zzz");
                                System.out.println("Current Date: " + ft.format(date));

                                /*
                                Date dNow = new Date( );
                                SimpleDateFormat ft =
                                        new SimpleDateFormat ("E yyyy.MM.dd 'at' hh:mm:ss a zzz");

                                System.out.println("Current Date: " + ft.format(dNow));
                                */
                            }
                            listViewInsert.setAdapter(adapterInsert);
                            System.out.println("Comments : " + comments);
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
            try {
                final JSONObject obj = new JSONObject(jsonobj);
                title.setText(obj.get("title").toString());
                content.setText(obj.get("content").toString());
                price.setText(obj.get("price").toString());
                System.out.println("json obj ::::::::::: " + jsonobj);

                RequestParams param = new RequestParams();
                param.add("article_id", obj.get("_id").toString());
                HttpClient.post("getCommentList/", param, new AsyncHttpResponseHandler() {
                    @Override
                    public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                        try {
                            if (responseBody != null) {
                                JSONArray comments = new JSONArray(new String(responseBody));

                                ArrayList<String> arrListInsert = new ArrayList();
                                ArrayAdapter<String> adapterInsert = new ArrayAdapter(getApplicationContext(), R.layout.calendertext, arrListInsert);
                                ListView listViewInsert = (ListView) findViewById(R.id.comment_lv);

                                for (int i = 0; i < comments.length(); i++) {
                                    JSONObject got = new JSONObject(comments.get(i).toString());
                                    arrListInsert.add(got.get("writer").toString() + " / " + got.get("content").toString());
                                }
                                listViewInsert.setAdapter(adapterInsert);
                                System.out.println("Comments : " + comments);
                            } else {
                                System.out.println("Here Checker");
                            }
                        } catch (JSONException e) {
                            System.out.println(e);
                        }
                        write.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                Intent intent = new Intent(getApplicationContext(), GroupCommentWriteActivity.class);
                                intent.putExtra("content", comment.getText().toString());
                                System.out.println(comment.getText());
                                try {
                                    intent.putExtra("article_id", obj.get("_id").toString());
                                } catch (Exception e) { }
                                startActivity(intent);
                            }
                        });
                    }

                    @Override
                    public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                        System.out.println("error message : " + error);
                    }
                });
            } catch (Exception e) {
            }
        }
    }
}

