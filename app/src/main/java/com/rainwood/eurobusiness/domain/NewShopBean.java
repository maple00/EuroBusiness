package com.rainwood.eurobusiness.domain;

import java.util.List;

/**
 * @Author: a797s
 * @Date: 2020/2/9
 * @Desc: 新建商品 bean
 */
public class NewShopBean {

    private String title;           // 模块名称
    private int type;               // 用于区分ListView显示不同的item，告诉适配器是什么类型的。
    private List<CommonUIBean> infosList;       // 模块信息
    private List<CommImgBean> imgList;          // 图片信息

    @Override
    public String toString() {
        return "NewShopBean{" +
                "title='" + title + '\'' +
                ", type=" + type +
                ", infosList=" + infosList +
                '}';
    }

    public List<CommImgBean> getImgList() {
        return imgList;
    }

    public void setImgList(List<CommImgBean> imgList) {
        this.imgList = imgList;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public List<CommonUIBean> getInfosList() {
        return infosList;
    }

    public void setInfosList(List<CommonUIBean> infosList) {
        this.infosList = infosList;
    }
}
