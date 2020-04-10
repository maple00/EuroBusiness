package com.rainwood.eurobusiness.ui.activity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.ViewGroup;
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
import com.rainwood.eurobusiness.ui.dialog.MenuDialog;
import com.rainwood.eurobusiness.ui.widget.CustomDialog;
import com.rainwood.tools.common.FontDisplayUtil;
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
    // mHandler
    private final int PRINT_SIZE = 0x1124;
    private final int INITIAL_SIZE = 0x101;

    private List<OrderListBean> mOrderList;
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
        // request
        showLoading("loading");
        RequestPost.getOrderList("", "", "", "", this);
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
                toast("筛选");
                break;
        }
    }

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
                                            public void onSelected(BaseDialog dialog, int position, String text) {
                                                dialog.dismiss();
                                                switch (text) {
                                                    case "待发货":
                                                        text = "waitSend";
                                                        break;
                                                    case "待付款":
                                                        text = "waitRec";
                                                        break;
                                                    case "已完成":
                                                        text = "complete";
                                                        break;
                                                }
                                                // request
                                                showLoading("loading");
                                                RequestPost.getOrderList("", "", text, "", OrderManagerActivity.this);
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
                                            public void onSelected(BaseDialog dialog, int position, String text) {
                                                dialog.dismiss();
                                                // request
                                                showLoading("loading");
                                                RequestPost.getOrderList("", text, "", "", OrderManagerActivity.this);
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
                        String type;
                        switch (position) {
                            case 1:
                                type = "online";
                                break;
                            case 2:
                                type = "offline";
                                break;
                            default:
                                type = "";
                                break;
                        }
                        // request
                        showLoading("loading");
                        RequestPost.getOrderList(type, "", "", "", OrderManagerActivity.this);
                    });
                    // 订单内容
                    OrderContentAdapter contentAdapter = new OrderContentAdapter(OrderManagerActivity.this, mOrderList);
                    contentList.setAdapter(contentAdapter);
                    contentAdapter.setOnClickItem(position -> {
                        // toast("订单详情：" + position);
                        Intent intent = new Intent(OrderManagerActivity.this, OrderDetailActivity.class);
                        intent.putExtra("orderId", mOrderList.get(position).getId());
                        startActivity(intent);
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
                // 订单列表
                if (result.url().contains("wxapi/v1/order.php?type=getBuyCarlist")) {
                    mOrderList = JsonParser.parseJSONArray(OrderListBean.class, JsonParser.parseJSONObject(body.get("data")).get("list"));
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
                    //Log.d(TAG, "=========== " + JsonParser.parseJSONObject(body.get("data")).get("orderStatelist"));
                }
            } else {
                toast(body.get("warn"));
            }
            dismissLoading();
        }
    }
}
