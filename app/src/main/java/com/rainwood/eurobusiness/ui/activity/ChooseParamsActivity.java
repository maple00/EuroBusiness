package com.rainwood.eurobusiness.ui.activity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.rainwood.eurobusiness.R;
import com.rainwood.eurobusiness.base.BaseActivity;
import com.rainwood.eurobusiness.common.Contants;
import com.rainwood.eurobusiness.domain.PressBean;
import com.rainwood.eurobusiness.json.JsonParser;
import com.rainwood.eurobusiness.okhttp.HttpResponse;
import com.rainwood.eurobusiness.okhttp.OnHttpListener;
import com.rainwood.eurobusiness.request.RequestPost;
import com.rainwood.eurobusiness.ui.adapter.ChooseParamsAdapter;
import com.rainwood.tools.viewinject.ViewById;
import com.rainwood.tools.widget.MeasureGridView;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static com.rainwood.eurobusiness.ui.activity.AddSizeActivity.COLOR_SIZE;
import static com.rainwood.eurobusiness.ui.activity.NewShopActivity.SIZE_REQUEST;

/**
 * @Author: a797s
 * @Date: 2020/2/17
 * @Desc: 选择颜色或者 尺码
 */
public class ChooseParamsActivity extends BaseActivity implements View.OnClickListener, OnHttpListener {
    @Override
    protected int getLayoutId() {
        return R.layout.activity_choose_params;
    }

    @ViewById(R.id.iv_back)
    private ImageView pageBack;
    @ViewById(R.id.tv_title)
    private TextView pageTitle;
    @ViewById(R.id.gv_list_params)
    private MeasureGridView listParams;

    private final int INITIAL_SIZE = 0x101;
    private List<String> colors;
    private List<String> sizes;

    @Override
    protected void initView() {
        pageBack.setOnClickListener(this);
        if (Contants.choose_size == 0) {
            pageTitle.setText("选择颜色");
        }
        if (Contants.choose_size == 1) {
            pageTitle.setText("选择尺码");
        }

        // request
        showLoading("");
        RequestPost.getGoodsSizeColor(this);

    }

    @Override
    protected void initData() {
        super.initData();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_back:
                finish();
                break;
        }
    }

    @SuppressLint("HandlerLeak")
    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(@NonNull Message msg) {
            switch (msg.what) {
                case INITIAL_SIZE:
                    List<PressBean> colorsList = new ArrayList<>();
                    for (String color : colors) {
                        PressBean press = new PressBean();
                        press.setTitle(color);
                        press.setChoose(false);
                        colorsList.add(press);
                    }

                    // 选择尺寸
                    List<PressBean> sizeList = new ArrayList<>();
                    for (String size : sizes) {
                        PressBean press = new PressBean();
                        press.setTitle(size);
                        press.setChoose(false);
                        sizeList.add(press);
                    }
                    if (Contants.choose_size == 0) {
                        setParamsAdapter(colorsList);
                    }
                    if (Contants.choose_size == 1) {
                        setParamsAdapter(sizeList);
                    }
                    break;
            }
        }
    };

    /**
     * 设置 Adapter
     *
     * @param mSizeList
     */
    private void setParamsAdapter(List<PressBean> mSizeList) {
        ChooseParamsAdapter paramsAdapter = new ChooseParamsAdapter(this, mSizeList);
        listParams.setAdapter(paramsAdapter);
        listParams.setNumColumns(4);
        paramsAdapter.setOnClickItem(position -> {
            for (PressBean pressBean : mSizeList) {
                pressBean.setChoose(false);
            }
            mSizeList.get(position).setChoose(true);
            // result  request
            Intent intent = new Intent();
            intent.putExtra("value", mSizeList.get(position).getTitle());
            if (Contants.choose_size == 0){     // 选择颜色
                setResult(COLOR_SIZE, intent);
            }
            if (Contants.choose_size == 1){     // 选择尺码
                setResult(SIZE_REQUEST, intent);
            }
            finish();
        });
    }

    @Override
    public void onHttpFailure(HttpResponse result) {

    }

    @Override
    public void onHttpSucceed(HttpResponse result) {
        Map<String, String> body = JsonParser.parseJSONObject(result.body());
        if (body != null) {
            if ("1".equals(body.get("code"))) {
                // 添加规格页面数据
                if (result.url().contains("wxapi/v1/goods.php?type=getGoodsSizeColor")) {
                    JSONArray colorArray = JsonParser.parseJSONArrayString(JsonParser.parseJSONObject(body.get("data")).get("colorOption"));
                    JSONArray sizeArray = JsonParser.parseJSONArrayString(JsonParser.parseJSONObject(body.get("data")).get("sizeOption"));
                    colors = new ArrayList<>();
                    for (int i = 0; i < colorArray.length(); i++) {
                        try {
                            colors.add(colorArray.getString(i));
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                    sizes = new ArrayList<>();
                    for (int i = 0; i < sizeArray.length(); i++) {
                        try {
                            sizes.add(sizeArray.getString(i));
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }

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
