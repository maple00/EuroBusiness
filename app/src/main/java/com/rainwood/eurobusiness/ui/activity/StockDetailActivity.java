package com.rainwood.eurobusiness.ui.activity;

import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.rainwood.eurobusiness.R;
import com.rainwood.eurobusiness.base.BaseActivity;
import com.rainwood.eurobusiness.domain.CommonUIBean;
import com.rainwood.eurobusiness.domain.OrderBean;
import com.rainwood.eurobusiness.ui.adapter.StockDetailAdapter;
import com.rainwood.tools.common.FontDisplayUtil;
import com.rainwood.tools.statusbar.StatusBarUtil;
import com.rainwood.tools.viewinject.ViewById;
import com.rainwood.tools.widget.MeasureListView;

import java.util.ArrayList;
import java.util.List;

/**
 * @Author: a797s
 * @Date: 2020/2/20
 * @Desc: 盘点详情
 */
public class StockDetailActivity extends BaseActivity implements View.OnClickListener {


    @Override
    protected int getLayoutId() {
        return R.layout.activity_stock_detail;
    }

    @ViewById(R.id.iv_back)
    private ImageView pageBack;
    @ViewById(R.id.tv_title)
    private TextView pageTitle;
    @ViewById(R.id.rl_action_bar)
    private RelativeLayout actionBar;
    @ViewById(R.id.rl_top_item)
    private RelativeLayout topItem;
    @ViewById(R.id.tv_status)
    private TextView status;
    // content
    @ViewById(R.id.lv_content_list)
    private MeasureListView contentList;

    @Override
    protected void initView() {
        initContext();
        status.setText("审核中");

        StockDetailAdapter detailAdapter = new StockDetailAdapter(this, mList);
        contentList.setAdapter(detailAdapter);
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
            for (int j = 0; j < stockTitle.length && i == 1; j++) {
                CommonUIBean commonUI = new CommonUIBean();
                commonUI.setTitle(stockTitle[j]);
                commonUI.setShowText(stockLabel[j]);
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

    /**
     * 初始化 Context
     */
    private void initContext() {
        pageBack.setOnClickListener(this);
        pageBack.setImageResource(R.drawable.icon_white_page_back);
        pageTitle.setText("盘点详情");
        pageTitle.setTextColor(getResources().getColor(R.color.white));
        // 设置状态栏
        StatusBarUtil.setCommonUI(getActivity(), false);
        StatusBarUtil.setRootViewFitsSystemWindows(getActivity(), false);
        // 设置RelativeLayout 的高度
        ViewGroup.LayoutParams params = topItem.getLayoutParams();
        params.height = FontDisplayUtil.dip2px(this, 180f) + StatusBarUtil.getStatusBarHeight(this);
        topItem.setLayoutParams(params);
        // 设置标题栏高度和外边距
        ViewGroup.MarginLayoutParams layoutParams = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, FontDisplayUtil.dip2px(this, 44f));
        layoutParams.setMargins(0, FontDisplayUtil.dip2px(this, 40), 0, 0);
        actionBar.setLayoutParams(layoutParams);
    }

    /*
    模拟数据
     */
    private List<OrderBean> mList;
    private String[] titles = {"", ""};
    // 商品信息
    private String[] goodsTitle = {"商品分类", "商品名称", "商品型号", "条码", "规格"};
    private String[] goodsLabel = {"女士时装-连衣裙", "西装外套式系缀扣连衣裙", "XDF-226023", "6920584471071", "杏色/XL"};
    // 盘点信息
    private String[] stockTitle = {"库存数", "盘点数", "备注", "制单人", "盘点时间"};
    private String[] stockLabel = {"2250", "2250", "这里是备注文字内容这里是备注文字内容", "李亚奇", "2019-12-26 13:20"};
}
