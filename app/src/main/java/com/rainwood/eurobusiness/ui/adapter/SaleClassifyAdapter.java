package com.rainwood.eurobusiness.ui.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.rainwood.eurobusiness.R;
import com.rainwood.eurobusiness.domain.ClassifyBean;
import com.rainwood.tools.widget.MeasureListView;

import java.util.List;

/**
 * @Author: shearson
 * @Time: 2020/2/26 17:05
 * @Desc: 批发商 ---  商品管理(商品分类)
 */
public class SaleClassifyAdapter extends RecyclerView.Adapter<SaleClassifyAdapter.VerticalViewHolder> {

    private Context mContext;
    private List<ClassifyBean> mList;

    public SaleClassifyAdapter(Context mContext) {
        this.mContext = mContext;
    }

    public void setmList(List<ClassifyBean> mList) {
        this.mList = mList;
    }

    @NonNull
    @Override
    public VerticalViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.item_sale_classify_parent, parent, false);
        return new VerticalViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull VerticalViewHolder holder, int position) {
        holder.tv_name.setText(mList.get(position).getName());
        // 显示子级目录
        SaleSubClassifyAdapter subAdapter = new SaleSubClassifyAdapter(mContext, mList.get(position).getSubList());
        holder.lv_sub_classify.setAdapter(subAdapter);
        subAdapter.setOnClickPoint(position, onClickPoint);
        // 默认不展示子级目录
        if (mList.get(position).getSelected() == 0){
            holder.lv_sub_classify.setVisibility(View.GONE);
        }else {
            holder.lv_sub_classify.setVisibility(View.VISIBLE);
        }
        // 点击展开
        holder.ll_line_item.setOnClickListener(v -> {
            int count = mList.get(position).getSelected() % 2;
            if (count == 0) {
                holder.iv_selector.setImageResource(R.drawable.ic_up_selector_black);
                mList.get(position).setSelected(++count);
                holder.lv_sub_classify.setVisibility(View.VISIBLE);
            } else {
                holder.iv_selector.setImageResource(R.drawable.ic_down_selector_black);
                mList.get(position).setSelected(++count);
                holder.lv_sub_classify.setVisibility(View.GONE);
            }
        });
        // 点击点
        holder.iv_point.setOnClickListener(v -> {
            onClickItem.onClickPoint(position);
            notifyDataSetChanged();
        });
    }

    @Override
    public int getItemCount() {
        return mList == null ? 0 : mList.size();
    }

    public interface OnClickItem {
        // 点击点
        void onClickPoint(int position);
    }

    private OnClickItem onClickItem;

    private SaleSubClassifyAdapter.OnClickPoint onClickPoint;

    public void setOnClickPoint(SaleSubClassifyAdapter.OnClickPoint onClickPoint) {
        this.onClickPoint = onClickPoint;
    }

    public void setOnClickItem(OnClickItem onClickItem) {
        this.onClickItem = onClickItem;
    }

    class VerticalViewHolder extends RecyclerView.ViewHolder {
        private LinearLayout ll_line_item;
        private ImageView iv_selector, iv_point;
        private TextView tv_name;
        private MeasureListView lv_sub_classify;

        private VerticalViewHolder(View view) {
            super(view);
            ll_line_item = view.findViewById(R.id.ll_line_item);
            iv_selector = view.findViewById(R.id.iv_selector);
            iv_point = view.findViewById(R.id.iv_point);
            tv_name = view.findViewById(R.id.tv_name);
            lv_sub_classify = view.findViewById(R.id.lv_sub_classify);
        }
    }
}
