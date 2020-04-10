package com.rainwood.eurobusiness.ui.activity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.text.Html;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.rainwood.eurobusiness.R;
import com.rainwood.eurobusiness.base.BaseActivity;
import com.rainwood.eurobusiness.common.Contants;
import com.rainwood.eurobusiness.domain.AddressBean;
import com.rainwood.eurobusiness.domain.CommonUIBean;
import com.rainwood.eurobusiness.domain.ISpecialBean;
import com.rainwood.eurobusiness.domain.SkuBean;
import com.rainwood.eurobusiness.domain.SupplierBean;
import com.rainwood.eurobusiness.json.JsonParser;
import com.rainwood.eurobusiness.okhttp.HttpResponse;
import com.rainwood.eurobusiness.okhttp.OnHttpListener;
import com.rainwood.eurobusiness.other.BaseDialog;
import com.rainwood.eurobusiness.request.RequestPost;
import com.rainwood.eurobusiness.ui.adapter.DiscountAdapter;
import com.rainwood.eurobusiness.ui.adapter.OrderSpecialAdapter;
import com.rainwood.eurobusiness.ui.dialog.DateDialog;
import com.rainwood.eurobusiness.ui.dialog.MenuDialog;
import com.rainwood.eurobusiness.utils.DateTimeUtils;
import com.rainwood.eurobusiness.utils.ListUtils;
import com.rainwood.tools.common.FontDisplayUtil;
import com.rainwood.tools.view.ClearEditText;
import com.rainwood.tools.viewinject.ViewById;
import com.rainwood.tools.widget.MeasureListView;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import static com.rainwood.eurobusiness.common.Contants.ADDRESS_REQUEST_SIZE;
import static com.rainwood.eurobusiness.common.Contants.GOODS_SPECIAL_REQUEST;
import static com.rainwood.eurobusiness.common.Contants.INVOICE_ADDRESS_REQUEST;

/**
 * @Author: a797s
 * @Date: 2020/2/19
 * @Desc: 新建订单
 */
public class OrderNewActivity extends BaseActivity implements View.OnClickListener, OnHttpListener {

    @ViewById(R.id.iv_back)
    private ImageView pageBack;
    @ViewById(R.id.tv_title)
    private TextView pageTitle;
    @ViewById(R.id.tv_supplier)
    private TextView companyName;               // 公司名称
    @ViewById(R.id.tv_shipping_address)
    private TextView shippingAddress;           // 收货地址
    @ViewById(R.id.tv_invoice_address)
    private TextView invoiceAddress;            // 发票地址
    @ViewById(R.id.tv_time)
    private TextView time;                      // 交货时间
    @ViewById(R.id.tv_tax)
    private TextView tax;                       // 税率
    @ViewById(R.id.tv_distribution)
    private TextView distribution;              // 配送方式
    @ViewById(R.id.et_note)
    private ClearEditText note;                      // 备注
    // 商品信息
    @ViewById(R.id.ll_special_top)
    private LinearLayout goodsTop;          // 商品管理头部
    @ViewById(R.id.mlv_special)
    private MeasureListView special;        // 商品规格
    @ViewById(R.id.rl_add_shop)
    private RelativeLayout addShop;                 // 添加商品
    // 折扣、运费
    @ViewById(R.id.ll_bottom)
    private LinearLayout bottom;
    @ViewById(R.id.iv_package_email)
    private ImageView packageEmail;             // 包邮
    @ViewById(R.id.tv_package_email)
    private TextView packageEmailTv;
    @ViewById(R.id.iv_not_package_email)        // 不包邮
    private ImageView notPackageEmail;
    @ViewById(R.id.tv_not_package_email)
    private TextView notPackageEmailTv;
    @ViewById(R.id.et_fee)
    private ClearEditText fee;
    @ViewById(R.id.tv_vip_discount)
    private TextView vipDiscount;
    @ViewById(R.id.mlv_discount)
    private MeasureListView discount;
    @ViewById(R.id.tv_total_amount)
    private TextView totalAmount;
    @ViewById(R.id.tv_commit_order)
    private TextView commitOrder;

