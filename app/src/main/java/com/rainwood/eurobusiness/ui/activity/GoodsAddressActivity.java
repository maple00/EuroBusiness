package com.rainwood.eurobusiness.ui.activity;

import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.rainwood.eurobusiness.R;
import com.rainwood.eurobusiness.base.BaseActivity;
import com.rainwood.eurobusiness.common.Contants;
import com.rainwood.eurobusiness.domain.AddressBean;
import com.rainwood.eurobusiness.ui.adapter.GoodsAddressAdapter;
import com.rainwood.tools.viewinject.ViewById;
import com.rainwood.tools.widget.MeasureListView;

import java.util.ArrayList;
import java.util.List;

/**
 * @Author: shearson
 * @Time: 2020/2/24 15:49
 * @Desc: 收获地址
 */
public class GoodsAddressActivity extends BaseActivity implements View.OnClickListener {

    @Override
    protected int getLayoutId() {
        return R.layout.activity_goods_address;
    }

    @ViewById(R.id.iv_back)
    private ImageView pageBack;
    @ViewById(R.id.tv_title)
    private TextView pageTitle;
    @ViewById(R.id.lv_content_list)
    private MeasureListView contentList;
    @ViewById(R.id.btn_new_address)
    private Button newAddress;

    @Override
    protected void initView() {
        pageBack.setOnClickListener(this);

        if (Contants.CHOOSE_MODEL_SIZE == 13) {          // 收货地址
            setDefaultUI("收货地址", "新增收货地址");
        }

        if (Contants.CHOOSE_MODEL_SIZE == 14) {              // 开票地址
            setDefaultUI("开票地址", "新增开票地址");
        }
    }

    private void setDefaultUI(String title, String bottomText) {
        newAddress.setOnClickListener(this);
        pageTitle.setText(title);
        newAddress.setText(bottomText);
        //
        GoodsAddressAdapter addressAdapter = new GoodsAddressAdapter(this, mList);
        contentList.setAdapter(addressAdapter);
        addressAdapter.setOnClickItem(new GoodsAddressAdapter.OnClickItem() {
            @Override
            public void onClickDefault(int position) {
                for (AddressBean addressBean : mList) {
                    addressBean.setChecked(false);
                }
                mList.get(position).setChecked(true);
            }

            @Override
            public void onClickEdit(int position) {
                toast("编辑收货地址");
                openActivity(NewGoodsAddressActivity.class);
            }

            @Override
            public void onClickDelete(int position) {
                //toast("删除");
                mList.remove(position);
            }

            @Override
            public void onClickItem(int position) {
                toast("您选择了地址：" + (position + 1));
            }
        });
    }

    @Override
    protected void initData() {
        super.initData();
        mList = new ArrayList<>();
        for (int i = 0; i < 2; i++) {
            AddressBean address = new AddressBean();
            if (i == 0) {
                address.setChecked(true);
            } else {
                address.setChecked(false);
            }
            address.setName("双木衣馆");
            address.setAddress("中国重庆市南岸区弹子石腾龙大道国际商务大厦A幢22-2");
            mList.add(address);
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_back:
                finish();
                break;
            case R.id.btn_new_address:
                if (Contants.CHOOSE_MODEL_SIZE == 13) {              // 新增收货地址
                    openActivity(NewGoodsAddressActivity.class);
                }
                if (Contants.CHOOSE_MODEL_SIZE == 14) {              // 新增开票地址
                    openActivity(NewGoodsAddressActivity.class);
                }
                break;
        }
    }

    /*
    模拟数据
     */
    private List<AddressBean> mList;
}
