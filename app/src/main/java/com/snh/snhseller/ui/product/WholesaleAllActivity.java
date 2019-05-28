package com.snh.snhseller.ui.product;

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
import com.snh.snhseller.BaseActivity;
import com.snh.snhseller.R;
import com.snh.snhseller.adapter.BusinessAdapter;
import com.snh.snhseller.bean.BaseResultBean;
import com.snh.snhseller.bean.BusinessBean;
import com.snh.snhseller.requestApi.NetSubscriber;
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
 * <p>desc：<p>
 * <p>author：DreamFly<p>
 * <p>creatTime：2019/5/23<p>
 * <p>changeTime：2019/5/23<p>
 * <p>version：1<p>
 */
public class WholesaleAllActivity extends BaseActivity {


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
    private BusinessAdapter adapter;
    private int index = 1;
    private String condition = "";
    private int type = 2;
    private boolean isShow = true;
    private List<BusinessBean> datas = new ArrayList<>();
    public boolean isFrist = true;

    @Override
    protected void initContentView(Bundle savedInstanceState) {
        setContentView(R.layout.activity_wholesaleall_layout);
    }

    @Override
    public void setUpViews() {
        heardTitle.setText("商品搜索");
        IsBang.setImmerHeard(this,rlHead,"#ffffff");
        setRecyclerView();
    }

    @Override
    public void setUpLisener() {
        refreshLayout.setOnLoadMoreListener(new OnLoadMoreListener() {
            @Override
            public void onLoadMore(@NonNull RefreshLayout refreshLayout) {
                index += 1;
                isShow = false;
                getData();
            }
        });
        adapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                isFrist = false;
                JumpUtils.simpJump(WholesaleAllActivity.this, AddSkuActivity.class, false);
            }
        });
        etSearch.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                    KeyBoardUtils.hintKeyBoard(WholesaleAllActivity.this);
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
    }

    private void setRecyclerView() {
        adapter = new BusinessAdapter(R.layout.item_wholesalesall_layout, null);
        recyclerView.addItemDecoration(new RecycleViewDivider(WholesaleAllActivity.this, LinearLayoutManager.VERTICAL, R.drawable.line));
        recyclerView.setLayoutManager(new LinearLayoutManager(WholesaleAllActivity.this));
        recyclerView.setAdapter(adapter);
    }

    private void getData() {
        RequestClient.MyCommodityList(index, condition, type, WholesaleAllActivity.this, new NetSubscriber<BaseResultBean<List<BusinessBean>>>(WholesaleAllActivity.this, isShow) {
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
                        adapter.setNewData(datas);
                    } else {
                        refreshLayout.finishLoadMoreWithNoMoreData();
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

    @OnClick(R.id.heard_back)
    public void onClick() {
        this.finish();
    }
}
