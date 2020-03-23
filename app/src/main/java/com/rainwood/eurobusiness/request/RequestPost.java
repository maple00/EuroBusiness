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
    public static void UnReclist(OnHttpListener listener){
        RequestParams params = new RequestParams();
        OkHttp.post(Contants.ROOT_URI + "wxapi/v1/statistics.php?type=getReclistct", params, listener);
    }

    //     客户管理
    /**
     * 获取客户列表
     * @param name      客户名称
     * @param kehuTypeId 客户id
     * @param listener
     */
    public static void getClientList(String name, String kehuTypeId, OnHttpListener listener){
        RequestParams params = new RequestParams();
        params.add("name", name);
        params.add("kehuTypeId", kehuTypeId);
        OkHttp.post(Contants.ROOT_URI + "wxapi/v1/client.php?type=getClientlist", params, listener);
    }

    /**
     * 获取客户分类列表
     * @param listener
     */
    public static void getClientTypeList(OnHttpListener listener){
        RequestParams params = new RequestParams();
        OkHttp.post(Contants.ROOT_URI + "wxapi/v1/client.php?type=getKehuTypelist", params, listener);
    }

    /**
     * 查看客户详情
      * @param id custom id
     * @param listener
     */
    public static void getClientDetail(String id, OnHttpListener listener){
        RequestParams params = new RequestParams();
        params.add("id", id);
        OkHttp.post(Contants.ROOT_URI + "wxapi/v1/client.php?type=getClientInfo", params, listener);
    }

    // 个人中心

    /**
     * 个人中心
     * @param listener
     */
    public static void getPersonalInfo(OnHttpListener listener){
       RequestParams params = new RequestParams();
       OkHttp.post(Contants.ROOT_URI + "wxapi/v1/index.php?type=getPerson", params, listener);
    }

    /**
     * 门店或开票信息
     * @param listener
     */
    public static void storeOrInvoice(OnHttpListener listener){
        RequestParams params = new RequestParams();
        OkHttp.post(Contants.ROOT_URI + "wxapi/v1/index.php?type=getStoreInfo", params, listener);
    }

    /**
     * 获取验证码
     * @param listener
     */
    public static void getVerifyCode(String tel,OnHttpListener listener){
        RequestParams params = new RequestParams();
        params.add("tel", tel);
        OkHttp.post(Contants.ROOT_URI + "wxapi/v1/login.php?type=getCode", params, listener);
    }

    /**
     * 修改密码
     * @param listener
     */
    public static void modifyPwd(String prove, String newPas, String surePas, OnHttpListener listener){
        RequestParams params = new RequestParams();
        params.add("prove", prove);
        params.add("newPas", newPas);
        params.add("surePas", surePas);
        OkHttp.post(Contants.ROOT_URI + "wxapi/v1/index.php?type=changePwd", params, listener);
    }

    /**
     * 帮助中心
     */
    public static void helpCenter(OnHttpListener listener){
        RequestParams params = new RequestParams();
        OkHttp.post(Contants.ROOT_URI + "wxapi/v1/index.php?type=getHelp", params, listener);
    }

}
