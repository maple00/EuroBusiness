package com.rainwood.eurobusiness.ui.activity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.text.Html;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.rainwood.eurobusiness.R;
import com.rainwood.eurobusiness.base.BaseActivity;
import com.rainwood.eurobusiness.domain.CommonUIBean;
import com.rainwood.eurobusiness.domain.GoodsListBean;
import com.rainwood.eurobusiness.domain.SkuMxBean;
import com.rainwood.eurobusiness.domain.SupplierBean;
import com.rainwood.eurobusiness.domain.replenmentBean;
import com.rainwood.eurobusiness.json.JsonParser;
import com.rainwood.eurobusiness.okhttp.HttpResponse;
import com.rainwood.eurobusiness.okhttp.OnHttpListener;
import com.rainwood.eurobusiness.other.BaseDialog;
import com.rainwood.eurobusiness.request.RequestPost;
import com.rainwood.eurobusiness.ui.adapter.DiscountAdapter;
import com.rainwood.eurobusiness.ui.dialog.DateDialog;
import com.rainwood.eurobusiness.ui.dialog.MenuDialog;
import com.rainwood.eurobusiness.utils.DateTimeUtils;
import com.rainwood.tools.common.FontDisplayUtil;
import com.rainwood.tools.viewinject.ViewById;
import com.rainwood.tools.widget.MeasureListView;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static com.rainwood.eurobusiness.common.Contants.GOODS_NAME_REQUEST;
import static com.rainwood.eurobusiness.common.Contants.GOODS_REQUEST;

/**
 * @Author: a797s
 * @Date: 2020/4/16 18:23
 * @Desc: 补货订单审核
 */
public final class AuditOrderActivity extends BaseActivity implements View.OnClickListener, OnHttpListener {

    @ViewById(R.id.iv_back)
    private ImageView mPageBack;
    @ViewById(R.id.tv_title)
    private TextView mPageTitle;
    @ViewById(R.id.tv_supplier)
    private TextView supplier;
    @ViewById(R.id.tv_time)
    private TextView time;
    @ViewById(R.id.tv_goods_type)
    private TextView goodsType;
    @ViewById(R.id.tv_goods_name)
    private TextView goodsName;
    @ViewById(R.id.tv_tax)
    private TextView tax;
    @ViewById(R.id.tv_payType)
    private TextView payType;
    @ViewById(R.id.tv_color)
    private TextView color;
    @ViewById(R.id.tv_size)
    private TextView size;
    @ViewById(R.id.tv_price)
    private TextView price;
    @ViewById(R.id.tv_num)
    private TextView num;
    @ViewById(R.id.tv_store)
    private TextView store;
    @ViewById(R.id.tv_total_num)
    private TextView totalNum;
    @ViewById(R.id.tv_purchase_price)
    private TextView purchasePrice;
    @ViewById(R.id.mlv_discount)
    private MeasureListView mlv_discount;
    @ViewById(R.id.tv_total_amount)
    private TextView totalAmount;
    @ViewById(R.id.tv_commit_order)
    private TextView commitOrder;

    private String[] discountStr = {"优惠单价", "优惠总价", "折扣比例", "折扣总价"};
    private List<CommonUIBean> mDiscountList;
    private List<SupplierBean> mSupplierList;
    // list
    private List<String> mSupplyList;           // 供应商list
    private List<String> mTaxOptionList;        // 税率列表
    private List<String> payTypeList;               // 付款方式
    private replenmentBean mReplement;
    private SkuMxBean mSkuMx;

