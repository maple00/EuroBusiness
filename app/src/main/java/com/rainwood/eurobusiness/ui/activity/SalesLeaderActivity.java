package com.rainwood.eurobusiness.ui.activity;

import android.os.Build;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.rainwood.eurobusiness.R;
import com.rainwood.eurobusiness.base.BaseActivity;
import com.rainwood.eurobusiness.common.Contants;
import com.rainwood.eurobusiness.domain.SalesLeaderBean;
import com.rainwood.eurobusiness.ui.adapter.SalesLeaderAdapter;
import com.rainwood.tools.viewinject.ViewById;
import com.rainwood.tools.widget.MeasureListView;

import java.util.ArrayList;
import java.util.List;

/**
 * @Author: a797s
 * @Date: 2020/2/13
 * @Desc: 销售排行榜
 */
public class SalesLeaderActivity extends BaseActivity implements View.OnClickListener {

    @Override
    protected int getLayoutId() {
        return R.layout.activity_sales_leader;
    }

    @ViewById(R.id.iv_back)
    private ImageView pageBack;
    @ViewById(R.id.tv_title)
    private TextView pageTitle;
    @ViewById(R.id.lv_goods_list)
    private MeasureListView goodsList;

    // 批发商
    @ViewById(R.id.ll_sales)
    private LinearLayout sales;
    @ViewById(R.id.tv_goods)
    private TextView goods;
    @ViewById(R.id.tv_stores)
    private TextView stores;
    @ViewById(R.id.tv_custom)
    private TextView custom;

    @Override
    protected void initView() {
        pageBack.setOnClickListener(this);
        pageBack.setImageResource(R.drawable.icon_white_page_back);
        pageTitle.setTextColor(getResources().getColor(R.color.white));
        pageTitle.setText("商品销售排行榜");
        if (Contants.CHOOSE_MODEL_SIZE == 12){
            sales.setVisibility(View.GONE);
            SalesLeaderAdapter leaderAdapter = new SalesLeaderAdapter(this, mList);
            goodsList.setAdapter(leaderAdapter);
        }
        if (Contants.CHOOSE_MODEL_SIZE == 114){
            sales.setVisibility(View.VISIBLE);
            goods.setOnClickListener(this);
            stores.setOnClickListener(this);
            custom.setOnClickListener(this);
            goods.setText("商品");
            stores.setText("门店");
            custom.setText("客户");
            SalesLeaderAdapter leaderAdapter = new SalesLeaderAdapter(this, mList);
            goodsList.setAdapter(leaderAdapter);
        }
    }

    @Override
    protected void initData() {
        mList = new ArrayList<>();
        for (int i = 0; i < 200; i++) {
            SalesLeaderBean salesLeader = new SalesLeaderBean();
            salesLeader.setRanking(String.valueOf(i+1));
            salesLeader.setName("西装外套式系连衣裙");
            salesLeader.setSpecial("白色/M");
            salesLeader.setNumber("9851");
            mList.add(salesLeader);
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_back:
                finish();
                break;
            case R.id.tv_goods:
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    setTopUI(R.drawable.shape_radius_yellow_left_4, R.drawable.shape_radius_yellow_full_empry_4, R.drawable.shape_radius_yellow_right_empty_4, R.color.black01, R.color.orange25, R.color.orange25);
                }
                break;
            case R.id.tv_stores:
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    setTopUI(R.drawable.shape_radius_yellow_left_empty_4, R.drawable.shape_radius_yellow_full_4, R.drawable.shape_radius_yellow_right_empty_4, R.color.orange25, R.color.black01, R.color.orange25);
                }
                break;
            case R.id.tv_custom:
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    setTopUI(R.drawable.shape_radius_yellow_left_empty_4, R.drawable.shape_radius_yellow_full_empry_4, R.drawable.shape_radius_yellow_right_4, R.color.orange25, R.color.orange25, R.color.black01);
                }
                break;
        }
    }

    /**
     * 查看批发商的销售排行榜
     */
    private void setTopUI(int p, int p2, int p3, int p4, int p5, int p6) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            goods.setBackground(getDrawable(p));
            stores.setBackground(getDrawable(p2));
            custom.setBackground(getDrawable(p3));
        }
        goods.setTextColor(getResources().getColor(p4));
        stores.setTextColor(getResources().getColor(p5));
        custom.setTextColor(getResources().getColor(p6));
    }


    /**
     * 模拟数据
     */
    private List<SalesLeaderBean> mList;

}
