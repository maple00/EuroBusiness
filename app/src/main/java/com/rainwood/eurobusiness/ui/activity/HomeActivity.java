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
import com.rainwood.eurobusiness.domain.CommonUIBean;
import com.rainwood.eurobusiness.domain.GoodsDetailBean;
import com.rainwood.eurobusiness.domain.ImagesBean;
import com.rainwood.eurobusiness.domain.SpecialBean;
import com.rainwood.eurobusiness.json.JsonParser;
import com.rainwood.eurobusiness.okhttp.HttpResponse;
import com.rainwood.eurobusiness.okhttp.OnHttpListener;
import com.rainwood.eurobusiness.request.RequestPost;
import com.rainwood.eurobusiness.ui.fragment.HomeFragment;
import com.rainwood.eurobusiness.ui.fragment.PersonalFragment;
import com.rainwood.eurobusiness.utils.ListUtils;
import com.rainwood.tools.permission.OnPermission;
import com.rainwood.tools.permission.Permission;
import com.rainwood.tools.permission.XXPermissions;
import com.rainwood.tools.viewinject.ViewById;
import com.rainwood.zxingqrc.android.QRCodeCaptureActivity;

import org.json.JSONArray;
import org.json.JSONException;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * @Author: sxs797
 * @Date: 2019/12/4 15:25
 * @Desc: 首页
 */
public class HomeActivity extends BaseActivity implements View.OnClickListener, OnHttpListener {

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
            createData();
            // 识别的商品到库存商品详情页面
            showLoading("loading");
            RequestPost.getStockDetail("", result, this);
            // toast("识别的内容：" + result);
        }
    }



    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.i("sxs", "1-onDestroy");
    }

    // 再摁一次退出到桌面
    private long mExitTime;

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {         // 回到Home页
            if ((System.currentTimeMillis() - mExitTime) > 2000) {
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

    @Override
    public void onHttpFailure(HttpResponse result) {

    }

    // 商品详情
    private List<GoodsDetailBean> mList;
    // 库存信息
    private List<SpecialBean> mSpeciaList;

    @Override
    public void onHttpSucceed(HttpResponse result) {
        Map<String, String> body = JsonParser.parseJSONObject(result.body());
        if (body != null) {
            if ("1".equals(body.get("code"))) {
                // 获取库存详情
                if (result.url().contains("wxapi/v1/stock.php?type=getStockInfo")) {
                    // 规格参数
                    mSpeciaList = JsonParser.parseJSONArray(SpecialBean.class,
                            JsonParser.parseJSONObject(body.get("data")).get("skulist"));
                    // 基本信息
                    Map<String, String> goodsInfo = JsonParser.parseJSONObject(JsonParser.parseJSONObject(body.get("data")).get("goodsInfo"));
                    // 商品信息
                    for (int i = 0; i < ListUtils.getSize(mList); i++) {
                        switch (i) {
                            case 0:         // 基本信息
                                for (int j = 0; j < ListUtils.getSize(mList.get(i).getCommList()); j++) {
                                    switch (j) {
                                        case 0:
                                            mList.get(i).getCommList().get(j).setShowText(goodsInfo.get("name"));
                                            break;
                                        case 1:
                                            mList.get(i).getCommList().get(j).setShowText(goodsInfo.get("model"));
                                            break;
                                        case 2:
                                            mList.get(i).getCommList().get(j).setShowText(goodsInfo.get("goodsType"));
                                            break;
                                        case 3:
                                            mList.get(i).getCommList().get(j).setShowText(goodsInfo.get("barCode"));
                                            break;
//                                        case 4:
//                                            mList.get(i).getCommList().get(j).setShowText(goodsInfo.get("name"));
//                                            break;
                                    }
                                }
                                break;
                            case 1:         // 商品定价
                                for (int j = 0; j < ListUtils.getSize(mList.get(i).getCommList()); j++) {
                                    switch (j) {
                                        case 0:
                                            mList.get(i).getCommList().get(j).setShowText(goodsInfo.get("tradePrice"));
                                            break;
                                        case 1:
                                            mList.get(i).getCommList().get(j).setShowText(goodsInfo.get("retailPrice"));
                                            break;
                                        case 2:
                                            mList.get(i).getCommList().get(j).setShowText(goodsInfo.get("isTax"));
                                            break;
                                    }
                                }
                                break;
                            case 2:         // 规格参数
                                for (int j = 0; j < ListUtils.getSize(mList.get(i).getCommList()); j++) {
                                    switch (j) {
                                        case 0:
                                            mList.get(i).getCommList().get(j).setShowText(goodsInfo.get("goodsOption"));
                                            break;
                                        case 1:
                                            mList.get(i).getCommList().get(j).setShowText(goodsInfo.get("goodsUnit"));
                                            break;
                                        case 2:
                                            mList.get(i).getCommList().get(j).setShowText(goodsInfo.get("startNum"));
                                            break;
                                    }
                                }
                                break;
                            case 3:         // 商品图片
                                JSONArray jsonArray = JsonParser.parseJSONArrayString(JsonParser.parseJSONObject(body.get("data")).get("icolist"));
                                List<ImagesBean> imageList = new ArrayList<>();
                                for (int j = 0; j < jsonArray.length(); j++) {
                                    ImagesBean images = new ImagesBean();
                                    try {
                                        images.setSrc(jsonArray.getString(j));
                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                    }
                                    imageList.add(images);
                                }
                                mList.get(i).setImgList(imageList);
                                break;
                        }
                    }

                    postDelayed(() -> {
                        Intent intent = new Intent(HomeActivity.this, InventoryGoodsActivity.class);
                        intent.putExtra("goodsDetail", (Serializable) mList);
                        intent.putExtra("specialData", (Serializable) mSpeciaList);
                        startActivity(intent);
                    }, 100);
                }
            }else {
                toast(body.get("warn"));
            }
            if (getDialog() != null)
                dismissLoading();
        }
    }

    private String[] mainTitles = {"基本信息", "商品定价", "规格参数", "商品图片"};  //， "创建者"
    // 基本信息
    private String[] baseTitle = {"商品名称", "商品型号", "商品分类", "条码"};
    // 商品定价
    private String[] priceTitles = {"批发价", "零售价", "增值税"};
    // 规格参数
    private String[] specialTitles = {"商品尺码", "商品规格", "最小起订量", "税率"};
    private void createData() {
        mList = new ArrayList<>();
        for (int i = 0; i < mainTitles.length; i++) {
            GoodsDetailBean goodsDetail = new GoodsDetailBean();
            goodsDetail.setTitle(mainTitles[i]);
            goodsDetail.setType(i);
            goodsDetail.setLoadType(101);
            List<CommonUIBean> commLists = new ArrayList<>();
            for (int j = 0; j < baseTitle.length && i == 0; j++) {      // 基本信息
                CommonUIBean commonUI = new CommonUIBean();
                commonUI.setTitle(baseTitle[j]);
                commLists.add(commonUI);
            }
            for (int j = 0; j < priceTitles.length && i == 1; j++) {        // 商品定价
                CommonUIBean commonUI = new CommonUIBean();
                commonUI.setTitle(priceTitles[j]);
                commLists.add(commonUI);
            }
            // 规格参数
            for (int j = 0; j < specialTitles.length && i == 2; j++) {
                CommonUIBean commonUI = new CommonUIBean();
                commonUI.setTitle(specialTitles[j]);
                commLists.add(commonUI);
            }
            goodsDetail.setCommList(commLists);
            mList.add(goodsDetail);
        }
    }
}
