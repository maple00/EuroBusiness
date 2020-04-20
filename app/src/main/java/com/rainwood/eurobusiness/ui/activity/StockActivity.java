package com.rainwood.eurobusiness.ui.activity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;

import com.rainwood.eurobusiness.R;
import com.rainwood.eurobusiness.base.BaseActivity;
import com.rainwood.eurobusiness.common.Contants;
import com.rainwood.eurobusiness.domain.PressBean;
import com.rainwood.eurobusiness.domain.StocksBean;
import com.rainwood.eurobusiness.json.JsonParser;
import com.rainwood.eurobusiness.okhttp.HttpResponse;
import com.rainwood.eurobusiness.okhttp.OnHttpListener;
import com.rainwood.eurobusiness.request.RequestPost;
import com.rainwood.eurobusiness.ui.adapter.ItemStockAdapter;
import com.rainwood.eurobusiness.ui.adapter.TopTypeAdapter;
import com.rainwood.tools.refresh.DaisyRefreshLayout;
import com.rainwood.tools.viewinject.ViewById;
import com.rainwood.tools.widget.MeasureGridView;
import com.rainwood.tools.widget.MeasureListView;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * @Author: a797s
 * @Date: 2020/2/20
 * @Desc: 盘点记录
 */
public class StockActivity extends BaseActivity implements View.OnClickListener, OnHttpListener {

    @Override
    protected int getLayoutId() {
        return R.layout.activity_stock;
    }

    @ViewById(R.id.iv_back)
    private ImageView pageBack;
    @ViewById(R.id.gv_top_type)
    private MeasureGridView topType;
    @ViewById(R.id.iv_new_found)                // 新建盘点
    private ImageView newFound;
    @ViewById(R.id.lv_content_list)
    private MeasureListView contentList;
    @ViewById(R.id.ll_search)
    private LinearLayout search;
    @ViewById(R.id.ll_search_1)
    private LinearLayout search1;
    @ViewById(R.id.iv_screening)
    private ImageView screening;
    @ViewById(R.id.drl_refresh)
    private DaisyRefreshLayout mRefreshLayout;

    private List<PressBean> topList;
    private List<StocksBean> mCopyList = new ArrayList<>();
    private String[] inTops = {"全部", "已完成", "审核中"};
    // 批发商
    private String[] salerTops = {"全部", "已完成", "待审核"};

    private final int INITIAL_SIZE = 0x101;
    private final int REFRESH_SIZE = 0x102;
    private static int pageCount = 0;
    private String mType;

    @SuppressLint("SetTextI18n")
    @Override
    protected void initView() {
        pageBack.setOnClickListener(this);
        newFound.setOnClickListener(this);
        if (Contants.CHOOSE_MODEL_SIZE == 7){
            newFound.setVisibility(View.VISIBLE);
            screening.setVisibility(View.GONE);
        }else {
            newFound.setVisibility(View.GONE);
            screening.setVisibility(View.VISIBLE);
        }
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
        topList = new ArrayList<>();
        for (int i = 0; i < inTops.length && Contants.CHOOSE_MODEL_SIZE == 7; i++) {
            PressBean press = new PressBean();
            if (i == 0) {
                press.setChoose(true);
            } else {
                press.setChoose(false);
            }
            press.setTitle(inTops[i]);
            topList.add(press);
        }

        for (int i = 0; i < salerTops.length && Contants.CHOOSE_MODEL_SIZE == 112; i++) {
            search.setVisibility(View.GONE);
            newFound.setVisibility(View.GONE);
            PressBean press = new PressBean();
            if (i == 0) {
                press.setChoose(true);
            } else {
                press.setChoose(false);
            }
            press.setTitle(salerTops[i]);
            topList.add(press);
        }
        // 头部类型
        if (Contants.CHOOSE_MODEL_SIZE == 7){
            setTopType();
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_back:
                finish();
                break;
            case R.id.iv_new_found:
                // toast("新建盘点");
                openActivity(NewStockActivity.class);
                break;
        }
    }

    private void setTopType() {
        TopTypeAdapter typeAdapter = new TopTypeAdapter(StockActivity.this, topList);
        topType.setAdapter(typeAdapter);
        topType.setNumColumns(3);
        typeAdapter.setOnClickItem(position -> {
            for (PressBean pressBean : topList) {
                pressBean.setChoose(false);
            }
            topList.get(position).setChoose(true);
            // change top type request
            switch (position) {
                default:
                    mType = "";
                    break;
                case 1:
                    mType = "complete";
                    break;
                case 2:
                    mType = "waitAudit";
                    break;
            }
            showLoading("loading");
            mCopyList = new ArrayList<>();
            RequestPost.getStockChecklist(String.valueOf(pageCount), mType, "", StockActivity.this);
        });
    }

