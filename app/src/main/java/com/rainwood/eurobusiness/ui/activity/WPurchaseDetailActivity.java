package com.rainwood.eurobusiness.ui.activity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.bumptech.glide.Glide;
import com.rainwood.eurobusiness.R;
import com.rainwood.eurobusiness.base.BaseActivity;
import com.rainwood.eurobusiness.domain.CommonUIBean;
import com.rainwood.eurobusiness.domain.PurchaseDetailBean;
import com.rainwood.eurobusiness.domain.SpecialParamBean;
import com.rainwood.eurobusiness.json.JsonParser;
import com.rainwood.eurobusiness.okhttp.HttpResponse;
import com.rainwood.eurobusiness.okhttp.OnHttpListener;
import com.rainwood.eurobusiness.request.RequestPost;
import com.rainwood.eurobusiness.ui.adapter.PurchaseDetatilAdapter;
import com.rainwood.eurobusiness.ui.adapter.StoresListAdapter;
import com.rainwood.eurobusiness.utils.ListUtils;
import com.rainwood.tools.common.FontDisplayUtil;
import com.rainwood.tools.statusbar.StatusBarUtil;
import com.rainwood.tools.viewinject.ViewById;
import com.rainwood.tools.widget.MeasureListView;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * @Author: a797s
 * @Date: 2020/4/15 17:12
 * @Desc: 批发商采购单详情
 */
public final class WPurchaseDetailActivity extends BaseActivity implements View.OnClickListener, OnHttpListener {

    @ViewById(R.id.iv_back)
    private ImageView mPageBack;
    @ViewById(R.id.tv_title)
    private TextView mPageTitle;
    @ViewById(R.id.rl_top_item)
    private RelativeLayout topItem;
    @ViewById(R.id.rl_action_bar)
    private RelativeLayout actionBar;
    @ViewById(R.id.tv_status)
    private TextView status;
    @ViewById(R.id.tv_supplier)
    private TextView supplier;
    @ViewById(R.id.tv_delivery_time)
    private TextView deliveryTime;
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
    @ViewById(R.id.mlv_special_params)       // 各个门店的规格、默认展示一个门店，展开收缩所有门店
    private MeasureListView specialParams;
    @ViewById(R.id.iv_expand)
    private ImageView expandStore;
    @ViewById(R.id.mlv_note)
    private MeasureListView noteInfo;
    @ViewById(R.id.btn_query)           // 查看明细
    private Button queryDetail;

    private final int INITIAL_SIZE = 0x101;
    private final int SPECIAL_SIZE = 0x102;
    private List<SpecialParamBean> mSpecialParamList;
    private PurchaseDetailBean mPurchaseDetail;
    private List<SpecialParamBean> mCopySpecialList;
    private String[] note = {"采购总数", "折扣单价", "折扣比例", "折扣总价"};
    private boolean hasExpand = true;          // 规格收缩标志、默认收缩

    @Override
    protected int getLayoutId() {
        return R.layout.activity_saler_purchase_detail;
    }

    @Override
    protected void initView() {
        initEvents();
        String goodsId = getIntent().getStringExtra("goodsId");
        if (goodsId != null) {
            // TODO: 批发商采购单详情数据
            showLoading("");
            RequestPost.getPurchaseOrderDetail(goodsId, this);
        }
    }

