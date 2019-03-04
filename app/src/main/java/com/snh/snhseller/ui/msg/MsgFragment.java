package com.snh.snhseller.ui.msg;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.gyf.barlibrary.ImmersionBar;
import com.netease.nim.uikit.business.recent.RecentContactsFragment;
import com.netease.nim.uikit.common.activity.UI;
import com.netease.nim.uikit.common.util.log.LogUtil;
import com.netease.nim.uikit.impl.NimUIKitImpl;
import com.netease.nimlib.sdk.NIMClient;
import com.netease.nimlib.sdk.RequestCallback;
import com.netease.nimlib.sdk.auth.AuthService;
import com.netease.nimlib.sdk.auth.LoginInfo;
import com.snh.snhseller.BaseFragment;
import com.snh.snhseller.R;
import com.snh.snhseller.utils.DBManager;
import com.snh.snhseller.utils.IsBang;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * <p>desc：<p>
 * <p>author：DreamFly<p>
 * <p>creatTime：2019/2/20<p>
 * <p>changeTime：2019/2/20<p>
 * <p>version：1<p>
 */
public class MsgFragment extends BaseFragment {

    Unbinder unbinder;
    @BindView(R.id.heard_title)
    TextView heardTitle;
    @BindView(R.id.heard_menu)
    ImageView heardMenu;
    @BindView(R.id.heard_tv_menu)
    TextView heardTvMenu;
    @BindView(R.id.rl_head)
    LinearLayout rlHead;
    private RecentContactsFragment fragment;
    @Override
    public int initContentView() {
        return R.layout.fragment_msg_layout;
    }

    @Override
    public void setUpViews(View view) {
        ImmersionBar.setTitleBar(getActivity(),rlHead);
        IsBang.setImmerHeard(getContext(), rlHead);
        heardTitle.setText("消息");
        addRecentContactsFragment();
        imLoging();
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

    private void imLoging(){
        LoginInfo info = new LoginInfo(DBManager.getInstance(getContext()).getUserInfo().Accid,DBManager.getInstance(getContext()).getUserInfo().Token); // config...
        RequestCallback<LoginInfo> callback = new RequestCallback<LoginInfo>() {
            @Override
            public void onSuccess(LoginInfo param) {
                LogUtil.audio("success");
                NimUIKitImpl.setAccount(param.getAccount());
            }

            @Override
            public void onFailed(int code) {
                LogUtil.audio("failed");
            }

            @Override
            public void onException(Throwable exception) {
                LogUtil.audio("esception"+exception.getMessage());
            }
        };
        NIMClient.getService(AuthService.class).login(info)
                .setCallback(callback);
    }

}
