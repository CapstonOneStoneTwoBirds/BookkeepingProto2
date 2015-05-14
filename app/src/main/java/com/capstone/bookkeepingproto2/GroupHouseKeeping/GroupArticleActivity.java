package com.capstone.bookkeepingproto2.GroupHouseKeeping;

import android.app.Activity;
import android.app.DownloadManager;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
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
public class GroupArticleActivity extends ActionBarActivity implements View.OnClickListener{
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.group_article_list);

        final String _id = getIntent().getStringExtra("_id");
        System.out.println("_id : " + _id);

        Button newarticle = (Button)findViewById(R.id.newarticle_btn);
        Button newannounce = (Button)findViewById(R.id.newannounce_btn);
        Button newmember = (Button)findViewById(R.id.newmember_btn);
        //Button articlelist = (Button)findViewById(R.id.articlelist_btn);
        //Button announcelist = (Button)findViewById(R.id.announcelist_btn);
        //Button memberlist = (Button)findViewById(R.id.memberlist_btn);
        newarticle.setOnClickListener(this);
        newannounce.setOnClickListener(this);
        newmember.setOnClickListener(this);
        //articlelist.setOnClickListener(this);
        //announcelist.setOnClickListener(this);
        //memberlist.setOnClickListener(this);

        RequestParams param = new RequestParams();
        param.put("groupid", _id);

        HttpClient.post("getArticleList/", param, new AsyncHttpResponseHandler() {
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

    @Override
    public void onClick(View v) {
        String _id = getIntent().getStringExtra("_id");
        Intent intent;
        switch( v.getId() ){
            case R.id.newarticle_btn:
                intent = new Intent(getApplicationContext(), WriteArticleActivity.class);
                intent.putExtra("_id", _id);
                startActivity(intent);
                break;

            case R.id.newannounce_btn:
                intent = new Intent(getApplicationContext(), WriteAnnounceActivity.class);
                intent.putExtra("_id", _id);
                startActivity(intent);
                break;

            case R.id.newmember_btn:
                intent = new Intent(getApplicationContext(), WriteMemberActivity.class);
                intent.putExtra("_id", _id);
                startActivity(intent);
                break;
/*
            case R.id.articlelist_btn:

                break;

            case R.id.announcelist_btn:

                break;

            case R.id.memberlist_btn:
                intent = new Intent(getApplicationContext(), GetMemberListActivity.class);
                intent.putExtra("_id", _id);
                startActivity(intent);
                break;
*/
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        menu.add(0, 0, 0, "Group Announce");
        menu.add(0, 1, 0, "Group Member");
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        Intent intent;
        switch (item.getItemId()) {
            case 0:
                //처리할 이벤트
                intent = new Intent(getApplicationContext(), GroupAnnounceActivity.class);
                startActivity(intent);
                break;
            case 1:
                intent = new Intent(getApplicationContext(), GroupMemberActivity.class);
                startActivity(intent);
                break;
            default:
                break;
        }
        return super.onOptionsItemSelected(item);
    }


    @Override
    public void onBackPressed() {
        finish();
    }
}
