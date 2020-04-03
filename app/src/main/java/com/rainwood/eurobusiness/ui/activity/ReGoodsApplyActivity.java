package com.rainwood.eurobusiness.ui.activity;

import android.annotation.SuppressLint;
import android.os.Handler;
import android.os.Message;
import android.text.Html;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.bumptech.glide.Glide;
import com.rainwood.eurobusiness.R;
import com.rainwood.eurobusiness.base.BaseActivity;
import com.rainwood.eurobusiness.common.Contants;
import com.rainwood.eurobusiness.domain.PurchaseRefundsBean;
import com.rainwood.eurobusiness.domain.RefundsOrderBean;
import com.rainwood.eurobusiness.domain.RefundsSepBean;
import com.rainwood.eurobusiness.json.JsonParser;
import com.rainwood.eurobusiness.okhttp.HttpResponse;
import com.rainwood.eurobusiness.okhttp.OnHttpListener;
import com.rainwood.eurobusiness.other.BaseDialog;
import com.rainwood.eurobusiness.request.RequestPost;
import com.rainwood.eurobusiness.ui.dialog.MenuDialog;
import com.rainwood.eurobusiness.utils.ListUtils;
import com.rainwood.tools.view.ClearEditText;
import com.rainwood.tools.viewinject.ViewById;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * @Author: a797s
 * @Date: 2020/2/22
 * @Desc: 退货申请
 */
public class ReGoodsApplyActivity extends BaseActivity implements View.OnClickListener, OnHttpListener {

