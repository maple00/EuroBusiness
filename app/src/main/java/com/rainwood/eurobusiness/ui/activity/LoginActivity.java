package com.rainwood.eurobusiness.ui.activity;

import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.rainwood.eurobusiness.R;
import com.rainwood.eurobusiness.base.BaseActivity;
import com.rainwood.eurobusiness.common.Contants;
import com.rainwood.eurobusiness.utils.TipsSizeUtils;
import com.rainwood.tools.view.ClearEditText;
import com.rainwood.tools.view.PasswordEditText;
import com.rainwood.tools.viewinject.ViewById;

/**
 * @Author: a797s
 * @Date: 2020/2/6
 * @Desc: 登陆
 */
public class LoginActivity extends BaseActivity implements View.OnClickListener {

    @Override
    protected int getLayoutId() {
        return R.layout.activity_login;
    }

    @ViewById(R.id.et_login_account)
    private ClearEditText loginAccount;
    @ViewById(R.id.et_login_password)
    private PasswordEditText loginPwd;
    @ViewById(R.id.tv_forget_pwd)
    private TextView forgetPwd;
    @ViewById(R.id.btn_login_commit)
    private Button loginConfirm;

    @Override
    protected void initView() {
        loginConfirm.setOnClickListener(this);
        // 设置用户名的提示文字的大小
        TipsSizeUtils.setHintSize(loginAccount, "请输入用户名", 20);
        // 设置密码提示文字的大小
        TipsSizeUtils.setHintSize(loginPwd, "请输入密码", 20);
        // 忘记密码
        forgetPwd.setOnClickListener(v -> openActivity(CheckPhoneActivity.class));
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_login_commit:
                if (Contants.userType == 0){        // 供应商
                    openActivity(HomeActivity.class);
                }else {                             // 门店
                    openActivity(HomeActivity.class);
                }
                break;
            default:
                break;
        }
    }
}