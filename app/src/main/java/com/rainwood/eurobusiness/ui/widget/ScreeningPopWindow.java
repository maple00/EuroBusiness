package com.rainwood.eurobusiness.ui.widget;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.PopupWindow;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.rainwood.eurobusiness.R;
import com.rainwood.eurobusiness.domain.PressTypeBean;
import com.rainwood.eurobusiness.domain.PressTypeRightBean;
import com.rainwood.eurobusiness.json.JsonParser;
import com.rainwood.eurobusiness.okhttp.HttpResponse;
import com.rainwood.eurobusiness.okhttp.OnHttpListener;
import com.rainwood.eurobusiness.request.RequestPost;
import com.rainwood.eurobusiness.ui.adapter.ScreeningLeftTypeAdapter;
import com.rainwood.eurobusiness.ui.adapter.ScreeningRightAdapter;
import com.rainwood.tools.widget.MeasureListView;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.function.Predicate;
import java.util.stream.Collectors;

/**
 * @Author: a797s
 * @Date: 2020/2/17
 * @Desc: 筛选弹窗
 */
public class ScreeningPopWindow extends PopupWindow implements OnHttpListener {

    /**
     * 上下文对象
     */
    private Context mContext;

    /**
     * 左边类型
     */
    private MeasureListView leftType;

    /**
     * 右边类型
     */
    private MeasureListView rightType;

    private static List<PressTypeBean> mLeftList;
    private static List<PressTypeRightBean> mRightList;
    private final int RIGHT_SIZE = 0x101;
    private final int PopSize = 1124;
    private static int count = -1;

    public ScreeningPopWindow(Context context) {
        super(context);
        mContext = context;
    }

    public ScreeningPopWindow(List<PressTypeRightBean> rightList, Context context) {
        this(null, rightList, context);
    }

    public ScreeningPopWindow(List<PressTypeBean> leftList, List<PressTypeRightBean> rightList, Context context) {
        super(context);
        mContext = context;
        mLeftList = leftList == null ? mLeftList : leftList;
        mRightList = rightList;
        // Log.d("sxs", "顶部 ===== " + leftList.toString());
        initView();
    }

    private void initView() {
        setHeight(ViewGroup.LayoutParams.WRAP_CONTENT);
        setWidth(ViewGroup.LayoutParams.MATCH_PARENT);
        setOutsideTouchable(true);
        setFocusable(true);
        setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        @SuppressLint("InflateParams")
        View contentView = LayoutInflater.from(mContext).inflate(R.layout.pop_window_screening,
                null, false);
        setContentView(contentView);
        // 初始化数据
        leftType = contentView.findViewById(R.id.lv_left_type);
        rightType = contentView.findViewById(R.id.lv_right_type);

        //
        if (count >= 0) {

        } else {
            count++;
            initData();
        }

        Message msg = new Message();
        msg.what = PopSize;
        mHandler.sendMessage(msg);
        Log.d("sxs", mRightList.toString());

        TextView reset = contentView.findViewById(R.id.tv_reset);
        TextView confirm = contentView.findViewById(R.id.tv_confirm);
        // 重置
        reset.setOnClickListener(v -> onClickChecked.onClickChecked("重置"));
        // 确定
        confirm.setOnClickListener(v -> {
            List<PressTypeRightBean> newRightList = new ArrayList<>();
            for (PressTypeBean pressBean : mLeftList) {
                if (pressBean.isChoose()) {         // 找到一级分类
                    // 筛选选中的二级分类
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                        newRightList = mRightList.stream().filter(PressTypeRightBean::isChoose).collect(Collectors.toList());
                    }
                    break;
                }
            }
            List<String> idList = new ArrayList<>();
            for (PressTypeRightBean typeRightBean : newRightList) {
                idList.add(typeRightBean.getGoodsTypeTwoId());
            }
            onClickChecked.getSubTypeList(idList);
        });
    }

    private void initData() {
        mLeftList.get(0).setChoose(true);
    }

    @SuppressLint("HandlerLeak")
    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(@NonNull Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case PopSize:
                    // 左边商品类型
                    ScreeningLeftTypeAdapter leftAdapter = new ScreeningLeftTypeAdapter(mContext, mLeftList);
                    leftType.setAdapter(leftAdapter);
                    leftAdapter.setOnClickLeft(position -> {
                        // 一级分类只能单选
                        for (PressTypeBean pressBean : mLeftList) {
                            pressBean.setChoose(false);
                        }
                        mLeftList.get(position).setChoose(true);
                        // request --- 查询二级分类
                        RequestPost.getGoodsTypeTwo(mLeftList.get(position).getGoodsTypeOneId(), ScreeningPopWindow.this);
                    });
                    // 加载右边类型
                    if (mRightList != null && mRightList.size() != 0) {
                        Message rightMsg = new Message();
                        rightMsg.what = RIGHT_SIZE;
                        mHandler.sendMessage(rightMsg);
                    }
                    break;
                case RIGHT_SIZE:
                    ScreeningRightAdapter rightAdapter = new ScreeningRightAdapter(mContext, mRightList);
                    rightType.setAdapter(rightAdapter);
                    rightAdapter.setOnClickRight(position -> {
                        // 二级分类可以多选
                        // 置反
                        mRightList.get(position).setChoose(!mRightList.get(position).isChoose());
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
        Log.d("sxs", " ----- result ----- " + result);
        Map<String, String> body = JsonParser.parseJSONObject(result.body());
        if (body != null) {
            if ("1".equals(body.get("code"))) {
                if (result.url().contains("wxapi/v1/goods.php?type=getNewGoodsTypeTwo")) {
                    Log.d("sxs", body.get("data"));
                    mRightList = JsonParser.parseJSONArray(PressTypeRightBean.class,
                            JsonParser.parseJSONObject(body.get("data")).get("goodsTypeTwo"));

                    Message msg = new Message();
                    msg.what = RIGHT_SIZE;
                    mHandler.sendMessage(msg);
                }
            }
        }
    }

    public interface OnClickChecked {
        void onClickChecked(String msg);

        // 返回找到的二级分类
        void getSubTypeList(List<String> rightList);
    }

    private OnClickChecked onClickChecked;

    public void setOnClickChecked(OnClickChecked onClickChecked) {
        this.onClickChecked = onClickChecked;
    }
}
