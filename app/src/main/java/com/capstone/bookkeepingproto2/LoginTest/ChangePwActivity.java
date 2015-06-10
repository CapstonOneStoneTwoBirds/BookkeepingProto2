package com.capstone.bookkeepingproto2.LoginTest;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
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
 * Created by New on 2015-06-06.
 */
public class ChangePwActivity extends Activity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.change_pw);

        final EditText pw1 = (EditText)findViewById(R.id.changepw_pw1_et);
        final EditText pw2 = (EditText)findViewById(R.id.changepw_pw2_et);
        Button btn = (Button)findViewById(R.id.changepw_btn);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if( pw1.getText().toString() != pw2.getText().toString() ){
                    String email = getIntent().getStringExtra("email");
                    RequestParams param = new RequestParams();
                    param.put("email", email);
                    param.put("pw", pw1.getText().toString());

                    HttpClient.post("changePw/", param, new AsyncHttpResponseHandler() {
                        @Override
                        public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                            Toast toastView = Toast.makeText(getApplicationContext(),
                                    "Thanks a lot", Toast.LENGTH_LONG);
                            toastView.setGravity(Gravity.CENTER, 40, 25);
                            toastView.show();
                            Log.i("ChangePwActivity | chgangePw Post", "onSuccess");
                            startActivity(new Intent(getApplicationContext(), SelectActivity.class));
                        }

                        @Override
                        public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                            Log.i("ChangePwActivity | chgangePw Post", "onFailure");
                        }
                    });
                }
                else{
                    Toast toastView = Toast.makeText(getApplicationContext(),
                            "Please the same inputs", Toast.LENGTH_LONG);
                    toastView.setGravity(Gravity.CENTER, 40, 25);
                    toastView.show();
                }
            }
        });


    }
}
