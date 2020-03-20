package com.rainwood.eurobusiness.domain;

import java.io.Serializable;
import java.util.List;

/**
 * @Author: a797s
 * @Date: 2020/2/20
 * @Desc:
 */
public class WarinReBean implements Serializable {

    private String imgPath;         // 图片地址
    private String name;            // 商品名字
    private String model;           // 商品型号
    private String params;          // 规格参数
    private String invenNum;        // 库存
    private String replenish;       // 补货
    // 详情部分
    private String status;          // 商品状态
    private List<CommonUIBean> commonUIList;        // title_label -- 商品型号、商品分类、条码
    private List<SizeBean> sizeList;        // 规格参数部分
    private List<CommonUIBean> paramsList;          // 详情参数部分 -- 批发价、商品规格、零售价、税率、增值税、最小起订量

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

    public String getModel() {
        return model;
    }

    public void setModel(String model) {
        this.model = model;
    }

    public String getParams() {
        return params;
    }

    public void setParams(String params) {
        this.params = params;
    }

    public String getInvenNum() {
        return invenNum;
    }

    public void setInvenNum(String invenNum) {
        this.invenNum = invenNum;
    }

    public String getReplenish() {
        return replenish;
    }

    public void setReplenish(String replenish) {
        this.replenish = replenish;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public List<CommonUIBean> getCommonUIList() {
        return commonUIList;
    }

    public void setCommonUIList(List<CommonUIBean> commonUIList) {
        this.commonUIList = commonUIList;
    }

    public List<SizeBean> getSizeList() {
        return sizeList;
    }

    public void setSizeList(List<SizeBean> sizeList) {
        this.sizeList = sizeList;
    }

    public List<CommonUIBean> getParamsList() {
        return paramsList;
    }

    public void setParamsList(List<CommonUIBean> paramsList) {
        this.paramsList = paramsList;
    }
}
