package com.rainwood.eurobusiness.ui.activity;

import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.rainwood.eurobusiness.R;
import com.rainwood.eurobusiness.base.BaseActivity;
import com.rainwood.eurobusiness.common.Contants;
import com.rainwood.eurobusiness.domain.ItemGridBean;
import com.rainwood.eurobusiness.domain.OutBoundBean;
import com.rainwood.eurobusiness.domain.PressBean;
import com.rainwood.eurobusiness.ui.adapter.ItemOutBoundAdapter;
import com.rainwood.eurobusiness.ui.adapter.LevelTypeAdapter;
import com.rainwood.eurobusiness.ui.adapter.TopTypeAdapter;
import com.rainwood.tools.viewinject.ViewById;
import com.rainwood.tools.widget.MeasureGridView;
import com.rainwood.tools.widget.MeasureListView;

import java.util.ArrayList;
import java.util.List;

/**
 * @Author: a797s
 * @Date: 2020/2/20
 * @Desc: 出库记录
 */
public class OutInBoundActivity extends BaseActivity implements View.OnClickListener {

    @Override
    protected int getLayoutId() {
        return R.layout.activity_out_bound;
    }

    @ViewById(R.id.iv_back)
    private ImageView pageBack;
    @ViewById(R.id.gv_top_type)
    private MeasureGridView topType;
    @ViewById(R.id.lv_content_list)
    private MeasureListView contentList;        // content

    @ViewById(R.id.ll_search)
    private LinearLayout search;
    @ViewById(R.id.ll_search_1)
    private LinearLayout search1;
    @ViewById(R.id.iv_new_found)
    private ImageView newFound;
    @ViewById(R.id.iv_screening)
    private ImageView screening;

    @Override
    protected void initView() {
        pageBack.setOnClickListener(this);
        if (Contants.CHOOSE_MODEL_SIZE == 5) {           // 出库记录
            OutInRecord(4);
            search1.setVisibility(View.GONE);
            screening.setVisibility(View.GONE);
        }

        if (Contants.CHOOSE_MODEL_SIZE == 6) {           // 入库记录
            search1.setVisibility(View.GONE);
            screening.setVisibility(View.GONE);
            OutInRecord(3);
        }
        // 批发商
        if (Contants.CHOOSE_MODEL_SIZE == 110) {        // 出库记录
            search.setVisibility(View.GONE);
            newFound.setVisibility(View.GONE);

            LevelTypeAdapter typeAdapter = new LevelTypeAdapter(this, levelTypeList);
            topType.setAdapter(typeAdapter);
            topType.setNumColumns(4);
            ItemOutBoundAdapter boundAdapter = new ItemOutBoundAdapter(this, mList);
            contentList.setAdapter(boundAdapter);
            boundAdapter.setOnClickItem(position -> {
                // toast("详情");
                openActivity(OutBoundDetailActivity.class);
            });
        }
        if (Contants.CHOOSE_MODEL_SIZE == 111) {         // 入库记录
            search.setVisibility(View.GONE);
            newFound.setVisibility(View.GONE);

            LevelTypeAdapter typeAdapter = new LevelTypeAdapter(this, levelTypeList);
            topType.setAdapter(typeAdapter);
            topType.setNumColumns(4);
            ItemOutBoundAdapter boundAdapter = new ItemOutBoundAdapter(this, mList);
            contentList.setAdapter(boundAdapter);
            boundAdapter.setOnClickItem(position -> {
                // toast("详情");
                openActivity(OutBoundDetailActivity.class);
            });
        }
    }

    @Override
    protected void initData() {
        super.initData();
        topList = new ArrayList<>();
        if (Contants.CHOOSE_MODEL_SIZE == 5) {              // 出库
            for (int i = 0; i < tops.length; i++) {
                PressBean press = new PressBean();
                if (i == 0) {
                    press.setChoose(true);
                } else {
                    press.setChoose(false);
                }
                press.setTitle(tops[i]);
                topList.add(press);
            }

            mList = new ArrayList<>();
            for (int i = 0; i < 3; i++) {
                OutBoundBean outBound = new OutBoundBean();
                outBound.setImgPath(null);
                outBound.setName("西装外套式系缀扣连衣裙");
                outBound.setModel("XDF-256165");
                outBound.setParams("杏色/XL");
                outBound.setSource("线上订单");
                outBound.setNum("100件");
                mList.add(outBound);
            }
        }
        if (Contants.CHOOSE_MODEL_SIZE == 6) {           // 入库
            for (int i = 0; i < inTops.length; i++) {
                PressBean press = new PressBean();
                if (i == 0) {
                    press.setChoose(true);
                } else {
                    press.setChoose(false);
                }
                press.setTitle(inTops[i]);
                topList.add(press);
            }

            mList = new ArrayList<>();
            for (int i = 0; i < 3; i++) {
                OutBoundBean outBound = new OutBoundBean();
                outBound.setImgPath(null);
                outBound.setName("西装外套式系缀扣连衣裙");
                outBound.setModel("XDF-256165");
                outBound.setParams("杏色/XL");
                outBound.setSource("线上订单");
                outBound.setNum("100件");
                mList.add(outBound);
            }
        }

        levelTypeList = new ArrayList<>();
        if (Contants.CHOOSE_MODEL_SIZE == 111){
            for (int i = 0; i < salerInTops.length; i++) {
                ItemGridBean itemGrid = new ItemGridBean();
                itemGrid.setItemName(salerInTops[i]);
                levelTypeList.add(itemGrid);
            }
            mList = new ArrayList<>();
            for (int i = 0; i < 3; i++) {
                OutBoundBean outBound = new OutBoundBean();
                outBound.setImgPath(null);
                outBound.setName("西装外套式系缀扣连衣裙");
                outBound.setModel("XDF-256165");
                outBound.setParams("杏色/XL");
                outBound.setSource("线上订单");
                outBound.setNum("100件");
                outBound.setAddress("来福士门店");
                mList.add(outBound);
            }
        }
    }

    /**
     * 出入库记录
     *
     * @param i
     */
    private void OutInRecord(int i) {
        TopTypeAdapter typeAdapter = new TopTypeAdapter(this, topList);
        topType.setAdapter(typeAdapter);
        topType.setNumColumns(i);
        typeAdapter.setOnClickItem(position -> {
            for (PressBean pressBean : topList) {
                pressBean.setChoose(false);
            }
            topList.get(position).setChoose(true);
        });

        // content
        ItemOutBoundAdapter boundAdapter = new ItemOutBoundAdapter(this, mList);
        contentList.setAdapter(boundAdapter);
        boundAdapter.setOnClickItem(position -> {
            // toast("详情");
            openActivity(OutBoundDetailActivity.class);
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_back:
                finish();
                break;
        }
    }

    /*
    模拟数据
     */
    private List<PressBean> topList;
    private List<OutBoundBean> mList;
    /*
    门店
     */
    // 出库
    private String[] tops = {"全部", "线上订单", "线下订单", "退货订单"};
    // 入库
    private String[] inTops = {"全部", "订单退货", "采购入库"};
    /*
    批发商
     */
    private List<ItemGridBean> levelTypeList;
    // 入库
    private String[] salerInTops = {"订单类型", "门店"};

}
