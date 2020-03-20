package com.rainwood.eurobusiness.ui.activity;

import android.annotation.SuppressLint;
import android.text.Html;
import android.view.View;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.rainwood.eurobusiness.R;
import com.rainwood.eurobusiness.base.BaseActivity;
import com.rainwood.eurobusiness.common.Contants;
import com.rainwood.eurobusiness.domain.CommonUIBean;
import com.rainwood.eurobusiness.ui.adapter.UnPayAdapter;
import com.rainwood.tools.common.FontDisplayUtil;
import com.rainwood.tools.viewinject.ViewById;

import java.util.ArrayList;
import java.util.List;

/**
 * @Author: shearson
 * @Time: 2020/2/24 21:24
 * @Desc: 未收款项 和未付款项
 */
public class UnPaymentActivity extends BaseActivity implements View.OnClickListener {

    @Override
    protected int getLayoutId() {
        return R.layout.activity_un_payment;
    }

    @ViewById(R.id.iv_back)
    private ImageView pageBack;
    @ViewById(R.id.tv_title)
    private TextView pageTitle;
    @ViewById(R.id.tv_all_money)
    private TextView allMoney;
    @ViewById(R.id.lv_content_list)
    private ListView contentList;

    @SuppressLint("SetTextI18n")
    @Override
    protected void initView() {
        pageBack.setOnClickListener(this);
        if(Contants.CHOOSE_MODEL_SIZE == 115){         // 未付款项
            pageTitle.setText("未付款项");
        }
        if (Contants.CHOOSE_MODEL_SIZE == 116){        // 未收款项
            pageTitle.setText("未收款项");
        }//
        allMoney.setText(Html.fromHtml("<font color='" + R.color.textColor
                + "' size='"+ FontDisplayUtil.dip2px(this, 13) +"'>合计：</font>"
                + "<font color='" + R.color.red30 + "'size='"+ FontDisplayUtil.dip2px(this, 17)
                +"'><b>" + "8000.00€" + "</b></font>"));

        UnPayAdapter payAdapter = new UnPayAdapter(this, mList);
        contentList.setAdapter(payAdapter);
    }

    @Override
    protected void initData() {
        super.initData();
        mList = new ArrayList<>();
        for (int i = 0; i < titles.length; i++) {
            CommonUIBean commonUI = new CommonUIBean();
            commonUI.setTitle(titles[i]);
            commonUI.setShowText(data[i]);
            mList.add(commonUI);
        }
    }

    @Override
    public void onClick(View v) {

    }

    /*
    模拟数据
     */
    private List<CommonUIBean> mList;
    private String[] titles = {"双木衣馆", "李雅琪", "李雅琪"};
    private String[] data = {"7000.00€","400.00€", "600.00€"};
}