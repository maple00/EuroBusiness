package com.rainwood.eurobusiness.ui.adapter;

import android.content.Context;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.rainwood.eurobusiness.R;
import com.rainwood.eurobusiness.domain.ClassifySubBean;

import java.util.List;

/**
 * @Author: shearson
 * @Time: 2020/2/26 17:29
 * @Desc: 批发商 ---  商品管理(商品分类) --- 子级
 */
public class SaleSubClassifyAdapter extends BaseAdapter {

    private Context mContext;
    private List<ClassifySubBean> mList;
    private int parentPos;

    public SaleSubClassifyAdapter(Context mContext, List<ClassifySubBean> mList) {
        this.mContext = mContext;
        this.mList = mList;
    }

    @Override
    public int getCount() {
        return mList == null ? 0 : mList.size();
    }

    @Override
    public ClassifySubBean getItem(int position) {
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
            convertView = LayoutInflater.from(mContext).inflate(R.layout.item_sale_classify_sub, parent, false);
            holder.tv_name = convertView.findViewById(R.id.tv_name);
            holder.tv_status = convertView.findViewById(R.id.tv_status);
            holder.iv_point = convertView.findViewById(R.id.iv_point);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        holder.tv_name.setText(getItem(position).getName());
        if (TextUtils.isEmpty(getItem(position).getState())) {
            holder.tv_status.setVisibility(View.GONE);
        } else {
            holder.tv_status.setVisibility(View.VISIBLE);
            if ("1".equals(getItem(position).getState())){
                holder.tv_status.setVisibility(View.INVISIBLE);
            }else {
                holder.tv_status.setVisibility(View.VISIBLE);
                holder.tv_status.setText("已停用");
            }
        }
        // 点击事件
        holder.iv_point.setOnClickListener(v -> {
            onClickPoint.onClickPoint(parentPos, position);
            notifyDataSetChanged();
        });
        return convertView;
    }

    public interface OnClickPoint {
        void onClickPoint(int parentPos, int position);
    }

    private OnClickPoint onClickPoint;

    public void setOnClickPoint(int parentPos, OnClickPoint onClickPoint) {
        this.parentPos = parentPos;
        this.onClickPoint = onClickPoint;
    }

    private class ViewHolder {
        private TextView tv_name, tv_status;
        private ImageView iv_point;
    }

}
