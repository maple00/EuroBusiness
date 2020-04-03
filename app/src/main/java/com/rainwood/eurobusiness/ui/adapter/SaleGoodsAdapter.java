package com.rainwood.eurobusiness.ui.adapter;

import android.content.Context;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.rainwood.eurobusiness.R;
import com.rainwood.eurobusiness.domain.SaleGoodsBean;
import com.rainwood.tools.widget.MeasureGridView;

import java.util.List;

/**
 * @Author: shearson
 * @Time: 2020/2/26 15:03
 * @Desc: 由批发商/门店创建的商品
 */
public class SaleGoodsAdapter extends BaseAdapter {

    private Context mContext;
    private List<SaleGoodsBean> mList;

    public SaleGoodsAdapter(Context mContext, List<SaleGoodsBean> mList) {
        this.mContext = mContext;
        this.mList = mList;
    }

    @Override
    public int getCount() {
        return mList == null ? 0 : mList.size();
    }

    @Override
    public SaleGoodsBean getItem(int position) {
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
            convertView = LayoutInflater.from(mContext).inflate(R.layout.item_sale_goods, parent, false);
            holder.iv_shop_img = convertView.findViewById(R.id.iv_shop_img);
            holder.tv_status = convertView.findViewById(R.id.tv_status);
            holder.tv_store_name = convertView.findViewById(R.id.tv_store_name);
            holder.gv_price = convertView.findViewById(R.id.gv_price);
            holder.tv_name = convertView.findViewById(R.id.tv_name);
            holder.ll_store_name = convertView.findViewById(R.id.ll_store_name);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        if (getItem(position).getType() == 0) {
            holder.ll_store_name.setVisibility(View.VISIBLE);
            holder.tv_store_name.setText(getItem(position).getStoreName());
        } else {
            holder.ll_store_name.setVisibility(View.INVISIBLE);
        }
        if (TextUtils.isEmpty(getItem(position).getImgPath())) {
            Glide.with(convertView).load(R.drawable.icon_loadding_fail).into(holder.iv_shop_img);
        } else {
            Glide.with(convertView).load(getItem(position).getImgPath()).into(holder.iv_shop_img);
        }
        if (TextUtils.isEmpty(getItem(position).getStatus())) {
            holder.tv_status.setVisibility(View.GONE);
            holder.ll_store_name.setVisibility(View.INVISIBLE);
        } else if (getItem(position).getStatus().equals("已下架")) {
            holder.tv_status.setTextColor(mContext.getResources().getColor(R.color.white));
            holder.tv_status.setBackground(mContext.getResources().getDrawable(R.drawable.shape_radius_gray_20));
            holder.tv_status.setText(getItem(position).getStatus());
        } else {
            holder.tv_status.setText(getItem(position).getStatus());
        }
        holder.tv_name.setText(getItem(position).getName());
        // 价格
        SubSaleGoodsAdapter saleGoodsAdapter = new SubSaleGoodsAdapter(mContext, getItem(position).getPriceList());
        holder.gv_price.setAdapter(saleGoodsAdapter);
        holder.gv_price.setNumColumns(3);
        return convertView;
    }


    private class ViewHolder {
        private ImageView iv_shop_img;
        private TextView tv_status, tv_name, tv_store_name;
        private MeasureGridView gv_price;
        private LinearLayout ll_store_name;
    }
}
