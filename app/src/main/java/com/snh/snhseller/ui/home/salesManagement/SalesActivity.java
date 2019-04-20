package com.snh.snhseller.ui.home.salesManagement;

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
import com.snh.snhseller.adapter.SalesAdapter;
import com.snh.snhseller.bean.BaseResultBean;
import com.snh.snhseller.bean.salesBean.SalesmanBean;
import com.snh.snhseller.requestApi.NetSubscriber;
import com.snh.snhseller.requestApi.RequestClient;
import com.snh.snhseller.ui.home.supplier.MySupplierActivity;
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
 * <p>desc：<p>
 * <p>author：DreamFly<p>
 * <p>creatTime：2019/3/12<p>
 * <p>changeTime：2019/3/12<p>
 * <p>version：1<p>
 */
public class SalesActivity extends BaseActivity {
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
    @BindView(R.id.btn_commit)
    Button btnCommit;


    private int index = 1;
    private String condition = "";
    private boolean isShow = true;
    private List<SalesmanBean> datas = new ArrayList<>();
    private SalesAdapter adapter;
    private DialogUtils dialogUtils;
    private int type;

    private Bundle bundle;
    private boolean isFrist = true;
    @Override
    protected void initContentView(Bundle savedInstanceState) {
        setContentView(R.layout.activity_sales_layout);
    }

    @Override
    public void setUpViews() {
        IsBang.setImmerHeard(this, rlHead);
        dialogUtils = new DialogUtils(this);
        heardTitle.setText("业务员");
        setRecyclerView();
        getData();
    }

    @Override
    public void setUpLisener() {
        etSearch.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                    KeyBoardUtils.hintKeyBoard(SalesActivity.this);
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
        refreshLayout.setEnableRefresh(false);
        adapter.setOnItemChildClickListener(new BaseQuickAdapter.OnItemChildClickListener() {
            @Override
            public void onItemChildClick(BaseQuickAdapter adapter, View view, final int position) {
                switch (view.getId()) {
                    case R.id.btnFreeze:
                        String content;
                        if (datas.get(position).States) {
                            content = "是否冻结该业务员";
                            type = 2;
                        } else {
                            content = "是否解冻该业务员";
                            type = 1;
                        }
                        dialogUtils.twoBtnDialog(content, new DialogUtils.ChoseClickLisener() {
                            @Override
                            public void onConfirmClick(View v) {
                                local(datas.get(position).SalesmanId, type);
                                dialogUtils.dismissDialog();
                            }

                            @Override
                            public void onCancelClick(View v) {
                                dialogUtils.dismissDialog();
                            }
                        }, false);
                        break;
                    case R.id.btnDelete:
                        dialogUtils.twoBtnDialog("确认删除该业务员吗？", new DialogUtils.ChoseClickLisener() {
                            @Override
                            public void onConfirmClick(View v) {
                                del(datas.get(position).SalesmanId);
                                dialogUtils.dismissDialog();
                            }

                            @Override
                            public void onCancelClick(View v) {
                                dialogUtils.dismissDialog();
                            }
                        }, false);
                        break;
                    case R.id.ll_item:
                        bundle = new Bundle();
                        bundle.putInt("type",2);
                        bundle.putParcelable("data",datas.get(position));
                        JumpUtils.dataJump(SalesActivity.this,EditSalesActivity.class,bundle,false);
                        break;
                }
            }
        });
    }

    private void setRecyclerView() {
        adapter = new SalesAdapter(R.layout.item_sales_layout, null);
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

    @OnClick({R.id.heard_back, R.id.btn_commit})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.heard_back:
                this.finish();
                break;
            case R.id.btn_commit:
                isFrist = false;
                bundle = new Bundle();
                bundle.putInt("type",1);
                JumpUtils.dataJump(this,EditSalesActivity.class,bundle,false);
                break;
        }
    }

    private void getData() {
        addSubscription(RequestClient.SalesmanList(index, condition, this, new NetSubscriber<BaseResultBean<List<SalesmanBean>>>(this, isShow) {
            @Override
            public void onResultNext(BaseResultBean<List<SalesmanBean>> model) {
                isShow = false;
                isFrist = false;
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

    private void del(int id) {
        addSubscription(RequestClient.DelSalesman(id, this, new NetSubscriber<BaseResultBean>(this, true) {
            @Override
            public void onResultNext(BaseResultBean model) {
                index = 1;
                getData();
            }
        }));
    }

    private void local(int id, int type) {
        addSubscription(RequestClient.LockSalesman(id, type, this, new NetSubscriber<BaseResultBean>(this, true) {
            @Override
            public void onResultNext(BaseResultBean model) {
                index = 1;
                getData();
            }
        }));
    }

    @Override
    protected void onResume() {
        super.onResume();
        if(!isFrist){
            getData();
        }
    }
}
