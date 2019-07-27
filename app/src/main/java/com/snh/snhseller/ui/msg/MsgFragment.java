package com.snh.snhseller.ui.msg;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.gyf.barlibrary.ImmersionBar;
import com.netease.nim.uikit.business.recent.RecentContactsFragment;
import com.netease.nim.uikit.common.activity.UI;
import com.snh.module_netapi.requestApi.BaseResultBean;
import com.snh.module_netapi.requestApi.NetSubscriber;
import com.snh.snhseller.BaseFragment;
import com.snh.snhseller.R;
import com.snh.snhseller.bean.NoticeNumBean;
import com.snh.snhseller.requestApi.RequestClient;
import com.snh.snhseller.utils.JumpUtils;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

import static com.snh.snhseller.MainActivity.tvNum;

/**
 * <p>desc：<p>
 * <p>author：DreamFly<p>
 * <p>creatTime：2019/2/20<p>
 * <p>changeTime：2019/2/20<p>
 * <p>version：1<p>
 */
public class MsgFragment extends BaseFragment {

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
    @BindView(R.id.tv_num1)
    TextView tvNum1;
    @BindView(R.id.ll_menu_1)
    LinearLayout llMenu1;
    @BindView(R.id.tv_num2)
    TextView tvNum2;
    @BindView(R.id.ll_menu_2)
    LinearLayout llMenu2;
    @BindView(R.id.tv_num3)
    TextView tvNum3;
    @BindView(R.id.ll_menu_3)
    LinearLayout llMenu3;
    @BindView(R.id.tv_num4)
    TextView tvNum4;
    @BindView(R.id.ll_menu_4)
    LinearLayout llMenu4;
    @BindView(R.id.recent_contacts_fragment)
    FrameLayout recentContactsFragment;

    private RecentContactsFragment fragment;

    @Override
    public int initContentView() {
        return R.layout.fragment_msg_layout;
    }

    @Override
    public void setUpViews(View view) {
//        IsBang.setImmerHeard(getContext(), rlHead);
//        ImmersionBar.setTitleBar(getActivity(), rlHead);
        ImmersionBar.with(getActivity()).statusBarColor(R.color.white).statusBarDarkFont(true).init();
        heardTitle.setText("消息");
        heardBack.setVisibility(View.GONE);
        addRecentContactsFragment();
//        imLoging();
    }

    // 将最近联系人列表fragment动态集成进来。
    private void addRecentContactsFragment() {
        fragment = new RecentContactsFragment();
        // 设置要集成联系人列表fragment的布局文件
        fragment.setContainerId(R.id.recent_contacts_fragment);

        final UI activity = (UI) getActivity();

        // 如果是activity从堆栈恢复，FM中已经存在恢复而来的fragment，此时会使用恢复来的，而new出来这个会被丢弃掉
        fragment = (RecentContactsFragment) activity.addFragment(fragment);
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

    Bundle bundle;

    @OnClick({R.id.ll_menu_1, R.id.ll_menu_2, R.id.ll_menu_3, R.id.ll_menu_4})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.ll_menu_1:
                bundle = new Bundle();
                bundle.putInt("userNum", userOrderNum);
                bundle.putInt("supplierNum", supplierOrderNum);
                JumpUtils.dataJump(getActivity(), OrderNoticeActivity.class, bundle, false);
                break;
            case R.id.ll_menu_2:
                bundle = new Bundle();
                bundle.putInt("applyNum", applyNum);
                bundle.putInt("systemNum", systemNum);
                JumpUtils.dataJump(getActivity(), SupplyNoticeActivity.class, bundle, false);
                break;
            case R.id.ll_menu_3:
                JumpUtils.simpJump(getActivity(), CapitalNoticeActivity.class, false);
                break;
            case R.id.ll_menu_4:
                JumpUtils.simpJump(getActivity(), SystemNoticeActivity.class, false);
                break;
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        getCount();
    }

    int sumNum1;
    int sumNum2;
    int sumNum3;
    int sumNum4;
    int userOrderNum;
    int supplierOrderNum;
    int applyNum;
    int systemNum;

    private void getCount() {
        RequestClient.GetSupplierNoticeUnreadCount(getContext(), new NetSubscriber<BaseResultBean<NoticeNumBean>>(getContext()) {
            @Override
            public void onResultNext(BaseResultBean<NoticeNumBean> model) {
                int sumNum = model.data.ApplyNRC + model.data.MoneyNoticeNRC + model.data.SystemNiticeNRC + model.data.UserOrderNRC + model.data.SupplierOrderNRC;
//                tvNum.setVisibility(View.GONE);
//                Badge badge = new QBadgeView(getContext());
//                badge.bindTarget(MainActivity.rl04);
//                badge.setBadgeGravity(Gravity.END | Gravity.TOP);
                userOrderNum = model.data.UserOrderNRC;
                supplierOrderNum = model.data.SupplierOrderNRC;
                applyNum = model.data.ApplyNRC;
                systemNum = model.data.SystemNiticeNRC;
                sumNum1 = model.data.UserOrderNRC + model.data.SupplierOrderNRC;
                sumNum2 = model.data.ApplyNRC;
                sumNum3 = model.data.MoneyNoticeNRC;
                sumNum4 = model.data.SystemNiticeNRC;
                tvNum1.setVisibility(View.VISIBLE);
                tvNum2.setVisibility(View.VISIBLE);
                tvNum3.setVisibility(View.VISIBLE);
                tvNum4.setVisibility(View.VISIBLE);
                if (sumNum > 99) {
                    tvNum.setText("99+");
//                    badge.setBadgeText("99+");
                } else if (sumNum <= 0) {
                    tvNum.setVisibility(View.INVISIBLE);
//                    badge.hide(false);
                } else {
                    tvNum.setText(sumNum + "");
                }
//                if (sumNum1 > 99) {
//                    tvNum1.setText("99+");
////                    badge.setBadgeNumber(sumNum);
//                } else
                    if (sumNum1 <= 0) {
                    tvNum1.setVisibility(View.INVISIBLE);
                } else {
                    tvNum1.setText("");
                }
//                if (sumNum2 > 99) {
//                    tvNum2.setText("99+");
//                } else

                    if (sumNum2 <= 0) {
                    tvNum2.setVisibility(View.INVISIBLE);
                } else {
                    tvNum2.setText("");
                }
//                if (sumNum3 > 99) {
//                    tvNum3.setText("99+");
//                } else
                    if (sumNum3 <= 0) {
                    tvNum3.setVisibility(View.INVISIBLE);
                } else {
                    tvNum3.setText("");
                }
//                if (sumNum4 > 99) {
//                    tvNum4.setText("99+");
//                } else
                    if (sumNum4 <= 0) {
                    tvNum4.setVisibility(View.INVISIBLE);
                } else {
                    tvNum4.setText("");
                }
//                badge.setOnDragStateChangedListener(new Badge.OnDragStateChangedListener() {
//                    @Override
//                    public void onDragStateChanged(int dragState, Badge badge, View targetView) {
//                        if (dragState == STATE_SUCCEED) {
//                            badge.hide(true);
//                        }
//                    }
//                });
            }
        });
    }

    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
        if(!hidden){
            ImmersionBar.with(getActivity()).statusBarColor(R.color.white).statusBarDarkFont(true).init();
        }
    }
}
