package com.snh.snhseller.ui.home.supplier;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnLoadMoreListener;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;
import com.snh.module_netapi.requestApi.BaseResultBean;
import com.snh.module_netapi.requestApi.NetSubscriber;
import com.snh.snhseller.BaseActivity;
import com.snh.snhseller.R;
import com.snh.snhseller.adapter.AllSupplierAdapter;
import com.snh.snhseller.bean.supplierbean.AllSupplierBean;
import com.snh.snhseller.requestApi.RequestClient;
import com.snh.snhseller.utils.IsBang;
import com.snh.snhseller.utils.JumpUtils;
import com.snh.snhseller.utils.KeyBoardUtils;
import com.snh.snhseller.utils.StrUtils;
import com.snh.snhseller.wediget.RecycleViewDivider;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * <p>desc：所有商户列表<p>
 * <p>author：DreamFly<p>
 * <p>creatTime：2019/3/8<p>
 * <p>changeTime：2019/3/8<p>
 * <p>version：1<p>
 */
public class AllSupplierActiviy extends BaseActivity {
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
    @BindView(R.id.et_search)
    EditText etSearch;
    @BindView(R.id.recyclerView)
    RecyclerView recyclerView;
    @BindView(R.id.refreshLayout)
    SmartRefreshLayout refreshLayout;

    private Bundle bundle;
    private int from;
    private boolean isShow = true;
    private int index = 1;
    private String condition = "";
    private AllSupplierAdapter adapter;
    private List<AllSupplierBean> datas = new ArrayList<>();

    private boolean isFrist = true;

    @Override
    protected void initContentView(Bundle savedInstanceState) {
        setContentView(R.layout.activity_allsupplier_layout);
        bundle = getIntent().getExtras();
        if (null != bundle) {
            from = bundle.getInt("from");
        }
    }

    @Override
    public void setUpViews() {
        IsBang.setImmerHeard(this,rlHead);
        if (from == 1) {
            heardTitle.setText("添加供应商");
        }
        if (from == 2) {
            heardTitle.setText("添加商户");
        }
        setRecyclerView();

    }

    private void setRecyclerView() {
        adapter = new AllSupplierAdapter(R.layout.item_allsupplier_layout, null);
        recyclerView.addItemDecoration(new RecycleViewDivider(this, LinearLayout.VERTICAL, R.drawable.line));
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);
    }

    @Override
    public void setUpLisener() {
        etSearch.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                    KeyBoardUtils.hintKeyBoard(AllSupplierActiviy.this);
                    isShow = true;
                    index = 1;
                    if (!StrUtils.isEmpty(etSearch.getText().toString())) {
                        condition = etSearch.getText().toString().trim();
                    } else {
                        condition = "";
                    }
                    adapter.setNewData(null);
                    getData();
                    return true;
                }
                return false;
            }
        });
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
                adapter.setNewData(null);
                getData();
            }
        });

        adapter.setOnItemChildClickListener(new BaseQuickAdapter.OnItemChildClickListener() {
            @Override
            public void onItemChildClick(BaseQuickAdapter adapter, View view, int position) {
                switch (view.getId()) {
                    case R.id.ll_item:
                        isFrist = false;
                        bundle = new Bundle();
                        bundle.putInt("id", datas.get(position).SupplierId);
                        bundle.putString("name", datas.get(position).SupplierName);
                        bundle.putString("url", datas.get(position).SupplierLogo);
                        bundle.putString("phone", datas.get(position).SupplierPhone);
                        bundle.putInt("type", 0);//证明关系的type 0 没关系，1 有关系
                        bundle.putInt("from", from);//从那个入口进来的type 1 供应商入口 2商户入口
                        bundle.putBoolean("isApply", datas.get(position).IsApply);
                        JumpUtils.dataJump(AllSupplierActiviy.this, StoreActivity.class, bundle, false);
                        break;
                }
            }
        });
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // TODO: add setContentView(...) invocation
        ButterKnife.bind(this);
    }

    private void getData() {
        addSubscription(RequestClient.SeekCommTenant(index, condition, from, this, new NetSubscriber<BaseResultBean<List<AllSupplierBean>>>(this, isShow) {
            @Override
            public void onResultNext(BaseResultBean<List<AllSupplierBean>> model) {
                isShow = false;
                refreshLayout.finishLoadMore();
                refreshLayout.finishRefresh();
                if (model.data.size() > 0) {
                    if (index == 1) {
                        datas = model.data;
                        adapter.setNewData(model.data);
                    } else {
                        datas.addAll(model.data);
                        adapter.setNewData(datas);
                    }
                } else {
                    if (index == 1) {
                        adapter.setNewData(null);
                        adapter.setEmptyView(R.layout.empty_layout, recyclerView);
                    }else {
                        refreshLayout.setNoMoreData(true);
                    }
                }
            }
        }));
    }

    @OnClick(R.id.heard_back)
    public void onClick() {
        this.finish();
    }

    @Override
    protected void onResume() {
        super.onResume();
            getData();
    }
}
