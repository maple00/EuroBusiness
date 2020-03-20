package com.rainwood.eurobusiness.ui.activity;

import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.rainwood.eurobusiness.R;
import com.rainwood.eurobusiness.base.BaseActivity;
import com.rainwood.eurobusiness.domain.SizeBean;
import com.rainwood.eurobusiness.ui.adapter.SizeAdapter;
import com.rainwood.tools.viewinject.ViewById;

import java.util.ArrayList;
import java.util.List;

/**
 * @Author: a797s
 * @Date: 2020/2/13
 * @Desc: 添加尺码
 */
public class AppendSizeActivity extends BaseActivity implements View.OnClickListener {

    @Override
    protected int getLayoutId() {
        return R.layout.activity_append_size;
    }

    @ViewById(R.id.iv_back)
    private ImageView pageBack;
    @ViewById(R.id.tv_title)
    private TextView pageTitle;
    @ViewById(R.id.tv_right_text)
    private TextView pageRightText;
    @ViewById(R.id.lv_size_list)
    private ListView sizeList;
    @ViewById(R.id.btn_add_size)
    private Button addSize;

    @Override
    protected void initView() {
        pageBack.setOnClickListener(this);
        pageTitle.setText("添加尺码");
        pageRightText.setText("保存");
        pageRightText.setTextColor(getResources().getColor(R.color.red30));
        addSize.setOnClickListener(this);
        //
        SizeAdapter sizeAdapter = new SizeAdapter(this, mList);
        sizeList.setAdapter(sizeAdapter);

    }

    @Override
    protected void initData() {
        super.initData();
        mList = new ArrayList<>();
        for (int i = 0; i < colors.length; i++) {
            SizeBean size = new SizeBean();
            size.setColor(colors[i]);
            size.setSize(sizes[i]);
            size.setRepertoryBelow(repertoryBelows[i]);
            size.setWholsePrice(whlosePrices[i]);
            size.setRepertory(repertorys[i]);
            mList.add(size);
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_back:
                finish();
                break;
            case R.id.btn_add_size:
                openActivity(AddSizeActivity.class);
                break;
        }
    }

    /*
    模拟数据
     */
    private List<SizeBean> mList;
    private String[] colors = {"枫叶红", "枫叶红", "粉色", "粉色", "粉色", "粉色"};
    private String[] sizes = {"S", "M", "S", "S", "S", "S"};
    private String[] repertoryBelows = {"10", "10", "8", "8", "8", "8"};
    private String[] whlosePrices = {"120.00€", "98.00€", "90.00€", "98.00€", "120.00€", "120.00€"};
    private String[] repertorys = {"1200", "900", "900", "900", "900", "1000"};
}
