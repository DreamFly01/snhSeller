package com.snh.moudle_coupons.ui.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.bigkoo.pickerview.builder.TimePickerBuilder;
import com.bigkoo.pickerview.listener.OnTimeSelectListener;
import com.bigkoo.pickerview.view.TimePickerView;
import com.snh.library_base.BaseFragment;
import com.snh.library_base.utils.DialogUtils;
import com.snh.library_base.utils.StrUtils;
import com.snh.library_base.utils.TimeUtils;
import com.snh.module_netapi.requestApi.BaseResultBean;
import com.snh.module_netapi.requestApi.NetSubscriber;
import com.snh.moudle_coupons.R;
import com.snh.moudle_coupons.R2;
import com.snh.moudle_coupons.bean.MsgEventBean;
import com.snh.moudle_coupons.netapi.RequestClient;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.Calendar;
import java.util.Date;
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
public class CreatMyCouponsFragment extends BaseFragment {
    @BindView(R2.id.coupons_et_money)
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
    @BindView(R2.id.coupons_etnum1)
    EditText couponsEtnum1;
    @BindView(R2.id.coupons_iv_isOpen)
    ImageView couponsIvIsOpen;
    @BindView(R2.id.coupons_et_name)
    EditText couponsEtName;
    Unbinder unbinder;

    private int isDisplay = 1;
    private Map<String, Object> map = new TreeMap<>();
    private DialogUtils dialogUtils;

    @Override
    public int initContentView() {
        EventBus.getDefault().register(this);
        return R.layout.coupons_fragment_creatmy_layout;
    }

    @Override
    public void setUpViews(View view) {
        dialogUtils = new DialogUtils(getContext());
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

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    @OnClick({R2.id.coupons_iv_isOpen, R2.id.coupons_tv_startTime, R2.id.coupons_tv_endTime, R2.id.coupons_tv_overdue})
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

        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onPostData(MsgEventBean bean) {
        if ("1".equals(bean.getMsg())) {
            if (check()) {
                postData();
            }
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
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
        if (StrUtils.isEmpty(couponsEtnum1.getText().toString().trim())) {
            dialogUtils.noBtnDialog("请输入限领张数");
            return false;
        }
        if (Integer.parseInt(couponsEtnum1.getText().toString().trim()) > Integer.parseInt(couponsEtNum.getText().toString().trim())) {
            dialogUtils.noBtnDialog("限领张数不能大于总发行数");
            return false;
        }

        if (Double.parseDouble(couponsEtMoney.getText().toString().trim()) >= Double.parseDouble(couponsEtCondition.getText().toString().trim())) {
            dialogUtils.noBtnDialog("优惠金额不能大于等于满减金额");
            return false;
        }
        if (StrUtils.isEmpty(couponsTvStartTime.getText().toString().trim())) {
            dialogUtils.noBtnDialog("请选择优惠劵开始日期");
            return false;
        }
        if (StrUtils.isEmpty(couponsTvEndTime.getText().toString().trim())) {
            dialogUtils.noBtnDialog("请选择优惠劵结束日期");
            return false;
        }
        return true;
    }

    private void setData() {
        map.put("CouponName", couponsEtName.getText().toString().trim());
        map.put("ConditionValue", couponsEtCondition.getText().toString().trim());
        map.put("CouponValue", couponsEtMoney.getText().toString().trim());
        map.put("CouponWay", 1);
        map.put("BeginDate", couponsTvStartTime.getText().toString().trim() + ":00:00");
        map.put("EndDate", couponsTvEndTime.getText().toString().trim() + ":00:00");
        if (!StrUtils.isEmpty(couponsTvOverdue.getText().toString().trim())) {
            map.put("ExpiredPromptTime", couponsTvOverdue.getText().toString().trim() + ":00:00");
        }
        map.put("TotalNum", couponsEtNum.getText().toString().trim());
        map.put("MaxReceiveNum", couponsEtnum1.getText().toString().trim());
        map.put("IsDisplay", isDisplay);
        map.put("IsPutaway", 1);
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

}
