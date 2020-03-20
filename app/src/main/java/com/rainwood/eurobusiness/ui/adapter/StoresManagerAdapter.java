package com.rainwood.eurobusiness.ui.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.rainwood.eurobusiness.R;
import com.rainwood.eurobusiness.domain.StoresBean;

import java.util.List;

/**
 * @Author: shearson
 * @Time: 2020/2/24 23:09
 * @Desc: 门店管理
 */
public class StoresManagerAdapter extends BaseAdapter {

    private Context mContext;
    private List<StoresBean> mList;

    public StoresManagerAdapter(Context mContext, List<StoresBean> mList) {
        this.mContext = mContext;
        this.mList = mList;
    }

    @Override
    public int getCount() {
        return mList == null ? 0 : mList.size();
    }

    @Override
    public StoresBean getItem(int position) {
        return mList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @SuppressLint("SetTextI18n")
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if (convertView == null) {
            holder = new ViewHolder();
            convertView = LayoutInflater.from(mContext).inflate(R.layout.item_stores_list, parent, false);
            holder.tv_name = convertView.findViewById(R.id.tv_name);
            holder.tv_id = convertView.findViewById(R.id.tv_id);
            holder.iv_point = convertView.findViewById(R.id.iv_point);
            holder.tv_delete_store = convertView.findViewById(R.id.tv_delete_store);
            holder.rl_item = convertView.findViewById(R.id.rl_item);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        holder.tv_name.setText(getItem(position).getName());
        holder.tv_id.setText("编号：" + getItem(position).getId());
        // 点击事件
        holder.iv_point.setOnClickListener(v -> {
            onClickPoint.onClickPoint(position);
            if (getItem(position).isDelete()) {         // 显示删除
                holder.tv_delete_store.setVisibility(View.VISIBLE);
                holder.tv_delete_store.setOnClickListener(v1 -> {
                    mList.remove(position);
                    holder.tv_delete_store.setVisibility(View.INVISIBLE);
                    notifyDataSetChanged();
                });
            } else {
                holder.tv_delete_store.setVisibility(View.INVISIBLE);
            }
        });
        holder.rl_item.setOnClickListener(v -> onClickPoint.onCliCkDetail(position));
        return convertView;
    }

    public interface OnClickPoint {
        void onClickPoint(int position);

        // 查看详情
        void onCliCkDetail(int position);
    }

    private OnClickPoint onClickPoint;

    public void setOnClickPoint(OnClickPoint onClickPoint) {
        this.onClickPoint = onClickPoint;
    }

    private class ViewHolder {
        private TextView tv_name, tv_id, tv_delete_store;
        private ImageView iv_point;
        private RelativeLayout rl_item;
    }

}
