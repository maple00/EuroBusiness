package com.rainwood.eurobusiness.domain;

import java.io.Serializable;
import java.util.List;

/**
 * @Author: a797s
 * @Date: 2020/2/18
 * @Desc: 商品详情
 */
public class GoodsDetailBean implements Serializable {

    private int type;                           //加载的类别
    private String title;                       // 标题
    private List<CommonUIBean> commList;         // 常见标准的列表
    private List<SpecialBean> paramsList;          // 规格参数列表
    private List<ImagesBean> imgList;            // 图片列表
    /**
     * 101: 库存商品 --- 商品信息
     *
     */
    private int loadType;                       // 加载的类型

    @Override
    public String toString() {
        return "GoodsDetailBean{" +
                "type=" + type +
                ", title='" + title + '\'' +
                ", commList=" + commList +
                ", paramsList=" + paramsList +
                ", imgList=" + imgList +
                ", loadType=" + loadType +
                '}';
    }

    public int getLoadType() {
        return loadType;
    }

    public void setLoadType(int loadType) {
        this.loadType = loadType;
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

    public List<CommonUIBean> getCommList() {
        return commList;
    }

    public void setCommList(List<CommonUIBean> commList) {
        this.commList = commList;
    }

    public List<SpecialBean> getParamsList() {
        return paramsList;
    }

    public void setParamsList(List<SpecialBean> paramsList) {
        this.paramsList = paramsList;
    }

    public List<ImagesBean> getImgList() {
        return imgList;
    }

    public void setImgList(List<ImagesBean> imgList) {
        this.imgList = imgList;
    }
}
