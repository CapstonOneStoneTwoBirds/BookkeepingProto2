package com.capstone.bookkeepingproto2;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

/**
 * Created by New on 2015-04-29.
 */
public class PrivateActivity extends Activity implements View.OnClickListener{
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.indivisual_main);
        Button clear_btn = (Button)findViewById(R.id.auto_btn);
        clear_btn.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        SharedPreferences mPreference = getSharedPreferences("myInfo", MODE_PRIVATE);
        SharedPreferences.Editor editor = mPreference.edit();
        editor.clear();
        editor.commit();
        Intent intent = new Intent(getApplicationContext(), MainActivity.class);
        startActivity(intent);
    }
}