package com.rainwood.eurobusiness.ui.activity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.bumptech.glide.Glide;
import com.rainwood.eurobusiness.R;
import com.rainwood.eurobusiness.base.BaseActivity;
import com.rainwood.eurobusiness.domain.ReplementBean;
import com.rainwood.eurobusiness.domain.TitleAndLabelBean;
import com.rainwood.eurobusiness.json.JsonParser;
import com.rainwood.eurobusiness.okhttp.HttpResponse;
import com.rainwood.eurobusiness.okhttp.OnHttpListener;
import com.rainwood.eurobusiness.request.RequestPost;
import com.rainwood.eurobusiness.ui.adapter.ReplementParamsAdapter;
import com.rainwood.tools.viewinject.ViewById;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * @Author: a797s
 * @Date: 2020/4/14 10:41
 * @Desc: 批发商采购记录--- 补货申请
 */
public final class PurchaseRepleActivity extends BaseActivity implements View.OnClickListener, OnHttpListener {

    @ViewById(R.id.iv_back)
    private ImageView mPageBack;
    @ViewById(R.id.tv_title)
    private TextView mPageTitle;
    @ViewById(R.id.iv_img)
    private ImageView goodsImg;
    @ViewById(R.id.tv_name)
    private TextView goodsName;
    @ViewById(R.id.tv_model)
    private TextView goodsModel;
    @ViewById(R.id.tv_discount)
    private TextView goodsDiscount;
    @ViewById(R.id.tv_rate)
    private TextView goodsRate;
    @ViewById(R.id.gv_special)
    private GridView specialList;
    @ViewById(R.id.lv_note)
    private ListView noteList;
    @ViewById(R.id.btn_agree)
    private Button agree;

    private List<TitleAndLabelBean> specialInititlList;
    private List<TitleAndLabelBean> noteInititlList;
    private String[] specialParams = {"进行：", "税率：", "批发价：", "增值税：", "零售价：", "最小起订量："};
    private String[] noteParams = {"交货时间\t\t", "补货数量\t\t", "备注\t\t\t"};

    private final int INITIAL_SIZE = 0x101;
    private ReplementBean mReplementBean;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_purchase_replen;
    }

    @Override
    protected void initView() {
        initEvents();
    }

    @Override
    protected void initData() {
        super.initData();
        specialInititlList = new ArrayList<>();
        for (String specialParam : specialParams) {
            TitleAndLabelBean andLabelBean = new TitleAndLabelBean();
            andLabelBean.setTitle(specialParam);
            andLabelBean.setFontSize(13);
            andLabelBean.setLabelColorSize(getResources().getColor(R.color.white66));
            specialInititlList.add(andLabelBean);
        }
        noteInititlList = new ArrayList<>();
        for (String noteParam : noteParams) {
            TitleAndLabelBean andLabelBean = new TitleAndLabelBean();
            andLabelBean.setTitle(noteParam);
            andLabelBean.setFontSize(16);
            andLabelBean.setLabelColorSize(getResources().getColor(R.color.textColor));
            noteInititlList.add(andLabelBean);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        String orderId = getIntent().getStringExtra("orderId");
        // TODO
        showLoading("");
        RequestPost.getPurchaseOrderDetail(orderId, this);
    }

    private void initEvents() {
        mPageBack.setOnClickListener(this);
        agree.setOnClickListener(this);
        mPageTitle.setText("补货申请");
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_back:
                finish();
                break;
            case R.id.btn_agree:            // 补货单号
                Intent intent = new Intent(this, AuditOrderActivity.class);
                intent.putExtra("orderNo", mReplementBean.getOrderNo());
                startActivity(intent);
                break;
        }
    }

    @SuppressLint("HandlerLeak")
    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(@NonNull Message msg) {
            switch (msg.what) {
                case INITIAL_SIZE:
                    ReplementParamsAdapter paramsAdapter = new ReplementParamsAdapter(PurchaseRepleActivity.this, specialInititlList);
                    specialList.setNumColumns(2);
                    specialList.setAdapter(paramsAdapter);
                    ReplementParamsAdapter noteAdapter = new ReplementParamsAdapter(PurchaseRepleActivity.this, noteInititlList);
                    noteList.setAdapter(noteAdapter);
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
        Map<String, String> body = JsonParser.parseJSONObject(result.body());
        if (body != null) {
            if ("1".equals(body.get("code"))) {
                // 获取补货订单详情
                if (result.url().contains("wxapi/v1/order.php?type=getPurchaseOrderInfo")) {
                    mReplementBean = JsonParser.parseJSONObject(ReplementBean.class, JsonParser.parseJSONObject(body.get("data")).get("info"));
                    // initial_data
                    if (mReplementBean != null) {
                        for (int i = 0; i < specialInititlList.size(); i++) {
                            switch (i) {
                                case 0:         // 进价
                                    specialInititlList.get(i).setLabel(mReplementBean.getPrice());
                                    break;
                                case 1:         // 税率
                                    specialInititlList.get(i).setLabel(mReplementBean.getTaxRate());
                                    break;
                                case 2:         // 批发价
                                    specialInititlList.get(i).setLabel(mReplementBean.getTradePrice());
                                    break;
                                case 3:         // 增值税
                                    specialInititlList.get(i).setLabel(mReplementBean.getTax());
                                    break;
                                case 4:         // 零售价
                                    specialInititlList.get(i).setLabel(mReplementBean.getRetailPrice());
                                    break;
                                case 5:         // 最小起订量
                                    specialInititlList.get(i).setLabel(mReplementBean.getStartNum());
                                    break;
                            }
                        }
                        for (int i = 0; i < noteInititlList.size(); i++) {
                            switch (i) {
                                case 0:         // 交货时间
                                    noteInititlList.get(i).setLabel(mReplementBean.getDeliveryDate());
                                    break;
                                case 1:         // 补货数量
                                    noteInititlList.get(i).setLabel(mReplementBean.getChargeNum());
                                    break;
                                case 2:         // 备注
                                    noteInititlList.get(i).setLabel(mReplementBean.getText());
                                    break;
                            }
                        }
                    }

                    // goodsInfo
                    if (mReplementBean != null) {
                        Glide.with(this).load(mReplementBean.getIco())
                                .placeholder(R.drawable.icon_loadding_fail)
                                .error(R.drawable.icon_loadding_fail).into(goodsImg);
                        goodsName.setText(mReplementBean.getGoodsName());
                        goodsModel.setText(mReplementBean.getModel());
                        goodsDiscount.setText(mReplementBean.getDiscount() + "%");
                        goodsRate.setText(mReplementBean.getTaxRate() + "%");
                        if ("待审核".equals(mReplementBean.getWorkFlow())){
                            agree.setVisibility(View.VISIBLE);
                        }else {
                            agree.setVisibility(View.GONE);
                        }
                    }

                    Message msg = new Message();
                    msg.what = INITIAL_SIZE;
                    mHandler.sendMessage(msg);
                }
            } else {
                toast(body.get("warn"));
            }
            if (getDialog() != null)
                dismissLoading();
        }
    }
}
