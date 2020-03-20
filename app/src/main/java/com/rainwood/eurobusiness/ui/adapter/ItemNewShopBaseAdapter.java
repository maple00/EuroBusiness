package com.rainwood.eurobusiness.ui.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.text.Html;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.rainwood.eurobusiness.R;
import com.rainwood.eurobusiness.domain.CommonUIBean;

import java.util.List;

/**
 * @Author: a797s
 * @Date: 2020/2/10
 * @Desc: 新建商品 —— 基本信息
 */
public class ItemNewShopBaseAdapter extends BaseAdapter {

    private Context mContext;
    private List<CommonUIBean> mList;
    private int parentPosition;

    public ItemNewShopBaseAdapter(Context mContext, List<CommonUIBean> mList) {
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

    @SuppressLint("ClickableViewAccessibility")
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if (convertView == null) {
            holder = new ViewHolder();
            convertView = LayoutInflater.from(mContext).inflate(R.layout.item_sub_new_shop_base,
                    parent, false);
            holder.tv_title = convertView.findViewById(R.id.tv_title);
            holder.et_hint = convertView.findViewById(R.id.et_hint);
            holder.iv_right_arrow = convertView.findViewById(R.id.iv_right_arrow);
            holder.iv_scan = convertView.findViewById(R.id.iv_scan);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        if (TextUtils.isEmpty(getItem(position).getShowText())){
            holder.et_hint.setHint(getItem(position).getLabel());
        }else {
            holder.et_hint.setText(getItem(position).getShowText());
        }
        // 标注必填项目
        if (getItem(position).getTitle().equals("条码")) {
            holder.tv_title.setText(Html.fromHtml(getItem(position).getTitle()));
            holder.iv_right_arrow.setVisibility(View.GONE);
            holder.iv_scan.setVisibility(View.VISIBLE);
        } else {
            holder.iv_scan.setVisibility(View.GONE);
            if (getItem(position).getTitle().contentEquals("商品分类")) {
                holder.iv_right_arrow.setVisibility(View.VISIBLE);
                holder.et_hint.setFocusableInTouchMode(false);//不可编辑
                holder.et_hint.setOnClickListener(v -> labelListener.onClickShopType(parentPosition, position));
            } else {             // 隐藏右箭头和扫一扫
                holder.iv_right_arrow.setVisibility(View.GONE);
                holder.et_hint.setFocusableInTouchMode(true);
            }
            holder.tv_title.setText(Html.fromHtml("<font color="
                    + mContext.getResources().getColor(R.color.red30) + ">*</font>"
                    + getItem(position).getTitle()));
        }

        // 扫一扫点击
        holder.iv_scan.setOnClickListener(v -> labelListener.onClickScan());
        return convertView;
    }

    // 点击事件
    public interface OnItemLabelListener {
        // 商品分类的点击事件
        void onClickShopType(int parentPosition, int position);

        // 条码
        void onClickScan();
    }

    private OnItemLabelListener labelListener;

    public void setLabelListener(int parentPosition, OnItemLabelListener labelListener) {
        this.labelListener = labelListener;
        this.parentPosition = parentPosition;
    }

    private class ViewHolder {
        private TextView tv_title;
        private EditText et_hint;
        private ImageView iv_right_arrow, iv_scan;
    }
}
