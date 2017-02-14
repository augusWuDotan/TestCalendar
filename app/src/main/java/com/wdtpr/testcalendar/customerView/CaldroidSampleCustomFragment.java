package com.wdtpr.testcalendar.customerView;

import com.roomorama.caldroid.CaldroidFragment;
import com.roomorama.caldroid.CaldroidGridAdapter;


/**
 * Created by User on 2017/2/14.
 */
public class CaldroidSampleCustomFragment  extends CaldroidFragment {


    @Override
    public CaldroidGridAdapter getNewDatesGridAdapter(int month, int year) {
        return new CaldroidSampleCustomAdapter(getActivity(),month,year,getCaldroidData(),extraData);
    }


}
