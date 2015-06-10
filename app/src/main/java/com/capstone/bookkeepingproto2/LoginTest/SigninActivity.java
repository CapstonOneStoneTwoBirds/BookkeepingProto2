package com.capstone.bookkeepingproto2.LoginTest;

import android.app.Activity;
import android.app.DownloadManager;
import android.content.Intent;
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
import org.apache.http.entity.StringEntity;
import org.apache.http.message.BasicHeader;
import org.apache.http.protocol.HTTP;
import org.json.JSONObject;

/**
 * Created by New on 2015-04-24.
 */
public class SigninActivity extends Activity{

    EditText email, pw1, pw2, name, phone;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.sign_in);

        email = (EditText)findViewById(R.id.email_edt);
        pw1 = (EditText)findViewById(R.id.pw_edt1);
        pw2 = (EditText)findViewById(R.id.pw_edt2);
        name = (EditText)findViewById(R.id.name_edt);
        phone = (EditText)findViewById(R.id.phone_edt);

        Button process = (Button)findViewById(R.id.pro_btn);
        process.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                try {
                    //toast test
                    if(!pw1.getText().toString().equals(pw2.getText().toString()))
                    {
                        Toast toastView = Toast.makeText(getApplicationContext(),
                                "passwords are different", Toast.LENGTH_LONG);
                        toastView.setGravity(Gravity.CENTER, 40, 25);
                        toastView.show();
                    }
                    else {
                        RequestParams param = new RequestParams();
                        param.put("email", email.getText().toString());
                        param.put("pw", pw1.getText().toString());
                        param.put("name", name.getText().toString());
                        param.put("phone", phone.getText().toString());

                        HttpClient.post("signin/", param, new AsyncHttpResponseHandler() {
                            @Override
                            public void onSuccess(int i, Header[] headers, byte[] bytes) {
                                System.out.println("onSuccess called.");
                                String code = new String(bytes);
                                switch (code) {
                                    // failed
                                    case "1":
                                        System.out.println("1 called");
                                        Toast toastView = Toast.makeText(getApplicationContext(),
                                                "Existed Email", Toast.LENGTH_LONG);
                                        toastView.setGravity(Gravity.CENTER, 40, 25);
                                        toastView.show();

                                        //toast로 바꾸자.
                                        //TextView status = (TextView)findViewById(R.id.status_textView);
                                        //status.setText("Existed Email");
                                        break;
                                    // success
                                    case "2":
                                        System.out.println("2 called");
                                        Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                                        startActivity(intent);
                                }
                            }

                            @Override
                            public void onFailure(int i, Header[] headers, byte[] bytes, Throwable throwable) {
                                System.out.println("onFailure called.");
                            }
                        });
                    }
                }catch(Exception e){}
            }
        });
    }
}
