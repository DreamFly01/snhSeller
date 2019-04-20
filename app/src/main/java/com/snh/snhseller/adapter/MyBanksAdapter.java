package com.snh.snhseller.adapter;

import android.graphics.Color;
import android.support.annotation.Nullable;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.aries.ui.view.radius.RadiusLinearLayout;
import com.bumptech.glide.Glide;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.snh.snhseller.R;
import com.snh.snhseller.bean.MyBankBean;
import com.snh.snhseller.utils.ImageUtils;
import com.snh.snhseller.utils.StrUtils;
import com.snh.snhseller.wediget.CornersLinearLayout;

import java.util.List;

/**
 * <p>desc：<p>
 * <p>author：DreamFly<p>
 * <p>creatTime：2019/4/2<p>
 * <p>changeTime：2019/4/2<p>
 * <p>version：1<p>
 */
public class MyBanksAdapter extends BaseQuickAdapter<MyBankBean, BaseViewHolder> {
    public MyBanksAdapter(int layoutResId, @Nullable List<MyBankBean> data) {
        super(layoutResId, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, MyBankBean item) {
        ImageUtils.loadUrlCircleImage(mContext, item.Logo, (ImageView) helper.getView(R.id.iv_bank_bg));
        helper.setText(R.id.tv_bank_name, item.BankTypeName);

        helper.setText(R.id.tv_bank_no, StrUtils.hideCardNo(item.BankCardNo));
        RadiusLinearLayout bankBg = helper.getView(R.id.ll_bank);
        bankBg.getDelegate().setBackgroundColor(Color.parseColor(item.Color));
        helper.addOnClickListener(R.id.btnDelete);

    }
}
