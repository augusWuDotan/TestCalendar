package com.wdtpr.testcalendar;

import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import com.roomorama.caldroid.CaldroidFragment;
import com.wdtpr.testcalendar.customerView.CaldroidSampleCustomFragment;

import java.text.SimpleDateFormat;
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

        if (caldroidFragment != null) {
            ColorDrawable blue = new ColorDrawable(getResources().getColor(R.color.blue));
            ColorDrawable green = new ColorDrawable(Color.GREEN);
            caldroidFragment.setBackgroundDrawableForDate(blue, blueDate);
            caldroidFragment.setBackgroundDrawableForDate(green, greenDate);
            caldroidFragment.setTextColorForDate(R.color.white, blueDate);
            caldroidFragment.setTextColorForDate(R.color.white, greenDate);

//            caldroidFragment.setMonthTitleTextView();
        }
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
            args.putInt(CaldroidFragment.THEME_RESOURCE, com.caldroid.R.style.CaldroidDefaultDark);

            caldroidFragment.setArguments(args);
        }

        setCustomResourceForDates();

        // Attach to the activity
        FragmentTransaction t = getSupportFragmentManager().beginTransaction();
        t.replace(R.id.calendar1, caldroidFragment);
        t.commit();

        LayoutInflater inflater = MainActivity.this.getLayoutInflater();
        View view = inflater.inflate(R.layout.date_text,null);
        TextView abc = (TextView)view.findViewById(R.id.abc);
        abc.setText("我是大笨蛋");
        caldroidFragment.setMonthTitleTextView(abc);

    }
}
