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
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.rainwood.eurobusiness.R;
import com.rainwood.eurobusiness.domain.CommonUIBean;

import java.util.List;

/**
 * @Author: a797s
 * @Date: 2020/2/11
 * @Desc: 商品规格参数
 */
public class ShopParamsAdapter extends BaseAdapter {

    private Context mContext;
    private List<CommonUIBean> mList;
    private int parentPosition;

    public ShopParamsAdapter(Context mContext, List<CommonUIBean> mList) {
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
            convertView = LayoutInflater.from(mContext).inflate(R.layout.item_sub_price_params,
                    parent, false);
            holder.tv_title = convertView.findViewById(R.id.tv_title);
            holder.tv_percent = convertView.findViewById(R.id.tv_percent);
            holder.et_hint = convertView.findViewById(R.id.et_hint);
            holder.rg_choose = convertView.findViewById(R.id.rg_choose);
            holder.rb_text1 = convertView.findViewById(R.id.rb_text1);
            holder.rb_text2 = convertView.findViewById(R.id.rb_text2);
            holder.iv_arrow = convertView.findViewById(R.id.iv_arrow);
            holder.ll_choose_size = convertView.findViewById(R.id.ll_choose_size);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        // 设置title
        if (getItem(position).getTitle().equals("最小起订量")) {
            holder.tv_title.setText(Html.fromHtml("<font color="
                    + mContext.getResources().getColor(R.color.red30) + ">*</font>"
                    + getItem(position).getTitle()));
        } else {
            holder.tv_title.setText(getItem(position).getTitle());
        }

        // 设置label
        if (getItem(position).getTitle().equals("商品尺码")) {
            holder.et_hint.setVisibility(View.GONE);
            holder.rg_choose.setVisibility(View.VISIBLE);
            holder.rb_text1.setText("混装");
            holder.rb_text2.setText("有尺码");
            // 如果有尺码，则去添加尺码
            View finalConvertView = convertView;
            holder.rg_choose.setOnCheckedChangeListener((group, checkedId) -> {
                RadioButton rb = finalConvertView.findViewById(checkedId);
                if (rb.getText().toString().trim().equals("有尺码")){
                    holder.ll_choose_size.setVisibility(View.VISIBLE);
                }else {
                    holder.ll_choose_size.setVisibility(View.GONE);
                }
                // 有尺码的点击事件
                holder.ll_choose_size.setOnClickListener(v -> paramsListener.onClickSize());
            });

        } else {
            holder.et_hint.setVisibility(View.VISIBLE);
            holder.rg_choose.setVisibility(View.GONE);
            if (TextUtils.isEmpty(getItem(position).getShowText())) {
                holder.et_hint.setHint(getItem(position).getLabel());
            } else {
                holder.et_hint.setText(getItem(position).getShowText());
            }
            if (getItem(position).getTitle().equals("税率")) {
                holder.tv_percent.setVisibility(View.VISIBLE);
            } else {
                holder.tv_percent.setVisibility(View.GONE);
            }
            if (getItem(position).getTitle().equals("商品规格")) {
                holder.iv_arrow.setVisibility(View.VISIBLE);
                holder.et_hint.setFocusableInTouchMode(false);  //不可编辑
                holder.et_hint.setOnClickListener(v -> {
                    paramsListener.onClickParams(parentPosition, position);
                    notifyDataSetChanged();
                });
            } else {
                holder.iv_arrow.setVisibility(View.GONE);
            }
        }
        return convertView;
    }

    public interface OnClickParamsListener {
        // 商品规格
        void onClickParams(int parentPosition, int position);

        // 选中的商品的尺码
         void onClickSize();
    }

    private OnClickParamsListener paramsListener;

    public void setParamsListener(int parentPosition, OnClickParamsListener paramsListener) {
        this.paramsListener = paramsListener;
        this.parentPosition = parentPosition;
    }

    private class ViewHolder {
        private TextView tv_title, tv_percent;
        private EditText et_hint;
        private RadioGroup rg_choose;
        private RadioButton rb_text1, rb_text2;
        private ImageView iv_arrow;
        private LinearLayout ll_choose_size;
    }
}
