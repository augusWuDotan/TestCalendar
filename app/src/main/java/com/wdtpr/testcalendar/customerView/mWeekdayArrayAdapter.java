package com.wdtpr.testcalendar.customerView;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;

import com.roomorama.caldroid.WeekdayArrayAdapter;

import java.util.List;

/**
 * Created by User on 2017/2/15.
 */
public class mWeekdayArrayAdapter extends WeekdayArrayAdapter {
    public mWeekdayArrayAdapter(Context context, int textViewResourceId, List<String> objects, int themeResource) {
        super(context, textViewResourceId, objects, themeResource);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        return super.getView(position, convertView, parent);
    }
}