    private void initEvents() {
        mPageBack.setOnClickListener(this);
        queryDetail.setOnClickListener(this);
        expandStore.setOnClickListener(this);
        mPageBack.setImageResource(R.drawable.icon_white_page_back);
        mPageTitle.setTextColor(getResources().getColor(R.color.white));
        mPageTitle.setText("采购单详情");
        // 设置状态栏
        StatusBarUtil.setCommonUI(getActivity(), false);
        StatusBarUtil.setRootViewFitsSystemWindows(getActivity(), false);
        // 设置RelativeLayout 的高度
        ViewGroup.LayoutParams params = topItem.getLayoutParams();
        params.height = FontDisplayUtil.dip2px(this, 180f) + StatusBarUtil.getStatusBarHeight(this);
        topItem.setLayoutParams(params);
        // 设置标题栏高度和外边距
        ViewGroup.MarginLayoutParams layoutParams = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                FontDisplayUtil.dip2px(this, 44f));
        layoutParams.setMargins(0, FontDisplayUtil.dip2px(this, 40), 0, 0);
        actionBar.setLayoutParams(layoutParams);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_back:
                finish();
                break;
            case R.id.btn_query:            // 查看门店入库详细
                // toast("查看明细");
                Intent intent = new Intent(this, StoreInventoryActivity.class);
                intent.putExtra("orderNo", mPurchaseDetail.getOrderNo());
                startActivity(intent);
                break;
            case R.id.iv_expand:            // 规格收缩
                hasExpand = !hasExpand;
                if (hasExpand){                 // 如果收缩
                    expandStore.setImageResource(R.drawable.ic_down_arrow);
                    mCopySpecialList = new ArrayList<>();
                    String storeFlag = "";
                    for (SpecialParamBean paramBean : mSpecialParamList) {
                        if ("".equals(storeFlag)){      // 把第一个规格赋值
                            storeFlag = paramBean.getStoreName();
                        }
                        if (storeFlag.equals(paramBean.getStoreName())){    // 找出同一个门店的所有规格、即只展示一个门店数据
                            mCopySpecialList.add(paramBean);
                        }
                    }
                    Message msg = new Message();
                    msg.what = SPECIAL_SIZE;
                    mHandler.sendMessage(msg);
                }else {
                    expandStore.setImageResource(R.drawable.ic_up_sign_arrow);
                    mCopySpecialList = mSpecialParamList;
                    Message msg = new Message();
                    msg.what = SPECIAL_SIZE;
                    mHandler.sendMessage(msg);
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
                    status.setText(mPurchaseDetail.getWorkFlow());
                    supplier.setText(mPurchaseDetail.getSupplierName());
                    deliveryTime.setText(mPurchaseDetail.getDeliveryDate());
                    Glide.with(WPurchaseDetailActivity.this).load(mPurchaseDetail.getIco())
                            .error(R.drawable.icon_loadding_fail)
                            .placeholder(R.drawable.icon_loadding_fail).into(goodsImg);
                    goodsName.setText(mPurchaseDetail.getGoodsName());
                    model.setText(mPurchaseDetail.getModel());
                    discount.setText(mPurchaseDetail.getDiscount() + "%");
                    rate.setText(mPurchaseDetail.getTaxRate() + "%");
                    // 商品规格 -- 默认展示一个门店的规格，点击展开所有
                    mCopySpecialList = new ArrayList<>();
                    String storeFlag = "";
                    for (SpecialParamBean paramBean : mSpecialParamList) {
                        if ("".equals(storeFlag)){      // 把第一个规格赋值
                            storeFlag = paramBean.getStoreName();
                        }
                        if (storeFlag.equals(paramBean.getStoreName())){    // 找出同一个门店的所有规格、即只展示一个门店数据
                            mCopySpecialList.add(paramBean);
                        }
                    }
                    hasExpand = false;
                    Message sizeMsg = new Message();
                    sizeMsg.what = SPECIAL_SIZE;
                    mHandler.sendMessage(sizeMsg);

                    // 门店详情数据
                    List<CommonUIBean> noteList = new ArrayList<>();
                    for (String s : note) {
                        CommonUIBean bean = new CommonUIBean();
                        bean.setTitle(s);
                        noteList.add(bean);
                    }
                    for (int i = 0; i < ListUtils.getSize(noteList); i++) {
                        switch (i){
                            case 0:
                                noteList.get(i).setShowText(mPurchaseDetail.getTotalBuy());
                                break;
                            case 1:
                                noteList.get(i).setShowText(mPurchaseDetail.getDiscountPrice());
                                break;
                            case 2:
                                noteList.get(i).setShowText(mPurchaseDetail.getDiscount());
                                break;
                            case 3:
                                noteList.get(i).setShowText(mPurchaseDetail.getTotalMoney());
                                break;
                        }
                    }
                    StoresListAdapter adapter = new StoresListAdapter(WPurchaseDetailActivity.this, noteList);
                    noteInfo.setAdapter(adapter);
                    break;
                case SPECIAL_SIZE:
                    PurchaseDetatilAdapter detatilAdapter = new PurchaseDetatilAdapter(WPurchaseDetailActivity.this, mCopySpecialList);
                    specialParams.setAdapter(detatilAdapter);
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
                // 获取采购单详情
                if (result.url().contains("wxapi/v1/order.php?type=getPurchaseOrderInfo")) {
                    mPurchaseDetail = JsonParser.parseJSONObject(PurchaseDetailBean.class, JsonParser.parseJSONObject(body.get("data")).get("info"));
                    mSpecialParamList = JsonParser.parseJSONArray(SpecialParamBean.class, JsonParser.parseJSONObject(body.get("data")).get("skulist"));

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
