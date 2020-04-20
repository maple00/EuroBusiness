package com.rainwood.eurobusiness.ui.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.rainwood.eurobusiness.R;
import com.rainwood.eurobusiness.domain.PressBean;

import java.util.List;

/**
 * @Author: a797s
 * @Date: 2020/2/17
 * @Desc: 选择参数属性 ----- 颜色或者尺寸
 */
public class SubChooseParamsAdapter extends BaseAdapter {

    private Context mContext;
    private List<PressBean> mList;

    public SubChooseParamsAdapter(Context mContext, List<PressBean> mList) {
        this.mContext = mContext;
        this.mList = mList;
    }

    @Override
    public int getCount() {
        return mList == null ? 0 : mList.size();
    }

    @Override
    public PressBean getItem(int position) {
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
            convertView = LayoutInflater.from(mContext).inflate(R.layout.item_choose_params, parent, false);
            holder.fl_item = convertView.findViewById(R.id.fl_item);
            holder.tv_text = convertView.findViewById(R.id.tv_text);
            holder.iv_checked = convertView.findViewById(R.id.iv_checked);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        holder.tv_text.setText(getItem(position).getTitle());
        if (getItem(position).isChoose()) {
            holder.fl_item.setBackgroundResource(R.drawable.shape_red65_4);
            holder.iv_checked.setImageResource(R.drawable.ic_icon_choice);
        } else {
            holder.fl_item.setBackgroundResource(R.drawable.shape_gray05_4);
            holder.iv_checked.setImageResource(R.drawable.ic_icon_choice2);
        }

        // 点击事件
        holder.fl_item.setOnClickListener(v -> {
            onClickItem.onClickItem(position);
            notifyDataSetChanged();
        });
        return convertView;
    }

    public interface OnClickItem {
        void onClickItem(int position);
    }

    private OnClickItem onClickItem;

    public void setOnClickItem(OnClickItem onClickItem) {
        this.onClickItem = onClickItem;
    }

    private class ViewHolder {
        private FrameLayout fl_item;
        private TextView tv_text;
        private ImageView iv_checked;
    }
}
