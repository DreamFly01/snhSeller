package com.snh.moudle_coupons.ui.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bigkoo.pickerview.builder.TimePickerBuilder;
import com.bigkoo.pickerview.listener.OnTimeSelectListener;
import com.bigkoo.pickerview.view.TimePickerView;
import com.snh.library_base.BaseFragment;
import com.snh.library_base.utils.DialogUtils;
import com.snh.library_base.utils.StrUtils;
import com.snh.library_base.utils.StringUtils;
import com.snh.library_base.utils.TimeUtils;
import com.snh.module_netapi.requestApi.BaseResultBean;
import com.snh.module_netapi.requestApi.NetSubscriber;
import com.snh.moudle_coupons.R;
import com.snh.moudle_coupons.R2;
import com.snh.moudle_coupons.adapter.AppointProductAdapter;
import com.snh.moudle_coupons.bean.CouponsProductIdBean;
import com.snh.moudle_coupons.bean.MsgEventBean;
import com.snh.moudle_coupons.netapi.RequestClient;
import com.snh.moudle_coupons.ui.activity.ProductActivity;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

/**
 * <p>desc：<p>
 * <p>author：DreamFly<p>
 * <p>creatTime：2019/5/31<p>
 * <p>changeTime：2019/5/31<p>
 * <p>version：1<p>
 */
public class CreatSupplierCouponsFragment extends BaseFragment {
    @BindView(R2.id.coupons_et_name)
    EditText couponsEtName;
    @BindView(R2.id.coupons_ll_product)
    LinearLayout couponsLlProduct;
    @BindView(R2.id.coupons_tv_money)
    EditText couponsEtMoney;
    @BindView(R2.id.coupons_et_condition)
    EditText couponsEtCondition;
    @BindView(R2.id.coupons_et_num)
    EditText couponsEtNum;
    @BindView(R2.id.coupons_tv_startTime)
    TextView couponsTvStartTime;
    @BindView(R2.id.coupons_tv_endTime)
    TextView couponsTvEndTime;
    @BindView(R2.id.coupons_tv_overdue)
    TextView couponsTvOverdue;
    @BindView(R2.id.coupons_iv_isOpen)
    ImageView couponsIvIsOpen;
    @BindView(R2.id.coupons_rcl)
    RecyclerView recyclerView;
    Unbinder unbinder;

    private int isDisplay = 1;
    private Map<String, Object> map = new TreeMap<>();
    private DialogUtils dialogUtils;
    private List<CouponsProductIdBean> idBeanList = new ArrayList<>();
    private AppointProductAdapter adapter;
    private List<Integer> idList = new ArrayList<>();

    @Override
    public int initContentView() {
        EventBus.getDefault().register(this);

        return R.layout.coupons_fragment_creatsupplier_layout;
    }

    @Override
    public void setUpViews(View view) {
        dialogUtils = new DialogUtils(getContext());
        setRecyclerView();

    }

    private void setRecyclerView() {
        adapter = new AppointProductAdapter(R.layout.coupons_item_apponitproduct_layout, null);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        layoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(adapter);
    }

    @Override
    public void setUpLisener() {

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // TODO: inflate a fragment view
        View rootView = super.onCreateView(inflater, container, savedInstanceState);
        unbinder = ButterKnife.bind(this, rootView);
        return rootView;
    }

