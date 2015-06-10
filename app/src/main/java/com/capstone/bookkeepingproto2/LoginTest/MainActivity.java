package com.capstone.bookkeepingproto2.LoginTest;

import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.Preference;
import android.support.v4.app.FragmentActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.capstone.bookkeepingproto2.Gcm.PreferenceUtil;
import com.capstone.bookkeepingproto2.HttpClient.HttpClient;
import com.capstone.bookkeepingproto2.PrivateHouseKeeping.ConfirmTest.ConfirmActivity;
import com.capstone.bookkeepingproto2.R;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.gcm.GoogleCloudMessaging;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.apache.http.Header;
import org.apache.http.entity.StringEntity;
import org.apache.http.message.BasicHeader;
import org.apache.http.protocol.HTTP;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.UnsupportedEncodingException;


public class MainActivity extends FragmentActivity implements View.OnClickListener{

    private final static int PLAY_SERVICES_RESOLUTION_REQUEST = 9000;
    private static final String SENDER_ID = "394987658992";

    private GoogleCloudMessaging _gcm;
    private String _regId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        SharedPreferences mPreference;
        mPreference = getSharedPreferences("myInfo", MODE_PRIVATE);

        if( mPreference.getString("email", "") == "" ) {
            setContentView(R.layout.activity_main);

            Button login = (Button) findViewById(R.id.login_btn);
            login.setOnClickListener(this);
            TextView sign_in = (TextView) findViewById(R.id.sign_btn);
            TextView findpw = (TextView)findViewById(R.id.labForgetPassword);
            findpw.setOnClickListener(this);
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
                            if( new String(bytes).equals("Wrong access")){
                                SharedPreferences mPreference = getSharedPreferences("myInfo", MODE_PRIVATE);
                                SharedPreferences.Editor editor = mPreference.edit();
                                editor.clear();
                                editor.commit();
                                startActivity(new Intent(getApplicationContext(), MainActivity.class));
                            }
                            else{
                                System.out.println("Auto Login Success");

                                Intent intent = new Intent(getApplicationContext(), SelectActivity.class);
                                startActivity(intent);
                            }
                        }

