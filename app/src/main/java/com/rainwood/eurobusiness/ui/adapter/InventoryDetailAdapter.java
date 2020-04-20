package com.rainwood.eurobusiness.ui.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.rainwood.eurobusiness.R;
import com.rainwood.eurobusiness.domain.InventoryDetailBean;
import com.rainwood.tools.widget.MeasureListView;

import java.util.List;

/**
 * @Author: a797s
 * @Date: 2020/4/16 17:15
 * @Desc: 门店入库明细adapter
 */
public final class InventoryDetailAdapter extends RecyclerView.Adapter<InventoryDetailAdapter.ViewHolder> {

    private Context mContext;
    private List<InventoryDetailBean> mList;

    public InventoryDetailAdapter(Context context) {
        mContext = context;
    }

    public void setList(List<InventoryDetailBean> list) {
        mList = list;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.item_inventory_detail, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.tv_store_name.setText(mList.get(position).getStoreName());
        holder.tv_status.setText(mList.get(position).getWorkFlow());
        // 显示子集
        SubInventoryDetailAdapter detailAdapter = new SubInventoryDetailAdapter(mContext, mList.get(position).getMxlist());
        holder.mlv_sub_special.setAdapter(detailAdapter);
        // 默认不展示子级目录
        if (mList.get(position).getClickPos() == 0) {
            holder.mlv_sub_special.setVisibility(View.GONE);
            holder.ll_special_top.setVisibility(View.GONE);
        } else {
            holder.ll_special_top.setVisibility(View.VISIBLE);
            holder.mlv_sub_special.setVisibility(View.VISIBLE);
        }
        // 点击展开
        holder.ll_item_line.setOnClickListener(v -> {
            int count = mList.get(position).getClickPos() % 2;
            if (count == 0) {
                holder.iv_arrow.setImageResource(R.drawable.ic_up_arrow);
                mList.get(position).setClickPos(++count);
                holder.ll_special_top.setVisibility(View.VISIBLE);
                holder.mlv_sub_special.setVisibility(View.VISIBLE);
            } else {
                holder.iv_arrow.setImageResource(R.drawable.ic_below_arrow);
                mList.get(position).setClickPos(++count);
                holder.ll_special_top.setVisibility(View.GONE);
                holder.mlv_sub_special.setVisibility(View.GONE);
            }
        });

    }

    @Override
    public int getItemCount() {
        return mList == null ? 0 : mList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        RelativeLayout rl_item;
        TextView tv_store_name, tv_status;
        ImageView iv_arrow;
        LinearLayout ll_item_line, ll_special_top;
        MeasureListView mlv_sub_special;

        public ViewHolder(@NonNull View view) {
            super(view);
            rl_item = view.findViewById(R.id.rl_item);
            tv_status = view.findViewById(R.id.tv_status);
            tv_store_name = view.findViewById(R.id.tv_store_name);
            iv_arrow = view.findViewById(R.id.iv_arrow);
            ll_item_line = view.findViewById(R.id.ll_item_line);
            mlv_sub_special = view.findViewById(R.id.mlv_sub_special);
            ll_special_top = view.findViewById(R.id.ll_special_top);
        }
    }
}
