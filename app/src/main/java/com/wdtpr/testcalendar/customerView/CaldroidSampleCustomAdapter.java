package com.wdtpr.testcalendar.customerView;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.roomorama.caldroid.CaldroidFragment;
import com.roomorama.caldroid.CaldroidGridAdapter;
import com.wdtpr.testcalendar.R;

import java.util.Map;

import hirondelle.date4j.DateTime;

/**
 * Created by User on 2017/2/14.
 */
public class CaldroidSampleCustomAdapter extends CaldroidGridAdapter {

    //
    LayoutInflater inflater;

    /**
     * Constructor
     *
     * @param context
     * @param month
     * @param year
     * @param caldroidData
     * @param extraData
     */
    public CaldroidSampleCustomAdapter(Context context, int month, int year, Map<String, Object> caldroidData, Map<String, Object> extraData) {
        super(context, month, year, caldroidData, extraData);
        inflater = LayoutInflater.from(context);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {


        //
        ViewTag tag = null;
        //
        if (convertView == null) {
            convertView = inflater.inflate(R.layout.custom_cell, null);
            convertView.setMinimumHeight(120);
            tag = new ViewTag();
            tag.tv1 = (TextView) convertView.findViewById(R.id.tv1);
            tag.tv2 = (TextView) convertView.findViewById(R.id.tv2);
            convertView.setTag(tag);
        } else {
            tag = (ViewTag)convertView.getTag();
        }

        int topPadding = convertView.getPaddingTop();
        int leftPadding = convertView.getPaddingLeft();
        int bottomPadding = convertView.getPaddingBottom();
        int rightPadding = convertView.getPaddingRight();

        tag.tv1.setTextColor(Color.BLACK);

        // Get dateTime of this cell
        DateTime dateTime = this.datetimeList.get(position);
        Resources resources = context.getResources();

        //
        // Set color of the dates in previous / next month
        if (dateTime.getMonth() != month) {
            tag.tv1.setTextColor(resources
                    .getColor(com.caldroid.R.color.caldroid_darker_gray));
        }

        boolean shouldResetDiabledView = false;
        boolean shouldResetSelectedView = false;

        // Customize for disabled dates and date outside min/max dates
        if ((minDateTime != null && dateTime.lt(minDateTime))
                || (maxDateTime != null && dateTime.gt(maxDateTime))
                || (disableDates != null && disableDates.indexOf(dateTime) != -1)) {

            tag.tv1.setTextColor(CaldroidFragment.disabledTextColor);
            if (CaldroidFragment.disabledBackgroundDrawable == -1) {
                convertView.setBackgroundResource(com.caldroid.R.drawable.disable_cell);
            } else {
                convertView.setBackgroundResource(CaldroidFragment.disabledBackgroundDrawable);
            }

            if (dateTime.equals(getToday())) {
                convertView.setBackgroundResource(com.caldroid.R.drawable.red_border_gray_bg);
            }

        } else {
            shouldResetDiabledView = true;
        }

        // Customize for selected dates
        if (selectedDates != null && selectedDates.indexOf(dateTime) != -1) {
            convertView.setBackgroundColor(resources
                    .getColor(com.caldroid.R.color.caldroid_sky_blue));

            tag.tv1.setTextColor(Color.BLACK);

        } else {
            shouldResetSelectedView = true;
        }

        if (shouldResetDiabledView && shouldResetSelectedView) {
            // Customize for today
            if (dateTime.equals(getToday())) {
                convertView.setBackgroundResource(com.caldroid.R.drawable.red_border);
            } else {
                convertView.setBackgroundResource(com.caldroid.R.drawable.cell_bg);
            }
        }

        tag.tv1.setText("" + dateTime.getDay());
        tag.tv2.setText("Hi");

        // Somehow after setBackgroundResource, the padding collapse.
        // This is to recover the padding
        convertView.setPadding(leftPadding, topPadding, rightPadding,
                bottomPadding);

        // Set custom color if required
        setCustomResources(dateTime, convertView, tag.tv1);


        return convertView;
    }

    public class ViewTag {
        //
        TextView tv1;
        TextView tv2;
    }
}
