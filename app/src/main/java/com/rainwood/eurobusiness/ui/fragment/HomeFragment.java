package com.rainwood.eurobusiness.ui.fragment;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Color;
import android.view.View;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.rainwood.eurobusiness.R;
import com.rainwood.eurobusiness.base.BaseFragment;
import com.rainwood.eurobusiness.common.Contants;
import com.rainwood.eurobusiness.domain.ItemGridBean;
import com.rainwood.eurobusiness.domain.ItemIndexListViewBean;
import com.rainwood.eurobusiness.ui.activity.CustomManagerActivity;
import com.rainwood.eurobusiness.ui.activity.InventoryActivity;
import com.rainwood.eurobusiness.ui.activity.NewShopActivity;
import com.rainwood.eurobusiness.ui.activity.OrderManagerActivity;
import com.rainwood.eurobusiness.ui.activity.OutInBoundActivity;
import com.rainwood.eurobusiness.ui.activity.PurchaseActivity;
import com.rainwood.eurobusiness.ui.activity.ReturnGoodsActivity;
import com.rainwood.eurobusiness.ui.activity.SaleGoodsManagerActivity;
import com.rainwood.eurobusiness.ui.activity.SaleStaticsActivity;
import com.rainwood.eurobusiness.ui.activity.SalesLeaderActivity;
import com.rainwood.eurobusiness.ui.activity.ShopManagementActivity;
import com.rainwood.eurobusiness.ui.activity.StockActivity;
import com.rainwood.eurobusiness.ui.activity.UnPaymentActivity;
import com.rainwood.eurobusiness.ui.activity.WarnRepertoryActivity;
import com.rainwood.eurobusiness.ui.adapter.ItemListViewAdapter;
import com.rainwood.eurobusiness.utils.ListUtils;
import com.rainwood.tools.statusbar.StatusBarUtil;
import com.rainwood.tools.widget.MeasureListView;
import com.stx.xhb.xbanner.XBanner;
import com.stx.xhb.xbanner.transformers.Transformer;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * @Author: a797s
 * @Date: 2019/12/4 15:28
 * @Desc: 门店首页碎片
 */
public class HomeFragment extends BaseFragment {

    @Override
    protected int initLayout() {
        return R.layout.fragment_index;
    }

    private XBanner xBanner;

