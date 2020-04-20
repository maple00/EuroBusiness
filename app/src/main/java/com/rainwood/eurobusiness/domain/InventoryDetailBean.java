package com.rainwood.eurobusiness.domain;

import java.io.Serializable;
import java.util.List;

/**
 * @Author: a797s
 * @Date: 2020/4/16 16:56
 * @Desc: 入库明细
 */
public final class InventoryDetailBean implements Serializable {

    private int clickPos = 0;               // 点击记录
    private String storeName;           // 门店名称
    private String workFlow;            // 状态
    private List<SpecialDetailBean> mxlist; // 订单列表

    public int getClickPos() {
        return clickPos;
    }

    public void setClickPos(int clickPos) {
        this.clickPos = clickPos;
    }

    public String getStoreName() {
        return storeName;
    }

    public void setStoreName(String storeName) {
        this.storeName = storeName;
    }

    public String getWorkFlow() {
        return workFlow;
    }

    public void setWorkFlow(String workFlow) {
        this.workFlow = workFlow;
    }

    public List<SpecialDetailBean> getMxlist() {
        return mxlist;
    }

    public void setMxlist(List<SpecialDetailBean> mxlist) {
        this.mxlist = mxlist;
    }
}
