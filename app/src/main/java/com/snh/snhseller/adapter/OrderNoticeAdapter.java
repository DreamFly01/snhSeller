package com.snh.snhseller.adapter;

import android.support.annotation.Nullable;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.snh.snhseller.R;
import com.snh.snhseller.bean.NoticeBean;
import com.snh.snhseller.utils.TimeUtils;

import java.util.List;

/**
 * <p>desc：<p>
 * <p>author：DreamFly<p>
 * <p>creatTime：2019/4/18<p>
 * <p>changeTime：2019/4/18<p>
 * <p>version：1<p>
 */
public class OrderNoticeAdapter extends BaseQuickAdapter<NoticeBean,BaseViewHolder> {
    public OrderNoticeAdapter(int layoutResId, @Nullable List<NoticeBean> data) {
        super(layoutResId, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, NoticeBean item) {
        if(item.IsLook==0){
            helper.getView(R.id.iv_state).setBackgroundResource(R.drawable.eye_open);
        }else if(item.IsLook == 1){
            helper.getView(R.id.iv_state).setBackgroundResource(R.drawable.eye_close);
        }
        helper.setText(R.id.tv_state,item.Title);
        helper.setText(R.id.tv_order_no,item.OrderNo);
        helper.setText(R.id.tv_content,item.Content);
        helper.setText(R.id.tv_time,TimeUtils.timeStamp2Date(item.CreateTime+"","yyyy-MM-dd    HH:mm"));
    }
}
