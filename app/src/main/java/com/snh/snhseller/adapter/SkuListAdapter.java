package com.snh.snhseller.adapter;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.snh.snhseller.R;
import com.snh.snhseller.bean.SkuBean;
import com.snh.snhseller.ui.product.AddSku1Activity;
import com.snh.snhseller.ui.product.AddSkuActivity;
import com.snh.snhseller.utils.JumpUtils;
import com.snh.snhseller.utils.StrUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * <p>desc：<p>
 * <p>author：DreamFly<p>
 * <p>creatTime：2019/3/12<p>
 * <p>changeTime：2019/3/12<p>
 * <p>version：1<p>
 */
public class SkuListAdapter extends BaseQuickAdapter<SkuBean,BaseViewHolder> {
    public SkuListAdapter(int layoutResId, @Nullable List<SkuBean> data) {
        super(layoutResId, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, final SkuBean item) {
        helper.setText(R.id.tv_sku_name,"规格名称："+item.NormName);
//        helper.setText(R.id.tv_sku_value,"规格值："+item.NormValue.substring(item.NormValue.indexOf("*")+1,item.NormValue.length())+"("+item.MeasureUnit+"/组)");
//        helper.setText(R.id.tv_sku_sprice,"二批价：¥"+StrUtils.moenyToDH(item.Price+""));
//        helper.setText(R.id.tv_sku_mprice,"终端价：¥"+StrUtils.moenyToDH(item.RetailPrice+""));
//        helper.setText(R.id.tv_sku_inventory,"库存："+item.Inventory);
        List<SkuBean> data = new ArrayList<>();
        data.add(item);
        RecyclerView recyclerView = helper.getView(R.id.rv_sku_item);
        SkuList1Adapter adapter = new SkuList1Adapter(R.layout.item_sku_item_layout,data);
        recyclerView.setLayoutManager(new LinearLayoutManager(mContext));
        adapter.setOnItemChildClickListener(new OnItemChildClickListener() {
            @Override
            public void onItemChildClick(BaseQuickAdapter adapter, View view, int position) {
                switch (view.getId())
                {
                    case R.id.ll_item:
                       Bundle bundle = new Bundle();
                        bundle.putParcelable("data", item);
                        bundle.putInt("goodsId", goodsId);
                        JumpUtils.dataJump((Activity) mContext,AddSkuActivity.class,bundle,false);
                        break;
                    case R.id.ll_delete:
                        break;
                }
            }
        });
        recyclerView.setAdapter(adapter);
        helper.addOnClickListener(R.id.ll_delete);
    }
    private int goodsId;
    public void setGoodsId(int goodsId){
        this.goodsId = goodsId;
    }
}
