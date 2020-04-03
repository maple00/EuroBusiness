package com.rainwood.eurobusiness.ui.activity;

import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import com.rainwood.eurobusiness.R;
import com.rainwood.eurobusiness.base.BaseActivity;
import com.rainwood.eurobusiness.json.JsonParser;
import com.rainwood.eurobusiness.okhttp.HttpResponse;
import com.rainwood.eurobusiness.okhttp.OnHttpListener;
import com.rainwood.eurobusiness.request.RequestPost;
import com.rainwood.tools.view.PasswordEditText;
import com.rainwood.tools.viewinject.ViewById;

import java.util.Map;

/**
 * @Author: a797s
 * @Date: 2020/2/6
 * @Desc: 重置密码
 */
public class ResetPassword extends BaseActivity implements View.OnClickListener, OnHttpListener {

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
                if (TextUtils.isEmpty(newPwd.getText())) {
                    toast("请输入新密码");
                    return;
                }
                if (TextUtils.isEmpty(newPwdAgain.getText())) {
                    toast("请输入确认密码");
                    return;
                }
                // request
                showLoading("");
                RequestPost.resetPwd(newPwd.getText().toString().trim(), newPwdAgain.getText().toString().trim(),
                        this);
                break;
            default:
                break;
        }
    }

    @Override
    public void onHttpFailure(HttpResponse result) {

    }

    @Override
    public void onHttpSucceed(HttpResponse result) {
        Map<String, String> body = JsonParser.parseJSONObject(result.body());
        if (body != null) {
            if ("1".equals(body.get("code"))) {
                // 重置密码
                if (result.url().contains("wxapi/v1/login.php?type=changePwd")) {
                    toast(body.get("warn"));
                    postDelayed(() -> openActivity(HomeActivity.class), 500);
                }
            } else {
                toast(body.get("warn"));
            }
            dismissLoading();
        }
    }
}
