package com.rainwood.eurobusiness.ui.activity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.rainwood.eurobusiness.R;
import com.rainwood.eurobusiness.base.BaseActivity;
import com.rainwood.eurobusiness.domain.SkuBean;
import com.rainwood.eurobusiness.domain.SupplierBean;
import com.rainwood.eurobusiness.json.JsonParser;
import com.rainwood.eurobusiness.okhttp.HttpResponse;
import com.rainwood.eurobusiness.okhttp.OnHttpListener;
import com.rainwood.eurobusiness.other.BaseDialog;
import com.rainwood.eurobusiness.request.RequestPost;
import com.rainwood.eurobusiness.ui.adapter.SpecialChioceAdapter;
import com.rainwood.eurobusiness.ui.dialog.MenuDialog;
import com.rainwood.tools.view.ClearEditText;
import com.rainwood.tools.viewinject.ViewById;
import com.rainwood.tools.widget.FlowLayout;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static com.rainwood.eurobusiness.common.Contants.GOODS_SPECIAL_REQUEST;

/**
 * @Author: a797s
 * @Date: 2020/4/8 11:52
 * @Desc: 添加商品
 */
public final class AddNewGoodsActivity extends BaseActivity implements View.OnClickListener, OnHttpListener {

    @ViewById(R.id.iv_back)
    private ImageView mPageBack;
    @ViewById(R.id.tv_title)
    private TextView mPageTitle;
//    @ViewById(R.id.fl_special)
//    private FlowLayout special;
    @ViewById(R.id.gv_special)
    private GridView special;
    @ViewById(R.id.tv_supply_store)
    private TextView supplyStore;
    @ViewById(R.id.cet_pur_num)
    private ClearEditText purchaseNum;
    @ViewById(R.id.btn_confirm)
    private Button confirm;

    private List<String> mSupplyList;           // 供应商列表
    private List<SkuBean> mSkuList;             // 流式规格
    private List<SkuBean> mCopySkuList;             // copy规格


    private final int INITIAL_SIZE = 0x101;
    private List<SupplierBean> mSupplierList;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_new_goods;
    }

    @Override
    protected void initView() {
        initEvents();
        mPageTitle.setText("添加商品");
        String goodsId = getIntent().getStringExtra("goodsId");
        // request -- 查询商品的规格以及门店信息
        showLoading("");
        RequestPost.getGoodsSpecial(goodsId, this);
    }

    private void initEvents() {
        mPageBack.setOnClickListener(this);
        supplyStore.setOnClickListener(this);
        confirm.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_back:
                finish();
                break;
            case R.id.tv_supply_store:
                new MenuDialog.Builder(this)
                        .setCancel(R.string.common_cancel)
                        .setAutoDismiss(false)
                        .setList(mSupplyList)
                        .setCanceledOnTouchOutside(false)
                        .setListener(new MenuDialog.OnListener<String>() {
                            @Override
                            public void onSelected(BaseDialog dialog, int position, String text) {
                                dialog.dismiss();
                                supplyStore.setText(text);
                            }

                            @Override
                            public void onCancel(BaseDialog dialog) {
                                dialog.dismiss();
                            }
                        }).show();
                break;
            case R.id.btn_confirm:          // 确认
                if (TextUtils.isEmpty(purchaseNum.getText())){
                    toast("请输入数量");
                    return;
                }
                int count = 0;
                mCopySkuList = new ArrayList<>();
                for (SkuBean bean : mSkuList) {
                    if (bean.isSelected()){
                        mCopySkuList.add(bean);
                        count++;
                    }
                }
                if (count == 0){
                    toast("请选择规格");
                    return;
                }
                if (TextUtils.isEmpty(supplyStore.getText())){
                    toast("请选择门店");
                    return;
                }
                String storeId = "";
                for (SupplierBean bean : mSupplierList) {
                    if (bean.getName().equals(supplyStore.getText().toString().trim())){
                        storeId = bean.getId();
                        break;
                    }
                }
                // TODO：回调
                Intent intent = new Intent();
                intent.putExtra("storeName", supplyStore.getText().toString().trim());
                intent.putExtra("storeId", storeId);
                Bundle bundle = new Bundle();
                bundle.putSerializable("special", (Serializable) mCopySkuList);
                intent.putExtras(bundle);
                intent.putExtra("num",purchaseNum.getText().toString().trim());
                setResult(GOODS_SPECIAL_REQUEST, intent);
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
                    SpecialChioceAdapter chioceAdapter = new SpecialChioceAdapter(AddNewGoodsActivity.this, mSkuList);
                    special.setAdapter(chioceAdapter);
                    special.setNumColumns(3);
                    chioceAdapter.setOnClickItem(position -> {
                        // 多选
                        mSkuList.get(position).setSelected(!mSkuList.get(position).isSelected());
                    });
                    /*special.setAlignByCenter(FlowLayout.AlienState.LEFT);
                    special.setHorizontalSpacing(10);
                    special.setVerticalSpacing(10);
                    special.setAdapter(mSkuList, R.layout.item_choose_params, new FlowLayout.ItemView() {
                        @Override
                        public void getCover(Object item, FlowLayout.ViewHolder holder, View inflate, int position) {
                            TextView text = holder.getView(R.id.tv_text);
                            ImageView tag = holder.getView(R.id.iv_checked);
                            text.setText(mSkuList.get(position).getName());
                            if (mSkuList.get(position).isSelected()) {
                                text.setBackgroundResource(R.drawable.shape_red65_4);
                                tag.setImageResource(R.drawable.ic_icon_choice);
                            } else {
                                text.setBackgroundResource(R.drawable.shape_gray05_4);
                                tag.setImageResource(R.drawable.ic_icon_choice2);
                            }
                        }
                    });*/
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
                // 获取商品规格信息
                if (result.url().contains("wxapi/v1/order.php?type=getGoodsSku")) {
                    mSupplierList = JsonParser.parseJSONArray(SupplierBean.class, JsonParser.parseJSONObject(body.get("data")).get("storelist"));
                    mSkuList = JsonParser.parseJSONArray(SkuBean.class, JsonParser.parseJSONObject(body.get("data")).get("goodsSkulist"));
                    if (mSupplierList != null) {
                        mSupplyList = new ArrayList<>();
                        for (SupplierBean bean : mSupplierList) {
                            mSupplyList.add(bean.getName());
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
