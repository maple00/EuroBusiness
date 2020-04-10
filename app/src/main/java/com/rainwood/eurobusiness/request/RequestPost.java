package com.rainwood.eurobusiness.request;

import android.util.Log;

import com.rainwood.eurobusiness.common.Contants;
import com.rainwood.eurobusiness.domain.ISpecialBean;
import com.rainwood.eurobusiness.okhttp.JsonParser;
import com.rainwood.eurobusiness.okhttp.OkHttp;
import com.rainwood.eurobusiness.okhttp.OnHttpListener;
import com.rainwood.eurobusiness.okhttp.RequestParams;

import java.io.File;
import java.util.List;

/**
 * @Author: a797s
 * @Date: 2020/3/10 17:01
 * @Desc: 后台数据请求
 */
public final class RequestPost {

    // 获取首页轮播图
    public static void getBannerlist(OnHttpListener listener) {
        RequestParams params = new RequestParams();
        params.add("token", Contants.token);
        OkHttp.post(Contants.ROOT_URI + "wxapi/v1/index.php?type=getBannerlist", params, listener);
    }

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
        params.add("token", Contants.token);
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
        params.add("token", Contants.token);
        params.add("id", id);
        OkHttp.post(Contants.ROOT_URI + "wxapi/v1/order.php?type=getRefundOrderInfo", params, listener);
    }

    /**
     * 订单销售统计页
     */
    public static void saleTotal(OnHttpListener listener) {
        RequestParams params = new RequestParams();
        params.add("token", Contants.token);
        OkHttp.post(Contants.ROOT_URI + "wxapi/v1/statistics.php?type=getTotal", params, listener);
    }

    /**
     * 销售排行榜
     */
    public static void saleRanking(String type, OnHttpListener listener) {
        RequestParams params = new RequestParams();
        params.add("token", Contants.token);
        params.add("type", type);
        OkHttp.post(Contants.ROOT_URI + "wxapi/v1/statistics.php?type=getSaleRank", params, listener);
    }

    /**
     * 未付款项
     */
    public static void UnPaylist(OnHttpListener listener) {
        RequestParams params = new RequestParams();
        params.add("token", Contants.token);
        OkHttp.post(Contants.ROOT_URI + "wxapi/v1/statistics.php?type=getPayclist", params, listener);
    }

    /**
     * 未收款项
     */
    public static void UnRecList(OnHttpListener listener) {
        RequestParams params = new RequestParams();
        params.add("token", Contants.token);
        OkHttp.post(Contants.ROOT_URI + "wxapi/v1/statistics.php?type=getReclist", params, listener);
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
        params.add("token", Contants.token);
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
        params.add("token", Contants.token);
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
        params.add("token", Contants.token);
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
        params.add("token", Contants.token);
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
        params.add("token", Contants.token);
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
        params.add("token", Contants.token);
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
        params.add("token", Contants.token);
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
        params.add("token", Contants.token);
        params.add("id", id);
        params.add("type", type);
        OkHttp.post(Contants.ROOT_URI + "wxapi/v1/client.php?type=setDefaultAddress", params, listener);
    }

    /**
     * 获取客户分类列表
     */
    public static void getClientManagerTypeList(OnHttpListener listener) {
        RequestParams params = new RequestParams();
        params.add("token", Contants.token);
        OkHttp.post(Contants.ROOT_URI + "wxapi/v1/client.php?type=getKehuTypelist", params, listener);
    }

    /**
     * 删除客户分类
     */
    public static void clearCustomType(String id, OnHttpListener listener) {
        RequestParams params = new RequestParams();
        params.add("token", Contants.token);
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
        params.add("token", Contants.token);
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
        params.add("token", Contants.token);
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
        params.add("token", Contants.token);
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
        params.add("token", Contants.token);
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
        params.add("token", Contants.token);
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
        params.add("token", Contants.token);
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
        params.add("token", Contants.token);
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
        params.add("token", Contants.token);
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
        params.add("token", Contants.token);
        params.add("id", id);
        params.add("barCode", barCode);
        OkHttp.post(Contants.ROOT_URI + "wxapi/v1/stock.php?type=getStockInfo", params, listener);
    }

    // 采购记录

    /**
     * 采购列表
     *
     * @param classify  charge:补货订单 new:采购订单
     * @param orderNo   订单号
     * @param goodsName 商品名称
     * @param workFlow  waitIn:待入库 complete:已完成 waitAudit:待审核
     * @param startDate 开始日期
     * @param endDate   结束日期
     * @param listener
     */
    public static void getPurchaseOrderlist(String classify, String orderNo, String goodsName, String workFlow,
                                            String startDate, String endDate, OnHttpListener listener) {
        RequestParams params = new RequestParams();
        params.add("token", Contants.token);
        params.add("classify", classify);
        params.add("orderNo", orderNo);
        params.add("goodsName", goodsName);
        params.add("workFlow", workFlow);
        params.add("startDate", startDate);
        params.add("endDate", endDate);
        OkHttp.post(Contants.ROOT_URI + "wxapi/v1/order.php?type=getPurchaseOrderlist", params, listener);
    }

    /**
     * 采购订单详情
     *
     * @param id
     * @param listener
     */
    public static void getPurchaseOrderDetail(String id, OnHttpListener listener) {
        RequestParams params = new RequestParams();
        params.add("token", Contants.token);
        params.add("orderNo", id);
        OkHttp.post(Contants.ROOT_URI + "wxapi/v1/order.php?type=getPurchaseOrderInfo", params, listener);
    }

    /**
     * 新建采购单页面数据
     *
     * @param listener
     */
    public static void newPurchaseInfo(OnHttpListener listener) {
        RequestParams params = new RequestParams();
        params.add("token", Contants.token);
        OkHttp.post(Contants.ROOT_URI + "wxapi/v1/order.php?type=newOrderPage", params, listener);
    }

    /**
     * 退货信息
     *
     * @param id
     * @param listener
     */
    public static void getRefundOrder(String id, OnHttpListener listener) {
        RequestParams params = new RequestParams();
        params.add("token", Contants.token);
        params.add("id", id);
        OkHttp.post(Contants.ROOT_URI + "wxapi/v1/order.php?type=getRefundPage", params, listener);
    }

    /**
     * 提交退货
     *
     * @param id           订单规格ID
     * @param num          退货数量
     * @param skuId        选择的规格 没有则不传
     * @param text         备注
     * @param freightMoney 运费
     * @param listener
     */
    public static void refundPurchaseOrder(String id, String num, String skuId, String text, String freightMoney, OnHttpListener listener) {
        RequestParams params = new RequestParams();
        params.add("token", Contants.token);
        params.add("id", id);
        params.add("num", num);
        params.add("skuId", skuId);
        params.add("text", text);
        params.add("freightMoney", freightMoney);
        OkHttp.post(Contants.ROOT_URI + "wxapi/v1/order.php?type=refundPurchaseOrder", params, listener);
    }

    /**
     * 有规格的入库
     */
    public static void inStockEdit(String id, String num, String text, OnHttpListener listener) {
        RequestParams params = new RequestParams();
        params.add("token", Contants.token);
        params.add("id", id);
        params.add("num", num);
        params.add("text", text);
        OkHttp.post(Contants.ROOT_URI + "wxapi/v1/order.php?type=inStockEdit", params, listener);
    }

    /**
     * 入库页面数据
     *
     * @param id       规格id
     * @param listener
     */
    public static void getInStockPage(String id, OnHttpListener listener) {
        RequestParams params = new RequestParams();
        params.add("token", Contants.token);
        params.add("id", id);
        OkHttp.post(Contants.ROOT_URI + "wxapi/v1/order.php?type=getInStockPage", params, listener);
    }

    // 商品管理

    /**
     * 商品列表页
     *
     * @param type         mine/我的，store/门店创建
     * @param state        online/上架，offline/下架
     * @param storelist    门店筛选ID数组
     * @param classifylist 分类筛选ID数组
     * @param listener
     */
    public static void getGoodsList(String type, String state, String storelist, List<String> classifylist, OnHttpListener listener) {
        RequestParams params = new RequestParams();
        params.add("token", Contants.token);
        params.add("type", type);
        params.add("state", state);
        params.add("storelist", storelist);
        for (int i = 0; i < classifylist.size(); i++) {
            params.add("classifylist[" + i + "]", classifylist.get(i));
        }
        Log.d("sxs", "==" + type + " ==" + state + " ==" + storelist + " ==" + classifylist);
        OkHttp.post(Contants.ROOT_URI + "wxapi/v1/goods.php?type=getGoodsList", params, listener);
    }

    /**
     * 商品上下架
     *
     * @param id       商品id
     * @param listener
     */
    public static void onSaleGoods(String id, OnHttpListener listener) {
        RequestParams params = new RequestParams();
        params.add("token", Contants.token);
        params.add("id", id);
        OkHttp.post(Contants.ROOT_URI + "wxapi/v1/goods.php?type=onSaleGoods", params, listener);
    }

    /**
     * 查看商品详情
     *
     * @param id
     * @param listener
     */
    public static void getGoodsDetail(String id, OnHttpListener listener) {
        RequestParams params = new RequestParams();
        params.add("token", Contants.token);
        params.add("id", id);
        OkHttp.post(Contants.ROOT_URI + "wxapi/v1/goods.php?type=getGoodsInfo", params, listener);
    }

    /**
     * 获取所有的商品的一级二级分类
     *
     * @param listener
     */
    public static void getAllGoodsType(OnHttpListener listener) {
        RequestParams params = new RequestParams();
        params.add("token", Contants.token);
        OkHttp.post(Contants.ROOT_URI + "wxapi/v1/goods.php?type=getGoodsType", params, listener);
    }

    /**
     * 新增或更新一级分类
     *
     * @param listener
     */
    public static void newGoodsTypeOne(String id, String name, OnHttpListener listener) {
        RequestParams params = new RequestParams();
        params.add("token", Contants.token);
        params.add("id", id);
        params.add("name", name);
        OkHttp.post(Contants.ROOT_URI + "wxapi/v1/goods.php?type=newGoodsTypeOne", params, listener);
    }

    /**
     * 停用或启动分类
     *
     * @param id
     * @param listener
     */
    public static void changeGoodsType(String id, OnHttpListener listener) {
        RequestParams params = new RequestParams();
        params.add("token", Contants.token);
        params.add("id", id);
        OkHttp.post(Contants.ROOT_URI + "wxapi/v1/goods.php?type=changeGoodsType", params, listener);
    }

    /**
     * 删除分类
     *
     * @param id
     * @param listener
     */
    public static void delGoodsType(String id, OnHttpListener listener) {
        RequestParams params = new RequestParams();
        params.add("token", Contants.token);
        params.add("id", id);
        OkHttp.post(Contants.ROOT_URI + "wxapi/v1/goods.php?type=delGoodsType", params, listener);
    }

    /**
     * 新增或更新二级分类
     *
     * @param type
     * @param id
     * @param name
     * @param listener
     */
    public static void newGoodsTypeChild(String type, String id, String name, OnHttpListener listener) {
        RequestParams params = new RequestParams();
        params.add("token", Contants.token);
        params.add("id", id);
        params.add("name", name);
        params.add("type", type);
        OkHttp.post(Contants.ROOT_URI + "wxapi/v1/goods.php?type=newGoodsTypeChild", params, listener);
    }

    /**
     * 获取商品一级分类列表   -
     *
     * @param listener
     */
    public static void getGoodsType(OnHttpListener listener) {
        RequestParams params = new RequestParams();
        params.add("token", Contants.token);
        OkHttp.post(Contants.ROOT_URI + "wxapi/v1/goods.php?type=getNewGoodsType", params, listener);
    }

    /**
     * 根据分类id获取二级分类列表
     *
     * @param id
     * @param listener
     */
    public static void getGoodsTypeTwo(String id, OnHttpListener listener) {
        RequestParams params = new RequestParams();
        params.add("token", Contants.token);
        params.add("id", id);
        Log.d("sxs", "id ======== " + id);
        OkHttp.post(Contants.ROOT_URI + "wxapi/v1/goods.php?type=getNewGoodsTypeTwo", params, listener);
    }

    // 门店管理

    /**
     * 获取门店列表
     *
     * @param listener
     */
    public static void getStorelist(OnHttpListener listener) {
        RequestParams params = new RequestParams();
        params.add("token", Contants.token);
        OkHttp.post(Contants.ROOT_URI + "wxapi/v1/goods.php?type=getStorelist", params, listener);
    }

    /**
     * 查看门店详情
     *
     * @param id       门店ID
     * @param listener
     */
    public static void getStoreDetail(String id, OnHttpListener listener) {
        RequestParams params = new RequestParams();
        params.add("token", Contants.token);
        params.add("id", id);
        OkHttp.post(Contants.ROOT_URI + "wxapi/v1/store.php?type=getStoreInfo", params, listener);
    }

    /**
     * 删除门店
     *
     * @param id
     * @param listener
     */
    public static void delStore(String id, OnHttpListener listener) {
        RequestParams params = new RequestParams();
        params.add("token", Contants.token);
        params.add("id", id);
        OkHttp.post(Contants.ROOT_URI + "wxapi/v1/store.php?type=delStore", params, listener);
    }

    /**
     * 新增或编辑门店信息
     *
     * @param id            门店ID
     * @param name          门店名称
     * @param adDutyId      门店权限ID
     * @param adLoginName   登录名称
     * @param loginTel      登录者电话
     * @param password      密码
     * @param email         邮箱
     * @param tel           电话
     * @param region        地区
     * @param address       详细地址
     * @param companyTaxNum 税号 PIVA
     * @param taxNum        税号 CF
     * @param contactName   联系人
     * @param text          门店描述
     * @param listener
     */
    public static void storerEdit(String id, String name, String adDutyId, String adLoginName, String loginTel,
                                  String password, String email, String tel, String region, String address,
                                  String companyTaxNum, String taxNum, String contactName, String text, OnHttpListener listener) {
        RequestParams params = new RequestParams();
        params.add("token", Contants.token);
        params.add("id", id);
        params.add("name", name);
        params.add("adDutyId", adDutyId);
        params.add("adLoginName", adLoginName);
        params.add("loginTel", loginTel);
        params.add("password", password);
        params.add("email", email);
        params.add("tel", tel);
        params.add("region", region);
        params.add("address", address);
        params.add("companyTaxNum", companyTaxNum);
        params.add("taxNum", taxNum);
        params.add("contactName", contactName);
        params.add("text", text);
        OkHttp.post(Contants.ROOT_URI + "wxapi/v1/store.php?type=storerEdit", params, listener);
    }

    /**
     * 门店新建获取地区列表和权限
     *
     * @param listener
     */
    public static void newStorePage(OnHttpListener listener) {
        RequestParams params = new RequestParams();
        params.add("token", Contants.token);
        OkHttp.post(Contants.ROOT_URI + "wxapi/v1/store.php?type=newStorePage", params, listener);
    }


    // 订单管理

    /**
     * query订单列表
     *
     * @param type     online/线上，offline/线下
     * @param payType  支付方式
     * @param workFlow
     * @param keyWord
     * @param listener
     */
    public static void getOrderList(String type, String payType, String workFlow, String keyWord, OnHttpListener listener) {
        RequestParams params = new RequestParams();
        params.add("token", Contants.token);
        params.add("type", type);
        params.add("payType", payType);
        params.add("workFlow", workFlow);
        params.add("keyWord", keyWord);
        OkHttp.post(Contants.ROOT_URI + "wxapi/v1/order.php?type=getBuyCarlist", params, listener);
    }

    /**
     * 获取订单管理详情
     *
     * @param id
     * @param listener
     */
    public static void getBuyCarDetail(String id, OnHttpListener listener) {
        RequestParams params = new RequestParams();
        params.add("token", Contants.token);
        params.add("id", id);
        OkHttp.post(Contants.ROOT_URI + "wxapi/v1/order.php?type=getBuyCarInfo", params, listener);
    }

    /**
     * 确认收货或确认收款
     *
     * @param id       订单id
     * @param workFlow 确认发货：waitRec 确认收款：complete
     * @param payType  支付方式 确认收款时传
     * @param listener
     */
    public static void changeBuyCarWorkFlow(String id, String workFlow, String payType, OnHttpListener listener) {
        RequestParams params = new RequestParams();
        params.add("token", Contants.token);
        params.add("id", id);
        params.add("workFlow", workFlow);
        params.add("payType", payType);
        OkHttp.post(Contants.ROOT_URI + "wxapi/v1/order.php?type=changeBuyCarWorkFlow", params, listener);
    }

    /**
     * 销售订单退货页
     *
     * @param id       订单商品明细ID
     * @param listener
     */
    public static void getBuyCarRefundPage(String id, OnHttpListener listener) {
        RequestParams params = new RequestParams();
        params.add("token", Contants.token);
        params.add("id", id);
        OkHttp.post(Contants.ROOT_URI + "wxapi/v1/order.php?type=getBuyCarRefundPage", params, listener);
    }

    /**
     * 提交销售退货单
     *
     * @param id           订单商品明细id
     * @param num          数量
     * @param text         退货原因
     * @param freightMoney 运费
     * @param listener
     */
    public static void commitBuyCarRefundOrder(String id, String num, String text, String freightMoney, OnHttpListener listener) {
        RequestParams params = new RequestParams();
        params.add("token", Contants.token);
        params.add("id", id);
        params.add("num", num);
        params.add("text", text);
        params.add("freightMoney", freightMoney);
        OkHttp.post(Contants.ROOT_URI + "wxapi/v1/order.php?type=commitBuyCarRefundOrder", params, listener);
    }

    /**
     * 新建订单页面数据
     *
     * @param listener
     */
    public static void newBuyCarPage(OnHttpListener listener) {
        RequestParams params = new RequestParams();
        params.add("token", Contants.token);
        OkHttp.post(Contants.ROOT_URI + "wxapi/v1/order.php?type=newBuyCarPage", params, listener);
    }

    // 个人中心

    /**
     * 个人中心
     *
     * @param listener
     */
    public static void getPersonalInfo(OnHttpListener listener) {
        RequestParams params = new RequestParams();
        params.add("token", Contants.token);
        OkHttp.post(Contants.ROOT_URI + "wxapi/v1/index.php?type=getPerson", params, listener);
    }

    /**
     * 门店或开票信息
     *
     * @param listener
     */
    public static void storeOrInvoice(OnHttpListener listener) {
        RequestParams params = new RequestParams();
        params.add("token", Contants.token);
        OkHttp.post(Contants.ROOT_URI + "wxapi/v1/index.php?type=getStoreInfo", params, listener);
    }

    /**
     * 获取验证码
     *
     * @param tel      手机号
     * @param type     客户类型
     * @param listener
     */
    public static void getVerifyCode(String tel, String type, OnHttpListener listener) {
        RequestParams params = new RequestParams();
        params.add("token", Contants.token);
        params.add("tel", tel);
        params.add("type", type);
        OkHttp.post(Contants.ROOT_URI + "wxapi/v1/login.php?type=getCode", params, listener);
    }

    /**
     * 修改密码
     *
     * @param listener
     */
    public static void modifyPwd(String prove, String newPas, String surePas, OnHttpListener listener) {
        RequestParams params = new RequestParams();
        params.add("token", Contants.token);
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
        params.add("token", Contants.token);
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
        params.add("token", Contants.token);
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
        params.add("token", Contants.token);
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
        params.add("token", Contants.token);
        params.add("skuId", skuId);
        params.add("num", num);
        params.add("text", text);
        params.add("deliveryDate", deliveryDate);
        OkHttp.post(Contants.ROOT_URI + "wxapi/v1/stock.php?type=commitChargeOrder", params, listener);
    }

    // 供应商

    /**
     * 获取供应商列表
     *
     * @param name     供应商名称
     * @param listener
     */
    public static void getSupplierlist(String name, OnHttpListener listener) {
        RequestParams params = new RequestParams();
        params.add("token", Contants.token);
        params.add("name", name);
        OkHttp.post(Contants.ROOT_URI + "wxapi/v1/supplier.php?type=getSupplierlist", params, listener);
    }

    /**
     * 新建供应商页面 -- 获取支付类型和地区选择
     *
     * @param listener
     */
    public static void newSupplierPage(OnHttpListener listener) {
        RequestParams params = new RequestParams();
        params.add("token", Contants.token);
        OkHttp.post(Contants.ROOT_URI + "wxapi/v1/supplier.php?type=newSupplierPage", params, listener);
    }

    /**
     * 供应商详情
     *
     * @param id       供应商ID
     * @param listener
     */
    public static void getSupplierDetail(String id, OnHttpListener listener) {
        RequestParams params = new RequestParams();
        params.add("token", Contants.token);
        params.add("id", id);
        OkHttp.post(Contants.ROOT_URI + "wxapi/v1/supplier.php?type=getSupplierInfo", params, listener);
    }

    /**
     * 新增或更新供应商
     *
     * @param id          供应商ID
     * @param name        供应商名称
     * @param tel         联系电话
     * @param chargeNamem 负责人名称
     * @param bankNum     银行账号
     * @param email       邮箱
     * @param area        国家/地区
     * @param address     详细地址
     * @param payType     支付方式
     * @param listener
     */
    public static void supplierEdit(String id, String name, String tel, String chargeNamem, String bankNum,
                                    String email, String area, String address, String payType, OnHttpListener listener) {
        RequestParams params = new RequestParams();
        params.add("token", Contants.token);
        params.add("id", id);
        params.add("name", name);
        params.add("tel", tel);
        params.add("chargeNamem", chargeNamem);
        params.add("bankNum", bankNum);
        params.add("email", email);
        params.add("area", area);
        params.add("address", address);
        params.add("payType", payType);
        OkHttp.post(Contants.ROOT_URI + "wxapi/v1/supplier.php?type=supplierEdit", params, listener);
    }

    /**
     * 删除供应商
     *
     * @param id
     * @param listener
     */
    public static void delSupplier(String id, OnHttpListener listener) {
        RequestParams params = new RequestParams();
        params.add("token", Contants.token);
        params.add("id", id);
        OkHttp.post(Contants.ROOT_URI + "wxapi/v1/supplier.php?type=delSupplier", params, listener);
    }


    /**
     * 账户登录
     *
     * @param userName 用户名
     * @param pwd      密码
     * @param listener
     */
    public static void loginIn(String userName, String pwd, OnHttpListener listener) {
        RequestParams params = new RequestParams();
        params.add("token", Contants.token);
        params.add("userName", userName);
        params.add("pwd", pwd);
        OkHttp.post(Contants.ROOT_URI + "wxapi/v1/login.php?type=loginIn", params, listener);
    }

    /**
     * 验证码比较
     *
     * @param prove
     * @param listener
     */
    public static void compareCode(String prove, OnHttpListener listener) {
        RequestParams params = new RequestParams();
        params.add("token", Contants.token);
        params.add("prove", prove);
        OkHttp.post(Contants.ROOT_URI + "wxapi/v1/login.php?type=compareCode", params, listener);
    }

    /**
     * 忘记密码修改
     *
     * @param newPas   新密码
     * @param surePas  确认密码
     * @param listener
     */
    public static void resetPwd(String newPas, String surePas, OnHttpListener listener) {
        RequestParams params = new RequestParams();
        params.add("token", Contants.token);
        params.add("newPas", newPas);
        params.add("surePas", surePas);
        OkHttp.post(Contants.ROOT_URI + "wxapi/v1/login.php?type=changePwd", params, listener);
    }

    /**
     * 一键报警
     *
     * @param pwd      输入密码
     * @param listener
     */
    public static void callPlat(String pwd, OnHttpListener listener) {
        RequestParams params = new RequestParams();
        params.add("token", Contants.token);
        params.add("pwd", pwd);
        OkHttp.post(Contants.ROOT_URI + "wxapi/v1/index.php?type=callPlat", params, listener);
    }

    /**
     * 新建商品页面
     *
     * @param listener
     */
    public static void getNewGoodsPage(OnHttpListener listener) {
        RequestParams params = new RequestParams();
        params.add("token", Contants.token);
        OkHttp.post(Contants.ROOT_URI + "wxapi/v1/goods.php?type=getNewGoodsPage", params, listener);
    }

    /**
     * 新增或者更新商品信息
     *
     * @param id             商品id 更新时传
     * @param type           commit 提交/save 保存
     * @param name           商品名称
     * @param goodsTypeId    分类ID
     * @param model          商品型号
     * @param barCode        条码
     * @param goodsUnit      商品单位
     * @param goodsOption    商品选项 有尺码/混装
     * @param tradePrice     批发价
     * @param retailPrice    零售价
     * @param startNum       最小起订量
     * @param isTax          是否有增值税 1：有 0：无
     * @param isPromotion    是否为促销商品 是/否
     * @param startDate      开始日期
     * @param endDate        结束日期
     * @param promotionPrice 促销价
     * @param discount       折扣
     * @param goodsWinAll    商品图片数组
     * @param listener
     */
    public static void goodsInfoEdit(String id, String type, String name, String goodsTypeId, String model,
                                     String barCode, String goodsUnit, String goodsOption, String tradePrice,
                                     String retailPrice, String startNum, String isTax, String isPromotion, String startDate,
                                     String endDate, String promotionPrice, String discount, List<File> goodsWinAll, OnHttpListener listener) {
        RequestParams params = new RequestParams();
        params.add("token", Contants.token);
        params.add("id", id);
        params.add("type", type);
        params.add("name", name);
        params.add("goodsTypeId", goodsTypeId);
        params.add("model", model);
        params.add("barCode", barCode);
        params.add("goodsUnit", goodsUnit);
        params.add("goodsOption", goodsOption);
        params.add("tradePrice", tradePrice);
        params.add("retailPrice", retailPrice);
        params.add("startNum", startNum);
        params.add("isTax", isTax);
        params.add("isPromotion", isPromotion);
        params.add("startDate", startDate);
        params.add("endDate", endDate);
        params.add("promotionPrice", promotionPrice);
        params.add("discount", discount);
        /*for (int i = 0; i < ListUtils.getSize(goodsWinAll); i++) {
            params.add("goodsWinAll["+ i +"]", goodsWinAll.get(i));
        }*/
        // params.add("goodsWinAll", goodsWinAll.get(0));
        OkHttp.post(Contants.ROOT_URI + "wxapi/v1/goods.php?type=goodsInfoEdit", params, listener);
    }

    /**
     * 添加规格页面
     *
     * @param id       商品id
     * @param listener
     */
    public static void getGoodsSkulist(String id, OnHttpListener listener) {
        RequestParams params = new RequestParams();
        params.add("token", Contants.token);
        params.add("id", id);
        OkHttp.post(Contants.ROOT_URI + "wxapi/v1/goods.php?type=getGoodsSkulist", params, listener);
    }

    /**
     * 获取颜色或尺寸
     *
     * @param listener
     */
    public static void getGoodsSizeColor(OnHttpListener listener) {
        RequestParams params = new RequestParams();
        params.add("token", Contants.token);
        OkHttp.post(Contants.ROOT_URI + "wxapi/v1/goods.php?type=getGoodsSizeColor", params, listener);
    }

    /**
     * 商品规格新建或编辑
     *
     * @param id
     * @param goodsId
     * @param goodsSize
     * @param goodsColor
     * @param tradePrice
     * @param iowerLimit
     * @param listener
     */
    public static void goodsSkuEdit(String id, String goodsId, String goodsSize, String goodsColor, String tradePrice, String iowerLimit, OnHttpListener listener) {
        RequestParams params = new RequestParams();
        params.add("token", Contants.token);
        params.add("id", id);
        params.add("goodsId", goodsId);
        params.add("goodsSize", goodsSize);
        params.add("goodsColor", goodsColor);
        params.add("tradePrice", tradePrice);
        params.add("iowerLimit", iowerLimit);
        OkHttp.post(Contants.ROOT_URI + "wxapi/v1/goods.php?type=goodsSkuEdit", params, listener);
    }

    /**
     * 获取商品列表
     *
     * @param keyWord    搜索关键字
     * @param classifyId 商品分类id
     * @param listener
     */
    public static void getGoodsList(String keyWord, String classifyId, OnHttpListener listener) {
        RequestParams params = new RequestParams();
        params.add("token", Contants.token);
        params.add("keyWord", keyWord);
        params.add("classifyId", classifyId);
        OkHttp.post(Contants.ROOT_URI + "wxapi/v1/goods.php?type=getGoods", params, listener);
    }

    /**
     * 获取商品规格
     *
     * @param goodsId  商品id
     * @param listener
     */
    public static void getGoodsSpecial(String goodsId, OnHttpListener listener) {
        RequestParams params = new RequestParams();
        params.add("token", Contants.token);
        params.add("goodsId", goodsId);
        OkHttp.post(Contants.ROOT_URI + "wxapi/v1/order.php?type=getGoodsSku", params, listener);
    }

    /**
     * 新建采购订单
     *
     * @param supplierId    供应商id
     * @param goodsId       商品id
     * @param taxRate       税率
     * @param deliveryDate  交付日期
     * @param payType       支付方式
     * @param text          备注
     * @param discount      折扣
     * @param newPrice      优惠单价
     * @param discountMoney 折扣总价
     * @param skuAll        规格数组
     * @param listener
     */
    public static void purchaseOrderEdit(String supplierId, String goodsId, String taxRate, String deliveryDate,
                                         String payType, String text, String discount, String newPrice, String discountMoney,
                                         List<ISpecialBean> skuAll, OnHttpListener listener) {
        RequestParams params = new RequestParams();
        params.add("token", Contants.token);
        params.add("supplierId", supplierId);
        params.add("goodsId", goodsId);
        params.add("taxRate", taxRate);
        params.add("deliveryDate", deliveryDate);
        params.add("text", text);
        params.add("discount", discount);
        params.add("newPrice", newPrice);
        params.add("payType", payType);
        params.add("skuAll", JsonParser.parseObject(skuAll));
        OkHttp.post(Contants.ROOT_URI + "wxapi/v1/order.php?type=purchaseOrderEdit", params, listener);
    }

    /**
     * 新建订单
     *
     * @param khid         客户id
     * @param goodsId      商品id
     * @param freightMoney 运费
     * @param discount     折扣
     * @param newPrice     优惠单价
     * @param text         备注
     * @param orderTime    下单时间
     * @param taxRate      税率
     * @param invoiceId    发票地址id
     * @param addressId    收货地址id
     * @param freightType  配送方式
     * @param skuAll       规格数组
     * @param listener
     */
    public static void buyCarEdit(String khid, String goodsId, String freightMoney, String discount, String newPrice,
                                  String text, String orderTime, String taxRate, String invoiceId, String addressId, String freightType,
                                  List<ISpecialBean> skuAll, OnHttpListener listener) {
        RequestParams params = new RequestParams();
        params.add("token", Contants.token);
        params.add("khid", khid);
        params.add("goodsId", goodsId);
        params.add("freightMoney", freightMoney);
        params.add("discount", discount);
        params.add("newPrice", newPrice);
        params.add("text", text);
        params.add("orderTime", orderTime);
        params.add("taxRate", taxRate);
        params.add("invoiceId", invoiceId);
        params.add("addressId", addressId);
        params.add("freightType", freightType);
        params.add("skuAll", JsonParser.parseObject(skuAll));
        //params.addStringBody(JsonParser.parseObject(skuAll));
        OkHttp.post(Contants.ROOT_URI + "wxapi/v1/order.php?type=buyCarEdit", params, listener);
    }
}
