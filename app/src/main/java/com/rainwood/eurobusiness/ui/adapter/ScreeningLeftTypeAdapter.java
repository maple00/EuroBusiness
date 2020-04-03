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
import com.rainwood.eurobusiness.domain.PressTypeBean;

import java.util.List;

/**
 * @Author: a797s
 * @Date: 2020/2/17
 * @Desc: 筛选左边菜单----
 */
public class ScreeningLeftTypeAdapter extends BaseAdapter {

    private Context mContext;
    private List<PressTypeBean> mList;

    public ScreeningLeftTypeAdapter(Context mContext, List<PressTypeBean> mList) {
        this.mContext = mContext;
        this.mList = mList;
    }

    @Override
    public int getCount() {
        return mList == null ? 0 : mList.size();
    }

    @Override
    public PressTypeBean getItem(int position) {
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
            convertView = LayoutInflater.from(mContext).inflate(R.layout.item_goods_screening_left, parent, false);
            holder.ll_item = convertView.findViewById(R.id.ll_item);
            holder.tv_title = convertView.findViewById(R.id.tv_title);
            holder.iv_right_arrow = convertView.findViewById(R.id.iv_right_arrow);
            convertView.setTag(holder);
        }else {
            holder = (ViewHolder) convertView.getTag();
        }
        holder.tv_title.setText(getItem(position).getGoodsTypeOne());
        if (getItem(position).isChoose()){
            holder.tv_title.setTextColor(mContext.getResources().getColor(R.color.red30));
            holder.iv_right_arrow.setVisibility(View.VISIBLE);
        }else {
            holder.tv_title.setTextColor(mContext.getResources().getColor(R.color.textColor));
            holder.iv_right_arrow.setVisibility(View.GONE);
        }
        // 点击事件
        holder.ll_item.setOnClickListener(v -> {
            onClickLeft.onClickLedt(position);
            notifyDataSetChanged();
        });
        return convertView;
    }

    public interface OnClickLeft{
        //
        void onClickLedt(int position);
    }

    private OnClickLeft onClickLeft;

    public void setOnClickLeft(OnClickLeft onClickLeft) {
        this.onClickLeft = onClickLeft;
    }

    private class ViewHolder{
        private LinearLayout ll_item;
        private TextView tv_title;
        private ImageView iv_right_arrow;
    }

}
