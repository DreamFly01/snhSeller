package com.snh.snhseller.ui.product;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
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
import com.snh.snhseller.adapter.BusinessAdapter;
import com.snh.snhseller.bean.BaseResultBean;
import com.snh.snhseller.bean.BusinessBean;
import com.snh.snhseller.requestApi.NetSubscriber;
import com.snh.snhseller.requestApi.RequestClient;
import com.snh.snhseller.utils.IsBang;
import com.snh.snhseller.utils.JumpUtils;
import com.snh.snhseller.wediget.RecycleViewDivider;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * <p>desc：<p>
 * <p>author：DreamFly<p>
 * <p>creatTime：2019/3/16<p>
 * <p>changeTime：2019/3/16<p>
 * <p>version：1<p>
 */
public class BusinessProduct2Activity extends BaseActivity {
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
    @BindView(R.id.recyclerView)
    RecyclerView recyclerView;
    @BindView(R.id.refreshLayout)
    SmartRefreshLayout refreshLayout;
    @BindView(R.id.btn_commit)
    Button btnCommit;


    private BusinessAdapter adapter;
    private int index = 1;
    private String condition = "";
    private int type =2;
    private boolean isShow = true;
    private List<BusinessBean> datas = new ArrayList<>();
    public boolean isFrist = true;

    @Override
    protected void initContentView(Bundle savedInstanceState) {
        setContentView(R.layout.fragment_business_layout);
    }

    @Override
    public void setUpViews() {
        heardTitle.setText("添加批发");
        btnCommit.setText("添加批发");
        btnCommit.setVisibility(View.GONE);
        IsBang.setImmerHeard(this,rlHead);
        setRecyclerView();
        getData();
    }

    @Override
    public void setUpLisener() {
        if (type == 0) {
            refreshLayout.setOnLoadMoreListener(new OnLoadMoreListener() {
                @Override
                public void onLoadMore(@NonNull RefreshLayout refreshLayout) {
                    index += 1;
                    isShow = false;
                    getData();
                }
            });
        } else {
            refreshLayout.setEnableLoadMore(false);
        }
        refreshLayout.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(@NonNull RefreshLayout refreshLayout) {
                index = 1;
                isShow = true;
                getData();
            }
        });
        adapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                isFrist = false;
                Bundle bundle = new Bundle();
                bundle.putInt("goodsId", datas.get(position).ShopgoodsId);
                if (datas.get(position).IsSetNorm) {
                    JumpUtils.dataJump(BusinessProduct2Activity.this, SkuListActivity.class, bundle, false);
                } else {
                    JumpUtils.dataJump(BusinessProduct2Activity.this, AddSkuActivity.class, bundle, false);
                }
            }
        });
    }

    private void setRecyclerView() {
        adapter = new BusinessAdapter(R.layout.item_business_layout, null);
        recyclerView.addItemDecoration(new RecycleViewDivider(BusinessProduct2Activity.this, LinearLayoutManager.VERTICAL, R.drawable.line));
        recyclerView.setLayoutManager(new LinearLayoutManager(BusinessProduct2Activity.this));
        recyclerView.setAdapter(adapter);
    }

    private void getData() {
        RequestClient.MyCommodityList(index, condition, type, BusinessProduct2Activity.this, new NetSubscriber<BaseResultBean<List<BusinessBean>>>(BusinessProduct2Activity.this, isShow) {
            @Override
            public void onResultNext(BaseResultBean<List<BusinessBean>> model) {
                if (index == 1) {
                    if (model.data.size() > 0) {
                        datas = model.data;
                        adapter.setNewData(model.data);
                    } else {
                        adapter.setNewData(null);
                        adapter.setEmptyView(R.layout.empty_layout, recyclerView);
                    }
                } else {
                    if (model.data.size() > 0) {
                        datas.addAll(model.data);
//                        adapter.addData(model.data);
                        adapter.setNewData(datas);
                    }
                }
                refreshLayout.finishRefresh();
                refreshLayout.finishLoadMore();
            }
        });
    }

    @Override
    public void onResume() {
        super.onResume();
        if (!isFrist) {
            index = 1;
            getData();
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // TODO: add setContentView(...) invocation
        ButterKnife.bind(this);
    }

    @OnClick({R.id.heard_back, R.id.btn_commit})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.heard_back:
                this.finish();
                break;
            case R.id.btn_commit:
                break;
        }
    }
}
