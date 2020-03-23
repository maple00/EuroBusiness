package com.rainwood.eurobusiness.ui.activity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.rainwood.eurobusiness.R;
import com.rainwood.eurobusiness.base.BaseActivity;
import com.rainwood.eurobusiness.common.Contants;
import com.rainwood.eurobusiness.domain.PressBean;
import com.rainwood.eurobusiness.domain.RefundGoodsBean;
import com.rainwood.eurobusiness.domain.ReturnGoodsBean;
import com.rainwood.eurobusiness.json.JsonParser;
import com.rainwood.eurobusiness.okhttp.HttpResponse;
import com.rainwood.eurobusiness.okhttp.OnHttpListener;
import com.rainwood.eurobusiness.request.RequestPost;
import com.rainwood.eurobusiness.ui.adapter.GoodsStatusAdapter;
import com.rainwood.eurobusiness.ui.adapter.ReturnGoodsAdapter;
import com.rainwood.eurobusiness.ui.adapter.ReturnGoodsSalerAdapter;
import com.rainwood.eurobusiness.ui.adapter.TopTypeAdapter;
import com.rainwood.tools.view.ClearEditText;
import com.rainwood.tools.viewinject.ViewById;
import com.rainwood.tools.widget.MeasureListView;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * @Author: a797s
 * @Date: 2020/2/21
 * @Desc:
 */
public class ReturnGoodsActivity extends BaseActivity implements View.OnClickListener, OnHttpListener {

    @Override
    protected int getLayoutId() {
        return R.layout.activity_return_goods;
    }

    @ViewById(R.id.iv_back)
    private ImageView pageBack;
    @ViewById(R.id.tv_search_tips)
    private TextView searchTips;
    @ViewById(R.id.cet_search_hint)
    private ClearEditText searchHint;
    @ViewById(R.id.iv_screening)
    private ImageView screening;            // 筛选
    @ViewById(R.id.gv_top_type)
    private GridView topType;        // 头部类型
    @ViewById(R.id.gv_type_list)
    private GridView typeList;       // 订单类型
    @ViewById(R.id.lv_content_list)
    private MeasureListView contentList;    // content
    // 批发商
    @ViewById(R.id.ll_search_1)
    private LinearLayout search1;
    @ViewById(R.id.ll_search)
    private LinearLayout search;
    @ViewById(R.id.iv_new_found)
    private ImageView newFound;
    // mHandler
    private final int DEFAULT_SIZE = 0x1124;
    private final int SALER_SIZE = 0x0929;

    private List<PressBean> topTypeList;
    private String[] topTypes = {"采购订单退货", "销售订单退货"};
    private List<PressBean> orderTypeList;
    private String[] orderTypes = {"全部", "已完成", "审核中", "草稿"};
    private List<PressBean> saleTypeList;
    private String[] saleType = {"全部", "已完成", "确认中"};
    private List<ReturnGoodsBean> mList;
    // 批发商
    private String[] orderType = {"全部", "已完成", "待审核"};
    private String[] saleTypes = {"全部", "已完成", "确认中", "待审核"};

    private static String mClassify;
    private static String mStatus;

    private List<RefundGoodsBean> goodsList;

    @Override
    protected void initView() {
        pageBack.setOnClickListener(this);
        screening.setOnClickListener(this);

        if (Contants.CHOOSE_MODEL_SIZE == 9) {
            searchTips.setText("退货单号");
            searchHint.setHint("请输入退货单号");
            search1.setVisibility(View.GONE);
            newFound.setVisibility(View.GONE);
        }
        if (Contants.CHOOSE_MODEL_SIZE == 108) {
            search.setVisibility(View.GONE);
            newFound.setVisibility(View.GONE);
        }
    }

