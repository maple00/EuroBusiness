package com.rainwood.eurobusiness.ui.widget;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.PopupWindow;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.rainwood.eurobusiness.R;
import com.rainwood.eurobusiness.domain.PressBean;
import com.rainwood.eurobusiness.ui.adapter.ScreeningLeftAdapter;
import com.rainwood.eurobusiness.ui.adapter.ScreeningRightAdapter;
import com.rainwood.tools.widget.MeasureListView;

import java.util.ArrayList;
import java.util.List;

/**
 * @Author: a797s
 * @Date: 2020/2/17
 * @Desc: 筛选弹窗
 */
public class ScreeningPopWindow extends PopupWindow {

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

    public ScreeningPopWindow(Context context) {
        super(context);
        mContext = context;
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
        initData();
        leftType = contentView.findViewById(R.id.lv_left_type);
        rightType = contentView.findViewById(R.id.lv_right_type);

        Message msg = new Message();
        msg.what = PopSize;
        mHandler.sendMessage(msg);

        TextView reset = contentView.findViewById(R.id.tv_reset);
        TextView confirm = contentView.findViewById(R.id.tv_confirm);

        // 重置
        reset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onClickChecked.onClickChecked("重置");
            }
        });
        // 确定
        confirm.setOnClickListener(v -> {
            int checkedSize = 0;     // 消息提示count
            for (PressBean pressBean : mLeftList) {
                if (pressBean.isChoose()) {
                    for (PressBean right : mRightList) {
                        if (right.isChoose()) {
                            checkedSize++;
                            onClickChecked.onClickChecked(pressBean.getTitle() + "/" + right.getTitle());
                            break;
                        }
                    }
                    break;
                }
            }

            if (checkedSize == 0){
                onClickChecked.onClickChecked("请选择好再确认哟！");
            }
        });
    }

    private void initData() {
        // 左边商品类型
        mLeftList = new ArrayList<>();
        for (String title : leftTitles) {
            PressBean press = new PressBean();
            press.setChoose(false);
            press.setTitle(title);
            mLeftList.add(press);
        }
        // 右边商品类型
        mRightList = new ArrayList<>();
        for (String title : rightTitles) {
            PressBean press = new PressBean();
            press.setChoose(false);
            press.setTitle(title);
            mRightList.add(press);
        }
    }

    private final int PopSize = 1124;

    @SuppressLint("HandlerLeak")
    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(@NonNull Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case PopSize:
                    // 左边商品类型
                    ScreeningLeftAdapter leftAdapter = new ScreeningLeftAdapter(mContext, mLeftList);
                    leftType.setAdapter(leftAdapter);
                    leftAdapter.setOnClickLeft(position -> {
                        for (PressBean pressBean : mLeftList) {
                            pressBean.setChoose(false);
                        }
                        mLeftList.get(position).setChoose(true);
                        // 加载右边类型
                        ScreeningRightAdapter rightAdapter = new ScreeningRightAdapter(mContext, mRightList);
                        rightType.setAdapter(rightAdapter);
                        rightAdapter.setOnClickRight(position1 -> {
                            for (PressBean pressBean : mRightList) {
                                pressBean.setChoose(false);
                            }
                            mRightList.get(position1).setChoose(true);
                        });
                    });
                    break;
            }
        }
    };

    /*
    模拟数据
     */
    // 左边类型
    private List<PressBean> mLeftList;
    private String[] leftTitles = {"女士时装", "女士整包", "女士大码", "男士服装", "儿童服装", "鞋子",
            "箱包", "帽子/围巾", "内衣"};
    // 右边类型
    private List<PressBean> mRightList;
    private String[] rightTitles = {"大衣", "针织衫/毛衣", "卫衣", "套头卫衣", "T恤", "打底衫", "双面尼大衣"};

    public interface OnClickChecked {
        /**
         * 选中消息回调
         */
        void onClickChecked(String msg);
    }

    private OnClickChecked onClickChecked;

    public void setOnClickChecked(OnClickChecked onClickChecked) {
        this.onClickChecked = onClickChecked;
    }
}
