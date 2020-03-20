package com.rainwood.eurobusiness.ui.activity;

import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import com.rainwood.eurobusiness.R;
import com.rainwood.eurobusiness.base.BaseActivity;
import com.rainwood.eurobusiness.common.Contants;
import com.rainwood.eurobusiness.utils.CountDownTimerUtils;
import com.rainwood.eurobusiness.utils.DialogUtils;
import com.rainwood.eurobusiness.utils.TipsSizeUtils;
import com.rainwood.tools.permission.OnPermission;
import com.rainwood.tools.permission.Permission;
import com.rainwood.tools.permission.XXPermissions;
import com.rainwood.tools.view.ClearEditText;
import com.rainwood.tools.view.InputTextHelper;
import com.rainwood.tools.viewinject.ViewById;

import java.util.List;

/**
 * @Author: a797s
 * @Date: 2020/2/6
 * @Desc: 手机号验证
 */
public class CheckPhoneActivity extends BaseActivity implements View.OnClickListener {

    @Override
    protected int getLayoutId() {
        return R.layout.activity_check_phone;
    }

    @ViewById(R.id.iv_back)
    private ImageView pageBack;
    @ViewById(R.id.et_check_phone)
    private ClearEditText checkPhone;
    @ViewById(R.id.btn_get_code)
    private Button getCode;

    private DialogUtils dialog;

    @Override
    protected void initView() {
        pageBack.setOnClickListener(this);
        getCode.setOnClickListener(this);
        // 设置提示文字的大小
        TipsSizeUtils.setHintSize(checkPhone, "请输入手机号", 24);
        // 设置聚焦
        showSoftInputFromWindow(checkPhone);
        // 手机号格式校验,如果输入的手机号格式不正确，则按钮置灰
        InputTextHelper.with(this)
                .addView(checkPhone)
                .setMain(getCode)
                .setListener(helper -> checkPhone.getText().toString().length() == 11).build();

        //
    }

    @Override
    protected void initData() {
        // 获取之前输入过最近的一次手机号,填写在EditTExt上
        if (Contants.PhoneCheckVerify != null) {
            checkPhone.setText(Contants.PhoneCheckVerify);
        }

        // 如果任务定时器的时间不足一分钟，则不允许再次获取验证码
        if (CountDownTimerUtils.CountDownTimerSize > 0) {
            CountDownTimerUtils.initCountDownTimer(CountDownTimerUtils.CountDownTimerSize / 1000, getCode, "可重新发送");
            CountDownTimerUtils.countDownTimer.start();
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_back:
                openActivity(LoginActivity.class);
                finish();
                break;
            case R.id.btn_get_code:
                XXPermissions.with(getActivity())
                        // 可设置被拒绝后继续申请，直到用户授权或者永久拒绝
                        .constantRequest()
                        .permission(Permission.RECEIVE_SMS, Permission.READ_SMS)    // 短信的读取权限
                        .request(new OnPermission() {
                            @Override
                            public void hasPermission(List<String> granted, boolean isAll) {
                                if (isAll) {
//                                    dialog = new DialogUtils(CheckPhoneActivity.this, "加载中");
//                                    dialog.showDialog();
                                    // 记录最新填写的手机号
                                    Contants.PhoneCheckVerify = checkPhone.getText().toString().trim();
                                    openActivity(CodeVerifyActivity.class);
                                } else {
                                    toast("权限不足，请开启");
                                }
                            }

                            @Override
                            public void noPermission(List<String> denied, boolean quick) {
                                if (quick) {
                                    toast("被永久拒绝授权，请手动授予权限");
                                    //如果是被永久拒绝就跳转到应用权限系统设置页面
                                    XXPermissions.gotoPermissionSettings(getActivity());
                                } else {
                                    toast("获取权限失败");
                                }
                            }
                        });
                break;
            default:
                break;
        }
    }
}
