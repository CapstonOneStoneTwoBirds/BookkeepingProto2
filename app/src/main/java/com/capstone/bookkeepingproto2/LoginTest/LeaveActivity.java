package com.capstone.bookkeepingproto2.LoginTest;

import android.app.Activity;
import android.app.DownloadManager;
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
 * Created by New on 2015-06-03.
 */
public class LeaveActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.leave_member);
        final EditText pw = (EditText)findViewById(R.id.leave_pwd_et);
        final Button leave = (Button)findViewById(R.id.leave_btn);

        SharedPreferences mPreference = getSharedPreferences("myInfo", MODE_PRIVATE);
        final String email = mPreference.getString("email", "");

        leave.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                String pwd = pw.getText().toString();

                final RequestParams param = new RequestParams();
                param.put("email", email);
                param.put("pw", pwd);

                HttpClient.post("leaveMember/", param, new AsyncHttpResponseHandler() {
                    @Override
                    public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                        System.out.println("responseBody : " + new String(responseBody));
                        if(new String(responseBody).equals("wrong password")){
                            System.out.println("here");
                            Toast toastView = Toast.makeText(getApplicationContext(),
                                    "Wrong Password", Toast.LENGTH_SHORT);
                            toastView.setGravity(Gravity.CENTER, 40, 25);
                            toastView.show();
                        }
                        else if(new String(responseBody).equals("leave Success")){
                            Toast toastView = Toast.makeText(getApplicationContext(),
                                    "Leave Member Success", Toast.LENGTH_SHORT);
                            toastView.setGravity(Gravity.CENTER, 40, 25);
                            toastView.show();

                            // 자동로그인 해제
                            SharedPreferences mPreference = getSharedPreferences("myInfo", MODE_PRIVATE);
                            SharedPreferences.Editor editor = mPreference.edit();
                            editor.clear();
                            editor.commit();

                            startActivity(new Intent(getApplicationContext(), MainActivity.class));
                        }
                    }

                    @Override
                    public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {

                    }
                });
            }
        });

    }
}
