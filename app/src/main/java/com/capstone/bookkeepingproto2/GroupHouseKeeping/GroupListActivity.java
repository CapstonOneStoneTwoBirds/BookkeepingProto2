package com.capstone.bookkeepingproto2.GroupHouseKeeping;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.capstone.bookkeepingproto2.HttpClient.HttpClient;
import com.capstone.bookkeepingproto2.R;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.apache.http.Header;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * YoungHoonKim
 * Created by New on 2015-05-11.
 */
public class GroupListActivity extends Activity implements View.OnClickListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.group_list);

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

                        final TextView tv1 = (TextView)findViewById(R.id.group1_tv);
                        final TextView tv2 = (TextView)findViewById(R.id.group2_tv);
                        final TextView tv3 = (TextView)findViewById(R.id.group3_tv);
                        final TextView tv4 = (TextView)findViewById(R.id.group4_tv);

                        ArrayList<TextView> tvlist = new ArrayList();
                        tvlist.add(tv1);
                        tvlist.add(tv2);
                        tvlist.add(tv3);
                        tvlist.add(tv4);


                        if( jsonarr.length() != 0 ){
                            //System.out.println(jsonarr.get(0));
                            //JSONObject got= new JSONObject(jsonarr.get(0).toString());

                            for (int i=0;i < jsonarr.length();i++) {
                                System.out.println(jsonarr.get(i));
                                final JSONObject got = new JSONObject(jsonarr.get(i).toString());
                                System.out.println("json check : " + i);
                                tvlist.get(i).setText(got.get("groupname").toString());

                                tvlist.get(i).setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        Intent intent = new Intent(getApplicationContext(), GroupMainActivity.class);
                                        try {
                                            intent.putExtra("_id", got.get("_id").toString());
                                        }catch( JSONException e){ }
                                        startActivity(intent);
                                    }
                                });
                            }
                        }
                    } catch (JSONException e) { System.out.println(e); }
                }

                @Override
                public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                    System.out.println("Failure Here ?");
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