    private final int INITIAL_SIZE = 0x101;
    private final int DISCOUNT_SIZE = 0x102;
    private String mOrderNo;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_audit_order;
    }

    @Override
    protected void initView() {
        initEvents();
        mOrderNo = getIntent().getStringExtra("orderNo");
        showLoading("");
        if (mOrderNo != null) {
            // TODO：同意补货之后的补货页面数据
            RequestPost.getChargeNewOrder(mOrderNo, this);
        }
    }

    @Override
    protected void initData() {
        super.initData();
        mDiscountList = new ArrayList<>();
        for (String s : discountStr) {
            CommonUIBean common = new CommonUIBean();
            common.setTitle(s);
            mDiscountList.add(common);
        }
    }

    private void initEvents() {
        mPageTitle.setText("编辑补货订单");
        mPageBack.setOnClickListener(this);
        supplier.setOnClickListener(this);
        time.setOnClickListener(this);
        goodsType.setOnClickListener(this);
        goodsName.setOnClickListener(this);
        commitOrder.setOnClickListener(this);
        tax.setOnClickListener(this);
        payType.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_back:
                finish();
                break;
            case R.id.tv_supplier:              // 选择供应商
                setTextValue(mSupplyList, supplier);
                break;
            case R.id.tv_time:              // 选择交货时间
                // 日期选择对话框
                new DateDialog.Builder(this)
                        .setTitle(getString(R.string.date_title))
                        .setConfirm(getString(R.string.common_confirm))
                        .setCancel(getString(R.string.common_cancel))
                        .setYear(DateTimeUtils.getNowYear())
                        .setMonth(DateTimeUtils.getNowMonth())
                        .setDay(DateTimeUtils.getNowDay())
                        //.setIgnoreDay()
                        .setListener(new DateDialog.OnListener() {
                            @SuppressLint("SetTextI18n")
                            @Override
                            public void onSelected(BaseDialog dialog, int year, int month, int day) {
                                dialog.dismiss();
                                time.setText(year + "-" + month + "-" + day);
                            }

                            @Override
                            public void onCancel(BaseDialog dialog) {
                                dialog.dismiss();
                            }
                        }).show();
                break;
            case R.id.tv_tax:               // 税率
                setTextValue(mTaxOptionList, tax);
                break;
            case R.id.tv_payType:           // 支付方式
                setTextValue(payTypeList, payType);
                break;
            case R.id.tv_goods_type:        // 商品分类
                Intent intent = new Intent(this, GoodsTypeActivity.class);
                startActivityForResult(intent, GOODS_REQUEST);
                break;
            case R.id.tv_goods_name:    // 商品名称
                Intent intent1 = new Intent(this, GoodsListActivity.class);
                intent1.putExtra("goodsTypeId", goodsType.getHint().toString().trim());
                startActivityForResult(intent1, GOODS_NAME_REQUEST);
                break;
            case R.id.tv_commit_order:
                if (TextUtils.isEmpty(supplier.getText())) {
                    toast("请选择供应商");
                    return;
                }
                if (TextUtils.isEmpty(time.getText())) {
                    toast("请选择交货时间");
                    return;
                }
                if (TextUtils.isEmpty(tax.getText())) {
                    toast("请选择税率");
                    return;
                }
                if (TextUtils.isEmpty(payType.getText())) {
                    toast("请选择支付方式");
                    return;
                }
                String supplierId = "";
                for (SupplierBean bean : mSupplierList) {
                    if (supplier.getText().toString().equals(bean.getName())) {
                        supplierId = bean.getId();
                        break;
                    }
                }
                // TODO: 提交订单
                showLoading("");
                RequestPost.auditChargeOrder(supplierId, mOrderNo, tax.getText().toString().trim(),
                        payType.getText().toString().trim(), time.getText().toString().trim(),"", "", "", this );
                break;
        }
    }

    /**
     * setValue
     *
     * @param optionList
     * @param textView
     */
    private void setTextValue(List<String> optionList, TextView textView) {
        new MenuDialog.Builder(this)
                .setCancel(R.string.common_cancel)
                .setAutoDismiss(false)
                .setList(optionList)
                .setCanceledOnTouchOutside(false)
                .setListener(new MenuDialog.OnListener<String>() {
                    @Override
                    public void onSelected(BaseDialog dialog, int position, String text) {
                        dialog.dismiss();
                        textView.setText(text);
                    }

                    @Override
                    public void onCancel(BaseDialog dialog) {
                        dialog.dismiss();
                    }
                }).show();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        // 获取商品分类
        if (requestCode == GOODS_REQUEST && resultCode == GOODS_REQUEST) {
            goodsType.setText(data.getStringExtra("goodsType"));
            goodsType.setHint(data.getStringExtra("subTypeId"));
        }
        // 获取商品名称
        if (requestCode == GOODS_NAME_REQUEST && resultCode == GOODS_NAME_REQUEST) {
            // 选择不同商品的时候刷新界面
            GoodsListBean mGoods = (GoodsListBean) data.getSerializableExtra("goods");
            goodsName.setText(mGoods.getName());
        }
    }

    @SuppressLint("HandlerLeak")
    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(@NonNull Message msg) {
            switch (msg.what) {
                case INITIAL_SIZE:
                    color.setText(mSkuMx.getGoodsColor());
                    size.setText(mSkuMx.getGoodsSize());
                    price.setText(mSkuMx.getTradePrice());
                    num.setText(mSkuMx.getNum());
                    store.setText(mSkuMx.getStoreName());
                    goodsType.setText(mReplement.getGoodsType());
                    goodsName.setText(mReplement.getGoodsName());
                    totalNum.setText(mReplement.getTotalNum());
                    purchasePrice.setText(mReplement.getPrice());

                    Message disCountMsg = new Message();
                    disCountMsg.what = DISCOUNT_SIZE;
                    mHandler.sendMessage(disCountMsg);
                    break;
                case DISCOUNT_SIZE:
                    // discount
                    DiscountAdapter discountAdapter = new DiscountAdapter(AuditOrderActivity.this, mDiscountList);
                    mlv_discount.setAdapter(discountAdapter);
                    int finalCount = Integer.parseInt(num.getText().toString().trim());
                    // 监听回调
                    discountAdapter.setOnClickEdit((adapter, position) -> {
                        // 合计
                        totalAmount.setText(Html.fromHtml("<font color=" + getResources().getColor(R.color.textColor) + " size='"
                                + FontDisplayUtil.dip2px(AuditOrderActivity.this, 12f) + "'>合计："
                                + (finalCount * Double.parseDouble(mReplement.getPrice())
                                - Double.parseDouble((mDiscountList.get(3).getShowText() == null)
                                || ("").equals(mDiscountList.get(3).getShowText()) ? "0" : mDiscountList.get(3).getShowText())) + "</font>"));
                        // 优惠总价
                        mDiscountList.get(1).setShowText(String.valueOf(finalCount * (Double.parseDouble(mReplement.getPrice())
                                - Double.parseDouble((mDiscountList.get(0).getShowText() == null)
                                || ("").equals(mDiscountList.get(0).getShowText()) ? "0" : mDiscountList.get(0).getShowText()))));
                        // 折扣总价

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
                // 获取新建采购订单页面数据
                if (result.url().contains("wxapi/v1/order.php?type=getChargeNewOrder")) {
                    // 供应商选择
                    mSupplierList = JsonParser.parseJSONArray(SupplierBean.class, JsonParser.parseJSONObject(body.get("data")).get("supplierlist"));
                    if (mSupplierList != null) {
                        mSupplyList = new ArrayList<>();
                        for (SupplierBean bean : mSupplierList) {
                            mSupplyList.add(bean.getName());
                        }
                    }

                    // 税率选择
                    JSONArray taxOption = JsonParser.parseJSONArrayString(JsonParser.parseJSONObject(body.get("data")).get("taxOption"));
                    if (taxOption != null) {
                        mTaxOptionList = new ArrayList<>();
                        for (int i = 0; i < taxOption.length(); i++) {
                            try {
                                mTaxOptionList.add(taxOption.getString(i));
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    }

                    // 付款方式
                    JSONArray payTypeArray = JsonParser.parseJSONArrayString(JsonParser.parseJSONObject(body.get("data")).get("payType"));
                    if (payTypeArray != null) {
                        payTypeList = new ArrayList<>();
                        for (int i = 0; i < payTypeArray.length(); i++) {
                            try {
                                payTypeList.add(payTypeArray.getString(i));
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    }

                    // 商品信息
                    mReplement = JsonParser.parseJSONObject(replenmentBean.class, JsonParser.parseJSONObject(body.get("data")).get("info"));
                    // 规格信息
                    mSkuMx = JsonParser.parseJSONObject(SkuMxBean.class, JsonParser.parseJSONObject(body.get("data")).get("skulist"));

                    Message msg = new Message();
                    msg.what = INITIAL_SIZE;
                    mHandler.sendMessage(msg);
                }

                // 提交订单
                if (result.url().contains("wxapi/v1/order.php?type=auditChargeOrder")){
                    toast(body.get("warn"));
                    postDelayed(this::finish, 500);
                }

            } else {
                toast(body.get("warn"));
            }
            if (getDialog() != null)
                dismissLoading();
        }
    }
}
