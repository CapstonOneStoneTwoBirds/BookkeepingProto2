package com.capstone.bookkeepingproto2.PrivateHouseKeeping.ConfirmTest;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.capstone.bookkeepingproto2.OCR.abbyy.ocrsdk.android.OCRActivity;
import com.capstone.bookkeepingproto2.PrivateHouseKeeping.CalendarTest.CalendarActivity;
import com.capstone.bookkeepingproto2.PrivateHouseKeeping.Control.MyDatabase;
import com.capstone.bookkeepingproto2.PrivateHouseKeeping.InputTest.InputActivity;
import com.capstone.bookkeepingproto2.PrivateHouseKeeping.SpeechTest.SpeechTestActivity;
import com.capstone.bookkeepingproto2.PrivateHouseKeeping.WidgetTest.UpperLimitActivity;
import com.capstone.bookkeepingproto2.R;


/**
 * Created by YeomJi on 15. 5. 11..
 */
public class ConfirmActivity  extends ActionBarActivity {

    TextView CTotal, CFood, CPlay, CHouse, CTraffic, CSaving;
    Button OK;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.confirm_main);

        CTotal = (TextView) findViewById(R.id.confirm_total);
        CFood = (TextView) findViewById(R.id.confirm_food);
        CPlay = (TextView) findViewById(R.id.confirm_play);
        CHouse = (TextView) findViewById(R.id.confirm_house);
        CTraffic = (TextView) findViewById(R.id.confirm_traffic);
        CSaving = (TextView) findViewById(R.id.confirm_saving);
        OK = (Button) findViewById(R.id.confrim_OK);


        MyDatabase myDB = new MyDatabase(this);
        final SQLiteDatabase db = myDB.getWritableDatabase();

        String sql = "SELECT * FROM moneybook";
        Cursor cursor = db.rawQuery(sql, null);

        int totalCol = cursor.getColumnIndex("total");
        int foodCol = cursor.getColumnIndex("food");
        int playCol = cursor.getColumnIndex("play");
        int houseCol = cursor.getColumnIndex("house");
        int trafficCol = cursor.getColumnIndex("traffic");
        int savingCol = cursor.getColumnIndex("saving");


        while (cursor.moveToNext()) {

            CTotal.setText(cursor.getString(totalCol));
            CFood.setText(cursor.getString(foodCol));
            CPlay.setText(cursor.getString(playCol));
            CHouse.setText(cursor.getString(houseCol));
            CTraffic.setText(cursor.getString(trafficCol));
            CSaving.setText(cursor.getString(savingCol));

        }

        cursor.close();
        db.close();


        OK.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        menu.add(0, 0, 0, "Calendar Test");
        menu.add(0, 1, 0, "Insert Test");
        menu.add(0, 2, 0, "Speech Test");
        menu.add(0, 3, 0, "Widget Test");
        menu.add(0, 4, 0, "OCR Test");
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        Intent intent;
        switch (item.getItemId()) {
            case 0:
                //처리할 이벤트
                intent = new Intent(getApplicationContext(), CalendarActivity.class);
                startActivity(intent);
                break;
            case 1:
                intent = new Intent(getApplicationContext(), InputActivity.class);
                startActivity(intent);
                break;
            case 2:
                intent = new Intent(getApplicationContext(), SpeechTestActivity.class);
                startActivity(intent);
                break;
            case 3:
                intent = new Intent(getApplicationContext(), UpperLimitActivity.class);
                startActivity(intent);
                break;
            case 4:
                intent = new Intent(getApplicationContext(), OCRActivity.class);
                startActivity(intent);
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
