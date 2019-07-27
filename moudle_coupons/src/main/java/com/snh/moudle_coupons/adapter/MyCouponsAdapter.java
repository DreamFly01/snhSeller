package com.snh.moudle_coupons.adapter;

import android.support.annotation.Nullable;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.snh.library_base.utils.GlideApp;
import com.snh.library_base.utils.StrUtils;
import com.snh.moudle_coupons.R;
import com.snh.moudle_coupons.bean.CouponsBean;

import java.util.List;

/**
 * <p>desc：<p>
 * <p>author：DreamFly<p>
 * <p>creatTime：2019/5/30<p>
 * <p>changeTime：2019/5/30<p>
 * <p>version：1<p>
 */
public class MyCouponsAdapter extends BaseQuickAdapter<CouponsBean, BaseViewHolder> {

    public MyCouponsAdapter(int layoutResId, @Nullable List<CouponsBean> data) {
        super(layoutResId, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, CouponsBean item) {
        helper.setText(R.id.coupons_tv_money, StrUtils.moenyToDH(item.CouponValue + ""));
        helper.setText(R.id.coupons_tv_condition, "满" + item.ConditionValue + "可用");
        helper.setText(R.id.coupons_tv_title, item.CouponName);
        helper.setText(R.id.coupons_tv_01, "发行总张的" + item.TotalNum + "张");
        helper.setText(R.id.coupons_tv_02, "每人限领" + item.MaxReceiveNum + "张");
        helper.setText(R.id.coupons_tv_03, "有效期：" + item.BeginDate + "-" + item.EndDate);
        helper.setText(R.id.coupons_tv_residue, item.StockNum + "");
        helper.setText(R.id.coupons_tv_recevie, item.ReceiveNum + "");
        helper.setText(R.id.coupons_tv_used, item.UsedNum + "");
        TextView look = helper.getView(R.id.coupons_tv_look);
        ImageView ivState = helper.getView(R.id.coupons_iv_state);
        ImageView ivState1 = helper.getView(R.id.coupons_iv_state1);
        ImageView ivLine = helper.getView(R.id.coupuons_line);
        switch (item.CouponWay) {
            //1针对用户的优惠券
            case 1:

                look.setVisibility(View.GONE);
                ivLine.setVisibility(View.GONE);
                GlideApp.with(mContext).load(R.drawable.coupons_store_bg).into(ivState);
                helper.getView(R.id.coupons_tv_02).setVisibility(View.VISIBLE);
                if(item.MaxReceiveNum<=0){
                    helper.setText(R.id.coupons_tv_02, "无限制领取");
                }
                break;
            //2针对商户的优惠券
            case 2:
                look.setVisibility(View.VISIBLE);
                ivLine.setVisibility(View.VISIBLE);
                GlideApp.with(mContext).load(R.drawable.coupons_supplier_bg).into(ivState);
                helper.getView(R.id.coupons_tv_02).setVisibility(View.GONE);
                break;
        }
        switch (item.IsDisplay)
        {
            case 0:
                GlideApp.with(mContext).load(R.drawable.coupons_hide_bg).into(ivState1);
                break;
            case 1:
                GlideApp.with(mContext).load(R.drawable.coupons_show_bg).into(ivState1);
                break;
        }
        helper.addOnClickListener(R.id.coupons_tv_look)
                .addOnClickListener(R.id.coupons_ll_edit);
    }
}
