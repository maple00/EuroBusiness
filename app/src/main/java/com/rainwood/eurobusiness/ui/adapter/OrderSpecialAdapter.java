package com.rainwood.eurobusiness.ui.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.rainwood.eurobusiness.R;
import com.rainwood.eurobusiness.domain.SkuBean;

import java.util.List;

/**
 * @Author: a797s
 * @Date: 2020/4/9 14:38
 * @Desc: 订单规格
 */
public final class OrderSpecialAdapter extends BaseAdapter {

    private Context mContext;
    private List<SkuBean> mList;

    public OrderSpecialAdapter(Context context, List<SkuBean> list) {
        mContext = context;
        mList = list;
    }

    @Override
    public int getCount() {
        return mList == null ? 0 : mList.size();
    }

    @Override
    public SkuBean getItem(int position) {
        return mList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if (convertView == null){
            holder = new ViewHolder();
            convertView = LayoutInflater.from(mContext).inflate(R.layout.item_order_special_info, parent, false);
            holder.tv_size = convertView.findViewById(R.id.tv_size);
            holder.tv_color = convertView.findViewById(R.id.tv_color);
            holder.tv_price = convertView.findViewById(R.id.tv_price);
            holder.tv_num = convertView.findViewById(R.id.tv_num);
            holder.ll_item = convertView.findViewById(R.id.ll_item);
            convertView.setTag(holder);
        }else {
            holder = (ViewHolder) convertView.getTag();
        }
        if (position % 2 == 0){
            holder.ll_item.setBackgroundResource(R.color.white);
        }else {
            holder.ll_item.setBackgroundResource(R.color.gold05);
        }
        String[] nameAndSize = getItem(position).getName().split("/");
        holder.tv_color.setText(nameAndSize[0] == null ? "" : nameAndSize[0]);
        holder.tv_size.setText(nameAndSize[1] == null ? "" : nameAndSize[1]);
        holder.tv_price.setText(getItem(position).getTradePrice());
        holder.tv_num.setText(getItem(position).getNum());
        return convertView;
    }

    class ViewHolder{
        private LinearLayout ll_item;
        private TextView tv_color, tv_size, tv_price, tv_num;
    }
}
