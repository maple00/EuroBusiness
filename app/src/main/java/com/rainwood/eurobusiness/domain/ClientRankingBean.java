package com.rainwood.eurobusiness.domain;

import java.io.Serializable;

/**
 * @Author: sxs
 * @Time: 2020/3/21 0:45
 * @Desc: client ranking
 */
public final class ClientRankingBean implements Serializable {

    private String ranking;             // 排名
    private String kehuName;           // 客户姓名
    private String money;               // 销售金额

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
