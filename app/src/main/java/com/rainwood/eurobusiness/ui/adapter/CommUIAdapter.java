package com.rainwood.eurobusiness.ui.adapter;

import android.content.Context;
import android.text.Html;
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
 * @Date: 2020/2/19
 * @Desc: title + label 形式的 adapter
 */
public class CommUIAdapter extends BaseAdapter {

    private Context mContext;
    private List<CommonUIBean> mList;
    private int parentPos;

    public CommUIAdapter(Context mContext, List<CommonUIBean> mList) {
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
            convertView = LayoutInflater.from(mContext).inflate(R.layout.sub_item_common_ui, parent, false);
            holder.tv_title = convertView.findViewById(R.id.tv_title);
            holder.et_hint = convertView.findViewById(R.id.et_hint);
            holder.iv_right_arrow = convertView.findViewById(R.id.iv_right_arrow);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        // 显示是否是必填项
        if (getItem(position).isFillIn()){             // 是必填项
            holder.tv_title.setText(Html.fromHtml("<font color="
                    + mContext.getResources().getColor(R.color.red30) + ">*</font>"
                    + getItem(position).getTitle()));
        }else {
            holder.tv_title.setText(getItem(position).getTitle());
        }
        // 显示showText
        if (TextUtils.isEmpty(getItem(position).getShowText())) {        // 显示提示语
            holder.et_hint.setHint(getItem(position).getLabel());
        } else {                                                         // 显示文本内容
            holder.et_hint.setText(getItem(position).getShowText());
        }
        if (getItem(position).getArrowType() == 0) {                // 不显示箭头，即为输入
            holder.et_hint.setFocusableInTouchMode(true);
            holder.et_hint.setFocusable(true);
            holder.iv_right_arrow.setVisibility(View.GONE);
        } else {
            holder.iv_right_arrow.setVisibility(View.VISIBLE);
            holder.et_hint.setFocusableInTouchMode(false);
            holder.et_hint.setFocusable(false);
            // 选择点击事件
            holder.et_hint.setOnClickListener(v -> onClickEditText.onClickText(parentPos, position));
        }
        return convertView;
    }

    public interface OnClickEditText {
        void onClickText(int parentPos, int position);
    }

    private OnClickEditText onClickEditText;

    public void setOnClickEditText(OnClickEditText onClickEditText) {
        this.onClickEditText = onClickEditText;
    }

    public void setOnClickEditText(int parentPos, OnClickEditText onClickEditText) {
        this.onClickEditText = onClickEditText;
        this.parentPos = parentPos;
    }

    private class ViewHolder {
        private TextView tv_title;
        private EditText et_hint;
        private ImageView iv_right_arrow;
    }
}
