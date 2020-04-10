package com.rainwood.eurobusiness.ui.activity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.util.SparseArray;
import android.view.KeyEvent;
import android.view.View;
import android.widget.RadioGroup;

import androidx.fragment.app.Fragment;

import com.rainwood.eurobusiness.R;
import com.rainwood.eurobusiness.base.BaseActivity;
import com.rainwood.eurobusiness.common.App;
import com.rainwood.eurobusiness.common.Contants;
import com.rainwood.eurobusiness.ui.fragment.HomeFragment;
import com.rainwood.eurobusiness.ui.fragment.PersonalFragment;
import com.rainwood.tools.permission.OnPermission;
import com.rainwood.tools.permission.Permission;
import com.rainwood.tools.permission.XXPermissions;
import com.rainwood.tools.viewinject.ViewById;
import com.rainwood.zxingqrc.android.QRCodeCaptureActivity;

import java.util.List;

/**
 * @Author: sxs797
 * @Date: 2019/12/4 15:25
 * @Desc: 首页
 */
public class HomeActivity extends BaseActivity implements View.OnClickListener {

    @Override
    protected int getLayoutId() {
        return R.layout.activity_home;
    }

    @ViewById(R.id.tabs_rg)
    private RadioGroup mTabRadioGroup;
    // fragment组
    private SparseArray<Fragment> mFragmentSparseArray;

    @Override
    protected void initView() {
        mFragmentSparseArray = new SparseArray<>();
        mFragmentSparseArray.append(R.id.rb_home, new HomeFragment());
        mFragmentSparseArray.append(R.id.rb_my, new PersonalFragment());

        // 默认显示首页
        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                mFragmentSparseArray.get(R.id.rb_home)).commitAllowingStateLoss();
        // 逻辑切换
        mTabRadioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                // 具体的fragment切换逻辑可以根据应用调整，例如使用show()/hide()
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                        mFragmentSparseArray.get(checkedId)).commitAllowingStateLoss();
            }
        });
        // 扫一扫
        findViewById(R.id.iv_scan).setOnClickListener(this);
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_scan:
                // 先获取相机权限
                XXPermissions.with(getActivity())
                        // 可设置被拒绝后继续申请，直到用户授权或永久拒绝
                        .constantRequest()
                        // 不指定权限则自定获取订单中的危险权限
                        .permission(Permission.CAMERA)
                        .request(new OnPermission() {
                            @Override
                            public void hasPermission(List<String> granted, boolean isAll) {
                                if (isAll) {
                                    // 去扫码
                                    Intent intent = new Intent(HomeActivity.this, QRCodeCaptureActivity.class);
                                    // 设置标题栏的颜色
                                    intent.putExtra(QRCodeCaptureActivity.STATUS_BAR_COLOR, Color.parseColor("#99000000"));
                                    startActivityForResult(intent, Contants.SCANCHECKCODE);
                                } else {
                                    toast("获取权限成功，部分权限未正常授予");
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
                throw new IllegalStateException("Unexpected value: " + v.getId());
        }
    }

    // 重写fragment中 的onActivityResult
    PersonalFragment personalFragment = new PersonalFragment();

    //获取扫码结果
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
//        personalFragment.onActivityResult(requestCode, resultCode, data);       // 会不会是这里的原因
        if (requestCode == Contants.SCANCHECKCODE && resultCode == RESULT_OK) {
            Bundle bundle = data.getExtras();
            String result = null;
            if (bundle != null) {
                result = bundle.getString(QRCodeCaptureActivity.CODE_CONTENT);
            }
            if (TextUtils.isEmpty(result)) {
                toast("未识别到任何内容");
                return;
            }
            toast("识别的内容：" + result);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.i("sxs","1-onDestroy");
    }

    private long mExitTime;

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {         // 回到Home页
            if ((System.currentTimeMillis() - mExitTime) > 2000) {  // 间隔大于2s
                toast("再按一次退出到桌面");
                mExitTime = System.currentTimeMillis();
                return false;
            } else {
                App.backHome(this);
                return true;
            }
        }
        return super.onKeyDown(keyCode, event);
    }
}
