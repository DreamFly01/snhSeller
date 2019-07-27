package com.snh.moudle_coupons.adapter;

import android.widget.ImageView;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseSectionQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.snh.library_base.utils.GlideApp;
import com.snh.library_base.utils.ImageUtils;
import com.snh.moudle_coupons.R;
import com.snh.moudle_coupons.bean.ScrollBean;

import java.util.List;

/**
 * <p>desc：<p>
 * <p>author：DreamFly<p>
 * <p>creatTime：2019/1/25<p>
 * <p>changeTime：2019/1/25<p>
 * <p>version：1<p>
 */
public class ScrollRightAdapter extends BaseSectionQuickAdapter<ScrollBean, BaseViewHolder> {

    public ScrollRightAdapter(int layoutResId, int sectionHeadResId, List<ScrollBean> data) {
        super(layoutResId, sectionHeadResId, data);
    }

    @Override
    protected void convertHead(BaseViewHolder helper, ScrollBean item) {
        helper.setText(R.id.right_title, item.header);
    }

    @Override
    protected void convert(BaseViewHolder helper, ScrollBean item) {
        ScrollBean.ScrollItemBean t = item.t;
        helper.setText(R.id.coupons_tv_title,t.CommodityName);
//        helper.setText(R.id.coupons_tv_sales_num,"销量："+t.OnThePin);
//        helper.setText(R.id.coupons_tv_money,StrUtils.moenyToDH(t.Price+""));
//        helper.setText(R.id.coupons_tv_unitsTitle,"/"+t.UnitsTitle);
        helper.setText(R.id.tv_inventory,"库存："+t.Inventory);
        ImageUtils.loadUrlImage(mContext,t.CommodityIconUrl, (ImageView) helper.getView(R.id.coupons_iv_logo));
        TextView tvState = helper.getView(R.id.coupons_tv_money);
        if(t.isSelect){
            GlideApp.with(mContext).load(R.drawable.coupons_check_true_bg).into((ImageView) helper.getView(R.id.coupons_iv_state));
        }else {
            GlideApp.with(mContext).load(R.drawable.coupons_check_flase_bg).into((ImageView) helper.getView(R.id.coupons_iv_state));
        }
        if(t.Status == 1){
            tvState.setText("商品库");
            tvState.setTextColor(mContext.getResources().getColor(R.color.white));
            tvState.setBackgroundColor(mContext.getResources().getColor(R.color.orange));
        }else {
            tvState.setText("出售中");
            tvState.setTextColor(mContext.getResources().getColor(R.color.txt_dark));
            tvState.setBackgroundColor(mContext.getResources().getColor(R.color.white));
        }
    }

}
