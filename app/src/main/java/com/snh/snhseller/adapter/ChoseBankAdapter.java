package com.snh.snhseller.adapter;

import android.support.annotation.Nullable;
import android.widget.ImageView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.google.zxing.aztec.encoder.HighLevelEncoder;
import com.snh.snhseller.R;
import com.snh.snhseller.bean.BanksBean;
import com.snh.snhseller.utils.ImageUtils;

import java.util.List;

/**
 * <p>desc：<p>
 * <p>author：DreamFly<p>
 * <p>creatTime：2019/4/2<p>
 * <p>changeTime：2019/4/2<p>
 * <p>version：1<p>
 */
public class ChoseBankAdapter extends BaseQuickAdapter<BanksBean,BaseViewHolder> {
    public ChoseBankAdapter(int layoutResId, @Nullable List<BanksBean> data) {
        super(layoutResId, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, BanksBean item) {
        helper.setText(R.id.tv_bank_name,item.BankName);
        ImageUtils.loadUrlImage(mContext,item.Logo, (ImageView) helper.getView(R.id.iv_bank_logo));
        ImageView chose  = helper.getView(R.id.bank_chose);
        if(item.isChose){
            chose.setBackgroundResource(R.drawable.pay_selete);
        }else {
            chose.setBackgroundResource(R.drawable.pay_normall);
        }
    }
}
