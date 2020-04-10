package com.rainwood.eurobusiness.ui.activity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.rainwood.eurobusiness.R;
import com.rainwood.eurobusiness.base.BaseActivity;
import com.rainwood.eurobusiness.domain.GoodsListBean;
import com.rainwood.eurobusiness.domain.SkuBean;
import com.rainwood.eurobusiness.json.JsonParser;
import com.rainwood.eurobusiness.okhttp.HttpResponse;
import com.rainwood.eurobusiness.okhttp.OnHttpListener;
import com.rainwood.eurobusiness.request.RequestPost;
import com.rainwood.eurobusiness.ui.adapter.SpecialChioceAdapter;
import com.rainwood.tools.view.ClearEditText;
import com.rainwood.tools.viewinject.ViewById;
import com.rainwood.tools.widget.MeasureGridView;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static com.rainwood.eurobusiness.common.Contants.GOODS_NAME_REQUEST;
import static com.rainwood.eurobusiness.common.Contants.GOODS_REQUEST;
import static com.rainwood.eurobusiness.common.Contants.GOODS_SPECIAL_REQUEST;

/**
 * @Author: a797s
 * @Date: 2020/2/19
 * @Desc: 订单添加商品页面
 */
public class OrderAddShopActivity extends BaseActivity implements View.OnClickListener, OnHttpListener {

    @ViewById(R.id.iv_back)
    private ImageView pageBack;
    @ViewById(R.id.tv_title)
    private TextView pageTitle;
    @ViewById(R.id.mgv_content_list)
    private MeasureGridView contentList;
    @ViewById(R.id.btn_confirm)
    private Button confirm;
    //
    @ViewById(R.id.tv_goods_type)
    private TextView goodsType;
    @ViewById(R.id.tv_goods_name)
    private TextView goodsName;
    @ViewById(R.id.cet_pur_num)
    private ClearEditText purchaseNum;

    private GoodsListBean mGoods;
    private List<SkuBean> mSkuList;
    private List<SkuBean> mCopySkuList;

    // handler
    private final int GOODS_SPECIAL_SIZE = 0x101;       // 商品规格列表

    @Override
    protected int getLayoutId() {
        return R.layout.activity_order_add_shop;
    }

    @Override
    protected void initView() {
        pageBack.setOnClickListener(this);
        confirm.setOnClickListener(this);
        goodsType.setOnClickListener(this);
        goodsName.setOnClickListener(this);
        pageTitle.setText("添加商品");
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
            case R.id.tv_goods_type:            // 选择商品类型
                Intent intent = new Intent(this, GoodsTypeActivity.class);
                startActivityForResult(intent, GOODS_REQUEST);
                break;
            case R.id.tv_goods_name:            // 选择商品
                Intent intent1 = new Intent(this, GoodsListActivity.class);
                intent1.putExtra("goodsTypeId", goodsType.getHint().toString().trim());
                startActivityForResult(intent1, GOODS_NAME_REQUEST);
                break;
            case R.id.btn_confirm:
                if (TextUtils.isEmpty(purchaseNum.getText())) {
                    toast("请输入数量");
                    return;
                }
                int count = 0;
                mCopySkuList = new ArrayList<>();
                if (mSkuList != null)
                    for (SkuBean bean : mSkuList) {
                        if (bean.isSelected()) {
                            bean.setNum(purchaseNum.getText().toString().trim());
                            mCopySkuList.add(bean);
                            count++;
                        }
                    }
                if (count == 0) {
                    toast("请选择商品规格");
                    return;
                }
                Intent intent2 = new Intent();
                Bundle bundle = new Bundle();
                bundle.putSerializable("special", (Serializable) mCopySkuList);
                intent2.putExtra("goodsId", mGoods.getGoodsId());
                intent2.putExtras(bundle);
                setResult(GOODS_SPECIAL_REQUEST, intent2);
                finish();
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        // 获取商品分类
        if (requestCode == GOODS_REQUEST && resultCode == GOODS_REQUEST) {
            // 重新选择，重新刷新页面
            mSkuList = new ArrayList<>();
            goodsType.setText(data.getStringExtra("goodsType"));
            goodsType.setHint(data.getStringExtra("subTypeId"));
            Message msg = new Message();
            msg.what = GOODS_SPECIAL_SIZE;
            mHandler.sendMessage(msg);
        }
        // 获取商品名称
        if (requestCode == GOODS_NAME_REQUEST && resultCode == GOODS_NAME_REQUEST) {
            // 选择不同商品的时候刷新界面
            mGoods = (GoodsListBean) data.getSerializableExtra("goods");
            goodsName.setText(mGoods.getName());
            // request --- 刷新商品得规格
            showLoading("");
            RequestPost.getGoodsSpecial(mGoods.getGoodsId(), this);
        }
    }

    @SuppressLint("HandlerLeak")
    private Handler mHandler = new Handler() {
        @SuppressLint("SetTextI18n")
        @Override
        public void handleMessage(@NonNull Message msg) {
            switch (msg.what) {
                case GOODS_SPECIAL_SIZE:            // 规格
                    SpecialChioceAdapter chioceAdapter = new SpecialChioceAdapter(OrderAddShopActivity.this, mSkuList);
                    contentList.setAdapter(chioceAdapter);
                    contentList.setNumColumns(3);
                    chioceAdapter.setOnClickItem(position -> {
                        // 多选
                        mSkuList.get(position).setSelected(!mSkuList.get(position).isSelected());
                    });
                    break;
            }
        }
    };

    @Override
    public void onHttpFailure(HttpResponse result) {

    }

    @Override
    public void onHttpSucceed(HttpResponse result) {
        Log.d(TAG, "===== result ==== " + result);
        Map<String, String> body = JsonParser.parseJSONObject(result.body());
        if (body != null) {
            if ("1".equals(body.get("code"))) {
                // 获取商品规格信息
                if (result.url().contains("wxapi/v1/order.php?type=getGoodsSku")) {
                    mSkuList = JsonParser.parseJSONArray(SkuBean.class, JsonParser.parseJSONObject(body.get("data")).get("goodsSkulist"));

                    Message msg = new Message();
                    msg.what = GOODS_SPECIAL_SIZE;
                    mHandler.sendMessage(msg);
                }
            } else {
                toast(body.get("warn"));
            }
            dismissLoading();
        }
    }
}
