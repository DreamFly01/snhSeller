package com.snh.snhseller.adapter;

import android.graphics.Color;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.snh.snhseller.R;
import com.snh.snhseller.utils.ImageUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * <p>desc：<p>
 * <p>author：DreamFly<p>
 * <p>creatTime：2019/3/6<p>
 * <p>changeTime：2019/3/6<p>
 * <p>version：1<p>
 */
public class CustomAdapter extends BaseQuickAdapter<String,BaseViewHolder> {
    private List<Integer> imgList = new ArrayList<>();
    public CustomAdapter(int layoutResId, @Nullable List<String> data) {
        super(layoutResId, data);
    }

    @Override
    protected void convert(final BaseViewHolder helper, String item) {
        TextView textView = helper.getView(R.id.tv_custom);
        ImageView imageView = helper.getView(R.id.iv_logo);
        textView.setText(item);
        Glide.with(mContext).load(imgList.get(helper.getPosition())).into(imageView);
        helper.getView(R.id.ll_item).setBackgroundColor(Color.WHITE);
        helper.getView(R.id.ll_item).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setOnItemClick(helper.getView(R.id.ll_item),helper.getPosition());
                helper.getView(R.id.ll_item).setBackgroundColor(Color.parseColor("#CACACA"));
            }
        });
    }
    public void setImgList(List<Integer> imgList){
        this.imgList = imgList;
        notifyDataSetChanged();
    }

}
