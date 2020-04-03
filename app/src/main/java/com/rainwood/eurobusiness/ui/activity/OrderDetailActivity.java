package com.rainwood.eurobusiness.ui.activity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.bumptech.glide.Glide;
import com.rainwood.eurobusiness.R;
import com.rainwood.eurobusiness.base.BaseActivity;
import com.rainwood.eurobusiness.common.Contants;
import com.rainwood.eurobusiness.domain.CommonUIBean;
import com.rainwood.eurobusiness.domain.OrderDetailBean;
import com.rainwood.eurobusiness.domain.PressBean;
import com.rainwood.eurobusiness.domain.SpecialBean;
import com.rainwood.eurobusiness.json.JsonParser;
import com.rainwood.eurobusiness.okhttp.HttpResponse;
import com.rainwood.eurobusiness.okhttp.OnHttpListener;
import com.rainwood.eurobusiness.request.RequestPost;
import com.rainwood.eurobusiness.ui.adapter.PayMethodAdapter;
import com.rainwood.eurobusiness.ui.adapter.SizeAdapter;
import com.rainwood.eurobusiness.ui.adapter.StoresListAdapter;
import com.rainwood.eurobusiness.utils.DateTimeUtils;
import com.rainwood.eurobusiness.utils.ListUtils;
import com.rainwood.tools.common.FontDisplayUtil;
import com.rainwood.tools.statusbar.StatusBarUtil;
import com.rainwood.tools.viewinject.ViewById;
import com.rainwood.tools.widget.MeasureGridView;
import com.rainwood.tools.widget.MeasureListView;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * @Author: shearson
 * @Time: 2020/2/25 12:08
 * @Desc: 订单详情
 */
public class OrderDetailActivity extends BaseActivity implements View.OnClickListener, OnHttpListener {

