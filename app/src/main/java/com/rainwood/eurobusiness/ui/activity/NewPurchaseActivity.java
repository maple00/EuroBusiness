package com.rainwood.eurobusiness.ui.activity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.text.Html;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.rainwood.eurobusiness.R;
import com.rainwood.eurobusiness.base.BaseActivity;
import com.rainwood.eurobusiness.domain.CommonUIBean;
import com.rainwood.eurobusiness.domain.GoodsListBean;
import com.rainwood.eurobusiness.domain.ISpecialBean;
import com.rainwood.eurobusiness.domain.SkuBean;
import com.rainwood.eurobusiness.domain.SupplierBean;
import com.rainwood.eurobusiness.json.JsonParser;
import com.rainwood.eurobusiness.okhttp.HttpResponse;
import com.rainwood.eurobusiness.okhttp.OnHttpListener;
import com.rainwood.eurobusiness.other.BaseDialog;
import com.rainwood.eurobusiness.request.RequestPost;
import com.rainwood.eurobusiness.ui.adapter.DiscountAdapter;
import com.rainwood.eurobusiness.ui.adapter.SpecialsAdapter;
import com.rainwood.eurobusiness.ui.dialog.DateDialog;
import com.rainwood.eurobusiness.ui.dialog.MenuDialog;
import com.rainwood.eurobusiness.utils.DateTimeUtils;
import com.rainwood.eurobusiness.utils.ListUtils;
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
import static com.rainwood.eurobusiness.common.Contants.GOODS_SPECIAL_REQUEST;

/**
 * @Author: a797s
 * @Date: 2020/4/8 9:33
 * @Desc: 新建采购单
 */
public final class NewPurchaseActivity extends BaseActivity implements View.OnClickListener, OnHttpListener {

    @ViewById(R.id.iv_back)
    private ImageView mPageBack;
    @ViewById(R.id.tv_title)
    private TextView mPageTitle;
    @ViewById(R.id.tv_supplier)
    private TextView tv_supplier;
    @ViewById(R.id.tv_time)
    private TextView time;
    @ViewById(R.id.tv_tax)
    private TextView tax;
    @ViewById(R.id.tv_goods_type)
    private TextView goodsType;
    @ViewById(R.id.tv_goods_name)
    private TextView goodsName;

    @ViewById(R.id.ll_special_top)
    private LinearLayout specialTop;
    @ViewById(R.id.tv_size)
    private TextView size;
    @ViewById(R.id.mlv_special)
    private MeasureListView specialList;
    // 添加规格
    @ViewById(R.id.rl_add_shop)
    private RelativeLayout addShop;
    // 折扣&
    @ViewById(R.id.tv_total_num)        // 采购总数
    private TextView totalNum;
    @ViewById(R.id.tv_purchase_price)   // 进价
    private TextView purchasePrice;
    @ViewById(R.id.mlv_discount)        // 折扣计算
    private MeasureListView discountMlv;
    @ViewById(R.id.tv_total_amount)     // 合计
    private TextView totalAmount;
    @ViewById(R.id.ll_discount)
    private LinearLayout discountUi;
    @ViewById(R.id.tv_commit_order)
    private TextView commitOrder;
    // list
    private List<String> mSupplyList;           // 供应商list
    private List<String> mTaxOptionList;        // 税率列表
    // 选择商品
    private GoodsListBean mGoods;
    private List<SkuBean> mCopySpeciList = new ArrayList<>();

