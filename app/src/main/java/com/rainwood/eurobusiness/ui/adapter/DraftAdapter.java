package com.rainwood.eurobusiness.ui.adapter;

import android.content.Context;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
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
 * @Time: 2020/2/26 16:15
 * @Desc: 草稿列表
 */
public class DraftAdapter extends BaseAdapter {

    private Context mContext;
    private List<SaleGoodsBean> mList;

    public DraftAdapter(Context mContext, List<SaleGoodsBean> mList) {
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
            convertView = LayoutInflater.from(mContext).inflate(R.layout.item_goods_draft, parent, false);
            holder.iv_shop_img = convertView.findViewById(R.id.iv_shop_img);
            holder.tv_name = convertView.findViewById(R.id.tv_name);
            holder.gv_price = convertView.findViewById(R.id.gv_price);
            holder.btn_edit = convertView.findViewById(R.id.btn_edit);
            holder.btn_shelves = convertView.findViewById(R.id.btn_shelves);
            holder.ll_item = convertView.findViewById(R.id.ll_item);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        if (TextUtils.isEmpty(getItem(position).getImgPath())) {
            Glide.with(convertView).load(R.drawable.icon_loadding_fail).into(holder.iv_shop_img);
        } else {
            Glide.with(convertView).load(getItem(position).getImgPath()).into(holder.iv_shop_img);
        }
        holder.tv_name.setText(getItem(position).getName());
        SubSaleGoodsAdapter priceAdapter = new SubSaleGoodsAdapter(mContext, getItem(position).getPriceList());
        holder.gv_price.setNumColumns(3);
        holder.gv_price.setAdapter(priceAdapter);
        // 点击事件
        holder.ll_item.setOnClickListener(v -> onClickItem.onClickDetail(position));
        holder.btn_edit.setText("编辑");
        holder.btn_edit.setOnClickListener(v -> onClickItem.onClickEdit(position));
        holder.btn_shelves.setText("提交");
        holder.btn_shelves.setOnClickListener(v -> onClickItem.onClickCommit(position));
        return convertView;
    }

    public interface OnClickItem {
        // 查看详情
        void onClickDetail(int position);

        // 编辑
        void onClickEdit(int position);

        // 提交
        void onClickCommit(int position);
    }

    private OnClickItem onClickItem;

    public void setOnClickItem(OnClickItem onClickItem) {
        this.onClickItem = onClickItem;
    }

    private class ViewHolder {
        private LinearLayout ll_item;
        private ImageView iv_shop_img;
        private TextView tv_name;
        private MeasureGridView gv_price;
        private Button btn_edit, btn_shelves;
    }
}
