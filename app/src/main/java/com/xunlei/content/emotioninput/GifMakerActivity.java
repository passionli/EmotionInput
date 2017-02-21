package com.xunlei.content.emotioninput;

import android.app.ProgressDialog;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.GlideBitmapDrawable;
import com.bumptech.glide.load.resource.gif.GifDrawable;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.OnTextChanged;

public class GifMakerActivity extends AppCompatActivity {
    private static final String TAG = "GifMakerActivity";

    @BindView(R.id.imageView)
    ImageView mImageView;
    @BindView(R.id.textView)
    TextView mTextView;
    @BindView(R.id.editText)
    EditText mEditText;
    @BindView(R.id.dstImageView)
    ImageView mDstImageView;
    @BindView(R.id.btn)
    Button mBtn;
    @BindView(R.id.textImage)
    ImageView mTextImage;


    //low level
    String mModel;
    private ProgressDialog mDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gif_maker);
        ButterKnife.bind(this);
        initData();
        initView();
    }

    private void initView() {
        mEditText.setText(R.string.emoticon_text);
        Glide.with(this).load(mModel).into(mImageView);
    }

    private void initData() {
        mModel = "https://img-fans-ssl.xunlei.com/11578842279936.gif";
        mModel = "https://img-fans-ssl.xunlei.com/11578897141760.jpg";
    }

    @OnTextChanged(R.id.editText)
    void onTextChanged(CharSequence text) {
        mTextView.setText(text);
    }

    @OnClick(R.id.btn)
    void encode(final View v) {
        Log.d(TAG, "onClick: ");
        final String text = mTextView.getText().toString();
        if (mModel.endsWith(".jpg")) {
            new MergeJpegTextTask().execute();
        } else if (mModel.endsWith(".gif")) {
            new AsyncTask<Object, Object, String>() {
                GifDrawable gifDrawable;
                Bitmap mTextBitmap;

                @Override
                protected void onPreExecute() {
                    mDialog = new ProgressDialog(GifMakerActivity.this);
                    mDialog.show();
                    Drawable drawable = mImageView.getDrawable();
                    if (drawable == null || !(drawable instanceof GifDrawable)) {
                        throw new RuntimeException("encodeGifText: drawable null or is not instance of GifDrawable");
                    }

//        imageView.setImageBitmap(null);

                    if (TextUtils.isEmpty(text)) {
                        throw new RuntimeException("text cannot be empty");
                    }

                    gifDrawable = (GifDrawable) drawable;
                    //这里可以：把current bitmap copy 一份，让ImageView显示一幅静态的图片
                    mImageView.setImageBitmap(null);
                    mTextBitmap = Bitmap.createBitmap(gifDrawable.getIntrinsicWidth(), gifDrawable.getIntrinsicHeight(), Bitmap.Config.ARGB_8888);
                    Canvas canvas = new Canvas(mTextBitmap);
                    mTextView.draw(canvas);
                    mTextImage.setImageBitmap(mTextBitmap);
                }

                @Override
                protected String doInBackground(Object... params) {
                    return LGUtils.encodeGifText(mModel, gifDrawable, mTextBitmap, text);
                }

                @Override
                protected void onPostExecute(String url) {
                    Log.d(TAG, "onPostExecute: url " + url);
                    mDialog.dismiss();
                }
            }.execute();
        }
    }

    private class MergeJpegTextTask extends AsyncTask<Void, Void, String> {
        Bitmap mBg;
        Bitmap mFg;
        String mText;
        Bitmap mParentBitmap;

        @Override
        protected void onPreExecute() {
            mDialog = new ProgressDialog(GifMakerActivity.this);
            mDialog.show();
//            GlideBitmapDrawable glideBitmapDrawable = (GlideBitmapDrawable) mImageView.getDrawable();
//            mBg = glideBitmapDrawable.getBitmap();
//
//            mFg = Bitmap.createBitmap(mTextView.getWidth(), mTextView.getHeight(), Bitmap.Config.ARGB_8888);
//            Canvas canvas = new Canvas(mFg);
//            mTextView.draw(canvas);
            mText = mTextView.getText().toString();
//            mTextImage.setImageBitmap(mFg);

            ViewGroup viewGroup = (ViewGroup) mImageView.getParent();
            mParentBitmap = Bitmap.createBitmap(viewGroup.getWidth(),viewGroup.getHeight(), Bitmap.Config.ARGB_8888);
            Canvas c = new Canvas(mParentBitmap);
            //输出JPEG格式的图片时，需要把画布填充成白色，不然背景会是黑色
            c.drawColor(Color.WHITE);
            //让父亲节点执行一次draw就可以把子View合成到bitmap中
            viewGroup.draw(c);
            mDstImageView.setImageBitmap(mParentBitmap);
        }

        @Override
        protected String doInBackground(Void... params) {
            return LGUtils.saveBitmap2File(mParentBitmap, mModel, mText);
        }

        @Override
        protected void onPostExecute(String url) {
            reloadData(url);
            mDialog.dismiss();
        }
    }

    private void reloadData(String url) {
        Glide.with(GifMakerActivity.this).load(url).into(mDstImageView);
        Glide.with(GifMakerActivity.this).load(mModel).into(mImageView);
    }
}
