package com.snh.snhseller.adapter;

import android.support.annotation.Nullable;
import android.view.View;
import android.widget.ImageView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.snh.snhseller.R;
import com.snh.snhseller.bean.MyMsgBean;
import com.snh.snhseller.bean.NoticeBean;
import com.snh.snhseller.utils.ImageUtils;
import com.snh.snhseller.utils.TimeUtils;

import java.util.List;

/**
 * <p>desc：<p>
 * <p>author：DreamFly<p>
 * <p>creatTime：2019/3/12<p>
 * <p>changeTime：2019/3/12<p>
 * <p>version：1<p>
 */
public class MyMsgAdapter extends BaseQuickAdapter<NoticeBean,BaseViewHolder> {
    public MyMsgAdapter(int layoutResId, @Nullable List<NoticeBean> data) {
        super(layoutResId, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, NoticeBean item) {
            helper.setText(R.id.tv_state,"申请通知");
            helper.setText(R.id.tv_time,TimeUtils.timeStamp2Date(item.CreateTime+"","yyyy-MM-dd HH:mm:ss"));
            helper.setText(R.id.tv_shopName,item.Title);
            helper.setText(R.id.tv_inventory,item.Content);
            helper.getView(R.id.iv_shop_logo).setVisibility(View.GONE);
//        ImageUtils.loadUrlImage(mContext,item.SponsorIconUrl, (ImageView) helper.getView(R.id.iv_shop_logo));
    }
}
