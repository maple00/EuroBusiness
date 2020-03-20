package com.rainwood.eurobusiness.ui.activity;

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
 * @Date: 2020/2/8
 * @Desc: 意见反馈
 */
public class FeedBackActivity extends BaseActivity implements View.OnClickListener {

    @Override
    protected int getLayoutId() {
        return R.layout.activity_feedback;
    }

    @ViewById(R.id.iv_back)
    private ImageView pageBack;
    @ViewById(R.id.tv_title)
    private TextView pageTitle;
    @ViewById(R.id.et_feedback_text)
    private ClearEditText feedbackText;     // 问题描述
    @ViewById(R.id.et_contact_tel)
    private ClearEditText contactTel;       // 联系电话
    @ViewById(R.id.btn_commit)
    private Button commit;                  // 提交

    @Override
    protected void initView() {
        pageBack.setOnClickListener(this);
        pageTitle.setText("意见反馈");
        commit.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_back:
                finish();
                break;
            case R.id.btn_commit:
                toast("提交完成");
                break;
        }
    }
}