    private List<String> mTaxList;              // 税率
    private List<SupplierBean> mSupplierList;   // 公司对象
    private List<CommonUIBean> mDiscountList;
    // handler Size
    private final int GOODS_SPECIAL_SIZE = 0x1124;
    private final int DISCOUNT_SIZE = 0x1125;
    private String[] discountStr = {"折扣单价", "折扣比例", "折扣总价"};
    private String[] mDistributes = {"快递", "自提"};
    private List<SkuBean> mSpeciaList;
    private boolean hasFee;         // 是否有运费 -- 默认不包邮
    private String mGoodsId;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_order_new;
    }

    @Override
    protected void initView() {
        pageBack.setOnClickListener(this);
        addShop.setOnClickListener(this);
        if (Contants.CHOOSE_MODEL_SIZE == 4) {
            pageTitle.setText("新建订单");
            initEvents();
            // request  -- 请求新建订单页面数据
            showLoading("");
            RequestPost.newBuyCarPage(this);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (ListUtils.getSize(mSpeciaList) == 0) {
            goodsTop.setVisibility(View.GONE);
            addShop.setVisibility(View.VISIBLE);
            bottom.setVisibility(View.GONE);
        } else {
            bottom.setVisibility(View.VISIBLE);
            addShop.setVisibility(View.GONE);
            goodsTop.setVisibility(View.VISIBLE);
            mDiscountList = new ArrayList<>();
            for (String s : discountStr) {
                CommonUIBean common = new CommonUIBean();
                common.setTitle(s);
                mDiscountList.add(common);
            }
            Message msg = new Message();
            msg.what = DISCOUNT_SIZE;
            mHandler.sendMessage(msg);
        }
    }

    private void initEvents() {
        companyName.setOnClickListener(this);
        shippingAddress.setOnClickListener(this);
        invoiceAddress.setOnClickListener(this);
        time.setOnClickListener(this);
        tax.setOnClickListener(this);
        distribution.setOnClickListener(this);
        packageEmail.setOnClickListener(this);
        packageEmailTv.setOnClickListener(this);
        notPackageEmail.setOnClickListener(this);
        notPackageEmailTv.setOnClickListener(this);
        commitOrder.setOnClickListener(this);
    }

    /**
     * 时间控件
     */
    private void getDate(int flag) {
        // 日期选择对话框
        new DateDialog.Builder(this)
                .setTitle(getString(R.string.date_title))
                .setConfirm(getString(R.string.common_confirm))
                .setCancel(getString(R.string.common_cancel))
                .setYear(DateTimeUtils.getNowYear())
                .setMonth(DateTimeUtils.getNowMonth())
                .setDay(DateTimeUtils.getNowDay())
                // 不选择天数
                //.setIgnoreDay()
                .setListener(new DateDialog.OnListener() {
                    @SuppressLint("SetTextI18n")
                    @Override
                    public void onSelected(BaseDialog dialog, int year, int month, int day) {
                        // toast(year + "-" + "-" + month + "-" + day);
                        if (flag == 0) {
                            time.setText(year + "-" + month + "-" + day);
                        }
                    }

                    @Override
                    public void onCancel(BaseDialog dialog) {
                        //toast("取消了");
                        dialog.dismiss();
                    }
                }).show();
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
            case R.id.rl_add_shop:
                // toast("添加商品");
                Intent intent1 = new Intent(this, OrderAddShopActivity.class);
                startActivityForResult(intent1, GOODS_SPECIAL_REQUEST);
                break;
            case R.id.tv_supplier:          // 公司名称
                List<String> companyList = new ArrayList<>();
                for (SupplierBean bean : mSupplierList) {
                    companyList.add(bean.getName());
                }
                setDialogValue(companyList, 0);
                break;
            case R.id.tv_shipping_address:      // 收货地址
                if (TextUtils.isEmpty(companyName.getText())) {
                    toast("请先选择公司名称");
                    return;
                }
                String customId = "";
                for (SupplierBean bean : mSupplierList) {
                    if (companyName.getText().toString().trim().equals(bean.getName())) {
                        customId = bean.getId();
                        break;
                    }
                }
                Contants.CHOOSE_MODEL_SIZE = 13;
                Intent intent = new Intent(this, GoodsAddressActivity.class);
                intent.putExtra("customId", customId);
                startActivityForResult(intent, ADDRESS_REQUEST_SIZE);
                break;
            case R.id.tv_invoice_address:       // 发票地址
                if (TextUtils.isEmpty(companyName.getText())) {
                    toast("请先选择公司名称");
                    return;
                }
                String invoinceId = "";
                for (SupplierBean bean : mSupplierList) {
                    if (companyName.getText().toString().trim().equals(bean.getName())) {
                        invoinceId = bean.getId();
                        break;
                    }
                }
                Contants.CHOOSE_MODEL_SIZE = 14;
                Intent invoinceIntent = new Intent(this, GoodsAddressActivity.class);
                invoinceIntent.putExtra("customId", invoinceId);
                startActivityForResult(invoinceIntent, INVOICE_ADDRESS_REQUEST);
                break;
            case R.id.tv_time:                  // 交货时间
                getDate(0);
                break;
            case R.id.tv_tax:                   // 税率
                setDialogValue(mTaxList, 1);
                break;
            case R.id.tv_distribution:          // 配送方式
                setDialogValue(Arrays.asList(mDistributes), 2);
                break;
            case R.id.iv_package_email: // 包邮
            case R.id.tv_package_email:
                if (hasFee) {
                    hasFee = false;
                    packageEmail.setImageResource(R.drawable.radio_checked_shape);
                    notPackageEmail.setImageResource(R.drawable.radio_uncheck_shape);
                }
                break;
            case R.id.iv_not_package_email: // 不包邮
            case R.id.tv_not_package_email:
                if (!hasFee) {
                    hasFee = true;
                    packageEmail.setImageResource(R.drawable.radio_uncheck_shape);
                    notPackageEmail.setImageResource(R.drawable.radio_checked_shape);
                }
                break;
            case R.id.tv_commit_order:
                Log.d(TAG, "==== 是否包邮 ==== " + hasFee);
                if (!hasFee) {        // 不包邮
                    if (TextUtils.isEmpty(fee.getText())) {
                        toast("请输入运费");
                        return;
                    }
                }
                if (TextUtils.isEmpty(companyName.getText())) {
                    toast("请选择公司");
                    return;
                }
                String companyId = "";
                for (SupplierBean bean : mSupplierList) {
                    if (companyName.getText().toString().trim().equals(bean.getName())) {
                        companyId = bean.getId();
                        break;
                    }
                }

                if (TextUtils.isEmpty(shippingAddress.getText())) {
                    toast("请选择收货地址");
                    return;
                }
                if (TextUtils.isEmpty(invoiceAddress.getText())) {
                    toast("请选择发票地址");
                    return;
                }
                if (TextUtils.isEmpty(time.getText())) {
                    toast("请选择下单时间");
                    return;
                }
                if (TextUtils.isEmpty(tax.getText())) {
                    toast("请选择税率");
                    return;
                }
                if (TextUtils.isEmpty(distribution.getText())) {
                    toast("请选择配送方式");
                    return;
                }
                List<ISpecialBean> copyList = new ArrayList<>();
                for (SkuBean bean : mSpeciaList) {
                    ISpecialBean special = new ISpecialBean();
                    special.setSkuId(bean.getId());
                    special.setNum(bean.getNum());
                    copyList.add(special);
                }
                // request -- 新建订单
                showLoading("");
                RequestPost.buyCarEdit(companyId, mGoodsId,
                        hasFee ? "0" : fee.getText().toString().trim(),
                        mDiscountList.get(1).getShowText() == null ? "0" : mDiscountList.get(1).getShowText(),
                        mDiscountList.get(0).getShowText() == null ? "0" : mDiscountList.get(0).getShowText(),
                        TextUtils.isEmpty(note.getText()) ? "" : note.getText().toString().trim(),
                        time.getText().toString().trim(),
                        tax.getText().toString().trim(),
                        invoiceAddress.getHint().toString().trim(),
                        shippingAddress.getHint().toString().trim(),
                        "快递".equals(distribution.getText().toString().trim()) ? "1" : "2", copyList,
                        this);
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        // 收货地址选择
        if (requestCode == ADDRESS_REQUEST_SIZE && resultCode == ADDRESS_REQUEST_SIZE) {
            AddressBean address = (AddressBean) data.getSerializableExtra("address");
            shippingAddress.setText(address.getName());
            shippingAddress.setHint(address.getId());
        }
        // 发票地址选择
        if (requestCode == INVOICE_ADDRESS_REQUEST && resultCode == INVOICE_ADDRESS_REQUEST) {
            AddressBean address = (AddressBean) data.getSerializableExtra("address");
            invoiceAddress.setText(address.getName());
            invoiceAddress.setHint(address.getId());
        }
        // 选择规格
        if (requestCode == GOODS_SPECIAL_REQUEST && resultCode == GOODS_SPECIAL_REQUEST) {
            mSpeciaList = (List<SkuBean>) data.getSerializableExtra("special");
            mGoodsId = data.getStringExtra("goodsId");
            Message msg = new Message();
            msg.what = GOODS_SPECIAL_SIZE;
            mHandler.sendMessage(msg);
        }
    }

    /**
     * setValue
     *
     * @param optionList
     */
    private void setDialogValue(List<String> optionList, int flag) {
        new MenuDialog.Builder(this)
                .setCancel(R.string.common_cancel)
                .setAutoDismiss(false)
                .setList(optionList)
                .setCanceledOnTouchOutside(false)
                .setListener(new MenuDialog.OnListener<String>() {
                    @Override
                    public void onSelected(BaseDialog dialog, int position, String text) {
                        dialog.dismiss();
                        // toast("位置：" + position + ", 文本：" + text);
                        if (flag == 0) {        // 公司名称
                            companyName.setText(text);
                        }
                        if (flag == 1) {         // 税率
                            tax.setText(text);
                        }
                        if (flag == 2) {         // 配送方式
                            distribution.setText(text);
                        }
                    }

                    @Override
                    public void onCancel(BaseDialog dialog) {
                        dialog.dismiss();
                    }
                }).show();
    }

    @SuppressLint("HandlerLeak")
    private Handler mHandler = new Handler() {
        @SuppressLint("SetTextI18n")
        @Override
        public void handleMessage(@NonNull Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case GOODS_SPECIAL_SIZE:        // 商品规格
                    OrderSpecialAdapter specialAdapter = new OrderSpecialAdapter(OrderNewActivity.this, mSpeciaList);
                    special.setAdapter(specialAdapter);
                    break;
                case DISCOUNT_SIZE:             // 折扣
                    // 计算总的金额
                    double totalMoney = 0;
                    for (SkuBean bean : mSpeciaList) {
                        totalMoney += Integer.parseInt(bean.getNum()) *
                                Double.parseDouble(bean.getTradePrice());
                    }
                    // discount
                    DiscountAdapter discountAdapter = new DiscountAdapter(OrderNewActivity.this, mDiscountList);
                    discount.setAdapter(discountAdapter);
                    // 监听回调
                    double finalTotalMoney = totalMoney;
                    discountAdapter.setOnClickEdit((adapter, position) -> {
                        // 折扣单价

                        // 折扣总价 == 折扣单价* 折扣比例
                        double discounTotal = 0;
                        for (SkuBean bean : mSpeciaList) {
                            discounTotal += (Double.parseDouble(bean.getTradePrice()) -
                                    Double.parseDouble((mDiscountList.get(0).getShowText() == null)
                                            || ("".equals(mDiscountList.get(0).getShowText())) ? "0" : mDiscountList.get(0).getShowText())) * Integer.parseInt(bean.getNum());
                        }
                        double totalMount = discounTotal * (Double.parseDouble(mDiscountList.get(1).getShowText() == null ||
                                "".equals(mDiscountList.get(1).getShowText()) ? "0" : mDiscountList.get(1).getShowText()) / 100);
                        mDiscountList.get(2).setShowText(String.valueOf(totalMount));

                        // 合计
                        totalAmount.setText(Html.fromHtml("<font color=" + getResources().getColor(R.color.textColor) + " size='"
                                + FontDisplayUtil.dip2px(OrderNewActivity.this, 12f) + "'>合计："
                                + (finalTotalMoney - totalMount) + "</font>"));

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
        Log.d(TAG, "==== result === " + result);
        Map<String, String> body = JsonParser.parseJSONObject(result.body());
        if (body != null) {
            if ("1".equals(body.get("code"))) {
                // 新建订单页面数据
                if (result.url().contains("wxapi/v1/order.php?type=newBuyCarPage")) {
                    mSupplierList = JsonParser.parseJSONArray(SupplierBean.class, JsonParser.parseJSONObject(body.get("data")).get("kehuList"));
                    JSONArray taxOption = JsonParser.parseJSONArrayString(JsonParser.parseJSONObject(body.get("data")).get("taxOption"));
                    mTaxList = new ArrayList<>();
                    for (int i = 0; i < taxOption.length(); i++) {
                        try {
                            mTaxList.add(taxOption.getString(i));
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }
                // 提交订单
                if (result.url().contains("wxapi/v1/order.php?type=buyCarEdit")){
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
