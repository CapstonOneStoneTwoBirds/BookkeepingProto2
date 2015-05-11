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
    static String MoneyBB;
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
        //MoneyBB = bundle.getInt("money");
        MoneyBB = bundle.getString("money");
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

        MyDatabase myDB = new MyDatabase(this);
        final SQLiteDatabase db = myDB.getWritableDatabase();

        String sql = "SELECT * FROM daymoney WHERE date LIKE ?";
        Cursor cursor = db.rawQuery(sql, new String[]{DayBB});

        int recordCount = cursor.getCount();
        Log.d(tag, "cursor count : " + recordCount + "\n");


        int dateCol = cursor.getColumnIndex("date");
        int accountCol = cursor.getColumnIndex("account");
        int categoryCol = cursor.getColumnIndex("category");
        int moneyCol = cursor.getColumnIndex("money");
        int contentCol = cursor.getColumnIndex("content");


        while (cursor.moveToNext()) {
            String date = cursor.getString(dateCol);
            String account = cursor.getString(accountCol);
            String category = cursor.getString(categoryCol);
            int money = cursor.getInt(moneyCol);
            String content = cursor.getString(contentCol);

            DayTxt2.setText(date);
            AccountTxt2.setText(account);
            CategroyTxt2.setText(category);
            MoneyTxt2.setText(money+"");
            ContentTxt2.setText(content);

        }

        cursor.close();

        DayTxt1.setText(DayBB);
        AccountTxt1.setText(AccountBB);
        CategroyTxt1.setText(CategroyBB);
        MoneyTxt1.setText(MoneyBB);
        ContentTxt1.setText(ContentBB);

        Toast.makeText(InsertResultActivity.this, MoneyBB,
                Toast.LENGTH_SHORT).show();


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
