package com.rainwood.eurobusiness.ui.activity;

import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.rainwood.eurobusiness.R;
import com.rainwood.eurobusiness.base.BaseActivity;
import com.rainwood.eurobusiness.utils.CountDownTimerUtils;
import com.rainwood.tools.viewinject.ViewById;

/**
 * @Author: a797s
 * @Date: 2020/2/6
 * @Desc: 输入手机验证码
 */
public class CodeVerifyActivity extends BaseActivity implements OnClickListener {

    @Override
    protected int getLayoutId() {
        return R.layout.activity_code_verify;
    }

    // 返回
    @ViewById(R.id.iv_back)
    private ImageView pageBack;
    // 验证码
    @ViewById(R.id.tv_verify_countdown)
    private TextView verifyCountDown;
    @ViewById(R.id.ed_code1)
    private EditText editText1;
    @ViewById(R.id.ed_code2)
    private EditText editText2;
    @ViewById(R.id.ed_code3)
    private EditText editText3;
    @ViewById(R.id.ed_code4)
    private EditText editText4;
    @ViewById(R.id.ed_code5)
    private EditText editText5;
    @ViewById(R.id.ed_code6)
    private EditText editText6;
    // 确定
    @ViewById(R.id.btn_confirm)
    private Button confirm;

    @Override
    protected void initView() {
        pageBack.setOnClickListener(this);
        confirm.setOnClickListener(this);
        // 初始化监听EditText
        initListener();
        // 初始化定时器
        CountDownTimerUtils.initCountDownTimer(60, verifyCountDown, "重新发送验证码");
        CountDownTimerUtils.countDownTimer.start();
        verifyCountDown.addTextChangedListener(new TextWatcher() {          // 监听倒计时是否完毕
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable mText) {
                if ("重新发送验证码".contentEquals(mText)) {    // 计时器完毕
                    verifyCountDown.setOnClickListener(CodeVerifyActivity.this);
                }
            }
        });
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_back:
                openActivity(CheckPhoneActivity.class);
                finish();
                break;
            case R.id.btn_confirm:
                openActivity(ResetPassword.class);
                break;
            default:
                break;
        }
    }


    int index = 1;
    private EditText[] mArray;

    /**
     * 输入的验证码的监听
     */
    private void initListener() {
        mArray = new EditText[]{editText1, editText2, editText3, editText4, editText5, editText6};
        for (int i = 0; i < mArray.length; i++) {
            final int j = i;
            mArray[j].addTextChangedListener(new TextWatcher() {
                private CharSequence temp;

                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {
                    temp = s;
                }

                @Override
                public void afterTextChanged(Editable s) {
                    if (temp.length() == 1 && j < mArray.length - 1) {
                        mArray[j + 1].setFocusable(true);
                        mArray[j + 1].setFocusableInTouchMode(true);
                        mArray[j + 1].requestFocus();
                    }

                    if (temp.length() == 0) {
                        if (j >= 1) {
                            mArray[j - 1].setFocusable(true);
                            mArray[j - 1].setFocusableInTouchMode(true);
                            mArray[j - 1].requestFocus();
                        }
                    }
                    checkNumber();
                }
            });
        }
    }

    /**
     * 检查验证码
     */
    private void checkNumber() {
        if (!TextUtils.isEmpty(editText1.getText().toString().trim()) && !TextUtils.isEmpty(editText2.getText().toString().trim())
                && !TextUtils.isEmpty(editText3.getText().toString().trim()) && !TextUtils.isEmpty(editText4.getText().toString().trim())
                && !TextUtils.isEmpty(editText5.getText().toString().trim()) && !TextUtils.isEmpty(editText6.getText().toString().trim())) {

            // 获取输入的验证码
            toast("输入的验证码: " + editText1.getText().toString().trim() + editText2.getText().toString().trim() +
                    editText3.getText().toString().trim() + editText4.getText().toString().trim()
                    + editText5.getText().toString().trim() + editText6.getText().toString().trim());
            index++;
            if (index % 2 == 0) {
                for (EditText editText : mArray) {
                    editText.setText("");
                }
                editText1.setFocusable(true);
                editText1.setFocusableInTouchMode(true);
                editText1.requestFocus();
            }
        }
    }
}