    private String mOrderId;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_order_detail;
    }

    @ViewById(R.id.iv_back)
    private ImageView pageBack;
    @ViewById(R.id.tv_title)
    private TextView pageTitle;
    @ViewById(R.id.tv_right_text)
    private TextView topText;
    @ViewById(R.id.rl_top_item)
    private RelativeLayout topItem;
    @ViewById(R.id.rl_action_bar)
    private RelativeLayout actionBar;
    @ViewById(R.id.tv_status)
    private TextView status;
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
    @ViewById(R.id.lv_content_list)             // 公司参数
    private MeasureListView contentList;
    @ViewById(R.id.lv_params_list)              // 订单参数
    private MeasureListView paramsList;
    @ViewById(R.id.gv_params_list)              // 汇总参数
    private MeasureGridView paramsbottomList;

    //
    @ViewById(R.id.tv_company_name)         // 公司名称
    private TextView companyName;
    @ViewById(R.id.tv_address)              // 收货地址
    private TextView address;
    @ViewById(R.id.tv_time_commit)          // 交货时间
    private TextView commitTime;
    @ViewById(R.id.tv_text)                 // 备注
    private TextView note;
    @ViewById(R.id.tv_order_type)           // 线上订单、线下订单
    private TextView orderType;
    @ViewById(R.id.btn_confirm)             // 确认收款、确认收货
    private Button confirm;

    private List<CommonUIBean> companyInfoList;
    private String[] topTitles = {"公司名称", "税号(P.IVA)：", "PEC邮箱/SDI：", "收货人", "地区"};
    private List<SpecialBean> sizeLists;
    private List<CommonUIBean> bottomInfoList;
    private String[] bottomTitle = {"运费", "增值税", "折扣比例", "VIP优惠", "折扣总价", "订单总计"};

    private final int INITIAL_SIZE = 0x101;
    private PopupWindow mPopupWindow;
    private List<PressBean> mPayMethods;


    @SuppressLint("SetTextI18n")
    @Override
    protected void initView() {
        initContext();
    }

    @Override
    protected void onResume() {
        super.onResume();
        mOrderId = getIntent().getStringExtra("orderId");
        if (mOrderId != null) {
            showLoading("");
            RequestPost.getBuyCarDetail(mOrderId, this);
        }
    }

    @Override
    protected void initData() {
        super.initData();
        companyInfoList = new ArrayList<>();
        for (int i = 0; i < topTitles.length; i++) {
            CommonUIBean commonUI = new CommonUIBean();
            commonUI.setTitle(topTitles[i]);
            companyInfoList.add(commonUI);
        }
        bottomInfoList = new ArrayList<>();
        for (int i = 0; i < bottomTitle.length; i++) {
            CommonUIBean commonUI = new CommonUIBean();
            commonUI.setTitle(bottomTitle[i]);
            bottomInfoList.add(commonUI);
        }
    }

    /**
     * initView
     */
    private void initContext() {
        pageBack.setOnClickListener(this);
        pageBack.setImageResource(R.drawable.icon_white_page_back);
        pageTitle.setText("订单详情");
        pageTitle.setTextColor(getResources().getColor(R.color.white));
        confirm.setOnClickListener(this);
        // 门店端订单管理才能收款或者发货
        if (Contants.CHOOSE_MODEL_SIZE == 4){
            confirm.setVisibility(View.VISIBLE);
        }else {
            confirm.setVisibility(View.GONE);
        }
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
            case R.id.btn_confirm:
                if ("确认收款".equals(confirm.getText().toString().trim())) {
                    paymentdMethod();
                }
                if ("确认发货".equals(confirm.getText().toString().trim())) {
                    // toast("确认发货");
                    showLoading("");
                    RequestPost.changeBuyCarWorkFlow(mOrderId, "waitRec", "", OrderDetailActivity.this);
                }
                break;
        }
    }

    /**
     * 支付方式--- 确认收款
     */
    private void paymentdMethod() {
        View rootView = LayoutInflater.from(this).inflate(R.layout.activity_order_detail, null);
        View view = View.inflate(this, R.layout.dialog_pay_methods, null);
        mPopupWindow = new PopupWindow(view, ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT, true);
        mPopupWindow.setOutsideTouchable(false);
        mPopupWindow.setClippingEnabled(false);
        mPopupWindow.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
        mPopupWindow.setAnimationStyle(R.style.BottomAnimStyle);
        mPopupWindow.showAtLocation(rootView, Gravity.CENTER, 0, 0);
        mPopupWindow.setOutsideTouchable(false);
        popOutShadow(mPopupWindow);
        Button btnCancel = view.findViewById(R.id.btn_cancel);
        btnCancel.setOnClickListener(v -> {
            //toast("取消了");
            mPopupWindow.dismiss();
        });
        Button btnConfirm = view.findViewById(R.id.btn_confirm);
        btnConfirm.setOnClickListener(v -> {
            // toast("确定了");
            String type = "";
            for (PressBean payMethod : mPayMethods) {
                if (payMethod.isChoose()) {
                    type = payMethod.getTitle();
                    break;
                }
            }
            // 确认收款
            mPopupWindow.dismiss();
            showLoading("");
            RequestPost.changeBuyCarWorkFlow(mOrderId, "complete", type, OrderDetailActivity.this);
        });
        // 数据
        ListView method = view.findViewById(R.id.lv_method);
        PayMethodAdapter methodAdapter = new PayMethodAdapter(this, mPayMethods);
        method.setAdapter(methodAdapter);
        methodAdapter.setOnClickItem(position -> {
            for (PressBean payMethod : mPayMethods) {
                payMethod.setChoose(false);
            }
            mPayMethods.get(position).setChoose(true);
        });
    }

    @SuppressLint("HandlerLeak")
    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(@NonNull Message msg) {
            switch (msg.what) {
                case INITIAL_SIZE:
                    StoresListAdapter storesListAdapter = new StoresListAdapter(OrderDetailActivity.this, companyInfoList);
                    contentList.setAdapter(storesListAdapter);
                    SizeAdapter sizeAdapter = new SizeAdapter(OrderDetailActivity.this, sizeLists, status.getText().toString().trim());
                    paramsList.setAdapter(sizeAdapter);
                    sizeAdapter.setOnClickRefunds(position -> {
                        // 退货
                        Intent intent = new Intent(OrderDetailActivity.this, ReGoodsApplyActivity.class);
                        intent.putExtra("specialId", sizeLists.get(position).getId());
                        startActivity(intent);
                    });
                    StoresListAdapter bottomAdapter = new StoresListAdapter(OrderDetailActivity.this, bottomInfoList);
                    paramsbottomList.setNumColumns(2);
                    paramsbottomList.setAdapter(bottomAdapter);
                    break;
            }
        }
    };

    /**
     * 让popupwindow以外区域阴影显示
     *
     * @param popupWindow
     */
    private void popOutShadow(PopupWindow popupWindow) {
        WindowManager.LayoutParams lp = getWindow().getAttributes();
        lp.alpha = 0.7f;//设置阴影透明度
        getWindow().setAttributes(lp);
        popupWindow.setOnDismissListener(() -> {
            WindowManager.LayoutParams lp1 = getWindow().getAttributes();
            lp1.alpha = 1f;
            getWindow().setAttributes(lp1);
        });
    }

    @Override
    public void onHttpFailure(HttpResponse result) {

    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onHttpSucceed(HttpResponse result) {
        Log.d(TAG, " ==== result ==== " + result);
        Map<String, String> body = JsonParser.parseJSONObject(result.body());
        if (body != null) {
            if ("1".equals(body.get("code"))) {
                // 获取订单详情
                if (result.url().contains("wxapi/v1/order.php?type=getBuyCarInfo")) {
                    OrderDetailBean orderdetail = JsonParser.parseJSONObject(OrderDetailBean.class, JsonParser.parseJSONObject(body.get("data")).get("info"));
                    sizeLists = JsonParser.parseJSONArray(SpecialBean.class, JsonParser.parseJSONObject(body.get("data")).get("skulist"));
                    // 支付方式
                    JSONArray jsonArray = JsonParser.parseJSONArrayString(JsonParser.parseJSONObject(body.get("data")).get("payType"));
                    mPayMethods = new ArrayList<>();
                    for (int i = 0; i < jsonArray.length(); i++) {
                        PressBean press = new PressBean();
                        try {
                            press.setTitle(jsonArray.getString(i));
                            mPayMethods.add(press);
                        } catch (JSONException e) {
                            toast("数据异常");
                        }
                    }
                    // 商品信息
                    if (orderdetail != null) {
                        status.setText(orderdetail.getWorkFlow());
                        Glide.with(this).load(orderdetail.getIco()).into(image);
                        name.setText(orderdetail.getGoodsName());
                        model.setText(orderdetail.getModel());
                        discount.setText(orderdetail.getDiscount() + " %");
                        rate.setText(orderdetail.getTaxRate() + "%");
                        orderType.setText("offline ".equals(orderdetail.getType()) ? "线下订单" : "线上订单");
                        companyName.setText(orderdetail.getCompanyName());          // 公司名称
                        address.setText(orderdetail.getAddressMx());
                        commitTime.setText(DateTimeUtils.Local2UTC());           // 交货时间
                        note.setText(orderdetail.getText());
                        // button confirm
                        confirm.setVisibility(View.VISIBLE);
                        if ("待收款".equals(orderdetail.getWorkFlow())) {
                            confirm.setBackgroundResource(R.drawable.selector_yellow_button);
                            confirm.setText("确认收款");
                        } else if ("待发货".equals(orderdetail.getWorkFlow())) {
                            confirm.setBackgroundResource(R.drawable.selector_red_button);
                            confirm.setText("确认发货");
                        } else {
                            confirm.setVisibility(View.GONE);
                        }
                    }
                    // 赋值 -- 商品规格信息
                    if (orderdetail != null)
                        for (int i = 0; i < ListUtils.getSize(companyInfoList); i++) {
                            switch (i) {
                                case 0:                 // 公司名称
                                    companyInfoList.get(i).setShowText(orderdetail.getInvoiceCompany());
                                    break;
                                case 1:                 // 税号
                                    companyInfoList.get(i).setShowText(orderdetail.getInvoiceTax());
                                    break;
                                case 2:                 // 邮箱
                                    companyInfoList.get(i).setShowText(orderdetail.getInvoiceEmail());
                                    break;
                                case 3:                 // 收货人
                                    companyInfoList.get(i).setShowText(orderdetail.getContactName());
                                    break;
                                case 4:                 // 地区
                                    companyInfoList.get(i).setShowText(orderdetail.getRegion());
                                    break;
                            }
                        }
                    //sizeLists = specialList;
                    for (int i = 0; i < ListUtils.getSize(bottomInfoList); i++) {
                        switch (i) {
                            case 0:             // 运费
                                bottomInfoList.get(i).setShowText(orderdetail.getFreight());
                                break;
//                            case 1:             // 折扣单价
//                                bottomInfoList.get(i).setShowText(orderdetail.getFreight());
//                                break;
                            case 1:             // 增值税
                                bottomInfoList.get(i).setShowText("1".equals(orderdetail.getIsTax()) ? "含税" : "不含税");
                                break;
                            case 2:             // 折扣比例
                                bottomInfoList.get(i).setShowText(orderdetail.getDiscount() + "%");
                                break;
                            case 3:             // VIP优惠
                                bottomInfoList.get(i).setShowText(orderdetail.getKehuDecMoney());
                                break;
                            case 4:             // 折扣总价
                                bottomInfoList.get(i).setShowText(orderdetail.getMoney());
                                break;
                            case 5:             // 订单总价
                                bottomInfoList.get(i).setShowText(orderdetail.getMoney());
                                break;
                        }
                    }

                    Message msg = new Message();
                    msg.what = INITIAL_SIZE;
                    mHandler.sendMessage(msg);
                }
                // 确认收款
                if (result.url().contains("wxapi/v1/order.php?type=changeBuyCarWorkFlow")) {
                    toast(body.get("warn"));
                    //
                    RequestPost.getBuyCarDetail(mOrderId, this);
                }
            } else {
                toast(body.get("warn"));
            }
            dismissLoading();
        }
    }
}
