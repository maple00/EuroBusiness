package com.rainwood.eurobusiness.domain;

import java.util.List;

/**
 * @Author: a797s
 * @Date: 2019/12/12 17:51
 * @Desc: 首页
 */
public class ItemIndexListViewBean {

    private String title;         // 模块的名字
    private List<ItemGridBean> gridViewList;  // 模块的子项

    @Override
    public String toString() {
        return "ItemIndexListViewBean{" +
                "title='" + title + '\'' +
                ", gridViewList=" + gridViewList +
                '}';
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public List<ItemGridBean> getGridViewList() {
        return gridViewList;
    }

    public void setGridViewList(List<ItemGridBean> gridViewList) {
        this.gridViewList = gridViewList;
    }
}
