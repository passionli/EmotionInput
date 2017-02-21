package com.xunlei.content.emotioninput;

/**
 * Created by admin on 2017/1/6.
 */

public class EmoticonBean {
    String id;
    String imageUrl;
    Integer resId;
    String title;

    public EmoticonBean(String id, String imageUrl, String title, Integer resId) {
        this.id = id;
        this.imageUrl = imageUrl;
        this.title = title;
        this.resId = resId;
    }

    @Override
    public String toString() {
        return "EmoticonBean{" +
                "id='" + id + '\'' +
                ", imageUrl='" + imageUrl + '\'' +
                ", title='" + title + '\'' +
                '}';
    }
}
