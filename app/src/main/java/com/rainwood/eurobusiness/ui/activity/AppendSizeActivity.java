package com.rainwood.eurobusiness.ui.activity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.rainwood.eurobusiness.R;
import com.rainwood.eurobusiness.base.BaseActivity;
import com.rainwood.eurobusiness.domain.SpecialBean;
import com.rainwood.eurobusiness.json.JsonParser;
import com.rainwood.eurobusiness.okhttp.HttpResponse;
import com.rainwood.eurobusiness.okhttp.OnHttpListener;
import com.rainwood.eurobusiness.request.RequestPost;
import com.rainwood.eurobusiness.ui.adapter.SizeAdapter;
import com.rainwood.eurobusiness.utils.ListUtils;
import com.rainwood.tools.viewinject.ViewById;

import java.util.List;
import java.util.Map;

import static com.rainwood.eurobusiness.ui.activity.NewShopActivity.SIZE_REQUEST;

/**
 * @Author: a797s
 * @Date: 2020/2/13
 * @Desc: 添加尺码
 */
public class AppendSizeActivity extends BaseActivity implements View.OnClickListener, OnHttpListener {

    private String mGoodsId;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_append_size;
    }

    @ViewById(R.id.iv_back)
    private ImageView pageBack;
    @ViewById(R.id.tv_title)
    private TextView pageTitle;
    @ViewById(R.id.tv_right_text)
    private TextView pageRightText;
    @ViewById(R.id.lv_size_list)
    private ListView sizeList;
    @ViewById(R.id.btn_add_size)
    private Button addSize;

    private final int INITIAL_SIZE = 0x101;
    private List<SpecialBean> mList;

    @Override
    protected void initView() {
        pageBack.setOnClickListener(this);
        pageTitle.setText("添加尺码");
        pageRightText.setText("保存");
        pageRightText.setTextColor(getResources().getColor(R.color.red30));
        pageRightText.setOnClickListener(this);
        addSize.setOnClickListener(this);

        mGoodsId = getIntent().getStringExtra("goodsId");
    }

    @Override
    protected void onResume() {
        super.onResume();
        // request
        showLoading("");
        RequestPost.getGoodsSkulist(mGoodsId, this);
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
            case R.id.btn_add_size:
                Intent intent = new Intent(this, AddSizeActivity.class);
                intent.putExtra("mGoodsId", mGoodsId);
                startActivity(intent);
                break;
            case R.id.tv_right_text:
                Intent saveIntent = new Intent();
                saveIntent.putExtra("sizeValue", "共添加了" + ListUtils.getSize(mList) + "种尺码");
                setResult(SIZE_REQUEST, saveIntent);
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
                    //
                    SizeAdapter sizeAdapter = new SizeAdapter(AppendSizeActivity.this, mList, "");
                    sizeList.setAdapter(sizeAdapter);
                    break;
            }
        }
    };

    @Override
    public void onHttpFailure(HttpResponse result) {

    }

    @Override
    public void onHttpSucceed(HttpResponse result) {
        Log.d(TAG, " --- result ---- " + result);
        Map<String, String> body = JsonParser.parseJSONObject(result.body());
        if (body != null) {
            if ("1".equals(body.get("code"))) {
                // 添加规格页面数据
                if (result.url().contains("wxapi/v1/goods.php?type=getGoodsSkulist")) {
                    mList = JsonParser.parseJSONArray(SpecialBean.class, JsonParser.parseJSONObject(body.get("data")).get("goodsSkulist"));

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
