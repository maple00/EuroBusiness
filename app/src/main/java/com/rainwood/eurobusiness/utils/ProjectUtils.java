package com.rainwood.eurobusiness.utils;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;

import com.rainwood.tools.permission.OnPermission;
import com.rainwood.tools.permission.Permission;
import com.rainwood.tools.permission.XXPermissions;
import com.rainwood.zxingqrc.android.QRCodeCaptureActivity;

import java.util.List;

/**
 * @Author: a797s
 * @Date: 2020/2/18
 * @Desc: 项目工具类
 */
public final class ProjectUtils {

    /**
     * 打开扫一扫
     */
    public static void openScreening(Activity activity, Context context) {
        // 先获取相机权限
        XXPermissions.with(activity)
                // 可设置被拒绝后继续申请，直到用户授权或永久拒绝
                .constantRequest()
                // 不指定权限则自定获取订单中的危险权限
                .permission(Permission.CAMERA)
                .request(new OnPermission() {
                    @Override
                    public void hasPermission(List<String> granted, boolean isAll) {
                        if (isAll) {
                            // 去扫码
                            Intent intent = new Intent(context, QRCodeCaptureActivity.class);
                            // 设置标题栏的颜色
                            intent.putExtra(QRCodeCaptureActivity.STATUS_BAR_COLOR, Color.parseColor("#99000000"));
                            //  startActivityForResult(intent, Contants.SCANCHECKCODE);
                        } else {
                            // toast("获取权限成功，部分权限未正常授予");
                        }
                    }

                    @Override
                    public void noPermission(List<String> denied, boolean quick) {
                        if (quick) {
                            // toast("被永久拒绝授权，请手动授予权限");
                            //如果是被永久拒绝就跳转到应用权限系统设置页面
                            XXPermissions.gotoPermissionSettings(activity);
                        } else {
                            // toast("获取权限失败");
                        }
                    }
                });
    }
}
