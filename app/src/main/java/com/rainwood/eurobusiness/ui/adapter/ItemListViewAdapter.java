package com.rainwood.eurobusiness.ui.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.TextView;

import com.rainwood.eurobusiness.R;
import com.rainwood.eurobusiness.domain.ItemGridBean;
import com.rainwood.eurobusiness.domain.ItemIndexListViewBean;

import java.util.List;

/**
 * @Author: a797s
 * @Date: 2019/12/12 17:43
 * @Desc: 首页ListView
 */
public class ItemListViewAdapter extends BaseAdapter {

    private Context mContext;
    private List<ItemIndexListViewBean> mList;

    public ItemListViewAdapter(Context mContext, List<ItemIndexListViewBean> mList) {
        this.mContext = mContext;
        this.mList = mList;
    }

    @Override
    public int getCount() {
        return mList == null ? 0 : mList.size();
    }

    @Override
    public List<ItemGridBean> getItem(int position) {
        return mList.get(position).getGridViewList();
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if (convertView == null) {
            convertView = LayoutInflater.from(mContext).inflate(R.layout.item_fragment_index, parent, false);
            holder = new ViewHolder();
            holder.tv_title = convertView.findViewById(R.id.tv_title);
            holder.gv_view = convertView.findViewById(R.id.gv_view);

            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        holder.tv_title.setText(mList.get(position).getTitle());

        ItemGridViewAdapter childAdapter = new ItemGridViewAdapter(mContext, mList, position, getItem(position));
        childAdapter.setImgClick(itemTouchClick);
        holder.gv_view.setNumColumns(4);        // 设置GridView的每行的显示
        holder.gv_view.setAdapter(childAdapter);
        return convertView;
    }

    // 子项的点击事件
    private ItemGridViewAdapter.ImgClick itemTouchClick;

    public void setItemTouchClick(ItemGridViewAdapter.ImgClick itemTouchClick) {
        this.itemTouchClick = itemTouchClick;
    }

    private class ViewHolder {
        private TextView tv_title;
        private GridView gv_view;
    }
}
