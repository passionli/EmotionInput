package com.xunlei.content.emotioninput;

import java.util.HashMap;

public class EmotionData {

    public static final HashMap<String, Integer> sEmotionMap = new HashMap<>();

    static {
        //read data config file
        for (int i = 0; i < 20; i++) {
            sEmotionMap.put("[xl_nr_emotion_" + i + "]", (i % 2 == 0) ? R.mipmap.emotion : R.mipmap.ic_launcher);
        }
    }

    public static Integer getEmotion(String key) {
        return sEmotionMap.get(key);
    }
}
