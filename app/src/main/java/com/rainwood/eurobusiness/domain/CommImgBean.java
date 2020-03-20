package com.rainwood.eurobusiness.domain;

import java.io.Serializable;

/**
 * @Author: a797s
 * @Date: 2020/2/11
 * @Desc: 常用图片 bean
 */
public class CommImgBean implements Serializable {

    private String imgPath;
    private String imgName;

    @Override
    public String toString() {
        return "CommImgBean{" +
                "imgPath='" + imgPath + '\'' +
                ", imgName='" + imgName + '\'' +
                '}';
    }

    public String getImgPath() {
        return imgPath;
    }

    public void setImgPath(String imgPath) {
        this.imgPath = imgPath;
    }

    public String getImgName() {
        return imgName;
    }

    public void setImgName(String imgName) {
        this.imgName = imgName;
    }
}
