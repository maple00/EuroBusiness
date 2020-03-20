package com.rainwood.eurobusiness.ui.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.TextView;

import com.rainwood.eurobusiness.R;
import com.rainwood.eurobusiness.domain.OrderBean;
import com.rainwood.tools.widget.MeasureGridView;
import com.rainwood.tools.widget.MeasureListView;

import java.util.List;

/**
 * @Author: a797s
 * @Date: 2020/2/19
 * @Desc: 订单管理  --- 添加商品
 */
public class ItemOrderAddAdapter extends BaseAdapter {

    private Context mContext;
    private List<OrderBean> mList;

    public ItemOrderAddAdapter(Context mContext, List<OrderBean> mList) {
        this.mContext = mContext;
        this.mList = mList;
    }

    @Override
    public int getCount() {
        return mList == null ? 0 : mList.size();
    }

    @Override
    public OrderBean getItem(int position) {
        return mList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public int getViewTypeCount() {
        return 2;
    }

    @Override
    public int getItemViewType(int position) {
        return mList.get(position).getItemType();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if (convertView == null) {
            holder = new ViewHolder();
            convertView = LayoutInflater.from(mContext).inflate(R.layout.item_order_add_shop, parent, false);
            holder.tv_title = convertView.findViewById(R.id.tv_title);
            holder.lv_comm_list = convertView.findViewById(R.id.lv_comm_list);
            holder.gv_press_list = convertView.findViewById(R.id.gv_press_list);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        holder.tv_title.setText(getItem(position).getTitle());
        switch (getItem(position).getItemType()) {
            case 0:
                CommUIAdapter commUIAdapter = new CommUIAdapter(mContext, getItem(position).getCommonList());
                holder.lv_comm_list.setAdapter(commUIAdapter);
                commUIAdapter.setOnClickEditText(position, onClickEditText);
                break;
            case 1:
                SubChooseParamsAdapter paramsAdapter = new SubChooseParamsAdapter(mContext, getItem(position).getPressList());
                holder.gv_press_list.setAdapter(paramsAdapter);
                holder.gv_press_list.setNumColumns(4);
                paramsAdapter.setOnClickItem(position, onClickItem);
                break;
        }
        return convertView;
    }

    private CommUIAdapter.OnClickEditText onClickEditText;
    private SubChooseParamsAdapter.OnClickItem onClickItem;

    public void setOnClickEditText(CommUIAdapter.OnClickEditText onClickEditText) {
        this.onClickEditText = onClickEditText;
    }

    public void setOnClickItem(SubChooseParamsAdapter.OnClickItem onClickItem) {
        this.onClickItem = onClickItem;
    }

    private class ViewHolder {
        private TextView tv_title;
        private MeasureListView lv_comm_list;
        private MeasureGridView gv_press_list;
    }
}