    private boolean check() {
        if (StrUtils.isEmpty(couponsEtName.getText().toString().trim())) {
            dialogUtils.noBtnDialog("请输入名称");
            return false;
        }
        if (StrUtils.isEmpty(couponsEtMoney.getText().toString().trim())) {
            dialogUtils.noBtnDialog("请输入优惠金额");
            return false;
        }
        if (StrUtils.isEmpty(couponsEtCondition.getText().toString().trim())) {
            dialogUtils.noBtnDialog("请输入使用条件");
            return false;
        }
        if (StrUtils.isEmpty(couponsEtNum.getText().toString().trim())) {
            dialogUtils.noBtnDialog("请输入发行总数");
            return false;
        }
        if (idBeanList.size() <= 0) {
            dialogUtils.noBtnDialog("请选择商品");
            return false;
        }
        if(StrUtils.isEmpty(couponsTvStartTime.getText().toString().trim())){
            dialogUtils.noBtnDialog("请选择优惠劵开始日期");
            return false;
        }
        if(StrUtils.isEmpty(couponsTvEndTime.getText().toString().trim())){
            dialogUtils.noBtnDialog("请选择优惠劵结束日期");
            return false;
        }
        return true;
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onPostData(MsgEventBean bean) {
        if ("2".equals(bean.getMsg())) {
            if (check()) {
                postData();
            }
        }
    }

    private void setData() {
        map.put("CouponName", couponsEtName.getText().toString().trim());
        map.put("ConditionValue", couponsEtCondition.getText().toString().trim());//优惠劵满足条件不输入传0
        map.put("CouponValue", couponsEtMoney.getText().toString().trim());
        map.put("CouponWay", 2);
        map.put("MaxReceiveNum", 0);
        map.put("BeginDate", couponsTvStartTime.getText().toString().trim() + ":00:00");
        map.put("EndDate", couponsTvEndTime.getText().toString().trim() + ":00:00");
        if (!StrUtils.isEmpty(couponsTvOverdue.getText().toString().trim())) {
            map.put("ExpiredPromptTime", couponsTvOverdue.getText().toString().trim() + ":00:00");
        }
        map.put("TotalNum", couponsEtNum.getText().toString().trim());
        map.put("IsDisplay", isDisplay);
        map.put("IsPutaway", 1);
        for (int i = 0; i < idBeanList.size(); i++) {
            idList.add(idBeanList.get(i).GoodsId);
        }
        map.put("GoodsIds", StringUtils.join(idList, ","));
    }

    private void postData() {
        setData();
        addSubscription(RequestClient.AddCoupons(map, getContext(), new NetSubscriber<BaseResultBean>(getContext(), true) {
            @Override
            public void onResultNext(BaseResultBean model) {
                EventBus.getDefault().post(new MsgEventBean("updataTitle"));
                dialogUtils.simpleDialog("创建成功", new DialogUtils.ConfirmClickLisener() {
                    @Override
                    public void onConfirmClick(View v) {
                        dialogUtils.dismissDialog();
                        getActivity().finish();
                    }
                }, false);
            }
        }));
    }

    private void showPickView(final int type) {
        Calendar startDate = Calendar.getInstance();
        Calendar endDate = Calendar.getInstance();
        Calendar currentDate = Calendar.getInstance();
        currentDate.set(TimeUtils.getYear(), TimeUtils.getMonth(), TimeUtils.getDay());
        if (type == 1) {
            startDate.set(TimeUtils.getYear(), TimeUtils.getMonth(), TimeUtils.getDay());
            endDate.set(TimeUtils.getYear() + 99, 11, 1);
        } else if (type == 2) {
            startDate.set(TimeUtils.getYear(couponsTvStartTime.getText().toString().trim()), TimeUtils.getMonth(couponsTvStartTime.getText().toString().trim()), TimeUtils.getDay(couponsTvStartTime.getText().toString().trim()));
            endDate.set(TimeUtils.getYear(couponsTvStartTime.getText().toString().trim()) + 99, 11, 1);
        } else if (type == 3) {
            startDate.set(TimeUtils.getYear(couponsTvStartTime.getText().toString().trim()), TimeUtils.getMonth(couponsTvStartTime.getText().toString().trim()), TimeUtils.getDay(couponsTvStartTime.getText().toString().trim()));
            endDate.set(TimeUtils.getYear(couponsTvEndTime.getText().toString().trim()), TimeUtils.getMonth(couponsTvEndTime.getText().toString().trim()), TimeUtils.getDay(couponsTvEndTime.getText().toString().trim()));

        }
        //startDate.set(2013,1,1);
        //endDate.set(2020,1,1);
        //正确设置方式 原因：注意事项有说明
        TimePickerView pvTime = new TimePickerBuilder(getActivity(), new OnTimeSelectListener() {
            @Override
            public void onTimeSelect(Date date, View v) {
//                tvTime.setText(TimeUtils.getDataString(date));
                if (type == 1) {
                    couponsTvStartTime.setText(TimeUtils.getDataString(date, "yyyy-MM-dd HH"));
                } else if (type == 2) {
                    couponsTvEndTime.setText(TimeUtils.getDataString(date, "yyyy-MM-dd HH"));
                } else if (type == 3) {
                    couponsTvOverdue.setText(TimeUtils.getDataString(date, "yyyy-MM-dd HH"));
                }
            }
        })
                .setType(new boolean[]{true, true, true, true, false, false})
                .setRangDate(startDate, endDate)
                .setDecorView((ViewGroup) getActivity().getWindow().getDecorView().findViewById(android.R.id.content)).build();
        pvTime.setDate(currentDate);
        pvTime.show();
    }

    @OnClick({R2.id.coupons_iv_isOpen, R2.id.coupons_tv_startTime, R2.id.coupons_tv_endTime, R2.id.coupons_tv_overdue, R2.id.coupons_ll_product})
    public void onClick(View view) {
        int id = view.getId();
        if (id == R.id.coupons_iv_isOpen) {
            if (isDisplay == 1) {
                isDisplay = 0;
                couponsIvIsOpen.setBackgroundResource(R.drawable.coupons_close_bg);
            } else {
                isDisplay = 1;
                couponsIvIsOpen.setBackgroundResource(R.drawable.coupons_open_bg);
            }
        } else if (id == R.id.coupons_tv_startTime) {
            showPickView(1);
        } else if (id == R.id.coupons_tv_endTime) {
            if (StrUtils.isEmpty(couponsTvStartTime.getText().toString())) {
                dialogUtils.noBtnDialog("请先选择开始时间");
            } else {
                showPickView(2);
            }
        } else if (id == R.id.coupons_tv_overdue) {
            if (StrUtils.isEmpty(couponsTvStartTime.getText().toString()) || StrUtils.isEmpty(couponsTvEndTime.getText().toString())) {
                dialogUtils.noBtnDialog("请选择结束时间");
            } else {
                showPickView(3);
            }

        } else if (id == R.id.coupons_ll_product) {
            Intent intent = new Intent(getContext(), ProductActivity.class);
            intent.putParcelableArrayListExtra("idList", (ArrayList<? extends Parcelable>) idBeanList);
            startActivityForResult(intent, 100);
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 100 && requestCode == 100) {
            if (null != data) {
                idBeanList.clear();
                idBeanList = data.getExtras().getParcelableArrayList("idList");
                adapter.setNewData(idBeanList);
            }
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }
}
