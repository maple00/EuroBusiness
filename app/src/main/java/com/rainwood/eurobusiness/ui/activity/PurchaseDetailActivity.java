package com.rainwood.eurobusiness.ui.activity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.text.Html;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.rainwood.eurobusiness.R;
import com.rainwood.eurobusiness.base.BaseActivity;
import com.rainwood.eurobusiness.domain.CommonUIBean;
import com.rainwood.eurobusiness.domain.PurchaseInfos;
import com.rainwood.eurobusiness.domain.PurchaseTypeBean;
import com.rainwood.eurobusiness.json.JsonParser;
import com.rainwood.eurobusiness.okhttp.HttpResponse;
import com.rainwood.eurobusiness.okhttp.OnHttpListener;
import com.rainwood.eurobusiness.request.RequestPost;
import com.rainwood.eurobusiness.ui.adapter.PurchaseLabelsAdapter;
import com.rainwood.eurobusiness.ui.adapter.PurchaseTypeAdapter;
import com.rainwood.tools.common.FontDisplayUtil;
import com.rainwood.tools.statusbar.StatusBarUtil;
import com.rainwood.tools.viewinject.ViewById;
import com.rainwood.tools.widget.MeasureListView;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * @Author: a797s
 * @Date: 2020/2/18
 * @Desc: 采购单详情 --- 门店端
 */
public class PurchaseDetailActivity extends BaseActivity implements View.OnClickListener, OnHttpListener {

