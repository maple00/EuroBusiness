package com.rainwood.eurobusiness.ui.activity;

import android.annotation.SuppressLint;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.rainwood.eurobusiness.R;
import com.rainwood.eurobusiness.base.BaseActivity;
import com.rainwood.tools.view.ClearEditText;
import com.rainwood.tools.viewinject.ViewById;

/**
 * @Author: a797s
 * @Date: 2020/2/18
 * @Desc: 采购单 -- 入库
 */
public class InStorageActivity extends BaseActivity implements View.OnClickListener {

    @Override
    protected int getLayoutId() {
        return R.layout.activity_in_storage;
    }

    @ViewById(R.id.iv_back)
    private ImageView pageBack;
    @ViewById(R.id.tv_title)
    private TextView pageTitle;
    @ViewById(R.id.iv_img)
    private ImageView goodsImg;
    @ViewById(R.id.tv_name)
    private TextView goodsName;
    @ViewById(R.id.tv_model)
    private TextView model;
    @ViewById(R.id.tv_discount)
    private TextView discount;
    @ViewById(R.id.tv_rate)
    private TextView rate;
    @ViewById(R.id.et_in_storage)
    private ClearEditText inStorage;
    @ViewById(R.id.et_note)
    private ClearEditText note;
    @ViewById(R.id.btn_confirm)
    private Button confirm;

    @Override
    protected void initView() {
        pageBack.setOnClickListener(this);
        pageTitle.setText("入库");
        confirm.setOnClickListener(this);
        // 初始化
        initContent();
    }

    @SuppressLint("SetTextI18n")
    private void initContent() {
        goodsImg.setImageResource(R.drawable.icon_loadding_fail);
        goodsName.setText("西装外套式系缀扣连衣裙");
        model.setText("XDF-256165");
        discount.setText("25%折扣");
        rate.setText("16%税率");

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_back:
                finish();
                break;
            case R.id.btn_confirm:
                toast("确定：" + inStorage.getText().toString() + " ---- " + note.getText().toString());
                break;
        }
    }
}
