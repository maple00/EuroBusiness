package com.rainwood.eurobusiness.ui.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.rainwood.eurobusiness.R;
import com.rainwood.eurobusiness.domain.SpecialDetailBean;

import java.util.List;

/**
 * @Author: a797s
 * @Date: 2020/4/16 17:44
 * @Desc: 采购订单详情中门店入库明细
 */
public final class SubInventoryDetailAdapter extends BaseAdapter {

    private Context mContext;
    private List<SpecialDetailBean> mList;

    public SubInventoryDetailAdapter(Context context, List<SpecialDetailBean> list) {
        mContext = context;
        mList = list;
    }

    @Override
    public int getCount() {
        return mList == null ? 0 : mList.size();
    }

    @Override
    public SpecialDetailBean getItem(int position) {
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
            convertView = LayoutInflater.from(mContext).inflate(R.layout.sub_item_inventory_detail, parent, false);
            holder.tv_color = convertView.findViewById(R.id.tv_color);
            holder.tv_size = convertView.findViewById(R.id.tv_size);
            holder.tv_num = convertView.findViewById(R.id.tv_num);
            holder.tv_in_num = convertView.findViewById(R.id.tv_in_num);
            holder.tv_refunds_num = convertView.findViewById(R.id.tv_refunds_num);
            holder.ll_item = convertView.findViewById(R.id.ll_item);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        if (position % 2 == 0) {
            holder.ll_item.setBackgroundColor(mContext.getResources().getColor(R.color.white));
        } else {
            holder.ll_item.setBackgroundColor(mContext.getResources().getColor(R.color.gold05));
        }
        holder.tv_color.setText(getItem(position).getGoodsColor());
        holder.tv_size.setText(getItem(position).getGoodsSize());
        holder.tv_num.setText(getItem(position).getNum());
        holder.tv_in_num.setText(getItem(position).getInStockNum());
        holder.tv_refunds_num.setText(getItem(position).getRefundNum());
        return convertView;
    }

    class ViewHolder {
        private LinearLayout ll_item;
        private TextView tv_color, tv_size, tv_num, tv_in_num, tv_refunds_num;
    }
}
