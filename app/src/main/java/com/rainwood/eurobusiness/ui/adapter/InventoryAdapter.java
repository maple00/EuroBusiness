package com.rainwood.eurobusiness.ui.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.rainwood.eurobusiness.R;
import com.rainwood.eurobusiness.domain.ShopBean;
import com.rainwood.eurobusiness.domain.StockBean;

import java.util.List;

/**
 * @Author: a797s
 * @Date: 2020/2/18
 * @Desc: 库存商品 content
 */
public class InventoryAdapter extends BaseAdapter {

    private Context mContext;
    private List<StockBean> mList;

    public InventoryAdapter(Context mContext, List<StockBean> mList) {
        this.mContext = mContext;
        this.mList = mList;
    }

    @Override
    public int getCount() {
        return mList == null ? 0 : mList.size();
    }

    @Override
    public StockBean getItem(int position) {
        return mList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @SuppressLint("SetTextI18n")
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if (convertView == null) {
            holder = new ViewHolder();
            convertView = LayoutInflater.from(mContext).inflate(R.layout.item_inventory_content, parent, false);
            holder.iv_img = convertView.findViewById(R.id.iv_img);
            holder.tv_name = convertView.findViewById(R.id.tv_name);
            holder.tv_model = convertView.findViewById(R.id.tv_model);
            holder.tv_inventory_num = convertView.findViewById(R.id.tv_inventory_num);
            holder.tv_wholesale_price = convertView.findViewById(R.id.tv_wholesale_price);
            holder.tv_retail_price = convertView.findViewById(R.id.tv_retail_price);
            holder.ll_item = convertView.findViewById(R.id.ll_item);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        // holder.iv_img.setImageResource(getItem(position).getImgPath());
        Glide.with(convertView).load(getItem(position).getIco()).into(holder.iv_img);
        holder.tv_name.setText(getItem(position).getName());
        holder.tv_model.setText(getItem(position).getModel());
        holder.tv_inventory_num.setText("库存" + (getItem(position).getStock()==null ? 0 :getItem(position).getStock()) + "件");
        holder.tv_wholesale_price.setText(getItem(position).getTradePrice());
        holder.tv_retail_price.setText(getItem(position).getRetailPrice());
        // 点击事件
        holder.ll_item.setOnClickListener(v -> onClickItem.onClickItem(position));
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
        private ImageView iv_img;
        private TextView tv_name, tv_model, tv_inventory_num, tv_wholesale_price, tv_retail_price;
        private LinearLayout ll_item;
    }
}
