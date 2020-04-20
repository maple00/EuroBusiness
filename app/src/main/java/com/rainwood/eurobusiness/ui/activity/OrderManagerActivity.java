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
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.rainwood.eurobusiness.R;
import com.rainwood.eurobusiness.base.BaseActivity;
import com.rainwood.eurobusiness.common.Contants;
import com.rainwood.eurobusiness.domain.ItemGridBean;
import com.rainwood.eurobusiness.domain.OrderListBean;
import com.rainwood.eurobusiness.domain.PressBean;
import com.rainwood.eurobusiness.domain.PrintBean;
import com.rainwood.eurobusiness.json.JsonParser;
import com.rainwood.eurobusiness.okhttp.HttpResponse;
import com.rainwood.eurobusiness.okhttp.OnHttpListener;
import com.rainwood.eurobusiness.other.BaseDialog;
import com.rainwood.eurobusiness.request.RequestPost;
import com.rainwood.eurobusiness.ui.adapter.GoodsStatusAdapter;
import com.rainwood.eurobusiness.ui.adapter.LevelTypeAdapter;
import com.rainwood.eurobusiness.ui.adapter.OrderContentAdapter;
import com.rainwood.eurobusiness.ui.dialog.DateDialog;
import com.rainwood.eurobusiness.ui.dialog.MenuDialog;
import com.rainwood.eurobusiness.ui.widget.CustomDialog;
import com.rainwood.tools.common.FontDisplayUtil;
import com.rainwood.tools.refresh.DaisyRefreshLayout;
import com.rainwood.tools.view.ClearEditText;
import com.rainwood.tools.viewinject.ViewById;
import com.rainwood.tools.widget.MeasureGridView;
import com.rainwood.tools.widget.MeasureListView;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * @Author: a797s
 * @Date: 2020/2/19
 * @Desc: 订单管理
 */
public class OrderManagerActivity extends BaseActivity implements View.OnClickListener, OnHttpListener {

    @Override
    protected int getLayoutId() {
        return R.layout.activity_order_manager;
    }

    @ViewById(R.id.iv_back)
    private ImageView pageBack;
    @ViewById(R.id.ll_search)
    private LinearLayout searchLine;            // 搜索框
    @ViewById(R.id.et_search)
    private ClearEditText searchContent;        // 搜索框
    @ViewById(R.id.iv_new)
    private ImageView newOreder;                // 新建订单
    @ViewById(R.id.iv_screen)
    private ImageView screen;
    @ViewById(R.id.gv_order)
    private MeasureGridView orderPay;           // 订单状态、订单支付
    @ViewById(R.id.tv_print)
    private TextView print;                     // 打印
    @ViewById(R.id.gv_order_type)
    private MeasureGridView orderType;          // 订单类型
    @ViewById(R.id.iv_screening)
    private ImageView screening;                // 筛选
    @ViewById(R.id.lv_content_list)
    private MeasureListView contentList;        // 订单列表
    @ViewById(R.id.tv_order_tips)
    private TextView orderTips;                 // 提示没有更多订单了
    @ViewById(R.id.drl_refresh)
    private DaisyRefreshLayout mRefreshLayout;
    // mHandler
    private final int PRINT_SIZE = 0x1124;
    private final int INITIAL_SIZE = 0x101;
    private final int REFRESH_SIZE = 0x102;
    private static int pageCount = 0;
    private static int posFalg;

    private List<OrderListBean> mCopyOrderList = new ArrayList<>();
    //
    private List<ItemGridBean> orderPayList;
    private String[] orderPays = {"订单状态", "支付方式"};
    // 订单状态
    private List<PressBean> statuList;
    private String[] status = {"全部", "线上订单", "线下订单"};
    // 支付方式
    private List<String> payMethodList;
    private List<String> orderStateList;

