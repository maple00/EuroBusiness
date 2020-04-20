package com.rainwood.eurobusiness.ui.activity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.rainwood.eurobusiness.R;
import com.rainwood.eurobusiness.base.BaseActivity;
import com.rainwood.eurobusiness.common.Contants;
import com.rainwood.eurobusiness.domain.CommonUIBean;
import com.rainwood.eurobusiness.domain.GoodsDetailBean;
import com.rainwood.eurobusiness.domain.ImagesBean;
import com.rainwood.eurobusiness.domain.ItemGridBean;
import com.rainwood.eurobusiness.domain.SpecialBean;
import com.rainwood.eurobusiness.domain.StockBean;
import com.rainwood.eurobusiness.json.JsonParser;
import com.rainwood.eurobusiness.okhttp.HttpResponse;
import com.rainwood.eurobusiness.okhttp.OnHttpListener;
import com.rainwood.eurobusiness.request.RequestPost;
import com.rainwood.eurobusiness.ui.adapter.InventoryAdapter;
import com.rainwood.eurobusiness.ui.adapter.LevelTypeAdapter;
import com.rainwood.eurobusiness.utils.ListUtils;
import com.rainwood.tools.permission.OnPermission;
import com.rainwood.tools.permission.Permission;
import com.rainwood.tools.permission.XXPermissions;
import com.rainwood.tools.refresh.DaisyRefreshLayout;
import com.rainwood.tools.viewinject.ViewById;
import com.rainwood.tools.widget.MeasureGridView;
import com.rainwood.tools.widget.MeasureListView;
import com.rainwood.zxingqrc.android.QRCodeCaptureActivity;

import org.json.JSONArray;
import org.json.JSONException;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * @Author: a797s
 * @Date: 2020/2/18
 * @Desc: 库存商品首页
 */
public class InventoryActivity extends BaseActivity implements View.OnClickListener, OnHttpListener {

    @Override
    protected int getLayoutId() {
        return R.layout.activity_inventory;
    }

    @ViewById(R.id.iv_back)
    private ImageView pageBack;
    @ViewById(R.id.tv_screening)
    private TextView screening;
    @ViewById(R.id.tv_all_num)
    private TextView allNum;
    @ViewById(R.id.tv_all_money)
    private TextView allMoney;
    @ViewById(R.id.lv_content)
    private MeasureListView contentList;
    @ViewById(R.id.gv_level_type)
    private MeasureGridView levelType;
    @ViewById(R.id.tv_already_bottom)
    private TextView alreadrBottom;
    @ViewById(R.id.drl_refresh)
    private DaisyRefreshLayout mRefreshLayout;


    private List<StockBean> mCopyList = new ArrayList<>();
    // levele
    private List<ItemGridBean> levelList;
    private String[] levels = {"一级分类", "二级分类"};

    private final int INITIAL_SIZE = 0x101;
    private final int REFRESH_SIZE = 0x102;
    private static int pageCount = 0;

    @SuppressLint("SetTextI18n")
    @Override
    protected void initView() {
        pageBack.setOnClickListener(this);
        screening.setOnClickListener(this);

    }

    @Override
    protected void onResume() {
        super.onResume();
        Message msg = new Message();
        msg.what = REFRESH_SIZE;
        mHandler.sendMessage(msg);
    }

    @Override
    protected void initData() {
        super.initData();
        levelList = new ArrayList<>();
        for (String level : levels) {
            ItemGridBean itemGrid = new ItemGridBean();
            itemGrid.setItemName(level);
            itemGrid.setImgId(R.drawable.ic_down_selector);
            levelList.add(itemGrid);
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_back:
                finish();
                break;
            case R.id.tv_screening:
                // toast("扫一扫");
                openScreening();
                break;
        }
    }

