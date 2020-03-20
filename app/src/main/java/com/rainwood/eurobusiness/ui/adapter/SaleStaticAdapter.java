package com.rainwood.eurobusiness.ui.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.rainwood.eurobusiness.R;
import com.rainwood.eurobusiness.domain.SaleStaticsBean;

import java.util.List;

/**
 * @Author: a797s
 * @Date: 2020/2/13
 * @Desc:
 */
public class SaleStaticAdapter extends BaseAdapter {

    private Context mContext;
    private List<SaleStaticsBean> mList;

    public SaleStaticAdapter(Context mContext, List<SaleStaticsBean> mList) {
        this.mContext = mContext;
        this.mList = mList;
    }

    @Override
    public int getCount() {
        return mList == null ? 0 : mList.size();
    }

    @Override
    public SaleStaticsBean getItem(int position) {
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
            convertView = LayoutInflater.from(mContext).inflate(R.layout.item_sales_static, parent, false);
            holder.tv_title = convertView.findViewById(R.id.tv_title);
            holder.tv_today_statics = convertView.findViewById(R.id.tv_today_statics);
            holder.tv_month_statics = convertView.findViewById(R.id.tv_month_statics);
            holder.ll_item = convertView.findViewById(R.id.ll_item);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        holder.tv_title.setText(getItem(position).getTitle());
        holder.tv_today_statics.setText(getItem(position).getToday());
        holder.tv_month_statics.setText(getItem(position).getMonth());
        if (position % 3 == 0) {
            holder.ll_item.setBackground(mContext.getResources().getDrawable(R.drawable.shape_gradient_bg));
        } else if (position % 3 == 1) {
            holder.ll_item.setBackground(mContext.getResources().getDrawable(R.drawable.shape_gradient_bg_1));
        } else {
            holder.ll_item.setBackground(mContext.getResources().getDrawable(R.drawable.shape_gradient_bg_2));
        }
        return convertView;
    }

    private class ViewHolder {
        private TextView tv_title, tv_today_statics, tv_month_statics;
        private LinearLayout ll_item;
    }
}
