package com.rainwood.eurobusiness.ui.adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.rainwood.eurobusiness.R;
import com.rainwood.eurobusiness.domain.OrderBean;
import com.rainwood.eurobusiness.utils.ListUtils;
import com.rainwood.tools.widget.FlowLayout;
import com.rainwood.tools.widget.MeasureGridView;
import com.rainwood.tools.widget.MeasureListView;

import java.util.List;

/**
 * @Author: a797s
 * @Date: 2020/2/20
 * @Desc: 新建盘点
 */
public class NewStockAdapter extends BaseAdapter {

    private Context mContext;
    private List<OrderBean> mList;

    public NewStockAdapter(Context mContext, List<OrderBean> mList) {
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
    public int getItemViewType(int position) {
        return mList.get(position).getItemType();
    }

    @Override
    public int getViewTypeCount() {
        return 2;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if (convertView == null) {
            holder = new ViewHolder();
            convertView = LayoutInflater.from(mContext).inflate(R.layout.item_new_stock, parent, false);
            holder.tv_title = convertView.findViewById(R.id.tv_title);
            holder.lv_comm_ui = convertView.findViewById(R.id.lv_comm_ui);
            holder.fl_params = convertView.findViewById(R.id.fl_params);
            holder.gv_press_list = convertView.findViewById(R.id.gv_press_list);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        holder.tv_title.setText(getItem(position).getTitle());
        switch (getItemViewType(position)) {
            case 0:                         // 商品信息
                // commUI 布局
                CommUIAdapter commUIAdapter = new CommUIAdapter(mContext, getItem(position).getCommonList());
                holder.lv_comm_ui.setAdapter(commUIAdapter);
                commUIAdapter.setOnClickEditText(position, onClickEditText);
                // 流式布局 -- 向容器中添加 text数据
                /*LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                layoutParams.setMargins(10, 5, 10, 5);
                if (holder.fl_params != null) {
                    holder.fl_params.removeAllViews();
                }
                for (int i = 0; i < ListUtils.size(getItem(position).getPressList()); i++) {
                    TextView tv = new TextView(mContext);
                    tv.setPadding(28, 10, 28, 10);
                    tv.setText(getItem(position).getPressList().get(i).getTitle());
                    tv.setMaxEms(10);
                    tv.setSingleLine();
                    tv.setBackgroundResource(R.drawable.shape_red65_4);
                    tv.setLayoutParams(layoutParams);
                    holder.fl_params.addView(tv, layoutParams);
                }*/
                SubChooseParamsAdapter paramsAdapter = new SubChooseParamsAdapter(mContext, getItem(position).getPressList());
                holder.gv_press_list.setAdapter(paramsAdapter);
                holder.gv_press_list.setNumColumns(3);
                break;
            case 1:                         // 库存信息
                CommUIAdapter stockAdapter = new CommUIAdapter(mContext, getItem(position).getCommonList());
                holder.lv_comm_ui.setAdapter(stockAdapter);
                stockAdapter.setOnClickEditText(position, onClickEditText);
                break;
        }
        return convertView;
    }

    /**
     * CommUi 布局
     */
    private CommUIAdapter.OnClickEditText onClickEditText;
    /**
     * GridView布局
     */
    private SubChooseParamsAdapter.OnClickItem onClickItem;

    public void setOnClickEditText(CommUIAdapter.OnClickEditText onClickEditText) {
        this.onClickEditText = onClickEditText;
    }

    public void setOnClickItem(SubChooseParamsAdapter.OnClickItem onClickItem) {
        this.onClickItem = onClickItem;
    }

    private class ViewHolder {
        private TextView tv_title;
        private MeasureListView lv_comm_ui;
        private FlowLayout fl_params;
        private MeasureGridView gv_press_list;
    }
}
