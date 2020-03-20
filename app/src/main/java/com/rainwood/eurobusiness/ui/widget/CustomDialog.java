package com.rainwood.eurobusiness.ui.widget;

import android.app.Dialog;
import android.content.Context;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

/**
 * @Author: shearson
 * @Time: 2020/2/28 12:51
 * @Desc: 自定义 dialog
 */
public class CustomDialog extends Dialog {
    //    style引用style样式
    public CustomDialog(Context context, int width, int height, View layout, int style) {
        super(context, style);
        setContentView(layout);
        Window window = getWindow();
        WindowManager.LayoutParams params = window.getAttributes();
        params.gravity = Gravity.CENTER;
        window.setAttributes(params);
    }
}
