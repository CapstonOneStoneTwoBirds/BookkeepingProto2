package com.capstone.bookkeepingproto2.GroupHouseKeeping;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

/**
 * YoungHoonKim
 * Created by New on 2015-05-13.
 */
public class GroupActivity extends Activity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        String groupname = getIntent().getStringExtra("groupname");
        SharedPreferences mPreference = getSharedPreferences("myInfo", MODE_PRIVATE);;
        String email = mPreference.getString("email", "");

        




    }
}
