package com.rainwood.eurobusiness.ui.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.rainwood.eurobusiness.R;
import com.rainwood.eurobusiness.domain.NewShopBean;
import com.rainwood.tools.widget.MeasureGridView;
import com.rainwood.tools.widget.MeasureListView;

import java.util.List;

/**
 * @Author: a797s
 * @Date: 2020/2/9
 * @Desc: 新建商品 --- adapter
 */
public class NewShopAdapter extends BaseAdapter {

    private Context mContext;
    private List<NewShopBean> mList;

    public NewShopAdapter(Context mContext, List<NewShopBean> mList) {
        this.mContext = mContext;
        this.mList = mList;
    }

    @Override
    public int getCount() {
        return mList == null ? 0 : mList.size();
    }

    @Override
    public NewShopBean getItem(int position) {
        return mList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    // 返回当前布局的type
    @Override
    public int getItemViewType(int position) {
        return mList.get(position).getType();
    }

    // 返回你有多少个不同的布局
    @Override
    public int getViewTypeCount() {
        return 5;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if (convertView == null) {
            holder = new ViewHolder();
            convertView = LayoutInflater.from(mContext).inflate(R.layout.item_new_shop, parent, false);
            holder.tv_title = convertView.findViewById(R.id.tv_title);
            holder.lv_params_label = convertView.findViewById(R.id.lv_params_label);
            holder.gv_img = convertView.findViewById(R.id.gv_img);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        holder.tv_title.setText(getItem(position).getTitle());
        // 加载不同的sub_item
        int itemViewType = getItemViewType(position);
        switch (itemViewType) {
            case 0:          // 基本信息
                ItemNewShopBaseAdapter baseAdapter = new ItemNewShopBaseAdapter(mContext, getItem(position).getInfosList());
                holder.lv_params_label.setAdapter(baseAdapter);
                baseAdapter.setLabelListener(position, baseListener);
                break;
            case 1:         // 商品定价
                ShopPriceAdapter priceAdapter = new ShopPriceAdapter(mContext, getItem(position).getInfosList());
                holder.lv_params_label.setAdapter(priceAdapter);
                break;
            case 2:         // 规格参数
                ShopParamsAdapter paramsAdapter = new ShopParamsAdapter(mContext, getItem(position).getInfosList());
                holder.lv_params_label.setAdapter(paramsAdapter);
                // 选择商品规格
                paramsAdapter.setParamsListener(position, paramsListener);
                break;
            case 3:         // 商品图片
                ItemImgGridAdapter gridAdapter = new ItemImgGridAdapter(mContext, getItem(position).getImgList());
                holder.gv_img.setAdapter(gridAdapter);
                gridAdapter.setOnImgListener(onImgListener);
                break;
            case 4:         // 促销信息
                PromotionAdapter promotionAdapter = new PromotionAdapter(mContext, getItem(position).getInfosList());
                holder.lv_params_label.setAdapter(promotionAdapter);
                promotionAdapter.setDateListener(position, dateListener);
                break;
            default:
                break;
        }

        return convertView;
    }

    // 基本信息
    private ItemNewShopBaseAdapter.OnItemLabelListener baseListener;
    // 商品规格
    private ShopParamsAdapter.OnClickParamsListener paramsListener;
    // 添加图片
    private ItemImgGridAdapter.OnImgListener onImgListener;
    // 时间控件
    private PromotionAdapter.OnClickDateListener dateListener;

    public void setBaseListener(ItemNewShopBaseAdapter.OnItemLabelListener baseListener) {
        this.baseListener = baseListener;
    }

    public void setParamsListener(ShopParamsAdapter.OnClickParamsListener paramsListener) {
        this.paramsListener = paramsListener;
    }

    public void setOnImgListener(ItemImgGridAdapter.OnImgListener onImgListener) {
        this.onImgListener = onImgListener;
    }

    public void setDateListener(PromotionAdapter.OnClickDateListener dateListener) {
        this.dateListener = dateListener;
    }

    private class ViewHolder {
        private TextView tv_title;
        private MeasureListView lv_params_label;
        private MeasureGridView gv_img;
    }

}
