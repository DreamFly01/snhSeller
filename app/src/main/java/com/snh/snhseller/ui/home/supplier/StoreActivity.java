package com.snh.snhseller.ui.home.supplier;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnLoadMoreListener;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;
import com.snh.snhseller.BaseActivity;
import com.snh.snhseller.R;
import com.snh.snhseller.adapter.GoodsAdapter;
import com.snh.snhseller.bean.BaseResultBean;
import com.snh.snhseller.bean.supplierbean.GoodsBean;
import com.snh.snhseller.requestApi.NetSubscriber;
import com.snh.snhseller.requestApi.RequestClient;
import com.snh.snhseller.utils.ImageUtils;
import com.snh.snhseller.utils.JumpUtils;
import com.snh.snhseller.wediget.RecycleViewDivider;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * <p>desc：店铺页面<p>
 * <p>author：DreamFly<p>
 * <p>creatTime：2019/3/7<p>
 * <p>changeTime：2019/3/7<p>
 * <p>version：1<p>
 */
public class StoreActivity extends BaseActivity {
    @BindView(R.id.heard_back)
    LinearLayout heardBack;
    @BindView(R.id.heard_title)
    TextView heardTitle;
    @BindView(R.id.heard_menu)
    ImageView heardMenu;
    @BindView(R.id.heard_tv_menu)
    TextView heardTvMenu;
    @BindView(R.id.rl_menu)
    RelativeLayout rlMenu;
    @BindView(R.id.rl_head)
    LinearLayout rlHead;
    @BindView(R.id.iv_logo)
    ImageView ivLogo;
    @BindView(R.id.tv_name)
    TextView tvName;
    @BindView(R.id.tv_phone)
    TextView tvPhone;
    @BindView(R.id.tv_introduce)
    TextView tvIntroduce;
    @BindView(R.id.recyclerView)
    RecyclerView recyclerView;
    @BindView(R.id.refreshLayout)
    SmartRefreshLayout refreshLayout;
    @BindView(R.id.tv_add)
    TextView tvAdd;

    private int index = 1;
    private int id;
    private int type = 0;
    private String condition = "";
    private String logoUrl;
    private String name;
    private String phone;
    private Bundle bundle;
    private boolean isShow = true;
    private GoodsAdapter adapter;
    private List<GoodsBean> datas = new ArrayList<>();
    private int from = 0;
    private boolean isApply;

    @Override
    protected void initContentView(Bundle savedInstanceState) {
        setContentView(R.layout.activity_store_layout);
        bundle = getIntent().getExtras();
        if (null != bundle) {
            logoUrl = bundle.getString("url");
            name = bundle.getString("name");
            phone = bundle.getString("phone");
            type = bundle.getInt("type");
            id = bundle.getInt("id");
            from = bundle.getInt("from");
            isApply = bundle.getBoolean("isApply");
        }
    }

    @Override
    public void setUpViews() {
        heardTitle.setText("");
        tvName.setText(name);
        tvPhone.setText(phone);
        ImageUtils.loadUrlImage(this, logoUrl, ivLogo);
        setRecyclerView();
        getData();
        if (type == 0) {
            if(isApply){
                tvAdd.setText("等待验证");
                tvAdd.setEnabled(false);
            }
            tvAdd.setVisibility(View.VISIBLE);
        } else {
            tvAdd.setVisibility(View.GONE);
        }
    }

    @Override
    public void setUpLisener() {
        refreshLayout.setOnLoadMoreListener(new OnLoadMoreListener() {
            @Override
            public void onLoadMore(@NonNull RefreshLayout refreshLayout) {
                index += 1;
                getData();
            }
        });
        refreshLayout.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(@NonNull RefreshLayout refreshLayout) {
                index = 1;
                isShow = true;
                adapter.setNewData(null);
                getData();
            }
        });
        adapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {

                if (from != 2&&type == 1) {
                    bundle = new Bundle();
                    bundle.putInt("id", datas.get(position).CommodityId);
                    bundle.putInt("shopId", id);
                    JumpUtils.dataJump(StoreActivity.this, ProductActivity.class, bundle, false);
                }
            }
        });
    }


    private void setRecyclerView() {
        adapter = new GoodsAdapter(R.layout.item_goods_layout, null);
        adapter.setFrom(from,type);
        recyclerView.addItemDecoration(new RecycleViewDivider(this, LinearLayout.VERTICAL, R.drawable.line));
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // TODO: add setContentView(...) invocation
        ButterKnife.bind(this);
    }

    @OnClick({R.id.heard_back, R.id.tv_add})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.heard_back:
                this.finish();
                break;
            case R.id.tv_add:
                apply();
                break;
        }
    }

    private void getData() {
        addSubscription(RequestClient.GetGoodsList(id, index, type, condition, this, new NetSubscriber<BaseResultBean<List<GoodsBean>>>(this, isShow) {
            @Override
            public void onResultNext(BaseResultBean<List<GoodsBean>> model) {
                isShow = false;
                refreshLayout.finishLoadMore();
                refreshLayout.finishRefresh();
                if (model.data.size() > 0) {
                    if (index == 1) {
                        datas = model.data;
                        adapter.setNewData(model.data);
                    } else {
                        adapter.addData(model.data);
                        datas.addAll(model.data);
                    }
                } else {
                    adapter.setEmptyView(R.layout.empty_layout, recyclerView);
                }
            }
        }));
    }

    private void apply() {
        addSubscription(RequestClient.ApplyFor(id, from, this, new NetSubscriber<BaseResultBean>(this, true) {
            @Override
            public void onResultNext(BaseResultBean model) {

            }
        }));
    }
}
