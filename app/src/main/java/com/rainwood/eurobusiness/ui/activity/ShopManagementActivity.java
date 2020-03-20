package com.rainwood.eurobusiness.ui.activity;

import android.annotation.SuppressLint;
import android.view.Gravity;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.core.widget.NestedScrollView;
import androidx.core.widget.PopupWindowCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.rainwood.eurobusiness.R;
import com.rainwood.eurobusiness.base.BaseActivity;
import com.rainwood.eurobusiness.common.Contants;
import com.rainwood.eurobusiness.domain.PressBean;
import com.rainwood.eurobusiness.domain.ShopBean;
import com.rainwood.eurobusiness.domain.StoresBean;
import com.rainwood.eurobusiness.domain.SuppierBean;
import com.rainwood.eurobusiness.ui.adapter.ItemSupplierAdapter;
import com.rainwood.eurobusiness.ui.adapter.ShopListContentListAdapter;
import com.rainwood.eurobusiness.ui.adapter.ShopMangerRecyclerAdapter;
import com.rainwood.eurobusiness.ui.adapter.StoresManagerAdapter;
import com.rainwood.eurobusiness.ui.widget.ScreeningPopWindow;
import com.rainwood.tools.view.ClearEditText;
import com.rainwood.tools.viewinject.ViewById;
import com.rainwood.tools.widget.MeasureListView;

import java.util.ArrayList;
import java.util.List;

/**
 * @Author: a797s
 * @Date: 2020/2/8
 * @Desc: 商品管理
 */
public class ShopManagementActivity extends BaseActivity implements View.OnClickListener {

    @Override
    protected int getLayoutId() {
        return R.layout.activity_shop_manager;
    }

    @ViewById(R.id.iv_new_shop)
    private ImageView newShop;
    @ViewById(R.id.iv_back)
    private ImageView pageBack;
    @ViewById(R.id.ll_search)
    private LinearLayout searchView;
    @ViewById(R.id.rl_top_list)
    private RecyclerView topList;
    @ViewById(R.id.iv_screen)
    private ImageView mScreening;
    @ViewById(R.id.tv_line)
    private TextView mLine;
    @ViewById(R.id.et_search)
    private ClearEditText etSearch;
    //content
    @ViewById(R.id.sl_content)
    private NestedScrollView contentScroll;
    @ViewById(R.id.lv_content)
    private MeasureListView contentList;
    @ViewById(R.id.tv_already_bottom)
    private TextView bottomText;
    // 批发商
    @ViewById(R.id.tv_payed_money)
    private TextView payMoney;
    @ViewById(R.id.ll_pay_money)
    private LinearLayout payMoneys;
    @ViewById(R.id.ll_top_info)
    private LinearLayout topInfo;

    @SuppressLint("SetTextI18n")
    @Override
    protected void initView() {
        newShop.setOnClickListener(this);
        pageBack.setOnClickListener(this);
        searchView.setOnClickListener(this);
        mScreening.setOnClickListener(this);
        /**
         *  门店端 - 商品管理
         */
        if (Contants.CHOOSE_MODEL_SIZE == 1) {
            payMoneys.setVisibility(View.GONE);
            LinearLayoutManager layoutManager = new LinearLayoutManager(this);
            layoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
            topList.setLayoutManager(layoutManager);
            ShopMangerRecyclerAdapter topAdapter = new ShopMangerRecyclerAdapter(mTopList);
            topList.setAdapter(topAdapter);
            // 选择查看不同状态的商品
            topAdapter.setiOnTopChoose(position -> {
                // 全部置为false
                for (PressBean pressBean : mTopList) {
                    pressBean.setChoose(false);
                }
                mTopList.get(position).setChoose(true);
                // 加载不同状态的商品
                ShopListContentListAdapter contentAdapter = new ShopListContentListAdapter(this, mContentList);
                contentList.setAdapter(contentAdapter);
            });
            // content -- 默认显示查看全部商品
            ShopListContentListAdapter contentAdapter = new ShopListContentListAdapter(this, mContentList);
            contentList.setAdapter(contentAdapter);
            // 商品列表的点击事件    --- 上下/架、编辑、查看大图
            contentAdapter.setiOnShopItemClick(new ShopListContentListAdapter.IOnShopItemClick() {
                @Override
                public void onShelves(int position) {
                    toast("上下架");
                }

                @Override
                public void onEdit(int position) {
                    toast("编辑");
                }

                @Override
                public void onBigPic(int position) {
                    toast("查看大图");
                }

                @Override
                public void onClickDetail(int position) {
                    //toast("查看详情");
                    openActivity(GoodsDetailActivity.class);
                }
            });

            // 滑动监听
            contentScroll.setOnScrollChangeListener((NestedScrollView.OnScrollChangeListener) (v, scrollX, scrollY, oldScrollX, oldScrollY) -> {
                if (contentScroll.getScrollY() == 0) {
                    //顶部 // toast("到了顶部");
                }

                View contentView = contentScroll.getChildAt(0);
                if (contentView != null && contentView.getMeasuredHeight() == (contentScroll.getScrollY() + contentScroll.getHeight())) {
                    //底部
                    toast("您已经到底了");
                    bottomText.setVisibility(View.VISIBLE);
                } else {
                    bottomText.setVisibility(View.GONE);
                }
            });
        }

        /**
         * 供应商管理
         */
        if (Contants.CHOOSE_MODEL_SIZE == 102) {
            payMoneys.setVisibility(View.VISIBLE);
            topInfo.setVisibility(View.GONE);
            mLine.setVisibility(View.GONE);
            payMoney.setText("53562.00€");
            etSearch.setHint("搜索供应商/首字母");

            ItemSupplierAdapter supplierAdapter = new ItemSupplierAdapter(this, suppierList);
            contentList.setAdapter(supplierAdapter);
            supplierAdapter.setOnClickItem(position -> toast("查看商品详情：" + position));
        }

        /**
         * 门店管理
         */
        if (Contants.CHOOSE_MODEL_SIZE == 103) {
            payMoneys.setVisibility(View.GONE);
            topInfo.setVisibility(View.GONE);
            mLine.setVisibility(View.GONE);

            StoresManagerAdapter storesAdapter = new StoresManagerAdapter(this, storesList);
            contentList.setAdapter(storesAdapter);
            storesAdapter.setOnClickPoint(new StoresManagerAdapter.OnClickPoint() {
                @Override
                public void onClickPoint(int position) {
                    if (storesList.get(position).isDelete()) {
                        storesList.get(position).setDelete(false);
                    } else {
                        storesList.get(position).setDelete(true);
                    }
                }

                @Override
                public void onCliCkDetail(int position) {
                    toast("查看详情");
                }
            });
        }
    }

