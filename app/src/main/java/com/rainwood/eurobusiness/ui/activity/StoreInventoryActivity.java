package com.rainwood.eurobusiness.ui.activity;

import android.annotation.SuppressLint;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.rainwood.eurobusiness.R;
import com.rainwood.eurobusiness.base.BaseActivity;
import com.rainwood.eurobusiness.domain.InventoryDetailBean;
import com.rainwood.eurobusiness.json.JsonParser;
import com.rainwood.eurobusiness.okhttp.HttpResponse;
import com.rainwood.eurobusiness.okhttp.OnHttpListener;
import com.rainwood.eurobusiness.request.RequestPost;
import com.rainwood.eurobusiness.ui.adapter.InventoryDetailAdapter;
import com.rainwood.eurobusiness.utils.RecyclerViewSpacesItemDecoration;
import com.rainwood.tools.common.FontDisplayUtil;
import com.rainwood.tools.viewinject.ViewById;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @Author: a797s
 * @Date: 2020/4/16 16:31
 * @Desc: 门店入库明细
 */
public final class StoreInventoryActivity extends BaseActivity implements OnHttpListener, View.OnClickListener {

    @ViewById(R.id.iv_back)
    private ImageView mPageBack;
    @ViewById(R.id.tv_title)
    private TextView mPageTitle;
    @ViewById(R.id.rv_store_inventory)
    private RecyclerView storeInventory;

    private final int INITIAL_SIZE = 0x101;
    private List<InventoryDetailBean> mInventoryDetailList;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_store_inventory;
    }

    @Override
    protected void initView() {
        mPageBack.setOnClickListener(this);
        mPageTitle.setText("门店入库明细");
        String orderNo = getIntent().getStringExtra("orderNo");
        if (orderNo != null) {
            // TODO: 获取门店入库信息
            showLoading("");
            RequestPost.getInStoreMx(orderNo, this);
        } else {
            toast("数据异常");
            finish();
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_back:
                finish();
                break;

        }
    }

    private int count = 0;

    @SuppressLint("HandlerLeak")
    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(@NonNull Message msg) {
            switch (msg.what) {
                case INITIAL_SIZE:
                    InventoryDetailAdapter detailAdapter = new InventoryDetailAdapter(StoreInventoryActivity.this);
                    LinearLayoutManager managerVertical = new LinearLayoutManager(StoreInventoryActivity.this);
                    managerVertical.setOrientation(LinearLayoutManager.VERTICAL);       // 纵向
                    detailAdapter.setList(mInventoryDetailList);
                    storeInventory.setAdapter(detailAdapter);
                    if (count == 0) {
                        HashMap<String, Integer> stringIntegerHashMap = new HashMap<>();
                        stringIntegerHashMap.put(RecyclerViewSpacesItemDecoration.BOTTOM_DECORATION, FontDisplayUtil.dip2px(StoreInventoryActivity.this, 36)); // 下间距
                        storeInventory.addItemDecoration(new RecyclerViewSpacesItemDecoration(stringIntegerHashMap));
                        storeInventory.setLayoutManager(managerVertical);
                        storeInventory.setHasFixedSize(true);
                        count++;
                    }
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
                // 获取入库明细
                if (result.url().contains("wxapi/v1/order.php?type=getInStoreMx")) {
                    mInventoryDetailList = JsonParser.parseJSONArray(InventoryDetailBean.class, JsonParser.parseJSONObject(body.get("data")).get("list"));

                    Message msg = new Message();
                    msg.what = INITIAL_SIZE;
                    mHandler.sendMessage(msg);
                }
            } else {
                toast(body.get("warn"));
            }
            if (getDialog() != null)
                dismissLoading();
        }
    }

}
