package com.xunlei.content.emotioninput;

import android.os.Bundle;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import hugo.weaving.DebugLog;

public class Main2Activity extends AppCompatActivity implements ViewPager.OnPageChangeListener {
    private static final String TAG = "Main2Activity";
    //High Level
    @BindView(R.id.viewPager)
    ViewPager mViewPager;
    PagerAdapter mPagerAdapter;
    @BindView(R.id.indicator)
    LinearLayout mIndicator;

    //Low Level
    int mEmotionSum = 50;
    int mPageSize = 20;
    List<String> mData = new ArrayList<>();

    /**
     * 上层向下层注册的表情被点击的回调
     */
    EmoticonOnClickListener mEmoticonOnClickListener = new EmoticonOnClickListener() {
        @Override
        public void onClick(EmoticonBean o) {
            Log.d(TAG, "onClick: " + o.toString());
        }
    };
    private boolean mFirstTime = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);
        ButterKnife.bind(this);
        mPagerAdapter = new EmoticonViewPagerAdapter(getSupportFragmentManager(), mEmoticonOnClickListener);
        mViewPager.setAdapter(mPagerAdapter);
        mViewPager.setOnPageChangeListener(this);
    }

    @DebugLog
    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
        if (positionOffset == 0.0 && mFirstTime) {
            mFirstTime = false;
            Log.d(TAG, "onPageScrolled: offset 0.0");
            updateIndicator(position);
        }
    }

    @DebugLog
    @Override
    public void onPageSelected(int position) {
        updateIndicator(position);
    }

    private void updateIndicator(int position) {
        int count = mPagerAdapter.getCount();
        for (int i = 0; i < count; i++) {
            ImageView child = (ImageView) mIndicator.getChildAt(i);
            if (child == null) {
                child = new ImageView(Main2Activity.this);
                LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.MATCH_PARENT);
                lp.setMargins(5, 0, 5, 0);
                child.setLayoutParams(lp);
                mIndicator.addView(child);
            }
            if (i == position) {
                child.setImageResource(R.mipmap.selected_dot);
            } else {
                child.setImageResource(R.mipmap.no_selected_dot);
            }
        }
    }

    @DebugLog
    @Override
    public void onPageScrollStateChanged(int state) {
    }


}
