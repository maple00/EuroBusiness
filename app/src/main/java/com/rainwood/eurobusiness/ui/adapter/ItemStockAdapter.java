package com.rainwood.eurobusiness.ui.adapter;

import android.content.Context;
import android.text.Html;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.rainwood.eurobusiness.R;
import com.rainwood.eurobusiness.domain.StockBean;
import com.rainwood.eurobusiness.domain.StocksBean;

import java.util.List;

/**
 * @Author: a797s
 * @Date: 2020/2/20
 * @Desc:
 */
public class ItemStockAdapter extends BaseAdapter {

   private Context mContext;
    private List<StocksBean> mList;

    public ItemStockAdapter(Context mContext, List<StocksBean> mList) {
        this.mContext = mContext;
        this.mList = mList;
    }

    @Override
    public int getCount() {
        return mList == null ? 0 : mList.size();
    }

    @Override
    public StocksBean getItem(int position) {
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
            convertView = LayoutInflater.from(mContext).inflate(R.layout.item_stock, parent, false);
            holder.iv_img = convertView.findViewById(R.id.iv_img);
            holder.tv_name = convertView.findViewById(R.id.tv_name);
            holder.tv_model = convertView.findViewById(R.id.tv_model);
            holder.tv_params = convertView.findViewById(R.id.tv_params);
            holder.tv_ven_num = convertView.findViewById(R.id.tv_ven_num);
            holder.tv_stock_num = convertView.findViewById(R.id.tv_stock_num);
            holder.tv_status = convertView.findViewById(R.id.tv_status);
            holder.ll_item = convertView.findViewById(R.id.ll_item);
            convertView.setTag(holder);
        }else {
            holder = (ViewHolder) convertView.getTag();
        }
        if (TextUtils.isEmpty(getItem(position).getIco())){
            Glide.with(convertView).load(R.drawable.icon_loadding_fail).into(holder.iv_img);
        }else {
            Glide.with(convertView).load(getItem(position).getIco()).into(holder.iv_img);
        }
        holder.tv_name.setText(getItem(position).getGoodsName());
        holder.tv_model.setText(getItem(position).getModel());
        holder.tv_params.setText(getItem(position).getGoodsSkuName());
        holder.tv_ven_num.setText(Html.fromHtml("<font color=" + mContext.getResources().getColor(R.color.fontColor) + " size='12px'>库存：</font>"
                + "<font color=" + mContext.getResources().getColor(R.color.orange15) + " size='12px'>" + getItem(position).getStock() + "</font>"));
        holder.tv_stock_num.setText(Html.fromHtml("<font color=" + mContext.getResources().getColor(R.color.fontColor) + " size='12px'>盘点：</font>"
                + "<font color=" + mContext.getResources().getColor(R.color.red100) + " size='12px'>" + getItem(position).getNum() + "</font>"));
        holder.tv_status.setText(getItem(position).getState());
        // 点击事件
        holder.ll_item.setOnClickListener(v -> onClickItem.onClickStockItem(position));
        return convertView;
    }

    public interface OnClickItem{
        void onClickStockItem(int position);
    }

    private OnClickItem onClickItem;

    public void setOnClickItem(OnClickItem onClickItem) {
        this.onClickItem = onClickItem;
    }

    private class ViewHolder{
        private ImageView iv_img;
        private TextView tv_name, tv_model, tv_params, tv_ven_num, tv_stock_num, tv_status;
        private LinearLayout ll_item;
    }
}
