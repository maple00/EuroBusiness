package com.rainwood.eurobusiness.ui.activity;

import android.annotation.SuppressLint;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.rainwood.eurobusiness.R;
import com.rainwood.eurobusiness.base.BaseActivity;
import com.rainwood.eurobusiness.domain.PressBean;
import com.rainwood.eurobusiness.domain.SelectedGoodsBean;
import com.rainwood.eurobusiness.ui.adapter.GoodsTypeLeftAdapter;
import com.rainwood.eurobusiness.ui.adapter.GoodsTypeRightAdapter;
import com.rainwood.tools.viewinject.ViewById;

import java.util.ArrayList;
import java.util.List;

/**
 * @Author: a797s
 * @Date: 2020/2/12
 * @Desc: 商品分类  --ListView 左右联动
 */
public class GoodsTypeActivity extends BaseActivity implements View.OnClickListener {

    @Override
    protected int getLayoutId() {
        return R.layout.activity_goods_type;
    }

    @ViewById(R.id.iv_back)
    private ImageView pageBack;
    @ViewById(R.id.tv_title)
    private TextView pageTitle;
    @ViewById(R.id.lv_left_type)
    private ListView leftType;
    @ViewById(R.id.lv_right_type)
    private ListView rightType;

    @Override
    protected void initView() {
        pageBack.setOnClickListener(this);
        pageTitle.setText("选择商品分类");

        GoodsTypeLeftAdapter leftAdapter = new GoodsTypeLeftAdapter(this, mList);
        leftType.setAdapter(leftAdapter);
        leftAdapter.setLeftListener(position -> {
            for (SelectedGoodsBean goodsBean : mList) {
                goodsBean.setChoose(false);
            }
            mList.get(position).setChoose(true);
            // 更新局部UI
            Message msg = new Message();
            msg.what = 1124;
            msg.obj = mList.get(position);
            mHandler.sendMessage(msg);
        });
    }

    @Override
    protected void initData() {
        mList = new ArrayList<>();
        for (int i = 0; i < types.length; i++) {
            SelectedGoodsBean selectedGoods = new SelectedGoodsBean();
            selectedGoods.setType(types[i]);
            List<PressBean> subList = new ArrayList<>();
            for (int j = 0; j < subTypes.length; j++) {
                PressBean press = new PressBean();
                press.setChoose(false);
                press.setTitle(subTypes[j]);
                subList.add(press);
            }
            selectedGoods.setSubList(subList);
            mList.add(selectedGoods);
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

    @SuppressLint("HandlerLeak")
    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(@NonNull Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case 1124:      // 加载子项类型--- 通过网络请求
                    SelectedGoodsBean selectedGoods = (SelectedGoodsBean) msg.obj;
                    // 默认所有的选项都不被选中
                    List<PressBean> subTypeList = selectedGoods.getSubList();
                    for (PressBean pressBean : subTypeList) {
                        pressBean.setChoose(false);
                    }
                    // 更改UI
                    GoodsTypeRightAdapter rightAdapter = new GoodsTypeRightAdapter(GoodsTypeActivity.this, subTypeList);
                    rightType.setAdapter(rightAdapter);
                    rightAdapter.setItemListener(position -> {
                        for (PressBean pressBean : subTypeList) {
                            pressBean.setChoose(false);
                        }
                        subTypeList.get(position).setChoose(true);
                        toast("----- " + selectedGoods.getType() + "/" + subTypeList.get(position).getTitle());
//                        Intent intent = new Intent(GoodsTypeActivity.this, NewShopActivity.class);
//                        Bundle bundle = new Bundle();
//                        bundle.putSerializable("shopType", selectedGoods.getType() + "/" + subTypeList.get(position).getTitle());
//                        startActivity(intent);
                    });
                    break;
            }
        }
    };

    /*
    模拟数据
     */
    private List<SelectedGoodsBean> mList;
    private String[] types = {"女士时装", "女士整包", "女士大码", "男士服装", "儿童服装", "鞋子",
            "箱包", "帽子/围巾", "内衣", "袜子", "配饰", "首饰", "男士服装", "儿童服装", "鞋子",
            "箱包", "帽子/围巾", "内衣", "袜子", "配饰", "首饰"};

    private String[] subTypes = {"大衣", "针织衫/毛衣", "卫衣", "套头毛衣", "T恤", "打底衫", "毛衣",
            "双面尼大衣", "大衣", "针织衫/毛衣", "卫衣", "套头毛衣", "T恤", "打底衫", "毛衣",
            "双面尼大衣"};

}
