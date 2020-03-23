package com.rainwood.eurobusiness.ui.adapter;

import android.content.Context;
import android.text.Html;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.rainwood.eurobusiness.R;
import com.rainwood.eurobusiness.domain.ClientManagerBean;
import com.rainwood.tools.common.FontDisplayUtil;

import java.util.List;

/**
 * @Author: a797s
 * @Date: 2020/2/22
 * @Desc: 客户管理
 */
public class CustomAdapter extends BaseAdapter {

    private Context mContext;
    private List<ClientManagerBean> mList;

    public CustomAdapter(Context mContext, List<ClientManagerBean> mList) {
        this.mContext = mContext;
        this.mList = mList;
    }

    @Override
    public int getCount() {
        return mList == null ? 0 : mList.size();
    }

    @Override
    public ClientManagerBean getItem(int position) {
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
            convertView = LayoutInflater.from(mContext).inflate(R.layout.item_custom_content, parent, false);
            holder.iv_img = convertView.findViewById(R.id.iv_img);
            holder.tv_name = convertView.findViewById(R.id.tv_name);
            holder.tv_type = convertView.findViewById(R.id.tv_type);
            holder.tv_gather = convertView.findViewById(R.id.tv_gather);
            holder.ll_item = convertView.findViewById(R.id.ll_item);
            holder.iv_point = convertView.findViewById(R.id.iv_point);
            holder.tv_delete_store = convertView.findViewById(R.id.tv_delete_store);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        if (TextUtils.isEmpty(getItem(position).getIco())) {
            Glide.with(convertView).load(R.drawable.icon_loadding_fail).into(holder.iv_img);
        } else {
            Glide.with(convertView).load(getItem(position).getIco()).into(holder.iv_img);
        }
        holder.tv_name.setText(getItem(position).getName());
        holder.tv_type.setText(getItem(position).getType());
        holder.tv_gather.setText(Html.fromHtml("<font color=" +
                mContext.getResources().getColor(R.color.fontColor) + " size='" + FontDisplayUtil.dip2px(mContext, 12)
                + "'>应收款：</font>" + "<font color=" + mContext.getResources().getColor(R.color.textColor)
                + " size='" + FontDisplayUtil.dip2px(mContext, 14) + "'>" + getItem(position).getMoney() + "</font>"));
//        if (getItem(position).getUiType() == 0) {
//            holder.iv_point.setVisibility(View.GONE);
//        } else {
        holder.iv_point.setVisibility(View.VISIBLE);
        holder.iv_point.setOnClickListener(v -> {
            if (!count) {
                holder.tv_delete_store.setVisibility(View.INVISIBLE);
            } else {
                holder.tv_delete_store.setVisibility(View.VISIBLE);
                holder.tv_delete_store.setOnClickListener(v1 -> {
                    mList.remove(position);
                    holder.tv_delete_store.setVisibility(View.INVISIBLE);
                    notifyDataSetChanged();
                });
            }
            count = !count;
        });
//        }
        // 点击事件
        holder.ll_item.setOnClickListener(v -> onClickContent.onClickContent(position));
        return convertView;
    }

    private boolean count;

    public interface OnClickContent {
        // 查看详情
        void onClickContent(int position);
    }

    private OnClickContent onClickContent;

    public void setOnClickContent(OnClickContent onClickContent) {
        this.onClickContent = onClickContent;
    }

    private class ViewHolder {
        private ImageView iv_img, iv_point;
        private TextView tv_name, tv_type, tv_gather, tv_delete_store;
        private LinearLayout ll_item;
    }
}
