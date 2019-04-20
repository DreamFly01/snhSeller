package com.snh.snhseller.adapter;

import android.graphics.Color;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.snh.snhseller.R;
import com.snh.snhseller.bean.supplierbean.AllSupplierBean;
import com.snh.snhseller.utils.ImageUtils;

import java.util.List;

/**
 * <p>desc：<p>
 * <p>author：DreamFly<p>
 * <p>creatTime：2019/3/8<p>
 * <p>changeTime：2019/3/8<p>
 * <p>version：1<p>
 */
public class AllSupplierAdapter extends BaseQuickAdapter<AllSupplierBean, BaseViewHolder> {
    public AllSupplierAdapter(int layoutResId, @Nullable List<AllSupplierBean> data) {
        super(layoutResId, data);
    }

    @Override
    protected void convert(final BaseViewHolder helper, AllSupplierBean item) {
        helper.setText(R.id.tv_shopName, item.SupplierName);
        TextView state = helper.getView(R.id.tv_inventory);
        if (item.IsApply) {
            state.setText("等待验证");
            state.setTextColor(Color.parseColor("#c5c4c4"));
        }else {
            state.setText("去申请");
            state.setTextColor(Color.parseColor("#F99B41"));
        }
        ImageUtils.loadUrlImage(mContext, item.SupplierLogo, (ImageView) helper.getView(R.id.iv_logo));
        RecyclerView recyclerView = helper.getView(R.id.recyclerView);
        AllSupplierItemAdapter adapter = new AllSupplierItemAdapter(R.layout.item_allsupplier_item_layout, item.CommTenantList);
        LinearLayoutManager manager = new LinearLayoutManager(mContext);
        manager.setOrientation(LinearLayoutManager.HORIZONTAL);
        recyclerView.setLayoutManager(manager);
        recyclerView.setAdapter(adapter);
        helper.addOnClickListener(R.id.ll_item);
        recyclerView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_UP) {
                    helper.getView(R.id.ll_item).performClick();
                }
                return false;
            }
        });
    }
}
