package com.rainwood.eurobusiness.ui.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.rainwood.eurobusiness.R;
import com.rainwood.eurobusiness.domain.SpecialParamBean;

import java.util.List;

/**
 * @Author: a797s
 * @Date: 2020/4/15 18:31
 * @Desc: 批发商采购单详情---商品规格详情
 */
public final class PurchaseDetatilAdapter extends BaseAdapter {

    private Context mContext;
    private List<SpecialParamBean> mList;

    public PurchaseDetatilAdapter(Context context, List<SpecialParamBean> list) {
        mContext = context;
        mList = list;
    }

    @Override
    public int getCount() {
        return mList == null ? 0 : mList.size();
    }

    @Override
    public SpecialParamBean getItem(int position) {
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
            convertView = LayoutInflater.from(mContext).inflate(R.layout.item_purchase_detail_special, parent, false);
            holder.tv_color = convertView.findViewById(R.id.tv_color);
            holder.tv_size = convertView.findViewById(R.id.tv_size);
            holder.tv_price = convertView.findViewById(R.id.tv_price);
            holder.tv_num = convertView.findViewById(R.id.tv_num);
            holder.tv_store = convertView.findViewById(R.id.tv_store);
            holder.ll_item = convertView.findViewById(R.id.ll_item);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        if (position % 2 == 0){
            holder.ll_item.setBackgroundColor(mContext.getResources().getColor(R.color.white));
        }else {
            holder.ll_item.setBackgroundColor(mContext.getResources().getColor(R.color.gold05));
        }
        holder.tv_color.setText(getItem(position).getGoodsColor());
        holder.tv_size.setText(getItem(position).getGoodsColor());
        holder.tv_price.setText(getItem(position).getPrice());
        holder.tv_num.setText(getItem(position).getNum());
        holder.tv_store.setText(getItem(position).getStoreName());
        return convertView;
    }

    private class ViewHolder {
        private LinearLayout ll_item;
        private TextView tv_color, tv_size, tv_price, tv_num, tv_store;
    }
}
