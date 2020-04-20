package com.rainwood.eurobusiness.ui.activity;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.DialogInterface;
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

import com.rainwood.eurobusiness.R;
import com.rainwood.eurobusiness.base.BaseActivity;
import com.rainwood.eurobusiness.domain.CommonUIBean;
import com.rainwood.eurobusiness.domain.OrderBean;
import com.rainwood.eurobusiness.json.JsonParser;
import com.rainwood.eurobusiness.okhttp.HttpResponse;
import com.rainwood.eurobusiness.okhttp.OnHttpListener;
import com.rainwood.eurobusiness.request.RequestPost;
import com.rainwood.eurobusiness.ui.adapter.StockDetailAdapter;
import com.rainwood.eurobusiness.utils.ListUtils;
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
 * @Date: 2020/2/20
 * @Desc: 盘点详情
 */
public class StockDetailActivity extends BaseActivity implements View.OnClickListener, OnHttpListener {


    private String mInventoryId;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_stock_detail;
    }

    @ViewById(R.id.iv_back)
    private ImageView pageBack;
    @ViewById(R.id.tv_title)
    private TextView pageTitle;
    @ViewById(R.id.rl_action_bar)
    private RelativeLayout actionBar;
    @ViewById(R.id.rl_top_item)
    private RelativeLayout topItem;
    @ViewById(R.id.tv_status)
    private TextView status;
    // content
    @ViewById(R.id.lv_content_list)
    private MeasureListView contentList;
    @ViewById(R.id.ll_audit)
    private LinearLayout audit;
    //
    @ViewById(R.id.btn_rejected)
    private Button rejected;
    @ViewById(R.id.btn_through)
    private Button through;
    private PopupWindow mPopupWindow;
    private List<OrderBean> mList;
    private String[] titles = {"", ""};
    // 商品信息
    private String[] goodsTitle = {"商品分类", "商品名称", "商品型号", "条码", "规格"};
    // 盘点信息
    private String[] stockTitle = {"库存数", "盘点数", "备注", "制单人", "盘点时间"};

    private final int INITIAL_SIZE = 0x101;

    @Override
    protected void initView() {
        initContext();
        String permissionId = getIntent().getStringExtra("permissionId");
        if ("0".equals(permissionId)){      //批发商端
            audit.setVisibility(View.VISIBLE);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        // request
        showLoading("loading");
        mInventoryId = getIntent().getStringExtra("InventoryId");
        if (mInventoryId != null) {
            RequestPost.getInventoryInfo(mInventoryId, this);
        } else {
            toast("异常错误");
            dismissLoading();
        }
    }

    @Override
    protected void initData() {
        super.initData();
        mList = new ArrayList<>();
        for (int i = 0; i < titles.length; i++) {
            OrderBean order = new OrderBean();
            order.setTitle(titles[i]);
            List<CommonUIBean> commonUIList = new ArrayList<>();
            for (int j = 0; j < goodsTitle.length && i == 0; j++) {
                CommonUIBean commonUI = new CommonUIBean();
                commonUI.setTitle(goodsTitle[j]);
                commonUIList.add(commonUI);
            }
            for (int j = 0; j < stockTitle.length && i == 1; j++) {
                CommonUIBean commonUI = new CommonUIBean();
                commonUI.setTitle(stockTitle[j]);
                commonUIList.add(commonUI);
            }
            order.setCommonList(commonUIList);
            mList.add(order);
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_back:
                finish();
                break;
            case R.id.btn_rejected:             // 驳回-- 驳回原因必填
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
                    // TODO: 提交驳货
                    showLoading("");
                    RequestPost.auditInventory(mInventoryId, "refuse", refuseReason.getText().toString().trim(), this);
                });

                break;
            case R.id.btn_through:              // 通过
                // TODO:
                showLoading("");
                RequestPost.auditInventory(mInventoryId, "pass", "", this);
                break;
        }
    }

    public void backgroundAlpha(float bgAlpha)  //阴影改变
    {
        WindowManager.LayoutParams lp = getWindow().getAttributes();
        lp.alpha = bgAlpha; //0.0-1.0
        getWindow().setAttributes(lp);
    }

    /**
     * 初始化 Context
     */
    private void initContext() {
        pageBack.setOnClickListener(this);
        pageBack.setImageResource(R.drawable.icon_white_page_back);
        pageTitle.setText("盘点详情");
        rejected.setOnClickListener(this);
        through.setOnClickListener(this);
        pageTitle.setTextColor(getResources().getColor(R.color.white));
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

    @SuppressLint("HandlerLeak")
    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(@NonNull Message msg) {
            switch (msg.what) {
                case INITIAL_SIZE:
                    StockDetailAdapter detailAdapter = new StockDetailAdapter(StockDetailActivity.this, mList);
                    contentList.setAdapter(detailAdapter);
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
                // 获取盘点记录详情
                if (result.url().contains("wxapi/v1/stock.php?type=getInventoryInfo")) {
                    Map<String, String> inventoryInfo = JsonParser.parseJSONObject(JsonParser.parseJSONObject(body.get("data")).get("inventoryInfo"));
                    status.setText(inventoryInfo.get("workFlow"));
                    if (!"已完成".equals(inventoryInfo.get("workFlow"))) {
                        rejected.setVisibility(View.VISIBLE);
                        through.setVisibility(View.VISIBLE);
                    }
                    for (int i = 0; i < ListUtils.getSize(mList); i++) {
                        switch (i) {
                            case 0:
                                setValues(inventoryInfo, i, "goodsType", "goodsName", "model", "barCode", "goodsSkuName");
                                break;
                            case 1:
                                setValues(inventoryInfo, i, "stock", "num", "text", "adName", "time");
                                break;
                        }
                    }

                    Message msg = new Message();
                    msg.what = INITIAL_SIZE;
                    mHandler.sendMessage(msg);
                }

                // 审批盘点记录
                if (result.url().contains("wxapi/v1/stock.php?type=auditInventory")) {
                    toast(body.get("warn"));
                    // 返回 finish
                    finish();
                    //  RequestPost.getInventoryInfo(mInventoryId, this);
                }
            } else {
                toast(body.get("warn"));
            }
            dismissLoading();
        }
    }

    /**
     * 赋值
     *
     * @param inventoryInfo
     * @param i
     * @param stock
     * @param num
     * @param text
     * @param adName
     * @param goodsType
     */
    private void setValues(Map<String, String> inventoryInfo, int i, String stock, String num, String text, String adName, String goodsType) {
        for (int j = 0; j < ListUtils.getSize(mList.get(i).getCommonList()); j++) {
            switch (j) {
                case 0:
                    mList.get(i).getCommonList().get(j).setShowText(inventoryInfo.get(stock));
                    break;
                case 1:
                    mList.get(i).getCommonList().get(j).setShowText(inventoryInfo.get(num));
                    break;
                case 2:
                    mList.get(i).getCommonList().get(j).setShowText(inventoryInfo.get(text));
                    break;
                case 3:
                    mList.get(i).getCommonList().get(j).setShowText(inventoryInfo.get(adName));
                    break;
                case 4:
                    mList.get(i).getCommonList().get(j).setShowText(inventoryInfo.get(goodsType));
                    break;
            }
        }
    }
}
