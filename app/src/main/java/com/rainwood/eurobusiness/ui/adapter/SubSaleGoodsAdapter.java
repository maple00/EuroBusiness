package com.rainwood.eurobusiness.ui.adapter;

import android.content.Context;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.rainwood.eurobusiness.R;
import com.rainwood.eurobusiness.domain.CommonUIBean;

import java.util.List;

/**
 * @Author: shearson
 * @Time: 2020/2/26 15:22
 * @Desc: 进价 --- 批发价 --- 零售价
 */
public class SubSaleGoodsAdapter extends BaseAdapter {

    private Context mContext;
    private List<CommonUIBean> mList;

    public SubSaleGoodsAdapter(Context mContext, List<CommonUIBean> mList) {
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
            convertView = LayoutInflater.from(mContext).inflate(R.layout.sub_item_sale_goods, parent, false);
            holder.tv_price_type = convertView.findViewById(R.id.tv_price_type);
            holder.tv_price = convertView.findViewById(R.id.tv_price);
            holder.ll_item = convertView.findViewById(R.id.ll_item);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        if (TextUtils.isEmpty(getItem(position).getShowText())) {
            holder.ll_item.setVisibility(View.GONE);
        } else {
            holder.ll_item.setVisibility(View.VISIBLE);
            holder.tv_price_type.setText(getItem(position).getTitle());
            holder.tv_price.setText(getItem(position).getShowText());
        }
        return convertView;
    }

    private class ViewHolder {
        private TextView tv_price_type, tv_price;
        private LinearLayout ll_item;
    }
}
