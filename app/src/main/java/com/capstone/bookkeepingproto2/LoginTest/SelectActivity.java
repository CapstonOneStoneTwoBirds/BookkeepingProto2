package com.capstone.bookkeepingproto2.LoginTest;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.capstone.bookkeepingproto2.GroupHouseKeeping.GroupListActivity;
import com.capstone.bookkeepingproto2.PrivateHouseKeeping.ConfirmTest.ConfirmActivity;
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
        Button pri = (Button) findViewById(R.id.private_sel_btn);
        Button gro = (Button) findViewById(R.id.group_sel_btn);
        Button fix = (Button)findViewById(R.id.fix_info_btn);
        Button leave = (Button)findViewById(R.id.leave_btn);
        leave.setOnClickListener(this);
        clear_btn.setOnClickListener(this);
        fix.setOnClickListener(this);
        pri.setOnClickListener(this);
        gro.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.private_sel_btn:
                startActivity(new Intent(getApplicationContext(), ConfirmActivity.class));
                break;

            case R.id.group_sel_btn:
                startActivity(new Intent(getApplicationContext(), GroupListActivity.class));
                break;
            case R.id.auto_uncheck_btn:
                SharedPreferences mPreference = getSharedPreferences("myInfo", MODE_PRIVATE);
                SharedPreferences.Editor editor = mPreference.edit();
                editor.clear();
                editor.commit();
                startActivity(new Intent(getApplicationContext(), MainActivity.class));
                break;
            case R.id.fix_info_btn:
                startActivity(new Intent(getApplicationContext(), FixInfoActivity.class));
                System.out.println("Here fix_info_btn");
                break;
            case R.id.leave_btn:
                startActivity(new Intent(getApplicationContext(), LeaveActivity.class));
                break;
        }
    }
}