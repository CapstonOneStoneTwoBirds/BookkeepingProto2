package com.capstone.bookkeepingproto2.LoginTest;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.capstone.bookkeepingproto2.HttpClient.HttpClient;
import com.capstone.bookkeepingproto2.PrivateHouseKeeping.ConfirmTest.ConfirmActivity;
import com.capstone.bookkeepingproto2.R;
import com.loopj.android.http.AsyncHttpResponseHandler;

import org.apache.http.Header;
import org.apache.http.entity.StringEntity;
import org.apache.http.message.BasicHeader;
import org.apache.http.protocol.HTTP;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;


public class MainActivity extends FragmentActivity implements View.OnClickListener{

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        SharedPreferences mPreference;
        mPreference = getSharedPreferences("myInfo", MODE_PRIVATE);

        if( mPreference.getString("email", "") == "" ) {
            setContentView(R.layout.activity_main);

            Button login = (Button) findViewById(R.id.login_btn);
            login.setOnClickListener(this);
            Button pass = (Button) findViewById(R.id.pass_btn);
            pass.setOnClickListener(this);
            TextView sign_in = (TextView) findViewById(R.id.sign_btn);
            sign_in.setOnClickListener(this);
        }
        else {

            if (mPreference.getString("email", "") != "") {
                // JSONObject
                JSONObject login_info = new JSONObject();
                try {
                    login_info.put("email", mPreference.getString("email", ""));
                    login_info.put("pw", mPreference.getString("pw", ""));
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                // Post Request
                try {
                    StringEntity entity = new StringEntity(login_info.toString());
                    entity.setContentType(new BasicHeader(HTTP.CONTENT_TYPE, "application/json"));

                    HttpClient.post(this, "login/", entity, new AsyncHttpResponseHandler() {
                        @Override
                        public void onSuccess(int i, Header[] headers, byte[] bytes) {
                            System.out.println("Success test here");
                            Intent intent = new Intent(getApplicationContext(), SelectActivity.class);
                            startActivity(intent);
                        }

                        @Override
                        public void onFailure(int i, Header[] headers, byte[] bytes, Throwable throwable) {
                            System.out.println("Fail here");
                        }
                    });
                } catch (UnsupportedEncodingException e) {
                }
            }
        }
    }

    @Override
    public void onClick(View v) {
        if( v.getId() == R.id.sign_btn ){
            Intent intent = new Intent(getApplicationContext(), SigninActivity.class);
            startActivity(intent);
        }
        else if( v.getId() == R.id.pass_btn){
            Intent intent = new Intent(getApplicationContext(), SelectActivity.class);
            startActivity(intent);
        }
        else {

            EditText email = (EditText) findViewById(R.id.email_login_edt);
            EditText pw = (EditText) findViewById(R.id.pw_login_edt);

            SharedPreferences mPreference = getSharedPreferences("myInfo", MODE_PRIVATE);
            SharedPreferences.Editor editor = mPreference.edit();
            editor.putString("email", email.getText().toString());
            editor.putString("pw", pw.getText().toString());
            editor.commit();

            JSONObject login_info = new JSONObject();
            try {
                login_info.put("email", email.getText().toString());
                login_info.put("pw", pw.getText().toString());
            } catch (JSONException e) {
                e.printStackTrace();
            }
            //Post Transport Success!!!!!!!!!!!!!!!!!!

            System.out.println(login_info);
            try {
                StringEntity entity = new StringEntity(login_info.toString());
                entity.setContentType(new BasicHeader(HTTP.CONTENT_TYPE, "application/json"));

                HttpClient.post(this, "login/", entity, new AsyncHttpResponseHandler() {
                    @Override
                    public void onSuccess(int i, Header[] headers, byte[] bytes) {

                        System.out.println("Success test here");
                        if (new String(bytes).equals("1")) {
                            Intent intent = new Intent(getApplicationContext(), SelectActivity.class);
                            startActivity(intent);
                        } else {
                            // toast로 아이디or패스워드 틀림 알리기
                            Toast toastView = Toast.makeText(getApplicationContext(),
                                    new String(bytes), Toast.LENGTH_LONG);
                            toastView.setGravity(Gravity.CENTER, 40, 25);
                            toastView.show();
                        }
                    }

                    @Override
                    public void onFailure(int i, Header[] headers, byte[] bytes, Throwable throwable) {
                        System.out.println("message : " + throwable.getMessage());
                        System.out.println("message : " + throwable.getCause());

                        System.out.println("Fail here");
                    }
                });
            } catch (UnsupportedEncodingException e) {
            }
        }
    }
}