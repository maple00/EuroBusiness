package com.rainwood.eurobusiness.ui.activity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.bumptech.glide.Glide;
import com.rainwood.eurobusiness.R;
import com.rainwood.eurobusiness.base.BaseActivity;
import com.rainwood.eurobusiness.domain.CommonUIBean;
import com.rainwood.eurobusiness.domain.RefundDetailBean;
import com.rainwood.eurobusiness.json.JsonParser;
import com.rainwood.eurobusiness.okhttp.HttpResponse;
import com.rainwood.eurobusiness.okhttp.OnHttpListener;
import com.rainwood.eurobusiness.request.RequestPost;
import com.rainwood.eurobusiness.ui.adapter.StoresListAdapter;
import com.rainwood.tools.common.FontDisplayUtil;
import com.rainwood.tools.statusbar.StatusBarUtil;
import com.rainwood.tools.view.ClearEditText;
import com.rainwood.tools.viewinject.ViewById;
import com.rainwood.tools.widget.MeasureListView;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * @Author: a797s
 * @Date: 2020/2/21
 * @Desc: 退货详情
 */
public class ReGoodsDetailActivity extends BaseActivity implements View.OnClickListener, OnHttpListener {

    private RefundDetailBean mDetail;
    private List<CommonUIBean> mUIList;
    private String mFlag;
    private PopupWindow mPopupWindow;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_return_goods_detail;
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
    @ViewById(R.id.lv_content_list)
    private MeasureListView contentList;

    // 状态删除/驳回
    @ViewById(R.id.tv_reason)
    private TextView reason;
    @ViewById(R.id.ll_status_hint)
    private LinearLayout statusHint;
    @ViewById(R.id.btn_delete)
    private Button delete;
    @ViewById(R.id.btn_edit)
    private Button edit;

    // mHandler
    private final int INITIAL_SIZE = 0x101;

    @Override
    protected void initView() {
        // initView
        initContext();
        String goodsId = getIntent().getStringExtra("goodsId");
        // TODO:商品详情
        if (goodsId != null) {
            RequestPost.returnGoodsDetail(goodsId, this);
        } else {
            toast("数据异常");
            finish();
        }
        mFlag = getIntent().getStringExtra("flag");
        if (mFlag == null) {
            toast("数据异常");
            finish();
        }
    }

    @Override
    protected void initData() {
        super.initData();
        mUIList = new ArrayList<>();
        for (int i = 0; i < titles.length; i++) {
            CommonUIBean commonUI = new CommonUIBean();
            commonUI.setTitle(titles[i]);
            mUIList.add(commonUI);
        }
    }

