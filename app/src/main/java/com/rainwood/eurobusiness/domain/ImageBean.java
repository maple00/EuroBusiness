package com.rainwood.eurobusiness.domain;

import java.io.Serializable;

/**
 * @Author: a797s
 * @Date: 2020/2/17
 * @Desc: 图片
 */
public class ImageBean implements Serializable {

//    private String id;              // 图片id
//    private String imgPath;
//    private String desc;            // 图片描述
    private String path;            // 图片地址
    private boolean hasAdd;      // 是否是添加图片标识
    private String src;         // 后台地址

    @Override
    public String toString() {
        return "ImageBean{" +
                "path='" + path + '\'' +
                ", hasAdd=" + hasAdd +
                ", src='" + src + '\'' +
                '}';
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public boolean isHasAdd() {
        return hasAdd;
    }

    public void setHasAdd(boolean hasAdd) {
        this.hasAdd = hasAdd;
    }

    public String getSrc() {
        return src;
    }

    public void setSrc(String src) {
        this.src = src;
    }
}
