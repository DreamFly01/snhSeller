package com.snh.snhseller.ui.home.supplier;

import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
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
import com.snh.snhseller.BaseActivity;
import com.snh.snhseller.R;
import com.snh.snhseller.adapter.SupplierAdapter;
import com.snh.snhseller.bean.BaseResultBean;
import com.snh.snhseller.bean.supplierbean.SupplierBean;
import com.snh.snhseller.requestApi.NetSubscriber;
import com.snh.snhseller.requestApi.RequestClient;
import com.snh.snhseller.utils.DialogUtils;
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
 * <p>desc：我的商户/供应商列表<p>
 * <p>author：DreamFly<p>
 * <p>creatTime：2019/3/7<p>
 * <p>changeTime：2019/3/7<p>
 * <p>version：1<p>
 */
public class MySupplierActivity extends BaseActivity {
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
    @BindView(R.id.et_search)
    EditText etSearch;

    private SupplierAdapter adapter;
    private boolean isShow = true;
    private List<SupplierBean> datas = new ArrayList<>();
    private int type = 1;
    private int index = 1;
    private Bundle bundle;
    private String condition = "";
    private DialogUtils dialogUtils;
    private String content;

    @Override
    protected void initContentView(Bundle savedInstanceState) {
        setContentView(R.layout.activity_mysupplier_layout);
        bundle = getIntent().getExtras();
        if (null != bundle)
            type = bundle.getInt("type");

        dialogUtils = new DialogUtils(this);
    }

    @Override
    public void setUpViews() {
        IsBang.setImmerHeard(this,rlHead);
        heardTvMenu.setText("订单");
        heardTvMenu.setTextColor(Color.WHITE);
        heardMenu.setBackgroundResource(R.drawable.order_bg);
        if (type == 1) {
            heardTitle.setText("我的供应商");
            btnCommit.setText("添加供应商");
            content = "删除供应商";
        }
        if (type == 2) {
            heardTitle.setText("我的商户");
            btnCommit.setText("添加商户");
            content = "删除商户";
        }
        setRecyclerView();
        getData();
    }

    @Override
    public void setUpLisener() {
        etSearch.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                    KeyBoardUtils.hintKeyBoard(MySupplierActivity.this);
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
                isShow = false;
                adapter.setNewData(null);
                getData();
            }
        });
        adapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                bundle = new Bundle();
                bundle.putInt("id", datas.get(position).SupplierId);
                bundle.putString("name", datas.get(position).SupplierName);
                bundle.putString("url", datas.get(position).SupplierIconurl);
                bundle.putString("phone", datas.get(position).SupplierPhone);
                bundle.putInt("type", 1);//证明关系的type 0 没关系，1 有关系
                bundle.putInt("from", type);//从那个入口进来的type 1 供应商入口 2商户入口
                JumpUtils.dataJump(MySupplierActivity.this, StoreActivity.class, bundle, false);
            }
        });
        adapter.setOnItemLongClickListener(new BaseQuickAdapter.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(BaseQuickAdapter adapter, View view, final int position) {
                dialogUtils.twoBtnDialog(content, new DialogUtils.ChoseClickLisener() {
                    @Override
                    public void onConfirmClick(View v) {
                        delete(datas.get(position).SupplierId, type);
                        dialogUtils.dismissDialog();
                    }

                    @Override
                    public void onCancelClick(View v) {
                        dialogUtils.dismissDialog();
                    }
                }, false);
                return false;
            }
        });
    }

    private void setRecyclerView() {
        adapter = new SupplierAdapter(R.layout.item_supplier_layout, null);
        recyclerView.addItemDecoration(new RecycleViewDivider(this, LinearLayoutManager.VERTICAL, R.drawable.line));
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
        addSubscription(RequestClient.MySupplier(index, type, condition, this, new NetSubscriber<BaseResultBean<List<SupplierBean>>>(this, isShow) {
            @Override
            public void onResultNext(BaseResultBean<List<SupplierBean>> model) {
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
                    }
                }

            }
        }));
    }

    @OnClick({R.id.heard_back, R.id.rl_menu, R.id.btn_commit})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.heard_back:
                this.finish();
                break;
            case R.id.rl_menu:
                bundle = new Bundle();
                bundle.putInt("type", type);
                JumpUtils.dataJump(this, OrderActivity.class, bundle, false);
                break;
            case R.id.btn_commit:
                bundle = new Bundle();
                bundle.putInt("from", type);
                JumpUtils.dataJump(this, AllSupplierActiviy.class, bundle, false);
                break;
        }
    }

    private void delete(int shopId, int from) {
        addSubscription(RequestClient.DelMySupplier(shopId, from, this, new NetSubscriber<BaseResultBean>(this, true) {
            @Override
            public void onResultNext(BaseResultBean model) {
                isShow = true;
                index = 1;
                getData();
            }
        }));
    }
}
