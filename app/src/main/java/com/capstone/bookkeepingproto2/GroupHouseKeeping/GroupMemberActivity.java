package com.capstone.bookkeepingproto2.GroupHouseKeeping;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;

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
 * Created by YeomJi on 15. 5. 14..
 */
public class GroupMemberActivity extends ActionBarActivity {

    String _id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.group_member_list);

        _id = getIntent().getStringExtra("groupid");

        Button addmember = (Button)findViewById(R.id.newmember_btn);
        addmember.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), WriteMemberActivity.class);
                intent.putExtra("groupid",_id);
                startActivity(intent);
            }
        });// proto
        RequestParams param = new RequestParams();
        param.add("groupid", _id);

        HttpClient.post("getMemberList/", param, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                try {
                    final JSONArray member = new JSONArray(new String(responseBody));

                    ArrayList<String> arrListInsert = new ArrayList();
                    ArrayAdapter<String> adapterInsert = new ArrayAdapter(getApplicationContext(), R.layout.calendertext, arrListInsert);
                    ListView listViewInsert = (ListView) findViewById(R.id.group_member_list);
                    //arrListInsert.add(result);
                    for (int i = 0; i < member.length(); i++) {
                        JSONObject got = new JSONObject(member.get(i).toString());
                        arrListInsert.add(got.get("ownership").toString() + " / " + got.get("name").toString());
                    }
                    listViewInsert.setAdapter(adapterInsert);

                    listViewInsert.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                        @Override
                        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                            try {
                                JSONObject obj = new JSONObject(member.get(position).toString());

                                Intent intent = new Intent(getApplicationContext(), GroupMemberInfoActivity.class);
                                intent.putExtra("jsonobject", obj.toString());
                                startActivity(intent);
                                    /*
                                    Toast toastView = Toast.makeText(getApplicationContext(),
                                            obj.get("title").toString(), Toast.LENGTH_LONG);
                                    toastView.setGravity(Gravity.CENTER, 40, 25);
                                    toastView.show();
                                    */
                            } catch (JSONException e) {
                            }
                        }
                    });

                    System.out.println("getMemberList Success 1");
                    System.out.println("members : " + member);
                    switch (new String(responseBody)) {
                        case "1":
                            System.out.println("getMemberList error");
                            break;

                        case "2":
                            System.out.println("getMemberList Success 2");
                            break;
                    }
                } catch (Exception e) {
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                System.out.println("error message 1 : " + error);
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        menu.add(0, 0, 0, "Group Article");
        menu.add(0, 1, 0, "Group Announce");
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        Intent intent;
        switch (item.getItemId()) {
            case 0:
                //처리할 이벤트
                intent = new Intent(getApplicationContext(), GroupArticleActivity.class);
                intent.putExtra("groupid", _id);
                startActivity(intent);
                finish();
                break;
            case 1:
                intent = new Intent(getApplicationContext(), GroupAnnounceActivity.class);
                intent.putExtra("groupid", _id);
                startActivity(intent);
                finish();
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
