package com.rainwood.eurobusiness.ui.adapter;

import android.content.Context;
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
import com.rainwood.eurobusiness.domain.InventoryOutBean;
import com.rainwood.eurobusiness.domain.OutBoundBean;
import com.rainwood.tools.viewinject.OnClick;

import java.util.List;

/**
 * @Author: a797s
 * @Date: 2020/2/20
 * @Desc: 出库记录 content
 */
public class ItemOutBoundAdapter extends BaseAdapter {

    private Context mContext;
    private List<InventoryOutBean> mList;

    public ItemOutBoundAdapter(Context mContext, List<InventoryOutBean> mList) {
        this.mContext = mContext;
        this.mList = mList;
    }

    @Override
    public int getCount() {
        return mList == null ? 0 : mList.size();
    }

    @Override
    public InventoryOutBean getItem(int position) {
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
            convertView = LayoutInflater.from(mContext).inflate(R.layout.item_out_bound, parent, false);
            holder.iv_img = convertView.findViewById(R.id.iv_img);
            holder.tv_name = convertView.findViewById(R.id.tv_name);
            holder.tv_model = convertView.findViewById(R.id.tv_model);
            holder.tv_params = convertView.findViewById(R.id.tv_params);
            holder.tv_order_source = convertView.findViewById(R.id.tv_order_source);
            holder.tv_num = convertView.findViewById(R.id.tv_num);
            holder.ll_item = convertView.findViewById(R.id.ll_item);
            holder.ll_saler_address = convertView.findViewById(R.id.ll_saler_address);
            holder.tv_address = convertView.findViewById(R.id.tv_address);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        if (TextUtils.isEmpty(getItem(position).getIco())) {
            Glide.with(convertView).load(R.drawable.icon_loadding_fail).into(holder.iv_img);
        } else {
            Glide.with(convertView).load(getItem(position).getIco()).into(holder.iv_img);
        }
        holder.tv_name.setText(getItem(position).getGoodsName());
        holder.tv_model.setText(getItem(position).getModel());
        holder.tv_params.setText(getItem(position).getSkuName());
        holder.tv_order_source.setText(getItem(position).getWorkFlow());
        holder.tv_num.setText(getItem(position).getNum());
        if (TextUtils.isEmpty(getItem(position).getStoreName())){
            holder.ll_saler_address.setVisibility(View.GONE);
        }else {
            holder.tv_address.setText(getItem(position).getStoreName());
        }
        // 点击事件
        holder.ll_item.setOnClickListener(v -> onClickItem.onClickItem(position));
        return convertView;
    }

    public interface OnClickItem{
        void onClickItem(int position);
    }

    private OnClickItem onClickItem;

    public void setOnClickItem(OnClickItem onClickItem) {
        this.onClickItem = onClickItem;
    }

    private class ViewHolder {
        private ImageView iv_img;
        private TextView tv_name, tv_model, tv_params, tv_order_source, tv_num, tv_address;
        private LinearLayout ll_item, ll_saler_address;
    }
}