    @SuppressLint("HandlerLeak")
    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(@NonNull Message msg) {
            switch (msg.what) {
                case INITIAL_SIZE:
                    // level
                    LevelTypeAdapter typeAdapter = new LevelTypeAdapter(InventoryActivity.this, levelList);
                    levelType.setAdapter(typeAdapter);
                    typeAdapter.setOnClickItem(new LevelTypeAdapter.OnClickItem() {
                        @Override
                        public void onClickItem(int position) {
                            toast(levelList.get(position).getItemName());
                        }
                    });
                    // content
                    mRefreshLayout.setRefreshing(false);
                    mRefreshLayout.setLoadMore(false);
                    InventoryAdapter inventoryAdapter = new InventoryAdapter(InventoryActivity.this, mCopyList);
                    contentList.setAdapter(inventoryAdapter);
                    inventoryAdapter.setOnClickItem(position -> {
                        // toast("点击了： " + position);
                        Intent intent = new Intent(InventoryActivity.this, InventoryGoodsActivity.class);
                        intent.putExtra("stockId", mCopyList.get(position).getId());
                        startActivity(intent);
                    });
                    alreadrBottom.setVisibility(View.VISIBLE);
                    break;
                case REFRESH_SIZE:
                    //上拉加载
                    mRefreshLayout.setOnLoadMoreListener(() -> {
                        pageCount++;
                        RequestPost.getAllStock(String.valueOf(pageCount), "", "", InventoryActivity.this);
                    });
                    // 下拉刷新
                    mRefreshLayout.setOnRefreshListener(() -> {
                        pageCount = 0;
                        mCopyList = new ArrayList<>();
                        RequestPost.getAllStock(String.valueOf(pageCount), "", "", InventoryActivity.this);
                    });
                    // 第一次进来的时候刷新
                    mRefreshLayout.setOnAutoLoadListener(() -> {
                        // default query new -- 默认查询供应商采购订单的全部
                        // showLoading("loading");
                        RequestPost.getAllStock(String.valueOf(pageCount), "", "", InventoryActivity.this);
                    });
                    mRefreshLayout.autoRefresh();
                    break;
            }
        }
    };

    /**
     * 打开扫一扫
     */
    private void openScreening() {
        // 先获取相机权限
        XXPermissions.with(getActivity())
                // 可设置被拒绝后继续申请，直到用户授权或永久拒绝
                .constantRequest()
                // 不指定权限则自定获取订单中的危险权限
                .permission(Permission.CAMERA)
                .request(new OnPermission() {
                    @Override
                    public void hasPermission(List<String> granted, boolean isAll) {
                        if (isAll) {
                            // 去扫码
                            Intent intent = new Intent(InventoryActivity.this, QRCodeCaptureActivity.class);
                            // 设置标题栏的颜色
                            intent.putExtra(QRCodeCaptureActivity.STATUS_BAR_COLOR, Color.parseColor("#99000000"));
                            startActivityForResult(intent, Contants.SCANCHECKCODE);
                        } else {
                            toast("获取权限成功，部分权限未正常授予");
                        }
                    }

                    @Override
                    public void noPermission(List<String> denied, boolean quick) {
                        if (quick) {
                            toast("被永久拒绝授权，请手动授予权限");
                            //如果是被永久拒绝就跳转到应用权限系统设置页面
                            XXPermissions.gotoPermissionSettings(getActivity());
                        } else {
                            toast("获取权限失败");
                        }
                    }
                });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == Contants.SCANCHECKCODE && resultCode == RESULT_OK) {
            Bundle bundle = data.getExtras();
            String result = null;
            if (bundle != null) {
                result = bundle.getString(QRCodeCaptureActivity.CODE_CONTENT);
            }
            if (TextUtils.isEmpty(result)) {
                toast("未识别到任何内容");
                return;
            }
            createData();
            // 识别的商品到库存商品详情页面
            showLoading("loading");
            RequestPost.getStockDetail("", result, this);
            // toast("识别的内容：" + result);
        }
    }

    @Override
    public void onHttpFailure(HttpResponse result) {

    }

    @Override
    public void onHttpSucceed(HttpResponse result) {
        Map<String, String> body = JsonParser.parseJSONObject(result.body());
        if (body != null) {
            if ("1".equals(body.get("code"))) {
                // 获取库存列表
                if (result.url().contains("wxapi/v1/stock.php?type=getAllStock")) {
                    List<StockBean> stockList = JsonParser.parseJSONArray(StockBean.class, JsonParser.parseJSONObject(body.get("data")).get("stocklist"));
                    if (stockList != null) {
                        mCopyList.addAll(stockList);
                    }
                    allNum.setText(JsonParser.parseJSONObject(JsonParser
                            .parseJSONObject(body.get("data")).get("info")).get("totalStock"));            // 总库存
                    allMoney.setText(JsonParser.parseJSONObject(JsonParser
                            .parseJSONObject(body.get("data")).get("info")).get("totalMoney"));          // 总金额

                    Message msg = new Message();
                    msg.what = INITIAL_SIZE;
                    mHandler.sendMessage(msg);
                }

                // 获取库存详情
                if (result.url().contains("wxapi/v1/stock.php?type=getStockInfo")) {
                    // 规格参数
                    mSpeciaList = JsonParser.parseJSONArray(SpecialBean.class,
                            JsonParser.parseJSONObject(body.get("data")).get("skulist"));
                    // 基本信息
                    Map<String, String> goodsInfo = JsonParser.parseJSONObject(JsonParser.parseJSONObject(body.get("data")).get("goodsInfo"));
                    // 商品信息
                    for (int i = 0; i < ListUtils.getSize(mList); i++) {
                        switch (i) {
                            case 0:         // 基本信息
                                for (int j = 0; j < ListUtils.getSize(mList.get(i).getCommList()); j++) {
                                    switch (j) {
                                        case 0:
                                            mList.get(i).getCommList().get(j).setShowText(goodsInfo.get("name"));
                                            break;
                                        case 1:
                                            mList.get(i).getCommList().get(j).setShowText(goodsInfo.get("model"));
                                            break;
                                        case 2:
                                            mList.get(i).getCommList().get(j).setShowText(goodsInfo.get("goodsType"));
                                            break;
                                        case 3:
                                            mList.get(i).getCommList().get(j).setShowText(goodsInfo.get("barCode"));
                                            break;
//                                        case 4:
//                                            mList.get(i).getCommList().get(j).setShowText(goodsInfo.get("name"));
//                                            break;
                                    }
                                }
                                break;
                            case 1:         // 商品定价
                                for (int j = 0; j < ListUtils.getSize(mList.get(i).getCommList()); j++) {
                                    switch (j) {
                                        case 0:
                                            mList.get(i).getCommList().get(j).setShowText(goodsInfo.get("tradePrice"));
                                            break;
                                        case 1:
                                            mList.get(i).getCommList().get(j).setShowText(goodsInfo.get("retailPrice"));
                                            break;
                                        case 2:
                                            mList.get(i).getCommList().get(j).setShowText(goodsInfo.get("isTax"));
                                            break;
                                    }
                                }
                                break;
                            case 2:         // 规格参数
                                for (int j = 0; j < ListUtils.getSize(mList.get(i).getCommList()); j++) {
                                    switch (j) {
                                        case 0:
                                            mList.get(i).getCommList().get(j).setShowText(goodsInfo.get("goodsOption"));
                                            break;
                                        case 1:
                                            mList.get(i).getCommList().get(j).setShowText(goodsInfo.get("goodsUnit"));
                                            break;
                                        case 2:
                                            mList.get(i).getCommList().get(j).setShowText(goodsInfo.get("startNum"));
                                            break;
                                    }
                                }
                                break;
                            case 3:         // 商品图片
                                JSONArray jsonArray = JsonParser.parseJSONArrayString(JsonParser.parseJSONObject(body.get("data")).get("icolist"));
                                List<ImagesBean> imageList = new ArrayList<>();
                                for (int j = 0; j < jsonArray.length(); j++) {
                                    ImagesBean images = new ImagesBean();
                                    try {
                                        images.setSrc(jsonArray.getString(j));
                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                    }
                                    imageList.add(images);
                                }
                                mList.get(i).setImgList(imageList);
                                break;
                        }
                    }

                    postDelayed(() -> {
                        Intent intent = new Intent(this, InventoryGoodsActivity.class);
                        intent.putExtra("goodsDetail", (Serializable) mList);
                        intent.putExtra("specialData", (Serializable) mSpeciaList);
                        startActivity(intent);
                    }, 100);
                }

            } else {
                toast(body.get("warn"));
            }
            if (getDialog() != null)
                dismissLoading();
        }
    }

    // 商品详情
    private List<GoodsDetailBean> mList;
    // 库存信息
    private List<SpecialBean> mSpeciaList;
    private String[] mainTitles = {"基本信息", "商品定价", "规格参数", "商品图片"};  //， "创建者"
    // 基本信息
    private String[] baseTitle = {"商品名称", "商品型号", "商品分类", "条码"};
    // 商品定价
    private String[] priceTitles = {"批发价", "零售价", "增值税"};
    // 规格参数
    private String[] specialTitles = {"商品尺码", "商品规格", "最小起订量", "税率"};
    private void createData() {
        mList = new ArrayList<>();
        for (int i = 0; i < mainTitles.length; i++) {
            GoodsDetailBean goodsDetail = new GoodsDetailBean();
            goodsDetail.setTitle(mainTitles[i]);
            goodsDetail.setType(i);
            goodsDetail.setLoadType(101);
            List<CommonUIBean> commLists = new ArrayList<>();
            for (int j = 0; j < baseTitle.length && i == 0; j++) {      // 基本信息
                CommonUIBean commonUI = new CommonUIBean();
                commonUI.setTitle(baseTitle[j]);
                commLists.add(commonUI);
            }
            for (int j = 0; j < priceTitles.length && i == 1; j++) {        // 商品定价
                CommonUIBean commonUI = new CommonUIBean();
                commonUI.setTitle(priceTitles[j]);
                commLists.add(commonUI);
            }
            // 规格参数
            for (int j = 0; j < specialTitles.length && i == 2; j++) {
                CommonUIBean commonUI = new CommonUIBean();
                commonUI.setTitle(specialTitles[j]);
                commLists.add(commonUI);
            }
            goodsDetail.setCommList(commLists);
            mList.add(goodsDetail);
        }
    }
}
