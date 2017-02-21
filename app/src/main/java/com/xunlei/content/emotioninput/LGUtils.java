package com.xunlei.content.emotioninput;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.os.Environment;
import android.util.Log;

import com.bumptech.glide.gifdecoder.GifDecoder;
import com.bumptech.glide.gifencoder.AnimatedGifEncoder;
import com.bumptech.glide.load.resource.gif.GifDrawable;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * Created by admin on 2017/1/9.
 */

public class LGUtils {
    private static final String TAG = "LGUtils";

    public static final String md5(final String s) {
        final String MD5 = "MD5";
        try {
            // Create MD5 Hash
            MessageDigest digest = java.security.MessageDigest
                    .getInstance(MD5);
            digest.update(s.getBytes());
            byte messageDigest[] = digest.digest();

            // Create Hex String
            StringBuilder hexString = new StringBuilder();
            for (byte aMessageDigest : messageDigest) {
                String h = Integer.toHexString(0xFF & aMessageDigest);
                while (h.length() < 2)
                    h = "0" + h;
                hexString.append(h);
            }
            return hexString.toString();

        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return "";
    }

    /**
     * 将Gif和文本数据合成新的Gif文件
     *
     * @param url      图片URL
     * @param drawable 显示Gif图片的Drawable
     * @param text     文本
     * @return 合成新的GIF的文件名
     * 空 表示失败
     */
    public static String encodeGifText(String url, GifDrawable drawable, Bitmap textBitmap, String text) {
        GifDecoder gifDecoder = drawable.getDecoder();
        gifDecoder.resetFrameIndex();
        gifDecoder.advance();

        Paint p = new Paint();
//        p.setColor(Color.RED);
        //TODO 动态改变字体大小？
//        p.setTextSize(64);

        //图片URL+添加的文字
        String md5 = LGUtils.md5(url + text);
        final String outputFileName = Environment.getExternalStorageDirectory().getPath() + "/liguang/" + md5 + ".gif";
        AnimatedGifEncoder gifEncoder = new AnimatedGifEncoder();
        if (!gifEncoder.start(outputFileName)) {
            Log.w(TAG, "run:  create file failed");
            return null;
        }

        boolean mScaled = false;
        for (int i = 0; i < gifDecoder.getFrameCount(); i++) {
            Bitmap bitmap = gifDecoder.getNextFrame();
            Canvas c = new Canvas(bitmap);
            //TODO 这里直接把TextView的Bitmap在Canvas上画上去？？？？
            //这样不用计算文本位置
//            c.drawText(text, 0, 0, p);
            if (!mScaled) {
                mScaled = true;
                textBitmap = Bitmap.createScaledBitmap(textBitmap, bitmap.getWidth(), bitmap.getHeight(), true);
            }
            c.drawBitmap(textBitmap, 0, 0, p);
            Log.d(TAG, "encodeGifText: gif w=" + bitmap.getWidth() + " h=" + bitmap.getHeight());
            Log.d(TAG, "encodeGifText: text w=" + textBitmap.getWidth() + " h=" + textBitmap.getHeight());

            gifEncoder.setDelay(gifDecoder.getNextDelay());
            if (!gifEncoder.addFrame(bitmap)) {
                Log.d(TAG, "encodeGifText: encoder addFrame failed");
                return null;
            }
            gifDecoder.advance();
        }
        if (!gifEncoder.finish()) {
            Log.d(TAG, "encodeGifText: encoder finish failed");
            return null;
        }

        return outputFileName;
    }

    public static String encodeBitmapText(Bitmap background, Bitmap foreground, String url, String text) {
        Bitmap copy = background.copy(Bitmap.Config.ARGB_8888, true);
        Log.d(TAG, "encodeBitmapText: copy " + copy.getWidth() + " " + copy.getHeight());
        Canvas canvas = new Canvas(copy);


//        foreground = Bitmap.createScaledBitmap(foreground, canvas.getWidth(), canvas.getHeight(), true);
        Log.d(TAG, "encodeBitmapText: foreground " + foreground.getWidth() + " " + foreground.getHeight());
        canvas.drawBitmap(foreground, 0, 0, new Paint());

        String md5 = LGUtils.md5(url + text);
        final String outputFileName = Environment.getExternalStorageDirectory().getPath() + "/liguang/" + md5 + ".jpg";
        OutputStream fOut = null;
        try {
            fOut = new FileOutputStream(outputFileName);
            copy.compress(Bitmap.CompressFormat.JPEG, 85, fOut); // saving the Bitmap to a file compressed as a JPEG with 85% compression rate
            fOut.flush(); // Not really required
            fOut.close(); // do not forget to close the stream
        } catch (IOException e) {
            e.printStackTrace();
        }
        return outputFileName;
    }

    public static String saveBitmap2File(Bitmap bitmap, String url, String text) {
        String md5 = LGUtils.md5(url + text);
        final String outputFileName = Environment.getExternalStorageDirectory().getPath() + "/liguang/" + md5 + ".jpg";
        OutputStream fOut = null;
        try {
            fOut = new FileOutputStream(outputFileName);
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, fOut); // saving the Bitmap to a file compressed as a JPEG with 85% compression rate
            fOut.flush(); // Not really required
            fOut.close(); // do not forget to close the stream
        } catch (IOException e) {
            e.printStackTrace();
        }
        return outputFileName;
    }
}
