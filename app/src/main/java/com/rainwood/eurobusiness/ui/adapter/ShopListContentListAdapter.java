package com.rainwood.eurobusiness.ui.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.rainwood.eurobusiness.R;
import com.rainwood.eurobusiness.domain.ShopBean;

import java.util.List;

/**
 * @Author: a797s
 * @Date: 2020/2/8
 * @Desc:
 */
public class ShopListContentListAdapter extends BaseAdapter {

    private Context mContext;
    private List<ShopBean> mList;

    public ShopListContentListAdapter(Context mContext, List<ShopBean> mList) {
        this.mContext = mContext;
        this.mList = mList;
    }

    @Override
    public int getCount() {
        return mList == null ? 0 : mList.size();
    }

    @Override
    public ShopBean getItem(int position) {
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
            convertView = LayoutInflater.from(mContext).inflate(R.layout.item_shop_content, parent, false);
            holder.iv_shop_img = convertView.findViewById(R.id.iv_shop_img);
            holder.tv_status = convertView.findViewById(R.id.tv_status);
            holder.tv_name = convertView.findViewById(R.id.tv_name);
            holder.tv_wholesale_price = convertView.findViewById(R.id.tv_wholesale_price);
            holder.tv_retail_price = convertView.findViewById(R.id.tv_retail_price);
            holder.tv_source = convertView.findViewById(R.id.tv_source);
            holder.btn_edit = convertView.findViewById(R.id.btn_edit);
            holder.btn_shelves = convertView.findViewById(R.id.btn_shelves);
            holder.rl_right_item = convertView.findViewById(R.id.rl_right_item);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        Glide.with(convertView).load(getItem(position).getImgPath()).into(holder.iv_shop_img);
        holder.tv_name.setText(getItem(position).getName());
        holder.tv_wholesale_price.setText(getItem(position).getWholesalePrice());
        holder.tv_retail_price.setText(getItem(position).getRetailPrice());
        // 商品状态分为三种：已在售、已下架和草稿
        if (getItem(position).getStatus().equals("在售中")) {       // 只显示下架
            holder.tv_status.setVisibility(View.VISIBLE);
            holder.tv_source.setVisibility(View.VISIBLE);
            holder.tv_status.setText(getItem(position).getStatus());
            holder.tv_source.setText(getItem(position).getSource());
            // button
            holder.btn_edit.setVisibility(View.GONE);
            holder.btn_shelves.setText("下架");
            holder.btn_shelves.setTextColor(mContext.getResources().getColor(R.color.textColor));
            holder.btn_shelves.setBackgroundResource(R.drawable.shape_radius_gray_14);
            // 状态背景
            holder.tv_status.setBackgroundResource(R.drawable.shape_radius_20);
        } else if (getItem(position).getStatus().equals("已下架")) {     // 显示编辑和上架
            holder.tv_status.setVisibility(View.VISIBLE);
            holder.tv_source.setVisibility(View.VISIBLE);
            holder.tv_status.setText(getItem(position).getStatus());
            holder.tv_source.setText(getItem(position).getSource());
            // button
            holder.btn_edit.setVisibility(View.VISIBLE);
            holder.btn_edit.setText("编辑");
            holder.btn_shelves.setText("上架");
            holder.btn_shelves.setTextColor(mContext.getResources().getColor(R.color.red30));
            holder.btn_shelves.setBackgroundResource(R.drawable.shape_radius_red_14);
            // 状态背景
            holder.tv_status.setBackgroundResource(R.drawable.shape_radius_gray_20);
        } else {                                                     // 草稿
            holder.tv_status.setVisibility(View.GONE);
            holder.tv_source.setVisibility(View.GONE);
            // button
            holder.btn_edit.setVisibility(View.VISIBLE);
            holder.btn_edit.setText("编辑");
            holder.btn_shelves.setText("上架");
            holder.btn_shelves.setTextColor(mContext.getResources().getColor(R.color.red30));
            holder.btn_shelves.setBackgroundResource(R.drawable.shape_radius_red_14);
            // 状态背景
            holder.tv_status.setBackgroundResource(R.drawable.shape_radius_gray_20);
        }

        // 点击事件--- 上/下架、编辑、查看大图
        // 编辑
        holder.btn_edit.setOnClickListener(v -> iOnShopItemClick.onEdit(position));
        // 上/下架
        holder.btn_shelves.setOnClickListener(v -> iOnShopItemClick.onShelves(position));
        // 查看大图
        holder.iv_shop_img.setOnClickListener(v -> iOnShopItemClick.onBigPic(position));
        // 查看详情
        holder.rl_right_item.setOnClickListener(v -> iOnShopItemClick.onClickDetail(position));
        return convertView;
    }

    public interface IOnShopItemClick {
        // 上/下架
        void onShelves(int position);

        // 编辑
        void onEdit(int position);

        // 查看大图
        void onBigPic(int position);

        // 查看详情
        void onClickDetail(int position);
    }

    private IOnShopItemClick iOnShopItemClick;

    public void setiOnShopItemClick(IOnShopItemClick iOnShopItemClick) {
        this.iOnShopItemClick = iOnShopItemClick;
    }

    private class ViewHolder {
        private ImageView iv_shop_img;
        private TextView tv_status, tv_name, tv_wholesale_price, tv_retail_price, tv_source;
        private Button btn_edit, btn_shelves;
        private RelativeLayout rl_right_item;
    }
}
