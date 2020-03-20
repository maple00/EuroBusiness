package com.rainwood.eurobusiness.ui.activity;

import android.view.View;
import android.widget.ImageView;

import com.rainwood.eurobusiness.R;
import com.rainwood.eurobusiness.base.BaseActivity;
import com.rainwood.eurobusiness.domain.OrderBean;
import com.rainwood.eurobusiness.domain.WarinReBean;
import com.rainwood.eurobusiness.ui.adapter.WarnRepertoryAdapter;
import com.rainwood.tools.viewinject.ViewById;
import com.rainwood.tools.widget.MeasureListView;

import java.util.ArrayList;
import java.util.List;

/**
 * @Author: a797s
 * @Date: 2020/2/20
 * @Desc: 预警库存
 */
public class WarnRepertoryActivity extends BaseActivity implements View.OnClickListener {
    @Override
    protected int getLayoutId() {
        return R.layout.activity_warn_repertory;
    }

    @ViewById(R.id.iv_back)
    private ImageView pageBack;
    @ViewById(R.id.lv_content_list)
    private MeasureListView contentList;

    @Override
    protected void initView() {
        pageBack.setOnClickListener(this);

        WarnRepertoryAdapter repertoryAdapter = new WarnRepertoryAdapter(this, mList);
        contentList.setAdapter(repertoryAdapter);
        repertoryAdapter.setOnClickItem(position -> {
            // toast("点击了：" + position);
            openActivity(WarnRepertoryDetailActivity.class);
        });
    }

    @Override
    protected void initData() {
        super.initData();
        mList = new ArrayList<>();
        for (int i = 0; i < 3; i++) {
            WarinReBean warinRe = new WarinReBean();
            warinRe.setImgPath(null);
            warinRe.setName("西装外套式系缀扣连衣裙");
            warinRe.setModel("XDF-256165");
            warinRe.setParams("杏色/XL");
            warinRe.setInvenNum("10");
            warinRe.setReplenish("500");
            mList.add(warinRe);
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
    private List<WarinReBean> mList;
}
