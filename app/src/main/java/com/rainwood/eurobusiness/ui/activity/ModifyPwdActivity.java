package com.rainwood.eurobusiness.ui.activity;

import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.rainwood.eurobusiness.R;
import com.rainwood.eurobusiness.base.BaseActivity;
import com.rainwood.eurobusiness.common.Contants;
import com.rainwood.eurobusiness.json.JsonParser;
import com.rainwood.eurobusiness.okhttp.HttpResponse;
import com.rainwood.eurobusiness.okhttp.OnHttpListener;
import com.rainwood.eurobusiness.request.RequestPost;
import com.rainwood.tools.view.ClearEditText;
import com.rainwood.tools.view.CountdownView;
import com.rainwood.tools.view.InputTextHelper;
import com.rainwood.tools.view.PasswordEditText;
import com.rainwood.tools.viewinject.ViewById;

import java.util.Map;

/**
 * @Author: a797s
 * @Date: 2020/2/7
 * @Desc: 账号设置 --- 修改密码
 */
public class ModifyPwdActivity extends BaseActivity implements View.OnClickListener, OnHttpListener {

    @Override
    protected int getLayoutId() {
        return R.layout.activity_modify_pwd;
    }

    @ViewById(R.id.iv_back)
    private ImageView pageBack;
    @ViewById(R.id.tv_title)
    private TextView pageTitle;
    @ViewById(R.id.btn_confirm)
    private Button confirm;
    @ViewById(R.id.cv_send_code)
    private CountdownView sendCode;

    @ViewById(R.id.et_phone)
    private ClearEditText editPhone;
    @ViewById(R.id.et_verify)
    private ClearEditText editVerify;
    @ViewById(R.id.et_new_pwd)
    private PasswordEditText editNewPwd;
    @ViewById(R.id.et_new_pwd_again)
    private PasswordEditText editNewPwdAgain;

    @Override
    protected void initView() {
        pageBack.setOnClickListener(this);
        pageTitle.setText("修改密码");
        confirm.setOnClickListener(this);
        sendCode.setOnClickListener(this);
        // 手机号码监听
        InputTextHelper.with(this)
                .addView(editPhone)
                .setMain(sendCode)
                .setListener(helper -> editPhone.getText().toString().length() == 11).build();

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_back:
                finish();
                break;
            case R.id.cv_send_code:                 // 发送验证码
                if (TextUtils.isEmpty(editPhone.getText())) {
                    toast("请输入手机号");
                    return;
                }
                String type;
                if (Contants.userType == 0) {
                    type = "saler";
                } else {
                    type = "store";
                }
                // request
                showLoading("loading");
                RequestPost.getVerifyCode(editPhone.getText().toString().trim(), type,this);
                break;
            case R.id.btn_confirm:          // 确认修改
                if (TextUtils.isEmpty(editVerify.getText())){
                    toast("请输入验证码");
                    return;
                }
                if (TextUtils.isEmpty(editNewPwd.getText())){
                    toast("请输入新密码");
                    return;
                }
                if (TextUtils.isEmpty(editNewPwdAgain.getText())){
                    toast("请输入确认密码");
                    return;
                }
                // request
                showLoading("loading");
                RequestPost.modifyPwd(editVerify.getText().toString().trim(), editNewPwd.getText().toString().trim(),
                        editNewPwdAgain.getText().toString().trim(), this);
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
                // 获取验证码
                if (result.url().contains("wxapi/v1/login.php?type=getCode")) {
                    toast(body.get("warn"));
                }
                // 修改密码
                if (result.url().contains("wxapi/v1/index.php?type=changePwd")){
                    toast(body.get("warn"));
                    postDelayed(this::finish, 1000);
                }

            }else {
                toast(body.get("warn"));
            }
            dismissLoading();
        }
    }
}
