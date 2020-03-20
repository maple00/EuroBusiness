package com.rainwood.eurobusiness.ui.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.rainwood.eurobusiness.R;
import com.rainwood.eurobusiness.domain.PressBean;

import java.util.List;

/**
 * @Author: a797s
 * @Date: 2020/2/20
 * @Desc: 头部类别 分类
 */
public class TopTypeAdapter extends BaseAdapter {

    private Context mContext;
    private List<PressBean> mList;

    public TopTypeAdapter(Context mContext, List<PressBean> mList) {
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
            convertView = LayoutInflater.from(mContext).inflate(R.layout.item_top_type, parent, false);
            holder.tv_title = convertView.findViewById(R.id.tv_title);
            holder.tv_line = convertView.findViewById(R.id.tv_line);
            holder.ll_item = convertView.findViewById(R.id.ll_item);
            convertView.setTag(holder);
        }else {
            holder = (ViewHolder) convertView.getTag();
        }
        holder.tv_title.setText(getItem(position).getTitle());
        if (getItem(position).isChoose()){
            holder.tv_line.setVisibility(View.VISIBLE);
        }else {
            holder.tv_line.setVisibility(View.INVISIBLE);
        }
        // 点击事件
        holder.ll_item.setOnClickListener(v -> {
            onClickItem.onClickItem(position);
            notifyDataSetChanged();
        });
        return convertView;
    }

    public interface OnClickItem{
        void onClickItem(int position);
    }

    private OnClickItem onClickItem;

    public void setOnClickItem(OnClickItem onClickItem) {
        this.onClickItem = onClickItem;
    }

    private class ViewHolder{
        private TextView tv_title, tv_line;
        private LinearLayout ll_item;
    }
}
