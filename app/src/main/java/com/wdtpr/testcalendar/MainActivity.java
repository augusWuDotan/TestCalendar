package com.wdtpr.testcalendar;

import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.roomorama.caldroid.CaldroidFragment;
import com.roomorama.caldroid.CaldroidListener;
import com.wdtpr.testcalendar.customerView.CaldroidSampleCustomFragment;

import java.text.ParsePosition;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

public class MainActivity extends AppCompatActivity {
    //
    private boolean undo = false;
    private CaldroidSampleCustomFragment caldroidFragment;
    private CaldroidFragment dialogCaldroidFragment;

    //
    private void setCustomResourceForDates() {
        Calendar cal = Calendar.getInstance();

        // Min date is last 7 days
        cal.add(Calendar.DATE, -7);
        Date blueDate = cal.getTime();

        // Max date is next 7 days
        cal = Calendar.getInstance();
        cal.add(Calendar.DATE, 7);
        Date greenDate = cal.getTime();

//        if (caldroidFragment != null) {
//                ColorDrawable blue = new ColorDrawable(getResources().getColor(R.color.blue));
//            ColorDrawable green = new ColorDrawable(Color.GREEN);
//            caldroidFragment.setBackgroundDrawableForDate(blue, blueDate);
//            caldroidFragment.setBackgroundDrawableForDate(green, greenDate);
//            caldroidFragment.setTextColorForDate(R.color.white, blueDate);
//            caldroidFragment.setTextColorForDate(R.color.white, greenDate);
//        }

        //
//        SimpleDateFormat sDateFormat = new SimpleDateFormat("yyyy-MM-dd");
//        String dateStr = String.format("2017-2-12");
//        ParsePosition pos = new ParsePosition(0);
//        Date date = sDateFormat.parse(dateStr, pos);
////        Calendar calendar = Calendar.getInstance();
////        calendar.setTime(date);
//        String week = new SimpleDateFormat("EEEE").format(date);
////        Log.d("test",week);
//        String weekdays[] = {"星期日"};
//
//        Log.d("test", isDisable(date, weekdays) + "");
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        final SimpleDateFormat formatter = new SimpleDateFormat("dd MMM yyyy");

        // Setup caldroid fragment
        // **** If you want normal CaldroidFragment, use below line ****
        caldroidFragment = new CaldroidSampleCustomFragment();

        // //////////////////////////////////////////////////////////////////////
        // **** This is to show customized fragment. If you want customized
        // version, uncomment below line ****
//		 caldroidFragment = new CaldroidSampleCustomFragment();

        // Setup arguments

        // If Activity is created after rotation
        if (savedInstanceState != null) {
            caldroidFragment.restoreStatesFromKey(savedInstanceState,
                    "CALDROID_SAVED_STATE");
        }
        // If activity is created from fresh
        else {
            Bundle args = new Bundle();
            Calendar cal = Calendar.getInstance();
            args.putInt(CaldroidFragment.MONTH, cal.get(Calendar.MONTH) + 1);
            args.putInt(CaldroidFragment.YEAR, cal.get(Calendar.YEAR));
            args.putBoolean(CaldroidFragment.ENABLE_SWIPE, true);
            args.putBoolean(CaldroidFragment.SIX_WEEKS_IN_CALENDAR, true);

            //將 周二 放在第一格 // Tuesday Uncomment this to customize startDayOfWeek
//            args.putInt(CaldroidFragment.START_DAY_OF_WEEK, CaldroidFragment.TUESDAY);

            //If you want to know when user clicks on disabled dates:
//            args.putBoolean(CaldroidFragment.ENABLE_CLICK_ON_DISABLED_DATES,false);

            // Uncomment this line to use Caldroid in compact mode 使用 文字區域
//             args.putBoolean(CaldroidFragment.SQUARE_TEXT_VIEW_CELL, true);

            // Uncomment this line to use dark theme 使用黑暗 模式
//            args.putInt(CaldroidFragment.THEME_RESOURCE, com.caldroid.R.style.CaldroidDefaultDark);

            caldroidFragment.setArguments(args);
        }

        setCustomResourceForDates();

        // Attach to the activity
        FragmentTransaction t = getSupportFragmentManager().beginTransaction();
        t.replace(R.id.calendar1, caldroidFragment);
        t.commit();

        // Setup listener
        final CaldroidListener listener = new CaldroidListener() {

            @Override
            public void onSelectDate(Date date, View view) {


                Toast.makeText(getApplicationContext(), formatter.format(date),
                        Toast.LENGTH_SHORT).show();


            }

            @Override
            public void onChangeMonth(int month, int year) {
                String text = "month: " + month + " year: " + year;
                Toast.makeText(getApplicationContext(), text,
                        Toast.LENGTH_SHORT).show();
                TextView textView = caldroidFragment.getMonthTitleTextView();
                textView.setText(year + "." + month);
                Log.d("Tag", textView.getText().toString());

                //變換月份時 就要依據 月份 排定禁用時間
                String []CompareWeekDays = {"星期日","星期六"};
                disableClickTimes(year,month,CompareWeekDays);

            }

            @Override
            public void onLongClickDate(Date date, View view) {
                Toast.makeText(getApplicationContext(),
                        "Long click " + formatter.format(date),
                        Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onCaldroidViewCreated() {
                if (caldroidFragment.getLeftArrowButton() != null) {
                    Toast.makeText(getApplicationContext(),
                            "Caldroid view is created", Toast.LENGTH_SHORT)
                            .show();
                }

//                Button leftButton = caldroidFragment.getLeftArrowButton();
//                Button rightButton = caldroidFragment.getRightArrowButton();
                TextView textView = caldroidFragment.getMonthTitleTextView();
                Log.d("Tag1", textView.getText().toString());
            }

        };

        // Setup Caldroid
        caldroidFragment.setCaldroidListener(listener);


    }

    /**
     * 建立禁止使用時間點
     *
     * @param year
     * @param month
     * @param CompareWeekDays 比對的[星期一至星期日]字串陣列
     */
    private void disableClickTimes(int year, int month, String[] CompareWeekDays) {
        //規則有 1.周一至周日哪一天是不給預約的
        //A.依據哪一個月份 month
        //B.找出月份的天數為[共有幾天]
        /**
         *  C.辨別這一個月的每一天 只要是禁用日就紀錄
         */
        //init
        String CalendarStr = String.format("%d-%d-", year, month);
        ArrayList<Date> disableDay_thisMonth = getDisableArray(CalendarStr, getDays(year,month), CompareWeekDays);
        CalendarStr = String.format("%d-%d-", month-1==0?year-1:year, month-1==0?12:month-1);
        ArrayList<Date> disableDay_lastMonth = getDisableArray(CalendarStr, getDays(month-1==0?year-1:year,month-1==0?12:month-1), CompareWeekDays);
        CalendarStr = String.format("%d-%d-", month+1==13?year+1:year,month+1==13?1:month+1);
        ArrayList<Date> disableDay_nextMonth = getDisableArray(CalendarStr, getDays(month+1==13?year+1:year,month+1==13?1:month+1), CompareWeekDays);

//        caldroidFragment.setDisableDates(disableDay);

        //上色
        ColorDrawable green = new ColorDrawable(getResources().getColor(R.color.grey));
        for (Date date : disableDay_thisMonth) {
            caldroidFragment.setBackgroundDrawableForDate(green, date);
            caldroidFragment.setTextColorForDate(R.color.black, date);
        }
        for (Date date : disableDay_lastMonth) {
            caldroidFragment.setBackgroundDrawableForDate(green, date);
            caldroidFragment.setTextColorForDate(R.color.black, date);
        }
        for (Date date : disableDay_nextMonth) {
            caldroidFragment.setBackgroundDrawableForDate(green, date);
            caldroidFragment.setTextColorForDate(R.color.black, date);
        }
//

//        caldroidFragment.refreshView();//刷新
    }

    //取得月份天數
    private int getDays(int year,int month){

        int maxday = 0;//取得最後一天的數字 就是最大日
        switch (month) {
            case 1:
            case 3:
            case 5:
            case 7:
            case 8:
            case 10:
            case 12:
                maxday = 31;
                break;
            case 4:
            case 6:
            case 9:
            case 11:
                maxday = 30;
                break;
            case 2:
                if ((year%4 == 0 && year%100 != 0) || (year%400 == 0)) {
                    maxday = 29;
                } else {
                    maxday = 28;
                }
                break;
            default:
                maxday = 0;
                break;
        }
        return maxday;
    }

    /**
     * 取得禁用日期陣列
     *
     * @param CalendarStr     組合過的字串 例如 2017-07-%s
     * @param maxday          該月的最大日
     * @param CompareWeekDays 比對的[星期一至星期日]字串陣列
     * @return 禁用的arrayList
     */
    private ArrayList<Date> getDisableArray(String CalendarStr, int maxday, String[] CompareWeekDays) {
        //
        ArrayList<Date> DateArrays = new ArrayList<>();
        //
        SimpleDateFormat sDateFormat = new SimpleDateFormat("yyyy-MM-dd");
        //
        for (int i = 0; i < maxday; i++) {
            String dateStr = String.format(CalendarStr+"%d", i + 1);
            ParsePosition pos = new ParsePosition(0);
            Date date = sDateFormat.parse(dateStr, pos);
            if (isDisable(date, CompareWeekDays)) {//比較是否為禁用
                //加入 禁用時間
                DateArrays.add(date);
                Log.d("test",date.toString());
            }

        }

        return DateArrays;
    }

    /**
     * 比對星期一至星期日
     *
     * @param date            日期
     * @param CompareWeekDays 比對的[周一-周日]字串陣列
     * @return 是否加入禁用日期
     */
    private boolean isDisable(Date date, String[] CompareWeekDays) {
        Boolean isDisable = false;
//        Calendar calendar = Calendar.getInstance();
//        calendar.setTime(date);
        String week = new SimpleDateFormat("EEEE").format(date);
        for (String str : CompareWeekDays) {
            if (week.equals(str)) {
                isDisable = true;
                break;
            }
        }
        return isDisable;
    }
}
