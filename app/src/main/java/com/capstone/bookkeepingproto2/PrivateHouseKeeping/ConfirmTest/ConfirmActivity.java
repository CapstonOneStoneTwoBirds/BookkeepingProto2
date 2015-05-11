package com.capstone.bookkeepingproto2.PrivateHouseKeeping.ConfirmTest;

import android.app.Activity;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import onestonetwobirds.capstontest1.Control.MyDatabase;
import onestonetwobirds.capstontest1.R;

/**
 * Created by YeomJi on 15. 5. 11..
 */
public class ConfirmActivity extends Activity {

    TextView CTotal, CFood, CPlay, CHouse, CTraffic, CSaving;
    Button OK;
    //String total, food, play, house, traffic, saving;

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
            /*
            String date = ;
            String account = cursor.getString(accountCol);
            String category = cursor.getString(categoryCol);
            String content = cursor.getString(contentCol);
            String content = cursor.getString(contentCol);
            String content = cursor.getString(contentCol);
            */

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
}
