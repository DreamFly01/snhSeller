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
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;
import com.snh.module_netapi.requestApi.BaseResultBean;
import com.snh.module_netapi.requestApi.NetSubscriber;
import com.snh.snhseller.BaseActivity;
import com.snh.snhseller.R;
import com.snh.snhseller.adapter.SkuListAdapter;
import com.snh.snhseller.bean.SkuBean;
import com.snh.snhseller.requestApi.RequestClient;
import com.snh.snhseller.utils.DialogUtils;
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
 * <p>creatTime：2019/3/12<p>
 * <p>changeTime：2019/3/12<p>
 * <p>version：1<p>
 */
public class SkuListActivity extends BaseActivity {
    @BindView(R.id.recyclerView)
    RecyclerView recyclerView;
    @BindView(R.id.refreshLayout)
    SmartRefreshLayout refreshLayout;
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
    @BindView(R.id.btn_commit)
    Button btnCommit;

    private boolean isShow = true;
    private Bundle bundle;
    private int goodsId;
    private SkuListAdapter adapter;
    private List<SkuBean> datas = new ArrayList<>();

    private DialogUtils dialogUtils;
    @Override
    protected void initContentView(Bundle savedInstanceState) {
        setContentView(R.layout.activity_skulist_layout);
        bundle = getIntent().getExtras();
        if (null != bundle) {
            goodsId = bundle.getInt("goodsId");
        }
    }

    @Override
    public void setUpViews() {
        dialogUtils = new DialogUtils(this);
        IsBang.setImmerHeard(this, rlHead);
        heardTitle.setText("");
        setRecyclerView();
        getData();
    }

    @Override
    public void setUpLisener() {
        refreshLayout.setEnableLoadMore(false);
        refreshLayout.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(@NonNull RefreshLayout refreshLayout) {
                getData();
            }
        });
        adapter.setOnItemChildClickListener(new BaseQuickAdapter.OnItemChildClickListener() {
            @Override
            public void onItemChildClick(final BaseQuickAdapter adapter, View view, final int position) {
                switch (view.getId()) {
                    case R.id.ll_delete:
                        dialogUtils.simpleDialog("确认删除此规格？", new DialogUtils.ConfirmClickLisener() {
                            @Override
                            public void onConfirmClick(View v) {

                                del(datas.get(position).NormId);
                                datas.remove(position);
                                adapter.setNewData(datas);
                                dialogUtils.dismissDialog();
                                if (datas.size() <= 0) {
                                    SkuListActivity.this.finish();
                                }
                            }
                        },true);

                        break;
                    case R.id.ll_item:
                        bundle = new Bundle();
                        bundle.putParcelable("data", datas.get(position));
                        bundle.putInt("goodsId", goodsId);
                       JumpUtils.dataJump(SkuListActivity.this,AddSkuActivity.class,bundle,false);
                        break;
                }
            }
        });
    }

    private void setRecyclerView() {
        adapter = new SkuListAdapter(R.layout.item_skulist_layout, null);
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

    private void getData() {
        addSubscription(RequestClient.GetNormList(goodsId, this, new NetSubscriber<BaseResultBean<List<SkuBean>>>(this, isShow) {
            @Override
            public void onResultNext(BaseResultBean<List<SkuBean>> model) {
                refreshLayout.finishRefresh();
                if (model.data.size() > 0) {
                    datas = model.data;
                    adapter.setNewData(model.data);
                }
            }
        }));
    }

    @OnClick({R.id.heard_back, R.id.btn_commit})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.heard_back:
                this.finish();
                break;
            case R.id.btn_commit:
                bundle = new Bundle();
                bundle.putInt("goodsId", goodsId);
                JumpUtils.dataJump(SkuListActivity.this, AddSkuActivity.class, bundle, false);
                break;
        }
    }

    private void del(int normId) {
        addSubscription(RequestClient.DelNorm(normId, this, new NetSubscriber<BaseResultBean>(this, true) {
            @Override
            public void onResultNext(BaseResultBean model) {
                showShortToast("删除成功");
            }
        }));
    }

    @Override
    protected void onResume() {
        super.onResume();
        getData();
    }
}