    /**
     * initView
     */
    private void initContext() {
        pageBack.setOnClickListener(this);
        pageBack.setImageResource(R.drawable.icon_white_page_back);
        pageTitle.setText("退货详情");
        pageTitle.setTextColor(getResources().getColor(R.color.white));
        delete.setOnClickListener(this);
        edit.setOnClickListener(this);
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
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_back:
                finish();
                break;
            case R.id.btn_delete:           // 删除退货订单
                // toast("删除");
                if ("seller".equals(mFlag)) {          // 批发商端-- 驳回
                    View contentView = LayoutInflater.from(this).inflate(R.layout.dialog_refuse_inventory, null);
                    mPopupWindow = new PopupWindow(contentView,
                            ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT, true);
                    mPopupWindow.setContentView(contentView);
                    View rootView = LayoutInflater.from(this).inflate(R.layout.activity_return_goods_detail, null);
                    mPopupWindow.setAnimationStyle(R.style.IOSAnimStyle);
                    mPopupWindow.showAtLocation(rootView, Gravity.CENTER, 0, 0);
                    backgroundAlpha(0.7f);
                    mPopupWindow.setOnDismissListener(() -> {
                        mPopupWindow.dismiss();
                        backgroundAlpha(1.0f);
                    });
                    ClearEditText refuseReason = contentView.findViewById(R.id.cet_refuse_reason);
                    Button cancel = contentView.findViewById(R.id.btn_cancel);
                    Button confirm = contentView.findViewById(R.id.btn_confirm);
                    cancel.setOnClickListener(v1 -> mPopupWindow.dismiss());
                    confirm.setOnClickListener(v12 -> {
                        if (TextUtils.isEmpty(refuseReason.getText())) {
                            toast("请输入驳回原因");
                            return;
                        }
                        showLoading("");
                        RequestPost.auditingPurchaseOrder(mDetail.getId(), "refuse",
                                refuseReason.getText().toString().trim(), ReGoodsDetailActivity.this);
                    });
                } else {
                    showLoading("");
                    RequestPost.delRefundOrder(mDetail.getId(), this);
                }
                break;
            case R.id.btn_edit:
                if ("seller".equals(mFlag)) {            // 批发商端 --- 通过审核
                    showLoading("");
                    RequestPost.auditingPurchaseOrder(mDetail.getId(), "pass","", ReGoodsDetailActivity.this);
                } else {                // 跳转到退货申请
                    Intent intent = new Intent(this, ReGoodsApplyActivity.class);
                    intent.putExtra("goodsId", mDetail.getBuyCarMxId());
                    startActivity(intent);
                }
                break;
        }
    }

    public void backgroundAlpha(float bgAlpha)  //阴影改变
    {
        WindowManager.LayoutParams lp = getWindow().getAttributes();
        lp.alpha = bgAlpha; //0.0-1.0
        getWindow().setAttributes(lp);
    }

    @SuppressLint("HandlerLeak")
    private Handler mHandler = new Handler() {
        @SuppressLint("SetTextI18n")
        @Override
        public void handleMessage(@NonNull Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case INITIAL_SIZE:
                    Glide.with(ReGoodsDetailActivity.this).load(mDetail.getIco()).into(image);
                    status.setText(mDetail.getWorkFlow());
                    if ("seller".equals(mFlag)) {     // 批发商端
                        if ("待审核".equals(mDetail.getWorkFlow())) {          // 待审核时需通过审核
                            statusHint.setVisibility(View.VISIBLE);
                            delete.setText("驳回");
                            edit.setText("通过");
                        }
                    } else {
                        if (!"".equals(mDetail.getAuditText())) {               // 驳回状态(状态)
                            topText.setText("审核记录");
                            status.setText("已驳回");
                            topText.setTextColor(getResources().getColor(R.color.white));
                            reason.setVisibility(View.VISIBLE);
                            reason.setText("驳回原因：" + mDetail.getAuditText());
                            statusHint.setVisibility(View.VISIBLE);
                        } else {
                            topText.setText("");
                            statusHint.setVisibility(View.GONE);
                            reason.setVisibility(View.GONE);
                        }
                    }
                    name.setText(mDetail.getGoodsName());
                    model.setText(mDetail.getModel());
                    discount.setText(mDetail.getDiscount() + "%");
                    rate.setText(mDetail.getTaxRate() + "%");
                    params.setText(mDetail.getSkuName());

                    StoresListAdapter listAdapter = new StoresListAdapter(ReGoodsDetailActivity.this, mUIList);
                    contentList.setAdapter(listAdapter);
                    break;
            }
        }
    };

    private String[] titles = {"退货数量", "退货金额", "退货原因", "运费", "订单类型", "申请时间", "退货单号"};

    @Override
    public void onHttpFailure(HttpResponse result) {

    }

    @Override
    public void onHttpSucceed(HttpResponse result) {
        Map<String, String> body = JsonParser.parseJSONObject(result.body());
        if (body != null) {
            if ("1".equals(body.get("code"))) {
                if (result.url().contains("wxapi/v1/order.php?type=getRefundOrder")) {
                    mDetail = JsonParser.parseJSONObject(RefundDetailBean.class,
                            JsonParser.parseJSONObject(body.get("data")).get("info"));
                    // 赋值
                    for (int i = 0; i < mUIList.size(); i++) {
                        switch (i) {
                            case 0:         // 退货数量
                                mUIList.get(i).setShowText(mDetail.getRefundNum());
                                break;
                            case 1:         // 退货金额
                                mUIList.get(i).setShowText(mDetail.getRefundMoney());
                                break;
                            case 2:         // 退货原因
                                mUIList.get(i).setShowText(mDetail.getText());
                                break;
                            case 3:         // 运费
                                mUIList.get(i).setShowText(mDetail.getFreightMoney());
                                break;
                            case 4:         // 订单类型
                                mUIList.get(i).setShowText(mDetail.getClassify());
                                break;
                            case 5:         // 申请时间
                                mUIList.get(i).setShowText(mDetail.getTime());
                                break;
                            case 6:         // 退货单号
                                mUIList.get(i).setShowText(mDetail.getId());
                                break;
                        }
                    }
                    Message msg = new Message();
                    msg.what = INITIAL_SIZE;
                    mHandler.sendMessage(msg);
                }

                // 删除退货订单
                if (result.url().contains("wxapi/v1/order.php?type=delRefundOrder")) {
                    toast(body.get("warn"));
                    postDelayed(this::finish, 500);
                }

                // 批发商采购单退货审批
                if (result.url().contains("wxapi/v1/order.php?type=auditingPurchaseOrder")) {
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
