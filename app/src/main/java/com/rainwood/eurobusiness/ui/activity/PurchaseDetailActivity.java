package com.rainwood.eurobusiness.ui.activity;

import android.annotation.SuppressLint;
import android.os.Handler;
import android.os.Message;
import android.text.Html;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.rainwood.eurobusiness.R;
import com.rainwood.eurobusiness.base.BaseActivity;
import com.rainwood.eurobusiness.domain.CommonUIBean;
import com.rainwood.eurobusiness.domain.PurchaseGoodsBean;
import com.rainwood.eurobusiness.domain.PurchaseTypeBean;
import com.rainwood.eurobusiness.ui.adapter.PurchaseContentAdapter;
import com.rainwood.eurobusiness.ui.adapter.PurchaseLabelsAdapter;
import com.rainwood.eurobusiness.ui.adapter.PurchaseTypeAdapter;
import com.rainwood.tools.common.FontDisplayUtil;
import com.rainwood.tools.statusbar.StatusBarUtil;
import com.rainwood.tools.viewinject.ViewById;
import com.rainwood.tools.widget.MeasureListView;

import java.util.ArrayList;
import java.util.List;

/**
 * @Author: a797s
 * @Date: 2020/2/18
 * @Desc: 采购单详情 --- 门店端
 */
public class PurchaseDetailActivity extends BaseActivity implements View.OnClickListener {

    @Override
    protected int getLayoutId() {
        return R.layout.activity_purchase_detail;
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
    @ViewById(R.id.lv_purchase_list)
    private MeasureListView purchaseList;
    // bottom label
    @ViewById(R.id.lv_purchase_label)
    private MeasureListView purchaseLabel;
    // btn
    @ViewById(R.id.btn_bulk_storage)
    private Button bulkStorage;
    @ViewById(R.id.ll_selected_bulk)
    private LinearLayout selected;
    @ViewById(R.id.btn_cancel)
    private Button cancel;
    @ViewById(R.id.btn_selected_bulk)
    private Button selectedBulk;
    // mHandler
    private final int CHECKED_SIZE = 0x1124;
    private int selectedCount = 0;

    @Override
    protected void initView() {
        // 初始化本Activity
        initContext();
        status.setText("待入库");
        // content
        Message msg = new Message();
        msg.what = CHECKED_SIZE;
        mHandler.sendMessage(msg);
        selectedBulk.setText("批量入库（已选0件）");
        // label
        PurchaseLabelsAdapter labelsAdapter = new PurchaseLabelsAdapter(this, labeList);
        purchaseLabel.setAdapter(labelsAdapter);
    }

    @Override
    protected void initData() {
        super.initData();
        mGoodsList = new ArrayList<>();
        for (int i = 0; i < goodsName.length; i++) {
            PurchaseGoodsBean purchaseGoods = new PurchaseGoodsBean();
            List<PurchaseTypeBean> typeList = new ArrayList<>();
            if (i == 0) {
                purchaseGoods.setName(goodsName[i]);
                purchaseGoods.setDiscount(discount[i]);
                purchaseGoods.setModel(models[i]);
                purchaseGoods.setRate(rate[i]);
                for (int j = 0; j < paramsSize.length; j++) {
                    PurchaseTypeBean type = new PurchaseTypeBean();
                    type.setAllMoney("49000.00€ ");
                    type.setParamSize(paramsSize[j]);
                    type.setImgPath(null);
                    type.setPurchase("500");
                    if (j == 1) {
                        type.setInStorage("300");
                    } else {
                        type.setInStorage("0");
                    }
                    type.setReturnNum("0");
                    type.setPrice("98.00€ ");
                    typeList.add(type);
                }
            }
            if (i == 1) {
                purchaseGoods.setName(goodsName[i]);
                purchaseGoods.setDiscount(discount[i]);
                purchaseGoods.setModel(models[i]);
                purchaseGoods.setRate(rate[i]);
                for (String paramSize : paramSizes) {
                    PurchaseTypeBean type = new PurchaseTypeBean();
                    type.setParamSize(paramSize);
                    type.setImgPath(null);
                    type.setPrice("98.00€ ");
                    type.setPurchase("500");
                    type.setInStorage("300");
                    type.setReturnNum("20");
                    type.setAllMoney("49000.00€ ");
                    typeList.add(type);
                }
            }
            purchaseGoods.setTypeList(typeList);
            mGoodsList.add(purchaseGoods);
        }

        // Label
        labeList = new ArrayList<>();
        for (int i = 0; i < titles.length; i++) {
            CommonUIBean commonUI = new CommonUIBean();
            commonUI.setTitle(titles[i]);
            commonUI.setShowText(showTexts[i]);
            labeList.add(commonUI);
        }
    }

    /**
     * 初始化 Context
     */
    private void initContext() {
        pageBack.setOnClickListener(this);
        pageBack.setImageResource(R.drawable.icon_white_page_back);
        pageTitle.setText("采购单详情");
        pageTitle.setTextColor(getResources().getColor(R.color.white));
        bulkStorage.setOnClickListener(this);
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
        // Button
        cancel.setOnClickListener(this);
        selectedBulk.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_back:
                finish();
                break;
            case R.id.btn_bulk_storage:
                selected.setVisibility(View.VISIBLE);
                bulkStorage.setVisibility(View.GONE);
                setSelector(true);
                break;
            case R.id.btn_cancel:
                selected.setVisibility(View.GONE);
                bulkStorage.setVisibility(View.VISIBLE);
                setSelector(false);
                break;
            case R.id.btn_selected_bulk:
                toast("批量入库选中：" + selectedCount + "件");
                break;
        }
    }

