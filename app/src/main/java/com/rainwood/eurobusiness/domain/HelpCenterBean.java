package com.rainwood.eurobusiness.domain;

import java.io.Serializable;

/**
 * @Author: a797s
 * @Date: 2020/3/23 19:56
 * @Desc: 帮助中心
 */
public final class HelpCenterBean implements Serializable {

    private String id;          // 图文id
    private String word;        // 文字
    private String img;         // 图片

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getWord() {
        return word;
    }

    public void setWord(String word) {
        this.word = word;
    }

    public String getImg() {
        return img;
    }

    public void setImg(String img) {
        this.img = img;
    }
}
