package com.rainwood.eurobusiness.ui.activity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.rainwood.eurobusiness.R;
import com.rainwood.eurobusiness.base.BaseActivity;
import com.rainwood.eurobusiness.common.Contants;
import com.rainwood.eurobusiness.domain.ClassifyBean;
import com.rainwood.eurobusiness.domain.ClassifySubBean;
import com.rainwood.eurobusiness.json.JsonParser;
import com.rainwood.eurobusiness.okhttp.HttpResponse;
import com.rainwood.eurobusiness.okhttp.OnHttpListener;
import com.rainwood.eurobusiness.request.RequestPost;
import com.rainwood.eurobusiness.ui.adapter.GoodsTypeLeftAdapter;
import com.rainwood.eurobusiness.ui.adapter.GoodsTypeRightAdapter;
import com.rainwood.tools.viewinject.ViewById;

import java.util.List;
import java.util.Map;

import static com.rainwood.eurobusiness.ui.activity.NewShopActivity.GOODS_REQUEST;

/**
 * @Author: a797s
 * @Date: 2020/2/12
 * @Desc: 商品分类  --ListView 左右联动
 */
public class GoodsTypeActivity extends BaseActivity implements View.OnClickListener, OnHttpListener {

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

    private final int INITIAL_SIZE = 0x99;
    private final int TYPE_SIZE = 0x1124;
    private List<ClassifyBean> mList;
    private List<ClassifySubBean> subTypeList;

    @Override
    protected void initView() {
        pageBack.setOnClickListener(this);
        pageTitle.setText("选择商品分类");

        // request
        showLoading("");
        RequestPost.getGoodsType(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_back:
                finish();
                break;
        }
    }

    private static int count = 0;                  // 父类
    private static int subCount = 0;        // 子类默认展示第一项
    private int flag = -1;

    @SuppressLint("HandlerLeak")
    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(@NonNull Message msg1111) {
            switch (msg1111.what) {
                case INITIAL_SIZE:
                    // 子类
                    mList.get(count).setChoose(true);
                    GoodsTypeLeftAdapter leftAdapter = new GoodsTypeLeftAdapter(GoodsTypeActivity.this, mList);
                    leftType.setAdapter(leftAdapter);
                    leftAdapter.setLeftListener(position -> {
                        for (ClassifyBean goodsBean : mList) {
                            goodsBean.setChoose(false);
                        }
                        mList.get(position).setChoose(true);
                        count = position;
                        subCount = 0;
                        // 更新局部UI -- request
                        showLoading("");
                        RequestPost.getGoodsTypeTwo(mList.get(position).getGoodsTypeOneId(), GoodsTypeActivity.this);
                    });
                    if (flag == -1) {
                        Message subMsg = new Message();
                        subMsg.what = TYPE_SIZE;
                        mHandler.sendMessage(subMsg);
                        flag++;
                    }
                    break;
                case TYPE_SIZE:      // 加载子项类型--- 通过网络请求
                    // 更改UI
                    subTypeList.get(subCount).setChoose(true);
                    GoodsTypeRightAdapter rightAdapter = new GoodsTypeRightAdapter(GoodsTypeActivity.this, subTypeList);
                    rightType.setAdapter(rightAdapter);
                    rightAdapter.setItemListener(position -> {
                        for (ClassifySubBean pressBean : subTypeList) {
                            pressBean.setChoose(false);
                        }
                        subTypeList.get(position).setChoose(true);
                        subCount = position;
                        // toast("----- " + mList.get(posFlag).getGoodsTypeOne() + "/" + subTypeList.get(position).getName());
                        Intent intent = new Intent();
                        intent.putExtra("goodsType", mList.get(count).getGoodsTypeOne() + "/" + subTypeList.get(position).getName());
                        intent.putExtra("subTypeId", subTypeList.get(position).getGoodsTypeTwoId());
                        setResult(GOODS_REQUEST, intent);
                        finish();
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
        Log.d(TAG, " --- result ---- " + result);
        Map<String, String> body = JsonParser.parseJSONObject(result.body());
        if (body != null) {
            if ("1".equals(body.get("code"))) {
                // 获取一级分类 wxapi/v1/goods.php?type=getNewGoodsTypeTwo
                if (result.url().equals(Contants.ROOT_URI + "wxapi/v1/goods.php?type=getNewGoodsType")) {
                    mList = JsonParser.parseJSONArray(ClassifyBean.class, JsonParser.parseJSONObject(body.get("data")).get("goodsTypeOne"));
                    subTypeList = JsonParser.parseJSONArray(ClassifySubBean.class, JsonParser.parseJSONObject(body.get("data")).get("goodsTypeTwo"));

                    Message msg = new Message();
                    msg.what = INITIAL_SIZE;
                    mHandler.sendMessage(msg);
                }

                // 获取二级分类
                if (result.url().equals(Contants.ROOT_URI + "wxapi/v1/goods.php?type=getNewGoodsTypeTwo")) {
                    subTypeList = JsonParser.parseJSONArray(ClassifySubBean.class, JsonParser.parseJSONObject(body.get("data")).get("goodsTypeTwo"));

                    Message msgSecond = new Message();
                    msgSecond.what = TYPE_SIZE;
                    mHandler.sendMessage(msgSecond);
                }
            } else {
                toast(body.get("warn"));
            }
            dismissLoading();
        }
    }
}
