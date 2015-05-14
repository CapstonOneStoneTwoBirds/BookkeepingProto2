package com.capstone.bookkeepingproto2.GroupHouseKeeping;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

import com.capstone.bookkeepingproto2.R;

/**
 * Created by YeomJi on 15. 5. 14..
 */
public class GroupMemberActivity extends ActionBarActivity {

    String _id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.group_member_list);

        _id = getIntent().getStringExtra("_id");
        System.out.println("_id : " + _id);

        Button newmember = (Button)findViewById(R.id.newmember_btn);

        newmember.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), WriteArticleActivity.class);
                intent.putExtra("_id", _id);
                startActivity(intent);
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        menu.add(0, 0, 0, "Group Article");
        menu.add(0, 1, 0, "Group Announce");
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        Intent intent;
        switch (item.getItemId()) {
            case 0:
                //처리할 이벤트
                intent = new Intent(getApplicationContext(), GroupArticleActivity.class);
                intent.putExtra("_id", _id);
                startActivity(intent);
                finish();
                break;
            case 1:
                intent = new Intent(getApplicationContext(), GroupAnnounceActivity.class);
                intent.putExtra("_id", _id);
                startActivity(intent);
                finish();
                break;
            default:
                break;
        }
        return super.onOptionsItemSelected(item);
    }


    @Override
    public void onBackPressed() {
        finish();
    }
}
