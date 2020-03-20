package com.rainwood.eurobusiness.ui.activity;

import android.annotation.SuppressLint;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.rainwood.eurobusiness.R;
import com.rainwood.eurobusiness.base.BaseActivity;
import com.rainwood.eurobusiness.common.Contants;
import com.rainwood.eurobusiness.domain.PressBean;
import com.rainwood.eurobusiness.domain.StockBean;
import com.rainwood.eurobusiness.ui.adapter.ItemStockAdapter;
import com.rainwood.eurobusiness.ui.adapter.TopTypeAdapter;
import com.rainwood.tools.viewinject.ViewById;
import com.rainwood.tools.widget.MeasureGridView;
import com.rainwood.tools.widget.MeasureListView;

import java.util.ArrayList;
import java.util.List;

/**
 * @Author: a797s
 * @Date: 2020/2/20
 * @Desc: 盘点记录
 */
public class StockActivity extends BaseActivity implements View.OnClickListener {

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

    @SuppressLint("SetTextI18n")
    @Override
    protected void initView() {
        pageBack.setOnClickListener(this);
        newFound.setOnClickListener(this);
        // 门店端
        if (Contants.CHOOSE_MODEL_SIZE == 7) {
            search1.setVisibility(View.GONE);
            screening.setVisibility(View.GONE);
            TopTypeAdapter typeAdapter = new TopTypeAdapter(this, topList);
            topType.setAdapter(typeAdapter);
            topType.setNumColumns(3);
            typeAdapter.setOnClickItem(position -> {
                for (PressBean pressBean : topList) {
                    pressBean.setChoose(false);
                }
                topList.get(position).setChoose(true);
            });
            // content
            ItemStockAdapter stockAdapter = new ItemStockAdapter(this, mList);
            contentList.setAdapter(stockAdapter);
            stockAdapter.setOnClickItem(position -> {
                // toast("详情");
                openActivity(StockDetailActivity.class);
            });
        }

        // 批发商
        if (Contants.CHOOSE_MODEL_SIZE == 112) {
            search.setVisibility(View.GONE);
            newFound.setVisibility(View.GONE);
            TopTypeAdapter typeAdapter = new TopTypeAdapter(this, topList);
            topType.setAdapter(typeAdapter);
            topType.setNumColumns(3);
            typeAdapter.setOnClickItem(position -> {
                for (PressBean pressBean : topList) {
                    pressBean.setChoose(false);
                }
                topList.get(position).setChoose(true);
            });
            // content
            ItemStockAdapter stockAdapter = new ItemStockAdapter(this, mList);
            contentList.setAdapter(stockAdapter);
            stockAdapter.setOnClickItem(position -> {
                // toast("详情");
                openActivity(StockDetailActivity.class);
            });
        }
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
            PressBean press = new PressBean();
            if (i == 0) {
                press.setChoose(true);
            } else {
                press.setChoose(false);
            }
            press.setTitle(salerTops[i]);
            topList.add(press);
        }

        mList = new ArrayList<>();
        for (int i = 0; i < 3; i++) {
            StockBean stock = new StockBean();
            stock.setImgPath(null);
            stock.setName("西装外套式系缀扣连衣裙");
            stock.setModel("XDF-256165");
            stock.setParams("杏色/XL");
            stock.setVenNum("200");
            stock.setStockNum("195");
            stock.setStatus("审核中");
            mList.add(stock);
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

    /*
    模拟数据
     */
    private List<PressBean> topList;
    private List<StockBean> mList;
    private String[] inTops = {"全部", "已完成", "审核中"};
    // 批发商
    private String[] salerTops = {"全部", "已完成","待审核"};
}
