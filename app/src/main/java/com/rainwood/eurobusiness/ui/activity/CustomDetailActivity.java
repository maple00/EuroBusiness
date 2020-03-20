package com.rainwood.eurobusiness.ui.activity;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.rainwood.eurobusiness.R;
import com.rainwood.eurobusiness.base.BaseActivity;
import com.rainwood.eurobusiness.common.Contants;
import com.rainwood.eurobusiness.domain.CommonUIBean;
import com.rainwood.eurobusiness.domain.OrderBean;
import com.rainwood.eurobusiness.ui.adapter.OutBoundDetailAdapter;
import com.rainwood.tools.viewinject.ViewById;
import com.rainwood.tools.widget.MeasureListView;

import java.util.ArrayList;
import java.util.List;

/**
 * @Author: a797s
 * @Date: 2020/2/23
 * @Desc: 客户详情
 */
public class CustomDetailActivity extends BaseActivity implements View.OnClickListener {
    @Override
    protected int getLayoutId() {
        return R.layout.activity_custom_detail;
    }

    @ViewById(R.id.iv_back)
    private ImageView pageBack;
    @ViewById(R.id.tv_title)
    private TextView pageTitle;
    @ViewById(R.id.tv_right_text)
    private TextView rightText;
    @ViewById(R.id.lv_content_list)
    private MeasureListView contentList;


    @Override
    protected void initView() {
        pageBack.setOnClickListener(this);
        pageTitle.setText("客户详情");
        rightText.setText("编辑");
        rightText.setTextColor(getResources().getColor(R.color.black));
        rightText.setOnClickListener(this);

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
            for (int j = 0; j < baseTitle.length && i == 0; j++) {
                CommonUIBean commonUI = new CommonUIBean();
                commonUI.setTitle(baseTitle[j]);
                commonUI.setShowText(baseLabel[j]);
                commonUIList.add(commonUI);
            }
            for (int j = 0; j < invoiceTitle.length && i == 1; j++) {
                CommonUIBean commonUI = new CommonUIBean();
                commonUI.setTitle(invoiceTitle[j]);
                commonUI.setShowText(invoiceLabel[j]);
                commonUIList.add(commonUI);
            }
            for (int j = 0; j < goodsTitle.length && i == 2; j++) {
                CommonUIBean commonUI = new CommonUIBean();
                commonUI.setTitle(goodsTitle[j]);
                commonUI.setShowText(goodsTitle[j]);
                commonUIList.add(commonUI);
            }
            for (int j = 0; j < payTitle.length && i == 3; j++) {
                CommonUIBean commonUI = new CommonUIBean();
                commonUI.setTitle(payTitle[j]);
                commonUI.setShowText(payLabel[j]);
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
            case R.id.tv_right_text:
                // toast("编辑");
                Contants.CHOOSE_MODEL_SIZE = 15;
                openActivity(CustomNewActivity.class);
                break;
        }
    }

    /*
    模拟信息
     */
    private List<OrderBean> mList;
    private String[] titles = {"基本资料", "开票信息", "收货信息", "支付信息"};
    // 基本资料
    private String[] baseTitle = {"公司名称", "联系人", "手机号", "邮箱"};
    private String[] baseLabel = {"双木衣馆", "谢廷智", "+86 13512270415", "5552415@163.com"};
    // 开票信息
    private String[] invoiceTitle = {"公司名称", "税号(P.IVA)", "PEC邮箱/SDI", "手机号", "所在地区", "详细地址"};
    private String[] invoiceLabel = {"双木衣馆服饰有限公司", "5301127134076", "双木衣馆服饰有限公司", "13512270415",
            "中国-重庆", "中国重庆市南岸区弹子石腾龙大道国际商务大厦A座22-2"};
    // 收货信息
    private String[] goodsTitle = {"公司名称", "手机号", "所在地区", "详细地址"};
    private String[] goodsLabel = {"双木衣馆服饰有限公司", "13512270415", "中国-重庆", "中国重庆市南岸区弹子石腾龙大道国际商务大厦A座22-2"};
    // 支付信息
    private String[] payTitle = {"客户类型", "支付方式", "支付年限", "备注"};
    private String[] payLabel = {"VIP会员", "支付宝", "按年付", "该客户很重要"};
}
