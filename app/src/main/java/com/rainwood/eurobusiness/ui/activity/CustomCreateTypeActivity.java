package com.rainwood.eurobusiness.ui.activity;

import android.annotation.SuppressLint;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.rainwood.eurobusiness.R;
import com.rainwood.eurobusiness.base.BaseActivity;
import com.rainwood.eurobusiness.domain.CustomTypeDetailBean;
import com.rainwood.eurobusiness.json.JsonParser;
import com.rainwood.eurobusiness.okhttp.HttpResponse;
import com.rainwood.eurobusiness.okhttp.OnHttpListener;
import com.rainwood.eurobusiness.request.RequestPost;
import com.rainwood.tools.view.ClearEditText;
import com.rainwood.tools.viewinject.ViewById;

import java.util.Map;

/**
 * @Author: a797s
 * @Date: 2020/4/13 18:23
 * @Desc: 客户分类 ---- 新增分类
 */
public final class CustomCreateTypeActivity extends BaseActivity implements View.OnClickListener, OnHttpListener {

    @ViewById(R.id.iv_back)
    private ImageView mPageBack;
    @ViewById(R.id.tv_title)
    private TextView mPageTitle;
    @ViewById(R.id.cet_custom_type)
    private ClearEditText customType;
    @ViewById(R.id.cet_discount)
    private ClearEditText discount;
    @ViewById(R.id.cet_order)
    private ClearEditText order;
    @ViewById(R.id.btn_confirm)
    private Button confirm;
    private String mTypeId;

    private final int INITIAL_SIZE = 0x101;
    private CustomTypeDetailBean mTypeDetail;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_custom_create_type;
    }

    @Override
    protected void initView() {
        mPageBack.setOnClickListener(this);
        confirm.setOnClickListener(this);
        mPageTitle.setText("新增分类");

        mTypeId = getIntent().getStringExtra("typeId");
        if (mTypeId != null){
            showLoading("");
            RequestPost.getCustomTypeDetail(mTypeId, this);
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_back:
                finish();
                break;
            case R.id.btn_confirm:
                if (TextUtils.isEmpty(customType.getText())) {
                    toast("请输入客户分类");
                    return;
                }
                if (TextUtils.isEmpty(discount.getText())) {
                    toast("请输入折扣");
                    return;
                }
                // TODO
                showLoading("");
                RequestPost.customTypeEdit(mTypeId == null ? "" : mTypeId, customType.getText().toString().trim(),
                        discount.getText().toString().trim(), order.getText().toString().trim(), this);
                break;
        }
    }

    @SuppressLint("HandlerLeak")
    private Handler mHandler = new Handler(){
        @Override
        public void handleMessage(@NonNull Message msg) {
            switch (msg.what){
                case INITIAL_SIZE:
                    customType.setText(mTypeDetail.getName());
                    discount.setText(mTypeDetail.getDiscount());
                    order.setText(mTypeDetail.getList());
                    break;
            }
        }
    };


    @Override
    public void onHttpFailure(HttpResponse result) {

    }

    @Override
    public void onHttpSucceed(HttpResponse result) {
        Map<String, String> body = JsonParser.parseJSONObject(result.body());
        if (body != null) {
            if ("1".equals(body.get("code"))) {
                // 新增客户
                if (result.url().contains("wxapi/v1/client.php?type=kehuTypeEdit")) {
                    toast(body.get("warn"));
                    postDelayed(this::finish, 500);
                }
                // 获取客户详情
                if (result.url().contains("wxapi/v1/client.php?type=getKehuTypeInfo")){
                    mTypeDetail = JsonParser.parseJSONObject(CustomTypeDetailBean.class, JsonParser.parseJSONObject(body.get("data")).get("info"));

                    Message msg = new Message();
                    msg.what = INITIAL_SIZE;
                    mHandler.sendMessage(msg);
                }
            } else {
                toast(body.get("warn"));
            }
            dismissLoading();
        }
    }
}
