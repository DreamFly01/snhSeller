package com.snh.snhseller.ui.home.supplier;

import android.content.Context;
import android.graphics.Paint;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.snh.snhseller.BaseActivity;
import com.snh.snhseller.R;
import com.snh.snhseller.adapter.ProductSkuAdapter;
import com.snh.snhseller.bean.BaseResultBean;
import com.snh.snhseller.bean.supplierbean.ProductBean;
import com.snh.snhseller.bean.supplierbean.SkuBean;
import com.snh.snhseller.requestApi.NetSubscriber;
import com.snh.snhseller.requestApi.RequestClient;
import com.snh.snhseller.utils.GlideImageLoader;
import com.snh.snhseller.utils.JumpUtils;
import com.snh.snhseller.wediget.RecycleViewDivider;
import com.youth.banner.Banner;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * <p>desc：商品详情<p>
 * <p>author：DreamFly<p>
 * <p>creatTime：2019/3/7<p>
 * <p>changeTime：2019/3/7<p>
 * <p>version：1<p>
 */
public class ProductActivity extends BaseActivity {
    @BindView(R.id.recyclerView)
    RecyclerView recyclerView;
    @BindView(R.id.refreshLayout)
    SmartRefreshLayout refreshLayout;
    @BindView(R.id.ll_back)
    LinearLayout llBack;
    @BindView(R.id.btn_commit)
    Button btnCommit;


    private TextView tvPrice;

    private TextView tvPrice1;

    private TextView tvName;

    private TextView tvYunfei;

    private TextView tvInventory;

    private Banner banner;

    private LinearLayout heard;
    private LayoutInflater inflater = null;
    private ProductSkuAdapter adapter;
    private int id;
    private int index = 1;
    private boolean isShow = true;
    private Bundle bundle;
    private List<String> bannerUrls = new ArrayList<>();
    private int shopId;
    @Override
    protected void initContentView(Bundle savedInstanceState) {
        setContentView(R.layout.activity_product_layout);
        inflater = (LayoutInflater) this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        bundle = getIntent().getExtras();
        if (null != bundle) {
            id = bundle.getInt("id");
            shopId = bundle.getInt("shopId");
        }
        setImm(false);
    }

    @Override
    public void setUpViews() {
        heard = (LinearLayout) inflater.inflate(R.layout.heard_banner_layout, null);
        banner = (Banner) heard.findViewById(R.id.banner);
        tvPrice = (TextView) heard.findViewById(R.id.tv_price);
        tvPrice1 = (TextView) heard.findViewById(R.id.tv_price1);
        tvName = (TextView) heard.findViewById(R.id.tv_name);
        tvInventory = (TextView) heard.findViewById(R.id.tv_inventory);
        tvYunfei = (TextView) heard.findViewById(R.id.tv_yunfei);
        setRecyclerView();
        getData();
    }

    @Override
    public void setUpLisener() {
        refreshLayout.setEnableRefresh(false);
        refreshLayout.setEnableLoadMore(false);
    }

    private void setRecyclerView() {
        adapter = new ProductSkuAdapter(R.layout.item_sku_layout, null);
        adapter.addHeaderView(heard);
        recyclerView.addItemDecoration(new RecycleViewDivider(this, LinearLayout.VERTICAL, R.drawable.line));
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);
    }

    private void fillView(ProductBean bean) {
        tvName.setText(bean.ShopGoodsName);
        tvInventory.setText("总库存：" + bean.SumInventory);
        tvPrice.setText("批发价：￥" + bean.Price);
        tvPrice1.setText("零售价：￥" + bean.RetailPrice);
        tvPrice1.getPaint().setFlags(Paint.STRIKE_THRU_TEXT_FLAG);
        tvYunfei.setText("运费：送货上门");
        banner.setImageLoader(new GlideImageLoader());
        for (int i = 0; i < bean.CarouselImgUrl.length; i++) {
            bannerUrls.add(bean.CarouselImgUrl[i]);
        }
        banner.setImages(bannerUrls);
        banner.start();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // TODO: add setContentView(...) invocation
        ButterKnife.bind(this);
    }

    private List<SkuBean> skuBeans = new ArrayList<>();
    private List<SkuBean> skudatas = new ArrayList<>();
    private ProductBean bean ;
    private void getData() {
        addSubscription(RequestClient.GetGoodsDetail(id, this, new NetSubscriber<BaseResultBean<ProductBean>>(this, isShow) {
            @Override
            public void onResultNext(BaseResultBean<ProductBean> model) {
                fillView(model.data);
                bean = model.data;
                if (model.data.ShopNormsList.size() > 0) {
                    skuBeans = model.data.ShopNormsList;
                    adapter.setNewData(skuBeans);
                } else {
                    adapter.setEmptyView(R.layout.empty_layout, recyclerView);
                }
            }
        }));
    }


    @OnClick({R.id.ll_back, R.id.btn_commit})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.ll_back:

                break;
            case R.id.btn_commit:
                skudatas.clear();
                for (SkuBean bean : skuBeans)
                {
                    if(bean.total>0){
                        skudatas.add(bean);
                    }
                }
                if (skudatas.size()>0) {
                bundle = new Bundle();
                bundle.putParcelableArrayList("data", (ArrayList<? extends Parcelable>) skudatas);
                bundle.putString("url",bean.ShopIconUrl);
                bundle.putString("name",bean.ShopGoodsName);
                bundle.putInt("shopId",shopId);
                bundle.putInt("goodsId",bean.ShopGoodsId);
                bundle.putInt("shopId",shopId);
                JumpUtils.dataJump(this,CommitOrderActivity.class,bundle,false);
                }else {
                    showShortToast("请选择商品规格！");
                }
                break;
        }
    }
}
