package com.capstone.bookkeepingproto2.PrivateHouseKeeping.LoginTest;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.capstone.bookkeepingproto2.PrivateHouseKeeping.Group.GroupActivity;
import com.capstone.bookkeepingproto2.PrivateHouseKeeping.LoginTest.MainActivity;
import com.capstone.bookkeepingproto2.PrivateHouseKeeping.Private.PrivateActivity;
import com.capstone.bookkeepingproto2.R;

/**
 * Created by New on 2015-05-11.
 */
public class SelectActivity extends Activity implements View.OnClickListener {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.select);
        Button clear_btn = (Button) findViewById(R.id.auto_uncheck_btn);
        clear_btn.setOnClickListener(this);
        Button pri = (Button) findViewById(R.id.private_sel_btn);
        Button gro = (Button) findViewById(R.id.group_sel_btn);
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.private_sel_btn) {
            startActivity(new Intent(getApplicationContext(), PrivateActivity.class));
        } else if (v.getId() == R.id.group_sel_btn) {
            startActivity(new Intent(getApplicationContext(), GroupActivity.class));
        } else {
            SharedPreferences mPreference = getSharedPreferences("myInfo", MODE_PRIVATE);
            SharedPreferences.Editor editor = mPreference.edit();
            editor.clear();
            editor.commit();
            Intent intent = new Intent(getApplicationContext(), MainActivity.class);
            startActivity(intent);
        }
    }
}