    @SuppressLint("ClickableViewAccessibility")
    @Override
    protected void initView(View view) {
        // XBanner
        xBanner = view.findViewById(R.id.xb_top_img);

        if (Contants.userType == 1) {               // 门店
            xBanner.setVisibility(View.GONE);
            // 设置状态栏背景
            StatusBarUtil.setStatusBarDarkTheme(this.getActivity(), true);
            StatusBarUtil.setStatusBarColor(this.getActivity(), Color.parseColor("#FFFFFF"));
            // 初始化数据
            ItemListViewAdapter adapter = new ItemListViewAdapter(getActivity(), mList);
            final MeasureListView mListView = view.findViewById(R.id.lv_list_index);
            mListView.setAdapter(adapter);
            MeasureListView.getListViewSelfHeight(mListView);
            adapter.notifyDataSetChanged();
            // Item点击事件
            adapter.setItemTouchClick((parent, parentPosition, childPosition) -> {
                switch (parent.get(parentPosition).getGridViewList().get(childPosition).getItemName()) {
                    case "新建商品":
                        Contants.CHOOSE_MODEL_SIZE = 0;
                        startActivity(NewShopActivity.class);
                        break;
                    case "商品管理":
                        Contants.CHOOSE_MODEL_SIZE = 1;
                        startActivity(ShopManagementActivity.class);
                        break;
                    case "采购记录":
                        Contants.CHOOSE_MODEL_SIZE = 2;
                        startActivity(PurchaseActivity.class);
                        break;
                    case "库存商品":
                        Contants.CHOOSE_MODEL_SIZE = 3;
                        startActivity(InventoryActivity.class);
                        break;
                    case "订单管理":
                        Contants.CHOOSE_MODEL_SIZE = 4;
                        startActivity(OrderManagerActivity.class);
                        break;
                    case "出库记录":
                        Contants.CHOOSE_MODEL_SIZE = 5;
                        startActivity(OutInBoundActivity.class);
                        break;
                    case "入库记录":
                        Contants.CHOOSE_MODEL_SIZE = 6;
                        startActivity(OutInBoundActivity.class);
                        break;
                    case "盘点记录":
                        Contants.CHOOSE_MODEL_SIZE = 7;
                        startActivity(StockActivity.class);
                        break;
                    case "预警库存":
                        Contants.CHOOSE_MODEL_SIZE = 8;
                        startActivity(WarnRepertoryActivity.class);
                        break;
                    case "退货管理":
                        Contants.CHOOSE_MODEL_SIZE = 9;
                        startActivity(ReturnGoodsActivity.class);
                        break;
                    case "客户管理":
                        Contants.CHOOSE_MODEL_SIZE = 10;
                        startActivity(CustomManagerActivity.class);
                        break;
                    case "销售统计":
                        Contants.CHOOSE_MODEL_SIZE = 11;
                        startActivity(SaleStaticsActivity.class);
                        break;
                    case "销售排行榜":
                        Contants.CHOOSE_MODEL_SIZE = 12;
                        startActivity(SalesLeaderActivity.class);
                        break;
                    default:
                        break;
                }
            });
        }
        // 供应商端
        if (Contants.userType == 0) {                   // 供应商端
            StatusBarUtil.setCommonUI(getActivity(), false);
            StatusBarUtil.setStatusBarColor(getActivity(), R.color.textColor);
            xBanner.setVisibility(View.VISIBLE);
            setXBanner();
            ItemListViewAdapter adapter = new ItemListViewAdapter(getActivity(), mList);
            final MeasureListView mListView = view.findViewById(R.id.lv_list_index);
            mListView.setAdapter(adapter);
            adapter.setItemTouchClick((parent, parentPosition, childPosition) -> {
                switch (parent.get(parentPosition).getGridViewList().get(childPosition).getItemName()) {
                    case "新建商品":
                        Contants.CHOOSE_MODEL_SIZE = 101;
                        startActivity(NewShopActivity.class);
                        break;
                    case "供应商管理":
                        Contants.CHOOSE_MODEL_SIZE = 102;
                        startActivity(ShopManagementActivity.class);
                        break;
                    case "门店管理":
                        Contants.CHOOSE_MODEL_SIZE = 103;
                        startActivity(ShopManagementActivity.class);
                        break;
                    case "商品管理":
                        Contants.CHOOSE_MODEL_SIZE = 104;
                        startActivity(SaleGoodsManagerActivity.class);
                        break;
                    case "库存商品":
                        Contants.CHOOSE_MODEL_SIZE = 105;
                        startActivity(InventoryActivity.class);
                        break;
                    case "订单管理":
                        Contants.CHOOSE_MODEL_SIZE = 106;
                        startActivity(OrderManagerActivity.class);
                        break;
                    case "客户管理":
                        Contants.CHOOSE_MODEL_SIZE = 107;
                        startActivity(CustomManagerActivity.class);
                        break;
                    case "退货管理":
                        Contants.CHOOSE_MODEL_SIZE = 108;
                        startActivity(ReturnGoodsActivity.class);
                        break;
                    case "采购记录":
                        Contants.CHOOSE_MODEL_SIZE = 109;
                        startActivity(PurchaseActivity.class);
                        break;
                    case "出库记录":
                        Contants.CHOOSE_MODEL_SIZE = 110;
                        startActivity(OutInBoundActivity.class);
                        break;
                    case "入库记录":
                        Contants.CHOOSE_MODEL_SIZE = 111;
                        startActivity(OutInBoundActivity.class);
                        break;
                    case "盘点记录":
                        Contants.CHOOSE_MODEL_SIZE = 112;
                        startActivity(StockActivity.class);
                        break;
                    case "销售统计":
                        Contants.CHOOSE_MODEL_SIZE = 113;
                        startActivity(SaleStaticsActivity.class);
                        break;
                    case "销售排行榜":
                        Contants.CHOOSE_MODEL_SIZE = 114;
                        startActivity(SalesLeaderActivity.class);
                        break;
                    case "未付款项":
                        Contants.CHOOSE_MODEL_SIZE = 115;
                        startActivity(UnPaymentActivity.class);
                        break;
                    case "未收款项":
                        Contants.CHOOSE_MODEL_SIZE = 116;
                        startActivity(UnPaymentActivity.class);
                        break;
                }
            });
        }
    }

