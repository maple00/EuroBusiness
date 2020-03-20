package com.rainwood.eurobusiness.domain;

import java.io.Serializable;
import java.util.List;

/**
 * @Author: shearson
 * @Time: 2020/2/26 16:56
 * @Desc: 批发商  ---- 商品管理(商品分类)
 */
public class ClassifyBean implements Serializable {

    private String imgPath;             // 图片地址
    private int selected = 0;               // 点击记录
    private String name;                // 名称
    private List<ClassifySubBean> subList;      // 子项分类

    public int getSelected() {
        return selected;
    }

    public void setSelected(int selected) {
        this.selected = selected;
    }

    public String getImgPath() {
        return imgPath;
    }

    public void setImgPath(String imgPath) {
        this.imgPath = imgPath;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<ClassifySubBean> getSubList() {
        return subList;
    }

    public void setSubList(List<ClassifySubBean> subList) {
        this.subList = subList;
    }
}
