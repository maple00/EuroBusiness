package com.rainwood.eurobusiness.request;

import android.util.Log;

import com.rainwood.eurobusiness.common.Contants;
import com.rainwood.eurobusiness.okhttp.OkHttp;
import com.rainwood.eurobusiness.okhttp.OnHttpListener;
import com.rainwood.eurobusiness.okhttp.RequestParams;

/**
 * @Author: a797s
 * @Date: 2020/3/10 17:01
 * @Desc: 后台数据请求
 */
public final class RequestPost {

    // 退货管理

    /**
     * 退货列表
     *
     * @param classify buy/采购订单，sale/销售订单
     * @param workFlow 订单状态 draft:草稿 waitAudit:待审批 complete:已完成
     * @param refundNo 退货单号
     * @param listener listener
     */
    public static void returnGoodsList(String classify, String workFlow, String refundNo, OnHttpListener listener) {
        RequestParams params = new RequestParams();
        params.add("classify", classify);
        params.add("workFlow", workFlow);
        params.add("refundNo", refundNo);
        Log.d("sxs", " - classify: " + classify + " - workFlow: " + workFlow + " - refundNo: " + refundNo);
        OkHttp.post(Contants.ROOT_URI + "wxapi/v1/order.php?type=getRefundOrder", params, listener);
    }

    /**
     * 退货订单详情
     *
     * @param id       查询id
     * @param listener
     */
    public static void returnGoodsDetail(String id, OnHttpListener listener) {
        RequestParams params = new RequestParams();
        params.add("id", id);
        OkHttp.post(Contants.ROOT_URI + "wxapi/v1/order.php?type=getRefundOrderInfo", params, listener);
    }

    /**
     * 订单销售统计页
     */
    public static void saleTotal(OnHttpListener listener) {
        RequestParams params = new RequestParams();
        OkHttp.post(Contants.ROOT_URI + "wxapi/v1/statistics.php?type=getTotal", params, listener);
    }

    /**
     * 销售排行榜
     */
    public static void saleRanking(String type, OnHttpListener listener) {
        RequestParams params = new RequestParams();
        params.add("type", type);
        OkHttp.post(Contants.ROOT_URI + "wxapi/v1/statistics.php?type=getSaleRank", params, listener);
    }

    /**
     * 未收款项
     */
    public static void UnReclist(OnHttpListener listener) {
        RequestParams params = new RequestParams();
        OkHttp.post(Contants.ROOT_URI + "wxapi/v1/statistics.php?type=getReclistct", params, listener);
    }

    //     客户管理

    /**
     * 获取客户列表
     *
     * @param name       客户名称
     * @param kehuTypeId 客户id
     * @param listener
     */
    public static void getClientList(String name, String kehuTypeId, OnHttpListener listener) {
        RequestParams params = new RequestParams();
        params.add("name", name);
        params.add("kehuTypeId", kehuTypeId);
        OkHttp.post(Contants.ROOT_URI + "wxapi/v1/client.php?type=getClientlist", params, listener);
    }

    /**
     * 获取客户分类列表
     *
     * @param listener
     */
    public static void getClientTypeList(OnHttpListener listener) {
        RequestParams params = new RequestParams();
        OkHttp.post(Contants.ROOT_URI + "wxapi/v1/client.php?type=getKehuTypelist", params, listener);
    }

    /**
     * 查看客户详情
     *
     * @param id       custom id
     * @param listener
     */
    public static void getClientDetail(String id, OnHttpListener listener) {
        RequestParams params = new RequestParams();
        params.add("id", id);
        OkHttp.post(Contants.ROOT_URI + "wxapi/v1/client.php?type=getClientInfo", params, listener);
    }

    /**
     * 获取发票地址列表
     *
     * @param id       客户id
     * @param listener
     */
    public static void getClientInvoicelist(String id, OnHttpListener listener) {
        RequestParams params = new RequestParams();
        params.add("id", id);
        OkHttp.post(Contants.ROOT_URI + "wxapi/v1/client.php?type=getClientInvoicelist", params, listener);
    }

    /**
     * 删除发票地址
     *
     * @param id
     * @param listener
     */
    public static void clearInvoice(String id, OnHttpListener listener) {
        RequestParams params = new RequestParams();
        params.add("id", id);
        OkHttp.post(Contants.ROOT_URI + "wxapi/v1/client.php?type=delClientInvoice", params, listener);
    }

    /**
     * 获取客户收货地址列表
     *
     * @param id
     * @param listener
     */
    public static void getClientAddresslist(String id, OnHttpListener listener) {
        RequestParams params = new RequestParams();
        params.add("id", id);
        OkHttp.post(Contants.ROOT_URI + "wxapi/v1/client.php?type=getClientAddresslist", params, listener);
    }

