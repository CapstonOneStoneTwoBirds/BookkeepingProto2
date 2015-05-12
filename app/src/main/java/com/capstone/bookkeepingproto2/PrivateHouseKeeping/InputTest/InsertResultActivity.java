package com.capstone.bookkeepingproto2.PrivateHouseKeeping.InputTest;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.capstone.bookkeepingproto2.PrivateHouseKeeping.CalendarTest.CalendarActivity;
import com.capstone.bookkeepingproto2.PrivateHouseKeeping.Control.MyDatabase;
import com.capstone.bookkeepingproto2.R;

/**
 * Created by YeomJi on 15. 4. 28..
 */
public class InsertResultActivity extends Activity {

    static String DayBB,AccountBB,CategroyBB, ContentBB;
    static int MoneyBB;
    TextView DayTxt1, AccountTxt1, CategroyTxt1, MoneyTxt1, ContentTxt1;
    TextView DayTxt2, AccountTxt2, CategroyTxt2, MoneyTxt2, ContentTxt2;
    Button gotocalendarBtn;

    private final String tag = "InsertResultActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.insert_result_main);

        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();
        DayBB = bundle.getString("date");
        AccountBB = bundle.getString("account");
        CategroyBB = bundle.getString("category");
        MoneyBB = bundle.getInt("money");
        //MoneyBB = bundle.getString("money");
        ContentBB = bundle.getString("content");

        DayTxt1 = (TextView) findViewById(R.id.dayB);
        AccountTxt1 = (TextView) findViewById(R.id.accountB);
        CategroyTxt1 = (TextView) findViewById(R.id.categoryB);
        MoneyTxt1 = (TextView) findViewById(R.id.moneyB);
        ContentTxt1 = (TextView) findViewById(R.id.contentB);

        DayTxt2 = (TextView) findViewById(R.id.dayS);
        AccountTxt2 = (TextView) findViewById(R.id.accountS);
        CategroyTxt2 = (TextView) findViewById(R.id.categoryS);
        MoneyTxt2 = (TextView) findViewById(R.id.moneyS);
        ContentTxt2 = (TextView) findViewById(R.id.contentS);

        gotocalendarBtn = (Button) findViewById(R.id.gotocalendar);


        DayTxt1.setText(DayBB);
        AccountTxt1.setText(AccountBB);
        CategroyTxt1.setText(CategroyBB);
        MoneyTxt1.setText(String.valueOf(MoneyBB));
        ContentTxt1.setText(ContentBB);



        gotocalendarBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                        Intent intent = new Intent(getApplicationContext(), CalendarActivity.class);
                        startActivity(intent);
                        finish();

            }
        });

    }
}
