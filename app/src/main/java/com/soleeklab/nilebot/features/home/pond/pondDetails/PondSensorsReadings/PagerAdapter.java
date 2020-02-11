package com.soleeklab.nilebot.features.home.pond.pondDetails.PondSensorsReadings;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;

import com.soleeklab.nilebot.features.home.pond.pondDetails.PondSensorsReadings.chart.ChartFragment;


public class PagerAdapter extends FragmentStatePagerAdapter {
    int mNumOfTabs;

    public PagerAdapter(FragmentManager fm, int NumOfTabs) {
        super(fm);
        this.mNumOfTabs = NumOfTabs;
    }

    @Override
    public Fragment getItem(int position) {

        switch (position) {
            case 0:
                PondSensorsReadingsFragment tab1 = new PondSensorsReadingsFragment();
                return tab1;
            case 1:
                ChartFragment tab2 = new ChartFragment();
                return tab2;
            case 2:
                SummaryFragment tab3 = new SummaryFragment();
                return tab3;
            default:
                return null;
        }
    }

    @Override
    public int getCount() {
        return mNumOfTabs;
    }
}
