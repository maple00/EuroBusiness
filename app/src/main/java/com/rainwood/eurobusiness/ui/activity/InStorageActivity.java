package com.rainwood.eurobusiness.ui.activity;

import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.rainwood.eurobusiness.R;
import com.rainwood.eurobusiness.base.BaseActivity;
import com.rainwood.eurobusiness.domain.PurchaseInfos;
import com.rainwood.eurobusiness.json.JsonParser;
import com.rainwood.eurobusiness.okhttp.HttpResponse;
import com.rainwood.eurobusiness.okhttp.OnHttpListener;
import com.rainwood.eurobusiness.other.BaseDialog;
import com.rainwood.eurobusiness.request.RequestPost;
import com.rainwood.eurobusiness.ui.dialog.MenuDialog;
import com.rainwood.tools.view.ClearEditText;
import com.rainwood.tools.viewinject.ViewById;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * @Author: a797s
 * @Date: 2020/2/18
 * @Desc: 采购单 -- 入库
 */
public class InStorageActivity extends BaseActivity implements View.OnClickListener, OnHttpListener {

    private String mSpecialId;
    private JSONArray mSizeOption;
    private JSONArray mColorOption;
    private PurchaseInfos mPurchaseInfos;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_in_storage;
    }

    @ViewById(R.id.iv_back)
    private ImageView pageBack;
    @ViewById(R.id.tv_title)
    private TextView pageTitle;
    @ViewById(R.id.iv_img)
    private ImageView goodsImg;
    @ViewById(R.id.tv_name)
    private TextView goodsName;
    @ViewById(R.id.tv_model)
    private TextView model;
    @ViewById(R.id.tv_discount)
    private TextView discount;
    @ViewById(R.id.tv_rate)
    private TextView rate;
    @ViewById(R.id.et_in_storage)
    private ClearEditText inStorage;
    @ViewById(R.id.et_note)
    private ClearEditText note;
    @ViewById(R.id.btn_confirm)
    private Button confirm;

    // 混装数据
    @ViewById(R.id.ll_color)
    private LinearLayout color;
    @ViewById(R.id.et_in_storage_color)
    private ClearEditText storageColor;
    @ViewById(R.id.ll_size)
    private LinearLayout size;
    @ViewById(R.id.et_in_storage_size)
    private ClearEditText storageSize;

    @Override
    protected void initView() {
        pageBack.setOnClickListener(this);
        pageTitle.setText("入库");
        confirm.setOnClickListener(this);
        storageColor.setOnClickListener(this);
        storageColor.setFocusable(false);
        storageColor.setFocusableInTouchMode(false);
        storageSize.setOnClickListener(this);
        storageSize.setFocusable(false);
        storageSize.setFocusableInTouchMode(false);

        mSpecialId = getIntent().getStringExtra("specialId");
        // request
        showLoading("loading");
        RequestPost.getInStockPage(mSpecialId, this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_back:
                finish();
                break;
            case R.id.et_in_storage_color:          // 混装选择颜色
                List<String> colorList = new ArrayList<>();
                for (int i = 0; i < mColorOption.length(); i++) {
                    try {
                        colorList.add(i, mColorOption.getString(i));
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
                bottomSelector(colorList, 1);
                break;
            case R.id.et_in_storage_size:           // 混装选择尺码
                List<String> sizeList = new ArrayList<>();
                for (int i = 0; i < mSizeOption.length(); i++) {
                    try {
                        sizeList.add(i, mSizeOption.getString(i));
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
                bottomSelector(sizeList, 2);
                break;
            case R.id.btn_confirm:
                if (TextUtils.isEmpty(inStorage.getText())) {
                    toast("请输入入库数量");
                    return;
                }
                // 如果是混装的情况
                if ("1".equals(mPurchaseInfos.getIsSku())) {
                    if (TextUtils.isEmpty(storageColor.getText()) || TextUtils.isEmpty(storageSize.getText())) {
                        toast("请填写入库颜色和入库尺码，不填写则默认为混装");
                    }
                }
                // request
                showLoading("loading");
                RequestPost.inStockEdit(mSpecialId, inStorage.getText().toString().trim(), note.getText().toString().trim(), this);
                break;
        }
    }

    /**
     * 选择规格
     * @param sizeOption
     * @param flag
     */
    private void bottomSelector(List sizeOption, int flag) {
        new MenuDialog.Builder(this)
                // 设置null 表示不显示取消按钮
                .setCancel(R.string.common_cancel)
                // 设置点击按钮后不关闭弹窗
                .setAutoDismiss(false)
                // 显示的数据
                .setList(sizeOption)
                .setCanceledOnTouchOutside(false)
                .setListener(new MenuDialog.OnListener<String>() {
                    @Override
                    public void onSelected(BaseDialog dialog, int position, String text) {
                        dialog.dismiss();
                        //toast("位置：" + position + ", 文本：" + text);
                        if (flag == 1) {         // 选择颜色
                            storageColor.setText(text);
                        }
                        if (flag == 2) {
                            storageSize.setText(text);
                        }
                    }

                    @Override
                    public void onCancel(BaseDialog dialog) {
                        dialog.dismiss();
                    }
                }).show();
    }

    @Override
    public void onHttpFailure(HttpResponse result) {

    }

    @Override
    public void onHttpSucceed(HttpResponse result) {
        Log.d(TAG, " --- result --- " + result);
        Map<String, String> body = JsonParser.parseJSONObject(result.body());
        if (body != null) {
            if ("1".equals(body.get("code"))) {
                // 提交规格入库
                if (result.url().contains("wxapi/v1/order.php?type=inStockEdit")) {
                    toast(body.get("warn"));

                    postDelayed(this::finish, 1000);
                }
                // 获取页面数据
                if (result.url().contains("wxapi/v1/order.php?type=getInStockPage")) {
                    // 颜色参数
                    mColorOption = JsonParser.parseJSONArrayString(JsonParser.parseJSONObject(body.get("data")).get("colorOption"));
                    Log.d(TAG, "color ----- " + mColorOption);
                    // 尺码参数
                    mSizeOption = JsonParser.parseJSONArrayString(JsonParser.parseJSONObject(body.get("data")).get("sizeOption"));
                    // 规格参数
                    mPurchaseInfos = JsonParser.parseJSONObject(PurchaseInfos.class, JsonParser.parseJSONObject(body.get("data")).get("info"));

                    if ("1".equals(mPurchaseInfos.getIsSku())) {  // 是混装入库
                        color.setVisibility(View.VISIBLE);
                        size.setVisibility(View.VISIBLE);

                    } else {
                        color.setVisibility(View.GONE);
                        size.setVisibility(View.GONE);
                    }
                    Glide.with(this).load(mPurchaseInfos.getIco()).into(goodsImg);
                    goodsName.setText(mPurchaseInfos.getGoodsName());
                    model.setText(mPurchaseInfos.getModel());
                    discount.setText(mPurchaseInfos.getDiscount());
                    rate.setText(mPurchaseInfos.getTaxRate());
                }
            } else {
                toast(body.get("warn"));
            }
            dismissLoading();
        }
    }
}
