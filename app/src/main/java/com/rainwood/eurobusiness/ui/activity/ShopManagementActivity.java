package com.rainwood.eurobusiness.ui.activity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.widget.NestedScrollView;
import androidx.core.widget.PopupWindowCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.rainwood.eurobusiness.R;
import com.rainwood.eurobusiness.base.BaseActivity;
import com.rainwood.eurobusiness.common.Contants;
import com.rainwood.eurobusiness.domain.GoodsBean;
import com.rainwood.eurobusiness.domain.PressBean;
import com.rainwood.eurobusiness.domain.PressTypeBean;
import com.rainwood.eurobusiness.domain.PressTypeRightBean;
import com.rainwood.eurobusiness.domain.StoresBean;
import com.rainwood.eurobusiness.domain.SuppierBean;
import com.rainwood.eurobusiness.json.JsonParser;
import com.rainwood.eurobusiness.okhttp.HttpResponse;
import com.rainwood.eurobusiness.okhttp.OnHttpListener;
import com.rainwood.eurobusiness.other.BaseDialog;
import com.rainwood.eurobusiness.request.RequestPost;
import com.rainwood.eurobusiness.ui.adapter.ItemSupplierAdapter;
import com.rainwood.eurobusiness.ui.adapter.ShopListContentListAdapter;
import com.rainwood.eurobusiness.ui.adapter.ShopMangerRecyclerAdapter;
import com.rainwood.eurobusiness.ui.adapter.StoresManagerAdapter;
import com.rainwood.eurobusiness.ui.dialog.MessageDialog;
import com.rainwood.eurobusiness.ui.widget.ScreeningPopWindow;
import com.rainwood.tools.view.ClearEditText;
import com.rainwood.tools.viewinject.ViewById;
import com.rainwood.tools.widget.MeasureListView;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * @Author: a797s
 * @Date: 2020/2/8
 * @Desc: 商品管理
 */
public class ShopManagementActivity extends BaseActivity implements View.OnClickListener, OnHttpListener {

    private String mTopType;

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
    @ViewById(R.id.tv_supper_tips)
    private TextView suppperTips;

    private List<PressBean> mTopList;
    private String[] topTitles = {"全部", "在售中", "已下架"};
    private List<GoodsBean> mContentList;
    // 供应商管理
    private List<SuppierBean> suppierList;
    // 门店管理
    private List<StoresBean> storesList;

    private final int INITIAL_SIZE = 0x101;

    @SuppressLint("SetTextI18n")
    @Override
    protected void initView() {
        newShop.setOnClickListener(this);
        pageBack.setOnClickListener(this);
        searchView.setOnClickListener(this);
        mScreening.setOnClickListener(this);

    }

