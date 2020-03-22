package com.rainwood.eurobusiness.domain;

import java.io.Serializable;

/**
 * @Author: sxs
 * @Time: 2020/3/21 0:21
 * @Desc: java类作用描述
 */
public final class GoodsRankingBean implements Serializable {

    private String raking;          // 排名
    private String goodsName;       // 商品名称
    private String goodsSkuName;    // 规格
    private String num;             // 销售数量

    public String getRaking() {
        return raking;
    }

    public void setRaking(String raking) {
        this.raking = raking;
    }

    public String getGoodsName() {
        return goodsName;
    }

    public void setGoodsName(String goodsName) {
        this.goodsName = goodsName;
    }

    public String getGoodsSkuName() {
        return goodsSkuName;
    }

    public void setGoodsSkuName(String goodsSkuName) {
        this.goodsSkuName = goodsSkuName;
    }

    public String getNum() {
        return num;
    }

    public void setNum(String num) {
        this.num = num;
    }
}
