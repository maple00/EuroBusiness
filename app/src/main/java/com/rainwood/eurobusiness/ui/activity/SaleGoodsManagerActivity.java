package com.rainwood.eurobusiness.ui.activity;

import android.util.SparseArray;
import android.widget.RadioGroup;

import androidx.fragment.app.Fragment;

import com.rainwood.eurobusiness.R;
import com.rainwood.eurobusiness.base.BaseActivity;
import com.rainwood.eurobusiness.ui.fragment.GoodsClassifyFragment;
import com.rainwood.eurobusiness.ui.fragment.SaleCreateFragment;
import com.rainwood.eurobusiness.ui.fragment.StoreCreateFragment;
import com.rainwood.tools.viewinject.ViewById;

/**
 * @Author: shearson
 * @Time: 2020/2/26 11:31
 * @Desc: 批发商  --- 商品管理
 */
public class SaleGoodsManagerActivity extends BaseActivity {

    @Override
    protected int getLayoutId() {
        return R.layout.activity_sale_goods_manager;
    }

    @ViewById(R.id.tabs_rg)
    private RadioGroup mTabRadioGroup;
    // fragment组
    private SparseArray<Fragment> mFragmentSparseArray;

    @Override
    protected void initView() {
        mFragmentSparseArray = new SparseArray<>();
        mFragmentSparseArray.append(R.id.rb_sale, new SaleCreateFragment());
        mFragmentSparseArray.append(R.id.rb_store, new StoreCreateFragment());
//        mFragmentSparseArray.append(R.id.rb_draft, new DraftFragment());
        mFragmentSparseArray.append(R.id.rb_classify, new GoodsClassifyFragment());

        // 默认显示批发商创建的商品
        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                mFragmentSparseArray.get(R.id.rb_sale)).commitAllowingStateLoss();
        // 逻辑切换
        mTabRadioGroup.setOnCheckedChangeListener((group, checkedId) -> {
            // 具体的fragment切换逻辑可以根据应用调整，例如使用show()/hide()
            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                    mFragmentSparseArray.get(checkedId)).commitAllowingStateLoss();
        });
    }
}
