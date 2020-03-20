package com.rainwood.eurobusiness.ui.activity;

import android.annotation.SuppressLint;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.rainwood.eurobusiness.R;
import com.rainwood.eurobusiness.base.BaseActivity;
import com.rainwood.eurobusiness.domain.CommonUIBean;
import com.rainwood.eurobusiness.domain.SizeBean;
import com.rainwood.eurobusiness.ui.adapter.SizeAdapter;
import com.rainwood.eurobusiness.ui.adapter.StoresListAdapter;
import com.rainwood.tools.common.FontDisplayUtil;
import com.rainwood.tools.statusbar.StatusBarUtil;
import com.rainwood.tools.viewinject.ViewById;
import com.rainwood.tools.widget.MeasureGridView;
import com.rainwood.tools.widget.MeasureListView;

import java.util.ArrayList;
import java.util.List;

/**
 * @Author: shearson
 * @Time: 2020/2/25 12:08
 * @Desc: 订单详情
 */
public class OrderDetailActivity extends BaseActivity implements View.OnClickListener {

    @Override
    protected int getLayoutId() {
        return R.layout.activity_order_detail;
    }

    @ViewById(R.id.iv_back)
    private ImageView pageBack;
    @ViewById(R.id.tv_title)
    private TextView pageTitle;
    @ViewById(R.id.tv_right_text)
    private TextView topText;
    @ViewById(R.id.rl_top_item)
    private RelativeLayout topItem;
    @ViewById(R.id.rl_action_bar)
    private RelativeLayout actionBar;
    @ViewById(R.id.tv_status)
    private TextView status;
    @ViewById(R.id.iv_img)
    private ImageView image;
    @ViewById(R.id.tv_name)
    private TextView name;
    @ViewById(R.id.tv_model)
    private TextView model;
    @ViewById(R.id.tv_discount)
    private TextView discount;
    @ViewById(R.id.tv_rate)
    private TextView rate;
    @ViewById(R.id.tv_params)
    private TextView params;
    @ViewById(R.id.lv_content_list)             // 公司参数
    private MeasureListView contentList;
    @ViewById(R.id.lv_params_list)              // 订单参数
    private MeasureListView paramsList;
    @ViewById(R.id.gv_params_list)              // 汇总参数
    private MeasureGridView paramsbottomList;

    @SuppressLint("SetTextI18n")
    @Override
    protected void initView() {
        initContext();

        // 模拟数据
        status.setText("待发货");
        image.setImageResource(R.drawable.icon_loadding_fail);
        name.setText("西装外套式系缀扣连衣裙连衣裙...");
        model.setText("XDF-256165");
        discount.setText("25%折扣");
        rate.setText("16%税率");

        StoresListAdapter storesListAdapter = new StoresListAdapter(this, companyInfoList);
        contentList.setAdapter(storesListAdapter);
        SizeAdapter sizeAdapter = new SizeAdapter(this, sizeLists);
        paramsList.setAdapter(sizeAdapter);

        StoresListAdapter bottomAdapter = new StoresListAdapter(this, bottomInfoList);
        paramsbottomList.setNumColumns(2);
        paramsbottomList.setAdapter(bottomAdapter);
    }

    @Override
    protected void initData() {
        super.initData();
        companyInfoList = new ArrayList<>();
        for (int i = 0; i < topTitles.length; i++) {
            CommonUIBean commonUI = new CommonUIBean();
            commonUI.setTitle(topTitles[i]);
            commonUI.setShowText(topLabel[i]);
            companyInfoList.add(commonUI);
        }
        sizeLists = new ArrayList<>();
        for (int j = 0; j < colors.length; j++) {             // 商品规格
            SizeBean sizeBean = new SizeBean();
            sizeBean.setColor(colors[j]);
            sizeBean.setSize(sizes[j]);
            sizeBean.setWholsePrice(wholsale[j]);
            sizeBean.setRepertory(repertory[j]);
            sizeLists.add(sizeBean);
        }
        bottomInfoList = new ArrayList<>();
        for (int i = 0; i < bottomTitle.length; i++) {
            CommonUIBean commonUI = new CommonUIBean();
            commonUI.setTitle(bottomTitle[i]);
            commonUI.setShowText(bottomLabel[i]);
            bottomInfoList.add(commonUI);
        }
    }

    /**
     * initView
     */
    private void initContext() {
        pageBack.setOnClickListener(this);
        pageBack.setImageResource(R.drawable.icon_white_page_back);
        pageTitle.setText("订单详情");
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
    private List<CommonUIBean> companyInfoList;
    private String[] topTitles = {"公司名称", "收货地址", "公司名称", "税号(P.IVA)：", "PEC邮箱/SDI：", "收货人", "地区"};
    private String[] topLabel = {"双木衣馆", "中国重庆市南岸区弹子石腾龙大道国际商务大厦A座22-2", "双木衣馆服饰有限公司", "53012023600",
                "450026@163.com", "李亚琪", "中国-重庆"};
    private List<SizeBean> sizeLists;
    private String[] colors = {"枫叶红", "枫叶红", "粉色", "粉色", "粉色"};
    private String[] sizes = {"S", "M", "S", "S", "S"};
    private String[] wholsale = {"120.00€", "120.00€", "120.00€", "120.00€", "120.00€"};
    private String[] repertory = {"900", "900", "900", "900", "900"};
    //
    private List<CommonUIBean> bottomInfoList;
    private String[] bottomTitle = {"运费", "折扣单价", "增值税", "折扣比例", "VIP优惠", "折扣总价", "订单总计"};
    private String[] bottomLabel = {"包邮", "90.00", "含税", "20%", "-200.00", "14900.00", "14900.00"};
}
