package com.capstone.bookkeepingproto2.GroupHouseKeeping;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.capstone.bookkeepingproto2.HttpClient.HttpClient;
import com.capstone.bookkeepingproto2.R;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.apache.http.Header;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by New on 2015-05-11.
 */
public class GroupActivity extends Activity implements View.OnClickListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.group_main);

        Button newgroup = (Button)findViewById(R.id.newgroup_btn);
        newgroup.setOnClickListener(this);

        // Get GroupLists
        try {
            SharedPreferences mPreference;
            mPreference = getSharedPreferences("myInfo", MODE_PRIVATE);

            RequestParams param = new RequestParams();
            param.put("owner", mPreference.getString("email", ""));
            HttpClient.post("getGroupList/", param, new AsyncHttpResponseHandler() {
                @Override
                public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                    try {
                        JSONArray jsonarr = new JSONArray(new String(responseBody));

                        System.out.println("json result : " + jsonarr);
                    } catch (JSONException e) {
                        System.out.println(e);
                    }

                }

                @Override
                public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                    System.out.println("Failure Here kl?");
                }
            });

        }catch(Exception e){}




    }

    @Override
    public void onClick(View v) {
        if(v.getId() == R.id.newgroup_btn){
            Intent intent = new Intent(this, CreateGroupActivity.class);
            startActivity(intent);

        }
    }
}
