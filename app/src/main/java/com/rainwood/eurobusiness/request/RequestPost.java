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
     *  退货列表
     * @param classify buy/采购订单，sale/销售订单
     * @param workFlow 订单状态 draft:草稿 waitAudit:待审批 complete:已完成
     * @param refundNo 退货单号
     * @param listener listener
     */
    public static void returnGoodsList(String classify, String workFlow, String refundNo, OnHttpListener listener){
        RequestParams params = new RequestParams();
        params.add("classify", classify);
        params.add("workFlow", workFlow);
        params.add("refundNo",refundNo);
        Log.d("sxs", " - classify: " + classify + " - workFlow: " + workFlow + " - refundNo: " + refundNo);
        OkHttp.post(Contants.ROOT_URI + "wxapi/v1/order.php?type=getRefundOrder", params, listener);
    }

    /**
     * 退货订单详情
     * @param id 查询id
     * @param listener
     */
    public static void returnGoodsDetail(String id, OnHttpListener listener){
        RequestParams params = new RequestParams();
        params.add("id", id);
        OkHttp.post(Contants.ROOT_URI + "wxapi/v1/order.php?type=getRefundOrderInfo", params, listener);
    }

    /**
     * 订单销售统计页
     */
    public static void saleTotal(OnHttpListener listener){
        RequestParams params = new RequestParams();
        OkHttp.post(Contants.ROOT_URI + "wxapi/v1/statistics.php?type=getTotal", params, listener);
    }

}
