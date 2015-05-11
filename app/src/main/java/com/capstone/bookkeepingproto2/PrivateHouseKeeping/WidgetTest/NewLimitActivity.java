package com.capstone.bookkeepingproto2.PrivateHouseKeeping.WidgetTest;

import android.app.Activity;
import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.capstone.bookkeepingproto2.PrivateHouseKeeping.Control.MyDatabase;
import com.capstone.bookkeepingproto2.R;


/**
 * Created by YeomJi on 15. 5. 6..
 */
public class NewLimitActivity extends Activity {

    EditText UpperGoal;
    Button UpperOK;
    String Title;
    int IDcount;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.new_limit_main);

        UpperGoal = (EditText) findViewById(R.id.upperGoal);
        UpperOK = (Button) findViewById(R.id.upperOK);

        Spinner spinnerUpper = (Spinner) findViewById(R.id.upperTitle);
        ArrayAdapter adapterUpper = ArrayAdapter.createFromResource(this, R.array.UpperTitle, android.R.layout.simple_spinner_item);
        adapterUpper.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerUpper.setAdapter(adapterUpper);
        spinnerUpper.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                Title = (String) parent.getItemAtPosition(position);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });

        MyDatabase myDB = new MyDatabase(this);
        final SQLiteDatabase db = myDB.getWritableDatabase();

        UpperOK.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String sql = "SELECT * FROM checkamount";
                Cursor cursor = db.rawQuery(sql, null);

                IDcount = 0;

                if (UpperGoal.getText().toString().equals(null)) {
                    Toast.makeText(NewLimitActivity.this, "Please insert Goal",
                            Toast.LENGTH_SHORT).show();
                } else { // 가계 내역 입력 갱신
                    ContentValues values = new ContentValues();



                    if(cursor.getCount() == 0) {
                        values.put("number",1);
                    } else {
                        while (IDcount < cursor.getCount()) {
                            IDcount++;
                        }
                        Toast.makeText(NewLimitActivity.this, cursor.getCount()+"",
                                Toast.LENGTH_SHORT).show();
                        values.put("number", ++IDcount);
                        Toast.makeText(NewLimitActivity.this, IDcount+"",
                                Toast.LENGTH_SHORT).show();
                    }



                    values.put("title", Title);
                    values.put("goal", Integer.valueOf(UpperGoal.getText().toString()));
                    values.put("acc", 0);

                    db.insert("checkamount", null, values);

                }
                db.close();
                finish();

            }
        });

    }
}
