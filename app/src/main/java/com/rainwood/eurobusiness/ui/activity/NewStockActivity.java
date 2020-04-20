package com.rainwood.eurobusiness.ui.activity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.rainwood.eurobusiness.R;
import com.rainwood.eurobusiness.base.BaseActivity;
import com.rainwood.eurobusiness.domain.GoodsListBean;
import com.rainwood.eurobusiness.domain.SpecialParamsBean;
import com.rainwood.eurobusiness.json.JsonParser;
import com.rainwood.eurobusiness.okhttp.HttpResponse;
import com.rainwood.eurobusiness.okhttp.OnHttpListener;
import com.rainwood.eurobusiness.request.RequestPost;
import com.rainwood.eurobusiness.ui.adapter.StockSpecialAdapter;
import com.rainwood.eurobusiness.utils.ListUtils;
import com.rainwood.tools.view.ClearEditText;
import com.rainwood.tools.viewinject.ViewById;
import com.rainwood.tools.widget.MeasureGridView;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static com.rainwood.eurobusiness.common.Contants.GOODS_NAME_REQUEST;
import static com.rainwood.eurobusiness.common.Contants.GOODS_REQUEST;

/**
 * @Author: a797s
 * @Date: 2020/2/20
 * @Desc: 新建盘点
 */
public class NewStockActivity extends BaseActivity implements View.OnClickListener, OnHttpListener {

    private List<SpecialParamsBean> mSpecialParamsList;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_new_stock;
    }

    @ViewById(R.id.iv_back)
    private ImageView pageBack;
    @ViewById(R.id.tv_title)
    private TextView pageTitle;
    //  @ViewById(R.id.lv_content_list)
    // private MeasureListView contentList;
    //
    @ViewById(R.id.mgv_content_list)
    private MeasureGridView specialList;        // 规格列表
    @ViewById(R.id.tv_goods_type)
    private TextView goodsType;
    @ViewById(R.id.tv_goods_name)
    private TextView goodsName;
    @ViewById(R.id.tv_inventory_num)       // 库存数量
    private TextView inventoryNum;
    @ViewById(R.id.cet_num)                 // 盘存数量
    private ClearEditText cet_num;
    @ViewById(R.id.cet_note)
    private ClearEditText note;
    @ViewById(R.id.btn_confirm)
    private Button confirm;

    private final int INITIAL_SIZE = 0x101;

    @Override
    protected void initView() {
        initEvents();

    }

    private void initEvents() {
        pageBack.setOnClickListener(this);
        pageTitle.setText("新建盘点");
        goodsType.setOnClickListener(this);
        goodsName.setOnClickListener(this);
        confirm.setOnClickListener(this);
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
            case R.id.tv_goods_type:                // 选择商品分类
                Intent intent = new Intent(this, GoodsTypeActivity.class);
                startActivityForResult(intent, GOODS_REQUEST);
                break;
            case R.id.tv_goods_name:                // 选择商品 -- 不选择商品则查询所有的商品
//                if (TextUtils.isEmpty(goodsType.getText())){
//                    toast("请选择商品分类");
//                    return;
//                }
                Intent intent1 = new Intent(this, GoodsListActivity.class);
                intent1.putExtra("goodsTypeId", goodsType.getHint().toString().trim());
                startActivityForResult(intent1, GOODS_NAME_REQUEST);
                break;
            case R.id.btn_confirm:
                int count = -1;
                for (int i = 0; i < ListUtils.getSize(mSpecialParamsList); i++) {
                    if (mSpecialParamsList.get(i).isHasSelected()) {
                        count = i;
                    }
                }
                if (count == -1) {
                    toast("请选择规格");
                    return;
                }
                if (TextUtils.isEmpty(cet_num.getText())) {
                    toast("请输入盘存数量");
                    return;
                }
                // TODO: 新建盘存
                showLoading("");
                RequestPost.inventoryEdit(mSpecialParamsList.get(count).getId(), goodsName.getHint().toString().trim(),
                        cet_num.getText().toString().trim(),
                        TextUtils.isEmpty(note.getText()) ? "" : note.getText().toString().trim(), this);
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == GOODS_REQUEST && resultCode == GOODS_REQUEST) {
            // 商品类别
            goodsType.setText(data.getStringExtra("goodsType"));
            // 二级分类id
            goodsType.setHint(data.getStringExtra("subTypeId"));
            // 选择了之后需要重置页面
            mSpecialParamsList = new ArrayList<>();
            goodsName.setText("");
            goodsName.setHint("");
            Message msg = new Message();
            msg.what = INITIAL_SIZE;
            mHandler.sendMessage(msg);
        }
        if (requestCode == GOODS_NAME_REQUEST && resultCode == GOODS_NAME_REQUEST) {
            GoodsListBean goods = (GoodsListBean) data.getSerializableExtra("goods");
            goodsName.setText(goods.getName());
            goodsName.setHint(goods.getGoodsId());
            // 查询商品下面的规格
            showLoading("");
            RequestPost.getInventoryGoodsSku(goods.getGoodsId(), this);
        }
    }

    @SuppressLint("HandlerLeak")
    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(@NonNull Message msg) {
            switch (msg.what) {
                case INITIAL_SIZE:
                    StockSpecialAdapter specialAdapter = new StockSpecialAdapter(NewStockActivity.this, mSpecialParamsList);
                    specialList.setNumColumns(3);
                    specialList.setAdapter(specialAdapter);
                    specialAdapter.setOnClickItem(position -> {
                        // 反选
                        if (mSpecialParamsList.get(position).isHasSelected()) {
                            mSpecialParamsList.get(position).setHasSelected(false);
                            inventoryNum.setText("0");
                            return;
                        }
                        // 单选
                        for (SpecialParamsBean bean : mSpecialParamsList) {
                            bean.setHasSelected(false);
                        }
                        mSpecialParamsList.get(position).setHasSelected(true);
                        // 库存数量
                        inventoryNum.setText(mSpecialParamsList.get(position).getStockNum());
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
        Map<String, String> body = JsonParser.parseJSONObject(result.body());
        if (body != null) {
            if ("1".equals(body.get("code"))) {
                // 获取商品下的规格信息
                if (result.url().contains("wxapi/v1/stock.php?type=getInventoryGoodsSku")) {
                    mSpecialParamsList = JsonParser.parseJSONArray(SpecialParamsBean.class, JsonParser.parseJSONObject(body.get("data")).get("goodsSkulist"));

                    Message msg = new Message();
                    msg.what = INITIAL_SIZE;
                    mHandler.sendMessage(msg);
                }

                // 新建盘存
                if (result.url().contains("wxapi/v1/stock.php?type=inventoryEdit")){
                    toast(body.get("warn"));
                    postDelayed(this::finish, 500);
                }
            } else {
                toast(body.get("warn"));
            }
            if (getDialog() != null)
                dismissLoading();
        }
    }
}
