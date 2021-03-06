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
 * YoungHoonKim
 * Created by New on 2015-05-13.
 */
public class GroupArticleActivity extends ActionBarActivity {
    String _id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.group_article_list);

        _id = getIntent().getStringExtra("groupid");
        System.out.println("groupid : " + _id);

        Button newarticle = (Button)findViewById(R.id.newarticle_btn);
        newarticle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), WriteArticleActivity.class);
                intent.putExtra("groupid", _id);
                startActivity(intent);
            }
        });


        RequestParams param = new RequestParams();
        param.put("groupid", _id);

        HttpClient.post("getArticleList/", param, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                try {
                    if(responseBody != null){
                        final JSONArray articles = new JSONArray(new String(responseBody));

                        ArrayList<String> arrListInsert = new ArrayList();
                        ArrayAdapter<String> adapterInsert = new ArrayAdapter(getApplicationContext(), R.layout.calendertext, arrListInsert);
                        ListView listViewInsert = (ListView)findViewById(R.id.group_article_list);
                        //arrListInsert.add(result);
                        for (int i=0;i < articles.length();i++) {
                            JSONObject got = new JSONObject(articles.get(i).toString());
                            arrListInsert.add(got.get("title").toString()+" / "+got.get("price").toString()+" / "+got.get("content").toString());
                        }
                        listViewInsert.setAdapter(adapterInsert);

                        listViewInsert.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                            @Override
                            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                                try {
                                    JSONObject obj = new JSONObject(articles.get(position).toString());

                                    Intent intent = new Intent(getApplicationContext(), GroupArticleCActivity.class );
                                    intent.putExtra("jsonobject", obj.toString());
                                    startActivity(intent);
                                    /*
                                    Toast toastView = Toast.makeText(getApplicationContext(),
                                            obj.get("title").toString(), Toast.LENGTH_LONG);
                                    toastView.setGravity(Gravity.CENTER, 40, 25);
                                    toastView.show();
                                    */
                                }catch(JSONException e){}
                            }
                        });

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
                intent.putExtra("groupid", _id);
                startActivity(intent);
                finish();
                break;
            case 1:
                intent = new Intent(getApplicationContext(), GroupMemberActivity.class);
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
