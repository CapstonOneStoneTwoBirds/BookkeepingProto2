package com.capstone.bookkeepingproto2.GroupHouseKeeping;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.capstone.bookkeepingproto2.HttpClient.HttpClient;
import com.capstone.bookkeepingproto2.LoginTest.MainActivity;
import com.capstone.bookkeepingproto2.R;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.apache.http.Header;
import org.bson.types.ObjectId;
import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Text;

import java.util.Date;

/**
 * Created by New on 2015-06-06.
 */
public class ConsentInviteActivity extends Activity implements View.OnClickListener {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.consent_invite);

        final TextView groupname = (TextView)findViewById(R.id.consentinvite_groupname_tv);
        final TextView owner = (TextView)findViewById(R.id.consentinvite_owner_tv);
        final TextView since = (TextView)findViewById(R.id.consentinvite_since_tv);
        final TextView member = (TextView)findViewById(R.id.consentinvite_member_tv);

        Button consent = (Button)findViewById(R.id.consentinvite_consent_button);
        Button deny = (Button)findViewById(R.id.consentinvite_deny_button);
        consent.setOnClickListener(this);
        deny.setOnClickListener(this);

        String group_id = getIntent().getStringExtra("group_id");
        RequestParams param = new RequestParams();
        param.put("group_id", group_id);

        HttpClient.post("getGroupData/", param, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                try {
                    System.out.println("getGroupDate Success");
                    JSONObject obj = new JSONObject(new String(responseBody));
                    groupname.setText(obj.get("groupname").toString());
                    owner.setText(obj.get("owner").toString());
                    member.setText(obj.get("member").toString());
                    since.setText(obj.get("since").toString());
                }catch(JSONException e){}
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                System.out.println("getGroupDate Fail");
            }
        });
    }

    @Override
    public void onClick(View v) {
        switch( v.getId() ){
            case R.id.consentinvite_consent_button:
                // 이걸 초대받은사람이 OK 했을때 실행해야해.

                final String group_id = getIntent().getStringExtra("group_id");
                String email = getIntent().getStringExtra("email");

                RequestParams param = new RequestParams();
                param.put("group_id", group_id);
                param.put("email", email);

                HttpClient.post("writeMember/", param, new AsyncHttpResponseHandler() {
                    @Override
                    public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                        System.out.println("Member Save Success");
                        System.out.println("output : " + new String(responseBody));
                        switch (new String(responseBody)) {
                            case "1":
                                System.out.println("write member error");
                                break;

                            case "2":
                                System.out.println("write member Success");

                                Intent intent = new Intent(getApplicationContext(), GroupArticleActivity.class);
                                intent.putExtra("groupid", group_id);
                                startActivity(intent);
                                finish();

                                break;
                        }
                    }

                    @Override
                    public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                        System.out.println("error message 1 : " + error);
                    }
                });

                break;
            case R.id.consentinvite_deny_button:
                Toast toastView = Toast.makeText(getApplicationContext(),
                        "denied Invitation", Toast.LENGTH_LONG);
                toastView.setGravity(Gravity.CENTER, 40, 25);
                toastView.show();
                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                startActivity(intent);
                break;
        }
    }
}
