package com.rainwood.eurobusiness.ui.activity;

import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import com.rainwood.eurobusiness.R;
import com.rainwood.eurobusiness.base.BaseActivity;
import com.rainwood.tools.view.PasswordEditText;
import com.rainwood.tools.viewinject.ViewById;

/**
 * @Author: a797s
 * @Date: 2020/2/6
 * @Desc: 重置密码
 */
public class ResetPassword extends BaseActivity implements View.OnClickListener {

    @Override
    protected int getLayoutId() {
        return R.layout.activity_reset_password;
    }

    @ViewById(R.id.iv_back)
    private ImageView pageBack;
    @ViewById(R.id.et_new_pwd)
    private PasswordEditText newPwd;
    @ViewById(R.id.et_new_pwd_again)
    private PasswordEditText newPwdAgain;
    @ViewById(R.id.btn_reset_confirm)
    private Button resetConfirm;

    @Override
    protected void initView() {
        pageBack.setOnClickListener(this);
        resetConfirm.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_back:
                openActivity(CodeVerifyActivity.class);
                finish();
                break;
            case R.id.btn_reset_confirm:
                openActivity(HomeActivity.class);
                break;
            default:
                break;
        }
    }
}
