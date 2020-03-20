package com.rainwood.eurobusiness.ui.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.rainwood.eurobusiness.R;
import com.rainwood.eurobusiness.domain.CustomTypeBean;

import java.util.List;

/**
 * @Author: shearson
 * @Time: 2020/2/24 18:47
 * @Desc: 客户分类
 */
public class CustomTypeAdapter extends BaseAdapter {

    private Context mContext;
    private List<CustomTypeBean> mList;

    public CustomTypeAdapter(Context mContext, List<CustomTypeBean> mList) {
        this.mContext = mContext;
        this.mList = mList;
    }

    @Override
    public int getCount() {
        return mList == null ? 0 : mList.size();
    }

    @Override
    public CustomTypeBean getItem(int position) {
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
            convertView = LayoutInflater.from(mContext).inflate(R.layout.item_custom_type, parent, false);
            holder.tv_serial = convertView.findViewById(R.id.tv_serial);
            holder.tv_type = convertView.findViewById(R.id.tv_type);
            holder.tv_percent = convertView.findViewById(R.id.tv_percent);
            holder.iv_point = convertView.findViewById(R.id.iv_point);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        holder.tv_serial.setText(getItem(position).getId());
        holder.tv_type.setText(getItem(position).getType());
        holder.tv_percent.setText(getItem(position).getPercent());
        // 点击事件
        holder.iv_point.setOnClickListener(v -> {
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
        private TextView tv_serial, tv_type, tv_percent;
        private ImageView iv_point;
    }
}
