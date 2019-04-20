package com.snh.snhseller.adapter;

import android.graphics.Color;
import android.support.annotation.Nullable;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.snh.snhseller.R;
import com.snh.snhseller.bean.CaptailsBean;
import com.snh.snhseller.utils.StrUtils;
import com.snh.snhseller.utils.TimeUtils;

import java.util.List;

/**
 * <p>desc：<p>
 * <p>author：DreamFly<p>
 * <p>creatTime：2019/4/2<p>
 * <p>changeTime：2019/4/2<p>
 * <p>version：1<p>
 */
public class CaptailsAdapter extends BaseQuickAdapter<CaptailsBean,BaseViewHolder> {
    public CaptailsAdapter(int layoutResId, @Nullable List<CaptailsBean> data) {
        super(layoutResId, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, CaptailsBean item) {
        helper.setText(R.id.tv_date_time,TimeUtils.timeStamp2Date(item.WDDate+"","yyyy-MM-dd HH:mm:ss"));
        TextView state = helper.getView(R.id.tv_state);
        switch (item.Type)
        {
            case 0:
                state.setText("已提交");
                state.setTextColor(Color.parseColor("#161616"));
                helper.setText(R.id.tv_money,StrUtils.moenyToDH(item.WDMoney+""));
                break;
            case 1:
                state.setTextColor(Color.parseColor("#161616"));
                state.setText("已通过");
                helper.setText(R.id.tv_money,"-"+StrUtils.moenyToDH(item.WDMoney+""));
                break;
            case 2:
                state.setText("已拒绝");
                state.setTextColor(Color.parseColor("#F81131"));
                helper.setText(R.id.tv_money,StrUtils.moenyToDH(item.WDMoney+""));
                break;
        }
    }
}
