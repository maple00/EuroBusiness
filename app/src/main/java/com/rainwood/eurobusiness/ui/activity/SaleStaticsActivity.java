package com.rainwood.eurobusiness.ui.activity;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.rainwood.eurobusiness.R;
import com.rainwood.eurobusiness.base.BaseActivity;
import com.rainwood.eurobusiness.domain.SaleStaticsBean;
import com.rainwood.eurobusiness.ui.adapter.SaleStaticAdapter;
import com.rainwood.tools.viewinject.ViewById;
import com.rainwood.tools.widget.MeasureListView;

import java.util.ArrayList;
import java.util.List;

/**
 * @Author: a797s
 * @Date: 2020/2/13
 * @Desc:
 */
public class SaleStaticsActivity extends BaseActivity implements View.OnClickListener {

    @Override
    protected int getLayoutId() {
        return R.layout.activity_sales_statics;
    }

    @ViewById(R.id.iv_back)
    private ImageView pageBack;
    @ViewById(R.id.tv_title)
    private TextView pageTitle;
    @ViewById(R.id.lv_modules_list)
    private MeasureListView moduleList;

    @Override
    protected void initView() {
        pageBack.setOnClickListener(this);
        pageBack.setImageResource(R.drawable.icon_white_page_back);
        pageTitle.setText("订单销售统计");
        pageTitle.setTextColor(getResources().getColor(R.color.white));

        SaleStaticAdapter staticAdapter = new SaleStaticAdapter(this, mList);
        moduleList.setAdapter(staticAdapter);

    }

    @Override
    protected void initData() {
        mList = new ArrayList<>();
        for (int i = 0; i < titles.length; i++) {
            SaleStaticsBean saleStatics = new SaleStaticsBean();
            saleStatics.setTitle(titles[i]);
            saleStatics.setToday(today[i]);
            saleStatics.setMonth(momth[i]);
            mList.add(saleStatics);
        }
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
    private List<SaleStaticsBean> mList;
    private String[] titles = {"客户数", "订单数", "销售额"};
    private String[] today = {"26", "48", "500€"};
    private String[] momth = {"530", "6967", "65820€"};

}
