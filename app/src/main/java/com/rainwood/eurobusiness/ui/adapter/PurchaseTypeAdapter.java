package com.rainwood.eurobusiness.ui.adapter;

import android.content.Context;
import android.text.Html;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.rainwood.eurobusiness.R;
import com.rainwood.eurobusiness.domain.PurchaseTypeBean;

import java.util.List;

/**
 * @Author: a797s
 * @Date: 2020/2/18
 * @Desc: 商品类别 ----  采购信息按照类别分类
 */
public class PurchaseTypeAdapter extends BaseAdapter {

    private Context mContext;
    private List<PurchaseTypeBean> mList;

    public PurchaseTypeAdapter(Context mContext, List<PurchaseTypeBean> mList) {
        this.mContext = mContext;
        this.mList = mList;
    }

    @Override
    public int getCount() {
        return mList == null ? 0 : mList.size();
    }

    @Override
    public PurchaseTypeBean getItem(int position) {
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
            convertView = LayoutInflater.from(mContext).inflate(R.layout.sub_item_purchase_type, parent, false);
            holder.iv_img = convertView.findViewById(R.id.iv_img);
            holder.tv_param_size = convertView.findViewById(R.id.tv_param_size);
            holder.tv_price = convertView.findViewById(R.id.tv_price);
            holder.tv_purchase = convertView.findViewById(R.id.tv_purchase);
            holder.tv_in_storage = convertView.findViewById(R.id.tv_in_storage);
            holder.tv_return_num = convertView.findViewById(R.id.tv_return_num);
            holder.tv_all_money = convertView.findViewById(R.id.tv_all_money);
            holder.tv_storage = convertView.findViewById(R.id.tv_storage);
            holder.tv_return_goods = convertView.findViewById(R.id.tv_return_goods);
            holder.iv_selector = convertView.findViewById(R.id.iv_selector);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        Glide.with(convertView)
                .load(TextUtils.isEmpty(getItem(position).getIco()) ? mContext.getResources().getDrawable(R.drawable.icon_loadding_fail) : getItem(position).getIco())
                .into(holder.iv_img);
        // 不是混装
        holder.tv_param_size.setText("0".equals(getItem(position).getIsSku()) ? getItem(position).getGoodsSkuNAme() : "混装");

        holder.tv_price.setText(getItem(position).getPrice());
        holder.tv_purchase.setText(Html.fromHtml("<font color=" + mContext.getResources().getColor(R.color.fontColor) + " size='13px'>采购&#160;</font>"
                + "<font color=" + mContext.getResources().getColor(R.color.textColor) + " size='13px'>"
                + (getItem(position).getNum() == null ? "0" : getItem(position).getNum()) + "</font>"));
        holder.tv_in_storage.setText(Html.fromHtml("<font color=" + mContext.getResources().getColor(R.color.fontColor) + " size='13px'>入库&#160;</font>"
                + "<font color=" + mContext.getResources().getColor(R.color.textColor) + " size='13px'>"
                + (getItem(position).getInStoreNum() == null ? "0" : getItem(position).getInStoreNum()) + "</font>"));
        holder.tv_return_num.setText(Html.fromHtml("<font color=" + mContext.getResources().getColor(R.color.fontColor) + " size='13px'>退货&#160;</font>"
                + "<font color=" + mContext.getResources().getColor(R.color.textColor) + " size='13px'>"
                + (getItem(position).getRefundNum() == null ? "0" : getItem(position).getRefundNum()) + "</font>"));
        holder.tv_all_money.setText(Html.fromHtml("<font color=" + mContext.getResources().getColor(R.color.fontColor) + " size='13px'>合计：</font>"
                + "<font color=" + mContext.getResources().getColor(R.color.textColor) + " size='13px'>"
                + (getItem(position).getTotalMoney() == null ? "0" : getItem(position).getTotalMoney()) + "</font>"));
        if (!"0".equals(getItem(position).getTotalMoney())) {
            holder.tv_return_goods.setVisibility(View.VISIBLE);
        } else {
            holder.tv_return_goods.setVisibility(View.GONE);
        }

        // 点击退货
        holder.tv_return_goods.setText("退货");
        holder.tv_return_goods.setOnClickListener(v -> {
            onClickItem.onClickReturnGoods(position);
            notifyDataSetChanged();
        });
        // 点击入库
        holder.tv_storage.setText("入库");
        holder.tv_storage.setOnClickListener(v -> {
            onClickItem.onClickInStorage(position);
            notifyDataSetChanged();
        });
        // 点击选中
        if (getItem(position).isBulkSelect()) {           // 是否批量选中
            holder.iv_selector.setVisibility(View.VISIBLE);
        } else {
            holder.iv_selector.setVisibility(View.GONE);
        }
        if (getItem(position).isSelected()) {            // 选中
            holder.iv_selector.setImageResource(R.drawable.radio_checked_shape);
        } else {
            holder.iv_selector.setImageResource(R.drawable.radio_uncheck_shape);
        }
        // 设置全选
        holder.iv_selector.setOnClickListener(v -> {
            onClickItem.onClickChecked(position);
            notifyDataSetChanged();
        });
        return convertView;
    }


    public interface OnClickItem {
        // 退货
        void onClickReturnGoods(int position);

        // 入库
        void onClickInStorage(int position);

        // 选中
        void onClickChecked(int position);
    }

    private OnClickItem onClickItem;

    public void setOnClickItem(OnClickItem onClickItem) {
        this.onClickItem = onClickItem;
    }

    private class ViewHolder {
        private ImageView iv_img, iv_selector;
        private TextView tv_param_size, tv_price, tv_purchase, tv_in_storage, tv_return_num,
                tv_all_money, tv_storage, tv_return_goods;
    }
}
