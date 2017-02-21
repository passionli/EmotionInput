package com.xunlei.content.emotioninput;

import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.Html;
import android.text.Layout;
import android.text.Selection;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.TextPaint;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.text.style.ForegroundColorSpan;
import android.text.style.StyleSpan;
import android.text.util.Linkify;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.zzhoujay.richtext.LinkHolder;
import com.zzhoujay.richtext.RichText;
import com.zzhoujay.richtext.callback.LinkFixCallback;
import com.zzhoujay.richtext.callback.OnImageClickListener;
import com.zzhoujay.richtext.callback.OnImageLongClickListener;
import com.zzhoujay.richtext.callback.OnUrlClickListener;
import com.zzhoujay.richtext.callback.OnUrlLongClickListener;

import org.xml.sax.XMLReader;

import java.util.List;
import java.util.regex.Pattern;

import butterknife.BindView;
import butterknife.ButterKnife;

public class RichTextActivity extends AppCompatActivity {
    private static final String TAG = "RichTextActivity";

    @BindView(R.id.textView)
    TextView mTextView;

    String mModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rich_text);
        initData();
        initView();
//        setUp();

        String bean = "@李光: #话题1# #话题2# 内容 @UserName ";
        mTextView.setText(bean);
        Linkify.addLinks(mTextView, Pattern.compile("@[\\u4e00-\\u9fa5a-zA-Z0-9_-]{2,30}"), "xl://profile?uid=");
        Linkify.addLinks(mTextView, Pattern.compile("#[^#]+#"), "xl://profile?topic=");
        mTextView.setMovementMethod(LinkMovementMethod.getInstance());
    }

    private void setUp() {
//        SpannableString spannableString = new SpannableString(mModel);

//        spannableString.setSpan(clickableSpan, 0, spannableString.length(), Spanned.SPAN_INCLUSIVE_EXCLUSIVE);
//        mTextView.setText(Html.fromHtml(mModel));
//        mTextView.setMovementMethod(LinkMovementMethod.getInstance());
//        mTextView.setText(spannableString);
//        mTextView.setMovementMethod(LinkMovementMethod.getInstance());

//        mTextView.setText(mModel);
//        Linkify.addLinks(mTextView, Pattern.compile("@(\\w+?)(?=\\W|$)(.)"), "mxn://profile?uid=");

        String html = "<strong>我的测试</strong><img src=\"http://tp1.sinaimg.cn/2668435432/180/5636292734/0\">";
        CharSequence charSequence = Html.fromHtml(html, new Html.ImageGetter() {
            @Override
            public Drawable getDrawable(String source) {
                Drawable drawable = getResources().getDrawable(R.mipmap.emotion);
                drawable.setBounds(0, 0, drawable.getIntrinsicWidth(), drawable.getIntrinsicHeight());
                return drawable;
//                Drawable drawable = null;
//                URL url;
//                try {
//                    url = new URL(source);
//                    drawable = Drawable.createFromStream(url.openStream(), "");  //获取网路图片
//                } catch (Exception e) {
//                    return null;
//                }
//                drawable.setBounds(0, 0, drawable.getIntrinsicWidth(), drawable
//                        .getIntrinsicHeight());
//                return drawable;
            }
        }, new Html.TagHandler() {
            @Override
            public void handleTag(boolean opening, String tag, Editable output, XMLReader xmlReader) {

            }
        });
//        mTextView.setText(charSequence);

        RichText.from(mModel).urlClick(new OnUrlClickListener() {
            @Override
            public boolean urlClicked(String url) {
                Log.d(TAG, "urlClicked: " + url);
                return false;
            }
        }).urlLongClick(new OnUrlLongClickListener() {
            @Override
            public boolean urlLongClick(String url) {
                Log.d(TAG, "urlLongClick: " + url);
                return false;
            }
        }).imageClick(new OnImageClickListener() {
            @Override
            public void imageClicked(List<String> imageUrls, int position) {
                Log.d(TAG, "imageClicked: " + imageUrls);
            }
        }).imageLongClick(new OnImageLongClickListener() {
            @Override
            public boolean imageLongClicked(List<String> imageUrls, int position) {
                Log.d(TAG, "imageLongClicked: " +  imageUrls);
                return false;
            }
        }).linkFix(new LinkFixCallback() {
            @Override
            public void fix(LinkHolder holder) {
                holder.setUnderLine(false);
                holder.setColor(Color.RED);
            }
        }).into(mTextView);

        String linkUrl = "http://www.baidu.com";
//        SpannableStringBuilder spannableStringBuilder = new SpannableStringBuilder();
//        spannableStringBuilder.setSpan();
//
//        SpannableStringBuilder longDescription = new SpannableStringBuilder();
//
//        longDescription.append(bean);
//
//        longDescription.append("First Part Not Bold ");
//
//        int start = longDescription.length();
//        longDescription.append("BOLD");
//        longDescription.setSpan(new ForegroundColorSpan(0xFFCC5500), start, longDescription.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
//        longDescription.setSpan(new StyleSpan(android.graphics.Typeface.BOLD), start, longDescription.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
//
//        longDescription.append(" rest not bold");
//
//        ClickableSpan clickableSpan = new NoLineClickSpan("@李光", new NoLineClickSpan.Callback() {
//            @Override
//            public void onClick(View widget, String text) {
//                Toast.makeText(RichTextActivity.this, text, Toast.LENGTH_SHORT).show();
//            }
//        });
////        longDescription.setSpan(clickableSpan, 0, "@李光".length(),Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
//        longDescription.setSpan(new ForegroundColorSpan(Color.BLUE), 0, "@李光".length(),Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
//
//        mTextView.setText(longDescription);
//
//        mTextView.setMovementMethod(new LinkTouchMovementMethod());
    }

    private void initView() {
        ButterKnife.bind(this);
    }

    private void initData() {
        mModel = "<strong>我的测试</strong><br/><img src=\"http://tp1.sinaimg.cn/2668435432/180/5636292734/0\"> " +
                "打造酷炫 AndroidStudio 插件 - 学了前面几篇文章后，我们终于可以开发一个稍微酷炫一点的插件了！" +
                "原创 by @SuperHua1001｜阅读全文 <a href='http://www.baidu.com'> 网页链接</a>";
    }

    private static class NoLineClickSpan extends ClickableSpan {
        String text;
        Callback cb;

        public NoLineClickSpan(String text, Callback cb) {
            super();
            this.text = text;
            this.cb = cb;
        }

        @Override
        public void updateDrawState(TextPaint ds) {
            ds.setColor(ds.linkColor);
            ds.setUnderlineText(false);
        }

        @Override
        public void onClick(View widget) {
            cb.onClick(widget, text);
        }

        interface Callback {
            void onClick(View widget, String text);
        }
    }


    private class LinkTouchMovementMethod extends LinkMovementMethod {
        private TouchableSpan mPressedSpan;

        @Override
        public boolean onTouchEvent(TextView textView, Spannable spannable, MotionEvent event) {
            if (event.getAction() == MotionEvent.ACTION_DOWN) {
                mPressedSpan = getPressedSpan(textView, spannable, event);
                if (mPressedSpan != null) {
                    mPressedSpan.setPressed(true);
                    Selection.setSelection(spannable, spannable.getSpanStart(mPressedSpan),
                            spannable.getSpanEnd(mPressedSpan));
                }
            } else if (event.getAction() == MotionEvent.ACTION_MOVE) {
                TouchableSpan touchedSpan = getPressedSpan(textView, spannable, event);
                if (mPressedSpan != null && touchedSpan != mPressedSpan) {
                    mPressedSpan.setPressed(false);
                    mPressedSpan = null;
                    Selection.removeSelection(spannable);
                }
            } else {
                if (mPressedSpan != null) {
                    mPressedSpan.setPressed(false);
                    super.onTouchEvent(textView, spannable, event);
                }
                mPressedSpan = null;
                Selection.removeSelection(spannable);
            }
            return true;
        }

        private TouchableSpan getPressedSpan(TextView textView, Spannable spannable, MotionEvent event) {

            int x = (int) event.getX();
            int y = (int) event.getY();

            x -= textView.getTotalPaddingLeft();
            y -= textView.getTotalPaddingTop();

            x += textView.getScrollX();
            y += textView.getScrollY();

            Layout layout = textView.getLayout();
            int line = layout.getLineForVertical(y);
            int off = layout.getOffsetForHorizontal(line, x);

            TouchableSpan[] link = spannable.getSpans(off, off, TouchableSpan.class);
            TouchableSpan touchedSpan = null;
            if (link.length > 0) {
                touchedSpan = link[0];
            }
            return touchedSpan;
        }

    }

    public abstract class TouchableSpan extends ClickableSpan {
        private boolean mIsPressed;
        private int mPressedBackgroundColor;
        private int mNormalTextColor;
        private int mPressedTextColor;

        public TouchableSpan(int normalTextColor, int pressedTextColor, int pressedBackgroundColor) {
            mNormalTextColor = normalTextColor;
            mPressedTextColor = pressedTextColor;
            mPressedBackgroundColor = pressedBackgroundColor;
        }

        public void setPressed(boolean isSelected) {
            mIsPressed = isSelected;
        }

        @Override
        public void updateDrawState(TextPaint ds) {
            super.updateDrawState(ds);
            ds.setColor(mIsPressed ? mPressedTextColor : mNormalTextColor);
            ds.bgColor = mIsPressed ? mPressedBackgroundColor : 0xffeeeeee;
            ds.setUnderlineText(false);
        }
    }
}
