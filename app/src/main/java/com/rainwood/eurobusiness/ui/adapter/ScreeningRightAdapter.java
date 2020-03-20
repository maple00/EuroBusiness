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
import com.rainwood.eurobusiness.domain.PressBean;

import java.util.List;

/**
 * @Author: a797s
 * @Date: 2020/2/17
 * @Desc: 筛选商品右边
 */
public class ScreeningRightAdapter extends BaseAdapter {

    private Context mContext;
    private List<PressBean> mList;

    public ScreeningRightAdapter(Context mContext, List<PressBean> mList) {
        this.mContext = mContext;
        this.mList = mList;
    }

    @Override
    public int getCount() {
        return mList == null ? 0 : mList.size();
    }

    @Override
    public PressBean getItem(int position) {
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
            convertView = LayoutInflater.from(mContext).inflate(R.layout.item_goods_screening_right, parent, false);
            holder.ll_item = convertView.findViewById(R.id.ll_item);
            holder.tv_title = convertView.findViewById(R.id.tv_title);
            holder.iv_checked = convertView.findViewById(R.id.iv_checked);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        holder.tv_title.setText(getItem(position).getTitle());
        if (getItem(position).isChoose()) {
            holder.iv_checked.setImageResource(R.drawable.ic_choose);
        } else {
            holder.iv_checked.setImageResource(R.drawable.radio_uncheck_shape);
        }
        // 点击事件
        holder.ll_item.setOnClickListener(v -> {
            onClickRight.onClickRight(position);
            notifyDataSetChanged();
        });
        return convertView;
    }

    public interface OnClickRight {
        void onClickRight(int position);
    }

    private OnClickRight onClickRight;

    public void setOnClickRight(OnClickRight onClickRight) {
        this.onClickRight = onClickRight;
    }

    private class ViewHolder {
        private LinearLayout ll_item;
        private TextView tv_title;
        private ImageView iv_checked;
    }

}
