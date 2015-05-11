package com.capstone.bookkeepingproto2.PrivateHouseKeeping.CalendarTest;


import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.capstone.bookkeepingproto2.PrivateHouseKeeping.Control.MyDatabase;
import com.capstone.bookkeepingproto2.R;
import com.roomorama.caldroid.CaldroidFragment;
import com.roomorama.caldroid.CaldroidListener;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;



/**
 * Created by YeomJi on 15. 4. 24..
 */

public class CalendarActivity extends FragmentActivity implements OnClickListener {

    public final static String
            DIALOG_TITLE = "dialogTitle",
            MONTH = "month",
            YEAR = "year",
            SHOW_NAVIGATION_ARROWS = "showNavigationArrows",
            DISABLE_DATES = "disableDates",
            SELECTED_DATES = "selectedDates",
            MIN_DATE = "minDate",
            MAX_DATE = "maxDate",
            ENABLE_SWIPE = "enableSwipe",
            START_DAY_OF_WEEK = "startDayOfWeek",
            SIX_WEEKS_IN_CALENDAR = "sixWeeksInCalendar",
            ENABLE_CLICK_ON_DISABLED_DATES = "enableClickOnDisabledDates",
            SQUARE_TEXT_VIEW_CELL = "squareTextViewCell",
            THEME_RESOURCE = "themeResource";

    final String TAG = "CalendarActivity";

    String result;

    public final static int FRAGMENT_CALENDAR = 0;
    public final static int FRAGMENT_LIST = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.calendar_main);

        // Log.e("Mytag","Activity1");
        //Log.d(TAG, "fragmentReplace " + reqNewFragmentIndex);

        CaldroidFragment caldroidFragment = new CaldroidFragment();
        Bundle args = new Bundle();
        Calendar cal = Calendar.getInstance();
        args.putInt(CaldroidFragment.MONTH, cal.get(Calendar.MONTH) + 1);
        args.putInt(CaldroidFragment.YEAR, cal.get(Calendar.YEAR));
        // args.putInt(CaldroidFragment.START_DAY_OF_WEEK, CaldroidFragment.TUESDAY); // Tuesday부터 시작
        //args.putBoolean(CaldroidFragment.ENABLE_CLICK_ON_DISABLED_DATES, true); // 불가능한 날짜 설정
        args.putBoolean(CaldroidFragment.SQUARE_TEXT_VIEW_CELL, false); // 네모박스 크기 작게하기
        args.putInt(CaldroidFragment.THEME_RESOURCE, com.caldroid.R.style.CaldroidDefaultDark); // 검은색 테마

        MyDatabase myDB = new MyDatabase(this);
        final SQLiteDatabase db = myDB.getWritableDatabase();


        caldroidFragment.setArguments(args);

        android.support.v4.app.FragmentTransaction t = getSupportFragmentManager().beginTransaction();

        // replace fragment

        t.replace(R.id.calendar_fragment, caldroidFragment);



        final ArrayList<String> arrListInsert = new ArrayList<String>();


        final CaldroidListener listener = new CaldroidListener() {

            @Override
            public void onSelectDate(Date date, View view) { // 특정 날짜 클릭했을 때
                SimpleDateFormat formatter = new SimpleDateFormat("yyyy-mm-dd");
                String subDate = formatter.format(date).substring(8,10);
                //Toast.makeText(getApplicationContext(), subDate, Toast.LENGTH_SHORT).show();

                ArrayAdapter<String> adapterInsert = new ArrayAdapter<String>(getApplicationContext(), R.layout.calendertext, arrListInsert);
                //ArrayAdapter<String> adapterInsert = new ArrayAdapter<String>(getApplicationContext(), android.R.layout.simple_list_item_1, arrListInsert);
                // 위는 리스트 내부의 글자색 흰색
                arrListInsert.clear();
                ListView listViewInsert = (ListView)findViewById(R.id.insertList);

                /*
                //ListViewFruit
                SQLiteDatabase db = openOrCreateDatabase("CapstonTest1.db", Context.MODE_PRIVATE, null);
                ListView listView = (ListView)findViewById(R.id.upperLimittList);
                Cursor cursor = db.rawQuery("SELECT rowid _id, * FROM daymoney", null);

                if(cursor.getCount() != 0) {
                    Toast.makeText(getApplicationContext(), cursor.getCount() + "", Toast.LENGTH_LONG).show();
                    cursor.moveToFirst();
                    String[] from = new String[]{"date", "account", "category", "money"};
                    int[] to = new int[]{R.id.lf_cal_date, R.id.lf_cal_account, R.id.lf_cal_category, R.id.lf_cal_money};
                    final SimpleCursorAdapter adapter = new SimpleCursorAdapter(
                            listView.getContext(),
                            R.layout.calendar_day_list,
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
                        // 디테일한 정보 화면으로 넘어감

                    }
                });

*/

                String sql = "SELECT * FROM daymoney WHERE date LIKE ?";
                Cursor cursor = db.rawQuery(sql, new String[]{subDate});

                int recordCount = cursor.getCount();
                Log.d(TAG, "cursor count : " + recordCount + "\n");

                int dateCol = cursor.getColumnIndex("date");
                int accountCol = cursor.getColumnIndex("account");
                int categoryCol = cursor.getColumnIndex("category");
                int moneyCol = cursor.getColumnIndex("money");
                int contentCol = cursor.getColumnIndex("content");

                int count = 0;


                while (cursor.moveToNext()) {
                    String Insertdate = cursor.getString(dateCol);
                    String account = cursor.getString(accountCol);
                    String category = cursor.getString(categoryCol);
                    int money = cursor.getInt(moneyCol);
                    String content = cursor.getString(contentCol);

                    result = Insertdate +" / "+ account +" / "+ category +" / "+ money +" / "+ content;
                    arrListInsert.add(result);
                    count++;

                }
                Toast.makeText(getApplicationContext(), subDate+"", Toast.LENGTH_SHORT).show();
                // 커서가 나타낼 경우가 여러 가지일 때를 해결하셈 ( 같은 날에 여러 개의 가계 입력 )


                listViewInsert.setAdapter(adapterInsert);
            }

            @Override
            public void onChangeMonth(int month, int year) { // 월 옮겼을 때
                String text = "month: " + month + " year: " + year;
                Toast.makeText(getApplicationContext(), text,
                        Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onLongClickDate(Date date, View view) { // 특정 날짜 오래 클릭했을 때
                SimpleDateFormat formatter = new SimpleDateFormat("yyyy-mm-dd");
                Toast.makeText(getApplicationContext(),
                        "Long click " + formatter.format(date),
                        Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onCaldroidViewCreated() { // 캘린더 실행했을 때
                Toast.makeText(getApplicationContext(),
                        "Caldroid view is created",
                        Toast.LENGTH_SHORT).show();
            }

        };

        caldroidFragment.setCaldroidListener(listener);

        // Commit the transaction

        t.commit();
    }






    @Override
    public void onClick(View v) { }

}