                        @Override
                        public void onFailure(int i, Header[] headers, byte[] bytes, Throwable throwable) {
                            System.out.println("error : " + throwable.getMessage());
                            System.out.println("Fail here");
                        }
                    });
                } catch (UnsupportedEncodingException e) { }
            }
        }
    }

    @Override
    public void onClick(View v) {
        if( v.getId() == R.id.sign_btn ){
            Intent intent = new Intent(getApplicationContext(), SigninActivity.class);
            startActivity(intent);
        }
        else if( v.getId() == R.id.labForgetPassword){
           startActivity(new Intent(getApplicationContext(), FindPwActivity.class));
        }
        else {

            EditText email = (EditText) findViewById(R.id.email_login_edt);
            EditText pw = (EditText) findViewById(R.id.pw_login_edt);

            final SharedPreferences mPreference = getSharedPreferences("myInfo", MODE_PRIVATE);
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

            System.out.println(login_info);
            try {
                StringEntity entity = new StringEntity(login_info.toString());
                entity.setContentType(new BasicHeader(HTTP.CONTENT_TYPE, "application/json"));

                HttpClient.post(this, "login/", entity, new AsyncHttpResponseHandler() {
                    @Override
                    public void onSuccess(int i, Header[] headers, byte[] bytes) {

                        System.out.println("Login Success");

                        if( new String(bytes).equals("Wrong access")){
                            // toast로 아이디or패스워드 틀림 알리기
                            Toast toastView = Toast.makeText(getApplicationContext(),
                                    new String(bytes), Toast.LENGTH_LONG);
                            toastView.setGravity(Gravity.CENTER, 40, 25);
                            toastView.show();
                        }
                        else{
                            /////////////////////// GCM Start

                            // google play service가 사용가능한가
                            if (checkPlayServices())
                            {
                                _gcm = GoogleCloudMessaging.getInstance(getApplicationContext());
                                _regId = getRegistrationId();
                                Log.e(":::::::::::_regId ", _regId);
                                Log.e("Hererererere", "1111111111111111");

                                RequestParams param = new RequestParams();
                                param.put("add", _regId);
                                param.put("CODE", "addGcm");
                                param.put("email", mPreference.getString("email", ""));
                                HttpClient.post("addGcmAddress/", param, new AsyncHttpResponseHandler() {
                                    @Override
                                    public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                                        Log.i("MainActivity ::::: ", "onSuccess");
                                    }

                                    @Override
                                    public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                                        Log.i("MainActivity ::::: ", error.toString());
                                    }
                                });

                                if (TextUtils.isEmpty(_regId))
                                    registerInBackground();
                            }
                            else
                            {
                                Log.i("MainActivity.java | onCreate", "|No valid Google Play Services APK found.|");
                            }

                            /////////////////////// GCM End
                            try {
                                if (new JSONObject(new String(bytes)).get("ch").equals("1")) {
                                    Intent intent = new Intent(getApplicationContext(), ChangePwActivity.class);
                                    intent.putExtra("email", new JSONObject(new String(bytes)).get("email").toString());
                                    startActivity(intent);
                                } else {
                                    Intent intent = new Intent(getApplicationContext(), SelectActivity.class);
                                    startActivity(intent);
                                }
                            }catch(JSONException e){
                                Log.e("MainActivity ||||", "Here is what I found");
                                Intent intent = new Intent(getApplicationContext(), SelectActivity.class);
                                startActivity(intent);
                            }
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

    @Override
    protected void onNewIntent(Intent intent)
    {
        super.onNewIntent(intent);

        // display received msg
        String msg = intent.getStringExtra("msg");
        Log.i("MainActivity.java | onNewIntent", "|" + msg + "|");
    }

    // google play service가 사용가능한가
    private boolean checkPlayServices()
    {
        int resultCode = GooglePlayServicesUtil.isGooglePlayServicesAvailable(this);
        if (resultCode != ConnectionResult.SUCCESS)
        {
            if (GooglePlayServicesUtil.isUserRecoverableError(resultCode))
            {
                GooglePlayServicesUtil.getErrorDialog(resultCode, this, PLAY_SERVICES_RESOLUTION_REQUEST).show();
            }
            else
            {
                Log.i("MainActivity.java | checkPlayService", "|This device is not supported.|");
                finish();
            }
            return false;
        }
        return true;
    }

    // registration  id를 가져온다.
    private String getRegistrationId()
    {
        String registrationId = PreferenceUtil.instance(getApplicationContext()).regId();
        if (TextUtils.isEmpty(registrationId))
        {
            Log.i("MainActivity.java | getRegistrationId", "|Registration not found.|");
            return "";
        }
        int registeredVersion = PreferenceUtil.instance(getApplicationContext()).appVersion();
        int currentVersion = getAppVersion();
        if (registeredVersion != currentVersion)
        {
            Log.i("MainActivity.java | getRegistrationId", "|App version changed.|");
            return "";
        }
        return registrationId;
    }

    // app version을 가져온다. 뭐에 쓰는건지는 모르겠다.
    private int getAppVersion()
    {
        try
        {
            PackageInfo packageInfo = getPackageManager().getPackageInfo(getPackageName(), 0);
            return packageInfo.versionCode;
        }
        catch (PackageManager.NameNotFoundException e)
        {
            // should never happen
            throw new RuntimeException("Could not get package name: " + e);
        }
    }

    // gcm 서버에 접속해서 registration id를 발급받는다.
    private void registerInBackground()
    {
        new AsyncTask<Void, Void, String>()
        {
            @Override
            protected String doInBackground(Void... params)
            {
                String msg = "";
                try
                {
                    if (_gcm == null)
                    {
                        _gcm = GoogleCloudMessaging.getInstance(getApplicationContext());
                    }

                    _regId = _gcm.register(SENDER_ID);
                    Log.e("RedID : ", _regId);
                    msg = "Device registered, registration ID=" + _regId;

                    // For this demo: we don't need to send it because the device
                    // will send upstream messages to a server that echo back the
                    // message using the 'from' address in the message.

                    // Persist the regID - no need to register again.
                    storeRegistrationId(_regId);
                }
                catch (IOException ex)
                {
                    msg = "Error :" + ex.getMessage();
                    // If there is an error, don't just keep trying to register.
                    // Require the user to click a button again, or perform
                    // exponential back-off.
                }

                return msg;
            }

            @Override
            protected void onPostExecute(String msg)
            {
                Log.i("MainActivity.java | onPostExecute", "|" + msg + "|");
            }
        }.execute(null, null, null);
    }

    // registraion id를 preference에 저장한다.
    private void storeRegistrationId(String regId)
    {
        int appVersion = getAppVersion();
        Log.i("MainActivity.java | storeRegistrationId", "|" + "Saving regId on app version " + appVersion + "|");
        PreferenceUtil.instance(getApplicationContext()).putRedId(regId);
        PreferenceUtil.instance(getApplicationContext()).putAppVersion(appVersion);
    }
}