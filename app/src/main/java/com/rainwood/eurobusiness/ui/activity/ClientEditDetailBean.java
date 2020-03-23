package com.rainwood.eurobusiness.ui.activity;

import com.rainwood.eurobusiness.domain.ClientBaseBean;
import com.rainwood.eurobusiness.domain.InvoiceDetailBean;
import com.rainwood.eurobusiness.domain.TakeGoodsBean;

import java.io.Serializable;

/**
 * @Author: a797s
 * @Date: 2020/3/23 17:30
 * @Desc: 客户详情编辑
 */
public final class ClientEditDetailBean implements Serializable {

    private ClientBaseBean mClientBase;
    private InvoiceDetailBean mInvoiceDetai;
    private TakeGoodsBean mTakeGoods;

    public ClientBaseBean getClientBase() {
        return mClientBase;
    }

    public void setClientBase(ClientBaseBean clientBase) {
        mClientBase = clientBase;
    }

    public InvoiceDetailBean getInvoiceDetai() {
        return mInvoiceDetai;
    }

    public void setInvoiceDetai(InvoiceDetailBean invoiceDetai) {
        mInvoiceDetai = invoiceDetai;
    }

    public TakeGoodsBean getTakeGoods() {
        return mTakeGoods;
    }

    public void setTakeGoods(TakeGoodsBean takeGoods) {
        mTakeGoods = takeGoods;
    }
}
