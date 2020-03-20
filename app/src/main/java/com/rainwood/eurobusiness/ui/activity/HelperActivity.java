package com.rainwood.eurobusiness.ui.activity;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.rainwood.eurobusiness.R;
import com.rainwood.eurobusiness.base.BaseActivity;
import com.rainwood.tools.viewinject.ViewById;

/**
 * @Author: a797s
 * @Date: 2020/2/7
 * @Desc: 帮助中心
 */
public class HelperActivity extends BaseActivity implements View.OnClickListener {

    @Override
    protected int getLayoutId() {
        return R.layout.activity_helper;
    }

    @ViewById(R.id.iv_back)
    private ImageView pageBack;
    @ViewById(R.id.tv_title)
    private TextView pageTitle;
    @ViewById(R.id.iv_helper)
    private ImageView helperImg;
    @ViewById(R.id.tv_helper)
    private TextView helperText;

    @Override
    protected void initView() {
        pageBack.setOnClickListener(this);
        pageTitle.setText("帮助中心");

        helperText.setText("这里是帮助中心的文字描述这里是帮助中心的文字描述这里是帮助中心的文字描述这里是帮助中心的文字描述这里是帮助中心的文字描述这里是帮助中心的文字描述这里是帮助中心的文字描述这里是帮助描述这里是帮助中心的文字描述这里是帮助中心的文字描述这里是帮助中心的文字描述这里是帮助中心的文字描述这里是帮助中心的文字描述中心的文字描述这里是帮助中心的文字描述这里是帮助中心的文字描述");
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_back:
                finish();
                break;
        }
    }
}
