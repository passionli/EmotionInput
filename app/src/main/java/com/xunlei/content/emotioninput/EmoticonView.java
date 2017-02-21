package com.xunlei.content.emotioninput;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.TextWatcher;
import android.text.style.ImageSpan;
import android.util.AttributeSet;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import hugo.weaving.DebugLog;

public class EmoticonView extends FrameLayout implements ViewPager.OnPageChangeListener {
    private static final String TAG = "EmoticonView";
    ViewPager mViewPager;
    LinearLayout mIndicator;

    PagerAdapter mPagerAdapter;
    private AppCompatActivity mActivity;
    private EditText mEditText;
    /**
     * 上层向下层注册的表情被点击的回调
     */
    EmoticonOnClickListener mEmoticonOnClickListener = new EmoticonOnClickListener() {
        @Override
        public void onClick(EmoticonBean bean) {
            Log.d(TAG, "onClick: " + bean.toString());
            handleEmoticonClick(bean);
        }
    };
    private View mContentView;
    private View mEmoticonBtn;
    private boolean mFirstTime = true;

    private TextWatcher mTextWatcher = new TextWatcher() {
        boolean mSelfChange;

        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            if (mSelfChange)
                return;
        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {

        }

        @Override
        public void afterTextChanged(Editable s) {
            if (mSelfChange)
                return;
            mSelfChange = true;
//            decodeEmotionContent();
            mSelfChange = false;
        }
    };

    public EmoticonView(Context context) {
        super(context);
    }

    public EmoticonView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    private void init() {
        LayoutInflater.from(getContext()).inflate(R.layout.emoticon_content, this);
        addViewPager();
        addIndicator();
    }

    private void addViewPager() {
//        mViewPager = new ViewPager(getContext());
//        mViewPager.setId(R.id.emoticon_viewPager);
        mViewPager = (ViewPager) findViewById(R.id.viewPager);
//        ViewGroup.LayoutParams lp = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
//        mViewPager.setLayoutParams(lp);
        mViewPager.setOnPageChangeListener(this);
//        addView(mViewPager);
    }

    private void addIndicator() {
        mIndicator = (LinearLayout) findViewById(R.id.indicator);
//        mIndicator = new LinearLayout(getContext());
//        mIndicator.setOrientation(LinearLayout.HORIZONTAL);
//        mIndicator.setGravity(Gravity.CENTER_VERTICAL);
//        TODO config height
//        LayoutParams lp = new LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, 128);
//        lp.gravity = Gravity.BOTTOM;
//        addView(mIndicator, lp);
    }

    public EmoticonView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @DebugLog
    @Override
    public void onPageScrolled(final int position, float positionOffset, int positionOffsetPixels) {
        if (positionOffset == 0.0 && mFirstTime) {
            mFirstTime = false;
            Log.d(TAG, "onPageScrolled: offset 0.0");
            //Window可能还没ready，所以放到ViewRootImpl的队列中延后执行
            post(new Runnable() {
                @Override
                public void run() {
                    updateIndicator(position);
                }
            });
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
                child = new ImageView(getContext());
                LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.MATCH_PARENT);
                lp.setMargins(5, 0, 5, 0);
                lp.gravity = Gravity.CENTER_VERTICAL;
                mIndicator.addView(child, lp);
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

    public EmoticonView with(AppCompatActivity activity) {
        mActivity = activity;
        return this;
    }


    public void setup() {
        mPagerAdapter = new EmoticonViewPagerAdapter(mActivity.getSupportFragmentManager(), mEmoticonOnClickListener);
        mViewPager.setAdapter(mPagerAdapter);
//        mEditText.addTextChangedListener(mTextWatcher);
        EmotionKeyboard
                .with(mActivity)
                .bindToContent(mContentView)
                .bindToEditText(mEditText)
                .bindToEmotionButton(mEmoticonBtn)
                .setEmotionView(this).build();
    }

    private void handleEmoticonClick(EmoticonBean bean) {
        //insert emotion key data
        int insertPos = mEditText.getSelectionStart();
        //插入一段表情Key的数据，可能在字符数组的前、中、后。
        String key = bean.id;
        mEditText.getEditableText().insert(insertPos, key);
        //TODO Glide
        ImageSpan span = new ImageSpan(getContext(), EmotionData.getEmotion(key));
        SpannableString spannableString = new SpannableString(mEditText.getText());
        spannableString.setSpan(span, insertPos, insertPos + key.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        mEditText.setText(spannableString);
        mEditText.setSelection(insertPos + key.length());
    }

    public EmoticonView bindContent(View contentView) {
        mContentView = contentView;
        return this;
    }

    public EmoticonView bindEditText(EditText editText) {
        mEditText = editText;
        return this;
    }

    public EmoticonView bindEmotionButton(View emoticonBtn) {
        mEmoticonBtn = emoticonBtn;
        return this;
    }

    public void decodeEmotionContent() {
        SpannableString spannableString = new SpannableString(mEditText.getText());
        Resources res = getResources();
        String regexEmotion = "\\[([xl_nr_emotion_\\d])+\\]";
        Pattern patternEmotion = Pattern.compile(regexEmotion);
        Matcher matcherEmotion = patternEmotion.matcher(spannableString);
        while (matcherEmotion.find()) {
            // 获取匹配到的具体字符
            String key = matcherEmotion.group();
            // 匹配字符串的开始位置
            int start = matcherEmotion.start();
            // 利用表情名字获取到对应的图片
            Log.d(TAG, "decodeEmotionContent: key " + key);
            Integer imgRes = EmotionData.getEmotion(key);
//            Integer imgRes = R.mipmap.emotion;
            if (imgRes != null) {
                // 压缩表情图片
                int size = (int) mEditText.getTextSize() * 13 / 10;
                //TODO 这里应该直接把点击的表情图像内存数据 cp 过来，而不是主线程一直decode
                Bitmap bitmap = BitmapFactory.decodeResource(res, imgRes);
                Bitmap scaleBitmap = Bitmap.createScaledBitmap(bitmap, size, size, true);
                // get bitmap from glide imageview?
                ImageSpan span = new ImageSpan(getContext(), scaleBitmap);
                spannableString.setSpan(span, start, start + key.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
            }
        }
        mEditText.setText(spannableString);
    }
}
