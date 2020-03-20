package com.rainwood.eurobusiness.ui.activity;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.rainwood.eurobusiness.R;
import com.rainwood.eurobusiness.base.BaseActivity;
import com.rainwood.eurobusiness.common.Contants;
import com.rainwood.eurobusiness.domain.PressBean;
import com.rainwood.eurobusiness.ui.adapter.ChooseParamsAdapter;
import com.rainwood.tools.viewinject.ViewById;
import com.rainwood.tools.widget.MeasureGridView;

import java.util.ArrayList;
import java.util.List;

/**
 * @Author: a797s
 * @Date: 2020/2/17
 * @Desc: 选择颜色或者 尺码
 */
public class ChooseParamsActivity extends BaseActivity implements View.OnClickListener {
    @Override
    protected int getLayoutId() {
        return R.layout.activity_choose_params;
    }

    @ViewById(R.id.iv_back)
    private ImageView pageBack;
    @ViewById(R.id.tv_title)
    private TextView pageTitle;
    @ViewById(R.id.gv_list_params)
    private MeasureGridView listParams;


    @Override
    protected void initView() {
        pageBack.setOnClickListener(this);
        if (Contants.choose_size == 0) {
            pageTitle.setText("选择颜色");
            setParamsAdapter(mColorsList);
        }
        if (Contants.choose_size == 1) {
            pageTitle.setText("选择尺码");

            setParamsAdapter(mSizeList);
        }
    }

    @Override
    protected void initData() {
        super.initData();
        mColorsList = new ArrayList<>();
        for (String color : colors) {
            PressBean press = new PressBean();
            press.setTitle(color);
            press.setChoose(false);
            mColorsList.add(press);
        }

        // 选择尺寸
        mSizeList = new ArrayList<>();
        for (int i = 0; i < sizes.length; i++) {
            PressBean press = new PressBean();
            press.setTitle(sizes[i]);
            press.setChoose(false);
            mSizeList.add(press);
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
     * 设置 Adapter
     *
     * @param mSizeList
     */
    private void setParamsAdapter(List<PressBean> mSizeList) {
        ChooseParamsAdapter paramsAdapter = new ChooseParamsAdapter(this, mSizeList);
        listParams.setAdapter(paramsAdapter);
        listParams.setNumColumns(4);
        paramsAdapter.setOnClickItem(position -> {
            for (PressBean pressBean : mSizeList) {
                pressBean.setChoose(false);
            }
            mSizeList.get(position).setChoose(true);
        });
    }

    /*
    模拟数据
     */
    private List<PressBean> mColorsList;
    private String[] colors = {"红色", "白色", "杏色", "墨绿色"};
    private List<PressBean> mSizeList;
    private String[] sizes = {"XS", "S", "M", "L", "XL", "XXL"};
}
