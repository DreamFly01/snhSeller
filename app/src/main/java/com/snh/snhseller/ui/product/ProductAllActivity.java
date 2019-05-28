package com.snh.snhseller.ui.product;

import android.os.Bundle;
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
import android.widget.Toast;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.snh.snhseller.BaseActivity;
import com.snh.snhseller.R;
import com.snh.snhseller.adapter.ProdcutAdapter;
import com.snh.snhseller.bean.BaseResultBean;
import com.snh.snhseller.bean.ProductBean;
import com.snh.snhseller.requestApi.NetSubscriber;
import com.snh.snhseller.requestApi.RequestClient;
import com.snh.snhseller.ui.merchantEntry.PerfectMyLocalActivity;
import com.snh.snhseller.utils.Contans;
import com.snh.snhseller.utils.DialogUtils;
import com.snh.snhseller.utils.IsBang;
import com.snh.snhseller.utils.JumpUtils;
import com.snh.snhseller.utils.KeyBoardUtils;
import com.snh.snhseller.utils.SPUtils;
import com.snh.snhseller.utils.StrUtils;
import com.snh.snhseller.wediget.RecycleViewDivider;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

/**
 * <p>desc：<p>
 * <p>author：DreamFly<p>
 * <p>creatTime：2019/5/21<p>
 * <p>changeTime：2019/5/21<p>
 * <p>version：1<p>
 */
public class ProductAllActivity extends BaseActivity {
    @BindView(R.id.recyclerView)
    RecyclerView recyclerView;
    Unbinder unbinder;
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
    private int type = 1;
    private int index = 1;
    private ProdcutAdapter adapter;
    private DialogUtils dialogUtils;
    private List<ProductBean> datas = new ArrayList<>();
    private boolean isShow = true;
    private String condition = "";

    @Override
    protected void initContentView(Bundle savedInstanceState) {
        setContentView(R.layout.activity_productall_layout);
    }

    @Override
    public void setUpViews() {
        dialogUtils = new DialogUtils(this);
        heardTitle.setText("商品搜索");
        IsBang.setImmerHeard(this,rlHead,"#ffffff");
        setRecyclerView();
    }