    @Override
    protected void initData() {
        // 商品管理
        if (Contants.CHOOSE_MODEL_SIZE == 1) {
            payMoneys.setVisibility(View.GONE);
            mTopList = new ArrayList<>();
            for (String topTitle : topTitles) {
                PressBean press = new PressBean();
                press.setTitle(topTitle);
                if (topTitle.equals("全部")) {
                    press.setChoose(true);
                }
                mTopList.add(press);
            }
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        // 商品管理
        if (Contants.CHOOSE_MODEL_SIZE == 1) {
            // request -- default query all
            showLoading("loading");
            RequestPost.getGoodsList("", "", "", new ArrayList<>(), this);
        }
        // 供应商管理
        if (Contants.CHOOSE_MODEL_SIZE == 102) {
            // request
            showLoading("");
            RequestPost.getSupplierlist("", this);
        }
        // 门店管理
        if (Contants.CHOOSE_MODEL_SIZE == 103) {
            // request
            showLoading("loading");
            RequestPost.getStorelist(this);
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
                if (Contants.CHOOSE_MODEL_SIZE == 102 || Contants.CHOOSE_MODEL_SIZE == 0x1002) {         // 新建供应商
                    openActivity(NewSupplierActivity.class);
                }

                // 新建门店
                if (Contants.CHOOSE_MODEL_SIZE == 0x1003) {
                    Contants.CHOOSE_MODEL_SIZE = 103;
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
                    // request
                    showLoading("loading");
                    RequestPost.getGoodsType(this);
                }
                break;
        }
    }

    @SuppressLint("HandlerLeak")
    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(@NonNull Message msg) {
            switch (msg.what) {
                case INITIAL_SIZE:
                    /**
                     *  门店端 - 商品管理
                     */
                    if (Contants.CHOOSE_MODEL_SIZE == 1) {
                        LinearLayoutManager layoutManager = new LinearLayoutManager(ShopManagementActivity.this);
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
                            mTopType = "";
                            switch (position) {
                                case 0:
                                    mTopType = "";
                                    break;
                                case 1:
                                    mTopType = "online";
                                    break;
                                case 2:
                                    mTopType = "offline";
                                    break;
                            }
                            // request
                            showLoading("loading");
                            RequestPost.getGoodsList("", mTopType, "", new ArrayList<>(), ShopManagementActivity.this);
                        });
                        // content -- 默认显示查看全部商品
                        ShopListContentListAdapter contentAdapter = new ShopListContentListAdapter(ShopManagementActivity.this, mContentList);
                        contentList.setAdapter(contentAdapter);
                        // 商品列表的点击事件    --- 上下/架、编辑、查看大图
                        contentAdapter.setiOnShopItemClick(new ShopListContentListAdapter.IOnShopItemClick() {
                            @Override
                            public void onShelves(int position) {
                                // request
                                if ("在售中".equals(mContentList.get(position).getState())) {
                                    new MessageDialog.Builder(ShopManagementActivity.this)
                                            .setMessage("是否确认该商品下架？")
                                            .setConfirm(getString(R.string.common_confirm))
                                            .setCancel(getString(R.string.common_cancel))
                                            .setAutoDismiss(false)
                                            .setCanceledOnTouchOutside(false)
                                            .setListener(new MessageDialog.OnListener() {
                                                @Override
                                                public void onConfirm(BaseDialog dialog) {
                                                    dialog.dismiss();
                                                    showLoading("loading");
                                                    RequestPost.onSaleGoods(mContentList.get(position).getGoodsId(), ShopManagementActivity.this);
                                                }

                                                @Override
                                                public void onCancel(BaseDialog dialog) {
                                                    dialog.dismiss();
                                                }
                                            }).show();
                                } else {
                                    showLoading("loading");
                                    RequestPost.onSaleGoods(mContentList.get(position).getGoodsId(), ShopManagementActivity.this);
                                }
                            }

                            @Override
                            public void onEdit(int position) {
                                toast("编辑");
                            }

                            @Override
                            public void onBigPic(int position) {
                                // toast("查看大图");
                            }

                            @Override
                            public void onClickDetail(int position) {
                                //toast("查看详情");
                                Intent intent = new Intent(ShopManagementActivity.this, GoodsDetailActivity.class);
                                intent.putExtra("goodsId", mContentList.get(position).getGoodsId());
                                startActivity(intent);
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
                        etSearch.setHint("搜索供应商/首字母");
                        ItemSupplierAdapter supplierAdapter = new ItemSupplierAdapter(ShopManagementActivity.this, suppierList);
                        contentList.setAdapter(supplierAdapter);
                        supplierAdapter.setOnClickItem(position -> {
                            Contants.CHOOSE_MODEL_SIZE = 0x1001;
                            Intent intent = new Intent(ShopManagementActivity.this, NewSupplierActivity.class);
                            intent.putExtra("supplyId", suppierList.get(position).getId());
                            startActivity(intent);
                        });
                    }

                    /**
                     * 门店管理
                     */
                    if (Contants.CHOOSE_MODEL_SIZE == 103) {
                        payMoneys.setVisibility(View.GONE);
                        topInfo.setVisibility(View.GONE);
                        mLine.setVisibility(View.GONE);

                        StoresManagerAdapter storesAdapter = new StoresManagerAdapter(ShopManagementActivity.this, storesList);
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
                                Contants.CHOOSE_MODEL_SIZE = 0x1003;
                                Intent intent = new Intent(ShopManagementActivity.this, NewSupplierActivity.class);
                                intent.putExtra("storeId", storesList.get(position).getStoreId());
                                startActivity(intent);
                            }
                        });
                    }
                    break;
            }
        }
    };

    @Override
    public void onHttpFailure(HttpResponse result) {

    }

    @Override
    public void onHttpSucceed(HttpResponse result) {
        Log.d(TAG, " --- result ---- " + result);
        Map<String, String> body = JsonParser.parseJSONObject(result.body());
        if (body != null) {
            if ("1".equals(body.get("code"))) {
                // 查询商品列表
                if (result.url().contains("wxapi/v1/goods.php?type=getGoodsList")) {
                    Log.d(TAG, "body ---- " + body.get("data"));
                    mContentList = JsonParser.parseJSONArray(GoodsBean.class, JsonParser.parseJSONObject(body.get("data")).get("goodslist"));
                    Message msg = new Message();
                    msg.what = INITIAL_SIZE;
                    mHandler.sendMessage(msg);
                }
                // 更改商品上下架状态
                if (result.url().contains("wxapi/v1/goods.php?type=onSaleGoods")) {
                    // request
                    //showLoading("loading");
                    RequestPost.getGoodsList("", mTopType, "", new ArrayList<>(), this);
                }
                // 获取门店列表
                if (result.url().contains("wxapi/v1/goods.php?type=getStorelist")) {
                    storesList = JsonParser.parseJSONArray(StoresBean.class, JsonParser.parseJSONObject(body.get("data")).get("storelist"));
                    Message msg = new Message();
                    msg.what = INITIAL_SIZE;
                    mHandler.sendMessage(msg);
                }
                // 查询商品列表
                if (result.url().contains("wxapi/v1/goods.php?type=getNewGoodsType")) {
                    if (Contants.CHOOSE_MODEL_SIZE == 1) {
                        List<PressTypeBean> topTypeList = JsonParser.parseJSONArray(PressTypeBean.class,
                                JsonParser.parseJSONObject(body.get("data")).get("goodsTypeOne"));
                        List<PressTypeRightBean> rightTypeList = JsonParser.parseJSONArray(PressTypeRightBean.class,
                                JsonParser.parseJSONObject(body.get("data")).get("goodsTypeTwo"));

                        ScreeningPopWindow screeningPop = new ScreeningPopWindow(topTypeList, rightTypeList, this);
                        PopupWindowCompat.showAsDropDown(screeningPop, mLine, 0, 0, Gravity.START);
                        screeningPop.setAnimationStyle(R.style.IOSAnimStyle);
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
                        screeningPop.setOnClickChecked(new ScreeningPopWindow.OnClickChecked() {
                            @Override
                            public void onClickChecked(String msg) {
                                // Log.d(TAG, "选中的类型" + msg);
                                toast("重置");
                            }

                            @Override
                            public void getSubTypeList(List<String> rightList) {
                                screeningPop.dismiss();
                                // request
                                showLoading("loading");
                                RequestPost.getGoodsList("", "", "", rightList, ShopManagementActivity.this);
                            }
                        });
                    }
                }
                // 获取右边的分类类型
                if (result.url().contains("wxapi/v1/goods.php?type=getNewGoodsTypeTwo")) {
                    List<PressTypeRightBean> rightList = JsonParser.parseJSONArray(PressTypeRightBean.class,
                            JsonParser.parseJSONObject(body.get("data")).get("goodsTypeTwo"));
                    // 传递到popWindow中
                    new ScreeningPopWindow(rightList, this);
                }
                // 获取供应商列表
                if (result.url().contains("wxapi/v1/supplier.php?type=getSupplierlist")) {
                    suppierList = JsonParser.parseJSONArray(SuppierBean.class, JsonParser.parseJSONObject(body.get("data")).get("supplierlist"));
                    String havePay = JsonParser.parseJSONObject(JsonParser.parseJSONObject(body.get("data")).get("info")).get("havePay");
                    payMoney.setText(havePay);
                    suppperTips.setText("已付金额");
                    for (int i = 0; i < suppierList.size(); i++) {
                        suppierList.get(i).setNum(i < 100 ? i < 10 ? ("00" + (i + 1)) : ("0" + (i + 1)) : "" + (i + 1));
                    }
                    Message msg = new Message();
                    msg.what = INITIAL_SIZE;
                    mHandler.sendMessage(msg);
                }
            } else {
                toast(body.get("warn"));
            }
            dismissLoading();
        }
    }
}
