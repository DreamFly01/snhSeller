package com.snh.snhseller.ui.salesmanManagement.home.declaration;

import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnLoadMoreListener;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;
import com.snh.snhseller.R;
import com.snh.snhseller.bean.BaseResultBean;
import com.snh.snhseller.bean.salebean.ApplyBean;
import com.snh.snhseller.requestApi.NetSubscriber;
import com.snh.snhseller.requestApi.RequestClient;
import com.snh.snhseller.ui.salesmanManagement.BaseActivity;
import com.snh.snhseller.ui.salesmanManagement.adapter.DeclarationAdapter;
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
 * <p>creatTime：2019/3/5<p>
 * <p>changeTime：2019/3/5<p>
 * <p>version：1<p>
 */
public class DeclarationListActivity extends BaseActivity {
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
    @BindView(R.id.recyclerView)
    RecyclerView recyclerView;
    @BindView(R.id.refreshLayout)
    SmartRefreshLayout refreshLayout;

    private List<ApplyBean> datas = new ArrayList<>();
    private int index = 1;
    private DeclarationAdapter adapter;
    private Bundle bundle;
    @Override
    protected void initContentView(Bundle savedInstanceState) {
        setContentView(R.layout.activity_declarationlist_layout);
    }

    @Override
    public void setUpViews() {
        IsBang.setImmerHeard(this,rlHead,"#2E8AFF");
        heardTitle.setText("申请记录");
        setRecyclerView();
    }

    private void setRecyclerView(){
        adapter = new DeclarationAdapter(R.layout.item_delaration_layout,null);
        RecycleViewDivider recycleViewDivider = new RecycleViewDivider(this, LinearLayout.VERTICAL, 10, Color.parseColor("#f3f3f7"));
        recyclerView.addItemDecoration(recycleViewDivider);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);
    }
    @Override
    public void setUpLisener() {
        refreshLayout.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(@NonNull RefreshLayout refreshLayout) {
                index = 1;
                getData();
            }
        });
        refreshLayout.setOnLoadMoreListener(new OnLoadMoreListener() {
            @Override
            public void onLoadMore(@NonNull RefreshLayout refreshLayout) {
                    index+=1;
                    getData();
            }
        });
        adapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                bundle = new Bundle();
                bundle.putInt("id",datas.get(position).CostId);
                JumpUtils.dataJump(DeclarationListActivity.this,DeclarationDetailsActivity.class,bundle,false);

            }
        });
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // TODO: add setContentView(...) invocation
        ButterKnife.bind(this);
    }

    @OnClick(R.id.heard_back)
    public void onClick() {
        this.finish();
    }

    private void getData(){
        addSubscription(RequestClient.GetApplyList(index, this, new NetSubscriber<BaseResultBean<List<ApplyBean>>>(this) {
            @Override
            public void onResultNext(BaseResultBean<List<ApplyBean>> model) {

                if(index == 1){
                    refreshLayout.finishRefresh();
                    datas.clear();
                    if(model.data.size()>0){
                        datas.addAll(model.data);
                        adapter.setNewData(model.data);
                    }else {
                        adapter.setNewData(null);
                        adapter.setEmptyView(R.layout.empty1_layout,recyclerView);
                    }
                }else{
                    datas.addAll(model.data);
                    refreshLayout.finishLoadMore();
                    if(model.data.size()>0){
                        adapter.addData(model.data);
                    }
                }
            }
        }));
    }

    @Override
    protected void onResume() {
        super.onResume();
        getData();
    }
}
