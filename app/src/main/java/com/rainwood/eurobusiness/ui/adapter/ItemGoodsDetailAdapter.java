package com.rainwood.eurobusiness.ui.adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.rainwood.eurobusiness.R;
import com.rainwood.eurobusiness.domain.GoodsDetailBean;
import com.rainwood.tools.widget.MeasureGridView;
import com.rainwood.tools.widget.MeasureListView;

import java.util.List;

/**
 * @Author: a797s
 * @Date: 2020/2/18
 * @Desc: 商品详情 -- 基本信息、商品定价、规格参数、商品图片、促销信息
 */
public class ItemGoodsDetailAdapter extends BaseAdapter {

    private Context mContext;
    private List<GoodsDetailBean> mList;

    public ItemGoodsDetailAdapter(Context mContext, List<GoodsDetailBean> mList) {
        this.mContext = mContext;
        this.mList = mList;
    }

    @Override
    public int getCount() {
        return mList == null ? 0 : mList.size();
    }

    @Override
    public GoodsDetailBean getItem(int position) {
        return mList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public int getItemViewType(int position) {
        return mList.get(position).getType();
    }

    @Override
    public int getViewTypeCount() {
        return 5;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if (convertView == null) {
            holder = new ViewHolder();
            convertView = LayoutInflater.from(mContext).inflate(R.layout.item_goods_detail, parent, false);
            holder.tv_check_all = convertView.findViewById(R.id.tv_check_all);
            holder.tv_title = convertView.findViewById(R.id.tv_title);
            holder.iv_right_arrow = convertView.findViewById(R.id.iv_right_arrow);
            holder.ll_params_view = convertView.findViewById(R.id.ll_params_view);
            holder.lv_comm_list = convertView.findViewById(R.id.lv_comm_list);
            holder.lv_size_list = convertView.findViewById(R.id.lv_size_list);
            holder.gv_img_list = convertView.findViewById(R.id.gv_img_list);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        holder.tv_title.setText(getItem(position).getTitle());
        int itemViewType = getItemViewType(position);
        switch (itemViewType) {
            case 0:                     // 基本信息
            case 1:                     // 商品定价
            case 4:                     // 促销信息
                SubGoodsDetailAdapter detailAdapter = new SubGoodsDetailAdapter(mContext, getItem(position).getCommList());
                holder.lv_comm_list.setAdapter(detailAdapter);
                break;
            case 2:                     // 规格参数
                if (getItem(position).getLoadType() == 101){                    // 库存商品
                    holder.ll_params_view.setVisibility(View.GONE);
                    holder.lv_size_list.setVisibility(View.GONE);
                }else {
                    holder.ll_params_view.setVisibility(View.VISIBLE);
                    holder.lv_size_list.setVisibility(View.VISIBLE);
                }
                SizeAdapter sizeAdapter = new SizeAdapter(mContext, getItem(position).getParamsList());
                holder.lv_size_list.setAdapter(sizeAdapter);
                SubGoodsDetailAdapter paramsAdapter = new SubGoodsDetailAdapter(mContext, getItem(position).getCommList());
                holder.lv_comm_list.setAdapter(paramsAdapter);
                break;
            case 3:                     // 商品图片
                if (getItem(position).getLoadType() == 101){                // 库存商品
                    holder.iv_right_arrow.setVisibility(View.GONE);
                    holder.tv_check_all.setVisibility(View.GONE);
                }else {
                    holder.iv_right_arrow.setVisibility(View.VISIBLE);
                    holder.tv_check_all.setVisibility(View.VISIBLE);
                }
                GoodsImageAdapter imageAdapter = new GoodsImageAdapter(mContext, getItem(position).getImgList());
                holder.gv_img_list.setAdapter(imageAdapter);
                break;
            default:
                break;
        }

        return convertView;
    }

    private class ViewHolder {
        private TextView tv_title, tv_check_all;
        private ImageView iv_right_arrow;
        private LinearLayout ll_params_view;
        private MeasureListView lv_comm_list, lv_size_list;
        private MeasureGridView gv_img_list;
    }
}
