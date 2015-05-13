package com.capstone.bookkeepingproto2.GroupHouseKeeping;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.capstone.bookkeepingproto2.HttpClient.HttpClient;
import com.capstone.bookkeepingproto2.R;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.apache.http.Header;

/**
 * YoungHoonKim
 * Created by New on 2015-05-12.
 */
public class CreateGroupActivity extends Activity{
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.group_newgroup);

        Button create = (Button)findViewById(R.id.create_confirm_btn);
        final EditText groupname = (EditText)findViewById(R.id.groupname_edt);
        create.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    SharedPreferences mPreference;
                    mPreference = getSharedPreferences("myInfo", MODE_PRIVATE);

                    RequestParams param = new RequestParams();
                    param.put("groupname", groupname.getText().toString());
                    param.put("owner", mPreference.getString("email", ""));
                    HttpClient.get("createGroup/", param, new AsyncHttpResponseHandler() {
                        @Override
                        public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                            String code = new String(responseBody);
                            System.out.println("code : " + code);
                            switch(code){
                                case "1":
                                // Already Exist
                                Toast toastView = Toast.makeText(getApplicationContext(),
                                        "Already you have the same group.", Toast.LENGTH_LONG);
                                toastView.setGravity(Gravity.CENTER, 40, 25);
                                toastView.show();

                                System.out.println("Success Here   1");
                                break;

                                case "2":
                                // Create Success
                                System.out.println("Success Here   2");

                                Intent intent = new Intent(getApplicationContext(), GroupListActivity.class);
                                startActivity(intent);
                                break;

                                case "4":
                                // Save Error
                                System.out.println("Success Here    4");
                                break;
                            }
                        }

                        @Override
                        public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                            System.out.println("Failure Here kl");
                        }
                    });

                }catch(Exception e){}
            }
        });

    }

}