    /**
     * 删除收货地址
     *
     * @param id
     * @param listener
     */
    public static void clearClientAddress(String id, OnHttpListener listener) {
        RequestParams params = new RequestParams();
        params.add("id", id);
        OkHttp.post(Contants.ROOT_URI + "wxapi/v1/client.php?type=delClientAddress", params, listener);
    }

    /**
     * 设置默认地址
     *
     * @param type     invoice/发票，address/收货地址
     * @param id       发票或地址ID
     * @param listener
     */
    public static void setDefaultAddress(String type, String id, OnHttpListener listener) {
        RequestParams params = new RequestParams();
        params.add("id", id);
        params.add("type", type);
        OkHttp.post(Contants.ROOT_URI + "wxapi/v1/client.php?type=setDefaultAddress", params, listener);
    }

    /**
     * 获取客户分类列表
     */
    public static void getClientManagerTypeList(OnHttpListener listener) {
        RequestParams params = new RequestParams();
        OkHttp.post(Contants.ROOT_URI + "wxapi/v1/client.php?type=getKehuTypelist", params, listener);
    }

    /**
     * 删除客户分类
     */
    public static void clearCustomType(String id, OnHttpListener listener) {
        RequestParams params = new RequestParams();
        params.add("id", id);
        OkHttp.post(Contants.ROOT_URI + "wxapi/v1/client.php?type=delKehuType", params, listener);
    }

    /**
     * 获取客户分类详情
     *
     * @param id
     * @param listener
     */
    public static void getClientTypeDetail(String id, OnHttpListener listener) {
        RequestParams params = new RequestParams();
        params.add("id", id);
        OkHttp.post(Contants.ROOT_URI + "wxapi/v1/client.php?type=getKehuTypeInfo", params, listener);
    }

    /**
     * 客户分类编辑
     *
     * @param id       分类的id
     * @param name     分类的名称
     * @param discount 折扣
     * @param list     排序
     * @param listener
     */
    public static void clientTypeEdit(String id, String name, String discount, String list, OnHttpListener listener) {
        RequestParams params = new RequestParams();
        params.add("id", id);
        OkHttp.post(Contants.ROOT_URI + "wxapi/v1/client.php?type=kehuTypeEdit", params, listener);
    }

    // 出入库记录

    /**
     * 出库记录
     *
     * @param classify  订单类型 saleOut：线下订单 saleOnlineOut：线上订单 returnOut：退货订单
     * @param goodsName 商品名称
     * @param storeId   门店ID
     * @param listener
     */
    public static void getInventoryOut(String classify, String goodsName, String storeId, OnHttpListener listener) {
        RequestParams params = new RequestParams();
        params.add("classify", classify);
        params.add("goodsName", goodsName);
        params.add("storeId", storeId);
        OkHttp.post(Contants.ROOT_URI + "wxapi/v1/stock.php?type=getInventoryOut", params, listener);
    }

    /**
     * 获取入库记录
     *
     * @param classify  订单类型 buyIn：采购入库 returnIn：退货订单
     * @param goodsName 商品名称
     * @param storeId   门店ID
     * @param listener
     */
    public static void getInventoryIn(String classify, String goodsName, String storeId, OnHttpListener listener) {
        RequestParams params = new RequestParams();
        params.add("classify", classify);
        params.add("goodsName", goodsName);
        params.add("storeId", storeId);
        OkHttp.post(Contants.ROOT_URI + "wxapi/v1/stock.php?type=getInventoryIn", params, listener);
    }

    /**
     * 出入库详情
     *
     * @param id       出入库记录ID
     * @param listener
     */
    public static void getStockChecklist(String id, OnHttpListener listener) {
        RequestParams params = new RequestParams();
        params.add("id", id);
        OkHttp.post(Contants.ROOT_URI + "wxapi/v1/stock.php?type=getInventoryMxInfo", params, listener);
    }

    // 盘点记录

    /**
     * =盘点记录列表
     *
     * @param workFlow  状态 complete:已完成 waitAudit:审核中
     * @param goodsName 商品名称
     * @param listener
     */
    public static void getStockChecklist(String workFlow, String goodsName, OnHttpListener listener) {
        RequestParams params = new RequestParams();
        params.add("workFlow", workFlow);
        params.add("goodsName", goodsName);
        OkHttp.post(Contants.ROOT_URI + "wxapi/v1/stock.php?type=getStockChecklist", params, listener);
    }

    /**
     * 获取盘点记录详情
     *
     * @param id       盘点记录id
     * @param listener
     */
    public static void getInventoryInfo(String id, OnHttpListener listener) {
        RequestParams params = new RequestParams();
        params.add("id", id);
        OkHttp.post(Contants.ROOT_URI + "wxapi/v1/stock.php?type=getInventoryInfo", params, listener);
    }

