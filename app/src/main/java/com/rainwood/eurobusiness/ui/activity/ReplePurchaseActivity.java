package com.rainwood.eurobusiness.ui.activity;

import android.widget.ImageView;
import android.widget.TextView;

import com.rainwood.eurobusiness.R;
import com.rainwood.eurobusiness.base.BaseActivity;
import com.rainwood.tools.viewinject.ViewById;
import com.rainwood.tools.widget.MeasureGridView;
import com.rainwood.tools.widget.MeasureListView;

/**
 * @Author: a797s
 * @Date: 2020/4/2 17:59
 * @Desc: 查看补货订单明细
 */
public final class ReplePurchaseActivity extends BaseActivity {
    @Override
    protected int getLayoutId() {
        return R.layout.activity_reple_purchase;
    }

    @ViewById(R.id.iv_back)
    private ImageView mPageback;
    @ViewById(R.id.tv_title)
    private TextView mPageTitle;
    @ViewById(R.id.tv_status)
    private TextView status;
    @ViewById(R.id.iv_img)
    private ImageView mImageView;
    @ViewById(R.id.tv_name)
    private TextView goodsName;
    @ViewById(R.id.tv_model)
    private TextView model;
    @ViewById(R.id.tv_discount)
    private TextView discount;
    @ViewById(R.id.tv_rate)
    private TextView rate;
    @ViewById(R.id.mgv_content)
    private MeasureGridView specialList;
    @ViewById(R.id.mlv_content)
    private MeasureListView noteList;

    private String[] specialStrs = {"进价", "税率", "批发价", "增值税", "零售价", "最小起订量"};
    private String[] noteStr = {"交货时间", "补货数量", "入库", "备注"};

    private final int INITIAL_SIZE = 0x101;

    @Override
    protected void initView() {

    }
}
