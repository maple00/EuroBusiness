package com.rainwood.eurobusiness.ui.activity;

import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.rainwood.eurobusiness.R;
import com.rainwood.eurobusiness.base.BaseActivity;
import com.rainwood.eurobusiness.common.Contants;
import com.rainwood.eurobusiness.domain.CommonUIBean;
import com.rainwood.eurobusiness.ui.adapter.AddSizeAdapter;
import com.rainwood.tools.viewinject.ViewById;
import com.rainwood.tools.widget.MeasureListView;

import java.util.ArrayList;
import java.util.List;

/**
 * @Author: a797s
 * @Date: 2020/2/16
 * @Desc: 添加尺码
 */
public class AddSizeActivity extends BaseActivity implements View.OnClickListener {

    @Override
    protected int getLayoutId() {
        return R.layout.activity_add_size;
    }

    @ViewById(R.id.iv_back)
    private ImageView pageBack;
    @ViewById(R.id.tv_title)
    private TextView pageTitle;
    @ViewById(R.id.tv_right_text)
    private TextView rightText;
    @ViewById(R.id.lv_add_size)
    private MeasureListView addSize;
    @ViewById(R.id.btn_save)
    private Button save;

    @Override
    protected void initView() {
        pageBack.setOnClickListener(this);
        pageTitle.setText("添加尺码");
        rightText.setText("删除");
        rightText.setTextColor(getResources().getColor(R.color.textColor));
        rightText.setOnClickListener(this);
        save.setOnClickListener(this);
        // 添加尺码
        AddSizeAdapter sizeAdapter = new AddSizeAdapter(this, mList);
        addSize.setAdapter(sizeAdapter);
        sizeAdapter.setOnClickEditText(new AddSizeAdapter.OnClickEditText() {
            @Override
            public void onClickColor(int position) {
                 // toast("选择颜色");
                 Contants.choose_size = 0;
                 openActivity(ChooseParamsActivity.class);
            }

            @Override
            public void onClickPreSize(int position) {      // 预设尺寸
                // toast("预设尺寸");
                mList.get(position).setLabel("请选择");
                mList.get(position).setType(0);
            }

            @Override
            public void onClickChooseSize(int position) {
                 Contants.choose_size = 1;
                 openActivity(ChooseParamsActivity.class);
            }

            @Override
            public void onClickCusSize(int position) {      // 自定义尺寸
                mList.get(position).setType(1);
                mList.get(position).setLabel("请输入");
            }
        });
    }

    @Override
    protected void initData() {
        super.initData();
        mList = new ArrayList<>();
        for (int i = 0; i < titles.length; i++) {
            CommonUIBean commonUI = new CommonUIBean();
            commonUI.setTitle(titles[i]);
            commonUI.setLabel(hints[i]);
            mList.add(commonUI);
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_back:
                finish();
                break;
            case R.id.btn_save:
                toast("保存");
                break;
            case R.id.tv_right_text:
                toast("删除");
                break;
        }
    }

    /*
    模拟数据
     */
    private List<CommonUIBean> mList;
    private String[] titles = {"颜色", "尺码", "库存", "库存下限", "批发价"};
    private String[] hints = {"请选择", "请选择", "请输入", "请输入", "请输入"};
}