    @Override
    protected void initData() {
        // 商品管理
        if (Contants.CHOOSE_MODEL_SIZE == 1) {
            mTopList = new ArrayList<>();
            for (String topTitle : topTitles) {
                PressBean press = new PressBean();
                press.setTitle(topTitle);
                if (topTitle.equals("全部")) {
                    press.setChoose(true);
                }
                mTopList.add(press);
            }

            // 模拟内容
            mContentList = new ArrayList<>();
            for (int i = 0; i < 10; i++) {
                ShopBean shop = new ShopBean();
                shop.setImgPath(R.drawable.icon_loadding_fail);
                if (i % 2 == 0) {
                    shop.setStatus("在售中");
                } else {
                    shop.setStatus("已下架");
                }
                if (i == 2) {
                    shop.setStatus("");
                }
                shop.setName("西装外套式系缀扣连衣裙");
                shop.setWholesalePrice("104.00€");
                shop.setRetailPrice("120.00€");
                shop.setSource("由批发商创建");
                mContentList.add(shop);
            }
        }

        // 供应商管理
        if (Contants.CHOOSE_MODEL_SIZE == 102) {
            suppierList = new ArrayList<>();
            for (int i = 0; i < 4; i++) {
                SuppierBean suppier = new SuppierBean();
                suppier.setId("00" + (i + 1));
                suppier.setName("莱尔维思莱尔维思服饰有限公司");
                suppier.setAllMoney("53562.00€");
                suppierList.add(suppier);
            }
        }
        // 门店管理
        if (Contants.CHOOSE_MODEL_SIZE == 103) {
            storesList = new ArrayList<>();
            for (int i = 0; i < 3; i++) {
                StoresBean stores = new StoresBean();
                stores.setName("来福士门店");
                stores.setId("00" + (i + 1));
                storesList.add(stores);
            }
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_back:
                finish();
                break;
            case R.id.iv_new_shop:          // 新建商品
                if (Contants.CHOOSE_MODEL_SIZE == 1) {
                    openActivity(NewShopActivity.class);
                }
                if (Contants.CHOOSE_MODEL_SIZE == 102) {         // 新建供应商
                    openActivity(NewSupplierActivity.class);
                }
                if (Contants.CHOOSE_MODEL_SIZE == 103) {
                    openActivity(NewSupplierActivity.class);
                }
                break;
            case R.id.ll_search:            // 搜索框

                break;
            case R.id.iv_screen:            // 筛选
                if (Contants.CHOOSE_MODEL_SIZE == 1) {                       // 门店端的筛选
                    mScreening.setImageResource(R.drawable.ic_screening_red);
                    ScreeningPopWindow screeningPop = new ScreeningPopWindow(this);
                    PopupWindowCompat.showAsDropDown(screeningPop, mLine, 0, 0, Gravity.START);
                    screeningPop.setAnimationStyle(R.anim.dialog_top_in);
                    contentScroll.setForeground(getResources().getDrawable(R.drawable.selector_foregorund));
                    contentScroll.setTransitionAlpha(0.1f);
                    // PopWindow 弹窗取消监听
                    screeningPop.setOnDismissListener(() -> {
                        screeningPop.setAnimationStyle(R.anim.dialog_top_out);
                        // contentScroll.setBackgroundColor(getResources().getColor(R.color.white));
                        contentScroll.setForeground(getResources().getDrawable(R.drawable.selector_transparent));
                        contentScroll.setTransitionAlpha(1.0f);
                        mScreening.setImageResource(R.drawable.ic_screening);
                    });
                    // 选择消息回调
                    screeningPop.setOnClickChecked(this::toast);
                }
                break;
        }
    }

    private List<PressBean> mTopList;
    private String[] topTitles = {"全部", "在售中", "已下架", "草稿"};
    /*
    模拟数据
     */
    private List<ShopBean> mContentList;
    // 供应商管理
    private List<SuppierBean> suppierList;
    // 门店管理
    private List<StoresBean> storesList;
}
