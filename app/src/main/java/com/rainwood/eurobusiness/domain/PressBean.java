package com.rainwood.eurobusiness.domain;

/**
 * @Author: a797s
 * @Date: 2020/2/6
 * @Desc: 选中实体类
 */
public class PressBean {

    private boolean choose;             // 是否选中
    private String title;               // 选中的title
    private String imgPath;             // 选中的图片的路径

    @Override
    public String toString() {
        return "PressBean{" +
                "choose=" + choose +
                ", title='" + title + '\'' +
                ", imgPath='" + imgPath + '\'' +
                '}';
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public boolean isChoose() {
        return choose;
    }

    public void setChoose(boolean choose) {
        this.choose = choose;
    }

    public String getImgPath() {
        return imgPath;
    }

    public void setImgPath(String imgPath) {
        this.imgPath = imgPath;
    }
}
