package ir.mirrajabi.persiancalendar.core.adapters;

import android.app.Fragment;
import android.app.FragmentManager;
import android.os.Bundle;


import ir.mirrajabi.persiancalendar.core.Constants;
import ir.mirrajabi.persiancalendar.core.fragments.MonthFragment;

//public class CalendarAdapter extends FragmentStatePagerAdapter {
//
//    public CalendarAdapter(FragmentManager fm) {
//        super(fm);
//    }
//
//    @Override
//    public Fragment getItem(int position) {
//        MonthFragment fragment = new MonthFragment();
//        Bundle bundle = new Bundle();
//        bundle.putInt(Constants.OFFSET_ARGUMENT, position - Constants.MONTHS_LIMIT / 2);
//        fragment.setArguments(bundle);
//        return fragment;
//    }
//
//    @Override
//    public int getCount() {
//        return Constants.MONTHS_LIMIT;
//    }
//}
