package com.snh.snhseller.adapter;

import android.support.annotation.Nullable;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.snh.snhseller.R;
import com.snh.snhseller.bean.BaseResultBean;
import com.snh.snhseller.bean.ProductBean;
import com.snh.snhseller.requestApi.NetSubscriber;
import com.snh.snhseller.requestApi.RequestClient;
import com.snh.snhseller.utils.DialogUtils;
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

    public ProdcutAdapter(int layoutResId, @Nullable List<ProductBean> data) {
        super(layoutResId, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, final ProductBean item) {

        switch (item.IsAuditing)
        {
            case 0:
                helper.getView(R.id.ll_state).setVisibility(View.VISIBLE);
                helper.setText(R.id.tv_state,"待审核");
                break;
            case 1:
                helper.getView(R.id.ll_state).setVisibility(View.GONE);
                helper.setText(R.id.tv_state,"通过");
                break;
            case 2:
                helper.getView(R.id.ll_state).setVisibility(View.VISIBLE);
                helper.setText(R.id.tv_state,"不通过");
                break;
        }
        helper.setText(R.id.tv_reason,item.Reason);
        helper.setText(R.id.tv_GoodsName,item.CommTenantName);
        helper.setText(R.id.tv_price,"￥"+StrUtils.moenyToDH(item.Price+""));
        helper.setText(R.id.tv_kc,"库存："+item.Inventory);
        helper.setText(R.id.tv_xl,"销量："+item.SalesVolume);
        ImageUtils.loadUrlImage(mContext,item.CommTenantIcon, (ImageView) helper.getView(R.id.iv_product_logo1));
        switch (item.state)
        {
            case 1:
                helper.getView(R.id.ll_01).setVisibility(View.VISIBLE);
                helper.getView(R.id.ll_02).setVisibility(View.GONE);
                helper.getView(R.id.ll_03).setVisibility(View.GONE);

                break;
            case 2:
                helper.getView(R.id.ll_01).setVisibility(View.GONE);
                helper.getView(R.id.ll_02).setVisibility(View.GONE);
                helper.getView(R.id.ll_03).setVisibility(View.VISIBLE);

                break;
            case 3:
                helper.getView(R.id.ll_01).setVisibility(View.GONE);
                helper.getView(R.id.ll_02).setVisibility(View.GONE);
                helper.getView(R.id.ll_03).setVisibility(View.GONE);
                if(item.IsAuditing == 2){
                    helper.getView(R.id.ll_02).setVisibility(View.VISIBLE);
                }
                break;
        }
        helper.addOnClickListener(R.id.ll_01)
        .addOnClickListener(R.id.ll_02)
        .addOnClickListener(R.id.ll_delete)
        .addOnClickListener(R.id.ll_up)
        .addOnClickListener(R.id.ll_item);

    }


}