    @Override
    protected void initView() {
        pageBack.setOnClickListener(this);
        newOreder.setOnClickListener(this);
        print.setOnClickListener(this);
        screening.setOnClickListener(this);
        if (Contants.CHOOSE_MODEL_SIZE == 106) {         // 批发商端
            newOreder.setVisibility(View.GONE);
            screen.setVisibility(View.VISIBLE);
            screening.setVisibility(View.GONE);
        }
        if (Contants.CHOOSE_MODEL_SIZE == 4) {           // 门店端
            newOreder.setVisibility(View.VISIBLE);
            screen.setVisibility(View.GONE);
            screening.setVisibility(View.VISIBLE);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        Message msg = new Message();
        msg.what = REFRESH_SIZE;
        mHandler.sendMessage(msg);
    }

    @Override
    protected void initData() {
        super.initData();
        // 订单支付情况
        orderPayList = new ArrayList<>();
        for (String level : orderPays) {
            ItemGridBean itemGrid = new ItemGridBean();
            itemGrid.setItemName(level);
            itemGrid.setImgId(R.drawable.ic_down_selector);
            orderPayList.add(itemGrid);
        }
        // 订单状态
        statuList = new ArrayList<>();
        for (int i = 0; i < status.length; i++) {
            PressBean press = new PressBean();
            press.setChoose(false);
            press.setTitle(status[i]);
            if (status[i].equals("全部")) {
                press.setChoose(true);
            }
            statuList.add(press);
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_back:
                finish();
                break;
            case R.id.iv_new:
                openActivity(OrderNewActivity.class);
                break;
            case R.id.tv_print:
                // toast("打印");
                Message msg = new Message();
                msg.what = PRINT_SIZE;
                mHandler.sendMessage(msg);
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
            // 通过时间查询订单列表 -- 默认查询当前选中的类型的全部状态
            // request
            showLoading("loading");
            dialog.dismiss();
            pageCount = 0;
            mCopyOrderList = new ArrayList<>();
            RequestPost.getOrderList(String.valueOf(pageCount), mType, mPayType, mGoodsStatus, "", startTime.getText().toString().trim(),
                    endTime.getText().toString().trim(), OrderManagerActivity.this);
            //toast("您选择了：" + startTime.getText().toString() + "至" + endTime.getText().toString());
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


    private String mPayType;
    private String mType;
    private String mGoodsStatus;

    @SuppressLint("HandlerLeak")
    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(@NonNull Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case PRINT_SIZE:
                    View view = getLayoutInflater().inflate(R.layout.dialog_print, null);
                    LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                    params.setMargins(FontDisplayUtil.dip2px(OrderManagerActivity.this, 30), 0,
                            FontDisplayUtil.dip2px(OrderManagerActivity.this, 30), 0);
                    CustomDialog customDialog = new CustomDialog(OrderManagerActivity.this, 0, 0, view, R.style.BaseDialogStyle);
                    if (view.getParent() != null) {
                        ((ViewGroup) view.getParent()).removeView(view);
                    }
                    customDialog.addContentView(view, params);
                    customDialog.setCancelable(false);
                    customDialog.show();
                    // 点击事件之类 --- 点击连接蓝牙打印机
                    PrintBean print = new PrintBean();
                    ImageView printA4 = view.findViewById(R.id.iv_a4);
                    ImageView receipts = view.findViewById(R.id.iv_receipts);
                    // A4
                    printA4.setOnClickListener(v -> {
                        printA4.setImageResource(R.drawable.radio_checked_shape);
                        receipts.setImageResource(R.drawable.radio_uncheck_shape);
                        print.setMethod("A4");
                    });
                    // 小票
                    receipts.setOnClickListener(v -> {
                        printA4.setImageResource(R.drawable.radio_uncheck_shape);
                        receipts.setImageResource(R.drawable.radio_checked_shape);
                        print.setMethod("小票");
                    });
                    Button cancel = view.findViewById(R.id.btn_cancel);
                    cancel.setOnClickListener(v -> customDialog.dismiss());
                    Button confirm = view.findViewById(R.id.btn_confirm);
                    confirm.setOnClickListener(v -> {
                        // 打印方式默认是A4
                        toast("打印方式：" + (print.getMethod() == null ? "A4" : print.getMethod()));
                    });
                    break;
                case INITIAL_SIZE:
                    // 订单状态、支付方式
                    LevelTypeAdapter typeAdapter = new LevelTypeAdapter(OrderManagerActivity.this, orderPayList);
                    orderPay.setAdapter(typeAdapter);
                    orderPay.setNumColumns(3);
                    typeAdapter.setOnClickItem(position -> {
                        // toast(orderPayList.get(position).getItemName());
                        switch (position) {
                            case 0:                     // 订单状态
                                new MenuDialog.Builder(OrderManagerActivity.this)
                                        .setCancel(R.string.common_cancel)
                                        .setAutoDismiss(false)
                                        .setList(orderStateList)
                                        .setCanceledOnTouchOutside(false)
                                        .setListener(new MenuDialog.OnListener<String>() {
                                            @Override
                                            public void onSelected(BaseDialog dialog, int position, String goodsStatus) {
                                                dialog.dismiss();
                                                switch (goodsStatus) {
                                                    case "待发货":
                                                        goodsStatus = "waitSend";
                                                        break;
                                                    case "待付款":
                                                        goodsStatus = "waitRec";
                                                        break;
                                                    case "已完成":
                                                        goodsStatus = "complete";
                                                        break;
                                                }
                                                // request
                                                showLoading("loading");
                                                mGoodsStatus = goodsStatus;
                                                pageCount = 0;
                                                mCopyOrderList = new ArrayList<>();
                                                RequestPost.getOrderList(String.valueOf(pageCount), mType, mPayType, mGoodsStatus,
                                                        "", "", "", OrderManagerActivity.this);
                                            }

                                            @Override
                                            public void onCancel(BaseDialog dialog) {
                                                dialog.dismiss();
                                            }
                                        }).show();
                                break;
                            case 1:                     // 支付方式
                                new MenuDialog.Builder(OrderManagerActivity.this)
                                        .setCancel(R.string.common_cancel)
                                        .setAutoDismiss(false)
                                        .setList(payMethodList)
                                        .setCanceledOnTouchOutside(false)
                                        .setListener(new MenuDialog.OnListener<String>() {
                                            @Override
                                            public void onSelected(BaseDialog dialog, int position, String payType) {
                                                dialog.dismiss();
                                                // request
                                                showLoading("loading");
                                                mPayType = payType;
                                                pageCount = 0;
                                                mCopyOrderList = new ArrayList<>();
                                                RequestPost.getOrderList(String.valueOf(pageCount), mType, mPayType, mGoodsStatus, "",
                                                        "", "", OrderManagerActivity.this);
                                            }

                                            @Override
                                            public void onCancel(BaseDialog dialog) {
                                                dialog.dismiss();
                                            }
                                        }).show();
                                break;
                        }
                    });
                    // 线上订单，线下订单
                    GoodsStatusAdapter statusAdapter = new GoodsStatusAdapter(OrderManagerActivity.this, statuList);
                    orderType.setAdapter(statusAdapter);
                    orderType.setNumColumns(4);
                    statusAdapter.setOnClickItem(position -> {
                        for (PressBean pressBean : statuList) {
                            pressBean.setChoose(false);
                        }
                        statuList.get(position).setChoose(true);
                        switch (position) {
                            case 1:
                                mType = "online";
                                break;
                            case 2:
                                mType = "offline";
                                break;
                            default:
                                mType = "";
                                break;
                        }
                        // request
                        showLoading("loading");
                        pageCount = 0;
                        mCopyOrderList = new ArrayList<>();
                        RequestPost.getOrderList(String.valueOf(pageCount), mType, mPayType, mGoodsStatus,
                                "", "", "", OrderManagerActivity.this);
                    });
                    // 订单内容
                    mRefreshLayout.setLoadMore(false);
                    mRefreshLayout.setRefreshing(false);
                    OrderContentAdapter contentAdapter = new OrderContentAdapter(OrderManagerActivity.this, mCopyOrderList);
                    contentList.setAdapter(contentAdapter);
                    contentAdapter.setOnClickItem(position -> {
                        // toast("订单详情：" + position);
                        Intent intent = new Intent(OrderManagerActivity.this, OrderDetailActivity.class);
                        intent.putExtra("orderId", mCopyOrderList.get(position).getId());
                        startActivity(intent);
                    });
                    break;
                case REFRESH_SIZE:
                    //上拉加载
                    mRefreshLayout.setOnLoadMoreListener(() -> {
                        pageCount++;
                        RequestPost.getOrderList(String.valueOf(pageCount), mType, mPayType, mGoodsStatus,
                                "", "", "", OrderManagerActivity.this);
                    });
                    // 下拉刷新
                    mRefreshLayout.setOnRefreshListener(() -> {
                        pageCount = 0;
                        mCopyOrderList = new ArrayList<>();
                        RequestPost.getOrderList(String.valueOf(pageCount), mType, mPayType, mGoodsStatus,
                                "", "", "", OrderManagerActivity.this);
                    });
                    // 第一次进来的时候刷新
                    mRefreshLayout.setOnAutoLoadListener(() -> {
                        // default query new --
                        // request
                        RequestPost.getOrderList(String.valueOf(pageCount), mType, mPayType, mGoodsStatus,
                                "", "", "", OrderManagerActivity.this);
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
        Map<String, String> body = JsonParser.parseJSONObject(result.body());
        if (body != null) {
            if ("1".equals(body.get("code"))) {
                // 订单列表
                if (result.url().contains("wxapi/v1/order.php?type=getBuyCarlist")) {
                    List<OrderListBean> orderList = JsonParser.parseJSONArray(OrderListBean.class, JsonParser.parseJSONObject(body.get("data")).get("list"));
                    if (orderList != null) {
                        mCopyOrderList.addAll(orderList);
                    }
                    Message msg = new Message();
                    msg.what = INITIAL_SIZE;
                    mHandler.sendMessage(msg);
                    // 支付方式
                    JSONArray jsonArray = JsonParser.parseJSONArrayString(JsonParser.parseJSONObject(body.get("data")).get("payOption"));
                    payMethodList = new ArrayList<>();
                    for (int i = 0; i < jsonArray.length(); i++) {
                        try {
                            payMethodList.add(jsonArray.getString(i));
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                    // 订单状态
                    JSONArray orderState = JsonParser.parseJSONArrayString(JsonParser.parseJSONObject(body.get("data")).get("orderStatelist"));
                    orderStateList = new ArrayList<>();
                    for (int i = 0; i < orderState.length(); i++) {
                        try {
                            orderStateList.add(orderState.getString(i));
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
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
