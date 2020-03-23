package com.rainwood.eurobusiness.domain;

import java.io.Serializable;

/**
 * @Author: a797s
 * @Date: 2020/3/23 9:19
 * @Desc: 销售排行榜 ----- 门店统计列表
 */
public final class StoreBean implements Serializable {

    private String ranking;
    private String storeName;
    private String money;

    @Override
    public String toString() {
        return "StoreBean{" +
                "ranking='" + ranking + '\'' +
                ", storeName='" + storeName + '\'' +
                ", money='" + money + '\'' +
                '}';
    }

    public String getRanking() {
        return ranking;
    }

    public void setRanking(String ranking) {
        this.ranking = ranking;
    }

    public String getStoreName() {
        return storeName;
    }

    public void setStoreName(String storeName) {
        this.storeName = storeName;
    }

    public String getMoney() {
        return money;
    }

    public void setMoney(String money) {
        this.money = money;
    }
}
