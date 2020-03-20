package com.rainwood.eurobusiness.domain;

/**
 * @Author: a797s
 * @Date: 2020/2/7
 * @Desc: 个人中心Bean
 */
public class PersonalListBean {

    private int imgPath;                // 图标路径
    private String name;                // 模块名称
    private String note;                // 备注

    public int getImgPath() {
        return imgPath;
    }

    public void setImgPath(int imgPath) {
        this.imgPath = imgPath;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }
}
