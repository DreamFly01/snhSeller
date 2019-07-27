package com.snh.snhseller.ui.merchantEntry;

import android.os.Bundle;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.just.agentweb.AgentWeb;
import com.snh.module_netapi.requestApi.BaseResultBean;
import com.snh.module_netapi.requestApi.NetSubscriber;
import com.snh.snhseller.BaseActivity;
import com.snh.snhseller.R;
import com.snh.snhseller.bean.AgreementBean;
import com.snh.snhseller.requestApi.RequestClient;
import com.snh.snhseller.utils.IsBang;
import com.snh.snhseller.wediget.CoolIndicatorLayout;

import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * <p>desc：<p>
 * <p>author：DreamFly<p>
 * <p>creatTime：2019/2/20<p>
 * <p>changeTime：2019/2/20<p>
 * <p>version：1<p>
 */
public class ProtocolActivity extends BaseActivity {
    @BindView(R.id.heard_back)
    LinearLayout heardBack;
    @BindView(R.id.heard_title)
    TextView heardTitle;
    @BindView(R.id.heard_menu)
    ImageView heardMenu;
    @BindView(R.id.rl_head)
    LinearLayout rlHead;
    @BindView(R.id.layout_web)
    LinearLayout layoutWeb;

    private Bundle bundle;

    private int flag;

    private AgentWeb agentWeb;
    private String url;

    @Override
    protected void initContentView(Bundle savedInstanceState) {
        setContentView(R.layout.activity_protocol_layout);
        bundle = getIntent().getExtras();
        if (bundle != null) {
            flag = bundle.getInt("flag");
        }
    }

    @Override
    public void setUpViews() {
        IsBang.setImmerHeard(this, rlHead);

    }

    @Override
    public void setUpLisener() {

    }

    @OnClick(R.id.heard_back)
    public void onClick() {
        this.finish();
    }


    @Override
    public void getDataOnCreate() {
        super.getDataOnCreate();
        addSubscription(RequestClient.GetAgreementList(this, new NetSubscriber<BaseResultBean<List<AgreementBean>>>(this, false) {
            @Override
            public void onResultNext(BaseResultBean<List<AgreementBean>> model) {

                for (int i = 0; i < model.data.size(); i++) {
                    if (model.data.get(i).AgreementType == flag) {
                        url = model.data.get(i).AgreementContent;
                    }
                }
                switch (flag) {
                    case 1:
                        heardTitle.setText("软件使用许可协议");
                        break;
                    case 2:
                        heardTitle.setText("算你狠平台服务协议");
                        break;
                    case 3:
                        heardTitle.setText("法律申明和隐私权政策");
                        break;
                    case 4:
                        heardTitle.setText("企业入驻协议");
                        break;

                }
                fillWebView();
            }
        }));
    }


    private void fillWebView() {
        CoolIndicatorLayout coolIndicatorLayout = new CoolIndicatorLayout(this);
        agentWeb = AgentWeb.with(this)
                .setAgentWebParent((LinearLayout) layoutWeb, new LinearLayout.LayoutParams(-1, -1))
                .setCustomIndicator(coolIndicatorLayout)
                .createAgentWeb()
                .ready()
                .go(url);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

    }
}
