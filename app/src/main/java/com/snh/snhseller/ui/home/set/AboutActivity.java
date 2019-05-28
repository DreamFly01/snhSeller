package com.snh.snhseller.ui.home.set;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.snh.snhseller.BaseActivity;
import com.snh.snhseller.R;
import com.snh.snhseller.utils.IsBang;
import com.snh.snhseller.utils.JumpUtils;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * <p>desc：<p>
 * <p>author：DreamFly<p>
 * <p>creatTime：2019/1/2<p>
 * <p>changeTime：2019/1/2<p>
 * <p>version：1<p>
 */
public class AboutActivity extends BaseActivity {
    @BindView(R.id.heard_back)
    LinearLayout heardBack;
    @BindView(R.id.heard_title)
    TextView heardTitle;
    @BindView(R.id.heard_menu)
    ImageView heardMenu;
    @BindView(R.id.rl_head)
    LinearLayout rlHead;
    @BindView(R.id.tv_version)
    TextView tvVersion;
    @BindView(R.id.ll_01)
    LinearLayout ll01;
    @BindView(R.id.ll_02)
    LinearLayout ll02;
    @BindView(R.id.ll_03)
    LinearLayout ll03;
    @BindView(R.id.heard_tv_menu)
    TextView heardTvMenu;
    @BindView(R.id.rl_menu)
    RelativeLayout rlMenu;
    @BindView(R.id.ll_04)
    LinearLayout ll04;
    @BindView(R.id.ll_05)
    LinearLayout ll05;
    @BindView(R.id.ll_06)
    LinearLayout ll06;
    @BindView(R.id.ll_07)
    LinearLayout ll07;
    @BindView(R.id.ll_08)
    LinearLayout ll08;

    @Override
    protected void initContentView(Bundle savedInstanceState) {
        setContentView(R.layout.activity_about_layout);
    }

    @Override
    public void setUpViews() {
        IsBang.setImmerHeard(this, rlHead);

        heardTitle.setText("关于我们");
        tvVersion.setText("算你狠 Android " + getLocalVersion(this));
    }

    @Override
    public void setUpLisener() {

    }


    private Bundle bundle;

    @OnClick({R.id.ll_01, R.id.ll_02, R.id.ll_03, R.id.heard_back,R.id.ll_04, R.id.ll_05, R.id.ll_06, R.id.ll_07, R.id.ll_08})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.heard_back:
                this.finish();
                break;
            case R.id.ll_01:
                bundle = new Bundle();
                bundle.putInt("flag", 1);
                JumpUtils.dataJump(this, ProtocolActivity.class, bundle, false);
                break;
            case R.id.ll_02:
                bundle = new Bundle();
                bundle.putInt("flag", 2);
                JumpUtils.dataJump(this, ProtocolActivity.class, bundle, false);
                break;
            case R.id.ll_03:
                bundle = new Bundle();
                bundle.putInt("flag", 3);
                JumpUtils.dataJump(this, ProtocolActivity.class, bundle, false);
                break;
            case R.id.ll_04:
                bundle = new Bundle();
                bundle.putInt("flag", 4);
                JumpUtils.dataJump(this, ProtocolActivity.class, bundle, false);
                break;
            case R.id.ll_05:
                bundle = new Bundle();
                bundle.putInt("flag", 5);
                JumpUtils.dataJump(this, ProtocolActivity.class, bundle, false);
                break;
            case R.id.ll_06:
                bundle = new Bundle();
                bundle.putInt("flag", 7);
                JumpUtils.dataJump(this, ProtocolActivity.class, bundle, false);
                break;
            case R.id.ll_07:
                bundle = new Bundle();
                bundle.putInt("flag",8);
                JumpUtils.dataJump(this, ProtocolActivity.class, bundle, false);
                break;
            case R.id.ll_08:
                bundle = new Bundle();
                bundle.putInt("flag",9);
                JumpUtils.dataJump(this, ProtocolActivity.class, bundle, false);
                break;
        }
    }

    public static String getLocalVersion(Context ctx) {
        String localVersion = "1.0.0";
        try {
            PackageInfo packageInfo = ctx.getApplicationContext()
                    .getPackageManager()
                    .getPackageInfo(ctx.getPackageName(), 0);
            localVersion = packageInfo.versionName;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return localVersion;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // TODO: add setContentView(...) invocation
        ButterKnife.bind(this);
    }

}
