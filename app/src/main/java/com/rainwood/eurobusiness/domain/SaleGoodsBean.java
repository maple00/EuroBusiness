package com.rainwood.eurobusiness.domain;

import java.io.Serializable;
import java.util.List;

/**
 * @Author: shearson
 * @Time: 2020/2/26 15:04
 * @Desc: 批发商/门店创建商品
 */
public class SaleGoodsBean implements Serializable {

    private int type;                   // 加载的类型            --- 0：批发商；1：门店端
    private String imgPath;             //图片路径
    private String status;              // 商品状态
    private String name;                // 商品名称
    /* private String inPrice;             // 进价
     private String wholePrice;          // 批发价
     private String retailPrice;         // 零售价*/
    private String storeName;           // 门店名称
    private List<CommonUIBean> priceList;           // 进价、批发价、零售价

    @Override
    public String toString() {
        return "SaleGoodsBean{" +
                "type=" + type +
                ", imgPath='" + imgPath + '\'' +
                ", status='" + status + '\'' +
                ", name='" + name + '\'' +
                ", storeName='" + storeName + '\'' +
                ", priceList=" + priceList +
                '}';
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public List<CommonUIBean> getPriceList() {
        return priceList;
    }

    public void setPriceList(List<CommonUIBean> priceList) {
        this.priceList = priceList;
    }

    public String getImgPath() {
        return imgPath;
    }

    public void setImgPath(String imgPath) {
        this.imgPath = imgPath;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getStoreName() {
        return storeName;
    }

    public void setStoreName(String storeName) {
        this.storeName = storeName;
    }
}
