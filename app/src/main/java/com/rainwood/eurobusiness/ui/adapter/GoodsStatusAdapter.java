package com.rainwood.eurobusiness.ui.adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.rainwood.eurobusiness.R;
import com.rainwood.eurobusiness.domain.PressBean;

import java.util.List;

/**
 * @Author: a797s
 * @Date: 2020/2/16
 * @Desc: 商品状态 -- 采购记录
 */
public class GoodsStatusAdapter extends BaseAdapter {

    private Context mContext;
    private List<PressBean> mList;

    public GoodsStatusAdapter(Context mContext, List<PressBean> mList) {
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
            convertView = LayoutInflater.from(mContext).inflate(R.layout.item_status_press, parent, false);
            holder.tv_status = convertView.findViewById(R.id.tv_status);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        if (getItem(position).isChoose()) {      // 被选中
            holder.tv_status.setBackgroundResource(R.drawable.shape_radius_red65_14);
            holder.tv_status.setTextColor(mContext.getResources().getColor(R.color.red30));
        } else {
            holder.tv_status.setBackgroundResource(R.drawable.shape_radius_gray10_14);
            holder.tv_status.setTextColor(mContext.getResources().getColor(R.color.textColor));
        }
        holder.tv_status.setText(getItem(position).getTitle());
        // 点击事件
        holder.tv_status.setOnClickListener(v -> {
            onClickItem.onClickStatus(position);
            notifyDataSetChanged();
        });
        return convertView;
    }

    public interface OnClickItem {
        void onClickStatus(int position);
    }

    private OnClickItem onClickItem;

    public void setOnClickItem(OnClickItem onClickItem) {
        this.onClickItem = onClickItem;
    }

    private class ViewHolder {
        private TextView tv_status;
    }
}
