package com.xunlei.content.emotioninput;

import android.content.Context;
import android.util.Log;

import com.bumptech.glide.Glide;
import com.bumptech.glide.GlideBuilder;
import com.bumptech.glide.load.DecodeFormat;
import com.bumptech.glide.module.GlideModule;

public class SimpleGlideModule implements GlideModule {
    private static final String TAG = "SimpleGlideModule";

    @Override
    public void applyOptions(Context context, GlideBuilder builder) {
        Log.d(TAG, "applyOptions: 在这里可以给Glide设置核心组件");
        builder.setDecodeFormat(DecodeFormat.PREFER_ARGB_8888);
    }

    @Override
    public void registerComponents(Context context, Glide glide) {

    }
}
