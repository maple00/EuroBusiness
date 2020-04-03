package com.rainwood.eurobusiness.ui.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.rainwood.eurobusiness.R;
import com.rainwood.eurobusiness.domain.PurchaseGoodsBean;
import com.rainwood.tools.widget.MeasureListView;

import java.util.List;

/**
 * @Author: a797s
 * @Date: 2020/2/18
 * @Desc:
 */
public class PurchaseContentAdapter extends BaseAdapter {

    private Context mContext;
    private List<PurchaseGoodsBean> mList;

    public PurchaseContentAdapter(Context mContext, List<PurchaseGoodsBean> mList) {
        this.mContext = mContext;
        this.mList = mList;
    }

    @Override
    public int getCount() {
        return mList == null ? 0 : mList.size();
    }

    @Override
    public PurchaseGoodsBean getItem(int position) {
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
            convertView = LayoutInflater.from(mContext).inflate(R.layout.item_purchase_detail, parent, false);
            holder.tv_goods_name = convertView.findViewById(R.id.tv_goods_name);
            holder.tv_model = convertView.findViewById(R.id.tv_model);
            holder.tv_discount = convertView.findViewById(R.id.tv_discount);
            holder.tv_rate = convertView.findViewById(R.id.tv_rate);
            holder.lv_type_list = convertView.findViewById(R.id.lv_type_list);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        holder.tv_goods_name.setText(getItem(position).getName());
        holder.tv_model.setText(getItem(position).getModel());
        holder.tv_discount.setText(getItem(position).getDiscount());
        holder.tv_rate.setText(getItem(position).getRate());
        // 商品按照类型分类
        PurchaseTypeAdapter typeAdapter = new PurchaseTypeAdapter(mContext, getItem(position).getTypeList());
        holder.lv_type_list.setAdapter(typeAdapter);
//        typeAdapter.setOnClickItem(position, onClickItem);
        return convertView;
    }

    private PurchaseTypeAdapter.OnClickItem onClickItem;

    public void setOnClickItem(PurchaseTypeAdapter.OnClickItem onClickItem) {
        this.onClickItem = onClickItem;
    }

    private class ViewHolder {
        private TextView tv_goods_name, tv_model, tv_discount, tv_rate;
        private MeasureListView lv_type_list;
    }
}
