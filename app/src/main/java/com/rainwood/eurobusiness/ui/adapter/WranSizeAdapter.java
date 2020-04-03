package com.rainwood.eurobusiness.ui.adapter;

import android.content.Context;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.rainwood.eurobusiness.R;
import com.rainwood.eurobusiness.domain.SpecificationBean;

import java.util.List;

/**
 * @Author: a797s
 * @Date: 2020/2/15
 * @Desc: 尺寸 adapter
 */
public class WranSizeAdapter extends BaseAdapter {

    private Context mContext;
    private List<SpecificationBean> mList;

    public WranSizeAdapter(Context mContext, List<SpecificationBean> mList) {
        this.mContext = mContext;
        this.mList = mList;
    }

    @Override
    public int getCount() {
        return mList == null ? 0 : mList.size();
    }

    @Override
    public SpecificationBean getItem(int position) {
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
            convertView = LayoutInflater.from(mContext).inflate(R.layout.item_size, parent, false);
            holder.tv_color = convertView.findViewById(R.id.tv_color);
            holder.tv_size = convertView.findViewById(R.id.tv_size);
            holder.tv_below = convertView.findViewById(R.id.tv_below);
            holder.tv_price = convertView.findViewById(R.id.tv_price);
            holder.tv_repertory = convertView.findViewById(R.id.tv_repertory);
            holder.rl_item = convertView.findViewById(R.id.rl_item);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        if (position % 2 == 0){
            holder.rl_item.setBackgroundColor(mContext.getResources().getColor(R.color.white));
        }else {
            holder.rl_item.setBackgroundColor(mContext.getResources().getColor(R.color.gold05));
        }
        holder.tv_color.setText(getItem(position).getGoodsColor());
        holder.tv_size.setText(getItem(position).getGoodsSize());
        if (TextUtils.isEmpty(getItem(position).getIowerLimit())){
            holder.tv_below.setVisibility(View.GONE);
        }else {
            holder.tv_below.setText(getItem(position).getIowerLimit());
        }
        holder.tv_price.setText(getItem(position).getTradePrice());
        holder.tv_repertory.setText(getItem(position).getStock());
        return convertView;
    }

    private class ViewHolder {
        private TextView tv_color, tv_size, tv_below, tv_price, tv_repertory;
        private RelativeLayout rl_item;
    }
}
