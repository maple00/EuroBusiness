package com.rainwood.eurobusiness.domain;

import java.io.Serializable;

/**
 * @Author: a797s
 * @Date: 2020/2/17
 * @Desc: 图片
 */
public class ImagesBean implements Serializable {

    private String id;
    private String src;
    private String imgPath;

    @Override
    public String toString() {
        return "ImagesBean{" +
                "id='" + id + '\'' +
                ", src='" + src + '\'' +
                ", imgPath='" + imgPath + '\'' +
                '}';
    }

    public String getImgPath() {
        return imgPath;
    }

    public void setImgPath(String imgPath) {
        this.imgPath = imgPath;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getSrc() {
        return src;
    }

    public void setSrc(String src) {
        this.src = src;
    }
}