    @SuppressLint("HandlerLeak")
    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(@NonNull Message msg) {
            switch (msg.what) {
                case INITIAL_SIZE:
                    mRefreshLayout.setLoadMore(false);
                    mRefreshLayout.setRefreshing(false);
                    // 门店端 --- 门店端不具备审核权限
                    if (Contants.CHOOSE_MODEL_SIZE == 7) {
                        for (StocksBean bean : mCopyList) {         // 设置权限
                            bean.setPermission(1);
                        }
                        // content
                        ItemStockAdapter stockAdapter = new ItemStockAdapter(StockActivity.this, mCopyList);
                        contentList.setAdapter(stockAdapter);
                        stockAdapter.setOnClickItem(position -> {
                            // toast("详情");
                            try {
                                Intent intent = new Intent(StockActivity.this, StockDetailActivity.class);
                                intent.putExtra("InventoryId", mCopyList.get(position).getId());
                                intent.putExtra("permissionId", "1");
                                startActivity(intent);
                            } catch (Exception e) {
                                toast("正在刷新请稍后");
                            }
                        });
                    }

                    // 批发商
                    if (Contants.CHOOSE_MODEL_SIZE == 112) {
                        TopTypeAdapter typeAdapter = new TopTypeAdapter(StockActivity.this, topList);
                        topType.setAdapter(typeAdapter);
                        topType.setNumColumns(3);
                        typeAdapter.setOnClickItem(position -> {
                            for (PressBean pressBean : topList) {
                                pressBean.setChoose(false);
                            }
                            topList.get(position).setChoose(true);
                            // change top type request
                            switch (position) {
                                default:
                                    mType = "";
                                    break;
                                case 1:
                                    mType = "complete";
                                    break;
                                case 2:
                                    mType = "waitAudit";
                                    break;
                            }
                            showLoading("loading");
                            mCopyList = new ArrayList<>();
                            RequestPost.getStockChecklist(String.valueOf(pageCount), mType, "", StockActivity.this);
                        });
                        // content
                        ItemStockAdapter stockAdapter = new ItemStockAdapter(StockActivity.this, mCopyList);
                        contentList.setAdapter(stockAdapter);
                        stockAdapter.setOnClickItem(position -> {
                            // toast("详情");
                            try {
                                Intent intent = new Intent(StockActivity.this, StockDetailActivity.class);
                                intent.putExtra("InventoryId", mCopyList.get(position).getId());
                                intent.putExtra("permissionId", "0");
                                startActivity(intent);
                            } catch (Exception e) {
                                toast("正在刷新请稍后");
                            }
                        });
                    }
                    break;
                case REFRESH_SIZE:
                    //上拉加载
                    mRefreshLayout.setOnLoadMoreListener(() -> {
                        pageCount++;
                        // request
                        RequestPost.getStockChecklist(String.valueOf(pageCount), mType, "", StockActivity.this);
                    });
                    // 下拉刷新
                    mRefreshLayout.setOnRefreshListener(() -> {
                        pageCount = 0;
                        // request
                        mCopyList = new ArrayList<>();
                        RequestPost.getStockChecklist(String.valueOf(pageCount), mType, "", StockActivity.this);
                    });
                    // 第一次进来的时候刷新
                    mRefreshLayout.setOnAutoLoadListener(() -> {
                        // default query new --
                        mCopyList = new ArrayList<>();
                        RequestPost.getStockChecklist(String.valueOf(pageCount), mType, "", StockActivity.this);
                    });
                    mRefreshLayout.autoRefresh();
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
                // 获取盘点记录列表
                if (result.url().contains("wxapi/v1/stock.php?type=getStockChecklist")) {
                    List<StocksBean> stocksList = JsonParser.parseJSONArray(StocksBean.class,
                            JsonParser.parseJSONObject(body.get("data")).get("checklist"));
                    if (stocksList != null) {
                        mCopyList.addAll(stocksList);
                        Message msg = new Message();
                        msg.what = INITIAL_SIZE;
                        mHandler.sendMessage(msg);
                    }
                }
            } else {
                toast(body.get("warn"));
            }
            if (getDialog() != null)
                dismissLoading();
        }
    }
}
