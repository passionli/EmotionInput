package com.xunlei.content.emotioninput;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.ImageSpan;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class MainActivity extends AppCompatActivity{
    private static final String TAG = "MainActivity";
    @BindView(R.id.et)
    EditText mEt;
    @BindView(R.id.btn)
    Button mBtn;
    @BindView(R.id.content)
    FrameLayout mContentView;
    @BindView(R.id.emotionView)
    EmoticonView mEmotionView;
    @BindView(R.id.sendBtn)
    Button mSendBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        initView();
    }

    private void initView() {
        mEmotionView.with(this)
                .bindContent(mContentView)
                .bindEditText(mEt)
                .bindEmotionButton(mBtn).setup();

        //receiver 输入框 / 消息列表
//        mEt.addTextChangedListener(new TextWatcher() {
//            CharSequence mLastText;
//
//            @Override
//            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
//                mLastText = s;
//            }
//
//            @Override
//            public void onTextChanged(CharSequence s, int start, int before, int count) {
//
//            }
//
//            @Override
//            public void afterTextChanged(Editable s) {
//                Log.d(TAG, "afterTextChanged: " + s.toString());
//                //TODO decode data
//            }
//        });
//        mEt.setOnDragListener(new View.OnDragListener() {
//            @Override
//            public boolean onDrag(View v, DragEvent event) {
//                Log.d(TAG, "onDrag: " + event);
//                switch (event.getAction()){
//                    case DragEvent.ACTION_DROP:
//                        ClipData  clipData = event.getClipData();
//                        ClipData.Item item = clipData.getItemAt(0);
//                        String emotionKey = item.getText().toString();
//                        Log.d(TAG, "onDrag: emotionKey " + emotionKey);
//                        break;
//                }
//                return true;
//            }
//        });

        //TODO 这个应该在加载完数据后设置
//        mEmotion1ImageView.setTag(R.string.emotion_drag_key, "[xl_nr_emotion_11]");
//        mEmotion2ImageView.setTag(R.string.emotion_drag_key, "[xl_nr_emotion_22]");
//
//        mEmotion1ImageView.setOnLongClickListener(mEmotionLongClickListener);
//        mEmotion2ImageView.setOnLongClickListener(mEmotionLongClickListener);
    }

    @OnClick(R.id.btn)
    void getData() {
//        getSoftInputHeight();
//        lockContentHeight();
    }



    @OnClick(R.id.sendBtn)
    void submit() {
        String data = mEt.getText().toString();
        // pass data to low level
        Log.d(TAG, "submit: " + data);
    }


}