    /**
     * 获取库存列表
     *
     * @param keyWord  搜索关键词
     * @param barCode  商品条码
     * @param listener
     */
    public static void getAllStock(String keyWord, String barCode, OnHttpListener listener) {
        RequestParams params = new RequestParams();
        params.add("keyWord", keyWord);
        params.add("barCode", barCode);
        OkHttp.post(Contants.ROOT_URI + "wxapi/v1/stock.php?type=getAllStock", params, listener);
    }

    /**
     * 获取库存详情
     *
     * @param id       商品ID
     * @param barCode  扫码时传
     * @param listener
     */
    public static void getStockDetail(String id, String barCode, OnHttpListener listener) {
        RequestParams params = new RequestParams();
        params.add("id", id);
        params.add("barCode", barCode);
        OkHttp.post(Contants.ROOT_URI + "wxapi/v1/stock.php?type=getStockInfo", params, listener);
    }

    // 采购记录

    // 个人中心
    public static void getPurchaseOrderlist(String classify, String orderNo, String goodsName, String workFlow,
                                            String startDate, String endDate, OnHttpListener listener) {
        RequestParams params = new RequestParams();
        params.add("classify", classify);
        params.add("orderNo", orderNo);
        params.add("goodsName", goodsName);
        params.add("workFlow", workFlow);
        params.add("startDate", startDate);
        params.add("endDate", endDate);
        OkHttp.post(Contants.ROOT_URI + "wxapi/v1/order.php?type=getPurchaseOrderlist", params, listener);
    }

    /**
     * 个人中心
     *
     * @param listener
     */
    public static void getPersonalInfo(OnHttpListener listener) {
        RequestParams params = new RequestParams();
        OkHttp.post(Contants.ROOT_URI + "wxapi/v1/index.php?type=getPerson", params, listener);
    }

    /**
     * 门店或开票信息
     *
     * @param listener
     */
    public static void storeOrInvoice(OnHttpListener listener) {
        RequestParams params = new RequestParams();
        OkHttp.post(Contants.ROOT_URI + "wxapi/v1/index.php?type=getStoreInfo", params, listener);
    }

    /**
     * 获取验证码
     *
     * @param listener
     */
    public static void getVerifyCode(String tel, OnHttpListener listener) {
        RequestParams params = new RequestParams();
        params.add("tel", tel);
        OkHttp.post(Contants.ROOT_URI + "wxapi/v1/login.php?type=getCode", params, listener);
    }

    /**
     * 修改密码
     *
     * @param listener
     */
    public static void modifyPwd(String prove, String newPas, String surePas, OnHttpListener listener) {
        RequestParams params = new RequestParams();
        params.add("prove", prove);
        params.add("newPas", newPas);
        params.add("surePas", surePas);
        OkHttp.post(Contants.ROOT_URI + "wxapi/v1/index.php?type=changePwd", params, listener);
    }

    /**
     * 帮助中心
     */
    public static void helpCenter(OnHttpListener listener) {
        RequestParams params = new RequestParams();
        OkHttp.post(Contants.ROOT_URI + "wxapi/v1/index.php?type=getHelp", params, listener);
    }

    // 库存管理

    /**
     * 预警库存列表
     *
     * @param keyWord  搜索关键字
     * @param listener
     */
    public static void warnStockList(String keyWord, OnHttpListener listener) {
        RequestParams params = new RequestParams();
        params.add("keyWord", keyWord);
        OkHttp.post(Contants.ROOT_URI + "wxapi/v1/stock.php?type=getWarnStocklist", params, listener);
    }

    /**
     * 预警库存详情
     *
     * @param id       预警详情id
     * @param listener
     */
    public static void warnStockDetail(String id, OnHttpListener listener) {
        RequestParams params = new RequestParams();
        params.add("id", id);
        OkHttp.post(Contants.ROOT_URI + "wxapi/v1/stock.php?type=getWarnStockInfo", params, listener);
    }

    /**
     * 预警库存补货
     *
     * @param skuId        规格ID
     * @param num          数量
     * @param text         备注
     * @param deliveryDate 交货时间
     * @param listener
     */
    public static void commitChargeOrder(String skuId, String num, String text, String deliveryDate, OnHttpListener listener) {
        RequestParams params = new RequestParams();
        params.add("skuId", skuId);
        params.add("num", num);
        params.add("text", text);
        params.add("deliveryDate", deliveryDate);
        OkHttp.post(Contants.ROOT_URI + "wxapi/v1/stock.php?type=commitChargeOrder", params, listener);
    }
}
