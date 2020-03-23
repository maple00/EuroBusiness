package com.rainwood.eurobusiness.domain;

import java.io.Serializable;

/**
 * @Author: a797s
 * @Date: 2020/3/23 9:18
 * @Desc: 销售排行榜---客户统计
 */
public final class ClientBean implements Serializable {

    private String ranking;
    private String kehuName;
    private String money;

    public String getRanking() {
        return ranking;
    }

    public void setRanking(String ranking) {
        this.ranking = ranking;
    }

    public String getKehuName() {
        return kehuName;
    }

    public void setKehuName(String kehuName) {
        this.kehuName = kehuName;
    }

    public String getMoney() {
        return money;
    }

    public void setMoney(String money) {
        this.money = money;
    }
}
