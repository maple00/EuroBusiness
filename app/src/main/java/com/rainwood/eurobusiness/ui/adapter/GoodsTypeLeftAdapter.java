package com.rainwood.eurobusiness.ui.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.rainwood.eurobusiness.R;
import com.rainwood.eurobusiness.domain.SelectedGoodsBean;

import java.util.List;

/**
 * @Author: a797s
 * @Date: 2020/2/12
 * @Desc: 商品分类联动左边 --- leftType
 */
public class GoodsTypeLeftAdapter extends BaseAdapter {

    private Context mContext;
    private List<SelectedGoodsBean> mList;

    public GoodsTypeLeftAdapter(Context mContext, List<SelectedGoodsBean> mList) {
        this.mContext = mContext;
        this.mList = mList;
    }

    @Override
    public int getCount() {
        return mList == null ? 0 : mList.size();
    }

    @Override
    public SelectedGoodsBean getItem(int position) {
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
            convertView = LayoutInflater.from(mContext).inflate(R.layout.activity_goods_type_left, parent, false);
            holder.tv_ver_line = convertView.findViewById(R.id.tv_ver_line);
            holder.tv_type = convertView.findViewById(R.id.tv_type);
            holder.ll_item = convertView.findViewById(R.id.ll_item);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        holder.tv_type.setText(getItem(position).getType());
        if (getItem(position).isChoose()) {
            holder.ll_item.setBackgroundResource(R.color.white);
            holder.tv_ver_line.setVisibility(View.VISIBLE);
        } else {
            holder.ll_item.setBackgroundResource(R.color.windowBackground);
            holder.tv_ver_line.setVisibility(View.GONE);
        }
        // 点击事件
        holder.ll_item.setOnClickListener(v -> {
            leftListener.onClickLeftType(position);
            notifyDataSetChanged();
        });
        return convertView;
    }

    public interface OnClickLeftListener {
        void onClickLeftType(int position);
    }

    private OnClickLeftListener leftListener;

    public void setLeftListener(OnClickLeftListener leftListener) {
        this.leftListener = leftListener;
    }

    private class ViewHolder {
        private TextView tv_ver_line, tv_type;
        private LinearLayout ll_item;
    }
}
