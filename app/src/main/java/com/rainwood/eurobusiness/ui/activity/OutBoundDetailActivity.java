package com.rainwood.eurobusiness.ui.activity;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.rainwood.eurobusiness.R;
import com.rainwood.eurobusiness.base.BaseActivity;
import com.rainwood.eurobusiness.common.Contants;
import com.rainwood.eurobusiness.domain.CommonUIBean;
import com.rainwood.eurobusiness.domain.OrderBean;
import com.rainwood.eurobusiness.ui.adapter.OrderNewAdapter;
import com.rainwood.eurobusiness.ui.adapter.OutBoundDetailAdapter;
import com.rainwood.tools.viewinject.ViewById;
import com.rainwood.tools.widget.MeasureListView;

import java.util.ArrayList;
import java.util.List;

/**
 * @Author: a797s
 * @Date: 2020/2/20
 * @Desc: 出库详情
 */
public class OutBoundDetailActivity extends BaseActivity implements View.OnClickListener {

    @Override
    protected int getLayoutId() {
        return R.layout.activity_bound_detail;
    }

    @ViewById(R.id.iv_back)
    private ImageView pageBack;
    @ViewById(R.id.tv_title)
    private TextView pageTitle;
    @ViewById(R.id.lv_content_list)
    private MeasureListView contentList;

    @Override
    protected void initView() {
        pageBack.setOnClickListener(this);
        if (Contants.CHOOSE_MODEL_SIZE == 111 || Contants.CHOOSE_MODEL_SIZE == 6){
            pageTitle.setText("入库详情");
        }
        if (Contants.CHOOSE_MODEL_SIZE ==  5 || Contants.CHOOSE_MODEL_SIZE == 110){
            pageTitle.setText("出库详情");
        }
        OutBoundDetailAdapter boundAdapter = new OutBoundDetailAdapter(this, mList);
        contentList.setAdapter(boundAdapter);

    }

    @Override
    protected void initData() {
        super.initData();
        mList = new ArrayList<>();
        for (int i = 0; i < titles.length; i++) {
            OrderBean order = new OrderBean();
            order.setTitle(titles[i]);
            List<CommonUIBean> commonUIList = new ArrayList<>();
            for (int j = 0; j < goodsTitle.length && i == 0; j++) {
                CommonUIBean commonUI = new CommonUIBean();
                commonUI.setTitle(goodsTitle[j]);
                commonUI.setShowText(goodsLabel[j]);
                commonUIList.add(commonUI);
            }
            for (int j = 0; j < boundTitle.length && i ==1; j++) {
                CommonUIBean commonUI = new CommonUIBean();
                commonUI.setTitle(boundTitle[j]);
                commonUI.setShowText(bounLabel[j]);
                commonUIList.add(commonUI);
            }
            order.setCommonList(commonUIList);
            mList.add(order);
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
    private List<OrderBean> mList;
    private String[] titles = {"商品信息", "出库信息"};
    private String[] goodsTitle = {"商品名称", "商品型号", "条码", "商品分类", "规格"};
    private String[] goodsLabel = {"西装外套式系缀扣连衣裙", "XDF-226023", "6920584471071", "女士时装-连衣裙", "杏色/XL"};
    private String[] boundTitle = {"出库类型", "订单号", "数量", "操作人", "出库时间"};
    private String[] bounLabel = {"线上订单", "55122000001200", "147", "李亚奇", "2020-01-15 13:44"};
}
