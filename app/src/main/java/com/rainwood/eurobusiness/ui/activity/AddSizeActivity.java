package com.rainwood.eurobusiness.ui.activity;

import android.content.Intent;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.Nullable;

import com.rainwood.eurobusiness.R;
import com.rainwood.eurobusiness.base.BaseActivity;
import com.rainwood.eurobusiness.common.Contants;
import com.rainwood.eurobusiness.json.JsonParser;
import com.rainwood.eurobusiness.okhttp.HttpResponse;
import com.rainwood.eurobusiness.okhttp.OnHttpListener;
import com.rainwood.eurobusiness.request.RequestPost;
import com.rainwood.tools.view.ClearEditText;
import com.rainwood.tools.viewinject.ViewById;

import java.util.Map;

import static com.rainwood.eurobusiness.ui.activity.NewShopActivity.SIZE_REQUEST;

/**
 * @Author: a797s
 * @Date: 2020/2/16
 * @Desc: 添加尺码
 */
public class AddSizeActivity extends BaseActivity implements View.OnClickListener, OnHttpListener {

    private String mMGoodsId;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_add_size;
    }

    @ViewById(R.id.iv_back)
    private ImageView pageBack;
    @ViewById(R.id.tv_title)
    private TextView pageTitle;
    @ViewById(R.id.tv_right_text)
    private TextView rightText;
    @ViewById(R.id.btn_save)
    private Button save;
    //
    @ViewById(R.id.et_color)
    private EditText colorEdit;
    @ViewById(R.id.iv_img)
    private ImageView ivReSize;
    @ViewById(R.id.tv_re_size)
    private TextView reSize;
    @ViewById(R.id.iv_img1)
    private ImageView ivReSize1;
    @ViewById(R.id.tv_cus_size)
    private TextView customSize;
    @ViewById(R.id.et_size)
    private EditText size;
    @ViewById(R.id.cet_inventory)
    private ClearEditText incentory;
    @ViewById(R.id.et_retail_price)
    private ClearEditText retailPrice;


    public static final int COLOR_SIZE = 0x1010;
    private boolean sizeType;           // 选择预设尺码还是自定义 --- 默认选择尺码

    @Override
    protected void initView() {
        pageBack.setOnClickListener(this);
        pageTitle.setText("添加尺码");
        rightText.setText("删除");
        rightText.setTextColor(getResources().getColor(R.color.textColor));
        rightText.setOnClickListener(this);
        save.setOnClickListener(this);
        iniEvents();
    }

    private void iniEvents() {
        colorEdit.setFocusable(false);
        colorEdit.setFocusableInTouchMode(false);
        colorEdit.setOnClickListener(this);
        ivReSize.setOnClickListener(this);
        reSize.setOnClickListener(this);
        ivReSize1.setOnClickListener(this);
        customSize.setOnClickListener(this);
        // 默认点击 -- 尺码
        size.setOnClickListener(this);
        size.setFocusableInTouchMode(false);
        size.setFocusable(false);
    }

    @Override
    protected void initData() {
        super.initData();
        mMGoodsId = getIntent().getStringExtra("mGoodsId");
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_back:
                finish();
                break;
            case R.id.et_color:
                // toast("选择颜色");
                Contants.choose_size = 0;
                // toast("去添加尺码");
                Intent colorIntent = new Intent(AddSizeActivity.this, ChooseParamsActivity.class);
                startActivityForResult(colorIntent, COLOR_SIZE);
                break;
            case R.id.iv_img:
            case R.id.tv_re_size:
                if (sizeType) {
                    sizeType = false;
                    ivReSize.setImageResource(R.drawable.radio_checked_shape);
                    ivReSize1.setImageResource(R.drawable.radio_uncheck_shape);
                }
                size.setOnClickListener(this);
                size.setFocusableInTouchMode(false);
                size.setFocusable(false);
                size.setHint("请选择");
                break;
            case R.id.iv_img1:
            case R.id.tv_cus_size:
                if (!sizeType){
                    sizeType = true;
                    ivReSize.setImageResource(R.drawable.radio_uncheck_shape);
                    ivReSize1.setImageResource(R.drawable.radio_checked_shape);
                }
                size.setOnClickListener(this);
                size.setFocusableInTouchMode(true);
                size.setFocusable(true);
                size.setHint("请输入");
                break;
            case R.id.et_size:
                Contants.choose_size = 1;
                // toast("去添加尺码");
                Intent sizeIntent = new Intent(AddSizeActivity.this, ChooseParamsActivity.class);
                startActivityForResult(sizeIntent, SIZE_REQUEST);
                break;
            case R.id.btn_save:
                // toast("保存");
                if (TextUtils.isEmpty(colorEdit.getText())) {
                    toast("请选择颜色");
                    return;
                }
                if (TextUtils.isEmpty(size.getText())) {
                    toast("请填写尺码");
                    return;
                }
                if (TextUtils.isEmpty(incentory.getText())) {
                    toast("请输入库存");
                    return;
                }
                if (TextUtils.isEmpty(retailPrice.getText())) {
                    toast("请输入价格");
                    return;
                }

                // request
                showLoading("");
                RequestPost.goodsSkuEdit("", mMGoodsId, size.getText().toString().trim(), colorEdit.getText().toString().trim(),
                        retailPrice.getText().toString().trim(), incentory.getText().toString().trim(), this);
                break;
            case R.id.tv_right_text:
                toast("删除");
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == COLOR_SIZE) {
            colorEdit.setText(data.getStringExtra("value"));
        }
        if (requestCode == SIZE_REQUEST) {
            size.setText(data.getStringExtra("value"));
        }

    }

    @Override
    public void onHttpFailure(HttpResponse result) {

    }

    @Override
    public void onHttpSucceed(HttpResponse result) {
        Map<String, String> body = JsonParser.parseJSONObject(result.body());
        if (body != null) {
            if ("1".equals(body.get("code"))) {
                // 添加规格
                if (result.url().contains("wxapi/v1/goods.php?type=goodsSkuEdit")) {
                    toast(body.get("warn"));
                    postDelayed(this::finish, 1000);
                }
            } else {
                toast(body.get("warn"));
            }
            dismissLoading();
        }
    }
}
