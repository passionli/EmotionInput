package com.xunlei.content.emotioninput;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

public class EmoticonViewPagerAdapter extends FragmentStatePagerAdapter {
        private static final int NUM_ITEMS = 3;
        EmoticonOnClickListener mListener;

        public EmoticonViewPagerAdapter(FragmentManager fm, EmoticonOnClickListener listener) {
            super(fm);
            mListener = listener;
        }

        @Override
        public int getCount() {
            return NUM_ITEMS;
        }

        @Override
        public Fragment getItem(int position) {
            return EmoticonListFragment.newInstance(position, mListener);
        }
    }