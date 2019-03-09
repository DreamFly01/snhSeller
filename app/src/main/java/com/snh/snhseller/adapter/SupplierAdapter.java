package com.snh.snhseller.adapter;

import android.support.annotation.Nullable;
import android.view.View;
import android.widget.ImageView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.snh.snhseller.R;
import com.snh.snhseller.bean.supplierbean.SupplierBean;
import com.snh.snhseller.utils.ImageUtils;
import com.snh.snhseller.utils.StrUtils;

import org.greenrobot.greendao.annotation.Id;

import java.util.List;

/**
 * <p>desc：<p>
 * <p>author：DreamFly<p>
 * <p>creatTime：2019/3/7<p>
 * <p>changeTime：2019/3/7<p>
 * <p>version：1<p>
 */
public class SupplierAdapter extends BaseQuickAdapter<SupplierBean, BaseViewHolder> {

    public SupplierAdapter(int layoutResId, @Nullable List<SupplierBean> data) {
        super(layoutResId, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, SupplierBean item) {
        ImageUtils.loadUrlImage(mContext, item.SupplierIconurl, (ImageView) helper.getView(R.id.iv_logo));
        helper.setText(R.id.tv_name, item.SupplierName);
        if (!StrUtils.isEmpty(item.SupplierPhone)) {
            helper.setText(R.id.tv_phone, item.SupplierPhone);
            helper.setText(R.id.tv_introduce, item.SupplierAddress);
        } else {
            helper.getView(R.id.tv_phone).setVisibility(View.GONE);
            helper.setText(R.id.tv_introduce, item.SupplierSynopsis);
        }
    }
}
