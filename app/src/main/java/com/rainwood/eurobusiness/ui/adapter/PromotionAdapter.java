package com.rainwood.eurobusiness.ui.adapter;

import android.content.Context;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.rainwood.eurobusiness.R;
import com.rainwood.eurobusiness.domain.CommonUIBean;
import com.rainwood.tools.widget.SwitchButton;

import java.util.List;

/**
 * @Author: a797s
 * @Date: 2020/2/12
 * @Desc: 促销信息 --- newShop
 */
public class PromotionAdapter extends BaseAdapter {

    private Context mContext;
    private List<CommonUIBean> mList;
    private int parentPosition;

    public PromotionAdapter(Context mContext, List<CommonUIBean> mList) {
        this.mContext = mContext;
        this.mList = mList;
    }

    @Override
    public int getCount() {
        return mList == null ? 0 : mList.size();
    }

    @Override
    public CommonUIBean getItem(int position) {
        return mList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if (convertView == null) {
            holder = new ViewHolder();
            convertView = LayoutInflater.from(mContext).inflate(R.layout.item_promotion, parent, false);
            holder.tv_title = convertView.findViewById(R.id.tv_title);
            holder.sb_switch = convertView.findViewById(R.id.sb_switch);
            holder.et_hint = convertView.findViewById(R.id.et_hint);
            holder.iv_right_arrow = convertView.findViewById(R.id.iv_right_arrow);
            holder.tv_percent = convertView.findViewById(R.id.tv_percent);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        if (getItem(position).getTitle().equals("设为促销商品")) {
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                    ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            holder.tv_title.setLayoutParams(params);
            holder.tv_title.setText(getItem(position).getTitle());
            holder.iv_right_arrow.setVisibility(View.GONE);
            holder.et_hint.setVisibility(View.GONE);
            holder.tv_percent.setVisibility(View.GONE);
            holder.sb_switch.setVisibility(View.VISIBLE);
        } else {
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                    200, ViewGroup.LayoutParams.WRAP_CONTENT);
            holder.tv_title.setLayoutParams(params);
            holder.tv_title.setText(getItem(position).getTitle());
            if (getItem(position).getTitle().equals("开始时间") || getItem(position).getTitle().equals("结束时间")) {
                holder.iv_right_arrow.setVisibility(View.VISIBLE);
                holder.et_hint.setVisibility(View.VISIBLE);
                holder.sb_switch.setVisibility(View.GONE);
                holder.tv_percent.setVisibility(View.GONE);
                // 时间控件     -- EditText 触发点击事件
                holder.et_hint.setFocusableInTouchMode(false);//不可编辑
                holder.et_hint.setOnClickListener(v -> dateListener.onClickDate(parentPosition, position));

            } else {
                if (getItem(position).getTitle().equals("折扣")) {
                    holder.tv_percent.setVisibility(View.VISIBLE);
                } else {
                    holder.tv_percent.setVisibility(View.GONE);
                }
                holder.iv_right_arrow.setVisibility(View.GONE);
                holder.et_hint.setVisibility(View.VISIBLE);
                holder.sb_switch.setVisibility(View.GONE);
            }
            if (TextUtils.isEmpty(getItem(position).getShowText())){
                holder.et_hint.setHint(getItem(position).getLabel());
            }else {
                holder.et_hint.setText(getItem(position).getShowText());
            }
        }
        return convertView;
    }

    public interface OnClickDateListener {
        // 选择时间日期
        void onClickDate(int parentPosition, int position);
    }

    private OnClickDateListener dateListener;

    public void setDateListener(int parentPosition, OnClickDateListener dateListener) {
        this.dateListener = dateListener;
        this.parentPosition = parentPosition;
    }

    private class ViewHolder {
        private TextView tv_title, tv_percent;
        private SwitchButton sb_switch;
        private EditText et_hint;
        private ImageView iv_right_arrow;
    }

}
