package com.rainwood.eurobusiness.domain;

import java.io.Serializable;
import java.util.List;

/**
 * @Author: a797s
 * @Date: 2020/2/19
 * @Desc: 订单信息
 */
public class OrderBean implements Serializable {

    private String title;       // 标题
    private int itemType;       // 加载item 类型
    private List<CommonUIBean> commonList;          // CommonUI 信息
    private List<PressBean> pressList;              // press 信息

    @Override
    public String toString() {
        return "OrderBean{" +
                "title='" + title + '\'' +
                ", itemType=" + itemType +
                ", commonList=" + commonList +
                ", pressList=" + pressList +
                '}';
    }

    public int getItemType() {
        return itemType;
    }

    public void setItemType(int itemType) {
        this.itemType = itemType;
    }

    public List<PressBean> getPressList() {
        return pressList;
    }

    public void setPressList(List<PressBean> pressList) {
        this.pressList = pressList;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public List<CommonUIBean> getCommonList() {
        return commonList;
    }

    public void setCommonList(List<CommonUIBean> commonList) {
        this.commonList = commonList;
    }
}
