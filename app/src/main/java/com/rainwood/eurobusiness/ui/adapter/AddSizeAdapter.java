package com.rainwood.eurobusiness.ui.adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.rainwood.eurobusiness.R;
import com.rainwood.eurobusiness.domain.CommonUIBean;

import java.util.List;

/**
 * @Author: a797s
 * @Date: 2020/2/16
 * @Desc: 添加尺码 adapter
 */
public class AddSizeAdapter extends BaseAdapter {

    private Context mContext;
    private List<CommonUIBean> mList;

    public AddSizeAdapter(Context mContext, List<CommonUIBean> mList) {
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
            convertView = LayoutInflater.from(mContext).inflate(R.layout.item_add_size, parent, false);
            holder.tv_title = convertView.findViewById(R.id.tv_title);
            holder.et_hint = convertView.findViewById(R.id.et_hint);
            holder.iv_right_arrow = convertView.findViewById(R.id.iv_right_arrow);
            holder.iv_img = convertView.findViewById(R.id.iv_img);
            holder.iv_img1 = convertView.findViewById(R.id.iv_img1);
            holder.rl_size = convertView.findViewById(R.id.rl_size);
            holder.ll_pre_size = convertView.findViewById(R.id.ll_pre_size);
            holder.ll_custom = convertView.findViewById(R.id.ll_custom);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        holder.tv_title.setText(getItem(position).getTitle());
        holder.et_hint.setHint(getItem(position).getLabel());
        // 隐藏右箭头
        if (getItem(position).getTitle().equals("颜色") || getItem(position).getTitle().equals("尺码")) {
            holder.iv_right_arrow.setVisibility(View.VISIBLE);
        } else {
            holder.iv_right_arrow.setVisibility(View.GONE);
        }
        // 重新绘制UI
        if (getItem(position).getTitle().equals("尺码")) {
            holder.rl_size.setVisibility(View.VISIBLE);
        } else {
            holder.rl_size.setVisibility(View.GONE);
        }

        // 选择颜色
        if (getItem(position).getTitle().equals("颜色")) {
            holder.et_hint.setFocusableInTouchMode(false);
            holder.et_hint.setOnClickListener(v -> onClickEditText.onClickColor(position));
        }
        // 选择尺寸
        if (getItem(position).getType() == 0) {      // 默认类型
            holder.iv_img.setImageResource(R.drawable.radio_checked_shape);
            holder.iv_img1.setImageResource(R.drawable.radio_uncheck_shape);
            holder.et_hint.setFocusableInTouchMode(false);
        } else {
            holder.et_hint.setFocusableInTouchMode(true);
            holder.iv_img.setImageResource(R.drawable.radio_uncheck_shape);
            holder.iv_img1.setImageResource(R.drawable.radio_checked_shape);
        }
        // 预设尺寸  -- 取消焦点，去选择
        holder.ll_pre_size.setOnClickListener(v -> {
            holder.et_hint.setFocusableInTouchMode(false);
            onClickEditText.onClickPreSize(position);
            holder.et_hint.setOnClickListener(v1 -> {
                onClickEditText.onClickChooseSize(position);
                notifyDataSetChanged();
            });
            notifyDataSetChanged();
        });
        // 自定义尺寸 -- 设置焦点，填写
        holder.ll_custom.setOnClickListener(v -> {
            holder.et_hint.setFocusableInTouchMode(true);
            onClickEditText.onClickCusSize(position);
            notifyDataSetChanged();
        });
        // 去选择
        return convertView;
    }

    public interface OnClickEditText {
        // 选择颜色
        void onClickColor(int position);
        // 选择预设尺码
        void onClickPreSize(int position);
        // 去选择预设尺码
        void onClickChooseSize(int position);
        // 选择自定义尺寸
        void onClickCusSize(int position);
    }

    private OnClickEditText onClickEditText;

    public void setOnClickEditText(OnClickEditText onClickEditText) {
        this.onClickEditText = onClickEditText;
    }

    private class ViewHolder {
        private TextView tv_title;
        private EditText et_hint;
        private ImageView iv_right_arrow, iv_img, iv_img1;
        private RelativeLayout rl_size;
        private LinearLayout ll_pre_size, ll_custom;
    }
}
