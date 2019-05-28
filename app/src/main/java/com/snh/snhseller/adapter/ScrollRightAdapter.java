package com.snh.snhseller.adapter;

import android.graphics.Color;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseSectionQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.snh.snhseller.R;
import com.snh.snhseller.bean.ScrollBean;
import com.snh.snhseller.utils.ImageUtils;
import com.snh.snhseller.utils.StrUtils;

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
        TextView tvEdit = helper.getView(R.id.tv_edit);
        TextView tvXj = helper.getView(R.id.tv_xj);
        helper.setText(R.id.tv_title,t.CommTenantName);
        helper.setText(R.id.tv_sales_num,"销量："+t.OnThePin);
        helper.setText(R.id.tv_money,StrUtils.moenyToDH(t.Price+""));
        helper.setText(R.id.tv_unitsTitle,"/"+t.UnitsTitle);
        helper.setText(R.id.tv_inventory,"库存："+t.Inventory);
        ImageUtils.loadUrlImage(mContext,t.CommTenantIcon, (ImageView) helper.getView(R.id.iv_logo));
        switch (t.status)
        {
            case 1:
                tvEdit.setVisibility(View.VISIBLE);
                tvXj.setVisibility(View.VISIBLE);
                tvXj.setText("下架");
                break;
            case 2:
                tvEdit.setVisibility(View.VISIBLE);
                tvXj.setVisibility(View.VISIBLE);
                tvXj.setText("上架");
                break;
            case 3:
                tvEdit.setVisibility(View.GONE);
                tvXj.setVisibility(View.GONE);
                break;
        }

        helper.addOnClickListener(R.id.tv_edit)
                .addOnClickListener(R.id.tv_xj);
    }

}
