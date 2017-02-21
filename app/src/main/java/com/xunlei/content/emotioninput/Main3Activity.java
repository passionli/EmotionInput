package com.xunlei.content.emotioninput;

import android.animation.Animator;
import android.animation.ObjectAnimator;
import android.animation.PropertyValuesHolder;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.GlideDrawableImageViewTarget;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnLongClick;
import butterknife.OnTouch;

public class Main3Activity extends AppCompatActivity {
    private static final String TAG = "Main3Activity";
    public static final int EMOTICON_TAG_KEY = 128;
    @BindView(R.id.container)
    FrameLayout mContainer;
    @BindView(R.id.content)
    LinearLayout mContentView;
    @BindView(R.id.imageView)
    ImageView mImageView;
    private ImageView mPopUpView;
    private float mX;
    private float mY;

    private float mInitX;
    private float mInitY;

    private EmoticonListener mListener = new EmoticonListener() {
        @Override
        public void onEmoticonDrop(View view, final View childView) {
            Log.d(TAG, "onEmoticonDrop: " + view.toString());
            Log.d(TAG, "onEmoticonDrop: share to " + childView.toString());
            runOnUiThread(new Runnable() {
                int count = 10;

                @Override
                public void run() {
                    childView.setVisibility(childView.getVisibility() == View.VISIBLE ? View.INVISIBLE : View.VISIBLE);
                    count--;
                    if (count > 0)
                        getWindow().getDecorView().postDelayed(this, 80);
                }
            });
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main3);
        ButterKnife.bind(this);
        GlideDrawableImageViewTarget imageViewTarget = new GlideDrawableImageViewTarget(mImageView);
        Glide.with(this)
//                .load("https://img-fans-ssl.xunlei.com/11578842279936.gif")
                .load("https://img-fans-ssl.xunlei.com/11578842275840.jpg")
                .placeholder(R.mipmap.emoticon_default_img)
                .into(imageViewTarget);
    }

    @OnTouch(R.id.imageView)
    boolean onTouch(View view, MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_MOVE:
                mX = event.getX();
                mY = event.getY();
                if (mPopUpView != null) {
                    Log.d(TAG, "onTouch: " + mX);
                    Log.d(TAG, "onTouch: " + mY);
//                    mPopUpView.setTranslationX(mX - mPopUpView.getMeasuredWidth() / 2 - mContainer.getPaddingLeft());
//                    mPopUpView.setTranslationY(mY - 120 - mPopUpView.getHeight() - mContainer.getPaddingTop());
                    mPopUpView.setTranslationX(mImageView.getLeft() + mX - 100 / 2 - mContainer.getPaddingLeft());
                    mPopUpView.setTranslationY((mImageView.getTop() + mY) - 100 - 150 - mContainer.getPaddingTop());
                }
                break;
            case MotionEvent.ACTION_UP:
            case MotionEvent.ACTION_CANCEL:
                //check position in content view area or not
                //dismiss or 彈性還原
                handleActionUpOrCancel();
                break;
        }
        return false;
    }

    private void handleActionUpOrCancel() {
        if (mPopUpView == null)
            return;
        View child = checkInContentView(mPopUpView, mContentView);
        if (child != null) {
            //callback data to high level observer
            //change UI firstly
            mContainer.removeView(mPopUpView);
            mListener.onEmoticonDrop(mPopUpView, child);
        } else {
            //go back to initial position
            resetPopupView();
        }
    }

    /**
     * 弹性滑动
     */
    private void resetPopupView() {
        PropertyValuesHolder pvhX = PropertyValuesHolder.ofFloat("translationX", mInitX);
        PropertyValuesHolder pvhY = PropertyValuesHolder.ofFloat("translationY", mInitY);
        ObjectAnimator animator = ObjectAnimator.ofPropertyValuesHolder(mPopUpView, pvhX, pvhY).setDuration(500);
        animator.setInterpolator(new AccelerateDecelerateInterpolator());
        animator.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {

            }

            @Override
            public void onAnimationEnd(Animator animation) {
                mContainer.removeView(mPopUpView);
                mPopUpView = null;
            }

            @Override
            public void onAnimationCancel(Animator animation) {

            }

            @Override
            public void onAnimationRepeat(Animator animation) {

            }
        });
        animator.start();
    }

    /**
     * 查找一个View的中心落在另一个View的哪个孩子节点
     *
     * @param imageView
     * @param contentView
     * @return
     */
    private View checkInContentView(View imageView, ViewGroup contentView) {
        //center point
        int centerX = (int) (imageView.getX() + imageView.getWidth() / 2);
        int centerY = (int) (imageView.getY() + imageView.getHeight() / 2);

        ViewGroup parent = contentView;
        for (int i = 0; i < parent.getChildCount(); i++) {
            View child = parent.getChildAt(i);
            if (centerX > child.getLeft() && centerX < child.getRight()
                    && centerY > child.getTop() && centerY < child.getBottom()) {
                return child;
            }
        }

        return null;
    }

    @OnLongClick(R.id.imageView)
    boolean onEmoticonLongClick(View view) {
        Log.d(TAG, "onEmoticonLongClick: " + view.toString());
        String data = "https://img-fans-ssl.xunlei.com/11578842279936.gif";
        int width = 100;
        int height = 100;
//        if (mPopUpView == null) {
        mPopUpView = new ImageView(this);
        FrameLayout.LayoutParams lp = new FrameLayout.LayoutParams(width, height);
//            lp.leftMargin = (int) (mX - mImageView.getDrawable().getIntrinsicWidth() / 2);
//            lp.topMargin = (int) (mY - mImageView.getDrawable().getIntrinsicHeight());
        mContainer.addView(mPopUpView, lp);
//        }

//        mPopUpView.setTranslationX(mImageView.getLeft() + mImageView.getWidth()/2 - width/2 - mContainer.getPaddingLeft());
        mInitX = mImageView.getLeft() + mX - width / 2 - mContainer.getPaddingLeft();
        mPopUpView.setTranslationX(mInitX);
        int margin = 150;
//        mPopUpView.setTranslationY(mImageView.getTop() - height - margin - mContainer.getPaddingTop());
        mInitY = (mImageView.getTop() + mY) - height - margin - mContainer.getPaddingTop();
        mPopUpView.setTranslationY(mInitY);
        //携带一块数据
        mPopUpView.setTag(R.string.emotion_drag_key, data);

        Glide.with(this)
                .load(data)
                .placeholder(R.mipmap.emoticon_default_img)
                .into(mPopUpView);
        return true;
    }


    interface EmoticonListener {
        /**
         * @param view      表情View
         * @param childView 接收表情的View
         */
        void onEmoticonDrop(View view, View childView);
    }
}
