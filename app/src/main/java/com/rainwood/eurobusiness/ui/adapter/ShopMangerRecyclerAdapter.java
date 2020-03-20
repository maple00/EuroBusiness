package com.rainwood.eurobusiness.ui.adapter;

import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.rainwood.eurobusiness.R;
import com.rainwood.eurobusiness.domain.PressBean;

import java.util.List;

/**
 * @Author: a797s
 * @Date: 2020/2/8
 * @Desc: 商品管理头部 横向
 */
public class ShopMangerRecyclerAdapter extends RecyclerView.Adapter<ShopMangerRecyclerAdapter.ViewHolder> {

    private List<PressBean> mList;

    public ShopMangerRecyclerAdapter(List<PressBean> mList) {
        this.mList = mList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_shop_manager_recycler, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.tv_title.setText(mList.get(position).getTitle());
        if (mList.get(position).isChoose()) {            // 有选中的    -- 直接获取R.color.... 会引起颜色混乱
            holder.tv_title.setTextColor(Color.parseColor("#CC302C"));
            holder.tv_line.setVisibility(View.VISIBLE);
        } else {
            holder.tv_title.setTextColor(Color.parseColor("#333333"));
            holder.tv_line.setVisibility(View.GONE);
        }

        // 选中事件
        holder.tv_title.setOnClickListener(v -> {
            iOnTopChoose.onChoose(position);
            notifyDataSetChanged();
        });
    }

    public interface IOnTopChoose {
        void onChoose(int position);
    }

    private IOnTopChoose iOnTopChoose;

    public void setiOnTopChoose(IOnTopChoose iOnTopChoose) {
        this.iOnTopChoose = iOnTopChoose;
    }

    @Override
    public int getItemCount() {
        return mList == null ? 0 : mList.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        private TextView tv_title, tv_line;

        ViewHolder(@NonNull View itemView) {
            super(itemView);
            tv_title = itemView.findViewById(R.id.tv_title);
            tv_line = itemView.findViewById(R.id.tv_line);
        }
    }
}
