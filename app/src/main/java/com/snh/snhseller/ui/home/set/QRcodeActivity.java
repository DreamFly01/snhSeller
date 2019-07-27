package com.snh.snhseller.ui.home.set;

import android.annotation.SuppressLint;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.SimpleTarget;
import com.bumptech.glide.request.transition.Transition;
import com.snh.library_base.db.DBManager;
import com.snh.snhseller.BaseActivity;
import com.snh.snhseller.R;
import com.snh.snhseller.utils.DialogUtils;
import com.snh.snhseller.utils.ImgSaveUtils;
import com.snh.snhseller.utils.IsBang;
import com.snh.snhseller.utils.QRcodeUtils;

import java.io.InputStream;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * <p>desc：<p>
 * <p>author：DreamFly<p>
 * <p>creatTime：2019/2/23<p>
 * <p>changeTime：2019/2/23<p>
 * <p>version：1<p>
 */
public class QRcodeActivity extends BaseActivity {
    @BindView(R.id.heard_back)
    LinearLayout heardBack;
    @BindView(R.id.heard_title)
    TextView heardTitle;
    @BindView(R.id.heard_menu)
    ImageView heardMenu;
    @BindView(R.id.heard_tv_menu)
    TextView heardTvMenu;
    @BindView(R.id.rl_head)
    LinearLayout rlHead;
    @BindView(R.id.iv_logo)
    ImageView ivLogo;
    @BindView(R.id.tv_bc)
    TextView tvBc;
    @BindView(R.id.tv_share)
    TextView tvShare;

    private String url1;
    private String url2;
    private Bitmap bitmap;
    private Bitmap bitmap1;

    private DialogUtils dialogUtils;
    @Override
    protected void initContentView(Bundle savedInstanceState) {
        setContentView(R.layout.activity_qrcode_layout);
        dialogUtils = new DialogUtils(this);
    }

    @Override
    public void setUpViews() {
        heardTitle.setText("店铺二维码");
        IsBang.setImmerHeard(this,rlHead);
        url1 = DBManager.getInstance(QRcodeActivity.this).getUserInfo().suppFxUrl;
        url2 = DBManager.getInstance(QRcodeActivity.this).getUserInfo().Logo;
        Resources resources = this.getResources();
        @SuppressLint("ResourceType") InputStream  inputStream = resources.openRawResource(R.mipmap.logo);
        final BitmapDrawable bitmapDrawable = new BitmapDrawable(inputStream);


        Glide.with(this).asBitmap().load(url2).into(new SimpleTarget<Bitmap>() {
            @Override
            public void onResourceReady(Bitmap resource, Transition<? super Bitmap> transition) {

                bitmap1 = QRcodeUtils.createQRCodeWithLogo(url1, 500,
                        resource);
                ivLogo.setImageBitmap(bitmap1);
            }

            @Override
            public void onLoadFailed(@Nullable Drawable errorDrawable) {
                super.onLoadFailed(errorDrawable);
                bitmap = bitmapDrawable.getBitmap();
                bitmap1 = QRcodeUtils.createQRCodeWithLogo(url1, 500,
                        bitmap);
                ivLogo.setImageBitmap(bitmap1);
            }
        });
    }

    @Override
    public void setUpLisener() {

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // TODO: add setContentView(...) invocation
        ButterKnife.bind(this);
    }

    @OnClick({R.id.heard_back, R.id.tv_bc, R.id.tv_share})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.heard_back:
                this.finish();
                break;
            case R.id.tv_bc:
                ImgSaveUtils.saveBitmapGallery(bitmap1,"店铺二维码",this);
                break;
            case R.id.tv_share:
                dialogUtils.ShareDialog("我的店铺","我的店铺二维码",bitmap1);
                break;
        }
    }
}
