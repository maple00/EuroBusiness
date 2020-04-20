package com.rainwood.eurobusiness.ui.activity;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
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
import com.rainwood.eurobusiness.json.JsonParser;
import com.rainwood.eurobusiness.okhttp.HttpResponse;
import com.rainwood.eurobusiness.okhttp.OnHttpListener;
import com.rainwood.eurobusiness.other.BaseDialog;
import com.rainwood.eurobusiness.request.RequestPost;
import com.rainwood.eurobusiness.ui.adapter.GoodsStatusAdapter;
import com.rainwood.eurobusiness.ui.adapter.ReturnGoodsAdapter;
import com.rainwood.eurobusiness.ui.adapter.ReturnGoodsSalerAdapter;
import com.rainwood.eurobusiness.ui.adapter.TopTypeAdapter;
import com.rainwood.eurobusiness.ui.dialog.DateDialog;
import com.rainwood.tools.refresh.DaisyRefreshLayout;
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
    @ViewById(R.id.drl_refresh)
    private DaisyRefreshLayout mRefreshLayout;

    // mHandler
    private final int DEFAULT_SIZE = 0x1124;
    private final int SALER_SIZE = 0x0929;
    private final int REFRESH_SIZE = 0x102;
    private static int pageCount = 0;

    private List<PressBean> topTypeList;
    private String[] topTypes = {"采购订单退货", "销售订单退货"};
    // 门店端的采购单状态
    private String[] orderTypes = {"全部", "已完成", "审核中", "草稿"};
    private List<PressBean> saleTypeList;
    // 门店端销售单状态
    private String[] saleType = {"全部", "已完成", "待入库", "待审核"};
    private List<RefundGoodsBean> mList;
    // 批发商
    private List<PressBean> orderTypeList;
    // 批发商端的采购单状态
    private String[] orderType = {"全部", "已完成", "待审核", "退货中"};
    // 销售单状态
    private String[] saleTypes = {"全部", "已完成", "待审核", "待入库"};

    private static String mClassify;
    private static String mStatus;

    private List<RefundGoodsBean> mCopyGoodsList = new ArrayList<>();

    @Override
    protected void initView() {
        pageBack.setOnClickListener(this);
        screening.setOnClickListener(this);

        // 门店端
        if (Contants.CHOOSE_MODEL_SIZE == 9) {
            searchTips.setText("退货单号");
            searchHint.setHint("请输入退货单号");
        }
        // 批发商端
        if (Contants.CHOOSE_MODEL_SIZE == 108) {
            search.setVisibility(View.GONE);
            newFound.setVisibility(View.GONE);
        }

        Message msg = new Message();
        msg.what = REFRESH_SIZE;
        mHandler.sendMessage(msg);
    }

    @Override
    protected void onResume() {
        super.onResume();
        pageCount = 0;
        mCopyGoodsList = new ArrayList<>();
        requestPost("", "");
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
        // 采购单类型
        orderTypeList = new ArrayList<>();
        for (int i = 0; i < orderTypes.length && Contants.CHOOSE_MODEL_SIZE == 9; i++) {
            PressBean press = new PressBean();
            press.setTitle(orderTypes[i]);
            if (i == 0) {
                press.setChoose(true);
            }
            orderTypeList.add(press);
        }
        // 销售单类型
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
        // 采购单类型
        for (int i = 0; i < orderType.length && Contants.CHOOSE_MODEL_SIZE == 108; i++) {
            PressBean press = new PressBean();
            press.setTitle(orderType[i]);
            if (i == 0) {
                press.setChoose(true);
            }
            orderTypeList.add(press);
        }
        // 销售单类型
        for (int i = 0; i < saleTypes.length && Contants.CHOOSE_MODEL_SIZE == 108; i++) {
            PressBean press = new PressBean();
            press.setTitle(saleTypes[i]);
            if (i == 0) {
                press.setChoose(true);
            }
            saleTypeList.add(press);
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_back:
                finish();
                break;
            case R.id.iv_screening:
                // toast("筛选");
                getCusTomDialog();
                break;
        }
    }

    /**
     * 选择时间段 Dialog
     */
    private void getCusTomDialog() {
        Dialog dialog = new Dialog(this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.dialog_period);
        Window dialogWindow = dialog.getWindow();
        dialogWindow.setGravity(Gravity.CENTER);
        dialogWindow.getDecorView().setPadding(0, 30, 0, 30);
        WindowManager.LayoutParams lp = dialogWindow.getAttributes();
        lp.width = WindowManager.LayoutParams.MATCH_PARENT;
        lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
        dialogWindow.setAttributes(lp);
        dialog.show();

        TextView startTime = dialog.findViewById(R.id.tv_start_time);
        startTime.setFocusable(true);
        startTime.setOnClickListener(v -> {
            // 日期选择对话框
            getDateDialog(startTime);
        });
        TextView endTime = dialog.findViewById(R.id.tv_end_time);
        endTime.setOnClickListener(v -> getDateDialog(endTime));
        // 监听
        TextView clear = dialog.findViewById(R.id.tv_clear_screening);
        clear.setOnClickListener(v -> {
            startTime.setText("");
            endTime.setText("");
            postAtTime(() -> toast("清除完成!"), 500);
        });
        TextView confirm = dialog.findViewById(R.id.tv_confirm);
        confirm.setOnClickListener(v -> {
            if (TextUtils.isEmpty(startTime.getText()) || TextUtils.isEmpty(endTime.getText())) {
                toast("选择时间范围不完整!");
                return;
            }

            // request
            dialog.dismiss();
            pageCount = 0;
            mCopyGoodsList = new ArrayList<>();
            requestPost(startTime.getText().toString().trim(), endTime.getText().toString().trim());
        });
        // dialog  dismiss 监听
        dialog.setOnDismissListener(DialogInterface::dismiss);
    }

    /**
     * 选择时间范围
     *
     * @param time TextView
     */
    private void getDateDialog(TextView time) {
        new DateDialog.Builder(this)
                .setTitle(getString(R.string.date_title))
                .setConfirm(getString(R.string.common_confirm))
                .setCancel(null)
                //.setIgnoreDay()
                .setListener(new DateDialog.OnListener() {
                    @SuppressLint("SetTextI18n")
                    @Override
                    public void onSelected(BaseDialog dialog, int year, int month, int day) {
                        // toast(year + "-" + "-" + month + "-" + day);
                        time.setText(year + "-" + (month < 10 ? ("0" + month) : month) + "-" + (day < 10 ? ("0" + day) : day));
                    }

                    @Override
                    public void onCancel(BaseDialog dialog) {
                        dialog.dismiss();
                    }
                }).show();
    }

    /**
     * request Data
     */
    private void requestPost(String startTime, String endTime) {
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
            mStatus = "";
        }
        // showLoading("");
        RequestPost.returnGoodsList(String.valueOf(pageCount), mClassify, mStatus, Contants.SEARCH_CONDITIONS, startTime, endTime, this);
    }

    // type position record
    private int posCount = 0;

    @SuppressLint("HandlerLeak")
    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(@NonNull Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case DEFAULT_SIZE:          // 门店端
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
                        mCopyGoodsList = new ArrayList<>();
                        requestPost("", "");
                        showLoading("");
                        posCount = position;
                    });
                    // 加载不同的状态
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
                        showLoading("");
                        mCopyGoodsList = new ArrayList<>();
                        requestPost("", "");
                    });

                    // content
                    ReturnGoodsAdapter goodsAdapter = new ReturnGoodsAdapter(posCount, ReturnGoodsActivity.this, mCopyGoodsList);
                    contentList.setAdapter(goodsAdapter);
                    goodsAdapter.setOnClickItem(new ReturnGoodsAdapter.OnClickItem() {
                        @Override
                        public void onClickItem(int position) {
                            Intent intent = new Intent(ReturnGoodsActivity.this, ReGoodsDetailActivity.class);
                            intent.putExtra("goodsId", mCopyGoodsList.get(position).getId());
                            intent.putExtra("flag", "store");
                            startActivity(intent);
                        }

                        @Override
                        public void onClickScrap(int position) {
                            // toast("报废");
                            Intent intent = new Intent(ReturnGoodsActivity.this, ScrapInActivity.class);
                            intent.putExtra("goodsId", mCopyGoodsList.get(position).getId());
                            intent.putExtra("flag", "scrap");
                            startActivity(intent);
                        }

                        @Override
                        public void onClickStorage(int position) {
                            Intent intent = new Intent(ReturnGoodsActivity.this, ScrapInActivity.class);
                            intent.putExtra("goodsId", mCopyGoodsList.get(position).getId());
                            intent.putExtra("flag", "inStorage");
                            startActivity(intent);
                        }
                    });
                    break;
                case SALER_SIZE:            // 批发商端
                    // 退货订单类型
                    TopTypeAdapter typesAdapter = new TopTypeAdapter(ReturnGoodsActivity.this, topTypeList);
                    topType.setAdapter(typesAdapter);
                    topType.setNumColumns(GridView.AUTO_FIT);
                    typesAdapter.setOnClickItem(position -> {
                        for (PressBean pressBean : topTypeList) {
                            pressBean.setChoose(false);
                        }
                        topTypeList.get(position).setChoose(true);
                        mClassify = topTypeList.get(position).getTitle();
                        // request
                        mCopyGoodsList = new ArrayList<>();
                        posCount = position;
                        showLoading("");
                        requestPost("", "");
                    });
                    // 加载不同的状态
                    GoodsStatusAdapter topStateAdapter;
                    if (posCount == 0) {
                        topStateAdapter = new GoodsStatusAdapter(ReturnGoodsActivity.this, orderTypeList);
                    } else {
                        topStateAdapter = new GoodsStatusAdapter(ReturnGoodsActivity.this, saleTypeList);
                    }
                    // 订单类型
                    // GoodsStatusAdapter statuAdapter = new GoodsStatusAdapter(ReturnGoodsActivity.this, orderTypeList);
                    typeList.setAdapter(topStateAdapter);
                    typeList.setNumColumns(5);
                    topStateAdapter.setOnClickItem(position -> {
                        for (PressBean pressBean : topTypeList) {
                            if (pressBean.isChoose()) {
                                mClassify = topTypeList.get(posCount).getTitle();
                                break;
                            }
                        }
                        if (posCount == 0) {
                            for (PressBean pressBean : orderTypeList) {
                                pressBean.setChoose(false);
                            }
                            orderTypeList.get(position).setChoose(true);
                            mStatus = orderTypeList.get(position).getTitle();
                        } else {
                            for (PressBean bean : saleTypeList) {
                                bean.setChoose(false);
                            }
                            saleTypeList.get(position).setChoose(true);
                            mStatus = orderTypeList.get(position).getTitle();
                        }
                        // request
                        showLoading("");
                        mList = new ArrayList<>();
                        requestPost("", "");
                    });
                    // content -- 采购单可以审批，销售单只能查看详情
                    ReturnGoodsSalerAdapter salerAdapter = new ReturnGoodsSalerAdapter(posCount, ReturnGoodsActivity.this, mList);
                    contentList.setAdapter(salerAdapter);
                    salerAdapter.setOnClickItem(position -> {
                        Intent intent = new Intent(ReturnGoodsActivity.this, ReGoodsDetailActivity.class);
                        intent.putExtra("goodsId", mList.get(position).getId());
                        intent.putExtra("flag", "seller");
                        startActivity(intent);
                    });
                    break;
                case REFRESH_SIZE:
                    //上拉加载
                    mRefreshLayout.setOnLoadMoreListener(() -> {
                        pageCount++;
                        requestPost("", "");
                    });
                    // 下拉刷新
                    mRefreshLayout.setOnRefreshListener(() -> {
                        pageCount = 0;
                        mCopyGoodsList = new ArrayList<>();
                        requestPost("", "");
                    });
                    // 第一次进来的时候刷新
                    mRefreshLayout.setOnAutoLoadListener(() -> {
                        // request
                        pageCount = 0;
                        mCopyGoodsList = new ArrayList<>();
                        requestPost("", "");
                    });
                    mRefreshLayout.autoRefresh();
                    break;
            }
        }
    };

    @Override
    public void onHttpFailure(HttpResponse result) {

    }

    @Override
    public void onHttpSucceed(HttpResponse result) {
        mRefreshLayout.setLoadMore(false);
        mRefreshLayout.setRefreshing(false);
        Map<String, String> body = JsonParser.parseJSONObject(result.body());
        if (body != null) {
            if ("1".equals(body.get("code"))) {
                if (result.url().contains("wxapi/v1/order.php?type=getRefundOrder")) {            // 查询退货列表
                    if (Contants.CHOOSE_MODEL_SIZE == 9) {                          // 门店端
                        List<RefundGoodsBean> goodsList = JsonParser.parseJSONArray(RefundGoodsBean.class,
                                JsonParser.parseJSONObject(body.get("data")).get("refundlist"));
                        if (goodsList != null) {
                            mCopyGoodsList.addAll(goodsList);
                            Message msg = new Message();
                            msg.what = DEFAULT_SIZE;
                            mHandler.sendMessage(msg);
                        }
                    }
                    if (Contants.CHOOSE_MODEL_SIZE == 108) {                        // 批发商端
                        mList = JsonParser.parseJSONArray(RefundGoodsBean.class,
                                JsonParser.parseJSONObject(body.get("data")).get("refundlist"));
                        Message msg = new Message();
                        msg.what = SALER_SIZE;
                        mHandler.sendMessage(msg);
                    }
                }
            } else {
                toast(body.get("warn"));
            }
            if (getDialog() != null)
                dismissLoading();
        }
    }
}
