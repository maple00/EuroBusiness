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
 * @Date: 2020/2/12
 * @Desc: 选择商品类型 -- 右边
 */
public class GoodsTypeRightAdapter extends BaseAdapter {

    private Context mContext;
    private List<PressBean> mList;

    public GoodsTypeRightAdapter(Context mContext, List<PressBean> mList) {
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
        if (convertView == null){
            holder = new ViewHolder();
            convertView = LayoutInflater.from(mContext).inflate(R.layout.activity_goods_type_right, parent, false);
            holder.ll_sub_item = convertView.findViewById(R.id.ll_sub_item);
            holder.tv_sub_type = convertView.findViewById(R.id.tv_sub_type);
            holder.iv_checked = convertView.findViewById(R.id.iv_checked);
            convertView.setTag(holder);
        }else {
            holder = (ViewHolder) convertView.getTag();
        }
        holder.tv_sub_type.setText(getItem(position).getTitle());
        if (getItem(position).isChoose()){      // 显示图标
            holder.tv_sub_type.setTextColor(mContext.getResources().getColor(R.color.red30));
            holder.iv_checked.setVisibility(View.VISIBLE);
        }else {
            holder.tv_sub_type.setTextColor(mContext.getResources().getColor(R.color.white66));
            holder.iv_checked.setVisibility(View.GONE);
        }
        // 点击事件
        holder.ll_sub_item.setOnClickListener(v -> {
            itemListener.onClickSubItem(position);
            notifyDataSetChanged();
        });
        return convertView;
    }

    public interface OnSubItemListener{
        void onClickSubItem(int position);
    }

    private OnSubItemListener itemListener;

    public void setItemListener(OnSubItemListener itemListener) {
        this.itemListener = itemListener;
    }

    private class ViewHolder{
        private TextView tv_sub_type;
        private ImageView iv_checked;
        private LinearLayout ll_sub_item;
    }
}
