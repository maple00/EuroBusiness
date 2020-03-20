package com.rainwood.eurobusiness.ui.activity;

import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.rainwood.eurobusiness.R;
import com.rainwood.eurobusiness.base.BaseActivity;
import com.rainwood.eurobusiness.domain.CommonUIBean;
import com.rainwood.eurobusiness.utils.TipsSizeUtils;
import com.rainwood.tools.view.ClearEditText;
import com.rainwood.tools.view.PasswordEditText;
import com.rainwood.tools.viewinject.ViewById;

import java.util.ArrayList;
import java.util.List;

/**
 * @Author: a797s
 * @Date: 2020/2/7
 * @Desc: 账号设置 --- 修改密码
 */
public class ModifyPwdActivity extends BaseActivity implements View.OnClickListener {

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

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_back:
                finish();
                break;
            case R.id.btn_confirm:
                toast("修改成功");
                break;
            default:
                break;
        }
    }

}
