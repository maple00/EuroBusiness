package com.rainwood.eurobusiness.utils;

import android.text.SpannableString;
import android.text.Spanned;
import android.text.style.AbsoluteSizeSpan;
import android.widget.EditText;

import com.rainwood.tools.view.ClearEditText;

/**
 * @Author: a797s
 * @Date: 2020/2/6
 * @Desc: hint 文字大小设置
 */
public final class TipsSizeUtils {

    /**
     * 设置"用户名"提示文字的大小
     */
    public static void setHintSize(EditText text, String tips, int textSize) {
        SpannableString s = new SpannableString(tips);
        AbsoluteSizeSpan sizeSpan = new AbsoluteSizeSpan(textSize, true);
        s.setSpan(sizeSpan, 0, s.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        text.setHint(s);
    }

    public static void setClearHintSize(ClearEditText text, String tips, int textSize) {
        SpannableString s = new SpannableString(tips);
        AbsoluteSizeSpan sizeSpan = new AbsoluteSizeSpan(textSize, true);
        s.setSpan(sizeSpan, 0, s.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        text.setHint(s);
    }
}