    // handler
    private final int GOODS_SPECIAL_SIZE = 0x101;       // 商品规格列表
    private final int DISCOUNT_SUM_SIZE = 0x102;             // 计算折扣单价之类
    private String[] discountStr = {"优惠单价", "优惠总价", "折扣比例", "折扣总价"};
    private List<CommonUIBean> mDiscountList;
    private List<SupplierBean> mSupplierList;
    private List<ISpecialBean> mISpecialList;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_purchase_create;
    }

    @Override
    protected void initView() {
        initEvents();
        mPageTitle.setText("新建采购单");
        // request
        showLoading("");
        RequestPost.newPurchaseInfo(this);
    }

    @Override
    protected void onResume() {
        super.onResume();
        if ((ListUtils.getSize(mCopySpeciList) == 0)) {
            specialTop.setVisibility(View.GONE);
            discountUi.setVisibility(View.GONE);
        } else {
            specialTop.setVisibility(View.VISIBLE);
            discountUi.setVisibility(View.VISIBLE);
            mDiscountList = new ArrayList<>();
            for (String s : discountStr) {
                CommonUIBean common = new CommonUIBean();
                common.setTitle(s);
                mDiscountList.add(common);
            }
            Message msg = new Message();
            msg.what = DISCOUNT_SUM_SIZE;
            mHandler.sendMessage(msg);
        }
    }

    private void initEvents() {
        mPageBack.setOnClickListener(this);
        tv_supplier.setOnClickListener(this);
        time.setOnClickListener(this);
        goodsType.setOnClickListener(this);
        goodsName.setOnClickListener(this);
        addShop.setOnClickListener(this);
        tax.setOnClickListener(this);
        commitOrder.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_back:
                finish();
                break;
            case R.id.tv_supplier:          // 选择供应商
                setValue(mSupplyList, tv_supplier);
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
                setValue(mTaxOptionList, tax);
                break;
            case R.id.tv_goods_type:        // 选择商品分类
                Intent intent = new Intent(this, GoodsTypeActivity.class);
                startActivityForResult(intent, GOODS_REQUEST);
                break;
            case R.id.tv_goods_name:        // 选择商品名称   -- 带商品分类id
                Intent intent1 = new Intent(this, GoodsListActivity.class);
                intent1.putExtra("goodsTypeId", goodsType.getHint().toString().trim());
                startActivityForResult(intent1, GOODS_NAME_REQUEST);
                //toast("选择商品名称");
                break;
            case R.id.rl_add_shop:
                if (TextUtils.isEmpty(goodsName.getText())) {
                    toast("请选择商品");
                    return;
                }
                Intent intent2 = new Intent(this, AddNewGoodsActivity.class);
                intent2.putExtra("goodsId", mGoods.getGoodsId());
                startActivityForResult(intent2, GOODS_SPECIAL_REQUEST);
                //toast("添加规格");
                break;
            case R.id.tv_commit_order:
                if (TextUtils.isEmpty(tv_supplier.getText())) {
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
                if (TextUtils.isEmpty(goodsName.getText())) {
                    toast("请选择商品");
                    return;
                }
                // 有值
                String supplyId = "";
                for (SupplierBean bean : mSupplierList) {
                    if (bean.getName().equals(tv_supplier.getText().toString().trim())) {
                        supplyId = bean.getId();
                        break;
                    }
                }

                // quest -- 提交订单
                showLoading("");
                RequestPost.purchaseOrderEdit(supplyId, mGoods.getGoodsId(), tax.getText().toString().trim(),
                        time.getText().toString().trim(), "", "", "", "", "",
                        mISpecialList, this);
                break;
        }
    }

    /**
     * 选择值设置
     *
     * @param OptionList
     * @param textValue
     */
    private void setValue(List<String> OptionList, TextView textValue) {
        new MenuDialog.Builder(this)
                .setCancel(R.string.common_cancel)
                .setAutoDismiss(false)
                .setList(OptionList)
                .setCanceledOnTouchOutside(false)
                .setListener(new MenuDialog.OnListener<String>() {
                    @Override
                    public void onSelected(BaseDialog dialog, int position, String text) {
                        dialog.dismiss();
                        // toast("位置：" + position + ", 文本：" + text);
                        textValue.setText(text);
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
            mCopySpeciList = new ArrayList<>();
            mGoods = (GoodsListBean) data.getSerializableExtra("goods");
            goodsName.setText(mGoods.getName());
            Message msg = new Message();
            msg.what = GOODS_SPECIAL_SIZE;
            mHandler.sendMessage(msg);
        }
        // 规格列表
        if (requestCode == GOODS_SPECIAL_REQUEST && resultCode == GOODS_SPECIAL_REQUEST) {
            String storeName = data.getStringExtra("storeName");
            String storeId = data.getStringExtra("storeId");
            List<SkuBean> speciaList = (List<SkuBean>) data.getSerializableExtra("special");
            String num = data.getStringExtra("num");
            for (int i = 0; i < ListUtils.getSize(speciaList); i++) {
                speciaList.get(i).setStoreName(storeName);
                speciaList.get(i).setNum(num);
            }
            // 接口数据组合
            mISpecialList = new ArrayList<>();
            for (int i = 0; i < ListUtils.getSize(speciaList); i++) {
                ISpecialBean iSpecial = new ISpecialBean();
                iSpecial.setSkuId(speciaList.get(i).getId());
                iSpecial.setStoreId(storeId);
                iSpecial.setNum(num);
                mISpecialList.add(iSpecial);
            }
            mCopySpeciList.addAll(speciaList);
            Message msg = new Message();
            msg.what = GOODS_SPECIAL_SIZE;
            mHandler.sendMessage(msg);
        }
    }

    @SuppressLint("HandlerLeak")
    private Handler mHandler = new Handler() {
        @SuppressLint("SetTextI18n")
        @Override
        public void handleMessage(@NonNull Message msg) {
            switch (msg.what) {
                case GOODS_SPECIAL_SIZE:            // 规格
                    SpecialsAdapter specialsAdapter = new SpecialsAdapter(NewPurchaseActivity.this, mCopySpeciList);
                    specialList.setAdapter(specialsAdapter);
                    break;
                case DISCOUNT_SUM_SIZE:
                    // 采购总数
                    int count = 0;
                    for (SkuBean bean : mCopySpeciList) {
                        count += Integer.parseInt(bean.getNum());
                    }
                    totalNum.setText(String.valueOf(count));
                    // 进价
                    purchasePrice.setText(mGoods.getPrice());
                    // discount
                    DiscountAdapter discountAdapter = new DiscountAdapter(NewPurchaseActivity.this, mDiscountList);
                    discountMlv.setAdapter(discountAdapter);
                    int finalCount = count;
                    // 监听回调
                    discountAdapter.setOnClickEdit((adapter, position) -> {
                        // 合计
                        totalAmount.setText(Html.fromHtml("<font color=" + getResources().getColor(R.color.textColor) + " size='"
                                + FontDisplayUtil.dip2px(NewPurchaseActivity.this, 12f) + "'>合计："
                                + (finalCount * Double.parseDouble(mGoods.getPrice())
                                - Double.parseDouble((mDiscountList.get(3).getShowText() == null)
                                || ("").equals(mDiscountList.get(3).getShowText()) ? "0" : mDiscountList.get(3).getShowText())) + "</font>"));
                        // 优惠总价
                        mDiscountList.get(1).setShowText(String.valueOf(finalCount * (Double.parseDouble(mGoods.getPrice())
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
                if (result.url().contains("wxapi/v1/order.php?type=newOrderPage")) {
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
                }
                // 提交订单
                if (result.url().contains("wxapi/v1/order.php?type=purchaseOrderEdit")) {
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
