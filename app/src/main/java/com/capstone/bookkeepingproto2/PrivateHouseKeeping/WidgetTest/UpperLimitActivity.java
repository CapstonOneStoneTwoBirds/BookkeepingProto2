package com.capstone.bookkeepingproto2.PrivateHouseKeeping.WidgetTest;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.Toast;

import com.capstone.bookkeepingproto2.R;


/**
 * Created by YeomJi on 15. 5. 6..
 */
public class UpperLimitActivity extends Activity {

    Button NewUpperLimit, EndUpperLimit;

    final String TAG = "UpperLimitActivity";

    int checkID;

    // 한 타이틀에 한 개의 리스트 가능 이 중 선택하여 위젯으로 올림


    private String title;
    private int goal, acc;

    public static String t = "";
    public static int g = 0, c = 0;
    
    private Cursor cursor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.upper_limit_main);

        NewUpperLimit = (Button) findViewById(R.id.NewUpperLimit);
        EndUpperLimit = (Button) findViewById(R.id.EndUpperLimit);

        /*

        ArrayList<String> uppwerList = new ArrayList<String>();
        
        MyDatabase myDB = new MyDatabase(this);
        final SQLiteDatabase db = myDB.getWritableDatabase();
        
        String sql = "SELECT * FROM checkamount";
        cursor = db.rawQuery(sql, null);
        
        int recordCount = cursor.getCount();
        Log.d(TAG, "cursor count : " + recordCount + "\n");

        int titleCol = cursor.getColumnIndex("title");
        int goalCol = cursor.getColumnIndex("goal");
        int accCol = cursor.getColumnIndex("acc");


        while (cursor.moveToNext()) {
            String chooseTitle = cursor.getString(titleCol);
            int goal = cursor.getInt(goalCol);
            int acc = cursor.getInt(accCol);

            result = "TYPE : " + chooseTitle +" / UPPER LIMIT : "+ goal +" / NOW : "+ acc;
            Toast.makeText(UpperLimitActivity.this, result,
                    Toast.LENGTH_SHORT).show();
            uppwerList.add(result);

        }

        ArrayAdapter<String> upperAdapter = new ArrayAdapter<String>(getApplicationContext(), R.layout.upperlimit_list, uppwerList);

        ListView listViewInsert = (ListView)findViewById(R.id.upperLimittList);
        listViewInsert.setAdapter(upperAdapter);
        */


        NewUpperLimit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), NewLimitActivity.class);
                startActivity(intent);
            }
        });

        EndUpperLimit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.i(TAG, "onResume");

        //ListViewFruit
        SQLiteDatabase db = openOrCreateDatabase("CapstonTest1.db", Context.MODE_PRIVATE, null);
        ListView listView = (ListView)findViewById(R.id.upperLimittList);
        //cursor = db.rawQuery("SELECT rowid _id,*  FROM checkamount", null);
        cursor = db.rawQuery("SELECT rowid _id, * FROM checkamount", null);

        if(cursor.getCount() != 0) {
            Toast.makeText(getApplicationContext(), cursor.getCount() + "", Toast.LENGTH_LONG).show();
            cursor.moveToFirst();
            String[] from = new String[]{"number", "title", "goal", "acc"};
            int[] to = new int[]{R.id.lf_tv_id, R.id.lf_tv_title, R.id.lf_tv_goal, R.id.lf_tv_acc};
            final SimpleCursorAdapter adapter = new SimpleCursorAdapter(
                    listView.getContext(),
                    R.layout.upperlimit_list,
                    cursor,
                    from,
                    to
            );

            listView.setAdapter(adapter);
        } else Toast.makeText(getApplicationContext(), "No DATA", Toast.LENGTH_LONG).show();

        db.close();
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View v, int position,
                                    long id) {

                if (id != 0)
                    Toast.makeText(getApplicationContext(), (int) id + "", Toast.LENGTH_LONG).show();
                else Toast.makeText(getApplicationContext(), "No id", Toast.LENGTH_LONG).show();
                checkID = (int) id;
                DialogSimple();

            }
        });


    }

    @Override
    protected void onPause() {
        super.onPause();
        Log.i(TAG, "onPause");
    }

    @Override
    protected void onStop() {
        super.onStop();
        Log.i(TAG, "onStop");

        if (cursor != null)
            cursor.close();
    }


    private void DialogSimple(){
        AlertDialog.Builder alt_bld = new AlertDialog.Builder(this);
        final SQLiteDatabase db = openOrCreateDatabase("CapstonTest1.db", Context.MODE_PRIVATE, null);

        String sql = "select * from checkamount where number like ?";
        Cursor cursor = db.rawQuery(sql, new String[]{String.valueOf(checkID)});

        int recordCount = cursor.getCount();
        Log.d(TAG, "cursor count : " + recordCount + "\n");

        int titleCol = cursor.getColumnIndex("title");
        int goalCol = cursor.getColumnIndex("goal");
        int accCol = cursor.getColumnIndex("acc");

        while (cursor.moveToNext()) {
            title = cursor.getString(titleCol);
            goal = cursor.getInt(goalCol);
            acc = cursor.getInt(accCol);
        }


            alt_bld.setMessage(title + "  " + goal + "  " + acc).setCancelable(
                    true).setPositiveButton("Modify",
                    new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            // Button -----> 구현하셈
                            Intent intent = new Intent(getApplicationContext(), NewLimitActivity.class);
                            //intent.putExtras(bundle);
                            startActivity(intent);
                        }
                    }).setNeutralButton("Widget",
                    new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            Toast.makeText(UpperLimitActivity.this, id + "  성공^^", Toast.LENGTH_SHORT).show();
                            /*
                            Bundle bundle = new Bundle();
                            bundle.putString("title", title);
                            bundle.putInt("goal", goal);
                            bundle.putInt("acc", acc);
                            */

                            db.execSQL("UPDATE checkamount SET isWidget = 0 ;");
                            db.execSQL("UPDATE checkamount SET isWidget = 1 WHERE title = '"+title+"';");
                            getContent(title, goal, acc);

                        }
                    }).setNegativeButton("Delete",
                    new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
// 앞 번호가 삭제되면 New 설정이 추가되지 않는 부분을 해결 해야 함.

                            db.execSQL("delete from checkamount where number = " + checkID + ";");
                            //db.execSQL("delete from checkamount ;");
                            dialog.cancel();
                            db.close();
                            onResume();
                        }
                    });
        AlertDialog alert = alt_bld.create();
        alert.setTitle("Dialog");
        //alert.setIcon(R.drawable.yumji1);
        alert.show();
    }

    public void getContent(String tt, int gg, int cc) {
        t = tt;
        g = gg;
        c = cc;
    }
}
