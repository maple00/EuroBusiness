package com.rainwood.eurobusiness.ui.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.rainwood.eurobusiness.R;
import com.rainwood.eurobusiness.domain.ItemGridBean;
import com.rainwood.eurobusiness.domain.ItemIndexListViewBean;

import java.util.List;

/**
 * @Author: a797s
 * @Date: 2019/12/4 15:54
 * @Desc: 首页子项布局
 */
public class ItemGridViewAdapter extends BaseAdapter {

    private List<ItemGridBean> mList;
    private LayoutInflater mLayoutInflater;
    private List<ItemIndexListViewBean> parentList;
    private int parentPosition;

    ItemGridViewAdapter(Context context, List<ItemIndexListViewBean> parentList, int parentPosition, List<ItemGridBean> list) {
        this.mLayoutInflater = LayoutInflater.from(context);
        this.parentList = parentList;
        this.parentPosition = parentPosition;
        this.mList = list;
    }

    @Override
    public int getCount() {
        return mList == null ? 0 : mList.size();
    }

    @Override
    public ItemGridBean getItem(int position) {
        return mList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if (convertView == null){
            // 填充子类布局
            convertView = mLayoutInflater.inflate(R.layout.item_grid_view_list, parent, false);
            holder = new ViewHolder();
            // 获取控件对象
            holder.iv_img = convertView.findViewById(R.id.iv_img);
            holder.tv_text = convertView.findViewById(R.id.tv_text);
            holder.ll_module = convertView.findViewById(R.id.ll_module);
            convertView.setTag(holder);
        }else {
            holder = (ViewHolder) convertView.getTag();
        }

        // 设置属性
        holder.iv_img.setBackgroundResource(mList.get(position).getImgId());
        holder.tv_text.setText(mList.get(position).getItemName());

        // item的点击事件
        holder.ll_module.setOnClickListener(v -> imgClick.onClick(parentList, parentPosition, position));
        return convertView;
    }

    // 给图片添加点击事件
    public interface ImgClick{
        void onClick(List<ItemIndexListViewBean> parent, int parentPosition, int childPosition);
    }

    private ImgClick imgClick;

    void setImgClick(ImgClick imgClick) {
        this.imgClick = imgClick;
    }

    class ViewHolder{
        ImageView iv_img;
        TextView tv_text;
        private LinearLayout ll_module;
    }
}