    private PurchaseInfos mPurchaseInfos;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_purchase_detail;
    }

    @ViewById(R.id.iv_back)
    private ImageView pageBack;
    @ViewById(R.id.tv_title)
    private TextView pageTitle;
    @ViewById(R.id.rl_action_bar)
    private RelativeLayout actionBar;
    @ViewById(R.id.rl_top_item)
    private RelativeLayout topItem;
    @ViewById(R.id.tv_status)
    private TextView status;
    // content
    @ViewById(R.id.tv_goods_name)
    private TextView goodsName;
    @ViewById(R.id.tv_model)
    private TextView model;
    @ViewById(R.id.tv_discount)
    private TextView discount;
    @ViewById(R.id.tv_rate)
    private TextView rate;
    @ViewById(R.id.mlv_type_list)
    private MeasureListView purchaseList;
    // bottom label
    @ViewById(R.id.lv_purchase_label)
    private MeasureListView purchaseLabel;
    // btn
    @ViewById(R.id.btn_bulk_storage)
    private Button bulkStorage;
    @ViewById(R.id.ll_selected_bulk)
    private LinearLayout selected;
    @ViewById(R.id.btn_cancel)
    private Button cancel;
    @ViewById(R.id.btn_selected_bulk)
    private Button selectedBulk;
    // mHandler
    private final int CHECKED_SIZE = 0x1124;
    private int selectedCount = 0;

    private List<PurchaseTypeBean> mList;
    // Label
    private List<CommonUIBean> labeList;
    private String[] titles = {"总计", "采购订单", "下单时间", "订单类型"};

    @Override
    protected void initView() {
        // 初始化本Activity
        initContext();

        selectedBulk.setText("批量入库（已选0件）");
    }

    @Override
    protected void onResume() {
        super.onResume();
        // request
        String orderId = getIntent().getStringExtra("orderId");
        if (orderId != null) {
            showLoading("loading");
            RequestPost.getPurchaseOrderDetail(orderId, this);
        }
    }

    @Override
    protected void initData() {
        super.initData();
        // 采购订单规格list
        mList = new ArrayList<>();
        // Label
        labeList = new ArrayList<>();
        for (int i = 0; i < titles.length; i++) {
            CommonUIBean commonUI = new CommonUIBean();
            commonUI.setTitle(titles[i]);
            labeList.add(commonUI);
        }
    }

    /**
     * 初始化 Context
     */
    private void initContext() {
        pageBack.setOnClickListener(this);
        pageBack.setImageResource(R.drawable.icon_white_page_back);
        pageTitle.setText("采购单详情");
        pageTitle.setTextColor(getResources().getColor(R.color.white));
        bulkStorage.setOnClickListener(this);
        // 设置状态栏
        StatusBarUtil.setCommonUI(getActivity(), false);
        StatusBarUtil.setRootViewFitsSystemWindows(getActivity(), false);
        // 设置RelativeLayout 的高度
        ViewGroup.LayoutParams params = topItem.getLayoutParams();
        params.height = FontDisplayUtil.dip2px(this, 180f) + StatusBarUtil.getStatusBarHeight(this);
        topItem.setLayoutParams(params);
        // 设置标题栏高度和外边距
        ViewGroup.MarginLayoutParams layoutParams = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, FontDisplayUtil.dip2px(this, 44f));
        layoutParams.setMargins(0, FontDisplayUtil.dip2px(this, 40), 0, 0);
        actionBar.setLayoutParams(layoutParams);
        // Button
        cancel.setOnClickListener(this);
        selectedBulk.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_back:
                finish();
                break;
            case R.id.btn_bulk_storage:
                if ("waitIn".equals(mPurchaseInfos.getWorkFlow())) {            // 待入库
                    selected.setVisibility(View.VISIBLE);
                    bulkStorage.setVisibility(View.GONE);
                    setSelector(true);
                }else {                                                             // 待收款
                    // request
                    toast("确认收款");
                }
                break;
            case R.id.btn_cancel:
                selected.setVisibility(View.GONE);
                bulkStorage.setVisibility(View.VISIBLE);
                setSelector(false);
                break;
            case R.id.btn_selected_bulk:
                toast("批量入库选中：" + selectedCount + "件");
                break;
        }
    }

    /**
     * 设置是否批量
     *
     * @param b
     */
    private void setSelector(boolean b) {
        for (PurchaseTypeBean typeBean : mList) {
            typeBean.setBulkSelect(b);
        }
        Message msg = new Message();
        msg.what = CHECKED_SIZE;
        mHandler.sendMessage(msg);
    }

    @SuppressLint("HandlerLeak")
    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(@NonNull Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case CHECKED_SIZE:              // 显示不同的状态
                    PurchaseTypeAdapter typeAdapter = new PurchaseTypeAdapter(PurchaseDetailActivity.this, mList);
                    purchaseList.setAdapter(typeAdapter);
                    typeAdapter.setOnClickItem(new PurchaseTypeAdapter.OnClickItem() {
                        @Override
                        public void onClickReturnGoods(int position) {      // 退货
                            Intent intent = new Intent(PurchaseDetailActivity.this, ReGoodsApplyActivity.class);
                            intent.putExtra("specialId", mList.get(position).getMxId());
                            startActivity(intent);
                        }

                        @Override
                        public void onClickInStorage(int position) {        // 入库
                            Intent intent = new Intent(PurchaseDetailActivity.this, InStorageActivity.class);
                            intent.putExtra("specialId", mList.get(position).getMxId());
                            startActivity(intent);
                        }

                        @Override
                        public void onClickChecked(int position) {
                            if (mList.get(position).isSelected()) {
                                mList.get(position).setSelected(false);
                                selectedCount -= Integer.parseInt(mList.get(position).getNum());
                            } else {
                                mList.get(position).setSelected(true);
                                selectedCount += Integer.parseInt(mList.get(position).getNum());
                            }
                            // 局部UI刷新
                            selectedBulk.setText(Html.fromHtml("<font color=" + getResources().getColor(R.color.white) + " size='14px'>批量入库</font>"
                                    + "<font color=" + getResources().getColor(R.color.white) + " size='12px'>（已选" + selectedCount + "件）</font>"));
                            Message msg = new Message();
                            msg.what = CHECKED_SIZE;
                            mHandler.sendMessage(msg);
                        }
                    });
                    // label
                    PurchaseLabelsAdapter labelsAdapter = new PurchaseLabelsAdapter(PurchaseDetailActivity.this, labeList);
                    purchaseLabel.setAdapter(labelsAdapter);
                    //
                    if ("waitIn".equals(mPurchaseInfos.getWorkFlow())) {
                        bulkStorage.setVisibility(View.VISIBLE);
                    }else if ("waitPay".equals(mPurchaseInfos.getWorkFlow())){
                        bulkStorage.setVisibility(View.VISIBLE);
                        bulkStorage.setText("确认收款");
                    }else {
                        bulkStorage.setVisibility(View.GONE);
                    }
                    break;
            }
        }
    };

    @Override
    public void onHttpFailure(HttpResponse result) {

    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onHttpSucceed(HttpResponse result) {
        Log.d(TAG, " --- result --- " + result);
        Map<String, String> body = JsonParser.parseJSONObject(result.body());
        if (body != null) {
            if ("1".equals(body.get("code"))) {
                // 采购记录详情
                if (result.url().contains("wxapi/v1/order.php?type=getPurchaseOrderInfo")) {
                    mList = JsonParser.parseJSONArray(PurchaseTypeBean.class, JsonParser.parseJSONObject(body.get("data")).get("skulist"));
                    mPurchaseInfos = JsonParser.parseJSONObject(PurchaseInfos.class, JsonParser.parseJSONObject(body.get("data")).get("info"));

                    if ("waitIn".equals(mPurchaseInfos.getWorkFlow())) {
                        status.setText("待入库");
                    }else if ("waitPay".equals(mPurchaseInfos.getWorkFlow())){
                        status.setText("待付款");
                    }else {
                        status.setText("已完成");
                    }

                    goodsName.setText(mPurchaseInfos.getGoodsName());
                    model.setText(mPurchaseInfos.getModel());
                    discount.setText(mPurchaseInfos.getDiscount() + "%折扣");
                    rate.setText((Double.parseDouble(mPurchaseInfos.getTaxRate()) * 100) + "%税率");

                    for (int i = 0; i < labeList.size(); i++) {
                        switch (i) {
                            case 0:
                                labeList.get(i).setShowText(mPurchaseInfos.getTotalMoney());
                                break;
                            case 1:
                                labeList.get(i).setShowText(mPurchaseInfos.getOrderNo());
                                break;
                            case 2:
                                labeList.get(i).setShowText(mPurchaseInfos.getTime());
                                break;
                            case 3:
                                labeList.get(i).setShowText(mPurchaseInfos.getClassify());
                                break;
                        }
                    }

                    Message msg = new Message();
                    msg.what = CHECKED_SIZE;
                    mHandler.sendMessage(msg);
                }
            } else {
                toast(body.get("warn"));
            }
            dismissLoading();
        }
    }
}