    @Override
    public void setUpLisener() {
        etSearch.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                    KeyBoardUtils.hintKeyBoard(ProductAllActivity.this);
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
        adapter = new ProdcutAdapter(R.layout.item_productall_content, null);
        adapter.setType(2);
        recyclerView.addItemDecoration(new RecycleViewDivider(this, LinearLayoutManager.VERTICAL, R.drawable.line));
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);
        adapter.setOnLoadMoreListener(new BaseQuickAdapter.RequestLoadMoreListener() {
            @Override
            public void onLoadMoreRequested() {
                recyclerView.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        index += 1;
                        isShow = false;
                        getData();
                    }
                }, 1000);
            }
        }, recyclerView);
        adapter.setOnItemChildClickListener(new BaseQuickAdapter.OnItemChildClickListener() {
            @Override
            public void onItemChildClick(BaseQuickAdapter adapter, View view, final int position) {
                switch (view.getId()) {

//                    //下架商品
//                    case R.id.ll_01:
//                        dialogUtils.twoBtnDialog("是否确定下架商品", new DialogUtils.ChoseClickLisener() {
//                            @Override
//                            public void onConfirmClick(View v) {
//                                UpOrDownProduct(datas.get(position).CommTenantId, 2, "下架成功");
//                            }
//
//                            @Override
//                            public void onCancelClick(View v) {
//                                dialogUtils.dismissDialog();
//                            }
//                        }, false);
//                        break;
                    //编辑商品
//                    case R.id.ll_item:
//                        if (datas.get(position).IsAuditing == 2) {
//                            Bundle bundle = new Bundle();
//                            bundle.putParcelable("data", datas.get(position));
//                            bundle.putInt("type", 2);
//                            JumpUtils.dataJump(ProductAllActivity.this, EditProduct1Activity.class, bundle, false);
//                        } else if (type == 2) {
//                            Bundle bundle = new Bundle();
//                            bundle.putParcelable("data", datas.get(position));
//                            bundle.putInt("type", 2);
//                            JumpUtils.dataJump(ProductAllActivity.this, EditProduct1Activity.class, bundle, false);
//                        } else if (datas.get(position).IsAuditing == 0) {
////                            dialogUtils.noBtnDialog("商品待审核，不可编辑");
//                        }
//                        break;
//                    case R.id.ll_02:
//                        dialogUtils.twoBtnDialog("是否确定删除商品", new DialogUtils.ChoseClickLisener() {
//                            @Override
//                            public void onConfirmClick(View v) {
//                                delProduct(datas.get(position).CommTenantId, "删除商品成功");
//                                getData();
//                            }
//
//                            @Override
//                            public void onCancelClick(View v) {
//                                dialogUtils.dismissDialog();
//                            }
//                        }, false);
//                        break;
                    //删除/编辑商品
                    case R.id.tv_edit:
//                        if (datas.get(position).Status==1) {
                            Bundle bundle = new Bundle();
                            bundle.putInt("type", 2);
                            bundle.putInt("isDel",datas.get(position).Status);
                            bundle.putString("CommTenantIcon",datas.get(position).CommTenantIcon);
                            bundle.putString("CommTenantName",datas.get(position).CommTenantName);
                            bundle.putString("CategoryName",datas.get(position).CategoryName);
                            bundle.putString("UnitsTitle",datas.get(position).UnitsTitle);
                            bundle.putInt("Inventory", (int) datas.get(position).Inventory);
                            bundle.putDouble("Price",datas.get(position).Price);
                            bundle.putDouble("MarketPrice",datas.get(position).MarketPrice);
                            bundle.putInt("CommTenantId",datas.get(position).CommTenantId);
                            JumpUtils.dataJump(ProductAllActivity.this, EditProductActivity.class, bundle, false);
//                        }else if(datas.get(position).Status == 2) {
//                        dialogUtils.twoBtnDialog("是否确定删除商品", new DialogUtils.ChoseClickLisener() {
//                            @Override
//                            public void onConfirmClick(View v) {
//                                delProduct(datas.get(position).CommTenantId, "删除商品成功",position);
//                            }
//
//                            @Override
//                            public void onCancelClick(View v) {
//                                dialogUtils.dismissDialog();
//                            }
//                        }, false);
//                        }

                        break;
                    //上架/下架商品
                    case R.id.tv_xj:
                        if(datas.get(position).Status == 1){
                        dialogUtils.twoBtnDialog("是否确定下架商品", new DialogUtils.ChoseClickLisener() {
                            @Override
                            public void onConfirmClick(View v) {
                                UpOrDownProduct(datas.get(position).CommTenantId, 2, "下架成功",position);
                            }

                            @Override
                            public void onCancelClick(View v) {
                                dialogUtils.dismissDialog();
                            }
                        }, false);
                        } else if (datas.get(position).Status==2) {
                        if ("1".equals(SPUtils.getInstance(ProductAllActivity.this).getString(Contans.IS_FULL))) {
                            dialogUtils.twoBtnDialog("是否确定上架商品", new DialogUtils.ChoseClickLisener() {
                                @Override
                                public void onConfirmClick(View v) {
                                    UpOrDownProduct(datas.get(position).CommTenantId, 1, "上架成功",position);
                                }

                                @Override
                                public void onCancelClick(View v) {
                                    dialogUtils.dismissDialog();
                                }
                            }, false);
                        } else if ("0".equals(SPUtils.getInstance(ProductAllActivity.this).getString(Contans.IS_FULL))) {
                            dialogUtils.twoBtnDialog("是否马上完善店铺信息", new DialogUtils.ChoseClickLisener() {
                                @Override
                                public void onConfirmClick(View v) {
                                    dialogUtils.dismissDialog();
                                    JumpUtils.simpJump(ProductAllActivity.this, PerfectMyLocalActivity.class, false);
                                }

                                @Override
                                public void onCancelClick(View v) {
                                    dialogUtils.dismissDialog();
                                }
                            }, true);
                        }
                        }

                        break;
                }
            }
        });
    }

    private void getData() {
        addSubscription(RequestClient.GetSaleOfGoods(-1, index, condition, this, new NetSubscriber<BaseResultBean<List<ProductBean>>>(this, isShow) {
            @Override
            public void onResultNext(BaseResultBean<List<ProductBean>> model) {
//                SPUtils.getInstance(this).savaBoolean(Contans.PRODUCT_IS_FRESH,false).commit();
                for (int i = 0; i < model.data.size(); i++) {
                    model.data.get(i).state = type;
                }
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
                        adapter.loadMoreComplete();
                    } else {
                        adapter.loadMoreEnd();
                    }
                }
            }
        }));
    }

    private void UpOrDownProduct(int commtenanId, final int type, final String content,final int position) {
        index = 1;
        addSubscription(RequestClient.UpOrDownProduct(commtenanId, type, this, new NetSubscriber<BaseResultBean>(this, true) {
            @Override
            public void onResultNext(BaseResultBean model) {
                dialogUtils.dismissDialog();
                Toast.makeText(ProductAllActivity.this, content, Toast.LENGTH_SHORT).show();
               if(type == 1){
                  //上架
                   datas.get(position).Status = 1;
                   adapter.setNewData(datas);
               }else if(type == 2){
                   //下架
                   datas.get(position).Status = 2;
                   adapter.setNewData(datas);
               }
            }
        }));
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