    private PurchaseRefundsBean mPurchaseRefunds;
    private RefundsOrderBean mRefundsOrder;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_return_goods_apply;
    }


    @ViewById(R.id.iv_back)
    private ImageView pageBack;
    @ViewById(R.id.tv_title)
    private TextView pageTitle;
    @ViewById(R.id.iv_img)
    private ImageView image;
    @ViewById(R.id.tv_name)
    private TextView name;
    @ViewById(R.id.tv_model)
    private TextView model;
    @ViewById(R.id.tv_discount)
    private TextView discount;
    @ViewById(R.id.tv_rate)
    private TextView rate;
    @ViewById(R.id.tv_params)
    private TextView params;
    // 退货
    @ViewById(R.id.tv_return_num)
    private TextView returnNum;
    @ViewById(R.id.et_return_num)
    private ClearEditText clearEditText;
    @ViewById(R.id.tv_return_reason)
    private TextView reason;
    @ViewById(R.id.et_return_reason)
    private ClearEditText clearReason;
    @ViewById(R.id.tv_return_fee)
    private TextView fee;
    @ViewById(R.id.et_return_fee)
    private EditText returnFee;
    // 订单退货
    @ViewById(R.id.ll_refunds_order)
    private LinearLayout refundsOrder;
    @ViewById(R.id.iv_inventory)
    private ImageView ivInventory;
    @ViewById(R.id.tv_inventory)
    private TextView tvInventory;
    @ViewById(R.id.iv_scrap)
    private ImageView ivScrap;
    @ViewById(R.id.tv_scrap)
    private TextView tvScrap;

    @ViewById(R.id.btn_confirm)
    private Button confirm;
    // 退货规格
    @ViewById(R.id.ll_color)
    private LinearLayout refundsColor;
    @ViewById(R.id.et_refunds_color)
    private ClearEditText etRefundsColor;
    private int specialPos = -1;

    private static final int REFUNDS_ORDER_SIZE = 0x012;
    private final int INITIAL_SIZE = 0x101;
    private boolean hasScrap;               // 是否报废。--- 默认入库

    @Override
    protected void initView() {
        initContent();

        // request
        // 采购订单退货
        showLoading("loading");
        if (Contants.CHOOSE_MODEL_SIZE == 2 || Contants.CHOOSE_MODEL_SIZE == 109) {
            refundsOrder.setVisibility(View.GONE);
            clearEditText.setVisibility(View.VISIBLE);
            String specialId = getIntent().getStringExtra("specialId");
            if (specialId != null) {
                RequestPost.getRefundOrder(specialId, this);
            } else {
                finish();
            }
        }
        // 销售订单退货
        if (Contants.CHOOSE_MODEL_SIZE == 4 || Contants.CHOOSE_MODEL_SIZE == 106) {
            refundsOrder.setVisibility(View.VISIBLE);
            clearEditText.setVisibility(View.GONE);
            String specialId = getIntent().getStringExtra("specialId");
            if (specialId != null) {
                RequestPost.getBuyCarRefundPage(specialId, this);
            } else {
                finish();
            }
        }
    }

    /**
     * init Content
     */
    private void initContent() {
        pageBack.setOnClickListener(this);
        pageTitle.setText("退货申请");
        confirm.setOnClickListener(this);
        etRefundsColor.setOnClickListener(this);
        etRefundsColor.setFocusable(false);
        etRefundsColor.setFocusableInTouchMode(false);
        ivInventory.setOnClickListener(this);
        tvInventory.setOnClickListener(this);
        ivScrap.setOnClickListener(this);
        tvScrap.setOnClickListener(this);
    }

    @Override
    protected void initData() {
        super.initData();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_back:
                finish();
                break;
            case R.id.et_refunds_color:
                List<String> specialList = new ArrayList<>();
                for (RefundsSepBean refundsSepBean : mPurchaseRefunds.getSkulist()) {
                    specialList.add(refundsSepBean.getName());
                }
                bottomSelector(specialList, 1);
                break;
            case R.id.iv_inventory:
            case R.id.tv_inventory:
                if (hasScrap) {
                    hasScrap = false;
                    ivInventory.setImageResource(R.drawable.radio_checked_shape);
                    ivScrap.setImageResource(R.drawable.radio_uncheck_shape);
                }
                break;
            case R.id.iv_scrap:
            case R.id.tv_scrap:
                if (!hasScrap) {
                    hasScrap = true;
                    ivInventory.setImageResource(R.drawable.radio_uncheck_shape);
                    ivScrap.setImageResource(R.drawable.radio_checked_shape);
                }
                break;
            case R.id.btn_confirm:
                if (Contants.CHOOSE_MODEL_SIZE == 2 || Contants.CHOOSE_MODEL_SIZE == 109) {

                    if (TextUtils.isEmpty(clearEditText.getText())) {
                        toast("请输入退货数量");
                        return;
                    }
                    if (TextUtils.isEmpty(clearReason.getText())) {
                        toast("请输入退货原因");
                        return;
                    }
                    if (TextUtils.isEmpty(returnFee.getText())) {
                        toast("请输入运费");
                        return;
                    }
                    if (!(ListUtils.getSize(mPurchaseRefunds.getSkulist()) > 0 && specialPos > -1)) {
                        toast("请选择规格");
                        return;
                    }
                    // request
                    showLoading("");
                    RequestPost.refundPurchaseOrder(mPurchaseRefunds.getMxId(), clearEditText.getText().toString().trim(),
                            mPurchaseRefunds.getSkulist().get(specialPos).getSkuId(),
                            clearReason.getText().toString().trim(), returnFee.getText().toString().trim(), this);
                }
                // 订单退货
                if (Contants.CHOOSE_MODEL_SIZE == 4 || Contants.CHOOSE_MODEL_SIZE == 106) {
                    clearEditText.setText("4");
                    if (TextUtils.isEmpty(clearEditText.getText())) {
                        toast("请输入退货数量");
                        return;
                    }
                    if (TextUtils.isEmpty(clearReason.getText())) {
                        toast("请输入退货原因");
                        return;
                    }
                    if (TextUtils.isEmpty(returnFee.getText())) {
                        toast("请输入运费");
                        return;
                    }
                    // request
                    showLoading("");
                    RequestPost.commitBuyCarRefundOrder(mRefundsOrder.getBuyCarMxId(), clearEditText.getText().toString().trim(),
                            clearReason.getText().toString().trim(), returnFee.getText().toString().trim(), this);
                }
                break;
        }
    }

    @SuppressLint("HandlerLeak")
    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(@NonNull Message msg) {
            switch (msg.what) {
                case INITIAL_SIZE:
                    if ("1".equals(mPurchaseRefunds.getIsSku())) {       // 有规格
                        refundsColor.setVisibility(View.VISIBLE);
                    } else {
                        refundsColor.setVisibility(View.GONE);
                    }
                    Glide.with(ReGoodsApplyActivity.this).load(mPurchaseRefunds.getIco()).into(image);
                    name.setText(mPurchaseRefunds.getGoodsName());
                    model.setText(mPurchaseRefunds.getModel());
                    discount.setText(mPurchaseRefunds.getDiscount());
                    rate.setText(mPurchaseRefunds.getTaxRate());
                    params.setText(mPurchaseRefunds.getGoodsSkuName());
                    returnNum.setText(Html.fromHtml("<font color=" + getResources().getColor(R.color.white66) + " size='14px'>退货数量\u3000\u3000</font>"
                            + "<font color=" + getResources().getColor(R.color.yellow05) + " size='12px'>" + "最多可退" + mPurchaseRefunds.getNum() + "件" + "</font>"));
                    reason.setText("退货原因");
                    fee.setText("运费");
                    break;
                case REFUNDS_ORDER_SIZE:
                    Glide.with(ReGoodsApplyActivity.this).load(mRefundsOrder.getIco()).into(image);
                    name.setText(mRefundsOrder.getGoodsName());
                    model.setText(mRefundsOrder.getModel());
                    discount.setText(mRefundsOrder.getDiscount());
                    rate.setText(mRefundsOrder.getTaxRate());
                    params.setText(mRefundsOrder.getSkuName());
                    returnNum.setText(Html.fromHtml("<font color=" + getResources().getColor(R.color.white66) + " size='14px'>退货途径\u3000\u3000</font>"));
                    reason.setText("退货原因");
                    fee.setText("运费");
                    break;
            }
        }
    };

    /**
     * 选择规格
     *
     * @param sizeOption
     * @param flag
     */
    private void bottomSelector(List sizeOption, int flag) {
        new MenuDialog.Builder(this)
                // 设置null 表示不显示取消按钮
                .setCancel(R.string.common_cancel)
                // 设置点击按钮后不关闭弹窗
                .setAutoDismiss(false)
                // 显示的数据
                .setList(sizeOption)
                .setCanceledOnTouchOutside(false)
                .setListener(new MenuDialog.OnListener<String>() {
                    @Override
                    public void onSelected(BaseDialog dialog, int position, String text) {
                        dialog.dismiss();
                        if (flag == 1) {         // 选择颜色
                            specialPos = position;
                            etRefundsColor.setText(text);
                        }
                    }

                    @Override
                    public void onCancel(BaseDialog dialog) {
                        dialog.dismiss();
                    }
                }).show();
    }

    @Override
    public void onHttpFailure(HttpResponse result) {

    }

    @Override
    public void onHttpSucceed(HttpResponse result) {
        Log.d(TAG, " --- result --- " + result);
        Map<String, String> body = JsonParser.parseJSONObject(result.body());
        if (body != null) {
            if ("1".equals(body.get("code"))) {
                // 采购退货数据
                if (result.url().contains("wxapi/v1/order.php?type=getRefundPage")) {
                    mPurchaseRefunds = JsonParser.parseJSONObject(PurchaseRefundsBean.class, JsonParser.parseJSONObject(body.get("data")).get("info"));
                    Message msg = new Message();
                    msg.what = INITIAL_SIZE;
                    mHandler.sendMessage(msg);
                }
                // 提交退货
                if (result.url().contains("wxapi/v1/order.php?type=refundPurchaseOrder")) {
                    toast(body.get("warn"));
                    postDelayed(this::finish, 500);
                }
                // 订单退货数据
                if (result.url().contains("wxapi/v1/order.php?type=getBuyCarRefundPage")) {
                    mRefundsOrder = JsonParser.parseJSONObject(RefundsOrderBean.class, JsonParser.parseJSONObject(body.get("data")).get("info"));

                    Message msg = new Message();
                    msg.what = REFUNDS_ORDER_SIZE;
                    mHandler.sendMessage(msg);
                }
                if (result.url().contains("wxapi/v1/order.php?type=commitBuyCarRefundOrder")) {
                    toast(body.get("warn"));
                    postDelayed(this::finish, 500);
                }
            } else {
                toast(body.get("warn"));
            }
            dismissLoading();
        }
    }
}
