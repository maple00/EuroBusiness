package com.rainwood.eurobusiness.ui.activity;

import android.annotation.SuppressLint;
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
import com.rainwood.eurobusiness.domain.CommonUIBean;
import com.rainwood.eurobusiness.domain.ItemGridBean;
import com.rainwood.eurobusiness.domain.OrderContentBean;
import com.rainwood.eurobusiness.domain.PressBean;
import com.rainwood.eurobusiness.domain.PrintBean;
import com.rainwood.eurobusiness.ui.adapter.GoodsStatusAdapter;
import com.rainwood.eurobusiness.ui.adapter.LevelTypeAdapter;
import com.rainwood.eurobusiness.ui.adapter.OrderContentAdapter;
import com.rainwood.eurobusiness.ui.widget.CustomDialog;
import com.rainwood.tools.common.FontDisplayUtil;
import com.rainwood.tools.view.ClearEditText;
import com.rainwood.tools.viewinject.ViewById;
import com.rainwood.tools.widget.MeasureGridView;
import com.rainwood.tools.widget.MeasureListView;

import java.util.ArrayList;
import java.util.List;

/**
 * @Author: a797s
 * @Date: 2020/2/19
 * @Desc: 订单管理
 */
public class OrderManagerActivity extends BaseActivity implements View.OnClickListener {

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

    @Override
    protected void initView() {
        pageBack.setOnClickListener(this);
        newOreder.setOnClickListener(this);
        print.setOnClickListener(this);
        screening.setOnClickListener(this);
        // 订单状态、支付方式
        LevelTypeAdapter typeAdapter = new LevelTypeAdapter(this, orderPayList);
        orderPay.setAdapter(typeAdapter);
        orderPay.setNumColumns(3);
        // 线上订单，线下订单
        GoodsStatusAdapter statusAdapter = new GoodsStatusAdapter(this, statuList);
        orderType.setAdapter(statusAdapter);
        orderType.setNumColumns(4);
        statusAdapter.setOnClickItem(position -> {
            for (PressBean pressBean : statuList) {
                pressBean.setChoose(false);
            }
            statuList.get(position).setChoose(true);
        });
        // 订单内容
        OrderContentAdapter contentAdapter = new OrderContentAdapter(this, mList);
        contentList.setAdapter(contentAdapter);
        contentAdapter.setOnClickItem(position -> {
            // toast("订单详情：" + position);
            openActivity(OrderDetailActivity.class);
        });
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
        // 订单内容
        mList = new ArrayList<>();
        for (int i = 0; i < 20; i++) {
            OrderContentBean content = new OrderContentBean();
            content.setMethod("线上");
            content.setNum("5515320560100");
            content.setStatus("待发货");
            List<CommonUIBean> labelList = new ArrayList<>();
            for (int j = 0; j < labelsTitle.length; j++) {
                CommonUIBean commonUI = new CommonUIBean();
                commonUI.setTitle(labelsTitle[j]);
                commonUI.setShowText(labels[j]);
                labelList.add(commonUI);
            }
            content.setManagerList(labelList);
            mList.add(content);
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
                    params.setMargins(FontDisplayUtil.dip2px(OrderManagerActivity.this, 30 ), 0,
                            FontDisplayUtil.dip2px(OrderManagerActivity.this, 30 ), 0);
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
                        // toast("确定");
                        toast("打印方式：" + print.getMethod());
                    });
                    break;
            }
        }
    };

    /*
    模拟数据
     */
    //
    private List<ItemGridBean> orderPayList;
    private String[] orderPays = {"订单状态", "支付方式"};
    // 订单状态
    private List<PressBean> statuList;
    private String[] status = {"全部", "线上订单", "线下订单"};
    // 订单内容
    private List<OrderContentBean> mList;
    private String[] labelsTitle = {"商品数量", "合计", "支付方式"};
    private String[] labels = {"1000", "2502.00€", "现金支付"};
}
