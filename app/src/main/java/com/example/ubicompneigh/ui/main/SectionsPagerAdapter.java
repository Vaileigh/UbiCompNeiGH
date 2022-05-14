package com.example.ubicompneigh.ui.main;

import android.content.Context;

import androidx.annotation.Nullable;
import androidx.annotation.StringRes;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import com.example.ubicompneigh.R;

import java.util.ArrayList;
import java.util.List;

/**
 * A [FragmentPagerAdapter] that returns a fragment corresponding to
 * one of the sections/tabs/pages.
 */
public class SectionsPagerAdapter extends FragmentPagerAdapter {

    @StringRes
    private static final int[] TAB_TITLES = new int[]{R.string.tab_text_1, R.string.tab_text_2};
    private final Context mContext;
    private final ArrayList<Lists> arrayList;
    private final ArrayList<Lists> notificationList;

    public SectionsPagerAdapter(Context context, FragmentManager fm, ArrayList<Lists> arrayList, ArrayList<Lists> notificationList) {
        super(fm);
        mContext = context;
        this.arrayList = arrayList;
        this.notificationList = notificationList;
    }

    @Override
    public Fragment getItem(int position) {
        // getItem is called to instantiate the fragment for the given page.
        // Return a PlaceholderFragment (defined as a static inner class below).
        switch (position){
            case 1:
                return PlaceholderFragment.newInstance(position + 1, arrayList);
            case 0:
                return NotificationFragment.newInstance(position + 1, notificationList);
        }

        return PlaceholderFragment.newInstance(position + 1, arrayList);
    }

    @Nullable
    @Override
    public CharSequence getPageTitle(int position) {
        return mContext.getResources().getString(TAB_TITLES[position]);
    }

    @Override
    public int getCount() {
        // Show 2 total pages.
        return 2;
    }

    public void clear(){
        arrayList.clear();
        notificationList.clear();
    }
}