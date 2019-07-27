package com.snh.snhseller.adapter;

import android.graphics.Color;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.snh.snhseller.R;
import com.snh.snhseller.bean.ProductBean;
import com.snh.snhseller.utils.ImageUtils;
import com.snh.snhseller.utils.StrUtils;

import java.util.List;

/**
 * <p>desc：<p>
 * <p>author：DreamFly<p>
 * <p>creatTime：2019/2/22<p>
 * <p>changeTime：2019/2/22<p>
 * <p>version：1<p>
 */
public class ProdcutAdapter extends BaseQuickAdapter<ProductBean,BaseViewHolder> {
    private int type = 0;
    public ProdcutAdapter(int layoutResId, @Nullable List<ProductBean> data) {
        super(layoutResId, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, final ProductBean item) {
        TextView tvEdit = helper.getView(R.id.tv_edit);
        TextView tvXj = helper.getView(R.id.tv_xj);
        TextView state = helper.getView(R.id.tv_state);
        switch (item.IsAuditing)
        {
            case 0:
//                helper.getView(R.id.ll_state).setVisibility(View.VISIBLE);
//                helper.setText(R.id.tv_state,"待审核");
                state.setVisibility(View.INVISIBLE);
                break;
            case 1:
//                helper.getView(R.id.ll_state).setVisibility(View.GONE);
//                helper.setText(R.id.tv_state,"通过");
                state.setVisibility(View.INVISIBLE);
                break;
            case 2:
//                helper.getView(R.id.ll_state).setVisibility(View.VISIBLE);
//                helper.setText(R.id.tv_state,"不通过");
                state.setVisibility(View.INVISIBLE);
                break;
        }
//        helper.setText(R.id.tv_reason,item.Reason);
        helper.setText(R.id.tv_title ,item.CommTenantName);
        helper.setText(R.id.tv_money,StrUtils.moenyToDH(item.Price+""));
        helper.setText(R.id.tv_inventory,"库存："+item.Inventory);
        helper.setText(R.id.tv_sales_num,"销量："+item.SalesVolume);
        helper.setText(R.id.tv_unitsTitle,"/"+item.UnitsTitle);
        ImageUtils.loadUrlImage(mContext,item.CommTenantIcon, (ImageView) helper.getView(R.id.iv_logo));

        switch (item.Status)
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

        if(type==2){
            TextView tvState = helper.getView(R.id.tv_state);
            tvState.setVisibility(View.VISIBLE);
            switch (item.Status)
            {
                case 1:
                    tvState.setText("出售中");
                    tvState.setBackgroundColor(Color.parseColor("#D81E06"));
                    break;
                case 2:
                    tvState.setText("商品库");
                    tvState.setBackgroundColor(Color.parseColor("#FF9600"));
                    break;
                case 3:
                    tvState.setText("审核中");
                    tvState.setBackgroundColor(Color.parseColor("#A5A5A5"));
                    break;
            }
        }
        helper.addOnClickListener(R.id.ll_item)
                .addOnClickListener(R.id.tv_edit)
                .addOnClickListener(R.id.tv_xj);

    }
    public void setType(int type){
        this.type = type;
    }
}
