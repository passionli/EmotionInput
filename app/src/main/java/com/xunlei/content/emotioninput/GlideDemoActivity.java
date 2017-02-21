package com.xunlei.content.emotioninput;

import android.animation.ObjectAnimator;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;

import com.bumptech.glide.DrawableRequestBuilder;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.drawable.GlideDrawable;
import com.bumptech.glide.request.Request;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.animation.ViewPropertyAnimation;
import com.bumptech.glide.request.target.SimpleTarget;
import com.bumptech.glide.request.target.Target;

import butterknife.BindView;
import butterknife.ButterKnife;

public class GlideDemoActivity extends AppCompatActivity {
    private static final String TAG = "GlideDemoActivity";
    @BindView(R.id.imageView)
    ImageView mImageView;

    String mDisplayUrl;
    String mDownloadUrl;

    private SimpleTarget<Bitmap> target = new SimpleTarget<Bitmap>() {
        @Override
        public void onResourceReady(Bitmap resource, GlideAnimation<? super Bitmap> glideAnimation) {
            mImageView.setImageBitmap(resource);
        }

        @Override
        public void setRequest(Request request) {
            super.setRequest(request);
        }

        @Override
        public Request getRequest() {
            return super.getRequest();
        }

        @Override
        public void onLoadCleared(Drawable placeholder) {
            super.onLoadCleared(placeholder);
        }

        @Override
        public void onLoadStarted(Drawable placeholder) {
            super.onLoadStarted(placeholder);
        }

        @Override
        public void onLoadFailed(Exception e, Drawable errorDrawable) {
            super.onLoadFailed(e, errorDrawable);
        }

        @Override
        public void onStart() {
            super.onStart();
        }

        @Override
        public void onStop() {
            super.onStop();
        }

        @Override
        public void onDestroy() {
            super.onDestroy();
        }
    };

    private RequestListener<String, GlideDrawable> requestListener = new RequestListener<String, GlideDrawable>() {
        @Override
        public boolean onException(Exception e, String model, Target<GlideDrawable> target, boolean isFirstResource) {
            Log.d(TAG, "onException: " + e.toString());
            return false;
        }

        @Override
        public boolean onResourceReady(GlideDrawable resource, String model, Target<GlideDrawable> target, boolean isFromMemoryCache, boolean isFirstResource) {
            return false;
        }
    };

    ViewPropertyAnimation.Animator animationObject = new ViewPropertyAnimation.Animator(){
        @Override
        public void animate(View view) {
            view.setAlpha(0f);

            ObjectAnimator fadeAnim = ObjectAnimator.ofFloat(view, "alpha", 0f, 1f);
            fadeAnim.setDuration(2500);
            fadeAnim.start();
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_glide_demo);
        ButterKnife.bind(this);
        initData();
    }

    private void initData() {
        mDisplayUrl = "https://img-fans-ssl.xunlei.com/11578897141760.jpg";
//        mDisplayUrl = "https://img-fans-ssl.xunlei.com/XXOO.jpg";
        mDownloadUrl = "https://img-fans-ssl.xunlei.com/11578842279936.gif";
//        loadImageSimpleTarget();
        loadThumbnail();
    }

    private void loadThumbnail() {
        DrawableRequestBuilder<String> thumbnailRequest = Glide
                .with(this)
                .load(mDisplayUrl);

        Glide
                .with(this)
                .load(mDownloadUrl)
                .thumbnail(thumbnailRequest)
                .listener(requestListener)
                .transform(new BlurTransformation(this))
                .animate(android.R.anim.slide_in_left)
                .animate(animationObject)
                .into(mImageView);
    }

    private void loadImageSimpleTarget() {
        //如果下层需要在Activity Context之外去处理请求，
        // 那么需要传入更大的Context-ActivityContext的上级ApplicationContext
        Glide.with(getApplicationContext()).load(mDisplayUrl).asBitmap().into(target);
    }
}
