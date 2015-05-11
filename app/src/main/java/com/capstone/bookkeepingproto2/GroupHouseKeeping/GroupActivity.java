package com.capstone.bookkeepingproto2.GroupHouseKeeping;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.capstone.bookkeepingproto2.R;

/**
 * Created by New on 2015-05-11.
 */
public class GroupActivity extends Activity implements View.OnClickListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.group_main);

        Button newgroup = (Button)findViewById(R.id.newgroup_btn);
        newgroup.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        if(v.getId() == R.id.newgroup_btn){
            Intent intent = new Intent(this, CreateGroupActivity.class);
            startActivity(intent);

        }
    }
}