    @Override
    protected void initData(Context mContext) {
        if (Contants.userType == 1) {           //门店
            ItemGridBean itemGrid;
            // 初始化管理
            final List<ItemGridBean> managerList = new ArrayList<>();
            for (int i = 0; i < ListUtils.getSize(Arrays.asList(managerText)); i++) {
                itemGrid = new ItemGridBean();
                itemGrid.setItemName(managerText[i]);
                itemGrid.setImgId(managerImg[i]);

                managerList.add(itemGrid);
            }
            // 初始化记录
            final List<ItemGridBean> recordList = new ArrayList<>();
            for (int i = 0; i < ListUtils.getSize(Arrays.asList(recordText)); i++) {
                itemGrid = new ItemGridBean();
                itemGrid.setItemName(recordText[i]);
                itemGrid.setImgId(recordImg[i]);

                recordList.add(itemGrid);
            }

            // 模块化
            mList = new ArrayList<>();      // 初始化数据
            ItemIndexListViewBean indexListViewBean;       // 模块对象
            for (int i = 0; i < ListUtils.getSize(Arrays.asList(moduleTitle)); i++) {
                indexListViewBean = new ItemIndexListViewBean();
                indexListViewBean.setTitle(moduleTitle[i]);
                if (i == 0) {
                    indexListViewBean.setGridViewList(managerList);
                }
                if (i == 1) {
                    indexListViewBean.setGridViewList(recordList);
                }
                mList.add(indexListViewBean);
            }
        }

        if (Contants.userType == 0) {                    // 批发商
            mList = new ArrayList<>();
            for (int i = 0; i < salerTitles.length; i++) {
                ItemIndexListViewBean itemIndex = new ItemIndexListViewBean();
                itemIndex.setTitle(salerTitles[i]);
                List<ItemGridBean> gridBeanList = new ArrayList<>();
                for (int j = 0; j < managerTitles.length && i == 0; j++) {          // 管理模块
                    ItemGridBean itemGrid = new ItemGridBean();
                    itemGrid.setItemName(managerTitles[j]);
                    itemGrid.setImgId(managerImgs[j]);
                    gridBeanList.add(itemGrid);
                }
                for (int j = 0; j < recordTitles.length && i == 1; j++) {                     // 记录模块
                    ItemGridBean itemGrid = new ItemGridBean();
                    itemGrid.setItemName(recordTitles[j]);
                    itemGrid.setImgId(recordImgs[j]);
                    gridBeanList.add(itemGrid);
                }
                for (int j = 0; j < dataTitles.length && i == 2; j++) {                     // 记录模块
                    ItemGridBean itemGrid = new ItemGridBean();
                    itemGrid.setItemName(dataTitles[j]);
                    itemGrid.setImgId(dataImg[j]);
                    gridBeanList.add(itemGrid);
                }
                itemIndex.setGridViewList(gridBeanList);
                mList.add(itemIndex);
            }
        }
    }

    /**
     * XBanner
     */
    private void setXBanner() {
        // 初始化XBanner中展示的数据
        final ArrayList<Integer> images = new ArrayList<>();
        for (int i = 0; i < 5; i++) {
            images.add(R.drawable.icon_loadding_fail);
        }
        if (xBanner != null) {
            xBanner.removeAllViews();
        }
        // 为XBanner绑定数据
        xBanner.setData(images, null);
        // XBanner适配数据
        xBanner.setmAdapter((banner, view, position) -> Glide.with(getActivity()).load(images.get(position)).into((ImageView) view));
        // 设置XBanner的页面切换特效
        xBanner.setPageTransformer(Transformer.Default);
        // 设置XBanner页面切换的时间，即动画时长
        xBanner.setPageChangeDuration(1000);
    }

    @Override
    public void onResume() {
        super.onResume();
        xBanner.startAutoPlay();
    }

    @Override
    public void onStop() {
        super.onStop();
        xBanner.stopAutoPlay();
    }

    // 初始化数据
    private List<ItemIndexListViewBean> mList;
    /*** 门店端 数据初始化*/
    // 模块
    private static final String[] moduleTitle = {"管理", "数据统计"};
    // 管理
    private static final String[] managerText = {"新建商品", "商品管理", "采购记录",
            "库存商品", "订单管理", "出库记录", "入库记录", "盘点记录", "预警库存", "退货管理", "客户管理"};
    private static final int[] managerImg = {
            R.drawable.ic_icon_new_found, R.drawable.ic_icon_commodity, R.drawable.ic_icon_purchase,
            R.drawable.ic_icon_stock, R.drawable.ic_icon_order, R.drawable.ic_icon_out,
            R.drawable.ic_icon_in, R.drawable.ic_icon_inventory, R.drawable.ic_icon_alert,
            R.drawable.ic_icon_refund, R.drawable.ic_icon_customer};
    // 数据统计
    private static final String[] recordText = {"销售统计", "销售排行榜"};
    private static final int[] recordImg = {R.drawable.ic_icon_sales_statistics, R.drawable.ic_icon_ranking_list};

    /**
     * 批发商端
     */
    private String[] salerTitles = {"管理", "记录", "数据统计"};
    private String[] managerTitles = {"新建商品", "供应商管理", "门店管理", "商品管理", "库存商品",
            "订单管理", "客户管理", "退货管理"};
    private final int[] managerImgs = {
            R.drawable.ic_icon_new_found, R.drawable.ic_icon_commodity, R.drawable.ic_icon_purchase,
            R.drawable.ic_icon_stock, R.drawable.ic_icon_order, R.drawable.ic_icon_out,
            R.drawable.ic_icon_in, R.drawable.ic_icon_inventory
    };
    private String[] recordTitles = {"采购记录", "出库记录", "入库记录", "盘点记录"};
    private final int[] recordImgs = {
            R.drawable.ic_icon_purchase, R.drawable.ic_icon_out, R.drawable.ic_icon_in, R.drawable.ic_icon_stock
    };
    private String[] dataTitles = {"销售统计", "销售排行榜", "未付款项", "未收款项"};
    private final int[] dataImg = {
            R.drawable.ic_icon_purchase, R.drawable.ic_icon_out, R.drawable.ic_icon_in, R.drawable.ic_icon_stock};
}
