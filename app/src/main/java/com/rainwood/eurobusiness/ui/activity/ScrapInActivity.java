package com.rainwood.eurobusiness.ui.activity;

import android.annotation.SuppressLint;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.bumptech.glide.Glide;
import com.rainwood.eurobusiness.R;
import com.rainwood.eurobusiness.base.BaseActivity;
import com.rainwood.eurobusiness.domain.RefundDetailBean;
import com.rainwood.eurobusiness.json.JsonParser;
import com.rainwood.eurobusiness.okhttp.HttpResponse;
import com.rainwood.eurobusiness.okhttp.OnHttpListener;
import com.rainwood.eurobusiness.request.RequestPost;
import com.rainwood.tools.view.ClearEditText;
import com.rainwood.tools.viewinject.ViewById;

import java.util.Map;

/**
 * @Author: a797s
 * @Date: 2020/4/16 10:35
 * @Desc: 入库报废
 */
public final class ScrapInActivity extends BaseActivity implements View.OnClickListener, OnHttpListener {

    @ViewById(R.id.iv_back)
    private ImageView mPageBack;
    @ViewById(R.id.tv_title)
    private TextView mPageTitle;
    @ViewById(R.id.iv_img)
    private ImageView goodsImg;
    @ViewById(R.id.tv_name)
    private TextView goodsName;
    @ViewById(R.id.tv_model)
    private TextView model;
    @ViewById(R.id.tv_discount)
    private TextView discount;
    @ViewById(R.id.tv_rate)
    private TextView rate;
    @ViewById(R.id.tv_params)
    private TextView params;
    //
    @ViewById(R.id.tv_storage)
    private TextView storage;
    @ViewById(R.id.cet_storage_num)
    private ClearEditText storageNum;
    @ViewById(R.id.tv_reason)
    private TextView reson;
    @ViewById(R.id.cet_reason)
    private ClearEditText reason;
    @ViewById(R.id.tv_line)
    private TextView line;
    @ViewById(R.id.btn_confirm)
    private Button confirm;

    private final int INITIAL_SIZE = 0x101;
    private RefundDetailBean mRefundDetail;
    private String mFlag;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_scrap_storage;
    }

    @Override
    protected void initView() {
        initEvents();
        mFlag = getIntent().getStringExtra("flag");
        if ("inStorage".equals(mFlag)) {
            reson.setVisibility(View.GONE);
            reason.setVisibility(View.GONE);
            line.setVisibility(View.GONE);
            storage.setText("入库数量");
            mPageTitle.setText("入库");
        } else {
            reson.setVisibility(View.VISIBLE);
            reason.setVisibility(View.VISIBLE);
            line.setVisibility(View.VISIBLE);
            storage.setText("报废数量");
            mPageTitle.setText("报废");
        }
        String goodsId = getIntent().getStringExtra("goodsId");
        if (goodsId != null) {
            showLoading("");
            RequestPost.returnGoodsDetail(goodsId, this);
        }else {
            toast("数据异常");
            finish();
        }
    }

    private void initEvents() {
        mPageBack.setOnClickListener(this);
        confirm.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_back:
                finish();
                break;
            case R.id.btn_confirm:
                if ("inStorage".equals(mFlag)) {        // 入库
                    if (TextUtils.isEmpty(storageNum.getText())) {
                        toast("请输入入库数量");
                        return;
                    }
                    // TODO:
                    showLoading("");
                    RequestPost.refundOrderInStock(mRefundDetail.getId(), "pass", storageNum.getText().toString().trim(), "", this);
                } else {
                    if (TextUtils.isEmpty(storageNum.getText())) {
                        toast("请输入报废数量");
                        return;
                    }
                    RequestPost.refundOrderInStock(mRefundDetail.getId(), "refuse", storageNum.getText().toString().trim(),
                            TextUtils.isEmpty(reason.getText()) ? "" : reason.getText().toString().trim(), this);
                }
                break;
        }
    }

    @SuppressLint("HandlerLeak")
    private Handler mHandler = new Handler() {
        @SuppressLint("SetTextI18n")
        @Override
        public void handleMessage(@NonNull Message msg) {
            switch (msg.what) {
                case INITIAL_SIZE:
                    Glide.with(ScrapInActivity.this)
                            .load(mRefundDetail.getIco())
                            .error(R.drawable.icon_loadding_fail)
                            .placeholder(R.drawable.icon_loadding_fail).into(goodsImg);
                    goodsName.setText(mRefundDetail.getGoodsName());
                    model.setText(mRefundDetail.getModel());
                    discount.setText(mRefundDetail.getDiscount() + "%");
                    rate.setText(mRefundDetail.getTaxRate() + "%");
                    params.setText(mRefundDetail.getSkuName());

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
            if ("1".equals(body.get("code"))) {         // 查看商品详情
                if (result.url().contains("wxapi/v1/order.php?type=getRefundOrder")) {
                    mRefundDetail = JsonParser.parseJSONObject(RefundDetailBean.class,
                            JsonParser.parseJSONObject(body.get("data")).get("info"));

                    Message msg = new Message();
                    msg.what = INITIAL_SIZE;
                    mHandler.sendMessage(msg);
                }

                // 订单入库或报废
                if (result.url().contains("wxapi/v1/order.php?type=refundOrderInStock")){
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
