package com.rainwood.eurobusiness.utils;

import android.annotation.SuppressLint;
import android.os.CountDownTimer;
import android.widget.TextView;

/**
 * @Author: a797s
 * @Date: 2019/12/21 14:14
 * @Desc: 发送验证码定时器
 */
public final class CountDownTimerUtils {

    /**
     * 全局定时器变量
     */
    public static long CountDownTimerSize = -1;
    /**
     * 定时器
     */
    public static CountDownTimer countDownTimer;

    @SuppressLint("SetTextI18n")
    public static void initCountDownTimer(long totalTime, final TextView mText, final String tips) {
        countDownTimer = new CountDownTimer(totalTime * 1000, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                // 记录倒计时时间
                CountDownTimerSize = millisUntilFinished;
                mText.setText(millisUntilFinished / 1000 + "s后可重新发送");
                mText.setEnabled(false);
            }

            @Override
            public void onFinish() {
                mText.setEnabled(true);
                mText.setText(tips);
            }
        };
    }

}
