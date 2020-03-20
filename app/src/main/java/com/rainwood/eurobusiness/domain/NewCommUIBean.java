package com.rainwood.eurobusiness.domain;

import java.util.List;

/**
 * @Author: a797s
 * @Date: 2020/2/23
 * @Desc: 新建 commUI
 */
public class NewCommUIBean {

    private String title;               // 标题
    private int type;                   // 子项加载的ViewItemType
    private List<CommonUIBean> commonUIList;            // 子项

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public List<CommonUIBean> getCommonUIList() {
        return commonUIList;
    }

    public void setCommonUIList(List<CommonUIBean> commonUIList) {
        this.commonUIList = commonUIList;
    }
}
