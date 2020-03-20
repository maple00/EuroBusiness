package com.rainwood.eurobusiness.ui.activity;

import android.text.Html;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.rainwood.eurobusiness.R;
import com.rainwood.eurobusiness.base.BaseActivity;
import com.rainwood.eurobusiness.domain.ReturnGoodsBean;
import com.rainwood.tools.view.ClearEditText;
import com.rainwood.tools.viewinject.ViewById;

/**
 * @Author: a797s
 * @Date: 2020/2/22
 * @Desc: 退货申请
 */
public class ReGoodsApplyActivity extends BaseActivity implements View.OnClickListener {


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
    @ViewById(R.id.ll_custom_pay)
    private LinearLayout customPay;
    @ViewById(R.id.iv_unchecked)
    private ImageView unchecked;
    @ViewById(R.id.ll_others_pay)
    private LinearLayout otherPay;          // 批发商支付
    @ViewById(R.id.iv_checked)
    private ImageView checked;
    @ViewById(R.id.btn_confirm)
    private Button confirm;

    @Override
    protected void initView() {
        initContent();

        image.setImageResource(R.drawable.icon_loadding_fail);
        name.setText(goods.getName());
        model.setText(goods.getModel());
        discount.setText(goods.getDiscount());
        rate.setText(goods.getRate());
        params.setText(goods.getParams());

        returnNum.setText(Html.fromHtml("<font color=" + getResources().getColor(R.color.white66) + " size='14px'>退货数量\u3000\u3000</font>"
                + "<font color=" + getResources().getColor(R.color.yellow05) + " size='12px'>" + "最多可退200件" + "</font>"));
        reason.setText("退货原因");
        fee.setText("运费");

    }

    @Override
    protected void initData() {
        super.initData();
        goods = new ReturnGoodsBean();
        goods.setName("西装外套式系缀扣连衣裙");
        goods.setModel("XDF-256165");
        goods.setDiscount("25%折扣");
        goods.setRate("16%税率");
        goods.setParams("杏色/XL");

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_back:
                finish();
                break;
            case R.id.btn_confirm:
                toast("确定");
                break;
            case R.id.ll_custom_pay:
                // toast("客户支付");
                unchecked.setImageResource(R.drawable.radio_checked_shape);
                checked.setImageResource(R.drawable.radio_uncheck_shape);
                break;
            case R.id.ll_others_pay:
                // toast("批发商支付");
                unchecked.setImageResource(R.drawable.radio_uncheck_shape);
                checked.setImageResource(R.drawable.radio_checked_shape);
                break;
        }
    }

    /**
     * init Content
     */
    private void initContent() {
        pageBack.setOnClickListener(this);
        pageTitle.setText("退货申请");
        confirm.setOnClickListener(this);
        customPay.setOnClickListener(this);
        otherPay.setOnClickListener(this);
    }

    /*
    模拟数据
     */
    ReturnGoodsBean goods;
}