    /**
     * 设置是否批量
     *
     * @param b
     */
    private void setSelector(boolean b) {
        for (PurchaseGoodsBean bean : mGoodsList) {
            for (PurchaseTypeBean typeBean : bean.getTypeList()) {
                typeBean.setBulkSelect(b);
            }
        }
        Message msg = new Message();
        msg.what = CHECKED_SIZE;
        mHandler.sendMessage(msg);
    }

    @SuppressLint("HandlerLeak")
    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(@NonNull Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case CHECKED_SIZE:
                    PurchaseContentAdapter contentAdapter = new PurchaseContentAdapter(PurchaseDetailActivity.this, mGoodsList);
                    purchaseList.setAdapter(contentAdapter);
                    contentAdapter.setOnClickItem(new PurchaseTypeAdapter.OnClickItem() {
                        @Override
                        public void onClickReturnGoods(int parentPos, int position) {
                            toast("退货：" + parentPos + " --- " + position);
                        }

                        @Override
                        public void onClickInStorage(int parentPos, int position) {
                            openActivity(InStorageActivity.class);
                        }

                        @Override
                        public void onClickChecked(int parentPos, int position) {
                            if (mGoodsList.get(parentPos).getTypeList().get(position).isSelected()) {
                                mGoodsList.get(parentPos).getTypeList().get(position).setSelected(false);
                                selectedCount -= Integer.parseInt(mGoodsList.get(parentPos).getTypeList().get(position).getPurchase());
                            } else {
                                mGoodsList.get(parentPos).getTypeList().get(position).setSelected(true);
                                selectedCount += Integer.parseInt(mGoodsList.get(parentPos).getTypeList().get(position).getPurchase());
                            }
                            // 局部UI刷新
                            selectedBulk.setText(Html.fromHtml("<font color=" + getResources().getColor(R.color.white) + " size='14px'>批量入库</font>"
                                    + "<font color=" + getResources().getColor(R.color.white) + " size='12px'>（已选" + selectedCount + "件）</font>"));
                            Message msg = new Message();
                            msg.what = CHECKED_SIZE;
                            mHandler.sendMessage(msg);
                        }
                    });
                    break;
            }
        }
    };

    /*
    模拟数据
     */
    private List<PurchaseGoodsBean> mGoodsList;
    private String[] goodsName = {"西装外套式系缀扣连衣裙", "西装外套式系缀扣连衣裙"};
    private String[] models = {"XDF-256165", "XDF-256165"};
    private String[] discount = {"25%折扣", "25%折扣"};
    private String[] rate = {"16%税率", "16%税率"};
    private String[] paramsSize = {"杏色/XL", "杏色/XL"};
    private String[] paramSizes = {"混装"};
    // Label
    private List<CommonUIBean> labeList;
    private String[] titles = {"总计", "采购订单", "下单时间", "订单类型"};
    private String[] showTexts = {"49000.00€ ", "55215002200026202", "2020.01.02 15:04:00", "批发商采购"};
}