    @Override
    protected void initData() {
        super.initData();
        topTypeList = new ArrayList<>();
        for (int i = 0; i < topTypes.length; i++) {
            PressBean press = new PressBean();
            press.setTitle(topTypes[i]);
            if (i == 0) {
                press.setChoose(true);
            }
            topTypeList.add(press);
        }
        // 门店端
        orderTypeList = new ArrayList<>();
        for (int i = 0; i < orderTypes.length && Contants.CHOOSE_MODEL_SIZE == 9; i++) {
            PressBean press = new PressBean();
            press.setTitle(orderTypes[i]);
            if (i == 0) {
                press.setChoose(true);
            }
            orderTypeList.add(press);
        }
        saleTypeList = new ArrayList<>();
        for (int i = 0; i < saleType.length && Contants.CHOOSE_MODEL_SIZE == 9; i++) {
            PressBean press = new PressBean();
            press.setTitle(saleType[i]);
            if (i == 0) {
                press.setChoose(true);
            }
            saleTypeList.add(press);
        }

        // 批发商
        for (int i = 0; i < orderType.length && Contants.CHOOSE_MODEL_SIZE == 108; i++) {
            PressBean press = new PressBean();
            press.setTitle(orderType[i]);
            if (i == 0) {
                press.setChoose(true);
            }
            orderTypeList.add(press);
        }
        // content
        mList = new ArrayList<>();
        for (int i = 0; i < 4; i++) {
            ReturnGoodsBean goods = new ReturnGoodsBean();
            goods.setImgPath(null);
            goods.setName("西装外套式系缀扣连衣裙");
            goods.setParams("红色/XL");
            goods.setReturnNum("50");
            goods.setMoney("156.00€");
            if (i == 3) {
                goods.setStatus("草稿");
            } else if (i == 0) {
                goods.setStatus("审核中");
            } else {
                goods.setStatus("已完成");
            }
            mList.add(goods);
        }

        // request
        requestPost();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_back:
                finish();
                break;
            case R.id.iv_screening:
                toast("筛选");
                break;
        }
    }

    /**
     * request Data
     */
    private void requestPost() {
        // 采购订单、销售订单
        if ("销售订单退货".equals(mClassify)) {
            mClassify = "sale";
        }
        if ("采购订单退货".equals(mClassify)) {
            mClassify = "buy";
        }
        // order status group
        if ("已完成".equals(mStatus)) {
            mStatus = "complete";
        } else if ("待审核".equals(mStatus) || "审核中".equals(mStatus) || "确认中".equals(mStatus)) {
            mStatus = "waitAudit";
        } else if ("草稿".equals(mStatus)) {
            mStatus = "draft";
        } else {
            mStatus = "others";
        }
        showLoading("加载中");
        RequestPost.returnGoodsList(mClassify, mStatus, Contants.SEARCH_CONDITIONS, this);
    }

    // type position record
    private int posCount = 0;

    @SuppressLint("HandlerLeak")
    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(@NonNull Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case DEFAULT_SIZE:
                    // count
                    // 退货订单类型
                    TopTypeAdapter typeAdapter = new TopTypeAdapter(ReturnGoodsActivity.this, topTypeList);
                    topType.setAdapter(typeAdapter);
                    topType.setNumColumns(GridView.AUTO_FIT);
                    typeAdapter.setOnClickItem(position -> {
                        for (PressBean pressBean : topTypeList) {
                            pressBean.setChoose(false);
                        }
                        topTypeList.get(position).setChoose(true);
                        mClassify = topTypeList.get(position).getTitle();
                        // request
                        requestPost();
                        posCount = position;
                    });
                    GoodsStatusAdapter statusAdapter;
                    if (posCount == 0) {
                        statusAdapter = new GoodsStatusAdapter(ReturnGoodsActivity.this, orderTypeList);
                    } else {
                        statusAdapter = new GoodsStatusAdapter(ReturnGoodsActivity.this, saleTypeList);
                    }
                    // 订单类型
                    typeList.setAdapter(statusAdapter);
                    typeList.setNumColumns(5);
                    statusAdapter.setOnClickItem(position1 -> {
                        for (PressBean pressBean : topTypeList) {
                            if (pressBean.isChoose()) {
                                mClassify = topTypeList.get(posCount).getTitle();
                                break;
                            }
                        }
                        if (posCount == 0) {             // 采购订单退货
                            for (PressBean pressBean : orderTypeList) {
                                pressBean.setChoose(false);
                            }
                            orderTypeList.get(position1).setChoose(true);
                            mStatus = orderTypeList.get(position1).getTitle();
                        } else {                         // 销售订单退货
                            for (PressBean pressBean : saleTypeList) {
                                pressBean.setChoose(false);
                            }
                            saleTypeList.get(position1).setChoose(true);
                            mStatus = saleTypeList.get(position1).getTitle();
                        }
                        // request
                        requestPost();
                    });

                    // content
                    ReturnGoodsAdapter goodsAdapter = new ReturnGoodsAdapter(ReturnGoodsActivity.this, goodsList);
                    contentList.setAdapter(goodsAdapter);
                    goodsAdapter.setOnClickItem(position -> {
                        // toast("点击详情");
                        Intent intent = new Intent(ReturnGoodsActivity.this, ReGoodsDetailActivity.class);
                        intent.putExtra("goodsId", goodsList.get(position).getId());
                        startActivity(intent);
                    });
                    break;
                case SALER_SIZE:
                    // 退货订单类型
                    TopTypeAdapter typesAdapter = new TopTypeAdapter(ReturnGoodsActivity.this, topTypeList);
                    topType.setAdapter(typesAdapter);
                    topType.setNumColumns(GridView.AUTO_FIT);
                    typesAdapter.setOnClickItem(position -> {
                        for (PressBean pressBean : topTypeList) {
                            pressBean.setChoose(false);
                        }
                        topTypeList.get(position).setChoose(true);
                    });
                    // 订单类型
                    GoodsStatusAdapter statuAdapter = new GoodsStatusAdapter(ReturnGoodsActivity.this, orderTypeList);
                    typeList.setAdapter(statuAdapter);
                    typeList.setNumColumns(5);
                    statuAdapter.setOnClickItem(position -> {
                        for (PressBean pressBean : orderTypeList) {
                            pressBean.setChoose(false);
                        }
                        orderTypeList.get(position).setChoose(true);
                    });
                    // content
                    ReturnGoodsSalerAdapter salerAdapter = new ReturnGoodsSalerAdapter(ReturnGoodsActivity.this, mList);
                    contentList.setAdapter(salerAdapter);
                    salerAdapter.setOnClickItem(new ReturnGoodsSalerAdapter.OnClickItem() {
                        @Override
                        public void onClickItem(int position) {
                            toast("查看详情");
                        }

                        @Override
                        public void onClickScrap(int position) {
                            toast("报废");
                        }

                        @Override
                        public void onClickStorage(int position) {
                            toast("入库");
                        }
                    });
                    break;
            }
        }
    };

    @Override
    public void onHttpFailure(HttpResponse result) {

    }

    @Override
    public void onHttpSucceed(HttpResponse result) {
        Map<String, String> body = JsonParser.parseJSONObject(result.body());
        if (body != null) {
            if ("1".equals(body.get("code"))) {
                if (result.url().contains("wxapi/v1/order.php?type=getRefundOrder")) {            // 查询退货列表
                    goodsList = JsonParser.parseJSONArray(RefundGoodsBean.class,
                            JsonParser.parseJSONObject(body.get("data")).get("refundlist"));
                    dismissLoading();
                    if (Contants.CHOOSE_MODEL_SIZE == 9) {
                        Message msg = new Message();
                        msg.what = DEFAULT_SIZE;
                        mHandler.sendMessage(msg);
                    }
                    if (Contants.CHOOSE_MODEL_SIZE == 108) {
                        Message msg = new Message();
                        msg.what = SALER_SIZE;
                        mHandler.sendMessage(msg);
                    }
                }
            } else {
                toast(body.get("warn"));
            }
            dismissLoading();
        }
    }
}
