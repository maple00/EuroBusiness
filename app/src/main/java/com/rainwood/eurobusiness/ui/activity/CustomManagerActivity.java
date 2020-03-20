package com.rainwood.eurobusiness.ui.activity;

import android.annotation.SuppressLint;
import android.os.Handler;
import android.os.Message;
import android.text.Html;
import android.view.View;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.rainwood.eurobusiness.R;
import com.rainwood.eurobusiness.base.BaseActivity;
import com.rainwood.eurobusiness.common.Contants;
import com.rainwood.eurobusiness.domain.CustomBean;
import com.rainwood.eurobusiness.domain.ItemGridBean;
import com.rainwood.eurobusiness.ui.adapter.CustomAdapter;
import com.rainwood.eurobusiness.ui.adapter.LevelTypeAdapter;
import com.rainwood.tools.viewinject.ViewById;
import com.rainwood.tools.widget.MeasureListView;

import java.util.ArrayList;
import java.util.List;

/**
 * @Author: a797s
 * @Date: 2020/2/22
 * @Desc: 客户管理
 */
public class CustomManagerActivity extends BaseActivity implements View.OnClickListener {
    @Override
    protected int getLayoutId() {
        return R.layout.activity_custom_manager;
    }

    @ViewById(R.id.iv_back)
    private ImageView pageBack;
    @ViewById(R.id.tv_manager_type)
    private TextView managerType;
    @ViewById(R.id.gv_custom_spinner)
    private GridView customSpinner;
    @ViewById(R.id.tv_all_money)
    private TextView allMoney;
    @ViewById(R.id.lv_content_list)
    private MeasureListView contentList;
    @ViewById(R.id.btn_new_custom)
    private Button newCustom;

    @Override
    protected void initView() {
        pageBack.setOnClickListener(this);
        managerType.setOnClickListener(this);
        newCustom.setOnClickListener(this);
        allMoney.setText(Html.fromHtml("<font color=" + getResources().getColor(R.color.fontColor) + " size='12px'>订单总额：</font>"
                + "<font color=" + getResources().getColor(R.color.red30) + " size='15px'>" + "156.00€" + "</font>"));
        if (Contants.CHOOSE_MODEL_SIZE == 10) {
            // 客户分类
            LevelTypeAdapter typeAdapter = new LevelTypeAdapter(this, customTypeList);
            customSpinner.setAdapter(typeAdapter);
            customSpinner.setNumColumns(GridView.AUTO_FIT);
            // content
            CustomAdapter customAdapter = new CustomAdapter(this, mList);
            contentList.setAdapter(customAdapter);
            customAdapter.setOnClickContent(new CustomAdapter.OnClickContent() {
                @Override
                public void onClickContent(int position) {
                    openActivity(CustomDetailActivity.class);
                }

                @Override
                public void onClickDelete(int position) {

                }
            });
        }

        if (Contants.CHOOSE_MODEL_SIZE == 107){
            // 客户分类
            LevelTypeAdapter typeAdapter = new LevelTypeAdapter(this, customTypeList);
            customSpinner.setAdapter(typeAdapter);
            customSpinner.setNumColumns(GridView.AUTO_FIT);
            // content
            CustomAdapter customAdapter = new CustomAdapter(this, mList);
            contentList.setAdapter(customAdapter);
            customAdapter.setOnClickContent(new CustomAdapter.OnClickContent() {
                @Override
                public void onClickContent(int position) {
                    openActivity(CustomDetailActivity.class);
                }

                @Override
                public void onClickDelete(int position) {
                    toast("删除该门店");
                }
            });
        }
    }

    @Override
    protected void initData() {
        super.initData();
        // 客户分类 Spinner
        customTypeList = new ArrayList<>();
        for (int i = 0; i < types.length && Contants.CHOOSE_MODEL_SIZE == 10; i++) {
            ItemGridBean itemGrid = new ItemGridBean();
            itemGrid.setItemName(types[i]);
            customTypeList.add(itemGrid);
        }

        for (int i = 0; i < topTypes.length && Contants.CHOOSE_MODEL_SIZE == 107; i++) {
            ItemGridBean itemGrid = new ItemGridBean();
            itemGrid.setItemName(topTypes[i]);
            customTypeList.add(itemGrid);
        }

        // content
        mList = new ArrayList<>();
        for (int i = 0; i < 3; i++) {
            CustomBean custom = new CustomBean();
            if (Contants.CHOOSE_MODEL_SIZE == 10){
                custom.setUiType(0);
            }
            if (Contants.CHOOSE_MODEL_SIZE == 107){
                custom.setUiType(1);
            }
            custom.setLogoPath(null);
            custom.setName("BETTY RILEY");
            if (i == 2) {
                custom.setType("普通客户");
            } else {
                custom.setType("VIP会员");
            }
            custom.setGather("156.00€");
            mList.add(custom);
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_back:
                finish();
                break;
            case R.id.tv_manager_type:              // 管理分类
                openActivity(CustomTypeActivity.class);
                break;
            case R.id.btn_new_custom:               // 新增客户
                openActivity(CustomNewActivity.class);
                break;
        }
    }

    @SuppressLint("HandlerLeak")
    private Handler mHandler = new Handler(){
        @Override
        public void handleMessage(@NonNull Message msg) {
            super.handleMessage(msg);
            switch (msg.what){

            }
        }
    };

    /*
    模拟数据
     */
    private List<ItemGridBean> customTypeList;
    private String[] types = {"客户分类"};
    private List<CustomBean> mList;
    // 批发商
    private String[] topTypes = {"客户分类", "门店"};

}
