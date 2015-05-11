package com.capstone.bookkeepingproto2.PrivateHouseKeeping.InputTest;

import android.app.Activity;
import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.capstone.bookkeepingproto2.PrivateHouseKeeping.Control.MyDatabase;
import com.capstone.bookkeepingproto2.PrivateHouseKeeping.WidgetTest.MyCustomWidget;
import com.capstone.bookkeepingproto2.PrivateHouseKeeping.WidgetTest.UpperLimitActivity;
import com.capstone.bookkeepingproto2.R;


/**
 * Created by YeomJi on 15. 4. 26..
 */
public class InputActivity extends Activity {

    String store, product, cost;

    EditText accountTxt, moneyTxt, contentTxt;
    Button insertContentBtn;
    String Y, Mo, D, H, Mi, Cate;
    int total, food, play, house, traffic, saving;
    int accCheck;

    private final String tag = "InputActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.input_main);


        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();


        accountTxt = (EditText) findViewById(R.id.account);
        moneyTxt = (EditText) findViewById(R.id.money);
        contentTxt = (EditText) findViewById(R.id.content);
        insertContentBtn = (Button) findViewById(R.id.insertContent);

        Spinner spinner = (Spinner) findViewById(R.id.category);
        final ArrayAdapter adapter = ArrayAdapter.createFromResource(this, R.array.cate, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                Cate = (String) parent.getItemAtPosition(position);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });


        // 날짜 스피너 시작
        Spinner spinnerY = (Spinner) findViewById(R.id.year);
        ArrayAdapter adapterY = ArrayAdapter.createFromResource(this, R.array.Y, android.R.layout.simple_spinner_item);
        adapterY.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerY.setAdapter(adapterY);
        spinnerY.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                Y = (String) parent.getItemAtPosition(position);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });


        Spinner spinnerMo = (Spinner) findViewById(R.id.month);
        ArrayAdapter adapterMo = ArrayAdapter.createFromResource(this, R.array.Mo, android.R.layout.simple_spinner_item);
        adapterMo.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerMo.setAdapter(adapterMo);
        spinnerMo.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                Mo = (String) parent.getItemAtPosition(position);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });

        Spinner spinnerD = (Spinner) findViewById(R.id.date);
        ArrayAdapter adapterD = ArrayAdapter.createFromResource(this, R.array.D, android.R.layout.simple_spinner_item);
        adapterD.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerD.setAdapter(adapterD);
        spinnerD.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                D = (String) parent.getItemAtPosition(position);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });

        Spinner spinnerH = (Spinner) findViewById(R.id.hour);
        ArrayAdapter adapterH = ArrayAdapter.createFromResource(this, R.array.H, android.R.layout.simple_spinner_item);
        adapterH.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerH.setAdapter(adapterH);
        spinnerH.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                H = (String) parent.getItemAtPosition(position);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });

        Spinner spinnerMi = (Spinner) findViewById(R.id.minute);
        ArrayAdapter adapterMi = ArrayAdapter.createFromResource(this, R.array.Mi, android.R.layout.simple_spinner_item);
        adapterMi.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerMi.setAdapter(adapterMi);
        spinnerMi.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                Mi = (String) parent.getItemAtPosition(position);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });

        // 날짜 스피너 끝


        //account --> 지출 제목, content --> 현금, 카드 등 거래 방식으로 일시적 교환. 디비 설계 다시 해야 됨
        //또한 day에서 년 월 일 분 초로 쪼개야 댐
        // 카테고리 디비에도 기타 항목을 추가해야 됨. 우선 기타 입력은 saving에 간다고 하자.


        if (!(bundle == null)) {
            System.out.println("bundle is not null!");
            accountTxt.setText(bundle.getString("store"));
            moneyTxt.setText(bundle.getString("cost"));
            switch (bundle.getString("product")) {
                case "의류": spinner.setSelection(1); break;
                case "주거": spinner.setSelection(2); break;
                case "여가": spinner.setSelection(3); break;
                case "교통": spinner.setSelection(4); break;
                case "저축": spinner.setSelection(5); break;
                case "기타": spinner.setSelection(5); break;
                default: break;
            }
        }


        MyDatabase myDB = new MyDatabase(this);
        final SQLiteDatabase db = myDB.getWritableDatabase();

        insertContentBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                Bundle bundle = null;
                if (accountTxt.getText().toString().equals(null)) {
                    Toast.makeText(InputActivity.this, "Please insert Account",
                            Toast.LENGTH_SHORT).show();
                } else if (moneyTxt.getText().toString().equals(null)) {
                    Toast.makeText(InputActivity.this, "Please insert Money",
                            Toast.LENGTH_SHORT).show();
                }/* else if (Integer.valueOf(moneyTxt.getText().toString()) <= 0) {
                                                        Toast.makeText(InputActivity.this, "Money is not correct",
                                                                Toast.LENGTH_SHORT).show();
                } */ else { // 가계 내역 입력 갱신
                    ContentValues values = new ContentValues();
                    values.put("date", D);
                    values.put("account", accountTxt.getText().toString());
                    values.put("category", Cate);
                    values.put("money", Integer.valueOf(moneyTxt.getText().toString()));
                    values.put("content", contentTxt.getText().toString());

                    db.insert("daymoney", null, values);

                    bundle = new Bundle();
                    bundle.putString("date", D);
                    bundle.putString("account", accountTxt.getText().toString());
                    bundle.putString("category", Cate);
                    bundle.putString("money", moneyTxt.getText().toString());
                    bundle.putString("content", contentTxt.getText().toString());
                }

                //cursor.getCount() 행의 갯수


                String sql = "SELECT * FROM moneybook";
                Cursor cursor = db.rawQuery(sql, null);

                int recordCount = cursor.getCount();
                Log.d(tag, "cursor count : " + recordCount + "\n");

                int foodCol = cursor.getColumnIndex("food");
                int totalCol = cursor.getColumnIndex("total");
                int playCol = cursor.getColumnIndex("play");
                int houseCol = cursor.getColumnIndex("house");
                int trafficCol = cursor.getColumnIndex("traffic");
                int savingCol = cursor.getColumnIndex("saving");

                while (cursor.moveToNext()) {
                    food = cursor.getInt(foodCol);
                    total = cursor.getInt(totalCol);
                    play = cursor.getInt(playCol);
                    house = cursor.getInt(houseCol);
                    traffic = cursor.getInt(trafficCol);
                    saving = cursor.getInt(savingCol);
                }

                // 위젯에 적용하기 위해 디비에 추가된 금액 갱신

                if (Cate.equals("food")) {
                    db.execSQL("UPDATE moneybook SET food = food + " + moneyTxt.getText().toString());
                    db.execSQL("UPDATE checkamount SET acc = acc + " + moneyTxt.getText().toString() + " WHERE title = 'food';");
                } else if (Cate.equals("play")) {
                    db.execSQL("UPDATE moneybook SET play = play + " + moneyTxt.getText().toString());
                    db.execSQL("UPDATE checkamount SET acc = acc + " + moneyTxt.getText().toString() + " WHERE title = 'play';");
                } else if (Cate.equals("house")) {
                    db.execSQL("UPDATE moneybook SET house = house + " + moneyTxt.getText().toString());
                    db.execSQL("UPDATE checkamount SET acc = acc + " + moneyTxt.getText().toString() + " WHERE title = 'house';");
                } else if (Cate.equals("traffic")) {
                    db.execSQL("UPDATE moneybook SET house = traffic + " + moneyTxt.getText().toString());
                    db.execSQL("UPDATE checkamount SET acc = acc + " + moneyTxt.getText().toString() + " WHERE title = 'traffic';");
                } else if (Cate.equals("saving")) {
                    db.execSQL("UPDATE moneybook SET house = saving + " + moneyTxt.getText().toString());
                    db.execSQL("UPDATE checkamount SET acc = acc + " + moneyTxt.getText().toString() + " WHERE title = 'saving';");
                }

                db.execSQL("UPDATE moneybook SET total = total + " + moneyTxt.getText().toString());
                db.execSQL("UPDATE checkamount SET acc = acc + " + moneyTxt.getText().toString() + " WHERE title = 'total';");

                sql = "SELECT * FROM checkamount WHERE title LIKE ? ";
                cursor = db.rawQuery(sql, new String[]{MyCustomWidget.titleWidget});

                int accCol = cursor.getColumnIndex("acc");

                while (cursor.moveToNext()) { accCheck = cursor.getInt(accCol); }

                System.out.println("Confirm3 --------> " + MyCustomWidget.titleWidget +"    "+accCheck);
                getContent(MyCustomWidget.titleWidget, MyCustomWidget.goalWidget, accCheck);

                db.close();

                Intent intent = new Intent(getApplicationContext(), InsertResultActivity.class);
                intent.putExtras(bundle);
                startActivity(intent);
                finish();

            }
        }


    );

}
    public void getContent(String tt, int gg, int cc) {
        UpperLimitActivity.t = tt;
        UpperLimitActivity.g = gg;
        UpperLimitActivity.c = cc;
    }
}
