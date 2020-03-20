package com.rainwood.eurobusiness.ui.adapter;

import android.content.Context;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.rainwood.eurobusiness.R;
import com.rainwood.eurobusiness.domain.CommonUIBean;

import java.util.List;

/**
 * @Author: a797s
 * @Date: 2020/2/21
 * @Desc: 预警库存 -- 补货 -- 新建供应商
 */
public class WarnReplishAdapter extends BaseAdapter {

    private Context mContext;
    private List<CommonUIBean> mList;

    public WarnReplishAdapter(Context mContext, List<CommonUIBean> mList) {
        this.mContext = mContext;
        this.mList = mList;
    }

    @Override
    public int getCount() {
        return mList == null ? 0 : mList.size();
    }

    @Override
    public CommonUIBean getItem(int position) {
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
            convertView = LayoutInflater.from(mContext).inflate(R.layout.item_warn_replish, parent, false);
            holder.tv_title = convertView.findViewById(R.id.tv_title);
            holder.et_hint = convertView.findViewById(R.id.et_hint);
            holder.iv_arrow = convertView.findViewById(R.id.iv_arrow);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        holder.tv_title.setText(getItem(position).getTitle());
        // 显示数据
        if (TextUtils.isEmpty(getItem(position).getShowText())) {
            holder.et_hint.setHint(getItem(position).getLabel());
        } else {
            holder.et_hint.setText(getItem(position).getShowText());
        }
        // 去焦
        if (getItem(position).getArrowType() == 0) {             // 输入事件
            holder.et_hint.setFocusableInTouchMode(true);
            holder.iv_arrow.setVisibility(View.GONE);
        } else {                                                // 点击事件
            holder.et_hint.setFocusableInTouchMode(false);
            holder.iv_arrow.setVisibility(View.VISIBLE);
            holder.et_hint.setOnClickListener(v -> onClickLine.onClickLine(position));
        }
        return convertView;
    }

    public interface OnClickLine {
        void onClickLine(int position);
    }

    private OnClickLine onClickLine;

    public void setOnClickLine(OnClickLine onClickLine) {
        this.onClickLine = onClickLine;
    }

    private class ViewHolder {
        private TextView tv_title;
        private EditText et_hint;
        private ImageView iv_arrow;
    }
}
